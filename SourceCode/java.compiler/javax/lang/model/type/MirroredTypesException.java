/*
 * Copyright (c) 2005, 2022, Oracle and/or its affiliates. All rights reserved.
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

package javax.lang.model.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.io.ObjectInputStream;
import java.io.IOException;
import javax.lang.model.element.Element;


/**
 * Thrown when an application attempts to access a sequence of {@link
 * Class} objects each corresponding to a {@link TypeMirror}.
 *
 * @see MirroredTypeException
 * @see Element#getAnnotation(Class)
 * @since 1.6
 */
public class MirroredTypesException extends RuntimeException {

    private static final long serialVersionUID = 269;

    transient List<? extends TypeMirror> types; // cannot be serialized

    /*
     * Trusted constructor to be called by MirroredTypeException.
     */
    MirroredTypesException(String message, TypeMirror type) {
        super(message);
        List<TypeMirror> tmp = (new ArrayList<>());
        tmp.add(type);
        types = Collections.unmodifiableList(tmp);
    }

    /**
     * Constructs a new MirroredTypesException for the specified types.
     *
     * @param types  the types being accessed
     */
    public MirroredTypesException(List<? extends TypeMirror> types) {
        super("Attempt to access Class objects for TypeMirrors " +
              (types = // defensive copy
               new ArrayList<>(types)).toString() );
        this.types = Collections.unmodifiableList(types);
    }

    /**
     * Returns the type mirrors corresponding to the types being accessed.
     * The type mirrors may be unavailable if this exception has been
     * serialized and then read back in.
     *
     * @return the type mirrors in construction order, or {@code null} if unavailable
     */
    public List<? extends TypeMirror> getTypeMirrors() {
        return types;
    }

    /**
     * Explicitly set all transient fields.
     * @param s the serial stream
     * @throws ClassNotFoundException for a missing class during
     * deserialization
     * @throws IOException for an IO problem during deserialization
     */
    private void readObject(ObjectInputStream s)
        throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        types = null;
    }
}
