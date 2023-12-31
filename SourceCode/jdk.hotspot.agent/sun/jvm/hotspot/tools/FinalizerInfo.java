/*
 * Copyright (c) 2004, 2022, Oracle and/or its affiliates. All rights reserved.
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

package sun.jvm.hotspot.tools;

import sun.jvm.hotspot.debugger.JVMDebugger;

import sun.jvm.hotspot.oops.*;
import sun.jvm.hotspot.utilities.SystemDictionaryHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/*
 * Iterates over the queue of object pending finalization and prints a
 * summary of these objects in the form of a histogram.
 */
public class FinalizerInfo extends Tool {

    public FinalizerInfo() {
        super();
    }

    public FinalizerInfo(JVMDebugger d) {
        super(d);
    }

    @Override
    public String getName() {
        return "finalizerInfo";
    }

    public static void main(String[] args) {
        FinalizerInfo finfo = new FinalizerInfo();
        finfo.execute(args);
    }

    public void run() {
        /*
         * The implementation here has a dependency on the implementation of
         * java.lang.ref.Finalizer. If the Finalizer implementation changes it's
         * possible this method will require changes too. We looked into using
         * ObjectReader to deserialize the objects from the target VM but as
         * there aren't any public methods to traverse the queue it means using
         * reflection which will also tie us to the implementation.
         *
         * The assumption here is that Finalizer.queue is the ReferenceQueue
         * with the objects awaiting finalization. The ReferenceQueue queueLength
         * is the number of objects in the queue, and 'head' is the head of the
         * queue.
         */
        InstanceKlass ik =
            SystemDictionaryHelper.findInstanceKlass("java.lang.ref.Finalizer");
        final Oop[] queueref = new Oop[1];
        ik.iterateStaticFields(new DefaultOopVisitor() {
            public void doOop(OopField field, boolean isVMField) {
              String name = field.getID().getName();
              if (name.equals("queue")) {
                queueref[0] = field.getValue(getObj());
              }
            }
          });
        Oop queue = queueref[0];

        InstanceKlass k = (InstanceKlass) queue.getKlass();

        LongField queueLengthField = (LongField) k.findField("queueLength", "J");
        long queueLength = queueLengthField.getValue(queue);

        OopField headField =  (OopField) k.findField("head", "Ljava/lang/ref/Reference;");
        Oop head = headField.getValue(queue);

        System.out.println("Number of objects pending for finalization: " + queueLength);

        /*
         * If 'head' is non-NULL then it is the head of a list of References.
         * We iterate over the list (end of list is when head.next == head)
         */
        if (head != null) {
            k = (InstanceKlass) head.getKlass();
            OopField referentField =
                (OopField) k.findField("referent", "Ljava/lang/Object;");
            OopField nextField =
                (OopField) k.findField("next", "Ljava/lang/ref/Reference;");

            HashMap<Klass, ObjectHistogramElement> map = new HashMap<>();
            for (;;) {
                Oop referent = referentField.getValue(head);

                Klass klass = referent.getKlass();
                if (!map.containsKey(klass)) {
                    map.put(klass, new ObjectHistogramElement(klass));
                }
                map.get(klass).updateWith(referent);

                Oop next = nextField.getValue(head);
                if (next == null || next.equals(head)) break;
                head = next;
            }

            /*
             * Sort results - descending order by total size
             */
            ArrayList<ObjectHistogramElement> list = new ArrayList<>();
            list.addAll(map.values());
            list.sort(ObjectHistogramElement::compare);

            /*
             * Print summary of objects in queue
             */
            System.out.println("");
            System.out.println("Count" + "\t" + "Class description");
            System.out.println("-------------------------------------------------------");
            for (int i=0; i<list.size(); i++) {
                ObjectHistogramElement e = list.get(i);
                System.out.println(e.getCount() + "\t" + e.getDescription());
            }
       }

   }
}
