/*
 * Copyright (c) 2005, 2022, Oracle and/or its affiliates. All rights reserved.
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
package com.sun.management.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;
import javax.management.ObjectName;
import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.HotSpotDiagnosticMXBean.ThreadDumpFormat;
import com.sun.management.VMOption;
import jdk.internal.vm.ThreadDumper;
import sun.management.Util;

/**
 * Implementation of the diagnostic MBean for Hotspot VM.
 */
public class HotSpotDiagnostic implements HotSpotDiagnosticMXBean {
    public HotSpotDiagnostic() {
    }

    @Override
    public void dumpHeap(String outputFile, boolean live) throws IOException {

        String propertyName = "jdk.management.heapdump.allowAnyFileSuffix";
        PrivilegedAction<Boolean> pa = () -> Boolean.parseBoolean(System.getProperty(propertyName, "false"));
        @SuppressWarnings("removal")
        boolean allowAnyFileSuffix = AccessController.doPrivileged(pa);
        if (!allowAnyFileSuffix && !outputFile.endsWith(".hprof")) {
            throw new IllegalArgumentException("heapdump file must have .hprof extension");
        }

        @SuppressWarnings("removal")
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkWrite(outputFile);
            Util.checkControlAccess();
        }

        dumpHeap0(outputFile, live);
    }

    private native void dumpHeap0(String outputFile, boolean live) throws IOException;

    @Override
    public List<VMOption> getDiagnosticOptions() {
        List<Flag> allFlags = Flag.getAllFlags();
        List<VMOption> result = new ArrayList<>();
        for (Flag flag : allFlags) {
            if (flag.isWriteable() && flag.isExternal()) {
                result.add(flag.getVMOption());
            }
        }
        return result;
    }

    @Override
    public VMOption getVMOption(String name) {
        if (name == null) {
            throw new NullPointerException("name cannot be null");
        }

        Flag f = Flag.getFlag(name);
        if (f == null) {
            throw new IllegalArgumentException("VM option \"" +
                name + "\" does not exist");
        }
        return f.getVMOption();
    }

    @Override
    public void setVMOption(String name, String value) {
        if (name == null) {
            throw new NullPointerException("name cannot be null");
        }
        if (value == null) {
            throw new NullPointerException("value cannot be null");
        }

        Util.checkControlAccess();
        Flag flag = Flag.getFlag(name);
        if (flag == null) {
            throw new IllegalArgumentException("VM option \"" +
                name + "\" does not exist");
        }
        if (!flag.isWriteable()){
            throw new IllegalArgumentException("VM Option \"" +
                name + "\" is not writeable");
        }

        // Check the type of the value
        Object v = flag.getValue();
        if (v instanceof Long) {
            try {
                long l = Long.parseLong(value);
                Flag.setLongValue(name, l);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid value:" +
                        " VM Option \"" + name + "\"" +
                        " expects numeric value", e);
            }
        } else if (v instanceof Double) {
            try {
                double d = Double.parseDouble(value);
                Flag.setDoubleValue(name, d);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid value:" +
                        " VM Option \"" + name + "\"" +
                        " expects numeric value", e);
            }
        } else if (v instanceof Boolean) {
            if (!value.equalsIgnoreCase("true") &&
                !value.equalsIgnoreCase("false")) {
                throw new IllegalArgumentException("Invalid value:" +
                    " VM Option \"" + name + "\"" +
                    " expects \"true\" or \"false\".");
            }
            Flag.setBooleanValue(name, Boolean.parseBoolean(value));
        } else if (v instanceof String) {
            Flag.setStringValue(name, value);
        } else {
            throw new IllegalArgumentException("VM Option \"" +
                name + "\" is of an unsupported type: " +
                v.getClass().getName());
        }
    }

    @Override
    public ObjectName getObjectName() {
        return Util.newObjectName("com.sun.management:type=HotSpotDiagnostic");
    }

    @Override
    @SuppressWarnings("removal")
    public void dumpThreads(String outputFile, ThreadDumpFormat format) throws IOException {
        Path file = Path.of(outputFile);
        if (!file.isAbsolute())
            throw new IllegalArgumentException("'outputFile' not absolute path");

        // need ManagementPermission("control")
        @SuppressWarnings("removal")
        SecurityManager sm = System.getSecurityManager();
        if (sm != null)
            Util.checkControlAccess();

        try (OutputStream out = Files.newOutputStream(file, StandardOpenOption.CREATE_NEW)) {
            PrivilegedExceptionAction<Void> pa = () -> {
                dumpThreads(out, format);
                return null;
            };
            try {
                AccessController.doPrivileged(pa);
            } catch (PrivilegedActionException pae) {
                Throwable cause = pae.getCause();
                if (cause instanceof IOException ioe)
                    throw ioe;
                if (cause instanceof RuntimeException e)
                    throw e;
                throw new RuntimeException(cause);
            }
        }
    }

    private void dumpThreads(OutputStream out, ThreadDumpFormat format) throws IOException {
        switch (format) {
            case TEXT_PLAIN -> ThreadDumper.dumpThreads(out);
            case JSON       -> ThreadDumper.dumpThreadsToJson(out);
        }
    }
}
