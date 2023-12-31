/*
 * Copyright (c) 2018, 2022, Oracle and/or its affiliates. All rights reserved.
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
 * Classes and interfaces to represent <em>nominal descriptors</em> for run-time
 * entities such as classes or method handles, and classfile entities such as
 * constant pool entries or {@code invokedynamic} call sites.  These classes
 * are suitable for use in bytecode reading and writing APIs, {@code invokedynamic}
 * bootstraps, bytecode intrinsic APIs, and compile-time or link-time program
 * analysis tools.
 *
 * <p>Every API that reads and writes bytecode instructions needs to model the
 * operands to these instructions and other classfile structures (such as entries
 * in the bootstrap methods table or stack maps, which frequently reference
 * entries in the classfile constant pool.) Such entries can denote values of
 * fundamental types, such as strings or integers; parts of a program, such as
 * classes or method handles; or values of arbitrary user-defined types.  The
 * {@link java.lang.constant.ConstantDesc} hierarchy provides a representation of
 * constant pool entries in nominal form that is convenient for APIs to model
 * operands of bytecode instructions.
 *
 * <h2><a id="nominal"></a>Nominal Descriptors</h2>
 *
 * <p>A {@link java.lang.constant.ConstantDesc} is a description of a constant
 * value.  Such a description is the <em>nominal form</em> of the constant value;
 * it is not the value itself, but rather a "recipe" for describing the value,
 * storing the value in a constant pool entry, or reconstituting the value given
 * a class loading context.  Every {@link java.lang.constant.ConstantDesc}
 * knows how to <em>resolve</em> itself -- compute the value that it describes --
 * via {@link java.lang.constant.ConstantDesc#resolveConstantDesc(java.lang.invoke.MethodHandles.Lookup) ConstantDesc.resolveConstantDesc}.
 * This allows an API which accepts {@link java.lang.constant.ConstantDesc}
 * objects to evaluate them reflectively, provided that the classes and methods
 * referenced in their nominal description are present and accessible.
 *
 * <p>The subtypes of {@link java.lang.constant.ConstantDesc} describe various kinds
 * of constant values.  For each type of loadable constant pool entry defined in JVMS {@jvms 4.4},
 * there is a corresponding subtype of {@link java.lang.constant.ConstantDesc}:
 * {@link java.lang.constant.ClassDesc}, {@link java.lang.constant.MethodTypeDesc},
 * {@link java.lang.constant.DirectMethodHandleDesc}, {@link java.lang.String},
 * {@link java.lang.Integer}, {@link java.lang.Long}, {@link java.lang.Float},
 * {@link java.lang.Double}, and {@link java.lang.constant.DynamicConstantDesc}.  These classes
 * provide type-specific accessor methods to extract the nominal information for
 * that kind of constant.  When a bytecode-writing API encounters a {@link java.lang.constant.ConstantDesc},
 * it should examine it to see which of these types it is, cast it, extract
 * its nominal information, and generate the corresponding entry to the constant pool.
 * When a bytecode-reading API encounters a constant pool entry, it can
 * convert it to the appropriate type of nominal descriptor.  For dynamic
 * constants, bytecode-reading APIs may wish to use the factory
 * {@link java.lang.constant.DynamicConstantDesc#ofCanonical(DirectMethodHandleDesc, java.lang.String, ClassDesc, ConstantDesc[]) DynamicConstantDesc.ofCanonical},
 * which will inspect the bootstrap and, for well-known bootstraps, return
 * a more specific subtype of {@link java.lang.constant.DynamicConstantDesc}, such as
 * {@link java.lang.Enum.EnumDesc}.
 *
 * <p>Another way to obtain the nominal description of a value is to ask the value
 * itself.  A {@link java.lang.constant.Constable} is a type whose values
 * can describe themselves in nominal form as a {@link java.lang.constant.ConstantDesc}.
 * Fundamental types such as {@link java.lang.String} and {@link java.lang.Class}
 * implement {@link java.lang.constant.Constable}, as can user-defined
 * classes.  Entities that generate classfiles (such as compilers) can introspect
 * over constable objects to obtain a more efficient way to represent their values
 * in classfiles.
 *
 * <p>This package also includes {@link java.lang.constant.DynamicCallSiteDesc},
 * which represents a (non-loadable) {@code Constant_InvokeDynamic_info} constant
 * pool entry.  It describes the bootstrap method, invocation name and type,
 * and bootstrap arguments associated with an {@code invokedynamic} instruction.
 * It is also suitable for describing {@code invokedynamic} call sites in bytecode
 * reading and writing APIs.
 *
 * <p>Other members of this package are {@link ModuleDesc}
 * and  {@link PackageDesc}. They represent module and package
 * info structures, suitable for describing modules and their content in bytecode
 * reading and writing APIs.
 *
 * @jvms 4.4 The Constant Pool
 *
 * @since 12
 */
package java.lang.constant;
