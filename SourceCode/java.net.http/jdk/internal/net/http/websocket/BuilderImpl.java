/*
 * Copyright (c) 2015, 2021, Oracle and/or its affiliates. All rights reserved.
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

package jdk.internal.net.http.websocket;

import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Builder;
import java.net.http.WebSocket.Listener;
import jdk.internal.net.http.common.Pair;

import java.net.ProxySelector;
import java.net.URI;
import java.time.Duration;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;
import static jdk.internal.net.http.common.Pair.pair;

public final class BuilderImpl implements Builder {

    private final HttpClient client;
    private URI uri;
    private Listener listener;
    private final Optional<ProxySelector> proxySelector;
    private final Collection<Pair<String, String>> headers;
    private final Collection<String> subprotocols;
    private Duration timeout;

    public BuilderImpl(HttpClient client, ProxySelector proxySelector)
    {
        this(client, null, null, Optional.ofNullable(proxySelector),
             new ArrayList<>(), new ArrayList<>(), null);
    }

    private BuilderImpl(HttpClient client,
                        URI uri,
                        Listener listener,
                        Optional<ProxySelector> proxySelector,
                        Collection<Pair<String, String>> headers,
                        Collection<String> subprotocols,
                        Duration timeout) {
        this.client = client;
        this.uri = uri;
        this.listener = listener;
        this.proxySelector = proxySelector;
        // If a proxy selector was supplied by the user, it should be present
        // on the client and should be the same that what we got as an argument
        assert client.proxy().isEmpty()
                || client.proxy().equals(proxySelector);
        this.headers = headers;
        this.subprotocols = subprotocols;
        this.timeout = timeout;
    }

    @Override
    public Builder header(String name, String value) {
        requireNonNull(name, "name");
        requireNonNull(value, "value");
        headers.add(pair(name, value));
        return this;
    }

    @Override
    public Builder subprotocols(String mostPreferred, String... lesserPreferred)
    {
        requireNonNull(mostPreferred, "mostPreferred");
        requireNonNull(lesserPreferred, "lesserPreferred");
        List<String> subprotocols = new ArrayList<>(lesserPreferred.length + 1);
        subprotocols.add(mostPreferred);
        for (int i = 0; i < lesserPreferred.length; i++) {
            String p = lesserPreferred[i];
            requireNonNull(p, "lesserPreferred[" + i + "]");
            subprotocols.add(p);
        }
        this.subprotocols.clear();
        this.subprotocols.addAll(subprotocols);
        return this;
    }

    @Override
    public Builder connectTimeout(Duration timeout) {
        this.timeout = requireNonNull(timeout, "timeout");
        return this;
    }

    @Override
    public CompletableFuture<WebSocket> buildAsync(URI uri, Listener listener) {
        this.uri = requireNonNull(uri, "uri");
        this.listener = requireNonNull(listener, "listener");
        // A snapshot of builder inaccessible for further modification
        // from the outside
        BuilderImpl copy = immutableCopy();
        return WebSocketImpl.newInstanceAsync(copy);
    }

    HttpClient getClient() { return client; }

    URI getUri() { return uri; }

    Listener getListener() { return listener; }

    Collection<Pair<String, String>> getHeaders() { return headers; }

    Collection<String> getSubprotocols() { return subprotocols; }

    Duration getConnectTimeout() { return timeout; }

    Optional<ProxySelector> getProxySelector() { return proxySelector; }

    private BuilderImpl immutableCopy() {
        @SuppressWarnings({"unchecked", "rawtypes"})
        BuilderImpl copy = new BuilderImpl(
                client,
                uri,
                listener,
                proxySelector,
                List.of(this.headers.toArray(new Pair[0])),
                List.of(this.subprotocols.toArray(new String[0])),
                timeout);
        return copy;
    }
}
