/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.support.annotation.NonNull;

public class UIDContextWrapper extends ContextWrapper {
    Resources.Theme newTheme;

    public UIDContextWrapper(@NonNull Context baseContext, @NonNull Resources.Theme newTheme) {
        super(baseContext);
        this.newTheme = newTheme;
    }

    @Override
    public Resources.Theme getTheme() {
        return newTheme;
    }

    public static Context getThemedContext(@NonNull Context baseContext, @NonNull Resources.Theme newTheme) {
        return new UIDContextWrapper(baseContext, newTheme);
    }
}
