package com.philips.cl.di.sampledigitalcareapp;

import com.philips.cl.di.digitalcare.ConsumerProductInfo;

public class ConsumerProductInfoDemo extends ConsumerProductInfo {
	private static String mGroup = "PERSONAL_CARE_GR";
	private static String mSector = "CARE";
	private static String mCategory = "MENS_SHAVING_CA";
	private static String mSubCategory = "XSMALL_SU";
	private static String mCtn = "*";
	private static String mProductTitle = "PRODCUT TITLE";

	@Override
	public String getGroup() {
		return mGroup;
	}

	@Override
	public String getSector() {
		return mSector;
	}

	@Override
	public String getCategory() {
		return mCategory;
	}

	@Override
	public String getSubCategory() {
		return mSubCategory;
	}

	@Override
	public String getCtn() {
		return mCtn;
	}

	@Override
	public String getProductTitle() {
		return mProductTitle;
	}
}
