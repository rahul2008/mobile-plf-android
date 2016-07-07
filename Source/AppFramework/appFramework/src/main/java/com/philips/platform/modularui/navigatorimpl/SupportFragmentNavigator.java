package com.philips.platform.modularui.navigatorimpl;

import android.content.Context;

import com.philips.cdp.digitalcare.homefragment.SupportHomeFragment;
import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.modularui.statecontroller.UIBaseNavigator;

/**
 * Created by 310240027 on 7/5/2016.
 */
public class SupportFragmentNavigator implements UIBaseNavigator {
    AppFrameworkApplication appFrameworkApplication;
    @Override
    public void navigate(Context context) {
        ((AppFrameworkBaseActivity)context).showFragment( new SupportHomeFragment(), new SupportHomeFragment().getClass().getSimpleName());
    }
}
