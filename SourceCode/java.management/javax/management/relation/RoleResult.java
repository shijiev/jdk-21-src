/*
 * Copyright (c) 2000, 2021, Oracle and/or its affiliates. All rights reserved.
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

package javax.management.relation;


import com.sun.jmx.mbeanserver.GetPropertyAction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;

import java.security.AccessController;

/**
 * Represents the result of a multiple access to several roles of a relation
 * (either for reading or writing).
 *
 * <p>The <b>serialVersionUID</b> of this class is <code>-6304063118040985512L</code>.
 *
 * @since 1.5
 */
@SuppressWarnings("serial")
public class RoleResult implements Serializable {

    // Serialization compatibility stuff:
    // Two serial forms are supported in this class. The selected form depends
    // on system property "jmx.serial.form":
    //  - "1.0" for JMX 1.0
    //  - any other value for JMX 1.1 and higher
    //
    // Serial version for old serial form
    private static final long oldSerialVersionUID = 3786616013762091099L;
    //
    // Serial version for new serial form
    private static final long newSerialVersionUID = -6304063118040985512L;
    //
    // Serializable fields in old serial form
    private static final ObjectStreamField[] oldSerialPersistentFields =
    {
      new ObjectStreamField("myRoleList", RoleList.class),
      new ObjectStreamField("myRoleUnresList", RoleUnresolvedList.class)
    };
    //
    // Serializable fields in new serial form
    private static final ObjectStreamField[] newSerialPersistentFields =
    {
      new ObjectStreamField("roleList", RoleList.class),
      new ObjectStreamField("unresolvedRoleList", RoleUnresolvedList.class)
    };
    //
    // Actual serial version and serial form
    private static final long serialVersionUID;
    /**
     * @serialField roleList RoleList List of roles successfully accessed
     * @serialField unresolvedRoleList RoleUnresolvedList List of roles unsuccessfully accessed
     */
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean compat = false;
    static {
        try {
            GetPropertyAction act = new GetPropertyAction("jmx.serial.form");
            @SuppressWarnings("removal")
            String form = AccessController.doPrivileged(act);
            compat = (form != null && form.equals("1.0"));
        } catch (Exception e) {
            // OK : Too bad, no compat with 1.0
        }
        if (compat) {
            serialPersistentFields = oldSerialPersistentFields;
            serialVersionUID = oldSerialVersionUID;
        } else {
            serialPersistentFields = newSerialPersistentFields;
            serialVersionUID = newSerialVersionUID;
        }
    }
    //
    // END Serialization compatibility stuff

    //
    // Private members
    //

    /**
     * @serial List of roles successfully accessed
     */
    private RoleList roleList = null;

    /**
     * @serial List of roles unsuccessfully accessed
     */
    private RoleUnresolvedList unresolvedRoleList = null;

    //
    // Constructor
    //

    /**
     * Constructor.
     *
     * @param list  list of roles successfully accessed.
     * @param unresolvedList  list of roles not accessed (with problem
     * descriptions).
     */
    public RoleResult(RoleList list,
                      RoleUnresolvedList unresolvedList) {

        setRoles(list);
        setRolesUnresolved(unresolvedList);
    }

    //
    // Accessors
    //

    /**
     * Retrieves list of roles successfully accessed.
     *
     * @return a RoleList
     *
     * @see #setRoles
     */
    public RoleList getRoles() {
        return roleList;
    }

    /**
     * Retrieves list of roles unsuccessfully accessed.
     *
     * @return a RoleUnresolvedList.
     *
     * @see #setRolesUnresolved
     */
    public RoleUnresolvedList getRolesUnresolved() {
        return unresolvedRoleList;
    }

    /**
     * Sets list of roles successfully accessed.
     *
     * @param list  list of roles successfully accessed
     *
     * @see #getRoles
     */
    public void setRoles(RoleList list) {
        if (list != null) {

            roleList = new RoleList();

            for (Object o : list) {
                Role currRole = (Role)o;
                roleList.add((Role)currRole.clone());
            }
        } else {
            roleList = null;
        }
    }

    /**
     * Sets list of roles unsuccessfully accessed.
     *
     * @param unresolvedList  list of roles unsuccessfully accessed
     *
     * @see #getRolesUnresolved
     */
    public void setRolesUnresolved(RoleUnresolvedList unresolvedList) {
        if (unresolvedList != null) {

            unresolvedRoleList = new RoleUnresolvedList();

            for (Object o : unresolvedList) {
                RoleUnresolved currRoleUnres = (RoleUnresolved)o;
                unresolvedRoleList.add((RoleUnresolved)currRoleUnres.clone());
            }
        } else {
            unresolvedRoleList = null;
        }
    }

    /**
     * Deserializes a {@link RoleResult} from an {@link ObjectInputStream}.
     */
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
      if (compat)
      {
        // Read an object serialized in the old serial form
        //
        ObjectInputStream.GetField fields = in.readFields();
        roleList = (RoleList) fields.get("myRoleList", null);
        if (fields.defaulted("myRoleList"))
        {
          throw new NullPointerException("myRoleList");
        }
        unresolvedRoleList = (RoleUnresolvedList) fields.get("myRoleUnresList", null);
        if (fields.defaulted("myRoleUnresList"))
        {
          throw new NullPointerException("myRoleUnresList");
        }
      }
      else
      {
        // Read an object serialized in the new serial form
        //
        in.defaultReadObject();
      }
    }


    /**
     * Serializes a {@link RoleResult} to an {@link ObjectOutputStream}.
     */
    private void writeObject(ObjectOutputStream out)
            throws IOException {
      if (compat)
      {
        // Serializes this instance in the old serial form
        //
        ObjectOutputStream.PutField fields = out.putFields();
        fields.put("myRoleList", roleList);
        fields.put("myRoleUnresList", unresolvedRoleList);
        out.writeFields();
      }
      else
      {
        // Serializes this instance in the new serial form
        //
        out.defaultWriteObject();
      }
    }
}
