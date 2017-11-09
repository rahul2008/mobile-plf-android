/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.introscreen;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.philips.cdp.di.iap.integration.IAPListener;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.thememanager.UIDHelper;

import java.util.ArrayList;

/**
 * This class host the onboarding of the user .
 * It has two sections
 * 1. The user registration
 * 2. Welcome fragments
 */
public class LaunchActivity extends AbstractAppFrameworkBaseActivity implements LaunchView, IAPListener {
    public static final String TAG = LaunchActivity.class.getSimpleName();

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RALog.d(TAG, " onCreate called  ");
        super.onCreate(savedInstanceState);
        presenter = new LaunchActivityPresenter(this);
        setContentView(R.layout.af_launch_activity);
        initToolBar();
        presenter.onEvent(LaunchActivityPresenter.APP_LAUNCH_STATE);
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.uid_toolbar);
        UIDHelper.setupToolbar(this);
        toolbar.setNavigationIcon(VectorDrawableCompat.create(getResources(), R.drawable.left_arrow, getTheme()));
    }

    @Override
    public void updateActionBarIcon(boolean b) {

    }

    @Override
    protected void onResume() {
        super.onResume();
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
        RALog.d(TAG, " finishActivityAffinity called  ");

        finishAffinity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {
        UIDHelper.setTitle(this, getResources().getString(i));
    }

    @Override
    public void updateActionBar(String title, boolean b) {
        UIDHelper.setTitle(this, title);
    }


    @Override
    public void onBackPressed() {
        RALog.d(TAG," onBackPressed called  ");
        boolean isConsumed = false;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager
                .findFragmentById(R.id.welcome_frame_container);
        if (fragment != null && fragment instanceof BackEventListener) {
            isConsumed = ((BackEventListener) fragment).handleBackEvent();
        }
        if (!isConsumed) {
            presenter.onEvent(Constants.BACK_BUTTON_CLICK_CONSTANT);
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
    public boolean isInMultiWindowMode() {
        return super.isInMultiWindowMode();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getSupportActionBar() != null) {
            presenter = null;
        }
    }

    @Override
    public void onGetCartCount(int i) {

    }

    @Override
    public void onUpdateCartCount() {

    }

    @Override
    public void updateCartIconVisibility(boolean b) {

    }

    @Override
    public void onGetCompleteProductList(ArrayList<String> arrayList) {

    }

    @Override
    public void onSuccess() {

    }


    @Override
    public void onSuccess(boolean isCartVisible) {

    }
    @Override
    public void onFailure(int i) {

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }
}
