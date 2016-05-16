/**
 * CdlsPhoneModel is bean class for phone.
 *
 * @author : Ritesh.jha@philips.com
 *
 * @since : 16 Dec 2014
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.contactus.models;


public class CdlsPhoneModel {
	private String mPhoneNumber = null;
	private String mOpeningHoursWeekdays = null;
	private String mOpeningHoursSaturday = null;
	private String mOpeningHoursSunday = null;
	private String mOptionalData1 = null;
	private String mOptionalData2 = null;
	private String mPhoneTariff=null;

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

    public String getmPhoneTariff() {
        return mPhoneTariff;
    }

    public void setmPhoneTariff(String mPhoneTariff) {
        this.mPhoneTariff = mPhoneTariff;
    }

    public String getOpeningHoursWeekdays() {
		return mOpeningHoursWeekdays.isEmpty() ? mOpeningHoursWeekdays
				: mOpeningHoursWeekdays + '\n';
	}

	public void setOpeningHoursWeekdays(String mOpeningHoursWeekdays) {
		this.mOpeningHoursWeekdays = mOpeningHoursWeekdays;
	}

	public String getOpeningHoursSaturday() {
		return mOpeningHoursSaturday.isEmpty() ? mOpeningHoursSaturday
				: mOpeningHoursSaturday + '\n';
	}

	public void setOpeningHoursSaturday(String mOpeningHoursSunday) {
		this.mOpeningHoursSaturday = mOpeningHoursSunday;
	}

	public String getOpeningHoursSunday() {
		return mOpeningHoursSunday.isEmpty() ? mOpeningHoursSunday
				: mOpeningHoursSunday + '\n';
	}

	public void setOpeningHoursSunday(String mOpeningHoursSunday) {
		this.mOpeningHoursSunday = mOpeningHoursSunday;
	}

	public String getOptionalData1() {
		return mOptionalData1.isEmpty() ? mOptionalData1
				: mOptionalData1 + '\n';
	}

	public void setOptionalData1(String optionalData) {
		this.mOptionalData1 = optionalData;
	}

	public String getOptionalData2() {
		return mOptionalData2.isEmpty() ? mOptionalData2
				: mOptionalData2 + '\n';
	}

	public void setOptionalData2(String optionalData) {
		this.mOptionalData2 = optionalData;
	}
}