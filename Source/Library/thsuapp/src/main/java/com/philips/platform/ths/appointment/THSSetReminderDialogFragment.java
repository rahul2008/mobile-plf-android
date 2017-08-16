package com.philips.platform.ths.appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.ths.R;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.RadioGroup;

public class THSSetReminderDialogFragment extends DialogFragment implements View.OnClickListener {

    public static String TAG = THSSetReminderDialogFragment.class.getSimpleName();
    private RadioGroup radioGroup;
    private Button set_reminder_confirmation_button, cancel_reminder_dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_set_reminder_dialog_fragment, container, false);
        radioGroup = (RadioGroup) view.findViewById(R.id.ths_reminder_radio_group);
        set_reminder_confirmation_button = (Button) view.findViewById(R.id.set_reminder_confirmation_button);
        cancel_reminder_dialog = (Button) view.findViewById(R.id.cancel_reminder_dialog);
        set_reminder_confirmation_button.setOnClickListener(this);
        cancel_reminder_dialog.setOnClickListener(this);
        radioGroup.check(R.id.ths_rb_15_mins);
        return view;
    }

    @Override
    public void onClick(View v) {
        int checkedRBId, reminderValue;
        if (v.getId() == R.id.cancel_reminder_dialog) {
            dismiss();
        }
        if (v.getId() == R.id.set_reminder_confirmation_button) {
            checkedRBId = radioGroup.getCheckedRadioButtonId();

            if (checkedRBId == R.id.ths_rb_no_reminder) {
                reminderValue = 0;
            }
            if (checkedRBId == R.id.ths_rb_15_mins) {
                reminderValue = 0
            }
            if (checkedRBId == R.id.ths_rb_one_hour) {
                reminderValue = 0
            }
            if (checkedRBId == R.id.ths_rb_four_hours) {
                reminderValue = 0
            }
            if (checkedRBId == R.id.ths_rb_eight_hours) {
                reminderValue = 0
            }
            if (checkedRBId == R.id.ths_rb_one_day) {
                reminderValue = 0
            }
            if (checkedRBId == R.id.ths_rb_one_week) {
                reminderValue = 0
            }

        }
    }
}
