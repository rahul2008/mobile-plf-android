package com.philips.cdp.digitalcare.locatephilips.models;

/**
 * AtosLocationModel is bean class for location.
 * 
 * @author: ritesh.jha@philips.com
 * 
 * @since: 9 May 2015
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */
public class AtosLocationModel {
	private String mLatitude = null;
	private String mLongitude = null;

	public String getLatitude() {
		return mLatitude;
	}

	public void setLatitude(String latitude) {
		this.mLatitude = latitude;
	}

	public String getLongitude() {
		return mLongitude;
	}

	public void setLongitude(String longitude) {
		this.mLongitude = longitude;
	}
}