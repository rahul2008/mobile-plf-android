/**
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.dialog;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.EnableGpsSettingsBinding;
import com.philips.cdp2.ews.injections.EWSComponent;

public class GPSEnableDialogFragment extends BaseDialogFragment<EnableGpsSettingsBinding> implements View.OnClickListener {

    @Override
    protected void bindViewModel(final EnableGpsSettingsBinding viewDataBinding) {
        viewDataBinding.setListener(this);
        setCancelable(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.enable_gps_settings;
    }

    @Override
    public void onClick(final View view) {
        getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        dismissAllowingStateLoss();
    }
}
