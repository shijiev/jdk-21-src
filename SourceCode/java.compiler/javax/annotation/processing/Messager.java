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

package javax.annotation.processing;

import javax.tools.Diagnostic;
import javax.lang.model.element.*;

/**
 * A {@code Messager} provides the way for an annotation processor to
 * report error messages, warnings, and other notices.  Elements,
 * annotations, and annotation values can be passed to provide a
 * location hint for the message.  However, such location hints may be
 * unavailable or only approximate.
 *
 * <p>Printing a message with an {@linkplain
 * javax.tools.Diagnostic.Kind#ERROR error kind} will {@linkplain
 * RoundEnvironment#errorRaised raise an error}.
 *
 * @apiNote
 * The messages &quot;printed&quot; by methods in this
 * interface may or may not appear as textual output to a location
 * like {@link System#out} or {@link System#err}.  Implementations may
 * choose to present this information in a different fashion, such as
 * messages in a window.
 *
 * @see ProcessingEnvironment#getLocale
 * @since 1.6
 */
public interface Messager {
    /**
     * Prints a message of the specified kind.
     *
     * @param kind the kind of message
     * @param msg  the message, or an empty string if none
     */
    void printMessage(Diagnostic.Kind kind, CharSequence msg);

    /**
     * Prints a message of the specified kind at the location of the
     * element.
     *
     * @param kind the kind of message
     * @param msg  the message, or an empty string if none
     * @param e    the element to use as a position hint
     */
    void printMessage(Diagnostic.Kind kind, CharSequence msg, Element e);

    /**
     * Prints a message of the specified kind at the location of the
     * annotation mirror of the annotated element.
     *
     * @param kind the kind of message
     * @param msg  the message, or an empty string if none
     * @param e    the annotated element
     * @param a    the annotation to use as a position hint
     */
    void printMessage(Diagnostic.Kind kind, CharSequence msg, Element e, AnnotationMirror a);

    /**
     * Prints a message of the specified kind at the location of the
     * annotation value inside the annotation mirror of the annotated
     * element.
     *
     * @param kind the kind of message
     * @param msg  the message, or an empty string if none
     * @param e    the annotated element
     * @param a    the annotation containing the annotation value
     * @param v    the annotation value to use as a position hint
     */
    void printMessage(Diagnostic.Kind kind,
                      CharSequence msg,
                      Element e,
                      AnnotationMirror a,
                      AnnotationValue v);
    /**
     * Prints an error.
     *
     * @implSpec
     * The default implementation is equivalent to {@code
     * printMessage(Diagnostic.Kind.ERROR, msg)}.
     *
     * @param msg  the message, or an empty string if none
     * @since 18
     */
    default void printError(CharSequence msg) {
        printMessage(Diagnostic.Kind.ERROR, msg);
    }

    /**
     * Prints an error at the location of the element.
     *
     * @implSpec
     * The default implementation is equivalent to {@code
     * printMessage(Diagnostic.Kind.ERROR, msg, e)}.
     *
     * @param msg  the message, or an empty string if none
     * @param e    the element to use as a position hint
     * @since 18
     */
    default void printError(CharSequence msg, Element e) {
        printMessage(Diagnostic.Kind.ERROR, msg, e);
    }

    /**
     * Prints a warning.
     *
     * @implSpec
     * The default implementation is equivalent to {@code
     * printMessage(Diagnostic.Kind.WARNING, msg)}.
     *
     * @param msg  the message, or an empty string if none
     * @since 18
     */
    default void printWarning(CharSequence msg) {
        printMessage(Diagnostic.Kind.WARNING, msg);
    }

    /**
     * Prints a warning at the location of the element.
     *
     * @implSpec
     * The default implementation is equivalent to {@code
     * printMessage(Diagnostic.Kind.WARNING, msg, e)}.
     *
     * @param msg  the message, or an empty string if none
     * @param e    the element to use as a position hint
     * @since 18
     */
    default void printWarning(CharSequence msg, Element e) {
        printMessage(Diagnostic.Kind.WARNING, msg, e);
    }

    /**
     * Prints a note.
     *
     * @implSpec
     * The default implementation is equivalent to {@code
     * printMessage(Diagnostic.Kind.NOTE, msg)}.
     *
     * @param msg  the message, or an empty string if none
     * @since 18
     */
    default void printNote(CharSequence msg) {
        printMessage(Diagnostic.Kind.NOTE, msg);
    }

    /**
     * Prints a note at the location of the element.
     *
     * @implSpec
     * The default implementation is equivalent to {@code
     * printMessage(Diagnostic.Kind.NOTE, msg, e)}.
     *
     * @param msg  the message, or an empty string if none
     * @param e    the element to use as a position hint
     * @since 18
     */
    default void printNote(CharSequence msg, Element e) {
        printMessage(Diagnostic.Kind.NOTE, msg, e);
    }
}
