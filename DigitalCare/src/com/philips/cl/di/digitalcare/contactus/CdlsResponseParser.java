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
			CdlsPhoneModel cdlsPhoneModel = null;
			CdlsEmailModel cdlsEmailModel = null;
			CdlsChatModel cdlsChatModel = null;
			CdlsErrorModel cdlsErrorModel = null;

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
				JSONObject jsonObjectDataChat = (JSONObject) jsonArrayDataChat
						.opt(FIRST_INDEX_VALUE);
				JSONObject jsonObjectDataEmail = (JSONObject) jsonArrayDataEmail
						.opt(FIRST_INDEX_VALUE);

				cdlsPhoneModel = new CdlsPhoneModel();

				cdlsPhoneModel.setPhoneNumber(jsonObjectDataPhone
						.optString("phoneNumber"));
				cdlsPhoneModel.setOpeningHoursWeekdays(jsonObjectDataPhone
						.optString("openingHoursWeekdays"));
				cdlsPhoneModel.setOpeningHoursSaturday(jsonObjectDataPhone
						.optString("openingHoursSaturday"));

				cdlsChatModel = new CdlsChatModel();
				cdlsChatModel.setContent(jsonObjectDataChat.optString("content"));
				cdlsChatModel.setOpeningHoursWeekdays(jsonObjectDataChat
						.optString("openingHoursWeekdays"));
				cdlsChatModel.setOpeningHoursSaturday(jsonObjectDataChat
						.optString("openingHoursSaturday"));

				cdlsEmailModel = new CdlsEmailModel();
				cdlsEmailModel.setLabel(jsonObjectDataEmail.optString("label"));
				cdlsEmailModel.setContentPath(jsonObjectDataEmail
						.optString("contentPath"));
			} else {
				cdlsErrorModel = new CdlsErrorModel();
				JSONObject jsonObjectData = jsonObject.optJSONObject("error");
				cdlsErrorModel.setErrorCode(jsonObjectData.optString("errorCode"));
				cdlsErrorModel.setErrorMessage(jsonObjectData
						.optString("errorMessage"));
			}
			// creating CDLS instance.
			mCdlsParsedResponse = new CdlsResponseModel(success, cdlsPhoneModel,
					cdlsChatModel, cdlsEmailModel, cdlsErrorModel);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
