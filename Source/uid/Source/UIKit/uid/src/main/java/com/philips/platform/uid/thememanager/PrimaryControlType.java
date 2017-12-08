/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.thememanager;

import android.content.res.Resources;
import com.philips.platform.uid.R;

public enum PrimaryControlType implements ThemeConfig {
    SECONDARY {
        @Override
        public void injectStyle(final Resources.Theme theme) {
            theme.applyStyle(R.style.UIDPrimaryControl_Secondary, true);
        }
    };
    abstract public void injectStyle(Resources.Theme theme);
}
