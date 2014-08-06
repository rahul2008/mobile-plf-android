package com.philips.cl.di.dev.pa.dashboard;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.philips.cl.di.dev.pa.R;

public class WeatherIcon {
	
	public static int getWeatherIconResID(int weatherPhenomenon) {
		
		try {
			Field field = R.drawable.class.getDeclaredField("weather_icon_" + weatherPhenomenon);
			int resId = field.getInt(R.drawable.class);
			return resId;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return R.drawable.weather_icon_0;
	}
	
	public static int getWeatherIconResId(String weatherPhenomenon) {
		int weatherPhenomenonId = 0;
		weatherPhenomenon = weatherPhenomenon.replaceAll("night", "");
		if(weatherPhenomenonMap.get(weatherPhenomenon.trim()) == null) {
			weatherPhenomenonId = 0;
		} else {
			weatherPhenomenonId = weatherPhenomenonMap.get(weatherPhenomenon.trim());
		}
		int resId = getWeatherIconResID(weatherPhenomenonId); 
		return resId;
	}
	
	private static Map<String, Integer> weatherPhenomenonMap = new HashMap<String, Integer>();
	
	public static void populateWeatherIconMap() {
		weatherPhenomenonMap.put("clear", 32);
		weatherPhenomenonMap.put("cloudy", 1);
		weatherPhenomenonMap.put("cloudyheavyfreezingrain",22);
		weatherPhenomenonMap.put("cloudyheavyfreezingrainlightning",23);
		weatherPhenomenonMap.put("cloudyheavymix",6);
		weatherPhenomenonMap.put("cloudyheavymixlightning",7);
		weatherPhenomenonMap.put("cloudyheavyrain",10);
		weatherPhenomenonMap.put("cloudyheavyrainlightning", 12);
		weatherPhenomenonMap.put("cloudyheavysleet", 6);
		weatherPhenomenonMap.put("cloudyheavysleetlightning", 7);
		weatherPhenomenonMap.put("cloudyheavysnow", 18);
		weatherPhenomenonMap.put("cloudyheavysnowlightning", 19);
		weatherPhenomenonMap.put("cloudylightfreezingrain", 20);
		weatherPhenomenonMap.put("cloudylightmix", 6);
		weatherPhenomenonMap.put("cloudylightrain", 8);
		weatherPhenomenonMap.put("cloudylightrainlightning", 11);
		weatherPhenomenonMap.put("cloudylightsleet", 6);
		weatherPhenomenonMap.put("cloudylightsnow", 16);
		weatherPhenomenonMap.put("cloudymediumfreezingrain", 21);
		weatherPhenomenonMap.put("cloudymediumfreezingrainlightning", 23);
		weatherPhenomenonMap.put("cloudymediummix", 6);
		weatherPhenomenonMap.put("cloudymediummixlightning", 12);
		weatherPhenomenonMap.put("cloudymediumrain", 9);
		weatherPhenomenonMap.put("cloudymediumrainlightning", 12);
		weatherPhenomenonMap.put("cloudymediumsleet", 6);
		weatherPhenomenonMap.put("cloudymediumsleetlightning", 7);
		weatherPhenomenonMap.put("cloudymediumsnow", 17);
		weatherPhenomenonMap.put("cloudymediumsnowlightning", 19);
		weatherPhenomenonMap.put("cloudyverylightfreezingrain", 20);
		weatherPhenomenonMap.put("cloudyverylightmix", 6);
		weatherPhenomenonMap.put("cloudyverylightrain", 8);
		weatherPhenomenonMap.put("cloudyverylightsleet", 6);
		weatherPhenomenonMap.put("cloudyverylightsleet", 6);
		weatherPhenomenonMap.put("cloudyverylightsnow", 16);
		weatherPhenomenonMap.put("mostlyclear", 29);
		weatherPhenomenonMap.put("mostlyclearheavyfreezingrain", 59);
		weatherPhenomenonMap.put("mostlyclearheavyfreezingrainlightning", 60);
		weatherPhenomenonMap.put("mostlyclearheavymix", 43);
		weatherPhenomenonMap.put("mostlyclearheavymixlightning", 44);
		weatherPhenomenonMap.put("mostlyclearheavyrain", 47);
		weatherPhenomenonMap.put("mostlyclearheavyrainlightning", 49);
		weatherPhenomenonMap.put("mostlyclearheavysleet", 43);
		weatherPhenomenonMap.put("mostlyclearheavysleetlightning", 44);
		weatherPhenomenonMap.put("mostlyclearheavysnow", 67);
		weatherPhenomenonMap.put("mostlyclearheavysnowlightning", 68);
		weatherPhenomenonMap.put("mostlyclearlightfreezingrain", 57);
		weatherPhenomenonMap.put("mostlyclearlightmix", 43);
		weatherPhenomenonMap.put("mostlyclearlightrain", 45);
		weatherPhenomenonMap.put("mostlyclearlightrainlightning", 48);
		weatherPhenomenonMap.put("mostlyclearlightsleet", 43);
		weatherPhenomenonMap.put("mostlyclearlightsnow", 53);
		weatherPhenomenonMap.put("mostlyclearmediumfreezingrain", 58);
		weatherPhenomenonMap.put("mostlyclearmediumfreezingrainlightning", 60);
		weatherPhenomenonMap.put("mostlyclearmediummix", 43);
		weatherPhenomenonMap.put("mostlyclearmediummixlightning", 44);
		weatherPhenomenonMap.put("mostlyclearmediumrain", 46);
		weatherPhenomenonMap.put("mostlyclearmediumrainlightning", 49);
		weatherPhenomenonMap.put("mostlyclearmediumsleet", 43);
		weatherPhenomenonMap.put("mostlyclearmediumsleetlightning", 44);
		weatherPhenomenonMap.put("mostlyclearmediumsnow", 54);
		weatherPhenomenonMap.put("mostlyclearmediumsnowlightning", 56);
		weatherPhenomenonMap.put("mostlyclearverylightfreezingrain", 57);
		weatherPhenomenonMap.put("mostlyclearverylightmix", 43);
		weatherPhenomenonMap.put("mostlyclearverylightrain", 45);
		weatherPhenomenonMap.put("mostlyclearverylightsleet", 43);
		weatherPhenomenonMap.put("mostlyclearverylightsnow", 53);
		weatherPhenomenonMap.put("mostlycloudy", 1);
		weatherPhenomenonMap.put("mostlycloudyheavyfreezingrain", 22);
		weatherPhenomenonMap.put("mostlycloudyheavyfreezingrainlightning", 23);
		weatherPhenomenonMap.put("mostlycloudyheavymix", 6);
		weatherPhenomenonMap.put("mostlycloudyheavymixlightning", 7);
		weatherPhenomenonMap.put("mostlycloudyheavyrain", 10);
		weatherPhenomenonMap.put("mostlycloudyheavyrainlightning", 12);
		weatherPhenomenonMap.put("mostlycloudyheavysleet", 6);
		weatherPhenomenonMap.put("mostlycloudyheavysleetlightning", 7);
		weatherPhenomenonMap.put("mostlycloudyheavysnow", 18);
		weatherPhenomenonMap.put("mostlycloudyheavysnowlightning", 19);
		weatherPhenomenonMap.put("mostlycloudylightfreezingrain", 20);
		weatherPhenomenonMap.put("mostlycloudylightmix", 6);
		weatherPhenomenonMap.put("mostlycloudylightrain", 8);
		weatherPhenomenonMap.put("mostlycloudylightrainlightning", 11);
		weatherPhenomenonMap.put("mostlycloudylightsleet", 6);
		weatherPhenomenonMap.put("mostlycloudylightsnow", 16);
		weatherPhenomenonMap.put("mostlycloudymediumfreezingrain", 21);
		weatherPhenomenonMap.put("mostlycloudymediumfreezingrainlightning", 23);
		weatherPhenomenonMap.put("mostlycloudymediummix", 6);
		weatherPhenomenonMap.put("mostlycloudymediummixlightning", 7);
		weatherPhenomenonMap.put("mostlycloudymediumrain", 9);
		weatherPhenomenonMap.put("mostlycloudymediumrainlightning", 12);
		weatherPhenomenonMap.put("mostlycloudymediumsleet", 6);
		weatherPhenomenonMap.put("mostlycloudymediumsleetlightning", 7);
		weatherPhenomenonMap.put("mostlycloudymediumsnow", 17);
		weatherPhenomenonMap.put("mostlycloudymediumsnowlightning", 19);
		weatherPhenomenonMap.put("mostlycloudyverylightfreezingrain", 20);
		weatherPhenomenonMap.put("mostlycloudyverylightmix", 6);
		weatherPhenomenonMap.put("mostlycloudyverylightrain", 8);
		weatherPhenomenonMap.put("mostlycloudyverylightsleet", 6);
		weatherPhenomenonMap.put("mostlycloudyverylightsnow", 16);
		weatherPhenomenonMap.put("partlycloudy", 2);
		weatherPhenomenonMap.put("partlycloudyheavyfreezingrain", 59);
		weatherPhenomenonMap.put("partlycloudyheavyfreezingrainlightning", 60);
		weatherPhenomenonMap.put("partlycloudyheavymix", 43);
		weatherPhenomenonMap.put("partlycloudyheavymixlightning", 44);
		weatherPhenomenonMap.put("partlycloudyheavyrain", 47);
		weatherPhenomenonMap.put("partlycloudyheavyrainlightning", 49);
		weatherPhenomenonMap.put("partlycloudyheavysleet", 43);
		weatherPhenomenonMap.put("partlycloudyheavysleetlightning", 44);
		weatherPhenomenonMap.put("partlycloudyheavysnow", 55);
		weatherPhenomenonMap.put("partlycloudyheavysnowlightning", 56);
		weatherPhenomenonMap.put("partlycloudylightfreezingrain", 57);
		weatherPhenomenonMap.put("partlycloudylightmix", 43);
		weatherPhenomenonMap.put("partlycloudylightrain", 45);
		weatherPhenomenonMap.put("partlycloudylightrainlightning", 48);
		weatherPhenomenonMap.put("partlycloudylightsleet", 43);
		weatherPhenomenonMap.put("partlycloudylightsnow", 53);
		weatherPhenomenonMap.put("partlycloudymediumfreezingrain", 58);
		weatherPhenomenonMap.put("partlycloudymediumfreezingrainlightning", 60);
		weatherPhenomenonMap.put("partlycloudymediummix", 43);
		weatherPhenomenonMap.put("partlycloudymediummixlightning", 44);
		weatherPhenomenonMap.put("partlycloudymediumrain", 46);
		weatherPhenomenonMap.put("partlycloudymediumrainlightning", 49);
		weatherPhenomenonMap.put("partlycloudymediumsleet", 43);
		weatherPhenomenonMap.put("partlycloudymediumsleetlightning", 44);
		weatherPhenomenonMap.put("partlycloudymediumsnow", 54);
		weatherPhenomenonMap.put("partlycloudymediumsnowlightning", 56);
		weatherPhenomenonMap.put("partlycloudyverylightfreezingrain", 57);
		weatherPhenomenonMap.put("partlycloudyverylightmix", 43);
		weatherPhenomenonMap.put("partlycloudyverylightrain", 45);
		weatherPhenomenonMap.put("partlycloudyverylightsleet", 43);
		weatherPhenomenonMap.put("partlycloudyverylightsnow", 53);
	}
}
