package com.philips.cl.di.dev.pa.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.IndoorDetailsActivity;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter.DrawerEvent;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter.DrawerEventListener;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.datamodel.FirmwarePortInfo;
import com.philips.cl.di.dev.pa.datamodel.FirmwarePortInfo.FirmwareState;
import com.philips.cl.di.dev.pa.demo.DemoModeConstant;
import com.philips.cl.di.dev.pa.fragment.AlertDialogFragment;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.fragment.SupportFragment;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager.PurifierEvent;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class IndoorFragment extends BaseFragment implements AirPurifierEventListener, OnClickListener,
	DrawerEventListener, AlertDialogBtnInterface {

	private RelativeLayout firmwareUpdatePopup;
	private int prevIndoorAqi;
	private float prevRotation = 0.0f;
	
	private FontTextView alartMessageTextView ;
	private FontTextView fanModeTxt ;
	private FontTextView filterStatusTxt ;
	private FontTextView aqiStatusTxt ;
	private FontTextView aqiSummaryTxt ;
	private FontTextView purifierNameTxt ;
	private FontTextView purifierEui64Txt;
	private ImageView aqiPointer ;
	private ImageView aqiMeter ;
	
	private FontTextView firmwareUpdateText;
	private FontTextView firmwareInfoButton;
	private ProgressBar firmwareProgress;
	private AlertDialogFragment firmwareInfoDialog;
	private int position ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.hf_indoor_dashboard, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String eui64 = "";
		
		if(getArguments() != null) {
			position = getArguments().getInt("position");
			PurAirDevice purifier = DiscoveryManager.getInstance().getStoreDevices().get(position);
			if (purifier == null) return;
			
			eui64 = purifier.getEui64() ;
		}
		
		ALog.i(ALog.DASHBOARD, "IndoorFragmet$onActivityCreated position: " + position);
		
		fanModeTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_fan_mode);
		filterStatusTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_filter);
		aqiStatusTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_aqi_reading);
		aqiSummaryTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_aqi_summary);
		purifierNameTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_purifier_name);
		purifierNameTxt.setSelected(true);
		
		alartMessageTextView = (FontTextView) getView().findViewById(R.id.hf_indoor_dashboard_cover_missing_alart_tv);
		alartMessageTextView.setVisibility(View.GONE);
		
		if (PurifierManager.getInstance().getCurrentPurifier() != null) {
			purifierNameTxt.setText(PurifierManager.getInstance().getCurrentPurifier().getName());
		}
		
		purifierEui64Txt = (FontTextView) getView().findViewById(R.id.hf_indoor_purifier_eui64);
		purifierEui64Txt.setText(eui64);

		aqiPointer = (ImageView) getView().findViewById(R.id.hf_indoor_circle_pointer);
		aqiPointer.setOnClickListener(this);
		aqiMeter = (ImageView) getView().findViewById(R.id.hf_indoor_circle_meter);

		initFirmwareUpdatePopup();
	}
	
	private void initFirmwareUpdatePopup() {
		firmwareUpdatePopup = (RelativeLayout) getView().findViewById(R.id.firmware_update_available);
		firmwareUpdatePopup.setOnClickListener(this);
		
		firmwareInfoButton = (FontTextView) getView().findViewById(R.id.lbl_firmware_info);
		firmwareInfoButton.setOnClickListener(this);

		firmwareUpdateText = (FontTextView) getView().findViewById(R.id.lbl_firmware_update_available);
		firmwareUpdateText.setOnClickListener(this);
		
		firmwareProgress = (ProgressBar) getView().findViewById(R.id.firmware_update_progress);
		firmwareProgress.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onResume() {
		super.onResume();

		PurifierManager.getInstance().addAirPurifierEventListener(this);
		
		DrawerAdapter.getInstance().addDrawerListener(this);
		
		updateDashboardUI();
		hideFirmwareUpdatePopup();
	}

	private void hideFirmwareUpdatePopup() {
		firmwareUpdatePopup.setVisibility(View.GONE);
		showFirmwareUI = false;
	}

	@Override
	public void onPause() {
		super.onPause();
		PurifierManager.getInstance().removeAirPurifierEventListener(this);
		DrawerAdapter.getInstance().removeDrawerListener(this);
	}

	private void updateDashboardUI() {
		Activity parent = this.getActivity();
		if (parent == null || !(parent instanceof MainActivity)) return;
		
		PurAirDevice purifier = ((MainActivity) parent).getCurrentPurifier();
		String tempEui64 = purifierEui64Txt.getText().toString();
		
		//For demo mode
		if (PurAirApplication.isDemoModeEnable()) {
			purifierNameTxt.setText(DemoModeConstant.DEMO);
		} else if (purifier == null || !purifier.getEui64().equals(tempEui64)) {
			purifier = DiscoveryManager.getInstance().getDeviceByEui64(tempEui64);
		}
		
		if (purifier == null) return;
		
		if(purifier.getFirmwarePortInfo() == null || purifier.getFirmwarePortInfo().getState() != FirmwareState.IDLE) {
			ALog.i(ALog.FIRMWARE, "IndoorFragment$updateDashboardUI hideFirmwareUpdatePopup ");
			hideFirmwareUpdatePopup();
		}
		
		purifierNameTxt.setText(purifier.getName());
		
		AirPortInfo airPortInfo = purifier.getAirPortInfo();
		if (airPortInfo == null) return;
		
		int indoorAqi = airPortInfo.getIndoorAQI();
		if (ConnectionState.DISCONNECTED == purifier.getConnectionState()) {
			hideFirmwareUpdatePopup();
			hideIndoorMeter();
			fanModeTxt.setText(getString(R.string.off));
			filterStatusTxt.setText(AppConstants.EMPTY_STRING);
			aqiStatusTxt.setTextSize(18.0f);
			aqiPointer.setImageResource(R.drawable.grey_circle_2x);
			aqiStatusTxt.setText(getString(R.string.not_connected));
			aqiSummaryTxt.setText(AppConstants.EMPTY_STRING);
			prevRotation = 0.0f;
			alartMessageTextView.setVisibility(View.GONE);

		} else {
			if(!airPortInfo.getPowerMode().equals(AppConstants.POWER_ON)) {
				fanModeTxt.setText(getString(R.string.off));
			}
			else {
				fanModeTxt.setText(getString(airPortInfo.getFanSpeed().getFanSpeedTextResID()));
			}
			
			DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(indoorAqi);
			
			filterStatusTxt.setText(IndoorDashboardUtils.getFilterStatus(airPortInfo));
			aqiPointer.setImageResource(apl.getPointerBackground());
			aqiStatusTxt.setTextSize(22.0f);
			aqiStatusTxt.setText(getString(apl.getTitle()));
			aqiSummaryTxt.setText(getString(apl.getSummary()));
			showIndoorMeter();
			showAlartErrorAirPort(airPortInfo, purifier.getName());
			

			ALog.i(ALog.DASHBOARD, "currentPurifier.getConnectionState() " + purifier.getConnectionState());
			
			aqiPointer.invalidate();
			if(prevIndoorAqi != indoorAqi) {
				Utils.rotateImageView(aqiPointer, prevRotation, apl.getPointerRotation());
			}
			prevRotation = apl.getPointerRotation();
			prevIndoorAqi = indoorAqi;
		}
	}
	
	private void showAlartErrorAirPort(AirPortInfo airPortInfo, String pName) {
		String powerMode = airPortInfo.getPowerMode();
		if (AppConstants.POWER_STATUS_C.equalsIgnoreCase(powerMode)) {
			alartMessageTextView.setVisibility(View.VISIBLE);
			alartMessageTextView.setText(pName + ": " + getString(R.string.front_panel_not_closed));
		} else if (AppConstants.POWER_STATUS_E.equalsIgnoreCase(powerMode)) {
			alartMessageTextView.setVisibility(View.VISIBLE);
			alartMessageTextView.setText(pName + ": " + getString(R.string.purifier_malfunctioning));
		} else {
			alartMessageTextView.setVisibility(View.GONE);
		}
	}

	private void showFirmwareUpdatePopup() {
		if(View.VISIBLE != firmwareUpdatePopup.getVisibility()) {
			firmwareUpdatePopup.setVisibility(View.VISIBLE);
		}
	}
	
	private void hideIndoorMeter() {
		aqiMeter.setVisibility(View.INVISIBLE);
	}
	
	private void showIndoorMeter() {
		aqiMeter.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onAirPurifierChanged() {
		if (getActivity() == null) return;
		
		ALog.i(ALog.DASHBOARD, "IndoorDashboard$onDiscoveredDevicesListChanged");
		PurAirDevice current = PurifierManager.getInstance().getCurrentPurifier();
		if (current == null) return;
		
		ALog.i(ALog.DASHBOARD, "Purifier connection state " + current.getConnectionState());
		switch (current.getConnectionState()) {
		case CONNECTED_LOCALLY:
		case CONNECTED_REMOTELY:
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
//					showIndoorMeter();
					updateDashboardUI();
				}
			});
			break;
		case DISCONNECTED:
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
//					hideIndoorMeter();
					updateDashboardUI();
				}
			});
			break;
		}
	}
	
	@Override
	public void onAirPurifierEventReceived() {
		if (getActivity() == null) return;
		
		ALog.i(ALog.DASHBOARD, "IndoorFragment$onAirPurifierEventReceived");
		
		PurAirDevice purifier = ((MainActivity) getActivity()).getCurrentPurifier();
		if(purifier == null || purifier.getConnectionState() == ConnectionState.DISCONNECTED) {
			ALog.i(ALog.DASHBOARD, "onAirPurifierEventReceived ");
			return ;
		}
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				updateDashboardUI();
			}
		});
	}
	
	@Override
	public void onFirmwareEventReceived() {
		if (getActivity() == null || !(getActivity() instanceof MainActivity)) return;

		ALog.i(ALog.DASHBOARD, "IndoorFragment$onFirmwareEventReceived");

		PurAirDevice purifier = ((MainActivity) getActivity()).getCurrentPurifier();
		if (purifier == null) return;
		final FirmwarePortInfo firmwarePortInfo = purifier.getFirmwarePortInfo();
		if(firmwarePortInfo == null) return;

		updateFirmwareUI(purifier.getEui64(), firmwarePortInfo);
	}
	
	private String status = "";
	private boolean showFirmwareUI = true;
	private boolean timerStarted;
	
	private CountDownTimer deviceSSIDTimer = new CountDownTimer(60000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
			ALog.i(ALog.SUBSCRIPTION, "Tick Tick tick " + millisUntilFinished);
		}

		@Override
		public void onFinish() {
			ALog.i(ALog.SUBSCRIPTION, "Firmware timer onFinish  " + (getActivity() != null));
			if(getActivity() != null && timerStarted) {
				MainActivity activity = (MainActivity) getActivity();
				activity.stopNormalMode();
				activity.startNormalMode();
			}
			timerStarted = false;
		}
	};
	
	private void updateFirmwareUI(String purifierEui64, FirmwarePortInfo firmwarePortInfo) {
		ALog.i(ALog.FIRMWARE, "updateFirmwareUI state " + firmwarePortInfo.getState());
		
		int progressVisibility = View.INVISIBLE;
		int progress = 0;
		int infoVisibility = View.INVISIBLE;
		// Added to check getState is null, in getState method some case return null
		if (firmwarePortInfo.getState() == null) return;
		
		switch (firmwarePortInfo.getState()) {
		case DOWNLOADING:
			showFirmwareUI = true;
			status = getString(R.string.downloading_new_firmware);
			progressVisibility = View.VISIBLE;
			progress = firmwarePortInfo.getProgress();
			infoVisibility = View.VISIBLE;
			
			if(!timerStarted) {
				deviceSSIDTimer.start();
				timerStarted = true;
			}
			break;
		case CHECKING:
			//Follow through
		case PROGRAMMING:
			showFirmwareUI = true;
			status = getString(R.string.installing_firmware_text);
			infoVisibility = View.VISIBLE;
			break;

		case ERROR:
			showFirmwareUI = false;
			return ;
			
		case IDLE:
//			showFirmwareUI = false;
			int prevFirmwareVersion = 0, currentFirmwareVersion = 0; 
			try {
				prevFirmwareVersion = Integer.parseInt(Utils.getFirmwareVersion(purifierEui64));
				currentFirmwareVersion = Integer.parseInt(firmwarePortInfo.getVersion());
			} catch (NumberFormatException e) {
				ALog.e(ALog.FIRMWARE, "Error parsing firmware version " + e.getMessage());
			}
			
			ALog.i(ALog.FIRMWARE, "IDLE prevVersion " + prevFirmwareVersion + " currentVersion " + currentFirmwareVersion);
			
			if(prevFirmwareVersion < currentFirmwareVersion) {
				ALog.i(ALog.FIRMWARE, "Show firmware installed");
				status = getString(R.string.firmware_update) + " " + currentFirmwareVersion;
				showFirmwareUI = true;
				infoVisibility = View.INVISIBLE;
			} else {
				showFirmwareUI = false;
			}
			
			Utils.saveFirmwareVersion(purifierEui64, firmwarePortInfo.getVersion());
			break;

		case CANCELING:
		case READY:
			//NOP : Should not occur in Mandatory firmware update
			return ;
			
		default:
			return ;
		}
		updateFirmwareUI(showFirmwareUI, status,progressVisibility,progress,infoVisibility) ; 
	}
	private void updateFirmwareUI(final boolean showFirmwareUI, final String status, final int progressVisibility, final int progress, final int infoVisibility) {
		ALog.i(ALog.DASHBOARD, "IndoorFragment$updateFirmwareUI showFirmwareUI " + showFirmwareUI);
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if(showFirmwareUI) {
					showFirmwareUpdatePopup();
					firmwareUpdateText.setText(status);
					firmwareProgress.setVisibility(progressVisibility);
					firmwareProgress.setProgress(progress);
					firmwareInfoButton.setVisibility(infoVisibility);
				} else {
					hideFirmwareUpdatePopup();
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.firmware_update_available:
			hideFirmwareUpdatePopup();
			break;
		case R.id.lbl_firmware_info:
			updateFirmwareUI(false, null, View.INVISIBLE, 0, View.INVISIBLE);
			
			firmwareInfoDialog = AlertDialogFragment.newInstance(R.string.firmware, R.string.firmware_download_info, R.string.ok);
			firmwareInfoDialog.show(getActivity().getSupportFragmentManager(), getTag());
			firmwareInfoDialog.setOnClickListener(new AlertDialogBtnInterface() {
				
				@Override
				public void onPositiveButtonClicked() {
					firmwareInfoDialog.dismiss();
				}
				
				@Override
				public void onNegativeButtonClicked() {
					//NOP
				}
			});
			break;
		case R.id.hf_indoor_circle_pointer:
			PurAirDevice purifier = ((MainActivity) getActivity()).getCurrentPurifier();
			if (getActivity() != null && purifier != null) {
				Intent intent = new Intent(getActivity(), IndoorDetailsActivity.class);
				startActivity(intent);
			} else {
				if (getActivity() != null) {
					Toast.makeText(getActivity(), 
							getString(R.string.purifier_not_connect_error), Toast.LENGTH_LONG).show();
				}
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onDrawerEvent(DrawerEvent event, View drawerView) {
		switch (event) {
		case DRAWER_CLOSED:
			break;
		case DRAWER_OPENED:
			hideFirmwareUpdatePopup();
			break;
		default:
			break;
		}
	}

	@Override
	public void onPositiveButtonClicked() {
		hideFirmwareUpdatePopup();
	}

	@Override
	public void onNegativeButtonClicked() {
		showSupportFragment();
	}

	private void showSupportFragment() {
		((MainActivity) getActivity()).showFragment(new SupportFragment());
	}

	@Override
	public void onErrorOccurred(PurifierEvent purifierEvent) {
		// NOP
	}

}
