/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.philips.cdp.registration.ui.utils.FontLoader;
import com.philips.platform.catk.CatkConstants;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.utils.UIDActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyAccountActivity extends UIDActivity implements OnClickListener, ActionBarListener {

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
            applicationName = bundle.getString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME);
            propositionName = bundle.getString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME);
        }

        setContentView(R.layout.mya_activity_account);

        ivBack = findViewById(R.id.mya_textview_back);
        FontLoader.getInstance().setTypeface(ivBack, iconFontAssetName);
        ivBack.setText(com.philips.cdp.registration.R.string.ic_reg_left);
        ivBack.setOnClickListener(this);
        initUI();
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        applicationName = state.getString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME, applicationName);
        propositionName = state.getString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME, propositionName);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME, applicationName);
        state.putString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME, propositionName);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager
                .findFragmentById(R.id.mya_frame_layout_fragment_container);
        if (fragment != null && fragment instanceof BackEventListener) {
            boolean isConsumed = ((BackEventListener) fragment).handleBackEvent();
            if (!isConsumed) {
                super.onBackPressed();
            }
        }
    }

    private void initUI() {
        launchAccountFragment();
    }

    private void launchAccountFragment() {
        MyaLaunchInput myaLaunchInput = new MyaLaunchInput();
        FragmentLauncher fragmentLauncher = new FragmentLauncher
                (this, R.id.mya_frame_layout_fragment_container, this);
        MyaInterface myaInterface = new MyaInterface();
        myaInterface.launch(fragmentLauncher, myaLaunchInput);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mya_textview_back) {
            onBackPressed();
        }
    }

    @Override
    public void updateActionBar(int titleResourceID, boolean isShowBack) {
        TextView tvTitle = findViewById(R.id.mya_textview_header_title);
        tvTitle.setText(getString(titleResourceID));
        if (isShowBack) {
            ivBack.setVisibility(View.VISIBLE);
            return;
        }
        ivBack.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateActionBar(String titleResourceText, boolean isShowBack) {
        TextView tvTitle = findViewById(R.id.mya_textview_header_title);
        tvTitle.setText(titleResourceText);
        if (isShowBack) {
            ivBack.setVisibility(View.VISIBLE);
            return;
        }
        ivBack.setVisibility(View.INVISIBLE);
    }
}
