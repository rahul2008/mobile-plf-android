package com.philips.cdp2.ews.troubleshooting.connecttowrongphone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.view.BaseTroubleShootingFragment;
import com.philips.cdp2.ews.view.EWSActivity;

public class ConnectToWrongPhoneTroubleshootingFragment extends BaseTroubleShootingFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connect_to_wrong_phone_troubleshooting_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ConnectToWrongPhoneTroubleshootingViewModel viewModel = ((EWSActivity) getActivity()).getEWSComponent()
                .connectToWrongPhoneTroubleshootingViewModel();

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
}
