/*
 * Copyright (c) 2016, 2022, Oracle and/or its affiliates. All rights reserved.
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

package jdk.javadoc.internal.doclets.toolkit.util;

import java.io.IOException;

import jdk.javadoc.internal.doclets.toolkit.DocletException;


/**
 * Wraps an IOException and the path for the resource to which it applies.
 *
 * @apiNote This exception should be thrown by a doclet when an IO exception occurs
 *  and the file is known that was in use when the exception occurred.
 */
public class ResourceIOException extends DocletException {

    /**
     * The resource that was in use when the exception occurred.
     */
    @SuppressWarnings("serial") // Type of field is not Serializable
    public final DocPath resource;

    private static final long serialVersionUID = 1L;

    /**
     * Creates an exception to wrap an IO exception, the resource which caused it.
     *
     * @param resource the resource in use when the exception occurred
     * @param cause the underlying exception
     */
    public ResourceIOException(DocPath resource, IOException cause) {
        super(resource.getPath(), cause);
        this.resource = resource;
    }
}
