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
package jdk.jfr.internal.dcmd;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import jdk.jfr.FlightRecorder;
import jdk.jfr.consumer.EventStream;
import jdk.jfr.internal.PlatformRecorder;
import jdk.jfr.internal.PrivateAccess;
import jdk.jfr.internal.RepositoryChunk;
import jdk.jfr.internal.query.Configuration;
import jdk.jfr.internal.query.QueryPrinter;
import jdk.jfr.internal.query.ViewPrinter;
import jdk.jfr.internal.query.Configuration.Truncate;
import jdk.jfr.internal.util.UserDataException;
import jdk.jfr.internal.util.UserSyntaxException;
import jdk.jfr.internal.util.Output;

/**
 * Helper class that holds recording chunks alive during a query. It also helps
 * out with configuration shared by DCmdView and DCmdQuery
 */
final class QueryRecording implements AutoCloseable {
    private final long DEFAULT_MAX_SIZE = 32 * 1024 * 1024L;
    private final long DEFAULT_MAX_AGE = 60 * 10;

    private final PlatformRecorder recorder;
    private final List<RepositoryChunk> chunks;
    private final EventStream eventStream;
    private final Instant endTime;

    public QueryRecording(Configuration configuration, ArgumentParser parser) throws IOException, DCmdException {
        if (!FlightRecorder.isInitialized()) {
            throw new DCmdException("No recording data available. Start a recording with JFR.start");
        }
        recorder = PrivateAccess.getInstance().getPlatformRecorder();
        Boolean verbose = parser.getOption("verbose");
        if (verbose != null) {
            configuration.verbose = verbose;
        }
        configuration.truncate = valueOf(parser.getOption("truncate"));
        Long width = parser.getOption("width");
        if (width != null) {
            configuration.width = (int) Math.min(Integer.MAX_VALUE, width.longValue());
        }
        Long height = parser.getOption("cell-height");
        if (height != null) {
            if (height < 1) {
                throw new DCmdException("Height must be at least 1");
            }
            configuration.cellHeight = (int) Math.min(Integer.MAX_VALUE, height.longValue());
        }
        Long maxAge = parser.getOption("maxage");

        Long maxSize = parser.getOption("maxsize");
        if (maxSize == null) {
            maxSize = DEFAULT_MAX_SIZE;;
        }
        Instant startTime = Instant.EPOCH;
        endTime = configuration.endTime;
        if (maxAge != null) {
            startTime = endTime.minus(Duration.ofNanos(maxAge));
        } else {
            startTime = endTime.minus(Duration.ofSeconds(DEFAULT_MAX_AGE));
        }
        chunks = acquireChunks(startTime);
        Instant streamStart = determineStreamStart(maxSize, startTime);
        configuration.startTime = streamStart;
        eventStream = makeStream(streamStart);
    }

    private List<RepositoryChunk> acquireChunks(Instant startTime) {
        synchronized (recorder) {
            List<RepositoryChunk> list = recorder.makeChunkList(startTime, endTime);
            list.add(currentChunk());
            for (RepositoryChunk r : list) {
                r.use();
            }
            return list;
        }
    }

    private RepositoryChunk currentChunk() {
        return PrivateAccess.getInstance().getPlatformRecorder().getCurrentChunk();
    }

    private void releaseChunks() {
        synchronized (recorder) {
            for (RepositoryChunk r : chunks) {
                r.release();
            }
        }
    }

    private EventStream makeStream(Instant startTime) throws IOException {
        EventStream es = EventStream.openRepository();
        es.setStartTime(startTime);
        es.setEndTime(endTime);
        return es;
    }

    private Instant determineStreamStart(Long maxSize, Instant startTime) {
        ListIterator<RepositoryChunk> iterator = chunks.listIterator(chunks.size());
        long size = 0;
        while (iterator.hasPrevious()) {
            RepositoryChunk r = iterator.previous();
            if (r.isFinished()) {
                size += r.getSize();
                if (size > maxSize) {
                    return r.getStartTime().isAfter(startTime) ? r.getStartTime() : startTime;
                }
            } else {
                size += r.getCurrentFileSize();
            }
        }
        return startTime;
    }

    private Truncate valueOf(String truncate) throws DCmdException {
        if (truncate == null || truncate.equals("end")) {
            return Truncate.END;
        }
        if (truncate.equals("beginning")) {
            return Truncate.BEGINNING;
        }
        throw new DCmdException("Truncate must be 'end' or 'beginning");
    }

    public EventStream getStream() {
        return eventStream;
    }

    @Override
    public void close() {
        eventStream.close();
        releaseChunks();
    }
}
