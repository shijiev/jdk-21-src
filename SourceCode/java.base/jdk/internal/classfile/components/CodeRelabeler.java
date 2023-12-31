/*
 * Copyright (c) 2022, 2023, Oracle and/or its affiliates. All rights reserved.
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
package jdk.internal.classfile.components;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import jdk.internal.classfile.CodeBuilder;
import jdk.internal.classfile.CodeTransform;
import jdk.internal.classfile.Label;
import jdk.internal.classfile.impl.CodeRelabelerImpl;

/**
 * A code relabeler is a {@link CodeTransform} replacing all occurrences
 * of {@link jdk.internal.classfile.Label} in the transformed code with new instances.
 * All {@link jdk.internal.classfile.instruction.LabelTarget} instructions are adjusted accordingly.
 * Relabeled code graph is identical to the original.
 * <p>
 * Primary purpose of CodeRelabeler is for repeated injections of the same code blocks.
 * Repeated injection of the same code block must be relabeled, so each instance of
 * {@link jdk.internal.classfile.Label} is bound in the target bytecode exactly once.
 */
public sealed interface CodeRelabeler extends CodeTransform permits CodeRelabelerImpl {

    /**
     * Creates a new instance of CodeRelabeler.
     * @return a new instance of CodeRelabeler
     */
    static CodeRelabeler of() {
        return of(new IdentityHashMap<>());
    }

    /**
     * Creates a new instance of CodeRelabeler storing the label mapping into the provided map.
     * @param map label map actively used for relabeling
     * @return a new instance of CodeRelabeler
     */
    static CodeRelabeler of(Map<Label, Label> map) {
        return of((l, cob) -> map.computeIfAbsent(l, ll -> cob.newLabel()));
    }

    /**
     * Creates a new instance of CodeRelabeler using provided {@link java.util.function.BiFunction}
     * to re-label the code.
     * @param mapFunction
     * @return a new instance of CodeRelabeler
     */
    static CodeRelabeler of(BiFunction<Label, CodeBuilder, Label> mapFunction) {
        return new CodeRelabelerImpl(mapFunction);
    }

    /**
     * Access method to internal re-labeling function.
     * @param label source label
     * @param codeBuilder builder to create new labels
     * @return target label
     */
    Label relabel(Label label, CodeBuilder codeBuilder);
}
