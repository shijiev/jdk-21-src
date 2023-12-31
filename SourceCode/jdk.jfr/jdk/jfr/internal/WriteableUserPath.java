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

package jdk.jfr.internal;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.Callable;

/**
 * Purpose of this class is to simplify analysis of security risks.
 * <p>
 * Paths in the public API should be wrapped in this class so we
 * at all time know what kind of paths we are dealing with.
 * <p>
 * A user supplied path must never be used in an unsafe context, such as a
 * shutdown hook or any other thread created by JFR.
 * <p>
 * All operation using this path must happen in {@link #doPrivilegedIO(Callable)}
 */
public final class WriteableUserPath {
    @SuppressWarnings("removal")
    private final AccessControlContext controlContext;
    private final Path original;
    private final Path real;
    private final String realPathText;
    private final String originalText;

    // Not to ensure security, but to help
    // against programming errors
    private volatile boolean inPrivileged;

    @SuppressWarnings("removal")
    public WriteableUserPath(Path path) throws IOException {
        controlContext = AccessController.getContext();
        // verify that the path is writeable
        if (Files.exists(path) && !Files.isWritable(path)) {
            // throw same type of exception as FileOutputStream
            // constructor, if file can't be opened.
            throw new FileNotFoundException("Could not write to file: " + path.toAbsolutePath());
        }
        // will throw if non-writeable
        BufferedWriter fw = Files.newBufferedWriter(path);
        fw.close();
        this.original = path;
        this.originalText = path.toString();
        this.real = path.toRealPath();
        this.realPathText = real.toString();
    }

    /**
     * Returns a potentially malicious path where the user may have implemented
     * their own version of Path. This method should never be called in an
     * unsafe context and the Path value should never be passed along to other
     * methods.
     *
     * @return path from a potentially malicious user
     */
    public Path getPotentiallyMaliciousOriginal() {
        return original;
    }

    /**
     * Returns a string representation of the real path.
     *
     * @return path as text
     */
    public String getRealPathText() {
        return realPathText;
    }

    /**
     * Returns a string representation of the original path.
     *
     * @return path as text
     */
    public String getOriginalText() {
        return originalText;
    }


    /**
     * Returns a potentially malicious path where the user may have implemented
     * their own version of Path. This method should never be called in an
     * unsafe context and the Path value should never be passed along to other
     * methods.
     *
     * @return path from a potentially malicious user
     */
    public Path getReal() {
        if (!inPrivileged) {
            throw new InternalError("A user path was accessed outside the context it was supplied in");
        }
        return real;
    }

    @SuppressWarnings("removal")
    public void doPrivilegedIO(Callable<?> function) throws IOException {
        try {
            inPrivileged = true;
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
                @Override
                public Void run() throws Exception {
                    function.call();
                    return null;
                }
            }, controlContext);
        } catch (Throwable t) {
            // prevent malicious user to propagate exception callback
            // in the wrong context
            Throwable cause = null;
            if (System.getSecurityManager() == null) {
                cause = t;
            }
            throw new IOException("Unexpected error during I/O operation", cause);
        } finally {
            inPrivileged = false;
        }
    }
}
