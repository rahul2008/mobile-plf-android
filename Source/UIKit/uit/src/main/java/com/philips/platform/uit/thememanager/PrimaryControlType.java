/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.thememanager;

import android.content.res.Resources;

import com.philips.platform.uit.R;

public enum PrimaryControlType {
    PRIMARY {
        @Override
        void injectPrimaryControlColors(Resources.Theme theme) {
            theme.applyStyle(R.style.UITPrimaryControl, true);
        }
    },
    ALTERNATE_PRIMARY {
        @Override
        void injectPrimaryControlColors(Resources.Theme theme) {
            theme.applyStyle(R.style.UITPrimaryControl_AlternatePrimary, true);
        }
    };

    abstract void injectPrimaryControlColors(Resources.Theme theme);
}
