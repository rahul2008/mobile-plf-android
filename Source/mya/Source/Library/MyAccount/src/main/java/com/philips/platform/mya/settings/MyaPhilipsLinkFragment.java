/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.philips.platform.mya.R;
import com.philips.platform.mya.base.MyaBaseFragment;

public class MyaPhilipsLinkFragment extends MyaBaseFragment {

    private WebView mywebview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.mya_philips_link_fragment, container, false);
        mywebview = view.findViewById(R.id.webView1);
        return view;
    }

    @Override
    public int getActionbarTitleResId() {
        return R.string.MYA_My_account;
    }

    @Override
    public String getActionbarTitle(Context context) {
        return context.getString(R.string.MYA_My_account);
    }

    @Override
    public boolean getBackButtonState() {
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mywebview.loadUrl("file:///android_asset/myresource.html");
    }
}
