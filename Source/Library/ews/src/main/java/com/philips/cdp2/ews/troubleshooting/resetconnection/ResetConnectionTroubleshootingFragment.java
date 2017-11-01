package com.philips.cdp2.ews.troubleshooting.resetconnection;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.FragmentResetConnectionTroubleshootingLayoutBinding;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.view.BaseTroubleShootingFragment;
import com.philips.cdp2.ews.view.EWSActivity;

public class ResetConnectionTroubleshootingFragment extends BaseTroubleShootingFragment {

    @NonNull
    FragmentResetConnectionTroubleshootingLayoutBinding fragmentResetConnectionTroubleshootingLayoutBinding;

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

        final ResetConnectionTroubleshootingViewModel viewModel = ((EWSActivity) getActivity()).getEWSComponent()
                        .resetConnectionTroubleshootingViewModel();
        fragmentResetConnectionTroubleshootingLayoutBinding.setViewModel(viewModel);

        view.findViewById(R.id.ews_H_03_02_button_yes)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //EWSTagger.trackActionSendData(Tag.);
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
    protected String getPageName() {
        return Page.RESET_CONNECTION;
    }
}
