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

/**
 * Created by 310190722 on 29-Mar-16.
 */
public class XNumberPickerDialog implements NumberPicker.OnValueChangeListener
{
    private TextView mTvSelctedTitleAge;
    private NumberPickerListener mNumberPickerListener;

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
        Button ok = (Button) dialog.findViewById(R.id.reg_btn_ok);
        Button cancel = (Button) dialog.findViewById(R.id.reg_btn_cancel);
        mTvSelctedTitleAge = (TextView) dialog.findViewById(R.id.tv_reg_coppa_header_title);
        mTvSelctedTitleAge.setText(String.valueOf(maxVal));
        final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.reg_age_picker);
        numberPicker.setMinValue(minVal);
        numberPicker.setMaxValue(maxVal);
        numberPicker.setValue(maxVal);
        numberPicker.setOnValueChangedListener(this);

        setDividerColor(numberPicker,R.color.reg_devider_color);
        ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                numberPicker.clearFocus();
                System.out.println("number picker vacle"+numberPicker.getValue());

                if(mNumberPickerListener!=null){
                        mNumberPickerListener.onNumberSelect(numberPicker.getValue());
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
