package com.philips.cl.di.dev.pa.outdoorlocations;

import java.io.IOException;
import java.io.InputStream;

import com.philips.cl.di.dev.pa.PurAirApplication;

public class OutdoorJsonReader {
	
	private CMACityData cmaCityData;
	private USEmbassyCityData usEmbassyCityData;
	
	public CMACityData getCmaCityData() {
		return cmaCityData;
	}

	public USEmbassyCityData getUsEmbassyCityData() {
		return usEmbassyCityData;
	}

	public String readCMACityJsonAsString() {
		try {
			InputStream is = PurAirApplication.getAppContext().getAssets().open("city_list_cma.json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String jsonAsString = new String(buffer, "UTF-8");
			return jsonAsString;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String readUSEmbassyCityJsonAsString() {
		try {
			InputStream is = PurAirApplication.getAppContext().getAssets().open("city_list_us_embassy.json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String jsonAsString = new String(buffer, "UTF-8");
			return jsonAsString;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String readNearbyCitiesJsonAsString() {
		try {
			InputStream is = PurAirApplication.getAppContext().getAssets().open("nearby_cities_list.json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String jsonAsString = new String(buffer, "UTF-8");
			return jsonAsString;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
