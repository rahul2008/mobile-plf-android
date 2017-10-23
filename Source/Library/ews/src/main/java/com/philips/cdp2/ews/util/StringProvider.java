package com.philips.cdp2.ews.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public class StringProvider {

    @NonNull private final Context context;

    public StringProvider(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    public String getString(@StringRes int id, @StringRes int param) {
        return context.getString(id, context.getString(param));
    }

    @NonNull
    public String getString(@StringRes int id, @StringRes int param, String param2) {
        return context.getString(id, context.getString(param), param2);
    }
}