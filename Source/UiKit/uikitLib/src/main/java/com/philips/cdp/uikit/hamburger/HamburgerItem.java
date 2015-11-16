package com.philips.cdp.uikit.hamburger;

import android.graphics.drawable.Drawable;

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



    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    public boolean isLastChild() {
        return isLastChild;
    }

    public void setIsLastChild(boolean isLastChild) {
        this.isLastChild = isLastChild;
    }
}
