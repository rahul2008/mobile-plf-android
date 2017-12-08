/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.thememanager;

import android.content.Context;
import android.content.res.Resources;
import com.philips.platform.uid.R;

public enum NavigationColor implements ThemeConfig{
    ULTRA_LIGHT {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            injectNavigationColor(theme, R.style.UIDNavigationbarUltraLight);
            NavigationColor.injectNavigationColor(theme, R.style.UIDStatusBarLight);
        }
    },
    VERY_LIGHT {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            injectNavigationColor(theme, R.style.UIDNavigationbarVeryLight);
            NavigationColor.injectNavigationColor(theme, R.style.UIDStatusBarLight);
        }
    },/*
    LIGHT {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            injectNavigationColor(theme, R.style.UIDNavigationbarLight);
        }
    },*/
    BRIGHT {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            injectNavigationColor(theme, R.style.UIDNavigationbarBright);
            NavigationColor.injectNavigationColor(theme, R.style.UIDStatusBarDark);
        }
    },
    VERY_DARK {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            injectNavigationColor(theme, R.style.UIDNavigationbarVeryDark);
            NavigationColor.injectNavigationColor(theme, R.style.UIDStatusBarDark);
        }
    };

    private static void injectNavigationColor(final Resources.Theme theme, final int style) {
        theme.applyStyle(style, true);
    }

    public void injectNavigationTopColors(Context context, Resources.Theme theme) {
        theme.applyStyle(ThemeUtils.getNavigationTopResourceID(context), true);
    }

    public abstract void injectStyle(Resources.Theme theme);

}
