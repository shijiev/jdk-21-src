/*
 * Copyright (c) 2011, 2023, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package jdk.vm.ci.hotspot;

import static jdk.internal.misc.Unsafe.ADDRESS_SIZE;
import static jdk.vm.ci.hotspot.CompilerToVM.compilerToVM;
import static jdk.vm.ci.hotspot.HotSpotJVMCIRuntime.runtime;
import static jdk.vm.ci.hotspot.HotSpotVMConfig.config;
import static jdk.vm.ci.hotspot.UnsafeAccess.UNSAFE;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import jdk.internal.vm.VMSupport;
import jdk.vm.ci.meta.AnnotationData;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaType;
import jdk.vm.ci.meta.ResolvedJavaType;
import jdk.vm.ci.meta.UnresolvedJavaType;

/**
 * Represents a field in a HotSpot type.
 */
class HotSpotResolvedJavaFieldImpl implements HotSpotResolvedJavaField {

    private final HotSpotResolvedObjectTypeImpl holder;
    private JavaType type;

    /**
     * Offset (in bytes) of field from start of its storage container (i.e. {@code instanceOop} or
     * {@code Klass*}).
     */
    private final int offset;

    /**
     * Value of {@code fieldDescriptor::index()}.
     */
    private final int index;

    /**
     * This value contains all flags from the class file
     */
    private final int classfileFlags;

    /**
     * This value contains VM internal flags
     */
    private final int internalFlags;

    HotSpotResolvedJavaFieldImpl(HotSpotResolvedObjectTypeImpl holder, JavaType type, int offset, int classfileFlags, int internalFlags, int index) {
        this.holder = holder;
        this.type = type;
        this.offset = offset;
        this.classfileFlags = classfileFlags;
        this.internalFlags = internalFlags;
        this.index = index;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof HotSpotResolvedJavaFieldImpl) {
            HotSpotResolvedJavaFieldImpl that = (HotSpotResolvedJavaFieldImpl) obj;
            if (that.offset != this.offset || that.isStatic() != this.isStatic()) {
                return false;
            } else if (this.holder.equals(that.holder)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return holder.hashCode() ^ offset;
    }

    @Override
    public int getModifiers() {
        return classfileFlags & HotSpotModifiers.jvmFieldModifiers();
    }

    @Override
    public boolean isInternal() {
        return (internalFlags & (1 << config().jvmFieldFlagInternalShift)) != 0;
    }

    /**
     * Determines if a given object contains this field.
     *
     * @return true iff this is a non-static field and its declaring class is assignable from
     *         {@code object}'s class
     */
    @Override
    public boolean isInObject(JavaConstant object) {
        if (isStatic()) {
            return false;
        }
        HotSpotObjectConstant constant = (HotSpotObjectConstant) object;
        return getDeclaringClass().isAssignableFrom(constant.getType());
    }

    @Override
    public HotSpotResolvedObjectTypeImpl getDeclaringClass() {
        return holder;
    }

    @Override
    public String getName() {
        return holder.getFieldInfo(index).getName(holder);
    }

    @Override
    public JavaType getType() {
        // Pull field into local variable to prevent a race causing
        // a ClassCastException below
        JavaType currentType = type;
        if (currentType instanceof UnresolvedJavaType) {
            // Don't allow unresolved types to hang around forever
            UnresolvedJavaType unresolvedType = (UnresolvedJavaType) currentType;
            JavaType resolved = HotSpotJVMCIRuntime.runtime().lookupType(unresolvedType.getName(), holder, false);
            if (resolved instanceof ResolvedJavaType) {
                type = resolved;
            }
        }
        return type;

    }

    /**
     * Gets the offset (in bytes) of field from start of its storage container (i.e.
     * {@code instanceOop} or {@code Klass*}).
     */
    @Override
    public int getOffset() {
        return offset;
    }

    /**
     * Gets the value of this field's index (i.e. {@code fieldDescriptor::index()} in the encoded
     * fields of the declaring class.
     */
    int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return format("HotSpotResolvedJavaFieldImpl<%H.%n %t:") + offset + ">";
    }

    @Override
    public boolean isSynthetic() {
        return (config().jvmAccSynthetic & classfileFlags) != 0;
    }

    /**
     * Checks if this field has the {@code Stable} annotation.
     *
     * @return true if field has {@code Stable} annotation, false otherwise
     */
    @Override
    public boolean isStable() {
        return (1 << (config().jvmFieldFlagStableShift ) & internalFlags) != 0;
    }

    private boolean hasAnnotations() {
        if (!isInternal()) {
            HotSpotVMConfig config = config();
            final long metaspaceAnnotations = UNSAFE.getAddress(holder.getKlassPointer() + config.instanceKlassAnnotationsOffset);
            if (metaspaceAnnotations != 0) {
                long fieldsAnnotations = UNSAFE.getAddress(metaspaceAnnotations + config.annotationsFieldAnnotationsOffset);
                if (fieldsAnnotations != 0) {
                    long fieldAnnotations = UNSAFE.getAddress(fieldsAnnotations + config.fieldsAnnotationsBaseOffset + (ADDRESS_SIZE * index));
                    return fieldAnnotations != 0;
                }
            }
        }
        return false;
    }

    @Override
    public Annotation[] getAnnotations() {
        if (!hasAnnotations()) {
            return new Annotation[0];
        }
        return runtime().reflection.getFieldAnnotations(this);
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        if (!hasAnnotations()) {
            return new Annotation[0];
        }
        return runtime().reflection.getFieldDeclaredAnnotations(this);
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        if (!hasAnnotations()) {
            return null;
        }
        return runtime().reflection.getFieldAnnotation(this, annotationClass);
    }

    @Override
    public JavaConstant getConstantValue() {
        return holder.getFieldInfo(index).getConstantValue(holder);
    }

    @Override
    public AnnotationData getAnnotationData(ResolvedJavaType annotationType) {
        if (!hasAnnotations()) {
            return null;
        }
        return getAnnotationData0(annotationType).get(0);
    }

    @Override
    public List<AnnotationData> getAnnotationData(ResolvedJavaType type1, ResolvedJavaType type2, ResolvedJavaType... types) {
        if (!hasAnnotations()) {
            return Collections.emptyList();
        }
        return getAnnotationData0(AnnotationDataDecoder.asArray(type1, type2, types));
    }

    private List<AnnotationData> getAnnotationData0(ResolvedJavaType... filter) {
        byte[] encoded = compilerToVM().getEncodedFieldAnnotationData(holder, index, filter);
        return VMSupport.decodeAnnotations(encoded, AnnotationDataDecoder.INSTANCE);
    }
}
