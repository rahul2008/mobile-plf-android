package com.philips.cl.di.dev.pa.dashboard;

import java.io.Serializable;

import android.content.Context;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;


public class OutdoorDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String updatedTime;
	private String cityName;
	private String temperature;
	private String weatherIcon;
	private String aqi;
	private String geo;
	private String pm10 ;
	private String pm25 ;
	private String so2 ;
	private String no2 ;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getPm10() {
		return pm10;
	}

	public void setPm10(String pm10) {
		this.pm10 = pm10;
	}

	public String getPm25() {
		return pm25;
	}

	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}

	public String getSo2() {
		return so2;
	}

	public void setSo2(String so2) {
		this.so2 = so2;
	}

	public String getNo2() {
		return no2;
	}

	public void setNo2(String no2) {
		this.no2 = no2;
	}

	public String getGeo() {
		return geo;
	}
	public void setGeo(String geo) {
		this.geo = geo;
	}
	
	public String getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	
	public String getAqi() {
		return aqi;
	}
	public void setAqi(String aqi) {
		if(aqi == null) {
			aqi = "";
		}
		this.aqi = aqi;
	}
	
	public String getAqiTitle() {
		if(aqi == null || aqi.isEmpty()) {
			return "";
		}
		
		try {
			int aqiInt = Integer.parseInt(aqi);
			if(aqiInt >= 0 && aqiInt <= 50) {
				 return PurAirApplication.getAppContext().getString(R.string.good);
			} else if(aqiInt > 50 && aqiInt <= 100) {
				return PurAirApplication.getAppContext().getString(R.string.moderate);
			} else if(aqiInt > 100 && aqiInt <= 150) {
				return PurAirApplication.getAppContext().getString(R.string.unhealthy_for_sensitive_groups);
			} else if(aqiInt > 150 && aqiInt <= 200) {
				return PurAirApplication.getAppContext().getString(R.string.unhealthy);
			} else if(aqiInt > 200 && aqiInt <= 300) {
				return PurAirApplication.getAppContext().getString(R.string.very_unhealthy_split);
			} else if(aqiInt > 300) {
				return PurAirApplication.getAppContext().getString(R.string.hazardous);
			}
		} catch (NumberFormatException e) {
			return "";
		}
		return "";
	}

	public String[] getAqiSummary() {
		String [] outdoorAQISummary = new String [2] ;
		Context appContext = PurAirApplication.getAppContext() ;
		if(aqi == null || aqi.isEmpty()) {
			return outdoorAQISummary;
		}
		
		try {
			int aqiInt = Integer.parseInt(aqi);
			if(aqiInt >= 0 && aqiInt <= 50) {
				outdoorAQISummary[0] = appContext.getString(R.string.outdoor_aqi_good_tip1);
				outdoorAQISummary[1] = appContext.getString(R.string.outdoor_aqi_good_tip2);
			} else if(aqiInt > 50 && aqiInt <= 100) {
				outdoorAQISummary[0] = appContext.getString(R.string.outdoor_aqi_moderate_tip1);
				outdoorAQISummary[1] = appContext.getString(R.string.outdoor_aqi_moderate_tip2);
			} else if(aqiInt > 100 && aqiInt <= 150) {
				outdoorAQISummary[0] = appContext.getString(R.string.outdoor_aqi_unhealthy_sensitive_group_tip1);
				outdoorAQISummary[1] = appContext.getString(R.string.outdoor_aqi_unhealthy_sensitive_group_tip2);
			} else if(aqiInt > 150 && aqiInt <= 200) {
				outdoorAQISummary[0] = appContext.getString(R.string.outdoor_aqi_unhealthy_tip1);
				outdoorAQISummary[1] = appContext.getString(R.string.outdoor_aqi_unhealthy_tip2);
			} else if(aqiInt > 200 && aqiInt <= 300) {
				outdoorAQISummary[0] = appContext.getString(R.string.outdoor_aqi_very_unhealthy_tip1) ;
				outdoorAQISummary[1] = appContext.getString(R.string.outdoor_aqi_very_unhealthy_tip2) ;
			} else if(aqiInt > 300) {
				outdoorAQISummary[0] = appContext.getString(R.string.outdoor_aqi_hazardous_tip1) ;
				outdoorAQISummary[1] = appContext.getString(R.string.outdoor_aqi_hazardous_tip2) ;
			}
		} catch (NumberFormatException e) {
			return outdoorAQISummary;
		}
		
		return outdoorAQISummary;
	}
	
	public int getAqiPointerImageResId() {

		if(aqi == null || aqi.isEmpty()) {
			return R.drawable.blue_circle_with_arrow_small;
		}
		
		try {
			int aqiInt = Integer.parseInt(aqi);
			if(aqiInt >= 0 && aqiInt <= 50) {
				 return R.drawable.blue_circle_with_arrow_small;
			} else if(aqiInt > 50 && aqiInt <= 100) {
				return R.drawable.pink_circle_with_arrow_small;
			} else if(aqiInt > 100 && aqiInt <= 150) {
				return R.drawable.light_pink_circle_arrow_small;
			} else if(aqiInt > 150 && aqiInt <= 200) {
				return R.drawable.light_pink_circle_arrow1_small;
			} else if(aqiInt > 200 && aqiInt <= 300) {
				return R.drawable.red_circle_arrow_small;
			} else if(aqiInt > 300 && aqiInt <= 500) {
				return R.drawable.light_red_circle_arrow_small;
			}
		} catch (NumberFormatException e) {
			return R.drawable.blue_circle_with_arrow_small;
		}

		return R.drawable.blue_circle_with_arrow_small;
	}
	
	public float getAqiPointerRotaion() {
		if(aqi == null || aqi.isEmpty()) {
			return 0.0f;
		}
		float rotation = 0.0f;
		try {
			int aqiInt = Integer.parseInt(aqi);
			float aqiFloat = Float.parseFloat(aqi);
			if(aqiInt >= 0 && aqiInt <= 50) {
				rotation = aqiFloat * 0.54f;
			} else if(aqiInt > 50 && aqiInt <= 100) {
				aqiFloat -= 50;
				rotation = 4 + 28 + aqiFloat * 0.54f;  //+5 offset to fix image mis-alignment.
			} else if(aqiInt > 100 && aqiInt <= 150) {
				aqiFloat -= 100;
				rotation = 57 + aqiFloat * 0.54f;
			} else if(aqiInt > 150 && aqiInt <= 200) {
				aqiFloat -= 150;
				rotation = -3 + 86 + aqiFloat * 0.54f; //-3 offset to fix image mis-alignment.
			} else if(aqiInt > 200 && aqiInt <= 300) {
				aqiFloat -= 200;
				rotation = -3 + 114 + aqiFloat * 0.705f; //-3 offset to fix image mis-alignment.
			} else if(aqiInt > 300) {
				aqiFloat -= 300;
				rotation = 187 + aqiFloat * 0.163f;
				if(rotation > 301) {
					rotation = 301.0f;
				}
			}
		} catch (NumberFormatException e) {
			return 0.0f;
		}
		return rotation;
	}

	public String getWeatherIcon() {
		return weatherIcon;
	}
	public void setWeatherIcon(String weatherIcon) {
		this.weatherIcon = weatherIcon;
	}
	
	public int getWeatherIconResId() {
		if(weatherIcon.compareToIgnoreCase(AppConstants.SUNNY) == 0) {
			return R.drawable.sunny_white;
		} 
		if (weatherIcon.compareToIgnoreCase(AppConstants.MIST) == 0) {
			return R.drawable.mist_white;
		} 
		if (weatherIcon.compareToIgnoreCase(AppConstants.CLOUDY) == 0) {
			return R.drawable.cloudy_white;
		}
		if (weatherIcon.compareToIgnoreCase(AppConstants.PARTLY_CLOUDY) == 0) {
			return R.drawable.partly_cloudy_white;
		}
		if (weatherIcon.compareToIgnoreCase(AppConstants.PARTLY_CLOUDY) == 0) {
			return R.drawable.partly_cloudy_night_white;
		}
		if (weatherIcon.compareToIgnoreCase(AppConstants.CLEAR_SKIES) == 0) {
			return R.drawable.clear_sky_night_white;
		}
		if (weatherIcon.compareToIgnoreCase(AppConstants.SNOW) == 0) {
			return R.drawable.snow_white;
		}
		if (weatherIcon.compareToIgnoreCase(AppConstants.LIGHT_RAIN_SHOWER) == 0 
				|| weatherIcon.compareToIgnoreCase(AppConstants.LIGHT_DRIZZLE) == 0) {
			return R.drawable.light_rain_shower_white;
		}
		if (weatherIcon.compareToIgnoreCase(AppConstants.PATCHY_LIGHT_RAIN_IN_AREA_WITH_THUNDER) == 0) {
			return R.drawable.light_rain_with_thunder_white;
		}
		if (weatherIcon.compareToIgnoreCase(AppConstants.MODERATE_OR_HEAVY_RAIN_SHOWER) == 0 
				|| weatherIcon.compareToIgnoreCase(AppConstants.TORRENTIAL_RAIN_SHOWER) == 0 
				|| weatherIcon.compareToIgnoreCase(AppConstants.HEAVY_RAIN) == 0) {
			return R.drawable.heavy_rain_white;
		}
		if (weatherIcon.compareToIgnoreCase(AppConstants.HEAVY_RAIN_AT_TIMES) == 0) {
			//TODO : Replace with proper icon. Icon not found, replacing with heavy rain
			return R.drawable.heavy_rain_white; 
		}
		if (weatherIcon.compareToIgnoreCase(AppConstants.MODERATE_OR_HEAVY_RAIN_IN_AREA_WITH_THUNDER) == 0) {
			return R.drawable.moderate_rain_with_thunder_white;
		}
		if (weatherIcon.compareToIgnoreCase(AppConstants.CLEAR) == 0) {
			//TODO : Find isDayTime from webservice.
			// Moving to a different webservice, ignoring this.
			String isDayTime = "Yes";
			if(isDayTime.compareToIgnoreCase("Yes") == 0) {
				return R.drawable.sunny_white;
			} else {
				return R.drawable.clear_sky_night_white;
			}
		}
		
		return R.drawable.light_rain_shower_white;
	}
	
}
