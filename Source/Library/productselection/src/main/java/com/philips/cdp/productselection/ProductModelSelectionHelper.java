package com.philips.cdp.productselection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v7.appcompat.BuildConfig;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.philips.cdp.productselection.activity.ProductSelectionActivity;
import com.philips.cdp.productselection.fragments.listfragment.ProductSelectionListingFragment;
import com.philips.cdp.productselection.fragments.welcomefragment.WelcomeScreenFragmentSelection;
import com.philips.cdp.productselection.launchertype.ActivityLauncher;
import com.philips.cdp.productselection.launchertype.FragmentLauncher;
import com.philips.cdp.productselection.launchertype.UiLauncher;
import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;
import com.philips.cdp.productselection.listeners.ProductSelectionListener;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.cdp.productselection.prx.PrxWrapper;
import com.philips.cdp.productselection.prx.SummaryDataListener;
import com.philips.cdp.productselection.utils.Constants;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.tagging.Tagging;

import java.util.List;
import java.util.Locale;


public class ProductModelSelectionHelper {

    private static final String TAG = ProductModelSelectionHelper.class.getSimpleName();
    private static ProductModelSelectionHelper mProductModelSelectionHelper = null;
    private static Context mContext = null;
    private static Locale mLocale = null;
    private static boolean isTabletLandscape = false;
    private static Configuration mVerticalOrientation = null;
    private ProductSelectionListener mProductSelectionListener = null;
    private UiLauncher mLauncherType = null;
    private ProductModelSelectionType mProductModelSelectionType = null;
    private ProgressDialog mProgressDialog = null;

    /*
     * Initialize everything(resources, variables etc) required for product selection.
     * Hosting app, which will integrate this product selection, has to pass app
     * context.
     */
    private ProductModelSelectionHelper() {
    }

    /*
     * Singleton pattern.
     */
    public static ProductModelSelectionHelper getInstance() {
        if (mProductModelSelectionHelper == null) {
            mProductModelSelectionHelper = new ProductModelSelectionHelper();
        }
        return mProductModelSelectionHelper;
    }

    public ProductModelSelectionType getProductModelSelectionType()

    {
        return mProductModelSelectionType;
    }

    public Locale getLocale() {
        return mLocale;
    }


    /**
     * Returns the Context used in the product selection Component
     *
     * @return Returns the Context using by the Component.
     */
    public Context getContext() {
        return mContext;
    }


    /**
     * <p>This is the product selection initialization method. Please make sure to call this method before invoking the product selection.
     * For more help/details please make sure to have a glance at the Demo sample </p>
     *
     * @param applicationContext Please pass the valid  Context
     */
    public void initialize(Context applicationContext) {
        if (mContext == null) {
            ProductModelSelectionHelper.mContext = applicationContext;
        }
    }

    public void initializeTagging(Boolean taggingEnabled, String appName, String appId, String launchingPage) {
        Tagging.enableAppTagging(taggingEnabled);
        Tagging.setTrackingIdentifier(appId);
        Tagging.setComponentVersionKey(Constants.ATTRIBUTE_KEY_PRODUCT_SELECTION);
        Tagging.setComponentVersionVersionValue(String.valueOf(BuildConfig.VERSION_NAME));
        Tagging.setLaunchingPageName(launchingPage);

        Tagging.init(getContext(), appName);
    }

    public UiLauncher getLauncherType() {
        return mLauncherType;
    }

    public void invokeProductSelection(final UiLauncher uiLauncher, final ProductModelSelectionType productModelSelectionType) {
        if (uiLauncher == null || productModelSelectionType == null) {
            throw new IllegalArgumentException("Please make sure to set the valid parameters before you invoke");
        }

        final Activity mActivity = (Activity) mContext;
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(mActivity, R.style.loaderTheme);
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        mProgressDialog.setCancelable(false);
        if (!(mActivity.isFinishing()) && (mProgressDialog != null)) {

            try {
                mProgressDialog.show();
            } catch (WindowManager.BadTokenException e) {
                ProductSelectionLogger.e(TAG, "WindowManager Exception Handled : " + e);
            }
        }


        PrxWrapper prxWrapperCode = new PrxWrapper(mContext, null,
                productModelSelectionType.getSector(),
                getLocale().toString(),
                productModelSelectionType.getCatalog());

        prxWrapperCode.requestPrxSummaryList(new SummaryDataListener() {
            @Override
            public void onSuccess(List<SummaryModel> summaryModels) {
                if (mProgressDialog != null) {
                    if (mProgressDialog.isShowing() && !mActivity.isFinishing()) {
                        try {
                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                        } catch (IllegalArgumentException e) {
                            ProductSelectionLogger.e(TAG, "Progress Dialog Exception " + e);
                        }
                    } else if (mProgressDialog.isShowing()) {

                        try {
                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                        } catch (IllegalArgumentException e) {
                            ProductSelectionLogger.e(TAG, "Progress Dialog Exception " + e);
                        }
                    }
                }
                if (summaryModels.size() >= 1) {
                    SummaryModel[] ctnArray = new SummaryModel[summaryModels.size()];
                    for (int i = 0; i < summaryModels.size(); i++)
                        ctnArray[i] = summaryModels.get(i);
                    productModelSelectionType.setProductModelList(ctnArray);
                    if (uiLauncher instanceof ActivityLauncher) {
                        ActivityLauncher activityLauncher = (ActivityLauncher) uiLauncher;
                        invokeAsActivity(uiLauncher.getEnterAnimation(), uiLauncher.getExitAnimation(), activityLauncher.getScreenOrientation());
                    } else if (uiLauncher instanceof FragmentLauncher) {
                        FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
                        invokeAsFragment(fragmentLauncher.getFragmentActivity(), fragmentLauncher.getParentContainerResourceID(),
                                fragmentLauncher.getActionbarUpdateListener(), uiLauncher.getEnterAnimation(), uiLauncher.getExitAnimation());
                    }
                } else {
                    if (mProductSelectionListener != null)
                        mProductSelectionListener.onProductModelSelected(null);
                }
            }
        }, productModelSelectionType.getHardCodedProductList(), null);

        mLauncherType = uiLauncher;
        mProductModelSelectionType = productModelSelectionType;


    }

    private void invokeAsFragment(FragmentActivity context,
                                  int parentContainerResId,
                                  ActionbarUpdateListener actionbarUpdateListener, int enterAnim,
                                  int exitAnim) {
        if (mContext == null || mLocale == null) {
            throw new RuntimeException("Please initialise context, locale before component invocation");
        }
        SharedPreferences prefs = context.getSharedPreferences(
                "user_product", Context.MODE_PRIVATE);
        String storedCtn = prefs.getString("mCtnFromPreference", "");
        if (storedCtn == "") {
            WelcomeScreenFragmentSelection welcomeScreenFragment = new WelcomeScreenFragmentSelection();
            welcomeScreenFragment.showFragment(context, parentContainerResId, welcomeScreenFragment,
                    actionbarUpdateListener, enterAnim, exitAnim);
        } else {
            ProductSelectionListingFragment productselectionListingFragment = new ProductSelectionListingFragment();
            productselectionListingFragment.showFragment(context, parentContainerResId, productselectionListingFragment,
                    actionbarUpdateListener, enterAnim, exitAnim);
        }
    }

    private boolean isTablet(FragmentActivity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        try {
            if (context.getWindowManager() != null)
                context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        } catch (NullPointerException e) {
            ProductSelectionLogger.e(TAG, "V4 library issue catch ");
        } finally {
            float yInches = metrics.heightPixels / metrics.ydpi;
            float xInches = metrics.widthPixels / metrics.xdpi;
            double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
            return diagonalInches >= 6.5;
        }
    }

    /*  Thsi is required to set, in order to achieve proper GUI for tablet. */
    public void setCurrentOrientation(Configuration config) {
        mVerticalOrientation = config;
    }

    private void invokeAsActivity(int startAnimation, int endAnimation, ActivityLauncher.ActivityOrientation orientation) {
        if (mContext == null || mLocale == null) {
            throw new RuntimeException("Please initialise context, locale before component invocation");
        }
        Intent intent = new Intent(this.getContext(), ProductSelectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.START_ANIMATION_ID, startAnimation);
        intent.putExtra(Constants.STOP_ANIMATION_ID, endAnimation);
        intent.putExtra(Constants.SCREEN_ORIENTATION, orientation.getOrientationValue());
        getContext().startActivity(intent);
    }

    public ProductSelectionListener getProductSelectionListener() {
        return this.mProductSelectionListener;
    }

    public void setProductSelectionListener(ProductSelectionListener mProductListener) {
        this.mProductSelectionListener = mProductListener;
    }

    public void setLocale(String langCode, String countryCode) {

        if (langCode != null && countryCode != null) {
            mLocale = new Locale(langCode, countryCode);
            ProductSelectionLogger.i(TAG, "setLocale API of ProductSelection : " + mLocale.toString());
        }
    }

    public String getProductSelectionLibVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public boolean isLaunchedAsActivity() {

        return mLauncherType instanceof ActivityLauncher;
    }


}
