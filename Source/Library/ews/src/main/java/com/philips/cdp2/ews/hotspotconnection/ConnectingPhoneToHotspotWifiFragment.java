package com.philips.cdp2.ews.hotspotconnection;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.hotspotconnection.ConnectingPhoneToHotspotWifiViewModel
        .ConnectingPhoneToHotSpotCallback;
import com.philips.cdp2.ews.view.BaseFragment;
import com.philips.cdp2.ews.view.EWSActivity;

import static com.philips.cdp2.ews.troubleshooting.hotspotconnectionfailure
        .ConnectionUnsuccessfulFragment.UNSUCCESSFUL_CONNECTION_RESULT;

public class ConnectingPhoneToHotspotWifiFragment extends BaseFragment implements
        ConnectingPhoneToHotSpotCallback {

    private static final int REQUEST_CODE = 100;

    @Nullable private ConnectingPhoneToHotspotWifiViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EWSActivity activity = (EWSActivity) getActivity();
        activity.hideCloseButton();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = createViewModel();
        viewModel.setFragmentCallback(this);
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
        if (viewModel != null) {
            viewModel.clear();
        }
    }

    @Override
    public void registerReceiver(@NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter) {
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void unregisterReceiver(@NonNull BroadcastReceiver receiver) {
        try {
            getActivity().unregisterReceiver(receiver);
        } catch (IllegalArgumentException ignored) {}
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public int requestCode() {
        return REQUEST_CODE;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            int result = data.getIntExtra(UNSUCCESSFUL_CONNECTION_RESULT, -1);
            if (viewModel != null) {
                viewModel.onResultReceived(result);
            }
        }
    }

    @Override
    public boolean handleBackEvent() {
        // Do nothing, back disabled in this screen
        return true;
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
