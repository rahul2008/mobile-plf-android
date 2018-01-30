
package com.philips.platform.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.ths.activity.THSLaunchActivity;
import com.philips.platform.ths.init.THSInitFragment;
import com.philips.platform.ths.uappclasses.THSMicroAppLaunchInput;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.thsdemolaunch.FirstActivity;
import com.philips.platform.thsdemolaunch.THSAppInfraInstance;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class DemoMicroAppApplicationuAppInterface implements UappInterface {

    private Context context;
    private AppInfraInterface appInfraInterface;

 /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
       this.context = uappSettings.getContext();
        this.appInfraInterface = uappDependencies.getAppInfra();
        THSAppInfraInstance.getInstance().setAppInfraInterface(appInfraInterface);
    }

    /**
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {

       /* if(uiLauncher instanceof ActivityLauncher) {
            Intent fragmentLauncher1 = new Intent(this.context, THSLaunchActivity.class);
            fragmentLauncher1.putExtra("KEY_ACTIVITY_THEME", ((ActivityLauncher)uiLauncher).getUiKitTheme());
            if(this.themeConfigurationExists((ActivityLauncher)uiLauncher)) {
                fragmentLauncher1.putExtras(this.getThemeConfigsIntent((ActivityLauncher)uiLauncher));
            }

            if(null != ((ActivityLauncher)uiLauncher).getScreenOrientation()) {
                ActivityLauncher.ActivityOrientation fragmentTransaction = ((ActivityLauncher)uiLauncher).getScreenOrientation();
                fragmentLauncher1.putExtra("KEY_ORIENTATION", fragmentTransaction);
            }

            fragmentLauncher1.addFlags(268435456);
            this.context.startActivity(fragmentLauncher1);
        } else {
            FragmentLauncher fragmentLauncher2 = (FragmentLauncher)uiLauncher;
            FragmentTransaction fragmentTransaction1 = fragmentLauncher2.getFragmentActivity().getSupportFragmentManager().beginTransaction();
            THSInitFragment thsBaseFragment = new THSInitFragment();
            this.lauchFirstFragment(thsBaseFragment, fragmentLauncher2, fragmentTransaction1);
        }*/

       if (uiLauncher instanceof ActivityLauncher) {
            Intent intent = new Intent(context, FirstActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } 
    }
}
