package com.philips.cl.di.digitalcare.contactus;
/*
 *	CdlsChat is bean class chat.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 16 Dec 2015
 */
public class CdlsChat {
	private String mOpeningHoursWeekdays = null;
	private String mOpeningHoursSaturday = null;
	private String mContent = null;

	public String getOpeningHoursWeekdays() {
		return mOpeningHoursWeekdays;
	}

	public void setOpeningHoursWeekdays(String mOpeningHoursWeekdays) {
		this.mOpeningHoursWeekdays = mOpeningHoursWeekdays;
	}

	public String getOpeningHoursSaturday() {
		return mOpeningHoursSaturday;
	}

	public void setOpeningHoursSaturday(String mOpeningHoursSaturday) {
		this.mOpeningHoursSaturday = mOpeningHoursSaturday;
	}

	public String getContent() {
		return mContent;
	}

	public void setContent(String mContent) {
		this.mContent = mContent;
	}
}

