package com.philips.cl.di.dev.pa.pureairui.fragments;

import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.gson.GsonBuilder;
import com.mobeta.android.dslv.DragSortListView;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dto.CityDetails;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.interfaces.ServerResponseListener;
import com.philips.cl.di.dev.pa.network.TaskGetHttp;
import com.philips.cl.di.dev.pa.pureairui.MainActivity;

public class OutdoorLocationsFragment extends Fragment implements ServerResponseListener, ConnectionCallbacks, OnConnectionFailedListener{
	private static final String TAG = OutdoorLocationsFragment.class.getSimpleName();
	
	private boolean isGooglePlayServiceAvailable;
	
	private LocationClient locationClient;
	private Location currentLocation;	
	
	private DragSortListView mListView;
	private ArrayAdapter<String> adapter;
	
	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			Log.i(TAG, "onDrop from " + from + " to " + to);
			String item=adapter.getItem(from);

			adapter.notifyDataSetChanged();
			adapter.remove(item);
			adapter.insert(item, to);
		}
	};

	private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
		@Override
		public void remove(int which) {
			Log.i(TAG, "onRemove which " + which);
			adapter.remove(adapter.getItem(which));
		}
	};

	private DragSortListView.DragScrollProfile ssProfile = new DragSortListView.DragScrollProfile() {
		@Override
		public float getSpeed(float w, long t) {
			Log.i(TAG, "ssProfile$getSpeed w " + w + " t " + t);
			if (w > 0.8f) {
				return ((float) adapter.getCount()) / 0.001f;
			} else {
				return 10.0f * w;
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		isGooglePlayServiceAvailable = ((MainActivity) getActivity()).isGooglePlayServiceAvailable();
		Log.i(TAG, "isGooglePlayServiceAvailable " + isGooglePlayServiceAvailable);
		
		locationClient = new LocationClient(getActivity(), this, this);
		createCitiesList();
		adapter = ((MainActivity) getActivity()).getOutdoorLocationsAdapter();
		super.onCreate(savedInstanceState);
	}
	
	private void createCitiesList() {
		String citiesString = null;
		try {
			InputStream is = getResources().getAssets().open("cities.json");
			byte[] data = new byte[is.available()];
			is.read(data);
			is.close();
			citiesString = new String(data);
		} catch (IOException e) {
			Log.e(TAG, "JSON Parse ERROR while creating Cities list");
		}
//		Log.i(TAG, "citiesJson " + citiesString);
		CityDetails city = new GsonBuilder().create().fromJson(citiesString, CityDetails.class);
		SessionDto.getInstance().setCityDetails(city) ;
	}

	@Override
	public void onStart() {
		locationClient.connect();
		super.onStart();
	}
	
	@Override
	public void onStop() {
		locationClient.disconnect();
		super.onStop();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		View view = inflater.inflate(R.layout.outdoor_locations_fragment, container, false);
		startGetCitiesListTask();
		mListView = (DragSortListView) view.findViewById(R.id.outdoor_locations_list);
		mListView.setDropListener(onDrop);
		mListView.setRemoveListener(onRemove);
		mListView.setDragScrollProfile(ssProfile);
		mListView.setAdapter(adapter);
		
//		mListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				Toast.makeText(getActivity(), "item click", 0).show();
//				
//			}
//			
//		});
		
		return view;
	}
	
/*	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem item = menu.getItem(0);
		//item.setIcon(R.drawable.plus_blue);
		super.onCreateOptionsMenu(menu, inflater);
	}*/
	
	private void startGetCitiesListTask() {
		Log.i(TAG, "startGetCitiesListTask");
		if(SessionDto.getInstance().getCityDetails() == null) {
			Log.i(TAG, "start task");
			TaskGetHttp citiesList = new TaskGetHttp("ixuanwu.com.cn/app/citys.php", getActivity(), this);
			citiesList.start();
		}
	}
	
	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			System.out.println("msg: "+msg.what+":"+msg.arg1+":"+msg.arg2);
			if ( msg.what == 1 ) {
				Log.i(TAG, "OutdoorLocations " + SessionDto.getSessionDto().getCityDetails().getCities());
			}
		};
	};
	
	private void updateOutdoorAQI() {
		handler.sendEmptyMessage(1);
	}

	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
		Log.i(TAG, "respCode " + responseCode + " respData " + responseData);
		if (getActivity() != null) {
			if ( responseCode == 200 ) {
				CityDetails city = new GsonBuilder().create().fromJson(responseData, CityDetails.class);
				SessionDto.getInstance().setCityDetails(city) ;
				updateOutdoorAQI();
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 10000) {
			
		}
		super.onActivityResult(requestCode, resultCode, data);
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

	@Override
	public void onConnected(Bundle dataBundle) {
		if(locationClient == null) {
			Toast.makeText(getActivity(), "Could not retrieve location", Toast.LENGTH_SHORT).show();
			return;
		}
		currentLocation = locationClient.getLastLocation();
		if(currentLocation == null) {
			Toast.makeText(getActivity(), "Could not retrieve location", Toast.LENGTH_SHORT).show();
			return;
		}
		Log.i(TAG, "current location is " + (currentLocation == null));
		Log.i(TAG, "Currentlocation " + currentLocation.toString() + " lat " + currentLocation.getLatitude() + " long " + currentLocation.getLongitude());
	}

	@Override
	public void onDisconnected() {
		
	}
	
}
