package com.philips.cl.di.dev.pa.dashboard;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.IndoorDashboardUtils.FanSpeed;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.datamodel.FirmwarePortInfo;
import com.philips.cl.di.dev.pa.datamodel.FirmwarePortInfo.FirmwareState;
import com.philips.cl.di.dev.pa.demo.DemoModeConstant;
import com.philips.cl.di.dev.pa.fragment.AboutFragment;
import com.philips.cl.di.dev.pa.fragment.AlertDialogFragment;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.fragment.SupportFragment;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;
import com.philips.cl.di.dev.pa.util.Coordinates;
import com.philips.cl.di.dev.pa.util.DashboardUtil;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.dicomm.communication.Error;

public class IndoorFragment extends BaseFragment implements AirPurifierEventListener, OnClickListener,
	AlertDialogBtnInterface {

	private RelativeLayout firmwareUpdatePopup;
	private int prevIndoorAqi;
	private float prevRotation = 0.0f;
	
	private FontTextView alartMessageTextView ;
	private FontTextView aqiStatusTxt ;
	private FontTextView aqiSummaryTxt ;
	private FontTextView purifierNameTxt ;
	private FontTextView purifierEui64Txt;
	private Button filters;
	private Button controls;
	
	private ImageView aqiPointer ;
	private ImageView aqiMeter ;
	private ImageButton infoImgBtn;
	
	private FontTextView firmwareUpdateText;
	private FontTextView firmwareInfoButton;
	private ProgressBar firmwareProgress;
	private AlertDialogFragment firmwareInfoDialog;
	private int position ;
	private ArrayList<Point> points;
	private ViewGroup dotContainer;
	private int prevFanSpeedResId;
	
	private int operationMode = -1;
	private int indexToHighLighted = -1;
	private int prevDotOnePosition = -1;
	private int prevDotTwoPosition = -1;
	private int prevDotThreePosition = -1;
	private int marginCenter;
	private int marginRadius;
	private boolean silentModeAnimationStarted = false;
	private static final int ANIMATION_DELAY = 120;//milli second
	private int animationDelay = ANIMATION_DELAY;
	private static final int FAN_SPEED_ONE = 1;
	private static final int FAN_SPEED_TWO = 2;
	private static final int FAN_SPEED_THREE = 3;
	private static final int FAN_SPEED_SILENT = 4;
	private static final int FAN_SPEED_TURBO = 5;
	private static final int ALPHA_LIGHT_INT = 255;
	private static final int ALPHA_DIM_INT = 50;
	private static final float ALPHA_LIGHT_FLOAT = 1.0f;
	private static final float ALPHA_DIM_FLOAT = .2f;
	
	private Handler handler = new Handler();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.hf_indoor_dashboard, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		MetricsTracker.trackPage(TrackPageConstants.DASHBOARD_INDOOR);
		marginCenter = (int) Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), 2); //margin 2F
		marginRadius = (int) Coordinates.getPxWithRespectToDip(PurAirApplication.getAppContext(), 6); //margin 6F
		
		String eui64 = "";
		if(getArguments() != null) {
			position = getArguments().getInt("position");
			AirPurifier purifier = DiscoveryManager.getInstance().getStoreDevices().get(position);
			if (purifier == null) return;
			
			eui64 = purifier.getNetworkNode().getCppId() ;
		}
		
		ALog.i(ALog.DASHBOARD, "IndoorFragmet$onActivityCreated position: " + position);
		dotContainer = (RelativeLayout) getView().findViewById(R.id.hf_indoor_circle_layt);
		aqiStatusTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_aqi_reading);
		aqiSummaryTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_aqi_summary);
		purifierNameTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_purifier_name);
		purifierNameTxt.setSelected(true);
		infoImgBtn = (ImageButton) getView().findViewById(R.id.connecting_info_img_btn);
		infoImgBtn.setOnClickListener(this);
		filters = (Button) getView().findViewById(R.id.filters);
		filters.setOnClickListener(this);
		controls = (Button) getView().findViewById(R.id.controls);
		controls.setOnClickListener(this);
		alartMessageTextView = (FontTextView) getView().findViewById(R.id.hf_indoor_dashboard_cover_missing_alart_tv);
		alartMessageTextView.setVisibility(View.GONE);
		
		if (AirPurifierManager.getInstance().getCurrentPurifier() != null) {
			purifierNameTxt.setText(AirPurifierManager.getInstance().getCurrentPurifier().getName());
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

		AirPurifierManager.getInstance().addAirPurifierEventListener(this);
		
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
		AirPurifierManager.getInstance().removeAirPurifierEventListener(this);
	}

	private void updateDashboardUI() {
		Activity parent = this.getActivity();
		if (parent == null || !(parent instanceof MainActivity)) return;
		
		AirPurifier purifier = ((MainActivity) parent).getCurrentPurifier();
		String tempEui64 = purifierEui64Txt.getText().toString();
		
		//For demo mode
		if (PurAirApplication.isDemoModeEnable()) {
			purifierNameTxt.setText(DemoModeConstant.DEMO);
		} else if (purifier == null || !purifier.getNetworkNode().getCppId().equals(tempEui64)) {
			purifier = DiscoveryManager.getInstance().getDeviceByEui64(tempEui64);
		}
		
		if (purifier == null) return;
		
		if(purifier.getFirmwarePort().getFirmwarePortInfo() == null || purifier.getFirmwarePort().getFirmwarePortInfo().getState() != FirmwareState.IDLE) {
			ALog.i(ALog.FIRMWARE, "IndoorFragment$updateDashboardUI hideFirmwareUpdatePopup ");
			hideFirmwareUpdatePopup();
		}
		
		purifierNameTxt.setText(purifier.getName());
		
		AirPortInfo airPortInfo = purifier.getAirPort().getAirPortInfo();
		if (airPortInfo == null) return;
		
		int indoorAqi = airPortInfo.getIndoorAQI();
		if (ConnectionState.DISCONNECTED == purifier.getNetworkNode().getConnectionState()) {
			hideFirmwareUpdatePopup();
			disconnect(getString(R.string.not_connected));
			aqiSummaryTxt.setText(AppConstants.EMPTY_STRING);
			alartMessageTextView.setVisibility(View.GONE);
		} else {
			controls.setVisibility(View.VISIBLE);
			filters.setVisibility(View.VISIBLE);
			infoImgBtn.setVisibility(View.GONE);
			DashboardAPL apl = IndoorDashboardUtils.getDashboardAPL(indoorAqi);
			aqiPointer.setImageResource(apl.getPointerBackground());
			
			aqiStatusTxt.setTextSize(20.0f);
			aqiStatusTxt.setText(getString(apl.getTitle()));
			aqiSummaryTxt.setText(getString(apl.getSummary()));
			
			if (showAlertErrorAirPort(airPortInfo, purifier.getName())) return;
			
			showIndoorMeter();
			
			aqiPointer.invalidate();
			if(prevIndoorAqi != indoorAqi) {
				Utils.rotateImageView(aqiPointer, prevRotation, apl.getPointerRotation());
			}
			prevRotation = apl.getPointerRotation();
			prevIndoorAqi = indoorAqi;
			drawWhiteDots();
			
			if(!airPortInfo.getPowerMode().equals(AppConstants.POWER_ON)) {
				handler.removeCallbacks(animateRunnable);
				prevFanSpeedResId = 0;
				resetWhiteDots();
				return;
			}
			FanSpeed fsd = getFanSpeed(airPortInfo.getActualFanSpeed());
			int resourceId = fsd.getFanSpeedTextResID();
			
			if (prevFanSpeedResId != resourceId ) {
				prevFanSpeedResId = resourceId;
				showWhiteDotsAsPerFanSpeed(resourceId);
			}
		}
	}
	
	public FanSpeed getFanSpeed(String actualFanSpeed) {
		if(actualFanSpeed == null || actualFanSpeed.isEmpty()) return null;
		if(actualFanSpeed.equalsIgnoreCase(AppConstants.FAN_SPEED_SILENT)) return FanSpeed.SILENT;
		if(actualFanSpeed.equalsIgnoreCase(AppConstants.FAN_SPEED_AUTO)) return FanSpeed.AUTO;
		if(actualFanSpeed.equalsIgnoreCase(AppConstants.FAN_SPEED_TURBO)) return FanSpeed.TURBO;
		if(actualFanSpeed.equalsIgnoreCase(AppConstants.FAN_SPEED_ONE)) return FanSpeed.ONE;
		if(actualFanSpeed.equalsIgnoreCase(AppConstants.FAN_SPEED_TWO)) return FanSpeed.TWO;
		if(actualFanSpeed.equalsIgnoreCase(AppConstants.FAN_SPEED_THREE)) return FanSpeed.THREE;
		return null;
	}
	
	private boolean showAlertErrorAirPort(AirPortInfo airPortInfo, String pName) {
		boolean errorMode = false;
		String powerMode = airPortInfo.getPowerMode();
		if (AppConstants.POWER_STATUS_C.equalsIgnoreCase(powerMode)) {
			errorMode = showErrorMessage(pName, 
					getString(R.string.front_panel_not_closed), getString(R.string.cover_open));
		} else if (AppConstants.POWER_STATUS_E.equalsIgnoreCase(powerMode)) {
			errorMode = showErrorMessage(pName, 
					getString(R.string.purifier_malfunctioning), getString(R.string.error_air_port));
		} else {
			alartMessageTextView.setVisibility(View.GONE);
		}
		return errorMode;
	}
	
	private boolean showErrorMessage(String purifierName, String message1, String message2) {
		alartMessageTextView.setVisibility(View.VISIBLE);
		alartMessageTextView.setText(purifierName + ": " + message1);
		disconnect(message2);
		return true;
	}
	
	private void disconnect(String message) {
		hideIndoorMeter();
		filters.setVisibility(View.INVISIBLE);
		controls.setVisibility(View.INVISIBLE);
		infoImgBtn.setVisibility(View.VISIBLE);
		aqiStatusTxt.setTextSize(16.0f);
		aqiPointer.setImageResource(R.drawable.grey_circle_2x);
		aqiStatusTxt.setText(message);
		prevRotation = 0.0f;
		removeWhiteDots();
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
		AirPurifier current = AirPurifierManager.getInstance().getCurrentPurifier();
		if (current == null) return;
		
		ALog.i(ALog.DASHBOARD, "Purifier connection state " + current.getNetworkNode().getConnectionState());
		switch (current.getNetworkNode().getConnectionState()) {
		case CONNECTED_LOCALLY:
		case CONNECTED_REMOTELY:
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updateDashboardUI();
				}
			});
			break;
		case DISCONNECTED:
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
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
		
		AirPurifier purifier = ((MainActivity) getActivity()).getCurrentPurifier();
		if(purifier == null || purifier.getNetworkNode().getConnectionState() == ConnectionState.DISCONNECTED) {
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

		AirPurifier purifier = ((MainActivity) getActivity()).getCurrentPurifier();
		if (purifier == null) return;
		final FirmwarePortInfo firmwarePortInfo = purifier.getFirmwarePort().getFirmwarePortInfo();
		if(firmwarePortInfo == null) return;

		updateFirmwareUI(purifier.getNetworkNode().getCppId(), firmwarePortInfo);
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
				ALog.e(ALog.FIRMWARE, "Error parsing firmware version " + "Error: " + e.getMessage());
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
		MainActivity activity = (MainActivity) getActivity();
		switch (v.getId()) {
		case R.id.controls:
			if (activity != null) {
				activity.showFragment(new DeviceControlFragment());
			}
			break;
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
			AirPurifier purifier = ((MainActivity) getActivity()).getCurrentPurifier();
			if (getActivity() != null && purifier != null) {
				HomeFragment homeFragment = (HomeFragment) getParentFragment();
				if (homeFragment != null) {
					homeFragment.toggleIndoorDetailFragment();
				}
//				Intent intent = new Intent(getActivity(), IndoorDetailsActivity.class);
//				startActivity(intent);
			} else {
				if (getActivity() != null) {
					Toast.makeText(getActivity(), 
							getString(R.string.purifier_not_connect_error), Toast.LENGTH_LONG).show();
				}
			}
			break;
		case R.id.connecting_info_img_btn:
			if (activity != null) {
				activity.showFragment(new AboutFragment());
			}
			break;
		case R.id.filters:
			if (activity != null) {
				activity.showFragment(new FilterStatusFragment());
			}
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
	public void onErrorOccurred(Error purifierEventError) {/**NOP*/}
	
	@Override
	public void onDestroy() {
		handler.removeCallbacks(animateRunnable);
		clearPrevPosition();
		super.onDestroy();
	}
	
	//White dot animation on the circle
	
	private void showWhiteDotsAsPerFanSpeed(int fanSpeedResID) {
		animationDelay = ANIMATION_DELAY;
		switch (fanSpeedResID) {
		case R.string.speed1:
			operationMode = FAN_SPEED_ONE;
			animationDelay = ANIMATION_DELAY;
			break;
		case R.string.speed2:
			operationMode = FAN_SPEED_TWO;
			animationDelay = ANIMATION_DELAY / 2;
			break;
		case R.string.speed3:
			operationMode = FAN_SPEED_THREE;
			animationDelay = ANIMATION_DELAY / 3;
			break;
		case R.string.silent:
			operationMode = FAN_SPEED_SILENT;
			animationDelay = ANIMATION_DELAY;
			break;
		case R.string.turbo:
			operationMode = FAN_SPEED_TURBO;
			animationDelay = ANIMATION_DELAY / 3;
			break;
		default:
			break;
		}
		
		resetWhiteDots();
		indexToHighLighted = -1;
		handler.removeCallbacks(animateRunnable);
		handler.post(animateRunnable);
	}
	
	private Runnable addDotRunnable = new Runnable() {
		
		@Override
		public void run() {
			handler.removeCallbacks(addDotRunnable);
			drawWhiteDots();
		}
	};
	
	private synchronized void drawWhiteDots() {
		ImageView lastWhiteDotDrawn = (ImageView) dotContainer.findViewWithTag(0);
		if (lastWhiteDotDrawn != null) return;
		// Get center of the circle
		Point imageCenter = new Point();
		imageCenter.x = aqiPointer.getLeft() + aqiPointer.getWidth() / 2;
		imageCenter.y = aqiPointer.getTop() + aqiPointer.getHeight() / 2;
		imageCenter.x = imageCenter.x - marginCenter;
		imageCenter.y = imageCenter.y - marginCenter;
		
		if (imageCenter.x < 1 && imageCenter.y < 1 ) {
			handler.removeCallbacks(addDotRunnable);
			handler.postDelayed(addDotRunnable, animationDelay);
			return; //Pointer image width and height zero
		}
		
		int radius = (aqiPointer.getHeight() / 2) - marginRadius;
		points = DashboardUtil.getCircularBoundary(imageCenter, radius, AppConstants.NUM_OFF_POINTS);
		if (points.isEmpty()) return;
		
		//Draw white dots
		for (int index = 0; index < AppConstants.NUM_OFF_POINTS; index++) {
			ImageView prevImage = (ImageView) dotContainer.findViewWithTag(index);
			if (prevImage == null && getActivity() != null) {
				ImageView whiteDot = new ImageView(getActivity());
				whiteDot.setImageResource(R.drawable.white_circle_small);
				DashboardUtil.setAplha(whiteDot, ALPHA_DIM_INT, ALPHA_DIM_FLOAT);
				RelativeLayout.LayoutParams lParam = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lParam.setMargins(points.get(index).x, points.get(index).y, 0, 0);
				whiteDot.setLayoutParams(lParam);
				whiteDot.setTag(index);
				dotContainer.addView(whiteDot);
			}
		}
	}
	
	private void resetWhiteDots() {
		silentModeAnimationStarted = false;
		clearPrevPosition();
		for (int tag = 0; tag < AppConstants.NUM_OFF_POINTS; tag++) {
			ImageView whiteDot = (ImageView) dotContainer.findViewWithTag(tag);
			if (whiteDot != null) {
				whiteDot.clearAnimation();
				DashboardUtil.setAplha(whiteDot, ALPHA_DIM_INT, ALPHA_DIM_FLOAT);
			}
		}
	}
	
	void removeWhiteDots() {
		clearAnimation();
		handler.removeCallbacks(animateRunnable);
		prevFanSpeedResId = 0;
		clearPrevPosition();
		for (int tag = 0; tag < AppConstants.NUM_OFF_POINTS; tag++) {
			ImageView whiteDot = (ImageView) dotContainer.findViewWithTag(tag);
			if (whiteDot != null) {
				whiteDot.clearAnimation();
				dotContainer.removeView(whiteDot);
			}
		}
	}
	
	private Runnable animateRunnable = new Runnable() {
		
		@Override
		public void run() {
			handler.removeCallbacks(animateRunnable);
			indexToHighLighted = 
					(indexToHighLighted + 1 == AppConstants.NUM_OFF_POINTS ? 0 : indexToHighLighted + 1);
			animateDotOnCircle(indexToHighLighted);
			handler.postDelayed(animateRunnable, animationDelay);
		}
	};
	
	private void clearAnimation() {
		for (int tag = 0; tag < AppConstants.NUM_OFF_POINTS; tag++) {
			ImageView whiteDot = (ImageView) dotContainer.findViewWithTag(tag);
			if (whiteDot != null) whiteDot.clearAnimation();
		}
	}
	
	private void animateDotOnCircle(final int position) {
		switch (operationMode) {
		//Fan speed auto can be any fan speed
		case FAN_SPEED_ONE: 
			animateWhiteDotsInFanSpeedOne(position);
			break;
		case FAN_SPEED_TWO: 
			animateWhiteDotsInFanSpeedTwo(position);
			break;
		case FAN_SPEED_THREE: 
			animateWhiteDotsInFanSpeedThree(position);
			break;
		case FAN_SPEED_TURBO: 
			animateWhiteDotsInFanSpeedTurbo(position);
			break;
		case FAN_SPEED_SILENT:
			if (!silentModeAnimationStarted) animateWhiteDotsInFanSpeedSilent();
			break;
		default:
			//NOP
			break;
		}
	}
	
	private int swapHighLightToDim(int currentPosition, int prevPosition) {
		highLightImageView(currentPosition);
		dimImageView(prevPosition);
		return currentPosition;
	}
	
	private void animateWhiteDotsInFanSpeedOne(int position) {
		prevDotOnePosition = swapHighLightToDim(position, prevDotOnePosition);
	}
	
	private void animateWhiteDotsInFanSpeedTwo(int position) {
		prevDotOnePosition = swapHighLightToDim(position, prevDotOnePosition);
		
		prevDotTwoPosition = 
				swapHighLightToDim((position + 12) % AppConstants.NUM_OFF_POINTS, prevDotTwoPosition);
	}
	
	private void animateWhiteDotsInFanSpeedThree(int position) {
		prevDotOnePosition = swapHighLightToDim(position, prevDotOnePosition);
		
		prevDotTwoPosition = 
				swapHighLightToDim((position + 8) % AppConstants.NUM_OFF_POINTS, prevDotTwoPosition);
		
		prevDotThreePosition = 
				swapHighLightToDim((position + 16) % AppConstants.NUM_OFF_POINTS, prevDotThreePosition);
	}
	
	private void animateWhiteDotsInFanSpeedSilent() {
		final Animation animationFadeIn = 
				AnimationUtils.loadAnimation(getActivity(), R.anim.fadein_dashboard_dot);
		final Animation animationFadeOut = 
				AnimationUtils.loadAnimation(getActivity(), R.anim.fadeout_dashboard_dot);

		for (int tag = 0; tag < AppConstants.NUM_OFF_POINTS; tag++) {
			ImageView whiteDot = (ImageView) dotContainer.findViewWithTag(tag);
			if (whiteDot != null) {
				DashboardUtil.setAplha(whiteDot, ALPHA_LIGHT_INT, ALPHA_LIGHT_FLOAT);
				silentModeAnimationStarted = true;
				if (tag % 2 == 0) {
					whiteDot.startAnimation(animationFadeOut);
				} else {
					whiteDot.startAnimation(animationFadeIn);
				}
			}
		}
	}
	
	private void animateWhiteDotsInFanSpeedTurbo(int position) {
		prevDotOnePosition = swapHighLightToDim(position, prevDotOnePosition);
		highLightImageView((position + 1) % AppConstants.NUM_OFF_POINTS);
		highLightImageView((position + 2) % AppConstants.NUM_OFF_POINTS);
		
		prevDotTwoPosition = 
				swapHighLightToDim((position + 8) % AppConstants.NUM_OFF_POINTS, prevDotTwoPosition);
		highLightImageView((position + 9) % AppConstants.NUM_OFF_POINTS);
		highLightImageView((position + 10) % AppConstants.NUM_OFF_POINTS);
		
		prevDotThreePosition = 
				swapHighLightToDim((position + 16) % AppConstants.NUM_OFF_POINTS, prevDotThreePosition);
		highLightImageView((position + 17) % AppConstants.NUM_OFF_POINTS);
		highLightImageView((position + 18) % AppConstants.NUM_OFF_POINTS);
	}
	
	private void highLightImageView(int position) {
		ImageView brightImage = (ImageView) dotContainer.findViewWithTag(position);
		DashboardUtil.setAplha(brightImage, ALPHA_LIGHT_INT, ALPHA_LIGHT_FLOAT);
	}
	
	private void dimImageView(int position) {
		if (position != -1) {
			ImageView dimImage = (ImageView) dotContainer.findViewWithTag(position);
			DashboardUtil.setAplha(dimImage, ALPHA_DIM_INT, ALPHA_DIM_FLOAT);
		}
	}
	
	private void clearPrevPosition() {
		prevDotOnePosition = -1;
		prevDotTwoPosition = -1;
		prevDotThreePosition = -1;
	}
}
