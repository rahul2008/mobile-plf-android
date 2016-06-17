package com.philips.cdp.prodreg.alert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.uikit.modalalert.BlurDialogFragment;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegLoadingFragment extends BlurDialogFragment {

    private TextView descriptionTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.prodreg_progress_dialog, container, false);
        descriptionTextView = (TextView) v.findViewById(R.id.dialogDescription);
        return v;
    }

    public void setDescription(String description) {
        descriptionTextView.setText(description != null ? description : "");
    }
}
