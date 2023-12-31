/*
 * Copyright (c) 2016, Oracle and/or its affiliates. All rights reserved.
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
package jdk.jshell.execution.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Jan Lahoda
 */
public class PipeInputStream extends InputStream {

    private static final int INITIAL_SIZE = 128;
    private int[] buffer = new int[INITIAL_SIZE];
    private int start;
    private int end;
    private boolean closed;

    @Override
    public synchronized int read() throws IOException {
        if (start == end && !closed) {
            inputNeeded();
        }
        while (start == end) {
            if (closed) {
                return -1;
            }
            try {
                wait();
            } catch (InterruptedException ex) {
                //ignore
            }
        }
        try {
            return buffer[start];
        } finally {
            start = (start + 1) % buffer.length;
        }
    }

    @Override
    public synchronized int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int c = read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte)c;

        int totalRead = 1;
        while (totalRead < len && start != end) {
            int r = read();
            if (r == (-1))
                break;
            b[off + totalRead++] = (byte) r;
        }
        return totalRead;
    }

    protected void inputNeeded() throws IOException {}

    private synchronized void write(int b) {
        if (closed) {
            throw new IllegalStateException("Already closed.");
        }
        int newEnd = (end + 1) % buffer.length;
        if (newEnd == start) {
            //overflow:
            int[] newBuffer = new int[buffer.length * 2];
            int rightPart = (end > start ? end : buffer.length) - start;
            int leftPart = end > start ? 0 : start - 1;
            System.arraycopy(buffer, start, newBuffer, 0, rightPart);
            System.arraycopy(buffer, 0, newBuffer, rightPart, leftPart);
            buffer = newBuffer;
            start = 0;
            end = rightPart + leftPart;
            newEnd = end + 1;
        }
        buffer[end] = b;
        end = newEnd;
        notifyAll();
    }

    @Override
    public synchronized void close() {
        closed = true;
        notifyAll();
    }

    public OutputStream createOutput() {
        return new OutputStream() {
            @Override public void write(int b) throws IOException {
                PipeInputStream.this.write(b);
            }
            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                for (int i = 0 ; i < len ; i++) {
                    write(Byte.toUnsignedInt(b[off + i]));
                }
            }
            @Override
            public void close() throws IOException {
                PipeInputStream.this.close();
            }
        };
    }

}
