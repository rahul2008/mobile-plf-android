/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.ews.configuration;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentConfigurationBindings {

    @BindingAdapter({"stringFormat", "content"})
    public static void setFormattedText(@NonNull TextView textView, String format, int argId){
        if(argId == 0) {
            return;
        }
        textView.setText(String.format(format, textView.getContext().getString(argId)));
    }

    @BindingAdapter({"stringFormat", "content"})
    public static void setStringFormattedText(@NonNull TextView textView, String format, String argId){
        if(TextUtils.isEmpty(argId)) {
            return;
        }
        textView.setText(String.format(format, argId));
    }

    @BindingAdapter({"imageResource"})
    public static void setEWSImageResource(@NonNull ImageView imageView, int argId) {
        if (argId == 0) {
            return;
        }
        imageView.setImageResource(argId);
    }

    @BindingAdapter({"stringFormat", "arg1", "arg2"})
    public static void setFormattedText(@NonNull TextView textView, String format, int arg1, int arg2) {
        if (arg1 == 0 || arg2 == 0) {
            return;
        }
        textView.setText(String.format(format, textView.getContext().getString(arg1), textView.getContext().getString(arg2)));
    }

    @BindingAdapter({"stringFormat", "arg1", "arg2"})
    public static void setStringFormattedText(@NonNull TextView textView, String format, int arg1, String arg2) {
        if (arg1 == 0 || TextUtils.isEmpty(arg2)) {
            return;
        }
        textView.setText(String.format(format, textView.getContext().getString(arg1), arg2));
    }
}
