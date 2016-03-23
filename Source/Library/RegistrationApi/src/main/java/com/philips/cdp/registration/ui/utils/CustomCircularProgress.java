package com.philips.cdp.registration.ui.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.philips.cdp.registration.R;

public class CustomCircularProgress extends ProgressDialog {


    public CustomCircularProgress(Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setCancelable(false);
        setContentView(R.layout.reg_custom_dialog);


    }

}
