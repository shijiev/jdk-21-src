/*
 * Copyright (c) 2006, 2022, Oracle and/or its affiliates. All rights reserved.
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

/**
 * An immutable sequence of characters.  When created by the same
 * implementation, objects implementing this interface must obey the
 * general {@linkplain Object#equals equals contract} when compared
 * with each other.  Therefore, {@code Name} objects from the same
 * implementation are usable in collections while {@code Name}s from
 * different implementations may not work properly in collections.
 *
 * <p id="empty_name">An {@linkplain CharSequence#isEmpty() empty}
 * {@code Name} has a {@linkplain CharSequence#length() length} of
 * zero.
 *
 * <p>In the context of {@linkplain
 * javax.annotation.processing.ProcessingEnvironment annotation
 * processing}, the guarantees for "the same" implementation must
 * include contexts where the {@linkplain javax.annotation.processing
 * API mediated} side effects of {@linkplain
 * javax.annotation.processing.Processor processors} could be visible
 * to each other, including successive annotation processing
 * {@linkplain javax.annotation.processing.RoundEnvironment rounds}.
 *
 * @see javax.lang.model.util.Elements#getName
 * @since 1.6
 */
public interface Name extends CharSequence {
    /**
     * Returns {@code true} if the argument represents the same
     * name as {@code this}, and {@code false} otherwise.
     *
     * <p>Note that the identity of a {@code Name} is a function both
     * of its content in terms of a sequence of characters as well as
     * the implementation which created it.
     *
     * @param obj  the object to be compared with this element
     * @return {@code true} if the specified object represents the same
     *          name as this
     * @see Element#equals
     */
    boolean equals(Object obj);

    /**
     * Obeys the general contract of {@link Object#hashCode Object.hashCode}.
     *
     * @see #equals
     */
    int hashCode();

    /**
     * Compares this name to the specified {@code CharSequence}. The result
     * is {@code true} if and only if this name represents the same sequence
     * of {@code char} values as the specified sequence.
     *
     * @return {@code true} if this name represents the same sequence
     * of {@code char} values as the specified sequence, {@code false}
     * otherwise
     *
     * @param cs The sequence to compare this name against
     * @see String#contentEquals(CharSequence)
     */
    boolean contentEquals(CharSequence cs);
}
