/*
 * Copyright (c) 1998, 2023, Oracle and/or its affiliates. All rights reserved.
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

package sun.net;

import java.security.PrivilegedAction;
import java.security.Security;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("removal")
public final class InetAddressCachePolicy {

    // Controls the cache policy for successful lookups only
    private static final String cachePolicyProp = "networkaddress.cache.ttl";
    private static final String cachePolicyPropFallback =
        "sun.net.inetaddr.ttl";

    // Controls the cache stale policy for successful lookups only
    private static final String cacheStalePolicyProp =
        "networkaddress.cache.stale.ttl";
    private static final String cacheStalePolicyPropFallback =
        "sun.net.inetaddr.stale.ttl";

    // Controls the cache policy for negative lookups only
    private static final String negativeCachePolicyProp =
        "networkaddress.cache.negative.ttl";
    private static final String negativeCachePolicyPropFallback =
        "sun.net.inetaddr.negative.ttl";

    public static final int FOREVER = -1;
    public static final int NEVER = 0;

    /* default value for positive lookups */
    public static final int DEFAULT_POSITIVE = 30;

    /* The Java-level namelookup cache policy for successful lookups:
     *
     * -1: caching forever
     * any positive value: the number of seconds to cache an address for
     *
     * default value is forever (FOREVER), as we let the platform do the
     * caching. For security reasons, this caching is made forever when
     * a security manager is set.
     */
    private static volatile int cachePolicy = FOREVER;

    /* The Java-level namelookup cache stale policy:
     *
     * any positive value: the number of seconds to use the stale names
     * zero: do not use stale names
     *
     * default value is never (NEVER).
     */
    private static volatile int staleCachePolicy = NEVER;

    /* The Java-level namelookup cache policy for negative lookups:
     *
     * -1: caching forever
     * any positive value: the number of seconds to cache an address for
     *
     * default value is 0. It can be set to some other value for
     * performance reasons.
     */
    private static volatile int negativeCachePolicy = NEVER;

    /*
     * Whether or not the cache policy for successful lookups was set
     * using a property (cmd line).
     */
    private static boolean propertySet;

    /*
     * Whether or not the cache policy for negative lookups was set
     * using a property (cmd line).
     */
    private static boolean propertyNegativeSet;

    /*
     * Initialize
     */
    static {
        Integer tmp = getProperty(cachePolicyProp, cachePolicyPropFallback);
        if (tmp != null) {
            cachePolicy = tmp < 0 ? FOREVER : tmp;
            propertySet = true;
        } else {
            /* No properties defined for positive caching. If there is no
             * security manager then use the default positive cache value.
             */
            if (System.getSecurityManager() == null) {
                cachePolicy = DEFAULT_POSITIVE;
            }
        }
        tmp = getProperty(negativeCachePolicyProp,
                          negativeCachePolicyPropFallback);

        if (tmp != null) {
            negativeCachePolicy = tmp < 0 ? FOREVER : tmp;
            propertyNegativeSet = true;
        }
        if (cachePolicy > 0) {
            tmp = getProperty(cacheStalePolicyProp,
                              cacheStalePolicyPropFallback);
            if (tmp != null) {
                staleCachePolicy = tmp;
            }
        }
    }

    private static Integer getProperty(String cachePolicyProp,
                                       String cachePolicyPropFallback)
    {
        return java.security.AccessController.doPrivileged(
                new PrivilegedAction<Integer>() {
                    public Integer run() {
                        try {
                            String tmpString = Security.getProperty(
                                    cachePolicyProp);
                            if (tmpString != null) {
                                return Integer.valueOf(tmpString);
                            }
                        } catch (NumberFormatException ignored) {
                            // Ignore
                        }

                        try {
                            String tmpString = System.getProperty(
                                    cachePolicyPropFallback);
                            if (tmpString != null) {
                                return Integer.decode(tmpString);
                            }
                        } catch (NumberFormatException ignored) {
                            // Ignore
                        }
                        return null;
                    }
                });
    }

    public static int get() {
        return cachePolicy;
    }

    public static int getStale() {
        return staleCachePolicy;
    }

    public static int getNegative() {
        return negativeCachePolicy;
    }

    /**
     * Sets the cache policy for successful lookups if the user has not
     * already specified a cache policy for it using a
     * command-property.
     * @param newPolicy the value in seconds for how long the lookup
     * should be cached
     */
    public static synchronized void setIfNotSet(int newPolicy) {
        /*
         * When setting the new value we may want to signal that the
         * cache should be flushed, though this doesn't seem strictly
         * necessary.
         */
        if (!propertySet) {
            checkValue(newPolicy, cachePolicy);
            cachePolicy = newPolicy;
        }
    }

    /**
     * Sets the cache policy for negative lookups if the user has not
     * already specified a cache policy for it using a
     * command-property.
     * @param newPolicy the value in seconds for how long the lookup
     * should be cached
     */
    public static void setNegativeIfNotSet(int newPolicy) {
        /*
         * When setting the new value we may want to signal that the
         * cache should be flushed, though this doesn't seem strictly
         * necessary.
         */
        if (!propertyNegativeSet) {
            // Negative caching does not seem to have any security
            // implications.
            // checkValue(newPolicy, negativeCachePolicy);
            // but we should normalize negative policy
            negativeCachePolicy = newPolicy < 0 ? FOREVER : newPolicy;
        }
    }

    private static void checkValue(int newPolicy, int oldPolicy) {
        /*
         * If malicious code gets a hold of this method, prevent
         * setting the cache policy to something laxer or some
         * invalid negative value.
         */
        if (newPolicy == FOREVER)
            return;

        if ((oldPolicy == FOREVER) ||
            (newPolicy < oldPolicy) ||
            (newPolicy < FOREVER)) {

            throw new
                SecurityException("can't make InetAddress cache more lax");
        }
    }
}
