package com.philips.cdp2.ews.troubleshooting.resetdevice;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.base.BaseTroubleShootingFragment;
import com.philips.cdp2.ews.databinding.FragmentResetDeviceTroubleshootingLayoutBinding;

public class ResetDeviceTroubleshootingFragment extends BaseTroubleShootingFragment {

    @NonNull
    @SuppressWarnings("NullableProblems")
    FragmentResetDeviceTroubleshootingLayoutBinding resetDeviceTroubleshootingLayoutBinding;


    @NonNull
    @SuppressWarnings("NullableProblems")
    ResetDeviceTroubleshootingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        resetDeviceTroubleshootingLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_reset_device_troubleshooting_layout, container, false);
        return resetDeviceTroubleshootingLayoutBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = getEWSComponent()
                        .resetDeviceTroubleshootingViewModel();
        resetDeviceTroubleshootingLayoutBinding.setViewModel(viewModel);

        view.findViewById(R.id.ews_H_03_03_button_done)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.onDoneButtonClicked();
                    }
                });

    }

    @Override
    protected void callTrackPageName() {
        viewModel.trackPageName();
    }
}
