package com.philips.multiproduct.homefragment;

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
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.multiproduct.ProductModelSelectionHelper;
import com.philips.multiproduct.R;
import com.philips.multiproduct.customview.NetworkAlertView;
import com.philips.multiproduct.listeners.ActionbarUpdateListener;
import com.philips.multiproduct.listeners.NetworkStateListener;
import com.philips.multiproduct.listeners.ProductListDetailsTabletListener;
import com.philips.multiproduct.utils.NetworkReceiver;
import com.philips.multiproduct.utils.ProductSelectionLogger;

import java.util.Locale;

public abstract class MultiProductBaseFragment extends Fragment implements
        NetworkStateListener {

    private static String TAG = MultiProductBaseFragment.class.getSimpleName();
    private static boolean isConnectionAvailable;
    private static int mContainerId = 0;
    private static ActionbarUpdateListener mActionbarUpdateListener = null;
    private static String mPreviousPageName = null;
    private static int mEnterAnimation = 0;
    private static int mExitAnimation = 0;
    private static FragmentActivity mFragmentActivityContext = null;
    private static FragmentActivity mActivityContext = null;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private NetworkReceiver mNetworkutility = null;
    private FragmentManager fragmentManager = null;
    private Thread mUiThread = Looper.getMainLooper().getThread();
    private TextView mActionBarTitle = null;
    private static ProductListDetailsTabletListener mProductListDetailsTabletListener = null;
    protected ProductListDetailsTabletListener mProductDetailsListener = null;
    protected int mLeftRightMarginPort = 0;
    protected int mLeftRightMarginLand = 0;

    public synchronized static void setStatus(boolean connection) {
        isConnectionAvailable = connection;
    }

    public abstract void setViewParams(Configuration config);

    public abstract String getActionbarTitle();

    public abstract String setPreviousPageName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ProductSelectionLogger.i(ProductSelectionLogger.FRAGMENT, "OnCreate on "
                + this.getClass().getSimpleName());
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        mFragmentActivityContext = getActivity();
        mActionBarTitle = (TextView) getActivity().findViewById(R.id.actionbarTitle);
        registerNetWorkReceiver();
        setLocaleLanguage();
    }

    private void setLocaleLanguage() {
        Locale locale = ProductModelSelectionHelper.getInstance().getLocale();
        if (locale != null) {
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            mFragmentActivityContext.getResources().updateConfiguration(config,
                    mFragmentActivityContext.getResources().getDisplayMetrics());
        }
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
        ProductSelectionLogger.i(ProductSelectionLogger.FRAGMENT, "OnCreateView on "
                + this.getClass().getSimpleName());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        ProductSelectionLogger.i(ProductSelectionLogger.FRAGMENT, "OnStart on "
                + this.getClass().getSimpleName());
        super.onStart();
    }

    @Override
    public void onResume() {
        ProductSelectionLogger.i(ProductSelectionLogger.FRAGMENT, "OnResume on "
                + this.getClass().getSimpleName());
        super.onResume();
        mActionBarTitle.setText(getActionbarTitle());
    }

    @Override
    public void onPause() {
        ProductSelectionLogger.i(ProductSelectionLogger.FRAGMENT, "OnPause on "
                + this.getClass().getSimpleName());
        super.onPause();
    }

    @Override
    public void onStop() {
        ProductSelectionLogger.i(ProductSelectionLogger.FRAGMENT, "OnStop on "
                + this.getClass().getSimpleName());
        super.onStop();
    }

    @Override
    public void onDestroy() {
        ProductSelectionLogger.i(ProductSelectionLogger.FRAGMENT, "onDestroy on "
                + this.getClass().getSimpleName());
        getActivity().unregisterReceiver(mNetworkutility);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        ProductSelectionLogger.i(ProductSelectionLogger.FRAGMENT, "OnDestroyView on "
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


                }
            });
            return false;
        }
    }


    protected void enableActionBarLeftArrow(ImageView hambergermenu, ImageView backarrow) {
        ProductSelectionLogger.d(TAG, "BackArrow Enabled");
        if (hambergermenu != null && backarrow != null) {
            backarrow.setVisibility(View.VISIBLE);
            backarrow.bringToFront();
        }
    }

    protected boolean isTablet() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
        return diagonalInches >= 6.5;
    }

    protected ProductListDetailsTabletListener getObserver(){
        if(mProductListDetailsTabletListener == null){
            mProductListDetailsTabletListener = new ProductListDetailsTabletListener(getActivity());
        }

        return mProductListDetailsTabletListener;
    }

    protected void hideActionBarIcons(ImageView hambergermenu, ImageView backarrow) {
        ProductSelectionLogger.d(TAG, "Hide menu & arrow icons");
        if (hambergermenu != null && backarrow != null) {
            hambergermenu.setVisibility(View.GONE);
            backarrow.setVisibility(View.GONE);
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


            }
        });
    }


    protected void showEULAAlert(final String message) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                new NetworkAlertView().showEULAAlertBox(
                        getActivity(),
                        // null,
                        message);
                     /*    getActivity().getResources().getString(
                                android.R.string.yes));
               AnalyticsTracker
                        .trackAction(
                                AnalyticsConstants.ACTION_SET_ERROR,
                                AnalyticsConstants.ACTION_KEY_TECHNICAL_ERROR,
                                AnalyticsConstants.ACTION_VALUE_TECHNICAL_ERROR_NETWORK_CONNECITON);*/

            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ProductSelectionLogger.i(TAG, TAG + " : onConfigurationChanged ");
        setLocaleLanguage();
        getAppName();
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
            fragmentTransaction.replace(containerId, fragment, "tagname");
            fragmentTransaction.hide(this);
            fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            ProductSelectionLogger.e(TAG, "IllegalStateException" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        ProductSelectionLogger.d(ProductSelectionLogger.FRAGMENT, "onHiddenChanged : " + hidden
                + " ---class " + this.getClass().getSimpleName());
        if (mContainerId == 0) {
//            if (this.getClass().getSimpleName()
//                    .equalsIgnoreCase(SupportHomeFragment.class.getSimpleName())) {
//
//            }
        }
    }


    public void showFragment(FragmentActivity context, int parentContainer,
                             Fragment fragment, ActionbarUpdateListener actionbarUpdateListener,
                             int startAnimation, int endAnimation) {
        mContainerId = parentContainer;
        mActivityContext = context;
        mActionbarUpdateListener = actionbarUpdateListener;

        String startAnim = null;
        String endAnim = null;

        if ((startAnimation != 0) && (endAnimation != 0)) {
            startAnim = context.getResources().getResourceName(startAnimation);
            endAnim = context.getResources().getResourceName(endAnimation);

            String packageName = context.getPackageName();
            mEnterAnimation = context.getResources().getIdentifier(startAnim,
                    "anim", packageName);
            mExitAnimation = context.getResources().getIdentifier(endAnim, "anim",
                    packageName);
        }

        try {
            FragmentTransaction fragmentTransaction = context
                    .getSupportFragmentManager().beginTransaction();
            if (mEnterAnimation != 0 && mExitAnimation != 0) {
                fragmentTransaction.setCustomAnimations(mEnterAnimation,
                        mExitAnimation, mEnterAnimation, mExitAnimation);
            }
            fragmentTransaction.replace(mContainerId, fragment, "tagname");
            fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            ProductSelectionLogger.e(TAG, e.getMessage());
        }
    }

    protected boolean backstackFragment() {
        if (fragmentManager == null && mActivityContext != null) {
            fragmentManager = mActivityContext.getSupportFragmentManager();
        } else if (fragmentManager == null) {
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

    protected boolean backstackToSupportFragment() {

        if(!ProductModelSelectionHelper.getInstance().isActivityInstance()) {
            if (fragmentManager == null && mActivityContext != null) {
                fragmentManager = mActivityContext.getSupportFragmentManager();
            } else if (fragmentManager == null) {
                fragmentManager = mFragmentActivityContext.getSupportFragmentManager();
            }
            for (int i = 0; i < fragmentManager.getFragments().size(); i++) {
                fragmentManager.popBackStack();
            }
        }else
        {

        }
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


//    private void updateActionbar() {
//        if (this.getClass().getSimpleName()
//                .equalsIgnoreCase(SupportHomeFragment.class.getSimpleName())) {
//            mActionbarUpdateListener.updateActionbar(getActionbarTitle(), true);
//        } else {
//            mActionbarUpdateListener.updateActionbar(getActionbarTitle(), false);
//        }
//    }

    protected String getPreviousName() {
        return mPreviousPageName;
    }
}
