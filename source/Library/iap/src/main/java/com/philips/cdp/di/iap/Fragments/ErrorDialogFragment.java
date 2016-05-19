package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.uikit.modalalert.BlurDialogFragment;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ErrorDialogFragment extends BlurDialogFragment {
    public interface ErrorDialogListener {
        void onTryAgainClick();
    }

    private Button mOkBtn, mTryAgain;
    private ErrorDialogListener mClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.iap_error_dialog, container, false);
        //  EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG_ON_ERROR), this);
        Bundle bundle = getArguments();
        TextView dialogTitle = (TextView) v.findViewById(R.id.dialogTitle);
        dialogTitle.setText(bundle.getString(IAPConstant.MODEL_ALERT_ERROR_TEXT));

        TextView errorDescription = (TextView) v.findViewById(R.id.dialogDescription);
        errorDescription.setText(bundle.getString(IAPConstant.MODEL_ALERT_ERROR_DESCRIPTION));

        mOkBtn = (Button) v.findViewById(R.id.btn_dialog_ok);
        mOkBtn.setText(bundle.getString(IAPConstant.MODEL_ALERT_BUTTON_TEXT));
        mTryAgain = (Button) v.findViewById(R.id.btn_dialog_tryAgain);
        if (bundle != null && bundle.getBoolean(IAPConstant.MODEL_ALERT_TRYAGAIN_BUTTON_VISIBLE)) {
            mTryAgain.setVisibility(View.VISIBLE);
            mOkBtn.setVisibility(View.GONE);
            mTryAgain.setOnClickListener(dismissDialogOnTryAgain());
        } else {
            mOkBtn.setVisibility(View.VISIBLE);
            mTryAgain.setVisibility(View.GONE);
            mOkBtn.setOnClickListener(dismissDialog());
        }
        return v;
    }

    private View.OnClickListener dismissDialogOnTryAgain() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onTryAgainClick();
                }
                setShowsDialog(false);
                dismiss();
            }
        };
    }

    @Override
    public void setShowsDialog(final boolean showsDialog) {
        super.setShowsDialog(showsDialog);
    }

    private View.OnClickListener dismissDialog() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowsDialog(false);
                dismiss();
            }
        };
    }

    public void setOnDialogClickListener(ErrorDialogListener pClickListener) {
        mClickListener = pClickListener;
    }
}
