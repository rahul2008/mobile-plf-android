package com.philips.platform.mya;


import android.support.v4.app.Fragment;

import java.io.Serializable;

public class MyaTab implements Serializable {
    private static final long serialVersionUID = 2886782333622236377L;
    private String tabName;
    private transient Fragment fragment;

    public MyaTab(String tabName, Fragment fragment) {
        this.tabName = tabName;
        this.fragment = fragment;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
