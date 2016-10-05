package com.philips.cdp.digitalcare;

import android.support.v4.app.FragmentActivity;

import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.productselection.launchertype.FragmentLauncher;
import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;
import com.philips.cdp.productselection.productselectiontype.HardcodedProductList;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * Created by 310190678 on 8/16/2016.
 */
public class CcInterface implements UappInterface {


    private static final String TAG = CcInterface.class.getSimpleName();

    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        CcSettings ccSettings = (CcSettings) uappSettings;
        CcDependencies ccDependencies = (CcDependencies) uappDependencies;
        DigitalCareConfigManager.getInstance().initializeDigitalCareLibrary(ccSettings.getContext()
                , ccDependencies.getAppInfra());
    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {

        CcLaunchInput ccLaunchInput = (CcLaunchInput) uappLaunchInput;

        HardcodedProductList productList = (HardcodedProductList)
                ccLaunchInput.getProductModelSelectionType();
        DigitalCareConfigManager.getInstance().registerCcListener(ccLaunchInput.getConsumerCareListener());

        if (uiLauncher instanceof com.philips.platform.uappframework.launcher.ActivityLauncher) {

            DigiCareLogger.i(TAG, "Activitylauncher Instance");

            ActivityLauncher.ActivityOrientation orientation = ((ActivityLauncher) uiLauncher).getScreenOrientation();
            int orientationvalue = orientation.getOrientationValue();
            int enterAnimation = uiLauncher.getEnterAnimation();
            int exitAnimation = uiLauncher.getExitAnimation();
            int uiKitTheme = ((ActivityLauncher) uiLauncher).getUiKitTheme();

            // TODO: Remove local ACTivity and fragment launcher, orientaion needs to be passed as is from uAPP
            com.philips.cdp.productselection.launchertype.ActivityLauncher consumerCarelauncher =
                    new com.philips.cdp.productselection.launchertype.ActivityLauncher(
                            com.philips.cdp.productselection.launchertype.ActivityLauncher.
                                    ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                            uiKitTheme);
            consumerCarelauncher.setAnimation(enterAnimation, exitAnimation);


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
            FragmentLauncher fragLauncher = new FragmentLauncher(
                    fragmentActivity, containerViewId, new ActionbarUpdateListener() {
                @Override
                public void updateActionbar(String s, Boolean hamburger) {
                    actionBarListener.updateActionBar(s, hamburger);
                }
            });
            fragLauncher.setAnimation(enterAnimation, exitAnimation);


            DigitalCareConfigManager.getInstance().invokeDigitalCare(fragLauncher,
                    productList);
        }

    }


}
