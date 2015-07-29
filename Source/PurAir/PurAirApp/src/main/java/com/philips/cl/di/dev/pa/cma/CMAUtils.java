package com.philips.cl.di.dev.pa.cma;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import android.util.Base64;

import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.outdoorlocations.NearbyCitiesData;
import com.philips.cl.di.dev.pa.outdoorlocations.NearbyCitiesData.LocalityInfo;

public class CMAUtils {
	private static final String URL_PATTERN = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	private static final String HASH_ALG = "HmacSHA1";
	/**
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encodeToBase64(byte[] data) throws Exception {
		String strEncodeBase64 = null;
		if(data!=null && data.length > 0){
			strEncodeBase64 = Base64.encodeToString(data, Base64.DEFAULT);
		}
		return strEncodeBase64;
	}
	
	public static byte[] hmacSha1(String value, String privateKey) {
		SecretKeySpec secret = new SecretKeySpec(privateKey.getBytes(Charset.defaultCharset()), HASH_ALG);
		Mac mac = null;
		try {
			mac = Mac.getInstance(HASH_ALG);
			mac.init(secret);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		if( mac == null ) return null ;
		byte[] bytes = mac.doFinal(value.getBytes(Charset.defaultCharset()));
		return bytes;
	}
	
	public static boolean isValidURL(String url) {
		Pattern pattern = Pattern.compile(URL_PATTERN);
		Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}
	
	@SuppressLint("SimpleDateFormat")
	public static boolean isValidDate(String inDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}
	
	public static String getParentAreaID(String areaId) {
		OutdoorManager.getInstance().saveNearbyCityData();
		NearbyCitiesData nearbyCitiesData = OutdoorManager.getInstance().getNeighbourhoodCityData();
		if (nearbyCitiesData != null) {
			Map<String, List<LocalityInfo>> nearby_cities = nearbyCitiesData.getNearbyCitiesMap();
			Iterator<String> idsIterator = nearby_cities.keySet().iterator();
			while (idsIterator.hasNext()) {
				String tempAreaId = idsIterator.next();
				List<LocalityInfo> localityInfos = nearby_cities.get(tempAreaId);
				if (localityInfos != null) {
					for (LocalityInfo localityInfo : localityInfos) {
						if (areaId.equals(localityInfo.getAreaID())) {
							return tempAreaId;
						}
					}
				}
			}
		}
		return areaId;
	}
}
