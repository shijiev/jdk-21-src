/*
 * Copyright (c) 2012, 2023, Oracle and/or its affiliates. All rights reserved.
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

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import jdk.jfr.EventType;
import jdk.jfr.Recording;
import jdk.jfr.SettingDescriptor;
import jdk.jfr.internal.Utils;

/**
 * JFR.check - invoked from native
 *
 */
final class DCmdCheck extends AbstractDCmd {

    @Override
    protected void execute(ArgumentParser parser) throws DCmdException {
        parser.checkUnknownArguments();
        Boolean verbose = parser.getOption("verbose");
        String name = parser.getOption("name");

        if (verbose == null) {
            verbose = Boolean.FALSE;
        }

        if (name != null) {
            printRecording(findRecording(name), verbose);
            return;
        }

        List<Recording> recordings = getRecordings();
        if (!verbose && recordings.isEmpty()) {
            println("No available recordings.");
            println();
            println("Use jcmd " + getPid() + " JFR.start to start a recording.");
            return;
        }
        boolean first = true;
        for (Recording recording : recordings) {
            // Print separation between recordings,
            if (!first) {
                println();
                if (Boolean.TRUE.equals(verbose)) {
                    println();
                }
            }
            first = false;
            printRecording(recording, verbose);
        }
    }

    private void printRecording(Recording recording, boolean verbose) {
        printGeneral(recording);
        if (verbose) {
            println();
            printSettings(recording);
        }
    }

    private void printGeneral(Recording recording) {
        print("Recording " + recording.getId() + ": name=" + recording.getName());

        Duration duration = recording.getDuration();
        if (duration != null) {
            print(" duration=");
            printTimespan(duration, "");
        }

        long maxSize = recording.getMaxSize();
        if (maxSize != 0) {
            print(" maxsize=");
            print(Utils.formatBytesCompact(maxSize));
        }
        Duration maxAge = recording.getMaxAge();
        if (maxAge != null) {
            print(" maxage=");
            printTimespan(maxAge, "");
        }

        print(" (" + recording.getState().toString().toLowerCase() + ")");
        println();
    }

    private void printSettings(Recording recording) {
        Map<String, String> settings = recording.getSettings();
        for (EventType eventType : sortByEventPath(getFlightRecorder().getEventTypes())) {
            StringJoiner sj = new StringJoiner(",", "[", "]");
            sj.setEmptyValue("");
            for (SettingDescriptor s : eventType.getSettingDescriptors()) {
                String settingsPath = eventType.getName() + "#" + s.getName();
                if (settings.containsKey(settingsPath)) {
                    sj.add(s.getName() + "=" + settings.get(settingsPath));
                }
            }
            String settingsText = sj.toString();
            if (!settingsText.isEmpty()) {
                print(" %s (%s)", eventType.getLabel(), eventType.getName());
                println();
                println("   " + settingsText);
            }
        }
    }

    private static List<EventType> sortByEventPath(Collection<EventType> events) {
        List<EventType> sorted = new ArrayList<>();
        sorted.addAll(events);
        sorted.sort(Comparator.comparing(EventType::getName));
        return sorted;
    }

    @Override
    public String[] printHelp() {
            // 0123456789001234567890012345678900123456789001234567890012345678900123456789001234567890
        return """
               Syntax : JFR.check [options]

               Options:

                 name     (Optional) Name of the flight recording. (STRING, no default value)

                 verbose  (Optional) Flag for printing the event settings for the recording
                          (BOOLEAN, false)

               Options must be specified using the <key> or <key>=<value> syntax.

               Example usage:

                $ jcmd <pid> JFR.check
                $ jcmd <pid> JFR.check verbose=true
                $ jcmd <pid> JFR.check name=1
                $ jcmd <pid> JFR.check name=benchmark
                $ jcmd <pid> JFR.check name=2 verbose=true

               """.lines().toArray(String[]::new);
    }

    @Override
    public Argument[] getArgumentInfos() {
        return new Argument[] {
            new Argument("name",
                "Recording name, e.g. \\\"My Recording\\\" or omit to see all recordings",
                "STRING", false, true, null, false),
            new Argument("verbose",
                "Print event settings for the recording(s)","BOOLEAN",
                false, true, "false", false)
        };
    }
}
