/*
 * Copyright (c) 2019, 2022, Oracle and/or its affiliates. All rights reserved.
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
package java.lang.invoke;

import jdk.internal.foreign.AbstractMemorySegmentImpl;
import jdk.internal.misc.ScopedMemoryAccess;
import jdk.internal.vm.annotation.ForceInline;

import java.lang.foreign.MemorySegment;
import java.lang.ref.Reference;

import java.util.Objects;

import static java.lang.invoke.MethodHandleStatics.UNSAFE;

// -- This file was mechanically generated: Do not edit! -- //

final class VarHandleSegmentAsShorts extends VarHandleSegmentViewBase {

    static final boolean BE = UNSAFE.isBigEndian();

    static final ScopedMemoryAccess SCOPED_MEMORY_ACCESS = ScopedMemoryAccess.getScopedMemoryAccess();

    static final int VM_ALIGN = Short.BYTES - 1;

    static final VarForm FORM = new VarForm(VarHandleSegmentAsShorts.class, MemorySegment.class, short.class, long.class);

    VarHandleSegmentAsShorts(boolean be, long length, long alignmentMask, boolean exact) {
        super(FORM, be, length, alignmentMask, exact);
    }

    @Override
    final MethodType accessModeTypeUncached(VarHandle.AccessType accessType) {
        return accessType.accessModeType(MemorySegment.class, short.class, long.class);
    }

    @Override
    public VarHandleSegmentAsShorts withInvokeExactBehavior() {
        return hasInvokeExactBehavior() ?
                this :
                new VarHandleSegmentAsShorts(be, length, alignmentMask, true);
    }

    @Override
    public VarHandleSegmentAsShorts withInvokeBehavior() {
        return !hasInvokeExactBehavior() ?
                this :
                new VarHandleSegmentAsShorts(be, length, alignmentMask, false);
    }

    @ForceInline
    static short convEndian(boolean big, short n) {
        return big == BE ? n : Short.reverseBytes(n);
    }

    @ForceInline
    static AbstractMemorySegmentImpl checkAddress(Object obb, long offset, long length, boolean ro) {
        AbstractMemorySegmentImpl oo = (AbstractMemorySegmentImpl)Objects.requireNonNull(obb);
        oo.checkAccess(offset, length, ro);
        return oo;
    }

    @ForceInline
    static long offset(AbstractMemorySegmentImpl bb, long offset, long alignmentMask) {
        long address = offsetNoVMAlignCheck(bb, offset, alignmentMask);
        if ((address & VM_ALIGN) != 0) {
            throw VarHandleSegmentViewBase.newIllegalArgumentExceptionForMisalignedAccess(address);
        }
        return address;
    }

    @ForceInline
    static long offsetNoVMAlignCheck(AbstractMemorySegmentImpl bb, long offset, long alignmentMask) {
        long base = bb.unsafeGetOffset();
        long address = base + offset;
        long maxAlignMask = bb.maxAlignMask();
        if (((address | maxAlignMask) & alignmentMask) != 0) {
            throw VarHandleSegmentViewBase.newIllegalArgumentExceptionForMisalignedAccess(address);
        }
        return address;
    }

    @ForceInline
    static short get(VarHandle ob, Object obb, long base) {
        VarHandleSegmentViewBase handle = (VarHandleSegmentViewBase)ob;
        AbstractMemorySegmentImpl bb = checkAddress(obb, base, handle.length, true);
        return SCOPED_MEMORY_ACCESS.getShortUnaligned(bb.sessionImpl(),
                bb.unsafeGetBase(),
                offsetNoVMAlignCheck(bb, base, handle.alignmentMask),
                handle.be);
    }

    @ForceInline
    static void set(VarHandle ob, Object obb, long base, short value) {
        VarHandleSegmentViewBase handle = (VarHandleSegmentViewBase)ob;
        AbstractMemorySegmentImpl bb = checkAddress(obb, base, handle.length, false);
        SCOPED_MEMORY_ACCESS.putShortUnaligned(bb.sessionImpl(),
                bb.unsafeGetBase(),
                offsetNoVMAlignCheck(bb, base, handle.alignmentMask),
                value,
                handle.be);
    }

    @ForceInline
    static short getVolatile(VarHandle ob, Object obb, long base) {
        VarHandleSegmentViewBase handle = (VarHandleSegmentViewBase)ob;
        AbstractMemorySegmentImpl bb = checkAddress(obb, base, handle.length, true);
        return convEndian(handle.be,
                          SCOPED_MEMORY_ACCESS.getShortVolatile(bb.sessionImpl(),
                                  bb.unsafeGetBase(),
                                  offset(bb, base, handle.alignmentMask)));
    }

    @ForceInline
    static void setVolatile(VarHandle ob, Object obb, long base, short value) {
        VarHandleSegmentViewBase handle = (VarHandleSegmentViewBase)ob;
        AbstractMemorySegmentImpl bb = checkAddress(obb, base, handle.length, false);
        SCOPED_MEMORY_ACCESS.putShortVolatile(bb.sessionImpl(),
                bb.unsafeGetBase(),
                offset(bb, base, handle.alignmentMask),
                convEndian(handle.be, value));
    }

    @ForceInline
    static short getAcquire(VarHandle ob, Object obb, long base) {
        VarHandleSegmentViewBase handle = (VarHandleSegmentViewBase)ob;
        AbstractMemorySegmentImpl bb = checkAddress(obb, base, handle.length, true);
        return convEndian(handle.be,
                          SCOPED_MEMORY_ACCESS.getShortAcquire(bb.sessionImpl(),
                                  bb.unsafeGetBase(),
                                  offset(bb, base, handle.alignmentMask)));
    }

    @ForceInline
    static void setRelease(VarHandle ob, Object obb, long base, short value) {
        VarHandleSegmentViewBase handle = (VarHandleSegmentViewBase)ob;
        AbstractMemorySegmentImpl bb = checkAddress(obb, base, handle.length, false);
        SCOPED_MEMORY_ACCESS.putShortRelease(bb.sessionImpl(),
                bb.unsafeGetBase(),
                offset(bb, base, handle.alignmentMask),
                convEndian(handle.be, value));
    }

    @ForceInline
    static short getOpaque(VarHandle ob, Object obb, long base) {
        VarHandleSegmentViewBase handle = (VarHandleSegmentViewBase)ob;
        AbstractMemorySegmentImpl bb = checkAddress(obb, base, handle.length, true);
        return convEndian(handle.be,
                          SCOPED_MEMORY_ACCESS.getShortOpaque(bb.sessionImpl(),
                                  bb.unsafeGetBase(),
                                  offset(bb, base, handle.alignmentMask)));
    }

    @ForceInline
    static void setOpaque(VarHandle ob, Object obb, long base, short value) {
        VarHandleSegmentViewBase handle = (VarHandleSegmentViewBase)ob;
        AbstractMemorySegmentImpl bb = checkAddress(obb, base, handle.length, false);
        SCOPED_MEMORY_ACCESS.putShortOpaque(bb.sessionImpl(),
                bb.unsafeGetBase(),
                offset(bb, base, handle.alignmentMask),
                convEndian(handle.be, value));
    }
}
