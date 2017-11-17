/**
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.EnableGpsSettingsBinding;

public class GPSEnableDialogFragment extends BaseDialogFragment<EnableGpsSettingsBinding> implements View.OnClickListener {

    String str;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        str = bundle.getString("DeviceName");
    }

    @Override
    protected void bindViewModel(final EnableGpsSettingsBinding viewDataBinding) {
        viewDataBinding.setListener(this);
        viewDataBinding.ewsVerifyDeviceBody.setText(getString(R.string.label_ews_enable_gps_settings_body, str));
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
