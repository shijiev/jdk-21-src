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

package jdk.jfr.internal.consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jdk.jfr.EventType;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.internal.LongMap;
import jdk.jfr.internal.MetadataDescriptor;
import jdk.jfr.internal.PrivateAccess;
import jdk.jfr.internal.Type;

/**
 * Class that create parsers suitable for reading events and constant pools
 */
final class ParserFactory {
    private final LongMap<Parser> parsers = new LongMap<>();
    private final TimeConverter timeConverter;
    private final LongMap<Type> types = new LongMap<>();
    private final LongMap<ConstantLookup> constantLookups;

    public ParserFactory(MetadataDescriptor metadata, LongMap<ConstantLookup> constantLookups, TimeConverter timeConverter) throws IOException {
        this.constantLookups = constantLookups;
        this.timeConverter = timeConverter;
        for (Type t : metadata.getTypes()) {
            types.put(t.getId(), t);
        }
        // Add to separate list
        // so createCompositeParser can throw
        // IOException outside lambda
        List<Type> typeList = new ArrayList<>();
        types.forEach(typeList::add);
        for (Type t : typeList) {
            if (!t.getFields().isEmpty()) { // Avoid primitives
                CompositeParser cp = createCompositeParser(t, false);
                if (t.isSimpleType()) { // Reduce to nested parser
                    parsers.put(t.getId(), cp.parsers[0]);
                }
            }
        }
        // Override event types with event parsers
        for (EventType t : metadata.getEventTypes()) {
            parsers.put(t.getId(), createEventParser(t));
        }
    }

    public LongMap<Parser> getParsers() {
        return parsers;
    }

    public LongMap<Type> getTypeMap() {
        return types;
    }

    private EventParser createEventParser(EventType eventType) throws IOException {
        List<Parser> parsers = new ArrayList<Parser>();
        for (ValueDescriptor f : eventType.getFields()) {
            parsers.add(createParser(f, true));
        }
        return new EventParser(timeConverter, eventType, parsers.toArray(new Parser[0]));
    }

    private Parser createParser(ValueDescriptor v, boolean event) throws IOException {
        boolean constantPool = PrivateAccess.getInstance().isConstantPool(v);
        if (v.isArray()) {
            Type valueType = PrivateAccess.getInstance().getType(v);
            ValueDescriptor element = PrivateAccess.getInstance().newValueDescriptor(v.getName(), valueType, v.getAnnotationElements(), 0, constantPool, null);
            return new ArrayParser(createParser(element, event));
        }
        long id = v.getTypeId();
        Type type = types.get(id);
        if (type == null) {
            throw new IOException("Type '" + v.getTypeName() + "' is not defined");
        }
        if (constantPool) {
            ConstantLookup lookup = constantLookups.get(id);
            if (lookup == null) {
                ConstantMap pool = new ConstantMap(ObjectFactory.create(type, timeConverter), type);
                lookup = new ConstantLookup(pool, type);
                constantLookups.put(id, lookup);
            }
            if (event) {
                return new EventValueConstantParser(lookup);
            }
            return new ConstantValueParser(lookup);
        }
        Parser parser = parsers.get(id);
        if (parser == null) {
            if (!v.getFields().isEmpty()) {
                return createCompositeParser(type, event);
            } else {
                return registerParserType(type, createPrimitiveParser(type, constantPool));
            }
        }
        return parser;
    }

    private Parser createPrimitiveParser(Type type, boolean event) throws IOException {
        return switch (type.getName()) {
            case "int" ->  new IntegerParser();
            case "long" -> new LongParser();
            case "float" ->  new FloatParser();
            case "double" -> new DoubleParser();
            case "char" ->  new CharacterParser();
            case "boolean" -> new BooleanParser();
            case "short" -> new ShortParser();
            case "byte" ->  new ByteParser();
            case "java.lang.String" -> {
                ConstantMap pool = new ConstantMap(ObjectFactory.create(type, timeConverter), type);
                ConstantLookup lookup = new ConstantLookup(pool, type);
                constantLookups.put(type.getId(), lookup);
                yield new StringParser(lookup, event);
            }
            default ->  throw new IOException("Unknown primitive type " + type.getName());
        };
    }

    private Parser registerParserType(Type t, Parser parser) {
        Parser p = parsers.get(t.getId());
        // check if parser exists (known type)
        if (p != null) {
            return p;
        }
        parsers.put(t.getId(), parser);
        return parser;
    }

    private CompositeParser createCompositeParser(Type type, boolean event) throws IOException {
        List<ValueDescriptor> vds = type.getFields();
        Parser[] parsers = new Parser[vds.size()];
        CompositeParser composite = new CompositeParser(parsers);
        // need to pre-register so recursive types can be handled
        registerParserType(type, composite);

        int index = 0;
        for (ValueDescriptor vd : vds) {
            parsers[index++] = createParser(vd, event);
        }
        return composite;
    }

    private static final class BooleanParser extends Parser {
        @Override
        public Object parse(RecordingInput input) throws IOException {
            return input.readBoolean() ? Boolean.TRUE : Boolean.FALSE;
        }

        @Override
        public void skip(RecordingInput input) throws IOException {
            input.skipBytes(1);
        }
    }

    private static final class ByteParser extends Parser {
        @Override
        public Object parse(RecordingInput input) throws IOException {
            return Byte.valueOf(input.readByte());
        }

        @Override
        public void skip(RecordingInput input) throws IOException {
            input.skipBytes(1);
        }
    }

    private static final class LongParser extends Parser {
        private Object lastLongObject = Long.valueOf(0);
        private long last = 0;

        @Override
        public Object parse(RecordingInput input) throws IOException {
            long l = input.readLong();
            if (l == last) {
                return lastLongObject;
            }
            last = l;
            lastLongObject = Long.valueOf(l);
            return lastLongObject;
        }

        @Override
        public void skip(RecordingInput input) throws IOException {
            input.readLong();
        }
    }

    private static final class IntegerParser extends Parser {
        private Integer lastIntegergObject = Integer.valueOf(0);
        private int last = 0;

        @Override
        public Object parse(RecordingInput input) throws IOException {
            int i = input.readInt();
            if (i != last) {
                last = i;
                lastIntegergObject = Integer.valueOf(i);
            }
            return lastIntegergObject;
        }

        @Override
        public void skip(RecordingInput input) throws IOException {
            input.readInt();
        }
    }

    private static final class ShortParser extends Parser {
        @Override
        public Object parse(RecordingInput input) throws IOException {
            return Short.valueOf(input.readShort());
        }

        @Override
        public void skip(RecordingInput input) throws IOException {
            input.readShort();
        }
    }

    private static final class CharacterParser extends Parser {
        @Override
        public Object parse(RecordingInput input) throws IOException {
            return Character.valueOf(input.readChar());
        }

        @Override
        public void skip(RecordingInput input) throws IOException {
            input.readChar();
        }
    }

    private static final class FloatParser extends Parser {
        @Override
        public Object parse(RecordingInput input) throws IOException {
            return Float.valueOf(input.readFloat());
        }

        @Override
        public void skip(RecordingInput input) throws IOException {
            input.skipBytes(Float.BYTES);
        }
    }

    private static final class DoubleParser extends Parser {
        @Override
        public Object parse(RecordingInput input) throws IOException {
            return Double.valueOf(input.readDouble());
        }

        @Override
        public void skip(RecordingInput input) throws IOException {
            input.skipBytes(Double.BYTES);
        }
    }

    private static final class ArrayParser extends Parser {
        private final Parser elementParser;

        public ArrayParser(Parser elementParser) {
            this.elementParser = elementParser;
        }

        @Override
        public Object parse(RecordingInput input) throws IOException {
            final int size = input.readInt();
            input.require(size, "Array size %d exceeds available data" );
            final Object[] array = new Object[size];
            for (int i = 0; i < size; i++) {
                array[i] = elementParser.parse(input);
            }
            return array;
        }

        @Override
        public Object parseReferences(RecordingInput input) throws IOException {
            final int size = input.readInt();
            final Object[] array = new Object[size];
            for (int i = 0; i < size; i++) {
                array[i] = elementParser.parse(input);
            }
            return array;
        }

        @Override
        public void skip(RecordingInput input) throws IOException {
            final int size = input.readInt();
            for (int i = 0; i < size; i++) {
                elementParser.skip(input);
            }
        }
    }

    private static final class EventValueConstantParser extends Parser {
        private final ConstantLookup lookup;
        private Object lastValue = 0;
        private long lastKey = -1;
        private Object lastReferenceValue;
        private long lastReferenceKey = -1;
        EventValueConstantParser(ConstantLookup lookup) {
            this.lookup = lookup;
        }

        @Override
        public Object parse(RecordingInput input) throws IOException {
            long key = input.readLong();
            if (key == lastKey) {
                return lastValue;
            }
            lastKey = key;
            lastValue = lookup.getCurrentResolved(key);
            return lastValue;
        }

        @Override
        public void skip(RecordingInput input) throws IOException {
            input.readLong();
        }

        @Override
        public Object parseReferences(RecordingInput input) throws IOException {
            long key = input.readLong();
            if (key == lastReferenceKey) {
                return lastReferenceValue;
            }
            lastReferenceKey = key;
            lastReferenceValue = new Reference(lookup.getLatestPool(), key);
            return lastReferenceValue;
        }
    }

    private static final class ConstantValueParser extends Parser {
        private final ConstantLookup lookup;
        ConstantValueParser(ConstantLookup lookup) {
            this.lookup = lookup;
        }

        @Override
        public Object parse(RecordingInput input) throws IOException {
            return lookup.getCurrent(input.readLong());
        }

        @Override
        public void skip(RecordingInput input) throws IOException {
            input.readLong();
        }

        @Override
        public Object parseReferences(RecordingInput input) throws IOException {
            return new Reference(lookup.getLatestPool(), input.readLong());
        }
    }
}
