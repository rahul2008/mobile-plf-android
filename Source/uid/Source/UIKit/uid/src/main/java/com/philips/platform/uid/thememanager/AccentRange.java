/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.thememanager;

import android.content.Context;
import android.content.res.Resources;
import com.philips.platform.uid.R;

/**
 * This is the theme configuration which indicate which accent flavour your application should support
 */
public enum AccentRange implements ThemeConfig {
    GROUP_BLUE {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.AccentGroupBlue, true);
        }
    },
    BLUE {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.AccentBlue, true);
        }
    },
    AQUA {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.AccentAqua, true);
        }
    },
    GREEN {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.AccentGreen, true);
        }
    },
    ORANGE {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.AccentOrange, true);
        }
    },
    PINK {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.AccentPink, true);
        }
    },
    PURPLE {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.AccentPurple, true);
        }
    },
    GRAY {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.AccentGray, true);
        }
    };

    public abstract void injectStyle(Resources.Theme theme);

    void injectAllAccentAttributes(Context context, Resources.Theme theme) {
        theme.applyStyle(ThemeUtils.getAccentResourceID(context), true);
    }
}