package com.philips.cdp.uikit.hamburger;

import android.graphics.drawable.Drawable;

public class HamburgerItem {

    private String title;
    private Drawable icon;
    private int count;

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
}
