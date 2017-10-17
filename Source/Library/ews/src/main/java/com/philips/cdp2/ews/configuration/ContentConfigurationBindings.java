/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.configuration;

import android.databinding.BindingAdapter;
import android.widget.TextView;

public class ContentConfigurationBindings {

    @BindingAdapter({"stringFormat", "content"})
    public static void setFormattedText(TextView textView, String format, int argId){
        if(argId == 0) return;
        textView.setText(String.format(format, textView.getContext().getString(argId)));
    }
}
