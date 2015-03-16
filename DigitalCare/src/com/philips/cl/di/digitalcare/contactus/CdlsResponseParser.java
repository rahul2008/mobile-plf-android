package com.philips.cl.di.digitalcare.contactus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.philips.cl.di.digitalcare.util.DLog;

/**
 * @description: CdlsResponseParserHelper will take care of parsing activity at
 *               digital care app module level.
 * @author: ritesh.jha@philips.com
 * @since: Dec 16, 2014
 */
public class CdlsResponseParser {
	private static final String TAG = CdlsResponseParser.class
			.getSimpleName();
	private Context mContext = null;
	private static CdlsResponseParser mParserController = null;
	private CdlsParsedResponse mCdlsParsedResponse = null;
	private static final int FIRST_INDEX_VALUE = 0;

	private CdlsResponseParser(Context context) {
		mContext = context;
		DLog.i(TAG, "ParserController constructor : " + mContext.toString());
	}

	public static CdlsResponseParser getParserControllInstance(
			Context context) {
		if (mParserController == null) {
			mParserController = new CdlsResponseParser(context);
		}
		return mParserController;
	}

	/*
	 * Returning CDLS BEAN instance
	 */
	public CdlsParsedResponse getCdlsBean() {
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
			CdlsPhone cdlsPhone = null;
			CdlsEmail cdlsEmail = null;
			CdlsChat cdlsChat = null;
			CdlsError cdlsError = null;

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

				cdlsPhone = new CdlsPhone();

				cdlsPhone.setPhoneNumber(jsonObjectDataPhone
						.optString("phoneNumber"));
				cdlsPhone.setOpeningHoursWeekdays(jsonObjectDataPhone
						.optString("openingHoursWeekdays"));
				cdlsPhone.setOpeningHoursSaturday(jsonObjectDataPhone
						.optString("openingHoursSaturday"));

				cdlsChat = new CdlsChat();
				cdlsChat.setContent(jsonObjectDataChat.optString("content"));
				cdlsChat.setOpeningHoursWeekdays(jsonObjectDataChat
						.optString("openingHoursWeekdays"));
				cdlsChat.setOpeningHoursSaturday(jsonObjectDataChat
						.optString("openingHoursSaturday"));

				cdlsEmail = new CdlsEmail();
				cdlsEmail.setLabel(jsonObjectDataEmail.optString("label"));
				cdlsEmail.setContentPath(jsonObjectDataEmail
						.optString("contentPath"));
			} else {
				cdlsError = new CdlsError();
				JSONObject jsonObjectData = jsonObject.optJSONObject("error");
				cdlsError.setErrorCode(jsonObjectData.optString("errorCode"));
				cdlsError.setErrorMessage(jsonObjectData
						.optString("errorMessage"));
			}
			// creating CDLS instance.
			mCdlsParsedResponse = new CdlsParsedResponse(success, cdlsPhone, cdlsChat, cdlsEmail,
					cdlsError);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
