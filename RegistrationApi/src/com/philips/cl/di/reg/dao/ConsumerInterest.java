package com.philips.cl.di.reg.dao;

public class ConsumerInterest {

	private String campaignName;
	private String subjectArea;
	private String topicCommunicationKey;
	private String topicValue;

	public ConsumerInterest() {

	}

	public ConsumerInterest(String campaignName, String subjectArea,
			String topicCommunicationKey, String topicValue) {

		this.campaignName = campaignName;
		this.subjectArea = subjectArea;
		this.topicCommunicationKey = topicCommunicationKey;
		this.topicValue = topicValue;

	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getSubjectArea() {
		return subjectArea;
	}

	public void setSubjectArea(String subjectArea) {
		this.subjectArea = subjectArea;
	}

	public String getTopicCommunicationKey() {
		return topicCommunicationKey;
	}

	public void setTopicCommunicationKey(String topicCommunicationKey) {
		this.topicCommunicationKey = topicCommunicationKey;
	}

	public String getTopicValue() {
		return topicValue;
	}

	public void setTopicValue(String topicValue) {
		this.topicValue = topicValue;
	}

}
