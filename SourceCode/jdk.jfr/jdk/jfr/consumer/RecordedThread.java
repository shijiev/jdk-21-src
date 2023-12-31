/*
 * Copyright (c) 2016, 2023, Oracle and/or its affiliates. All rights reserved.
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

package jdk.jfr.consumer;

import jdk.jfr.internal.consumer.ObjectContext;

/**
 * A recorded thread.
 *
 * @since 9
 */
public final class RecordedThread extends RecordedObject {
    private final long uniqueId;

    // package private
    RecordedThread(ObjectContext objectContext, long id, Object[] values) {
        super(objectContext, values);
        this.uniqueId = id;
    }

    /**
     * Returns the thread name used by the operating system.
     *
     * @return the OS thread name, or {@code null} if doesn't exist
     */
    public String getOSName() {
        return getTyped("osName", String.class, null);
    }

    /**
     * Returns the thread ID used by the operating system.
     *
     * @return the OS thread ID, or {@code -1} if doesn't exist
     */
    public long getOSThreadId() {
        if (isVirtual()) {
            return -1L;
        }
        Long l = getTyped("osThreadId", Long.class, -1L);
        return l.longValue();
    }

    /**
     * Returns the Java thread group, if available.
     *
     * @return the thread group, or {@code null} if doesn't exist
     */
    public RecordedThreadGroup getThreadGroup() {
        return getTyped("group", RecordedThreadGroup.class, null);
    }

    /**
     * Returns the Java thread name, or {@code null} if doesn't exist.
     * <p>
     * Returns {@code java.lang.Thread.getName()} if the thread has a Java
     * representation. {@code null} otherwise.
     *
     * @return the Java thread name, or {@code null} if doesn't exist
     */
    public String getJavaName() {
        return getTyped("javaName", String.class, null);
    }

    /**
     * Returns the Java thread ID, or {@code -1} if it's not a Java thread.
     *
     * @return the Java thread ID, or {@code -1} if it's not a Java thread
     *
     * @see java.lang.Thread#threadId()
     */
    public long getJavaThreadId() {
        Long l = getTyped("javaThreadId", Long.class, -1L);
        long id = l.longValue();
        return id == 0 ? -1L : id;
    }

    /**
     * Returns a unique ID for both native threads and Java threads that can't be
     * reused within the lifespan of the JVM.
     * <p>
     * See {@link #getJavaThreadId()} for the ID that is returned by
     * {@code java.lang.Thread.threadId()}.
     * <p>
     * See {@link #getOSThreadId()} for the ID that is returned by
     * the operating system.
     *
     * @return a unique ID for the thread
     */
    public long getId() {
        return uniqueId;
    }

    /**
     * {@return {@code true} if this is a virtual Thread, {@code false} otherwise}
     *
     * @since 21
     */
    public boolean isVirtual() {
        return getTyped("virtual", Boolean.class, Boolean.FALSE);
    }

}
