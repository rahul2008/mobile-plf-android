package com.philips.cdp2.ews.hotspotconnection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.view.BaseFragment;
import com.philips.cdp2.ews.view.EWSActivity;

public class ConnectingPhoneToHotspotWifiFragment extends BaseFragment {

    @Nullable private ConnectingPhoneToHotspotWifiViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = createViewModel();
        return inflater.inflate(R.layout.fragment_connecting_phone_to_hotspot_layout, container,
                false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.cancel_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelButtonClicked();
                    }
                });
    }

    private void cancelButtonClicked() {
        if (viewModel != null) {
            viewModel.handleCancelButtonClicked();
        }
    }

    private ConnectingPhoneToHotspotWifiViewModel createViewModel() {
        return ((EWSActivity) getActivity()).getEWSComponent().connectingPhoneToHotspotWifiViewModel();
    }
}
