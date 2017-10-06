package com.philips.cdp2.ews.view;

import android.support.v4.app.Fragment;

import com.philips.cdp2.ews.view.dialog.CancelDialogFragment;
import com.philips.platform.uappframework.listener.BackEventListener;

public class BaseFragment extends Fragment implements BackEventListener {

    protected void handleCancelButtonClicked() {
        new CancelDialogFragment().show(getChildFragmentManager(), "cancelDialog");
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }
}
