package com.philips.cdp.productselection;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.productselection.welcomefragment.WelcomeScreenFragmentSelection;
import com.philips.cdp.productselection.BuildConfig;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.cdp.productselection.activity.ProductSelectionActivity;
import com.philips.cdp.productselection.launcher.ActivityLauncher;
import com.philips.cdp.productselection.launcher.FragmentLauncher;
import com.philips.cdp.productselection.launcher.UiLauncher;
import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;
import com.philips.cdp.productselection.listeners.ProductModelSelectionListener;
import com.philips.cdp.productselection.utils.Constants;

import java.util.Locale;


public class ProductModelSelectionHelper {

    private static final String TAG = ProductModelSelectionHelper.class.getSimpleName();
    private static ProductModelSelectionHelper mDigitalCareInstance = null;
    private static String[] mCtnList;
    private static Context mContext = null;
    private static Locale mLocale = null;
    private static String mCtn = null;
    private ProductModelSelectionListener mProductSelectionListener = null;
    private SummaryModel mUserSelectedProduct = null;
    private UiLauncher mLaucherType = null;

    private ProductModelSelectionType mProductModelSelectionType = null;

    /*
     * Initialize everything(resources, variables etc) required for DigitalCare.
     * Hosting app, which will integrate this DigitalCare, has to pass app
     * context.
     */
    private ProductModelSelectionHelper() {
    }

    /*
     * Singleton pattern.
     */
    public static ProductModelSelectionHelper getInstance() {
        if (mDigitalCareInstance == null) {
            mDigitalCareInstance = new ProductModelSelectionHelper();
        }
        return mDigitalCareInstance;
    }

    public ProductModelSelectionType getProductModelSelectionType()

    {
        return mProductModelSelectionType;
    }

    public Locale getLocale() {
        return mLocale;
    }


    /**
     * Returs the Context used in the DigitalCare Component
     *
     * @return Returns the Context using by the Component.
     */
    public Context getContext() {
        return mContext;
    }


    /**
     * <p>This is the DigitalCare initialization method. Please make sure to call this method before invoking the DigitalComponent.
     * For more help/details please make sure to have a glance at the Demo sample </p>
     *
     * @param applicationContext Please pass the valid  Context
     */
    public void initialize(Context applicationContext) {
        if (mContext == null) {
            ProductModelSelectionHelper.mContext = applicationContext;

        }
    }


    public void invokeProductSelection(UiLauncher uiLauncher, ProductModelSelectionType productModelSelectionType) {

        mLaucherType = uiLauncher;
        if (productModelSelectionType != null) {
            mProductModelSelectionType = productModelSelectionType;
            mCtnList = productModelSelectionType.getHardCodedProductList();
        } else
            throw new IllegalArgumentException("Please make sure to set the valid ProductModelSelectionType object");
        if (uiLauncher instanceof ActivityLauncher) {
            invokeAsActivity(uiLauncher.getEnterAnimation(), uiLauncher.getExitAnimation(), uiLauncher.getScreenOrientation());
        } else {

            FragmentLauncher fragmentLauncher = new FragmentLauncher();
            invokeAsFragment(uiLauncher.getFragmentActivity(), fragmentLauncher.getLayoutResourceID(),
                    fragmentLauncher.getActionbarUpdateListener(), uiLauncher.getEnterAnimation(), uiLauncher.getExitAnimation());
        }
    }


    private void invokeAsFragment(FragmentActivity context,
                                  int parentContainerResId,
                                  ActionbarUpdateListener actionbarUpdateListener, int enterAnim,
                                  int exitAnim) {
        if (mContext == null || mLocale == null) {
            throw new RuntimeException("Please initialise context, locale and consumerproductInfo before Support page is invoked");
        }

        //TODO: Include Tagging
//        if (mTaggingEnabled) {
//            if (mAppID == null || mAppID.equals("")) {
//                throw new RuntimeException("Please make sure to set the valid AppID for Tagging.");
//            }
//        }

        //  AnalyticsTracker.setTaggingInfo(mTaggingEnabled, mAppID);

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

    public void setProductListener(ProductModelSelectionListener mProductListener) {
        this.mProductSelectionListener = mProductListener;
    }

    public ProductModelSelectionListener getProductListener() {
        return this.mProductSelectionListener;
    }


    public void setLocale(String langCode, String countryCode) {

        if (langCode != null && countryCode != null) {
            mLocale = new Locale(langCode, countryCode);
            ProductSelectionLogger.d(TAG, "Setting Locale :  : " + mLocale.toString());
        }
    }


    public String getMultiProductModuleLibVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public String getCtn() {
        return mCtn;
    }

    public void setCtn(String ctn) {
        mCtn = ctn;
    }

    public String[] getProductCtnList() {
        return mCtnList;
    }



    public SummaryModel getUserSelectedProduct() {
        return mUserSelectedProduct;
    }

    public void setUserSelectedProduct(SummaryModel summaryModel) {
        mUserSelectedProduct = summaryModel;
    }

    public boolean isActivityInstance() {

        if (mLaucherType instanceof ActivityLauncher)
            return true;
        else
            return false;
    }


}
