/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.troubleshooting.hotspotconnectionfailure;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.EwsDeviceConnUnsuccessfulBinding;
import com.philips.cdp2.ews.injections.DaggerEWSComponent;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.injections.EWSModule;
import com.philips.cdp2.ews.view.EWSBaseFragment;

import javax.inject.Inject;

public class ConnectionUnsuccessfulFragment extends EWSBaseFragment<EwsDeviceConnUnsuccessfulBinding>
        implements UnsuccessfulConnectionCallback {

    public static final String UNSUCCESSFUL_CONNECTION_RESULT = "result";

    @Inject
    ConnectionUnsuccessfulViewModel viewModel;

    @NonNull
    @Override
    protected String getPageName() {
        return ConnectionUnsuccessfulFragment.class.getCanonicalName();
    }

    @Override
    protected void bindViewModel(final EwsDeviceConnUnsuccessfulBinding viewDataBinding) {
        viewModel.setCallback(this);
        viewDataBinding.setViewModel(viewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ews_device_conn_unsuccessful;
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getHierarchyLevel() {
        return 0;
    }

    @Override
    protected EWSComponent getEwsComponent() {
        return DaggerEWSComponent.builder().eWSModule(new EWSModule(getContext(), getFragmentManager())).build();
    }

    @Override
    public void hideDialogWithResult(int result) {
        Intent data = new Intent();
        data.putExtra(UNSUCCESSFUL_CONNECTION_RESULT, result);
        getActivity().setResult(Activity.RESULT_OK, data);
        getActivity().finish();
    }
}
