
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.dao;

public class ConsumerInterest {

	private String mCampaignName;

	private String mSubjectArea;

	private String mTopicCommunicationKey;

	private String mTopicValue;

	public ConsumerInterest() {

	}

	public ConsumerInterest(String campaignName, String subjectArea, String topicCommunicationKey,
	        String topicValue) {

		mCampaignName = campaignName;
		mSubjectArea = subjectArea;
		mTopicCommunicationKey = topicCommunicationKey;
		mTopicValue = topicValue;

	}

	public String getCampaignName() {
		return mCampaignName;
	}

	public void setCampaignName(String campaignName) {
		mCampaignName = campaignName;
	}

	public String getSubjectArea() {
		return mSubjectArea;
	}

	public void setSubjectArea(String subjectArea) {
		mSubjectArea = subjectArea;
	}

	public String getTopicCommunicationKey() {
		return mTopicCommunicationKey;
	}

	public void setTopicCommunicationKey(String topicCommunicationKey) {
		mTopicCommunicationKey = topicCommunicationKey;
	}

	public String getTopicValue() {
		return mTopicValue;
	}

	public void setTopicValue(String topicValue) {
		mTopicValue = topicValue;
	}

}
