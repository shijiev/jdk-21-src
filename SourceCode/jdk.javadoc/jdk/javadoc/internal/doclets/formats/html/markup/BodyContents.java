/*
 * Copyright (c) 2019, 2022, Oracle and/or its affiliates. All rights reserved.
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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Content for the {@code <body>} element.
 *
 * The content is a {@code <div>} element that contains a
 * header that is always visible, and main content that
 * can be scrolled if necessary.
 */
public class BodyContents extends Content {

    private final List<Content> mainContents = new ArrayList<>();
    private HtmlTree header = null;
    private HtmlTree footer = null;

    public BodyContents addMainContent(Content content) {
        mainContents.add(content);
        return this;
    }

    public BodyContents setHeader(HtmlTree header) {
        this.header = Objects.requireNonNull(header);
        return this;
    }

    public BodyContents setFooter(HtmlTree footer) {
        this.footer = footer;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec This implementation always returns {@code false}.
     *
     * @return {@code false}
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean write(Writer out, String newline, boolean atNewline) throws IOException {
        return toContent().write(out, newline, atNewline);
    }

    /**
     * Returns the HTML for the contents of the BODY element.
     *
     * @return the HTML
     */
    private Content toContent() {
        if (header == null)
            throw new NullPointerException();

        HtmlTree flexHeader = header.addStyle(HtmlStyle.flexHeader);

        var flexContent = HtmlTree.DIV(HtmlStyle.flexContent)
                .add(HtmlTree.MAIN().add(mainContents))
                .add(footer == null ? Text.EMPTY : footer);

        return HtmlTree.DIV(HtmlStyle.flexBox)
                .add(flexHeader)
                .add(flexContent);
    }
}
