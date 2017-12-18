package com.philips.platform.ews.troubleshooting.connecttowrongphone;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.ews.R;
import com.philips.platform.ews.base.BaseTroubleShootingFragment;
import com.philips.platform.ews.databinding.FragmentConnectToWrongPhoneTroubleshootingLayoutBinding;

public class ConnectToWrongPhoneTroubleshootingFragment extends BaseTroubleShootingFragment {

    @NonNull
    @SuppressWarnings("NullableProblems")
    private FragmentConnectToWrongPhoneTroubleshootingLayoutBinding connectToWrongPhoneTroubleshootingLayoutBinding;

    @NonNull
    @SuppressWarnings("NullableProblems")
    private ConnectToWrongPhoneTroubleshootingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        connectToWrongPhoneTroubleshootingLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_connect_to_wrong_phone_troubleshooting_layout, container, false);
        return connectToWrongPhoneTroubleshootingLayoutBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = getEWSComponent()
                .connectToWrongPhoneTroubleshootingViewModel();

        connectToWrongPhoneTroubleshootingLayoutBinding.setViewmodel(viewModel);

        view.findViewById(R.id.ews_H_03_01_button_yes)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.onYesButtonClicked();
                    }
                });
        view.findViewById(R.id.ews_H_03_01_button_no)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.onNoButtonClicked();
                    }
                });
    }

    @Override
    protected void callTrackPageName() {
        viewModel.trackPageName();
    }
}