/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappdemo.screens.aboutscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.uappdemo.screens.base.UappBaseActivity;
import com.philips.platform.uappdemo.screens.base.UAppBaseFragment;
import com.philips.platform.uappdemolibrary.BuildConfig;
import com.philips.platform.uappdemolibrary.R;

/**
 * About screen to display content and version number
 * This class is for sutomising the about screen present from UiKit Lib
 * Added custom titles
 * Background color
 * Latest version
 */

public class AboutScreenFragmentU extends UAppBaseFragment
{
    public static final String TAG =AboutScreenFragmentU.class.getSimpleName();

    @Override
    public void onResume() {
        super.onResume();
        ((UappBaseActivity)getActivity()).updateActionBarIcon(false);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.about_screen_title);
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.uikit_about_screen, container, false);
        TextView  version =(TextView)view.findViewById(R.id.about_version);
        version.setText(getResources().getString(R.string.about_screen_app_version) + BuildConfig.VERSION_NAME);
        TextView  content =(TextView)view.findViewById(R.id.about_content);
        content.setText(R.string.about_screen_description);
        return view;

    }
}
