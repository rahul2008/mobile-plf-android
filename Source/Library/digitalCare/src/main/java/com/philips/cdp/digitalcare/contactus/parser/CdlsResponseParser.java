/**
 * CdlsResponseParserHelper will take care of parsing activity at digital care
 * app module level.
 *
 * @author: ritesh.jha@philips.com
 * @since: Dec 16, 2014
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.contactus.parser;

import com.philips.cdp.digitalcare.contactus.models.CdlsChatModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsEmailModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsErrorModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsPhoneModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsResponseModel;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CdlsResponseParser {
    private static final String TAG = CdlsResponseParser.class.getSimpleName();
    private static final int FIRST_INDEX_VALUE = 0;

    private CdlsPhoneModel cdlsPhoneModel = null;
    private CdlsEmailModel cdlsEmailModel = null;
    private CdlsChatModel cdlsChatModel = null;
    private CdlsErrorModel cdlsErrorModel = null;
    private CdlsParsingCallback mParsingCompletedCallback = null;

    public CdlsResponseParser(CdlsParsingCallback parsingCompletedCallback) {
        mParsingCompletedCallback = parsingCompletedCallback;
      /*  DigiCareLogger.i(TAG, "ParserController constructor : ");*/
    }

    /*
     * This method will create CDLS bean object and pass back to calling class.
     */
    public void parseCdlsResponse(String response) {
      /*  DigiCareLogger.i(TAG, "response : " + response);*/
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            boolean success = jsonObject
                    .optBoolean(DigitalCareConstants.CDLS_SUCCESS_KEY);

           /* DigiCareLogger.i(TAG, "response : " + response);*/
            if (success) {
                JSONObject jsonObjectData = jsonObject
                        .optJSONObject(DigitalCareConstants.CDLS_DATA_KEY);

                JSONArray jsonArrayDataPhone = jsonObjectData
                        .optJSONArray(DigitalCareConstants.CDLS_PHONE_KEY);
                JSONArray jsonArrayDataChat = jsonObjectData
                        .optJSONArray(DigitalCareConstants.CDLS_CHAT_KEY);
                JSONArray jsonArrayDataEmail = jsonObjectData
                        .optJSONArray(DigitalCareConstants.CDLS_EMAIL_KEY);

                if (jsonArrayDataPhone != null) {
                    JSONObject jsonObjectDataPhone = (JSONObject) jsonArrayDataPhone
                            .opt(FIRST_INDEX_VALUE);
                    cdlsPhoneModel = new CdlsPhoneModel();

                    cdlsPhoneModel.setPhoneNumber(jsonObjectDataPhone
                            .optString(DigitalCareConstants.CDLS_PHONENUMBER));
                    cdlsPhoneModel
                            .setOpeningHoursWeekdays(jsonObjectDataPhone
                                    .optString(DigitalCareConstants.CDLS_OPENINGHOURS_WEEKDAYS));
                    cdlsPhoneModel
                            .setOpeningHoursSaturday(jsonObjectDataPhone
                                    .optString(DigitalCareConstants.CDLS_OPENINGHOURS_SATURDAY));
                    cdlsPhoneModel
                            .setOpeningHoursSunday(jsonObjectDataPhone
                                    .optString(DigitalCareConstants.CDLS_OPENINGHOURS_SUNDAY));
                    cdlsPhoneModel
                            .setOptionalData1(jsonObjectDataPhone
                                    .optString(DigitalCareConstants.CDLS_OPTIONALDATA_ONE));
                    cdlsPhoneModel
                            .setOptionalData2(jsonObjectDataPhone
                                    .optString(DigitalCareConstants.CDLS_OPTIONALDATA_TWO));
                    cdlsPhoneModel.setmPhoneTariff(jsonObjectDataPhone.optString(DigitalCareConstants.CDLS_PHONE_TARIFF_KEY));
                }
                if (jsonArrayDataChat != null) {
                    JSONObject jsonObjectDataChat = (JSONObject) jsonArrayDataChat
                            .opt(FIRST_INDEX_VALUE);
                    cdlsChatModel = new CdlsChatModel();
                    cdlsChatModel.setContent(jsonObjectDataChat
                            .optString(DigitalCareConstants.CDLS_CHAT_CONTENT));
                    cdlsChatModel.setScript(jsonObjectDataChat
                            .optString(DigitalCareConstants.CDLS_CHAT_SCRIPT));
                    cdlsChatModel
                            .setOpeningHoursWeekdays(jsonObjectDataChat
                                    .optString(DigitalCareConstants.CDLS_CHAT_OPENINGINGHOURS_WEEKDAYS));
                    cdlsChatModel
                            .setOpeningHoursSaturday(jsonObjectDataChat
                                    .optString(DigitalCareConstants.CDLS_CHAT_OPENINGHOURS_SATURDAY));
                }
                if (jsonArrayDataEmail != null) {
                    JSONObject jsonObjectDataEmail = (JSONObject) jsonArrayDataEmail
                            .opt(FIRST_INDEX_VALUE);
                    cdlsEmailModel = new CdlsEmailModel();
                    cdlsEmailModel.setLabel(jsonObjectDataEmail
                            .optString(DigitalCareConstants.CDLS_EMAIL_LABEL));
                    cdlsEmailModel
                            .setContentPath(jsonObjectDataEmail
                                    .optString(DigitalCareConstants.CDLS_EMAIL_CONTENTPATH));
                }
            } else {
                cdlsErrorModel = new CdlsErrorModel();
                JSONObject jsonObjectData = jsonObject
                        .optJSONObject(DigitalCareConstants.CDLS_ERROR_KEY);
                cdlsErrorModel.setErrorCode(jsonObjectData
                        .optString(DigitalCareConstants.CDLS_ERROR_CODE));
                cdlsErrorModel.setErrorMessage(jsonObjectData
                        .optString(DigitalCareConstants.CDLS_ERROR_MESSAGE));
            }
            // creating CDLS instance.
            CdlsResponseModel cdlsParsedResponse = new CdlsResponseModel(
                    success, cdlsPhoneModel, cdlsChatModel, cdlsEmailModel,
                    cdlsErrorModel);
            mParsingCompletedCallback.onCdlsParsingComplete(cdlsParsedResponse);
        } catch (JSONException e) {
            DigiCareLogger.e(TAG, "JSONException : " + e);
        }
    }
}
