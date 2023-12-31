/*
 * Copyright (c) 1996, 2022, Oracle and/or its affiliates. All rights reserved.
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
package java.awt;

/**
 * Defines an interface for classes that know how to layout {@code Container}s
 * based on a layout constraints object.
 *
 * This interface extends the {@code LayoutManager} interface to deal with layouts
 * explicitly in terms of constraint objects that specify how and where
 * components should be added to the layout.
 * <p>
 * This minimal extension to {@code LayoutManager} is intended for tool
 * providers who wish to create constraint-based layouts.
 * It does not yet provide full, general support for custom
 * constraint-based layout managers.
 *
 * @see LayoutManager
 * @see Container
 *
 * @author      Jonni Kanerva
 */
public interface LayoutManager2 extends LayoutManager {

    /**
     * Adds the specified component to the layout, using the specified
     * constraint object.
     * @param comp the component to be added
     * @param constraints  where/how the component is added to the layout.
     */
    void addLayoutComponent(Component comp, Object constraints);

    /**
     * Calculates the maximum size dimensions for the specified container,
     * given the components it contains.
     *
     * @see java.awt.Component#getMaximumSize
     * @see LayoutManager
     * @param  target the target container
     * @return the maximum size of the container
     */
    public Dimension maximumLayoutSize(Container target);

    /**
     * Returns the alignment along the x axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param  target the target container
     * @return the x-axis alignment preference
     */
    public float getLayoutAlignmentX(Container target);

    /**
     * Returns the alignment along the y axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param  target the target container
     * @return the y-axis alignment preference
     */
    public float getLayoutAlignmentY(Container target);

    /**
     * Invalidates the layout, indicating that if the layout manager
     * has cached information it should be discarded.
     * @param  target the target container
     */
    public void invalidateLayout(Container target);

}
