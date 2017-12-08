/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.philips.platform.catalogapp.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PickersFragment extends BaseFragment {
    public static final String DATE_FORMATTER = "EEE, MMM d, yyyy";
    public static final String TIME_FORMATTER = "h:mm a";
    public static final String PICKER_DATE = "PICKER_DATE";
    public static final String PICKER_START_DATE = "PICKER_START_DATE";
    public static final String PICKER_END_DATE = "PICKER_END_DATE";
    public static final String PICKER_TIMESLOT = "PICKER_TIMESLOT";
    public final ObservableField dateTimePickerDate = new ObservableField();
    public final ObservableField rangePickerStartDate = new ObservableField();
    public final ObservableField rangePickerEndDate = new ObservableField();
    public final ObservableField timeslotPickerTime = new ObservableField();
    public final ObservableInt disableTimePicker = new ObservableInt(View.GONE);
    public ObservableBoolean isValidRange = new ObservableBoolean(false);
    private com.philips.platform.catalogapp.databinding.FragmentPickersBinding fragmentPickersBinding;
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
        initDateTimeFields(savedInstanceState);

        return fragmentPickersBinding.getRoot();
    }

    private void initDateTimeFields(final Bundle savedInstanceState) {
        date = new Date(System.currentTimeMillis());
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        final Date formattedDate = date;
        final Date formattedTime = date;
        dateTimePickerDate.set(formattedDate);
        rangePickerStartDate.set(formattedDate);
        rangePickerEndDate.set(formattedDate);
        timeslotPickerTime.set(formattedTime);

        initFieldsUsingBundle(savedInstanceState);

        fragmentPickersBinding.setDateTimePickerDate(dateTimePickerDate);
        fragmentPickersBinding.setRangePickerStartDate(rangePickerStartDate);
        fragmentPickersBinding.setRangePickerEndDate(rangePickerEndDate);
        fragmentPickersBinding.setTimeslotPickerTime(timeslotPickerTime);
    }

    private void initFieldsUsingBundle(final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Date pickerDate = (Date) savedInstanceState.getSerializable(PICKER_DATE);
            Date pickerStartDate = (Date) savedInstanceState.getSerializable(PICKER_START_DATE);
            Date pickerEndDate = (Date) savedInstanceState.getSerializable(PICKER_END_DATE);
            Date pickerTimeslot = (Date) savedInstanceState.getSerializable(PICKER_TIMESLOT);

            dateTimePickerDate.set(pickerDate);
            rangePickerStartDate.set(pickerStartDate);
            rangePickerEndDate.set(pickerEndDate);
            timeslotPickerTime.set(pickerTimeslot);
            updateHasError();
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putSerializable(PICKER_DATE, (Serializable) dateTimePickerDate.get());
        outState.putSerializable(PICKER_START_DATE, (Serializable) rangePickerStartDate.get());
        outState.putSerializable(PICKER_END_DATE, (Serializable) rangePickerEndDate.get());
        outState.putSerializable(PICKER_TIMESLOT, (Serializable) timeslotPickerTime.get());
        super.onSaveInstanceState(outState);
    }

    public void showDatePicker(final Object datePickerDate, final boolean isRangeType) {
        calendar.setTime((Date) ((ObservableField) datePickerDate).get());
        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getActivity(), new DateSetListener((ObservableField<Date>) datePickerDate, isRangeType), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void showTimePicker(@NonNull final Object timePickerTime, final boolean isRangeType) {

        calendar.setTime((Date) ((ObservableField) timePickerTime).get());
        date = calendar.getTime();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minutes = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getActivity(), new TimeSetListener((ObservableField) timePickerTime, isRangeType), hour, minutes, true);
        timePickerDialog.show();
    }

    public String getFormattedDate(@NonNull Object date) {
        return new SimpleDateFormat(DATE_FORMATTER, Locale.getDefault()).format(date);
    }

    public String getFormattedTime(@NonNull Object date) {
        return new SimpleDateFormat(TIME_FORMATTER, Locale.getDefault()).format(date);
    }

    private void updateHasError() {
        isValidRange.set(((Date) rangePickerStartDate.get()).after((Date) rangePickerEndDate.get()));
    }

    public int getTextColor() {
        int color = Color.RED;
        if (!isValidRange.get()) {
            TypedArray typedArray = getActivity().getTheme().obtainStyledAttributes(new int[]{R.attr.uidContentItemPrimaryNormalTextColor});
            if (typedArray != null) {
                color = typedArray.getColor(0, Color.WHITE);
                typedArray.recycle();
            }
        }
        return color;
    }

    private class DateSetListener implements DatePickerDialog.OnDateSetListener {
        final private ObservableField<Date> dateValue;
        final private boolean isRangeType;

        private DateSetListener(@NonNull final ObservableField<Date> datePickerDate, final boolean isRangeType) {
            this.dateValue = datePickerDate;
            this.isRangeType = isRangeType;
        }

        @Override
        public void onDateSet(@NonNull final DatePicker view, final int year, final int month, final int dayOfMonth) {
            calendar.set(year, month, dayOfMonth);
            dateValue.set(calendar.getTime());
            if (isRangeType) {
                updateHasError();
            }
        }
    }

    private class TimeSetListener implements TimePickerDialog.OnTimeSetListener {
        final private ObservableField<Date> timeValue;
        final private boolean isRangeType;

        private TimeSetListener(@NonNull final ObservableField timeValue, final boolean isRangeType) {
            this.timeValue = timeValue;
            this.isRangeType = isRangeType;
        }

        @Override
        public void onTimeSet(@NonNull final TimePicker view, final int hourOfDay, final int minute) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            calendar.set(year, month, day, hourOfDay, minute);
            final Date time = calendar.getTime();
            timeValue.set(time);
            if (isRangeType) {
                updateHasError();
            }
        }
    }
}
