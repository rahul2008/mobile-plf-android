/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.appframework;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cdp.appframework.utility.Constants;
import com.philips.cdp.appframework.utility.Logger;
import com.philips.cdp.appframework.utility.NetworkReceiver;
import com.philips.cdp.appframework.utility.NetworkStateListener;

/**
 * AppFrameworkBaseFragment is the <b>Base class</b> for all fragments.
 *
 * @author: ritesh.jha@philips.com
 * @since: June 17, 2016
 */
public abstract class AppFrameworkBaseFragment extends Fragment implements
        NetworkStateListener {

    private static String TAG = AppFrameworkBaseFragment.class.getSimpleName();
    private static boolean isConnectionAvailable;
    private static int mContainerId = 0;
    private static String mPreviousPageName = null;
    private static int mEnterAnimation = 0;
    private static int mExitAnimation = 0;
    private static FragmentActivity mFragmentActivityContext = null;
    private static FragmentActivity mActivityContext = null;
    private static String FRAGMENT_TAG_NAME = "productselection";
    private static Boolean mListViewRequired = true;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    protected SharedPreferences prefs = null;
    protected int mLeftRightMarginPort = 0;
    protected int mLeftRightMarginLand = 0;
    private NetworkReceiver mNetworkutility = null;
    private FragmentManager fragmentManager = null;
    private Thread mUiThread = Looper.getMainLooper().getThread();
    private TextView mActionBarTitle = null;

    public synchronized static void setStatus(boolean connection) {
        isConnectionAvailable = connection;
    }

//    public abstract void setViewParams(Configuration config);

    public abstract String getActionbarTitle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        Logger.i(Constants.FRAGMENT, TAG + " : onCreate ");
        mFragmentActivityContext = getActivity();
//        registerNetWorkReceiver();
    }


//    private void registerNetWorkReceiver() {
//        IntentFilter filter = new IntentFilter(
//                "android.net.conn.CONNECTIVITY_CHANGE");
//        mNetworkutility = new NetworkReceiver(this);
//        getActivity().registerReceiver(mNetworkutility, filter);
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.i(Constants.FRAGMENT, " : onActivityCreated ");
//        mLeftRightMarginPort = (int) mFragmentActivityContext.getResources()
//                .getDimension(R.dimen.activity_margin_port);
//        mLeftRightMarginLand = (int) mFragmentActivityContext.getResources()
//                .getDimension(R.dimen.activity_margin_land);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.i(Constants.FRAGMENT, "OnCreateView on "
                + this.getClass().getSimpleName());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        Logger.i(Constants.FRAGMENT, "OnStart on "
                + this.getClass().getSimpleName());
        super.onStart();
    }

    @Override
    public void onResume() {
        Logger.i(Constants.FRAGMENT, "OnResume on "
                + this.getClass().getSimpleName());
        super.onResume();
        setActionbarTitle();
    }

    @Override
    public void onPause() {
        Logger.i(Constants.FRAGMENT, "OnPause on "
                + this.getClass().getSimpleName());
        super.onPause();
    }

    @Override
    public void onStop() {
        Logger.i(Constants.FRAGMENT, "OnStop on "
                + this.getClass().getSimpleName());
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Logger.i(Constants.FRAGMENT, "onDestroy on "
                + this.getClass().getSimpleName());
//        getActivity().unregisterReceiver(mNetworkutility);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Logger.i(Constants.FRAGMENT, "OnDestroyView on "
                + this.getClass().getSimpleName());
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

//    protected boolean isConnectionAvailable() {
//        if (isConnectionAvailable)
//            return true;
//        else {
//            // new NetworkAlertView().showNetworkAlert(getActivity());
//            mHandler.postAtFrontOfQueue(new Runnable() {
//
//                @Override
//                public void run() {
//                    new NetworkAlertView().showAlertBox(
//                            getActivity(),
//                            null,
//                            getActivity().getResources().getString(
//                                    R.string.No_Internet),
//                            getActivity().getResources().getString(
//                                    android.R.string.yes));
//
//
//                }
//            });
//            return false;
//        }
//    }

//    protected boolean isTablet() {
//        DisplayMetrics metrics = new DisplayMetrics();
//        try {
//            if (getActivity().getWindowManager() != null)
//                getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        } catch (NullPointerException e) {
//            Logger.e(TAG, "V4 library issue catch ");
//        } finally {
//            float yInches = metrics.heightPixels / metrics.ydpi;
//            float xInches = metrics.widthPixels / metrics.xdpi;
//            double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
//            return diagonalInches >= 6.5;
//        }
//    }

//    protected void showAlert(final String message) {
//        mHandler.post(new Runnable() {
//
//            @Override
//            public void run() {
//                new NetworkAlertView().showAlertBox(
//                        getActivity(),
//                        null,
//                        message,
//                        getActivity().getResources().getString(
//                                android.R.string.yes));
//            }
//        });
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Logger.i(Constants.FRAGMENT, " : onConfigurationChanged ");
        // setLocaleLanguage();
    }

	/*
     * This method can be called directly from outside and helps to invoke the
	 * fragments, instead of full screen mActivity. DigitalCare fragments will be
	 * added in the root container of hosting app. Integrating app has to pass
	 * some parameters in order to do smooth operations.
	 */

//    public void removeFragmentByTag(String tag) {
//        try {
//            FragmentTransaction fragmentTransaction = mFragmentActivityContext
//                    .getSupportFragmentManager().beginTransaction();
//            Fragment fragmentDetailsTablet = mFragmentActivityContext.getSupportFragmentManager().findFragmentByTag(tag);
//            fragmentTransaction.remove(fragmentDetailsTablet).commit();
//        } catch (IllegalStateException e) {
//            Logger.e(Constants.FRAGMENT, "IllegalStateException" + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Logger.d(Constants.FRAGMENT, "onHiddenChanged : " + hidden
                + " ---class " + this.getClass().getSimpleName());
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
     * selection/creation.
     */
    private void setActionbarTitle() {
        if (mActionBarTitle == null) {
            mActionBarTitle = (TextView) getActivity().findViewById(R.id.hamburger_title);
        }
        String titleText = null;
        if (getActionbarTitle() == null) {
            titleText = getResources().getString(R.string.app_name);
        } else {
            titleText = getActionbarTitle();
        }
        mActionBarTitle.setText(titleText);
    }
}
