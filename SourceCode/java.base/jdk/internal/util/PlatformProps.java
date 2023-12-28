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
package jdk.internal.util;

/**
 * The corresponding source file is generated by GensrcMisc.gmk for java.base.
 * @see OperatingSystem
 * @see Architecture
 */
class PlatformProps {

    // Name of the current OperatingSystem enum as substituted by the build
    static final String CURRENT_OS_STRING = "windows";

    // Precomputed booleans for each Operating System
    static final boolean TARGET_OS_IS_LINUX   = "windows" == "linux";
    static final boolean TARGET_OS_IS_MACOS   = "windows" == "macos";
    static final boolean TARGET_OS_IS_WINDOWS = "windows" == "windows";
    static final boolean TARGET_OS_IS_AIX     = "windows" == "aix";

    // The Architecture value for the current architecture
    static final String CURRENT_ARCH_STRING = "x64";

    // Architecture.is64Bit() uses this value
    static final int TARGET_ARCH_BITS = 64;

    // Architecture.isLittleEndian value from the build
    static final boolean TARGET_ARCH_LITTLE_ENDIAN = "little" == "little";

    // Precomputed booleans for each Architecture, shared with jdk.internal.util.Architecture
    // The variables are named to match the Architecture value names, and
    // the values chosen to match the build values.
    static final boolean TARGET_ARCH_IS_X64     = "x64" == "x64";
    static final boolean TARGET_ARCH_IS_X86     = "x64" == "x86";
    static final boolean TARGET_ARCH_IS_AARCH64 = "x64" == "aarch64";
    static final boolean TARGET_ARCH_IS_ARM     = "x64" == "arm";
    static final boolean TARGET_ARCH_IS_RISCV64 = "x64" == "riscv64";
    static final boolean TARGET_ARCH_IS_LOONGARCH64 = "x64" == "loongarch64";
    static final boolean TARGET_ARCH_IS_S390    = "x64" == "s390";
    static final boolean TARGET_ARCH_IS_PPC64   = "x64" == "ppc64";
    static final boolean TARGET_ARCH_IS_MIPSEL  = "x64" == "mipsel";
    static final boolean TARGET_ARCH_IS_MIPS64EL= "x64" == "mips64el";
}
