/**
 *	CdlsChatModel is bean class for chat.
 *
 * @author : Ritesh.jha@philips.com
 *
 * @since: 16 Dec 2014
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.contactus.models;

public class CdlsChatModel {
	private String mOpeningHoursWeekdays = null;
	private String mOpeningHoursSaturday = null;
	private String mContent = null;
	private String mScript=null;

	public void setScript(String mScript) {
		this.mScript = mScript;
	}

	public String getScript() {
		return mScript;
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

