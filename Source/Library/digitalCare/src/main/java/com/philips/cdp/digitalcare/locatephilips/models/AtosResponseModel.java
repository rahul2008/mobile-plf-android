package com.philips.cdp.digitalcare.locatephilips.models;

import com.philips.cdp.digitalcare.locatephilips.models.AtosResultsModel;

import java.util.ArrayList;

/**
 * AtosResponseModel is bean class for all ATOS related objects.
 * 
 * @author: ritesh.jha@philips.com
 * 
 * @since: 9 May 2015
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */

public class AtosResponseModel {
	private Boolean mSuccess = null;
	private AtosLocationModel mCurrentLocation = null;
	private ArrayList<AtosResultsModel> mResultsModel = null;
	private AtosErrorModel mCdlsErrorModel = null;

	public AtosResponseModel(boolean success, AtosLocationModel locationModel,
			ArrayList<AtosResultsModel> resultsModel, AtosErrorModel errorModel) {
		mSuccess = success;
		mCurrentLocation = locationModel;
		mResultsModel = resultsModel;
		mCdlsErrorModel = errorModel;
	}

	public boolean getSuccess() {
		return mSuccess;
	}

	public void setSuccess(Boolean success) {
		this.mSuccess = success;
	}

	public AtosErrorModel getCdlsErrorModel() {
		return mCdlsErrorModel;
	}

	public void setCdlsErrorModel(AtosErrorModel cdlsErrorModel) {
		this.mCdlsErrorModel = cdlsErrorModel;
	}

	public AtosLocationModel getCurrentLocation() {
		return mCurrentLocation;
	}

	public void setCurrentLocation(AtosLocationModel currentLocation) {
		this.mCurrentLocation = currentLocation;
	}

	public ArrayList<AtosResultsModel> getResultsModel() {
		return mResultsModel;
	}

	public void setResultsModel(ArrayList<AtosResultsModel> resultsModel) {
		this.mResultsModel = resultsModel;
	}
}
