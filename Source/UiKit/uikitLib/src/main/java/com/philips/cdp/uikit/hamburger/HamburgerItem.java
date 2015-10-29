package com.philips.cdp.uikit.hamburger;

public class HamburgerItem {

    private String title;
    private int icon;
    private String count = "0";

    public HamburgerItem() {
    }

    public HamburgerItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public HamburgerItem(String title, int icon, String count) {
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

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getCount() {
        return this.count;
    }

    public void setCount(String count) {
        this.count = count;
    }

}
