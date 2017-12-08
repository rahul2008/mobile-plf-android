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

        @Override
        public String getThemeName() {
            return "GroupBlue";
        }
    },
    BLUE {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Theme_DLS_Blue, true);
        }

        @Override
        public String getThemeName() {
            return "Blue";
        }
    },
    AQUA {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Theme_DLS_Aqua, true);
        }

        @Override
        public String getThemeName() {
            return "Aqua";
        }
    },
    GREEN {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Theme_DLS_Green, true);
        }

        @Override
        public String getThemeName() {
            return "Green";
        }
    },
    ORANGE {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Theme_DLS_Orange, true);
        }

        @Override
        public String getThemeName() {
            return "Orange";
        }
    },
    PINK {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Theme_DLS_Pink, true);
        }

        @Override
        public String getThemeName() {
            return "Pink";
        }
    },
    PURPLE {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Theme_DLS_Purple, true);
        }

        @Override
        public String getThemeName() {
            return "Purple";
        }
    },
    GRAY {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.Theme_DLS_Gray, true);
        }

        @Override
        public String getThemeName() {
            return "Gray";
        }
    };

    @Override
    abstract public void injectStyle(final Resources.Theme theme);

    abstract public String getThemeName();
}
