package com.example.cdpp.bluelibreferenceapp.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cdpp.bluelibreferenceapp.BuildConfig;
import com.example.cdpp.bluelibreferenceapp.R;
import com.example.cdpp.bluelibreferenceapp.ReferenceApplication;
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
        mShnCentral = ReferenceApplication.get().getShnCentral();

        // Set dialog title
        getDialog().setTitle(R.string.dialog_title_versions);

        // Display version string
        TextView versionNameTxt = (TextView) rootView.findViewById(R.id.versionNameTxt);
        versionNameTxt.setText(getVersionsString());

        return rootView;
    }

    private final String getVersionsString() {
        final String blueLibVersion = mShnCentral.getVersion();

        return new StringBuilder(getString(R.string.bluelib_version))
                .append(blueLibVersion)
                .append("\n")
                .append(getString(R.string.app_version))
                .append(BuildConfig.VERSION_NAME)
                .toString();
    }
}
