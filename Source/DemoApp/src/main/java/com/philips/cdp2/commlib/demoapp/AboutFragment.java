/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.demoapp;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import static com.philips.cdp2.commlib.demouapp.BuildConfig.LIBRARY_VERSION;
import static com.philips.cdp2.commlib.demouapp.BuildConfig.VERSION_NAME;

public class AboutFragment extends DialogFragment {

    public AboutFragment() {
    }

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AboutDialog);

        // Set title
        getDialog().setTitle(R.string.menu_action_versions);

        // Display version string
        TextView versionNameTxt = (TextView) rootView.findViewById(R.id.versionNameTxt);
        versionNameTxt.setText(getVersionsString());

        return rootView;
    }

    private String getVersionsString() {
        return String.format(Locale.US, getString(R.string.about_versions_string), VERSION_NAME, LIBRARY_VERSION);
    }
}
