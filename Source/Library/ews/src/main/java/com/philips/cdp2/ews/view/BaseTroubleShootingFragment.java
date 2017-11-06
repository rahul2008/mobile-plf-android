package com.philips.cdp2.ews.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.viewmodel.BaseTroubleShootingViewModel;

public class BaseTroubleShootingFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void setToolbarTitle() {
        ((EWSActivity) getActivity()).updateActionBar(getString(R.string.ews_support_title),false);
    }

    @Override
    protected void handleCancelButtonClicked(@StringRes int stringId) {
        BaseTroubleShootingViewModel viewModel =
                ((EWSActivity) getActivity()).getEWSComponent().baseTroubleShootingViewModel();
        viewModel.onCancelButtonClicked();
    }

    @Override
    public boolean handleBackEvent() {
        handleCancelButtonClicked(-1);
        return true;
    }
}
