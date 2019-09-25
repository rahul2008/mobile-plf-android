package com.philips.cdp.productselection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import com.philips.cdp.productselection.activity.ProductSelectionActivity;
import com.philips.cdp.productselection.fragments.listfragment.ProductSelectionListingFragment;
import com.philips.cdp.productselection.fragments.welcomefragment.WelcomeScreenFragmentSelection;
import com.philips.cdp.productselection.listeners.ProductSelectionListener;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.cdp.productselection.prx.PrxWrapper;
import com.philips.cdp.productselection.prx.SummaryDataListener;
import com.philips.cdp.productselection.utils.Constants;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import java.util.List;

public class ProductModelSelectionHelper {

    private static final String TAG = ProductModelSelectionHelper.class.getSimpleName();
    private static ProductModelSelectionHelper mProductModelSelectionHelper = null;
    private Context mContext = null;
    //private static Locale mLocale = null;
    private ProductSelectionListener mProductSelectionListener = null;
    private UiLauncher mLauncherType = null;
    private ProductModelSelectionType mProductModelSelectionType = null;
    private AppInfraInterface mAppInfraInterface;
    private static boolean isTabletLandscape = false;
    private static Configuration mVerticalOrientation = null;
    private static int DLS_THEME;
    private static ThemeConfiguration themeConfiguration;

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
    public void initialize(Context applicationContext, AppInfraInterface appInfraInterface) {
        if (mContext == null) {
            mContext = applicationContext;
        }
        mAppInfraInterface = appInfraInterface;
    }

    public AppInfraInterface getAPPInfraInstance() {
        return mAppInfraInterface;
    }

    public AppTaggingInterface getTaggingInterface() {
        AppTaggingInterface taggingInterface =
                getAPPInfraInstance().getTagging().createInstanceForComponent
                        (Constants.COMPONENT_NAME_PS, com.philips.cdp.productselection.BuildConfig.VERSION_NAME);
        taggingInterface.setPreviousPage(Constants.PAGE_DIGITALCARE_HOME_SCREEN);
        return taggingInterface;
    }

    public LoggingInterface getLoggerInterface() {

        LoggingInterface loggingInterface = null;
        AppInfraInterface appInfraInstance = getAPPInfraInstance();
        if (appInfraInstance != null) {
            loggingInterface = appInfraInstance.getLogging().
                    createInstanceForComponent(Constants.COMPONENT_NAME_PS, com.philips.cdp.productselection.BuildConfig.VERSION_NAME);
            return loggingInterface;
        }
        return loggingInterface;
    }


    public void invokeProductSelection(final UiLauncher uiLauncher, final ProductModelSelectionType productModelSelectionType) {

        if (uiLauncher == null || productModelSelectionType == null) {
            throw new IllegalArgumentException("Please make sure to set the valid parameters before you invoke");
        }

        if(uiLauncher instanceof  ActivityLauncher) {
            themeConfiguration = ((ActivityLauncher) uiLauncher).getDlsThemeConfiguration();
            DLS_THEME = ((ActivityLauncher) uiLauncher).getUiKitTheme();
        }

        PrxWrapper prxWrapperCode = new PrxWrapper(mContext, mAppInfraInterface, null,
                productModelSelectionType.getSector(),
                productModelSelectionType.getCatalog());

        prxWrapperCode.requestPrxSummaryList(new SummaryDataListener() {
            @Override
            public void onSuccess(List<SummaryModel> summaryModels) {
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
                                fragmentLauncher.getActionbarListener(), uiLauncher.getEnterAnimation(), uiLauncher.getExitAnimation());
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
                                  ActionBarListener actionbarUpdateListener, int enterAnim,
                                  int exitAnim) {
        if (mContext == null) {
            throw new RuntimeException("Please initialise context, locale before component invocation");
        }
        SharedPreferences prefs = context.getSharedPreferences(
                "user_product", Context.MODE_PRIVATE);
        String storedCtn = prefs.getString("mCtnFromPreference", "");
        if (storedCtn.isEmpty()) {
            WelcomeScreenFragmentSelection welcomeScreenFragment = new WelcomeScreenFragmentSelection();
            welcomeScreenFragment.showFragment(context, parentContainerResId, welcomeScreenFragment,
                    actionbarUpdateListener, enterAnim, exitAnim);
        } else {
            ProductSelectionListingFragment productselectionListingFragment = new ProductSelectionListingFragment();
            productselectionListingFragment.showFragment(context, parentContainerResId, productselectionListingFragment,
                    actionbarUpdateListener, enterAnim, exitAnim);
        }
    }

    private void invokeAsActivity(int startAnimation, int endAnimation, ActivityLauncher.ActivityOrientation orientation) {
        if (mContext == null) {
            throw new RuntimeException("Please initialise context before component invocation");
        }
        Intent intent = new Intent(this.getContext(), ProductSelectionActivity.class);
        intent.putExtra(Constants.START_ANIMATION_ID, startAnimation);
        intent.putExtra(Constants.STOP_ANIMATION_ID, endAnimation);
        intent.putExtra(Constants.SCREEN_ORIENTATION, orientation.getOrientationValue());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    public ProductSelectionListener getProductSelectionListener() {
        return this.mProductSelectionListener;
    }

    public void setProductSelectionListener(ProductSelectionListener mProductListener) {
        this.mProductSelectionListener = mProductListener;
    }

    public boolean isLaunchedAsActivity() {
        return mLauncherType instanceof ActivityLauncher;
    }

    public void setCurrentOrientation(Configuration config) {
        mVerticalOrientation = config;
    }

    public String getProductSelectionLibVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public UiLauncher getLauncherType() {
        return mLauncherType;
    }

    public ThemeConfiguration getThemeConfiguration() {
        return themeConfiguration;
    }

    public int getDlsTheme(){
        return DLS_THEME;
    }

}