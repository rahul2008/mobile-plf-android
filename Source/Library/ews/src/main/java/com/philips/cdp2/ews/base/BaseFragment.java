/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.dialog.EWSAlertDialogFragment;
import com.philips.cdp2.ews.injections.DependencyHelper;
import com.philips.cdp2.ews.injections.DaggerEWSComponent;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.injections.EWSConfigurationModule;
import com.philips.cdp2.ews.injections.EWSDependencyProviderModule;
import com.philips.cdp2.ews.injections.EWSModule;
import com.philips.cdp2.ews.microapp.EWSActionBarListener;
import com.philips.cdp2.ews.microapp.EWSLauncherInput;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;

public abstract class BaseFragment extends Fragment implements BackEventListener {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!DependencyHelper.areDependenciesInitialized()){
            this.getActivity().finish();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((EWSActionBarListener) getContext()).closeButton(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        setToolbarTitle();
        if (getChildFragmentManager().getFragments().isEmpty()){
            callTrackPageName();
        }
    }

    public void handleCancelButtonClicked() {
        EWSComponent ewsComponent = getEWSComponent();
        showCancelDialog(ewsComponent.getBaseContentConfiguration().getDeviceName(), ewsComponent.getEWSTagger());
    }

    @VisibleForTesting
    void showCancelDialog(@StringRes int deviceName, @NonNull final EWSTagger ewsTagger) {
        Context context = getContext();
        View view = LayoutInflater.from(context).cloneInContext(UIDHelper.getPopupThemedContext(context)).inflate(R.layout.cancel_setup_dialog,
                null, false);

        AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(context)
                .setDialogView(view)
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDimLayer(DialogConstants.DIM_STRONG)
                .setCancelable(false);

        final EWSAlertDialogFragment alertDialogFragment = (EWSAlertDialogFragment) builder.create(new EWSAlertDialogFragment());
        alertDialogFragment.setDialogLifeCycleListener(new EWSAlertDialogFragment.DialogLifeCycleListener() {
            @Override
            public void onStart() {
                ewsTagger.trackPage(Page.CANCEL_WIFI_SETUP);
            }
        });

        alertDialogFragment.show(getChildFragmentManager(), AlertDialogFragment.class.getCanonicalName());

        Button yesButton = view.findViewById(R.id.ews_04_02_button_cancel_setup_yes);
        Button noButton = view.findViewById(R.id.ews_04_02_button_cancel_setup_no);
        ((TextView) view.findViewById(R.id.ews_verify_device_body)).setText(getString(R.string.label_ews_cancel_setup_body, getString(deviceName)));
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callTrackPageName();
                alertDialogFragment.dismiss();
            }
        });
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }

    public void setToolbarTitle() {
        ((EWSActionBarListener) getContext()).updateActionBar(R.string.ews_title, true);
    }

    public EWSComponent getEWSComponent() {
        return DaggerEWSComponent.builder()
                .eWSModule(new EWSModule(this.getActivity()
                        , this.getActivity().getSupportFragmentManager()
                        , EWSLauncherInput.getContainerFrameId(), DependencyHelper.getCommCentral()))
                .eWSConfigurationModule(new EWSConfigurationModule(this.getActivity(), DependencyHelper.getContentConfiguration()))
                .eWSDependencyProviderModule(new EWSDependencyProviderModule(DependencyHelper.getAppInfraInterface(), DependencyHelper.getProductKeyMap()))
                .build();
    }

    protected abstract void callTrackPageName();
}
