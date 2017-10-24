package com.philips.cdp2.ews.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.view.dialog.CancelDialogFragment;
import com.philips.platform.uappframework.listener.BackEventListener;

public class BaseFragment extends Fragment implements BackEventListener {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof EWSActivity) {
            EWSActivity activity = (EWSActivity) getActivity();
            activity.showCloseButton();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() instanceof EWSActivity) {
            setToolbarTitle();
        }
    }

    protected void handleCancelButtonClicked() {
        new CancelDialogFragment().show(getChildFragmentManager(), "cancelDialog");
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }

    public void setToolbarTitle(){
        ((EWSActivity) getActivity()).updateActionBar(getString(R.string.ews_title),true);
    }
}
