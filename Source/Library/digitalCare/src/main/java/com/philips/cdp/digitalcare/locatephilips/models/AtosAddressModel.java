package com.philips.cdp.digitalcare.locatephilips.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * AtosAddressModel is bean class for Address.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 9 May 2015
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */
public class AtosAddressModel {
	private String mZip = null;
	private String mPhone = null;
	private String mAddress1 = null;
	private String mAddress2 = null;
	private String mUrl = null;
	private String mCityState = null;
	private ArrayList<String> mPhoneList = null;

	public String getZip() {
		return mZip;
	}

	public void setZip(String zip) {
		mZip = zip;
	}

	private void setPhoneList(ArrayList<String> phoneList) {
		mPhoneList = phoneList;
	}

	public ArrayList<String> getPhoneList() {
		return mPhoneList;
	}

	public String getPhone() {
		return mPhoneList.get(0);
	}

	public void setPhone(String phone) {
		mPhone = phone;
		mPhone = mPhone.replaceAll("-", "");
		List<String> list = Arrays.asList(mPhone.split(","));
		ArrayList<String> listPhone = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			listPhone.add(list.get(i));
		}
		setPhoneList(listPhone);
		// setPhoneList((ArrayList<String>)
		// Arrays.asList(mPhone.split(mPhone)));
	}

	public String getCityState() {
		return mCityState;
	}

	public void setCityState(String cityState) {
		mCityState = cityState;
	}

	// public String getState() {
	// return mState;
	// }
	//
	// public void setState(String state) {
	// mState = state;
	// }

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

	// public String getCity() {
	// return mCity;
	// }
	//
	// public void setCity(String city) {
	// mCity = city;
	// }
}