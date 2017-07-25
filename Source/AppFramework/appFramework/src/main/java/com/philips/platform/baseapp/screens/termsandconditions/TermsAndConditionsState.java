package com.philips.platform.baseapp.screens.termsandconditions;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 24/07/17.
 */

public class TermsAndConditionsState extends BaseState {

    public TermsAndConditionsState() {
        super(AppStates.TERMSANDCONITIONSSTATE);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        Intent intent=new Intent(fragmentLauncher.getFragmentActivity(),WebViewActivity.class);
        fragmentLauncher.getFragmentActivity().startActivity(intent);
//        WebViewActivity.show(fragmentLauncher.getFragmentActivity());
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void updateDataModel() {

    }
}
