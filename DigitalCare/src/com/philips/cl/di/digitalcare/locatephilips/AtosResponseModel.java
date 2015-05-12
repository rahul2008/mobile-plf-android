package com.philips.cl.di.digitalcare.locatephilips;

import java.util.ArrayList;

/**
 * CdlsResponseModel is bean class for all ATOS related objects.
 * 
 * @author: ritesh.jha@philips.com
 * 
 * @since: 9 May 2015
 */

public class AtosResponseModel {
	private Boolean mSuccess = null;
	private LocationModel mCurrentLocation = null;
	private ArrayList<ResultsModel> mResultsModel = null;
	private AtosErrorModel mCdlsErrorModel = null;

	public AtosResponseModel(boolean success, LocationModel locationModel,
			ArrayList<ResultsModel> resultsModel, AtosErrorModel errorModel) {
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

	public LocationModel getCurrentLocation() {
		return mCurrentLocation;
	}

	public void setCurrentLocation(LocationModel currentLocation) {
		this.mCurrentLocation = currentLocation;
	}

	public ArrayList<ResultsModel> getResultsModel() {
		return mResultsModel;
	}

	public void setResultsModel(ArrayList<ResultsModel> resultsModel) {
		this.mResultsModel = resultsModel;
	}
}
