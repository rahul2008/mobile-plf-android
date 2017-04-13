/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.thememanager;

import android.content.res.Resources;

import com.philips.platform.uid.R;

/**
 * This is the configuration which indicates which color you app is running into this will be used as base to use coloring
 * <br>
 * This will initialized the color levels used in the controls
 * </br>
 * e.g if you use color range as GroupBlue then
 * <br>
 * color level 5(uidColorLevel5) will be uid_group_blue_color_level_5
 * <br>
 * Mapping is done as as below
 * <item name="uidColorLevel5">@color/uid_group_blue_level_5</item>
 * </br>
 */
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
