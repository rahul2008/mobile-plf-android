package com.philips.platform.ews.troubleshooting.resetconnection;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.ews.R;
import com.philips.platform.ews.base.BaseTroubleShootingFragment;
import com.philips.platform.ews.databinding.FragmentResetConnectionTroubleshootingLayoutBinding;

public class ResetConnectionTroubleshootingFragment extends BaseTroubleShootingFragment {

    @NonNull
    @SuppressWarnings("NullableProblems")
    FragmentResetConnectionTroubleshootingLayoutBinding fragmentResetConnectionTroubleshootingLayoutBinding;

    @NonNull
    @SuppressWarnings("NullableProblems")
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

        viewModel = getEWSComponent()
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

    @Override
    protected void callTrackPageName() {
        viewModel.trackPageName();
    }
}