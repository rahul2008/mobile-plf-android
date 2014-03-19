package com.philips.cl.di.common.ssdp.models;

import java.util.List;

/**
 * @author 310151556
 * @version $Revision: 1.0 $
 */
public class SSDPdevice {
	/**
	 * Field baseURL.
	 */
	private String mBaseURL;
	/**
	 * Field deviceType.
	 */
	private String mDeviceType;
	/**
	 * Field friendlyName.
	 */
	private String mFriendlyName;
	/**
	 * Field iconList.
	 */
	private List<Icon> mIconList;
	/**
	 * Field iconURL.
	 */
	private String mIconURL;
	/**
	 * Field manufacturer.
	 */
	private String mManufacturer;
	/**
	 * Field manufacturerURL.
	 */
	private String mManufacturerURL;
	/**
	 * Field modelDescription.
	 */
	private String mModelDescription;
	/**
	 * Field modelName.
	 */
	private String mModelName;
	/**
	 * Field modelNumber.
	 */
	private String mModelNumber;
	/**
	 * Field presentationURL.
	 */
	private String mPresentationURL;
	/**
	 * Field UDN.
	 */
	private String mUdn;
	/**
	 * Field UPC.
	 */
	private String mUPC;
	/**
	 * Field xScreen.
	 */
	private String mXScreen;

	/**
	 * Method equals.
	 * 
	 * @param obj
	 *            Object
	 * @return boolean
	 */
	@Override
	public boolean equals(final Object obj) {
		boolean retVal = false;
		if (obj instanceof SSDPdevice) {
			final SSDPdevice pdevice = (SSDPdevice) obj;
			if (pdevice.getManufacturer().equals(mManufacturer)) {
				retVal = true;
			}
		} else {
			retVal = super.equals(obj);
		}
		return retVal;
	}

	/**
	 * @return the mBaseURL
	 */
	public String getBaseURL() {
		return mBaseURL;
	}

	/**
	 * @return the mDeviceType
	 */
	public String getDeviceType() {
		return mDeviceType;
	}

	/**
	 * @return the mFriendlyName
	 */
	public String getFriendlyName() {
		return mFriendlyName;
	}

	/**
	 * @return the mIconList
	 */
	public List<Icon> getIconList() {
		return mIconList;
	}

	/**
	 * @return the mIconURL
	 */
	public String getIconURL() {
		return mIconURL;
	}

	/**
	 * @return the mManufacturer
	 */
	public String getManufacturer() {
		return mManufacturer;
	}

	/**
	 * @return the mManufacturerURL
	 */
	public String getManufacturerURL() {
		return mManufacturerURL;
	}

	/**
	 * @return the mModelDescription
	 */
	public String getModelDescription() {
		return mModelDescription;
	}

	/**
	 * @return the mModelName
	 */
	public String getModelName() {
		return mModelName;
	}

	/**
	 * @return the mModelNumber
	 */
	public String getModelNumber() {
		return mModelNumber;
	}

	/**
	 * @return the mPresentationURL
	 */
	public String getPresentationURL() {
		return mPresentationURL;
	}

	/**
	 * @return the mUdn
	 */
	public String getUdn() {
		return mUdn;
	}

	/**
	 * @return the mUPC
	 */
	public String getUPC() {
		return mUPC;
	}

	/**
	 * @return the mXScreen
	 */
	public String getXScreen() {
		return mXScreen;
	}

	/**
	 * Method hashCode.
	 * 
	 * @return int
	 */
	@Override
	public int hashCode() {
		return Integer.valueOf(mFriendlyName + mManufacturer);
	}

	/**
	 * @param mBaseURL
	 *            the mBaseURL to set
	 */
	public void setBaseURL(final String mBaseURL) {
		this.mBaseURL = mBaseURL;
	}

	/**
	 * @param mDeviceType
	 *            the mDeviceType to set
	 */
	public void setDeviceType(final String mDeviceType) {
		this.mDeviceType = mDeviceType;
	}

	/**
	 * @param mFriendlyName
	 *            the mFriendlyName to set
	 */
	public void setFriendlyName(final String mFriendlyName) {
		this.mFriendlyName = mFriendlyName;
	}

	/**
	 * @param mIconList
	 *            the mIconList to set
	 */
	public void setIconList(final List<Icon> mIconList) {
		this.mIconList = mIconList;
	}

	/**
	 * @param mIconURL
	 *            the mIconURL to set
	 */
	public void setIconURL(final String mIconURL) {
		this.mIconURL = mIconURL;
	}

	/**
	 * @param mManufacturer
	 *            the mManufacturer to set
	 */
	public void setManufacturer(final String mManufacturer) {
		this.mManufacturer = mManufacturer;
	}

	/**
	 * @param mManufacturerURL
	 *            the mManufacturerURL to set
	 */
	public void setManufacturerURL(final String mManufacturerURL) {
		this.mManufacturerURL = mManufacturerURL;
	}

	/**
	 * @param mModelDescription
	 *            the mModelDescription to set
	 */
	public void setModelDescription(final String mModelDescription) {
		this.mModelDescription = mModelDescription;
	}

	/**
	 * @param mModelName
	 *            the mModelName to set
	 */
	public void setModelName(final String mModelName) {
		this.mModelName = mModelName;
	}

	/**
	 * @param mModelNumber
	 *            the mModelNumber to set
	 */
	public void setModelNumber(final String mModelNumber) {
		this.mModelNumber = mModelNumber;
	}

	/**
	 * @param mPresentationURL
	 *            the mPresentationURL to set
	 */
	public void setPresentationURL(final String mPresentationURL) {
		this.mPresentationURL = mPresentationURL;
	}

	/**
	 * @param mUdn
	 *            the mUdn to set
	 */
	public void setUdn(final String mUdn) {
		this.mUdn = mUdn;
	}

	/**
	 * @param mUPC
	 *            the mUPC to set
	 */
	public void setUPC(final String mUPC) {
		this.mUPC = mUPC;
	}

	/**
	 * @param mXScreen
	 *            the mXScreen to set
	 */
	public void setXScreen(final String mXScreen) {
		this.mXScreen = mXScreen;
	}

	/**
	 * Method toString.
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("[mManufacturer ").append(mManufacturer).append("][ mModelName ");
		builder.append(mModelName).append("][ mUdn ").append(mUdn);
		builder.append("][ mDeviceType ").append(mDeviceType);
		builder.append(" ][mBaseURL ").append(mBaseURL).append("][ mPresentationURL");
		builder.append(mPresentationURL).append(']');
		return builder.toString();
	}
}
