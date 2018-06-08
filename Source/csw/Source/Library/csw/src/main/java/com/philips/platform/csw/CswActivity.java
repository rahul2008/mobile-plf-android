/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.philips.cdp.registration.ui.utils.FontLoader;
import com.philips.platform.csw.permission.PermissionFragment;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.utils.UIDActivity;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CswActivity extends UIDActivity implements OnClickListener, ActionBarListener {

    final String iconFontAssetName = "PUIIcon.ttf";
    private TextView ivBack;
    protected CswInterface cswInterface = new CswInterface();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initDLSThemeIfExists();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.csw_activity);

        ivBack = findViewById(R.id.csw_textview_back);
        FontLoader.getInstance().setTypeface(ivBack, iconFontAssetName);
        ivBack.setText(com.philips.cdp.registration.R.string.ic_reg_left);
        ivBack.setOnClickListener(this);

        initUI();
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
        if (fragment instanceof PermissionFragment) {
            this.finish();
            return;
        } else if (fragment != null && fragment instanceof BackEventListener) {
            boolean isConsumed = ((BackEventListener) fragment).handleBackEvent();
            if (isConsumed)
                return;
        }
        super.onBackPressed();
    }

    public void initDLSThemeIfExists() {
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            getTheme().applyStyle(extras.getInt(CswConstants.DLS_THEME), true);
        }
    }

    private void initUI() {
        launchCswFragment();
    }

    private void launchCswFragment() {
        cswInterface.launch(new FragmentLauncher(this, R.id.csw_frame_layout_fragment_container, this), buildLaunchInput());
    }

    private CswLaunchInput buildLaunchInput() {
        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();
        return new CswLaunchInput(this, (List<ConsentDefinition>) extras.getSerializable(CswConstants.CONSENT_DEFINITIONS));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.csw_textview_back) {
            onBackPressed();
        }
    }

    @Override
    public void updateActionBar(int titleResourceID, boolean isShowBack) {
        updateActionBar(getString(titleResourceID), isShowBack);
        TextView tvTitle = findViewById(R.id.csw_textview_header_title);
        tvTitle.setText(getString(titleResourceID));
        if (isShowBack) {
            ivBack.setVisibility(View.VISIBLE);
            return;
        }
        ivBack.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateActionBar(String titleResourceText, boolean isShowBack) {
        TextView tvTitle = findViewById(R.id.csw_textview_header_title);
        tvTitle.setText(titleResourceText);
        if (isShowBack) {
            ivBack.setVisibility(View.VISIBLE);
            return;
        }
        ivBack.setVisibility(View.INVISIBLE);
    }
}
