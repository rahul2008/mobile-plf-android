package com.philips.platform.ths.appointment;

import android.app.DatePickerDialog;

import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerdetails.THSProviderEntity;

import java.util.Calendar;
import java.util.Date;

public class THSDatePickerFragmentUtility{
    public static final String TAG = THSDatePickerFragmentUtility.class.getSimpleName();

    private Date date;
    private Calendar calendar;
    THSBaseFragment mThsBaseFragment;

    public THSDatePickerFragmentUtility(THSBaseFragment thsBaseFragment){
        this.mThsBaseFragment = thsBaseFragment;
        initDateTimeFields();
    }


    private void initDateTimeFields() {
        date = new Date(System.currentTimeMillis());
        calendar = Calendar.getInstance();
        calendar.setTime(date);
    }

    public void showDatePicker(DatePickerDialog.OnDateSetListener listener) {
        calendar.setTime(date);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mThsBaseFragment.getActivity(),
                listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

        datePickerDialog.setCancelable(false);
    }

    public void setCalendar(int year, int month, int day) {
        calendar.set(year,month,day);
        date.setTime(calendar.getTimeInMillis());
    }
}
