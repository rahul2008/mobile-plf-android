package com.philips.cdp.registration.dao;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

public class ConsumerInterestTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {
    ConsumerInterest consumerInterest = new ConsumerInterest();

    public ConsumerInterestTest() {
        super(RegistrationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
    }

    public void testCampaignName() {
        consumerInterest.setCampaignName("CampaignName");
        assertEquals("CampaignName", consumerInterest.getCampaignName());
    }

    public void testTopicCommunicationKey() {
        consumerInterest.setTopicCommunicationKey("TopicCommunicationKey");
        assertEquals("TopicCommunicationKey", consumerInterest.getTopicCommunicationKey());
    }

    public void testSubjectArea() {
        consumerInterest.setSubjectArea("SubjectArea");
        assertEquals("SubjectArea", consumerInterest.getSubjectArea());
    }

    public void testTopicValue() {
        consumerInterest.setTopicValue("TopicValue");
        assertEquals("TopicValue", consumerInterest.getTopicValue());
    }
    public void testConsumerInterest(){
        consumerInterest = new ConsumerInterest("campaignName","subjectArea","topicCommunicationKey",
                "topicValue");
        assertNotNull(consumerInterest);
    }
}
