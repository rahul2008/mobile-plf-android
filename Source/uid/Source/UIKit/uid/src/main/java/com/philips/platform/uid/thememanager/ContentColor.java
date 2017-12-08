/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.thememanager;

import android.content.res.Resources;
import com.philips.platform.uid.R;

/**
 * This class will initialize the theme with some specific configuration as per specified tonal range
 */
public enum ContentColor implements ThemeConfig {

    ULTRA_LIGHT {
        @Override
        public void injectStyle(Resources.Theme theme) {
            theme.applyStyle(R.style.Base_UltraLight, true);
        }

        @Override
        public String getThemeName() {
            return "UltraLight";
        }
    },
    VERY_LIGHT {
        @Override
        public void injectStyle(Resources.Theme theme) {
            theme.applyStyle(R.style.Base_VeryLight, true);
        }

        @Override
        public String getThemeName() {
            return "VeryLight";
        }
    },
    /*LIGHT {
        @Override
        public void injectStyle(Resources.Theme theme) {
            theme.applyStyle(R.style.Base_Light, true);
        }
    },*/
    BRIGHT {
        @Override
        public void injectStyle(Resources.Theme theme) {
            theme.applyStyle(R.style.Base_Bright, true);
        }

        @Override
        public String getThemeName() {
            return "Bright";
        }
    },
    VERY_DARK {
        @Override
        public void injectStyle(Resources.Theme theme) {
            theme.applyStyle(R.style.Base_VeryDark, true);
        }

        @Override
        public String getThemeName() {
            return "VeryDark";
        }
    };

    public abstract void injectStyle(Resources.Theme theme);

    abstract public String getThemeName();
}
