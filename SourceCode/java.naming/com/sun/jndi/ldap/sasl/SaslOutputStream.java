/*
 * Copyright (c) 2001, 2021, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.jndi.ldap.sasl;

import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import java.io.IOException;
import java.io.FilterOutputStream;
import java.io.OutputStream;

class SaslOutputStream extends FilterOutputStream {
    private static final boolean debug = false;

    private byte[] lenBuf = new byte[4];  // buffer for storing length
    private int rawSendSize = 65536;
    private SaslClient sc;

    SaslOutputStream(SaslClient sc, OutputStream out) throws SaslException {
        super(out);
        this.sc = sc;

        if (debug) {
            System.err.println("SaslOutputStream: " + out);
        }

        String str = (String) sc.getNegotiatedProperty(Sasl.RAW_SEND_SIZE);
        if (str != null) {
            try {
                rawSendSize = Integer.parseInt(str);
            } catch (NumberFormatException e) {
                throw new SaslException(Sasl.RAW_SEND_SIZE +
                    " property must be numeric string: " + str);
            }
        }
    }

    // Override this method to call write(byte[], int, int) counterpart
    // super.write(int) simply calls out.write(int)

    public void write(int b) throws IOException {
        byte[] buffer = new byte[1];
        buffer[0] = (byte)b;
        write(buffer, 0, 1);
    }

    /**
     * Override this method to "wrap" the outgoing buffer before
     * writing it to the underlying output stream.
     */
    public void write(byte[] buffer, int offset, int total) throws IOException {
        int count;
        byte[] wrappedToken;

        // "Packetize" buffer to be within rawSendSize
        if (debug) {
            System.err.println("Total size: " + total);
        }

        for (int i = 0; i < total; i += rawSendSize) {

            // Calculate length of current "packet"
            count = (total - i) < rawSendSize ? (total - i) : rawSendSize;

            // Generate wrapped token
            wrappedToken = sc.wrap(buffer, offset+i, count);

            // Write out length
            intToNetworkByteOrder(wrappedToken.length, lenBuf, 0, 4);

            if (debug) {
                System.err.println("sending size: " + wrappedToken.length);
            }
            out.write(lenBuf, 0, 4);

            // Write out wrapped token
            out.write(wrappedToken, 0, wrappedToken.length);
        }
    }

    public void close() throws IOException {
        SaslException save = null;
        try {
            sc.dispose();  // Dispose of SaslClient's state
        } catch (SaslException e) {
            // Save exception for throwing after closing 'in'
            save = e;
        }
        super.close();  // Close underlying output stream

        if (save != null) {
            throw save;
        }
    }

    // Copied from com.sun.security.sasl.util.SaslImpl
    /**
     * Encodes an integer into 4 bytes in network byte order in the buffer
     * supplied.
     */
    private static void intToNetworkByteOrder(int num, byte[] buf, int start,
        int count) {
        if (count > 4) {
            throw new IllegalArgumentException("Cannot handle more than 4 bytes");
        }

        for (int i = count-1; i >= 0; i--) {
            buf[start+i] = (byte)(num & 0xff);
            num >>>= 8;
        }
    }
}
