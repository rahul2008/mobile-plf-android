
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.dao;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;

public class ProductRegistrationInfo {

	private String mContractNumber = null;

	private String mDeviceID = null;

	private String mDeviceName = null;

	private String mExtendedWarranty = null;

	private String mIsGenerations = null;

	private String mIsPrimaryUser = null;

	private String mLastSolicitDate = null;

	private String mProductCatalogLocaleID = null;

	private String mProductID = null;

	private String mProductModelNumber = null;

	private String mProductRegistrationID = null;

	private String mProductSerialNumber = null;

	private String mPurchaseDate = null;

	private String mPurchasePlace = null;

	private String mRegistrationChannel = null;

	private String mRegistrationDate = null;

	private String mSlashWinCompetition = null;

	private String mUserUUID = null;

	private String mWarrantyInMonths = null;

	private String mCtn = null;

	private Sector mSector = null;

	private Catalog mCatalog = null;

	public String getContractNumber() {
		return mContractNumber;
	}

	public void setContractNumber(String contractNumber) {
		mContractNumber = contractNumber;
	}

	public String getDeviceID() {
		return mDeviceID;
	}

	public void setDeviceID(String deviceID) {
		mDeviceID = deviceID;
	}

	public String getDeviceName() {
		return mDeviceName;
	}

	public void setDeviceName(String deviceName) {
		mDeviceName = deviceName;
	}

	public String getExtendedwarranty() {
		return mExtendedWarranty;
	}

	public void setExtendedWarranty(String extendedWarranty) {
		mExtendedWarranty = extendedWarranty;
	}

	public String getIsGenerations() {
		return mIsGenerations;
	}

	public void setIsGenerations(String isGenerations) {
		mIsGenerations = isGenerations;
	}

	public String getIsPrimaryUser() {
		return mIsPrimaryUser;
	}

	public void setIsPrimaryUser(String isPrimaryUser) {
		mIsPrimaryUser = isPrimaryUser;
	}

	public String getLastSolicitDate() {
		return mLastSolicitDate;
	}

	public void setLastSolicitDate(String lastSolicitDate) {
		mLastSolicitDate = lastSolicitDate;
	}

	public String getProductCatalogLocaleID() {
		return mProductCatalogLocaleID;
	}

	public void setProductCatalogLocaleID(String productCatalogLocaleID) {
		mProductCatalogLocaleID = productCatalogLocaleID;
	}

	public String getProductID() {
		return mProductID;
	}

	public void setProductID(String productID) {
		mProductID = productID;
	}

	public String getProductModelNumber() {
		return mProductModelNumber;
	}

	public void setProductModelNumber(String productModelNumber) {
		mProductModelNumber = productModelNumber;
	}

	public String getProductRegistrationID() {
		return mProductRegistrationID;
	}

	public void setProductRegistrationID(String productRegistrationID) {
		mProductRegistrationID = productRegistrationID;
	}

	public String getProductSerialNumber() {
		return mProductSerialNumber;
	}

	public void setProductSerialNumber(String productSerialNumber) {
		mProductSerialNumber = productSerialNumber;
	}

	public String getPurchaseDate() {
		return mPurchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		mPurchaseDate = purchaseDate;
	}

	public String getPurchasePlace() {
		return mPurchasePlace;
	}

	public void setPurchasePlace(String purchasePlace) {
		mPurchasePlace = purchasePlace;
	}

	public String getRegistrationChannel() {
		return mRegistrationChannel;
	}

	public void setRegistrationChannel(String registrationChannel) {
		mRegistrationChannel = registrationChannel;
	}

	public String getRegistrationDate() {
		return mRegistrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		mRegistrationDate = registrationDate;
	}

	public String getSlashWinCompetition() {
		return mSlashWinCompetition;
	}

	public void setSlashWinCompetition(String slashWinCompetition) {
		mSlashWinCompetition = slashWinCompetition;
	}

	public String getUserUUID() {
		return mUserUUID;
	}

	public void setUserUUID(String userUUID) {
		mUserUUID = userUUID;
	}

	public String getWarrantyInMonths() {
		return mWarrantyInMonths;
	}

	public void setWarrantyInMonths(String warrantyInMonths) {
		mWarrantyInMonths = warrantyInMonths;
	}

	public String getCtn() {
		return mCtn;
	}

	public void setCtn(String ctn) {
		mCtn = ctn;
	}

	public Sector getSector() {
		return mSector;
	}

	public void setSector(Sector sector) {
		mSector = sector;
	}

	public Catalog getCatalog() {
		return mCatalog;
	}

	public void setCatalog(Catalog catalog) {
		mCatalog = catalog;
	}

}
