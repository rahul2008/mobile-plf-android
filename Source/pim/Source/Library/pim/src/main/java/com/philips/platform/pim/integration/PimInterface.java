package com.philips.platform.pim.integration;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.pim.PimActivity;
import com.philips.platform.pim.configration.PimDataProvider;
import com.philips.platform.pim.fragment.PimFragment;
import com.philips.platform.pim.injection.PimComponent;
import com.philips.platform.pim.manager.PimConfigManager;
import com.philips.platform.pim.utilities.PimConstants;
import com.philips.platform.pim.utilities.PimLog;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;


public class PimInterface implements UappInterface {

    private String TAG = PimInterface.class.getSimpleName();
    private PimComponent component;
    private Context context;

    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
//        component = initDaggerComponents(uappDependencies, uappSettings);
        context = uappSettings.getContext();
        //PimConfiguration.getInstance().setComponent(component);
//        PimLog.init();
//        PimTagging.init();
        PimConfigManager pimConfigManager = new PimConfigManager(uappDependencies.getAppInfra());
        pimConfigManager.downloadSDServiceURLSWithCompletion(uappDependencies.getAppInfra().getServiceDiscovery());
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
        PimFragment udiFragment = new PimFragment();
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
        intent.putExtra(PimConstants.PIM_KEY_ACTIVITY_THEME, uiLauncher.getUiKitTheme());
        uiLauncher.getActivityContext().startActivity(intent);
    }

//    @NonNull
//    private PimComponent initDaggerComponents(UappDependencies uappDependencies, UappSettings uappSettings) {
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
            PimLog.d(TAG, "getUserDataInterface: Context is null");
            return null;
        }
        return new PimDataProvider(context);
    }
}
