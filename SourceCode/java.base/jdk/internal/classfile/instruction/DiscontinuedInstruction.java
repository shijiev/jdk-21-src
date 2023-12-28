/*
 * Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
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
package jdk.internal.classfile.instruction;

import jdk.internal.classfile.CodeElement;
import jdk.internal.classfile.CodeModel;
import jdk.internal.classfile.Instruction;
import jdk.internal.classfile.Label;
import jdk.internal.classfile.Opcode;
import jdk.internal.classfile.impl.AbstractInstruction;
import jdk.internal.classfile.impl.Util;

/**
 * Models instruction discontinued from the {@code code} array of a {@code Code}
 * attribute. Delivered as a {@link CodeElement} when traversing the elements of
 * a {@link CodeModel}.
 */
public sealed interface DiscontinuedInstruction extends Instruction {

    /**
     * Models JSR and JSR_W instructions discontinued from the {@code code}
     * array of a {@code Code} attribute since class file version 51.0.
     * Corresponding opcodes will have a {@code kind} of
     * {@link Opcode.Kind#DISCONTINUED_JSR}.  Delivered as a {@link CodeElement}
     * when traversing the elements of a {@link CodeModel}.
     */
    sealed interface JsrInstruction extends DiscontinuedInstruction
            permits AbstractInstruction.BoundJsrInstruction,
                    AbstractInstruction.UnboundJsrInstruction {

        /**
         * {@return the target of the JSR instruction}
         */
        Label target();

        /**
         * {@return a JSR instruction}
         *
         * @param op the opcode for the specific type of JSR instruction,
         *           which must be of kind {@link Opcode.Kind#DISCONTINUED_JSR}
         * @param target target label of the subroutine
         */
        static JsrInstruction of(Opcode op, Label target) {
            Util.checkKind(op, Opcode.Kind.DISCONTINUED_JSR);
            return new AbstractInstruction.UnboundJsrInstruction(op, target);
        }

        /**
         * {@return a JSR instruction}
         *
         * @param target target label of the subroutine
         */
        static JsrInstruction of(Label target) {
            return of(Opcode.JSR, target);
        }
    }

    /**
     * Models RET and RET_W instructions discontinued from the {@code code}
     * array of a {@code Code} attribute since class file version 51.0.
     * Corresponding opcodes will have a {@code kind} of
     * {@link Opcode.Kind#DISCONTINUED_RET}.  Delivered as a {@link CodeElement}
     * when traversing the elements of a {@link CodeModel}.
     */
    sealed interface RetInstruction extends DiscontinuedInstruction
            permits AbstractInstruction.BoundRetInstruction,
                    AbstractInstruction.UnboundRetInstruction {

        /**
         * {@return the local variable slot with return address}
         */
        int slot();

        /**
         * {@return a RET or RET_W instruction}
         *
         * @param op the opcode for the specific type of RET instruction,
         *           which must be of kind {@link Opcode.Kind#DISCONTINUED_RET}
         * @param slot the local variable slot to load return address from
         */
        static RetInstruction of(Opcode op, int slot) {
            Util.checkKind(op, Opcode.Kind.DISCONTINUED_RET);
            return new AbstractInstruction.UnboundRetInstruction(op, slot);
        }

        /**
         * {@return a RET instruction}
         *
         * @param slot the local variable slot to load return address from
         */
        static RetInstruction of(int slot) {
            return of(slot < 256 ? Opcode.RET : Opcode.RET_W, slot);
        }
    }
}
