package com.philips.cl.di.digitalcare.locatephilips;

/**
 * AddressModel is bean class for Address.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 9 May 2015
 */
public class AddressModel {
	private String mZip = null;
	private String mPhone = null;
	private String mState = null;
	private String mAddress1 = null;
	private String mAddress2 = null;
	private String mUrl = null;
	private String mCity = null;

	public String getZip() {
		return mZip;
	}

	public void setZip(String zip) {
		mZip = zip;
	}

	public String getPhone() {
		return mPhone;
	}

	public void setPhone(String phone) {
		mPhone = phone;
	}

	public String getState() {
		return mState;
	}

	public void setState(String state) {
		mState = state;
	}

	public String getAddress1() {
		return mAddress1;
	}

	public void setAddress1(String address1) {
		mAddress1 = address1;
	}

	public String getAddress2() {
		return mAddress2;
	}

	public void setAddress2(String address2) {
		mAddress2 = address2;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

	public String getCity() {
		return mCity;
	}

	public void setCity(String city) {
		mCity = city;
	}
}