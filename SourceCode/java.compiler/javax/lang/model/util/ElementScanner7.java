/*
 * Copyright (c) 2010, 2022, Oracle and/or its affiliates. All rights reserved.
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

package javax.lang.model.util;

import javax.lang.model.element.*;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import static javax.lang.model.SourceVersion.*;


/**
 * A scanning visitor of program elements with default behavior
 * appropriate for the {@link SourceVersion#RELEASE_7 RELEASE_7}
 * source version.  The <code>visit<i>Xyz</i></code> methods in this
 * class scan their component elements by calling {@link
 * ElementScanner6#scan(Element, Object) scan} on their {@linkplain
 * Element#getEnclosedElements enclosed elements}, {@linkplain
 * ExecutableElement#getParameters parameters}, etc., as indicated in
 * the individual method specifications.  A subclass can control the
 * order elements are visited by overriding the
 * <code>visit<i>Xyz</i></code> methods.  Note that clients of a
 * scanner may get the desired behavior by invoking {@code v.scan(e,
 * p)} rather than {@code v.visit(e, p)} on the root objects of
 * interest.
 *
 * <p>When a subclass overrides a <code>visit<i>Xyz</i></code> method, the
 * new method can cause the enclosed elements to be scanned in the
 * default way by calling <code>super.visit<i>Xyz</i></code>.  In this
 * fashion, the concrete visitor can control the ordering of traversal
 * over the component elements with respect to the additional
 * processing; for example, consistently calling
 * <code>super.visit<i>Xyz</i></code> at the start of the overridden
 * methods will yield a preorder traversal, etc.  If the component
 * elements should be traversed in some other order, instead of
 * calling <code>super.visit<i>Xyz</i></code>, an overriding visit method
 * should call {@code scan} with the elements in the desired order.
 *
 * @apiNote
 * Methods in this class may be overridden subject to their general
 * contract.
 *
 * @param <R> the return type of this visitor's methods.  Use {@link
 *            Void} for visitors that do not need to return results.
 * @param <P> the type of the additional parameter to this visitor's
 *            methods.  Use {@code Void} for visitors that do not need an
 *            additional parameter.
 *
 * @see <a href="ElementScanner6.html#note_for_subclasses"><strong>Compatibility note for subclasses</strong></a>
 * @see ElementScanner6
 * @see ElementScanner8
 * @see ElementScanner9
 * @see ElementScanner14
 * @since 1.7
 */
@SupportedSourceVersion(RELEASE_7)
public class ElementScanner7<R, P> extends ElementScanner6<R, P> {
    /**
     * Constructor for concrete subclasses; uses {@code null} for the
     * default value.
     *
     * @deprecated Release 7 is obsolete; update to a visitor for a newer
     * release level.
     */
    @Deprecated(since="12")
    protected ElementScanner7(){
        super(null); // Superclass constructor deprecated too
    }

    /**
     * Constructor for concrete subclasses; uses the argument for the
     * default value.
     *
     * @param defaultValue the default value
     *
     * @deprecated Release 7 is obsolete; update to a visitor for a newer
     * release level.
     */
    @Deprecated(since="12")
    protected ElementScanner7(R defaultValue){
        super(defaultValue); // Superclass constructor deprecated too
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec This implementation scans the enclosed elements.
     *
     * @param e  {@inheritDoc}
     * @param p  {@inheritDoc}
     * @return the result of scanning
     */
    @Override
    public R visitVariable(VariableElement e, P p) {
        return scan(e.getEnclosedElements(), p);
    }
}
