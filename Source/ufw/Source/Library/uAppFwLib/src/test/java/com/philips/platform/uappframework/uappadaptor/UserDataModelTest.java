package com.philips.platform.uappframework.uappadaptor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
/**
 * Created by philips on 1/16/18.
 */
public class UserDataModelTest {
    private UserDataModel userDataModel;

    @Before
    public void setUp(){
        userDataModel = new UserDataModel();
        userDataModel.setUserFirstName("First name");
        userDataModel.setUserLastName("Last name");
        userDataModel.setEmailAddress("emailaddress@company.com");
        userDataModel.setAccessToken("AccessToken");
        userDataModel.setUUID("UUID");
        userDataModel.setHSDPUUID("HSDP UUID");
        userDataModel.setMobileNumber("98765453210");
        userDataModel.setGender("Male");
        //userDataModel.setDateOfBirth("01-01-1990");
    }

    @Test
    public void testNotNullForGetters(){
        assertNotNull(userDataModel.getUserFirstName());
        assertNotNull(userDataModel.getUserLastName());
        assertNotNull(userDataModel.getEmailAddress());
        assertNotNull(userDataModel.getAccessToken());
        assertNotNull(userDataModel.getUUID());
        assertNotNull(userDataModel.getHSDPUUID());
        assertNotNull(userDataModel.getMobileNumber());
        assertNotNull(userDataModel.getGender());


    }



}