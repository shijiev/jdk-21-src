/*
 * Copyright (c) 2011, 2022, Oracle and/or its affiliates. All rights reserved.
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
 * appropriate for source versions {@link SourceVersion#RELEASE_9
 * RELEASE_9} through {@link SourceVersion#RELEASE_14 RELEASE_14}.
 *
 * The <code>visit<i>Xyz</i></code> methods in this class scan their
 * component elements by calling {@link ElementScanner6#scan(Element,
 * Object) scan} on their {@linkplain Element#getEnclosedElements
 * enclosed elements}, {@linkplain ExecutableElement#getParameters
 * parameters}, etc., as indicated in the individual method
 * specifications.  A subclass can control the order elements are
 * visited by overriding the <code>visit<i>Xyz</i></code> methods.
 * Note that clients of a scanner may get the desired behavior by
 * invoking {@code v.scan(e, p)} rather than {@code v.visit(e, p)} on
 * the root objects of interest.
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
 * @see ElementScanner7
 * @see ElementScanner8
 * @see ElementScanner14
 * @since 9
 */
@SupportedSourceVersion(RELEASE_14)
public class ElementScanner9<R, P> extends ElementScanner8<R, P> {
    /**
     * Constructor for concrete subclasses; uses {@code null} for the
     * default value.
     */
    protected ElementScanner9(){
        super(null);
    }

    /**
     * Constructor for concrete subclasses; uses the argument for the
     * default value.
     *
     * @param defaultValue the default value
     */
    protected ElementScanner9(R defaultValue){
        super(defaultValue);
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec This implementation scans the enclosed elements.
     *
     * @param e the element to visit
     * @param p a visitor-specified parameter
     * @return  the result of the scan
     */
    @Override
    public R visitModule(ModuleElement e, P p) {
        return scan(e.getEnclosedElements(), p); // TODO: Hmmm, this might not be right
    }
}
