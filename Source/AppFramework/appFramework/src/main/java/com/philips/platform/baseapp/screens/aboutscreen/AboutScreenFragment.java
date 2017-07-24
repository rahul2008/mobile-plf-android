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
import android.widget.Toast;

import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.NoConditionFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseFragment;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

/**
 * About screen to display content and version number
 * This class is for sutomising the about screen present from UiKit Lib
 * Added custom titles
 * Background color
 * Latest version
 */

public class AboutScreenFragment extends AbstractAppFrameworkBaseFragment
{
    public static final String TAG =AboutScreenFragment.class.getSimpleName();
    private static final String TERMS_CONSITIONS_CLICK = "TermsAndConditions";


    @Override
    public void onResume() {
        RALog.d(TAG, " onResume");
        super.onResume();
        updateActionBar();
    }

    protected void updateActionBar() {
        ((AbstractAppFrameworkBaseActivity)getActivity()).updateActionBarIcon(false);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.RA_AboutScreen_Title);
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.about_screen_fragment, container, false);
        TextView  version =(TextView)view.findViewById(R.id.about_version);
        version.setText(getResources().getString(R.string.RA_About_App_Version) +BuildConfig.VERSION_NAME);
        TextView  content =(TextView)view.findViewById(R.id.about_content);
        content.setText(R.string.RA_About_Description);
        TextView termaAndConditionsTextView=(TextView)view.findViewById(R.id.about_terms);
        termaAndConditionsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseFlowManager targetFlowManager = getApplicationContext().getTargetFlowManager();
                BaseState baseState = null;
                try {
                    baseState = targetFlowManager.getNextState(targetFlowManager.getCurrentState(), TERMS_CONSITIONS_CLICK);
                } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                        e) {
                    RALog.d(TAG, e.getMessage());
                    Toast.makeText(getApplicationContext(), getActivity().getString(R.string.RA_something_wrong), Toast.LENGTH_SHORT).show();
                }
                if (null != baseState) {
                    baseState.navigate(new FragmentLauncher(getActivity(), R.id.frame_container, (ActionBarListener) getActivity()));
                }
//                WebViewActivity.show(getActivity(),R.string.global_terms_link,R.string.terms_and_conditions_url_about);
            }
        });
        startAppTagging(TAG);
        return view;

    }
    private AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) getActivity().getApplication();
    }
}
