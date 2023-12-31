/*
 * Copyright (c) 2005, 2016, Oracle and/or its affiliates. All rights reserved.
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
package javax.imageio.plugins.tiff;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the tags found in a GeoTIFF IFD.  GeoTIFF is a
 * standard for annotating georeferenced or geocoded raster imagery.
 * This class does <i>not</i> handle the <i>GeoKey</i>s referenced
 * from a <i>GeoKeyDirectoryTag</i> as those are not TIFF tags per se.
 *
 * <p>The definitions of the data types referenced by the field
 * definitions may be found in the {@link TIFFTag TIFFTag} class.</p>
 *
 * @since 9
 */
public final class GeoTIFFTagSet extends TIFFTagSet {

    private static GeoTIFFTagSet theInstance = null;

    /**
     * A tag used to specify the size of raster pixel spacing in
     * model space units.
     */
    public static final int TAG_MODEL_PIXEL_SCALE = 33550;

    /**
     * A tag used to specify the transformation matrix between the raster
     * space and the model space.
     */
    public static final int TAG_MODEL_TRANSFORMATION = 34264;

    /** A tag used to store raster-to-model tiepoint pairs. */
    public static final int TAG_MODEL_TIE_POINT = 33922;

    /** A tag used to store the <i>GeoKey</i> directory. */
    public static final int TAG_GEO_KEY_DIRECTORY = 34735;

    /** A tag used to store all {@code double}-values <i>GeoKey</i>s. */
    public static final int TAG_GEO_DOUBLE_PARAMS = 34736;

    /** A tag used to store all ASCII-values <i>GeoKey</i>s. */
    public static final int TAG_GEO_ASCII_PARAMS = 34737;

    // GeoTIFF tags

    static class ModelPixelScale extends TIFFTag {
        public ModelPixelScale() {
            super("ModelPixelScaleTag",
                  TAG_MODEL_PIXEL_SCALE,
                  1 << TIFFTag.TIFF_DOUBLE);
        }
    }

    static class ModelTransformation extends TIFFTag {
        public ModelTransformation() {
            super("ModelTransformationTag",
                  TAG_MODEL_TRANSFORMATION,
                  1 << TIFFTag.TIFF_DOUBLE);
        }
    }

    static class ModelTiepoint extends TIFFTag {
        public ModelTiepoint() {
            super("ModelTiepointTag",
                  TAG_MODEL_TIE_POINT,
                  1 << TIFFTag.TIFF_DOUBLE);
        }
    }

    static class GeoKeyDirectory extends TIFFTag {
        public GeoKeyDirectory() {
            super("GeoKeyDirectoryTag",
                  TAG_GEO_KEY_DIRECTORY,
                  1 << TIFFTag.TIFF_SHORT);
        }
    }

    static class GeoDoubleParams extends TIFFTag {
        public GeoDoubleParams() {
            super("GeoDoubleParamsTag",
                  TAG_GEO_DOUBLE_PARAMS,
                  1 << TIFFTag.TIFF_DOUBLE);
        }
    }

    static class GeoAsciiParams extends TIFFTag {
        public GeoAsciiParams() {
            super("GeoAsciiParamsTag",
                  TAG_GEO_ASCII_PARAMS,
                  1 << TIFFTag.TIFF_ASCII);
        }
    }

    private static List<TIFFTag> tags;

    private static void initTags() {
        tags = new ArrayList<TIFFTag>(42);

        tags.add(new GeoTIFFTagSet.ModelPixelScale());
        tags.add(new GeoTIFFTagSet.ModelTransformation());
        tags.add(new GeoTIFFTagSet.ModelTiepoint());
        tags.add(new GeoTIFFTagSet.GeoKeyDirectory());
        tags.add(new GeoTIFFTagSet.GeoDoubleParams());
        tags.add(new GeoTIFFTagSet.GeoAsciiParams());
    }

    private GeoTIFFTagSet() {
        super(tags);
    }

    /**
     * Returns a shared instance of a {@code GeoTIFFTagSet}.
     *
     * @return a {@code GeoTIFFTagSet} instance.
     */
    public static synchronized GeoTIFFTagSet getInstance() {
        if (theInstance == null) {
            initTags();
            theInstance = new GeoTIFFTagSet();
            tags = null;
        }
        return theInstance;
    }
}
