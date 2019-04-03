package integration;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.pim.PIMActivity;
import com.philips.platform.pim.fragment.DummyFragment;
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

    @Override
    public void init(@NonNull UappDependencies uappDependencies, @NonNull UappSettings uappSettings) {
        context = uappSettings.getContext();
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
        mLoggingInterface.log(DEBUG,TAG,"PIMInterface init called");

        PIMSettingManager.getInstance().init(uappDependencies);
        PIMConfigManager pimConfigManager = new PIMConfigManager();
        pimConfigManager.init(uappDependencies.getAppInfra().getServiceDiscovery());
        PIMUserManager pimUserManager = new PIMUserManager();
        pimUserManager.init(uappDependencies.getAppInfra().getSecureStorage());
        PIMSettingManager.getInstance().setPimUserManager(pimUserManager);
    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {

        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity(((ActivityLauncher) uiLauncher), uappLaunchInput);
            mLoggingInterface.log(DEBUG, TAG, "Launch : Launched as activity");
        } else if (uiLauncher instanceof FragmentLauncher) {
            launchAsFragment((FragmentLauncher) uiLauncher, uappLaunchInput);
            mLoggingInterface.log(DEBUG, TAG, "Launch : Launched as fragment");
        }
    }

    private void launchAsFragment(FragmentLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        DummyFragment pimFragment = new DummyFragment();
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
     * @since 2018.1.0
     */
    public UserDataInterface getUserDataInterface() {
        if (context == null) {
            mLoggingInterface.log(DEBUG, TAG, "getUserDataInterface: Context is null");
            return null;
        }
        return new PIMDataImplementation(context, PIMSettingManager.getInstance().getPimUserManager());
    }
}
