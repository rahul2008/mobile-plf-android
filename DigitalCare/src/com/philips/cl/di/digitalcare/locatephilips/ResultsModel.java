package com.philips.cl.di.digitalcare.locatephilips;

/**
 * ResultsModel is bean class for result JSON .
 * 
 * @author: ritesh.jha@philips.com
 * 
 * @since: 9 May 2015
 */
public class ResultsModel {
	private String mId = null;
	private String mTitle = null;
	private String mInfoType = null;
	private LocationModel mLocationModel = null;
	private AddressModel mAddressModel = null;

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

	public LocationModel getLocationModel() {
		return mLocationModel;
	}

	public void setLocationModel(LocationModel locationModel) {
		this.mLocationModel = locationModel;
	}

	public AddressModel getmAddressModel() {
		return mAddressModel;
	}

	public void setAddressModel(AddressModel addressModel) {
		this.mAddressModel = addressModel;
	}

}