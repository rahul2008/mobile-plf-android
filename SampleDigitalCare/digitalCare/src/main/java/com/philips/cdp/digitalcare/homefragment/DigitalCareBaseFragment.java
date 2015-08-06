package com.philips.cdp.digitalcare.homefragment;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.philips.cdp.digitalcare.listeners.ActionbarUpdateListener;
import com.philips.cdp.digitalcare.listeners.NetworkStateListener;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.customview.DigitalCareFontTextView;
import com.philips.cdp.digitalcare.customview.NetworkAlertView;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;
import com.philips.cdp.digitalcare.util.NetworkReceiver;

/**
 * DigitalCareBaseFragment is <b>Base class</b> for all fragments.
 *
 * @author: ritesh.jha@philips.com
 * @since: Dec 5, 2014
 */
public abstract class DigitalCareBaseFragment extends Fragment implements
        OnClickListener, NetworkStateListener {

    private static String TAG = DigitalCareBaseFragment.class.getSimpleName();
    private static boolean isConnectionAvailable;
    private static int mContainerId = 0;
    private static ActionbarUpdateListener mActionbarUpdateListener = null;
    private static String mPreviousPageName = null;
    private static int mEnterAnimation = 0;
    private static int mExitAnimation = 0;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    protected int mLeftRightMarginPort = 0;
    protected int mLeftRightMarginLand = 0;
    private static FragmentActivity mFragmentActivityContext = null;
    private NetworkReceiver mNetworkutility = null;
    private static FragmentActivity mActivityContext = null;
    private FragmentManager fragmentManager = null;
    private Thread mUiThread = Looper.getMainLooper().getThread();

    public synchronized static void setStatus(boolean connection) {
        if (connection)
            isConnectionAvailable = true;
        else {
            isConnectionAvailable = false;
        }
    }

    public abstract void setViewParams(Configuration config);

    public abstract String getActionbarTitle();

    public abstract String setPreviousPageName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        DigiCareLogger.i(DigiCareLogger.FRAGMENT, "OnCreate on "
                + this.getClass().getSimpleName());
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        mFragmentActivityContext = getActivity();
        registerNetWorkReceiver();
    }

    private void registerNetWorkReceiver() {

        IntentFilter mfilter = new IntentFilter(
                "android.net.conn.CONNECTIVITY_CHANGE");
        mNetworkutility = new NetworkReceiver(this);
        getActivity().registerReceiver(mNetworkutility, mfilter);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLeftRightMarginPort = (int) mFragmentActivityContext.getResources()
                .getDimension(R.dimen.activity_margin_port);
        mLeftRightMarginLand = (int) mFragmentActivityContext.getResources()
                .getDimension(R.dimen.activity_margin_land);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.i(DigiCareLogger.FRAGMENT, "OnCreateView on "
                + this.getClass().getSimpleName());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        DigiCareLogger.i(DigiCareLogger.FRAGMENT, "OnStart on "
                + this.getClass().getSimpleName());
        super.onStart();
    }

    @Override
    public void onResume() {
        DigiCareLogger.i(DigiCareLogger.FRAGMENT, "OnResume on "
                + this.getClass().getSimpleName());
        super.onResume();
        setActionbarTitle();
    }

    @Override
    public void onPause() {
        DigiCareLogger.i(DigiCareLogger.FRAGMENT, "OnPause on "
                + this.getClass().getSimpleName());
        super.onPause();
    }

    @Override
    public void onStop() {
        DigiCareLogger.i(DigiCareLogger.FRAGMENT, "OnStop on "
                + this.getClass().getSimpleName());
        super.onStop();
    }

    @Override
    public void onDestroy() {
        DigiCareLogger.i(DigiCareLogger.FRAGMENT, "onDestroy on "
                + this.getClass().getSimpleName());
        getActivity().unregisterReceiver(mNetworkutility);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        DigiCareLogger.i(DigiCareLogger.FRAGMENT, "OnDestroyView on "
                + this.getClass().getSimpleName());
        super.onDestroyView();
        mPreviousPageName = setPreviousPageName();
        hideKeyboard();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected boolean isConnectionAvailable() {
        if (isConnectionAvailable)
            return true;
        else {
            // new NetworkAlertView().showNetworkAlert(getActivity());
            mHandler.postAtFrontOfQueue(new Runnable() {

                @Override
                public void run() {
                    new NetworkAlertView().showAlertBox(
                            getActivity(),
                            null,
                            getActivity().getResources().getString(
                                    R.string.no_internet),
                            getActivity().getResources().getString(
                                    android.R.string.yes));
                    AnalyticsTracker
                            .trackAction(
                                    AnalyticsConstants.ACTION_SET_ERROR,
                                    AnalyticsConstants.ACTION_KEY_TECHNICAL_ERROR,
                                    AnalyticsConstants.ACTION_VALUE_TECHNICAL_ERROR_NETWORK_CONNECITON);

                }
            });
            return false;

        }
    }

    protected void showAlert(final String message) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                new NetworkAlertView().showAlertBox(
                        getActivity(),
                        null,
                        message,
                        getActivity().getResources().getString(
                                android.R.string.yes));
                AnalyticsTracker
                        .trackAction(
                                AnalyticsConstants.ACTION_SET_ERROR,
                                AnalyticsConstants.ACTION_KEY_TECHNICAL_ERROR,
                                AnalyticsConstants.ACTION_VALUE_TECHNICAL_ERROR_NETWORK_CONNECITON);

            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DigiCareLogger.i(TAG, TAG + " : onConfigurationChanged ");
        getAppName();
    }

    private void enableActionBarLeftArrow() {

        ImageView backToHome = (ImageView) mFragmentActivityContext
                .findViewById(R.id.back_to_home_img);
        ImageView homeIcon = (ImageView) mFragmentActivityContext
                .findViewById(R.id.home_icon);
        homeIcon.setVisibility(View.GONE);
        backToHome.setVisibility(View.VISIBLE);
        backToHome.bringToFront();
    }

	/*
     * This method can be called directly from outside and helps to invoke the
	 * fragments, instead of full screen activity. DigitalCare fragments will be
	 * added in the root container of hosting app. Integrating app has to pass
	 * some parameters in order to do smooth operations.
	 */

    /*
    This method will provide vertical APP NAME which is required for TAGGING (Analytics).
     */
    protected String getAppName() {
        String appName = getActivity().getString(R.string.app_name);
        try {
            ApplicationInfo appInfo = getActivity().getPackageManager().getApplicationInfo(getActivity().getPackageName(),
                    getActivity().getPackageManager().GET_META_DATA);
            appName = appInfo.loadLabel(getActivity().getPackageManager()).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
    }

    protected void showFragment(Fragment fragment) {
        int containerId = R.id.mainContainer;

        if (mContainerId != 0) {
            containerId = mContainerId;
            mFragmentActivityContext = mActivityContext;
        } else {
            enableActionBarLeftArrow();
            InputMethodManager imm = (InputMethodManager) mFragmentActivityContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (mFragmentActivityContext.getWindow() != null
                    && mFragmentActivityContext.getWindow().getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(mFragmentActivityContext
                        .getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        try {
            FragmentTransaction fragmentTransaction = mFragmentActivityContext
                    .getSupportFragmentManager().beginTransaction();
            if (mEnterAnimation != 0 && mExitAnimation != 0) {
                fragmentTransaction.setCustomAnimations(mEnterAnimation,
                        mExitAnimation, mEnterAnimation, mExitAnimation);
            }
                fragmentTransaction.replace(containerId, fragment, DigitalCareConstants.DIGITALCARE_FRAGMENT_TAG);
            fragmentTransaction.hide(this);
            fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            DigiCareLogger.e(TAG, "");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        DigiCareLogger.d(DigiCareLogger.FRAGMENT, "onHiddenChanged : " + hidden
                + " ---class " + this.getClass().getSimpleName());
        if (mContainerId != 0) {
            updateActionbar();
        }
    }

    public void showFragment(FragmentActivity context, int parentContainer,
                             Fragment fragment, ActionbarUpdateListener actionbarUpdateListener,
                             String enterAnim, String exitAnim) {
        mContainerId = parentContainer;
        mActivityContext = context;
        mActionbarUpdateListener = actionbarUpdateListener;

        String packageName = context.getPackageName();
        mEnterAnimation = context.getResources().getIdentifier(enterAnim,
                "anim", packageName);
        mExitAnimation = context.getResources().getIdentifier(exitAnim, "anim",
                packageName);

        try {
            FragmentTransaction fragmentTransaction = context
                    .getSupportFragmentManager().beginTransaction();
            if (mEnterAnimation != 0 && mExitAnimation != 0) {
                fragmentTransaction.setCustomAnimations(mEnterAnimation,
                        mExitAnimation, mEnterAnimation, mExitAnimation);
            }
            fragmentTransaction.replace(mContainerId, fragment, DigitalCareConstants.DIGITALCARE_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            DigiCareLogger.e(TAG, e.getMessage());
        }
    }

    protected boolean backstackFragment() {
        if(fragmentManager == null && mActivityContext != null) {
            fragmentManager = mActivityContext.getSupportFragmentManager();
        }else if(fragmentManager == null)
        {
            fragmentManager = mFragmentActivityContext.getSupportFragmentManager();
        }
        // if (fragmentManager.getBackStackEntryCount() == 2) {
        // fragmentManager.popBackStack();
        // removeCurrentFragment();
        // } else {
        fragmentManager.popBackStack();
        // removeCurrentFragment();
        // }
        return false;
    }

    protected void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onNetworkStateChanged(boolean connection) {
        setStatus(connection);
    }

    protected final void updateUI(Runnable runnable) {
        if (Thread.currentThread() != mUiThread) {
            mHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    /**
     * Updating action bar title. The text has to be updated at each fragment
     * seletion/creation.
     */
    private void setActionbarTitle() {
        if (mContainerId == 0) {
            ((DigitalCareFontTextView) getActivity().findViewById(
                    R.id.action_bar_title)).setText(getActionbarTitle());
        } else {
            updateActionbar();
        }
    }

    private void updateActionbar() {
        if (this.getClass().getSimpleName()
                .equalsIgnoreCase(SupportHomeFragment.class.getSimpleName())) {
            mActionbarUpdateListener.updateActionbar(getActionbarTitle(), true);
        } else {
            mActionbarUpdateListener.updateActionbar(getActionbarTitle(), false);
        }
    }

    protected String getPreviousName() {
        return mPreviousPageName;
    }
}
