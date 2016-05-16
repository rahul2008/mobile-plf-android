package com.philips.cdp.digitalcare.locatephilips.models;

import com.philips.cdp.digitalcare.locatephilips.models.AtosLocationModel;

/**
 * AtosResultsModel is bean class for result JSON .
 * 
 * @author: ritesh.jha@philips.com
 * 
 * @since: 9 May 2015
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */
public class AtosResultsModel {
	private String mId = null;
	private String mTitle = null;
	private String mInfoType = null;
	private AtosLocationModel mLocationModel = null;
	private AtosAddressModel mAddressModel = null;

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		this.mId = id;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getInfoType() {
		return mInfoType;
	}

	public void setInfoType(String infoType) {
		this.mInfoType = infoType;
	}

	public AtosLocationModel getLocationModel() {
		return mLocationModel;
	}

	public void setLocationModel(AtosLocationModel locationModel) {
		this.mLocationModel = locationModel;
	}

	public AtosAddressModel getAddressModel() {
		return mAddressModel;
	}

	public void setAddressModel(AtosAddressModel addressModel) {
		this.mAddressModel = addressModel;
	}

}