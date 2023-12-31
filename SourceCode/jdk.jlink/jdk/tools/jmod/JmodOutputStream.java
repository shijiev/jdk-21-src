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

package jdk.tools.jmod;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import jdk.internal.jmod.JmodFile;
import java.time.LocalDateTime;

import static jdk.internal.jmod.JmodFile.*;

/**
 * Output stream to write to JMOD file
 */
class JmodOutputStream extends OutputStream implements AutoCloseable {
    private final Map<Section, Set<String>> entries = new HashMap<>();

    /**
     * This method creates (or overrides, if exists) the JMOD file,
     * returning the output stream to write to the JMOD file.
     */
    static JmodOutputStream newOutputStream(Path file, LocalDateTime date, int compressLevel) throws IOException {
        OutputStream out = Files.newOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(out);
        return new JmodOutputStream(bos, date, compressLevel);
    }

    private final ZipOutputStream zos;
    private final LocalDateTime date;
    private JmodOutputStream(OutputStream out, LocalDateTime date, int compressLevel) {
        this.zos = new ZipOutputStream(out);
        this.zos.setLevel(compressLevel);
        this.date = date;
        try {
            JmodFile.writeMagicNumber(out);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Writes the input stream to the named entry of the given section.
     */
    public void writeEntry(InputStream in, Section section, String name)
        throws IOException
    {
        ZipEntry ze = newEntry(section, name);
        zos.putNextEntry(ze);
        in.transferTo(zos);
        zos.closeEntry();
    }

    /**
     * Writes the given bytes to the named entry of the given section.
     */
    public void writeEntry(byte[] bytes, Section section, String path)
        throws IOException
    {
        ZipEntry ze = newEntry(section, path);
        zos.putNextEntry(ze);
        zos.write(bytes);
        zos.closeEntry();
    }

    /**
     * Writes the given entry to the given input stream.
     */
    public void writeEntry(InputStream in, Entry e) throws IOException {
        ZipEntry e1 = e.zipEntry();
        // Only preserve attributes which won't change by
        // inflating and deflating the entry. See:
        // sun.tools.jar.Main.update()
        ZipEntry e2 = new ZipEntry(e1.getName());
        e2.setMethod(e1.getMethod());
        if (date != null) {
            e2.setTimeLocal(date);
        } else {
            e2.setTime(e1.getTime());
        }
        e2.setComment(e1.getComment());
        e2.setExtra(e1.getExtra());
        if (e1.getMethod() == ZipEntry.STORED) {
            e2.setSize(e1.getSize());
            e2.setCrc(e1.getCrc());
        }
        zos.putNextEntry(e2);
        zos.write(in.readAllBytes());
        zos.closeEntry();
    }

    private ZipEntry newEntry(Section section, String path) throws IOException {
        if (contains(section, path)) {
            throw new IOException("duplicate entry: " + path + " in section " + section);
        }
        String prefix = section.jmodDir();
        String name = Paths.get(prefix, path).toString()
                           .replace(File.separatorChar, '/');
        entries.get(section).add(path);
        ZipEntry zipEntry = new ZipEntry(name);
        if (date != null) {
            zipEntry.setTimeLocal(date);
        }
        return zipEntry;
    }

    public boolean contains(Section section, String path) {
        Set<String> set = entries.computeIfAbsent(section, k -> new HashSet<>());
        return set.contains(path);
    }

    @Override
    public void write(int b) throws IOException {
        zos.write(b);
    }

    @Override
    public void close() throws IOException {
        zos.close();
    }
}
