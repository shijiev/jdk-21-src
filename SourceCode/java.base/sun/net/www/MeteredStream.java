/*
 * Copyright (c) 1994, 2022, Oracle and/or its affiliates. All rights reserved.
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

package sun.net.www;

import java.io.*;
import java.util.concurrent.locks.ReentrantLock;
import sun.net.www.http.ChunkedInputStream;


public class MeteredStream extends FilterInputStream {

    // Instance variables.
    /* if expected != -1, after we've read >= expected, we're "closed" and return -1
     * from subsequent read() 's
     */
    protected boolean closed = false;
    protected long expected;
    protected long count = 0;
    protected long markedCount = 0;
    protected int markLimit = -1;
    private final ReentrantLock readLock = new ReentrantLock();

    public MeteredStream(InputStream is, long expected)
    {
        super(is);

        this.expected = expected;
    }

    private final void justRead(long n) throws IOException {
        assert isLockHeldByCurrentThread();

        if (n == -1) {

            /*
             * don't close automatically when mark is set and is valid;
             * cannot reset() after close()
             */
            if (!isMarked()) {
                close();
            }
            return;
        }

        count += n;

        /**
         * If read beyond the markLimit, invalidate the mark
         */
        if (count - markedCount > markLimit) {
            markLimit = -1;
        }

        if (isMarked()) {
            return;
        }

        // if expected length is known, we could determine if
        // read overrun.
        if (expected > 0)   {
            if (count >= expected) {
                close();
            }
        }
    }

    /**
     * Returns true if the mark is valid, false otherwise
     */
    private boolean isMarked() {
        assert isLockHeldByCurrentThread();

        if (markLimit < 0) {
            return false;
        }

        // mark is set, but is not valid anymore
        if (count - markedCount > markLimit) {
           return false;
        }

        // mark still holds
        return true;
    }

    public int read() throws java.io.IOException {
        lock();
        try {
            if (closed) return -1;
            int c = in.read();
            if (c != -1) {
                justRead(1);
            } else {
                justRead(c);
            }
            return c;
        } finally {
            unlock();
        }
    }

    public int read(byte b[], int off, int len)
                throws java.io.IOException {
        lock();
        try {
            if (closed) return -1;

            int n = in.read(b, off, len);
            justRead(n);
            return n;
        } finally {
            unlock();
        }
    }

    public long skip(long n) throws IOException {
        lock();
        try {
            // REMIND: what does skip do on EOF????
            if (closed) return 0;

            if (in instanceof ChunkedInputStream) {
                n = in.skip(n);
            } else {
                // just skip min(n, num_bytes_left)
                long min = (n > expected - count) ? expected - count : n;
                n = in.skip(min);
            }
            justRead(n);
            return n;
        } finally {
            unlock();
        }
    }

    public void close() throws IOException {
        lock();
        try {
            if (closed) return;

            closed = true;
            in.close();
        } finally {
            unlock();
        }
    }

    public int available() throws IOException {
        lock();
        try {
            return closed ? 0 : in.available();
        } finally {
            unlock();
        }
    }

    public void mark(int readLimit) {
        lock();
        try {
            if (closed) return;
            super.mark(readLimit);

            /*
             * mark the count to restore upon reset
             */
            markedCount = count;
            markLimit = readLimit;
        } finally {
            unlock();
        }
    }

    public void reset() throws IOException {
        lock();
        try {
            if (closed) return;
            if (!isMarked()) {
                throw new IOException("Resetting to an invalid mark");
            }

            count = markedCount;
            super.reset();
        } finally {
            unlock();
        }
    }

    public boolean markSupported() {
        lock();
        try {
            if (closed) return false;
            return super.markSupported();
        } finally {
            unlock();
        }
    }

    public final void lock() {
        readLock.lock();
    }

    public final void unlock() {
        readLock.unlock();
    }

    public final boolean isLockHeldByCurrentThread() {
        return readLock.isHeldByCurrentThread();
    }
}
