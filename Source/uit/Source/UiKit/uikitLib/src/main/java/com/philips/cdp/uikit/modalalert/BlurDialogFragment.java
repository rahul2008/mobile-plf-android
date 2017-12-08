/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.uikit.modalalert;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.philips.cdp.uikit.R;

/**
 * BlurDialogFragment enables you to get Blur background followed by dialog
 */

public class BlurDialogFragment extends DialogFragment {

    /**
     * This API enables to do initial resource initialization
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.UikitModalAlertTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.UikitModalAlertTheme;
    }
}