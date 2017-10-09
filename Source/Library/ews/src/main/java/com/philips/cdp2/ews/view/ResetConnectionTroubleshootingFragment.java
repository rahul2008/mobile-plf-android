package com.philips.cdp2.ews.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.viewmodel.ResetConnectionTroubleshootingViewModel;

public class ResetConnectionTroubleshootingFragment extends BaseTroubleShootingFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reset_connection_troubleshooting_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ResetConnectionTroubleshootingViewModel viewModel = ((EWSActivity) getActivity()).getEWSComponent()
                        .resetConnectionTroubleshootingViewModel();

        view.findViewById(R.id.reset_connection_yes_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.onYesButtonClicked();
                    }
                });
        view.findViewById(R.id.reset_connection_no_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.onNoButtonClicked();
                    }
                });
    }
}
