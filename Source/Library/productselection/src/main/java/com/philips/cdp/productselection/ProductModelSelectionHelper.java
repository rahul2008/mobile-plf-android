package com.philips.cdp.productselection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.productselection.activity.ProductSelectionActivity;
import com.philips.cdp.productselection.fragments.welcomefragment.WelcomeScreenFragmentSelection;
import com.philips.cdp.productselection.launchertype.ActivityLauncher;
import com.philips.cdp.productselection.launchertype.FragmentLauncher;
import com.philips.cdp.productselection.launchertype.UiLauncher;
import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;
import com.philips.cdp.productselection.listeners.ProductModelSelectionListener;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.cdp.productselection.prx.PrxWrapper;
import com.philips.cdp.productselection.prx.SummaryDataListener;
import com.philips.cdp.productselection.utils.Constants;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;

import java.util.List;
import java.util.Locale;


public class ProductModelSelectionHelper {

    private static final String TAG = ProductModelSelectionHelper.class.getSimpleName();
    private static ProductModelSelectionHelper mProductModelSelectionHelper = null;
    private static Context mContext = null;
    private static Locale mLocale = null;
    private ProductModelSelectionListener mProductSelectionListener = null;
    private SummaryDataListener mSummaryDataListener = null;
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

    public void invokeProductSelection(final UiLauncher uiLauncher, final ProductModelSelectionType productModelSelectionType) {
        if (uiLauncher == null || productModelSelectionType == null) {
            throw new IllegalArgumentException("Please make sure to set the valid parameters before you invoke");
        }
        Activity mActivity = (Activity) mContext;
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(mActivity, R.style.loaderTheme);
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        mProgressDialog.setCancelable(false);
        if (!(mActivity.isFinishing()))
            mProgressDialog.show();


        PrxWrapper prxWrapperCode = new PrxWrapper(mContext, null,
                productModelSelectionType.getSector(),
                getLocale().toString(),
                productModelSelectionType.getCatalog());

        prxWrapperCode.requestPrxSummaryList(new SummaryDataListener() {
            @Override
            public void onSuccess(List<SummaryModel> summaryModels) {
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.cancel();
                if (summaryModels.size() > 1) {
                    SummaryModel[] ctnArray = new SummaryModel[summaryModels.size()];
                    for (int i = 0; i < summaryModels.size(); i++)
                        ctnArray[i] = summaryModels.get(i);
                    productModelSelectionType.setProductModelList(ctnArray);
                    mSummaryDataListener.onSuccess(summaryModels);

                    if (uiLauncher instanceof ActivityLauncher) {
                        ActivityLauncher activityLauncher = (ActivityLauncher) uiLauncher;
                        invokeAsActivity(uiLauncher.getEnterAnimation(), uiLauncher.getExitAnimation(), activityLauncher.getScreenOrientation());
                    } else if (uiLauncher instanceof FragmentLauncher) {
                        FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
                        invokeAsFragment(fragmentLauncher.getFragmentActivity(), fragmentLauncher.getParentContainerResourceID(),
                                fragmentLauncher.getActionbarUpdateListener(), uiLauncher.getEnterAnimation(), uiLauncher.getExitAnimation());
                    }
                } else
                    mSummaryDataListener.onSuccess(summaryModels);
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
        WelcomeScreenFragmentSelection welcomeScreenFragment = new WelcomeScreenFragmentSelection();
        welcomeScreenFragment.showFragment(context, parentContainerResId, welcomeScreenFragment,
                actionbarUpdateListener, enterAnim, exitAnim);
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

    public ProductModelSelectionListener getProductListener() {
        return this.mProductSelectionListener;
    }

    public void setProductListener(ProductModelSelectionListener mProductListener) {
        this.mProductSelectionListener = mProductListener;
    }

    public void setSummaryDataListener(SummaryDataListener summaryDataListener) {
        this.mSummaryDataListener = summaryDataListener;
    }

    public void setLocale(String langCode, String countryCode) {

        if (langCode != null && countryCode != null) {
            mLocale = new Locale(langCode, countryCode);
            ProductSelectionLogger.d(TAG, "Setting Locale :  : " + mLocale.toString());
        }
    }


    public String getProductSelectionLibVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public boolean isLaunchedAsActivity() {

        if (mLauncherType instanceof ActivityLauncher)
            return true;
        else
            return false;
    }


}
