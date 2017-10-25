package com.philips.cdp2.ews.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.viewmodel.SetupAccessPointModeTroubleshootingViewModel;

public class SetupAccessPointModeTroubleshootingFragment extends BaseTroubleShootingFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setup_access_point_troubleshooting_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final SetupAccessPointModeTroubleshootingViewModel viewModel = ((EWSActivity) getActivity()).getEWSComponent()
                .setupAccessPointModeTroubleshootingViewModel();

        view.findViewById(R.id.ews_H_03_04_button_done)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.onDoneButtonClicked();
                    }
                });
    }
}
