package com.philips.cl.di.dev.pa.datamodel;

public class OutdoorLocation {

	private int mAreaId;
	private String mCity;
	private String mDistrict;
	private String mProvince;
	private String mCountry;
	private String mStationType;
	private int mShortlist;

	public OutdoorLocation(int areaId, String city, String district,
			String province, String country, String stationType,
			boolean onShortList) {

		mAreaId = areaId;
		mCity = city;
		mDistrict = district;
		mProvince = province;
		mCountry = country;
		mStationType = stationType;
		setIsOnShortlist(onShortList);
	}

	public int getAreaId() {
		return mAreaId;
	}

	public void setAreaId(int areaId) {
		mAreaId = areaId;
	}

	public String getCity() {
		return mCity;
	}

	public void setCity(String city) {
		mCity = city;
	}

	public String getDistrict() {
		return mDistrict;
	}

	public void setDistrict(String district) {
		mDistrict = district;
	}

	public String getProvince() {
		return mProvince;
	}

	public void setProvince(String province) {
		mProvince = province;
	}

	public String getCountry() {
		return mCountry;
	}

	public void setCountry(String country) {
		mCountry = country;
	}

	public String getStationType() {
		return mStationType;
	}

	public void setStationType(String stationType) {
		mStationType = stationType;
	}

	public boolean getIsOnShortlist() {
		if (mShortlist == 0) {
			return false;
		} else {
			return true;
		}
	}

	public void setIsOnShortlist(boolean shortlist) {
		if (shortlist) {
			mShortlist = 1;
		} else {
			mShortlist = 0;
		}
	}
}
