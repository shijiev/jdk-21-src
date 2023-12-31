/*
 * Copyright (c) 2005, 2023, Oracle and/or its affiliates. All rights reserved.
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

/*
 * (C) Copyright Taligent, Inc. 1996, 1997 - All Rights Reserved
 * (C) Copyright IBM Corp. 1996 - 1998 - All Rights Reserved
 *
 * The original version of this source code and documentation
 * is copyrighted and owned by Taligent, Inc., a wholly-owned
 * subsidiary of IBM. These materials are provided under terms
 * of a License Agreement between Taligent and Sun. This technology
 * is protected by multiple US and International patents.
 *
 * This notice and attribution to Taligent may not be removed.
 * Taligent is a registered trademark of Taligent, Inc.
 *
 */

package sun.text.resources.ext;

import java.util.ListResourceBundle;

public class CollationData_sv extends ListResourceBundle {

    protected final Object[][] getContents() {
        return new Object[][] {
            { "Rule",
                "& Z < a\u030a , A\u030a" +  // a-ring, aa ligaure
                "< a\u0308 , A\u0308 < a\u030b, A\u030b " +  // a-umlaut, a-double-acute
                "< \u00e6 , \u00c6 " +                   //  ae ligature
                "< o\u0308 , O\u0308 " +   // o-umlaut
                "< o\u030b , O\u030b ; \u00f8 , \u00d8 " +   // o-double-acute < o-stroke
                "& Y, u\u0308 , U\u0308" + // u-double-acute
                "; u\u030b, U\u030b "
            },
            {"Rule.trad",
                "& Z < a\u030a , A\u030a" +  // a-ring, aa ligaure
                "< a\u0308 , A\u0308 < a\u030b, A\u030b " +  // a-umlaut, a-double-acute
                "< \u00e6 , \u00c6 " +                   //  ae ligature
                "< o\u0308 , O\u0308 " +   // o-umlaut
                "< o\u030b , O\u030b ; \u00f8 , \u00d8 " +   // o-double-acute < o-stroke
                "& V ; w , W" +
                "& Y, u\u0308 , U\u0308" + // u-double-acute
                "; u\u030b, U\u030b "
            }
        };
    }
}
