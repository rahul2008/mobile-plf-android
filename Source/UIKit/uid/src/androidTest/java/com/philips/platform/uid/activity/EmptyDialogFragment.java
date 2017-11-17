/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

public class EmptyDialogFragment extends Fragment implements View.OnClickListener {
    private static final String DIALOG_TAG = "DIALOG_TAG";
    private static final String SHOW_DIVIDERS = "SHOW_DIVIDERS";

    public EmptyDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setPositiveButton(com.philips.platform.uid.test.R.string.dialog_positive, this)
                .setDialogLayout(0)
                .setTitle(com.philips.platform.uid.test.R.string.dialog_title)
                .setDimLayer(DialogConstants.DIM_STRONG);
        final AlertDialogFragment alertDialogFragment = builder.setCancelable(false).create();
        alertDialogFragment.show(getFragmentManager(), DIALOG_TAG);
        alertDialogFragment.setAlternateButtonListener(this);
        alertDialogFragment.setPositiveButtonListener(this);
        alertDialogFragment.setNegativeButtonListener(this);
        return view;
    }

    @Override
    public void onClick(final View v) {
        ((AlertDialogFragment) getFragmentManager().findFragmentByTag(DIALOG_TAG)).dismiss();
    }

    public static EmptyDialogFragment create() {
        final Bundle bundle = new Bundle();
        bundle.putBoolean(SHOW_DIVIDERS, true);
        final EmptyDialogFragment emptyDialogFragment = new EmptyDialogFragment();
        emptyDialogFragment.setArguments(bundle);
        return emptyDialogFragment;
    }

}
