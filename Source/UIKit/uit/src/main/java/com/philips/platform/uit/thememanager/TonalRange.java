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
            theme.applyStyle(R.style.BaseMapping_UltraLight, true);
        }
    },
    VERY_LIGHT {
        @Override
        public void injectTonalRange(Resources.Theme theme) {
//            theme.injectColorRange(R.style.VeryLightBright, true);
        }
    },
    LIGHT{
        @Override
        public void injectTonalRange(Resources.Theme theme) {
//            theme.injectColorRange(R.style.VeryLightDark, true);
        }
    },
    BRIGHT{
        @Override
        public void injectTonalRange(Resources.Theme theme) {
//            theme.injectColorRange(R.style.VeryLightDark, true);
        }
    },
    DARK {
        @Override
        public void injectTonalRange(Resources.Theme theme) {
//            theme.injectColorRange(R.style.Bright, true);
        }
    },
    VERYDARK {
        @Override
        public void injectTonalRange(Resources.Theme theme) {
//            theme.injectColorRange(R.style.VeryDark, true);
        }
    };

    public abstract void injectTonalRange(Resources.Theme theme);
}
