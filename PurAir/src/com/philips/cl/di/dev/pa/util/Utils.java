package com.philips.cl.di.dev.pa.util;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.conn.util.InetAddressUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.constant.ParserConstants;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.datamodel.IndoorHistoryDto;
import com.philips.cl.di.dev.pa.datamodel.IndoorTrendDto;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.demo.DemoModeConstant;
import com.philips.cl.di.dev.pa.ews.EWSConstant;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants;
import com.philips.cl.di.dev.pa.scheduler.SchedulerUtil;
import com.philips.cl.di.dev.pa.security.Util;
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
	private static int currentTimeHourOfDay;
	private static int currentTimeDayOfWeek;
	public static final String BOOT_STRAP_ID_4 = "AwMg==" ;
	public static final String CMA_BASEURL_3 = "XRoZXIuY29tL" ;

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
		long dayInMillisecs = 1000 * 60 * 60 * 24 ; // 1 Day
		int noOfDays = -2;
		if (date == null || date.isEmpty()) return noOfDays;

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		long timeDiff = 0;
		try {
			Date lastDate = sf.parse(date);
			Date prevDate = sf.parse(date0);
			timeDiff = lastDate.getTime() - prevDate.getTime();
			noOfDays = (int) (timeDiff / dayInMillisecs);
			//			ALog.i(ALog.INDOOR_RDCP, 
			//					"Download data date: " + date + " - 28 day ago date: " + date0 +" = " + noOfDays);
		} catch (ParseException e) {
			ALog.i(ALog.INDOOR_RDCP, "Date ParseException " + "Error: " + e.getMessage());
			return noOfDays;
		}
		return noOfDays;
	}

	public static int getDifferenceBetweenHrFromCurrentHr(String date, String date0) {
		long hourInMilliSecs = 1000 * 60 * 60 ; // 1Hour
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

			noOfHrs = (int) (timeDiff / hourInMilliSecs);
			//			ALog.i(ALog.INDOOR_RDCP, 
			//					"Download data date: " + date + " - 24 hr ago date: " + date0 +" = " + noOfHrs);

		} catch (ParseException e) {
			ALog.i(ALog.INDOOR_RDCP, "Date ParseException " + "Error: " + e.getMessage());
			return -2;
		}

		return noOfHrs;
	}

	public static String getDate(long timeMillis) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		String date = dateFormat.format(new Date(timeMillis));
		return date;
	}

	@SuppressLint("UseSparseArrays")
	public static void getIndoorAqiValues(String downloadedData, String eui64) {
		ALog.i(ALog.INDOOR_RDCP, "Rdcp values downloaded successfully");

		HashMap<Integer, Float> hrlyAqiValueMap = new HashMap<Integer, Float>();
		HashMap<Integer, Integer> hrlyAqiValueCounterMap = new HashMap<Integer, Integer>();

		HashMap<Integer, Float> dailyAqiValueMap = new HashMap<Integer, Float>();
		HashMap<Integer, Integer> dailyAqiValueCounterMap = new HashMap<Integer, Integer>();

		List<Integer> goodAirQualityList = new ArrayList<Integer>();
		int totalAqi = 0;
		int goodAqi = 0;
		int monthlyTotalAqi = 0;
		int monthlyGoodAqi = 0;
		int weeklyTotalAqi = 0;
		int weeklyGoodAqi = 0;

		if (downloadedData != null) {
			List<IndoorHistoryDto> indoorAQIHistory = DataParser.parseHistoryData(downloadedData);

			if (indoorAQIHistory != null && indoorAQIHistory.size() > 0) {

				for (int index = 0; index < indoorAQIHistory.size(); index++) {
					String date = indoorAQIHistory.get(index).getTimeStamp();
					//					ALog.i(ALog.INDOOR_RDCP, "Date: " + date +",  " + indoorAQIHistory.get(index).getAqi());
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

						totalAqi = totalAqi + 1;
						if (indoorAQIHistory.get(index).getAqi() <= 14) {
							goodAqi = goodAqi + 1; 
						}
						//						ALog.i(ALog.INDOOR_RDCP, "Hourly AQI: "+ indoorAQIHistory.get(index).getAqi() + "; goodAqi: " + goodAqi +"; Total: "+ totalAqi);
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

						if (numberOfDays > 20) {
							weeklyTotalAqi = weeklyTotalAqi + 1;
							if (indoorAQIHistory.get(index).getAqi() <= 14) {
								weeklyGoodAqi = weeklyGoodAqi + 1; 
							}
							//							ALog.i(ALog.INDOOR_RDCP, "Week AQI: "+ indoorAQIHistory.get(index).getAqi() + "; weeklyGoodAqi: " + weeklyGoodAqi +"; Total: "+ weeklyTotalAqi);
						}

						monthlyTotalAqi = monthlyTotalAqi + 1;
						if (indoorAQIHistory.get(index).getAqi() <= 14) {
							monthlyGoodAqi = monthlyGoodAqi + 1; 
						}
						//						ALog.i(ALog.INDOOR_RDCP, "4Week AQI: "+ indoorAQIHistory.get(index).getAqi() + "; monthlyGoodAqi: " + monthlyGoodAqi +"; Total: "+ monthlyTotalAqi);
					}

				}
				goodAirQualityList.add(getPercentage(goodAqi, totalAqi));
				goodAirQualityList.add(getPercentage(weeklyGoodAqi, weeklyTotalAqi));
				goodAirQualityList.add(getPercentage(monthlyGoodAqi, monthlyTotalAqi));

				setIndoorAqiHistory(hrlyAqiValueMap,hrlyAqiValueCounterMap, 
						dailyAqiValueMap, dailyAqiValueCounterMap, goodAirQualityList, eui64);

			}
		}
	}

	private static void setIndoorAqiHistory(HashMap<Integer, Float> hrlyAqiValueMap,
			HashMap<Integer, Integer> hrlyAqiValueCounterMap, 
			HashMap<Integer, Float> dailyAqiValueMap, 
			HashMap<Integer, Integer> dailyAqiValueCounterMap,
			List<Integer> goodAirQualityList, String eui64) {

		IndoorTrendDto indoorTrend = new IndoorTrendDto();
		List<Float> hrlyAqiValues = getIndoorAqiHistoryLastDay(hrlyAqiValueMap, hrlyAqiValueCounterMap);

		//		ALog.i(ALog.INDOOR_RDCP, "Rdcp hrlyAqiValueMap: " + hrlyAqiValueMap);
		//		ALog.i(ALog.INDOOR_RDCP, "Rdcp hrlyAqiValues counter: " + hrlyAqiValueCounterMap);
		//		ALog.i(ALog.INDOOR_RDCP, "Rdcp hrlyAqiValues: " + hrlyAqiValues);

		indoorTrend.setHourlyList(hrlyAqiValues);

		hrlyAqiValueMap = null;
		hrlyAqiValueCounterMap = null;
		hrlyAqiValues = null;

		List<Float> dailyAqiValues = getIndoorAqiHistoryLastMonth(dailyAqiValueMap, dailyAqiValueCounterMap);

		//		ALog.i(ALog.INDOOR_RDCP, "Rdcp dailyAqiValueMap: " + dailyAqiValueMap);
		//		ALog.i(ALog.INDOOR_RDCP, "Rdcp dailyAqiValues counter: " + dailyAqiValueCounterMap);
		//		ALog.i(ALog.INDOOR_RDCP, "Rdcp dailyAqiValues: " + dailyAqiValues);
		//		ALog.i(ALog.INDOOR_RDCP, "Rdcp goodAirQualityList: " + goodAirQualityList);
		//		ALog.i(ALog.INDOOR_RDCP, "Rdcp downloaded time: " + Calendar.getInstance().get(Calendar.DATE));

		indoorTrend.setDailyList(dailyAqiValues);
		indoorTrend.setGoodAirQualityList(goodAirQualityList);
		indoorTrend.setTimeMin(Calendar.getInstance().getTimeInMillis() / 60000);

		dailyAqiValueMap = null;
		dailyAqiValueCounterMap = null;
		dailyAqiValues = null;
		goodAirQualityList = null;

		SessionDto.getInstance().setIndoorTrendDto(eui64,indoorTrend);
	}

	private static List<Float> getIndoorAqiHistoryLastDay(HashMap<Integer, Float> hrlyAqiValueMap, 
			HashMap<Integer, Integer> hrlyAqiValueCounterMap) {
		List<Float> hrlyAqiValues = new ArrayList<Float>();
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

		return hrlyAqiValues;
	}

	private static List<Float> getIndoorAqiHistoryLastMonth(HashMap<Integer, Float> dailyAqiValueMap, 
			HashMap<Integer, Integer> dailyAqiValueCounterMap) {
		List<Float> dailyAqiValues = new ArrayList<Float>();
		for (int i = 0; i < 28; i++) {
			if (dailyAqiValueMap.containsKey(i) 
					&& dailyAqiValueCounterMap.containsKey(i)) {
				float aqi = dailyAqiValueMap.get(i);
				int noOffValue = dailyAqiValueCounterMap.get(i);
				aqi = aqi / noOffValue;
				dailyAqiValues.add(aqi/10);
			} else {
				dailyAqiValues.add(-1F);
			}
		}
		return dailyAqiValues;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getCPPQuery(AirPurifier purifier) {
		long dayInMilliSecs = 24 * 60 * 60 * 1000L ;
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatTime = new SimpleDateFormat(":mm:ss");

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		int hrOffDayInt = cal.get(Calendar.HOUR_OF_DAY);
		currentTimeHourOfDay = hrOffDayInt;
		currentTimeDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		Date currentDateChinese = getCurrentChineseDate();
		String hrOffDay = get2DigitHr(hrOffDayInt);
		String endDate = formatDate.format(currentDateChinese);
		String endTime = hrOffDay + formatTime.format(currentDateChinese);

		String qryPart3 = "endDate=" + endDate + "T" + endTime + ".1508314Z";

		long startDateDiff = currentDateChinese.getTime() - (dayInMilliSecs * 28);
		Date dateStart = new Date(startDateDiff);
		String startDate = formatDate.format(dateStart);
		String startTime = hrOffDay + formatTime.format(dateStart);

		String qryPart2 = "startDate=" + startDate + "T" + startTime + ".1508314Z;";

		String eui64 = (purifier == null ? "" : purifier.getNetworkNode().getCppId());
		String qry = String.format(AppConstants.CLIENT_ID_RDCP, eui64) + qryPart2 + qryPart3;

		ALog.i(ALog.INDOOR_RDCP, "rdcp qry:   "+qry);
		long hourInMilliSecs = 24 * 60 * 60 * 1000; // 1 Hour
		long endDateDiff = currentDateChinese.getTime() - hourInMilliSecs;
		Date dateEnd = new Date(endDateDiff);
		hrOffDayInt = hrOffDayInt + 1; 
		if (hrOffDayInt >= 24) {
			hrOffDayInt = 0;
		}
		ago24HrDate = formatDate.format(dateEnd) + " " + get2DigitHr(hrOffDayInt);

		long startDateDiff1 = currentDateChinese.getTime() - (dayInMilliSecs * 27);
		Date dateStart1 = new Date(startDateDiff1);
		ago27DayDate = formatDate.format(dateStart1);

		return qry;
	}

	public static int getCurrentTimeHourOfDay() {
		return currentTimeHourOfDay;
	}

	public static int getCurrentTimeDayOfWeek() {
		return currentTimeDayOfWeek;
	}

	public static String get2DigitHr( int hr) {
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
	public static Drawable getIndoorAQICircleBackground(Context ctx, int indoorAQI) {
		Log.i(TAG, "aqi=  " + indoorAQI);
		if(indoorAQI <= 14) {
			return ctx.getResources().getDrawable(R.drawable.aqi_blue_circle_2x);
		} else if (indoorAQI > 14 && indoorAQI <= 23) {
			return ctx.getResources().getDrawable(R.drawable.aqi_purple_circle_2x);
		} else if (indoorAQI > 23 && indoorAQI <= 35) {
			return ctx.getResources().getDrawable(R.drawable.aqi_fusia_circle_2x);
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
		if (filterStatusValue < 96) {
			return getPreFilterFormattedText((AppConstants.PRE_FILTER_MAX_VALUE-filterStatusValue));
		} else if (filterStatusValue >= 96 && filterStatusValue < 112) {
			return getPreFilterFormattedText((AppConstants.PRE_FILTER_MAX_VALUE-filterStatusValue));
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
		if (filterStatusValue < 784) {
			return PurAirApplication.getAppContext().getString(R.string.change_soon, (AppConstants.MULTI_CARE_FILTER_MAX_VALUE-filterStatusValue)/AppConstants.RUNNING_HRS);
		} else if (filterStatusValue >= 784 && filterStatusValue < 840) {
			return PurAirApplication.getAppContext().getString(R.string.change_soon, (AppConstants.MULTI_CARE_FILTER_MAX_VALUE-filterStatusValue)/AppConstants.RUNNING_HRS);
		} else if (filterStatusValue >= 840 && filterStatusValue < 960) {
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
		if (filterStatusValue < 2704) {
			return PurAirApplication.getAppContext().getString(R.string.change_soon, (AppConstants.ACTIVE_CARBON_FILTER_MAX_VALUE-filterStatusValue)/AppConstants.RUNNING_HRS);
		} else if (filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return PurAirApplication.getAppContext().getString(R.string.change_soon, (AppConstants.ACTIVE_CARBON_FILTER_MAX_VALUE-filterStatusValue)/AppConstants.RUNNING_HRS);
		} else if (filterStatusValue >= 2760 && filterStatusValue < 2880) {
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
		if (filterStatusValue < 2704) {
			return PurAirApplication.getAppContext().getString(R.string.change_soon, (AppConstants.HEPA_FILTER_MAX_VALUE-filterStatusValue)/AppConstants.RUNNING_HRS);
		} else if (filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return PurAirApplication.getAppContext().getString(R.string.change_soon, (AppConstants.HEPA_FILTER_MAX_VALUE-filterStatusValue)/AppConstants.RUNNING_HRS);
		} else if (filterStatusValue >= 2760 && filterStatusValue < 2880) {
			return PurAirApplication.getAppContext().getString(R.string.change_now);
		} else {
			return PurAirApplication.getAppContext().getString(R.string.filter_lock);
		}
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

	public static int getPercentage(int goodAir, int totalAir) {
		int percent = 0;
		if (totalAir > 0) {
			percent = (goodAir * 100) / totalAir;
		}
		return percent;
	}

	private enum Days {
		SUNDAY(1, R.string.sun),
		MONDAY(2, R.string.mon),
		TUESDAY(3, R.string.tue),
		WEDNESDAY(4, R.string.wed),
		THURSDAY(5, R.string.thu),
		FRIDAY(6, R.string.fri),
		SATURDAY(7, R.string.sat);

		private int resId;
		private int dayInt;

		private Days(int dayInt, int resId) {
			this.dayInt = dayInt;
			this.resId = resId;
		}

		public static int valueOf(int dayInt) {
			for (Days days : Days.values()) {
				if (days.dayInt == dayInt) {
					return days.resId;
				}
			}
			return R.string.empty_string;
		}
	}

	public static String getDayOfWeek(Context context, int dayInt) {
		return context.getString(Days.valueOf(dayInt));
	}

	public static void setOutdoorWeatherDirImg(Context contex, float windSpeed, float degree, ImageView iv) {
		Drawable weatherImage = null;
		if (windSpeed < 15) {
			weatherImage = contex.getResources().getDrawable(R.drawable.arrow_down_1x);
		} else if (windSpeed >= 15 && windSpeed < 25) {
			weatherImage = contex.getResources().getDrawable(R.drawable.arrow_down_2x);
		} else if (windSpeed >= 25){
			weatherImage = contex.getResources().getDrawable(R.drawable.arrow_down_3x);
		}

		iv.setImageDrawable(weatherImage);
		rotateImageView(iv, 0, degree);
		Log.i("degree", "Wind degree: " + degree);
	}

	public static void rotateImageView(ImageView imageView, float prevRotation, float newRotation) {
		Drawable drawable = imageView.getDrawable();
		ALog.i(ALog.OUTDOOR_DETAILS, "Utils$setRotationAnimation rotation " + newRotation + " aqiPointer.getWidth()/2 " + (imageView.getWidth()/2) + " drawable " + drawable.getMinimumHeight());

		Animation rotateAnim = new RotateAnimation(prevRotation, newRotation, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

		rotateAnim.setDuration(2000);
		rotateAnim.setRepeatCount(0);
		rotateAnim.setFillAfter(true);

		imageView.setAnimation(rotateAnim);
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
			aqiStatusArray[0] = PurAirApplication.getAppContext().getString(R.string.good_indoor) ;
			aqiStatusArray[1] = PurAirApplication.getAppContext().getString(R.string.indoor_aqi_good_tip1) ;
		} else if(indoorAQI > 14 && indoorAQI <= 23) {
			aqiStatusArray[0] = PurAirApplication.getAppContext().getString(R.string.moderate_indoor) ;
			aqiStatusArray[1] = PurAirApplication.getAppContext().getString(R.string.indoor_aqi_moderate_tip1) ;
		} else if(indoorAQI > 23 && indoorAQI <= 35) {
			aqiStatusArray[0] = PurAirApplication.getAppContext().getString(R.string.unhealthy_indoor) ;
			aqiStatusArray[1] = PurAirApplication.getAppContext().getString(R.string.indoor_aqi_unhealthy_tip1) ;
		} else if(indoorAQI > 35) {
			aqiStatusArray[0] = PurAirApplication.getAppContext().getString(R.string.very_unhealthy_split_indoor) ;
			aqiStatusArray[1] = PurAirApplication.getAppContext().getString(R.string.indoor_aqi_very_unhealthy_tip1) ;
		}
		return aqiStatusArray;
	}

	public static int getBackgroundResource(int indoorAQI) {
		int resId = R.drawable.home_indoor_bg_2x;
		if(indoorAQI > -1 && indoorAQI <= 14) {
			resId = R.drawable.indoor_very_healthy_2x;
		} else if(indoorAQI > 14 && indoorAQI <= 23) {
			resId = R.drawable.indoor_moduralty_polluted_2x;
		} else if(indoorAQI > 23 && indoorAQI <= 35) {
			resId = R.drawable.indoor_unhelathy_2x;
		} else if(indoorAQI > 35) {
			resId = R.drawable.indoor_harzadous_2x;
		}
		return resId;
	}

	public static String getPreFilterFormattedText(int hours){
		if(hours==0)
			return PurAirApplication.getAppContext().getString(R.string.clean_now);
		else if(hours<8 && hours>0)
			return PurAirApplication.getAppContext().getString(R.string.clean_today);
		else
			return PurAirApplication.getAppContext().getString(R.string.clean_soon, (int)hours/AppConstants.RUNNING_HRS);	
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

	public static String getBootStrapKey() {
		String bootStrapKey = AppConstants.EMPTY_STRING ;
		StringBuilder bootStrapBuilder = new StringBuilder(SchedulerUtil.BOOTSTRAP_KEY_1);
		bootStrapBuilder.append(TrackPageConstants.BOOT_STRAP_KEY_2);
		bootStrapBuilder.append(ParserConstants.BOOT_STRAP_KEY_3) ;
		bootStrapBuilder.append(Fonts.BOOT_STRAP_KEY_4) ;
		bootStrapBuilder.append(EWSConstant.BOOT_STRAP_KEY_5) ;
		try {
			bootStrapKey = new String(Util.decodeFromBase64(bootStrapBuilder.toString()), Charset.defaultCharset()) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bootStrapKey;
	}

	public static String getCMA_AppID() {
		String cmaAppId = AppConstants.EMPTY_STRING ;
		StringBuilder cmaAppIdBuilder = new StringBuilder(AppConstants.CMA_APP_ID_1);
		cmaAppIdBuilder.append(SchedulerConstants.CMA_APP_ID_2);
		cmaAppIdBuilder.append(DemoModeConstant.CMA_APP_ID_3) ;
		cmaAppIdBuilder.append(TrackPageConstants.CMA_APP_ID_4) ;
		try {
			cmaAppId = new String(Util.decodeFromBase64(cmaAppIdBuilder.toString()), Charset.defaultCharset()) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cmaAppId;
	}

	public static String getCMA_PrivateKey() {
		String cmaPrivateKey = AppConstants.EMPTY_STRING ;
		StringBuilder cmaPrivateKeyBuilder = new StringBuilder(EWSConstant.CMA_PRIVATE_KEY_1);
		cmaPrivateKeyBuilder.append(TrackPageConstants.CMA_PRIVATE_KEY_2);
		cmaPrivateKeyBuilder.append(DemoModeConstant.CMA_PRIVATE_KEY_3) ;
		cmaPrivateKeyBuilder.append(GraphConst.CMA_PRIVATE_KEY_4) ;
		try {
			cmaPrivateKey = new String(Util.decodeFromBase64(cmaPrivateKeyBuilder.toString()), Charset.defaultCharset()) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		cmaPrivateKey = "feilipu_test_twoweek";
		return cmaPrivateKey;
	}

	public static String getCMA_BaseURL() {
		String cmaBaseURL = AppConstants.EMPTY_STRING ;
		StringBuilder cmaBaseURLBuilder = new StringBuilder(ParserConstants.CMA_BASEURL_1);
		cmaBaseURLBuilder.append(Coordinates.CMA_BASEURL_2);
		cmaBaseURLBuilder.append(CMA_BASEURL_3) ;
		cmaBaseURLBuilder.append(GraphConst.CMA_BASEURL_4) ;
		try {
			cmaBaseURL = new String(Util.decodeFromBase64(cmaBaseURLBuilder.toString()), Charset.defaultCharset()) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cmaBaseURL;
	}

	public static int getTimeDiffInMinite(IndoorTrendDto trendDto) {
		if( trendDto == null) return 0;
		long currTime = Calendar.getInstance().getTimeInMillis() / 60000;
		int diff = (int) (currTime - trendDto.getTimeMin());
		return diff;
	}

	public static void saveNoOfVisit(int count) {
		SharedPreferences preferences = 
				PurAirApplication.getAppContext().getSharedPreferences(AppConstants.NO_OF_VISIT_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(AppConstants.NO_OF_VISIT_PREF_KEY, ++count);
		editor.commit();
	}

	public static int getNoOfVisit() {
		SharedPreferences preferences = 
				PurAirApplication.getAppContext().getSharedPreferences(AppConstants.NO_OF_VISIT_PREF, Context.MODE_PRIVATE);
		return preferences.getInt(AppConstants.NO_OF_VISIT_PREF_KEY, 0);
	}

	public static void saveAppFirstUse(boolean firstUse) {
		SharedPreferences preferences = 
				PurAirApplication.getAppContext().getSharedPreferences(AppConstants.START_FLOW_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(AppConstants.START_FLOW_PREF_KEY, firstUse);
		editor.commit();
	}

	public static boolean getAppFirstUse() {
		SharedPreferences preferences = 
				PurAirApplication.getAppContext().getSharedPreferences(AppConstants.START_FLOW_PREF, Context.MODE_PRIVATE);
		return preferences.getBoolean(AppConstants.START_FLOW_PREF_KEY, true);
	}

	public static void saveFirmwareVersion(String purifierEui64, String firmwareVersion) {
		SharedPreferences preferences = 
				PurAirApplication.getAppContext().getSharedPreferences(AppConstants.FIRMWARE_VERSION, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(purifierEui64, firmwareVersion);
		editor.commit();
	}

	public static String getFirmwareVersion(String purifierEui64) {
		SharedPreferences preferences = 
				PurAirApplication.getAppContext().getSharedPreferences(AppConstants.FIRMWARE_VERSION, Context.MODE_PRIVATE);
		return preferences.getString(purifierEui64, "");
	}

	public static void saveUserGPSPermission(boolean allowGPS) {
		SharedPreferences preferences = 
				PurAirApplication.getAppContext().getSharedPreferences(AppConstants.ALLOW_GPS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("allow_gps", allowGPS);
		editor.commit();
	}

	public static boolean getUserGPSPermission( ) {
		SharedPreferences preferences = 
				PurAirApplication.getAppContext().getSharedPreferences(AppConstants.ALLOW_GPS, Context.MODE_PRIVATE);
		return preferences.getBoolean("allow_gps", true);
	}

	public static Toast getCustomToast(String message) {
		LayoutInflater inflater = (LayoutInflater)PurAirApplication.getAppContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View toastView = inflater.inflate(R.layout.toast, null);
		FontTextView msg = (FontTextView) toastView.findViewById(R.id.tv_toast_msg);
		msg.setText(message);

		Toast toast = new Toast(PurAirApplication.getAppContext());
		toast.setView(toastView);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		return toast;
	}
	public static String getStaticIpAddress() {
		return getIPAddress(true) ;
	}

	/**
	 * Get IP address from first non-localhost interface
	 * @param ipv4  true=return ipv4, false=return ipv6
	 * @return  address or empty string
	 */
	public static String getIPAddress(boolean useIPv4) {
		try {
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
				for (InetAddress addr : addrs) {
					if (!addr.isLoopbackAddress()) {
						String sAddr = addr.getHostAddress().toUpperCase();
						boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
						if (useIPv4) {
							if (isIPv4) 
								return sAddr;
						} else {
							if (!isIPv4) {
								int delim = sAddr.indexOf('%'); // drop ip6 port suffix
								return delim<0 ? sAddr : sAddr.substring(0, delim);
							}
						}
					}
				}
			}
		} catch (Exception ex) { } // for now eat exceptions
		return "";
	}

	public static boolean isVersionChanged() {
		final SharedPreferences prefs = CPPController.getInstance(PurAirApplication.getAppContext()).
				getGCMPreferences();

		int registeredVersion = prefs.getInt(AppConstants.PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = PurAirApplication.getAppVersion();
		boolean isGCMRegistrationExpired = (registeredVersion != currentVersion);

		if (isGCMRegistrationExpired) {
			ALog.d(ALog.NOTIFICATION, "Registration ID expired - App version changed");
			return true;
		}

		return false;
	}

	public static boolean isLocaleChanged() {
		final SharedPreferences prefs = CPPController.getInstance(PurAirApplication.getAppContext()).
				getGCMPreferences();
		String languageLocale = LanguageUtils.getLanguageForLocale(Locale.getDefault());

		String registeredLocale = prefs.getString(AppConstants.PROPERTY_APP_LOCALE,
				LanguageUtils.DEFAULT_LANGUAGE);
		boolean isLocalChanged = registeredLocale.equalsIgnoreCase(languageLocale);

		if (!isLocalChanged) {
			ALog.d(ALog.NOTIFICATION,
					"App Locale change happened");
			return true;
		}

		return false;
	}

	public static String getVersionNumber() {
		String versionCode = "";
		try {
			versionCode = PurAirApplication.getAppContext().getPackageManager().getPackageInfo(PurAirApplication.getAppContext().getPackageName(),
					0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	public static Date getCurrentChineseDate() {
		TimeZone timeZoneChina = TimeZone.getTimeZone("GMT+8");
		TimeZone timeZoneCurrent = Calendar.getInstance().getTimeZone();

		//Time difference between time zone and GMT
		int offsetChina = timeZoneChina.getOffset(Calendar.getInstance().getTimeInMillis());
		int offsetCurrent = timeZoneCurrent.getOffset(Calendar.getInstance().getTimeInMillis());
		int offset = offsetChina - offsetCurrent;

		Calendar cal = Calendar.getInstance();

		Date currentDate = new Date(cal.getTimeInMillis() + offset);
		return currentDate;
	}

	public static String getHistoricDataUpdateDate(String timeStamp) {
		if (timeStamp == null || timeStamp.isEmpty()) return null;
		StringBuilder builder = new StringBuilder();
		builder.append(timeStamp.substring(0, 4)).append("-");
		builder.append(timeStamp.substring(4, 6)).append("-");;
		builder.append(timeStamp.substring(6, 8));
		return builder.toString();
	}

	private static SharedPreferences getGPSPreferences() {
		return PurAirApplication.getAppContext().getSharedPreferences(
				AppConstants.GPS_PREFERENCE_FILE_NAME,
				Context.MODE_PRIVATE);
	}

	public static void setGPSDisabledDialogShownValue(boolean isShown){
		SharedPreferences preferences= getGPSPreferences();
		Editor editor= preferences.edit();
		editor.putBoolean(AppConstants.DIABLED_DIALOG_SHOWN, isShown);
		editor.commit();
	}

	public static boolean getGPSDisabledDialogShownValue(){
		SharedPreferences preferences= getGPSPreferences();
		return preferences.getBoolean(AppConstants.DIABLED_DIALOG_SHOWN, false);
	}

	public static void setGPSEnabledDialogShownValue(boolean isShown){
		SharedPreferences preferences= getGPSPreferences();
		Editor editor= preferences.edit();
		editor.putBoolean(AppConstants.ENABLED_DIALOG_SHOWN, isShown);
		editor.commit();
	}

	public static boolean getGPSEnabledDialogShownValue(){
		SharedPreferences preferences= getGPSPreferences();
		return preferences.getBoolean(AppConstants.ENABLED_DIALOG_SHOWN, false);
	}


	public static File getExternalStorageDirectory(String dirName) {
		File file = new File(Environment.getExternalStorageDirectory(), dirName);
		if (!file.exists() && !file.mkdirs()) {
			ALog.e(ALog.FILE_DOWNLOAD, "Error creating file!");
			file=null;
		}
		return file;
	}


	public static String getCountryLocale(){

		Locale loc=Locale.getDefault();
		if (loc == null) return "en";

		String language = loc.getLanguage();

		String localeString= Locale.getDefault().toString();
		String languageCode="en";

		if(language.equalsIgnoreCase("zh")){
			languageCode= AppConstants.SIMPLIFIED_CHINESE_LANGUAGE_CODE;
		}
		
		if(localeString.equalsIgnoreCase("zh_TW")){
			languageCode= AppConstants.TRADITIONAL_CHINESE_LANGUAGE_CODE;
		}

		languageCode= languageCode.toLowerCase();
		return languageCode;		
	}
	
	public static String getCountryCode() {
		String  countryCode = Locale.getDefault().getCountry();
		if (countryCode == null || countryCode.isEmpty()) {
			countryCode = "NL";
		}
		return countryCode;
	}
}
