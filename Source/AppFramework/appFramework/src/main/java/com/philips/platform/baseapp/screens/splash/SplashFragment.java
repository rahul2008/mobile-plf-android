/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.splash;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.listeners.FlowManagerListener;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.AppInitializationCallback;
import com.philips.platform.baseapp.base.OnboardingBaseFragment;
import com.philips.platform.baseapp.base.UIBasePresenter;
import com.philips.platform.baseapp.screens.introscreen.LaunchActivity;
import com.philips.platform.uappframework.listener.BackEventListener;

public class SplashFragment extends OnboardingBaseFragment implements BackEventListener {
    public static String TAG = LaunchActivity.class.getSimpleName();
    private static int SPLASH_TIME_OUT = 3000;
    private final int APP_START = 1;
    UIBasePresenter presenter;
    private boolean isVisible = false;
    ImageView logo;
    TextView title;
    private Handler handler = new Handler();


    /*
     * 'Android N' doesn't support single parameter in "Html.fromHtml". So adding the if..else condition and
     * suppressing "deprecation" for 'else' block.
     */
    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.uikit_splash_screen_logo_center_tb, container, false);
        logo = (ImageView) view.findViewById(R.id.splash_logo);
        logo.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.uikit_philips_logo, getActivity().getTheme()));
        String splashScreenTitle = getResources().getString(R.string.RA_SplashScreen_Title);
        CharSequence titleText;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            titleText = Html.fromHtml(splashScreenTitle, Html.FROM_HTML_MODE_LEGACY);
        } else {
            titleText = Html.fromHtml(splashScreenTitle);
        }

        title = (TextView) view.findViewById(R.id.splash_title);
        title.setText(titleText);
        return view;
    }

    private void initializeFlowManager() {
        setFlowManager();
    }

    private void setFlowManager() {
        getApplicationContext().setTargetFlowManager(new FlowManagerListener() {
            @Override
            public void onParseSuccess() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter = new SplashPresenter(SplashFragment.this);
                        presenter.onEvent(APP_START);
                    }
                }, 200);
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof LaunchActivity) {
            final LaunchActivity launchActivity = (LaunchActivity) getActivity();
            launchActivity.hideActionBar();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                ((AppFrameworkApplication) getActivity().getApplicationContext()).initializeAppInfra(new AppInitializationCallback.AppInfraInitializationCallback() {
                    @Override
                    public void onAppInfraInitialization() {
                        if (getActivity() instanceof LaunchActivity) {
                            ((LaunchActivity)getActivity()).startCollectingLifecycleData();
                            ((LaunchActivity)getActivity()).startPushNotificationFlow();
                        }
                        ((AppFrameworkApplication) getActivity().getApplicationContext()).initialize(new AppInitializationCallback.AppStatesInitializationCallback() {
                            @Override
                            public void onAppStatesInitialization() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        initializeFlowManager();
                                    }
                                });
                            }
                        });
                    }
                });

                Looper.loop();
            }
        });
        thread.start();
        modifyLayoutforMultiWindow();
    }

    private void modifyLayoutforMultiWindow() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if (getFragmentActivity().isInMultiWindowMode()) {
                logo.getLayoutParams().width = (int) getResources().getDimension(R.dimen.uikit_hamburger_logo_width);
                logo.getLayoutParams().height = (int) getResources().getDimension(R.dimen.uikit_hamburger_logo_height);
                logo.setPadding(0, 0, 0, 20);
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
        if (isInMultiWindowMode) {
            modifyLayoutforMultiWindow();
        } else {
            getActivity().getWindow().getDecorView().requestLayout();
        }

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


    public AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) getFragmentActivity().getApplicationContext();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logo = null;
        title = null;
    }
}
