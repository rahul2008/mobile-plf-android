
package com.philips.cl.di.reg.dao;

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
