/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.csw.description;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.csw.CswBaseFragment;
import com.philips.platform.csw.R;

public class DescriptionView extends CswBaseFragment implements DescriptionInterface {

    public static final String HELP = "help";

    @Override
    public int getTitleResourceId() {
        return R.string.csw_consent_help_label;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.csw_description_view, container, false);

        int helpText = getArguments().getInt(HELP, R.string.mya_default_help_text);
        TextView description = view.findViewById(R.id.description);
        description.setText(helpText);

        return view;
    }

    public static void show(FragmentManager fragmentManager, int helpText, int containerViewId) {
        Bundle args = new Bundle();
        args.putInt(HELP, helpText);

        DescriptionView fragment = new DescriptionView();
        fragment.setArguments(args);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, DescriptionView.class.getName());
        fragmentTransaction.addToBackStack(DescriptionView.class.getName());
        fragmentTransaction.commitAllowingStateLoss();
    }
}
