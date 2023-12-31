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

package com.sun.tools.javac.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

/** Utilities for Iterators.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class Iterators {

    // cache the result of java.util.Collections.emptyIterator(), which
    // explicitly does not guarantee to return the same instance
    private static final Iterator<?> EMPTY = Collections.emptyIterator();

    @SuppressWarnings("unchecked")
    public static <T> Iterator<T> emptyIterator() {
        return (Iterator<T>) EMPTY;
    }

    public static <I, O> Iterator<O> createCompoundIterator(Iterable<I> inputs, Function<I, Iterator<O>> converter) {
        return new CompoundIterator<>(inputs, converter);
    }

    private static class CompoundIterator<I, O> implements Iterator<O> {

        private final Iterator<I> inputs;
        private final Function<I, Iterator<O>> converter;
        private Iterator<O> currentIterator = emptyIterator();

        public CompoundIterator(Iterable<I> inputs, Function<I, Iterator<O>> converter) {
            this.inputs = inputs.iterator();
            this.converter = converter;
        }

        @Override
        public boolean hasNext() {
            // if there's no element currently available, advance until there
            // is one or the input is exhausted
            for (;;) {
                if (currentIterator.hasNext())
                    return true;
                else if (inputs.hasNext())
                    currentIterator = converter.apply(inputs.next());
                else
                    return false;
            }
        }

        @Override
        public O next() {
            // next() cannot assume hasNext() was called immediately before:
            // next() must itself be able to find the next available element
            // if there is one
            while (!currentIterator.hasNext() && inputs.hasNext()) {
                currentIterator = converter.apply(inputs.next());
            }
            return currentIterator.next();
        }
    }

    // input.next() is assumed to never return null
    public static <E> Iterator<E> createFilterIterator(Iterator<E> input, Predicate<E> test) {
        return new Iterator<>() {
            private E current = update();
            private E update () {
                while (input.hasNext()) {
                    E sym = input.next();
                    if (test.test(sym)) {
                        return sym;
                    }
                }

                return null;
            }
            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                E res = current;
                current = update();
                return res;
            }
        };
    }

}
