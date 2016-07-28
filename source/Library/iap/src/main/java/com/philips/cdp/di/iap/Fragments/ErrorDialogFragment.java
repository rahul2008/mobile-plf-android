/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.utils.IAPConstant;

public class ErrorDialogFragment extends DialogFragment {
    public interface ErrorDialogListener {
        void OnOkClickFromSomethingWentWrong();
    }

    private Button mOkBtn, mTryAgain;
    private ErrorDialogListener mClickListener;
    Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

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
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        onClickOfOK();
    }

    @Override
    public void setShowsDialog(final boolean showsDialog) {
        super.setShowsDialog(showsDialog);
    }

    private View.OnClickListener dismissDialog() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOfOK();
            }
        };
    }

    private void onClickOfOK() {
//        String error = bundle.getString(IAPConstant.MODEL_ALERT_ERROR_DESCRIPTION);
//        if (error != null && error.equals(getString(R.string.iap_time_out_error))) {
//            IAPLog.i(IAPLog.LOG, "SWITCH_TO_NO_NETWORK_CONNECTION");
//            addFragment(NoNetworkConnectionFragment.createInstance(bundle, BaseAnimationSupportFragment.AnimationType.NONE),
//                    NoNetworkConnectionFragment.TAG);
//        }
        setShowsDialog(false);
        dismiss();
    }

//    public void addFragment(BaseAnimationSupportFragment newFragment,
//                            String newFragmentTag) {
//
//        if (getActivity() != null && !getActivity().isFinishing()) {
//            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//            transaction.replace(getId(), newFragment, newFragmentTag);
//            transaction.addToBackStack(null);
//            transaction.commitAllowingStateLoss();
//
//            IAPLog.d(IAPLog.LOG, "Add fragment " + newFragment.getClass().getSimpleName() + "   ("
//                    + newFragmentTag + ")");
//        }
//    }

    // TODO: 08-06-2016  This must be avoided and code using this function must be moved to
    // basefragment. It can have side effects if this is the first fragment. It will replace the
    // vertical fragment. Please check for code duplication as well.
//    private int getPreviousFragmentID() {
//        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
//        List<Fragment> fragments = supportFragmentManager.getFragments();
//        int size = fragments.size();
//        return fragments.get(fragments.size() -1).getId();
//    }
}
