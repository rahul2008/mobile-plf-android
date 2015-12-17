package com.philips.cdp.ui.catalog.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.philips.cdp.ui.catalog.R;

import java.util.Calendar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PickerActivity extends CatalogActivity {

    private int year, month, day;
    private Calendar calendar;
    private int hour;
    private int min;
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
        }
    };
    private TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickers);

        calendar = Calendar.getInstance();

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.time_picker:
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                min = calendar.get(Calendar.MINUTE);
                onCreateDialog(1).show();
                break;
            case R.id.date_picker:
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                onCreateDialog(0).show();
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 0)
            return new DatePickerDialog(this, myDateListener, year, month, day);
        else
            return new TimePickerDialog(this, onTimeSetListener, hour, min, false);
    }

}
