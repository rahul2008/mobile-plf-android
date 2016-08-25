/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.base;

import android.content.Context;

import com.janrain.android.capture.Capture;
import com.janrain.android.capture.CaptureRecord;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.coppa.interfaces.CoppaConsentUpdateCallback;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.servertime.ServerTime;
import com.philips.cdp.servertime.constants.ServerTimeConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class CoppaConsentUpdater {

    private Context mContext;

    CoppaConsentUpdater(final Context context) {
        mContext = context;
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
        //  if(Jump.getSignedInUser() != null){
        ServerTime.init(mContext);
        CaptureRecord updatedUser = CaptureRecord.loadFromDisk(mContext);
        JSONObject originalUserInfo = CaptureRecord.loadFromDisk(mContext);
        JSONObject consentsObject = new JSONObject();

        try {

            if (!CoppaConfiguration.isCampaignIdPresent()) {
                if (CoppaConfiguration.getCurrentConsentsArray().length() == 0) {
                    buildConsentStatus(coppaConsentStatus, consentsObject);
                    JSONArray newArray = new JSONArray();
                    newArray.put(consentsObject);
                    updatedUser.put(CoppaConfiguration.CONSENTS, newArray);
                } else {
                    JSONArray consents = CoppaConfiguration.getCurrentConsentsArray();
                    buildConsentStatus(coppaConsentStatus, consentsObject);
                    consents.put(consentsObject);
                    updatedUser.put(CoppaConfiguration.CONSENTS, consents);
                }
            } else {
                JSONArray consents = CoppaConfiguration.getCurrentConsentsArray();
                buildConsentStatus(coppaConsentStatus, consentsObject);
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

    private void buildConsentStatus(boolean coppaConsentStatus,
                                    JSONObject consentsObject) throws JSONException {
        consentsObject.put(CoppaConfiguration.CAMPAIGN_ID,
                RegistrationConfiguration.getInstance().getCampaignId());
        consentsObject.put(CoppaConfiguration.MICRO_SITE_ID,
                RegistrationConfiguration.getInstance().getMicrositeId());
        consentsObject.put(CoppaConfiguration.GIVEN, Boolean.toString(coppaConsentStatus));
        consentsObject.put(CoppaConfiguration.LOCALE,
                RegistrationHelper.getInstance().getLocale(mContext).toString());
        consentsObject.put(CoppaConfiguration.STORED_AT,
                ServerTime.getInstance().
                        getCurrentUTCTimeWithFormat(ServerTimeConstants.DATE_FORMAT_FOR_JUMP));
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
                ServerTime.getInstance().
                        getCurrentUTCTimeWithFormat(ServerTimeConstants.DATE_FORMAT_FOR_JUMP));
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
        ServerTime.init(mContext);
        CaptureRecord updatedUser = CaptureRecord.loadFromDisk(mContext);
        JSONObject originalUserInfo = CaptureRecord.loadFromDisk(mContext);
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
}
