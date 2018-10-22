/*
 * Copyright (c) Koninklijke Philips N.V., 2018.
 * All rights reserved.
 */
package com.philips.platform.ews.util;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.philips.platform.ews.homewificonnection.SelectWiFiAdapter;

public class CustomWidgetBinder {

    @BindingAdapter("selectWiFiAdapter")
    public static void setSelectWiFiAdapter(@NonNull final RecyclerView recyclerView, @Nullable final SelectWiFiAdapter adapter) {
        if (adapter != null) {
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(adapter);
        }
    }
}