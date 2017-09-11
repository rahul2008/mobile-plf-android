/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ListAdapter;

import com.philips.cdp2.commlib.demouapp.R;

public class SelectorDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private int titleResId;
    private ListAdapter listAdapter;
    private OnDialogSelectorListener dialogSelectorListener;

    public interface OnDialogSelectorListener {
        void onSelectedOption(int index);
    }

    public static SelectorDialog newInstance(int titleResId, ListAdapter listAdapter, OnDialogSelectorListener dialogSelectorListener) {
        final SelectorDialog dialog = new SelectorDialog();
        dialog.titleResId = titleResId;
        dialog.listAdapter = listAdapter;
        dialog.dialogSelectorListener = dialogSelectorListener;

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        builder.setTitle(titleResId);
        builder.setSingleChoiceItems(listAdapter, ListAdapter.NO_SELECTION, this);
        builder.setNegativeButton(R.string.cml_cancel, this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_NEGATIVE:
                dialog.cancel();
                break;

            default:
                dialogSelectorListener.onSelectedOption(which);
                dialog.dismiss();

                break;
        }
    }
}
