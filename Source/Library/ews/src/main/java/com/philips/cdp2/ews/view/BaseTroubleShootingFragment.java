package com.philips.cdp2.ews.view;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.viewmodel.BaseTroubleShootingViewModel;

public abstract class BaseTroubleShootingFragment extends BaseFragment {

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

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        String pageName = getPageName();
        if (pageName != null) {
            EWSTagger.trackPage(pageName);
        }
    }

    protected abstract String getPageName();
}
