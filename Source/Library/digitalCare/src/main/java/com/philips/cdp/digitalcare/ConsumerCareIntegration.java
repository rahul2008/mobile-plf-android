package com.philips.cdp.digitalcare;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.productselection.launchertype.FragmentLauncher;
import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;
import com.philips.cdp.productselection.productselectiontype.HardcodedProductList;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.UappListener;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

/**
 * Created by 310190678 on 8/16/2016.
 */
public class ConsumerCareIntegration implements UappInterface {


    private static final String TAG = ConsumerCareIntegration.class.getSimpleName();


    private ActionbarUpdateListener actionBarClickListener = new ActionbarUpdateListener() {

        @Override
        public void updateActionbar(String titleActionbar, Boolean hamburgerIconAvailable) {
            if (hamburgerIconAvailable) {
                // enableActionBarHome();
            } else {
                //  enableActionBarLeftArrow();
            }
        }
    };

    @Override
    public void init(Context context, UappDependencies uappDependencies) {

        DigitalCareConfigManager.getInstance().initializeDigitalCareLibrary(context);

    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput, UappListener uappListener) {
        HardcodedProductList productList = (HardcodedProductList) uappLaunchInput;

        if (uiLauncher instanceof com.philips.platform.uappframework.launcher.ActivityLauncher) {

            DigiCareLogger.i(TAG, "Activitylauncher Instance");

            ActivityLauncher.ActivityOrientation orientation = ((ActivityLauncher) uiLauncher).getScreenOrientation();
            int orientationvalue = orientation.getOrientationValue();
            int enterAnimation = uiLauncher.getEnterAnimation();
            int exitAnimation = uiLauncher.getExitAnimation();
            int uiKitTheme = ((ActivityLauncher) uiLauncher).getUiKitTheme();

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
