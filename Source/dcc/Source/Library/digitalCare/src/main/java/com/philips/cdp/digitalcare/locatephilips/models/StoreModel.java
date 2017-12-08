package com.philips.cdp.digitalcare.locatephilips.models;

/**
 * StoreModel is bean class for stores.
 * 
 * @author: ritesh.jha@philips.com
 * 
 * @since: 9 May 2015
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */
public class StoreModel {
	private String mPhoneNumber = null;
	private String mOpeningHoursWeekdays = null;
	private String mOpeningHoursSaturday = null;
	private String mOpeningHoursSunday = null;

	public String getPhoneNumber() {
		return mPhoneNumber;
	}

	public void setPhoneNumber(String mPhoneNumber) {
		if (mPhoneNumber.contains("(")) {
			this.mPhoneNumber = mPhoneNumber.substring(0,
					mPhoneNumber.indexOf("("));
		} else {
			this.mPhoneNumber = mPhoneNumber;
		}
	}

	public String getOpeningHoursWeekdays() {
		return mOpeningHoursWeekdays;
	}

	public void setOpeningHoursWeekdays(String mOpeningHoursWeekdays) {
		this.mOpeningHoursWeekdays = mOpeningHoursWeekdays;
	}

	public String getOpeningHoursSaturday() {
		return mOpeningHoursSaturday;
	}

	public void setOpeningHoursSaturday(String mOpeningHoursSunday) {
		this.mOpeningHoursSaturday = mOpeningHoursSunday;
	}

	public String getOpeningHoursSunday() {
		return mOpeningHoursSunday;
	}

	public void setOpeningHoursSunday(String mOpeningHoursSunday) {
		this.mOpeningHoursSunday = mOpeningHoursSunday;
	}
}