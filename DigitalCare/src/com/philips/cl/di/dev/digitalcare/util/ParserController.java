package com.philips.cl.di.dev.digitalcare.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.philips.cl.di.dev.digitalcare.bean.CdlsBean;
import com.philips.cl.di.dev.digitalcare.bean.CdlsChat;
import com.philips.cl.di.dev.digitalcare.bean.CdlsEmail;
import com.philips.cl.di.dev.digitalcare.bean.CdlsError;
import com.philips.cl.di.dev.digitalcare.bean.CdlsPhone;

/*
 * ParserController will take care of parsing activity at digital care app module level. 
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 16 Dec 2015
 */
public class ParserController {
	private static final String TAG = ParserController.class.getSimpleName();
	private Context mContext = null;
	private static ParserController mParserController = null;
	private CdlsBean cdlsBean = null;
	private static final int FIRST_INDEX_VALUE = 0;

	private ParserController(Context context) {
		mContext = context;
		DLog.i(TAG, "ParserController constructor : " + mContext.toString());
	}

	public static ParserController getParserControllInstance(Context context) {
		if (mParserController == null) {
			mParserController = new ParserController(context);
		}
		return mParserController;
	}

	/*
	 * Returning CDLS BEAN instance
	 */
	public CdlsBean getCdlsBean() {
		return cdlsBean;
	}

	/*
	 * This method will create CDLS bean object and pass back to calling class.
	 */
	public void extractCdlsValues(String response) {
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
			cdlsBean = new CdlsBean(success, cdlsPhone, cdlsChat, cdlsEmail,
					cdlsError);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
