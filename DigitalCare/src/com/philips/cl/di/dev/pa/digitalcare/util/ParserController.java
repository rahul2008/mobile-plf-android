package com.philips.cl.di.dev.pa.digitalcare.util;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.philips.cl.di.dev.pa.digitalcare.bean.CdlsBean;
import com.philips.cl.di.dev.pa.digitalcare.bean.CdlsChat;
import com.philips.cl.di.dev.pa.digitalcare.bean.CdlsEmail;
import com.philips.cl.di.dev.pa.digitalcare.bean.CdlsError;
import com.philips.cl.di.dev.pa.digitalcare.bean.CdlsPhone;

/*
 * ParserController will take care of parsing activity at digital care app module level. 
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 16 Dec 2015
 */
public class ParserController {
	private static final int FIRST_INDEX_VALUE = 0;

	/*
	 * This method will create CDLS bean object and pass back to calling class.
	 */
	public static CdlsBean extractCdlsValues(boolean success,
			JSONObject jsonObject) {
		Log.i("testing", "jsonObject : " + jsonObject);
		CdlsBean cdlsBean = null;

		if (success) {
			JSONObject jsonObjectData = jsonObject.optJSONObject("data");

			JSONArray jsonArrayDataPhone = jsonObjectData.optJSONArray("phone");
			JSONArray jsonArrayDataChat = jsonObjectData.optJSONArray("chat");
			JSONArray jsonArrayDataEmail = jsonObjectData.optJSONArray("email");

			JSONObject jsonObjectDataPhone = (JSONObject) jsonArrayDataPhone
					.opt(FIRST_INDEX_VALUE);
			JSONObject jsonObjectDataChat = (JSONObject) jsonArrayDataChat
					.opt(FIRST_INDEX_VALUE);
			JSONObject jsonObjectDataEmail = (JSONObject) jsonArrayDataEmail
					.opt(FIRST_INDEX_VALUE);

			CdlsPhone cdlsPhone = new CdlsPhone();

			cdlsPhone.setPhoneNumber(jsonObjectDataPhone
					.optString("phoneNumber"));
			cdlsPhone.setOpeningHoursWeekdays(jsonObjectDataPhone
					.optString("openingHoursWeekdays"));
			cdlsPhone.setOpeningHoursSaturday(jsonObjectDataPhone
					.optString("openingHoursSaturday"));

			CdlsChat cdlsChat = new CdlsChat();
			cdlsChat.setContent(jsonObjectDataChat.optString("content"));
			cdlsChat.setOpeningHoursWeekdays(jsonObjectDataChat
					.optString("openingHoursWeekdays"));
			cdlsChat.setOpeningHoursSaturday(jsonObjectDataChat
					.optString("openingHoursSaturday"));

			CdlsEmail cdlsEmail = new CdlsEmail();
			cdlsEmail.setLabel(jsonObjectDataEmail.optString("label"));
			cdlsEmail.setContentPath(jsonObjectDataEmail
					.optString("contentPath"));

			cdlsBean = new CdlsBean(success, cdlsPhone, cdlsChat, cdlsEmail);
		} else {
			CdlsError cdlsError = new CdlsError();
			JSONObject jsonObjectData = jsonObject.optJSONObject("error");
			cdlsError.setErrorCode(jsonObjectData.optString("errorCode"));
			cdlsError.setErrorMessage(jsonObjectData.optString("errorMessage"));
			cdlsBean = new CdlsBean(success, cdlsError);
		}

		return cdlsBean;
	}
}
