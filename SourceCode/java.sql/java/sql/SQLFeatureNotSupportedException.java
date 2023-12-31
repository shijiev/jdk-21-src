/*
 * Copyright (c) 2005, 2022, Oracle and/or its affiliates. All rights reserved.
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

package java.sql;

/**
 * The subclass of {@link SQLException} thrown when the SQLState class value is '<i>0A</i>'
 * ( the value is 'zero' A).
 * This indicates that the JDBC driver does not support an optional JDBC feature.
 * Optional JDBC features can fall into the following categories:
 *
 *<UL>
 *<LI>no support for an optional feature
 *<LI>no support for an optional overloaded method
 *<LI>no support for an optional mode for a method.  The mode for a method is
 *determined based on constants passed as parameter values to a method
 *</UL>
 *
 * @since 1.6
 */
public class SQLFeatureNotSupportedException extends SQLNonTransientException {

        /**
         * Constructs a {@code SQLFeatureNotSupportedException} object.
         *  The {@code reason}, {@code SQLState} are initialized
         * to {@code null} and the vendor code is initialized to 0.
         *
         * The {@code cause} is not initialized, and may subsequently be
         * initialized by a call to the
         * {@link Throwable#initCause(java.lang.Throwable)} method.
         *
         * @since 1.6
         */
        public SQLFeatureNotSupportedException() {
                super();
        }

        /**
         * Constructs a {@code SQLFeatureNotSupportedException} object
         * with a given {@code reason}. The {@code SQLState}
         * is initialized to {@code null} and the vendor code is initialized
         * to 0.
         *
         * The {@code cause} is not initialized, and may subsequently be
         * initialized by a call to the
         * {@link Throwable#initCause(java.lang.Throwable)} method.
         *
         * @param reason a description of the exception
         * @since 1.6
         */
        public SQLFeatureNotSupportedException(String reason) {
                super(reason);
        }

        /**
         * Constructs a {@code SQLFeatureNotSupportedException} object
         * with a given {@code reason} and {@code SQLState}.
         *
         * The {@code cause} is not initialized, and may subsequently be
         * initialized by a call to the
         * {@link Throwable#initCause(java.lang.Throwable)} method. The vendor code
         * is initialized to 0.
         *
         * @param reason a description of the exception
         * @param SQLState an XOPEN or SQL:2003 code identifying the exception
         * @since 1.6
         */
        public SQLFeatureNotSupportedException(String reason, String SQLState) {
                super(reason,SQLState);
        }

        /**
         * Constructs a {@code SQLFeatureNotSupportedException} object
         *  with a given {@code reason}, {@code SQLState}  and
         * {@code vendorCode}.
         *
         * The {@code cause} is not initialized, and may subsequently be
         * initialized by a call to the
         * {@link Throwable#initCause(java.lang.Throwable)} method.
         *
         * @param reason a description of the exception
         * @param SQLState an XOPEN or SQL:2003 code identifying the exception
         * @param vendorCode a database vendor specific exception code
         * @since 1.6
         */
        public SQLFeatureNotSupportedException(String reason, String SQLState, int vendorCode) {
                super(reason,SQLState,vendorCode);
        }

    /**
     * Constructs a {@code SQLFeatureNotSupportedException} object
     *   with a given  {@code cause}.
     * The {@code SQLState} is initialized
     * to {@code null} and the vendor code is initialized to 0.
     * The {@code reason}  is initialized to {@code null} if
     * {@code cause==null} or to {@code cause.toString()} if
     * {@code cause!=null}.
     *
     * @param cause the underlying reason for this {@code SQLException} (which is saved for later retrieval by the {@code getCause()} method); may be null indicating
     *     the cause is non-existent or unknown.
     * @since 1.6
     */
    public SQLFeatureNotSupportedException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a {@code SQLFeatureNotSupportedException} object
     * with a given
     * {@code reason} and  {@code cause}.
     * The {@code SQLState} is  initialized to {@code null}
     * and the vendor code is initialized to 0.
     *
     * @param reason a description of the exception.
     * @param cause the underlying reason for this {@code SQLException} (which is saved for later retrieval by the {@code getCause()} method); may be null indicating
     *     the cause is non-existent or unknown.
     * @since 1.6
     */
    public SQLFeatureNotSupportedException(String reason, Throwable cause) {
        super(reason,cause);
    }

    /**
     * Constructs a {@code SQLFeatureNotSupportedException} object
     * with a given
     * {@code reason}, {@code SQLState} and  {@code cause}.
     * The vendor code is initialized to 0.
     *
     * @param reason a description of the exception.
     * @param SQLState an XOPEN or SQL:2003 code identifying the exception
     * @param cause the (which is saved for later retrieval by the {@code getCause()} method); may be null indicating
     *     the cause is non-existent or unknown.
     * @since 1.6
     */
    public SQLFeatureNotSupportedException(String reason, String SQLState, Throwable cause) {
        super(reason,SQLState,cause);
    }

    /**
     *  Constructs a {@code SQLFeatureNotSupportedException} object
     * with a given
     * {@code reason}, {@code SQLState}, {@code vendorCode}
     * and  {@code cause}.
     *
     * @param reason a description of the exception
     * @param SQLState an XOPEN or SQL:2003 code identifying the exception
     * @param vendorCode a database vendor-specific exception code
     * @param cause the underlying reason for this {@code SQLException} (which is saved for later retrieval by the {@code getCause()} method); may be null indicating
     *     the cause is non-existent or unknown.
     * @since 1.6
     */
    public SQLFeatureNotSupportedException(String reason, String SQLState, int vendorCode, Throwable cause) {
        super(reason,SQLState,vendorCode,cause);
    }

    private static final long serialVersionUID = -1026510870282316051L;
}
