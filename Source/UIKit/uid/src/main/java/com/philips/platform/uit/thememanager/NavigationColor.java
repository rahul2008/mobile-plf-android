package com.philips.platform.uit.thememanager;

import android.content.res.Resources;

import com.philips.platform.uit.R;

public enum NavigationColor {
    ULTRA_LIGHT {
        @Override
        public void injectNavigationColor(final Resources.Theme theme) {
            injectColor(theme, R.style.UIDNavigationbarUltraLight);
        }
    },
    VERY_LIGHT {
        @Override
        public void injectNavigationColor(final Resources.Theme theme) {
            injectColor(theme, R.style.UIDNavigationbarVeryLight);
        }
    },
    LIGHT {
        @Override
        public void injectNavigationColor(final Resources.Theme theme) {
            injectColor(theme, R.style.UIDNavigationbarLight);
        }
    },
    BRIGHT {
        @Override
        public void injectNavigationColor(final Resources.Theme theme) {
            injectColor(theme, R.style.UIDNavigationbarBright);
        }
    },
    VERY_DARK {
        @Override
        public void injectNavigationColor(final Resources.Theme theme) {
            injectColor(theme, R.style.UIDNavigationbarVeryDark);
        }
    };

    private static void injectColor(final Resources.Theme theme, final int style) {
        theme.applyStyle(style, true);
    }

    public abstract void injectNavigationColor(Resources.Theme theme);
}
