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
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.cppdatabase.CppDatabaseModel;

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
	
	public static int getDifferenceBetweenDaysFromCurrentDay(String date) {		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd") ; 
		Calendar cal = Calendar.getInstance() ;
		int noOfDays = 0 ;
		try {
			Date lastDate = sf.parse(date) ;
			
			long timeDiff = cal.getTimeInMillis() - lastDate.getTime()  ;
			noOfDays = (int) timeDiff / (1000* 60 * 60 * 24) ;
		
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return noOfDays ;
	}
	
	public static String getRDCPQueryForIndoorHistory() {
		
		return null ;
	}
}
