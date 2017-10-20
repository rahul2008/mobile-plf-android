package com.philips.cdp2.ews.util;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public class StringProvider {

    @NonNull private final Resources resources;

    public StringProvider(@NonNull Resources resources) {
        this.resources = resources;
    }

    @NonNull
    public String getString(@StringRes int id, @StringRes int param) {
        return resources.getString(id, resources.getString(param));
    }

}
