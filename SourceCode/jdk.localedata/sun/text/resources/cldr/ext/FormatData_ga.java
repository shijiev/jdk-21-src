/*
 * Copyright (c) 2012, 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * COPYRIGHT AND PERMISSION NOTICE
 *
 * Copyright (c) 1991-2022 Unicode, Inc. All rights reserved.
 * Distributed under the Terms of Use in https://www.unicode.org/copyright.html.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of the Unicode data files and any associated documentation
 * (the "Data Files") or Unicode software and any associated documentation
 * (the "Software") to deal in the Data Files or Software
 * without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, and/or sell copies of
 * the Data Files or Software, and to permit persons to whom the Data Files
 * or Software are furnished to do so, provided that either
 * (a) this copyright and permission notice appear with all copies
 * of the Data Files or Software, or
 * (b) this copyright and permission notice appear in associated
 * Documentation.
 *
 * THE DATA FILES AND SOFTWARE ARE PROVIDED "AS IS", WITHOUT WARRANTY OF
 * ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT OF THIRD PARTY RIGHTS.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR HOLDERS INCLUDED IN THIS
 * NOTICE BE LIABLE FOR ANY CLAIM, OR ANY SPECIAL INDIRECT OR CONSEQUENTIAL
 * DAMAGES, OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE,
 * DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER
 * TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THE DATA FILES OR SOFTWARE.
 *
 * Except as contained in this notice, the name of a copyright holder
 * shall not be used in advertising or otherwise to promote the sale,
 * use or other dealings in these Data Files or Software without prior
 * written authorization of the copyright holder.
 */

package sun.text.resources.cldr.ext;

import java.util.ListResourceBundle;

public class FormatData_ga extends ListResourceBundle {
    @Override
    protected final Object[][] getContents() {
        final String[] metaValue_generic_AmPmMarkers = new String[] {
            "r.n.",
            "i.n.",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
        };
        final String metaValue_generic_DateFormatItem_yyyyMMMd = "d MMM y G";
        final String metaValue_generic_DateFormatItem_yyyyMd = "dd/MM/y GGGGG";
        final String metaValue_generic_DateFormatItem_yyyyMMMEd = "E d MMM y G";
        final String metaValue_generic_DateFormatItem_Gy = "y G";
        final String metaValue_generic_DateFormatItem_yyyyMMM = "MMM y G";
        final String[] metaValue_MonthNames = new String[] {
            "Ean\u00e1ir",
            "Feabhra",
            "M\u00e1rta",
            "Aibre\u00e1n",
            "Bealtaine",
            "Meitheamh",
            "I\u00fail",
            "L\u00fanasa",
            "Me\u00e1n F\u00f3mhair",
            "Deireadh F\u00f3mhair",
            "Samhain",
            "Nollaig",
            "",
        };
        final String[] metaValue_MonthAbbreviations = new String[] {
            "Ean",
            "Feabh",
            "M\u00e1rta",
            "Aib",
            "Beal",
            "Meith",
            "I\u00fail",
            "L\u00fan",
            "MF\u00f3mh",
            "DF\u00f3mh",
            "Samh",
            "Noll",
            "",
        };
        final String[] metaValue_MonthNarrows = new String[] {
            "E",
            "F",
            "M",
            "A",
            "B",
            "M",
            "I",
            "L",
            "M",
            "D",
            "S",
            "N",
            "",
        };
        final String[] metaValue_generic_DayNames = new String[] {
            "D\u00e9 Domhnaigh",
            "D\u00e9 Luain",
            "D\u00e9 M\u00e1irt",
            "D\u00e9 C\u00e9adaoin",
            "D\u00e9ardaoin",
            "D\u00e9 hAoine",
            "D\u00e9 Sathairn",
        };
        final String[] metaValue_generic_DayAbbreviations = new String[] {
            "Domh",
            "Luan",
            "M\u00e1irt",
            "C\u00e9ad",
            "D\u00e9ar",
            "Aoine",
            "Sath",
        };
        final String[] metaValue_generic_DayNarrows = new String[] {
            "D",
            "L",
            "M",
            "C",
            "D",
            "A",
            "S",
        };
        final String[] metaValue_generic_QuarterNames = new String[] {
            "1\u00fa r\u00e1ithe",
            "2\u00fa r\u00e1ithe",
            "3\u00fa r\u00e1ithe",
            "4\u00fa r\u00e1ithe",
        };
        final String[] metaValue_generic_QuarterAbbreviations = new String[] {
            "R1",
            "R2",
            "R3",
            "R4",
        };
        final String[] metaValue_Eras = new String[] {
            "RC",
            "AD",
        };
        final String[] metaValue_generic_TimePatterns = new String[] {
            "HH:mm:ss zzzz",
            "HH:mm:ss z",
            "HH:mm:ss",
            "HH:mm",
        };
        final String[] metaValue_generic_DateTimePatterns = new String[] {
            "{1} {0}",
            "{1} {0}",
            "{1} {0}",
            "{1} {0}",
        };
        final String metaValue_generic_DateFormatItem_hm = "h:mm\u202fa";
        final String metaValue_generic_DateFormatItem_yMMMEd = "E d MMM y";
        final String metaValue_generic_DateFormatItem_Ehms = "E h:mm:ss\u202fa";
        final String metaValue_generic_DateFormatItem_Ehm = "E h:mm\u202fa";
        final String metaValue_generic_DateFormatItem_yMEd = "E dd/MM/y";
        final String metaValue_generic_DateFormatItem_hms = "h:mm:ss\u202fa";
        final String metaValue_generic_DateFormatItem_hmv = "h:mm\u202fa v";
        final String metaValue_generic_DateFormatItem_hmsv = "h:mm:ss\u202fa v";
        final String metaValue_generic_DateFormatItem_yMMMd = "d MMM y";
        final String metaValue_generic_DateFormatItem_yw = "'seachtain' 'a' w 'in' Y";
        final String metaValue_generic_DateFormatItem_yQQQ = "QQQ y";
        final String metaValue_generic_DateFormatItem_yMMM = "MMM y";
        final String metaValue_generic_DateFormatItem_Md = "dd/MM";
        final String metaValue_generic_DateFormatItem_Ed = "E d";
        final String metaValue_generic_DateFormatItem_MMMMd = "d MMMM";
        final String metaValue_generic_DateFormatItem_M = "LL";
        final String metaValue_generic_DateFormatItem_MEd = "E dd/MM";
        final String metaValue_generic_DateFormatItem_MMMEd = "E d MMM";
        final String metaValue_generic_DateFormatItem_h = "h\u202fa";
        final String metaValue_generic_DateFormatItem_yMd = "dd/MM/y";
        final String metaValue_generic_DateFormatItem_MMMd = "d MMM";
        final String metaValue_generic_DateFormatItem_yM = "MM/y";
        final String metaValue_generic_DateFormatItem_yMMMM = "MMMM y";
        final String metaValue_generic_DateFormatItem_MMMMW = "'seachtain' 'a' W 'i' MMMM";
        final String metaValue_generic_DateFormatItem_yQQQQ = "QQQQ y";
        final String[] metaValue_generic_QuarterNarrows = new String[] {
            "1",
            "2",
            "3",
            "4",
        };
        final String[] metaValue_java_time_buddhist_long_Eras = new String[] {
            "BC",
            "RB",
        };
        final String[] metaValue_java_time_generic_DatePatterns = new String[] {
            "EEEE d MMMM y G",
            "d MMMM y G",
            "d MMM y G",
            "dd/MM/y GGGGG",
        };
        final String[] metaValue_generic_DatePatterns = new String[] {
            "EEEE d MMMM y GGGG",
            "d MMMM y GGGG",
            "d MMM y GGGG",
            "dd/MM/y G",
        };
        final String metaValue_generic_DateFormatItem_yyyyMMMM = "MMMM y G";
        final String metaValue_generic_DateFormatItem_yyyyQQQQ = "QQQQ y G";
        final String metaValue_generic_DateFormatItem_yyyyMEd = "E dd/MM/y GGGGG";
        final String metaValue_generic_DateFormatItem_yyyyQQQ = "QQQ y G";
        final String metaValue_generic_DateFormatItem_Hmsv = "HH:mm:ss v";
        final String metaValue_generic_DateFormatItem_yyyyM = "MM/y GGGGG";
        final String metaValue_generic_DateFormatItem_Hmv = "HH:mm v";
        final String metaValue_generic_DateFormatItem_y = "y";
        final String metaValue_buddhist_DateFormatItem_Bhms = "h:mm:ss B";
        final String metaValue_buddhist_DateFormatItem_Hm = "HH:mm";
        final String metaValue_buddhist_DateFormatItem_ms = "mm:ss";
        final String metaValue_buddhist_DateFormatItem_EHms = "E HH:mm:ss";
        final String metaValue_buddhist_DateFormatItem_EHm = "E HH:mm";
        final String metaValue_buddhist_DateFormatItem_MMM = "LLL";
        final String metaValue_buddhist_DateFormatItem_EBhms = "E h:mm:ss B";
        final String metaValue_buddhist_DateFormatItem_EBhm = "E h:mm B";
        final String metaValue_buddhist_DateFormatItem_E = "ccc";
        final String metaValue_buddhist_DateFormatItem_Bhm = "h:mm B";
        final String metaValue_buddhist_DateFormatItem_H = "HH";
        final String metaValue_buddhist_DateFormatItem_d = "d";
        final String metaValue_buddhist_DateFormatItem_Bh = "h B";
        final String metaValue_buddhist_DateFormatItem_Hms = "HH:mm:ss";
        final String metaValue_calendarname_gregorian = "F\u00e9ilire Ghr\u00e9ag\u00f3ra";
        final Object[][] data = new Object[][] {
            { "generic.DayNames", metaValue_generic_DayNames },
            { "generic.DayAbbreviations", metaValue_generic_DayAbbreviations },
            { "generic.DayNarrows", metaValue_generic_DayNarrows },
            { "generic.QuarterNames", metaValue_generic_QuarterNames },
            { "generic.QuarterAbbreviations", metaValue_generic_QuarterAbbreviations },
            { "generic.QuarterNarrows", metaValue_generic_QuarterNarrows },
            { "generic.AmPmMarkers", metaValue_generic_AmPmMarkers },
            { "generic.narrow.AmPmMarkers", metaValue_generic_AmPmMarkers },
            { "generic.abbreviated.AmPmMarkers", metaValue_generic_AmPmMarkers },
            { "generic.TimePatterns", metaValue_generic_TimePatterns },
            { "java.time.generic.DatePatterns", metaValue_java_time_generic_DatePatterns },
            { "generic.DatePatterns", metaValue_generic_DatePatterns },
            { "generic.DateTimePatterns", metaValue_generic_DateTimePatterns },
            { "generic.DateFormatItem.hm", metaValue_generic_DateFormatItem_hm },
            { "generic.DateFormatItem.yyyyMMMM", metaValue_generic_DateFormatItem_yyyyMMMM },
            { "generic.DateFormatItem.yMMMEd", metaValue_generic_DateFormatItem_yMMMEd },
            { "generic.DateFormatItem.Ehms", metaValue_generic_DateFormatItem_Ehms },
            { "generic.DateFormatItem.yyyyQQQQ", metaValue_generic_DateFormatItem_yyyyQQQQ },
            { "generic.DateFormatItem.Ehm", metaValue_generic_DateFormatItem_Ehm },
            { "generic.DateFormatItem.yMEd", metaValue_generic_DateFormatItem_yMEd },
            { "generic.DateFormatItem.hms", metaValue_generic_DateFormatItem_hms },
            { "generic.DateFormatItem.yyyyMMMd", metaValue_generic_DateFormatItem_yyyyMMMd },
            { "generic.DateFormatItem.hmv", metaValue_generic_DateFormatItem_hmv },
            { "generic.DateFormatItem.Gy", metaValue_generic_DateFormatItem_Gy },
            { "generic.DateFormatItem.yyyyMMMEd", metaValue_generic_DateFormatItem_yyyyMMMEd },
            { "generic.DateFormatItem.hmsv", metaValue_generic_DateFormatItem_hmsv },
            { "generic.DateFormatItem.GyMMMd", metaValue_generic_DateFormatItem_yyyyMMMd },
            { "generic.DateFormatItem.yMMMd", metaValue_generic_DateFormatItem_yMMMd },
            { "generic.DateFormatItem.yw", metaValue_generic_DateFormatItem_yw },
            { "generic.DateFormatItem.yQQQ", metaValue_generic_DateFormatItem_yQQQ },
            { "generic.DateFormatItem.yyyyMEd", metaValue_generic_DateFormatItem_yyyyMEd },
            { "generic.DateFormatItem.yyyyMd", metaValue_generic_DateFormatItem_yyyyMd },
            { "generic.DateFormatItem.yMMM", metaValue_generic_DateFormatItem_yMMM },
            { "generic.DateFormatItem.Md", metaValue_generic_DateFormatItem_Md },
            { "generic.DateFormatItem.Ed", metaValue_generic_DateFormatItem_Ed },
            { "generic.DateFormatItem.yyyyMMM", metaValue_generic_DateFormatItem_yyyyMMM },
            { "generic.DateFormatItem.MMMMd", metaValue_generic_DateFormatItem_MMMMd },
            { "generic.DateFormatItem.M", metaValue_generic_DateFormatItem_M },
            { "generic.DateFormatItem.MEd", metaValue_generic_DateFormatItem_MEd },
            { "generic.DateFormatItem.MMMEd", metaValue_generic_DateFormatItem_MMMEd },
            { "generic.DateFormatItem.yyyyQQQ", metaValue_generic_DateFormatItem_yyyyQQQ },
            { "generic.DateFormatItem.h", metaValue_generic_DateFormatItem_h },
            { "generic.DateFormatItem.Hmsv", metaValue_generic_DateFormatItem_Hmsv },
            { "generic.DateFormatItem.GyMd", metaValue_generic_DateFormatItem_yyyyMd },
            { "generic.DateFormatItem.GyMMMEd", metaValue_generic_DateFormatItem_yyyyMMMEd },
            { "generic.DateFormatItem.yyyyM", metaValue_generic_DateFormatItem_yyyyM },
            { "generic.DateFormatItem.Hmv", metaValue_generic_DateFormatItem_Hmv },
            { "generic.DateFormatItem.yMd", metaValue_generic_DateFormatItem_yMd },
            { "generic.DateFormatItem.MMMd", metaValue_generic_DateFormatItem_MMMd },
            { "generic.DateFormatItem.yM", metaValue_generic_DateFormatItem_yM },
            { "generic.DateFormatItem.yMMMM", metaValue_generic_DateFormatItem_yMMMM },
            { "generic.DateFormatItem.MMMMW", metaValue_generic_DateFormatItem_MMMMW },
            { "generic.DateFormatItem.yQQQQ", metaValue_generic_DateFormatItem_yQQQQ },
            { "generic.DateFormatItem.yyyy", metaValue_generic_DateFormatItem_Gy },
            { "generic.DateFormatItem.y", metaValue_generic_DateFormatItem_y },
            { "generic.DateFormatItem.GyMMM", metaValue_generic_DateFormatItem_yyyyMMM },
            { "MonthNames", metaValue_MonthNames },
            { "standalone.MonthNames", metaValue_MonthNames },
            { "MonthAbbreviations", metaValue_MonthAbbreviations },
            { "standalone.MonthAbbreviations", metaValue_MonthAbbreviations },
            { "MonthNarrows", metaValue_MonthNarrows },
            { "standalone.MonthNarrows", metaValue_MonthNarrows },
            { "DayNames", metaValue_generic_DayNames },
            { "standalone.DayNames", metaValue_generic_DayNames },
            { "DayAbbreviations", metaValue_generic_DayAbbreviations },
            { "standalone.DayAbbreviations", metaValue_generic_DayAbbreviations },
            { "DayNarrows", metaValue_generic_DayNarrows },
            { "standalone.DayNarrows", metaValue_generic_DayNarrows },
            { "QuarterNames", metaValue_generic_QuarterNames },
            { "standalone.QuarterNames", metaValue_generic_QuarterNames },
            { "QuarterAbbreviations", metaValue_generic_QuarterAbbreviations },
            { "standalone.QuarterAbbreviations", metaValue_generic_QuarterAbbreviations },
            { "AmPmMarkers", metaValue_generic_AmPmMarkers },
            { "narrow.AmPmMarkers", metaValue_generic_AmPmMarkers },
            { "abbreviated.AmPmMarkers", metaValue_generic_AmPmMarkers },
            { "long.Eras",
                new String[] {
                    "Roimh Chr\u00edost",
                    "Anno Domini",
                }
            },
            { "Eras", metaValue_Eras },
            { "narrow.Eras", metaValue_Eras },
            { "field.era", "r\u00e9" },
            { "field.year", "bliain" },
            { "field.month", "m\u00ed" },
            { "field.week", "seachtain" },
            { "field.weekday", "l\u00e1 den tseachtain" },
            { "field.dayperiod", "a.m./p.m." },
            { "field.hour", "uair" },
            { "timezone.gmtFormat", "MAG{0}" },
            { "timezone.gmtZeroFormat", "MAG" },
            { "field.minute", "n\u00f3im\u00e9ad" },
            { "field.second", "soicind" },
            { "field.zone", "crios ama" },
            { "TimePatterns", metaValue_generic_TimePatterns },
            { "DatePatterns",
                new String[] {
                    "EEEE d MMMM y",
                    "d MMMM y",
                    "d MMM y",
                    "dd/MM/y",
                }
            },
            { "DateTimePatterns", metaValue_generic_DateTimePatterns },
            { "PluralRules", "one:n = 1;few:n = 3..6;many:n = 7..10;two:n = 2" },
            { "DateFormatItem.hm", metaValue_generic_DateFormatItem_hm },
            { "DateFormatItem.yMMMEd", metaValue_generic_DateFormatItem_yMMMEd },
            { "DateFormatItem.Ehms", metaValue_generic_DateFormatItem_Ehms },
            { "DateFormatItem.Ehm", metaValue_generic_DateFormatItem_Ehm },
            { "DateFormatItem.yMEd", metaValue_generic_DateFormatItem_yMEd },
            { "DateFormatItem.hms", metaValue_generic_DateFormatItem_hms },
            { "DateFormatItem.hmv", metaValue_generic_DateFormatItem_hmv },
            { "DateFormatItem.Gy", metaValue_generic_DateFormatItem_Gy },
            { "DateFormatItem.hmsv", metaValue_generic_DateFormatItem_hmsv },
            { "DateFormatItem.GyMMMd", metaValue_generic_DateFormatItem_yyyyMMMd },
            { "DateFormatItem.yMMMd", metaValue_generic_DateFormatItem_yMMMd },
            { "DateFormatItem.yw", metaValue_generic_DateFormatItem_yw },
            { "DateFormatItem.yQQQ", metaValue_generic_DateFormatItem_yQQQ },
            { "DateFormatItem.yMMM", metaValue_generic_DateFormatItem_yMMM },
            { "DateFormatItem.Md", metaValue_generic_DateFormatItem_Md },
            { "DateFormatItem.Ed", metaValue_generic_DateFormatItem_Ed },
            { "DateFormatItem.MMMMd", metaValue_generic_DateFormatItem_MMMMd },
            { "DateFormatItem.M", metaValue_generic_DateFormatItem_M },
            { "DateFormatItem.MEd", metaValue_generic_DateFormatItem_MEd },
            { "DateFormatItem.MMMEd", metaValue_generic_DateFormatItem_MMMEd },
            { "DateFormatItem.h", metaValue_generic_DateFormatItem_h },
            { "DateFormatItem.GyMd", metaValue_generic_DateFormatItem_yyyyMd },
            { "DateFormatItem.GyMMMEd", metaValue_generic_DateFormatItem_yyyyMMMEd },
            { "DateFormatItem.yMd", metaValue_generic_DateFormatItem_yMd },
            { "DateFormatItem.MMMd", metaValue_generic_DateFormatItem_MMMd },
            { "DateFormatItem.yM", metaValue_generic_DateFormatItem_yM },
            { "DateFormatItem.yMMMM", metaValue_generic_DateFormatItem_yMMMM },
            { "DateFormatItem.MMMMW", metaValue_generic_DateFormatItem_MMMMW },
            { "DateFormatItem.yQQQQ", metaValue_generic_DateFormatItem_yQQQQ },
            { "DateFormatItem.GyMMM", metaValue_generic_DateFormatItem_yyyyMMM },
            { "buddhist.MonthNames", metaValue_MonthNames },
            { "buddhist.MonthAbbreviations", metaValue_MonthAbbreviations },
            { "buddhist.MonthNarrows", metaValue_MonthNarrows },
            { "buddhist.DayNames", metaValue_generic_DayNames },
            { "buddhist.DayAbbreviations", metaValue_generic_DayAbbreviations },
            { "buddhist.DayNarrows", metaValue_generic_DayNarrows },
            { "buddhist.QuarterNames", metaValue_generic_QuarterNames },
            { "buddhist.QuarterAbbreviations", metaValue_generic_QuarterAbbreviations },
            { "buddhist.QuarterNarrows", metaValue_generic_QuarterNarrows },
            { "buddhist.AmPmMarkers", metaValue_generic_AmPmMarkers },
            { "buddhist.narrow.AmPmMarkers", metaValue_generic_AmPmMarkers },
            { "buddhist.abbreviated.AmPmMarkers", metaValue_generic_AmPmMarkers },
            { "java.time.buddhist.long.Eras", metaValue_java_time_buddhist_long_Eras },
            { "buddhist.long.Eras", metaValue_java_time_buddhist_long_Eras },
            { "java.time.buddhist.Eras", metaValue_java_time_buddhist_long_Eras },
            { "buddhist.Eras", metaValue_java_time_buddhist_long_Eras },
            { "java.time.buddhist.narrow.Eras", metaValue_java_time_buddhist_long_Eras },
            { "buddhist.narrow.Eras", metaValue_java_time_buddhist_long_Eras },
            { "buddhist.TimePatterns", metaValue_generic_TimePatterns },
            { "java.time.buddhist.DatePatterns", metaValue_java_time_generic_DatePatterns },
            { "buddhist.DatePatterns", metaValue_generic_DatePatterns },
            { "buddhist.DateTimePatterns", metaValue_generic_DateTimePatterns },
            { "buddhist.DateFormatItem.hm", metaValue_generic_DateFormatItem_hm },
            { "buddhist.DateFormatItem.yyyyMMMM", metaValue_generic_DateFormatItem_yyyyMMMM },
            { "buddhist.DateFormatItem.yMMMEd", metaValue_generic_DateFormatItem_yMMMEd },
            { "buddhist.DateFormatItem.Ehms", metaValue_generic_DateFormatItem_Ehms },
            { "buddhist.DateFormatItem.yyyyQQQQ", metaValue_generic_DateFormatItem_yyyyQQQQ },
            { "buddhist.DateFormatItem.Ehm", metaValue_generic_DateFormatItem_Ehm },
            { "buddhist.DateFormatItem.yMEd", metaValue_generic_DateFormatItem_yMEd },
            { "buddhist.DateFormatItem.hms", metaValue_generic_DateFormatItem_hms },
            { "buddhist.DateFormatItem.yyyyMMMd", metaValue_generic_DateFormatItem_yyyyMMMd },
            { "buddhist.DateFormatItem.hmv", metaValue_generic_DateFormatItem_hmv },
            { "buddhist.DateFormatItem.Gy", metaValue_generic_DateFormatItem_Gy },
            { "buddhist.DateFormatItem.Bhms", metaValue_buddhist_DateFormatItem_Bhms },
            { "buddhist.DateFormatItem.yyyyMMMEd", metaValue_generic_DateFormatItem_yyyyMMMEd },
            { "buddhist.DateFormatItem.hmsv", metaValue_generic_DateFormatItem_hmsv },
            { "buddhist.DateFormatItem.GyMMMd", metaValue_generic_DateFormatItem_yyyyMMMd },
            { "buddhist.DateFormatItem.yMMMd", metaValue_generic_DateFormatItem_yMMMd },
            { "buddhist.DateFormatItem.yw", metaValue_generic_DateFormatItem_yw },
            { "buddhist.DateFormatItem.yQQQ", metaValue_generic_DateFormatItem_yQQQ },
            { "buddhist.DateFormatItem.yyyyMEd", metaValue_generic_DateFormatItem_yyyyMEd },
            { "buddhist.DateFormatItem.Hm", metaValue_buddhist_DateFormatItem_Hm },
            { "buddhist.DateFormatItem.ms", metaValue_buddhist_DateFormatItem_ms },
            { "buddhist.DateFormatItem.EHms", metaValue_buddhist_DateFormatItem_EHms },
            { "buddhist.DateFormatItem.EHm", metaValue_buddhist_DateFormatItem_EHm },
            { "buddhist.DateFormatItem.yyyyMd", metaValue_generic_DateFormatItem_yyyyMd },
            { "buddhist.DateFormatItem.MMM", metaValue_buddhist_DateFormatItem_MMM },
            { "buddhist.DateFormatItem.yMMM", metaValue_generic_DateFormatItem_yMMM },
            { "buddhist.DateFormatItem.Md", metaValue_generic_DateFormatItem_Md },
            { "buddhist.DateFormatItem.EBhms", metaValue_buddhist_DateFormatItem_EBhms },
            { "buddhist.DateFormatItem.Ed", metaValue_generic_DateFormatItem_Ed },
            { "buddhist.DateFormatItem.yyyyMMM", metaValue_generic_DateFormatItem_yyyyMMM },
            { "buddhist.DateFormatItem.EBhm", metaValue_buddhist_DateFormatItem_EBhm },
            { "buddhist.DateFormatItem.E", metaValue_buddhist_DateFormatItem_E },
            { "buddhist.DateFormatItem.MMMMd", metaValue_generic_DateFormatItem_MMMMd },
            { "buddhist.DateFormatItem.Bhm", metaValue_buddhist_DateFormatItem_Bhm },
            { "buddhist.DateFormatItem.H", metaValue_buddhist_DateFormatItem_H },
            { "buddhist.DateFormatItem.M", metaValue_generic_DateFormatItem_M },
            { "buddhist.DateFormatItem.MEd", metaValue_generic_DateFormatItem_MEd },
            { "buddhist.DateFormatItem.MMMEd", metaValue_generic_DateFormatItem_MMMEd },
            { "buddhist.DateFormatItem.yyyyQQQ", metaValue_generic_DateFormatItem_yyyyQQQ },
            { "buddhist.DateFormatItem.d", metaValue_buddhist_DateFormatItem_d },
            { "buddhist.DateFormatItem.Bh", metaValue_buddhist_DateFormatItem_Bh },
            { "buddhist.DateFormatItem.h", metaValue_generic_DateFormatItem_h },
            { "buddhist.DateFormatItem.Hmsv", metaValue_generic_DateFormatItem_Hmsv },
            { "buddhist.DateFormatItem.GyMd", metaValue_generic_DateFormatItem_yyyyMd },
            { "buddhist.DateFormatItem.GyMMMEd", metaValue_generic_DateFormatItem_yyyyMMMEd },
            { "buddhist.DateFormatItem.yyyyM", metaValue_generic_DateFormatItem_yyyyM },
            { "buddhist.DateFormatItem.Hms", metaValue_buddhist_DateFormatItem_Hms },
            { "buddhist.DateFormatItem.Hmv", metaValue_generic_DateFormatItem_Hmv },
            { "buddhist.DateFormatItem.yMd", metaValue_generic_DateFormatItem_yMd },
            { "buddhist.DateFormatItem.MMMd", metaValue_generic_DateFormatItem_MMMd },
            { "buddhist.DateFormatItem.yM", metaValue_generic_DateFormatItem_yM },
            { "buddhist.DateFormatItem.yMMMM", metaValue_generic_DateFormatItem_yMMMM },
            { "buddhist.DateFormatItem.MMMMW", metaValue_generic_DateFormatItem_MMMMW },
            { "buddhist.DateFormatItem.yQQQQ", metaValue_generic_DateFormatItem_yQQQQ },
            { "buddhist.DateFormatItem.yyyy", metaValue_generic_DateFormatItem_Gy },
            { "buddhist.DateFormatItem.y", metaValue_generic_DateFormatItem_y },
            { "buddhist.DateFormatItem.GyMMM", metaValue_generic_DateFormatItem_yyyyMMM },
            { "japanese.MonthNames", metaValue_MonthNames },
            { "japanese.MonthAbbreviations", metaValue_MonthAbbreviations },
            { "japanese.MonthNarrows", metaValue_MonthNarrows },
            { "japanese.DayNames", metaValue_generic_DayNames },
            { "japanese.DayAbbreviations", metaValue_generic_DayAbbreviations },
            { "japanese.DayNarrows", metaValue_generic_DayNarrows },
            { "japanese.QuarterNames", metaValue_generic_QuarterNames },
            { "japanese.QuarterAbbreviations", metaValue_generic_QuarterAbbreviations },
            { "japanese.QuarterNarrows", metaValue_generic_QuarterNarrows },
            { "japanese.AmPmMarkers", metaValue_generic_AmPmMarkers },
            { "japanese.narrow.AmPmMarkers", metaValue_generic_AmPmMarkers },
            { "japanese.abbreviated.AmPmMarkers", metaValue_generic_AmPmMarkers },
            { "japanese.TimePatterns", metaValue_generic_TimePatterns },
            { "java.time.japanese.DatePatterns", metaValue_java_time_generic_DatePatterns },
            { "japanese.DatePatterns", metaValue_generic_DatePatterns },
            { "japanese.DateFormatItem.hm", metaValue_generic_DateFormatItem_hm },
            { "japanese.DateFormatItem.yMMMEd", metaValue_generic_DateFormatItem_yMMMEd },
            { "japanese.DateFormatItem.Ehms", metaValue_generic_DateFormatItem_Ehms },
            { "japanese.DateFormatItem.Ehm", metaValue_generic_DateFormatItem_Ehm },
            { "japanese.DateFormatItem.yMEd", metaValue_generic_DateFormatItem_yMEd },
            { "japanese.DateFormatItem.hms", metaValue_generic_DateFormatItem_hms },
            { "japanese.DateFormatItem.hmv", metaValue_generic_DateFormatItem_hmv },
            { "japanese.DateFormatItem.Gy", metaValue_generic_DateFormatItem_Gy },
            { "japanese.DateFormatItem.Bhms", metaValue_buddhist_DateFormatItem_Bhms },
            { "japanese.DateFormatItem.hmsv", metaValue_generic_DateFormatItem_hmsv },
            { "japanese.DateFormatItem.GyMMMd", metaValue_generic_DateFormatItem_yyyyMMMd },
            { "japanese.DateFormatItem.yMMMd", metaValue_generic_DateFormatItem_yMMMd },
            { "japanese.DateFormatItem.yw", metaValue_generic_DateFormatItem_yw },
            { "japanese.DateFormatItem.yQQQ", metaValue_generic_DateFormatItem_yQQQ },
            { "japanese.DateFormatItem.Hm", metaValue_buddhist_DateFormatItem_Hm },
            { "japanese.DateFormatItem.ms", metaValue_buddhist_DateFormatItem_ms },
            { "japanese.DateFormatItem.EHms", metaValue_buddhist_DateFormatItem_EHms },
            { "japanese.DateFormatItem.EHm", metaValue_buddhist_DateFormatItem_EHm },
            { "japanese.DateFormatItem.MMM", metaValue_buddhist_DateFormatItem_MMM },
            { "japanese.DateFormatItem.yMMM", metaValue_generic_DateFormatItem_yMMM },
            { "japanese.DateFormatItem.Md", metaValue_generic_DateFormatItem_Md },
            { "japanese.DateFormatItem.EBhms", metaValue_buddhist_DateFormatItem_EBhms },
            { "japanese.DateFormatItem.Ed", metaValue_generic_DateFormatItem_Ed },
            { "japanese.DateFormatItem.EBhm", metaValue_buddhist_DateFormatItem_EBhm },
            { "japanese.DateFormatItem.E", metaValue_buddhist_DateFormatItem_E },
            { "japanese.DateFormatItem.MMMMd", metaValue_generic_DateFormatItem_MMMMd },
            { "japanese.DateFormatItem.Bhm", metaValue_buddhist_DateFormatItem_Bhm },
            { "japanese.DateFormatItem.H", metaValue_buddhist_DateFormatItem_H },
            { "japanese.DateFormatItem.M", metaValue_generic_DateFormatItem_M },
            { "japanese.DateFormatItem.MEd", metaValue_generic_DateFormatItem_MEd },
            { "japanese.DateFormatItem.MMMEd", metaValue_generic_DateFormatItem_MMMEd },
            { "japanese.DateFormatItem.d", metaValue_buddhist_DateFormatItem_d },
            { "japanese.DateFormatItem.Bh", metaValue_buddhist_DateFormatItem_Bh },
            { "japanese.DateFormatItem.h", metaValue_generic_DateFormatItem_h },
            { "japanese.DateFormatItem.Hmsv", metaValue_generic_DateFormatItem_Hmsv },
            { "japanese.DateFormatItem.GyMd", metaValue_generic_DateFormatItem_yyyyMd },
            { "japanese.DateFormatItem.GyMMMEd", metaValue_generic_DateFormatItem_yyyyMMMEd },
            { "japanese.DateFormatItem.Hms", metaValue_buddhist_DateFormatItem_Hms },
            { "japanese.DateFormatItem.Hmv", metaValue_generic_DateFormatItem_Hmv },
            { "japanese.DateFormatItem.yMd", metaValue_generic_DateFormatItem_yMd },
            { "japanese.DateFormatItem.MMMd", metaValue_generic_DateFormatItem_MMMd },
            { "japanese.DateFormatItem.yM", metaValue_generic_DateFormatItem_yM },
            { "japanese.DateFormatItem.yMMMM", metaValue_generic_DateFormatItem_yMMMM },
            { "japanese.DateFormatItem.MMMMW", metaValue_generic_DateFormatItem_MMMMW },
            { "japanese.DateFormatItem.yQQQQ", metaValue_generic_DateFormatItem_yQQQQ },
            { "japanese.DateFormatItem.y", metaValue_generic_DateFormatItem_y },
            { "japanese.DateFormatItem.GyMMM", metaValue_generic_DateFormatItem_yyyyMMM },
            { "roc.MonthNames", metaValue_MonthNames },
            { "roc.MonthAbbreviations", metaValue_MonthAbbreviations },
            { "roc.MonthNarrows", metaValue_MonthNarrows },
            { "roc.DayNames", metaValue_generic_DayNames },
            { "roc.DayAbbreviations", metaValue_generic_DayAbbreviations },
            { "roc.DayNarrows", metaValue_generic_DayNarrows },
            { "roc.QuarterNames", metaValue_generic_QuarterNames },
            { "roc.QuarterAbbreviations", metaValue_generic_QuarterAbbreviations },
            { "roc.QuarterNarrows", metaValue_generic_QuarterNarrows },
            { "roc.AmPmMarkers", metaValue_generic_AmPmMarkers },
            { "roc.narrow.AmPmMarkers", metaValue_generic_AmPmMarkers },
            { "roc.abbreviated.AmPmMarkers", metaValue_generic_AmPmMarkers },
            { "roc.TimePatterns", metaValue_generic_TimePatterns },
            { "java.time.roc.DatePatterns", metaValue_java_time_generic_DatePatterns },
            { "roc.DatePatterns", metaValue_generic_DatePatterns },
            { "roc.DateFormatItem.hm", metaValue_generic_DateFormatItem_hm },
            { "roc.DateFormatItem.yMMMEd", metaValue_generic_DateFormatItem_yMMMEd },
            { "roc.DateFormatItem.Ehms", metaValue_generic_DateFormatItem_Ehms },
            { "roc.DateFormatItem.Ehm", metaValue_generic_DateFormatItem_Ehm },
            { "roc.DateFormatItem.yMEd", metaValue_generic_DateFormatItem_yMEd },
            { "roc.DateFormatItem.hms", metaValue_generic_DateFormatItem_hms },
            { "roc.DateFormatItem.hmv", metaValue_generic_DateFormatItem_hmv },
            { "roc.DateFormatItem.Gy", metaValue_generic_DateFormatItem_Gy },
            { "roc.DateFormatItem.Bhms", metaValue_buddhist_DateFormatItem_Bhms },
            { "roc.DateFormatItem.hmsv", metaValue_generic_DateFormatItem_hmsv },
            { "roc.DateFormatItem.GyMMMd", metaValue_generic_DateFormatItem_yyyyMMMd },
            { "roc.DateFormatItem.yMMMd", metaValue_generic_DateFormatItem_yMMMd },
            { "roc.DateFormatItem.yw", metaValue_generic_DateFormatItem_yw },
            { "roc.DateFormatItem.yQQQ", metaValue_generic_DateFormatItem_yQQQ },
            { "roc.DateFormatItem.Hm", metaValue_buddhist_DateFormatItem_Hm },
            { "roc.DateFormatItem.ms", metaValue_buddhist_DateFormatItem_ms },
            { "roc.DateFormatItem.EHms", metaValue_buddhist_DateFormatItem_EHms },
            { "roc.DateFormatItem.EHm", metaValue_buddhist_DateFormatItem_EHm },
            { "roc.DateFormatItem.MMM", metaValue_buddhist_DateFormatItem_MMM },
            { "roc.DateFormatItem.yMMM", metaValue_generic_DateFormatItem_yMMM },
            { "roc.DateFormatItem.Md", metaValue_generic_DateFormatItem_Md },
            { "roc.DateFormatItem.EBhms", metaValue_buddhist_DateFormatItem_EBhms },
            { "roc.DateFormatItem.Ed", metaValue_generic_DateFormatItem_Ed },
            { "roc.DateFormatItem.EBhm", metaValue_buddhist_DateFormatItem_EBhm },
            { "roc.DateFormatItem.E", metaValue_buddhist_DateFormatItem_E },
            { "roc.DateFormatItem.MMMMd", metaValue_generic_DateFormatItem_MMMMd },
            { "roc.DateFormatItem.Bhm", metaValue_buddhist_DateFormatItem_Bhm },
            { "roc.DateFormatItem.H", metaValue_buddhist_DateFormatItem_H },
            { "roc.DateFormatItem.M", metaValue_generic_DateFormatItem_M },
            { "roc.DateFormatItem.MEd", metaValue_generic_DateFormatItem_MEd },
            { "roc.DateFormatItem.MMMEd", metaValue_generic_DateFormatItem_MMMEd },
            { "roc.DateFormatItem.d", metaValue_buddhist_DateFormatItem_d },
            { "roc.DateFormatItem.Bh", metaValue_buddhist_DateFormatItem_Bh },
            { "roc.DateFormatItem.h", metaValue_generic_DateFormatItem_h },
            { "roc.DateFormatItem.Hmsv", metaValue_generic_DateFormatItem_Hmsv },
            { "roc.DateFormatItem.GyMd", metaValue_generic_DateFormatItem_yyyyMd },
            { "roc.DateFormatItem.GyMMMEd", metaValue_generic_DateFormatItem_yyyyMMMEd },
            { "roc.DateFormatItem.Hms", metaValue_buddhist_DateFormatItem_Hms },
            { "roc.DateFormatItem.Hmv", metaValue_generic_DateFormatItem_Hmv },
            { "roc.DateFormatItem.yMd", metaValue_generic_DateFormatItem_yMd },
            { "roc.DateFormatItem.MMMd", metaValue_generic_DateFormatItem_MMMd },
            { "roc.DateFormatItem.yM", metaValue_generic_DateFormatItem_yM },
            { "roc.DateFormatItem.yMMMM", metaValue_generic_DateFormatItem_yMMMM },
            { "roc.DateFormatItem.MMMMW", metaValue_generic_DateFormatItem_MMMMW },
            { "roc.DateFormatItem.yQQQQ", metaValue_generic_DateFormatItem_yQQQQ },
            { "roc.DateFormatItem.y", metaValue_generic_DateFormatItem_y },
            { "roc.DateFormatItem.GyMMM", metaValue_generic_DateFormatItem_yyyyMMM },
            { "islamic.DayNames", metaValue_generic_DayNames },
            { "islamic.DayAbbreviations", metaValue_generic_DayAbbreviations },
            { "islamic.DayNarrows", metaValue_generic_DayNarrows },
            { "islamic.QuarterNames", metaValue_generic_QuarterNames },
            { "islamic.QuarterAbbreviations", metaValue_generic_QuarterAbbreviations },
            { "islamic.QuarterNarrows", metaValue_generic_QuarterNarrows },
            { "islamic.AmPmMarkers", metaValue_generic_AmPmMarkers },
            { "islamic.narrow.AmPmMarkers", metaValue_generic_AmPmMarkers },
            { "islamic.abbreviated.AmPmMarkers", metaValue_generic_AmPmMarkers },
            { "islamic.TimePatterns", metaValue_generic_TimePatterns },
            { "java.time.islamic.DatePatterns", metaValue_java_time_generic_DatePatterns },
            { "islamic.DatePatterns", metaValue_generic_DatePatterns },
            { "islamic.DateFormatItem.hm", metaValue_generic_DateFormatItem_hm },
            { "islamic.DateFormatItem.yMMMEd", metaValue_generic_DateFormatItem_yMMMEd },
            { "islamic.DateFormatItem.Ehms", metaValue_generic_DateFormatItem_Ehms },
            { "islamic.DateFormatItem.Ehm", metaValue_generic_DateFormatItem_Ehm },
            { "islamic.DateFormatItem.yMEd", metaValue_generic_DateFormatItem_yMEd },
            { "islamic.DateFormatItem.hms", metaValue_generic_DateFormatItem_hms },
            { "islamic.DateFormatItem.hmv", metaValue_generic_DateFormatItem_hmv },
            { "islamic.DateFormatItem.Gy", metaValue_generic_DateFormatItem_Gy },
            { "islamic.DateFormatItem.Bhms", metaValue_buddhist_DateFormatItem_Bhms },
            { "islamic.DateFormatItem.hmsv", metaValue_generic_DateFormatItem_hmsv },
            { "islamic.DateFormatItem.GyMMMd", metaValue_generic_DateFormatItem_yyyyMMMd },
            { "islamic.DateFormatItem.yMMMd", metaValue_generic_DateFormatItem_yMMMd },
            { "islamic.DateFormatItem.yw", metaValue_generic_DateFormatItem_yw },
            { "islamic.DateFormatItem.yQQQ", metaValue_generic_DateFormatItem_yQQQ },
            { "islamic.DateFormatItem.Hm", metaValue_buddhist_DateFormatItem_Hm },
            { "islamic.DateFormatItem.ms", metaValue_buddhist_DateFormatItem_ms },
            { "islamic.DateFormatItem.EHms", metaValue_buddhist_DateFormatItem_EHms },
            { "islamic.DateFormatItem.EHm", metaValue_buddhist_DateFormatItem_EHm },
            { "islamic.DateFormatItem.MMM", metaValue_buddhist_DateFormatItem_MMM },
            { "islamic.DateFormatItem.yMMM", metaValue_generic_DateFormatItem_yMMM },
            { "islamic.DateFormatItem.Md", metaValue_generic_DateFormatItem_Md },
            { "islamic.DateFormatItem.EBhms", metaValue_buddhist_DateFormatItem_EBhms },
            { "islamic.DateFormatItem.Ed", metaValue_generic_DateFormatItem_Ed },
            { "islamic.DateFormatItem.EBhm", metaValue_buddhist_DateFormatItem_EBhm },
            { "islamic.DateFormatItem.E", metaValue_buddhist_DateFormatItem_E },
            { "islamic.DateFormatItem.MMMMd", metaValue_generic_DateFormatItem_MMMMd },
            { "islamic.DateFormatItem.Bhm", metaValue_buddhist_DateFormatItem_Bhm },
            { "islamic.DateFormatItem.H", metaValue_buddhist_DateFormatItem_H },
            { "islamic.DateFormatItem.M", metaValue_generic_DateFormatItem_M },
            { "islamic.DateFormatItem.MEd", metaValue_generic_DateFormatItem_MEd },
            { "islamic.DateFormatItem.MMMEd", metaValue_generic_DateFormatItem_MMMEd },
            { "islamic.DateFormatItem.d", metaValue_buddhist_DateFormatItem_d },
            { "islamic.DateFormatItem.Bh", metaValue_buddhist_DateFormatItem_Bh },
            { "islamic.DateFormatItem.h", metaValue_generic_DateFormatItem_h },
            { "islamic.DateFormatItem.Hmsv", metaValue_generic_DateFormatItem_Hmsv },
            { "islamic.DateFormatItem.GyMd", metaValue_generic_DateFormatItem_yyyyMd },
            { "islamic.DateFormatItem.GyMMMEd", metaValue_generic_DateFormatItem_yyyyMMMEd },
            { "islamic.DateFormatItem.Hms", metaValue_buddhist_DateFormatItem_Hms },
            { "islamic.DateFormatItem.Hmv", metaValue_generic_DateFormatItem_Hmv },
            { "islamic.DateFormatItem.yMd", metaValue_generic_DateFormatItem_yMd },
            { "islamic.DateFormatItem.MMMd", metaValue_generic_DateFormatItem_MMMd },
            { "islamic.DateFormatItem.yM", metaValue_generic_DateFormatItem_yM },
            { "islamic.DateFormatItem.yMMMM", metaValue_generic_DateFormatItem_yMMMM },
            { "islamic.DateFormatItem.MMMMW", metaValue_generic_DateFormatItem_MMMMW },
            { "islamic.DateFormatItem.yQQQQ", metaValue_generic_DateFormatItem_yQQQQ },
            { "islamic.DateFormatItem.y", metaValue_generic_DateFormatItem_y },
            { "islamic.DateFormatItem.GyMMM", metaValue_generic_DateFormatItem_yyyyMMM },
            { "islamic-civil.DateFormatItem.hm", metaValue_generic_DateFormatItem_hm },
            { "islamic-civil.DateFormatItem.yMMMEd", metaValue_generic_DateFormatItem_yMMMEd },
            { "islamic-civil.DateFormatItem.Ehms", metaValue_generic_DateFormatItem_Ehms },
            { "islamic-civil.DateFormatItem.Ehm", metaValue_generic_DateFormatItem_Ehm },
            { "islamic-civil.DateFormatItem.yMEd", metaValue_generic_DateFormatItem_yMEd },
            { "islamic-civil.DateFormatItem.hms", metaValue_generic_DateFormatItem_hms },
            { "islamic-civil.DateFormatItem.hmv", metaValue_generic_DateFormatItem_hmv },
            { "islamic-civil.DateFormatItem.Gy", metaValue_generic_DateFormatItem_Gy },
            { "islamic-civil.DateFormatItem.Bhms", metaValue_buddhist_DateFormatItem_Bhms },
            { "islamic-civil.DateFormatItem.hmsv", metaValue_generic_DateFormatItem_hmsv },
            { "islamic-civil.DateFormatItem.GyMMMd", metaValue_generic_DateFormatItem_yyyyMMMd },
            { "islamic-civil.DateFormatItem.yMMMd", metaValue_generic_DateFormatItem_yMMMd },
            { "islamic-civil.DateFormatItem.yw", metaValue_generic_DateFormatItem_yw },
            { "islamic-civil.DateFormatItem.yQQQ", metaValue_generic_DateFormatItem_yQQQ },
            { "islamic-civil.DateFormatItem.Hm", metaValue_buddhist_DateFormatItem_Hm },
            { "islamic-civil.DateFormatItem.ms", metaValue_buddhist_DateFormatItem_ms },
            { "islamic-civil.DateFormatItem.EHms", metaValue_buddhist_DateFormatItem_EHms },
            { "islamic-civil.DateFormatItem.EHm", metaValue_buddhist_DateFormatItem_EHm },
            { "islamic-civil.DateFormatItem.MMM", metaValue_buddhist_DateFormatItem_MMM },
            { "islamic-civil.DateFormatItem.yMMM", metaValue_generic_DateFormatItem_yMMM },
            { "islamic-civil.DateFormatItem.Md", metaValue_generic_DateFormatItem_Md },
            { "islamic-civil.DateFormatItem.EBhms", metaValue_buddhist_DateFormatItem_EBhms },
            { "islamic-civil.DateFormatItem.Ed", metaValue_generic_DateFormatItem_Ed },
            { "islamic-civil.DateFormatItem.EBhm", metaValue_buddhist_DateFormatItem_EBhm },
            { "islamic-civil.DateFormatItem.E", metaValue_buddhist_DateFormatItem_E },
            { "islamic-civil.DateFormatItem.MMMMd", metaValue_generic_DateFormatItem_MMMMd },
            { "islamic-civil.DateFormatItem.Bhm", metaValue_buddhist_DateFormatItem_Bhm },
            { "islamic-civil.DateFormatItem.H", metaValue_buddhist_DateFormatItem_H },
            { "islamic-civil.DateFormatItem.M", metaValue_generic_DateFormatItem_M },
            { "islamic-civil.DateFormatItem.MEd", metaValue_generic_DateFormatItem_MEd },
            { "islamic-civil.DateFormatItem.MMMEd", metaValue_generic_DateFormatItem_MMMEd },
            { "islamic-civil.DateFormatItem.d", metaValue_buddhist_DateFormatItem_d },
            { "islamic-civil.DateFormatItem.Bh", metaValue_buddhist_DateFormatItem_Bh },
            { "islamic-civil.DateFormatItem.h", metaValue_generic_DateFormatItem_h },
            { "islamic-civil.DateFormatItem.Hmsv", metaValue_generic_DateFormatItem_Hmsv },
            { "islamic-civil.DateFormatItem.GyMd", metaValue_generic_DateFormatItem_yyyyMd },
            { "islamic-civil.DateFormatItem.GyMMMEd", metaValue_generic_DateFormatItem_yyyyMMMEd },
            { "islamic-civil.DateFormatItem.Hms", metaValue_buddhist_DateFormatItem_Hms },
            { "islamic-civil.DateFormatItem.Hmv", metaValue_generic_DateFormatItem_Hmv },
            { "islamic-civil.DateFormatItem.yMd", metaValue_generic_DateFormatItem_yMd },
            { "islamic-civil.DateFormatItem.MMMd", metaValue_generic_DateFormatItem_MMMd },
            { "islamic-civil.DateFormatItem.yM", metaValue_generic_DateFormatItem_yM },
            { "islamic-civil.DateFormatItem.yMMMM", metaValue_generic_DateFormatItem_yMMMM },
            { "islamic-civil.DateFormatItem.MMMMW", metaValue_generic_DateFormatItem_MMMMW },
            { "islamic-civil.DateFormatItem.yQQQQ", metaValue_generic_DateFormatItem_yQQQQ },
            { "islamic-civil.DateFormatItem.y", metaValue_generic_DateFormatItem_y },
            { "islamic-civil.DateFormatItem.GyMMM", metaValue_generic_DateFormatItem_yyyyMMM },
            { "islamic-umalqura.DateFormatItem.hm", metaValue_generic_DateFormatItem_hm },
            { "islamic-umalqura.DateFormatItem.yMMMEd", metaValue_generic_DateFormatItem_yMMMEd },
            { "islamic-umalqura.DateFormatItem.Ehms", metaValue_generic_DateFormatItem_Ehms },
            { "islamic-umalqura.DateFormatItem.Ehm", metaValue_generic_DateFormatItem_Ehm },
            { "islamic-umalqura.DateFormatItem.yMEd", metaValue_generic_DateFormatItem_yMEd },
            { "islamic-umalqura.DateFormatItem.hms", metaValue_generic_DateFormatItem_hms },
            { "islamic-umalqura.DateFormatItem.hmv", metaValue_generic_DateFormatItem_hmv },
            { "islamic-umalqura.DateFormatItem.Gy", metaValue_generic_DateFormatItem_Gy },
            { "islamic-umalqura.DateFormatItem.Bhms", metaValue_buddhist_DateFormatItem_Bhms },
            { "islamic-umalqura.DateFormatItem.hmsv", metaValue_generic_DateFormatItem_hmsv },
            { "islamic-umalqura.DateFormatItem.GyMMMd", metaValue_generic_DateFormatItem_yyyyMMMd },
            { "islamic-umalqura.DateFormatItem.yMMMd", metaValue_generic_DateFormatItem_yMMMd },
            { "islamic-umalqura.DateFormatItem.yw", metaValue_generic_DateFormatItem_yw },
            { "islamic-umalqura.DateFormatItem.yQQQ", metaValue_generic_DateFormatItem_yQQQ },
            { "islamic-umalqura.DateFormatItem.Hm", metaValue_buddhist_DateFormatItem_Hm },
            { "islamic-umalqura.DateFormatItem.ms", metaValue_buddhist_DateFormatItem_ms },
            { "islamic-umalqura.DateFormatItem.EHms", metaValue_buddhist_DateFormatItem_EHms },
            { "islamic-umalqura.DateFormatItem.EHm", metaValue_buddhist_DateFormatItem_EHm },
            { "islamic-umalqura.DateFormatItem.MMM", metaValue_buddhist_DateFormatItem_MMM },
            { "islamic-umalqura.DateFormatItem.yMMM", metaValue_generic_DateFormatItem_yMMM },
            { "islamic-umalqura.DateFormatItem.Md", metaValue_generic_DateFormatItem_Md },
            { "islamic-umalqura.DateFormatItem.EBhms", metaValue_buddhist_DateFormatItem_EBhms },
            { "islamic-umalqura.DateFormatItem.Ed", metaValue_generic_DateFormatItem_Ed },
            { "islamic-umalqura.DateFormatItem.EBhm", metaValue_buddhist_DateFormatItem_EBhm },
            { "islamic-umalqura.DateFormatItem.E", metaValue_buddhist_DateFormatItem_E },
            { "islamic-umalqura.DateFormatItem.MMMMd", metaValue_generic_DateFormatItem_MMMMd },
            { "islamic-umalqura.DateFormatItem.Bhm", metaValue_buddhist_DateFormatItem_Bhm },
            { "islamic-umalqura.DateFormatItem.H", metaValue_buddhist_DateFormatItem_H },
            { "islamic-umalqura.DateFormatItem.M", metaValue_generic_DateFormatItem_M },
            { "islamic-umalqura.DateFormatItem.MEd", metaValue_generic_DateFormatItem_MEd },
            { "islamic-umalqura.DateFormatItem.MMMEd", metaValue_generic_DateFormatItem_MMMEd },
            { "islamic-umalqura.DateFormatItem.d", metaValue_buddhist_DateFormatItem_d },
            { "islamic-umalqura.DateFormatItem.Bh", metaValue_buddhist_DateFormatItem_Bh },
            { "islamic-umalqura.DateFormatItem.h", metaValue_generic_DateFormatItem_h },
            { "islamic-umalqura.DateFormatItem.Hmsv", metaValue_generic_DateFormatItem_Hmsv },
            { "islamic-umalqura.DateFormatItem.GyMd", metaValue_generic_DateFormatItem_yyyyMd },
            { "islamic-umalqura.DateFormatItem.GyMMMEd", metaValue_generic_DateFormatItem_yyyyMMMEd },
            { "islamic-umalqura.DateFormatItem.Hms", metaValue_buddhist_DateFormatItem_Hms },
            { "islamic-umalqura.DateFormatItem.Hmv", metaValue_generic_DateFormatItem_Hmv },
            { "islamic-umalqura.DateFormatItem.yMd", metaValue_generic_DateFormatItem_yMd },
            { "islamic-umalqura.DateFormatItem.MMMd", metaValue_generic_DateFormatItem_MMMd },
            { "islamic-umalqura.DateFormatItem.yM", metaValue_generic_DateFormatItem_yM },
            { "islamic-umalqura.DateFormatItem.yMMMM", metaValue_generic_DateFormatItem_yMMMM },
            { "islamic-umalqura.DateFormatItem.MMMMW", metaValue_generic_DateFormatItem_MMMMW },
            { "islamic-umalqura.DateFormatItem.yQQQQ", metaValue_generic_DateFormatItem_yQQQQ },
            { "islamic-umalqura.DateFormatItem.y", metaValue_generic_DateFormatItem_y },
            { "islamic-umalqura.DateFormatItem.GyMMM", metaValue_generic_DateFormatItem_yyyyMMM },
            { "calendarname.islamic-civil", "F\u00e9ilire Ioslamach (t\u00e1blach, seanr\u00e9 shibhialta)" },
            { "calendarname.buddhist", "F\u00e9ilire B\u00fada\u00edoch" },
            { "calendarname.japanese", "F\u00e9ilire Seap\u00e1nach" },
            { "calendarname.roc", "F\u00e9ilire T\u00e9av\u00e1nach" },
            { "calendarname.islamic-umalqura", "F\u00e9ilire Ioslamach (Umm al-Qura)" },
            { "calendarname.islamic", "F\u00e9ilire Ioslamach" },
            { "calendarname.gregorian", metaValue_calendarname_gregorian },
            { "calendarname.gregory", metaValue_calendarname_gregorian },
            { "latn.NumberElements",
                new String[] {
                    ".",
                    ",",
                    ";",
                    "%",
                    "0",
                    "#",
                    "-",
                    "E",
                    "\u2030",
                    "\u221e",
                    "Nuimh",
                    "",
                    "",
                }
            },
            { "latn.NumberPatterns",
                new String[] {
                    "#,##0.###",
                    "\u00a4#,##0.00",
                    "#,##0%",
                    "\u00a4#,##0.00;(\u00a4#,##0.00)",
                }
            },
            { "short.CompactNumberPatterns",
                new String[] {
                    "",
                    "",
                    "",
                    "{one:0k two:0k few:0k many:0k other:0k}",
                    "{one:00k two:00k few:00k many:00k other:00k}",
                    "{one:000k two:000k few:000k many:000k other:000k}",
                    "{one:0M two:0M few:0M many:0M other:0M}",
                    "{one:00M two:00M few:00M many:00M other:00M}",
                    "{one:000M two:000M few:000M many:000M other:000M}",
                    "{one:0B two:0B few:0B many:0B other:0B}",
                    "{one:00B two:00B few:00B many:00B other:00B}",
                    "{one:000B two:000B few:000B many:000B other:000B}",
                    "{one:0T two:0T few:0T many:0T other:0T}",
                    "{one:00T two:00T few:00T many:00T other:00T}",
                    "{one:000T two:000T few:000T many:000T other:000T}",
                }
            },
            { "long.CompactNumberPatterns",
                new String[] {
                    "",
                    "",
                    "",
                    "{one:0' 'mh\u00edle two:0' 'mh\u00edle few:0' 'mh\u00edle many:0' 'm\u00edle other:0' 'm\u00edle}",
                    "{one:00' 'm\u00edle two:00' 'm\u00edle few:00' 'm\u00edle many:00' 'm\u00edle other:00' 'm\u00edle}",
                    "{one:000' 'm\u00edle two:000' 'm\u00edle few:000' 'm\u00edle many:000' 'm\u00edle other:000' 'm\u00edle}",
                    "{one:0' 'mhilli\u00fan two:0' 'mhilli\u00fan few:0' 'mhilli\u00fan many:0' 'milli\u00fan other:0' 'milli\u00fan}",
                    "{one:00' 'milli\u00fan two:00' 'milli\u00fan few:00' 'milli\u00fan many:00' 'milli\u00fan other:00' 'milli\u00fan}",
                    "{one:000' 'milli\u00fan two:000' 'milli\u00fan few:000' 'milli\u00fan many:000' 'milli\u00fan other:000' 'milli\u00fan}",
                    "{one:0' 'bhilli\u00fan two:0' 'bhilli\u00fan few:0' 'bhilli\u00fan many:0' 'mbilli\u00fan other:0' 'billi\u00fan}",
                    "{one:00' 'billi\u00fan two:00' 'billi\u00fan few:00' 'billi\u00fan many:00' 'mbilli\u00fan other:00' 'billi\u00fan}",
                    "{one:000' 'billi\u00fan two:000' 'billi\u00fan few:000' 'billi\u00fan many:000' 'billi\u00fan other:000' 'billi\u00fan}",
                    "{one:0' 'trilli\u00fan two:0' 'thrilli\u00fan few:0' 'thrilli\u00fan many:0' 'dtrilli\u00fan other:0' 'trilli\u00fan}",
                    "{one:00' 'trilli\u00fan two:00' 'trilli\u00fan few:00' 'trilli\u00fan many:00' 'dtrilli\u00fan other:00' 'trilli\u00fan}",
                    "{one:000' 'trilli\u00fan two:000' 'trilli\u00fan few:000' 'trilli\u00fan many:000' 'trilli\u00fan other:000' 'trilli\u00fan}",
                }
            },
        };
        return data;
    }
}
