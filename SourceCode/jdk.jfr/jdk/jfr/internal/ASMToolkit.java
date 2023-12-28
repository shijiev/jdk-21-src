/*
 * Copyright (c) 2016, 2022, Oracle and/or its affiliates. All rights reserved.
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

package jdk.jfr.internal;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.util.TraceClassVisitor;
import jdk.jfr.ValueDescriptor;

final class ASMToolkit {
    public static final Type TYPE_STRING = Type.getType(String.class);
    private static final Type TYPE_THREAD = Type.getType(Thread.class);
    private static final Type TYPE_CLASS = Type.getType(Class.class);

    public static Type toType(ValueDescriptor v) {
        return switch (v.getTypeName()) {
            case "byte" -> Type.BYTE_TYPE;
            case "short" -> Type.SHORT_TYPE;
            case "int" ->  Type.INT_TYPE;
            case "long" ->Type.LONG_TYPE;
            case "double" -> Type.DOUBLE_TYPE;
            case "float" -> Type.FLOAT_TYPE;
            case "char" -> Type.CHAR_TYPE;
            case "boolean" -> Type.BOOLEAN_TYPE;
            case "java.lang.String" -> TYPE_STRING;
            case "java.lang.Thread" -> TYPE_THREAD;
            case "java.lang.Class" -> TYPE_CLASS;
            default -> throw new Error("Not a valid type " + v.getTypeName());
        };
    }

    /**
     * Converts "int" into "I" and "java.lang.String" into "Ljava/lang/String;"
     *
     * @param typeName
     *            type
     *
     * @return descriptor
     */
    public static String getDescriptor(String typeName) {
        return switch (typeName) {
            case "int" -> "I";
            case "long" -> "J";
            case "boolean" -> "Z";
            case "float" -> "F";
            case "double" -> "D";
            case "short" -> "S";
            case "char" -> "C";
            case "byte" -> "B";
            default -> Type.getObjectType(getInternalName(typeName)).getDescriptor();
        };
    }

    /**
     * Converts java.lang.String into java/lang/String
     *
     * @param className
     *
     * @return internal name
     */
    public static String getInternalName(String className) {
        return className.replace(".", "/");
    }

    public static void logASM(String className, byte[] bytes) {
        Logger.log(LogTag.JFR_SYSTEM_BYTECODE, LogLevel.INFO, "Generated bytecode for class " + className);
        if (Logger.shouldLog(LogTag.JFR_SYSTEM_BYTECODE, LogLevel.TRACE)) {
            ClassReader cr = new ClassReader(bytes);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintWriter w = new PrintWriter(baos);
            w.println("Bytecode:");
            cr.accept(new TraceClassVisitor(w), 0);
            Logger.log(LogTag.JFR_SYSTEM_BYTECODE, LogLevel.TRACE, baos.toString());
        };
    }
}