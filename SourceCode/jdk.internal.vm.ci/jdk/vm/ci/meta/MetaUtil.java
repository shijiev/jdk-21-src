/*
 * Copyright (c) 2012, 2023, Oracle and/or its affiliates. All rights reserved.
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
 */
package jdk.vm.ci.meta;

/**
 * Miscellaneous collection of utility methods used by {@code jdk.vm.ci.meta} and its clients.
 */
public class MetaUtil {

    public static final char PACKAGE_SEPARATOR_INTERNAL = '/';
    public static final char HIDDEN_SEPARATOR_INTERNAL = '.';
    public static final char PACKAGE_SEPARATOR_JAVA = HIDDEN_SEPARATOR_INTERNAL;
    public static final char HIDDEN_SEPARATOR_JAVA = PACKAGE_SEPARATOR_INTERNAL;

    /**
     * Extends the functionality of {@link Class#getSimpleName()} to include a non-empty string for
     * anonymous and local classes.
     *
     * @param clazz the class for which the simple name is being requested
     * @param withEnclosingClass specifies if the returned name should be qualified with the name(s)
     *            of the enclosing class/classes of {@code clazz} (if any). This option is ignored
     *            if {@code clazz} denotes an anonymous or local class.
     * @return the simple name
     */
    public static String getSimpleName(Class<?> clazz, boolean withEnclosingClass) {
        final String simpleName = safeSimpleName(clazz);
        if (simpleName.length() != 0) {
            if (withEnclosingClass) {
                String prefix = "";
                Class<?> enclosingClass = clazz;
                while ((enclosingClass = enclosingClass.getEnclosingClass()) != null) {
                    prefix = safeSimpleName(enclosingClass) + "." + prefix;
                }
                return prefix + simpleName;
            }
            return simpleName;
        }
        // Must be an anonymous or local class
        final String name = clazz.getName();
        int index = name.indexOf('$');
        if (index == -1) {
            return name;
        }
        index = name.lastIndexOf('.', index);
        if (index == -1) {
            return name;
        }
        return name.substring(index + 1);
    }

    private static String safeSimpleName(Class<?> clazz) {
        try {
            return clazz.getSimpleName();
        } catch (InternalError e) {
            // Scala inner class names do not always start with '$',
            // causing Class.getSimpleName to throw an InternalError
            Class<?> enclosingClass = clazz.getEnclosingClass();
            String fqn = clazz.getName();
            if (enclosingClass == null) {
                // Should never happen given logic in
                // Class.getSimpleName but best be safe
                return fqn;
            }
            String enclosingFQN = enclosingClass.getName();
            int length = fqn.length();
            if (enclosingFQN.length() >= length) {
                // Should also never happen
                return fqn;
            }
            return fqn.substring(enclosingFQN.length());
        }
    }

    /**
     * Hidden classes have {@code /} characters in their internal names and {@code .} characters in their names returned
     * by {@link Class#getName()} that are not package separators.
     * These are distinguished by being followed by a character that is not a
     * {@link Character#isJavaIdentifierStart(char)} (e.g.,
     * "jdk.vm.ci.runtime.test.TypeUniverse$$Lambda/869601985").
     *
     * @param name the name to perform the replacements on
     * @param packageSeparator the {@link Character} used as the package separator, e.g. {@code /} in internal form
     * @param hiddenSeparator the {@link Character} used as the hidden class separator, e.g. {@code .} in internal form
     */
    private static String replacePackageAndHiddenSeparators(String name, Character packageSeparator, Character hiddenSeparator) {
        int index = name.indexOf(hiddenSeparator);   // check if it's a hidden class
        int length = name.length();
        StringBuilder buf = new StringBuilder(length);
        if (index < 0) {
            buf.append(name.replace(packageSeparator, hiddenSeparator));
        } else {
            buf.append(name.substring(0, index).replace(packageSeparator, hiddenSeparator));
            buf.append(packageSeparator);
            buf.append(name.substring(index + 1));
        }
        return buf.toString();
    }

    /**
     * Converts a type name in internal form to an external form.
     *
     * @param name the internal name to convert
     * @param qualified whether the returned name should be qualified with the package name
     * @param classForNameCompatible specifies if the returned name for array types should be in
     *            {@link Class#forName(String)} format (e.g., {@code "[Ljava.lang.Object;"},
     *            {@code "[[I"}) or in Java source code format (e.g., {@code "java.lang.Object[]"},
     *            {@code "int[][]"} ).
     */
    public static String internalNameToJava(String name, boolean qualified, boolean classForNameCompatible) {
        switch (name.charAt(0)) {
            case 'L': {
                String type = name.substring(1, name.length() - 1);
                String result = replacePackageAndHiddenSeparators(type, PACKAGE_SEPARATOR_INTERNAL, HIDDEN_SEPARATOR_INTERNAL);
                if (!qualified) {
                    final int lastDot = result.lastIndexOf(HIDDEN_SEPARATOR_INTERNAL);
                    if (lastDot != -1) {
                        result = result.substring(lastDot + 1);
                    }
                }
                return result;
            }
            case '[':
                if (classForNameCompatible) {
                    return replacePackageAndHiddenSeparators(name, PACKAGE_SEPARATOR_INTERNAL, HIDDEN_SEPARATOR_INTERNAL);
                } else {
                    return internalNameToJava(name.substring(1), qualified, false) + "[]";
                }
            default:
                if (name.length() != 1) {
                    throw new IllegalArgumentException("Illegal internal name: " + name);
                }
                return JavaKind.fromPrimitiveOrVoidTypeChar(name.charAt(0)).getJavaName();
        }
    }

    /**
     * Convenient shortcut for calling
     * {@link #appendLocation(StringBuilder, ResolvedJavaMethod, int)} without having to supply a
     * {@link StringBuilder} instance and convert the result to a string.
     */
    public static String toLocation(ResolvedJavaMethod method, int bci) {
        return appendLocation(new StringBuilder(), method, bci).toString();
    }

    /**
     * Appends a string representation of a location specified by a given method and bci to a given
     * {@link StringBuilder}. If a stack trace element with a non-null file name and non-negative
     * line number is {@linkplain ResolvedJavaMethod#asStackTraceElement(int) available} for the
     * given method, then the string returned is the {@link StackTraceElement#toString()} value of
     * the stack trace element, suffixed by the bci location. For example:
     *
     * <pre>
     *     java.lang.String.valueOf(String.java:2930) [bci: 12]
     * </pre>
     *
     * Otherwise, the string returned is the value of applying {@link JavaMethod#format(String)}
     * with the format string {@code "%H.%n(%p)"}, suffixed by the bci location. For example:
     *
     * <pre>
     *     java.lang.String.valueOf(int) [bci: 12]
     * </pre>
     *
     * @param sb
     * @param method
     * @param bci
     */
    public static StringBuilder appendLocation(StringBuilder sb, ResolvedJavaMethod method, int bci) {
        if (method != null) {
            StackTraceElement ste = method.asStackTraceElement(bci);
            if (ste.getFileName() != null && ste.getLineNumber() > 0) {
                sb.append(ste);
            } else {
                sb.append(method.format("%H.%n(%p)"));
            }
        } else {
            sb.append("Null method");
        }
        return sb.append(" [bci: ").append(bci).append(']');
    }

    static void appendProfile(StringBuilder buf, AbstractJavaProfile<?, ?> profile, int bci, String type, String sep) {
        if (profile != null) {
            AbstractProfiledItem<?>[] pitems = profile.getItems();
            if (pitems != null) {
                buf.append(String.format("%s@%d:", type, bci));
                for (int j = 0; j < pitems.length; j++) {
                    AbstractProfiledItem<?> pitem = pitems[j];
                    buf.append(String.format(" %.6f (%s)%s", pitem.getProbability(), pitem.getItem(), sep));
                }
                if (profile.getNotRecordedProbability() != 0) {
                    buf.append(String.format(" %.6f <other %s>%s", profile.getNotRecordedProbability(), type, sep));
                } else {
                    buf.append(String.format(" <no other %s>%s", type, sep));
                }
            }
        }
    }

    /**
     * Converts a Java source-language class name into the internal form.
     *
     * @param className the class name
     * @return the internal name form of the class name
     */
    public static String toInternalName(String className) {
        if (className.startsWith("[")) {
            /* Already in the correct array style. */
            return replacePackageAndHiddenSeparators(className, PACKAGE_SEPARATOR_JAVA, HIDDEN_SEPARATOR_JAVA);
        }

        StringBuilder result = new StringBuilder();
        String base = className;
        while (base.endsWith("[]")) {
            result.append("[");
            base = base.substring(0, base.length() - 2);
        }

        switch (base) {
            case "boolean":
                result.append("Z");
                break;
            case "byte":
                result.append("B");
                break;
            case "short":
                result.append("S");
                break;
            case "char":
                result.append("C");
                break;
            case "int":
                result.append("I");
                break;
            case "float":
                result.append("F");
                break;
            case "long":
                result.append("J");
                break;
            case "double":
                result.append("D");
                break;
            case "void":
                result.append("V");
                break;
            default:
                result.append("L")
                        .append(replacePackageAndHiddenSeparators(base, PACKAGE_SEPARATOR_JAVA, HIDDEN_SEPARATOR_JAVA))
                        .append(";");
                break;
        }
        return result.toString();
    }

    /**
     * Gets a string representation of an object based soley on its class and its
     * {@linkplain System#identityHashCode(Object) identity hash code}. This avoids and calls to
     * virtual methods on the object such as {@link Object#hashCode()}.
     */
    public static String identityHashCodeString(Object obj) {
        if (obj == null) {
            return "null";
        }
        return obj.getClass().getName() + "@" + System.identityHashCode(obj);
    }
}
