/*
 * Copyright (c) 2013, 2021, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.source.util;

import com.sun.source.doctree.DocCommentTree;
import com.sun.source.doctree.DocTree;
import com.sun.source.tree.CompilationUnitTree;

/**
 * Provides methods to obtain the position of a DocTree within a javadoc comment.
 * A position is defined as a simple character offset from the start of a
 * CompilationUnit where the first character is at offset 0.
 *
 * @since 1.8
 */
public interface DocSourcePositions extends SourcePositions {

    /**
     * Returns the starting position of the tree within the comment within the file.  If tree is not found within
     * file, or if the starting position is not available,
     * returns {@link javax.tools.Diagnostic#NOPOS}.
     * The given tree should be under the given comment tree, and the given documentation
     * comment tree should be returned from a {@link DocTrees#getDocCommentTree(com.sun.source.util.TreePath) }
     * for a tree under the given file.
     * The returned position must be at the start of the yield of this tree, that
     * is for any sub-tree of this tree, the following must hold:
     *
     * <p>
     * {@code getStartPosition(file, comment, tree) <= getStartPosition(file, comment, subtree)} or <br>
     * {@code getStartPosition(file, comment, tree) == NOPOS} or <br>
     * {@code getStartPosition(file, comment, subtree) == NOPOS}
     * </p>
     *
     * @param file compilation unit in which to find tree
     * @param comment the comment tree that encloses the tree for which the
     *                position is being sought
     * @param tree tree for which a position is sought
     * @return the start position of tree
     */
    long getStartPosition(CompilationUnitTree file, DocCommentTree comment, DocTree tree);

    /**
     * Returns the ending position of the tree within the comment within the file.  If tree is not found within
     * file, or if the ending position is not available,
     * returns {@link javax.tools.Diagnostic#NOPOS}.
     * The given tree should be under the given comment tree, and the given documentation
     * comment tree should be returned from a {@link DocTrees#getDocCommentTree(com.sun.source.util.TreePath) }
     * for a tree under the given file.
     * The returned position must be at the end of the yield of this tree,
     * that is for any sub-tree of this tree, the following must hold:
     *
     * <p>
     * {@code getEndPosition(file, comment, tree) >= getEndPosition(file, comment, subtree)} or <br>
     * {@code getEndPosition(file, comment, tree) == NOPOS} or <br>
     * {@code getEndPosition(file, comment, subtree) == NOPOS}
     * </p>
     *
     * In addition, the following must hold:
     *
     * <p>
     * {@code getStartPosition(file, comment, tree) <= getEndPosition(file, comment, tree)} or <br>
     * {@code getStartPosition(file, comment, tree) == NOPOS} or <br>
     * {@code getEndPosition(file, comment, tree) == NOPOS}
     * </p>
     *
     * @param file compilation unit in which to find tree
     * @param comment the comment tree that encloses the tree for which the
     *                position is being sought
     * @param tree tree for which a position is sought
     * @return the end position of tree
     */
    long getEndPosition(CompilationUnitTree file, DocCommentTree comment, DocTree tree);

}
