package com.philips.cdp2.ews.troubleshooting.setupaccesspointmode;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.base.BaseTroubleShootingFragment;
import com.philips.cdp2.ews.databinding.FragmentSetupAccessPointTroubleshootingLayoutBinding;

public class SetupAccessPointModeTroubleshootingFragment extends BaseTroubleShootingFragment {

    @NonNull
    @SuppressWarnings("NullableProblems")
    FragmentSetupAccessPointTroubleshootingLayoutBinding setupAccessPointTroubleshootingLayoutBinding;

    @NonNull
    @SuppressWarnings("NullableProblems")
    SetupAccessPointModeTroubleshootingViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setupAccessPointTroubleshootingLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setup_access_point_troubleshooting_layout, container, false);
        return setupAccessPointTroubleshootingLayoutBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = getEWSComponent().setupAccessPointModeTroubleshootingViewModel();
        setupAccessPointTroubleshootingLayoutBinding.setViewmodel(viewModel);

        view.findViewById(R.id.ews_H_03_04_button_done)
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
