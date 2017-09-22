/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view.dialog;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.EwsDeviceConnUnsuccesfulBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.viewmodel.ConnectionUnsuccessfulViewModel;

import javax.inject.Inject;

public class ConnectionUnsuccessfulDialog extends BaseDialogFragment<EwsDeviceConnUnsuccesfulBinding> {

    @Inject
    ConnectionUnsuccessfulViewModel viewModel;

    @Override
    protected void bindViewModel(final EwsDeviceConnUnsuccesfulBinding viewDataBinding) {
        viewModel.setDialogDismissListener(this);
        viewDataBinding.setViewModel(viewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ews_device_conn_unsuccesful;
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.removeDialogDismissListener();
    }
}
