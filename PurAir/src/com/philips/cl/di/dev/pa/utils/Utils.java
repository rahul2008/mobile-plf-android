package com.philips.cl.di.dev.pa.utils;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;

// TODO: Auto-generated Javadoc
/**
 * The Class Utils.
 */
public class Utils {
	
	/**
	 * Populates the image names  for the left menu.
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
	 * Populates the Labels  for the left menu.
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
	 * @param context the context
	 * @return The new Typeface
	 */
	public static Typeface getTypeFace(Context context) {
		return Typeface.createFromAsset(context.getAssets(), AppConstants.FONT);

	}

	/**
	 * Returns the screen width of the device.
	 *
	 * @param context the context
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
	 * @param context the context
	 * @return the iP address
	 */
	public static String getIPAddress(Context context) {
		String ipAddress = context.getSharedPreferences("sharedPreferences",0).getString("ipAddress", AppConstants.defaultIPAddress);
		return ipAddress;
	}
	

	/**
	 * Sets the ip address.
	 *
	 * @param ipAddress the ip address
	 * @param context the context
	 */
	public static void setIPAddress(String ipAddress, Context context) {
		
		SharedPreferences settings = context.getSharedPreferences("sharedPreferences",0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString("ipAddress", ipAddress);
	    editor.commit();
	}

	
	/**
	 * Show incorrect ip dialog.(This method will be removed after IFA)
	 *
	 * @param context the context
	 */
	public static void showIncorrectIpDialog(Context context) {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setTitle(AppConstants.MESSAGE_INCORRECT_IP);
		dialogBuilder.setPositiveButton(AppConstants.MESSAGE_OK, null);
		dialogBuilder.show();

	}
	
	
	/**
	 * Shows IP Address dialog.
	 *
	 * @param context the context
	 */
	public static void showIpDialog(final Context context)
	{
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		View dialogView = inflater.inflate(R.layout.dialog_settings,null);
		dialogBuilder.setView(dialogView);

		final EditText input = (EditText)dialogView.findViewById(R.id.editTextIPAddress);
		input.setText(Utils.getIPAddress(context));
		input.setSelection(input.length());


		InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					android.text.Spanned dest, int dstart, int dend) {
				if (end > start) {
					String destTxt = dest.toString();
					String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
					if (!resultingTxt.matches ("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) { 
						return "";
					} else {
						String[] splits = resultingTxt.split("\\.");
						for (int i=0; i<splits.length; i++) {
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
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		dialog.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialogInterface) {
				Button buttonOk = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
				buttonOk.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						String ipAddressMatchString = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
						String ipAddress = input.getText().toString();
						boolean isIpCorrect = ipAddress.matches(ipAddressMatchString);

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




}
