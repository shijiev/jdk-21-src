/*
 * Copyright (c) 2009, 2023, Oracle and/or its affiliates. All rights reserved.
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
package sun.net.www.protocol.http;

import java.net.URL;
import java.net.PasswordAuthentication;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import sun.util.logging.PlatformLogger;

/**
 * Proxy class for loading NTLMAuthentication, so as to remove static
 * dependency.
 */
class NTLMAuthenticationProxy {
    private static Method supportsTA;
    private static Method isTrustedSite;
    private static final String clazzStr = "sun.net.www.protocol.http.ntlm.NTLMAuthentication";
    private static final String supportsTAStr = "supportsTransparentAuth";
    private static final String isTrustedSiteStr = "isTrustedSite";

    static final NTLMAuthenticationProxy proxy = tryLoadNTLMAuthentication();
    static final boolean supported = proxy != null ? true : false;
    static final boolean supportsTransparentAuth = supported ? supportsTransparentAuth() : false;

    private final Constructor<? extends AuthenticationInfo> threeArgCtr;
    private final Constructor<? extends AuthenticationInfo> fourArgCtr;

    private NTLMAuthenticationProxy(Constructor<? extends AuthenticationInfo> threeArgCtr,
                                    Constructor<? extends AuthenticationInfo> fourArgCtr) {
        this.threeArgCtr = threeArgCtr;
        this.fourArgCtr = fourArgCtr;
    }


    AuthenticationInfo create(boolean isProxy,
                              URL url,
                              PasswordAuthentication pw) {
        try {
            return threeArgCtr.newInstance(isProxy, url, pw);
        } catch (ReflectiveOperationException roe) {
            finest(roe);
        }

        return null;
    }

    AuthenticationInfo create(boolean isProxy,
                              String host,
                              int port,
                              PasswordAuthentication pw) {
        try {
            return fourArgCtr.newInstance(isProxy, host, port, pw);
        } catch (ReflectiveOperationException roe) {
            finest(roe);
        }

        return null;
    }

    /* Returns true if the NTLM implementation supports transparent
     * authentication (try with the current users credentials before
     * prompting for username and password, etc).
     */
    private static boolean supportsTransparentAuth() {
        try {
            return (Boolean)supportsTA.invoke(null);
        } catch (ReflectiveOperationException roe) {
            finest(roe);
        }

        return false;
    }

    /* Transparent authentication should only be tried with a trusted
     * site ( when running in a secure environment ).
     */
    public static boolean isTrustedSite(URL url) {
        try {
            return (Boolean)isTrustedSite.invoke(null, url);
        } catch (ReflectiveOperationException roe) {
            finest(roe);
        }

        return false;
    }

    /**
     * Loads the NTLM authentiation implementation through reflection. If
     * the class is present, then it must have the required constructors and
     * method. Otherwise, it is considered an error.
     */
    @SuppressWarnings("unchecked")
    private static NTLMAuthenticationProxy tryLoadNTLMAuthentication() {
        Class<? extends AuthenticationInfo> cl;
        Constructor<? extends AuthenticationInfo> threeArg, fourArg;
        try {
            cl = (Class<? extends AuthenticationInfo>)Class.forName(clazzStr, true, null);
            if (cl != null) {
                threeArg = cl.getConstructor(boolean.class,
                                             URL.class,
                                             PasswordAuthentication.class);
                fourArg = cl.getConstructor(boolean.class,
                                            String.class,
                                            int.class,
                                            PasswordAuthentication.class);
                supportsTA = cl.getDeclaredMethod(supportsTAStr);
                isTrustedSite = cl.getDeclaredMethod(isTrustedSiteStr, java.net.URL.class);
                return new NTLMAuthenticationProxy(threeArg,
                                                   fourArg);
            }
        } catch (ClassNotFoundException cnfe) {
            finest(cnfe);
        } catch (ReflectiveOperationException roe) {
            throw new AssertionError(roe);
        }

        return null;
    }

    static void finest(Exception e) {
        PlatformLogger logger = HttpURLConnection.getHttpLogger();
        if (logger.isLoggable(PlatformLogger.Level.FINEST)) {
            logger.finest("NTLMAuthenticationProxy: " + e);
        }
    }
}
