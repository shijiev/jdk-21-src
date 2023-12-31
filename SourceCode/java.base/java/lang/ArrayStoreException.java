/*
 * Copyright (c) 1995, 2023, Oracle and/or its affiliates. All rights reserved.
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

package java.lang;

/**
 * Thrown to indicate that an attempt has been made to store the
 * wrong type of object into an array of objects. For example, the
 * following code generates an {@code ArrayStoreException}:
 * <blockquote><pre>
 *     Object x[] = new String[3];
 *     x[0] = Integer.valueOf(0);
 * </pre></blockquote>
 *
 * @since   1.0
 */
public class ArrayStoreException extends RuntimeException {
    @java.io.Serial
    private static final long serialVersionUID = -4522193890499838241L;

    /**
     * Constructs an {@code ArrayStoreException} with no detail message.
     */
    public ArrayStoreException() {
        super();
    }

    /**
     * Constructs an {@code ArrayStoreException} with the specified
     * detail message.
     *
     * @param   s   the detail message.
     */
    public ArrayStoreException(String s) {
        super(s);
    }
}
