package com.philips.cl.di.dev.pa.scheduler;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;
import android.widget.TimePicker;

import com.philips.cl.di.dev.pa.util.ALog;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {      
	
	//private Activity mActivity;
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
	
	@Override     
	public Dialog onCreateDialog(Bundle savedInstanceState) {         
		// Use the current time as the default values for the picker    
		ALog.i("Scheduler", "TimePickerFragment::onCreateDialog method beginning is called");
		final Calendar c = Calendar.getInstance();         
		int hour = c.get(Calendar.HOUR_OF_DAY);         
		int minute = c.get(Calendar.MINUTE);
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
	
	/*private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker picker, int selectedHour, int selectedMinute) {
			ALog.i("Scheduler", "Hours is " + selectedHour);
			ALog.i("Scheduler", "minute is " + selectedMinute);
			
			sSelectedTime = selectedHour + ":" + selectedMinute;
 
			// set current time into timepicker
			picker.setCurrentHour(selectedHour);
			picker.setCurrentMinute(selectedMinute); 
		}
	};*/
	
	/*public String getSelectedTime() {
		return sSelectedTime;
	}*/

}
