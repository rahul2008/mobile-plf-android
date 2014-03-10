package com.philips.cl.di.dev.pa.utils;

import static com.philips.cl.di.dev.pa.constants.AnimatorConstants.ANIM_ROTATION;
import static com.philips.cl.di.dev.pa.constants.AppConstants.CLEAR;
import static com.philips.cl.di.dev.pa.constants.AppConstants.CLEAR_SKIES;
import static com.philips.cl.di.dev.pa.constants.AppConstants.CLOUDY;
import static com.philips.cl.di.dev.pa.constants.AppConstants.HEAVY_RAIN;
import static com.philips.cl.di.dev.pa.constants.AppConstants.HEAVY_RAIN_AT_TIMES;
import static com.philips.cl.di.dev.pa.constants.AppConstants.LIGHT_DRIZZLE;
import static com.philips.cl.di.dev.pa.constants.AppConstants.LIGHT_RAIN_SHOWER;
import static com.philips.cl.di.dev.pa.constants.AppConstants.MIST;
import static com.philips.cl.di.dev.pa.constants.AppConstants.MODERATE_OR_HEAVY_RAIN_IN_AREA_WITH_THUNDER;
import static com.philips.cl.di.dev.pa.constants.AppConstants.MODERATE_OR_HEAVY_RAIN_SHOWER;
import static com.philips.cl.di.dev.pa.constants.AppConstants.PARTLY_CLOUDY;
import static com.philips.cl.di.dev.pa.constants.AppConstants.PATCHY_LIGHT_RAIN_IN_AREA_WITH_THUNDER;
import static com.philips.cl.di.dev.pa.constants.AppConstants.SNOW;
import static com.philips.cl.di.dev.pa.constants.AppConstants.SUNNY;
import static com.philips.cl.di.dev.pa.constants.AppConstants.TORRENTIAL_RAIN_SHOWER;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import com.nineoldandroids.animation.ObjectAnimator;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.cppdatabase.CppDatabaseModel;
import com.philips.cl.di.dev.pa.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.detail.utils.Coordinates;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.dto.IndoorHistoryDto;
import com.philips.cl.di.dev.pa.dto.IndoorTrendDto;
import com.philips.cl.di.dev.pa.dto.OutdoorAQIEventDto;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.pureairui.fragments.HomeFragment;

// TODO: Auto-generated Javadoc
/**
 * The Class Utils.
 */
@SuppressLint("SimpleDateFormat")
public class Utils {

	/** The Constant TAG. */
	private static final String TAG = "Utils";
	private static String currentDateHr = "";
	private static String ago24DateHr = "";
	public static final List<Integer> outdoorAQIPercentageList = new ArrayList<Integer>();

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

	public static void storeCPPKeys(Context context,
			CppDatabaseModel cppDataModel) {
		SharedPreferences settings = context.getSharedPreferences(
				"cpp_preferences01", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("airpurifierid", cppDataModel.getDistribution());
		editor.putString("euid", cppDataModel.getEuId());
		editor.putString("privatekey",
				cppDataModel.getSysKey() + cppDataModel.getPrivateKey());
		editor.putString("registrationid", cppDataModel.getRegId());
		editor.putString("mac_address", cppDataModel.getMacId());
		editor.commit();
	}

	public static String getMacAddress(Context context) {
		return context.getSharedPreferences("cpp_preferences01", 0).getString(
				"mac_address", "");
	}

	public static String getPrivateKey(Context context) {
		String privateKey = context
				.getSharedPreferences("cpp_preferences01", 0).getString(
						"privatekey", "");
		return privateKey;
	}

	public static String getRegistrationID(Context context) {
		String registrationid = context.getSharedPreferences(
				"cpp_preferences01", 0).getString("registrationid", "");
		return registrationid;
	}

	public static String getEuid(Context context) {
		String euid = context.getSharedPreferences("cpp_preferences01", 0)
				.getString("euid", "");
		return euid;
	}

	public static String getAirPurifierID(Context context) {
		String airPurifierID = context.getSharedPreferences(
				"cpp_preferences01", 0).getString("airpurifierid", "");
		return airPurifierID;
	}

	public static void clearCPPDetails(Context context) {
		context.getSharedPreferences("cpp_preferences01", 0).edit().clear()
		.commit();
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
	public static String getTimeRemaining(int filterType, int filterValue) {
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

	public static String getCurrentDateForDB() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date now = new Date(System.currentTimeMillis());
		return sdf.format(now);

	}

	public static int getDifferenceBetweenDaysFromCurrentDay(String date,
			String date0) {
		// Log.i("DOWNLOAD", "date: " + date + " : prev date: " + date0);
		int noOfDays = 0;

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		long timeDiff = 0;
		try {
			Date lastDate = sf.parse(date);
			if (date0 == null) {
				timeDiff = cal.getTimeInMillis() - lastDate.getTime();
			} else {
				Date prevDate = sf.parse(date0);
				timeDiff = lastDate.getTime() - prevDate.getTime();
			}
			noOfDays = (int) (timeDiff / (1000 * 60 * 60 * 24));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return noOfDays;
	}

	public static int getDifferenceBetweenHrFromCurrentHr(String date,
			String date0) {
		int noOfHrs = 0;

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH");
		long timeDiff = 0;
		try {
			Date d = sf.parse(date);
			Date d0 = sf.parse(date0);
			if (d.getTime() >= d0.getTime()) {
				timeDiff = d.getTime() - d0.getTime();
			} else {
				return -2;
			}

			noOfHrs = (int) (timeDiff / (1000 * 60 * 60));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return noOfHrs;
	}

	public static String getRDCPQueryForIndoorHistory() {

		return null;
	}

	public static void parseIndoorDetails(String downloadedData) {

		List<Float> hrlyAqiValues = new ArrayList<Float>();
		List<Float> dailyAqiValues = new ArrayList<Float>();
		List<Integer> powerOnStatusList = new ArrayList<Integer>();

		int counter = 0;
		float aqiSum = 0.0f;

		String currentAQIDate = "";
		String currentAQIDateHr = "";
		int counterHr = 0;
		float aqiSumHr = 0.0f;
		int numOfHrs = -2;

		if (downloadedData != null) {
			List<IndoorHistoryDto> indoorAQIHistory = new DataParser(
					downloadedData).parseHistoryData();
			if (indoorAQIHistory != null) {
				for (int index = 0; index < indoorAQIHistory.size(); index++) {
					String date = indoorAQIHistory.get(index).getTimeStamp();
					/**
					 * Hourly
					 */
					if (numOfHrs == -2) {

						numOfHrs = Utils.getDifferenceBetweenHrFromCurrentHr(
								date.substring(0, 10) + " "
										+ date.substring(11, 13), ago24DateHr);
						if (numOfHrs >= 0 && numOfHrs <= 24) {
							for (int i = 0; i < numOfHrs; i++) {
								hrlyAqiValues.add(-1.0F);
								powerOnStatusList.add(0);
							}
							numOfHrs = -1;

						}
					}

					if (numOfHrs == -1) {

						if (currentAQIDateHr.equals("")) {
							aqiSumHr = indoorAQIHistory.get(index).getAqi();
							counterHr = 1;
						} else if (!currentAQIDateHr.equals("")) {
							String s1 = currentAQIDateHr.substring(0, 10) + " "
									+ currentAQIDateHr.substring(11, 13);
							String s2 = date.substring(0, 10) + " "
									+ date.substring(11, 13);
							if (s1.equals(s2)) {
								aqiSumHr = aqiSumHr
										+ indoorAQIHistory.get(index).getAqi();
								counterHr++;
							} else {
								aqiSumHr = aqiSumHr / counterHr;
								hrlyAqiValues.add(aqiSumHr / 100);
								if (index > 0
										&& indoorAQIHistory.get(index)
										.getTfav() > indoorAQIHistory
										.get(index - 1).getTfav()) {
									powerOnStatusList.add(1);
								} else {
									powerOnStatusList.add(0);
								}
								aqiSumHr = indoorAQIHistory.get(index).getAqi();
								counterHr = 1;
								String ss1 = currentAQIDateHr.substring(0, 10)
										+ " "
										+ currentAQIDateHr.substring(11, 13);
								String ss2 = date.substring(0, 10) + " "
										+ date.substring(11, 13);
								int valueEmptyHrs = Utils
										.getDifferenceBetweenHrFromCurrentHr(
												ss2, ss1);
								if (valueEmptyHrs > 0) {
									for (int j = 0; j < valueEmptyHrs - 1; j++) {
										hrlyAqiValues.add(-1.0F);
										powerOnStatusList.add(0);
									}
								}
							}
						}

						if (index == indoorAQIHistory.size() - 1) {
							if (counterHr != 0) {
								aqiSumHr = aqiSumHr / counterHr;
								hrlyAqiValues.add(aqiSumHr / 100);
								if (index > 0
										&& indoorAQIHistory.get(index)
										.getTfav() > indoorAQIHistory
										.get(index - 1).getTfav()) {
									powerOnStatusList.add(1);
								} else if (index == 0) {
									powerOnStatusList.add(1);
								} else {
									powerOnStatusList.add(0);
								}
								int valueEmptyHrs1 = Utils
										.getDifferenceBetweenHrFromCurrentHr(
												currentDateHr,
												date.substring(0, 10)
												+ " "
												+ date.substring(11, 13));
								if (valueEmptyHrs1 > 0) {
									for (int j = 0; j < valueEmptyHrs1 - 1; j++) {
										hrlyAqiValues.add(-1.0F);
										powerOnStatusList.add(0);
									}
								}
							} else if (counterHr == 0) {
								for (int i = 0; i < 24; i++) {
									hrlyAqiValues.add(-1.0F);
									powerOnStatusList.add(0);
								}
							}
						}
						currentAQIDateHr = date;
					}

					/**
					 * Daily
					 */
					if (index == 0) {
						int numberOfDays = Utils
								.getDifferenceBetweenDaysFromCurrentDay(
										date.substring(0, 10), null);
						if (numberOfDays < 28) {
							for (int i = 0; i < (28 - numberOfDays - 1); i++) {
								dailyAqiValues.add(-1.0F);
							}
						}
					}

					if (currentAQIDate.equals("")) {
						aqiSum = indoorAQIHistory.get(index).getAqi();
						counter = 1;
					} else if (!currentAQIDate.equals("")) {
						if (currentAQIDate.substring(0, 10).equals(
								date.substring(0, 10))) {
							aqiSum = aqiSum
									+ indoorAQIHistory.get(index).getAqi();
							counter++;
						} else {
							aqiSum = aqiSum / counter;
							dailyAqiValues.add(aqiSum / 100);

							aqiSum = indoorAQIHistory.get(index).getAqi();
							counter = 1;

							int valueEmptyDays = Utils
									.getDifferenceBetweenDaysFromCurrentDay(
											date.substring(0, 10),
											currentAQIDate.substring(0, 10));
							if (valueEmptyDays > 0) {
								for (int j = 0; j < valueEmptyDays - 1; j++) {
									dailyAqiValues.add(-1.0F);
								}
							}
						}
					}

					/**
					 * Condition for last value
					 */
					if (index == indoorAQIHistory.size() - 1) {
						if (counter != 0) {
							aqiSum = aqiSum / counter;
							dailyAqiValues.add(aqiSum / 100);
							int valueEmptyDays = Utils
									.getDifferenceBetweenDaysFromCurrentDay(
											date.substring(0, 10), null);
							if (valueEmptyDays > 0) {
								for (int j = 0; j < valueEmptyDays; j++) {
									dailyAqiValues.add(-1.0F);
								}
							}
						} else {
							for (int j = 0; j < 28; j++) {
								dailyAqiValues.add(-1.0F);
							}
						}
					}

					currentAQIDate = date;
				}

				// Log.i("Download", "hrlyAqiValuesBefore==  " + hrlyAqiValues);
				// Log.i("Download", "dailyAqiValuesBefore==  " +
				// dailyAqiValues);
				// Log.i("Download", "powerOnStatusListBefore==  " +
				// powerOnStatusList);

				IndoorTrendDto indoorTrend = new IndoorTrendDto();
				if (hrlyAqiValues != null && hrlyAqiValues.size() > 0) {

					if (hrlyAqiValues.size() > 24) {
						int diff = hrlyAqiValues.size() - 24;
						for (int i = 0; i < diff; i++) {
							hrlyAqiValues.remove(0);
						}
					}
					indoorTrend.setHourlyList(hrlyAqiValues);
				}
				if (dailyAqiValues != null && dailyAqiValues.size() > 0) {
					if (dailyAqiValues.size() > 28) {
						int diff = dailyAqiValues.size() - 28;
						for (int i = 0; i < diff; i++) {
							dailyAqiValues.remove(0);
						}
					}
					indoorTrend.setDailyList(dailyAqiValues);
				}
				if (powerOnStatusList != null && powerOnStatusList.size() > 0) {
					if (powerOnStatusList.size() > 24) {
						int diff = powerOnStatusList.size() - 24;
						for (int i = 0; i < diff; i++) {
							powerOnStatusList.remove(0);
						}
					}
					indoorTrend.setPowerDetailsList(powerOnStatusList);
				}
				SessionDto.getInstance().setIndoorTrendDto(indoorTrend);
			}
		}
	}

	public static String getCPPQuery(Context context) {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss");
		SimpleDateFormat formatHr = new SimpleDateFormat("hh");

		String endDate = formatDate.format(new Date());
		String endTime = formatTime.format(new Date());
		String endHr = formatHr.format(new Date());

		String qryPart3 = "endDate=" + endDate + "T" + endTime + ".1508314Z";
		currentDateHr = endDate + " " + endHr;

		Calendar cal = Calendar.getInstance();

		long startDateDiff = cal.getTimeInMillis()
				- (28 * 24 * 60 * 60 * 1000L);
		Date dateStart = new Date(startDateDiff);
		String startDate = formatDate.format(dateStart);
		String startTime = formatTime.format(dateStart);

		String qryPart2 = "startDate=" + startDate + "T" + startTime
				+ ".1508314Z;";

		String qry = String.format(AppConstants.CLIENT_ID_RDCP,
				getAirPurifierID(context)) + qryPart2 + qryPart3;
		Log.i("QUERY", qry);

		long endDateDiff = cal.getTimeInMillis() - (24 * 60 * 60 * 1000);
		Date dateEnd = new Date(endDateDiff);
		ago24DateHr = formatDate.format(dateEnd) + " "
				+ formatHr.format(dateEnd);

		return qry;
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
	(Context ctx, float indoorAQI, CustomTextView status, CustomTextView summary) {

		if(indoorAQI <= 1.4f && indoorAQI > 0) {
			status.setText(ctx.getString(R.string.good)) ;
			summary.setText(ctx.getString(R.string.very_healthy_msg_indoor)) ;
		} else if(indoorAQI > 1.4f && indoorAQI <= 2.3f) {
			status.setText(ctx.getString(R.string.moderate)) ;
			summary.setText(ctx.getString(R.string.healthy_msg_indoor)) ;
		} else if(indoorAQI > 2.3f && indoorAQI <= 3.5f) {
			status.setText(ctx.getString(R.string.unhealthy)) ;
			summary.setText(ctx.getString(R.string.slightly_polluted_msg_indoor)) ;
		} else if(indoorAQI > 3.5f) {
			String tempStatus[] = ctx.getString(R.string.very_unhealthy).trim().split(" ");
			if (tempStatus != null && tempStatus.length > 1) {
				status.setText(tempStatus[0] + "\n" + tempStatus[1]);
			} else {
				status.setText(ctx.getString(R.string.very_unhealthy));
			}
			summary.setText(ctx
					.getString(R.string.moderately_polluted_msg_indoor));
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
		if (filterStatusValue < 96) {
			return AppConstants.GOOD;
		} else if (filterStatusValue >= 96 && filterStatusValue < 112) {
			return AppConstants.CLEAN_SOON;
		} else {
			return AppConstants.CLEAN_NOW;
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
			return AppConstants.GOOD;
		} else if (filterStatusValue >= 784 && filterStatusValue < 840) {
			return AppConstants.ACT_SOON;
		} else if (filterStatusValue >= 840 && filterStatusValue < 960) {
			return AppConstants.ACT_NOW;
		} else {
			return AppConstants.FILTER_LOCK;
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
			return AppConstants.GOOD;
		} else if (filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return AppConstants.ACT_SOON;
		} else if (filterStatusValue >= 2760 && filterStatusValue < 2880) {
			return AppConstants.ACT_NOW;
		} else {
			return AppConstants.FILTER_LOCK;
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
			return AppConstants.GOOD;
		} else if (filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return AppConstants.ACT_SOON;
		} else if (filterStatusValue >= 2760 && filterStatusValue < 2880) {
			return AppConstants.ACT_NOW;
		} else {
			return AppConstants.FILTER_LOCK;
		}
	}

	public static String getFanSpeedText(String fanSpeed) {
		if (AppConstants.FAN_SPEED_SILENT.equals(fanSpeed)) {
			return "Silent";
		} else if (AppConstants.FAN_SPEED_TURBO.equals(fanSpeed)) {
			return "Turbo";
		} else if (AppConstants.FAN_SPEED_AUTO.equals(fanSpeed)) {
			return "Auto";
		} else if (AppConstants.FAN_SPEED_ONE.equals(fanSpeed)) {
			return "1";
		} else if (AppConstants.FAN_SPEED_TWO.equals(fanSpeed)) {
			return "2";
		} else if (AppConstants.FAN_SPEED_THREE.equals(fanSpeed)) {
			return "3";
		}
		return "";
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
			AirPurifierEventDto airPurifierEventDto) {
		String filterStatus = "-";
		if (airPurifierEventDto != null) {
			filterStatus = AppConstants.GOOD;
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
				filterStatus = AppConstants.ACT_NOW;
			} else if (preFilterStatus.equals(AppConstants.CLEAN_NOW)) {
				filterStatus = AppConstants.CLEAN_NOW;
			} else if (preFilterStatus.equals(AppConstants.CLEAN_SOON)) {
				filterStatus = AppConstants.CLEAN_SOON;
			} else if (multiCareFilterStatus.equals(AppConstants.ACT_SOON)
					|| activeFilterStatus.equals(AppConstants.ACT_SOON)
					|| hepaFilterStatus.equals(AppConstants.ACT_SOON)) {
				filterStatus = AppConstants.ACT_SOON;
			} else if (multiCareFilterStatus.equals(AppConstants.FILTER_LOCK)
					|| activeFilterStatus.equals(AppConstants.FILTER_LOCK)
					|| hepaFilterStatus.equals(AppConstants.FILTER_LOCK)) {
				filterStatus = AppConstants.FILTER_LOCK;
			}
		}
		return filterStatus;
	}

	public static int getIndoorAQIMessage(float aqi) {
		if (aqi <= 1.4f) {
			return R.string.very_healthy_msg_indoor;
		} else if (aqi > 1.4f && aqi <= 2.3f) {
			return R.string.healthy_msg_indoor;
		} else if (aqi > 2.3f && aqi <= 3.5f) {
			return R.string.slightly_polluted_msg_indoor;
		} else if (aqi > 3.5f) {
			return R.string.moderately_polluted_msg_indoor;
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

		if ((weatherDesc.compareToIgnoreCase(SUNNY)) == 0) {
			weatherImage = contex.getResources().getDrawable(R.drawable.sunny);
		} else if ((weatherDesc.compareToIgnoreCase(MIST)) == 0) {
			weatherImage = contex.getResources().getDrawable(R.drawable.mist);
		} else if ((weatherDesc.compareToIgnoreCase(CLOUDY)) == 0) {
			weatherImage = contex.getResources().getDrawable(R.drawable.cloudy);
		} else if ((weatherDesc.compareToIgnoreCase(PARTLY_CLOUDY)) == 0) {

			if (isDayTime.compareToIgnoreCase("Yes") == 0)
				weatherImage = contex.getResources().getDrawable(
						R.drawable.partly_cloudy);
			else
				weatherImage = contex.getResources().getDrawable(
						R.drawable.partly_cloudy_night);
			// weatherImage =
			// contex.getResources().getDrawable(R.drawable.partly_cloudy_night);
		} else if ((weatherDesc.compareToIgnoreCase(CLEAR_SKIES)) == 0) {
			if (isDayTime.compareToIgnoreCase("Yes") == 0)
				weatherImage = contex.getResources().getDrawable(
						R.drawable.sunny);
			else
				weatherImage = contex.getResources().getDrawable(
						R.drawable.clear_sky_night);
			// weatherImage =
			// contex.getResources().getDrawable(R.drawable.clear_sky_night);
		} else if ((weatherDesc.compareToIgnoreCase(SNOW)) == 0) {
			weatherImage = contex.getResources().getDrawable(R.drawable.snow);
		} else if ((weatherDesc.compareToIgnoreCase(LIGHT_RAIN_SHOWER) == 0)
				|| (weatherDesc.compareToIgnoreCase(LIGHT_DRIZZLE) == 0)) {
			weatherImage = contex.getResources().getDrawable(
					R.drawable.light_rain_shower);
		} else if ((weatherDesc
				.compareToIgnoreCase(PATCHY_LIGHT_RAIN_IN_AREA_WITH_THUNDER)) == 0) {
			weatherImage = contex.getResources().getDrawable(
					R.drawable.light_rain_with_thunder);
		} else if ((weatherDesc
				.compareToIgnoreCase(MODERATE_OR_HEAVY_RAIN_SHOWER) == 0)
				|| (weatherDesc.compareToIgnoreCase(TORRENTIAL_RAIN_SHOWER) == 0)
				|| (weatherDesc.compareToIgnoreCase(HEAVY_RAIN) == 0)) {
			weatherImage = contex.getResources().getDrawable(
					R.drawable.heavy_rain);
		} else if ((weatherDesc.compareToIgnoreCase(HEAVY_RAIN_AT_TIMES)) == 0) {
			// TODO : Replace with proper icon. Icon not found, replacing with
			// heavy raind
			weatherImage = contex.getResources().getDrawable(
					R.drawable.heavy_rain);
		} else if ((weatherDesc
				.compareToIgnoreCase(MODERATE_OR_HEAVY_RAIN_IN_AREA_WITH_THUNDER)) == 0) {
			weatherImage = contex.getResources().getDrawable(
					R.drawable.moderate_rain_with_thunder);
		} else if ((weatherDesc.compareToIgnoreCase(CLEAR)) == 0) {
			if (isDayTime.compareToIgnoreCase("Yes") == 0)
				weatherImage = contex.getResources().getDrawable(
						R.drawable.sunny);
			else
				weatherImage = contex.getResources().getDrawable(
						R.drawable.clear_sky_night);
		} else {
			weatherImage = contex.getResources().getDrawable(
					R.drawable.light_rain_shower);
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
			if (outdoorAQIPercentageList != null ) {
				outdoorAQIPercentageList.clear();
			}
			int idx[] = outdoorAQIEventDto.getIdx();
			float[] lastDayAQIReadings = new float[24];
			float[] last7dayAQIReadings = new float[7];
			float[] last4weekAQIReadings = new float[28];
			/** last day days */
			/**
			 * Adding last 24 data into lastDayReadings array, from index 696 t0
			 * 719
			 */
			int lastDayHr = 24;
			if (idx != null) {
				goodAirCount = 0;
				totalAirCount = 0;
				for (int i = 0; i < lastDayAQIReadings.length; i++) {
					lastDayAQIReadings[i] = idx[lastDayHr - 1 - i];
					if (idx[lastDayHr - 1 - i] < 100) {
						goodAirCount++;
					}
					totalAirCount++;
				}
				outdoorAQIPercentageList.add(Utils.getPercentage(goodAirCount,
						totalAirCount));
			}

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
			int last7dayHrs = (6 * 24) + hr;

			if (idx != null) {
				float sum = 0;
				float avg = 0;
				int j = 0;
				goodAirCount = 0;
				totalAirCount = 0;
				for (int i = 0; i < last7dayHrs; i++) {
					float x = idx[last7dayHrs - 1 - i];
					sum = sum + x;
					if (i == 23 || i == 47 || i == 71 || i == 95 || i == 119
							|| i == 143) {
						avg = sum / (float) 24;
						last7dayAQIReadings[j] = avg;
						if (avg < 100) {
							goodAirCount++;
						}
						totalAirCount++;

						j++;
						sum = 0;
						avg = 0;
					} else if (i == last7dayHrs - 1) {
						avg = sum / (float) hr;
						last7dayAQIReadings[j] = avg;
						if (avg < 100) {
							goodAirCount++;
						}
						totalAirCount++;

						sum = 0;
						avg = 0;
					}
				}
				outdoorAQIPercentageList.add(Utils.getPercentage(goodAirCount,
						totalAirCount));
			}

			/** last 4 weeks */
			/**
			 * TODO - Explain the logic in 3lines
			 */
			int last4WeekHrs = (3 * 7 * 24) + (6 * 24) + hr;

			if (idx != null) {
				int count = 1;
				float sum = 0;
				float avg = 0;
				int j = 0;
				goodAirCount = 0;
				totalAirCount = 0;
				for (int i = 0; i < last4WeekHrs; i++) {

					float x = idx[last4WeekHrs - 1 - i];
					sum = sum + x;
					if (count == 24 && j < 21) {
						avg = sum / (float) 24;
						last4weekAQIReadings[j] = avg;
						if (avg < 100) {
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
							if (last7dayAQIReadings[m] < 100) {
								goodAirCount++;
							}
							totalAirCount++;
							j++;
						}
						break;
					}
					count++;
				}
				outdoorAQIPercentageList.add(Utils.getPercentage(goodAirCount,
						totalAirCount));
			}
		}
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

	/**
	 * This will use for writing number on text
	 * */
	public static Bitmap writeTextOnDrawable(Context ctx, int drawableId,
			String text) {

		Bitmap bm = BitmapFactory
				.decodeResource(ctx.getResources(), drawableId).copy(
						Bitmap.Config.ARGB_8888, true);

		Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.BLACK);
		paint.setTypeface(tf);
		paint.setTextAlign(Align.CENTER);

		if (Coordinates.getInstance(ctx) != null) {
			paint.setTextSize(Coordinates.getInstance(ctx).getIdTxtSize());
		}

		Rect textRect = new Rect();
		paint.getTextBounds(text, 0, text.length(), textRect);

		Canvas canvas = new Canvas(bm);

		// Calculate the positions
		int xPos = (canvas.getWidth() / 2) - 2;

		int yPos = (canvas.getHeight()) - 5;

		canvas.drawText(text, xPos, yPos, paint);

		return bm;
	}
}
