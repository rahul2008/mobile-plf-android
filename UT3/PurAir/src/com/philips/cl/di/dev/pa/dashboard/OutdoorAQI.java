package com.philips.cl.di.dev.pa.dashboard;

import android.content.Context;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;

public class OutdoorAQI {

	private int pm25;
	private int outdoorAqi;
	private String time;
	private String areaID;

	public OutdoorAQI(int p1, int p2, String p9, String areaID) {
		this.pm25 = p1;
		this.outdoorAqi = p2;
		this.time = p9;
		this.areaID = areaID;
	}

	public int getPM25() {
		return pm25;
	}
	public int getAQI() {
		return outdoorAqi;
	}
	public String getPublishTime() {
		return time;
	}
	public String getAreaID() {
		return areaID;
	}

	public String getAqiTitle() {
		if(outdoorAqi >= 0 && outdoorAqi <= 50) {
			return PurAirApplication.getAppContext().getString(R.string.good);
		} else if(outdoorAqi > 50 && outdoorAqi <= 100) {
			return PurAirApplication.getAppContext().getString(R.string.moderate);
		} else if(outdoorAqi > 100 && outdoorAqi <= 150) {
			return PurAirApplication.getAppContext().getString(R.string.unhealthy_for_sensitive_groups);
		} else if(outdoorAqi > 150 && outdoorAqi <= 200) {
			return PurAirApplication.getAppContext().getString(R.string.unhealthy);
		} else if(outdoorAqi > 200 && outdoorAqi <= 300) {
			return PurAirApplication.getAppContext().getString(R.string.very_unhealthy_split);
		} else if(outdoorAqi > 300) {
			return PurAirApplication.getAppContext().getString(R.string.hazardous);
		}
		return "";
	}

	public String[] getAqiSummary() {
		String [] outdoorAQISummary = new String [2] ;
		Context appContext = PurAirApplication.getAppContext() ;
		if(outdoorAqi >= 0 && outdoorAqi <= 50) {
			outdoorAQISummary[0] = appContext.getString(R.string.outdoor_aqi_good_tip1);
			outdoorAQISummary[1] = appContext.getString(R.string.outdoor_aqi_good_tip2);
		} else if(outdoorAqi > 50 && outdoorAqi <= 100) {
			outdoorAQISummary[0] = appContext.getString(R.string.outdoor_aqi_moderate_tip1);
			outdoorAQISummary[1] = appContext.getString(R.string.outdoor_aqi_moderate_tip2);
		} else if(outdoorAqi > 100 && outdoorAqi <= 150) {
			outdoorAQISummary[0] = appContext.getString(R.string.outdoor_aqi_unhealthy_sensitive_group_tip1);
			outdoorAQISummary[1] = appContext.getString(R.string.outdoor_aqi_unhealthy_sensitive_group_tip2);
		} else if(outdoorAqi > 150 && outdoorAqi <= 200) {
			outdoorAQISummary[0] = appContext.getString(R.string.outdoor_aqi_unhealthy_tip1);
			outdoorAQISummary[1] = appContext.getString(R.string.outdoor_aqi_unhealthy_tip2);
		} else if(outdoorAqi > 200 && outdoorAqi <= 300) {
			outdoorAQISummary[0] = appContext.getString(R.string.outdoor_aqi_very_unhealthy_tip1) ;
			outdoorAQISummary[1] = appContext.getString(R.string.outdoor_aqi_very_unhealthy_tip2) ;
		} else if(outdoorAqi > 300) {
			outdoorAQISummary[0] = appContext.getString(R.string.outdoor_aqi_hazardous_tip1) ;
			outdoorAQISummary[1] = appContext.getString(R.string.outdoor_aqi_hazardous_tip2) ;
		}

		return outdoorAQISummary;
	}

	public int getAqiPointerImageResId() {

		if(outdoorAqi >= 0 && outdoorAqi <= 50) {
			return R.drawable.blue_circle_with_arrow_small;
		} else if(outdoorAqi > 50 && outdoorAqi <= 100) {
			return R.drawable.pink_circle_with_arrow_small;
		} else if(outdoorAqi > 100 && outdoorAqi <= 150) {
			return R.drawable.light_pink_circle_arrow_small;
		} else if(outdoorAqi > 150 && outdoorAqi <= 200) {
			return R.drawable.light_pink_circle_arrow1_small;
		} else if(outdoorAqi > 200 && outdoorAqi <= 300) {
			return R.drawable.red_circle_arrow_small;
		} else if(outdoorAqi > 300 && outdoorAqi <= 500) {
			return R.drawable.light_red_circle_arrow_small;
		}

		return R.drawable.blue_circle_with_arrow_small;
	}

	public float getAqiPointerRotaion() {
		float rotation = 0.0f;
		float aqiFloat = (float) outdoorAqi;
		if(outdoorAqi >= 0 && outdoorAqi <= 50) {
			rotation = aqiFloat * 0.54f;
		} else if(outdoorAqi > 50 && outdoorAqi <= 100) {
			aqiFloat -= 50;
			rotation = 4 + 28 + aqiFloat * 0.54f;  //+5 offset to fix image mis-alignment.
		} else if(outdoorAqi > 100 && outdoorAqi <= 150) {
			aqiFloat -= 100;
			rotation = 57 + aqiFloat * 0.54f;
		} else if(outdoorAqi > 150 && outdoorAqi <= 200) {
			aqiFloat -= 150;
			rotation = -3 + 86 + aqiFloat * 0.54f; //-3 offset to fix image mis-alignment.
		} else if(outdoorAqi > 200 && outdoorAqi <= 300) {
			aqiFloat -= 200;
			rotation = -3 + 114 + aqiFloat * 0.705f; //-3 offset to fix image mis-alignment.
		} else if(outdoorAqi > 300) {
			aqiFloat -= 300;
			rotation = 187 + aqiFloat * 0.163f;
			if(rotation > 301) {
				rotation = 301.0f;
			}
		}
		return rotation;
	}
}
