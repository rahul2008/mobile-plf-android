package com.philips.cl.di.digitalcare.locatephilips;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * AtosResponseParser will take care of parsing ATOS resonse(LocateNearYou).
 * 
 * @author: ritesh.jha@philips.com
 * 
 * @since: 9 May 2015
 */
public class AtosResponseParser {
	private static final String TAG = AtosResponseParser.class.getSimpleName();
	private Context mContext = null;
	private static AtosResponseParser mParserController = null;
	private AtosResponseModel mCdlsParsedResponse = null;
	private ArrayList<ResultsModel> mArrayListResultsModel = null;

	private AtosResponseParser(Context context) {
		mContext = context;
		Log.i(TAG, "ParserController constructor : " + mContext.toString());
	}

	public static AtosResponseParser getParserControllInstance(Context context) {
		if (mParserController == null) {
			mParserController = new AtosResponseParser(context);
		}
		return mParserController;
	}

	/*
	 * Returning CDLS BEAN instance
	 */
	public AtosResponseModel getCdlsBean() {
		return mCdlsParsedResponse;
	}

	/*
	 * This method will create CDLS bean object and pass back to calling class.
	 */
	public void processCdlsResponse(String response) {
		Log.i(TAG, "response : " + response);
		JSONObject jsonObject = null;

		try {
			jsonObject = new JSONObject(response);
			boolean success = jsonObject.optBoolean("success");

			JSONObject jsonObjectData = jsonObject.optJSONObject("data");

			Log.i(TAG, "response jsonObjectData : " + jsonObjectData);

			LocationModel currentLocationModel = null;
			AtosErrorModel cdlsErrorModel = null;

			if (success) {
				JSONObject centerMap = jsonObjectData
						.optJSONObject("centerMap");
				String latitude = centerMap.optString("latitude");
				String longitude = centerMap.optString("longitude");
				currentLocationModel = new LocationModel();
				currentLocationModel.setLatitude(latitude);
				currentLocationModel.setLongitude(longitude);

				JSONArray jsonArray = jsonObjectData.optJSONArray("results");
				mArrayListResultsModel = new ArrayList<ResultsModel>();
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
			mCdlsParsedResponse = new AtosResponseModel(success,
					currentLocationModel, mArrayListResultsModel,
					cdlsErrorModel);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void parseResult(JSONObject jsonObj) {
		String title = jsonObj.optString("title");
		String infoType = jsonObj.optString("infoType");

		JSONObject jsonAddress = jsonObj.optJSONObject("address");

		/* Location */
		String id = jsonObj.optString("id");
		JSONObject location = jsonObj.optJSONObject("location");
		String latitude = location.optString("latitude");
		String longitude = location.optString("longitude");
		LocationModel locationModel = new LocationModel();
		locationModel.setLatitude(latitude);
		locationModel.setLongitude(longitude);

		Log.i("testing", "title : " + title);
		Log.i("testing", "jsonAddress : " + jsonAddress);

		/* Address */
		String zip = jsonAddress.optString("zip");
		String phone = jsonAddress.optString("phone");
		String state = jsonAddress.optString("state");
		String address1 = jsonAddress.optString("address1");
		String address2 = jsonAddress.optString("address2");
		String url = jsonAddress.optString("url");
		String city = jsonAddress.optString("city");

		AddressModel addressModel = new AddressModel();
		addressModel.setZip(zip);
		addressModel.setPhone(phone);
		addressModel.setState(state);
		addressModel.setAddress1(address1);
		addressModel.setAddress2(address2);
		addressModel.setUrl(url);
		addressModel.setCity(city);

		/*Result */
		ResultsModel resultModel = new ResultsModel();
		resultModel.setAddressModel(addressModel);
		resultModel.setId(id);
		resultModel.setInfoType(infoType);
		resultModel.setLocationModel(locationModel);
		resultModel.setTitle(title);

		mArrayListResultsModel.add(resultModel);
	}
}
