package com.philips.platform.ths.appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.entity.consumer.RemindOptions;
import com.philips.platform.ths.R;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.RadioGroup;


public class THSSetReminderDialogFragment extends DialogFragment implements View.OnClickListener {

    public static String TAG = THSSetReminderDialogFragment.class.getSimpleName();
    protected RadioGroup radioGroup;
    protected THSDialogFragmentCallback<String> thsDialogFragmentCallback;

    public void setDialogFragmentCallback(THSDialogFragmentCallback<String> thsDialogFragmentCallback){
        this.thsDialogFragmentCallback = thsDialogFragmentCallback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_set_reminder_dialog_fragment, container, false);
        radioGroup = view.findViewById(R.id.ths_reminder_radio_group);
        Button set_reminder_confirmation_button = view.findViewById(R.id.set_reminder_confirmation_button);
        Button cancel_reminder_dialog = view.findViewById(R.id.cancel_reminder_dialog);
        set_reminder_confirmation_button.setOnClickListener(this);
        cancel_reminder_dialog.setOnClickListener(this);
        final RemindOptions reminderOptions = thsDialogFragmentCallback.getReminderOptions();
        setReminder(reminderOptions);
        return view;
    }

    public void setReminder(RemindOptions reminderOptions){
        switch (reminderOptions){
            case EIGHT_HOURS:
                radioGroup.check(R.id.ths_rb_eight_hours);
                break;
            case ONE_DAY:
                radioGroup.check(R.id.ths_rb_one_day);
                break;
            case NO_REMINDER:
                radioGroup.check(R.id.ths_rb_no_reminder);
                break;
            case FIFTEEN_MIN:
                radioGroup.check(R.id.ths_rb_15_mins);
                break;
            case ONE_HOUR:
                radioGroup.check(R.id.ths_rb_one_hour);
                break;
            case FOUR_HOURS:
                radioGroup.check(R.id.ths_rb_four_hours);
                break;
            case ONE_WEEK:
                radioGroup.check(R.id.ths_rb_one_week);
                break;
        }
        radioGroup.setSelected(true);
    }

    @Override
    public void onClick(View v) {
        int checkedRBId;
        String reminderValue = THSConstants.THS_NO_REMINDER_STRING;
        if (v.getId() == R.id.cancel_reminder_dialog) {
            dismiss();
        }
        if (v.getId() == R.id.set_reminder_confirmation_button) {
            checkedRBId = radioGroup.getCheckedRadioButtonId();

            if (checkedRBId == R.id.ths_rb_no_reminder) {
                reminderValue = THSConstants.THS_NO_REMINDER_STRING;
            }
            if (checkedRBId == R.id.ths_rb_15_mins) {
                reminderValue = THSConstants.THS_15_MINS_REMINDER;
            }
            if (checkedRBId == R.id.ths_rb_one_hour) {
                reminderValue = THSConstants.THS_ONE_HOUR_REMINDER;
            }
            if (checkedRBId == R.id.ths_rb_four_hours) {
                reminderValue = THSConstants.THS_FOUR_HOURS_REMINDER;
            }
            if (checkedRBId == R.id.ths_rb_eight_hours) {
                reminderValue = THSConstants.THS_EIGHT_HOURS_REMINDER;
            }
            if (checkedRBId == R.id.ths_rb_one_day) {
                reminderValue = THSConstants.THS_ONE_DAY_REMINDER;
            }
            if (checkedRBId == R.id.ths_rb_one_week) {
                reminderValue = THSConstants.THS_ONE_WEEK_REMINDER;
            }
            thsDialogFragmentCallback.onPostData(reminderValue);
            getDialog().dismiss();
        }
    }


}