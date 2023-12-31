/*
 * Copyright (c) 2005, 2016, Oracle and/or its affiliates. All rights reserved.
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
package com.sun.imageio.plugins.tiff;

import javax.imageio.plugins.tiff.BaselineTIFFTagSet;
import java.io.IOException;
import javax.imageio.IIOException;

/**
 *
 */
public class TIFFRLECompressor extends TIFFFaxCompressor {

    public TIFFRLECompressor() {
        super("CCITT RLE", BaselineTIFFTagSet.COMPRESSION_CCITT_RLE, true);
    }

    /**
     * Encode a row of data using Modified Huffman Compression also known as
     * CCITT RLE (Run Length Encoding).
     *
     * @param data        The row of data to compress.
     * @param rowOffset   Starting index in {@code data}.
     * @param colOffset   Bit offset within first {@code data[rowOffset]}.
     * @param rowLength   Number of bits in the row.
     * @param compData    The compressed data.
     *
     * @return The number of bytes saved in the compressed data array.
     */
    public int encodeRLE(byte[] data,
                         int rowOffset,
                         int colOffset,
                         int rowLength,
                         byte[] compData) {
        //
        // Initialize bit buffer machinery.
        //
        initBitBuf();

        //
        // Run-length encode line.
        //
        int outIndex =
            encode1D(data, rowOffset, colOffset, rowLength, compData, 0);

        //
        // Flush pending bits
        //
        while (ndex > 0) {
            compData[outIndex++] = (byte)(bits >>> 24);
            bits <<= 8;
            ndex -= 8;
        }

        //
        // Flip the bytes if inverse fill was requested.
        //
        if (inverseFill) {
            byte[] flipTable = TIFFFaxDecompressor.flipTable;
            for(int i = 0; i < outIndex; i++) {
                compData[i] = flipTable[compData[i] & 0xff];
            }
        }

        return outIndex;
    }

    public int encode(byte[] b, int off,
                      int width, int height,
                      int[] bitsPerSample,
                      int scanlineStride) throws IOException {
        if (bitsPerSample.length != 1 || bitsPerSample[0] != 1) {
            throw new IIOException(
                            "Bits per sample must be 1 for RLE compression!");
        }

        // In the worst case, 2 bits of input will result in 9 bits of output,
        // plus 2 extra bits if the row starts with black.
        int maxBits = 9*((width + 1)/2) + 2;
        byte[] compData = new byte[(maxBits + 7)/8];

        int bytes = 0;
        int rowOffset = off;

        for (int i = 0; i < height; i++) {
            int rowBytes = encodeRLE(b, rowOffset, 0, width, compData);
            stream.write(compData, 0, rowBytes);

            rowOffset += scanlineStride;
            bytes += rowBytes;
        }

        return bytes;
    }
}
