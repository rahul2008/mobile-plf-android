/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.thememanager;

import android.content.res.Resources;
import android.support.annotation.StyleRes;

import com.philips.platform.uit.R;

/**
 * This class is used to set the theme type of application
 */
public enum ContentColor {

    ULTRA_LIGHT {
        @Override
        public void injectTonalRange(Resources.Theme theme) {
            injectTonalRange(theme, R.style.UltraLight);
        }
    },
    VERY_LIGHT {
        @Override
        public void injectTonalRange(Resources.Theme theme) {
            injectTonalRange(theme, R.style.VeryLight);
        }
    },
    LIGHT {
        @Override
        public void injectTonalRange(Resources.Theme theme) {
            injectTonalRange(theme, R.style.Light);
        }
    },
    BRIGHT {
        @Override
        public void injectTonalRange(Resources.Theme theme) {
            injectTonalRange(theme, R.style.Bright);
        }
    },
    VERY_DARK {
        @Override
        public void injectTonalRange(Resources.Theme theme) {
            injectTonalRange(theme, R.style.VeryDark);
        }
    };

    void injectTonalRange(Resources.Theme theme, @StyleRes int style) {
        theme.applyStyle(R.style.Base, true);
        theme.applyStyle(style, true);
    }

    public abstract void injectTonalRange(Resources.Theme theme);
}
