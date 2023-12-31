/*
 * Copyright (c) 2000, 2022, Oracle and/or its affiliates. All rights reserved.
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

import java.util.*;

import sun.jvm.hotspot.debugger.*;
import sun.jvm.hotspot.memory.*;
import sun.jvm.hotspot.runtime.*;
import sun.jvm.hotspot.types.TypeDataBase;
import sun.jvm.hotspot.utilities.*;
import sun.jvm.hotspot.utilities.Observable;
import sun.jvm.hotspot.utilities.Observer;

/** A utility class encapsulating useful oop operations */

public class OopUtilities {

  // FIXME: access should be synchronized and cleared when VM is
  // resumed
  // String fields
  private static ByteField coderField;
  private static OopField valueField;
  // Thread fields
  private static OopField threadHolderField;
  private static OopField threadNameField;
  private static LongField threadEETopField;
  //tid field is new since 1.5
  private static LongField threadTIDField;
  // parkBlocker field is new since 1.6
  private static OopField threadParkBlockerField;
  // Thread$FieldHolder fields
  private static IntField threadStatusField;
  private static IntField threadPriorityField;
  private static BooleanField threadDaemonField;

  // possible values of JavaThreadStatus
  public static int THREAD_STATUS_NEW;
  public static int THREAD_STATUS_RUNNABLE;
  public static int THREAD_STATUS_SLEEPING;
  public static int THREAD_STATUS_IN_OBJECT_WAIT;
  public static int THREAD_STATUS_IN_OBJECT_WAIT_TIMED;
  public static int THREAD_STATUS_PARKED;
  public static int THREAD_STATUS_PARKED_TIMED;
  public static int THREAD_STATUS_BLOCKED_ON_MONITOR_ENTER;
  public static int THREAD_STATUS_TERMINATED;

  // java.util.concurrent.locks.AbstractOwnableSynchronizer fields
  private static OopField absOwnSyncOwnerThreadField;

  private static final int JVMTI_THREAD_STATE_ALIVE = 0x0001;

  static {
    VM.registerVMInitializedObserver(new Observer() {
        public void update(Observable o, Object data) {
          initialize(VM.getVM().getTypeDataBase());
        }
      });
  }

  private static synchronized void initialize(TypeDataBase db) {
    // FIXME: don't need this observer; however, do need a VM resumed
    // and suspended observer to refetch fields
  }

  public static String charArrayToString(TypeArray charArray) {
    if (charArray == null) {
      return null;
    }
    int length = (int)charArray.getLength();
    StringBuilder buf = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      buf.append(charArray.getCharAt(i));
    }
    return buf.toString();
  }

  public static String byteArrayToString(TypeArray byteArray, byte coder) {
    if (byteArray == null) {
      return null;
    }
    int length = (int)byteArray.getLength() >> coder;
    StringBuilder buf = new StringBuilder(length);
    if (coder == 0) {
      // Latin1 encoded
      for (int i = 0; i < length; i++) {
        buf.append((char)(byteArray.getByteAt(i) & 0xff));
      }
    } else {
      // UTF16 encoded
      for (int i = 0; i < length; i++) {
        buf.append(byteArray.getCharAt(i));
      }
    }
    return buf.toString();
  }

  public static String escapeString(String s) {
    StringBuilder sb = null;
    for (int index = 0; index < s.length(); index++) {
      char value = s.charAt(index);
      if (value >= 32 && value < 127 || value == '\'' || value == '\\') {
        if (sb != null) {
          sb.append(value);
        }
      } else {
        if (sb == null) {
          sb = new StringBuilder(s.length() * 2);
          sb.append(s, 0, index);
        }
        sb.append("\\u");
        if (value < 0x10) sb.append("000");
        else if (value < 0x100) sb.append("00");
        else if (value < 0x1000) sb.append("0");
        sb.append(Integer.toHexString(value));
      }
    }
    if (sb != null) {
      return sb.toString();
    }
    return s;
  }

  public static String stringOopToString(Oop stringOop) {
    InstanceKlass k = (InstanceKlass) stringOop.getKlass();
    coderField  = (ByteField) k.findField("coder", "B");
    valueField  = (OopField) k.findField("value",  "[B");
    if (Assert.ASSERTS_ENABLED) {
       Assert.that(coderField != null, "Field \'coder\' of java.lang.String not found");
       Assert.that(valueField != null, "Field \'value\' of java.lang.String not found");
    }
    return byteArrayToString((TypeArray) valueField.getValue(stringOop), coderField.getValue(stringOop));
  }

  public static String stringOopToEscapedString(Oop stringOop) {
    return escapeString(stringOopToString(stringOop));
  }

  private static void initThreadFields() {
    if (threadNameField == null) {
      SystemDictionary sysDict = VM.getVM().getSystemDictionary();
      InstanceKlass k = sysDict.getThreadKlass();
      threadHolderField  = (OopField) k.findField("holder", "Ljava/lang/Thread$FieldHolder;");
      threadNameField  = (OopField) k.findField("name", "Ljava/lang/String;");
      threadEETopField = (LongField) k.findField("eetop", "J");
      threadTIDField = (LongField) k.findField("tid", "J");
      threadParkBlockerField = (OopField) k.findField("parkBlocker",
                                     "Ljava/lang/Object;");
      k = sysDict.getThreadFieldHolderKlass();
      threadPriorityField = (IntField) k.findField("priority", "I");
      threadStatusField = (IntField) k.findField("threadStatus", "I");
      threadDaemonField = (BooleanField) k.findField("daemon", "Z");

      TypeDataBase db = VM.getVM().getTypeDataBase();
      THREAD_STATUS_NEW = db.lookupIntConstant("JavaThreadStatus::NEW").intValue();

      THREAD_STATUS_RUNNABLE = db.lookupIntConstant("JavaThreadStatus::RUNNABLE").intValue();
      THREAD_STATUS_SLEEPING = db.lookupIntConstant("JavaThreadStatus::SLEEPING").intValue();
      THREAD_STATUS_IN_OBJECT_WAIT = db.lookupIntConstant("JavaThreadStatus::IN_OBJECT_WAIT").intValue();
      THREAD_STATUS_IN_OBJECT_WAIT_TIMED = db.lookupIntConstant("JavaThreadStatus::IN_OBJECT_WAIT_TIMED").intValue();
      THREAD_STATUS_PARKED = db.lookupIntConstant("JavaThreadStatus::PARKED").intValue();
      THREAD_STATUS_PARKED_TIMED = db.lookupIntConstant("JavaThreadStatus::PARKED_TIMED").intValue();
      THREAD_STATUS_BLOCKED_ON_MONITOR_ENTER = db.lookupIntConstant("JavaThreadStatus::BLOCKED_ON_MONITOR_ENTER").intValue();
      THREAD_STATUS_TERMINATED = db.lookupIntConstant("JavaThreadStatus::TERMINATED").intValue();

      if (Assert.ASSERTS_ENABLED) {
        // it is okay to miss threadStatusField, because this was
        // introduced only in 1.5 JDK.
        Assert.that(threadNameField   != null &&
                    threadEETopField  != null, "must find all java.lang.Thread fields");
      }
    }
  }

  public static String threadOopGetName(Oop threadOop) {
    initThreadFields();
    return stringOopToString(threadNameField.getValue(threadOop));
  }

  /** May return null if, e.g., thread was not started */
  public static JavaThread threadOopGetJavaThread(Oop threadOop) {
    initThreadFields();
    Address addr = threadOop.getHandle().getAddressAt(threadEETopField.getOffset());
    if (addr == null) {
      return null;
    }
    return VM.getVM().getThreads().createJavaThreadWrapper(addr);
  }

  public static long threadOopGetTID(Oop threadOop) {
    initThreadFields();
    if (threadTIDField != null) {
      return threadTIDField.getValue(threadOop);
    } else {
      return 0;
    }
  }

  /** returns value of java.lang.Thread.threadStatus field */
  public static int threadOopGetThreadStatus(Oop threadOop) {
    initThreadFields();
    // The threadStatus is only present starting in 1.5
    if (threadStatusField != null) {
      Oop holderOop = threadHolderField.getValue(threadOop);
      return threadStatusField.getValue(holderOop);
    } else {
      // All we can easily figure out is if it is alive, but that is
      // enough info for a valid unknown status.
      JavaThread thr = threadOopGetJavaThread(threadOop);
      if (thr == null) {
        // the thread hasn't run yet or is in the process of exiting
        return THREAD_STATUS_NEW;
      } else {
        return JVMTI_THREAD_STATE_ALIVE;
      }
    }
  }

  /** returns value of java.lang.Thread.parkBlocker field */
  public static Oop threadOopGetParkBlocker(Oop threadOop) {
    initThreadFields();
    if (threadParkBlockerField != null) {
      return threadParkBlockerField.getValue(threadOop);
    }
    return null;
  }

  // initialize fields for j.u.c.l AbstractOwnableSynchornizer class
  private static void initAbsOwnSyncFields() {
    if (absOwnSyncOwnerThreadField == null) {
       SystemDictionary sysDict = VM.getVM().getSystemDictionary();
       InstanceKlass k = sysDict.getAbstractOwnableSynchronizerKlass();
       absOwnSyncOwnerThreadField =
           (OopField) k.findField("exclusiveOwnerThread",
                                  "Ljava/lang/Thread;");
    }
  }

  // return exclusiveOwnerThread field of AbstractOwnableSynchronizer class
  public static Oop abstractOwnableSynchronizerGetOwnerThread(Oop oop) {
    initAbsOwnSyncFields();
    if (absOwnSyncOwnerThreadField == null) {
      return null; // pre-1.6 VM?
    } else {
      return absOwnSyncOwnerThreadField.getValue(oop);
    }
  }

  public static int threadOopGetPriority(Oop threadOop) {
    initThreadFields();
    if (threadPriorityField != null) {
      Oop holderOop = threadHolderField.getValue(threadOop);
      return threadPriorityField.getValue(holderOop);
    } else {
      return 0;
    }
  }

  public static boolean threadOopGetDaemon(Oop threadOop) {
    initThreadFields();
    if (threadDaemonField != null) {
      Oop holderOop = threadHolderField.getValue(threadOop);
      return threadDaemonField.getValue(holderOop);
    } else {
      return false;
    }
  }

  public static String threadOopGetThreadStatusName(Oop threadOop) {
    int status = OopUtilities.threadOopGetThreadStatus(threadOop);
    if(status == THREAD_STATUS_NEW){
      return "NEW";
    }else if(status == THREAD_STATUS_RUNNABLE){
      return "RUNNABLE";
    }else if(status == THREAD_STATUS_SLEEPING){
      return "TIMED_WAITING (sleeping)";
    }else if(status == THREAD_STATUS_IN_OBJECT_WAIT){
      return "WAITING (on object monitor)";
    }else if(status == THREAD_STATUS_IN_OBJECT_WAIT_TIMED){
      return "TIMED_WAITING (on object monitor)";
    }else if(status == THREAD_STATUS_PARKED){
      return "WAITING (parking)";
    }else if(status == THREAD_STATUS_PARKED_TIMED){
      return "TIMED_WAITING (parking)";
    }else if(status == THREAD_STATUS_BLOCKED_ON_MONITOR_ENTER){
      return "BLOCKED (on object monitor)";
    }else if(status == THREAD_STATUS_TERMINATED){
      return "TERMINATED";
    }
    return "UNKNOWN";
  }
}
