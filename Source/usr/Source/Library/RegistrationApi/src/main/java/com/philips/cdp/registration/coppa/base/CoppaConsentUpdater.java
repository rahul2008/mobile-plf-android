/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.base;

import android.content.Context;
import androidx.annotation.Nullable;

import com.janrain.android.Jump;
import com.janrain.android.capture.Capture;
import com.janrain.android.capture.CaptureRecord;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.coppa.interfaces.CoppaConsentUpdateCallback;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.ServerTime;
import com.philips.platform.appinfra.timesync.TimeInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class CoppaConsentUpdater {

    private Context mContext;

    private TimeInterface timeInterface;

    CoppaConsentUpdater(final Context context) {
        mContext = context;
        timeInterface = RegistrationConfiguration.getInstance().getComponent().getTimeInterface();
    }

    /**
     * {@code updateCoppaConsentConsentStatus}to update coppa consent Consent status
     *
     * @param coppaConsentStatus         this will give the coppa consent status as true or false
     * @param coppaConsentUpdateCallback call back  to get onSuccess or onFailure
     */
    public void updateCoppaConsentStatus(
            final boolean coppaConsentStatus,
            final CoppaConsentUpdateCallback coppaConsentUpdateCallback) {
        ServerTime.init(timeInterface);
        CaptureRecord updatedUser = Jump.getSignedInUser();
        JSONObject originalUserInfo = getCurrentUserAsJsonObject();
        JSONObject consentsObject = new JSONObject();


        String locale = RegistrationHelper.getInstance().getLocale().toString();

        try {

            if (!CoppaConfiguration.isCampaignIdPresent()) {
                if (CoppaConfiguration.getCurrentConsentsArray().length() == 0) {
                    buildConsentStatus(coppaConsentStatus,locale, consentsObject);
                    JSONArray newArray = new JSONArray();
                    newArray.put(consentsObject);
                    updatedUser.put(CoppaConfiguration.CONSENTS, newArray);
                } else {
                    JSONArray consents = CoppaConfiguration.getCurrentConsentsArray();
                    buildConsentStatus(coppaConsentStatus,locale, consentsObject);
                    consents.put(consentsObject);
                    updatedUser.put(CoppaConfiguration.CONSENTS, consents);
                }
            } else {
                JSONArray consents = CoppaConfiguration.getCurrentConsentsArray();
                buildConsentStatus(coppaConsentStatus,locale, consentsObject);
                consents.put(CoppaConfiguration.consentIndex(), consentsObject);
                updatedUser.put(CoppaConfiguration.CONSENTS, consents);
            }

            updatedUser.synchronize(new CoppaConsentUpdateHandler(coppaConsentUpdateCallback),
                    originalUserInfo);
        } catch (JSONException | Capture.InvalidApidChangeException e) {
            e.printStackTrace();
        }

        //  }else{
        //  }

    }

    private void buildConsentStatus(boolean coppaConsentStatus,String locale,
                                    JSONObject consentsObject) throws JSONException {
        consentsObject.put(CoppaConfiguration.CAMPAIGN_ID,
                RegistrationConfiguration.getInstance().getCampaignId());
        consentsObject.put(CoppaConfiguration.MICRO_SITE_ID,
                RegistrationConfiguration.getInstance().getMicrositeId());
        consentsObject.put(CoppaConfiguration.GIVEN, Boolean.toString(coppaConsentStatus));
        consentsObject.put(CoppaConfiguration.LOCALE,locale);
        consentsObject.put(CoppaConfiguration.STORED_AT,
                ServerTime.
                        getCurrentUTCTimeWithFormat(ServerTime.DATE_FORMAT_FOR_JUMP));
    }

    private void buildConsentConfirmation(boolean coppaConsentConfirmationStatus,
                                          JSONObject consentsObject) throws JSONException {
        consentsObject.put(CoppaConfiguration.CAMPAIGN_ID,
                RegistrationConfiguration.getInstance().getCampaignId());
        consentsObject.put(CoppaConfiguration.MICRO_SITE_ID,
                RegistrationConfiguration.getInstance().getMicrositeId());
        consentsObject.put(CoppaConfiguration.CONFIRMATION_GIVEN,
                Boolean.toString(coppaConsentConfirmationStatus));
        consentsObject.put(CoppaConfiguration.CONFIRMATION_STORED_AT,
                ServerTime.
                        getCurrentUTCTimeWithFormat(ServerTime.DATE_FORMAT_FOR_JUMP));
    }

    /**
     * {@code updateCoppaConsentConfirmationStatus}to update coppa consent confirmation status
     *
     * @param coppaConsentStatus         this will give the coppa consent status as true or false
     * @param coppaConsentUpdateCallback call back  to get onSuccess or onFailure
     */

    public void updateCoppaConsentConfirmationStatus(
            final boolean coppaConsentStatus,
            final CoppaConsentUpdateCallback coppaConsentUpdateCallback) {
        ServerTime.init(timeInterface);//TODO: remove this API
        CaptureRecord updatedUser = Jump.getSignedInUser();

        JSONObject originalUserInfo = getCurrentUserAsJsonObject();
        JSONObject consentsObject = new JSONObject();

        try {

            if (!CoppaConfiguration.isCampaignIdPresent()) {
                if (CoppaConfiguration.getCurrentConsentsArray().length() == 0) {
                    buildConsentConfirmation(coppaConsentStatus, consentsObject);
                    JSONArray newArray = new JSONArray();
                    newArray.put(consentsObject);
                    updatedUser.put(CoppaConfiguration.CONSENTS, newArray);
                } else {
                    JSONArray consents = CoppaConfiguration.getCurrentConsentsArray();
                    buildConsentConfirmation(coppaConsentStatus, consentsObject);
                    consents.put(consentsObject);
                    updatedUser.put(CoppaConfiguration.CONSENTS, consents);
                }
            } else {
                JSONArray consents = CoppaConfiguration.getCurrentConsentsArray();
                buildConsentConfirmation(coppaConsentStatus, consentsObject);
                consents.put(CoppaConfiguration.consentIndex(), consentsObject);
                updatedUser.put(CoppaConfiguration.CONSENTS, consents);
            }
            updatedUser.synchronize(new CoppaConsentUpdateHandler(coppaConsentUpdateCallback),
                    originalUserInfo);
        } catch (JSONException | Capture.InvalidApidChangeException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private JSONObject getCurrentUserAsJsonObject() {
        JSONObject userData = null;
        try {
            userData = new JSONObject(Jump.getSignedInUser().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userData;
    }

}

