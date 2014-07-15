package com.philips.cl.di.dev.pa.scheduler;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;

public class SchedulerConstants {
	
	public static final String BOOT_STRAP_ID_2 = "AwZmZm" ;
	public static final String CMA_APP_ID_2 = "kZWZkOD" ;
	//Fragment IDs
	public enum SchedulerID {
		OVERVIEW_EVENT,
		ADD_EVENT,
		DELETE_EVENT,
		REPEAT,
		FAN_SPEED,
	}
	
	public enum SCHEDULE_TYPE {
		ADD,
		DELETE,
		EDIT,
		GET,
		GET_SCHEDULE_DETAILS
	}
	
	public static final String ENABLED = "enabled";
	public static final String TIME = "time";
	public static final String EXTRAT_EDIT= "edit";
	public static final String DAYS = "days";
	public static final String PRODUCT = "product";
	public static final String PORT = "port";
	public static final String SPEED = "speed";
	public static final String MARKED_DELETION = "marked4del";
	public static final String COMMAND = "command";
	public static final String DIGITS = "0123456789";
	public static final String SUNDAY = PurAirApplication.getAppContext().getString(R.string.sunday);
	public static final String MONDAY = PurAirApplication.getAppContext().getString(R.string.monday);
	public static final String TUESDAY = PurAirApplication.getAppContext().getString(R.string.tuesday);
	public static final String WEDNESDAY = PurAirApplication.getAppContext().getString(R.string.wednesday);
	public static final String THURSDAY = PurAirApplication.getAppContext().getString(R.string.thursday);
	public static final String FRIDAY = PurAirApplication.getAppContext().getString(R.string.friday);
	public static final String SATURDAY = PurAirApplication.getAppContext().getString(R.string.saturday);
	public static final String EMPTY_STRING = "";
	public static final int SCHEDULER_COUNT = 5;
	public static final String ONE_TIME = PurAirApplication.getAppContext().getString(R.string.onetime);

	public static final String NAME = "Name";
	public static final String DEFAULT_FANSPEED_SCHEDULER = "a" ;
	public static final String TIMER_FRAGMENT_TAG = "timePicker" ;
	public static final String REPEAT_FRAGMENT_TAG = "RepeatFragment" ;
	public static final String FANSPEED_FRAGMENT_TAG = "FanspeedFragment" ;
	
	public static final char CHAR_ZERO = '0';
	public static final char CHAR_ONE = '1';
	public static final char CHAR_TWO = '2';
	public static final char CHAR_THREE = '3';
	public static final char CHAR_FOUR = '4';
	public static final char CHAR_FIVE = '5';
	public static final char CHAR_SIX = '6';
}

