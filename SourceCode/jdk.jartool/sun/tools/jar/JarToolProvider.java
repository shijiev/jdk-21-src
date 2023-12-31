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

package sun.tools.jar;

import java.io.PrintWriter;
import java.util.Optional;
import java.util.spi.ToolProvider;

public class JarToolProvider implements ToolProvider {
    public String name() {
        return "jar";
    }

    public Optional<String> description() {
        return Optional.of(Main.getMsg("jar.description"));
    }

    public int run(PrintWriter out, PrintWriter err, String... args) {
        boolean ok = new Main(out, err, name()).run(args);
        return ok ? 0 : 1;
    }
}
