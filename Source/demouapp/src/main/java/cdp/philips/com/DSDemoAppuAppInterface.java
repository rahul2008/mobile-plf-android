
package cdp.philips.com;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import cdp.philips.com.activity.DemoActivity;
import cdp.philips.com.utility.DemoAppManager;


public class DSDemoAppuAppInterface implements UappInterface {

    private Context context;
    public AppInfraInterface appInfra;
    DemoAppManager demoAppManager;

 /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        context = uappSettings.getContext();
        appInfra = uappDependencies.getAppInfra();
        //demoAppManager = new DemoAppManager(uappSettings.getContext(),appInfra);
        demoAppManager = DemoAppManager.getInstance();
        demoAppManager.initPreRequisite(uappSettings.getContext(),appInfra);
    }

    /**
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
       if (uiLauncher instanceof ActivityLauncher) {
            Intent intent = new Intent(context, DemoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } 
    }
}
