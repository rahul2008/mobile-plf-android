package com.philips.platform.mya;


import androidx.fragment.app.Fragment;

import java.io.Serializable;

/**
 * Class used to add the proposition specific configurable tab in my account
 * @since 2018.1.0
 */
public class MyaTabConfig implements Serializable {
    private static final long serialVersionUID = 2886782333622236377L;
    private String tabName;
    private transient Fragment fragment;

    /**
     * Constructor for MyaTabConfig
     * @param tabName name of the tab
     * @param fragment the fragment of the tab
     */
    public MyaTabConfig(String tabName, Fragment fragment) {
        this.tabName = tabName;
        this.fragment = fragment;
    }

    /**
     * Get the name of the tab
     * @since 2018.1.0
     * @return returns the Tab name
     */
    public String getTabName() {
        return tabName;
    }

    /**
     * Set the tab name
     * @since 2018.1.0
     * @param tabName the name of the tab
     */
    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    /**
     * Get the configurable fragment
     * @since 2018.1.0
     * @return return the configurable tab fragment
     */
    public Fragment getFragment() {
        return fragment;
    }

    /**
     * Set the fragment
     * @since 2018.1.0
     * @param fragment the configurable tab fragment
     */
    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
