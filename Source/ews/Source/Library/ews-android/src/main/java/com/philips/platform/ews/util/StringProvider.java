package com.philips.platform.ews.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

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

    @NonNull
    public String getString(@StringRes int id, @StringRes int param, @StringRes int param2) {
        return context.getString(id, context.getString(param), context.getString(param2));
    }

    @NonNull
    public Drawable getImageResource(@DrawableRes int id) {
        return ContextCompat.getDrawable(context, id);
    }

    public String getString(@StringRes int id, String param) {
        return context.getString(id, param);
    }

    public String getString(@StringRes int id){
        return context.getString(id);
    }
}