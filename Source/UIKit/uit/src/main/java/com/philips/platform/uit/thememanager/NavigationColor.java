package com.philips.platform.uit.thememanager;

import android.content.res.Resources;

import com.philips.platform.uit.R;

public enum NavigationColor {
    ULTRA_LIGHT {
        @Override
        public void injectNavigationColor(final Resources.Theme theme) {
            injectColor(theme, R.style.UITNavigationbarUltraLight);
        }
    },
    VERY_LIGHT {
        @Override
        public void injectNavigationColor(final Resources.Theme theme) {
            injectColor(theme, R.style.UITNavigationbarVeryLight);
        }
    },
    LIGHT {
        @Override
        public void injectNavigationColor(final Resources.Theme theme) {
            injectColor(theme, R.style.UITNavigationbarLight);
        }
    },
    BRIGHT {
        @Override
        public void injectNavigationColor(final Resources.Theme theme) {
            injectColor(theme, R.style.UITNavigationbarBright);
        }
    },
    VERY_DARK {
        @Override
        public void injectNavigationColor(final Resources.Theme theme) {
            injectColor(theme, R.style.UITNavigationbarVeryDark);
        }
    };

    private static void injectColor(final Resources.Theme theme, final int style) {
        theme.applyStyle(style, true);
    }

    public abstract void injectNavigationColor(Resources.Theme theme);
}
