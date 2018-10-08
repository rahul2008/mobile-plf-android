package com.philips.platform.ews.troubleshooting.networknotlisted;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.philips.platform.ews.R;
import com.philips.platform.ews.base.BaseFragment;
import com.philips.platform.ews.databinding.FragmentNetworkNotListedBinding;

public class NetworkNotListedFragment extends BaseFragment {

    @Nullable
    NetworkNotListedViewModel viewModel;
    @Nullable
    FragmentNetworkNotListedBinding binding;

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_network_not_listed, container, false);
        viewModel = createViewModel();
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @NonNull
    private NetworkNotListedViewModel createViewModel() {
        return getEWSComponent().networkNotListedViewModel();
    }

    @Override
    protected void callTrackPageName() {
        if (viewModel != null) {
            viewModel.trackPageName();
        }
    }
}