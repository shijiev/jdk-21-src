/*
 * Copyright (c) 2022, Oracle and/or its affiliates. All rights reserved.
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

/**
 * <h2>Specific components, transformations, and tools built on top of the
 * Classfile API</h2>
 *
 * The {@code jdk.internal.classfile.components} package contains specific
 * transformation components and utility classes helping to compose very complex
 * tasks with minimal effort.
 *
 * <h3>{@link ClassPrinter}</h3>
 * <p>
 * {@link ClassPrinter} is a helper class providing seamless export of a {@link
 * jdk.internal.classfile.ClassModel}, {@link jdk.internal.classfile.FieldModel},
 * {@link jdk.internal.classfile.MethodModel}, or {@link
 * jdk.internal.classfile.CodeModel} into human-readable structured text in
 * JSON, XML, or YAML format, or into a tree of traversable and printable nodes.
 * <p>
 * Primary purpose of {@link ClassPrinter} is to provide human-readable class
 * info for debugging, exception handling and logging purposes. The printed
 * class also conforms to a standard format to support automated offline
 * processing.
 * <p>
 * The most frequent use case is to simply print a class:
 * {@snippet lang="java" class="PackageSnippets" region="printClass"}
 * <p>
 * {@link ClassPrinter} allows to traverse tree of simple printable nodes to
 * hook custom printer:
 * {@snippet lang="java" class="PackageSnippets" region="customPrint"}
 * <p>
 * Another use case for {@link ClassPrinter} is to simplify writing of automated
 * tests:
 * {@snippet lang="java" class="PackageSnippets" region="printNodesInTest"}
 *
 * <h3>{@link ClassRemapper}</h3>
 * ClassRemapper is a {@link jdk.internal.classfile.ClassTransform}, {@link
 * jdk.internal.classfile.FieldTransform}, {@link
 * jdk.internal.classfile.MethodTransform} and {@link
 * jdk.internal.classfile.CodeTransform} deeply re-mapping all class references
 * in any form, according to given map or map function.
 * <p>
 * The re-mapping is applied to superclass, interfaces, all kinds of descriptors
 * and signatures, all attributes referencing classes in any form (including all
 * types of annotations), and to all instructions referencing to classes.
 * <p>
 * Primitive types and arrays are never subjects of mapping and are not allowed
 * targets of mapping.
 * <p>
 * Arrays of reference types are always decomposed, mapped as the base reference
 * types and composed back to arrays.
 * <p>
 * Single class remappigng example:
 * {@snippet lang="java" class="PackageSnippets" region="singleClassRemap"}
 * <p>
 * Remapping of all classes under specific package:
 * {@snippet lang="java" class="PackageSnippets" region="allPackageRemap"}
 *
 * <h3>{@link CodeLocalsShifter}</h3>
 * {@link CodeLocalsShifter} is a {@link jdk.internal.classfile.CodeTransform}
 * shifting locals to newly allocated positions to avoid conflicts during code
 * injection. Locals pointing to the receiver or to method arguments slots are
 * never shifted. All locals pointing beyond the method arguments are re-indexed
 * in order of appearance.
 * <p>
 * Sample of code transformation shifting all locals in all methods:
 * {@snippet lang="java" class="PackageSnippets" region="codeLocalsShifting"}
 *
 * <h3>{@link CodeRelabeler}</h3>
 * {@link CodeRelabeler} is a {@link jdk.internal.classfile.CodeTransform}
 * replacing all occurences of {@link jdk.internal.classfile.Label} in the
 * transformed code with new instances.
 * All {@link jdk.internal.classfile.instruction.LabelTarget} instructions are
 * adjusted accordingly.
 * Relabeled code graph is identical to the original.
 * <p>
 * Primary purpose of {@link CodeRelabeler} is for repeated injections of the
 * same code blocks.
 * Repeated injection of the same code block must be relabeled, so each instance
 * of {@link jdk.internal.classfile.Label} is bound in the target bytecode
 * exactly once.
 * <p>
 * Sample transformation relabeling all methods:
 * {@snippet lang="java" class="PackageSnippets" region="codeRelabeling"}
 *
 * <h3>Class Instrumentation Sample</h3>
 * Following snippet is sample composition of {@link ClassRemapper}, {@link
 * CodeLocalsShifter} and {@link CodeRelabeler} into fully functional class
 * instrumenting transformation:
 * {@snippet lang="java" class="PackageSnippets" region="classInstrumentation"}
 */
package jdk.internal.classfile.components;
