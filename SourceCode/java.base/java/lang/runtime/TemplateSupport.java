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

package java.lang.runtime;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import jdk.internal.access.JavaLangAccess;
import jdk.internal.access.JavaTemplateAccess;
import jdk.internal.access.SharedSecrets;

/**
 * This class provides runtime support for string templates. The methods within
 * are intended for internal use only.
 *
 * @since 21
 *
 * Warning: This class is part of PreviewFeature.Feature.STRING_TEMPLATES.
 *          Do not rely on its availability.
 */
final class TemplateSupport implements JavaTemplateAccess {

    /**
     * Private constructor.
     */
    private TemplateSupport() {
    }

    static {
        SharedSecrets.setJavaTemplateAccess(new TemplateSupport());
    }

    private static final JavaLangAccess JLA = SharedSecrets.getJavaLangAccess();

    /**
     * Returns a StringTemplate composed from fragments and values.
     *
     * @implSpec The {@code fragments} list size must be one more that the
     * {@code values} list size.
     *
     * @param fragments list of string fragments
     * @param values    list of expression values
     *
     * @return StringTemplate composed from fragments and values
     *
     * @throws IllegalArgumentException if fragments list size is not one more
     *         than values list size
     * @throws NullPointerException if fragments is null or values is null or if any fragment is null.
     *
     * @implNote Contents of both lists are copied to construct immutable lists.
     */
    @Override
    public StringTemplate of(List<String> fragments, List<?> values) {
        return StringTemplateImplFactory.newStringTemplate(fragments, values);
    }

    /**
     * Creates a string that interleaves the elements of values between the
     * elements of fragments.
     *
     * @param fragments  list of String fragments
     * @param values     list of expression values
     *
     * @return String interpolation of fragments and values
     */
    @Override
    public String interpolate(List<String> fragments, List<?> values) {
        int fragmentsSize = fragments.size();
        int valuesSize = values.size();
        if (fragmentsSize == 1) {
            return fragments.get(0);
        }
        int size = fragmentsSize + valuesSize;
        String[] strings = new String[size];
        int i = 0, j = 0;
        for (; j < valuesSize; j++) {
            strings[i++] = fragments.get(j);
            strings[i++] = String.valueOf(values.get(j));
        }
        strings[i] = fragments.get(j);
        return JLA.join("", "", "", strings, size);
    }

    /**
     * Combine one or more {@link StringTemplate StringTemplates} to produce a combined {@link StringTemplate}.
     * {@snippet :
     * StringTemplate st = StringTemplate.combine("\{a}", "\{b}", "\{c}");
     * assert st.interpolate().equals("\{a}\{b}\{c}");
     * }
     *
     * @param sts  zero or more {@link StringTemplate}
     *
     * @return combined {@link StringTemplate}
     *
     * @throws NullPointerException if sts is null or if any element of sts is null
     */
    @Override
    public StringTemplate combine(StringTemplate... sts) {
        Objects.requireNonNull(sts, "sts must not be null");
        if (sts.length == 0) {
            return StringTemplate.of("");
        } else if (sts.length == 1) {
            return Objects.requireNonNull(sts[0], "string templates should not be null");
        }
        int size = 0;
        for (StringTemplate st : sts) {
            Objects.requireNonNull(st, "string templates should not be null");
            size += st.values().size();
        }
        String[] combinedFragments = new String[size + 1];
        Object[] combinedValues = new Object[size];
        combinedFragments[0] = "";
        int fragmentIndex = 1;
        int valueIndex = 0;
        for (StringTemplate st : sts) {
            Iterator<String> iterator = st.fragments().iterator();
            combinedFragments[fragmentIndex - 1] += iterator.next();
            while (iterator.hasNext()) {
                combinedFragments[fragmentIndex++] = iterator.next();
            }
            for (Object value : st.values()) {
                combinedValues[valueIndex++] = value;
            }
        }
        return StringTemplateImplFactory.newTrustedStringTemplate(combinedFragments, combinedValues);
    }

}
