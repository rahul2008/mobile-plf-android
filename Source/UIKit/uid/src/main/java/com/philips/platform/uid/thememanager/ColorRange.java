/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.thememanager;

import android.content.res.Resources;

import com.philips.platform.uid.R;

public enum ColorRange implements ThemeConfig {
    GROUP_BLUE {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.GroupBlue, true);
        }
    },
    BLUE {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Blue, true);
        }
    },
    AQUA {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Aqua, true);
        }
    },
    GREEN {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Green, true);
        }
    },
    ORANGE {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Orange, true);
        }
    },
    PINK {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Pink, true);
        }
    },
    PURPLE {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Purple, true);
        }
    },
    GRAY {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Gray, true);
        }
    };

    @Override
    abstract public void injectStyle(final Resources.Theme theme);
}
