/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.screens;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.ecs.demouapp.R;
import com.ecs.demouapp.ui.utils.ECSConstant;

import java.util.List;

public class ErrorDialogFragment extends DialogFragment {
    public interface ErrorDialogListener {
        void onDialogOkClick();
    }

    private Bundle bundle;
    private ErrorDialogListener mErrorDialogListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ecs_error_dialog, container, false);
        initializeViews(v);
        return v;
    }

    void initializeViews(View v) {
        bundle = getArguments();

        TextView dialogTitle = v.findViewById(R.id.dialogTitle);
        dialogTitle.setText(bundle.getString(ECSConstant.SINGLE_BUTTON_DIALOG_TITLE));

        final TextView dialogDescription = v.findViewById(R.id.dialogDescription);
        dialogDescription.setText(bundle.getString(ECSConstant.SINGLE_BUTTON_DIALOG_DESCRIPTION));

        Button mOkBtn = v.findViewById(R.id.btn_dialog_ok);
        mOkBtn.setText(bundle.getString(ECSConstant.SINGLE_BUTTON_DIALOG_TEXT));
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mErrorDialogListener != null) {
                    mErrorDialogListener.onDialogOkClick();
                }
                dismissDialog();
                handleEmptyScreen();
            }
        });
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void setShowsDialog(final boolean showsDialog) {
        super.setShowsDialog(showsDialog);
    }

    public void setErrorDialogListener(ErrorDialogListener dialogClickListener) {
        mErrorDialogListener = dialogClickListener;
    }

    private void dismissDialog() {
        dismiss();
        setShowsDialog(false);
    }

    public Fragment getVisibleFragment(FragmentManager fragmentManager){

        if(fragmentManager==null) return null;

        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {

                dismissDialog();
                handleEmptyScreen();

            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissDialog();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dismissDialog();
    }

    private void handleEmptyScreen() {
        if (bundle.getString(ECSConstant.SINGLE_BUTTON_DIALOG_DESCRIPTION) != null
                && bundle.getString(ECSConstant.SINGLE_BUTTON_DIALOG_DESCRIPTION).equals(getString(R.string.iap_something_went_wrong))) {
            getActivity().getFragmentManager().popBackStackImmediate();
        }
        if(getVisibleFragment(getFragmentManager())!=null && getVisibleFragment(getFragmentManager()) instanceof OrderSummaryFragment || getVisibleFragment(getFragmentManager()) instanceof ShoppingCartFragment ){
            getActivity().getFragmentManager().popBackStackImmediate();
        }

    }
}
