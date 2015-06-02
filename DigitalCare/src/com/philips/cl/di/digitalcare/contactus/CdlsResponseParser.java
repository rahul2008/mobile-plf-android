package com.philips.cl.di.digitalcare.contactus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.philips.cl.di.digitalcare.util.DLog;

/**
 * CdlsResponseParserHelper will take care of parsing activity at digital care
 * app module level.
 * 
 * @author: ritesh.jha@philips.com
 * @since: Dec 16, 2014
 */
public class CdlsResponseParser {
	private static final String TAG = CdlsResponseParser.class.getSimpleName();
	private Context mContext = null;
	private static CdlsResponseParser mParserController = null;
	private CdlsResponseModel mCdlsParsedResponse = null;
	private static final int FIRST_INDEX_VALUE = 0;

	private CdlsPhoneModel cdlsPhoneModel = null;
	private CdlsEmailModel cdlsEmailModel = null;
	private CdlsChatModel cdlsChatModel = null;
	private CdlsErrorModel cdlsErrorModel = null;

	private CdlsResponseParser(Context context) {
		mContext = context;
		DLog.i(TAG, "ParserController constructor : " + mContext.toString());
	}

	public static CdlsResponseParser getParserControllInstance(Context context) {
		if (mParserController == null) {
			mParserController = new CdlsResponseParser(context);
		}
		return mParserController;
	}

	/*
	 * Returning CDLS BEAN instance
	 */
	public CdlsResponseModel getCdlsBean() {
		return mCdlsParsedResponse;
	}

	public CdlsPhoneModel getCdlsPhoneModel() {
		return cdlsPhoneModel;
	}

	public CdlsEmailModel getCdlsEmailModel() {
		return cdlsEmailModel;
	}

	public CdlsChatModel getCdlsChatModel() {
		return cdlsChatModel;
	}

	public CdlsErrorModel getCdlsErrorModel() {
		return cdlsErrorModel;
	}

	/*
	 * This method will create CDLS bean object and pass back to calling class.
	 */
	public void processCdlsResponse(String response) {
		DLog.i(TAG, "response : " + response);
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(response);
			boolean success = jsonObject.optBoolean("success");

			DLog.i(TAG, "response : " + response);
			if (success) {
				JSONObject jsonObjectData = jsonObject.optJSONObject("data");

				JSONArray jsonArrayDataPhone = jsonObjectData
						.optJSONArray("phone");
				JSONArray jsonArrayDataChat = jsonObjectData
						.optJSONArray("chat");
				JSONArray jsonArrayDataEmail = jsonObjectData
						.optJSONArray("email");

				JSONObject jsonObjectDataPhone = (JSONObject) jsonArrayDataPhone
						.opt(FIRST_INDEX_VALUE);
				if (jsonArrayDataChat != null) {
					JSONObject jsonObjectDataChat = (JSONObject) jsonArrayDataChat
							.opt(FIRST_INDEX_VALUE);
					cdlsChatModel = new CdlsChatModel();
					cdlsChatModel.setContent(jsonObjectDataChat
							.optString("content"));
					cdlsChatModel.setOpeningHoursWeekdays(jsonObjectDataChat
							.optString("openingHoursWeekdays"));
					cdlsChatModel.setOpeningHoursSaturday(jsonObjectDataChat
							.optString("openingHoursSaturday"));
				}
				if (jsonArrayDataEmail != null) {
					JSONObject jsonObjectDataEmail = (JSONObject) jsonArrayDataEmail
							.opt(FIRST_INDEX_VALUE);
					cdlsEmailModel = new CdlsEmailModel();
					cdlsEmailModel.setLabel(jsonObjectDataEmail
							.optString("label"));
					cdlsEmailModel.setContentPath(jsonObjectDataEmail
							.optString("contentPath"));
				}
				cdlsPhoneModel = new CdlsPhoneModel();

				cdlsPhoneModel.setPhoneNumber(jsonObjectDataPhone
						.optString("phoneNumber"));
				cdlsPhoneModel.setOpeningHoursWeekdays(jsonObjectDataPhone
						.optString("openingHoursWeekdays"));
				cdlsPhoneModel.setOpeningHoursSaturday(jsonObjectDataPhone
						.optString("openingHoursSaturday"));
				cdlsPhoneModel.setOpeningHoursSunday(jsonObjectDataPhone
						.optString("openingHoursSunday"));
				cdlsPhoneModel.setOptionalData1(jsonObjectDataPhone
						.optString("optionalData1"));
				cdlsPhoneModel.setOptionalData2(jsonObjectDataPhone
						.optString("optionalData2"));
			} else {
				cdlsErrorModel = new CdlsErrorModel();
				JSONObject jsonObjectData = jsonObject.optJSONObject("error");
				cdlsErrorModel.setErrorCode(jsonObjectData
						.optString("errorCode"));
				cdlsErrorModel.setErrorMessage(jsonObjectData
						.optString("errorMessage"));
			}
			// creating CDLS instance.
			mCdlsParsedResponse = new CdlsResponseModel(success,
					cdlsPhoneModel, cdlsChatModel, cdlsEmailModel,
					cdlsErrorModel);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
