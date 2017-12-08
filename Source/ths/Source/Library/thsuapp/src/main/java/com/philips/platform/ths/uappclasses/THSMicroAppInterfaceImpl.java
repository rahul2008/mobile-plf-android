package com.philips.platform.ths.uappclasses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.ths.activity.THSLaunchActivity;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.init.THSInitFragment;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfig;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import java.io.Serializable;
import java.util.List;


public class THSMicroAppInterfaceImpl implements UappInterface {
    protected Context context;
    protected AppInfraInterface appInfra;
    /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        this.context = uappSettings.getContext();
        appInfra = uappDependencies.getAppInfra();
    }

    /**
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        if( uappLaunchInput instanceof THSMicroAppLaunchInput){
            THSMicroAppLaunchInput thsMicroAppLaunchInput=(THSMicroAppLaunchInput)uappLaunchInput;
            THSManager.getInstance().setThsCompletionProtocol(thsMicroAppLaunchInput.getThsCompletionProtocol());
        }
        THSManager.getInstance().setAppInfra(appInfra);
        if (uiLauncher instanceof ActivityLauncher) {
            Intent intent = new Intent(context, THSLaunchActivity.class);
            intent.putExtra(THSConstants.KEY_ACTIVITY_THEME, ((ActivityLauncher) uiLauncher).getUiKitTheme());
            if(themeConfigurationExists((ActivityLauncher) uiLauncher)) {
                intent.putExtras(getThemeConfigsIntent((ActivityLauncher) uiLauncher));
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
            FragmentTransaction fragmentTransaction = (fragmentLauncher.getFragmentActivity()).getSupportFragmentManager().beginTransaction();
            THSBaseFragment thsBaseFragment;
            thsBaseFragment = new THSInitFragment();
            lauchFirstFragment(thsBaseFragment,fragmentLauncher,fragmentTransaction);
        }
    }

    private boolean themeConfigurationExists(ActivityLauncher uiLauncher) {
        return null != uiLauncher.getDlsThemeConfiguration();
    }

    private Intent getThemeConfigsIntent(ActivityLauncher activityLauncher) {
        ThemeConfiguration themeConfiguration = activityLauncher.getDlsThemeConfiguration();
        List<ThemeConfig> configurations = themeConfiguration.getConfigurations();
        return getConfigurationIntent(configurations);
    }

    private Intent getConfigurationIntent(List<ThemeConfig> configurations) {
        Intent intent = new Intent();
        for (ThemeConfig config : configurations) {
            if(config instanceof ColorRange) {
                intent.putExtra(THSConstants.KEY_COLOR_RANGE, getConfigElement(config));
            }
            if (config instanceof ContentColor) {
                intent.putExtra(THSConstants.KEY_CONTENT_COLOR, getConfigElement(config));
            }
            if(config instanceof NavigationColor) {
                intent.putExtra(THSConstants.KEY_NAVIGATION_COLOR, getConfigElement(config));
            }
            if(config instanceof AccentRange) {
                intent.putExtra(THSConstants.KEY_ACCENT_RANGE, getConfigElement(config));
            }
        }
        return intent;
    }

    private <T extends Serializable> T getConfigElement(ThemeConfig config) {
        T configElement = (T) config;
        return configElement;
    }

    private void lauchFirstFragment(THSBaseFragment thsBaseFragment,FragmentLauncher fragmentLauncher, FragmentTransaction fragmentTransaction) {
        Bundle bundle = new Bundle();
        thsBaseFragment.setArguments(bundle);
        thsBaseFragment.setActionBarListener(fragmentLauncher.getActionbarListener());
        thsBaseFragment.setFragmentLauncher(fragmentLauncher);
        fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(), thsBaseFragment, THSInitFragment.TAG).
                addToBackStack(THSInitFragment.TAG).commitAllowingStateLoss();
    }
}
