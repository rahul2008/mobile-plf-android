/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.thememanager;

import android.content.res.Resources;

import com.philips.platform.uit.R;

public enum PrimaryControlType {

    ALTERNATE_PRIMARY {
        @Override
        void injectPrimaryControlColors(Resources.Theme theme) {
            theme.applyStyle(R.style.AlternatePrimary, true);
        }
    };

    abstract void injectPrimaryControlColors(Resources.Theme theme);
}
