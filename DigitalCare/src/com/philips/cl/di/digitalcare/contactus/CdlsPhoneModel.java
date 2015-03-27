package com.philips.cl.di.digitalcare.contactus;

/**
 * CdlsPhoneModel is bean class for phone.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 16 Dec 2014
 */
public class CdlsPhoneModel {
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