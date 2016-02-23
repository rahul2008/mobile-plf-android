package com.philips.productselection;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.productselection.base.ProductSelectionActivity;
import com.philips.productselection.base.ProductModelSelectionType;
import com.philips.productselection.listeners.ProductModelSelectionListener;
import com.philips.productselection.welcomefragment.WelcomeScreenFragmentSelection;
import com.philips.productselection.component.ActivityLauncher;
import com.philips.productselection.component.UiLauncher;
import com.philips.productselection.listeners.ActionbarUpdateListener;
import com.philips.productselection.utils.Constants;
import com.philips.productselection.utils.ProductSelectionLogger;

import java.util.Locale;


public class ProductModelSelectionHelper {

    private static final String TAG = ProductModelSelectionHelper.class.getSimpleName();
    private static ProductModelSelectionHelper mDigitalCareInstance = null;
    private static String[] mCtnList;
    private static Context mContext = null;
    private static Locale mLocale = null;
    private static String mCtn = null;
    private static String mSectorCode = null;
    private static String mCatalogCode = null;
    private ProductModelSelectionListener mProductSelectionListener = null;
    private SummaryModel mUserSelectedProduct = null;
    private boolean isActivityInstance;
    private int mPortraitTablet= 0;

    public static ProductModelSelectionType mProductModelSelectionType = null;

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

        if (productModelSelectionType != null) {
            mProductModelSelectionType = productModelSelectionType;
            mCatalogCode = mProductModelSelectionType.getCatalog();
            mSectorCode = mProductModelSelectionType.getSector();
            mCtnList = productModelSelectionType.getHardCodedProductList();
        } else
            throw new IllegalArgumentException("Please make sure to set the valid ProductModelSelectionType object");
        if (uiLauncher instanceof ActivityLauncher) {
            isActivityInstance = true;
            invokeAsActivity(uiLauncher.getEnterAnimation(), uiLauncher.getExitAnimation(), uiLauncher.getScreenOrientation());
        } else
            invokeAsFragment(uiLauncher.getFragmentActivity(), uiLauncher.getLayoutResourceID(),
                    uiLauncher.getActionbarUpdateListener(), uiLauncher.getEnterAnimation(), uiLauncher.getExitAnimation());

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

    private void invokeAsActivity(int startAnimation, int endAnimation, ActivityOrientation orientation) {
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

    public String getSectorCode() {
        return mSectorCode;
    }

    public void setSectorCode(String sectorCode) {
        mSectorCode = sectorCode;
    }

    public String getCatalogCode() {
        return mCatalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        mCatalogCode = catalogCode;
    }

    public String[] getProductCtnList() {
        return mCtnList;
    }

    /**
     * These are Flags used for setting/controlling screen orientation.
     * <p>This method helps only you are using the DigitalCare component from the Intent</p>
     * <p/>
     * <p> <b>Note : </b> The flags are similar to deafult android screen orientation flags</p>
     */
    public enum ActivityOrientation {


        SCREEN_ORIENTATION_UNSPECIFIED(-1), SCREEN_ORIENTATION_LANDSCAPE(0),
        SCREEN_ORIENTATION_PORTRAIT(1), SCREEN_ORIENTATION_USER(2), SCREEN_ORIENTATION_BEHIND(3),
        SCREEN_ORIENTATION_SENSOR(4),
        SCREEN_ORIENTATION_NOSENSOR(5),
        SCREEN_ORIENTATION_SENSOR_LANDSCAPE(6),
        SCREEN_ORIENTATION_SENSOR_PORTRAIT(7),
        SCREEN_ORIENTATION_REVERSE_LANDSCAPE(8),
        SCREEN_ORIENTATION_REVERSE_PORTRAIT(9),
        SCREEN_ORIENTATION_FULL_SENSOR(10),
        SCREEN_ORIENTATION_USER_LANDSCAPE(11),
        SCREEN_ORIENTATION_USER_PORTRAIT(12),
        SCREEN_ORIENTATION_FULL_USER(13),
        SCREEN_ORIENTATION_LOCKED(14);
        private int value;

        ActivityOrientation(int value) {
            this.value = value;
        }

        public int getOrientationValue() {
            return value;
        }
    }

    public SummaryModel getUserSelectedProduct() {
        return mUserSelectedProduct;
    }

    public void setUserSelectedProduct(SummaryModel summaryModel) {
        mUserSelectedProduct = summaryModel;
    }

    public boolean isActivityInstance() {
        return isActivityInstance;
    }


}
