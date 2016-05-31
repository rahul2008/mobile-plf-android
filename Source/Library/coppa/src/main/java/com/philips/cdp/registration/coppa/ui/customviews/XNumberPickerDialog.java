/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.ui.customviews;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.listener.NumberPickerListener;


public class XNumberPickerDialog implements NumberPicker.OnValueChangeListener
{
    private TextView mTvSelctedTitleAge;
    private NumberPickerListener mNumberPickerListener;

    private  NumberPicker mNumberPicker;

    public XNumberPickerDialog(NumberPickerListener numberPickerListener) {
        mNumberPickerListener = numberPickerListener;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        mTvSelctedTitleAge.setText(String.valueOf(newVal));
    }

    public void showNumberPickerDialog(Context context, int minVal, int maxVal)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_reg_coppa_age_verification);
        dialog.setCancelable(false);
        Button ok = (Button) dialog.findViewById(R.id.reg_btn_ok);
        Button cancel = (Button) dialog.findViewById(R.id.reg_btn_cancel);
        mTvSelctedTitleAge = (TextView) dialog.findViewById(R.id.tv_reg_coppa_header_title);
        mTvSelctedTitleAge.setText(String.valueOf(maxVal));
        mNumberPicker= (NumberPicker) dialog.findViewById(R.id.reg_age_picker);
        mNumberPicker.setMinValue(minVal);
        mNumberPicker.setMaxValue(maxVal);
        mNumberPicker.setValue(maxVal);
        mNumberPicker.setOnValueChangedListener(this);

        setDividerColor(mNumberPicker,R.color.reg_devider_color);
        ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                mNumberPicker.clearFocus();
                if(mNumberPickerListener!=null){
                        mNumberPickerListener.onNumberSelect(mNumberPicker.getValue());
                }
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

public void setInitialValue(int value){
    mNumberPicker.setValue(value);
    mTvSelctedTitleAge.setText(String.valueOf(value));
}


    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
