/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */


package com.philips.platform.uid.components.pickers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PickersFragment extends Fragment {
    public static final String DATE_FORMATTER = "EEE, MMM d, yyyy";
    public static final String TIME_FORMATTER = "h:mm a";
    private Date date;
    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(com.philips.platform.uid.test.R.layout.fragment_pickers, container, false);
        date = new Date(System.currentTimeMillis());
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        view.findViewById(com.philips.platform.uid.test.R.id.datePicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                showDatePicker();
            }
        });
        view.findViewById(com.philips.platform.uid.test.R.id.timePicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                showTimePicker();
            }
        });

        return view;
    }

    public void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getActivity(), new MyOnDateSetListener(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void showTimePicker() {
        date = calendar.getTime();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getActivity(), new MyOnTimeSetListener(), date.getHours(), date.getMinutes(), true);
        timePickerDialog.show();
    }

    private String getFormattedDate(Date date) {
        return new SimpleDateFormat(DATE_FORMATTER, Locale.getDefault()).format(date);
    }

    private String getFormattedTime(Date date) {
        return new SimpleDateFormat(TIME_FORMATTER, Locale.getDefault()).format(date);
    }

    private class MyOnDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(final DatePicker view, final int year, final int month, final int dayOfMonth) {
            calendar.set(year, month, dayOfMonth);
        }
    }

    private class MyOnTimeSetListener implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(final TimePicker view, final int hourOfDay, final int minute) {
            final Date time = calendar.getTime();
            calendar.set(time.getYear(), time.getMonth(), time.getDate(), hourOfDay, minute);
        }
    }
}
