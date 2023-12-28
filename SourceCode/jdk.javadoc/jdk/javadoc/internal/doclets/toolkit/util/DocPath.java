/*
 * Copyright (c) 1998, 2022, Oracle and/or its affiliates. All rights reserved.
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

package jdk.javadoc.internal.doclets.toolkit.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Abstraction for immutable relative paths.
 * Paths always use '/' as a separator, and never begin or end with '/'.
 */
public class DocPath {
    private final String path;

    /** The empty path. */
    public static final DocPath empty = new DocPath("");

    /** The empty path. */
    public static final DocPath parent = new DocPath("..");

    /**
     * Creates a path from a string.
     * @param p the string
     * @return the path
     */
    public static DocPath create(String p) {
        return (p == null) || p.isEmpty() ? empty : new DocPath(p);
    }

    protected DocPath(String p) {
        path = (p.endsWith("/") ? p.substring(0, p.length() - 1) : p);
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof DocPath dp) && path.equals(dp.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    public DocPath basename() {
        int sep = path.lastIndexOf("/");
        return (sep == -1) ? this : new DocPath(path.substring(sep + 1));
    }

    public DocPath parent() {
        int sep = path.lastIndexOf("/");
        return (sep == -1) ? empty : new DocPath(path.substring(0, sep));
    }

    /**
     * Returns the path formed by appending the specified string to the current path.
     * @param p the string
     * @return the path
     */
    public DocPath resolve(String p) {
        if (p == null || p.isEmpty())
            return this;
        if (path.isEmpty())
            return new DocPath(p);
        return new DocPath(path + "/" + p);
    }

    /**
     * Returns the path by appending the specified path to the current path.
     * @param p the path
     * @return the path
     */
    public DocPath resolve(DocPath p) {
        if (p == null || p.isEmpty())
            return this;
        if (path.isEmpty())
            return p;
        return new DocPath(path + "/" + p.getPath());
    }

    /**
     * Return the inverse path for this path.
     * For example, if the path is a/b/c, the inverse path is ../../..
     * @return the path
     */
    public DocPath invert() {
        return new DocPath(path.replaceAll("[^/]+", ".."));
    }

    /**
     * Returns the path formed by eliminating empty components,
     * '.' components, and redundant name/.. components.
     * @return the path
     */
    public DocPath normalize() {
        return path.isEmpty()
                ? this
                : new DocPath(String.join("/", normalize(path)));
    }

    private static List<String> normalize(String path) {
        return normalize(Arrays.asList(path.split("/")));
    }

    private static List<String> normalize(List<String> parts) {
        if (parts.stream().noneMatch(s -> s.isEmpty() || s.equals(".") || s.equals(".."))) {
            return parts;
        }
        List<String> normalized = new ArrayList<>();
        for (String part : parts) {
            switch (part) {
                case "":
                case ".":
                    break;
                case "..":
                    int n = normalized.size();
                    if (n > 0 && !normalized.get(n - 1).equals("..")) {
                        normalized.remove(n - 1);
                    } else {
                        normalized.add(part);
                    }
                    break;
                default:
                    normalized.add(part);
            }
        }
        return normalized;
    }

    /**
     * Normalize and relativize a path against this path,
     * assuming that this path is for a file (not a directory),
     * in which the other path will appear.
     *
     * @param other the path to be relativized.
     * @return the simplified path
     */
    public DocPath relativize(DocPath other) {
        if (other == null || other.path.isEmpty()) {
            return this;
        }

        if (path.isEmpty()) {
            return other;
        }

        List<String> originParts = normalize(path);
        int sep = path.lastIndexOf("/");
        List<String> destParts = sep == -1
                ? normalize(other.path)
                : normalize(path.substring(0, sep + 1) + other.path);
        int common = 0;
        while (common < originParts.size()
                && common < destParts.size()
                && originParts.get(common).equals(destParts.get(common))) {
            common++;
        }

        List<String> newParts;
        if (common == originParts.size()) {
            newParts = destParts.subList(common, destParts.size());
        } else {
            newParts = new ArrayList<>();
            newParts.addAll(Collections.nCopies(originParts.size() - common - 1, ".."));
            newParts.addAll(destParts.subList(common, destParts.size()));
        }
        return new DocPath(String.join("/", newParts));
    }

    /**
     * Return true if this path is empty.
     * @return true if this path is empty
     */
    public boolean isEmpty() {
        return path.isEmpty();
    }

    /**
     * Creates a DocLink formed from this path and a fragment identifier.
     * @param fragment the fragment
     * @return the link
     */
    public DocLink fragment(String fragment) {
        return new DocLink(path, fragment);
    }

    /**
     * Returns this path as a string.
     * @return the path
     */
    // This is provided instead of using toString() to help catch
    // unintended use of toString() in string concatenation sequences.
    public String getPath() {
        return path;
    }
}
