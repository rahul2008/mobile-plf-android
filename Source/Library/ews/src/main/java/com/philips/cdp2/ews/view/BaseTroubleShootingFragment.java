package com.philips.cdp2.ews.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.philips.cdp2.ews.viewmodel.BaseTroubleShootingViewModel;

public class BaseTroubleShootingFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void handleCancelButtonClicked() {
        BaseTroubleShootingViewModel viewModel =
                ((EWSActivity) getActivity()).getEWSComponent().baseTroubleShootingViewModel();
        viewModel.onCancelButtonClicked();
    }

    @Override
    public boolean handleBackEvent() {
        handleCancelButtonClicked();
        return true;
    }
}
