/*
 * Copyright (c) 2015, 2023, Oracle and/or its affiliates. All rights reserved.
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

import java.util.Map;
import java.util.function.Function;

import jdk.tools.jlink.internal.ResourcePoolManager.ResourcePoolImpl;
import jdk.tools.jlink.plugin.ResourcePool;
import jdk.tools.jlink.plugin.ResourcePoolBuilder;
import jdk.tools.jlink.internal.ImagePluginStack;
import jdk.tools.jlink.internal.ResourcePoolManager;
import jdk.tools.jlink.internal.ResourcePrevisitor;
import jdk.tools.jlink.internal.StringTable;

/**
 *
 * ZIP and String Sharing compression plugin
 */
public final class DefaultCompressPlugin extends AbstractPlugin implements ResourcePrevisitor {
    public static final String FILTER = "filter";
    public static final String LEVEL_0 = "0";
    public static final String LEVEL_1 = "1";
    public static final String LEVEL_2 = "2";

    private StringSharingPlugin ss;
    private ZipPlugin zip;

    public DefaultCompressPlugin() {
        super("compress");
    }

    @Override
    public ResourcePool transform(ResourcePool in, ResourcePoolBuilder out) {
        if (ss != null && zip != null) {
            ResourcePoolManager resMgr = new ImagePluginStack.OrderedResourcePoolManager(
                    in.byteOrder(), ((ResourcePoolImpl)in).getStringTable());
            return zip.transform(ss.transform(in, resMgr.resourcePoolBuilder()), out);
        } else if (ss != null) {
            return ss.transform(in, out);
        } else if (zip != null) {
            return zip.transform(in, out);
        } else {
            in.transformAndCopy(Function.identity(), out);
            return out.build();
        }
    }

    @Override
    public void previsit(ResourcePool resources, StringTable strings) {
        if (ss != null) {
            ss.previsit(resources, strings);
        }
    }

    @Override
    public Category getType() {
        return Category.COMPRESSOR;
    }

    @Override
    public boolean hasArguments() {
        return true;
    }

    @Override
    public void configure(Map<String, String> config) {
        ResourceFilter resFilter = ResourceFilter.includeFilter(config.get(FILTER));
        String level = config.get(getName());
        if (level != null) {
            switch (level) {
                case LEVEL_0:
                    System.err.println(getMessage("compress.warn.argumentdeprecated", LEVEL_0));
                    ss = null;
                    zip = null;
                    break;
                case LEVEL_1:
                    System.err.println(getMessage("compress.warn.argumentdeprecated", LEVEL_1));
                    ss = new StringSharingPlugin(resFilter);
                    break;
                case LEVEL_2:
                    System.err.println(getMessage("compress.warn.argumentdeprecated", LEVEL_2));
                    zip = new ZipPlugin(resFilter);
                    break;
                default:
                    if (level.length() == 5 && level.startsWith("zip-")) {
                        try {
                            int zipLevel = Integer.parseInt(level.substring(4));
                            zip = new ZipPlugin(resFilter, zipLevel);
                            break;
                        } catch (NumberFormatException ignored) {}
                    }
                    throw new IllegalArgumentException("Invalid compression level " + level);
            }
        } else {
            throw new IllegalArgumentException("Invalid compression level " + level);
        }
    }
}
