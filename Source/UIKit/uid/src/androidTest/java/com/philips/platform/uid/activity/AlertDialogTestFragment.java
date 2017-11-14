/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.activity;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.uid.R;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

public class AlertDialogTestFragment extends Fragment implements View.OnClickListener {
    private static final String ALERT_DIALOG_TAG = "ALERT_DIALOG_TAG";
    public static final String SHOW_TITLE = "SHOW_TITLE";

    public AlertDialogTestFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        View dialogLayout = inflater.inflate(com.philips.platform.uid.test.R.layout.dialog_container, container);
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                .setMessage("Hello").
                        setPositiveButton("Positive", this).
                        setNegativeButton("Negative", this)
                .setDialogView(dialogLayout);
        final Bundle arguments = getArguments();
        if (arguments == null) {
            builder.setTitle("dialog_screen_title_text");
            builder.setIcon(android.R.drawable.ic_menu_more);
        }
        final AlertDialogFragment alertDialogFragment = builder.setCancelable(false).create();
        alertDialogFragment.show(getFragmentManager(), ALERT_DIALOG_TAG);
        return view;
    }

    @Override
    public void onClick(final View v) {
        ((AlertDialogFragment) getFragmentManager().findFragmentByTag(ALERT_DIALOG_TAG)).dismiss();
    }

    public static AlertDialogTestFragment create() {
        final Bundle bundle = new Bundle();
        bundle.putBoolean(SHOW_TITLE, true);
        final AlertDialogTestFragment alertDialogTestFragment = new AlertDialogTestFragment();
        alertDialogTestFragment.setArguments(bundle);
        return alertDialogTestFragment;
    }
}
