/*
 * Copyright (c) 2016, Oracle and/or its affiliates. All rights reserved.
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
package jdk.tools.jlink.internal.plugins;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import jdk.tools.jlink.internal.ModuleSorter;
import jdk.tools.jlink.internal.Utils;
import jdk.tools.jlink.plugin.PluginException;
import jdk.tools.jlink.plugin.ResourcePool;
import jdk.tools.jlink.plugin.ResourcePoolBuilder;
import jdk.tools.jlink.plugin.ResourcePoolEntry;
import jdk.tools.jlink.plugin.ResourcePoolEntry.Type;
import jdk.tools.jlink.plugin.ResourcePoolModule;

/**
 * A plugin to de-duplicate the legal notices from JMOD files.
 *
 * For a de-duplicated legal notice, the actual copy will be in
 * the base module and with symbolic links in other modules.
 * On platform that does not support symbolic links, a file
 * will be created to contain the path to the linked target.
 */
public final class LegalNoticeFilePlugin extends AbstractPlugin {

    private static final String ERROR_IF_NOT_SAME_CONTENT = "error-if-not-same-content";
    private final Map<String, List<ResourcePoolEntry>> licenseOrNotice =
        new HashMap<>();

    private boolean errorIfNotSameContent = false;

    public LegalNoticeFilePlugin() {
        super("dedup-legal-notices");
    }

    @Override
    public Set<State> getState() {
        return EnumSet.of(State.AUTO_ENABLED, State.FUNCTIONAL);
    }

    @Override
    public void configure(Map<String, String> config) {
        String arg = config.get(getName());
        if (arg != null) {
            if (arg.equals(ERROR_IF_NOT_SAME_CONTENT)) {
                errorIfNotSameContent = true;
            } else {
                throw new IllegalArgumentException(getName() + ": " + arg);
            }
        }
    }

    @Override
    public ResourcePool transform(ResourcePool in, ResourcePoolBuilder out) {
        // Sort modules in the topological order
        // process all legal notices/licenses entries
        new ModuleSorter(in.moduleView())
            .sorted()
            .flatMap(ResourcePoolModule::entries)
            .filter(entry -> entry.type() == Type.LEGAL_NOTICE)
            .forEach(this::dedupLegalNoticeEntry);

        in.entries()
            .filter(entry -> entry.type() != Type.LEGAL_NOTICE)
            .forEach(out::add);

        licenseOrNotice.values().stream()
            .flatMap(List::stream)
            .forEach(out::add);
        return out.build();
    }

    private void dedupLegalNoticeEntry(ResourcePoolEntry entry) {
        Path path = Utils.getJRTFSPath(entry.path());
        Path filename = path.getFileName();

        List<ResourcePoolEntry> entries =
            licenseOrNotice.computeIfAbsent(filename.toString(), _k -> new ArrayList<>());

        Optional<ResourcePoolEntry> otarget = entries.stream()
            .filter(e -> e.linkedTarget() == null)
            .filter(e -> Arrays.equals(e.contentBytes(), entry.contentBytes()))
            .findFirst();
        if (otarget.isEmpty()) {
            if (errorIfNotSameContent) {
                // all legal notices of the same file name are expected
                // to contain the same content
                Optional<ResourcePoolEntry> ores =
                    entries.stream().filter(e -> e.linkedTarget() == null)
                           .findAny();

                if (ores.isPresent()) {
                    throw new PluginException(ores.get().path() + " " +
                        entry.path() + " contain different content");
                }
            }
            entries.add(entry);
        } else {
            entries.add(ResourcePoolEntry.createSymLink(entry.path(),
                                                        entry.type(),
                                                        otarget.get()));
        }
    }

    @Override
    public Category getType() {
        return Category.TRANSFORMER;
    }

    @Override
    public boolean hasArguments() {
        return true;
    }
}
