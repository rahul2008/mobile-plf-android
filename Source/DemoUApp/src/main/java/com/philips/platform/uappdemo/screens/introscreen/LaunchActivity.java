/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.uappdemo.screens.introscreen;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.flowmanager.utility.UappConstants;
import com.philips.platform.uappdemo.screens.base.UappBaseActivity;
import com.philips.platform.uappdemo.screens.splash.SplashFragment;
import com.philips.platform.uappdemo.UappDemoUiHelper;
import com.philips.platform.uappdemolibrary.R;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;


/**
 * This class host the onboarding of the user .
 * It has two sections
 * 1. The user registration
 * 2. Welcome fragments
 */
public class LaunchActivity extends UappBaseActivity implements UappLaunchView {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initDLS();
        super.onCreate(savedInstanceState);
        presenter = new LaunchActivityPresenter(this);
        initCustomActionBar();
        setContentView(R.layout.af_launch_activity);
        presenter.onEvent(LaunchActivityPresenter.APP_LAUNCH_STATE);
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
    public void finishActivity() {
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {
        textView.setText(i);
        setTitle(getResources().getString(i));
    }

    @Override
    public void updateActionBar(String s, boolean b) {
        textView.setText(s);
        setTitle(s);
    }

    @Override
    public void updateActionBarIcon(boolean b) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == SplashFragment.PERMISSION_ALL) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                Fragment fragment = fragmentManager
                        .findFragmentById(R.id.welcome_frame_container);
                if (fragment instanceof SplashFragment) {
                    SplashFragment splashFragment = (SplashFragment) fragment;
                    splashFragment.permissionGranted();
                }
            } else {
                //Displaying another toast if permission is not granted
                finish();
            }
        }
    }

    public void initDLS() {
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));
        getTheme().applyStyle(R.style.Theme_Philips_DarkBlue_NoActionBar, true);
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
            presenter.onEvent(UappConstants.BACK_BUTTON_CLICK_CONSTANT);
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

    @Override
    public BaseFlowManager getTargetFlowManager() {
        return UappDemoUiHelper.getInstance().getFlowManager();
    }

    @Override
    public boolean isInMultiWindowMode() {
        return super.isInMultiWindowMode();
    }
}
