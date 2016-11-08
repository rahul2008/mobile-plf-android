package com.philips.platform.uit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.uit.view.widget.AlertDialogFragment;

public class DialogTestFragment extends Fragment implements View.OnClickListener {
    private static final String ALERT_DIALOG_TAG = "ALERT_DIALOG_TAG";

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                .setMessage("Hello").
                        setPositiveButton("Positive", this).
                        setNegativeButton("Negative", this);
        final boolean isWithTitle = true;
        if (isWithTitle) {
            builder.setTitle("dialog_screen_title_text");
            final boolean showIcon = true;
            if (showIcon) {
                builder.setIcon(android.R.drawable.ic_menu_more);
            }
        }
        final AlertDialogFragment alertDialogFragment = builder.setCancelable(false).create();
        alertDialogFragment.show(getFragmentManager(), ALERT_DIALOG_TAG);
        return view;
    }

    @Override
    public void onClick(final View v) {
        ((AlertDialogFragment) getFragmentManager().findFragmentByTag(ALERT_DIALOG_TAG)).dismiss();
    }
}
