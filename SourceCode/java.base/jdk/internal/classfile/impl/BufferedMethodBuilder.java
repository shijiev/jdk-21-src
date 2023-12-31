/*
 * Copyright (c) 2022, Oracle and/or its affiliates. All rights reserved.
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
 *
 *
 */
package jdk.internal.classfile.impl;

import java.lang.constant.MethodTypeDesc;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import jdk.internal.classfile.AccessFlags;

import jdk.internal.classfile.BufWriter;
import jdk.internal.classfile.ClassModel;
import jdk.internal.classfile.CodeBuilder;
import jdk.internal.classfile.CodeModel;
import jdk.internal.classfile.CodeTransform;
import jdk.internal.classfile.constantpool.ConstantPoolBuilder;
import jdk.internal.classfile.MethodBuilder;
import jdk.internal.classfile.MethodElement;
import jdk.internal.classfile.MethodModel;
import jdk.internal.classfile.constantpool.Utf8Entry;

public final class BufferedMethodBuilder
        implements TerminalMethodBuilder, MethodInfo {
    private final List<MethodElement> elements;
    private final SplitConstantPool constantPool;
    private final Utf8Entry name;
    private final Utf8Entry desc;
    private AccessFlags flags;
    private final MethodModel original;
    private int[] parameterSlots;
    MethodTypeDesc mDesc;

    public BufferedMethodBuilder(SplitConstantPool constantPool,
                                 Utf8Entry nameInfo,
                                 Utf8Entry typeInfo,
                                 MethodModel original) {
        this.elements = new ArrayList<>();
        this.constantPool = constantPool;
        this.name = nameInfo;
        this.desc = typeInfo;
        this.flags = AccessFlags.ofMethod();
        this.original = original;
    }

    @Override
    public MethodBuilder with(MethodElement element) {
        elements.add(element);
        if (element instanceof AccessFlags f) this.flags = f;
        return this;
    }

    @Override
    public ConstantPoolBuilder constantPool() {
        return constantPool;
    }

    @Override
    public Optional<MethodModel> original() {
        return Optional.ofNullable(original);
    }

    @Override
    public Utf8Entry methodName() {
        return name;
    }

    @Override
    public Utf8Entry methodType() {
        return desc;
    }

    @Override
    public MethodTypeDesc methodTypeSymbol() {
        if (mDesc == null) {
            if (original instanceof MethodInfo mi) {
                mDesc = mi.methodTypeSymbol();
            } else {
                mDesc = MethodTypeDesc.ofDescriptor(methodType().stringValue());
            }
        }
        return mDesc;
    }

    @Override
    public int methodFlags() {
        return flags.flagsMask();
    }

    @Override
    public int parameterSlot(int paramNo) {
        if (parameterSlots == null)
            parameterSlots = Util.parseParameterSlots(methodFlags(), methodTypeSymbol());
        return parameterSlots[paramNo];
    }

    @Override
    public MethodBuilder withCode(Consumer<? super CodeBuilder> handler) {
        return with(new BufferedCodeBuilder(this, constantPool, null)
                            .run(handler)
                            .toModel());
    }

    @Override
    public MethodBuilder transformCode(CodeModel code, CodeTransform transform) {
        BufferedCodeBuilder builder = new BufferedCodeBuilder(this, constantPool, code);
        builder.transform(code, transform);
        return with(builder.toModel());
    }

    @Override
    public BufferedCodeBuilder bufferedCodeBuilder(CodeModel original) {
        return new BufferedCodeBuilder(this, constantPool, original);
    }

    public BufferedMethodBuilder run(Consumer<? super MethodBuilder> handler) {
        handler.accept(this);
        return this;
    }

    public MethodModel toModel() {
        return new Model();
    }

    public final class Model
            extends AbstractUnboundModel<MethodElement>
            implements MethodModel, MethodInfo {
        public Model() {
            super(elements);
        }

        @Override
        public AccessFlags flags() {
            return flags;
        }

        @Override
        public Optional<ClassModel> parent() {
            return original().flatMap(MethodModel::parent);
        }

        @Override
        public Utf8Entry methodName() {
            return name;
        }

        @Override
        public Utf8Entry methodType() {
            return desc;
        }

        @Override
        public MethodTypeDesc methodTypeSymbol() {
            return BufferedMethodBuilder.this.methodTypeSymbol();
        }

        @Override
        public int methodFlags() {
            return flags.flagsMask();
        }

        @Override
        public int parameterSlot(int paramNo) {
            return BufferedMethodBuilder.this.parameterSlot(paramNo);
        }

        @Override
        public Optional<CodeModel> code() {
            throw new UnsupportedOperationException("nyi");
        }

        @Override
        public void writeTo(DirectClassBuilder builder) {
            builder.withMethod(methodName(), methodType(), methodFlags(), new Consumer<>() {
                @Override
                public void accept(MethodBuilder mb) {
                    forEachElement(mb);
                }
            });
        }

        @Override
        public void writeTo(BufWriter buf) {
            DirectMethodBuilder mb = new DirectMethodBuilder(constantPool, name, desc, methodFlags(), null);
            elements.forEach(mb);
            mb.writeTo(buf);
        }

        @Override
        public String toString() {
            return String.format("MethodModel[methodName=%s, methodType=%s, flags=%d]",
                    name.stringValue(), desc.stringValue(), flags.flagsMask());
        }
    }
}
