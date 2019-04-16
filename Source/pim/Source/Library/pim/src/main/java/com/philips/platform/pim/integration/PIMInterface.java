package com.philips.platform.pim.integration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.pim.PIMActivity;
import com.philips.platform.pim.fragment.PIMFragment;
import com.philips.platform.pim.manager.PIMConfigManager;
import com.philips.platform.pim.manager.PIMSettingManager;
import com.philips.platform.pim.manager.PIMUserManager;
import com.philips.platform.pim.utilities.PIMConstants;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;


public class PIMInterface implements UappInterface {

    private final String TAG = PIMInterface.class.getSimpleName();
    private LoggingInterface mLoggingInterface;

    private Context context;

    /**
     * API to initialize PIM. Please make sure no propositions are being used before URInterface$init.
     *
     * @param uappDependencies pass instance of UappDependencies
     * @param uappSettings     pass instance of UappSettings
     * @since TODO
     */
    @Override
    public void init(@NonNull UappDependencies uappDependencies, @NonNull UappSettings uappSettings) {
        context = uappSettings.getContext();

        long startrtime = System.currentTimeMillis();

        PIMSettingManager.getInstance().init(uappDependencies);
        PIMUserManager pimUserManager = new PIMUserManager();
        PIMSettingManager.getInstance().setPimUserManager(pimUserManager);

        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
        mLoggingInterface.log(DEBUG, TAG, "PIMInterface init called.");
        PIMConfigManager pimConfigManager = new PIMConfigManager(pimUserManager);
        pimConfigManager.init(uappDependencies.getAppInfra().getServiceDiscovery());
        long sd_oidc_time = System.currentTimeMillis();
        mLoggingInterface.log(DEBUG, TAG, "time taken for sd_oidc download : " + (sd_oidc_time - startrtime));

        pimUserManager.init(uappDependencies.getAppInfra());
        //pimUserManager.saveUserProfileJsonToStorage(context);
        long endtime = System.currentTimeMillis();
        mLoggingInterface.log(DEBUG, TAG, "time taken to fetch from secure storage : " + (endtime - sd_oidc_time));
    }

    /**
     * Launches the PIM user interface. The component can be launched either with an ActivityLauncher or a FragmentLauncher.
     *
     * @param uiLauncher      pass ActivityLauncher or FragmentLauncher
     * @param uappLaunchInput pass instance of  URLaunchInput
     * @since TODO
     */
    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {

        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity(((ActivityLauncher) uiLauncher), uappLaunchInput);
            mLoggingInterface.log(DEBUG, TAG, "Launch : Launched as activity");
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchAsFragment((FragmentLauncher) uiLauncher, (PIMLaunchInput) uappLaunchInput);
            mLoggingInterface.log(DEBUG, TAG, "Launch : Launched as fragment");
        }
    }

    private void launchAsFragment(FragmentLauncher uiLauncher, PIMLaunchInput pimLaunchInput) {
        PIMFragment pimFragment = new PIMFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(PIMConstants.PIM_KEY_SCOPES, pimLaunchInput.getPimScopes());
        bundle.putStringArrayList(PIMConstants.PIM_KEY_CUSTOM_CLAIMS, pimLaunchInput.getPimCustomClaims());
        pimFragment.setArguments(bundle);
        addFragment(uiLauncher, pimFragment);
    }

    private void addFragment(FragmentLauncher uiLauncher, Fragment fragment) {
        uiLauncher.getFragmentActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(uiLauncher.getParentContainerResourceID(), fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    private void launchAsActivity(ActivityLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        Intent intent = new Intent(uiLauncher.getActivityContext(), PIMActivity.class);
        intent.putExtra(PIMConstants.PIM_KEY_ACTIVITY_THEME, uiLauncher.getUiKitTheme());
        uiLauncher.getActivityContext().startActivity(intent);
    }

    /**
     * Get the User Data Interface
     *
     * @since TODO
     */
    public UserDataInterface getUserDataInterface() {
        if (context == null) {
            mLoggingInterface.log(DEBUG, TAG, "getUserDataInterface: Context is null");
            return null;
        }
        return new PIMDataImplementation(context, PIMSettingManager.getInstance().getPimUserManager());
    }
}
