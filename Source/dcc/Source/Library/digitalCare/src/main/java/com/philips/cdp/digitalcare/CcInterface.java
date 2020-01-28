package com.philips.cdp.digitalcare;

import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentActivity;

import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.productselection.productselectiontype.HardcodedProductList;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

/**
 * Interface class for initiating and launching the consumer care library from vertical app
 * <p>
 * Created by sampath.kumar on 8/16/2016.
 */
@SuppressWarnings("serial")
public class CcInterface implements UappInterface {


    private static final String TAG = CcInterface.class.getSimpleName();

    /**
     * initialise the consumer care library
     *
     * @param uappDependencies
     * @param uappSettings
     * @since 1.0.0
     */
    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        CcSettings ccSettings = (CcSettings) uappSettings;
        CcDependencies ccDependencies = (CcDependencies) uappDependencies;

        DigitalCareConfigManager.getInstance().initializeDigitalCareLibrary(ccSettings.getContext()
                , ccDependencies.getAppInfra());
    }

    /**
     * launch the support screen through fragment or activity
     *
     * @param uiLauncher
     * @param uappLaunchInput
     * @since 1.0.0
     */
    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {

        CcLaunchInput ccLaunchInput = (CcLaunchInput) uappLaunchInput;

        HardcodedProductList productList = (HardcodedProductList)
                ccLaunchInput.getProductModelSelectionType();
        DigitalCareConfigManager.getInstance().registerCcListener(ccLaunchInput.getConsumerCareListener());
        DigitalCareConfigManager.getInstance().setLiveChatUrl(ccLaunchInput.getLiveChatUrl());


        if (uiLauncher instanceof com.philips.platform.uappframework.launcher.ActivityLauncher) {

            DigiCareLogger.i(TAG, "Activitylauncher Instance");


            ActivityLauncher.ActivityOrientation orientation = ((ActivityLauncher) uiLauncher).getScreenOrientation();
            int orientationvalue = orientation.getOrientationValue();
            int enterAnimation = uiLauncher.getEnterAnimation();
            int exitAnimation = uiLauncher.getExitAnimation();
            int uiKitTheme = ((ActivityLauncher) uiLauncher).getUiKitTheme();
            ThemeConfiguration themeConfiguration = ((ActivityLauncher) uiLauncher).getDlsThemeConfiguration();
            //int DLS_THEME = ((ActivityLauncher) uiLauncher).getUiKitTheme();


            // TODO: Remove local ACTivity and fragment launcher, orientaion needs to be passed as is from uAPP
            ActivityLauncher consumerCarelauncher =
                    new ActivityLauncher(((ActivityLauncher) uiLauncher).getActivityContext(), ActivityLauncher.
                            ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, themeConfiguration, uiKitTheme,
                            null);
            consumerCarelauncher.setCustomAnimation(enterAnimation, exitAnimation);


            DigitalCareConfigManager.getInstance().invokeDigitalCare(consumerCarelauncher,
                    productList);


        } else {

            DigiCareLogger.i(TAG, "FragmentLauncher Instance");

            com.philips.platform.uappframework.launcher.FragmentLauncher fragmentLauncher
                    = (com.philips.platform.uappframework.launcher.FragmentLauncher) uiLauncher;

            FragmentActivity fragmentActivity = fragmentLauncher.getFragmentActivity();
            int containerViewId = fragmentLauncher.getParentContainerResourceID();
            int enterAnimation = fragmentLauncher.getEnterAnimation();
            int exitAnimation = fragmentLauncher.getExitAnimation();
            final ActionBarListener actionBarListener = fragmentLauncher.getActionbarListener();


            // TODO: Use actionbar listener provided by uAPp and remove local copy.
            @SuppressWarnings("serial")
            FragmentLauncher fragLauncher = new FragmentLauncher(
                    fragmentActivity, containerViewId, new ActionBarListener() {
                @Override
                public void updateActionBar(@StringRes int i, boolean b) {
                    actionBarListener.updateActionBar(i, b);
                }

                @Override
                public void updateActionBar(String s, boolean hamburger) {
                    actionBarListener.updateActionBar(s, hamburger);
                }
            });

            /*FragmentLauncher fragLauncher = new FragmentLauncher(
                    fragmentActivity, containerViewId, new ActionBarListener() {
                @Override
                public void updateActionbar(String s, Boolean hamburger) {
                    actionBarListener.updateActionBar(s, hamburger);
                }
            });*/
            fragLauncher.setCustomAnimation(enterAnimation, exitAnimation);


            DigitalCareConfigManager.getInstance().invokeDigitalCare(fragLauncher,
                    productList);
        }

    }


}
