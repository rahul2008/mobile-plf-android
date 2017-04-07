/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.thememanager;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ThemeConfiguration {
    private List<ThemeConfig> configurations;
    private Context context;

    public ThemeConfiguration(@NonNull final Context context, @NonNull final ThemeConfig... themeConfigs) {
        configurations = new ArrayList<>();
        this.context = context;
        for (ThemeConfig themeConfig : themeConfigs) {
            configurations.add(themeConfig);
        }
    }

    public void add(ThemeConfig config) {
        configurations.add(config);
    }

    public Context getContext() {
        return context;
    }

    public List<ThemeConfig> getConfigurations() {
        return configurations;
    }
}