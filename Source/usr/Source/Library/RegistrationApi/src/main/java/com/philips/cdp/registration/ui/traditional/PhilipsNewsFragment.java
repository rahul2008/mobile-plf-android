/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.platform.uid.view.widget.Label;

public class PhilipsNewsFragment extends RegistrationBaseFragment {
    private String TAG = "PhilipsNewsFragment";
    private Label titleLabel;
    private Label discretionLabel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reg_fragment_philips_news, null);
        RLog.i(TAG, "Screen name is " + TAG);
        titleLabel = view.findViewById(R.id.usr_forgotpassword_title_label);
        discretionLabel = view.findViewById(R.id.reg_philips_communication_content1);

        handleOrientation(view);
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(TAG, " onConfigurationChanged");
        setCustomParams(config);
    }

    @Override
    protected void setViewParams(Configuration config, int width) {

    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        if (bundle != null) {
            titleLabel.setText(bundle.getString(RegConstants.PHILIPS_NEWS_TITLE));
            discretionLabel.setText(bundle.getString(RegConstants.PHILIPS_NEWS_DISCRETION));
        }
    }

    @Override
    public int getTitleResourceId() {
        return R.string.USR_DLS_PhilipsNews_NavigationBar_Title;
    }

    @Override
    public void notificationInlineMsg(String msg) {
        //NOP
    }
}
