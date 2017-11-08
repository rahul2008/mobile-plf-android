package com.philips.cdp2.ews.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.EWSActivity;

public class BaseTroubleShootingFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void setToolbarTitle() {
        ((EWSActivity) getActivity()).setToolbarTitle(getString(R.string.ews_support_title));
    }

    @Override
    public void handleCancelButtonClicked(@StringRes int stringId) {
        BaseTroubleShootingViewModel viewModel =
                EWSDependencyProvider.getInstance().getEwsComponent().baseTroubleShootingViewModel();
        viewModel.onCancelButtonClicked();
    }

    @Override
    public boolean handleBackEvent() {
        handleCancelButtonClicked(-1);
        return true;
    }
}
