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
 * Wraps an IOException and the filename to which it applies.
 *
 * @apiNote This exception should be thrown by a doclet when an IO exception occurs
 *  and the file is known that was in use when the exception occurred.
 */
public class DocFileIOException extends DocletException {
    /**
     * A hint for the type of operation being performed when the exception occurred.
     *
     * @apiNote This may be used as a hint when reporting a message to the end user.
     */
    public enum Mode {
        /** The file was being opened for reading, or being read when the exception occurred. */
        READ,
        /** The file was being opened for writing, or being written when the exception occurred. */
        WRITE
    }

    /**
     * The file that was in use when the exception occurred.
     */
    @SuppressWarnings("serial") // Type of field is not Serializable
    public final DocFile fileName;

    /**
     * The mode in which the file was being used when the exception occurred.
     */
    public final Mode mode;

    private static final long serialVersionUID = 1L;

    /**
     * Creates an exception to wrap an IO exception, the file which caused it, and the manner
     * in which the file was being used.
     *
     * @param fileName the file in use when the exception occurred
     * @param mode the manner in which the file was being used
     * @param cause the underlying exception
     */
    public DocFileIOException(DocFile fileName, Mode mode, IOException cause) {
        super(mode + ":" + fileName.getPath(), cause);
        this.fileName = fileName;
        this.mode = mode;
    }
}
