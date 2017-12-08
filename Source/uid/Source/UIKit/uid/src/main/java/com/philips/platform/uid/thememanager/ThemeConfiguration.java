/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.thememanager;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThemeConfiguration {
    private List<ThemeConfig> configurations;
    private Context context;

    public ThemeConfiguration(@NonNull final Context context, @NonNull final ThemeConfig... themeConfigs) {
        configurations = new ArrayList<>();
        this.context = context;
        configurations.addAll(Arrays.asList(themeConfigs));
    }

    /**
     * Add configs to the list if missed in constructor
     *
     * @param config ThemeConfig to be applied to the list
     */
    public void add(ThemeConfig config) {
        configurations.add(config);
    }

    /**
     * Returns the context set
     *
     * @return context
     */
    public Context getContext() {
        return context;
    }

    /**
     * Returns the list of configuration set through constructor or using add API
     * @return ThemeConfig List
     */
    public List<ThemeConfig> getConfigurations() {
        return configurations;
    }
}