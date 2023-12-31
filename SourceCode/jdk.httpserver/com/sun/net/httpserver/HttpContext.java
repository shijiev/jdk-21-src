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

package com.sun.net.httpserver;
import java.util.List;
import java.util.Map;

/**
 * {@code HttpContext} represents a mapping between the root {@link java.net.URI}
 * path of an application to a {@link HttpHandler} which is invoked to handle
 * requests destined for that path on the associated {@link HttpServer} or
 * {@link HttpsServer}.
 *
 * <p> {@code HttpContext} instances are created by the create methods in
 * {@code HttpServer} and {@code HttpsServer}.
 *
 * <p> A chain of {@link Filter} objects can be added to a {@code HttpContext}.
 * All exchanges processed by the context can be pre- and post-processed by each
 * {@code Filter} in the chain.
 *
 * @since 1.6
 */
public abstract class HttpContext {

    /**
     * Constructor for subclasses to call.
     */
    protected HttpContext() {
    }

    /**
     * Returns the handler for this context.
     *
     * @return the {@code HttpHandler} for this context
     */
    public abstract HttpHandler getHandler();

    /**
     * Sets the handler for this context, if not already set.
     *
     * @param handler the handler to set for this context
     * @throws IllegalArgumentException if the context for this handler is already set.
     * @throws NullPointerException if handler is {@code null}
     */
    public abstract void setHandler(HttpHandler handler);

    /**
     * Returns the path this context was created with.
     *
     * @return the context of this path
     */
    public abstract String getPath();

    /**
     * Returns the server this context was created with.
     *
     * @return the context of this server
     */
    public abstract HttpServer getServer();

    /**
     * Returns a mutable {@link Map}, which can be used to pass configuration
     * and other data to {@link Filter} modules and to the context's exchange
     * handler.
     *
     * <p> Every attribute stored in this {@code Map} will be visible to every
     * {@code HttpExchange} processed by this context.
     *
     * @return a {@code Map} containing the attributes of this context
     */
    public abstract Map<String,Object> getAttributes() ;

    /**
     * Returns this context's {@link List} of {@linkplain Filter filters}. This
     * is the actual list used by the server when dispatching requests so
     * modifications to this list immediately affect the handling of exchanges.
     *
     * @return a {@link List} containing the filters of this context
     */
    public abstract List<Filter> getFilters();

    /**
     * Sets the {@link Authenticator} for this {@code HttpContext}. Once an authenticator
     * is established on a context, all client requests must be authenticated,
     * and the given object will be invoked to validate each request. Each call
     * to this method replaces any previous value set.
     *
     * @param auth the {@code Authenticator} to set. If {@code null} then any previously
     *             set {@code Authenticator} is removed, and client authentication
     *             will no longer be required.
     * @return the previous {@code Authenticator}, if any set, or {@code null} otherwise.
     */
    public abstract Authenticator setAuthenticator(Authenticator auth);

    /**
     * Returns the currently set {@link Authenticator} for this context
     * if one exists.
     *
     * @return this {@linkplain HttpContext HttpContext's} {@code Authenticator},
     * or {@code null} if none is set
     */
    public abstract Authenticator getAuthenticator();
}
