/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.connectionestabilish;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.philips.cdp2.ews.R;

public class ConnectionEstablishDialogFragment extends DialogFragment {

    public static final String CONNECT_MSG = "connect.msg";

    public static ConnectionEstablishDialogFragment getInstance(@StringRes final int connectMessage) {
        final Bundle arguments = new Bundle();
        arguments.putInt(CONNECT_MSG, connectMessage);
        ConnectionEstablishDialogFragment dialogFragment = new ConnectionEstablishDialogFragment();
        dialogFragment.setArguments(arguments);
        return dialogFragment;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.ews_connecting_dialog, null);
        TextView textView = (TextView) view.findViewById(R.id.connecting_dialog_body);
        textView.setText(getArguments().getInt(CONNECT_MSG));
        builder.setView(view);
        setCancelable(false);
        return builder.create();
    }

    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        @SuppressWarnings("ConstantConditions")
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }
}
