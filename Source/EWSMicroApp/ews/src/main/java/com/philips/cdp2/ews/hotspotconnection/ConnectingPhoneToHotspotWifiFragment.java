package com.philips.cdp2.ews.hotspotconnection;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.hotspotconnection.ConnectingPhoneToHotspotWifiViewModel
        .ConnectingPhoneToHotSpotCallback;
import com.philips.cdp2.ews.view.BaseFragment;
import com.philips.cdp2.ews.view.EWSActivity;

public class ConnectingPhoneToHotspotWifiFragment extends BaseFragment implements
        ConnectingPhoneToHotSpotCallback {

    @Nullable private ConnectingPhoneToHotspotWifiViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = createViewModel();
        viewModel.setCallback(this);
        viewModel.connectToHotSpot();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.clear();
    }

    private void cancelButtonClicked() {
        if (viewModel != null) {
            viewModel.handleCancelButtonClicked();
        }
    }

    private ConnectingPhoneToHotspotWifiViewModel createViewModel() {
        return ((EWSActivity) getActivity()).getEWSComponent().connectingPhoneToHotspotWifiViewModel();
    }

    @Override
    public void registerReceiver(@NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter) {
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void unregisterReceiver(@NonNull BroadcastReceiver receiver) {
        getActivity().unregisterReceiver(receiver);
    }
}
