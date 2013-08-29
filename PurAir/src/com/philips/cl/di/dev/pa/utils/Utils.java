package com.philips.cl.di.dev.pa.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.res.AssetManager;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Environment;
import android.text.InputFilter;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.screens.adapters.DatabaseAdapter;

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
	 * Shows IP Address dialog.
	 * 
	 * @param context
	 *            the context
	 */
	public static void showIpDialog(final Context context) {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
				context);
		LayoutInflater inflater = LayoutInflater.from(context);
		View dialogView = inflater.inflate(R.layout.dialog_settings, null);
		dialogBuilder.setView(dialogView);

		final EditText input = (EditText) dialogView
				.findViewById(R.id.editTextIPAddress);
		input.setText(Utils.getIPAddress(context));
		input.setSelection(input.length());

		InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					android.text.Spanned dest, int dstart, int dend) {
				if (end > start) {
					String destTxt = dest.toString();
					String resultingTxt = destTxt.substring(0, dstart)
							+ source.subSequence(start, end)
							+ destTxt.substring(dend);
					if (!resultingTxt
							.matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
						return "";
					} else {
						String[] splits = resultingTxt.split("\\.");
						for (int i = 0; i < splits.length; i++) {
							if (Integer.valueOf(splits[i]) > 255) {
								return "";
							}
						}
					}
				}
				return null;
			}
		};
		input.setFilters(filters);

		dialogBuilder.setTitle(AppConstants.MESSAGE_ENTER_IP_ADDRESS);
		dialogBuilder.setPositiveButton(AppConstants.MESSAGE_OK, null);

		final AlertDialog dialog = dialogBuilder.create();
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		dialog.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialogInterface) {
				Button buttonOk = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
				buttonOk.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						String ipAddressMatchString = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
						String ipAddress = input.getText().toString();
						boolean isIpCorrect = ipAddress
								.matches(ipAddressMatchString);

						if (isIpCorrect) {
							Utils.setIPAddress(ipAddress, context);
							dialog.dismiss();
						} else {
							dialog.dismiss();
							showIncorrectIpDialog(context);

						}

					}
				});

			}
		});

		dialog.show();
	}

	/**
	 * Gets the time remaining.
	 * 
	 * @param filterValue
	 *            the filter value
	 * @return the time remaining
	 */
	public static String getTimeRemaining(int filterValue) {
		String timeRemaining = "";
		if (filterValue >= 360) {
			timeRemaining = "> 1 year left";
		} else if (filterValue >= 180) {
			timeRemaining = "1 year left";
		} else if (filterValue >= 90) {
			timeRemaining = "6 months left";
		} else if (filterValue >= 75) {
			timeRemaining = "3 months left";
		} else if (filterValue >= 60) {
			timeRemaining = "2.5 months left";
		} else if (filterValue >= 45) {
			timeRemaining = "2 months left";
		} else if (filterValue >= 28) {
			timeRemaining = "1.5 months left";
		} else if (filterValue >= 21) {
			timeRemaining = "4 weeks left";
		} else if (filterValue >= 14) {
			timeRemaining = "3 weeks left";
		} else if (filterValue >= 10) {
			timeRemaining = "2 weeks left";
		} else if (filterValue >= 7) {
			timeRemaining = "1.5 weeks left";
		} else if (filterValue >= 5) {
			timeRemaining = "1 weeks left";
		} else if (filterValue >= 3) {
			timeRemaining = "5 days left";
		} else if (filterValue > 2) {
			timeRemaining = "3 days left";
		} else if (filterValue > 1) {
			timeRemaining = "2 days left";
		} else if (filterValue > 0) {
			timeRemaining = "1 days left";
		} else if (filterValue < 1) {
			timeRemaining = "overdue";
		}

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
	 * Gets the indoor ring array for images.
	 * 
	 * @param iAQI
	 *            the i aqi
	 * @return the indoor ring
	 */
	public static String[] getIndoorRing(int iAQI) {
		if (iAQI >= 0 && iAQI <= 125) {
			return AppConstants.VGOOD_RING;
		} else if (iAQI > 125 && iAQI <= 250) {
			return AppConstants.GOOD_RING;
		} else if (iAQI > 250 && iAQI <= 375) {
			return AppConstants.FAIR_RING;
		} else if (iAQI > 375 && iAQI <= 500) {
			return AppConstants.BAD_RING;
		}

		return null;
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
	 * Gets the indoor bg.
	 * 
	 * @param iAQI
	 *            the i aqi
	 * @return the indoor bg
	 */
	public static String getIndoorBG(int iAQI) {
		if (iAQI >= 0 && iAQI <= 125) {
			return AppConstants.HOME_INDOOR_VGOOD;
		} else if (iAQI > 125 && iAQI <= 250) {
			return AppConstants.HOME_INDOOR_GOOD;
		} else if (iAQI > 250 && iAQI <= 375) {
			return AppConstants.HOME_INDOOR_FAIR;
		} else if (iAQI > 375 && iAQI <= 500) {
			return AppConstants.HOME_INDOOR_BAD;
		}

		return AppConstants.HOME_INDOOR_VGOOD;
	}

	/**
	 * Gets the aQI color.
	 * 
	 * @param AQI
	 *            the aqi
	 * @return the aQI color
	 */
	public static int getAQIColor(int AQI) {
		if (AQI > 0 && AQI <= 125) {
			return AppConstants.COLOR_VGOOD;
		} else if (AQI > 125 && AQI <= 250) {
			return AppConstants.COLOR_GOOD;
		} else if (AQI > 250 && AQI <= 375) {
			return AppConstants.COLOR_FAIR;
		} else if (AQI > 375 && AQI <= 500) {
			return AppConstants.COLOR_BAD;
		}
		return AppConstants.COLOR_NA;
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
	 * Gets the outdoor bg.
	 * 
	 * @param iOutdoorAQI
	 *            the i outdoor aqi
	 * @return the outdoor bg
	 */
	public static String getOutdoorBG(int iOutdoorAQI) {
		Log.i("BEFORE CRASH", iOutdoorAQI+"");
		if (iOutdoorAQI >= 0 && iOutdoorAQI <= 125) {
			return AppConstants.SHANGHAI_VGOOD;
		} else if (iOutdoorAQI > 125 && iOutdoorAQI <= 250) {
			return AppConstants.SHANGHAI_GOOD;
		} else if (iOutdoorAQI > 250 && iOutdoorAQI <= 375) {
			return AppConstants.SHANGHAI_FAIR;
		} else if (iOutdoorAQI > 375 && iOutdoorAQI <= 500) {
			return AppConstants.SHANGHAI_BAD;
		}

		return AppConstants.SHANGHAI_VGOOD;
	}

	/**
	 * Gets the fan indicator.
	 * 
	 * @param iAQI
	 *            the i aqi
	 * @return the fan indicator
	 */
	public static String getFanIndicator(int iAQI) {
		if (iAQI >= 0 && iAQI <= 125) {
			return AppConstants.WARNING_VGOOD;
		} else if (iAQI > 125 && iAQI <= 250) {
			return AppConstants.WARNING_GOOD;
		} else if (iAQI > 250 && iAQI <= 375) {
			return AppConstants.WARNING_FAIR;
		} else if (iAQI > 375 && iAQI <= 500) {
			return AppConstants.WARNING_FAIR;
		}
		return AppConstants.WARNING_VGOOD;
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
			if(array_AQI.get(i)<0)
			{
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
	 * @param array_AQI
	 *            the array_ aqi
	 * @return the good air percentage
	 */
	public static int getGoodAirPercentage(ArrayList<Integer> array_AQI) {
		int percent = 0;
		int count = array_AQI.size();
		int countGood_VGood = 0;
		for (int i = 0; i < array_AQI.size(); i++) {
			// Discard the value if it is negative
			if(array_AQI.get(i)<0)
			{
				count -- ;
				continue;
			}
			
			if (array_AQI.get(i) <= 250) {
				countGood_VGood++;
			}
		}
		percent = (countGood_VGood * 100) / count;
		return percent;
	}

	/**
	 * Gets the average aqi.
	 * 
	 * @param array_AQI
	 *            the array_ aqi
	 * @return the average aqi
	 */
	public static int getAverageAQI(ArrayList<Integer> array_AQI) {
		int sum = 0;
		int count = array_AQI.size();
		for (int i = 0; i < array_AQI.size(); i++) {
			if(array_AQI.get(i)<0)
			{
				count -- ;
				continue;
			}
			sum = sum + array_AQI.get(i);
		}

		return (sum / count);
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
}
