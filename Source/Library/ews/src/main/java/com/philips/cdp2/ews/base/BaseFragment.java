/**
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.philips.cdp2.ews.EWSActivity;
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.dialog.EWSAlertDialogFragment;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;

public abstract class BaseFragment extends Fragment implements BackEventListener {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof EWSActivity) {
            EWSActivity activity = (EWSActivity) getActivity();
            activity.showCloseButton();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() instanceof EWSActivity) {
            setToolbarTitle();
        }
        if (getChildFragmentManager().getFragments().isEmpty()){
            callTrackPageName();
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void handleCancelButtonClicked(@StringRes int stringId) {
        showCancelDialog(stringId);
    }

    @VisibleForTesting
    public void showCancelDialog(@StringRes int deviceName) {
        Context context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.cancel_setup_dialog,
                null, false);

        AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(context)
                .setDialogView(view)
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDimLayer(DialogConstants.DIM_STRONG)
                .setCancelable(false);

        final EWSAlertDialogFragment alertDialogFragment = (EWSAlertDialogFragment) builder.create(new EWSAlertDialogFragment());
        alertDialogFragment.setFragmentLifeCycleListener(new EWSAlertDialogFragment.FragmentLifeCycleListener() {
            @Override
            public void onStart() {
                EWSTagger.trackPage(Page.CANCEL_WIFI_SETUP);
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

        alertDialogFragment.show(getChildFragmentManager(), AlertDialogFragment.class.getCanonicalName());

        Button yesButton = (Button) view.findViewById(R.id.ews_04_02_button_cancel_setup_yes);
        Button noButton = (Button) view.findViewById(R.id.ews_04_02_button_cancel_setup_no);
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
        ((EWSActivity) getActivity()).updateActionBar(getString(R.string.ews_title), true);
    }

    protected abstract void callTrackPageName();
}
