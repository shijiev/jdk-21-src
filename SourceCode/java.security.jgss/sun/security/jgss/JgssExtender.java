/*
 * Copyright (c) 2014, 2022, Oracle and/or its affiliates. All rights reserved.
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

package sun.security.jgss;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;

/**
 * The extending point of basic JGSS-API.
 * <p>
 * If a module wants to extend basic JGSS-API classes, it should extend this
 * class and register itself as "the extender" using the setExtender method.
 * When various GSSManager.createXXX methods are called, they will call
 * "the extender"'s wrap methods to create objects of extended types
 * instead of basic types.
 * <p>
 * We have only one extension now defined in com.sun.security.jgss, and the
 * registering process is triggered in {@link GSSManagerImpl} by calling
 * Class.forName("com.sun.security.jgss.Extender"). Only GSSContext
 * and GSSCredential are extended now.
 * <p>
 * The setExtender method should be called before any JGSS call.
 */
public class JgssExtender {

    // "The extender"
    private static volatile JgssExtender theOne = new JgssExtender();

    /**
     * Gets "the extender". GSSManager calls this method so that it can
     * wrap basic objects into extended objects.
     * @return the extender
     */
    public static JgssExtender getExtender() {
        return theOne;
    }

    /**
     * Set "the extender" so that GSSManager can create extended objects.
     */
    protected static void setExtender(JgssExtender theOne) {
        JgssExtender.theOne = theOne;
    }

    /**
     * Wraps a plain GSSCredential object into an extended type.
     */
    public GSSCredential wrap(GSSCredential cred) {
        return cred;
    }

    /**
     * Wraps a plain GSSContext object into an extended type.
     */
    public GSSContext wrap(GSSContext ctxt) {
        return ctxt;
    }
}
