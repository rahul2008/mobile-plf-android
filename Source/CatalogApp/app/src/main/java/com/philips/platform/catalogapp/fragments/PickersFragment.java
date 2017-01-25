/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.philips.platform.catalogapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PickersFragment extends BaseFragment {
    public static final String DATE_FORMATTER = "EEE, MMM d, yyyy";
    public static final String TIME_FORMATTER = "h:mm a";
    private com.philips.platform.catalogapp.databinding.FragmentPickersBinding fragmentPickersBinding;
    public final ObservableField<String> updatedDate = new ObservableField<String>();
    public final ObservableField<String> updatedTime = new ObservableField<String>();
    private Date date;
    private Calendar calendar;

    @Override
    public int getPageTitle() {
        return R.string.page_title_date_time_picker;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        fragmentPickersBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pickers, container, false);
        fragmentPickersBinding.setFragment(this);
        date = new Date(System.currentTimeMillis());
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        updatedDate.set(getFormattedDate(date));
        updatedTime.set(getFormattedTime(date));
        return fragmentPickersBinding.getRoot();
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

    private class MyOnDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(final DatePicker view, final int year, final int month, final int dayOfMonth) {
            calendar.set(year, month, dayOfMonth);
            updatedDate.set(getFormattedDate(calendar.getTime()));
        }
    }

    private String getFormattedDate(Date date) {
        return new SimpleDateFormat(DATE_FORMATTER, Locale.getDefault()).format(date);
    }

    private String getFormattedTime(Date date) {
        return new SimpleDateFormat(TIME_FORMATTER, Locale.getDefault()).format(date);
    }

    private class MyOnTimeSetListener implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(final TimePicker view, final int hourOfDay, final int minute) {
            calendar.set(date.getYear(), date.getMonth(), date.getDate(), hourOfDay, minute);
            updatedTime.set(getFormattedTime(calendar.getTime()));
        }
    }
}
