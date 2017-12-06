package com.philips.cdp2.ews.troubleshooting.resetconnection;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.base.BaseTroubleShootingFragment;
import com.philips.cdp2.ews.databinding.FragmentResetConnectionTroubleshootingLayoutBinding;
import com.philips.cdp2.ews.injections.DependencyHelper;
import com.philips.cdp2.ews.injections.DaggerEWSComponent;
import com.philips.cdp2.ews.injections.EWSConfigurationModule;
import com.philips.cdp2.ews.injections.EWSModule;
import com.philips.cdp2.ews.microapp.EWSLauncherInput;

public class ResetConnectionTroubleshootingFragment extends BaseTroubleShootingFragment {

    @NonNull
    FragmentResetConnectionTroubleshootingLayoutBinding fragmentResetConnectionTroubleshootingLayoutBinding;

    @NonNull
    ResetConnectionTroubleshootingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentResetConnectionTroubleshootingLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_reset_connection_troubleshooting_layout, container, false);
        return fragmentResetConnectionTroubleshootingLayoutBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = DaggerEWSComponent.builder()
                .eWSModule(new EWSModule(this.getActivity()
                        , EWSLauncherInput.getFragmentManager()
                        , EWSLauncherInput.getContainerFrameId(), DependencyHelper.getCommCentral()))
                .eWSConfigurationModule(new EWSConfigurationModule(this.getActivity(), DependencyHelper.getContentConfiguration()))
                .build()
                .resetConnectionTroubleshootingViewModel();
        fragmentResetConnectionTroubleshootingLayoutBinding.setViewModel(viewModel);

        view.findViewById(R.id.ews_H_03_02_button_yes)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.onYesButtonClicked();
                    }
                });
        view.findViewById(R.id.ews_03_02_button_no)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.onNoButtonClicked();
                    }
                });
    }

    @NonNull
    @Override
    protected void callTrackPageName() {
        viewModel.trackPageName();
    }
}
