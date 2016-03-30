package com.philips.cdp.registration.coppa.ui.customviews;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.listener.NumberPickerListener;

/**
 * Created by 310190722 on 29-Mar-16.
 */
public class XNumberPickerDialog implements NumberPicker.OnValueChangeListener
{
    private TextView mTvSelctedTitleAge;
    private NumberPickerListener mNumberPickerListener;
    private int mNumberPickerChangedVal;

    public XNumberPickerDialog(NumberPickerListener numberPickerListener) {
        mNumberPickerListener = numberPickerListener;
    }


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        mNumberPickerChangedVal = newVal;
        mTvSelctedTitleAge.setText(String.valueOf(newVal));
    }

    public void showConfirmAgeDialog(Context context, int minVal, int maxVal)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_reg_coppa_age_verification);
        Button ok = (Button) dialog.findViewById(R.id.reg_btn_ok);
        Button cancel = (Button) dialog.findViewById(R.id.reg_btn_cancel);
        mTvSelctedTitleAge = (TextView) dialog.findViewById(R.id.tv_reg_age_title);
        if(minVal==0){
            minVal=1;
            mTvSelctedTitleAge.setText(String.valueOf(minVal));
        }
        mTvSelctedTitleAge.setText(String.valueOf(minVal));
        final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.reg_age_picker);
        numberPicker.setMinValue(minVal);
        numberPicker.setMaxValue(maxVal);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(this);
        ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(mNumberPickerListener!=null){
                    if(mNumberPickerChangedVal ==0){
                        mNumberPickerListener.onNumberSelect(numberPicker.getValue());
                    }else{
                        mNumberPickerListener.onNumberSelect(mNumberPickerChangedVal);
                    }
                }
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
