/*
 * Copyright (c) 2005, 2023, Oracle and/or its affiliates. All rights reserved.
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

package sun.security.jgss.wrapper;

import org.ietf.jgss.ChannelBinding;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;
import org.ietf.jgss.Oid;

import java.util.Hashtable;

/**
 * This class is essentially a JNI calling stub for all wrapper classes.
 *
 * @author Valerie Peng
 * @since 1.6
 */

class GSSLibStub {

    private final Oid mech;
    private long pMech; // Warning: used by NativeUtil.c

    /**
     * Initialization routine to dynamically load function pointers.
     *
     * @param lib library name to dlopen
     * @param debug set to true for reporting native debugging info
     * @return true if succeeded, false otherwise.
     */
    static native boolean init(String lib, boolean debug);
    private static native long getMechPtr(byte[] oidDerEncoding);

    // Miscellaneous routines
    static native Oid[] indicateMechs();
    native Oid[] inquireNamesForMech() throws GSSException;

    // Name related routines
    native void releaseName(long pName);
    native long importName(byte[] name, Oid type);
    native boolean compareName(long pName1, long pName2);
    native long canonicalizeName(long pName);
    native byte[] exportName(long pName) throws GSSException;
    native Object[] displayName(long pName) throws GSSException;

    // Credential related routines
    native long acquireCred(long pName, int lifetime, int usage)
                                        throws GSSException;
    native long releaseCred(long pCred);
    native long getCredName(long pCred);
    native int getCredTime(long pCred);
    native int getCredUsage(long pCred);

    // Context related routines
    native NativeGSSContext importContext(byte[] interProcToken);
    native byte[] initContext(long pCred, long targetName, ChannelBinding cb,
                              byte[] inToken, NativeGSSContext context);
    native byte[] acceptContext(long pCred, ChannelBinding cb,
                                byte[] inToken, NativeGSSContext context);
    native long[] inquireContext(long pContext);
    native Oid getContextMech(long pContext);
    native long getContextName(long pContext, boolean isSrc);
    native int getContextTime(long pContext);
    native long deleteContext(long pContext);
    native int wrapSizeLimit(long pContext, int flags, int qop, int outSize);
    native byte[] exportContext(long pContext);
    native byte[] getMic(long pContext, int qop, byte[] msg);
    native void verifyMic(long pContext, byte[] token, byte[] msg,
                          MessageProp prop) ;
    native byte[] wrap(long pContext, byte[] msg, MessageProp prop);
    native byte[] unwrap(long pContext, byte[] msgToken, MessageProp prop);

    private static final Hashtable<Oid, GSSLibStub>
        table = new Hashtable<>(5);

    static GSSLibStub getInstance(Oid mech) throws GSSException {
        GSSLibStub s = table.get(mech);
        if (s == null) {
            s = new GSSLibStub(mech);
            table.put(mech, s);
        }
        return s;
    }
    private GSSLibStub(Oid mech) throws GSSException {
        if (SunNativeProvider.DEBUG) {
            SunNativeProvider.debug("Created GSSLibStub for mech " + mech);
        }
        this.mech = mech;
        this.pMech = getMechPtr(mech.getDER());
    }
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof GSSLibStub)) {
            return false;
        }
        return (mech.equals(((GSSLibStub) obj).getMech()));
    }
    public int hashCode() {
        return mech.hashCode();
    }
    Oid getMech() {
        return mech;
    }
}
