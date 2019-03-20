package com.philips.platform.pim.integration;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.pim.PimActivity;
import com.philips.platform.pim.configration.PIMDataProvider;
import com.philips.platform.pim.manager.PIMSettingManager;
import com.philips.platform.pim.fragment.PIMFragment;
import com.philips.platform.pim.manager.PIMConfigManager;
import com.philips.platform.pim.utilities.PIMConstants;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;


public class PIMInterface implements UappInterface {

    private String TAG = PIMInterface.class.getSimpleName();

    private Context context;
    public static AppInfra mAppInfra;

    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
//        component = initDaggerComponents(uappDependencies, uappSettings);
        context = uappSettings.getContext();
        //PIMConfiguration.getInstance().setComponent(component);
//        PIMLog.init();
//        PIMTagging.init();
        mAppInfra = (AppInfra) uappDependencies.getAppInfra();

        PIMSettingManager.getInstance().setDependencies((PIMDependencies) uappDependencies);

        new PIMConfigManager();
    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity(((ActivityLauncher) uiLauncher), uappLaunchInput);
            Log.i(TAG, "Launch : Launched as activity");
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchAsFragment((FragmentLauncher) uiLauncher, uappLaunchInput);
            Log.i(TAG, "Launch : Launched as fragment");
        }
    }

    private void launchAsFragment(FragmentLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        PIMFragment udiFragment = new PIMFragment();
        addFragment(uiLauncher, udiFragment);
    }

    private void addFragment(FragmentLauncher uiLauncher, Fragment fragment) {
        uiLauncher.getFragmentActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(uiLauncher.getParentContainerResourceID(), fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    private void launchAsActivity(ActivityLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        Intent intent = new Intent(uiLauncher.getActivityContext(), PimActivity.class);
        intent.putExtra(PIMConstants.PIM_KEY_ACTIVITY_THEME, uiLauncher.getUiKitTheme());
        uiLauncher.getActivityContext().startActivity(intent);
    }

//    @NonNull
//    private PIMComponent initDaggerComponents(UappDependencies uappDependencies, UappSettings uappSettings) {
//        return DaggerPimComponent.builder()
//                .appInfraModule(new AppInfraModule(uappDependencies.getAppInfra()))
//                .build();
//    }

    /**
     * Get the User Data Interface
     *
     * @since 2018.1.0
     */
    public UserDataInterface getUserDataInterface() {
        if (context == null) {
            Log.d(TAG, "getUserDataInterface: Context is null");
            return null;
        }
        return new PIMDataProvider(context);
    }
}
