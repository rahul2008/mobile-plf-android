package com.philips.cdp2.ews.hotspotconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.base.BaseFragment;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.databinding.FragmentConnectingWithDeviceBinding;
import com.philips.cdp2.ews.dialog.EWSAlertDialogFragment;
import com.philips.cdp2.ews.hotspotconnection.ConnectingWithDeviceViewModel.ConnectingPhoneToHotSpotCallback;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.microapp.EWSActionBarListener;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

public class ConnectingWithDeviceFragment extends BaseFragment implements
        ConnectingPhoneToHotSpotCallback {

    private static final String TAG = "ConnectingWithDeviceFragment";
    @Nullable
    private ConnectingWithDeviceViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((EWSActionBarListener) getContext()).closeButton(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentConnectingWithDeviceBinding viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_connecting_with_device,
                container, false);
        viewModel = createViewModel();
        viewModel.setFragmentCallback(this);
        viewModel.connectToHotSpot();
        viewDataBinding.setViewModel(viewModel);

        return viewDataBinding.getRoot();
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
        } catch (IllegalArgumentException e) {
            EWSLogger.e(TAG, e.toString());
        }
    }

    @Override
    public void showTroubleshootHomeWifiDialog(@NonNull  BaseContentConfiguration baseContentConfiguration) {
        Context context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.ews_device_conn_unsuccessful_dialog,
                null, false);

        EWSAlertDialogFragment.Builder builder = new EWSAlertDialogFragment.Builder(context)
                .setDialogView(view)
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDimLayer(DialogConstants.DIM_STRONG)
                .setCancelable(false);
        final EWSAlertDialogFragment alertDialogFragment = (EWSAlertDialogFragment) builder.create(new EWSAlertDialogFragment());
        alertDialogFragment.setFragmentLifeCycleListener(new EWSAlertDialogFragment.FragmentLifeCycleListener() {
            @Override
            public void onStart() {
                EWSTagger.trackPage(Page.PHONE_TO_DEVICE_CONNECTION_FAILED);
            }

            @Override
            public void onStop() {

            }

            @Override
            public void onDismiss(DialogInterface dialog) {

            }

            @Override
            public void onCancel(DialogInterface dialog) {

            }

            @Override
            public void onActivityCreated(Bundle savedInstanceState) {

            }
        });
        alertDialogFragment.showAllowingStateLoss(getChildFragmentManager(), AlertDialogFragment.class.getCanonicalName());
        getChildFragmentManager().executePendingTransactions();
        ((Label) view.findViewById(R.id.connection_problem_please_step2)).setText(getString(R.string.label_ews_connection_problem_please_step2, getString(baseContentConfiguration.getDeviceName())));
        ((Label) view.findViewById(R.id.connection_problem_please_step3)).setText(getString(R.string.label_ews_connection_problem_please_step3, getString(baseContentConfiguration.getDeviceName())));

        Button yesButton = view.findViewById(R.id.ews_H_03_00_a_button_yes);
        Button noButton = view.findViewById(R.id.ews_H_03_00_a_button_no);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel != null) {
                    alertDialogFragment.dismiss();
                    viewModel.onHelpNeeded();
                }
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel != null) {
                    alertDialogFragment.dismiss();
                    viewModel.onHelpNotNeeded();
                }
            }
        });
    }

    @Override
    public Fragment getFragment() {
        return this;
    }


    @Override
    public boolean handleBackEvent() {
        return true;
    }

    @NonNull
    @Override
    protected void callTrackPageName() {
        if (viewModel != null) {
            viewModel.trackPageName();
        }
    }

    private ConnectingWithDeviceViewModel createViewModel() {
        return getEWSComponent().connectingWithDeviceViewModel();
    }
}
