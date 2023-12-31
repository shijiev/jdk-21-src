/*
 * Copyright (c) 2011, 2022, Oracle and/or its affiliates. All rights reserved.
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

import java.io.*;
import java.util.*;
import sun.jvm.hotspot.debugger.*;
import sun.jvm.hotspot.runtime.*;
import sun.jvm.hotspot.types.*;
import sun.jvm.hotspot.utilities.*;

// MultiBranchData
//
// A MultiBranchData is used to access profiling information for
// a multi-way branch (*switch bytecodes).  It consists of a series
// of (count, displacement) pairs, which count the number of times each
// case was taken and specify the data displacement for each branch target.
public class MultiBranchData extends ArrayData {
  static final int   defaultCountOffSet = 0;
  static final int     defaultDisaplacementOffSet = 1;
  static final int     caseArrayStart = 2;
  static final int   relativeCountOffSet = 0;
  static final int     relativeDisplacementOffSet = 1;
  static final int     perCaseCellCount = 2;

  public MultiBranchData(DataLayout layout) {
    super(layout);
    //assert(layout.tag() == DataLayout.multiBranchDataTag, "wrong type");
  }

  // static int computeCellCount(BytecodeStream stream);

  int numberOfCases() {
    int alen = arrayLen() - 2; // get rid of default case here.
    //assert(alen % perCaseCellCount == 0, "must be even");
    return (alen / perCaseCellCount);
  }

  int defaultCount() {
    return arrayUintAt(defaultCountOffSet);
  }
  int defaultDisplacement() {
    return arrayIntAt(defaultDisaplacementOffSet);
  }

  int countAt(int index) {
    return arrayUintAt(caseArrayStart +
                         index * perCaseCellCount +
                         relativeCountOffSet);
  }
  int displacementAt(int index) {
    return arrayIntAt(caseArrayStart +
                        index * perCaseCellCount +
                        relativeDisplacementOffSet);
  }

  // Code generation support
  static int defaultCountOffset() {
    return arrayElementOffset(defaultCountOffSet);
  }
  static int defaultDisplacementOffset() {
    return arrayElementOffset(defaultDisaplacementOffSet);
  }
  static int caseCountOffset(int index) {
    return caseArrayOffset() +
      (perCaseSize() * index) +
      relativeCountOffset();
  }
  static int caseArrayOffset() {
    return arrayElementOffset(caseArrayStart);
  }
  static int perCaseSize() {
    return (perCaseCellCount) * MethodData.cellSize;
  }
  static int relativeCountOffset() {
    return (relativeCountOffSet) * MethodData.cellSize;
  }
  static int relativeDisplacementOffset() {
    return (relativeDisplacementOffSet) * MethodData.cellSize;
  }

  public void printDataOn(PrintStream st) {
    printShared(st, "MultiBranchData");
    st.println("default_count(" + defaultCount() + ") displacement(" + defaultDisplacement() + ")");
    int cases = numberOfCases();
    for (int i = 0; i < cases; i++) {
      tab(st);
      st.println("count(" + countAt(i) + ") displacement(" + displacementAt(i) + ")");
    }
  }
}
