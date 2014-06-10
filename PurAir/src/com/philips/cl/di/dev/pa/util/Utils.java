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
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nineoldandroids.animation.ObjectAnimator;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AnimatorConstants;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.constant.ParserConstants;
import com.philips.cl.di.dev.pa.datamodel.IndoorHistoryDto;
import com.philips.cl.di.dev.pa.datamodel.IndoorTrendDto;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.ews.EWSConstant;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.scheduler.SchedulerUtil;
import com.philips.cl.di.dev.pa.security.Util;


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
	public static final String BOOT_STRAP_ID_4 = "0002" ;

	public static String getPortUrl(Port port, String ipAddress) {
		if (port == null) {
			return String.format(AppConstants.URL_BASEALLPORTS, ipAddress,"1", "invalidport") ;
		}
		return String.format(AppConstants.URL_BASEALLPORTS, ipAddress, port.port,port.urlPart) ;
	}
	
	public static String getScheduleDetailsUrl(String ipAddress, int scheduleNumber) {
		return String.format(AppConstants.URL_GET_SCHEDULES, ipAddress,scheduleNumber) ;
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
				hrlyAqiValues.add(aqi/10);
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
				dailyAqiValues.add(aqi/10);
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

		String eui64 = (purifier == null ? "" : purifier.getEui64());
		String qry = String.format(AppConstants.CLIENT_ID_RDCP, eui64) + qryPart2 + qryPart3;

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
					R.drawable.aqi_light_pink_circle_2x);
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
			int indoorAQI) {
		Log.i(TAG, "aqi=  " + indoorAQI);
		if(indoorAQI <= 14) {
			return ctx.getResources()
					.getDrawable(R.drawable.aqi_blue_circle_2x);
		} else if (indoorAQI > 14 && indoorAQI <= 23) {
			return ctx.getResources().getDrawable(
					R.drawable.aqi_purple_circle_2x);
		} else if (indoorAQI > 23 && indoorAQI <= 35) {
			return ctx.getResources().getDrawable(
					R.drawable.aqi_fusia_circle_2x);
		} else if (indoorAQI > 35) {
			return ctx.getResources().getDrawable(R.drawable.aqi_red_circle_2x);
		}
		return null;
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
		if ((filterStatusValue/8) < 12) {
			return getPreFilterFormattedText(12-filterStatusValue/8);
		} else if ((filterStatusValue/8) >= 12 && (filterStatusValue/8)/8 < 14) {
			return getPreFilterFormattedText(14-filterStatusValue/8);
		} else {
			return getPreFilterFormattedText(0);
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
		if ((filterStatusValue/8) < 98) {
			return PurAirApplication.getAppContext().getString(R.string.change_soon, 98-(filterStatusValue/8));
		} else if ((filterStatusValue/8) >= 98 && (filterStatusValue/8) < 105) {
			return PurAirApplication.getAppContext().getString(R.string.change_soon, 105-(filterStatusValue/8));
		} else if ((filterStatusValue/8) >= 105 && (filterStatusValue/8) < 120) {
			return PurAirApplication.getAppContext().getString(R.string.change_now);
		} else {
			return PurAirApplication.getAppContext().getString(R.string.filter_lock);
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
		if ((filterStatusValue/8) < 338) {
			return PurAirApplication.getAppContext().getString(R.string.change_soon, 338-(filterStatusValue/8));
		} else if ((filterStatusValue/8) >= 338 && (filterStatusValue/8) < 345) {
			return PurAirApplication.getAppContext().getString(R.string.change_soon, 345-(filterStatusValue/8));
		} else if ((filterStatusValue/8) >= 345 && (filterStatusValue/8) < 360) {
			return PurAirApplication.getAppContext().getString(R.string.change_now);
		} else {
			return PurAirApplication.getAppContext().getString(R.string.filter_lock);
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
		if ((filterStatusValue/8) < 338) {
			return PurAirApplication.getAppContext().getString(R.string.change_soon, 338-(filterStatusValue/8));
		} else if ((filterStatusValue/8) >= 338 && (filterStatusValue/8) < 345) {
			return PurAirApplication.getAppContext().getString(R.string.change_soon, 345-(filterStatusValue/8));
		} else if ((filterStatusValue/8) >= 345 && (filterStatusValue/8) < 360) {
			return PurAirApplication.getAppContext().getString(R.string.change_now);
		} else {
			return PurAirApplication.getAppContext().getString(R.string.filter_lock);
		}
	}

	//TODO : Remove Unused
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

			if (isDayTime == null) {
				return contex.getResources().getDrawable(R.drawable.partly_cloudy_night);
			}

			if (isDayTime.compareToIgnoreCase("Yes") == 0)
				weatherImage = contex.getResources().getDrawable(
						R.drawable.partly_cloudy);
			else
				weatherImage = contex.getResources().getDrawable(
						R.drawable.partly_cloudy_night);
			// weatherImage =
			// contex.getResources().getDrawable(R.drawable.partly_cloudy_night);
		} else if (weatherDesc.compareToIgnoreCase(CLEAR_SKIES) == 0) {
			if (isDayTime == null) {
				return contex.getResources().getDrawable(R.drawable.clear_sky_night);
			}
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
			if (isDayTime == null) {
				return contex.getResources().getDrawable(R.drawable.clear_sky_night);
			}
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
		Drawable weatherImage = null;
		if (windDir == null || windDir.equals("") || degree < 0) {
			return;
		}
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
		Date currentDate = new Date();
		long currenttimeInMillis = currentDate.getTime();

		// Difference between current and previous timestamp
		long diff = currenttimeInMillis - pairedOn;
		long diffInDays = diff / (1000 * 60 * 60 * 24);

		return diffInDays ;
	}

	public static String getMacAddressFromUsn(String usn){
		if(usn==null || usn.isEmpty())return null;
		String[] usnArray=usn.split("::");
		if(usnArray==null || usnArray.length <= 0) return null;

		String mac = usnArray[0];
		return mac.substring(mac.length()-12);
	}

	public static int getLastDayHours(String currentCityTimeHr) {
		int hr = 0;
		if (currentCityTimeHr == null || currentCityTimeHr.isEmpty()) {
			return hr;
		}
		try {
			hr = Integer.parseInt(currentCityTimeHr);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return hr;
		}

		if (hr == 0) {
			hr = 24;
		}
		return hr;
	}

	public static String[] getAQIStatusAndSummary(int indoorAQI) {
		String [] aqiStatusArray = new String[2] ;
		if(indoorAQI > -1 && indoorAQI <= 14) {
			aqiStatusArray[0] = PurAirApplication.getAppContext().getString(R.string.good) ;
			aqiStatusArray[1] = PurAirApplication.getAppContext().getString(R.string.indoor_aqi_good_tip1) ;
		} else if(indoorAQI > 14 && indoorAQI <= 23) {
			aqiStatusArray[0] = PurAirApplication.getAppContext().getString(R.string.moderate) ;
			aqiStatusArray[1] = PurAirApplication.getAppContext().getString(R.string.indoor_aqi_moderate_tip1) ;
		} else if(indoorAQI > 23 && indoorAQI <= 35) {
			aqiStatusArray[0] = PurAirApplication.getAppContext().getString(R.string.unhealthy) ;
			aqiStatusArray[1] = PurAirApplication.getAppContext().getString(R.string.indoor_aqi_unhealthy_tip1) ;
		} else if(indoorAQI > 35) {
			aqiStatusArray[0] = PurAirApplication.getAppContext().getString(R.string.very_unhealthy_split) ;
			aqiStatusArray[1] = PurAirApplication.getAppContext().getString(R.string.indoor_aqi_very_unhealthy_tip1) ;
		}
		return aqiStatusArray;
	}

	public static String getPreFilterFormattedText(int days){
		if(days==0)
			return PurAirApplication.getAppContext().getString(R.string.clean_now);
		else
			return PurAirApplication.getAppContext().getString(R.string.clean_soon, days);	
	}

	public static String getFilterFormattedText(int days){
		return PurAirApplication.getAppContext().getString(R.string.change_soon, days);		
	}
	
	public static boolean isGooglePlayServiceAvailable()
	{
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(PurAirApplication.getAppContext());
		if(resultCode == ConnectionResult.SUCCESS)
		{
			return true;
		}
		return false;
	}
	
	public static String getBootStrapID() {
		String bootStrap = new String(Util.decodeFromBase64(Util.getEncodedBootStrapID())) ;
		return bootStrap ;
	}
	
	public static String getEncodedBootStrapKey() {
		String encodedBootStrapKey = AppConstants.EMPTY_STRING ;
		StringBuilder bootStrapBuilder = new StringBuilder(SchedulerUtil.BOOTSTRAP_KEY_1);
		bootStrapBuilder.append(AnimatorConstants.BOOT_STRAP_KEY_2);
		bootStrapBuilder.append(ParserConstants.BOOT_STRAP_KEY_3) ;
		bootStrapBuilder.append(Fonts.BOOT_STRAP_KEY_4) ;
		bootStrapBuilder.append(EWSConstant.BOOT_STRAP_KEY_5) ;
		try {
			encodedBootStrapKey = Util.encodeToBase64(bootStrapBuilder.toString().getBytes()) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encodedBootStrapKey;
	}
}
