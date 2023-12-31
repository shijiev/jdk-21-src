/*
 * Copyright (c) 2005, 2022, Oracle and/or its affiliates. All rights reserved.
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

package javax.lang.model.element;

import jdk.internal.javac.PreviewFeature;

import javax.lang.model.util.Elements;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeKind;

/**
 * Represents a field, {@code enum} constant, method or constructor
 * parameter, local variable, resource variable, or exception
 * parameter.
 *
 * @since 1.6
 */
public interface VariableElement extends Element {
    /**
     * {@return the type of this variable}
     *
     * Note that the types of variables range over {@linkplain
     * TypeKind many kinds} of types, including primitive types,
     * declared types, and array types, among others.
     *
     * @see TypeKind
     */
    @Override
    TypeMirror asType();

    /**
     * Returns the value of this variable if this is a {@code final}
     * field initialized to a compile-time constant.  Returns {@code
     * null} otherwise.  The value will be of a primitive type or a
     * {@code String}.  If the value is of a primitive type, it is
     * wrapped in the appropriate wrapper class (such as {@link
     * Integer}).
     *
     * <p>Note that not all {@code final} fields will have
     * constant values.  In particular, {@code enum} constants are
     * <em>not</em> considered to be compile-time constants.  To have a
     * constant value, a field's type must be either a primitive type
     * or {@code String}.
     *
     * @return the value of this variable if this is a {@code final}
     * field initialized to a compile-time constant, or {@code null}
     * otherwise
     *
     * @see Elements#getConstantExpression(Object)
     * @jls 15.29 Constant Expressions
     * @jls 4.12.4 final Variables
     */
    Object getConstantValue();

    /**
     * {@return the simple name of this variable element}
     *
     * <p>For method and constructor parameters, the name of each
     * parameter must be distinct from the names of all other
     * parameters of the same executable.  If the original source
     * names are not available, an implementation may synthesize names
     * subject to the distinctness requirement above.
     *
     * <p>For variables, the name of each variable is returned, or an empty name
     * if the variable is unnamed.
     */
    @Override
    Name getSimpleName();

    /**
     * {@return the enclosing element of this variable}
     *
     * The enclosing element of a method or constructor parameter is
     * the executable declaring the parameter.
     */
    @Override
    Element getEnclosingElement();

    /**
     * {@return {@code true} if this is an unnamed variable and {@code
     * false} otherwise}
     *
     * @implSpec
     * The default implementation of this method calls {@code
     * getSimpleName()} and returns {@code true} if the result is
     * empty and {@code false} otherwise.
     *
     * @jls 6.1 Declarations
     * @jls 14.4 Local Variable Declarations
     *
     * @since 21
     */
    @PreviewFeature(feature=PreviewFeature.Feature.UNNAMED, reflective = true)
    default boolean isUnnamed() { return getSimpleName().isEmpty(); }
}
