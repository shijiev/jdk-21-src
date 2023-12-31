/*
 * Copyright (c) 2000, 2022, Oracle and/or its affiliates. All rights reserved.
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

import java.io.Serial;
import java.security.Provider;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidParameterException;
import java.security.ProviderException;
import sun.security.jgss.krb5.Krb5MechFactory;
import sun.security.jgss.spnego.SpNegoMechFactory;
import static sun.security.util.SecurityConstants.PROVIDER_VER;

/**
 * Defines the Sun JGSS provider.
 * Will merger this with the Sun security provider
 * sun.security.provider.Sun when the JGSS src is merged with the JDK
 * src.
 *
 * Mechanisms supported are:
 *
 * - Kerberos v5 as defined in RFC 1964.
 *   Oid is 1.2.840.113554.1.2.2
 *
 * - SPNEGO as defined in RFC 2478
 *   Oid is 1.3.6.1.5.5.2
 *
 *   [Dummy mechanism is no longer compiled:
 * - Dummy mechanism. This is primarily useful to test a multi-mech
 *   environment.
 *   Oid is 1.3.6.1.4.1.42.2.26.1.2]
 *
 * @author Mayank Upadhyay
 */

public final class SunProvider extends Provider {

    @Serial
    private static final long serialVersionUID = -238911724858694198L;

    private static final String INFO = "Sun " +
        "(Kerberos v5, SPNEGO)";
    //  "(Kerberos v5, Dummy GSS-API Mechanism)";

    private static final class ProviderService extends Provider.Service {
        ProviderService(Provider p, String type, String algo, String cn) {
            super(p, type, algo, cn, null, null);
        }

        @Override
        public Object newInstance(Object ctrParamObj)
            throws NoSuchAlgorithmException {
            String type = getType();
            if (ctrParamObj != null) {
                throw new InvalidParameterException
                    ("constructorParameter not used with " + type +
                     " engines");
            }
            String algo = getAlgorithm();
            try {
                if (type.equals("GssApiMechanism")) {
                    if (algo.equals("1.2.840.113554.1.2.2")) {
                        return new Krb5MechFactory();
                    } else if (algo.equals("1.3.6.1.5.5.2")) {
                        return new SpNegoMechFactory();
                    }
                }
            } catch (Exception ex) {
                throw new NoSuchAlgorithmException
                    ("Error constructing " + type + " for " +
                    algo + " using SunJGSS", ex);
            }
            throw new ProviderException("No impl for " + algo +
                " " + type);
        }
    }

    @SuppressWarnings("removal")
    public SunProvider() {
        /* We are the Sun JGSS provider */
        super("SunJGSS", PROVIDER_VER, INFO);

        final Provider p = this;
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            putService(new ProviderService(p, "GssApiMechanism",
                       "1.2.840.113554.1.2.2",
                       "sun.security.jgss.krb5.Krb5MechFactory"));
            putService(new ProviderService(p, "GssApiMechanism",
                       "1.3.6.1.5.5.2",
                       "sun.security.jgss.spnego.SpNegoMechFactory"));
            return null;
        });
    }
}
