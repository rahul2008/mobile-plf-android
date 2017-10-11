package com.philips.cdp2.ews.troubleshooting.wificonnectionfailure;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.FragmentWifiConnectionUnsuccessfulBinding;
import com.philips.cdp2.ews.view.BaseFragment;
import com.philips.cdp2.ews.view.EWSActivity;

/**
 * Created by salvatorelafiura on 11/10/2017.
 */

public class WifiConnectionUnsuccessfulFragment extends BaseFragment {
    private final static String WIFI_SSID = "wifi_ssid";

    public static Fragment newInstance(String wifiSSID) {
        WifiConnectionUnsuccessfulFragment fragment = new WifiConnectionUnsuccessfulFragment();
        Bundle bundle = new Bundle();
        bundle.putString(WIFI_SSID, wifiSSID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    WIFIConnectionUnsuccessfulViewModel viewModel;
    @Nullable
    FragmentWifiConnectionUnsuccessfulBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wifi_connection_unsuccessful, container, false);
        viewModel = createViewModel();
        viewModel.setDescription(getString(R.string.label_ews_phone_not_found_on_network_body, getArguments().getString(WIFI_SSID)));
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    private WIFIConnectionUnsuccessfulViewModel createViewModel() {
        return ((EWSActivity) getActivity()).getEWSComponent().wIFIConnectionUnsuccessfulViewModel();
    }
}
