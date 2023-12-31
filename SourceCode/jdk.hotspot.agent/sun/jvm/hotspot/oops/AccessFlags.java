/*
 * Copyright (c) 2000, 2023, Oracle and/or its affiliates. All rights reserved.
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
 */

package sun.jvm.hotspot.oops;

import sun.jvm.hotspot.runtime.ClassConstants;
import java.io.*;

public class AccessFlags implements /* imports */ ClassConstants {
  public AccessFlags(long flags) {
    this.flags = flags;
  }

  private long flags;

  // Java access flags
  public boolean isPublic      () { return (flags & JVM_ACC_PUBLIC      ) != 0; }
  public boolean isPrivate     () { return (flags & JVM_ACC_PRIVATE     ) != 0; }
  public boolean isProtected   () { return (flags & JVM_ACC_PROTECTED   ) != 0; }
  public boolean isStatic      () { return (flags & JVM_ACC_STATIC      ) != 0; }
  public boolean isFinal       () { return (flags & JVM_ACC_FINAL       ) != 0; }
  public boolean isSynchronized() { return (flags & JVM_ACC_SYNCHRONIZED) != 0; }
  public boolean isSuper       () { return (flags & JVM_ACC_SUPER       ) != 0; }
  public boolean isVolatile    () { return (flags & JVM_ACC_VOLATILE    ) != 0; }
  public boolean isBridge      () { return (flags & JVM_ACC_BRIDGE      ) != 0; }
  public boolean isTransient   () { return (flags & JVM_ACC_TRANSIENT   ) != 0; }
  public boolean isVarArgs     () { return (flags & JVM_ACC_VARARGS     ) != 0; }
  public boolean isNative      () { return (flags & JVM_ACC_NATIVE      ) != 0; }
  public boolean isEnum        () { return (flags & JVM_ACC_ENUM        ) != 0; }
  public boolean isAnnotation  () { return (flags & JVM_ACC_ANNOTATION  ) != 0; }
  public boolean isInterface   () { return (flags & JVM_ACC_INTERFACE   ) != 0; }
  public boolean isAbstract    () { return (flags & JVM_ACC_ABSTRACT    ) != 0; }
  public boolean isStrict      () { return (flags & JVM_ACC_STRICT      ) != 0; }
  public boolean isSynthetic   () { return (flags & JVM_ACC_SYNTHETIC   ) != 0; }

  public long getValue         () { return flags; }

  // Klass* flags
  public boolean hasFinalizer         () { return (flags & JVM_ACC_HAS_FINALIZER          ) != 0; }
  public boolean isCloneable          () { return (flags & JVM_ACC_IS_CLONEABLE           ) != 0; }

  public void printOn(PrintStream tty) {
    // prints only .class flags and not the hotspot internal flags
    if (isPublic      ()) tty.print("public "      );
    if (isPrivate     ()) tty.print("private "     );
    if (isProtected   ()) tty.print("protected "   );
    if (isStatic      ()) tty.print("static "      );
    if (isFinal       ()) tty.print("final "       );
    if (isSynchronized()) tty.print("synchronized ");
    if (isVolatile    ()) tty.print("volatile "    );
    if (isBridge      ()) tty.print("bridge "      );
    if (isTransient   ()) tty.print("transient "   );
    if (isVarArgs     ()) tty.print("varargs "     );
    if (isNative      ()) tty.print("native "      );
    if (isEnum        ()) tty.print("enum "        );
    if (isInterface   ()) tty.print("interface "   );
    if (isAbstract    ()) tty.print("abstract "    );
    if (isStrict      ()) tty.print("strict "      );
    if (isSynthetic   ()) tty.print("synthetic "   );
  }

  // get flags written to .class files
  public int getStandardFlags() {
    return (int) (flags & JVM_ACC_WRITTEN_FLAGS);
  }
}
