/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.introscreen;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

/**
 * This class host the onboarding of the user .
 * It has two sections
 * 1. The user registration
 * 2. Welcome fragments
 */
public class LaunchActivity extends AppFrameworkBaseActivity implements ActionBarListener, WelcomeView {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LaunchActivityPresenter(this);
        initCustomActionBar();
        setContentView(R.layout.af_welcome_activity);
        presenter.onLoad();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initCustomActionBar() {
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setDisplayShowCustomEnabled(true);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the text view in the ActionBar !
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER);
            View mCustomView = LayoutInflater.from(this).inflate(R.layout.af_home_action_bar, null); // layout which contains your button.


            final FrameLayout frameLayout = (FrameLayout) mCustomView.findViewById(R.id.home_action_bar_button_layout);
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    onBackPressed();
                }
            });
            final ImageView arrowImage = (ImageView) mCustomView
                    .findViewById(R.id.home_action_bar_arrow_left);
            textView = (TextView) mCustomView.findViewById(R.id.home_action_bar_text);
            arrowImage.setBackground(VectorDrawable.create(this, R.drawable.left_arrow));
            mActionBar.setCustomView(mCustomView, params);
        }
    }

    @Override
    public void showActionBar() {
        if (getSupportActionBar() != null)
            getSupportActionBar().show();
    }

    @Override
    public void hideActionBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
    }

    @Override
    public void finishActivityAffinity() {
        finishAffinity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {
        textView.setText(i);
    }

    @Override
    public void updateActionBar(String s, boolean b) {
        textView.setText(s);
    }

    @Override
    public void updateActionBarIcon(boolean b) {

    }

    @Override
    public void onBackPressed() {
        boolean isConsumed = false;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager
                .findFragmentById(R.id.welcome_frame_container);
        if (fragment != null && fragment instanceof BackEventListener) {
            isConsumed = ((BackEventListener) fragment).handleBackEvent();
        }
        if (!isConsumed) {

            presenter.onClick(Constants.BACK_BUTTON_CLICK_CONSTANT);
        }
    }
    @Override
    public FragmentActivity getFragmentActivity() {
        return this;
    }

    @Override
    public ActionBarListener getActionBarListener() {
        return this;
    }

    @Override
    public int getContainerId() {
        return R.id.welcome_frame_container;
    }
}
