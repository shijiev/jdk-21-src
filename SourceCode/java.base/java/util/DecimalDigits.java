/*
 * Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
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

package java.util;

import java.lang.invoke.MethodHandle;

import jdk.internal.vm.annotation.Stable;

/**
 * Digits class for decimal digits.
 *
 * @since 21
 */
final class DecimalDigits implements Digits {
    @Stable
    private static final short[] DIGITS;

    /**
     * Singleton instance of DecimalDigits.
     */
    static final Digits INSTANCE = new DecimalDigits();

    static {
        short[] digits = new short[10 * 10];

        for (int i = 0; i < 10; i++) {
            short hi = (short) ((i + '0') << 8);

            for (int j = 0; j < 10; j++) {
                short lo = (short) (j + '0');
                digits[i * 10 + j] = (short) (hi | lo);
            }
        }

        DIGITS = digits;
    }

    /**
     * Constructor.
     */
    private DecimalDigits() {
    }

    @Override
    public int digits(long value, byte[] buffer, int index,
                      MethodHandle putCharMH) throws Throwable {
        boolean negative = value < 0;
        if (!negative) {
            value = -value;
        }

        long q;
        int r;
        while (value <= Integer.MIN_VALUE) {
            q = value / 100;
            r = (int)((q * 100) - value);
            value = q;
            int digits = DIGITS[r];

            putCharMH.invokeExact(buffer, --index, digits & 0xFF);
            putCharMH.invokeExact(buffer, --index, digits >> 8);
        }

        int iq, ivalue = (int)value;
        while (ivalue <= -100) {
            iq = ivalue / 100;
            r = (iq * 100) - ivalue;
            ivalue = iq;
            int digits = DIGITS[r];
            putCharMH.invokeExact(buffer, --index, digits & 0xFF);
            putCharMH.invokeExact(buffer, --index, digits >> 8);
        }

        if (ivalue < 0) {
            ivalue = -ivalue;
        }

        int digits = DIGITS[ivalue];
        putCharMH.invokeExact(buffer, --index, digits & 0xFF);

        if (9 < ivalue) {
            putCharMH.invokeExact(buffer, --index, digits >> 8);
        }

        if (negative) {
            putCharMH.invokeExact(buffer, --index, (int)'-');
        }

        return index;
    }

    @Override
    public int size(long value) {
        boolean negative = value < 0;
        int sign = negative ? 1 : 0;

        if (!negative) {
            value = -value;
        }

        long precision = -10;
        for (int i = 1; i < 19; i++) {
            if (value > precision)
                return i + sign;

            precision = 10 * precision;
        }

        return 19 + sign;
    }
}
