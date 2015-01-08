package com.philips.cl.di.dev.pa.dashboard;

import java.io.Serializable;

import android.content.Context;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;

public class OutdoorAQI implements  Serializable {

	private static final long serialVersionUID = 1L;
	private int p1;
	private int p2;
	private int p3;
	private int p4;
	private int p5;
	private String timeStamp;
	private String areaID;

	public OutdoorAQI(int p1, int p2, int pm10, int so2, 
			int no2, String areaID, String timeStamp) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = pm10;
		this.p4 = so2;
		this.p5 = no2;
		this.areaID = areaID;
		this.timeStamp = timeStamp;
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}

	public int getPM25() {
		return p1;
	}
	public int getAQI() {
		return p2;
	}
	public String getAreaID() {
		return areaID;
	}
	
	public int getPm10() {
		return p3;
	}

	public int getSo2() {
		return p4;
	}

	public int getNo2() {
		return p5;
	}
	
	public String getAqiTitle() {
		if(p2 >= 0 && p2 <= 50) {
			return PurAirApplication.getAppContext().getString(R.string.good_outdoor);
		} else if(p2 > 50 && p2 <= 100) {
			return PurAirApplication.getAppContext().getString(R.string.moderate_outdoor);
		} else if(p2 > 100 && p2 <= 150) {
			return PurAirApplication.getAppContext().getString(R.string.slightly_polluted_outdoor);
		} else if(p2 > 150 && p2 <= 200) {
			return PurAirApplication.getAppContext().getString(R.string.moderately_polluted_outdoor);
		} else if(p2 > 200 && p2 <= 300) {
			return PurAirApplication.getAppContext().getString(R.string.unhealthy_outdoor);
		} else if(p2 > 300) {
			return PurAirApplication.getAppContext().getString(R.string.hazardous_outdoor);
		}
		return "";
	}

	public String[] getAqiSummary() {
		String [] outdoorAQISummary = new String [2] ;
		Context appContext = PurAirApplication.getAppContext() ;
		if(p2 >= 0 && p2 <= 50) {
			outdoorAQISummary[0] = appContext.getString(R.string.outdoor_aqi_good_tip1);
			outdoorAQISummary[1] = appContext.getString(R.string.outdoor_aqi_good_tip2);
		} else if(p2 > 50 && p2 <= 100) {
			outdoorAQISummary[0] = appContext.getString(R.string.outdoor_aqi_moderate_tip1);
			outdoorAQISummary[1] = appContext.getString(R.string.outdoor_aqi_moderate_tip2);
		} else if(p2 > 100 && p2 <= 150) {
			outdoorAQISummary[0] = appContext.getString(R.string.outdoor_aqi_unhealthy_sensitive_group_tip1);
			outdoorAQISummary[1] = appContext.getString(R.string.outdoor_aqi_unhealthy_sensitive_group_tip2);
		} else if(p2 > 150 && p2 <= 200) {
			outdoorAQISummary[0] = appContext.getString(R.string.outdoor_aqi_unhealthy_tip1);
			outdoorAQISummary[1] = appContext.getString(R.string.outdoor_aqi_unhealthy_tip2);
		} else if(p2 > 200 && p2 <= 300) {
			outdoorAQISummary[0] = appContext.getString(R.string.outdoor_aqi_very_unhealthy_tip1) ;
			outdoorAQISummary[1] = appContext.getString(R.string.outdoor_aqi_very_unhealthy_tip2) ;
		} else if(p2 > 300) {
			outdoorAQISummary[0] = appContext.getString(R.string.outdoor_aqi_hazardous_tip1) ;
			outdoorAQISummary[1] = appContext.getString(R.string.outdoor_aqi_hazardous_tip2) ;
		}

		return outdoorAQISummary;
	}

	public int getAqiPointerImageResId() {

		if(p2 >= 0 && p2 <= 50) {
			return R.drawable.blue_circle_with_arrow_small;
		} else if(p2 > 50 && p2 <= 100) {
			return R.drawable.pink_circle_with_arrow_small;
		} else if(p2 > 100 && p2 <= 150) {
			return R.drawable.light_pink_circle_arrow_small;
		} else if(p2 > 150 && p2 <= 200) {
			return R.drawable.light_pink_circle_arrow1_small;
		} else if(p2 > 200 && p2 <= 300) {
			return R.drawable.red_circle_arrow_small;
		} else if(p2 > 300) {
			return R.drawable.light_red_circle_arrow_small;
		}

		return R.drawable.blue_circle_with_arrow_small;
	}

	public float getAqiPointerRotaion() {
		float rotation = 0.0f;
		float aqiFloat = (float) p2;
		if(p2 >= 0 && p2 <= 50) {
			rotation = aqiFloat * 0.54f;
		} else if(p2 > 50 && p2 <= 100) {
			aqiFloat -= 50;
			rotation = 4 + 28 + aqiFloat * 0.54f;  //+5 offset to fix image mis-alignment.
		} else if(p2 > 100 && p2 <= 150) {
			aqiFloat -= 100;
			rotation = 57 + aqiFloat * 0.54f;
		} else if(p2 > 150 && p2 <= 200) {
			aqiFloat -= 150;
			rotation = -3 + 86 + aqiFloat * 0.54f; //-3 offset to fix image mis-alignment.
		} else if(p2 > 200 && p2 <= 300) {
			aqiFloat -= 200;
			rotation = -3 + 114 + aqiFloat * 0.705f; //-3 offset to fix image mis-alignment.
		} else if(p2 > 300) {
			aqiFloat -= 300;
			rotation = 187 + aqiFloat * 0.163f;
			if(rotation > 301) {
				rotation = 301.0f;
			}
		}
		return rotation;
	}
}
