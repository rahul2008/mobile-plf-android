package com.philips.cl.di.dev.pa.fragment;

import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.HomeFragment;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.dashboard.OutdoorController;
import com.philips.cl.di.dev.pa.dashboard.OutdoorController.CurrentCityAreaIdReceivedListener;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.outdoorlocations.AddOutdoorLocationActivity;
import com.philips.cl.di.dev.pa.outdoorlocations.AddOutdoorLocationHelper;
import com.philips.cl.di.dev.pa.outdoorlocations.OutdoorDataProvider;
import com.philips.cl.di.dev.pa.outdoorlocations.UpdateMyLocationsListener;
import com.philips.cl.di.dev.pa.outdoorlocations.UserCitiesDatabase;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.util.LocationUtils;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class OutdoorLocationsFragment extends BaseFragment implements ConnectionCallbacks,
	OnConnectionFailedListener, CurrentCityAreaIdReceivedListener {
	private static final String TAG = OutdoorLocationsFragment.class.getSimpleName();
	
	private boolean isGooglePlayServiceAvailable;
	private ListView mOutdoorLocationListView;
	private Hashtable<String, Boolean> selectedItemHashtable;
	private UserSelectedCitiesAdapter userSelectedCitiesAdapter;
	private UserCitiesDatabase userCitiesDatabase;
	private List<String> userCitiesId;
	private List<OutdoorCityInfo> outdoorCityInfoList;
	private FontTextView editTV;
	
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
		View view = inflater.inflate(R.layout.my_outdoor_locations_fragment, container, false);
		
		editTV = (FontTextView) view.findViewById(R.id.outdoor_location_edit_tv);
		editTV.setText(getString(R.string.edit));
		mOutdoorLocationListView = (ListView) view.findViewById(R.id.outdoor_locations_list);
		mOutdoorLocationListView.setOnItemClickListener(mOutdoorLocationsItemClickListener);
		editTV.setOnClickListener(locationOnClickListener);
		
		return view;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		userCitiesDatabase = new UserCitiesDatabase();
		
		userCitiesId = userCitiesDatabase.getAllCities();
		outdoorCityInfoList = AddOutdoorLocationHelper.getSortedUserSelectedCitiesInfo(userCitiesId) ;
		addCurrentLocationIntoList();
		userSelectedCitiesAdapter = new UserSelectedCitiesAdapter(getActivity(), R.layout.simple_list_item, outdoorCityInfoList);
		mOutdoorLocationListView.setAdapter(userSelectedCitiesAdapter);
		addAreaIdToCityList();
		
		MetricsTracker.trackPage(TrackPageConstants.OUTDOOR_LOCATIONS);
		HomeFragment homeFragment = (HomeFragment) getParentFragment();
		if (homeFragment != null) {
			homeFragment.setUpdateMyLocationsListner(new UpdateMyLocationsListener() {
				
				@Override
				public void onUpdate() {
					if (getString(R.string.done).equals(editTV.getText().toString())) {
						editTV.setText(getString(R.string.edit));
						setAdapter();
					}
				}
			});
		}
	}
	
	@Override
	public void onResume() {
		showCurrentCityVisibility();
		if (!selectedItemHashtable.isEmpty()) selectedItemHashtable.clear();// remove selected items
		OutdoorController.getInstance().setCurrentCityAreaIdReceivedListener(this);
		setAdapter();
		super.onResume();
	}
	
	@Override
	public void onPause() {
		OutdoorController.getInstance().removeCurrentCityAreaIdReceivedListener();
		super.onPause();
	}
	@Override
	public void onStop() {
		HomeFragment homeFragment = (HomeFragment) getParentFragment();
		if (homeFragment != null) {
			homeFragment.setUpdateMyLocationsListner(null);
		}
		super.onStop();
	}
	
	private void showCurrentCityVisibility() {
		if (LocationUtils.getCurrentLocationAreaId().isEmpty()) {
			startCurrentCityAreaIdTask();
		}
	}
	
	private void startCurrentCityAreaIdTask() {
		String lat = LocationUtils.getCurrentLocationLat();
		String lon = LocationUtils.getCurrentLocationLon();
		if (!lat.isEmpty() && !lon.isEmpty()) {
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
		OutdoorManager.getInstance().clearCitiesList();
		
		if (outdoorCityInfoList != null && !outdoorCityInfoList.isEmpty()) {
			
			for (OutdoorCityInfo outdoorCityInfo : outdoorCityInfoList) {
				String key = AddOutdoorLocationHelper.getCityKeyWithRespectDataProvider(outdoorCityInfo);
				OutdoorManager.getInstance().addAreaIDToUsersList(key);
			}
		}
		
		//Add current location 
		if (LocationUtils.isCurrentLocationEnabled()) {
			OutdoorManager.getInstance().addCurrentCityAreaIDToUsersList(LocationUtils.getCurrentLocationAreaId());
		} 
	}
	
	@Override
	public void onConnected(Bundle arg0) {/**NOP*/}

	@Override
	public void onDisconnected() {/**NOP*/}
	
	
	private void gotoPage(int position) {
		HomeFragment homeFragment = (HomeFragment) getParentFragment();
		if (homeFragment != null) {
			homeFragment.gotoOutdoorViewPage(position);
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
		private List<OutdoorCityInfo> outdoorCityInfoListAdapter;
		
		public UserSelectedCitiesAdapter(Context context, int resource, List<OutdoorCityInfo> outdoorCityInfoList) {
			super(context, resource, outdoorCityInfoList);
			this.context = context;
			this.outdoorCityInfoListAdapter = outdoorCityInfoList;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.simple_list_item, null);
			OutdoorCityInfo info = outdoorCityInfoListAdapter.get(position);
			final String key = AddOutdoorLocationHelper.getCityKeyWithRespectDataProvider(info);
			ImageView deleteSign = (ImageView) view.findViewById(R.id.list_item_delete);
			FontTextView tvName = (FontTextView) view.findViewById(R.id.list_item_name);
			FontTextView delete = (FontTextView) view.findViewById(R.id.list_item_right_text);
			deleteSign.setClickable(false);
		    deleteSign.setFocusable(false);
			
			String cityName = info.getCityName();
			if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")) {
				cityName = info.getCityNameCN();
			} else if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
				cityName = info.getCityNameTW();
			}
			//Replace first latter Capital and append US Embassy
			if( info.getDataProvider() == OutdoorDataProvider.US_EMBASSY.ordinal()) {
				StringBuilder builder = new StringBuilder(AddOutdoorLocationHelper.getFirstWordCapitalInSentence(cityName)) ;
				builder.append(" (").append(context.getString(R.string.us_embassy)).append(" )") ;

				cityName = builder.toString() ;
			}
			tvName.setText(cityName);
			
			tvName.setTag(key);
			
			if (position == 0) {
				deleteSign.setVisibility(View.VISIBLE);
				deleteSign.setImageResource(R.drawable.white_plus);
				return view;
			}
			
			if (position == 1) {
				deleteSign.setVisibility(View.VISIBLE);
				deleteSign.setImageResource(R.drawable.location_white);
				return view;
			}
			
			if (getString(R.string.edit).equals(editTV.getText().toString())) {
				deleteSign.setVisibility(View.GONE);
				delete.setVisibility(View.GONE);
			} else {
				deleteSign.setVisibility(View.VISIBLE);
				if (selectedItemHashtable.containsKey(key) && selectedItemHashtable.get(key)) {
					delete.setVisibility(View.VISIBLE);
					deleteSign.setImageResource(R.drawable.red_cross);
				} else {
					delete.setVisibility(View.GONE);
					deleteSign.setImageResource(R.drawable.white_cross);
				}
			}
			
			delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					deleteItem(key);
					reloadList();
				}
			});

			return view;
		}
	}
	
	private void deleteItem(String key) {
		OutdoorManager.getInstance().removeAreaIDFromUsersList(key);
		new UserCitiesDatabase().deleteCity(key);
		
		if (selectedItemHashtable.containsKey(key)) {
			selectedItemHashtable.remove(key);
		}
	}
	
	private void reloadList() {
		HomeFragment homeFragment = (HomeFragment) getParentFragment();
		if (homeFragment != null) {
			int count = 0;
			OutdoorManager.getInstance().processDataBaseInfo();
			List<String> myCitiesList = OutdoorManager.getInstance().getUsersCitiesList()  ;
			if(myCitiesList != null ) {
				count = myCitiesList.size() ;
			}
			OutdoorManager.getInstance().setOutdoorViewPagerCurrentPage(count);
			homeFragment.notifyOutdoorPager();
		}
	}
	
	private OnItemClickListener mOutdoorLocationsItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
			if (position == 0) {
				Intent	intent = new Intent(getActivity(), AddOutdoorLocationActivity.class);
				startActivity(intent);
				return;
			}
			if (position == 1) {
				if (getString(R.string.edit).equals(editTV.getText().toString())) {
					gotoPage(0);
				} 
				return;
			}
			OutdoorCityInfo outdoorCityInfo = (OutdoorCityInfo) userSelectedCitiesAdapter.getItem(position);
			final String key = AddOutdoorLocationHelper.getCityKeyWithRespectDataProvider(outdoorCityInfo);
			
			if (getString(R.string.done).equals(editTV.getText().toString())) {
				ImageView deleteSign = (ImageView) view.findViewById(R.id.list_item_delete);
				FontTextView delete = (FontTextView) view.findViewById(R.id.list_item_right_text);
//				selectedItemHashtable.clear();
				if(delete.getVisibility() == View.GONE) {
					delete.setVisibility(View.VISIBLE);
					deleteSign.setImageResource(R.drawable.red_cross);
					selectedItemHashtable.put(key, true);
				} else {
					delete.setVisibility(View.GONE);
					deleteSign.setImageResource(R.drawable.white_cross);
					selectedItemHashtable.put(key, false);
				}
//				setAdapter();
			} else {
				int index = OutdoorManager.getInstance().getUsersCitiesList().indexOf(key);
				gotoPage(index);
			}
		}
	};
	
	private OnClickListener locationOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.outdoor_location_edit_tv:
				if (getString(R.string.edit).equals(editTV.getText().toString())) {
					editTV.setText(getString(R.string.done));
				} else {
					editTV.setText(getString(R.string.edit));
				}
				setAdapter();
				break;
			default:
				break;
			}
			
		}
	};
	
	private void setAdapter() {
		userCitiesId = userCitiesDatabase.getAllCities();
		outdoorCityInfoList.clear();
		outdoorCityInfoList.addAll(AddOutdoorLocationHelper.getSortedUserSelectedCitiesInfo(userCitiesId)) ;
		addCurrentLocationIntoList();
		userSelectedCitiesAdapter.notifyDataSetChanged();
//		userSelectedCitiesAdapter = new UserSelectedCitiesAdapter(getActivity(), R.layout.simple_list_item, outdoorCityInfoList);
//		mOutdoorLocationListView.setAdapter(userSelectedCitiesAdapter);
		addAreaIdToCityList();
	}
	
	private void addCurrentLocationIntoList() {
		String addLoc = getString(R.string.add_outdoor_location);
		String currentLoc = getString(R.string.current_location);
		OutdoorCityInfo outdoorCityInfo = new OutdoorCityInfo(addLoc, addLoc, addLoc, 0f, 0f, "", 0);
		outdoorCityInfoList.add(0, outdoorCityInfo);
		outdoorCityInfo = new OutdoorCityInfo(currentLoc, currentLoc, currentLoc, 0f, 0f, LocationUtils.getCurrentLocationAreaId(), 0);
		outdoorCityInfoList.add(1, outdoorCityInfo);
		
	}

}
