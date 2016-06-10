package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.uikit.modalalert.BlurDialogFragment;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ErrorDialogFragment extends BlurDialogFragment {
    public interface ErrorDialogListener {
        void OnOkClickFromSomethingWentWrong();
    }

    private Button mOkBtn, mTryAgain;
    private ErrorDialogListener mClickListener;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.iap_error_dialog, container, false);
        bundle = getArguments();
        TextView dialogTitle = (TextView) v.findViewById(R.id.dialogTitle);
        dialogTitle.setText(bundle.getString(IAPConstant.MODEL_ALERT_ERROR_TEXT));

        TextView errorDescription = (TextView) v.findViewById(R.id.dialogDescription);
        errorDescription.setText(bundle.getString(IAPConstant.MODEL_ALERT_ERROR_DESCRIPTION));

        mOkBtn = (Button) v.findViewById(R.id.btn_dialog_ok);
        mOkBtn.setText(bundle.getString(IAPConstant.MODEL_ALERT_BUTTON_TEXT));
        mOkBtn.setOnClickListener(dismissDialog());
        return v;
    }


    @Override
    public void setShowsDialog(final boolean showsDialog) {
        super.setShowsDialog(showsDialog);
    }

    private View.OnClickListener dismissDialog() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String error = bundle.getString(IAPConstant.MODEL_ALERT_ERROR_DESCRIPTION);
                if (error!=null && error.equals(getString(R.string.iap_time_out_error))) {
                    IAPLog.i(IAPLog.LOG, "SWITCH_TO_NO_NETWORK_CONNECTION");
                    addFragment(NoNetworkConnectionFragment.createInstance(bundle, BaseAnimationSupportFragment.AnimationType.NONE),
                            NoNetworkConnectionFragment.TAG);
                }
                setShowsDialog(false);
                dismiss();
            }
        };
    }

    public void addFragment(BaseAnimationSupportFragment newFragment,
                            String newFragmentTag) {

        if (getActivity() != null && !getActivity().isFinishing()) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_mainFragmentContainer, newFragment, newFragmentTag);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();

            IAPLog.d(IAPLog.LOG, "Add fragment " + newFragment.getClass().getSimpleName() + "   ("
                    + newFragmentTag + ")");
        }
    }

}
