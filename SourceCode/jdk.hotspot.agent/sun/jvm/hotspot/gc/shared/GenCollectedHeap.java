/*
 * Copyright (c) 2000, 2022, Oracle and/or its affiliates. All rights reserved.
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

package sun.jvm.hotspot.gc.shared;

import java.io.*;
import java.util.*;

import sun.jvm.hotspot.debugger.*;
import sun.jvm.hotspot.gc.shared.*;
import sun.jvm.hotspot.runtime.*;
import sun.jvm.hotspot.types.*;
import sun.jvm.hotspot.utilities.*;
import sun.jvm.hotspot.utilities.Observable;
import sun.jvm.hotspot.utilities.Observer;

public abstract class GenCollectedHeap extends CollectedHeap {
  private static AddressField youngGenField;
  private static AddressField oldGenField;

  private static AddressField youngGenSpecField;
  private static AddressField oldGenSpecField;

  private static GenerationFactory genFactory;

  static {
    VM.registerVMInitializedObserver(new Observer() {
        public void update(Observable o, Object data) {
          initialize(VM.getVM().getTypeDataBase());
        }
      });
  }

  private static synchronized void initialize(TypeDataBase db) {
    Type type = db.lookupType("GenCollectedHeap");

    youngGenField = type.getAddressField("_young_gen");
    oldGenField = type.getAddressField("_old_gen");
    youngGenSpecField = type.getAddressField("_young_gen_spec");
    oldGenSpecField = type.getAddressField("_old_gen_spec");

    genFactory = new GenerationFactory();
  }

  public GenCollectedHeap(Address addr) {
    super(addr);
  }

  public int nGens() {
    return 2; // Young + Old
  }

  public Generation getGen(int i) {
    if (Assert.ASSERTS_ENABLED) {
      Assert.that((i == 0) || (i == 1), "Index " + i +
                  " out of range (should be 0 or 1)");
    }

    switch (i) {
    case 0:
      return genFactory.newObject(youngGenField.getValue(addr));
    case 1:
      return genFactory.newObject(oldGenField.getValue(addr));
    default:
      // no generation for i, and assertions disabled.
      return null;
    }
  }

  public boolean isIn(Address a) {
    for (int i = 0; i < nGens(); i++) {
      Generation gen = getGen(i);
      if (gen.isIn(a)) {
        return true;
      }
    }

    return false;
  }

  public long capacity() {
    long capacity = 0;
    for (int i = 0; i < nGens(); i++) {
      capacity += getGen(i).capacity();
    }
    return capacity;
  }

  public long used() {
    long used = 0;
    for (int i = 0; i < nGens(); i++) {
      used += getGen(i).used();
    }
    return used;
  }

  /** Package-private access to GenerationSpecs */
  GenerationSpec spec(int level) {
    if (Assert.ASSERTS_ENABLED) {
      Assert.that((level == 0) || (level == 1), "Index " + level +
                  " out of range (should be 0 or 1)");
    }

    if ((level != 0) && (level != 1)) {
      return null;
    }

    if (level == 0) {
      return VMObjectFactory.newObject(GenerationSpec.class,
              youngGenSpecField.getAddress());
    } else {
      return VMObjectFactory.newObject(GenerationSpec.class,
              oldGenSpecField.getAddress());
    }
  }

  public void liveRegionsIterate(LiveRegionsClosure closure) {
    // Run through all generations, obtaining bottom-top pairs.
    for (int i = 0; i < nGens(); i++) {
      Generation gen = getGen(i);
      gen.liveRegionsIterate(closure);
    }
  }

  public void printOn(PrintStream tty) {
    for (int i = 0; i < nGens(); i++) {
      tty.print("Gen " + i + ": ");
      getGen(i).printOn(tty);
      tty.println("Invocations: " + getGen(i).invocations());
      tty.println();
    }
  }
}
