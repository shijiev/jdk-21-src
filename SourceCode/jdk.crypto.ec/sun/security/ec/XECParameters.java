/*
 * Copyright (c) 2018, 2020, Oracle and/or its affiliates. All rights reserved.
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

package sun.security.ec;

import java.io.IOException;
import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.NamedParameterSpec;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

import sun.security.util.KnownOIDs;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AlgorithmId;

public class XECParameters {

    static final XECParameters X25519;
    static final XECParameters X448;

    static ParametersMap<XECParameters> namedParams = new ParametersMap<>();

    // Naming/identification parameters
    private final ObjectIdentifier oid;
    private final String name;

    // Curve/field parameters
    private final int bits;
    private final BigInteger p;
    private final int logCofactor;
    private final int a24;
    private final byte basePoint;

    /**
     *
     * Construct an object holding the supplied parameters. No parameters are
     * checked, so this method always succeeds. This method supports
     * Montgomery curves of the form y^2 = x^3 + ax^2 + x.
     *
     * @param bits The number of relevant bits in a public/private key.
     * @param p The prime that defines the finite field.
     * @param a24 The value of (a - 2) / 4, where a is the second-degree curve
     *            coefficient.
     * @param basePoint The point that generates the desired group
     * @param logCofactor The base-2 logarithm of the cofactor of the curve
     * @param oid
     * @param name
     */
    public XECParameters(int bits, BigInteger p, int a24,
                         byte basePoint, int logCofactor,
                         ObjectIdentifier oid, String name) {

        this.bits = bits;
        this.logCofactor = logCofactor;
        this.p = p;
        this.a24 = a24;
        this.basePoint = basePoint;
        this.oid = oid;
        this.name = name;

    }

    public int getBits() {
        return bits;
    }
    public int getBytes() {
        return (bits + 7) / 8;
    }
    public int getLogCofactor() {
        return logCofactor;
    }
    public BigInteger getP() {
        return p;
    }
    public int getA24() {
        return a24;
    }
    public byte getBasePoint() {
        return basePoint;
    }
    public ObjectIdentifier getOid() {
        return oid;
    }
    public String getName() {
        return name;
    }

    static {
        final BigInteger TWO = BigInteger.valueOf(2);

        Map<Integer, XECParameters> bySize = new HashMap<>();
        Map<ObjectIdentifier, XECParameters> byOid = new HashMap<>();
        Map<String, XECParameters> byName = new HashMap<>();

        // set up X25519
        BigInteger p2 = TWO.pow(255).subtract(BigInteger.valueOf(19));
        X25519 = addParameters(255, p2, 121665, (byte)0x09, 3,
            KnownOIDs.X25519, NamedParameterSpec.X25519.getName());

        // set up X448
        BigInteger p4 = TWO.pow(448).subtract(TWO.pow(224))
            .subtract(BigInteger.ONE);
        X448 = addParameters(448, p4, 39081, (byte)0x05, 2,
            KnownOIDs.X448, NamedParameterSpec.X448.getName());

        namedParams.fix();
    }

    private static XECParameters addParameters(int bits, BigInteger p, int a24,
        byte basePoint, int logCofactor, KnownOIDs koid, String name) {

        ObjectIdentifier oid = ObjectIdentifier.of(koid);
        XECParameters params =
            new XECParameters(bits, p, a24, basePoint, logCofactor, oid, name);
        namedParams.put(name.toLowerCase(), oid, bits, params);
        return params;
    }

    boolean oidEquals(XECParameters other) {
        return oid.equals(other.getOid());
    }


    public static
    <T extends Throwable>
    XECParameters getBySize(Function<String, T> exception,
                            int size) throws T {

        return namedParams.getBySize(exception, size);
    }

    public static
    <T extends Throwable>
    XECParameters get(Function<String, T> exception,
                      AlgorithmId algId) throws T {

        return namedParams.get(exception, algId);
    }

    public static
    <T extends Throwable>
    XECParameters get(Function<String, T> exception,
                      AlgorithmParameterSpec params) throws T {

        return namedParams.get(exception, params);
    }
}

