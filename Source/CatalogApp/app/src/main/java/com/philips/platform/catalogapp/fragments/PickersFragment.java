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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.philips.platform.catalogapp.BR;
import com.philips.platform.catalogapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PickersFragment extends BaseFragment {
    public static final String DATE_FORMATTER = "EEE, MMM d, yyyy";
    public static final String TIME_FORMATTER = "h:mm a";
    private com.philips.platform.catalogapp.databinding.FragmentPickersBinding fragmentPickersBinding;
    public final ObservableField<String> dateTimePickerDate = new ObservableField<String>();
    public final ObservableField<String> dateTimePickerTime = new ObservableField<String>();
    public final ObservableField<String> rangePickerStartDate = new ObservableField<String>();
    public final ObservableField<String> rangePickerStartTime = new ObservableField<String>();
    public final ObservableField<String> rangePickerEndDate = new ObservableField<String>();
    public final ObservableField<String> rangePickerEndTime = new ObservableField<String>();
    public final ObservableField<String> timeslotPickerTime = new ObservableField<String>();
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
        initDateTimeFields();

        return fragmentPickersBinding.getRoot();
    }

    private void initDateTimeFields() {
        fragmentPickersBinding.setVariable(BR.datePickerDate, dateTimePickerDate);
        fragmentPickersBinding.setVariable(BR.dateTimePickerTime, dateTimePickerTime);
        fragmentPickersBinding.setVariable(BR.rangePickerStartDate, rangePickerStartDate);
        fragmentPickersBinding.setVariable(BR.rangePickerStartTime, rangePickerStartTime);
        fragmentPickersBinding.setVariable(BR.rangePickerEndDate, rangePickerEndDate);
        fragmentPickersBinding.setVariable(BR.rangePickerEndTime, rangePickerEndTime);
        fragmentPickersBinding.setVariable(BR.timeslotPickerTime, timeslotPickerTime);

        date = new Date(System.currentTimeMillis());
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        final String formattedDate = getFormattedDate(date);
        final String formattedTime = getFormattedTime(date);
        dateTimePickerDate.set(formattedDate);
        dateTimePickerTime.set(formattedTime);
        rangePickerStartDate.set(formattedDate);
        rangePickerStartTime.set(formattedTime);
        rangePickerEndDate.set(formattedDate);
        rangePickerEndTime.set(formattedTime);
        timeslotPickerTime.set(formattedTime);
    }

    public void showDatePicker(@NonNull final ObservableField datePickerDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getActivity(), new DateSetListener(datePickerDate), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void showTimePicker(@NonNull final ObservableField timePickerTime) {
        date = calendar.getTime();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minutes = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getActivity(), new TimeSetListener(timePickerTime), hour, minutes, true);
        timePickerDialog.show();
    }

    private class DateSetListener implements DatePickerDialog.OnDateSetListener {
        final private ObservableField<String> dateValue;

        private DateSetListener(@NonNull final ObservableField<String> datePickerDate) {
            this.dateValue = datePickerDate;
        }

        @Override
        public void onDateSet(@NonNull final DatePicker view, final int year, final int month, final int dayOfMonth) {
            calendar.set(year, month, dayOfMonth);
            dateValue.set(getFormattedDate(calendar.getTime()));
        }
    }

    private String getFormattedDate(@NonNull Date date) {
        return new SimpleDateFormat(DATE_FORMATTER, Locale.getDefault()).format(date);
    }

    private String getFormattedTime(@NonNull Date date) {
        return new SimpleDateFormat(TIME_FORMATTER, Locale.getDefault()).format(date);
    }

    private class TimeSetListener implements TimePickerDialog.OnTimeSetListener {
        final private ObservableField<String> timeValue;

        private TimeSetListener(@NonNull final ObservableField timeValue) {
            this.timeValue = timeValue;
        }

        @Override
        public void onTimeSet(@NonNull final TimePicker view, final int hourOfDay, final int minute) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            calendar.set(year, month, day, hourOfDay, minute);
            final Date time = calendar.getTime();
            timeValue.set(getFormattedTime(time));
        }
    }
}
