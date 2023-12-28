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

import java.lang.foreign.MemorySegment;
import java.util.Objects;

/**



 * A read-only HeapCharBuffer.  This class extends the corresponding
 * read/write class, overriding the mutation methods to throw a {@link
 * ReadOnlyBufferException} and overriding the view-buffer methods to return an
 * instance of this class rather than of the superclass.

 */



final

class HeapCharBufferR
    extends HeapCharBuffer



{















    HeapCharBufferR(int cap, int lim, MemorySegment segment) {            // package-private








        super(cap, lim, segment);
        this.isReadOnly = true;

    }

    HeapCharBufferR(char[] buf, int off, int len, MemorySegment segment) { // package-private








        super(buf, off, len, segment);
        this.isReadOnly = true;

    }

    protected HeapCharBufferR(char[] buf,
                                   int mark, int pos, int lim, int cap,
                                   int off, MemorySegment segment)
    {








        super(buf, mark, pos, lim, cap, off, segment);
        this.isReadOnly = true;

    }

    public CharBuffer slice() {
        int pos = this.position();
        int lim = this.limit();
        int rem = (pos <= lim ? lim - pos : 0);
        return new HeapCharBufferR(hb,
                                        -1,
                                        0,
                                        rem,
                                        rem,
                                        pos + offset, segment);
    }

    @Override
    public CharBuffer slice(int index, int length) {
        Objects.checkFromIndexSize(index, length, limit());
        return new HeapCharBufferR(hb,
                                        -1,
                                        0,
                                        length,
                                        length,
                                        index + offset, segment);
    }

    public CharBuffer duplicate() {
        return new HeapCharBufferR(hb,
                                        this.markValue(),
                                        this.position(),
                                        this.limit(),
                                        this.capacity(),
                                        offset, segment);
    }

    public CharBuffer asReadOnlyBuffer() {








        return duplicate();

    }




















































    public boolean isReadOnly() {
        return true;
    }

    public CharBuffer put(char x) {




        throw new ReadOnlyBufferException();

    }

    public CharBuffer put(int i, char x) {




        throw new ReadOnlyBufferException();

    }

    public CharBuffer put(char[] src, int offset, int length) {










        throw new ReadOnlyBufferException();

    }

    public CharBuffer put(CharBuffer src) {





        throw new ReadOnlyBufferException();

    }

    public CharBuffer put(int index, CharBuffer src, int offset, int length) {





        throw new ReadOnlyBufferException();

    }

    public CharBuffer put(int index, char[] src, int offset, int length) {







        throw new ReadOnlyBufferException();

    }



    //
    // Use getChars() to load chars directly into the heap buffer array.
    // For a String or StringBuffer source this improves performance if
    // a proper subsequence is being appended as copying to a new intermediate
    // String object is avoided. For a StringBuilder where either a subsequence
    // or the full sequence of chars is being appended, copying the chars to
    // an intermedite String in StringBuilder::toString is avoided.
    //
    private CharBuffer appendChars(CharSequence csq, int start, int end) {
        checkSession();

        Objects.checkFromToIndex(start, end, csq.length());

        int length = end - start;
        int pos = position();
        int lim = limit();
        int rem = (pos <= lim) ? lim - pos : 0;
        if (length > rem)
            throw new BufferOverflowException();

        if (csq instanceof String str) {
            str.getChars(start, end, hb, ix(pos));
        } else if (csq instanceof StringBuilder sb) {
            sb.getChars(start, end, hb, ix(pos));
        } else if (csq instanceof StringBuffer sb) {
            sb.getChars(start, end, hb, ix(pos));
        }

        position(pos + length);

        return this;
    }

    public CharBuffer append(CharSequence csq) {






        throw new ReadOnlyBufferException();

    }

    public CharBuffer append(CharSequence csq, int start, int end) {







        throw new ReadOnlyBufferException();

    }

    public CharBuffer put(String src, int start, int end) {













        throw new ReadOnlyBufferException();

    }



    public CharBuffer compact() {











        throw new ReadOnlyBufferException();

    }






















































































































































































































































































































































    String toString(int start, int end) {               // package-private
        try {
            return new String(hb, start + offset, end - start);
        } catch (StringIndexOutOfBoundsException x) {
            throw new IndexOutOfBoundsException();
        }
    }


    // --- Methods to support CharSequence ---

    public CharBuffer subSequence(int start, int end) {
        int pos = position();
        Objects.checkFromToIndex(start, end, limit() - pos);
        return new HeapCharBufferR(hb,
                                      -1,
                                      pos + start,
                                      pos + end,
                                      capacity(),
                                      offset, segment);
    }






    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }



    ByteOrder charRegionOrder() {
        return order();
    }

}
