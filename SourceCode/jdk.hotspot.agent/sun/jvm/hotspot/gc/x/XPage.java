/*
 * Copyright (c) 2018, 2021, Oracle and/or its affiliates. All rights reserved.
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

package sun.jvm.hotspot.gc.x;

import java.util.ArrayList;
import java.util.List;

import sun.jvm.hotspot.debugger.Address;
import sun.jvm.hotspot.debugger.OopHandle;
import sun.jvm.hotspot.gc.shared.LiveRegionsProvider;
import sun.jvm.hotspot.memory.MemRegion;
import sun.jvm.hotspot.oops.Oop;
import sun.jvm.hotspot.oops.UnknownOopException;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.runtime.VMObject;
import sun.jvm.hotspot.runtime.VMObjectFactory;
import sun.jvm.hotspot.types.AddressField;
import sun.jvm.hotspot.types.CIntegerField;
import sun.jvm.hotspot.types.Type;
import sun.jvm.hotspot.types.TypeDataBase;

public class XPage extends VMObject implements LiveRegionsProvider {
    private static CIntegerField typeField;
    private static CIntegerField seqnumField;
    private static long virtualFieldOffset;
    private static AddressField topField;

    static {
        VM.registerVMInitializedObserver((o, d) -> initialize(VM.getVM().getTypeDataBase()));
    }

    private static synchronized void initialize(TypeDataBase db) {
        Type type = db.lookupType("XPage");

        typeField = type.getCIntegerField("_type");
        seqnumField = type.getCIntegerField("_seqnum");
        virtualFieldOffset = type.getField("_virtual").getOffset();
        topField = type.getAddressField("_top");
    }

    public XPage(Address addr) {
        super(addr);
    }

    private byte type() {
        return typeField.getJByte(addr);
    }

    private int seqnum() {
        return seqnumField.getJInt(addr);
    }

    private XVirtualMemory virtual() {
        return VMObjectFactory.newObject(XVirtualMemory.class, addr.addOffsetTo(virtualFieldOffset));
    }

    private Address top() {
        return topField.getValue(addr);
    }

    private boolean is_relocatable() {
        return seqnum() < XGlobals.XGlobalSeqNum();
    }

    long start() {
        return virtual().start();
    }

    long size() {
        return virtual().end() - virtual().start();
    }

    long object_alignment_shift() {
        if (type() == XGlobals.XPageTypeSmall) {
            return XGlobals.XObjectAlignmentSmallShift();
        } else if (type() == XGlobals.XPageTypeMedium) {
            return XGlobals.XObjectAlignmentMediumShift;
        } else {
            assert(type() == XGlobals.XPageTypeLarge);
            return XGlobals.XObjectAlignmentLargeShift;
        }
    }

    long objectAlignmentSize() {
        return 1 << object_alignment_shift();
    }

    public boolean isIn(Address addr) {
        long offset = XAddress.offset(addr);
        // FIXME: it does not consider the sign.
        return (offset >= start()) && (offset < top().asLongValue());
    }

    private long getObjectSize(Address good) {
        OopHandle handle = good.addOffsetToAsOopHandle(0);
        Oop obj = null;

        try {
           obj = VM.getVM().getObjectHeap().newOop(handle);
        } catch (UnknownOopException exp) {
          throw new RuntimeException(" UnknownOopException  " + exp);
        }

        return VM.getVM().alignUp(obj.getObjectSize(), objectAlignmentSize());
    }

    public List<MemRegion> getLiveRegions() {
        Address start = XAddress.good(XUtils.longToAddress(start()));

        // Can't convert top() to a "good" address because it might
        // be at the top of the "offset" range, and therefore also
        // looks like one of the color bits. Instead use the "good"
        // address and add the size.
        long size = top().asLongValue() - start();
        Address end = start.addOffsetTo(size);

        return List.of(new MemRegion(start, end));
    }
}
