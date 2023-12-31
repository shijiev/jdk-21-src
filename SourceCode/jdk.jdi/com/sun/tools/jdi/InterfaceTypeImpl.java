/*
 * Copyright (c) 1998, 2021, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.tools.jdi;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sun.jdi.ClassType;
import com.sun.jdi.InterfaceType;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;

public final class InterfaceTypeImpl extends InvokableTypeImpl
                                     implements InterfaceType {

    private static class IResult implements InvocationResult {
        private final JDWP.InterfaceType.InvokeMethod rslt;

        public IResult(JDWP.InterfaceType.InvokeMethod rslt) {
            this.rslt = rslt;
        }

        @Override
        public ObjectReferenceImpl getException() {
            return rslt.exception;
        }

        @Override
        public ValueImpl getResult() {
            return rslt.returnValue;
        }

    }

    private SoftReference<List<InterfaceType>> superinterfacesRef = null;

    protected InterfaceTypeImpl(VirtualMachine aVm,long aRef) {
        super(aVm, aRef);
    }

    public List<InterfaceType> superinterfaces() {
        List<InterfaceType> superinterfaces = (superinterfacesRef == null) ? null :
                                     superinterfacesRef.get();
        if (superinterfaces == null) {
            superinterfaces = getInterfaces();
            superinterfaces = Collections.unmodifiableList(superinterfaces);
            superinterfacesRef = new SoftReference<List<InterfaceType>>(superinterfaces);
        }
        return superinterfaces;
    }

    public List<InterfaceType> subinterfaces() {
        List<InterfaceType> subs = new ArrayList<InterfaceType>();
        vm.forEachClass(refType -> {
            if (refType instanceof InterfaceType) {
                InterfaceType interfaze = (InterfaceType)refType;
                if (interfaze.isPrepared() && interfaze.superinterfaces().contains(this)) {
                    subs.add(interfaze);
                }
            }
        });
        return subs;
    }

    public List<ClassType> implementors() {
        List<ClassType> implementors = new ArrayList<ClassType>();
        vm.forEachClass(refType -> {
            if (refType instanceof ClassType) {
                ClassType clazz = (ClassType)refType;
                if (clazz.isPrepared() && clazz.interfaces().contains(this)) {
                    implementors.add(clazz);
                }
            }
        });
        return implementors;
    }

    public boolean isInitialized() {
        return isPrepared();
    }

    public String toString() {
       return "interface " + name() + " (" + loaderString() + ")";
    }

    @Override
    InvocationResult waitForReply(PacketStream stream) throws JDWPException {
        return new IResult(JDWP.InterfaceType.InvokeMethod.waitForReply(vm, stream));
    }

    @Override
    CommandSender getInvokeMethodSender(final ThreadReferenceImpl thread,
                                        final MethodImpl method,
                                        final ValueImpl[] args,
                                        final int options) {
        return () ->
            JDWP.InterfaceType.InvokeMethod.enqueueCommand(vm,
                                                           InterfaceTypeImpl.this,
                                                           thread,
                                                           method.ref(),
                                                           args,
                                                           options);
    }

    @Override
    ClassType superclass() {
        return null;
    }

    @Override
    boolean isAssignableTo(ReferenceType type) {
        if (type.name().equals("java.lang.Object")) {
            // interfaces are always assignable to j.l.Object
            return true;
        }
        return super.isAssignableTo(type);
    }

    @Override
    List<InterfaceType> interfaces() {
        return superinterfaces();
    }

    @Override
    boolean canInvoke(Method method) {
        // method must be directly in this interface
        return this.equals(method.declaringType());
    }
}
