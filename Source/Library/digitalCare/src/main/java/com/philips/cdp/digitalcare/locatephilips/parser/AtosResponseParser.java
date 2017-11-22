package com.philips.cdp.digitalcare.locatephilips.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.philips.cdp.digitalcare.locatephilips.models.AtosAddressModel;
import com.philips.cdp.digitalcare.locatephilips.models.AtosErrorModel;
import com.philips.cdp.digitalcare.locatephilips.models.AtosLocationModel;
import com.philips.cdp.digitalcare.locatephilips.models.AtosResponseModel;
import com.philips.cdp.digitalcare.locatephilips.models.AtosResultsModel;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

/**
 * AtosResponseParser will take care of parsing ATOS resonse(LocateNearYou).
 * 
 * @author: ritesh.jha@philips.com
 * 
 * @since: 9 May 2015
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */
public class AtosResponseParser {
	private static final String TAG = AtosResponseParser.class.getSimpleName();
	private ArrayList<AtosResultsModel> mArrayListResultsModel = null;
	private AtosParsingCallback mParsingCompletedCallback = null;

	public AtosResponseParser(AtosParsingCallback parsingCompletedCallback) {
		mParsingCompletedCallback = parsingCompletedCallback;
		/*DigiCareLogger.i(TAG, "ParserController constructor : ");*/
	}

	/*
	 * This method will create CDLS bean object and pass back to calling class.
	 */
	public void parseAtosResponse(String response) {
		JSONObject jsonObject = null;

		try {
			jsonObject = new JSONObject(response);
			boolean success = jsonObject.optBoolean("success");

			JSONObject jsonObjectData = jsonObject.optJSONObject("data");
			AtosLocationModel currentLocationModel = null;
			AtosErrorModel cdlsErrorModel = null;

			if (success) {
				JSONArray jsonArray = jsonObjectData.optJSONArray("results");
				mArrayListResultsModel = new ArrayList<AtosResultsModel>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.optJSONObject(i);
					parseResult(jsonObj);
				}

			} else {
				cdlsErrorModel = new AtosErrorModel();
				JSONObject jsonObjectError = jsonObject.optJSONObject("error");
				cdlsErrorModel.setErrorCode(jsonObjectError
						.optString("errorCode"));
				cdlsErrorModel.setErrorMessage(jsonObjectError
						.optString("errorMessage"));
			}
			// creating CDLS instance.
			AtosResponseModel cdlsParsedResponse = new AtosResponseModel(
					success, currentLocationModel, mArrayListResultsModel,
					cdlsErrorModel);
			mParsingCompletedCallback.onAtosParsingComplete(cdlsParsedResponse);
		} catch (JSONException e) {
			DigiCareLogger.e(TAG, "JSON Exception : "+ e);
		}
	}

	private void parseResult(JSONObject jsonObj) {
		String id = jsonObj.optString("id");
		String title = jsonObj.optString("title");
		String infoType = jsonObj.optString("infoType");

		/* Location */
		JSONObject location = jsonObj.optJSONObject("location");
		AtosLocationModel locationModel = null;

		if (location != null) {
			String latitude = location.optString("latitude");
			String longitude = location.optString("longitude");
			locationModel = new AtosLocationModel();
			locationModel.setLatitude(latitude);
			locationModel.setLongitude(longitude);
		}

		// Log.i("testing", "title : " + title);
		// Log.i("testing", "jsonAddress : " + jsonAddress);

		/* Address */
		JSONObject jsonAddress = jsonObj.optJSONObject("address");
		AtosAddressModel addressModel = null;
		if (jsonAddress != null) {
			String zip = jsonAddress.optString("zip");
			String phone = jsonAddress.optString("phone");
			// String state = jsonAddress.optString("state");
			String address1 = jsonAddress.optString("address1");
			String address2 = jsonAddress.optString("address2");
			String url = jsonAddress.optString("url");
			// String city = jsonAddress.optString("city");
			String cityState = jsonAddress.optString("city") + " "
					+ jsonAddress.optString("state");

			addressModel = new AtosAddressModel();
			addressModel.setZip(zip);
			addressModel.setPhone(phone);
			// addressModel.setState(state);
			addressModel.setAddress1(address1);
			addressModel.setAddress2(address2);
			addressModel.setUrl(url);
			// addressModel.setCity(city);
			addressModel.setCityState(cityState);
		}

		/* Result */
		AtosResultsModel resultModel = new AtosResultsModel();
		resultModel.setAddressModel(addressModel);
		resultModel.setId(id);
		resultModel.setLocationModel(locationModel);
		resultModel.setTitle(title);

		mArrayListResultsModel.add(resultModel);
	}
}
