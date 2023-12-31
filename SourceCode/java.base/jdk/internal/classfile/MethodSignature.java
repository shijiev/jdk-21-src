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

import java.lang.constant.MethodTypeDesc;
import java.util.List;
import jdk.internal.classfile.impl.SignaturesImpl;
import static java.util.Objects.requireNonNull;
import jdk.internal.classfile.impl.Util;

/**
 * Models the generic signature of a method, as defined by {@jvms 4.7.9}.
 */
public sealed interface MethodSignature
        permits SignaturesImpl.MethodSignatureImpl {

    /** {@return the type parameters of this method} */
    List<Signature.TypeParam> typeParameters();

    /** {@return the signatures of the parameters of this method} */
    List<Signature> arguments();

    /** {@return the signatures of the return value of this method} */
    Signature result();

    /** {@return the signatures of the exceptions thrown by this method} */
    List<Signature.ThrowableSig> throwableSignatures();

    /** {@return the raw signature string} */
    String signatureString();

    /**
     * @return method signature for a raw (no generic information) method descriptor
     * @param methodDescriptor the method descriptor
     */
    public static MethodSignature of(MethodTypeDesc methodDescriptor) {

        requireNonNull(methodDescriptor);
        return new SignaturesImpl.MethodSignatureImpl(
                List.of(),
                List.of(),
                Signature.of(methodDescriptor.returnType()),
                Util.mappedList(methodDescriptor.parameterList(), Signature::of));
    }

    /**
     * @return method signature
     * @param result signature for the return type
     * @param arguments signatures for the method arguments
     */
    public static MethodSignature of(Signature result,
                                     Signature... arguments) {

        return new SignaturesImpl.MethodSignatureImpl(List.of(),
                                                      List.of(),
                                                      requireNonNull(result),
                                                      List.of(arguments));
    }

    /**
     * @return method signature
     * @param typeParameters signatures for the type parameters
     * @param exceptions sigantures for the exceptions
     * @param result signature for the return type
     * @param arguments signatures for the method arguments
     */
    public static MethodSignature of(List<Signature.TypeParam> typeParameters,
                                     List<Signature.ThrowableSig> exceptions,
                                     Signature result,
                                     Signature... arguments) {

        return new SignaturesImpl.MethodSignatureImpl(
                List.copyOf(requireNonNull(typeParameters)),
                List.copyOf(requireNonNull(exceptions)),
                requireNonNull(result),
                List.of(arguments));
    }

    /**
     * Parses a raw method signature string into a {@linkplain MethodSignature}
     * @param methodSignature the raw method signature string
     * @return method signature
     */
    public static MethodSignature parseFrom(String methodSignature) {

        return new SignaturesImpl().parseMethodSignature(requireNonNull(methodSignature));
    }
}
