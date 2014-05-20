package com.philips.cl.di.dev.pa.fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
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
import com.mobeta.android.dslv.DragSortListView;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.util.Utils;

public class OutdoorLocationsFragment extends BaseFragment implements ConnectionCallbacks, OnConnectionFailedListener{
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
		isGooglePlayServiceAvailable = Utils.isGooglePlayServiceAvailable();
		Log.i(TAG, "isGooglePlayServiceAvailable " + isGooglePlayServiceAvailable);
		
		locationClient = new LocationClient(getActivity(), this, this);
		adapter = ((MainActivity) getActivity()).getOutdoorLocationsAdapter();
		super.onCreate(savedInstanceState);
	}
	
	//TODO : Get locations from OutdoorManager.
	
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
		mListView = (DragSortListView) view.findViewById(R.id.outdoor_locations_list);
		mListView.setDropListener(onDrop);
		mListView.setRemoveListener(onRemove);
		mListView.setDragScrollProfile(ssProfile);
		mListView.setAdapter(adapter);
		
		return view;
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
