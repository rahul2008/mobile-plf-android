package com.philips.platform.csw.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

public class ProgressDialogFragment extends AlertDialogFragment implements DialogInterface.OnKeyListener {

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnKeyListener(this);
        return dialog;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog.dismiss();
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        }
        return true;
    }
}
