package com.philips.cl.di.dev.pa.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;
import android.widget.TimePicker;

import com.philips.cl.di.dev.pa.util.ALog;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {      
	
	private int hourSelected = - 1;
	private int minSelected = - 1;
	private OnTimeSetListener mListener;
	
	@Override
    public void onAttach(Activity activity) {
		super.onAttach(activity);
        //mActivity = activity;        
        try {
            mListener = (OnTimeSetListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnTimeSetListener");
        }
    }
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override     
	public Dialog onCreateDialog(Bundle savedInstanceState) { 
		ALog.i("Scheduler", "TimePickerFragment::onCreateDialog method beginning is called");
		// Use the current time as the default values for the picker    
		final Calendar c = Calendar.getInstance();         
		int hour = c.get(Calendar.HOUR_OF_DAY);         
		int minute = c.get(Calendar.MINUTE);
		
		if (hourSelected != -1 || minSelected != -1) {
			hour = hourSelected;
			minute = minSelected;
		}
		
		ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Dialog);
		//TimePickerDialog picker = new TimePickerDialog(getActivity(), mListener, hour, minute, false);
		TimePickerDialog picker = new TimePickerDialog(wrapper, mListener, hour, minute, false);
	    picker.setCanceledOnTouchOutside(true);
	    ALog.i("Scheduler", "TimePickerFragment::onCreateDialog method ending is called");
	    return picker;		
	}      
	
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {         
		// Do something with the time chosen by the user  
		ALog.i("Scheduler", "Hours is " + hourOfDay);
		ALog.i("Scheduler", "minute is " + minute);
	}
	
	@SuppressLint("SimpleDateFormat")
	public void setTime(String time) {
		if (time == null || time.isEmpty()) return;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date date;
		try {
			date = sdf.parse(time);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			hourSelected = c.get(Calendar.HOUR_OF_DAY);
			
			minSelected = c.get(Calendar.MINUTE);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
