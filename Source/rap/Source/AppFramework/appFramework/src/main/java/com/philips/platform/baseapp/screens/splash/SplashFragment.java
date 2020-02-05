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
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.listeners.FlowManagerListener;
import com.philips.platform.baseapp.base.AbstractOnboardingBaseFragment;
import com.philips.platform.baseapp.base.AbstractUIBasePresenter;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.AppFrameworkTagging;
import com.philips.platform.baseapp.base.AppInitializationCallback;
import com.philips.platform.baseapp.screens.introscreen.LaunchActivity;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.listener.BackEventListener;

public class SplashFragment extends AbstractOnboardingBaseFragment implements BackEventListener {
    public static String TAG = SplashFragment.class.getSimpleName();
    private final int APP_START = 1;
    AbstractUIBasePresenter presenter;
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
        View view = inflater.inflate(R.layout.af_splash_fragment, container, false);
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
        if (getApplicationContext() != null) {
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

        final FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity == null) return;
        AppFrameworkApplication applicationContext = (AppFrameworkApplication) fragmentActivity.getApplicationContext();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                applicationContext.initializeAppInfra(new AppInitializationCallback.AppInfraInitializationCallback() {
                    @Override
                    public void onAppInfraInitialization() {
                        startAppTagging();
                        if (getActivity() instanceof LaunchActivity) {
                            ((LaunchActivity) getActivity()).startCollectingLifecycleData();
                        }
                        applicationContext.initialize(new AppInitializationCallback.AppStatesInitializationCallback() {
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
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        RALog.d(TAG, " onMultiWindowModeChanged called");
        super.onStop();
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
        if (getFragmentActivity() != null) {
            return (AppFrameworkApplication) getFragmentActivity().getApplicationContext();
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
