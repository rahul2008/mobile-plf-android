package com.philips.cl.di.dev.pa.fragment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;


public class StartFlowDialogFragment extends DialogFragment {

	public static final String DIALOG_NUMBER = "dialog_number";
	public static final int NO_INTERNET = 0;
	public static final int LOCATION_SERVICES = 1;
	public static final int LOCATION_SERVICES_TURNED_OFF = 2;
	public static final int NO_WIFI = 4;
	public static final int AP_SELCTOR = 5;
	public static final int SEARCHING = 6;
	private static final String SHARED_PREFERENCE_NAME = "StartFlowPreferences";
	private static final String SHARED_PREFERENCE_FIRST_USE = "FirstUse";
	
	private WifiManager mWifiManager;
	
	private int mSelectedDialogNumber = 0;
	private StartFlowListener mListener;
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	
		Bundle bundle = getArguments();
		mSelectedDialogNumber = bundle.getInt(DIALOG_NUMBER, 0);
		
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        switch (mSelectedDialogNumber) {
		case NO_INTERNET:
			builder = createNoInternetDialog(builder);
			break;
		case LOCATION_SERVICES:
			builder = createLocationServicesDialog(builder);
			break;
		case LOCATION_SERVICES_TURNED_OFF:
			builder = createLocationServicesTurnedOffDialog(builder);
			break;
		case NO_WIFI:
			builder = createNoWifiDialog(builder);
			break;
		case AP_SELCTOR:
			builder = createApSelectorDialog(builder);
			break;
		case SEARCHING:
			builder = createSearchingDialog(builder);
			break;
		default:
			break;
		}

        return builder.create();
    }
	
	public void setListener(StartFlowListener listener) {
		mListener = listener;
	}
	
	private AlertDialog.Builder createNoInternetDialog(AlertDialog.Builder builder) {
		builder.setTitle(R.string.no_internet_title)
        .setMessage(R.string.no_internet_text)
               .setPositiveButton(R.string.turn_it_on, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   mWifiManager = (WifiManager) getActivity().getSystemService(MainActivity.WIFI_SERVICE);
                	   mWifiManager.setWifiEnabled(true);
                	   dismiss();
                	   mListener.noInternetTurnOnClicked(StartFlowDialogFragment.this);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   dismiss();
                	   mListener.dialogCancelClicked(StartFlowDialogFragment.this);
                   }
               });
		return builder;
	}
	
	private AlertDialog.Builder createLocationServicesTurnedOffDialog(AlertDialog.Builder builder) {
		builder.setTitle(R.string.location_services_turned_off_title)
        .setMessage(R.string.location_services_turned_off_text)
               .setPositiveButton(R.string.turn_it_on, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
		            	// Enable device location service window since android doesn't allow to change location settings in code
		       			Intent myIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		       			startActivity(myIntent);
		       			dismiss();
		       			mListener.locationServiceTurnOnClicked(StartFlowDialogFragment.this);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   dismiss();
                	   mListener.dialogCancelClicked(StartFlowDialogFragment.this);
                   }
               });
		return builder;
	}
	
	private AlertDialog.Builder createLocationServicesDialog(AlertDialog.Builder builder) {
		builder.setTitle(R.string.location_services_title)
        .setMessage(R.string.location_services_text)
               .setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   dismiss();
                	   mListener.locationServiceAllowClicked(StartFlowDialogFragment.this);
                   }
               })
               .setNegativeButton(R.string.do_not_allow, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   dismiss();
                	   mListener.dialogCancelClicked(StartFlowDialogFragment.this);
                   }
               });
		return builder;
	}
	
	private AlertDialog.Builder createNoWifiDialog(AlertDialog.Builder builder) {
		builder.setTitle(R.string.no_wifi_title)
        .setMessage(R.string.no_wifi_text)
               .setPositiveButton(R.string.turn_it_on, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   mWifiManager = (WifiManager) getActivity().getSystemService(MainActivity.WIFI_SERVICE);
                	   mWifiManager.setWifiEnabled(true);
                	   dismiss();
                	   mListener.noWifiTurnOnClicked(StartFlowDialogFragment.this);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   dismiss();
                	   mListener.dialogCancelClicked(StartFlowDialogFragment.this);
                   }
               });
		return builder;
	}
	
	private AlertDialog.Builder createApSelectorDialog(AlertDialog.Builder builder) {
		DiscoveryManager discoveryManager = DiscoveryManager.getInstance();
		final ArrayList<PurAirDevice> apItems = discoveryManager.getDiscoveredDevices();
		ArrayList<String> listItemsArrayList = new ArrayList<String>();
		String[] listItems = {};
		
		for (int i = 0; i < apItems.size(); i++) {
			listItemsArrayList.add(apItems.get(i).getName());
		}
		
		listItems = listItemsArrayList.toArray(new String[listItemsArrayList.size()]);
		
		builder.setTitle(R.string.which_purifier_to_connect)
		.setItems(listItems, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int position) {
            	   Log.e("TEMP", "item clicked: " + apItems.get(position).getName());
            	   //TODO connect with specific air purifier
            	   
            	   SharedPreferences mPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
            	   Editor mEditor = mPreferences.edit();
            	   mEditor.putBoolean(SHARED_PREFERENCE_FIRST_USE, false);
            	   mEditor.commit();
            	   dismiss();            	   
               }
        });
		return builder;
	}
	
	private AlertDialog.Builder createSearchingDialog(AlertDialog.Builder builder) {
		builder.setMessage(R.string.searching)
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   dismiss();
                	   mListener.dialogCancelClicked(StartFlowDialogFragment.this);
                   }
               });
		return builder;
	}
	
	public interface StartFlowListener {
		public void noWifiTurnOnClicked(DialogFragment dialog);
		public void noInternetTurnOnClicked(DialogFragment dialog);
		public void locationServiceAllowClicked(DialogFragment dialog);
		public void locationServiceTurnOnClicked(DialogFragment dialog);
		
		public void dialogCancelClicked(DialogFragment dialog);
	}
	
}
