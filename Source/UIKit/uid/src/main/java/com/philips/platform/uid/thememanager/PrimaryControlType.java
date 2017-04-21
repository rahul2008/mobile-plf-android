/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.thememanager;

import android.content.res.Resources;

import com.philips.platform.uid.R;

public enum PrimaryControlType implements ThemeConfig {
    ALTERNATE_PRIMARY {
        @Override
        public void injectStyle(Resources.Theme theme) {
            theme.applyStyle(R.style.UIDPrimaryControl_AlternatePrimary, true);
        }
    },
    SECONDARY {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.UIDPrimaryControl_Secondary, true);
        }
    }, ACCENT {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.UIDPrimaryControl_Accent, true);
        }
    };

    abstract public void injectStyle(Resources.Theme theme);
}
