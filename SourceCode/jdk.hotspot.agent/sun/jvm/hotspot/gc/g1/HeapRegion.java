/*
 * Copyright (c) 2011, 2023, Oracle and/or its affiliates. All rights reserved.
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

package sun.jvm.hotspot.gc.g1;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import sun.jvm.hotspot.utilities.Observable;
import sun.jvm.hotspot.utilities.Observer;
import sun.jvm.hotspot.debugger.Address;
import sun.jvm.hotspot.debugger.OopHandle;
import sun.jvm.hotspot.gc.shared.ContiguousSpace;
import sun.jvm.hotspot.gc.shared.LiveRegionsProvider;
import sun.jvm.hotspot.memory.MemRegion;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.runtime.VMObjectFactory;
import sun.jvm.hotspot.types.AddressField;
import sun.jvm.hotspot.types.CIntegerField;
import sun.jvm.hotspot.types.Type;
import sun.jvm.hotspot.types.TypeDataBase;

// Mirror class for HeapRegion. Currently we don't actually include
// any of its fields but only iterate over it.

public class HeapRegion extends ContiguousSpace implements LiveRegionsProvider {
    private static AddressField bottomField;
    private static AddressField topField;
    private static AddressField endField;

    private static CIntegerField grainBytesField;
    private static long typeFieldOffset;
    private static long pointerSize;

    private HeapRegionType type;

    static {
        VM.registerVMInitializedObserver(new Observer() {
                public void update(Observable o, Object data) {
                    initialize(VM.getVM().getTypeDataBase());
                }
            });
    }

    private static synchronized void initialize(TypeDataBase db) {
        Type type = db.lookupType("HeapRegion");

        bottomField = type.getAddressField("_bottom");
        topField = type.getAddressField("_top");
        endField = type.getAddressField("_end");

        grainBytesField = type.getCIntegerField("GrainBytes");
        typeFieldOffset = type.getField("_type").getOffset();

        pointerSize = db.lookupType("HeapRegion*").getSize();
    }

    public static long grainBytes() {
        return grainBytesField.getValue();
    }

    public HeapRegion(Address addr) {
        super(addr);
        Address typeAddr = (addr instanceof OopHandle) ? addr.addOffsetToAsOopHandle(typeFieldOffset)
                                                       : addr.addOffsetTo(typeFieldOffset);
        type = VMObjectFactory.newObject(HeapRegionType.class, typeAddr);
    }

    public Address bottom()        { return bottomField.getValue(addr); }
    public Address top()           { return topField.getValue(addr); }
    public Address end()           { return endField.getValue(addr); }

    @Override
    public List<MemRegion> getLiveRegions() {
        List<MemRegion> res = new ArrayList<>();
        res.add(new MemRegion(bottom(), top()));
        return res;
    }

    /** Returns a subregion of the space containing all the objects in
        the space. */
    public MemRegion usedRegion() {
        return new MemRegion(bottom(), end());
    }

    public long used() {
        return top().minus(bottom());
    }

    public long free() {
        return end().minus(top());
    }

    public boolean isFree() {
        return type.isFree();
    }

    public boolean isYoung() {
        return type.isYoung();
    }

    public boolean isHumongous() {
        return type.isHumongous();
    }

    public boolean isOld() {
        return type.isOld();
    }

    public static long getPointerSize() {
        return pointerSize;
    }

    public void printOn(PrintStream tty) {
        tty.print("Region: " + bottom() + "," + top() + "," + end());
        tty.println(":" + type.typeAnnotation());
    }
}
