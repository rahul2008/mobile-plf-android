/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.aboutscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkBaseFragment;

/**
 * About screen to display content and version number
 * This class is for sutomising the about screen present from UiKit Lib
 * Added custom titles
 * Background color
 * Latest version
 */

public class AboutScreenFragment extends AppFrameworkBaseFragment
{
    public static final String TAG =AboutScreenFragment.class.getSimpleName();

    @Override
    public void onResume() {
        super.onResume();
        ((AppFrameworkBaseActivity)getActivity()).updateActionBarIcon(false);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.about_screen_title);
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.uikit_about_screen, container, false);
        TextView  version =(TextView)view.findViewById(R.id.about_version);
        version.setText(getResources().getString(R.string.about_screen_app_version) +BuildConfig.VERSION_NAME);
        TextView  content =(TextView)view.findViewById(R.id.about_content);
        content.setText(R.string.about_screen_description);
        return view;

    }
}
