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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.uid.R;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

public class DialogTestFragment extends Fragment implements View.OnClickListener {
    private static final String DIALOG_TAG = "DIALOG_TAG";
    private static final String SHOW_DIVIDERS = "SHOW_DIVIDERS";

    public DialogTestFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        Drawable iconDrawable = new ColorDrawable(getResources().getColor(R.color.uidColorBlack));
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDialogStyle(DialogFragment.STYLE_NORMAL)
                .setMessage(com.philips.platform.uid.test.R.string.dialog_message)
                .setPositiveButton(com.philips.platform.uid.test.R.string.dialog_positive, this)
                .setNegativeButton(com.philips.platform.uid.test.R.string.dialog_negative, this)
                .setDialogLayout(com.philips.platform.uid.test.R.layout.dialog_container)
                .setIcon(iconDrawable)
                .setTitle(com.philips.platform.uid.test.R.string.dialog_title)
                .setDimLayer(DialogConstants.DIM_STRONG);

        final Bundle arguments = getArguments();
        if (arguments != null) {
            builder.setDividers(true);
            builder.setAlternateButton("Alternate", this);
        }
        final AlertDialogFragment alertDialogFragment = builder.setCancelable(false).create();
        alertDialogFragment.show(getFragmentManager(), DIALOG_TAG);
        return view;
    }

    @Override
    public void onClick(final View v) {
        ((AlertDialogFragment) getFragmentManager().findFragmentByTag(DIALOG_TAG)).dismiss();
    }

    public static DialogTestFragment create() {
        final Bundle bundle = new Bundle();
        bundle.putBoolean(SHOW_DIVIDERS, true);
        final DialogTestFragment dialogTestFragment = new DialogTestFragment();
        dialogTestFragment.setArguments(bundle);
        return dialogTestFragment;
    }

}
