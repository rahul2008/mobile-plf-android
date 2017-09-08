/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import android.app.DatePickerDialog;

import com.philips.platform.ths.base.THSBaseFragment;

import java.util.Calendar;
import java.util.Date;

public class THSDatePickerFragmentUtility{
    public static final String TAG = THSDatePickerFragmentUtility.class.getSimpleName();

    private Date date;
    private Calendar calendar;
    private THSBaseFragment mThsBaseFragment;
    private boolean showPreviousDates;

    public THSDatePickerFragmentUtility(THSBaseFragment thsBaseFragment, boolean showPreviousDates){
        this.mThsBaseFragment = thsBaseFragment;
        this.showPreviousDates = showPreviousDates;
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
        if(showPreviousDates) {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }
        datePickerDialog.show();

        datePickerDialog.setCancelable(false);
    }

    public void setCalendar(int year, int month, int day) {
        calendar.set(year,month,day);
        date.setTime(calendar.getTimeInMillis());
    }
}
