/**
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view.dialog;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.CancelSetupDialogBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.common.callbacks.FragmentCallback;
import com.philips.cdp2.ews.viewmodel.CancelSetupViewModel;

import javax.inject.Inject;

public class CancelDialogFragment extends BaseDialogFragment<CancelSetupDialogBinding>
        implements FragmentCallback {

    @Inject
    CancelSetupViewModel viewModel;

    @Override
    protected void bindViewModel(final CancelSetupDialogBinding viewDataBinding) {
        viewModel.setDialogDismissListener(this);
        viewDataBinding.setViewModel(viewModel);
        viewModel.setFragmentCallback(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.cancel_setup_dialog;
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

    @Override
    public void finishMicroApp() {
        getActivity().finish();
    }
}
