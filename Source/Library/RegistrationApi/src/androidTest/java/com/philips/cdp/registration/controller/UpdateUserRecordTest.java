package com.philips.cdp.registration.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.InstrumentationTestCase;

import com.janrain.android.Jump;
import com.janrain.android.capture.CaptureRecord;
import com.philips.cdp.registration.settings.RegistrationSettings;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.servertime.ServerTime;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by 310243576 on 8/31/2016.
 */
public class UpdateUserRecordTest extends InstrumentationTestCase {

    Context mContext;
    UpdateUserRecord updateUserRecord;
    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        mContext = getInstrumentation().getTargetContext();
        updateUserRecord = new UpdateUserRecord(mContext);
    }

    @Test
    public void testUpdateUserRecord(){
        assertNotNull(updateUserRecord);
         updateUserRecord.updateUserRecordLogin();
        assertEquals(null, Jump.getSignedInUser());
        CaptureRecord updatedUser = CaptureRecord.loadFromDisk(mContext);
        assertEquals(null,updatedUser);

        SharedPreferences myPrefs = mContext.getSharedPreferences(
                RegistrationSettings.REGISTRATION_API_PREFERENCE, 0);
        String microSiteId = myPrefs.getString(RegistrationSettings.MICROSITE_ID, null);
        assertEquals(null,microSiteId);


//        updateUserRecord.updateUserRecordRegister();
    }
    @Test
    public void testarray(){
        try {
            String currentDate = ServerTime.getInstance().getCurrentUTCTimeWithFormat("yyyy-MM-dd HH:mm:ss");
            assertEquals("",currentDate);

            JSONObject visitedMicroSitesObject = new JSONObject();
            visitedMicroSitesObject.put(RegistrationSettings.MICROSITE_ID, "77000");
            visitedMicroSitesObject.put("timestamp", currentDate);
            assertNotNull(visitedMicroSitesObject);

            JSONArray visitedMicroSitesArray = new JSONArray();
            visitedMicroSitesArray.put(visitedMicroSitesObject);
            assertNotNull(visitedMicroSitesArray);

            JSONObject rolesObject = new JSONObject();
            rolesObject.put("role", "consumer");
            rolesObject.put("role_assigned", currentDate);
            assertNotNull(rolesObject);

            JSONArray rolesArray = new JSONArray();
            rolesArray.put(rolesObject);
            assertNotNull(rolesArray);

            // PrimaryAddress
            JSONObject primaryAddressObject = new JSONObject();

            primaryAddressObject.put("country", UserRegistrationInitializer.getInstance().getRegistrationSettings()
                    .getPreferredCountryCode());
            primaryAddressObject.put("address1", "");
            primaryAddressObject.put("address2", "");
            primaryAddressObject.put("address3", "");
            primaryAddressObject.put("city", "");
            primaryAddressObject.put("company", "");
            primaryAddressObject.put("dayTimePhoneNumber", "");
            primaryAddressObject.put("houseNumber", "");
            primaryAddressObject.put("mobile", "");
            primaryAddressObject.put("phone", "");
            primaryAddressObject.put("state", "");
            primaryAddressObject.put("zip", "");
            primaryAddressObject.put("zipPlus4", "");
            assertNotNull(primaryAddressObject);





        }catch (Exception e){

        }
    }
}