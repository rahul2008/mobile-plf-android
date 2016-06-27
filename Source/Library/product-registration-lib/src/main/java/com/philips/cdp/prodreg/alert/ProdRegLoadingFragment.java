package com.philips.cdp.prodreg.alert;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.uikit.modalalert.BlurDialogFragment;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegLoadingFragment extends BlurDialogFragment {

    public static ProdRegLoadingFragment newInstance(String title) {
        ProdRegLoadingFragment frag = new ProdRegLoadingFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setCancelable(false);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.prodreg_progress_dialog, null);
        ((TextView) view.findViewById(R.id.dialogDescription)).setText(title);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        setRetainInstance(true);
        return builder.create();
    }
}
