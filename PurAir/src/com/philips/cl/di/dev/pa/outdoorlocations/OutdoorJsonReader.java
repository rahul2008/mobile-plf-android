package com.philips.cl.di.dev.pa.outdoorlocations;

import java.io.IOException;
import java.io.InputStream;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.util.DataParser;

public class OutdoorJsonReader {
	
	private CMACityData cmaCityData;
	private USEmbassyCityData usEmbassyCityData;
	
	public OutdoorJsonReader() {
		cmaCityData = DataParser.parseCMACityData(readCMACityJsonAsString());
		usEmbassyCityData = DataParser.parseUSEmbassyCityData(readUSEmbassyCityJsonAsString());
	}
	
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
	
}
