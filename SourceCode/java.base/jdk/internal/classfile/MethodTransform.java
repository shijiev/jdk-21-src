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
package jdk.internal.classfile;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import jdk.internal.classfile.impl.TransformImpl;

/**
 * A transformation on streams of {@link MethodElement}.
 *
 * @see ClassfileTransform
 */
@FunctionalInterface
public non-sealed interface MethodTransform
        extends ClassfileTransform<MethodTransform, MethodElement, MethodBuilder> {

    /**
     * A method transform that sends all elements to the builder.
     */
    MethodTransform ACCEPT_ALL = new MethodTransform() {
        @Override
        public void accept(MethodBuilder builder, MethodElement element) {
            builder.with(element);
        }
    };

    /**
     * Create a stateful method transform from a {@link Supplier}.  The supplier
     * will be invoked for each transformation.
     *
     * @param supplier a {@link Supplier} that produces a fresh transform object
     *                 for each traversal
     * @return the stateful method transform
     */
    static MethodTransform ofStateful(Supplier<MethodTransform> supplier) {
        return new TransformImpl.SupplierMethodTransform(supplier);
    }

    /**
     * Create a method transform that passes each element through to the builder,
     * and calls the specified function when transformation is complete.
     *
     * @param finisher the function to call when transformation is complete
     * @return the method transform
     */
    static MethodTransform endHandler(Consumer<MethodBuilder> finisher) {
        return new MethodTransform() {
            @Override
            public void accept(MethodBuilder builder, MethodElement element) {
                builder.with(element);
            }

            @Override
            public void atEnd(MethodBuilder builder) {
                finisher.accept(builder);
            }
        };
    }

    /**
     * Create a method transform that passes each element through to the builder,
     * except for those that the supplied {@link Predicate} is true for.
     *
     * @param filter the predicate that determines which elements to drop
     * @return the method transform
     */
    static MethodTransform dropping(Predicate<MethodElement> filter) {
        return (b, e) -> {
            if (!filter.test(e))
                b.with(e);
        };
    }

    /**
     * Create a method transform that transforms {@link CodeModel} elements
     * with the supplied code transform.
     *
     * @param xform the method transform
     * @return the class transform
     */
    static MethodTransform transformingCode(CodeTransform xform) {
        return new TransformImpl.MethodCodeTransform(xform);
    }

    @Override
    default ResolvedTransform<MethodElement> resolve(MethodBuilder builder) {
        return new TransformImpl.ResolvedTransformImpl<>(e -> accept(builder, e),
                                                         () -> atEnd(builder),
                                                         () -> atStart(builder));
    }

    @Override
    default MethodTransform andThen(MethodTransform t) {
        return new TransformImpl.ChainedMethodTransform(this, t);
    }
}
