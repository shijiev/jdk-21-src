/*
 * Copyright (c) 2000, 2023, Oracle and/or its affiliates. All rights reserved.
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

package sun.security.provider.certpath;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.cert.CertPathValidatorException;

/**
 * A specification of a PKIX validation state
 * which is initialized by each build and updated each time a
 * certificate is added to the current path.
 *
 * @since       1.4
 * @author      Sean Mullan
 * @author      Yassir Elley
 */

interface State extends Cloneable {

    /**
     * Update the state with the next certificate added to the path.
     *
     * @param cert the certificate which is used to update the state
     */
    void updateState(X509Certificate cert)
        throws CertificateException, IOException, CertPathValidatorException;

    /**
     * Creates and returns a copy of this object
     */
    Object clone();

    /**
     * Returns a boolean flag indicating if the state is initial
     * (just starting)
     *
     * @return boolean flag indicating if the state is initial (just starting)
     */
    boolean isInitial();
}
