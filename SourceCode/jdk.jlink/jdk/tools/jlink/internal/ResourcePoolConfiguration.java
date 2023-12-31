/*
 * Copyright (c) 2016, 2020, Oracle and/or its affiliates. All rights reserved.
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

package jdk.tools.jlink.internal;

import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jdk.tools.jlink.plugin.ResourcePool;
import jdk.tools.jlink.plugin.ResourcePoolModule;

final class ResourcePoolConfiguration {
    private ResourcePoolConfiguration() {}

    private static ModuleDescriptor descriptorOf(ResourcePoolModule mod) {
        ModuleDescriptor md = mod.descriptor();

        // drop hashes
        ModuleDescriptor.Builder builder = ModuleDescriptor.newModule(md.name());
        md.requires()
          .forEach(builder::requires);
        md.exports()
          .forEach(builder::exports);
        md.opens()
          .forEach(builder::opens);
        md.uses()
          .forEach(builder::uses);
        md.provides()
          .forEach(builder::provides);
        builder.packages(md.packages());

        md.version().ifPresent(builder::version);
        md.mainClass().ifPresent(builder::mainClass);

        return builder.build();
    }

    private static ModuleReference moduleReference(ModuleDescriptor desc) {
        return new ModuleReference(desc, null) {
            @Override
            public ModuleReader open() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static Map<String, ModuleReference> allModRefs(ResourcePool pool) {
        return pool.moduleView().modules().
            collect(Collectors.toMap(ResourcePoolModule::name,
                m -> moduleReference(descriptorOf(m))));
    }

    private static void checkPackages(ResourcePool pool) {
        // check that each resource pool module's packages()
        // returns a set that is consistent with the module
        // descriptor of that module.

        pool.moduleView().modules().forEach(m -> {
            ModuleDescriptor desc = m.descriptor();
            if (!desc.packages().equals(m.packages())) {
                throw new RuntimeException("Module " + m.name() +
                   "'s descriptor indicates the set of packages is : " +
                   desc.packages() + ", but module contains packages: " +
                   m.packages());
            }
        });
    }

    static Configuration validate(ResourcePool pool) {
        checkPackages(pool);
        final Map<String, ModuleReference> nameToModRef = allModRefs(pool);
        final Set<ModuleReference> allRefs = new HashSet<>(nameToModRef.values());

        final ModuleFinder finder = new ModuleFinder() {
            @Override
            public Optional<ModuleReference> find(String name) {
                return Optional.ofNullable(nameToModRef.get(name));
            }

            @Override
            public Set<ModuleReference> findAll() {
                return allRefs;
            }
        };

        return Configuration.empty().resolve(
            finder, ModuleFinder.of(), nameToModRef.keySet());
    }
}
