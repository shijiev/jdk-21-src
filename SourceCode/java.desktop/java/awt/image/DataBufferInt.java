/*
 * Copyright (c) 1997, 2022, Oracle and/or its affiliates. All rights reserved.
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

/* ****************************************************************
 ******************************************************************
 ******************************************************************
 *** COPYRIGHT (c) Eastman Kodak Company, 1997
 *** As  an unpublished  work pursuant to Title 17 of the United
 *** States Code.  All rights reserved.
 ******************************************************************
 ******************************************************************
 ******************************************************************/

package java.awt.image;

import static sun.java2d.StateTrackable.State.STABLE;
import static sun.java2d.StateTrackable.State.UNTRACKABLE;

/**
 * This class extends {@code DataBuffer} and stores data internally
 * as integers.
 * <p>
 * <a id="optimizations">
 * Note that some implementations may function more efficiently
 * if they can maintain control over how the data for an image is
 * stored.
 * For example, optimizations such as caching an image in video
 * memory require that the implementation track all modifications
 * to that data.
 * Other implementations may operate better if they can store the
 * data in locations other than a Java array.
 * To maintain optimum compatibility with various optimizations
 * it is best to avoid constructors and methods which expose the
 * underlying storage as a Java array as noted below in the
 * documentation for those methods.
 * </a>
 */
public final class DataBufferInt extends DataBuffer
{
    /** The default data bank. */
    int[] data;

    /** All data banks */
    int[][] bankdata;

    /**
     * Constructs an integer-based {@code DataBuffer} with a single bank
     * and the specified size.
     *
     * @param size The size of the {@code DataBuffer}.
     */
    public DataBufferInt(int size) {
        super(STABLE, TYPE_INT, size);
        data = new int[size];
        bankdata = new int[1][];
        bankdata[0] = data;
    }

    /**
     * Constructs an integer-based {@code DataBuffer} with the specified number of
     * banks, all of which are the specified size.
     *
     * @param size The size of the banks in the {@code DataBuffer}.
     * @param numBanks The number of banks in the {@code DataBuffer}.
     */
    public DataBufferInt(int size, int numBanks) {
        super(STABLE, TYPE_INT, size, numBanks);
        bankdata = new int[numBanks][];
        for (int i= 0; i < numBanks; i++) {
            bankdata[i] = new int[size];
        }
        data = bankdata[0];
    }

    /**
     * Constructs an integer-based {@code DataBuffer} with a single bank using the
     * specified array.
     * Only the first {@code size} elements should be used by accessors of
     * this {@code DataBuffer}.  {@code dataArray} must be large enough to
     * hold {@code size} elements.
     * <p>
     * Note that {@code DataBuffer} objects created by this constructor
     * may be incompatible with <a href="#optimizations">performance
     * optimizations</a> used by some implementations (such as caching
     * an associated image in video memory).
     *
     * @param dataArray The integer array for the {@code DataBuffer}.
     * @param size The size of the {@code DataBuffer} bank.
     */
    public DataBufferInt(int[] dataArray, int size) {
        super(UNTRACKABLE, TYPE_INT, size);
        data = dataArray;
        bankdata = new int[1][];
        bankdata[0] = data;
    }

    /**
     * Constructs an integer-based {@code DataBuffer} with a single bank using the
     * specified array, size, and offset.  {@code dataArray} must have at least
     * {@code offset} + {@code size} elements.  Only elements {@code offset}
     * through {@code offset} + {@code size} - 1
     * should be used by accessors of this {@code DataBuffer}.
     * <p>
     * Note that {@code DataBuffer} objects created by this constructor
     * may be incompatible with <a href="#optimizations">performance
     * optimizations</a> used by some implementations (such as caching
     * an associated image in video memory).
     *
     * @param dataArray The integer array for the {@code DataBuffer}.
     * @param size The size of the {@code DataBuffer} bank.
     * @param offset The offset into the {@code dataArray}.
     */
    public DataBufferInt(int[] dataArray, int size, int offset) {
        super(UNTRACKABLE, TYPE_INT, size, 1, offset);
        data = dataArray;
        bankdata = new int[1][];
        bankdata[0] = data;
    }

    /**
     * Constructs an integer-based {@code DataBuffer} with the specified arrays.
     * The number of banks will be equal to {@code dataArray.length}.
     * Only the first {@code size} elements of each array should be used by
     * accessors of this {@code DataBuffer}.
     * <p>
     * Note that {@code DataBuffer} objects created by this constructor
     * may be incompatible with <a href="#optimizations">performance
     * optimizations</a> used by some implementations (such as caching
     * an associated image in video memory).
     *
     * @param dataArray The integer arrays for the {@code DataBuffer}.
     * @param size The size of the banks in the {@code DataBuffer}.
     */
    public DataBufferInt(int[][] dataArray, int size) {
        super(UNTRACKABLE, TYPE_INT, size, dataArray.length);
        bankdata = dataArray.clone();
        data = bankdata[0];
    }

    /**
     * Constructs an integer-based {@code DataBuffer} with the specified arrays, size,
     * and offsets.
     * The number of banks is equal to {@code dataArray.length}.  Each array must
     * be at least as large as {@code size} + the corresponding offset.   There must
     * be an entry in the offset array for each {@code dataArray} entry.  For each
     * bank, only elements {@code offset} through
     * {@code offset} + {@code size} - 1 should be
     * used by accessors of this {@code DataBuffer}.
     * <p>
     * Note that {@code DataBuffer} objects created by this constructor
     * may be incompatible with <a href="#optimizations">performance
     * optimizations</a> used by some implementations (such as caching
     * an associated image in video memory).
     *
     * @param dataArray The integer arrays for the {@code DataBuffer}.
     * @param size The size of the banks in the {@code DataBuffer}.
     * @param offsets The offsets into each array.
     */
    public DataBufferInt(int[][] dataArray, int size, int[] offsets) {
        super(UNTRACKABLE, TYPE_INT, size, dataArray.length, offsets);
        bankdata = dataArray.clone();
        data = bankdata[0];
    }

    /**
     * Returns the default (first) int data array in {@code DataBuffer}.
     * <p>
     * Note that calling this method may cause this {@code DataBuffer}
     * object to be incompatible with <a href="#optimizations">performance
     * optimizations</a> used by some implementations (such as caching
     * an associated image in video memory).
     *
     * @return The first integer data array.
     */
    public int[] getData() {
        theTrackable.setUntrackable();
        return data;
    }

    /**
     * Returns the data array for the specified bank.
     * <p>
     * Note that calling this method may cause this {@code DataBuffer}
     * object to be incompatible with <a href="#optimizations">performance
     * optimizations</a> used by some implementations (such as caching
     * an associated image in video memory).
     *
     * @param bank The bank whose data array you want to get.
     * @return The data array for the specified bank.
     */
    public int[] getData(int bank) {
        theTrackable.setUntrackable();
        return bankdata[bank];
    }

    /**
     * Returns the data arrays for all banks.
     * <p>
     * Note that calling this method may cause this {@code DataBuffer}
     * object to be incompatible with <a href="#optimizations">performance
     * optimizations</a> used by some implementations (such as caching
     * an associated image in video memory).
     *
     * @return All of the data arrays.
     */
    public int[][] getBankData() {
        theTrackable.setUntrackable();
        return bankdata.clone();
    }

    /**
     * Returns the requested data array element from the first (default) bank.
     *
     * @param i The data array element you want to get.
     * @return The requested data array element as an integer.
     * @see #setElem(int, int)
     * @see #setElem(int, int, int)
     */
    public int getElem(int i) {
        return data[i+offset];
    }

    /**
     * Returns the requested data array element from the specified bank.
     *
     * @param bank The bank from which you want to get a data array element.
     * @param i The data array element you want to get.
     * @return The requested data array element as an integer.
     * @see #setElem(int, int)
     * @see #setElem(int, int, int)
     */
    public int getElem(int bank, int i) {
        return bankdata[bank][i+offsets[bank]];
    }

    /**
     * Sets the requested data array element in the first (default) bank
     * to the specified value.
     *
     * @param i The data array element you want to set.
     * @param val The integer value to which you want to set the data array element.
     * @see #getElem(int)
     * @see #getElem(int, int)
     */
    public void setElem(int i, int val) {
        data[i+offset] = val;
        theTrackable.markDirty();
    }

    /**
     * Sets the requested data array element in the specified bank
     * to the integer value {@code i}.
     * @param bank The bank in which you want to set the data array element.
     * @param i The data array element you want to set.
     * @param val The integer value to which you want to set the specified data array element.
     * @see #getElem(int)
     * @see #getElem(int, int)
     */
    public void setElem(int bank, int i, int val) {
        bankdata[bank][i+offsets[bank]] = val;
        theTrackable.markDirty();
    }
}
