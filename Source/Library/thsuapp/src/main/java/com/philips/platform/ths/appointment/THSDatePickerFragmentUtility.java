/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import android.app.DatePickerDialog;
import android.widget.ImageView;

import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.THSDateEnum;

import java.util.Calendar;
import java.util.Date;

public class THSDatePickerFragmentUtility {
    public static final String TAG = THSDatePickerFragmentUtility.class.getSimpleName();

    private Date date;
    private Calendar calendar;
    private THSBaseFragment mThsBaseFragment;
    private THSDateEnum thsDateEnum;

    public THSDatePickerFragmentUtility(THSBaseFragment thsBaseFragment, THSDateEnum thsDateEnum) {
        this.mThsBaseFragment = thsBaseFragment;
        this.thsDateEnum = thsDateEnum;
        initDateTimeFields();
    }

    public THSDatePickerFragmentUtility(THSBaseFragment thsBaseFragment) {
        this(thsBaseFragment,THSDateEnum.DEFAULT);
    }


    private void initDateTimeFields() {
        date = new Date(System.currentTimeMillis());
        calendar = Calendar.getInstance();
        calendar.setTime(date);
    }

    public void showDatePicker(DatePickerDialog.OnDateSetListener listener) {
        calendar.setTime(date);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mThsBaseFragment.getContext(),
                listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        //c.add( Calendar.MONTH, 6 );
        c.add(Calendar.DATE,179); // current + 179 = 180 days(6 month)
        long maxDate = c.getTime().getTime();

        if (thsDateEnum.getValue() == THSDateEnum.HIDEPREVIOUSDATE.getValue()) {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        } else if (thsDateEnum.getValue() == THSDateEnum.HIDEFUTUREDATE.getValue()) {
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        } else if(thsDateEnum.getValue()  == THSDateEnum.HIDESIXMONTHSLATERDATE.getValue()){
            datePickerDialog.getDatePicker().setMaxDate(maxDate);
        }else if (thsDateEnum.getValue() == THSDateEnum.HIDEPREVDATEANDSIXMONTHSLATERDATE.getValue()){
            datePickerDialog.getDatePicker().setMaxDate(maxDate);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }

        datePickerDialog.show();

        datePickerDialog.setCancelable(false);
    }

    public void setCalendar(int year, int month, int day) {
        calendar.set(year, month, day);
        date.setTime(calendar.getTimeInMillis());
    }
}
