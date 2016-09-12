/*
 * Copyright © 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.example.cdpp.bluelibexampleapp.about;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cdpp.bluelibexampleapp.BlueLibExampleApplication;
import com.example.cdpp.bluelibexampleapp.BuildConfig;
import com.example.cdpp.bluelibexampleapp.R;
import com.philips.pins.shinelib.SHNCentral;

public class AboutFragment extends DialogFragment {

    private SHNCentral mShnCentral;

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

        // Obtain reference to BlueLib instance
        mShnCentral = BlueLibExampleApplication.get().getShnCentral();

        // Set dialog title
        getDialog().setTitle(R.string.dialog_title_versions);

        // Display version string
        TextView versionNameTxt = (TextView) rootView.findViewById(R.id.versionNameTxt);
        versionNameTxt.setText(getVersionsString());

        return rootView;
    }

    private String getVersionsString() {
        final String blueLibVersion = mShnCentral.getVersion();

        return getString(R.string.bluelib_version) +
                blueLibVersion +
                "\n" +
                getString(R.string.app_version) +
                BuildConfig.VERSION_NAME;
    }
}
