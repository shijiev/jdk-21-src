/*
 * Copyright (c) 2003, Oracle and/or its affiliates. All rights reserved.
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

package sun.font;

/*
 * This class is used so that a java.awt.Font does not directly
 * reference a Font2D object. This introduces occasional minor
 * de-referencing overhead but increases robustness of the
 * implementation when "bad fonts" are encountered.
 * A handle is created by a Font2D constructor and references
 * the Font2D itself. In the event that the Font2D implementation
 * determines it the font resource has errors (a bad font file)
 * it makes its handle point at another "stable" Font2D.
 * Once all referrers no longer have a reference to the Font2D it
 * may be GC'd and its resources freed.
 * This does not immediately help in the case that objects are
 * already using a bad Font2D (ie have already dereferenced the
 * handle) so there is a window for more problems. However this
 * is already the case as this is the code which must detect the
 * problem.
 * However there is also the possibility of intercepting problems
 * even when a font2D reference is already directly held. Certain
 * validation points may check that font2Dhandle.font2D == font2D
 * If this is not true, then this font2D is not valid. Arguably
 * this check also just needs to be a de-referencing assignment :
 * font2D = font2DHandle.font2D.
 * The net effect of these steps is that very soon after a font
 * is identified as bad, that references and uses of it will be
 * eliminated.
 * In the initial implementation a Font2DHandle is what is held by
 * - java.awt.Font
 * - FontManager.initialisedFonts map
 * Font2D is held by
 * - FontFamily objects
 * - FontManager.registeredFonts map
 * - FontInfo object on a SunGraphics2D
 *
 * On discovering a bad font, all but the latter remove references to
 * the font. See FontManager.deRegisterBadFont(Font2D)
 */

public final class Font2DHandle {

    public Font2D font2D;

    public Font2DHandle(Font2D font) {
        font2D = font;
    }
}
