/**
 * LocateNearCustomDialog is the dialog which will be shown when there are no service centre
 * available at selected country.
 *
 * @author : ritesh.jha@philips.com
 * @since : 4 April 2016
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.locatephilips;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.customview.DigitalCareFontTextView;



public class LocateNearCustomDialog extends Dialog implements android.view.View.OnClickListener {
    private Context mContext = null;
    private DigitalCareFontTextView mTextViewOk = null;
    private DigitalCareFontTextView mTextViewSupport = null;
    private FragmentManager mFragmentManager = null;
    private GoToContactUsListener mGoToContactUsListener = null;

    public LocateNearCustomDialog(Activity context, FragmentManager fragmentManager,
                                  GoToContactUsListener goToContactUsListener) {
        super(context);
        mContext = context;
        mFragmentManager = fragmentManager;
        mGoToContactUsListener = goToContactUsListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.locate_philips_dialog);
        mTextViewOk = (DigitalCareFontTextView) findViewById(R.id.dialogTextViewOk);
        mTextViewOk.setOnClickListener(this);

        mTextViewSupport = (DigitalCareFontTextView) findViewById(R.id.dialogTextViewSupportPage);
        mTextViewSupport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dialogTextViewOk) {
            dismiss();
        } else if (i == R.id.dialogTextViewSupportPage) {
            dismiss();
            mGoToContactUsListener.goToContactUsSelected();
        }
    }
}