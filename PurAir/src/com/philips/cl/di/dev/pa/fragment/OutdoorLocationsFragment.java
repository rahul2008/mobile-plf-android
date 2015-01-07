package com.philips.cl.di.dev.pa.fragment;

import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.dashboard.OutdoorController;
import com.philips.cl.di.dev.pa.dashboard.OutdoorController.CurrentCityAreaIdReceivedListener;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.outdoorlocations.AddOutdoorLocationHelper;
import com.philips.cl.di.dev.pa.outdoorlocations.OutdoorDataProvider;
import com.philips.cl.di.dev.pa.outdoorlocations.UserCitiesDatabase;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.util.LocationUtils;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class OutdoorLocationsFragment extends BaseFragment implements ConnectionCallbacks,
	OnConnectionFailedListener, OnCheckedChangeListener, CurrentCityAreaIdReceivedListener {
	private static final String TAG = OutdoorLocationsFragment.class.getSimpleName();
	
	private boolean isGooglePlayServiceAvailable;
	private ProgressBar searchingLoctionProgress;
	private ListView mOutdoorLocationListView;
	private Hashtable<String, Boolean> selectedItemHashtable;
	private ToggleButton currentLocation;
	private UserSelectedCitiesAdapter userSelectedCitiesAdapter;
	private UserCitiesDatabase userCitiesDatabase;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		isGooglePlayServiceAvailable = Utils.isGooglePlayServiceAvailable();
		Log.i(TAG, "isGooglePlayServiceAvailable " + isGooglePlayServiceAvailable);
		selectedItemHashtable = new Hashtable<String, Boolean>();
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		View view = inflater.inflate(R.layout.outdoor_locations_fragment, container, false);
		currentLocation = (ToggleButton) view.findViewById(R.id.btn_current_location);
		mOutdoorLocationListView = (ListView) view.findViewById(R.id.outdoor_locations_list);
		searchingLoctionProgress = (ProgressBar) view.findViewById(R.id.outdoor_current_location_progressBar);
		mOutdoorLocationListView.setOnItemClickListener(mOutdoorLocationsItemClickListener);
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		userCitiesDatabase = new UserCitiesDatabase();
		MetricsTracker.trackPage(TrackPageConstants.OUTDOOR_LOCATIONS);
	}
	
	@Override
	public void onResume() {
		showCurrentCityVisibility();
		if (!selectedItemHashtable.isEmpty()) selectedItemHashtable.clear();// remove selected items
		OutdoorController.getInstance().setCurrentCityAreaIdReceivedListener(this);
		
		List<String> userCities = userCitiesDatabase.getAllCities();
		List<OutdoorCityInfo> outdoorCityInfoList = AddOutdoorLocationHelper.getSortedUserSelectedCitiesInfo(userCities) ;
		userSelectedCitiesAdapter = new UserSelectedCitiesAdapter(getActivity(), R.layout.simple_list_item, outdoorCityInfoList);
		
		mOutdoorLocationListView.setAdapter(userSelectedCitiesAdapter);
		addAreaIdToCityList();
		super.onResume();
	}
	
	@Override
	public void onPause() {
		OutdoorController.getInstance().removeCurrentCityAreaIdReceivedListener();
		super.onPause();
	}
	
	private void showCurrentCityVisibility() {
		if (LocationUtils.getCurrentLocationAreaId().isEmpty()) {
			currentLocation.setClickable(false);
			currentLocation.setEnabled(false);
			if (OutdoorController.getInstance().isProviderEnabled()) {
				searchingLoctionProgress.setVisibility(View.VISIBLE);
			}
			stratCurrentCityAreaIdTask();
			
		} else {
			currentLocation.setClickable(true);
			currentLocation.setEnabled(true);
			currentLocation.setChecked(LocationUtils.isCurrentLocationEnabled());
			currentLocation.setOnCheckedChangeListener(this);
			searchingLoctionProgress.setVisibility(View.GONE);
		}
	}
	
	private void stratCurrentCityAreaIdTask() {
		String lat = LocationUtils.getCurrentLocationLat();
		String lon = LocationUtils.getCurrentLocationLon();
		if (!lat.isEmpty() && !lon.isEmpty()) {
			searchingLoctionProgress.setVisibility(View.VISIBLE);
			try {
				OutdoorController.getInstance().startGetAreaIDTask(Double.parseDouble(lon), Double.parseDouble(lat));
			} catch (NumberFormatException e) {
				ALog.e(ALog.ERROR, "OutdoorLocationFragment$showCurrentCityVisibility: " + "Error: " + e.getMessage());
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if(result.hasResolution()) {
			Log.i(TAG, "onConnectionFailed#hasResolution");
		} else {
//			showErrorDialog(result.getErrorCode());
			Log.i(TAG, "onConnectionFailed#noResolution");
		}
	}
	
	private void addAreaIdToCityList() {
		//Added out side some time cursor does not have data
		OutdoorManager.getInstance().clearCitiesList();
		List<String> userCities = userCitiesDatabase.getAllCities();
		List<OutdoorCityInfo> outdoorCityInfoList = AddOutdoorLocationHelper.getSortedUserSelectedCitiesInfo(userCities) ;
		
		if (!outdoorCityInfoList.isEmpty()) {
			
			for (OutdoorCityInfo outdoorCityInfo : outdoorCityInfoList) {
				String key = AddOutdoorLocationHelper.getCityKeyWithRespectDataProvider(outdoorCityInfo);
				OutdoorManager.getInstance().addAreaIDToUsersList(key);
			}
		}
		
		//If current location get, add into outdoor location info list
		if (LocationUtils.isCurrentLocationEnabled()
				&& !LocationUtils.getCurrentLocationAreaId().isEmpty()) {
			OutdoorManager.getInstance().addCurrentCityAreaIDToUsersList(LocationUtils.getCurrentLocationAreaId());
		} 
	}
	
	@Override
	public void onConnected(Bundle arg0) {/**NOP*/}

	@Override
	public void onDisconnected() {/**NOP*/}
	
	private OnItemClickListener mOutdoorLocationsItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
			ImageView deleteSign = (ImageView) view.findViewById(R.id.list_item_delete);
			FontTextView delete = (FontTextView) view.findViewById(R.id.list_item_right_text);
			
			OutdoorCityInfo outdoorCityInfo = (OutdoorCityInfo) userSelectedCitiesAdapter.getItem(position);
			final String key = AddOutdoorLocationHelper.getCityKeyWithRespectDataProvider(outdoorCityInfo);
			
			if(delete.getVisibility() == View.GONE) {
				delete.setVisibility(View.VISIBLE);
				deleteSign.setImageResource(R.drawable.delete_t2b);
				selectedItemHashtable.put(key, true);
			} else {
				delete.setVisibility(View.GONE);
				deleteSign.setImageResource(R.drawable.delete_l2r);
				selectedItemHashtable.put(key, false);
			}
		}
	};

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView.getId() == R.id.btn_current_location) {
			LocationUtils.saveCurrentLocationEnabled(isChecked);
			//Update outdoor location info list;
			if (isChecked) {
				//For download outdoor AQI and weather detail, resetting lastUpdatedTime to zero
				OutdoorManager.getInstance().resetUpdatedTime();
				OutdoorManager.getInstance().addCurrentCityAreaIDToUsersList(LocationUtils.getCurrentLocationAreaId());
			} else {
				addAreaIdToCityList();
			}
		}
	}

	@Override
	public void areaIdReceived() {
		if (getActivity() == null) return;
		
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				showCurrentCityVisibility();
			}
		});
	}
	
	
	private class UserSelectedCitiesAdapter extends ArrayAdapter<OutdoorCityInfo> {

		private Context context;
		private List<OutdoorCityInfo> outdoorCityInfoList;
		
		public UserSelectedCitiesAdapter(Context context, int resource, List<OutdoorCityInfo> outdoorCityInfoList) {
			super(context, resource, outdoorCityInfoList);
			this.context = context;
			this.outdoorCityInfoList = outdoorCityInfoList;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.simple_list_item, null);

			OutdoorCityInfo info = outdoorCityInfoList.get(position);
			final String key = AddOutdoorLocationHelper.getCityKeyWithRespectDataProvider(info);
			ImageView deleteSign = (ImageView) view.findViewById(R.id.list_item_delete);
			FontTextView tvName = (FontTextView) view.findViewById(R.id.list_item_name);
			
			deleteSign.setVisibility(View.VISIBLE);
			
			String cityName = info.getCityName();

			if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")) {
				cityName = info.getCityNameCN();
			} else if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
				cityName = info.getCityNameTW();
			}

			//Replace first latter Capital and append US Embassy
			if( info.getDataProvider() == OutdoorDataProvider.US_EMBASSY.ordinal()) {
				StringBuilder builder = new StringBuilder() ;
				builder.append(cityName.substring(0,1).toUpperCase()).append(cityName.substring(1)) ;
				builder.append(" (").append(context.getString(R.string.us_embassy)).append(" )") ;

				cityName = builder.toString() ;
			}
			tvName.setText(cityName);
			
			tvName.setTag(key);
			
			FontTextView delete = (FontTextView) view.findViewById(R.id.list_item_right_text);
			
			if (selectedItemHashtable.containsKey(key) && selectedItemHashtable.get(key)) {
				delete.setVisibility(View.VISIBLE);
				deleteSign.setImageResource(R.drawable.delete_t2b);
			} else {
				delete.setVisibility(View.GONE);
				deleteSign.setImageResource(R.drawable.delete_l2r);
			}
			
			delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					OutdoorManager.getInstance().removeAreaIDFromUsersList(key);
					new UserCitiesDatabase().deleteCity(key);
					
					if (selectedItemHashtable.containsKey(key)) {
						selectedItemHashtable.remove(key);
					}
					List<String> userCities = userCitiesDatabase.getAllCities();
					List<OutdoorCityInfo> outdoorCityInfoList = AddOutdoorLocationHelper.getSortedUserSelectedCitiesInfo(userCities) ;
					userSelectedCitiesAdapter = new UserSelectedCitiesAdapter(getActivity(), R.layout.simple_list_item, outdoorCityInfoList);
					
					mOutdoorLocationListView.setAdapter(userSelectedCitiesAdapter);
					addAreaIdToCityList();
				}
			});

			return view;
		}
	}

}
