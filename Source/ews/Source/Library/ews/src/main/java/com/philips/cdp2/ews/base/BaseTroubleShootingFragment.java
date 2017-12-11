package com.philips.cdp2.ews.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.injections.DependencyHelper;
import com.philips.cdp2.ews.injections.DaggerEWSComponent;
import com.philips.cdp2.ews.injections.EWSConfigurationModule;
import com.philips.cdp2.ews.injections.EWSModule;
import com.philips.cdp2.ews.microapp.EWSActionBarListener;
import com.philips.cdp2.ews.microapp.EWSLauncherInput;

public abstract class BaseTroubleShootingFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void setToolbarTitle() {
        ((EWSActionBarListener) getContext()).updateActionBar(R.string.ews_support_title, true);
    }

    @Override
    public void handleCancelButtonClicked() {
        BaseTroubleShootingViewModel viewModel =
                getEWSComponent().baseTroubleShootingViewModel();
        viewModel.onCancelButtonClicked();
    }

    @Override
    public boolean handleBackEvent() {
        handleCancelButtonClicked();
        return true;
    }

}
