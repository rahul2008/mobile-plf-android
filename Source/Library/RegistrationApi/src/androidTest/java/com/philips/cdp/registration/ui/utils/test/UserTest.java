/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.utils.test;


import android.content.Context;
import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.User;

public class UserTest extends InstrumentationTestCase {

    User mUser = null;

    //	public UserTest() {
    //	super(RegistrationActivity.class);
    //}
    Context context;
    @Override
    protected void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        super.setUp();
        context = getInstrumentation().getTargetContext();
        System.setProperty("dexmaker.dexcache", context.getCacheDir().getPath());
        mUser = new User(context);


        //getInstrumentation().get
    }












//    public void testAddConsumerInterest(){
//        AddConsumerInterestHandler addConsumerInterestHandler = new AddConsumerInterestHandler() {
//            @Override
//            public void onAddConsumerInterestSuccess() {
//
//            }
//
//            @Override
//            public void onAddConsumerInterestFailedWithError(int error) {
//
//            }
//        };
//        ConsumerArray consumerArray = new  ConsumerArray();
//
////        List<ConsumerInterest> consumerInterestList = new ArrayList<ConsumerInterest>();
////        ConsumerInterest consumerInterest = new ConsumerInterest();
////        consumerInterest.setCampaignName("campaignName");
////        consumerInterest.setSubjectArea("subjectArea");
////        consumerInterest.setTopicCommunicationKey("topicCommunicationKey");
////        consumerInterest.setTopicValue("topicValue");
////        consumerInterestList.add(consumerInterest);
////        consumerArray.setConsumerArraylist(consumerInterestList);
//        mUser.addConsumerInterest(addConsumerInterestHandler,consumerArray);
//
//
//    }


}
