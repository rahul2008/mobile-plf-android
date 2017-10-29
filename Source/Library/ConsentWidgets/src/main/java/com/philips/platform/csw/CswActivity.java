/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.platform.csw;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.philips.cdp.registration.ui.utils.FontLoader;
import com.philips.platform.mya.catk.utils.ConsentUtil;
import com.philips.platform.mya.consentwidgets.R;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.utils.UIDActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CswActivity extends UIDActivity implements OnClickListener,
        ActionBarListener {

    final String iconFontAssetName = "PUIIcon.ttf";

    private TextView ivBack;

    private String applicationName;
    private String propositionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            applicationName = bundle.getString(ConsentUtil.BUNDLE_KEY_APPLICATION_NAME);
            propositionName = bundle.getString(ConsentUtil.BUNDLE_KEY_PROPOSITION_NAME);

            /*if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }*/
        }

        setContentView(R.layout.csw_activity);

        ivBack = (TextView) findViewById(R.id.csw_textview_back);
        FontLoader.getInstance().setTypeface(ivBack, iconFontAssetName);
        ivBack.setText(com.philips.cdp.registration.R.string.ic_reg_left);
        ivBack.setOnClickListener(this);

        initUI();

    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        applicationName = state.getString(ConsentUtil.BUNDLE_KEY_APPLICATION_NAME, applicationName);
        propositionName = state.getString(ConsentUtil.BUNDLE_KEY_PROPOSITION_NAME, propositionName);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString(ConsentUtil.BUNDLE_KEY_APPLICATION_NAME, applicationName);
        state.putString(ConsentUtil.BUNDLE_KEY_PROPOSITION_NAME, propositionName);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager
                .findFragmentById(R.id.csw_frame_layout_fragment_container);
        if (fragment != null && fragment instanceof BackEventListener) {
            boolean isConsumed = ((BackEventListener) fragment).handleBackEvent();
            if (isConsumed)
                return;
        }
        super.onBackPressed();
    }

    private void initUI() {
        launchCswFragment();
    }

    private void launchCswFragment() {
        new CswInterface().launch(new FragmentLauncher(this, R.id.csw_frame_layout_fragment_container, this), buildLaunchInput());
    }

    private CswLaunchInput buildLaunchInput() {
        CswLaunchInput cswLaunchInput = new CswLaunchInput();
        cswLaunchInput.setApplicationName(applicationName);
        cswLaunchInput.setPropositionName(propositionName);
        return cswLaunchInput;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.csw_textview_back) {
            onBackPressed();
        }
    }

    @Override
    public void updateActionBar(int titleResourceID, boolean isShowBack) {
        TextView tvTitle = ((TextView) findViewById(R.id.csw_textview_header_title));
        tvTitle.setText(getString(titleResourceID));
        if (isShowBack) {
            ivBack.setVisibility(View.VISIBLE);
            return;
        }
        ivBack.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateActionBar(String titleResourceText, boolean isShowBack) {
        TextView tvTitle = ((TextView) findViewById(R.id.csw_textview_header_title));
        tvTitle.setText(titleResourceText);
        if (isShowBack) {
            ivBack.setVisibility(View.VISIBLE);
            return;
        }
        ivBack.setVisibility(View.INVISIBLE);
    }
}
