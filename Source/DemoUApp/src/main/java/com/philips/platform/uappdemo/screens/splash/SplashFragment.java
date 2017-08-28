/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappdemo.screens.splash;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.listeners.FlowManagerListener;
import com.philips.platform.uappdemo.screens.base.UappBasePresenter;
import com.philips.platform.uappdemo.screens.base.UappOnBoardingBaseFragment;
import com.philips.platform.uappdemo.screens.introscreen.LaunchActivity;
import com.philips.platform.uappdemo.UappDemoUiHelper;
import com.philips.platform.uappdemolibrary.R;
import com.philips.platform.uappframework.listener.BackEventListener;


public class SplashFragment extends UappOnBoardingBaseFragment implements BackEventListener, FlowManagerListener {
    public static String TAG = LaunchActivity.class.getSimpleName();
    public static int PERMISSION_ALL = 998;
    private static int SPLASH_TIME_OUT = 3000;
    private final int APP_START = 1;
    UappBasePresenter presenter;
    ImageView logo;
    TextView title;
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
            View view = inflater.inflate(R.layout.uikit_splash_screen_logo_center_tb,container,false);
        logo = (ImageView) view.findViewById(R.id.splash_logo);
        logo.setImageDrawable(VectorDrawableCompat.create(getResources(),R.drawable.uikit_philips_logo, getActivity().getTheme()));
        String splashScreenTitle = getResources().getString(R.string.splash_screen_title);
        CharSequence titleText;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            titleText = Html.fromHtml(splashScreenTitle, Html.FROM_HTML_MODE_LEGACY);
        } else {
            titleText = Html.fromHtml(splashScreenTitle);
        }

        title = (TextView) view.findViewById(R.id.splash_title);
        title.setText(titleText);
        return view;
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(getFragmentActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
    }

    private void showProgressDialog(boolean show) {
        if (show && !getFragmentActivity().isFinishing() && !progressDialog.isShowing())
            progressDialog.show();
        else if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void initializeFlowManager() {
        showProgressDialog(true);
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (hasPermissions(PERMISSIONS)) {
             setFlowManager();
            if (!isMultiwindowEnabled)
                startTimer();
        } else {
            requestStoragePermission();
        }
    }

    private void setFlowManager() {
       UappDemoUiHelper.getInstance().setTargetFlowManager(this,getActivity());
    }

    //Requesting permission
    private void requestStoragePermission() {
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!hasPermissions(PERMISSIONS)) {
            ActivityCompat.requestPermissions(getFragmentActivity(), PERMISSIONS, PERMISSION_ALL);
        }
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final LaunchActivity launchActivity = (LaunchActivity) getActivity();
        launchActivity.hideActionBar();

        initProgressDialog();
        initializeFlowManager();
    }

    private boolean hasPermissions(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getFragmentActivity() != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(getFragmentActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
        modifyLayoutforMultiWindow();
    }

    private void modifyLayoutforMultiWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (getFragmentActivity().isInMultiWindowMode()) {
                logo.getLayoutParams().width = (int) getResources().getDimension(R.dimen.uikit_hamburger_logo_width);
                logo.getLayoutParams().height = (int) getResources().getDimension(R.dimen.uikit_hamburger_logo_height);
                logo.setPadding(0,0,0,20);
                logo.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.uikit_philips_logo, getActivity().getTheme()));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(0, 0, 0, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                title.setLayoutParams(params);

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
       if(isInMultiWindowMode){
           modifyLayoutforMultiWindow();
       }
        else {
           getActivity().getWindow().getDecorView().requestLayout();
       }

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


    public void permissionGranted() {
         setFlowManager();
        if (!isMultiwindowEnabled)
            startTimer();
    }

    @Override
    public BaseFlowManager getTargetFlowManager() {
        return UappDemoUiHelper.getInstance().getFlowManager();
    }
}
