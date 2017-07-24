package com.philips.platform.baseapp.screens.termsandconditions;

import android.content.Context;

import com.philips.platform.appframework.R;
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
        WebViewActivity.show(fragmentLauncher.getFragmentActivity(), R.string.global_terms_link,R.string.terms_and_conditions_url_about);
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void updateDataModel() {

    }
}
