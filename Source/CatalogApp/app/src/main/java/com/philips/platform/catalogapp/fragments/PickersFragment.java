package com.philips.platform.catalogapp.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentPickersBinding;

import java.util.Calendar;
import java.util.Date;

public class PickersFragment extends BaseFragment {
    private FragmentPickersBinding fragmentPickersBinding;

    @Override
    public int getPageTitle() {
        return R.string.page_title_date_time_picker;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        fragmentPickersBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pickers, container, false);
        fragmentPickersBinding.setFragment(this);
        return fragmentPickersBinding.getRoot();
    }

    public void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        final Date date = new Date(System.currentTimeMillis());

        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getActivity(), R.style.UIDDatePickerStyle, new MyOnDateSetListener(), calendar.getTime().getYear(), calendar.getTime().getMonth(), calendar.getTime().getDay());
        datePickerDialog.show();
    }

    public void showTimePicker() {
        final Date date = Calendar.getInstance().getTime();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getActivity(), R.style.UIDTimePickerStyle, new MyOnTimeSetListener(), date.getHours(), date.getMinutes(), true);
        timePickerDialog.show();
    }

    private class MyOnDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(final DatePicker view, final int year, final int month, final int dayOfMonth) {
            setDate(year, month, dayOfMonth);
        }
    }

    private void setDate(final int year, final int month, final int dayOfMonth) {
        //Need to use some default date formatter instead of this
        fragmentPickersBinding.datePicker.setText(String.format("%s:%s:%s", year, month, dayOfMonth));
    }

    private class MyOnTimeSetListener implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(final TimePicker view, final int hourOfDay, final int minute) {
            setTime(hourOfDay, minute);
        }
    }

    private void setTime(final int hourOfDay, final int minute) {
        //Need to use some default date formatter instead of this
        fragmentPickersBinding.timePicker.setText(String.format("%s:%s", hourOfDay, minute));
    }
}
