/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.thememanager;

import android.content.res.Resources;

import com.philips.platform.uit.R;

/**
 * This class is used to set the theme type of application
 */
public enum TonalRange {

    ULTRA_LIGHT{
        @Override
        public void injectTonalRange(Resources.Theme theme) {
            theme.applyStyle(R.style.UltraLight, true);
        }
    },
    VERY_LIGHT {
        @Override
        public void injectTonalRange(Resources.Theme theme) {
            theme.applyStyle(R.style.VeryLight, true);
        }
    },
    LIGHT{
        @Override
        public void injectTonalRange(Resources.Theme theme) {
            theme.applyStyle(R.style.Light, true);
        }
    },
    BRIGHT{
        @Override
        public void injectTonalRange(Resources.Theme theme) {
            theme.applyStyle(R.style.Bright, true);
        }
    },
    VERYDARK {
        @Override
        public void injectTonalRange(Resources.Theme theme) {
            theme.applyStyle(R.style.VeryDark, true);
        }
    };

    public abstract void injectTonalRange(Resources.Theme theme);
}
