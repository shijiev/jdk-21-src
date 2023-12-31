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

package sun.security.jgss.spi;

import org.ietf.jgss.*;
import java.security.Provider;

/**
 * This interface is implemented by a mechanism specific name element. A
 * GSSName is conceptually a container class of several name elements from
 * different mechanisms.
 *
 * @author Mayank Upadhyay
 */

public interface GSSNameSpi {

    Provider getProvider();

    /**
     * Equals method for the GSSNameSpi objects.
     * If either name denotes an anonymous principal, the call should
     * return false.
     *
     * @param name to be compared with
     * @return true if they both refer to the same entity, else false
     * @exception GSSException with major codes of BAD_NAMETYPE,
     *    BAD_NAME, FAILURE
     */
    boolean equals(GSSNameSpi name) throws GSSException;

    /**
     * Compares this <code>GSSNameSpi</code> object to another Object
     * that might be a <code>GSSNameSpi</code>. The behaviour is exactly
     * the same as in {@link #equals(GSSNameSpi) equals} except that
     * no GSSException is thrown; instead, false will be returned in the
     * situation where an error occurs.
     *
     * @param another the object to be compared to
     * @return true if they both refer to the same entity, else false
     * @see #equals(GSSNameSpi)
     */
    boolean equals(Object another);

    /**
     * Returns a hashcode value for this GSSNameSpi.
     *
     * @return a hashCode value
     */
    int hashCode();

    /**
     * Returns a flat name representation for this object. The name
     * format is defined in RFC 2078.
     *
     * @return the flat name representation for this object
     * @exception GSSException with major codes NAME_NOT_MN, BAD_NAME,
     *    BAD_NAME, FAILURE.
     */
    byte[] export() throws GSSException;


    /**
     * Get the mechanism type that this NameElement corresponds to.
     *
     * @return the Oid of the mechanism type
     */
    Oid getMechanism();

    /**
     * Returns a string representation for this name. The printed
     * name type can be obtained by calling getStringNameType().
     *
     * @return string form of this name
     * @see #getStringNameType()
     * @overrides Object#toString
     */
    String toString();


    /**
     * Returns the oid describing the format of the printable name.
     *
     * @return the Oid for the format of the printed name
     */
    Oid getStringNameType();

    /**
     * Indicates if this name object represents an Anonymous name.
     */
    boolean isAnonymousName();
}
