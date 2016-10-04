
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

/**
 * Class Product registration info
 */
public class ProductRegistrationInfo {

    /* Contract number*/
	private String mContractNumber = null;

	/* Device id */
	private String mDeviceID = null;

	/* Device name */
	private String mDeviceName = null;

	/* Extended warranty */
	private String mExtendedWarranty = null;

	/* Generations */
	private String mIsGenerations = null;

	/* primary user */
	private String mIsPrimaryUser = null;

	/* last solicit date */
	private String mLastSolicitDate = null;

	/* product catalog locale id */
	private String mProductCatalogLocaleID = null;

	/* product id */
	private String mProductID = null;

	/* product mode number */
	private String mProductModelNumber = null;

	/* product registration id */
	private String mProductRegistrationID = null;

	/* product serial number */
	private String mProductSerialNumber = null;

	/* purchase date */
	private String mPurchaseDate = null;

	/* purchase place */
	private String mPurchasePlace = null;

	/* registration channel */
	private String mRegistrationChannel = null;

	/* registration date */
	private String mRegistrationDate = null;

	/* slash win competition*/
	private String mSlashWinCompetition = null;

	/* user uuid */
	private String mUserUUID = null;

	/* warranty in months */
	private String mWarrantyInMonths = null;

	/* */
	private String mCtn = null;

	/* Sector */
	private Sector mSector = null;

	/* Catelog */
	private Catalog mCatalog = null;

	/**
	 * {@code getContractNumber} method to get contract number
	 * @return mContractNumber
     */
	public String getContractNumber() {
		return mContractNumber;
	}

	/**
	 * {@code setContractNumber}set contract number
	 * @param contractNumber contract number
     */
	public void setContractNumber(String contractNumber) {
		mContractNumber = contractNumber;
	}

	/**
	 * {@code getDeviceID} method to get device id
	 * @return mDeviceID device id
     */
	public String getDeviceID() {
		return mDeviceID;
	}

	/**
	 * {@code setDeviceID} method to set device id
	 * @param deviceID Device id
     */
	public void setDeviceID(String deviceID) {
		mDeviceID = deviceID;
	}

	/**
	 * {@code getDeviceName} method to get device name
	 * @return mDeviceName Device name
     */
	public String getDeviceName() {
		return mDeviceName;
	}

	/**
	 * {@code setDeviceName} method to set device name
	 * @param deviceName device name
     */
	public void setDeviceName(String deviceName) {
		mDeviceName = deviceName;
	}

	/**
	 *  {@code getExtendedwarranty} method to get extended warranty
	 * @return mExtendedWarranty extended warranty
     */
	public String getExtendedwarranty() {
		return mExtendedWarranty;
	}

	/**
	 * {@code setExtendedWarranty} method to set extended warranty
	 * @param extendedWarranty   extended warranty
     */
	public void setExtendedWarranty(String extendedWarranty) {
		mExtendedWarranty = extendedWarranty;
	}

	/**
	 * {@code getIsGenerations} method to get generations
	 * @return mIsGenerations
     */
	public String getIsGenerations() {
		return mIsGenerations;
	}

	/**
	 * {@code setIsGenerations} method to set  generations
	 * @param isGenerations
     */
	public void setIsGenerations(String isGenerations) {
		mIsGenerations = isGenerations;
	}

	/**
	 * {@code getIsPrimaryUser} method to get is primary user
	 * @return mIsPrimaryUser true if primary user else false
     */
	public String getIsPrimaryUser() {
		return mIsPrimaryUser;
	}

	/**
	 * {@code setIsPrimaryUser} method to set is primary user
	 * @param isPrimaryUser validating the user
     */
	public void setIsPrimaryUser(String isPrimaryUser) {
		mIsPrimaryUser = isPrimaryUser;
	}

	/**
	 * {@code getLastSolicitDate} method to get last solicit date
	 * @return mLastSolicitDate last solicit date
     */
	public String getLastSolicitDate() {
		return mLastSolicitDate;
	}

	/**
	 * {@code setLastSolicitDate} method to set last solicit date
	 * @param lastSolicitDate last solicit date
     */
	public void setLastSolicitDate(String lastSolicitDate) {
		mLastSolicitDate = lastSolicitDate;
	}

	/**
	 * {@code getProductCatalogLocaleID} method to get product catalog locale id
	 * @return mProductCatalogLocaleID product catalog locale id
     */
	public String getProductCatalogLocaleID() {
		return mProductCatalogLocaleID;
	}

	/**
	 * {@code setProductCatalogLocaleID} method to set product catalog locale id
	 * @param productCatalogLocaleID product catalog locale id
     */
	public void setProductCatalogLocaleID(String productCatalogLocaleID) {
		mProductCatalogLocaleID = productCatalogLocaleID;
	}

	/**
	 * {@code getProductID} method to get product id
	 * @return mProductID product id
     */
	public String getProductID() {
		return mProductID;
	}

	/**
	 * {@code setProductID} method to set product id
	 * @param productID product id
     */
	public void setProductID(String productID) {
		mProductID = productID;
	}

	/**
	 * {@code getProductModelNumber} method to get product model number
	 * @return mProductModelNumber product model number
     */
	public String getProductModelNumber() {
		return mProductModelNumber;
	}

	/**
	 * {@code setProductModelNumber} method to set product model number
	 * @param productModelNumber product model number
     */
	public void setProductModelNumber(String productModelNumber) {
		mProductModelNumber = productModelNumber;
	}

	/**
	 * {@code getProductRegistrationID } method to get product registration id
	 * @return mProductRegistrationID product registration id
     */
	public String getProductRegistrationID() {
		return mProductRegistrationID;
	}

	/**
	 * {@code setProductRegistrationID} method to set product registration id
	 * @param productRegistrationID product registration Id
     */
	public void setProductRegistrationID(String productRegistrationID) {
		mProductRegistrationID = productRegistrationID;
	}

	/**
	 * {@code getProductSerialNumber} method to get product serial number
	 * @return mProductSerialNumber product serial number
     */
	public String getProductSerialNumber() {
		return mProductSerialNumber;
	}

	/**
	 * {@code setProductSerialNumber } method to set product serial number
	 * @param productSerialNumber product serial number
     */
	public void setProductSerialNumber(String productSerialNumber) {
		mProductSerialNumber = productSerialNumber;
	}

	/**
	 * {@code getPurchaseDate} method to get purchase date
	 * @return mPurchaseDate purchase date
     */
	public String getPurchaseDate() {
		return mPurchaseDate;
	}

	/**
	 * {@code setPurchaseDate} method to set purchase date
	 * @param purchaseDate purchase date
     */
	public void setPurchaseDate(String purchaseDate) {
		mPurchaseDate = purchaseDate;
	}

	/**
	 * {@code getPurchasePlace } method to get purchase place
	 * @return mPurchasePlace purchase place
     */
	public String getPurchasePlace() {
		return mPurchasePlace;
	}

	/**
	 * {@code setPurchasePlace} method to set purchase place
	 * @param purchasePlace purchase place
     */
	public void setPurchasePlace(String purchasePlace) {
		mPurchasePlace = purchasePlace;
	}

	/**
     * {@code getRegistrationChannel } method to get regitration channel
	 * @return mRegistrationChannel
     */
	public String getRegistrationChannel() {
		return mRegistrationChannel;
	}

	/**
	 * {@code setRegistrationChannel} method to set registration channel
	 * @param registrationChannel
     */
	public void setRegistrationChannel(String registrationChannel) {
		mRegistrationChannel = registrationChannel;
	}

	/**
	 * {@code getRegistrationDate} method  to get registration date
	 * @return mRegistrationDate registration date
     */
	public String getRegistrationDate() {
		return mRegistrationDate;
	}

	/**
	 * {@code setRegistrationDate} method to set registration date
	 * @param registrationDate    registration date
     */
	public void setRegistrationDate(String registrationDate) {
		mRegistrationDate = registrationDate;
	}

	/**
	 * {@code getSlashWinCompetition} method to get slash win competition
	 * @return mSlashWinCompetition Slash win competition
     */
	public String getSlashWinCompetition() {
		return mSlashWinCompetition;
	}

	/**
     *
	 * {@code setSlashWinCompetition} method to set slash win competition
	 * @param slashWinCompetition    Slash win competition
     */
	public void setSlashWinCompetition(String slashWinCompetition) {
		mSlashWinCompetition = slashWinCompetition;
	}

	/**
	 * {@code getUserUUID} method to get user uuid
	 * @return mUserUUID user uuid
     */
	public String getUserUUID() {
		return mUserUUID;
	}

	/**
     * {@code setUserUUID} method to set user uuid
	 * @param userUUID    user uuid
     */
	public void setUserUUID(String userUUID) {
		mUserUUID = userUUID;
	}

	/**
	 * {@code getWarrantyInMonths} method to get warranty in months
	 * @return mWarrantyInMonths Warrently in months
     */
	public String getWarrantyInMonths() {
		return mWarrantyInMonths;
	}

	/**
	 * {@code setWarrantyInMonths} method to set warranty in months
	 * @param warrantyInMonths Warranty in months
     */
	public void setWarrantyInMonths(String warrantyInMonths) {
		mWarrantyInMonths = warrantyInMonths;
	}

	/**
	 *
	 * @return mCtn
     */
	public String getCtn() {
		return mCtn;
	}

	/**
	 *
	 * @param ctn
     */
	public void setCtn(String ctn) {
		mCtn = ctn;
	}

	/**
     * {@getSector} method to get sector
	 * {@link com.philips.cdp.localematch.enums.Sector}
	 * @return mSector Sector object
     */
	public Sector getSector() {
		return mSector;
	}

	/**
     * {@code setSector} method to set sector
	 *{@link com.philips.cdp.localematch.enums.Sector}
	 * @param sector Sector object
     */
	public void setSector(Sector sector) {
		mSector = sector;
	}

	/**
     *  {@code getCatalog} method to get catalog
	 *{@link com.philips.cdp.localematch.enums.Catalog}
	 * @return mCatalog Catalog object
     */
	public Catalog getCatalog() {
		return mCatalog;
	}

	/**
     * {@code setCatalog} method to set catalog
 	 *{@link com.philips.cdp.localematch.enums.Catalog}
	 * @param catalog enum class object
     */
	public void setCatalog(Catalog catalog) {
		mCatalog = catalog;
	}

}
