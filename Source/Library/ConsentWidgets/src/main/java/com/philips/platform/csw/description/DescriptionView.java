/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.description;

import com.philips.platform.csw.CswBaseFragment;
import com.philips.platform.mya.consentwidgets.R;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DescriptionView extends CswBaseFragment implements
        DescriptionInterface {

    private static final String TAG = "DescriptionView";
    public static final String HELP = "help";

    @Override
    protected void setViewParams(Configuration config, int width) {
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.csw_privacy_settings;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.csw_description_view, container, false);
        ((TextView) view.findViewById(R.id.description)).setText(getArguments().getString(HELP, "please specify helptext in ConsentDefinition"));
        handleOrientation(view);
        return view;
    }

    public static void show(FragmentManager fragmentManager, String helpText) {
        Bundle args = new Bundle();
        args.putString(HELP, helpText);

        DescriptionView fragment = new DescriptionView();
        fragment.setArguments(args);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.csw_frame_layout_view_container, fragment, TAG);
        fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
