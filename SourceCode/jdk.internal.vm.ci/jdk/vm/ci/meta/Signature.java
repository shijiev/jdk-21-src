/*
 * Copyright (c) 2009, 2015, Oracle and/or its affiliates. All rights reserved.
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
package jdk.vm.ci.meta;

/**
 * Represents a method signature provided by the runtime.
 *
 * @jvms 4.3.3
 */
public interface Signature {

    /**
     * Returns the number of parameters in this signature, adding 1 for a receiver if requested.
     *
     * @param receiver true if 1 is to be added to the result for a receiver
     * @return the number of parameters; + 1 iff {@code receiver == true}
     */
    int getParameterCount(boolean receiver);

    /**
     * Gets the parameter type at the specified position.
     *
     * @param index the index into the parameters, with {@code 0} indicating the first parameter
     * @param accessingClass the context of the type lookup. If non-null, its class loader is used
     *            for resolving the type. If {@code null}, then the type returned is either
     *            unresolved or a resolved type whose resolution is context free (e.g., a primitive
     *            type or a type in a java.* package).
     * @return the {@code index}'th parameter type
     * @throws LinkageError if {@code accessingClass != null} and resolution fails
     *
     */
    JavaType getParameterType(int index, ResolvedJavaType accessingClass);

    /**
     * Gets the parameter kind at the specified position. This is the same as calling
     * {@link #getParameterType}. {@link JavaType#getJavaKind getJavaKind}.
     *
     * @param index the index into the parameters, with {@code 0} indicating the first parameter
     * @return the kind of the parameter at the specified position
     */
    default JavaKind getParameterKind(int index) {
        return getParameterType(index, null).getJavaKind();
    }

    /**
     * Gets the return type of this signature.
     *
     * @param accessingClass the context of the type lookup. If non-null, its class loader is used
     *            for resolving the type. If {@code null}, then the type returned is either
     *            unresolved or a resolved type whose resolution is context free (e.g., a primitive
     *            type or a type in a java.* package).
     * @return the return type
     * @throws LinkageError if {@code accessingClass != null} and resolution fails
     */
    JavaType getReturnType(ResolvedJavaType accessingClass);

    /**
     * Gets the return kind of this signature. This is the same as calling {@link #getReturnType}.
     * {@link JavaType#getJavaKind getJavaKind}.
     */
    default JavaKind getReturnKind() {
        return getReturnType(null).getJavaKind();
    }

    /**
     * Gets the method descriptor ({@jvms 4.3.3}) corresponding to this signature. For example:
     *
     * <pre>
     * (ILjava/lang/String;D)V
     * </pre>
     *
     * @return the signature as a string
     */
    default String toMethodDescriptor() {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < getParameterCount(false); ++i) {
            sb.append(getParameterType(i, null).getName());
        }
        sb.append(')').append(getReturnType(null).getName());
        return sb.toString();
    }

    default JavaType[] toParameterTypes(JavaType receiverType) {
        int args = getParameterCount(false);
        JavaType[] result;
        int i = 0;
        if (receiverType != null) {
            result = new JavaType[args + 1];
            result[0] = receiverType;
            i = 1;
        } else {
            result = new JavaType[args];
        }
        for (int j = 0; j < args; j++) {
            result[i + j] = getParameterType(j, null);
        }
        return result;
    }

    default JavaKind[] toParameterKinds(boolean receiver) {
        int args = getParameterCount(false);
        JavaKind[] result;
        int i = 0;
        if (receiver) {
            result = new JavaKind[args + 1];
            result[0] = JavaKind.Object;
            i = 1;
        } else {
            result = new JavaKind[args];
        }
        for (int j = 0; j < args; j++) {
            result[i + j] = getParameterKind(j);
        }
        return result;
    }
}
