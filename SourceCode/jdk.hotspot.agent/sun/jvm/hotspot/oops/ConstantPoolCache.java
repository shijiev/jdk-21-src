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

import java.io.*;
import java.util.*;
import sun.jvm.hotspot.debugger.*;
import sun.jvm.hotspot.runtime.*;
import sun.jvm.hotspot.types.*;
import sun.jvm.hotspot.utilities.*;
import sun.jvm.hotspot.utilities.Observable;
import sun.jvm.hotspot.utilities.Observer;

//  ConstantPoolCache : A constant pool cache (ConstantPoolCache).
//  See cpCache.hpp for details about this class.
//
public class ConstantPoolCache extends Metadata {
  static {
    VM.registerVMInitializedObserver(new Observer() {
        public void update(Observable o, Object data) {
          initialize(VM.getVM().getTypeDataBase());
        }
      });
  }

  private static synchronized void initialize(TypeDataBase db) throws WrongTypeException {
    Type type      = db.lookupType("ConstantPoolCache");
    constants      = new MetadataField(type.getAddressField("_constant_pool"), 0);
    baseOffset     = type.getSize();
    Type elType    = db.lookupType("ConstantPoolCacheEntry");
    elementSize    = elType.getSize();
    length         = new CIntField(type.getCIntegerField("_length"), 0);
    intSize        = VM.getVM().getObjectHeap().getIntSize();
    resolvedReferences = type.getAddressField("_resolved_references");
    referenceMap   = type.getAddressField("_reference_map");
    resolvedIndyArray = type.getAddressField("_resolved_indy_entries");
  }

  public ConstantPoolCache(Address addr) {
    super(addr);
  }

  public boolean isConstantPoolCache() { return true; }

  private static MetadataField constants;

  private static long baseOffset;
  private static long elementSize;
  private static CIntField length;
  private static long intSize;
  private static AddressField  resolvedReferences;
  private static AddressField  referenceMap;
  private static AddressField  resolvedIndyArray;

  public ConstantPool getConstants() { return (ConstantPool) constants.getValue(this); }

  public long getSize() {
    return alignSize(baseOffset + getLength() * elementSize);
  }

  public ConstantPoolCacheEntry getEntryAt(int i) {
    Objects.checkIndex(i, getLength());
    return new ConstantPoolCacheEntry(this, i);
  }

  public ResolvedIndyEntry getIndyEntryAt(int i) {
    Address addr = resolvedIndyArray.getValue(getAddress());
    ResolvedIndyArray array = new ResolvedIndyArray(addr);
    return array.getAt(i);
  }

  public int getIntAt(int entry, int fld) {
    long offset = baseOffset + entry * elementSize + fld * intSize;
    return (int) getAddress().getCIntegerAt(offset, intSize, true );
  }


  public void printValueOn(PrintStream tty) {
    tty.print("ConstantPoolCache for " + getConstants().getPoolHolder().getName().asString() + " address = " + getAddress() + " offset = " + baseOffset);
  }

  public int getLength() {
    return (int) length.getValue(getAddress());
  }

  public void iterateFields(MetadataVisitor visitor) {
    super.iterateFields(visitor);
    visitor.doMetadata(constants, true);
      for (int i = 0; i < getLength(); i++) {
        ConstantPoolCacheEntry entry = getEntryAt(i);
        entry.iterateFields(visitor);
      }
    }

  public Oop getResolvedReferences() {
    Address handle = resolvedReferences.getValue(getAddress());
    if (handle != null) {
      // Load through the handle
      OopHandle refs = handle.getOopHandleAt(0);
      return VM.getVM().getObjectHeap().newOop(refs);
    }
    return null;
  }

  public U2Array referenceMap() {
    return new U2Array(referenceMap.getValue(getAddress()));
  }
};
