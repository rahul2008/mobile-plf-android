/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.uikit.hamburger;

import android.graphics.drawable.Drawable;

/**
 * Modal class of Hamburger Menu
 */
public class HamburgerItem {

    private String title;
    private Drawable icon;
    private int count;
    private boolean isParent;
    private boolean isLastChild;

    public HamburgerItem() {
    }

    public HamburgerItem(String title, Drawable icon) {
        this.title = title;
        this.icon = icon;
    }

    public HamburgerItem(String title, Drawable icon, int count) {
        this.title = title;
        this.icon = icon;
        this.count = count;
    }

    public HamburgerItem(String title, Drawable icon, int count, boolean isParent) {
        this.title = title;
        this.icon = icon;
        this.count = count;
        this.isParent = isParent;
    }

    public HamburgerItem(String title, Drawable icon, int count, boolean isParent, boolean isLastChild) {
        this.title = title;
        this.icon = icon;
        this.count = count;
        this.isParent = isParent;
        this.isLastChild = isLastChild;
    }


    /**
     * @return Returns Title as type String
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * API to set Title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return Returns Icon as type drawable
     */
    public Drawable getIcon() {
        return this.icon;
    }

    /**
     * Set icon
     * @param icon
     */
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    /**
     *
     * @return Returns count as type integer
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Set count to be displayed
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     *
     * @return Returns true if row is parent
     */
    public boolean isParent() {
        return isParent;
    }

    /**
     *
     * @param isParent API to set row as parent
     */
    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    /**
     *
     * @return Returns true if it is last row
     */
    public boolean isLastChild() {
        return isLastChild;
    }

    /**
     * Set as true if last row
     * @param isLastChild
     */
    public void setIsLastChild(boolean isLastChild) {
        this.isLastChild = isLastChild;
    }
}
