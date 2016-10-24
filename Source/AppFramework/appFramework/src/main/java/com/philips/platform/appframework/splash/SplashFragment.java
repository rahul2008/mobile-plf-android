package com.philips.platform.appframework.splash;

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

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.introscreen.WelcomeActivity;
import com.philips.platform.appframework.introscreen.WelcomeView;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.uappframework.listener.ActionBarListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SplashFragment extends Fragment implements WelcomeView{
    private boolean isVisible = false;
    private static int SPLASH_TIME_OUT = 3000;
    public static String TAG = WelcomeActivity.class.getSimpleName();
    UIBasePresenter presenter;

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
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
        startTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        isVisible = false;
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
                    presenter.onLoad();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void showActionBar() {
        final WelcomeActivity welcomeActivity = (WelcomeActivity) getActivity();
        welcomeActivity.showActionBar();
    }

    @Override
    public void hideActionBar() {
        final WelcomeActivity welcomeActivity = (WelcomeActivity) getActivity();
        welcomeActivity.hideActionBar();
    }

    @Override
    public void loadWelcomeFragment() {
        final WelcomeActivity welcomeActivity = (WelcomeActivity) getActivity();
        welcomeActivity.loadWelcomeFragment();
    }

    @Override
    public void finishActivityAffinity() {
        final WelcomeActivity welcomeActivity = (WelcomeActivity) getActivity();
        welcomeActivity.finishAffinity();
    }

    @Override
    public ActionBarListener getActionBarListener() {
        return (WelcomeActivity) getActivity();
    }

    @Override
    public int getContainerId() {
        return R.id.welcome_frame_container;
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return (WelcomeActivity)getActivity();
    }

}
