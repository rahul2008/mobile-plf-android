/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.splash;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.listeners.AppFlowJsonListener;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.OnboardingBaseFragment;
import com.philips.platform.baseapp.base.UIBasePresenter;
import com.philips.platform.baseapp.screens.introscreen.LaunchActivity;
import com.philips.platform.uappframework.listener.BackEventListener;

public class SplashFragment extends OnboardingBaseFragment implements BackEventListener, AppFlowJsonListener {
    public static String TAG = LaunchActivity.class.getSimpleName();
    public static int STORAGE_PERMISSION_CODE = 999;
    private static int SPLASH_TIME_OUT = 3000;
    private final int APP_START = 1;
    UIBasePresenter presenter;
    private boolean isVisible = false;
    private boolean isMultiwindowEnabled = false;
    private ProgressDialog progressDialog;

    /*
     * 'Android N' doesn't support single parameter in "Html.fromHtml". So adding the if..else condition and
     * suppressing "deprecation" for 'else' block.
     */
    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.uikit_splash_screen_logo_center_tb, container, false);
        initProgressDialog();
        ImageView logo = (ImageView) view.findViewById(R.id.splash_logo);
        logo.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.uikit_philips_logo, getActivity().getTheme()));

        String splashScreenTitle = getResources().getString(R.string.splash_screen_title);
        CharSequence titleText;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            titleText = Html.fromHtml(splashScreenTitle, Html.FROM_HTML_MODE_LEGACY);
        } else {
            titleText = Html.fromHtml(splashScreenTitle);
        }

        TextView title = (TextView) view.findViewById(R.id.splash_title);
        title.setText(titleText);
        initializeFlowManager();
        return view;
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(getFragmentActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
    }

    private void showProgressDialog(boolean show) {
        if (show)
            progressDialog.show();
        else if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void initializeFlowManager() {
        showProgressDialog(true);
        if (isPermissionGranted()) {
            setFlowManager();
            if (!isMultiwindowEnabled)
                startTimer();
        } else {
            requestStoragePermission();
        }
    }

    private void setFlowManager() {
        getApplicationContext().setTargetFlowManager();
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getFragmentActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getFragmentActivity(), "Request to grant permission to read App flow Json", Toast.LENGTH_LONG).show();
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getFragmentActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final LaunchActivity launchActivity = (LaunchActivity) getActivity();
        launchActivity.hideActionBar();
    }

    private boolean isPermissionGranted() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getFragmentActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        //If permission is not granted returning false
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        isMultiwindowEnabled = true;
    }

    private void startTimer() {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                if (isVisible) {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    showProgressDialog(false);
                    presenter = new SplashPresenter(SplashFragment.this);
                    presenter.onEvent(APP_START);
                }
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onStop() {
        super.onStop();
        isVisible = false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getActivity().getWindow().getDecorView().requestLayout();
    }

    @Override
    public boolean handleBackEvent() {
        return true;
    }

    @Override
    public void onParseSuccess() {

    }

    public AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) getFragmentActivity().getApplicationContext();
    }

    public void permissionGranted() {
        setFlowManager();
        if (!isMultiwindowEnabled)
            startTimer();
    }
}
