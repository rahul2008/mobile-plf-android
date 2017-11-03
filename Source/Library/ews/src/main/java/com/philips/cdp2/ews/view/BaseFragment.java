package com.philips.cdp2.ews.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.philips.cdp2.ews.R;
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
    }

    protected void handleCancelButtonClicked(@StringRes int stringId) {
        showCancelDialog(stringId);
        EWSTagger.trackPage(Page.CANCEL_WIFI_SETUP);
    }

    @VisibleForTesting
    void showCancelDialog(@StringRes int deviceName) {
        Context context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.cancel_setup_dialog,
                null, false);

        AlertDialogFragment.Builder builder = getNewAlertDialogFragmentBuilder(context)
                .setDialogView(view)
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDimLayer(DialogConstants.DIM_STRONG)
                .setCancelable(true);
        final AlertDialogFragment alertDialogFragment = builder.create();
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
                alertDialogFragment.dismiss();
            }
        });
    }

    @VisibleForTesting
    AlertDialogFragment.Builder getNewAlertDialogFragmentBuilder(Context context){
        return new AlertDialogFragment.Builder(context);
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }

    public void setToolbarTitle() {
        ((EWSActivity) getActivity()).updateActionBar(getString(R.string.ews_title), true);
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        String pageName = getPageName();
        if (pageName != null) {
            EWSTagger.trackPage(pageName);
        }
    }

    protected abstract String getPageName();
}
