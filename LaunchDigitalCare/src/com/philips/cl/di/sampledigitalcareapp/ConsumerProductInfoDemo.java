package com.philips.cl.di.sampledigitalcareapp;

import com.philips.cl.di.digitalcare.ConsumerProductInfo;

public class ConsumerProductInfoDemo extends ConsumerProductInfo {
	private static String mGroup = "PERSONAL_CARE_GR";
	private static String mSector = "B2C";
	private static String mCatalog = "CARE";
	private static String mCategory = "MENS_SHAVING_CA";
	private static String mSubCategory = "HAIR_STYLERS_SU";
	private static String mCtn = "HD8967_01";
	private static String mProductTitle = "PRODUCT TITLE";

	@Override
	public String getGroup() {
		return mGroup;
	}

	@Override
	public String getSector() {
		return mSector;
	}

	@Override
	public String getCatalog() {
		return mCatalog;
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
