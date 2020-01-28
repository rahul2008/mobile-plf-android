/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.aboutscreen;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import android.widget.Toast;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.NoConditionFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.webview.WebViewStateData;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;


/**
 * Created by philips on 25/07/17.
 */

public class AboutScreenPresenter implements AboutScreenContract.Action {

    private static final String TAG = AboutScreenPresenter.class.getSimpleName();

    private Context context;

    private FragmentActivity activity;

    private AboutScreenContract.View view;

    private static final String TERMS_CONDITIONS_CLICK = "TermsAndConditions";


    public AboutScreenPresenter(Context context, AboutScreenContract.View view) {
        this.context = context;
        activity = (FragmentActivity) context;
        this.view = view;
    }

    @Override
    public void loadTermsAndPrivacy(String serviceId, String title) {
        BaseFlowManager targetFlowManager = getTargetFlowManager();
        BaseState baseState = null;
        try {
            baseState = targetFlowManager.getNextState(targetFlowManager.getCurrentState(), TERMS_CONDITIONS_CLICK);
        } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                e) {
            RALog.d(TAG, e.getMessage());
            Toast.makeText(context, context.getString(R.string.RA_something_wrong), Toast.LENGTH_SHORT).show();
        }
        if (null != baseState) {
            WebViewStateData webViewStateData = new WebViewStateData();
            webViewStateData.setServiceId(serviceId);
            webViewStateData.setTitle(title);
            baseState.setUiStateData(webViewStateData);
            baseState.navigate(new FragmentLauncher(activity, R.id.frame_container, (ActionBarListener) activity));
        }
    }

    protected BaseFlowManager getTargetFlowManager() {
        return ((AppFrameworkApplication) context.getApplicationContext()).getTargetFlowManager();
    }
}
