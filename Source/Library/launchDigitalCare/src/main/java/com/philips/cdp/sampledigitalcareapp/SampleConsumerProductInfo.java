package com.philips.cdp.sampledigitalcareapp;

import com.philips.cdp.digitalcare.ConsumerProductInfo;

public class SampleConsumerProductInfo extends ConsumerProductInfo {
	private static String mGroup = "PERSONAL_CARE_GR";
	private static String mSector = "B2C";
	private static String mCatalog = "CARE";
	private static String mCategory = "HAIRCARE_CA";
	private static String mSubCategory = "HAIR_STYLERS_SU";
	private static String mCtn = "RQ1250/17";
	private static String mProductTitle = "HairStyler";

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

	public static void setGroup(String mGroup) {
		SampleConsumerProductInfo.mGroup = mGroup;
	}

	public static void setSector(String mSector) {
		SampleConsumerProductInfo.mSector = mSector;
	}

	public static void setCatalog(String mCatalog) {
		SampleConsumerProductInfo.mCatalog = mCatalog;
	}

	public static void setCategory(String mCategory) {
		SampleConsumerProductInfo.mCategory = mCategory;
	}

	public static void setSubCategory(String mSubCategory) {
		SampleConsumerProductInfo.mSubCategory = mSubCategory;
	}

	public static void setCtn(String mCtn) {
		SampleConsumerProductInfo.mCtn = mCtn;
	}

	public static void setProductTitle(String mProductTitle) {
		SampleConsumerProductInfo.mProductTitle = mProductTitle;
	}
}
