package com.philips.cdp.prodreg.launcher;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;

import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * It is used to initialize and launch Product Registration
 * @since 1.0.0
 */
public class PRInterface implements UappInterface {

    private static final long serialVersionUID = -6635233525340545674L;

    /**
     * API used for initializing Product Registration
     * @param uappDependencies - pass instance of UappDependencies
     * @param uappSettings - pass instance of UappSettings
     * @since 1.0.0
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        if(null == ((PRDependencies)uappDependencies).getUserDataInterface())
            throw new RuntimeException("UserDataInterface is not injected in PRDependencies");
        PRUiHelper.getInstance().init(uappDependencies, uappSettings);
        PRUiHelper.getInstance().setAppInfraInstance(uappDependencies.getAppInfra());
        ProdRegLogger.init(uappDependencies.getAppInfra().getLogging());
    }

    /**
     * API used for Launching Product Registration as activity or fragment
     * @param uiLauncher - pass instance of UiLauncher
     * @param uappLaunchInput - pass instance of UappLaunchInput
     * @since 1.0.0
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        PRUiHelper.getInstance().launch(uiLauncher, uappLaunchInput);
    }

    public void launchProductAuthentication(Context context){
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse("https://www.chk.philips.com/gb_en/"));
    }
}
