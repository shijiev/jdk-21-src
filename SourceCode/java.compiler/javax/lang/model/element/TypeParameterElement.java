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

import java.util.List;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

/**
 * Represents a formal type parameter of a generic class, interface, method,
 * or constructor element.
 * A type parameter declares a {@link TypeVariable}.
 *
 * @see TypeVariable
 * @since 1.6
 */
public interface TypeParameterElement extends Element {
    /**
     * {@return the {@linkplain TypeVariable type variable}
     * corresponding to this type parameter element}
     *
     * @see TypeVariable
     */
    @Override
    TypeMirror asType();

    /**
     * {@return the generic class, interface, method, or constructor that is
     * parameterized by this type parameter}
     */
    Element getGenericElement();

    /**
     * Returns the bounds of this type parameter.
     * These are the types given by the {@code extends} clause
     * used to declare this type parameter.
     * If no explicit {@code extends} clause was used,
     * then {@code java.lang.Object} is considered to be the sole bound.
     *
     * @return the bounds of this type parameter, or an empty list if
     * there are none
     */
    List<? extends TypeMirror> getBounds();

    /**
     * {@return the {@linkplain TypeParameterElement#getGenericElement
     * generic element} of this type parameter}
     */
    @Override
    Element getEnclosingElement();
}
