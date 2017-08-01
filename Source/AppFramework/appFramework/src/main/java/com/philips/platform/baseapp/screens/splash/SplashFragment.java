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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.listeners.FlowManagerListener;
import com.philips.platform.baseapp.base.AbstractUIBasePresenter;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.AppFrameworkTagging;
import com.philips.platform.baseapp.base.AppInitializationCallback;
import com.philips.platform.baseapp.base.AbstractOnboardingBaseFragment;
import com.philips.platform.baseapp.screens.introscreen.LaunchActivity;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.listener.BackEventListener;

public class SplashFragment extends AbstractOnboardingBaseFragment implements BackEventListener {
    public static String TAG = SplashFragment.class.getSimpleName();
    private static int SPLASH_TIME_OUT = 3000;
    private final int APP_START = 1;
    AbstractUIBasePresenter presenter;
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
        RALog.d(TAG, " onCreateView ");
        View view = inflater.inflate(R.layout.uikit_splash_screen_logo_center_tb, container, false);
        logo = (ImageView) view.findViewById(R.id.splash_logo);
        logo.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.uikit_philips_logo, getActivity().getTheme()));
        String splashScreenTitle = getResources().getString(R.string.RA_DLS_splash_title);
        CharSequence titleText;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            titleText = Html.fromHtml(splashScreenTitle, Html.FROM_HTML_MODE_LEGACY);
        } else {
            titleText = Html.fromHtml(splashScreenTitle);
        }

        title = (TextView) view.findViewById(R.id.splash_title);
        title.setGravity(Gravity.CENTER);
        title.setText(titleText);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, logo.getId());
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        final int inPixels= (int) getResources().getDimension(R.dimen.pop_up_image_margin_top);
        params.setMargins(0, inPixels, 0, 0);
        title.setLayoutParams(params);

        return view;
    }

    protected void startAppTagging() {
        AppFrameworkTagging.getInstance().trackPage(TAG);
    }

    private void initializeFlowManager() {
        RALog.d(TAG, " initializeFlowManager ");
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
        RALog.d(TAG, " onResume called ");
        super.onResume();
        isVisible = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                ((AppFrameworkApplication) getActivity().getApplicationContext()).initializeAppInfra(new AppInitializationCallback.AppInfraInitializationCallback() {
                    @Override
                    public void onAppInfraInitialization() {
                        startAppTagging();
                        if (getActivity() instanceof LaunchActivity) {
                            ((LaunchActivity) getActivity()).startCollectingLifecycleData();
                            ((LaunchActivity) getActivity()).startPushNotificationFlow();
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
        RALog.d(TAG, " modifyLayoutforMultiWindow called ");
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
        RALog.d(TAG, " onMultiWindowModeChanged called");
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        if (isInMultiWindowMode) {
            modifyLayoutforMultiWindow();
        } else {
            getActivity().getWindow().getDecorView().requestLayout();
        }

    }

    @Override
    public void onStop() {
        RALog.d(TAG, " onMultiWindowModeChanged called");
        super.onStop();
        isVisible = false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        RALog.d(TAG, " onConfigurationChanged called  ");
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
