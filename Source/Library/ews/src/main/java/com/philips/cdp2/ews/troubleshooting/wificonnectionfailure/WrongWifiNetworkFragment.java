package com.philips.cdp2.ews.troubleshooting.wificonnectionfailure;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.FragmentWrongWifiNetworkBinding;
import com.philips.cdp2.ews.view.BaseFragment;
import com.philips.cdp2.ews.view.EWSActivity;

/**
 * Created by salvatorelafiura on 11/10/2017.
 */

public class WrongWifiNetworkFragment extends BaseFragment {
    private final static String NETWORK_SSID = "network_SSID";

    @Nullable
    WrongWifiNetworkViewModel viewModel;
    @Nullable
    FragmentWrongWifiNetworkBinding binding;

    public static Fragment newInstance(@NonNull String networkName) {
        WrongWifiNetworkFragment fragment = new WrongWifiNetworkFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NETWORK_SSID, networkName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wrong_wifi_network, container, false);
        viewModel = createViewModel();
        if (binding != null && viewModel != null) {
            viewModel.setNetworkName(getArguments().getString(NETWORK_SSID));
            binding.setViewModel(viewModel);
        }
        return binding.getRoot();
    }

    private WrongWifiNetworkViewModel createViewModel() {
        return ((EWSActivity) getActivity()).getEWSComponent().wrongWifiNetworkViewModel();
    }
}
