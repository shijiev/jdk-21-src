/*
 * Copyright (c) 2009, 2022, Oracle and/or its affiliates. All rights reserved.
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
package sun.nio.ch.sctp;

import com.sun.nio.sctp.Association;
import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.NotificationHandler;
import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.SctpMultiChannel;
import com.sun.nio.sctp.SctpSocketOption;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

/**
 * Unimplemented.
 */
public class SctpMultiChannelImpl
        extends SctpMultiChannel {

    public SctpMultiChannelImpl(SelectorProvider provider) {
        super(provider);
        throw UnsupportedUtil.sctpUnsupported();
    }

    @Override
    public Set<Association> associations() {
        throw UnsupportedUtil.sctpUnsupported();
    }

    @Override
    public SctpMultiChannel bind(SocketAddress local,
                                 int backlog) throws IOException {
        throw UnsupportedUtil.sctpUnsupported();
    }

    @Override
    public SctpMultiChannel bindAddress(InetAddress address) throws IOException {
        throw UnsupportedUtil.sctpUnsupported();
    }

    @Override
    public SctpMultiChannel unbindAddress(InetAddress address) throws IOException {
        throw UnsupportedUtil.sctpUnsupported();
    }

    @Override
    public Set<SocketAddress> getAllLocalAddresses() throws IOException {
        throw UnsupportedUtil.sctpUnsupported();
    }

    @Override
    public Set<SocketAddress> getRemoteAddresses(Association association) throws IOException {
        throw UnsupportedUtil.sctpUnsupported();
    }

    @Override
    public SctpMultiChannel shutdown(Association association) throws IOException {
        throw UnsupportedUtil.sctpUnsupported();
    }

    @Override
    public <T> T getOption(SctpSocketOption<T> name,
                           Association association) throws IOException {
        throw UnsupportedUtil.sctpUnsupported();
    }

    @Override
    public <T> SctpMultiChannel setOption(SctpSocketOption<T> name,
                                          T value,
                                          Association association) throws IOException {
        throw UnsupportedUtil.sctpUnsupported();
    }

    @Override
    public Set<SctpSocketOption<?>> supportedOptions() {
        throw UnsupportedUtil.sctpUnsupported();
    }

    @Override
    public <T> MessageInfo receive(ByteBuffer buffer,
                                   T attachment,
                                   NotificationHandler<T> handler) throws IOException {
        throw UnsupportedUtil.sctpUnsupported();
    }

    @Override
    public int send(ByteBuffer buffer,
                    MessageInfo messageInfo) throws IOException {
        throw UnsupportedUtil.sctpUnsupported();
    }

    @Override
    public SctpChannel branch(Association association) throws IOException {
        throw UnsupportedUtil.sctpUnsupported();
    }

    @Override
    protected void implConfigureBlocking(boolean block) throws IOException {
        throw UnsupportedUtil.sctpUnsupported();
    }

    @Override
    public void implCloseSelectableChannel() throws IOException {
        throw UnsupportedUtil.sctpUnsupported();
    }
}
