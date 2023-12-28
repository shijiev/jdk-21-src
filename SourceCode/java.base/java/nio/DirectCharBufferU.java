/*
 * Copyright (c) 2000, 2023, Oracle and/or its affiliates. All rights reserved.
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

// -- This file was mechanically generated: Do not edit! -- //

package java.nio;

import java.io.FileDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.ref.Reference;
import java.util.Objects;
import jdk.internal.foreign.MemorySessionImpl;
import jdk.internal.misc.ScopedMemoryAccess.ScopedAccessError;
import jdk.internal.misc.VM;
import jdk.internal.ref.Cleaner;
import sun.nio.ch.DirectBuffer;



sealed



class DirectCharBufferU

    extends CharBuffer



    implements DirectBuffer

    permits DirectCharBufferRU

{



    // Cached unaligned-access capability
    protected static final boolean UNALIGNED = Bits.unaligned();

    // Base address, used in all indexing calculations
    // NOTE: moved up to Buffer.java for speed in JNI GetDirectBufferAddress
    //    protected long address;

    // An object attached to this buffer. If this buffer is a view of another
    // buffer then we use this field to keep a reference to that buffer to
    // ensure that its memory isn't freed before we are done with it.
    private final Object att;

    public Object attachment() {
        return att;
    }




































    public Cleaner cleaner() { return null; }




















































































































    // For duplicates and slices
    //
    DirectCharBufferU(DirectBuffer db,         // package-private
                               int mark, int pos, int lim, int cap, int off,



                               MemorySegment segment)
    {

        super(mark, pos, lim, cap,



              segment);
        address = ((Buffer)db).address + off;



        Object attachment = db.attachment();
        att = (attachment == null ? db : attachment);








    }

    @Override
    Object base() {
        return null;
    }

    public CharBuffer slice() {
        int pos = this.position();
        int lim = this.limit();
        int rem = (pos <= lim ? lim - pos : 0);
        int off = (pos << 1);
        assert (off >= 0);
        return new DirectCharBufferU(this,
                                              -1,
                                              0,
                                              rem,
                                              rem,
                                              off,




                                              segment);
    }

    @Override
    public CharBuffer slice(int index, int length) {
        Objects.checkFromIndexSize(index, length, limit());
        return new DirectCharBufferU(this,
                                              -1,
                                              0,
                                              length,
                                              length,
                                              index << 1,




                                              segment);
    }

    public CharBuffer duplicate() {
        return new DirectCharBufferU(this,
                                              this.markValue(),
                                              this.position(),
                                              this.limit(),
                                              this.capacity(),
                                              0,




                                              segment);
    }

    public CharBuffer asReadOnlyBuffer() {

        return new DirectCharBufferRU(this,
                                           this.markValue(),
                                           this.position(),
                                           this.limit(),
                                           this.capacity(),
                                           0,




                                           segment);



    }



    public long address() {
        MemorySessionImpl session = session();
        if (session != null) {
            if (session.ownerThread() == null && session.isCloseable()) {
                throw new UnsupportedOperationException("ByteBuffer derived from closeable shared sessions not supported");
            }
            session.checkValidState();
        }
        return address;
    }

    private long ix(int i) {
        return address + ((long)i << 1);
    }

    public char get() {
        try {
            return ((SCOPED_MEMORY_ACCESS.getChar(session(), null, ix(nextGetIndex()))));
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public char get(int i) {
        try {
            return ((SCOPED_MEMORY_ACCESS.getChar(session(), null, ix(checkIndex(i)))));
        } finally {
            Reference.reachabilityFence(this);
        }
    }


    char getUnchecked(int i) {
        try {
            return ((SCOPED_MEMORY_ACCESS.getChar(null, null, ix(i))));
        } finally {
            Reference.reachabilityFence(this);
        }
    }



    public CharBuffer put(char x) {

        try {
            SCOPED_MEMORY_ACCESS.putChar(session(), null, ix(nextPutIndex()), ((x)));
        } finally {
            Reference.reachabilityFence(this);
        }
        return this;



    }

    public CharBuffer put(int i, char x) {

        try {
            SCOPED_MEMORY_ACCESS.putChar(session(), null, ix(checkIndex(i)), ((x)));
        } finally {
            Reference.reachabilityFence(this);
        }
        return this;



    }

    public CharBuffer compact() {

        int pos = position();
        int lim = limit();
        assert (pos <= lim);
        int rem = (pos <= lim ? lim - pos : 0);
        try {
            // null is passed as destination MemorySession to avoid checking session() twice
            SCOPED_MEMORY_ACCESS.copyMemory(session(), null, null,
                    ix(pos), null, ix(0), (long)rem << 1);
        } finally {
            Reference.reachabilityFence(this);
        }
        position(rem);
        limit(capacity());
        discardMark();
        return this;



    }

    public boolean isDirect() {
        return true;
    }

    public boolean isReadOnly() {
        return false;
    }




    public String toString(int start, int end) {
        Objects.checkFromToIndex(start, end, limit());
        int len = end - start;
        char[] ca = new char[len];
        CharBuffer cb = CharBuffer.wrap(ca);
        CharBuffer db = this.duplicate();
        db.position(start);
        db.limit(end);
        cb.put(db);
        return new String(ca);
    }


    // --- Methods to support CharSequence ---


    private static final int APPEND_BUF_SIZE = 1024;

    private CharBuffer appendChars(CharSequence csq, int start, int end) {
        Objects.checkFromToIndex(start, end, csq.length());

        int pos = position();
        int lim = limit();
        int rem = (pos <= lim) ? lim - pos : 0;
        int length = end - start;
        if (length > rem)
            throw new BufferOverflowException();

        char[] buf = new char[Math.min(APPEND_BUF_SIZE, length)];
        int index = pos;
        while (start < end) {
            int count = end - start;
            if (count > buf.length)
                count = buf.length;

            if (csq instanceof String str) {
                str.getChars(start, start + count, buf, 0);
            } else if (csq instanceof StringBuilder sb) {
                sb.getChars(start, start + count, buf, 0);
            } else if (csq instanceof StringBuffer sb) {
                sb.getChars(start, start + count, buf, 0);
            }

            putArray(index, buf, 0, count);

            start += count;
            index += count;
        }

        position(pos + length);

        return this;
    }


    public CharBuffer append(CharSequence csq) {

        if (csq instanceof StringBuilder) 
            return appendChars(csq, 0, csq.length());
 
        return super.append(csq);



    }
 
    public CharBuffer append(CharSequence csq, int start, int end) { 

        if (csq instanceof String || csq instanceof StringBuffer ||
            csq instanceof StringBuilder)
            return appendChars(csq, start, end);

        return super.append(csq, start, end);



    }

    public CharBuffer subSequence(int start, int end) {
        int pos = position();
        int lim = limit();
        assert (pos <= lim);
        pos = (pos <= lim ? pos : lim);
        int len = lim - pos;

        Objects.checkFromToIndex(start, end, len);
        return new DirectCharBufferU(this,
                                            -1,
                                            pos + start,
                                            pos + end,
                                            capacity(),
                                            offset, segment);
    }







    public ByteOrder order() {





        return ((ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN)
                ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);

    }




    ByteOrder charRegionOrder() {
        return order();
    }











}
