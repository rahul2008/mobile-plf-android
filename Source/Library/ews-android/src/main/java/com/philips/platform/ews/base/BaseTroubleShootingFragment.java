package com.philips.platform.ews.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.philips.platform.ews.R;
import com.philips.platform.ews.microapp.EWSActionBarListener;

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
