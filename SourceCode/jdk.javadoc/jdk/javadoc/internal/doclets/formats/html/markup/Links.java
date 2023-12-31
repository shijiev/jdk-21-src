/*
 * Copyright (c) 1997, 2022, Oracle and/or its affiliates. All rights reserved.
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

package jdk.javadoc.internal.doclets.formats.html.markup;

import jdk.javadoc.internal.doclets.toolkit.Content;
import jdk.javadoc.internal.doclets.toolkit.util.DocLink;
import jdk.javadoc.internal.doclets.toolkit.util.DocPath;

/**
 * Factory for HTML A elements: links (with a {@code href} attribute).
 */
public class Links {

    private final DocPath file;

    /**
     * Creates a {@code Links} object for a specific file.
     * Links to other files will be made relative to this file where possible.
     *
     * @param file the file
     */
    public Links(DocPath file) {
        this.file = file;
    }

    /**
     * Creates a link of the form {@code <a href="#id">label</a>}.
     *
     * @param id    the position of the link in the file
     * @param label the content for the link
     * @return the link
     */
    public Content createLink(HtmlId id, Content label) {
        DocLink l = DocLink.fragment(id.name());
        return createLink(l, label, "");
    }

    /**
     * Creates a link of the form {@code <a href="#id">label</a>} if {@code link}
     * is {@code true}, or else just returns {@code label}.
     *
     * @param id    the position of the link in the file
     * @param label the content for the link
     * @param link  whether to create a link or just return the label
     * @return the link or just the label
     */
    public Content createLink(HtmlId id, Content label, boolean link) {
        return link ? createLink(id, label) : label;
    }

    /**
     * Creates a link of the form {@code <a href="#id" title="title">label</a>}.
     *
     * @param id     the id to which the link will be created
     * @param label  the content for the link
     * @param title  the title for the link
     *
     * @return the link
     */
    public Content createLink(HtmlId id, Content label, String title) {
        DocLink l = DocLink.fragment(id.name());
        return createLink(l, label, title);
    }

    /**
     * Creates a link of the form {@code <a href="path">label</a>}.
     *
     * @param path   the path for the link
     * @param label  the content for the link
     * @return the link
     */
    public Content createLink(DocPath path, String label) {
        return createLink(path, Text.of(label), null, "");
    }

    /**
     * Creates a link of the form {@code <a href="path">label</a>}.
     *
     * @param path   the path for the link
     * @param label  the content for the link
     * @return the link
     */
    public Content createLink(DocPath path, Content label) {
        return createLink(path, label, "");
    }

    /**
     * Creates a link of the form {@code <a href="path" title="title">label</a>}.
     * If {@code style} is not null, it will be added as {@code class="style"} to the link.
     *
     * @param path      the path for the link
     * @param label     the content for the link
     * @param style     the style for the link, or null
     * @param title     the title for the link
     * @return the link
     */
    public Content createLink(DocPath path, Content label, HtmlStyle style, String title) {
        return createLink(new DocLink(path), label, style, title);
    }

    /**
     * Creates a link of the form {@code <a href="path" title="title">label</a>}.
     *
     * @param path      the path for the link
     * @param label     the content for the link
     * @param title     the title for the link
     * @return the link
     */
    public Content createLink(DocPath path, Content label, String title) {
        return createLink(new DocLink(path), label, title);
    }

    /**
     * Creates a link of the form {@code <a href="link">label</a>}.
     *
     * @param link      the details for the link
     * @param label     the content for the link
     * @return the link
     */
    public Content createLink(DocLink link, Content label) {
        return createLink(link, label, "");
    }

    /**
     * Creates a link of the form {@code <a href="path" title="title">label</a>}.
     *
     * @param link      the details for the link
     * @param label     the content for the link
     * @param title     the title for the link
     * @return the link
     */
    public Content createLink(DocLink link, Content label, String title) {
        var anchor = HtmlTree.A(link.relativizeAgainst(file).toString(), label);
        if (title != null && title.length() != 0) {
            anchor.put(HtmlAttr.TITLE, title);
        }
        return anchor;
    }

    /**
     * Creates a link of the form {@code <a href="link" title="title" >label</a>}.
     * If {@code style} is not null, it will be added as {@code class="style"} to the link.
     *
     * @param link      the details for the link
     * @param label     the content for the link
     * @param style     the style for the link, or null
     * @param title     the title for the link
     * @return the link
     */
    public Content createLink(DocLink link, Content label, HtmlStyle style,
                              String title) {
        return createLink(link, label, style, title, false);
    }

    /**
     * Creates a link of the form {@code <a href="link" title="title">label</a>}.
     * If {@code style} is not null, it will be added as {@code class="style"} to the link.
     *
     * @param link       the details for the link
     * @param label      the content for the link
     * @param style      the style for the link, or null
     * @param title      the title for the link
     * @param isExternal is the link external to the generated documentation
     * @return the link
     */
    public Content createLink(DocLink link, Content label, HtmlStyle style,
                              String title, boolean isExternal) {
        var l = HtmlTree.A(link.relativizeAgainst(file).toString(), label);
        if (style != null) {
            l.setStyle(style);
        }
        if (title != null && title.length() != 0) {
            l.put(HtmlAttr.TITLE, title);
        }
        if (isExternal) {
            // Use addStyle as external links might have an explicit style set above as well.
            l.addStyle(HtmlStyle.externalLink);
        }
        return l;
    }

    /**
     * Creates an external link.
     *
     * @param link       the details for the link
     * @param label      the content for the link
     * @return the link
     */
    public Content createExternalLink(DocLink link, Content label) {
        return HtmlTree.A(link.relativizeAgainst(file).toString(), label)
            .setStyle(HtmlStyle.externalLink);
    }
}
