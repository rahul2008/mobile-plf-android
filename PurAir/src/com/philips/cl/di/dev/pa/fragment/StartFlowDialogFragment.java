package com.philips.cl.di.dev.pa.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.ews.EWSWifiManager;
import com.philips.cl.di.dev.pa.newpurifier.AddNewPurifierListener;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Utils;


public class StartFlowDialogFragment extends DialogFragment implements AddNewPurifierListener {

	public static final String DIALOG_NUMBER = "dialog_number";
	public static final int NO_INTERNET = 0;
	public static final int LOCATION_SERVICES = 1;
	public static final int LOCATION_SERVICES_TURNED_OFF = 2;
	public static final int NO_WIFI = 4;
	public static final int AP_SELCTOR = 5;
	public static final int SEARCHING = 6;
	
	private WifiManager mWifiManager;
	
	private int mSelectedDialogNumber = 0;
	private StartFlowListener mListener;
	private AppSelectorAdapter appSelectorAdapter;
	private ArrayList<String> listItemsArrayList;
	private List<PurAirDevice> appItems;
	
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
			builder = createAppSelectorDialog(builder);
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
				dialog.dismiss();
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
	
	private AlertDialog.Builder createAppSelectorDialog(AlertDialog.Builder builder) {
		final DiscoveryManager discoveryManager = DiscoveryManager.getInstance();
		discoveryManager.setAddNewPurifierListener(this);
		appItems = discoveryManager.getNewDevicesDiscovered();
		listItemsArrayList = new ArrayList<String>();
		
		for (int i = 0; i < appItems.size(); i++) {
			listItemsArrayList.add(appItems.get(i).getName());
		}
		
		appSelectorAdapter = new AppSelectorAdapter(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, listItemsArrayList);
		
		builder.setTitle(R.string.which_purifier_to_connect)
		.setAdapter(appSelectorAdapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int position) {
				//TODO connect with specific air purifier
				PurAirDevice currentPurifier = appItems.get(position);
				currentPurifier.setConnectionState(ConnectionState.CONNECTED_LOCALLY) ;
				currentPurifier.setLastKnownNetworkSsid(EWSWifiManager.getSsidOfConnectedNetwork()) ;
				PurifierManager.getInstance().setCurrentPurifier(currentPurifier);
				
				((MainActivity) getActivity()).showFragment(((MainActivity) getActivity()).getDashboard());
				((MainActivity)getActivity()).setTitle(getString(R.string.dashboard_title));
				Utils.saveAppFirstUse(false);
				
				PurifierDatabase purifierDatabase = new PurifierDatabase();
				purifierDatabase.insertPurAirDevice(currentPurifier);
				List<PurAirDevice> purifiers = DiscoveryManager.getInstance().updateStoreDevices();
				PurifierManager.getInstance().setCurrentIndoorViewPagerPosition(purifiers.size() - 1);
				clearSelectPurifierObject();
				dismiss();            	   
			}
        });
		
		
		builder.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					clearSelectPurifierObject();
				}
				return false;
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
	
	private class AppSelectorAdapter extends ArrayAdapter<String> {

		public AppSelectorAdapter(Context context, int resource,
				int textViewResourceId, List<String> objects) {
			super(context, resource, textViewResourceId, objects);
			Log.i(ALog.TEMP, "AppSelectorAdapter constructor call.");
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return super.getView(position, convertView, parent);
		}
		
	}

	@Override
	public void onNewPurifierDiscover() {
		if (getActivity() == null || appSelectorAdapter == null || listItemsArrayList == null ) return;
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				appItems = DiscoveryManager.getInstance().getNewDevicesDiscovered();
				if (!listItemsArrayList.isEmpty()) {
					listItemsArrayList.clear();
				}
				for (int i = 0; i < appItems.size(); i++) {
					listItemsArrayList.add(appItems.get(i).getName());
				}
				appSelectorAdapter.notifyDataSetChanged();
			}
		});
	};
	
	private void clearSelectPurifierObject() {
		DiscoveryManager.getInstance().removeAddNewPurifierListener();
		appSelectorAdapter = null;
		listItemsArrayList = null;
	}
}
