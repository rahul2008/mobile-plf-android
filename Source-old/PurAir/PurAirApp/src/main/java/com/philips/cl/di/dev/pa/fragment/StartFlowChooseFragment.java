package com.philips.cl.di.dev.pa.fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.WifiPort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.GPSLocation;
import com.philips.cl.di.dev.pa.dashboard.OutdoorController;
import com.philips.cl.di.dev.pa.ews.EWSActivity;
import com.philips.cl.di.dev.pa.ews.EWSWifiManager;
import com.philips.cl.di.dev.pa.ews.SetupDialogFactory;
import com.philips.cl.di.dev.pa.fragment.StartFlowDialogFragment.StartFlowListener;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.FontTextView;

import java.util.ArrayList;
import java.util.List;

public class StartFlowChooseFragment extends BaseFragment implements
OnClickListener, StartFlowListener, DiscoveryEventListener, OnItemClickListener {

	private Button mBtnNewPurifier;
	private ProgressBar searchingPurifierProgressBar;
	private ListView discoveredPurifierListView;
	private AirPurifier selectedPurifier;
	private ArrayAdapter<String> appSelectorAdapter;
	private ArrayList<String> listItemsArrayList;
	private List<? extends DICommAppliance> appItems;
	private ImageView seperatorupImgView;
	private ImageView seperatordownImgView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.start_flow_choose_fragment, container, false);
		
		ImageButton backButton = (ImageButton) view.findViewById(R.id.heading_back_imgbtn);
		backButton.setVisibility(View.VISIBLE);
		FontTextView heading=(FontTextView) view.findViewById(R.id.heading_name_tv);
		heading.setText(getString(R.string.add_new_purifier));
		ImageButton closeButton = (ImageButton) view.findViewById(R.id.heading_close_imgbtn);
		closeButton.setVisibility(View.VISIBLE);
		closeButton.setOnClickListener(this);
		mBtnNewPurifier = (Button) view.findViewById(R.id.start_flow_choose_btn_connect_new);
		searchingPurifierProgressBar = (ProgressBar) view.findViewById(R.id.start_flow_choose_progressBar);
		discoveredPurifierListView  = (ListView) view.findViewById(R.id.start_flow_choose_listView);
		seperatorupImgView = (ImageView) view.findViewById(R.id.start_flow_choose_seperator_up);
		seperatordownImgView = (ImageView) view.findViewById(R.id.start_flow_choose_seperator_down);

		mBtnNewPurifier.setOnClickListener(this);
		discoveredPurifierListView.setOnItemClickListener(this);
		backButton.setOnClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (!GPSLocation.getInstance().isLocationEnabled()) {
			// If GPS is not enabled, request user to
			// enable location services here.
			showLocationServiceTurnedOffDialog();
		}
		MetricsTracker.trackPage(TrackPageConstants.ADD_PURIFIER);
	}

	private void showLocationServiceTurnedOffDialog() {
		Bundle mBundle = new Bundle();
		StartFlowDialogFragment mDialog;
		try {
			mBundle.clear();
			mDialog = new StartFlowDialogFragment();
			mDialog.setListener(this);
			mBundle.putInt(StartFlowDialogFragment.DIALOG_NUMBER, 
					StartFlowDialogFragment.LOCATION_SERVICES_TURNED_OFF);
			mDialog.setArguments(mBundle);
			mDialog.show(getFragmentManager(), "start_flow_dialog");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		OutdoorController.getInstance().setLocationProvider();
		showDiscoveredPurifier();
	}

	@Override
	public void onPause() {
		super.onPause();
		clearDiscoveredPurifierObject();
	}

	private void startEWS() {
		Intent intent = new Intent(getActivity(), EWSActivity.class);
		getActivity().startActivity(intent);
		getFragmentManager().popBackStackImmediate();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_flow_choose_btn_connect_new:
			startEWS();
			break;
		case R.id.heading_back_imgbtn:
		case R.id.heading_close_imgbtn:
			MainActivity activity = (MainActivity) getActivity();
			if (activity != null) {
				activity.onBackPressed();
			}
			break;
		default:
			break;
		}
	}

	private void showAlertDialog(String title, String message) {
		if (getActivity() == null)	return;
		try {
			FragmentTransaction fragTransaction = getActivity()
					.getSupportFragmentManager().beginTransaction();

			Fragment prevFrag = getActivity().getSupportFragmentManager()
					.findFragmentByTag("no_purifier_found");
			if (prevFrag != null) {
				fragTransaction.remove(prevFrag);
			}

			fragTransaction.add(DownloadAlerDialogFragement.newInstance(title, message),
					"no_purifier_found").commitAllowingStateLoss();
		} catch (IllegalStateException e) {
			ALog.e(ALog.ERROR, "Error: " + e.getMessage());
		}
	}

	private CountDownTimer connectTimer = new CountDownTimer(30000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
		}

		@Override
		public void onFinish() {
			showErrorOnConnectPurifier();
		}
	};

	private void showErrorOnConnectPurifier() {
		SetupDialogFactory.getInstance(getActivity()).dismissSignalStrength();
        MetricsTracker.trackActionTechnicalError(getString(R.string.purifier_add_fail_title));
		showAlertDialog(getString(R.string.purifier_add_fail_title),
				getString(R.string.purifier_add_fail_msg));
	}

	private void stopSSIDTimer() {
		if (connectTimer != null) {
			connectTimer.cancel();
		}
	}

	private void getWifiDetails() {
		ALog.i(ALog.MANAGE_PUR, "gettWifiDetails");

		final WifiPort wifiPort = selectedPurifier.getWifiPort();
		wifiPort.addPortListener(new DICommPortListener() {
            
            @Override
            public void onPortUpdate(DICommPort<?> port) {
                stopSSIDTimer();
                onSuccessfullyConnected();
                port.removePortListener(this);
            }

            @Override
            public void onPortError(DICommPort<?> port, Error error, String errorData) {
            	port.removePortListener(this);
            }
        });
		// TODO DIComm Refactor - See if key exchange retries are necessary
		wifiPort.getPortProperties();
	}

	private void onSuccessfullyConnected() {
		SetupDialogFactory.getInstance(getActivity()).dismissSignalStrength();
		Location location = OutdoorController.getInstance().getCurrentLocation();
		if (selectedPurifier != null) {
			clearDiscoveredPurifierObject();//Clear adapter object
			selectedPurifier.setConnectionState(ConnectionState.CONNECTED_LOCALLY);
			selectedPurifier.getNetworkNode().setHomeSsid(EWSWifiManager.getSsidOfSupplicantNetwork());
			if (location != null) {
				ALog.i(ALog.MANAGE_PUR, 
						"Add purifier: Purifier Current city lat: " + location.getLatitude() + "; long:" + location.getLongitude());
				selectedPurifier.setLatitude(String.valueOf(location.getLatitude()));
				selectedPurifier.setLongitude(String.valueOf(location.getLongitude()));
			}

			AirPurifierManager.getInstance().setCurrentAppliance(selectedPurifier);
			
			CongratulationFragment congratulationFragment = new CongratulationFragment();
			Bundle bundle = new Bundle();
			bundle.putBoolean(AppConstants.SHOW_HEADING, true);
			congratulationFragment.setArguments(bundle);

			((MainActivity) getActivity()).showFragment(congratulationFragment);

			((DiscoveryManager<AirPurifier>)DiscoveryManager.getInstance()).insertApplianceToDatabase(selectedPurifier);
		} else {
			showAlertDialog(getString(R.string.purifier_add_fail_title),
					getString(R.string.purifier_add_fail_msg));
		}
	}

	@Override
	public void onPurifierSelect(AirPurifier purifier) {
		SetupDialogFactory.getInstance(getActivity()).cleanUp();
		selectedPurifier = purifier;
		SetupDialogFactory.getInstance(getActivity()).dismissSignalStrength();
		SetupDialogFactory.getInstance(getActivity()).getDialog(SetupDialogFactory.CHECK_SIGNAL_STRENGTH).show();
		getWifiDetails();
	}
	
	@Override
	public void noWifiTurnOnClicked(DialogFragment dialog) {/**NOP*/}

	@Override
	public void noInternetTurnOnClicked(DialogFragment dialog) {/**NOP*/}

	@Override
	public void locationServiceAllowClicked(DialogFragment dialog) {/**NOP*/}

	@Override
	public void locationServiceTurnOnClicked(DialogFragment dialog) {/**NOP*/}

	@Override
	public void dialogCancelClicked(DialogFragment dialog) {/**NOP*/}

	private void showDiscoveredPurifier() {
		final DiscoveryManager discoveryManager = DiscoveryManager.getInstance();
		discoveryManager.addDiscoveryEventListener(this);
		appItems = discoveryManager.getNewAppliancesDiscovered();
		listItemsArrayList = new ArrayList<String>();

		for (int i = 0; i < appItems.size(); i++) {
			listItemsArrayList.add(appItems.get(i).getName());
		}

		showSearchingPurifierProgressBar(listItemsArrayList);

		appSelectorAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.single_item, R.id.single_list_item_name, listItemsArrayList);
		discoveredPurifierListView.setAdapter(appSelectorAdapter);

	}

	private void showSearchingPurifierProgressBar(ArrayList<String> listItems) {
		if (listItems.isEmpty()) {
			searchingPurifierProgressBar.setVisibility(View.VISIBLE);
			seperatorupImgView.setVisibility(View.GONE);
			seperatordownImgView.setVisibility(View.GONE);
		} else {
			searchingPurifierProgressBar.setVisibility(View.GONE);
			seperatorupImgView.setVisibility(View.VISIBLE);
			seperatordownImgView.setVisibility(View.VISIBLE);
		}
	}

	private void clearDiscoveredPurifierObject() {
		DiscoveryManager.getInstance().removeDiscoverEventListener(this);
		appSelectorAdapter = null;
		listItemsArrayList = null;
	}

	@Override
	public void onDiscoveredAppliancesListChanged() {

		if (getActivity() == null) return;
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (appSelectorAdapter == null || listItemsArrayList == null) return;
				appItems = DiscoveryManager.getInstance().getNewAppliancesDiscovered();
				if (!listItemsArrayList.isEmpty()) {
					listItemsArrayList.clear();
				}
				for (int i = 0; i < appItems.size(); i++) {
					listItemsArrayList.add(appItems.get(i).getName());
				}

				showSearchingPurifierProgressBar(listItemsArrayList);
				appSelectorAdapter.notifyDataSetChanged();
			}
		});
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		AirPurifier currentPurifier = (AirPurifier) appItems.get(position);
		onPurifierSelect(currentPurifier);
	}
}
