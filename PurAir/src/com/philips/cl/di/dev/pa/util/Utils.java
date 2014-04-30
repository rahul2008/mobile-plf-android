package com.philips.cl.di.dev.pa.util;

import static com.philips.cl.di.dev.pa.constant.AnimatorConstants.ANIM_ROTATION;
import static com.philips.cl.di.dev.pa.constant.AppConstants.CLEAR;
import static com.philips.cl.di.dev.pa.constant.AppConstants.CLEAR_SKIES;
import static com.philips.cl.di.dev.pa.constant.AppConstants.CLOUDY;
import static com.philips.cl.di.dev.pa.constant.AppConstants.HEAVY_RAIN;
import static com.philips.cl.di.dev.pa.constant.AppConstants.HEAVY_RAIN_AT_TIMES;
import static com.philips.cl.di.dev.pa.constant.AppConstants.LIGHT_DRIZZLE;
import static com.philips.cl.di.dev.pa.constant.AppConstants.LIGHT_RAIN_SHOWER;
import static com.philips.cl.di.dev.pa.constant.AppConstants.MIST;
import static com.philips.cl.di.dev.pa.constant.AppConstants.MODERATE_OR_HEAVY_RAIN_IN_AREA_WITH_THUNDER;
import static com.philips.cl.di.dev.pa.constant.AppConstants.MODERATE_OR_HEAVY_RAIN_SHOWER;
import static com.philips.cl.di.dev.pa.constant.AppConstants.PARTLY_CLOUDY;
import static com.philips.cl.di.dev.pa.constant.AppConstants.PATCHY_LIGHT_RAIN_IN_AREA_WITH_THUNDER;
import static com.philips.cl.di.dev.pa.constant.AppConstants.SNOW;
import static com.philips.cl.di.dev.pa.constant.AppConstants.SUNNY;
import static com.philips.cl.di.dev.pa.constant.AppConstants.TORRENTIAL_RAIN_SHOWER;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.datamodel.IndoorHistoryDto;
import com.philips.cl.di.dev.pa.datamodel.IndoorTrendDto;
import com.philips.cl.di.dev.pa.datamodel.OutdoorAQIEventDto;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.fragment.HomeFragment;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.view.FontTextView;

/**
 * The Class Utils.
 */
@SuppressLint("SimpleDateFormat")
public class Utils {

	/** The Constant TAG. */
	private static final String TAG = "Utils";
	//private static String currentDateHr = "";
	private static String ago24HrDate = "";
	private static String ago27DayDate = "";
	public static final List<Integer> OUTDOOR_AQI_PERCENTAGE_LIST = new ArrayList<Integer>();

	public static String getPortUrl(Port port, String ipAddress) {
		if (port == null) {
			return String.format(AppConstants.URL_BASEALLPORTS, ipAddress,"1", "invalidport") ;
		}
		return String.format(AppConstants.URL_BASEALLPORTS, ipAddress, port.port,port.urlPart) ;
	}

	public static int getDifferenceBetweenDaysFromCurrentDay(String date, String date0) {
		int noOfDays = -2;

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		long timeDiff = 0;
		try {
			Date lastDate = sf.parse(date);
			Date prevDate = sf.parse(date0);
			timeDiff = lastDate.getTime() - prevDate.getTime();
			noOfDays = (int) (timeDiff / (1000 * 60 * 60 * 24));
			ALog.i(ALog.INDOOR_RDCP, 
					"Download data date: " + date + " - 28 day ago date: " + date0 +" = " + noOfDays);
		} catch (ParseException e) {
			ALog.i(ALog.INDOOR_RDCP, "Date ParseException");
			return noOfDays;
		}
		return noOfDays;
	}

	public static int getDifferenceBetweenHrFromCurrentHr(String date, String date0) {
		
		int noOfHrs = -2;

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH");
		long timeDiff = 0;
		try {
			Date d = sf.parse(date);
			Date d0 = sf.parse(date0);
			if (d.getTime() >= d0.getTime()) {
				timeDiff = d.getTime() - d0.getTime();
			} else {
				return noOfHrs;
			}

			noOfHrs = (int) (timeDiff / (1000 * 60 * 60));
			ALog.i(ALog.INDOOR_RDCP, 
					"Download data date: " + date + " - 24 hr ago date: " + date0 +" = " + noOfHrs);
			
		} catch (ParseException e) {
			ALog.i(ALog.INDOOR_RDCP, "Date ParseException");
			return -2;
		}

		return noOfHrs;
	}

	@SuppressLint("UseSparseArrays")
	public static void getIndoorAqiValues(String downloadedData) {
		ALog.i(ALog.INDOOR_RDCP, "Rdcp values downloaded successfully");
		
		HashMap<Integer, Float> hrlyAqiValueMap = new HashMap<Integer, Float>();
		HashMap<Integer, Integer> hrlyAqiValueCounterMap = new HashMap<Integer, Integer>();
		
		HashMap<Integer, Float> dailyAqiValueMap = new HashMap<Integer, Float>();
		HashMap<Integer, Integer> dailyAqiValueCounterMap = new HashMap<Integer, Integer>();

		if (downloadedData != null) {
			List<IndoorHistoryDto> indoorAQIHistory = DataParser.parseHistoryData(downloadedData);
			
			if (indoorAQIHistory != null && indoorAQIHistory.size() > 0) {
				
				for (int index = 0; index < indoorAQIHistory.size(); index++) {
					String date = indoorAQIHistory.get(index).getTimeStamp();
					ALog.i(ALog.INDOOR_RDCP, "Date: " + date +",  " + indoorAQIHistory.get(index).getAqi());
					/**
					 * Hourly
					 */
					int diffOfHrs = Utils.getDifferenceBetweenHrFromCurrentHr(
							date.substring(0, 10) + " "+ date.substring(11, 13), ago24HrDate);
					if (diffOfHrs >= 0) {
						if (hrlyAqiValueMap.containsKey(diffOfHrs) 
								&& hrlyAqiValueCounterMap.containsKey(diffOfHrs)) {
							float faqi = hrlyAqiValueMap.get(diffOfHrs);
							faqi = faqi + indoorAQIHistory.get(index).getAqi();
							hrlyAqiValueMap.put(diffOfHrs, faqi);
							int counterMap = hrlyAqiValueCounterMap.get(diffOfHrs);
							counterMap++;
							hrlyAqiValueCounterMap.put(diffOfHrs, counterMap);
							
						} else {
							hrlyAqiValueCounterMap.put(diffOfHrs, 1);
							hrlyAqiValueMap.put(diffOfHrs, indoorAQIHistory.get(index).getAqi());
						}
					}
					
					/**
					 * Daily
					 */
					int numberOfDays = 
							getDifferenceBetweenDaysFromCurrentDay(date.substring(0, 10), ago27DayDate);
					
					if (numberOfDays >= 0) {
						if (dailyAqiValueMap.containsKey(numberOfDays) 
								&& dailyAqiValueCounterMap.containsKey(numberOfDays)) {
							float faqi = dailyAqiValueMap.get(numberOfDays);
							faqi = faqi + indoorAQIHistory.get(index).getAqi();
							dailyAqiValueMap.put(numberOfDays, faqi);
							int counterMap = dailyAqiValueCounterMap.get(numberOfDays);
							counterMap++;
							dailyAqiValueCounterMap.put(numberOfDays, counterMap);
							
						} else {
							dailyAqiValueCounterMap.put(numberOfDays, 1);
							dailyAqiValueMap.put(numberOfDays, indoorAQIHistory.get(index).getAqi());
						}
					}
				}
				
				setIndoorAqiHistory(hrlyAqiValueMap,hrlyAqiValueCounterMap, 
						dailyAqiValueMap, dailyAqiValueCounterMap);

			}
		}
	}
	
	private static void setIndoorAqiHistory(HashMap<Integer, Float> hrlyAqiValueMap,
			HashMap<Integer, Integer> hrlyAqiValueCounterMap, 
			HashMap<Integer, Float> dailyAqiValueMap, 
			HashMap<Integer, Integer> dailyAqiValueCounterMap) {
		
		List<Float> hrlyAqiValues = new ArrayList<Float>();
		List<Float> dailyAqiValues = new ArrayList<Float>();
		
		IndoorTrendDto indoorTrend = new IndoorTrendDto();
		
		for (int i = 0; i < 24; i++) {
			if (hrlyAqiValueMap.containsKey(i) 
					&& hrlyAqiValueCounterMap.containsKey(i)) {
				float aqi = hrlyAqiValueMap.get(i);
				int noOffValue = hrlyAqiValueCounterMap.get(i);
				aqi = aqi / noOffValue;
				hrlyAqiValues.add(aqi/100);
			} else {
				hrlyAqiValues.add(-1F);
			}
		}
		
		ALog.i(ALog.INDOOR_RDCP, "Rdcp hrlyAqiValueMap: " + hrlyAqiValueMap);
		ALog.i(ALog.INDOOR_RDCP, "Rdcp hrlyAqiValues counter: " + hrlyAqiValueCounterMap);
		ALog.i(ALog.INDOOR_RDCP, "Rdcp hrlyAqiValues: " + hrlyAqiValues);
		
		indoorTrend.setHourlyList(hrlyAqiValues);
		
		hrlyAqiValueMap = null;
		hrlyAqiValueCounterMap = null;
		hrlyAqiValues = null;
		
		for (int I = 0; I < 28; I++) {
			if (dailyAqiValueMap.containsKey(I) 
					&& dailyAqiValueCounterMap.containsKey(I)) {
				float aqi = dailyAqiValueMap.get(I);
				int noOffValue = dailyAqiValueCounterMap.get(I);
				aqi = aqi / noOffValue;
				dailyAqiValues.add(aqi/100);
			} else {
				dailyAqiValues.add(-1F);
			}
		}
		
		ALog.i(ALog.INDOOR_RDCP, "Rdcp dailyAqiValueMap: " + dailyAqiValueMap);
		ALog.i(ALog.INDOOR_RDCP, "Rdcp dailyAqiValues counter: " + dailyAqiValueCounterMap);
		ALog.i(ALog.INDOOR_RDCP, "Rdcp dailyAqiValues: " + dailyAqiValues);
		
		indoorTrend.setDailyList(dailyAqiValues);
		
		dailyAqiValueMap = null;
		dailyAqiValueCounterMap = null;
		dailyAqiValues = null;
		
		SessionDto.getInstance().setIndoorTrendDto(indoorTrend);
	}

	@SuppressLint("SimpleDateFormat")
	public static String getCPPQuery(PurAirDevice purifier) {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatTime = new SimpleDateFormat(":mm:ss");
		
		Calendar cal = Calendar.getInstance();
		int hrOffDayInt = cal.get(Calendar.HOUR_OF_DAY);
		String hrOffDay = get2DigitHr(hrOffDayInt);
		String endDate = formatDate.format(new Date());
		String endTime = hrOffDay + formatTime.format(new Date());
		
		String qryPart3 = "endDate=" + endDate + "T" + endTime + ".1508314Z";

		long lt28 = 28 * 24 * 60 * 60 * 1000L;
		long startDateDiff = cal.getTimeInMillis() - lt28;
		Date dateStart = new Date(startDateDiff);
		String startDate = formatDate.format(dateStart);
		String startTime = hrOffDay + formatTime.format(dateStart);

		String qryPart2 = "startDate=" + startDate + "T" + startTime + ".1508314Z;";

//		String qry = String.format(
//				AppConstants.CLIENT_ID_RDCP, "1c5a6bfffe6c74b1") + qryPart2 + qryPart3;
		
		String qry = String.format(AppConstants.CLIENT_ID_RDCP, purifier.getEui64()) + qryPart2 + qryPart3;

		ALog.i(ALog.INDOOR_RDCP, "rdcp qry:   "+qry);
		long lt1 = 24 * 60 * 60 * 1000;
		long endDateDiff = cal.getTimeInMillis() - lt1;
		Date dateEnd = new Date(endDateDiff);
		hrOffDayInt = hrOffDayInt + 1; 
		if (hrOffDayInt >= 24) {
			hrOffDayInt = 0;
		}
		ago24HrDate = formatDate.format(dateEnd) + " " + get2DigitHr(hrOffDayInt);
		
		long lt27 = 27 * 24 * 60 * 60 * 1000L;
		long startDateDiff1 = cal.getTimeInMillis() - lt27;
		Date dateStart1 = new Date(startDateDiff1);
		ago27DayDate = formatDate.format(dateStart1);

		return qry;
	}
	
	private static String get2DigitHr( int hr) {
		String hrStr = null;
		if (hr == 0) {
			hrStr = "00";
		} else if (hr < 10) {
			hrStr = "0" + hr ;
		} else {
			hrStr = String.valueOf(hr);
		}
		return hrStr;
	}

	/**
	 * This method will set the AQI circle background depending on the AQI range
	 * 
	 * @param aqi
	 * @return
	 */
	public static Drawable getOutdoorAQICircleBackground(Context ctx, int aqi) {
		if (aqi >= 0 && aqi <= 50) {
			return ctx.getResources()
					.getDrawable(R.drawable.aqi_blue_circle_2x);
		} else if (aqi > 50 && aqi <= 100) {
			return ctx.getResources()
					.getDrawable(R.drawable.aqi_pink_circle_2x);
		} else if (aqi > 100 && aqi <= 150) {
			return ctx.getResources().getDrawable(
					R.drawable.aqi_bluedark_circle_2x);
		} else if (aqi > 150 && aqi <= 200) {
			return ctx.getResources().getDrawable(
					R.drawable.aqi_purple_circle_2x);
		} else if (aqi > 200 && aqi <= 300) {
			return ctx.getResources().getDrawable(
					R.drawable.aqi_fusia_circle_2x);
		} else if (aqi > 300 /* && aqi <= 500 */) {
			return ctx.getResources().getDrawable(R.drawable.aqi_red_circle_2x);
		}
		return null;
	}

	/**
	 * 
	 * @param ctx
	 * @param pSense
	 * @return
	 */
	public static Drawable getIndoorAQICircleBackground(Context ctx,
			float indoorAQI) {
		Log.i(TAG, "aqi=  " + indoorAQI);
		if (indoorAQI <= 1.4f) {
			return ctx.getResources()
					.getDrawable(R.drawable.aqi_blue_circle_2x);
		} else if (indoorAQI > 1.4f && indoorAQI <= 2.3f) {
			return ctx.getResources().getDrawable(
					R.drawable.aqi_purple_circle_2x);
		} else if (indoorAQI > 2.3f && indoorAQI <= 3.5f) {
			return ctx.getResources().getDrawable(
					R.drawable.aqi_fusia_circle_2x);
		} else if (indoorAQI > 3.5f) {
			return ctx.getResources().getDrawable(R.drawable.aqi_red_circle_2x);
		}
		return null;
	}

	/**
	 * 
	 * @param ctx
	 * @param pSense
	 * @param status
	 * @param summary
	 */
	public static void setIndoorAQIStatusAndComment
	(Context ctx, float indoorAQI, FontTextView status, FontTextView summary, String purifierName) {

		if(indoorAQI <= 1.4f && indoorAQI > 0) {
			status.setText(ctx.getString(R.string.good)) ;
			summary.setText(ctx.getString(R.string.indoor_aqi_good_tip1, purifierName)) ;
		} else if(indoorAQI > 1.4f && indoorAQI <= 2.3f) {
			status.setText(ctx.getString(R.string.moderate)) ;
			summary.setText(ctx.getString(R.string.indoor_aqi_moderate_tip1, purifierName)) ;
		} else if(indoorAQI > 2.3f && indoorAQI <= 3.5f) {
			status.setText(ctx.getString(R.string.unhealthy)) ;
			summary.setText(ctx.getString(R.string.indoor_aqi_unhealthy_tip1, purifierName)) ;
		} else if(indoorAQI > 3.5f) {
			String tempStatus[] = ctx.getString(R.string.very_unhealthy).trim().split(" ");
			if (tempStatus != null && tempStatus.length > 1) {
				status.setText(tempStatus[0] + "\n" + tempStatus[1]);
			} else {
				status.setText(ctx.getString(R.string.very_unhealthy));
			}
			summary.setText(ctx
					.getString(R.string.indoor_aqi_very_unhealthy_tip1, purifierName));
		}
	}

	public static int getPreFilterStatusColour(int filterStatusValue) {
		if (filterStatusValue < 96) {
			return AppConstants.COLOR_GOOD;
		} else if (filterStatusValue >= 96 && filterStatusValue < 112) {
			return AppConstants.COLOR_FAIR;
		} else {
			return AppConstants.COLOR_BAD;
		}
	}

	public static String getPreFilterStatusText(int filterStatusValue) {
		Context context=PurAirApplication.getAppContext();
		if (filterStatusValue < 96) {
			return context.getString(R.string.good);
		} else if (filterStatusValue >= 96 && filterStatusValue < 112) {
			return context.getString(R.string.clean_soon);
		} else {
			return context.getString(R.string.change_now);
		}
	}

	public static int getMultiCareFilterStatusColour(int filterStatusValue) {
		if (filterStatusValue < 784) {
			return AppConstants.COLOR_GOOD;
		} else if (filterStatusValue >= 784 && filterStatusValue < 840) {
			return AppConstants.COLOR_FAIR;
		} else {
			return AppConstants.COLOR_BAD;
		}
	}

	public static String getMultiCareFilterStatusText(int filterStatusValue) {
		Context context=PurAirApplication.getAppContext();
		if (filterStatusValue < 784) {
			return context.getString(R.string.good);
		} else if (filterStatusValue >= 784 && filterStatusValue < 840) {
			return context.getString(R.string.change_soon);
		} else if (filterStatusValue >= 840 && filterStatusValue < 960) {
			return context.getString(R.string.change_now);
		} else {
			return context.getString(R.string.filter_lock);
		}
	}

	public static int getActiveCarbonFilterStatusColour(int filterStatusValue) {
		if (filterStatusValue < 2704) {
			return AppConstants.COLOR_GOOD;
		} else if (filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return AppConstants.COLOR_FAIR;
		} else {
			return AppConstants.COLOR_BAD;
		}
	}

	public static String getActiveCarbonFilterStatusText(int filterStatusValue) {
		Context context=PurAirApplication.getAppContext();
		if (filterStatusValue < 2704) {
			return context.getString(R.string.good);
		} else if (filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return context.getString(R.string.change_soon);
		} else if (filterStatusValue >= 2760 && filterStatusValue < 2880) {
			return context.getString(R.string.change_now);
		} else {
			return context.getString(R.string.filter_lock);
		}
	}

	public static int getHEPAFilterStatusColour(int filterStatusValue) {
		if (filterStatusValue < 2704) {
			return AppConstants.COLOR_GOOD;
		} else if (filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return AppConstants.COLOR_FAIR;
		} else {
			return AppConstants.COLOR_BAD;
		}
	}

	public static String getHEPAFilterFilterStatusText(int filterStatusValue) {
		Context context=PurAirApplication.getAppContext();
		if (filterStatusValue < 2704) {
			return context.getString(R.string.good);
		} else if (filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return context.getString(R.string.change_soon);
		} else if (filterStatusValue >= 2760 && filterStatusValue < 2880) {
			return context.getString(R.string.change_now);
		} else {
			return context.getString(R.string.filter_lock);
		}
	}

	public static String getMode(String fanSpeed, Context context) {
		String mode = "";
		if (AppConstants.FAN_SPEED_SILENT.equals(fanSpeed)) {
			mode = context.getString(R.string.silent);
		} else if (AppConstants.FAN_SPEED_TURBO.equals(fanSpeed)) {
			mode = context.getString(R.string.turbo);
		} else if (AppConstants.FAN_SPEED_AUTO.equals(fanSpeed)) {
			mode = context.getString(R.string.auto);
		} else if (AppConstants.FAN_SPEED_ONE.equals(fanSpeed)) {
			mode = context.getString(R.string.speed1);
		} else if (AppConstants.FAN_SPEED_TWO.equals(fanSpeed)) {
			mode = context.getString(R.string.speed2);
		} else if (AppConstants.FAN_SPEED_THREE.equals(fanSpeed)) {
			mode = context.getString(R.string.speed3);
		}
		return mode;
	}

	public static String getFilterStatusForDashboard(
			AirPortInfo airPurifierEventDto) {
		String filterStatus = "-";
		if (airPurifierEventDto != null) {
			Context context=PurAirApplication.getAppContext();
			filterStatus = context.getString(R.string.good);
			String preFilterStatus = getPreFilterStatusText(airPurifierEventDto
					.getFilterStatus1());
			String multiCareFilterStatus = getMultiCareFilterStatusText(airPurifierEventDto
					.getFilterStatus2());
			String activeFilterStatus = getActiveCarbonFilterStatusText(airPurifierEventDto
					.getFilterStatus3());
			String hepaFilterStatus = getHEPAFilterFilterStatusText(airPurifierEventDto
					.getFilterStatus4());

			if (multiCareFilterStatus.equals(AppConstants.ACT_NOW)
					|| activeFilterStatus.equals(AppConstants.ACT_NOW)
					|| hepaFilterStatus.equals(AppConstants.ACT_NOW)) {
				filterStatus = context.getString(R.string.change_now);
			} else if (preFilterStatus.equals(AppConstants.CLEAN_NOW)) {
				filterStatus = context.getString(R.string.clean_now);
			} else if (preFilterStatus.equals(AppConstants.CLEAN_SOON)) {
				filterStatus =context.getString(R.string.clean_soon);
			} else if (multiCareFilterStatus.equals(AppConstants.ACT_SOON)
					|| activeFilterStatus.equals(AppConstants.ACT_SOON)
					|| hepaFilterStatus.equals(AppConstants.ACT_SOON)) {
				filterStatus = context.getString(R.string.change_now);
			} else if (multiCareFilterStatus.equals(AppConstants.FILTER_LOCK)
					|| activeFilterStatus.equals(AppConstants.FILTER_LOCK)
					|| hepaFilterStatus.equals(AppConstants.FILTER_LOCK)) {
				filterStatus = context.getString(R.string.filter_lock);
			}
		}
		return filterStatus;
	}

	public static int getIndoorAQIMessage(float aqi) {
		if (aqi <= 1.4f) {
			return R.string.indoor_aqi_good_tip1;
		} else if (aqi > 1.4f && aqi <= 2.3f) {
			return R.string.indoor_aqi_moderate_tip1;
		} else if (aqi > 2.3f && aqi <= 3.5f) {
			return R.string.indoor_aqi_unhealthy_tip1;
		} else if (aqi > 3.5f) {
			return R.string.indoor_aqi_very_unhealthy_tip1;
		}
		return R.string.n_a;
	}

	/**
	 * 
	 * @param goodAir
	 * @param totalAir
	 */
	public static int getPercentage(int goodAir, int totalAir) {
		int percent = 0;
		if (totalAir > 0) {
			percent = (goodAir * 100) / totalAir;
		}
		return percent;
	}

	public static String getDayOfWeek(Context contex, int dayInt) {
		switch (dayInt) {
		case 1:
			return contex.getString(R.string.sun);
		case 2:
			return contex.getString(R.string.mon);
		case 3:
			return contex.getString(R.string.tue);
		case 4:
			return contex.getString(R.string.wed);
		case 5:
			return contex.getString(R.string.thu);
		case 6:
			return contex.getString(R.string.fri);
		case 7:
			return contex.getString(R.string.sat);
		}
		return null;
	}

	public static Drawable getOutdoorTemperatureImage(Context contex,
			String weatherDesc, String isDayTime) {
		Drawable weatherImage = null;
		if (weatherDesc == null || weatherDesc.equals("")) {
			return null;
		}

		if (weatherDesc.compareToIgnoreCase(SUNNY) == 0) {
			weatherImage = contex.getResources().getDrawable(R.drawable.sunny);
		} else if (weatherDesc.compareToIgnoreCase(MIST) == 0) {
			weatherImage = contex.getResources().getDrawable(R.drawable.mist);
		} else if (weatherDesc.compareToIgnoreCase(CLOUDY) == 0) {
			weatherImage = contex.getResources().getDrawable(R.drawable.cloudy);
		} else if (weatherDesc.compareToIgnoreCase(PARTLY_CLOUDY) == 0) {

			if (isDayTime.compareToIgnoreCase("Yes") == 0)
				weatherImage = contex.getResources().getDrawable(
						R.drawable.partly_cloudy);
			else
				weatherImage = contex.getResources().getDrawable(
						R.drawable.partly_cloudy_night);
			// weatherImage =
			// contex.getResources().getDrawable(R.drawable.partly_cloudy_night);
		} else if (weatherDesc.compareToIgnoreCase(CLEAR_SKIES) == 0) {
			if (isDayTime.compareToIgnoreCase("Yes") == 0)
				weatherImage = contex.getResources().getDrawable(
						R.drawable.sunny);
			else
				weatherImage = contex.getResources().getDrawable(
						R.drawable.clear_sky_night);
			// weatherImage =
			// contex.getResources().getDrawable(R.drawable.clear_sky_night);
		} else if (weatherDesc.compareToIgnoreCase(SNOW) == 0) {
			weatherImage = contex.getResources().getDrawable(R.drawable.snow);
		} else if (weatherDesc.compareToIgnoreCase(LIGHT_RAIN_SHOWER) == 0
				|| weatherDesc.compareToIgnoreCase(LIGHT_DRIZZLE) == 0) {
			weatherImage = contex.getResources().getDrawable(
					R.drawable.light_rain_shower);
		} else if (weatherDesc
				.compareToIgnoreCase(PATCHY_LIGHT_RAIN_IN_AREA_WITH_THUNDER) == 0) {
			weatherImage = contex.getResources().getDrawable(
					R.drawable.light_rain_with_thunder);
		} else if (weatherDesc
				.compareToIgnoreCase(MODERATE_OR_HEAVY_RAIN_SHOWER) == 0
				|| weatherDesc.compareToIgnoreCase(TORRENTIAL_RAIN_SHOWER) == 0
				|| weatherDesc.compareToIgnoreCase(HEAVY_RAIN) == 0) {
			weatherImage = contex.getResources().getDrawable(
					R.drawable.heavy_rain);
		} else if (weatherDesc.compareToIgnoreCase(HEAVY_RAIN_AT_TIMES) == 0) {
			// TODO : Replace with proper icon. Icon not found, replacing with
			// heavy raind
			weatherImage = contex.getResources().getDrawable(
					R.drawable.heavy_rain);
		} else if (weatherDesc
				.compareToIgnoreCase(MODERATE_OR_HEAVY_RAIN_IN_AREA_WITH_THUNDER) == 0) {
			weatherImage = contex.getResources().getDrawable(
					R.drawable.moderate_rain_with_thunder);
		} else if (weatherDesc.compareToIgnoreCase(CLEAR) == 0) {
			if (isDayTime.compareToIgnoreCase("Yes") == 0)
				weatherImage = contex.getResources().getDrawable(R.drawable.sunny);
			else
				weatherImage = contex.getResources().getDrawable(R.drawable.clear_sky_night);
		} else {
			weatherImage = contex.getResources().getDrawable(R.drawable.light_rain_shower);
		}

		return weatherImage;
	}

	public static void setOutdoorWeatherDirImg(Context contex, float windSpeed,
			String windDir, float degree, ImageView iv) {
		// System.out.println("SETTING Wind Direction: windSpeed= "+windSpeed+"  degree= "+degree);
		Drawable weatherImage = null;
		if ((windDir == null || windDir.equals("")) || degree < 0) {
			return;
		}
		// System.out.println("SETTING Wind Direction: windSpeed= "+windSpeed+"  degree= "+degree);
		if (windSpeed < 15) {
			weatherImage = contex.getResources().getDrawable(R.drawable.arrow_down_1x);
		} else if (windSpeed >= 15 && windSpeed < 25) {
			weatherImage = contex.getResources().getDrawable(R.drawable.arrow_down_2x);
		} else if (windSpeed >= 25){
			weatherImage = contex.getResources().getDrawable(R.drawable.arrow_down_3x);
		}

		iv.setImageDrawable(weatherImage);
		Log.i("degree", "Wind degree: " + degree);
		ObjectAnimator.ofFloat(iv, ANIM_ROTATION, 0, degree).setDuration(2000).start();
	}

	public static float getPxWithRespectToDip(Context context, float dip) {
		return context.getResources().getDisplayMetrics().density * dip;
	}

	public static void calculateOutdoorAQIValues() {
		int goodAirCount = 0;
		int totalAirCount = 0;
		OutdoorAQIEventDto outdoorAQIEventDto = HomeFragment.getOutdoorAQIEventDto();
		if (outdoorAQIEventDto != null){
			if (OUTDOOR_AQI_PERCENTAGE_LIST.size() > 0 ) {
				OUTDOOR_AQI_PERCENTAGE_LIST.clear();
			}
			int idx[] = outdoorAQIEventDto.getIdx();
			float[] lastDayAQIReadings = new float[24];
			float[] last7dayAQIReadings = new float[7];
			float[] last4weekAQIReadings = new float[28];
			
			if (idx.length == 0) {
				return;
			}
			
			/** last day days */
			/**
			 * Adding last 24 data into lastDayReadings array, from index 696 t0
			 * 719
			 */
			int lastDayHr = 24;
			goodAirCount = 0;
			totalAirCount = 0;
			for (int i = 0; i < lastDayAQIReadings.length; i++) {
				if (i == 0 && idx[i] == 0) {
					idx[i] = idx[i + 1];
					// lastDayHr = 25;
				}
				lastDayAQIReadings[i] = idx[lastDayHr - 1 - i];
				if (idx[lastDayHr - 1 - i] <= 50) {
					goodAirCount++;
				}
				totalAirCount++;
			}
			OUTDOOR_AQI_PERCENTAGE_LIST.add(getPercentage(goodAirCount, totalAirCount));

			/** last 7 days */
			/**
			 * Adding last 7 days data into last7dayReadings array, from index
			 * last to till 7 days
			 */
			Calendar calender = Calendar.getInstance();
			int hr = calender.get(Calendar.HOUR_OF_DAY);
			Log.i("outdoor", "Current Hour Avg condition: " + HomeFragment.getCurrentHour());
			if (HomeFragment.getCurrentHour() != -1) {
				hr = HomeFragment.getCurrentHour();
			}
			if (hr == 0) {
				hr = 24;
			}
			int last7dayHrs = 6 * 24 + hr;

			float sum = 0;
			float avg = 0;
			int j = 0;
			goodAirCount = 0;
			totalAirCount = 0;
			for (int i = 0; i < last7dayHrs; i++) {
				float x = idx[last7dayHrs - 1 - i];
				sum = sum + x;
				if (i == 23 || i == 47 || i == 71 || i == 95 
						|| i == 119 || i == 143) {
					avg = sum / (float) 24;
					last7dayAQIReadings[j] = avg;
					if (avg <= 50) {
						goodAirCount++;
					}
					totalAirCount++;

					j++;
					sum = 0;
					avg = 0;
				} else if (i == last7dayHrs - 1) {
					avg = sum / (float) hr;
					last7dayAQIReadings[j] = avg;
					if (avg <= 50) {
						goodAirCount++;
					}
					totalAirCount++;

					sum = 0;
					avg = 0;
				}
			}
			OUTDOOR_AQI_PERCENTAGE_LIST.add(Utils.getPercentage(goodAirCount,
					totalAirCount));

			/** last 4 weeks */
			/**
			 * TODO - Explain the logic in 3lines
			 */
			int last4WeekHrs = 3 * 7 * 24 + 6 * 24 + hr;

			int count = 1;
			sum = 0;
			avg = 0;
			j = 0;
			goodAirCount = 0;
			totalAirCount = 0;
			for (int i = 0; i < last4WeekHrs; i++) {

				float x = idx[last4WeekHrs - 1 - i];
				sum = sum + x;
				if (count == 24 && j < 21) {
					avg = sum / (float) 24;
					last4weekAQIReadings[j] = avg;
					if (avg <= 50) {
						goodAirCount++;
					}
					totalAirCount++;
					j++;
					sum = 0;
					avg = 0;
					count = 0;
				} else if (j >= 21) {
					for (int m = 0; m < last7dayAQIReadings.length; m++) {
						last4weekAQIReadings[j] = last7dayAQIReadings[m];
						if (last7dayAQIReadings[m] <= 50) {
							goodAirCount++;
						}
						totalAirCount++;
						j++;
					}
					break;
				}
				count++;
			}
			OUTDOOR_AQI_PERCENTAGE_LIST.add(Utils.getPercentage(goodAirCount,
					totalAirCount));
		}
		
		Log.i("percent", "outdoorAQIPercentageList==" +OUTDOOR_AQI_PERCENTAGE_LIST);
	}

	public static String splitToHr(String timeStr) {
		char[] strArr = timeStr.toCharArray();
		String newTime = "";
		for (int i = 0; i < strArr.length; i++) {
			newTime = String.valueOf(strArr[strArr.length - 1 - i] + newTime);
			if (i == 1) {
				newTime = ":" + newTime;
			}
		}
		return newTime;
	}

	public static long getDiffInDays(long pairedOn) {
		java.util.Date currentDate = new java.util.Date();
		long currenttimeInMillis = currentDate.getTime();

		// Difference between current and previous timestamp
		long diff = currenttimeInMillis - pairedOn;
		long diffInDays = diff / (1000 * 60 * 60 * 24);
		
		return diffInDays ;
	}
	
	public static void extractMacAddress(String usn){
		String[] usnArray=usn.split("::");
		if(usnArray!=null && usnArray.length>0){
			String mac=usnArray[0];
			mac=mac.substring(mac.length()-12);
			SessionDto session= SessionDto.getInstance();
			session.setPurifierMacAddress(mac);
			ALog.d(ALog.DIAGNOSTICS, "Mac Address: "+mac);
		}
	}
	
	
}
