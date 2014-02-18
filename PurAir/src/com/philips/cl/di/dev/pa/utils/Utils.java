package com.philips.cl.di.dev.pa.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.cppdatabase.CppDatabaseModel;
import com.philips.cl.di.dev.pa.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.dto.IndoorHistoryDto;
import com.philips.cl.di.dev.pa.dto.IndoorTrendDto;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.screens.TrendsActivity;
import com.philips.icpinterface.data.Errors;

// TODO: Auto-generated Javadoc
/**
 * The Class Utils.
 */
public class Utils {

	/** The Constant TAG. */
	private static final String TAG = "Utils";

	/**
	 * Populates the image names for the left menu.
	 * 
	 * @return the icon array
	 */

	public static ArrayList<String> getIconArray() {
		ArrayList<String> alImageNames = new ArrayList<String>();
		alImageNames.add(AppConstants.ICON_HOME);
		alImageNames.add(AppConstants.ICON_MYCITY);
		alImageNames.add(AppConstants.ICON_CLOUD);
		alImageNames.add(AppConstants.ICON_REG);
		alImageNames.add(AppConstants.ICON_HELP);
		alImageNames.add(AppConstants.ICON_SETTING);
		return alImageNames;

	}

	/**
	 * Populates the Labels for the left menu.
	 * 
	 * @return the label array
	 */

	public static ArrayList<String> getLabelArray() {
		ArrayList<String> alNames = new ArrayList<String>();
		alNames.add(AppConstants.LABEL_HOME);
		alNames.add(AppConstants.LABEL_MYCITY);
		alNames.add(AppConstants.LABEL_CLOUD);
		alNames.add(AppConstants.LABEL_REG);
		alNames.add(AppConstants.LABEL_HELP);
		alNames.add(AppConstants.LABEL_SETTING);
		return alNames;
	}

	/**
	 * Create a new typeface from the specified font data.
	 * 
	 * @param context
	 *            the context
	 * @return The new Typeface
	 */
	public static Typeface getTypeFace(Context context) {
		return Typeface.createFromAsset(context.getAssets(), AppConstants.FONT);

	}

	/**
	 * Returns the screen width of the device.
	 * 
	 * @param context
	 *            the context
	 * @return screen width
	 */
	@SuppressWarnings("deprecation")
	public static int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		return display.getWidth();
	}

	/**
	 * Gets the iP address of the air purifier.
	 * 
	 * @param context
	 *            the context
	 * @return the iP address
	 */
	public static String getIPAddress(Context context) {
		String ipAddress = context.getSharedPreferences("sharedPreferences", 0)
				.getString("ipAddress", AppConstants.defaultIPAddress);
		return ipAddress;
	}

	/**
	 * 
	 */

	public static void storeCPPKeys(Context context,CppDatabaseModel cppDataModel) {
		SharedPreferences settings = context.getSharedPreferences(
				"cpp_preferences01", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("airpurifierid", cppDataModel.getDistribution());
		editor.putString("euid", cppDataModel.getEuId());
		editor.putString("privatekey", cppDataModel.getSysKey() + cppDataModel.getPrivateKey());
		editor.putString("registrationid", cppDataModel.getRegId()) ;
		editor.commit();
	}

	public static String getPrivateKey(Context context) {
		String privateKey = context.getSharedPreferences("cpp_preferences01", 0)
				.getString("privatekey", "");
		return privateKey;
	}

	public static String getRegistrationID(Context context) {
		String registrationid = context.getSharedPreferences("cpp_preferences01", 0)
				.getString("registrationid", "");
		return registrationid;
	}

	public static String getEuid(Context context) {
		String euid = context.getSharedPreferences("cpp_preferences01", 0)
				.getString("euid", "");
		return euid;
	}

	public static String getAirPurifierID(Context context) {
		String airPurifierID = context.getSharedPreferences("cpp_preferences01", 0)
				.getString("airpurifierid", "");
		return airPurifierID;
	}

	public static void clearCPPDetails(Context context ) {
		context.getSharedPreferences("cpp_preferences01", 0).edit().clear().commit() ;
	}

	/**
	 * Sets the ip address.
	 * 
	 * @param ipAddress
	 *            the ip address
	 * @param context
	 *            the context
	 */
	public static void setIPAddress(String ipAddress, Context context) {

		SharedPreferences settings = context.getSharedPreferences(
				"sharedPreferences", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("ipAddress", ipAddress);
		editor.commit();
	}

	/**
	 * Show incorrect ip dialog.(This method will be removed after IFA)
	 * 
	 * @param context
	 *            the context
	 */
	public static void showIncorrectIpDialog(Context context) {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
				context);
		dialogBuilder.setTitle(AppConstants.MESSAGE_INCORRECT_IP);
		dialogBuilder.setPositiveButton(AppConstants.MESSAGE_OK, null);
		dialogBuilder.show();

	}


	/**
	 * Gets the time remaining.
	 * 
	 * @param filterValue
	 *            the filter value
	 * @return the time remaining
	 */
	public static String getTimeRemaining(int filterType,int filterValue) {
		String timeRemaining = "Needs to be implemented";

		return timeRemaining;
	}

	/**
	 * Checks if is json.
	 * 
	 * @param result
	 *            the result
	 * @return true, if is json
	 */
	public static boolean isJson(String result) {
		if (result == null)
			return false;
		if (result.length() < 2)
			return false;
		if (result.charAt(0) != '{' && result.charAt(0) != '[')
			return false;
		return true;
	}

	/**
	 * Gets the filter status color.
	 * 
	 * @param filterStatusValue
	 *            the filter status value
	 * @return the filter status color
	 */
	public static int getFilterStatusColor(float filterStatusValue) {
		float filterRange = (filterStatusValue / (AppConstants.MAXIMUMFILTER - AppConstants.MINIMUNFILTER)) * 100;
		if (filterRange >= 0 && filterRange <= 25) {
			return AppConstants.COLOR_BAD;
		} else if (filterRange > 25 && filterRange <= 50) {
			return AppConstants.COLOR_FAIR;
		} else if (filterRange > 50 && filterRange <= 75) {
			return AppConstants.COLOR_GOOD;
		} else if (filterRange > 75 && filterRange <= 100) {
			return AppConstants.COLOR_VGOOD;
		}
		return 0;
	}

	/**
	 * Gets the current date time.
	 * 
	 * @return the current date time
	 */
	public static String getCurrentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date now = new Date(System.currentTimeMillis());
		return sdf.format(now);
	}


	/**
	 * Gets the resource id.
	 * 
	 * @param drawableName
	 *            the drawable name
	 * @param context
	 *            the context
	 * @return the resource id
	 */
	public static int getResourceID(String drawableName, Context context) {
		return context.getResources().getIdentifier(drawableName, "drawable",
				context.getPackageName());
	}





	/**
	 * Gets the outdoor aqi date time.
	 * 
	 * @param datetime
	 *            the datetime
	 * @return the outdoor aqi date time
	 */
	public static String getOutdoorAQIDateTime(String datetime) {
		String dateToReturn = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		try {
			Date startDate = sdf.parse(datetime);

			sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

			dateToReturn = sdf.format(startDate);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateToReturn;
	}


	/**
	 * Copy database from assets to s dcard.
	 * 
	 * @param context
	 *            the context
	 */
	public static void copyDatabaseFromAssetsToSDcard(Context context) {
		if (!isFileExists(context)) {
			AssetManager assetManager = context.getAssets();
			try {
				String filenames[] = assetManager.list("");

				for (String filename : filenames) {
					if (filename.equalsIgnoreCase(AppConstants.DATABASE)) {
						Log.i("File name => ", filename);
						InputStream in = null;
						OutputStream out = null;
						try {
							in = assetManager.open(filename); // if files
							// resides
							// inside the
							// "Files"
							// directory
							// itself
							out = new FileOutputStream(context.getFilesDir()
									+ "/" + filename);
							copyFile(in, out);
							in.close();
							in = null;
							out.flush();
							out.close();
							out = null;
						} catch (Exception e) {
							Log.e("tag", e.getMessage());
							e.printStackTrace();
						}
					}
				}
			} catch (IOException e) {
				Log.e("tag", e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Checks if is file exists.
	 * 
	 * @return true, if is file exists
	 */
	private static boolean isFileExists(Context context) {
		File file = new File(context.getFilesDir(), AppConstants.DATABASE);
		return file.exists();
	}

	/**
	 * Copy file.
	 * 
	 * @param in
	 *            the in
	 * @param out
	 *            the out
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void copyFile(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	/**
	 * Gets the maximum aqi from the given array of aqi values.
	 * 
	 * @param arrayAQIValues
	 *            the array_ aqi
	 * @return the maximum aqi
	 */
	public static int getMaximumAQI(ArrayList<Integer> arrayAQIValues) {
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < arrayAQIValues.size(); i++) {
			if (arrayAQIValues.get(i) > max) {
				max = arrayAQIValues.get(i);
			}
		}

		return max;
	}

	/**
	 * Gets the minimum aqi from the given array of aqi values.
	 * 
	 * @param array_AQI
	 *            the array_ aqi
	 * @return the minimum aqi
	 */
	public static int getMinimumAQI(ArrayList<Integer> array_AQI) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < array_AQI.size(); i++) {
			if (array_AQI.get(i) < 0) {
				continue;
			}
			if (array_AQI.get(i) < min) {
				min = array_AQI.get(i);
			}
		}
		return min;
	}

	/**
	 * Gets the good air percentage in the given array of aqi values.
	 * 
	 * @param listAQIIndoor
	 *            the array_ aqi
	 * @return the good air percentage
	 */
	public static int getGoodAirPercentage(List<Integer> listAQIIndoor) {
		int percent = 0;
		int count = listAQIIndoor.size();
		int countGood_VGood = 0;
		for (int i = 0; i < listAQIIndoor.size(); i++) {
			// Discard the value if it is negative
			if (listAQIIndoor.get(i) < 0) {
				count--;
				continue;
			}

			if (listAQIIndoor.get(i) <= 250) {
				countGood_VGood++;
			}
		}
		if (count > 0)
			percent = (countGood_VGood * 100) / count;
		return percent;
	}

	/**
	 * Gets the average aqi.
	 * 
	 * @param listAQIIndoor
	 *            the array_ aqi
	 * @return the average aqi
	 */
	public static int getAverageAQI(List<Integer> listAQIIndoor) {
		int sum = 0;
		int count = listAQIIndoor.size();
		for (int i = 0; i < listAQIIndoor.size(); i++) {
			if (listAQIIndoor.get(i) < 0) {
				count--;
				continue;
			}
			sum = sum + listAQIIndoor.get(i);
		}
		if (count > 0)
			return (sum / count);
		else
			return 0;
	}

	public static int[] getArrayForAQIGraph(ArrayList<Integer> alAQIValues) {
		int[] array_aqi = new int[24];
		int i = 0;
		for (int iAQI : alAQIValues) {
			if (i > 23)
				break;

			array_aqi[i] = iAQI;
			i++;
		}
		return array_aqi;
	}


	public static String getToday() {
		Calendar now = Calendar.getInstance();
		int month = now.get(Calendar.MONTH);
		int date = now.get(Calendar.DATE);

		return getMonthForInt(month) + " " + date;
	}

	private static String getMonthForInt(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 11) {
			month = months[num];
		}
		return month;
	}


	public static String getCurrentDateForDB()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date now = new Date(System.currentTimeMillis());
		return sdf.format(now);

	}


	public static int getDifferenceBetweenDaysFromCurrentDay(String date, String date0) {	
		Log.i("DOWNLOAD", "date: " + date + " : prev date: " + date0);
		int noOfDays = 0 ;

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd") ; 
		Calendar cal = Calendar.getInstance() ;
		long timeDiff = 0;
		try {
			Date lastDate = sf.parse(date) ;
			if (date0 == null) {
				timeDiff = cal.getTimeInMillis() - lastDate.getTime()  ;
			} else {
				Date prevDate = sf.parse(date0) ;
				Log.i("Download", "datattata= " +sf.format(lastDate) +"    "+sf.format(prevDate));
				timeDiff = lastDate.getTime() - prevDate.getTime() ;
			}
			noOfDays = (int) (timeDiff / (1000* 60 * 60 * 24)) ;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return noOfDays ;
	}

	public static int getDifferenceBetweenHrFromCurrentHr(String date, String date0) {	
		int noOfHrs = 0 ;

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH") ; 
		long timeDiff = 0;
		try {
			Date d = sf.parse(date) ;
			Date d0 = sf.parse(date0) ;
			if (d.getTime() >= d0.getTime()) {
				timeDiff = d.getTime() - d0.getTime() ;
			}else {
				return -2;
			}

			noOfHrs = (int) (timeDiff / (1000* 60 * 60)) ;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return noOfHrs ;
	}

	public static String getRDCPQueryForIndoorHistory() {

		return null ;
	}

	public static void parseIndoorDetails(String downloadedData) {
		// TODO Auto-generated method stub
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss");
		SimpleDateFormat formatHr = new SimpleDateFormat("hh");
		Calendar cal = Calendar.getInstance() ;
		long endDateDiff = cal.getTimeInMillis() - (1*24*60*60*1000);
		Date dateEnd = new Date(endDateDiff);
		String startDateHr = formatDate.format(dateEnd) + " " + formatHr.format(dateEnd);
		
		List<Float> hrlyAqiValues = new ArrayList<Float>() ;
		List<Float> dailyAqiValues = new ArrayList<Float>() ;
		List<Integer> powerOnStatusList = new ArrayList<Integer>() ;

		int counter = 0 ;
		float aqiSum = 0.0f;

		String currentAQIDate = "" ;
		String currentAQIDateHr = "" ;
		int counterHr = 0 ;
		float aqiSumHr = 0.0f;
		int numOfHrs = -2;

		if (downloadedData != null ) {
			List<IndoorHistoryDto> indoorAQIHistory = new DataParser(downloadedData).parseHistoryData() ;
			if( indoorAQIHistory != null ) {
				for ( int index = 0 ; index < indoorAQIHistory.size() ; index ++ ) {
					String date = indoorAQIHistory.get(index).getTimeStamp() ;
					/**
					 * Hourly
					 */
					if (numOfHrs == -2) {
						
						numOfHrs = Utils.getDifferenceBetweenHrFromCurrentHr
								(date.substring(0,10)+" "+date.substring(11,13), startDateHr) ;
						if (numOfHrs >= 0 && numOfHrs <= 24) {
							for( int i = 0; i < numOfHrs; i ++ ) {
								hrlyAqiValues.add(-1.0F) ;
								powerOnStatusList.add(0);
							}
							numOfHrs = -1;
						}
					}

					if (numOfHrs == -1) {
						if ( currentAQIDateHr.equals("")) {
							aqiSumHr = indoorAQIHistory.get(index).getAqi() ;
							counterHr = 1 ;
						}
						else if ( !currentAQIDateHr.equals("")) {
							String s1 = currentAQIDateHr.substring(0,10)+" "+currentAQIDateHr.substring(11,13);
							String s2 = date.substring(0,10)+" "+date.substring(11,13);
							if(s1.equals(s2)) {
								aqiSumHr =  aqiSumHr + indoorAQIHistory.get(index).getAqi() ;	
								counterHr ++ ;
							}
							else {
								aqiSumHr = aqiSumHr / counterHr ;
								hrlyAqiValues.add(aqiSumHr/100) ;
								if (index > 0 && indoorAQIHistory.get(index).getTfav() 
										> indoorAQIHistory.get(index -1).getTfav()) {
									powerOnStatusList.add(1);
								}else {
									powerOnStatusList.add(0);
								}
								aqiSumHr = indoorAQIHistory.get(index).getAqi() ;
								counterHr = 1;
								String ss1 = currentAQIDateHr.substring(0,10)+" "+currentAQIDateHr.substring(11,13);
								String ss2 = date.substring(0,10)+" "+date.substring(11,13);
								int valueEmptyHrs = Utils.getDifferenceBetweenHrFromCurrentHr
										(ss2, ss1) ;
								if (valueEmptyHrs > 0) {
									for (int j = 0; j < valueEmptyHrs - 1; j++) {
										hrlyAqiValues.add(-1.0F) ;
										powerOnStatusList.add(0);
									}
								}
							}
						}

						if ( index == indoorAQIHistory.size() - 1 ) {
							if (counterHr != 0) { 
								aqiSumHr = aqiSumHr / counterHr ;
								hrlyAqiValues.add(aqiSumHr/100) ; 
								if (index > 0 && indoorAQIHistory.get(index).getTfav() 
										> indoorAQIHistory.get(index -1).getTfav()) {
									powerOnStatusList.add(1);
								} else if (index == 0){
									powerOnStatusList.add(1);
								} else {
									powerOnStatusList.add(0);
								}
								int valueEmptyHrs1 = Utils.getDifferenceBetweenHrFromCurrentHr
										(formatDate.format(new Date()) + " " + formatHr.format(new Date()), date.substring(0,10)+" "+date.substring(11,13)) ;
								if (valueEmptyHrs1 > 0) {
									for (int j = 0; j < valueEmptyHrs1 - 1; j++) {
										hrlyAqiValues.add(-1.0F) ;
										powerOnStatusList.add(0);
									}
								}
							}
						}
						currentAQIDateHr = date;
					} else {
						if ( index == indoorAQIHistory.size() - 1 ) {
							if (counterHr == 0) { 
								for( int i = 0; i < 24 ; i ++ ) {
									hrlyAqiValues.add(-1.0F) ;
									powerOnStatusList.add(0);
								}
							}
						}
					}


					/**
					 * Daily
					 */
					if ( index == 0 ) {
						int numberOfDays = Utils.getDifferenceBetweenDaysFromCurrentDay(date.substring(0,10),null) ;
						if ( numberOfDays < 28 ) {
							for( int i = 0; i < (28 - numberOfDays - 1) ; i ++ ) {
								dailyAqiValues.add(-1.0F) ;
							}
						}
					}


					if ( currentAQIDate.equals("")) {
						aqiSum = indoorAQIHistory.get(index).getAqi() ;
						counter = 1 ;
					}
					else if ( !currentAQIDate.equals("")) {
						if(currentAQIDate.substring(0,10).equals(date.substring(0,10))) {
							aqiSum =  aqiSum + indoorAQIHistory.get(index).getAqi() ;							
							counter ++ ;
						}
						else {
							aqiSum = aqiSum / counter ;
							dailyAqiValues.add(aqiSum/100) ;

							aqiSum = indoorAQIHistory.get(index).getAqi() ;
							counter = 1;

							int valueEmptyDays = Utils.getDifferenceBetweenDaysFromCurrentDay
									(date.substring(0,10), currentAQIDate.substring(0,10)) ;
							if (valueEmptyDays > 0) {
								for (int j = 0; j < valueEmptyDays - 1; j++) {
									dailyAqiValues.add(-1.0F) ;
								}
							}
						}
					} 

					/**
					 * Condition for last value
					 */
					if ( index == indoorAQIHistory.size() -1 ) {
						if (counter != 0) {
							aqiSum = aqiSum / counter ;
							dailyAqiValues.add(aqiSum/100) ;
							int valueEmptyDays = Utils.getDifferenceBetweenDaysFromCurrentDay
									(date.substring(0,10), null) ;
							if (valueEmptyDays > 0) {
								for (int j = 0; j < valueEmptyDays; j++) {
									dailyAqiValues.add(-1.0F) ;
								}
							}
						}else {
							for (int j = 0; j < 28; j++) {
								dailyAqiValues.add(-1.0F) ;
							}
						}
					}

					currentAQIDate = date ;
				}
				IndoorTrendDto indoorTrend = new IndoorTrendDto() ;
				if( hrlyAqiValues != null &&  hrlyAqiValues.size() > 0 ) {
					indoorTrend.setHourlyList(hrlyAqiValues) ;
				}
				if( dailyAqiValues != null && dailyAqiValues.size() > 0 ) {
					indoorTrend.setDailyList(dailyAqiValues) ;
				}
				if( powerOnStatusList != null && powerOnStatusList.size() > 0 ) {
					indoorTrend.setPowerDetailsList(powerOnStatusList) ;
				}
				SessionDto.getInstance().setIndoorTrendDto(indoorTrend) ;
			}
		}
	}
	
	
	public static String getCPPQuery(Context context) {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss");
		SimpleDateFormat formatHr = new SimpleDateFormat("hh");

		String endDate = formatDate.format(new Date()); 
		String endTime = formatTime.format(new Date()); 
		

		String qryPart3 = "endDate="+endDate+"T"+endTime+".1508314Z";

		Calendar cal = Calendar.getInstance();

		long startDateDiff = cal.getTimeInMillis() - (27*24*60*60*1000l);
		Date dateStart = new Date(startDateDiff);
		String startDate = formatDate.format(dateStart); 
		String startTime = formatTime.format(dateStart); 

		String qryPart2 = "startDate="+startDate+"T"+startTime+".1508314Z;";

		String qry = String.format(AppConstants.CLIENT_ID_RDCP, getAirPurifierID(context))+qryPart2+qryPart3;
		Log.i("QUERY", qry) ;
		return qry;
	}
	
	/**
	 * 
	 * @param ctx
	 * @param pSense
	 * @return
	 */
	public static Drawable setAQICircleBackground(Context ctx, int pSense) {
		Log.i(TAG, "aqi=  " + pSense);
		if(pSense >= 0 && pSense < 2) {
			return ctx.getResources().getDrawable(R.drawable.aqi_blue_circle_2x);
		} else if(pSense >= 2 && pSense < 3) {
			return ctx.getResources().getDrawable(R.drawable.aqi_purple_circle_2x);
		} else if(pSense >= 3 && pSense < 4) {
			return ctx.getResources().getDrawable(R.drawable.aqi_fusia_circle_2x);
		} else {
			return ctx.getResources().getDrawable(R.drawable.aqi_red_circle_2x);
		}
	}

	/**
	 * 
	 * @param ctx
	 * @param pSense
	 * @param status
	 * @param summary
	 */
	public static void setIndoorAQIStatusAndComment
		(Context ctx, int pSense, CustomTextView status, CustomTextView summary) {
		
		if(pSense >= 0 && pSense <= 1) {
			status.setText(ctx.getString(R.string.good)) ;
			summary.setText(ctx.getString(R.string.very_healthy_msg_indoor)) ;
		} else if(pSense > 2 && pSense <= 3) {
			status.setText(ctx.getString(R.string.moderate)) ;
			summary.setText(ctx.getString(R.string.healthy_msg_indoor)) ;
		} else if(pSense > 3 && pSense <= 4) {
			status.setText(ctx.getString(R.string.unhealthy)) ;
			summary.setText(ctx.getString(R.string.slightly_polluted_msg_indoor)) ;
		} else if(pSense < 4) {
			String tempStatus[] = ctx.getString(R.string.very_unhealthy).trim().split(" ");
			if (tempStatus != null && tempStatus.length > 1) {
				status.setText(tempStatus[0]+"\n"+tempStatus[1]);
			} else { 
				status.setText(ctx.getString(R.string.very_unhealthy)) ;
			}
			summary.setText(ctx.getString(R.string.moderately_polluted_msg_indoor)) ;
		} 
	}

	public static int getPreFilterStatusColour(int filterStatusValue) {
		if(filterStatusValue < 96) {
			return AppConstants.COLOR_GOOD;
		} else if ( filterStatusValue >= 96 && filterStatusValue < 112) {
			return AppConstants.COLOR_FAIR;
		} else {
			return AppConstants.COLOR_BAD;
		}
	}
	
	public static String getPreFilterStatusText(int filterStatusValue) {
		if(filterStatusValue < 96) {
			return AppConstants.GOOD;
		} else if ( filterStatusValue >= 96 && filterStatusValue < 112) {
			return AppConstants.CLEAN_SOON;
		} else {
			return AppConstants.CLEAN_NOW;
		}
	}
	
	public static int getMultiCareFilterStatusColour(int filterStatusValue) {
		if(filterStatusValue < 784) {
			return AppConstants.COLOR_GOOD;
		} else if ( filterStatusValue >= 784 && filterStatusValue < 840) {
			return AppConstants.COLOR_FAIR;
		} else {
			return AppConstants.COLOR_BAD;
		}
	}
	
	public static String getMultiCareFilterStatusText(int filterStatusValue) {
		if(filterStatusValue < 784) {
			return AppConstants.GOOD ;
		} else if (filterStatusValue >= 784 && filterStatusValue < 840) {
			return AppConstants.ACT_SOON ;
		} else if(filterStatusValue >= 840 && filterStatusValue < 960){
			return AppConstants.ACT_NOW ;
		} else {
			return AppConstants.FILTER_LOCK ;
		}
	}
	
	public static int getActiveCarbonFilterStatusColour(int filterStatusValue) {
		if(filterStatusValue < 2704) {
			return AppConstants.COLOR_GOOD;
		} else if ( filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return AppConstants.COLOR_FAIR;
		} else {
			return AppConstants.COLOR_BAD;
		}
	}
	
	public static String getActiveCarbonFilterStatusText(int filterStatusValue) {
		if(filterStatusValue < 2704) {
			return  AppConstants.GOOD ;
		} else if (filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return AppConstants.ACT_SOON ;
		} else if(filterStatusValue >= 2760 && filterStatusValue < 2880){
			return AppConstants.ACT_NOW ;
		} else {
			return AppConstants.FILTER_LOCK ;
		}
	}
	
	public static int getHEPAFilterStatusColour(int filterStatusValue) {
		if(filterStatusValue < 2704) {
			return AppConstants.COLOR_GOOD;
		} else if ( filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return AppConstants.COLOR_FAIR;
		} else {
			return AppConstants.COLOR_BAD;
		}
	}
	
	public static String getHEPAFilterFilterStatusText(int filterStatusValue) {
		if(filterStatusValue < 2704) {
			return AppConstants.GOOD;
		} else if (filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return AppConstants.ACT_SOON;
		} else if(filterStatusValue >= 2760 && filterStatusValue < 2880){
			return AppConstants.ACT_NOW;
		} else {
			return AppConstants.FILTER_LOCK;
		}
	}
	
	public static String getFanSpeedText(String fanSpeed) {
		if(AppConstants.FAN_SPEED_SILENT.equals(fanSpeed)) {
			return "Silent";
		} else if(AppConstants.FAN_SPEED_TURBO.equals(fanSpeed)) {
			return "Turbo";
		} else if(AppConstants.FAN_SPEED_AUTO.equals(fanSpeed)) {
			return "Auto";
		} else if(AppConstants.FAN_SPEED_ONE.equals(fanSpeed)) {
			return "1";
		} else if(AppConstants.FAN_SPEED_TWO.equals(fanSpeed)) {
			return "2";
		} else if(AppConstants.FAN_SPEED_THREE.equals(fanSpeed)) {
			return "3";
		} 
		return "";
	}
	
	public static String getMode(String fanSpeed, Context context) {
		String mode = "";
		if(AppConstants.FAN_SPEED_SILENT.equals(fanSpeed)) {
			mode = context.getString(R.string.silent);
		} else if(AppConstants.FAN_SPEED_TURBO.equals(fanSpeed)) {
			mode = context.getString(R.string.turbo);
		} else if(AppConstants.FAN_SPEED_AUTO.equals(fanSpeed)) {
			mode = context.getString(R.string.auto);
		} else if(AppConstants.FAN_SPEED_ONE.equals(fanSpeed)) {
			mode = context.getString(R.string.speed1);
		} else if(AppConstants.FAN_SPEED_TWO.equals(fanSpeed)) {
			mode = context.getString(R.string.speed2);
		} else if(AppConstants.FAN_SPEED_THREE.equals(fanSpeed)) {
			mode = context.getString(R.string.speed3);
		}
		return mode;
	}

	public static String getFilterStatusForDashboard(
			AirPurifierEventDto airPurifierEventDto) {
		String filterStatus = AppConstants.GOOD ;
		if ( airPurifierEventDto != null ) {
			String preFilterStatus = getPreFilterStatusText(airPurifierEventDto.getFilterStatus1()) ;
			String multiCareFilterStatus = getMultiCareFilterStatusText(airPurifierEventDto.getFilterStatus2()) ;
			String activeFilterStatus = getActiveCarbonFilterStatusText(airPurifierEventDto.getFilterStatus3()) ;
			String hepaFilterStatus = getHEPAFilterFilterStatusText(airPurifierEventDto.getFilterStatus4()) ;
			
			if ( multiCareFilterStatus.equals(AppConstants.ACT_NOW) ||
					activeFilterStatus.equals(AppConstants.ACT_NOW) ||
					hepaFilterStatus.equals(AppConstants.ACT_NOW)) {
				filterStatus = AppConstants.ACT_NOW ;
			}
			else if ( preFilterStatus.equals(AppConstants.CLEAN_NOW)) {
				filterStatus = AppConstants.CLEAN_NOW ;
			}
			else if ( preFilterStatus.equals(AppConstants.CLEAN_SOON)) {
				filterStatus = AppConstants.CLEAN_SOON ;
			}
			else if (multiCareFilterStatus.equals(AppConstants.ACT_SOON) ||
					activeFilterStatus.equals(AppConstants.ACT_SOON) ||
					hepaFilterStatus.equals(AppConstants.ACT_SOON)) {
				filterStatus = AppConstants.ACT_SOON ;
			}
			else if (multiCareFilterStatus.equals(AppConstants.FILTER_LOCK) ||
					activeFilterStatus.equals(AppConstants.FILTER_LOCK) ||
					hepaFilterStatus.equals(AppConstants.FILTER_LOCK)) {
				filterStatus = AppConstants.FILTER_LOCK ;
			}
		}
		return filterStatus ;
	}
	
	public static int getIndoorAQIMessage(int aqi) {
		if(aqi >= 0 && aqi <= 50) {
			return R.string.very_healthy_msg_indoor;
		} else if(aqi > 50 && aqi <= 100) {
			return R.string.healthy_msg_indoor;
		} else if(aqi > 100 && aqi <= 150) {
			return R.string.slightly_polluted_msg_indoor;
		} else if(aqi > 150 && aqi <= 200) {
			return R.string.moderately_polluted_msg_indoor;
		} else if(aqi > 200 && aqi <= 300) {
			return R.string.unhealthy_msg_indoor;
		} else if(aqi > 300 && aqi <= 1000) {
			return R.string.hazardous_msg_indoor;
		}
		return R.string.n_a;
	}
}
