/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.splash;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.uikit.customviews.CircularProgressbar;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.FileUtility;
import com.philips.platform.baseapp.base.UIBasePresenter;
import com.philips.platform.baseapp.screens.introscreen.LaunchActivity;
import com.philips.platform.baseapp.screens.introscreen.LaunchView;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import philips.appframeworklibrary.flowmanager.listeners.AppFlowJsonListener;

public class SplashFragment extends Fragment implements LaunchView, BackEventListener,AppFlowJsonListener {
    public static String TAG = LaunchActivity.class.getSimpleName();
    private static int SPLASH_TIME_OUT = 3000;
    private final int APP_START = 1;
    UIBasePresenter presenter;
    private boolean isVisible = false;
	private boolean isMultiwindowEnabled = false;
    private CircularProgressbar circularProgressbar;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.uikit_splash_screen_logo_center_tb,container,false);
        ViewGroup group = (ViewGroup) view.findViewById(R.id.splash_layout);
        ImageView logo = (ImageView) view.findViewById(R.id.splash_logo);
        logo.setImageDrawable(VectorDrawableCompat.create(getResources(),R.drawable.uikit_philips_logo, getActivity().getTheme()) );

        String splashScreenTitle = getResources().getString(R.string.splash_screen_title);
        CharSequence titleText = Html.fromHtml(splashScreenTitle);

        TextView title = (TextView) view.findViewById(R.id.splash_title);
        title.setText(titleText);
        initializeFlowManager();
        return view;
    }

    private void initializeFlowManager() {
        final int resId = R.string.com_philips_app_fmwk_app_flow_url;
        FileUtility fileUtility = new FileUtility(getFragmentActivity());
        circularProgressbar = (CircularProgressbar) getFragmentActivity().findViewById(R.id.splash_progress_bar);
        circularProgressbar.setVisibility(View.VISIBLE);
        getApplicationContext().getTargetFlowManager().initFlowManager(getFragmentActivity().getApplicationContext(),fileUtility.createFileFromInputStream(resId).getPath(),this);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final LaunchActivity launchActivity = (LaunchActivity) getActivity();
        launchActivity.hideActionBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
		if(!isMultiwindowEnabled)
           startTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        //isVisible = false;
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
                if(isVisible) {
                    // This method will be executed once the timer is over
                    // Start your app main activity
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
    public void showActionBar() {
        final LaunchActivity launchActivity = (LaunchActivity) getActivity();
        launchActivity.showActionBar();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getFragmentActivity().getWindow().getDecorView().requestLayout();
    }

    @Override
    public void hideActionBar() {
        final LaunchActivity launchActivity = (LaunchActivity) getActivity();
        launchActivity.hideActionBar();
    }

    @Override
    public void finishActivityAffinity() {
        final LaunchActivity launchActivity = (LaunchActivity) getActivity();
        launchActivity.finishAffinity();
    }

    @Override
    public ActionBarListener getActionBarListener() {
        return (LaunchActivity) getActivity();
    }

    @Override
    public int getContainerId() {
        return R.id.welcome_frame_container;
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }

    @Override
    public boolean handleBackEvent() {
        return true;
    }

    @Override
    public void onParseSuccess() {
        circularProgressbar.setVisibility(View.GONE);
    }

    protected AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) getFragmentActivity().getApplicationContext();
    }

}
