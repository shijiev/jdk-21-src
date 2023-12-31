/*
 * Copyright (c) 2015, 2022, Oracle and/or its affiliates. All rights reserved.
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

package sun.java2d.marlin;

import static sun.java2d.marlin.ArrayCacheConst.ARRAY_SIZES;
import static sun.java2d.marlin.ArrayCacheConst.BUCKETS;
import static sun.java2d.marlin.ArrayCacheConst.MAX_ARRAY_SIZE;

import static sun.java2d.marlin.MarlinConst.DO_STATS;
import static sun.java2d.marlin.MarlinConst.DO_CHECKS;
import static sun.java2d.marlin.MarlinConst.DO_LOG_WIDEN_ARRAY;
import static sun.java2d.marlin.MarlinConst.DO_LOG_OVERSIZE;

import static sun.java2d.marlin.MarlinUtils.logInfo;
import static sun.java2d.marlin.MarlinUtils.logException;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import sun.java2d.marlin.ArrayCacheConst.BucketStats;
import sun.java2d.marlin.ArrayCacheConst.CacheStats;

/*
 * Note that the ArrayCache[Int/IntClean] files are nearly identical except
 * for their array type [byte/double/int] and class name differences.
 * ArrayCache[Int]Clean class deals with zero-filled arrays.
 */

final class ArrayCacheIntClean {

    /* members */
    private final int bucketCapacity;
    private WeakReference<Bucket[]> refBuckets = null;
    final CacheStats stats;

    ArrayCacheIntClean(final int bucketCapacity) {
        this.bucketCapacity = bucketCapacity;
        this.stats = (DO_STATS) ?
            new CacheStats("ArrayCacheInt(Clean)") : null;
    }

    Bucket getCacheBucket(final int length) {
        final int bucket = ArrayCacheConst.getBucket(length);
        return getBuckets()[bucket];
    }

    private Bucket[] getBuckets() {
        // resolve reference:
        Bucket[] buckets = (refBuckets != null) ? refBuckets.get() : null;

        // create a new buckets ?
        if (buckets == null) {
            buckets = new Bucket[BUCKETS];

            for (int i = 0; i < BUCKETS; i++) {
                buckets[i] = new Bucket(ARRAY_SIZES[i], bucketCapacity,
                        (DO_STATS) ? stats.bucketStats[i] : null);
            }

            // update weak reference:
            refBuckets = new WeakReference<>(buckets);
        }
        return buckets;
    }

    Reference createRef(final int initialSize) {
        return new Reference(this, initialSize);
    }

    static final class Reference {

        // initial array reference (direct access)
        final int[] initial;
        private final ArrayCacheIntClean cache;

        Reference(final ArrayCacheIntClean cache, final int initialSize) {
            this.cache = cache;
            this.initial = createArray(initialSize);
            if (DO_STATS) {
                cache.stats.totalInitial += initialSize;
            }
        }

        int[] getArray(final int length) {
            if (length <= MAX_ARRAY_SIZE) {
                return cache.getCacheBucket(length).getArray();
            }
            if (DO_STATS) {
                cache.stats.oversize++;
            }
            if (DO_LOG_OVERSIZE) {
                logInfo("ArrayCacheInt(Clean): "
                        + "getArray[oversize]: length=\t" + length);
            }
            return createArray(length);
        }

        int[] widenArray(final int[] array, final int usedSize,
                          final int needSize)
        {
            final int length = array.length;
            if (DO_CHECKS && length >= needSize) {
                return array;
            }
            if (DO_STATS) {
                cache.stats.resize++;
            }

            // maybe change bucket:
            // ensure getNewSize() > newSize:
            final int[] res = getArray(ArrayCacheConst.getNewSize(usedSize, needSize));

            // use wrapper to ensure proper copy:
            System.arraycopy(array, 0, res, 0, usedSize); // copy only used elements

            // maybe return current array:
            putArray(array, 0, usedSize); // ensure array is cleared

            if (DO_LOG_WIDEN_ARRAY) {
                logInfo("ArrayCacheInt(Clean): "
                        + "widenArray[" + res.length
                        + "]: usedSize=\t" + usedSize + "\tlength=\t" + length
                        + "\tneeded length=\t" + needSize);
            }
            return res;
        }

        boolean doSetRef(final int[] array) {
            return (array != initial);
        }

        int[] putArrayClean(final int[] array)
        {
            // must be protected by doSetRef() call !
            if (array.length <= MAX_ARRAY_SIZE) {
                // ensure to never store initial arrays in cache:
                cache.getCacheBucket(array.length).putArray(array);
            }
            return initial;
        }

        int[] putArray(final int[] array, final int fromIndex,
                        final int toIndex)
        {
            if (array.length <= MAX_ARRAY_SIZE) {
                if (toIndex != 0) {
                    // clean-up array of dirty part[fromIndex; toIndex[
                    fill(array, fromIndex, toIndex, /*(int)*/ 0);
                }
                // ensure to never store initial arrays in cache:
                if (array != initial) {
                    cache.getCacheBucket(array.length).putArray(array);
                }
            }
            return initial;
        }
    }

    static final class Bucket {

        private int tail = 0;
        private final int arraySize;
        private final int[][] arrays;
        private final BucketStats stats;

        Bucket(final int arraySize,
               final int capacity, final BucketStats stats)
        {
            this.arraySize = arraySize;
            this.stats = stats;
            this.arrays = new int[capacity][];
        }

        int[] getArray() {
            if (DO_STATS) {
                stats.getOp++;
            }
            // use cache:
            if (tail != 0) {
                final int[] array = arrays[--tail];
                arrays[tail] = null;
                return array;
            }
            if (DO_STATS) {
                stats.createOp++;
            }
            return createArray(arraySize);
        }

        void putArray(final int[] array)
        {
            if (DO_CHECKS && (array.length != arraySize)) {
                logInfo("ArrayCacheInt(Clean): "
                        + "bad length = " + array.length);
                return;
            }
            if (DO_STATS) {
                stats.returnOp++;
            }
            // fill cache:
            if (arrays.length > tail) {
                arrays[tail++] = array;

                if (DO_STATS) {
                    stats.updateMaxSize(tail);
                }
            } else if (DO_CHECKS) {
                logInfo("ArrayCacheInt(Clean): "
                        + "array capacity exceeded !");
            }
        }
    }

    static int[] createArray(final int length) {
        return new int[length];
    }

    static void fill(final int[] array, final int fromIndex,
                     final int toIndex, final int value)
    {
        // clear array data:
        Arrays.fill(array, fromIndex, toIndex, value);
        if (DO_CHECKS) {
            check(array, fromIndex, toIndex, value);
        }
    }

    static void check(final int[] array, final int fromIndex,
                      final int toIndex, final int value)
    {
        if (DO_CHECKS) {
            // check zero on full array:
            for (int i = 0; i < array.length; i++) {
                if (array[i] != value) {
                    logException("Invalid value at: " + i + " = " + array[i]
                            + " from: " + fromIndex + " to: " + toIndex + "\n"
                            + Arrays.toString(array), new Throwable());

                    // ensure array is correctly filled:
                    Arrays.fill(array, value);

                    return;
                }
            }
        }
    }
}
