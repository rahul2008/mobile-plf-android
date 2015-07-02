package com.philips.cl.di.digitalcare.contactus;
/**
 *	CdlsChatModel is bean class for chat.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since: 16 Dec 2014
 */
public class CdlsChatModel {
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

