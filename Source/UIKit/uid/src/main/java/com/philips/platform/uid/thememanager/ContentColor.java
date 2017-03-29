/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.thememanager;

import android.content.res.Resources;
import android.support.annotation.StyleRes;

import com.philips.platform.uid.R;

/**
 * This class is used to set the theme type of application
 */
public enum ContentColor {

    ULTRA_LIGHT {
        @Override
        public void injectTonalRange(Resources.Theme theme) {
            injectTonalRange(theme, R.style.Base_UltraLight);
        }
    },
    VERY_LIGHT {
        @Override
        public void injectTonalRange(Resources.Theme theme) {
            injectTonalRange(theme, R.style.Base_VeryLight);
        }
    },
    LIGHT {
        @Override
        public void injectTonalRange(Resources.Theme theme) {
            injectTonalRange(theme, R.style.Base_Light);
        }
    },
    BRIGHT {
        @Override
        public void injectTonalRange(Resources.Theme theme) {
            injectTonalRange(theme, R.style.Base_Bright);
        }
    },
    VERY_DARK {
        @Override
        public void injectTonalRange(Resources.Theme theme) {
            injectTonalRange(theme, R.style.Base_VeryDark);
        }
    };

    void injectTonalRange(Resources.Theme theme, @StyleRes int style) {
        theme.applyStyle(style, true);
    }

    public abstract void injectTonalRange(Resources.Theme theme);
}
