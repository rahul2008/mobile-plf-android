
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.dao;

/**
 * Class Consumer interest
 */
public class ConsumerInterest {

	/* Campaign name */
	private String mCampaignName;

	/* Subject area */
	private String mSubjectArea;

	/* Topic communication key */
	private String mTopicCommunicationKey;

	/*Topic value */
	private String mTopicValue;

	/**
	 * Class Constructor
	 */
	public ConsumerInterest() {

	}

	/**
	 * Class Constructor
	 * @param campaignName
	 * @param subjectArea
	 * @param topicCommunicationKey
	 * @param topicValue
	 */
	public ConsumerInterest(String campaignName, String subjectArea, String topicCommunicationKey,
	        String topicValue) {

		mCampaignName = campaignName;
		mSubjectArea = subjectArea;
		mTopicCommunicationKey = topicCommunicationKey;
		mTopicValue = topicValue;

	}

	/**
	 * {@code getCampaignName} method to get campaign name
	 * @return campaign name
     */
	public String getCampaignName() {
		return mCampaignName;
	}

	/**
	 * {@code setCampaignName } method to set campaign name
	 * @param campaignName
     */
	public void setCampaignName(String campaignName) {
		mCampaignName = campaignName;
	}

	/**
	 * {@code getSubjectArea} method to get subject area
	 * @return subject area
     */
	public String getSubjectArea() {
		return mSubjectArea;
	}

	/**
	 * {@code setSubjectArea} method to set Subject area
	 * @param subjectArea
     */
	public void setSubjectArea(String subjectArea) {
		mSubjectArea = subjectArea;
	}

	/**
	 * {@code getTopicCommunicationKey} method to get topic communication key
	 * @return
     */
	public String getTopicCommunicationKey() {
		return mTopicCommunicationKey;
	}

	/**
	 * {@code setTopicCommunicationKey} method to set topic communication key
	 * @param topicCommunicationKey
     */
	public void setTopicCommunicationKey(String topicCommunicationKey) {
		mTopicCommunicationKey = topicCommunicationKey;
	}

	/**
	 * {@code getTopicValue} method to get topic value
	 * @return
     */
	public String getTopicValue() {
		return mTopicValue;
	}

	/**
	 * {@code setTopicValue} method to set topic value
	 * @param topicValue
     */
	public void setTopicValue(String topicValue) {
		mTopicValue = topicValue;
	}

}
