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

package sun.net.httpserver;

import java.io.*;
import javax.net.ssl.*;
import java.nio.channels.*;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

/**
 * encapsulates all the connection specific state for a HTTP/S connection
 * one of these is hung from the selector attachment and is used to locate
 * everything from that.
 */
class HttpConnection {

    HttpContextImpl context;
    SSLEngine engine;
    SSLContext sslContext;
    SSLStreams sslStreams;

    /* high level streams returned to application */
    InputStream i;

    /* low level stream that sits directly over channel */
    InputStream raw;
    OutputStream rawout;

    SocketChannel chan;
    SelectionKey selectionKey;
    String protocol;
    long idleStartTime; // absolute time in milli seconds, starting when the connection was marked idle
    volatile long reqStartedTime; // time when the request was initiated
    volatile long rspStartedTime; // time we started writing the response
    int remaining;
    boolean closed = false;
    Logger logger;

    public enum State {IDLE, REQUEST, RESPONSE, NEWLY_ACCEPTED};
    volatile State state;

    public String toString() {
        final var sb = new StringBuilder(HttpConnection.class.getSimpleName());
        if (chan != null) {
            sb.append(" (");
            sb.append(chan);
            sb.append(")");
        }
        return sb.toString();
    }

    HttpConnection () {
    }

    void setChannel (SocketChannel c) {
        chan = c;
    }

    void setContext (HttpContextImpl ctx) {
        context = ctx;
    }

    State getState() {
        return state;
    }

    void setState (State s) {
        state = s;
    }

    void setParameters (
        InputStream in, OutputStream rawout, SocketChannel chan,
        SSLEngine engine, SSLStreams sslStreams, SSLContext sslContext, String protocol,
        HttpContextImpl context, InputStream raw
    )
    {
        this.context = context;
        this.i = in;
        this.rawout = rawout;
        this.raw = raw;
        this.protocol = protocol;
        this.engine = engine;
        this.chan = chan;
        this.sslContext = sslContext;
        this.sslStreams = sslStreams;
        this.logger = context.getLogger();
    }

    SocketChannel getChannel () {
        return chan;
    }

    synchronized void close () {
        if (closed) {
            return;
        }
        closed = true;
        if (logger != null && chan != null) {
            logger.log (Level.TRACE, "Closing connection: " + chan.toString());
        }

        if (!chan.isOpen()) {
            ServerImpl.dprint ("Channel already closed");
            return;
        }
        try {
            /* need to ensure temporary selectors are closed */
            if (raw != null) {
                raw.close();
            }
        } catch (IOException e) {
            ServerImpl.dprint (e);
        }
        try {
            if (rawout != null) {
                rawout.close();
            }
        } catch (IOException e) {
            ServerImpl.dprint (e);
        }
        try {
            if (sslStreams != null) {
                sslStreams.close();
            }
        } catch (IOException e) {
            ServerImpl.dprint (e);
        }
        try {
            chan.close();
        } catch (IOException e) {
            ServerImpl.dprint (e);
        }
    }

    /* remaining is the number of bytes left on the lowest level inputstream
     * after the exchange is finished
     */
    void setRemaining (int r) {
        remaining = r;
    }

    int getRemaining () {
        return remaining;
    }

    SelectionKey getSelectionKey () {
        return selectionKey;
    }

    InputStream getInputStream () {
            return i;
    }

    OutputStream getRawOutputStream () {
            return rawout;
    }

    String getProtocol () {
            return protocol;
    }

    SSLEngine getSSLEngine () {
            return engine;
    }

    SSLContext getSSLContext () {
            return sslContext;
    }

    HttpContextImpl getHttpContext () {
            return context;
    }
}
