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
            theme.applyStyle(R.style.Theme_DLS_GroupBlue, true);
        }
    },
    BLUE {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Theme_DLS_Blue, true);
        }
    },
    AQUA {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Theme_DLS_Aqua, true);
        }
    },
    GREEN {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Theme_DLS_Green, true);
        }
    },
    ORANGE {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Theme_DLS_Orange, true);
        }
    },
    PINK {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Theme_DLS_Pink, true);
        }
    },
    PURPLE {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Theme_DLS_Purple, true);
        }
    },
    GRAY {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Theme_DLS_Gray, true);
        }
    };

    @Override
    abstract public void injectStyle(final Resources.Theme theme);
}
