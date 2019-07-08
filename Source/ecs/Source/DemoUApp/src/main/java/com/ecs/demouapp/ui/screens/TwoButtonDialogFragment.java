package com.ecs.demouapp.ui.screens;///**
// * (C) Koninklijke Philips N.V., 2015.
// * All rights reserved.
// */
//package com.philips.cdp.di.iap.screens;
//
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.philips.cdp.di.iap.R;
//import com.philips.cdp.di.iap.utils.IAPConstant;
//
//public class TwoButtonDialogFragment extends DialogFragment {
//
//    public interface TwoButtonDialogListener {
//        void onPositiveButtonClicked();
//
//        void onNegativeButtonClicked();
//    }
//
//    private TwoButtonDialogListener mDialogClickListener;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.iap_two_button_dialog, container, false);
//
//        Bundle bundle = getArguments();
//
//        TextView dialogTitle = (TextView) v.findViewById(R.id.dialogTitle);
//        dialogTitle.setText(bundle.getString(IAPConstant.TWO_BUTTON_DIALOG_TITLE));
//
//        TextView description = (TextView) v.findViewById(R.id.dialogDescription);
//        description.setText(bundle.getString(IAPConstant.TWO_BUTTON_DIALOG_DESCRIPTION));
//
//        Button positiveButton = (Button) v.findViewById(R.id.dialogButtonOk);
//        positiveButton.setText(bundle.getString(IAPConstant.TWO_BUTTON_DIALOG_POSITIVE_TEXT));
//        positiveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mDialogClickListener != null) {
//                    mDialogClickListener.onPositiveButtonClicked();
//                }
//                dismissDialog();
//            }
//        });
//
//        Button negativeButton = (Button) v.findViewById(R.id.dialogButtonCancel);
//        negativeButton.setText(bundle.getString(IAPConstant.TWO_BUTTON_DIALOG_NEGATIVE_TEXT));
//        negativeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mDialogClickListener != null) {
//                    mDialogClickListener.onNegativeButtonClicked();
//                }
//                dismissDialog();
//            }
//        });
//
//        return v;
//    }
//
//    public void setOnDialogClickListener(TwoButtonDialogListener dialogClickListener) {
//        mDialogClickListener = dialogClickListener;
//    }
//
//    private void dismissDialog() {
//        dismiss();
//        setShowsDialog(false);
//    }
//}
