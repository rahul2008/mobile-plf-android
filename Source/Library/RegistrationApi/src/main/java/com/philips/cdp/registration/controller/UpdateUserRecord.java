
package com.philips.cdp.registration.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.janrain.android.Jump;
import com.janrain.android.capture.Capture;
import com.janrain.android.capture.Capture.InvalidApidChangeException;
import com.janrain.android.capture.CaptureApiError;
import com.janrain.android.capture.CaptureRecord;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.RegistrationSettings;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateUserRecord implements UpdateUserRecordHandler {

    private String CONSUMER_TIMESTAMP = "timestamp";

    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private String CONSUMER_VISITED_MICROSITE_IDS = "visitedMicroSites";

    private String OLDER_THAN_AGE_LIMIT = "olderThanAgeLimit";

    private Context mContext;

    private String CONSUMER_ROLE = "role";

    private String CONSUMER_ROLES = "roles";

    private String CONSUMER_ROLE_ASSIGNED = "role_assigned";

    private String CONSUMER_COUNTRY = "country";

    private String CONSUMER_ADDRESS1 = "address1";

    private String CONSUMER_ADDRESS2 = "address2";

    private String CONSUMER_ADDRESS3 = "address3";

    private String CONSUMER_CITY = "city";

    private String CONSUMER_COMPANY = "company";

    private String CONSUMER_PHONE_NUMBER = "dayTimePhoneNumber";

    private String CONSUMER_HOUSE_NUMBER = "houseNumber";

    private String CONSUMER_MOBILE = "mobile";

    private String CONSUMER_PHONE = "phone";

    private String CONSUMER_STATE = "state";

    private String CONSUMER_ZIP = "zip";

    private String CONSUMER_NAME = "consumer";

    private String CONSUMER_ZIP_PLUS = "zipPlus4";

    private String CONSUMER_PREFERED_LANGUAGE = "preferredLanguage";

    private String CONSUMER_PRIMARY_ADDRESS = "primaryAddress";

    private String LOG_TAG = "ContinueSocialProviderLogin";

    public UpdateUserRecord(Context context) {
        mContext = context;
    }

    @Override
    public void updateUserRecordRegister() {
        if (Jump.getSignedInUser() != null) {
            CaptureRecord updatedUser = CaptureRecord.loadFromDisk(mContext);
            JSONObject originalUserInfo = CaptureRecord.loadFromDisk(mContext);
            SharedPreferences myPrefs = mContext.getSharedPreferences(
                    RegistrationSettings.REGISTRATION_API_PREFERENCE, 0);
            String microSiteId = myPrefs.getString(RegistrationSettings.MICROSITE_ID, null);

            RegistrationHelper userSettings = RegistrationHelper.getInstance();
            // visitedMicroSites
            try {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                String currentDate = sdf.format(c.getTime());

                JSONObject visitedMicroSitesObject = new JSONObject();
                visitedMicroSitesObject.put(RegistrationSettings.MICROSITE_ID, microSiteId);
                visitedMicroSitesObject.put(CONSUMER_TIMESTAMP, currentDate);
                JSONArray visitedMicroSitesArray = new JSONArray();
                visitedMicroSitesArray.put(visitedMicroSitesObject);
                // roles
                JSONObject rolesObject = new JSONObject();
                rolesObject.put(CONSUMER_ROLE, CONSUMER_NAME);
                rolesObject.put(CONSUMER_ROLE_ASSIGNED, currentDate);
                JSONArray rolesArray = new JSONArray();
                rolesArray.put(rolesObject);

                // PrimaryAddress
                JSONObject primaryAddressObject = new JSONObject();

                primaryAddressObject.put(CONSUMER_COUNTRY, UserRegistrationInitializer.getInstance().getRegistrationSettings()
                        .getPreferredCountryCode());
                primaryAddressObject.put(CONSUMER_ADDRESS1, "");
                primaryAddressObject.put(CONSUMER_ADDRESS2, "");
                primaryAddressObject.put(CONSUMER_ADDRESS3, "");
                primaryAddressObject.put(CONSUMER_CITY, "");
                primaryAddressObject.put(CONSUMER_COMPANY, "");
                primaryAddressObject.put(CONSUMER_PHONE_NUMBER, "");
                primaryAddressObject.put(CONSUMER_HOUSE_NUMBER, "");
                primaryAddressObject.put(CONSUMER_MOBILE, "");
                primaryAddressObject.put(CONSUMER_PHONE, "");
                primaryAddressObject.put(CONSUMER_STATE, "");
                primaryAddressObject.put(CONSUMER_ZIP, "");
                primaryAddressObject.put(CONSUMER_ZIP_PLUS, "");
                JSONArray primaryAddressArray = new JSONArray();
                primaryAddressArray.put(primaryAddressObject);

                updatedUser.put(CONSUMER_VISITED_MICROSITE_IDS, visitedMicroSitesArray);
                updatedUser.put(CONSUMER_ROLES, rolesArray);
                updatedUser.put(CONSUMER_PREFERED_LANGUAGE, UserRegistrationInitializer.getInstance().getRegistrationSettings()
                        .getPreferredLangCode());
                updatedUser.put(CONSUMER_PRIMARY_ADDRESS, primaryAddressObject);
                if (!(originalUserInfo.getBoolean(OLDER_THAN_AGE_LIMIT) && updatedUser.getBoolean(OLDER_THAN_AGE_LIMIT))) {
                    updatedUser.put(OLDER_THAN_AGE_LIMIT, true);
                }
                updateUserRecord(updatedUser, originalUserInfo);

            } catch (JSONException e) {
                Log.e(LOG_TAG, "On success, Caught JSON Exception");
            }
        }

    }

    private void updateUserRecord(CaptureRecord user, JSONObject originalUserInfo) {
        try {
            user.synchronize(new Capture.CaptureApiRequestCallback() {

                @Override
                public void onSuccess() {
                    Jump.saveToDisk(mContext);
                }

                @Override
                public void onFailure(CaptureApiError e) {
                }
            }, originalUserInfo);

        } catch (InvalidApidChangeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUserRecordLogin() {
        if (Jump.getSignedInUser() != null) {
            CaptureRecord updatedUser = CaptureRecord.loadFromDisk(mContext);
            JSONObject originalUserInfo = CaptureRecord.loadFromDisk(mContext);
            SharedPreferences myPrefs = mContext.getSharedPreferences(
                    RegistrationSettings.REGISTRATION_API_PREFERENCE, 0);
            String microSiteId = myPrefs.getString(RegistrationSettings.MICROSITE_ID, null);
            try {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                String currentDate = sdf.format(c.getTime());

                JSONObject visitedMicroSitesObject = new JSONObject();
                visitedMicroSitesObject.put(RegistrationSettings.MICROSITE_ID, microSiteId);
                visitedMicroSitesObject.put(CONSUMER_TIMESTAMP, currentDate);
                JSONArray visitedMicroSitesArray = (JSONArray) updatedUser.get(CONSUMER_VISITED_MICROSITE_IDS);
                Log.d(LOG_TAG, "Visited microsite ids = " + visitedMicroSitesArray);
                if (null == visitedMicroSitesArray) {
                    visitedMicroSitesArray = new JSONArray();
                }
                visitedMicroSitesArray.put(visitedMicroSitesObject);
                updatedUser.put(CONSUMER_VISITED_MICROSITE_IDS, visitedMicroSitesArray);

                if (!(originalUserInfo.getBoolean(OLDER_THAN_AGE_LIMIT) && updatedUser.getBoolean(OLDER_THAN_AGE_LIMIT))) {
                    updatedUser.put(OLDER_THAN_AGE_LIMIT, true);
                }
                updateUserRecord(updatedUser, originalUserInfo);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "On success, Caught JSON Exception");
            }
        }
    }
}
