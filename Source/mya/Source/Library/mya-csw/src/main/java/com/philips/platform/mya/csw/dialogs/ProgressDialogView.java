/*
 * Copyright (c) 2018 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.dialogs;


import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;

import com.philips.platform.mya.csw.R;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

public class ProgressDialogView extends DialogView {
    public void showDialog(FragmentActivity activity) {
        super.showDialog(activity, null, null);
    }

    public void hideDialog() {
        if (alertDialogFragment != null) {
            alertDialogFragment.dismiss();
        }
    }

    @Override
    protected void setupView(FragmentActivity activity) {
        Context popupThemedContext = UIDHelper.getPopupThemedContext(activity);
        view = LayoutInflater
                .from(activity)
                .cloneInContext(popupThemedContext)
                .inflate(R.layout.csw_progress_dialog_connection, null, false);
    }

    @Override
    protected void showButton(FragmentActivity activity) {
        alertDialogFragment.show(activity.getSupportFragmentManager(), AlertDialogFragment.class.getCanonicalName());
    }

    @Override
    protected void setupTitleAndText(String title, String body) {
        // Progress bar does not have title and body
    }

    @Override
    protected void setupOkButton() {
        // Progress bar does not have an ok button
    }
}
