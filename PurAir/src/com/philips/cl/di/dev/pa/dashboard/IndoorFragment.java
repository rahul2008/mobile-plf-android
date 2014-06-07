package com.philips.cl.di.dev.pa.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.IndoorDetailsActivity;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter.DrawerEvent;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter.DrawerEventListener;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.firmware.FirmwarePortInfo;
import com.philips.cl.di.dev.pa.firmware.FirmwarePortInfo.FirmwareState;
import com.philips.cl.di.dev.pa.fragment.AlertDialogFragment;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.fragment.SupportFragment;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager.PURIFIER_EVENT;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class IndoorFragment extends BaseFragment implements AirPurifierEventListener, OnClickListener, DrawerEventListener, AlertDialogBtnInterface {

	private RelativeLayout firmwareUpdatePopup;
	private int prevIndoorAqi;
	
	private FontTextView fanModeTxt ;
	private FontTextView filterStatusTxt ;
	private FontTextView aqiStatusTxt ;
	private FontTextView aqiSummaryTxt ;
	private FontTextView purifierNameTxt ;
	private ImageView aqiPointer ;
	private ImageView aqiMeter ;
	
	private FontTextView firmwareUpdateText;
	private FontTextView firmwareInfoButton;
	private ProgressBar firmwareProgress;
	private AlertDialogFragment dialogFragment;
	private AlertDialogFragment firmwareInfoDialog;
	
	private FirmwareState prevState;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.hf_indoor_dashboard, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fanModeTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_fan_mode);
		filterStatusTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_filter);
		aqiStatusTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_aqi_reading);
		aqiSummaryTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_aqi_summary);
		purifierNameTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_purifier_name);
		aqiPointer = (ImageView) getView().findViewById(R.id.hf_indoor_circle_pointer);
		aqiMeter = (ImageView) getView().findViewById(R.id.hf_indoor_circle_meter);
		initFirmwareUpdatePopup();
	}
	
	private void initFirmwareUpdatePopup() {
		firmwareUpdatePopup = (RelativeLayout) getView().findViewById(R.id.firmware_update_available);
		
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
		
		updateDashboard();
		hideFirmwareUpdatePopup();
	}

	private void hideFirmwareUpdatePopup() {
		firmwareUpdatePopup.setVisibility(View.GONE);
	}

	@Override
	public void onPause() {
		super.onPause();
		PurifierManager.getInstance().removeAirPurifierEventListener(this);
		DrawerAdapter.getInstance().removeDrawerListener(this);
	}

	private void updateDashboard() {
		Activity parent = this.getActivity();
		if (parent == null || !(parent instanceof MainActivity)) return;
		
		PurAirDevice purifier = ((MainActivity) parent).getCurrentPurifier();
		if (purifier == null) {
			purifierNameTxt.setText("");
			hideIndoorMeter();
			setRotationAnimation(aqiPointer, IndoorDashboardUtils.getAqiPointerRotation(0));
			return;
		}
		
		purifierNameTxt.setText(purifier.getName());
		
		AirPortInfo airPortInfo = purifier.getAirPortInfo();
		if (airPortInfo == null) return;
		
		int indoorAqi = airPortInfo.getIndoorAQI();
		fanModeTxt.setText(getString(IndoorDashboardUtils.getFanSpeedText(airPortInfo.getFanSpeed())));
		filterStatusTxt.setText(IndoorDashboardUtils.getFilterStatus(airPortInfo));
		aqiStatusTxt.setText(getString(IndoorDashboardUtils.getAqiTitle(indoorAqi)));
		aqiSummaryTxt.setText(getString(IndoorDashboardUtils.getAqiSummary(indoorAqi)));

		ALog.i(ALog.DASHBOARD, "currentPurifier.getConnectionState() " + purifier.getConnectionState());
		if(purifier.getConnectionState() == ConnectionState.DISCONNECTED) {
			hideIndoorMeter();
		} else {
			showIndoorMeter();
		}

		aqiPointer.setOnClickListener(this);
		aqiPointer.setImageResource(IndoorDashboardUtils.getAqiPointerBackgroundId(indoorAqi));
		aqiPointer.invalidate();
		if(prevIndoorAqi != indoorAqi) {
			setRotationAnimation(aqiPointer, IndoorDashboardUtils.getAqiPointerRotation(indoorAqi));
		}
		prevIndoorAqi = indoorAqi;
	}

	private void setRotationAnimation(ImageView aqiPointer, float rotation) {
		Drawable drawable = aqiPointer.getDrawable();
		ALog.i(ALog.DASHBOARD, "IndoorFragment$getRotationAnimation rotation " + rotation + " aqiPointer.getWidth()/2 " + (aqiPointer.getWidth()/2) + " drawable " + drawable.getMinimumHeight());
		
		Animation aqiCircleRotateAnim = new RotateAnimation(0.0f, rotation, drawable.getMinimumWidth()/2, drawable.getMinimumHeight()/2);
		
	    aqiCircleRotateAnim.setDuration(2000);
	    aqiCircleRotateAnim.setRepeatCount(0);
	    aqiCircleRotateAnim.setFillAfter(true);
	 
	    aqiPointer.setAnimation(aqiCircleRotateAnim);
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
					showIndoorMeter();
				}
			});
			break;
		case DISCONNECTED:
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideIndoorMeter();
				}
			});
			break;
		}
	}
	
	@Override
	public void onAirPurifierEventReceived() {
		if (getActivity() == null) return;
		
		PurAirDevice purifier = ((MainActivity) getActivity()).getCurrentPurifier();
		if(purifier == null || purifier.getConnectionState() == ConnectionState.DISCONNECTED || 
				(null != purifier.getFirmwarePortInfo() && FirmwareState.IDLE != purifier.getFirmwarePortInfo().getState())) {
			ALog.i(ALog.DASHBOARD, "onAirPurifierEventReceived ");
			return ;
		}
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				updateDashboard();
			}
		});
	}
	
	@Override
	public void onFirmwareEventReceived() {
		if (getActivity() == null || !(getActivity() instanceof MainActivity)) return;

		PurAirDevice purifier = ((MainActivity) getActivity()).getCurrentPurifier();
		if (purifier == null) return;
		final FirmwarePortInfo firmwarePortInfo = purifier.getFirmwarePortInfo();
		if(firmwarePortInfo == null) return;
		updateFirmwareUI(firmwarePortInfo);
	}
	
//	private void updateFirmwareUI(final boolean showFirmwareUI, final String status, final int progressVisibility, final int progress, final int infoVisibility) {
	private void updateFirmwareUI(FirmwarePortInfo firmwarePortInfo) {
		ALog.i(ALog.FIRMWARE, "updateFirmwareUI state " + firmwarePortInfo.getState());
		boolean showFirmwareUI = false;
		String status = "";
		int progressVisibility = View.INVISIBLE;
		int progress = 0;
		int infoVisibility = View.INVISIBLE;
		
		switch (firmwarePortInfo.getState()) {
		case DOWNLOADING:
			showFirmwareUI = true;
			status = getString(R.string.downloading_new_firmware);
			progressVisibility = View.VISIBLE;
			progress = firmwarePortInfo.getProgress();
			infoVisibility = View.VISIBLE;
			break;
		case CHECKING:
			//Follow through
		case PROGRAMMING:
			showFirmwareUI = true;
			status = getString(R.string.installing_firmware_text);
			infoVisibility = View.VISIBLE;
			break;

		case ERROR:
			if(null != dialogFragment && !dialogFragment.isVisible()) {
				dialogFragment = AlertDialogFragment.newInstance(R.string.download_failed, R.string.firmware_failed_msg, R.string.ok, R.string.help);
				dialogFragment.setOnClickListener(IndoorFragment.this);
				dialogFragment.show(getActivity().getSupportFragmentManager(), getTag());
			}
			return ;
			
		case IDLE:
			if(FirmwareState.PROGRAMMING == prevState) {
				showFirmwareUI = true;
				status = getString(R.string.firmware_install_success) + " " + firmwarePortInfo.getVersion();
			} else {
				
			}
			break;

		case CANCELING:
		case READY:
			//NOP : Should not occur in Mandatory firmware update
			return ;
			
		default:
			return ;
		}
		updateFirmwareUI(showFirmwareUI, status,progressVisibility,progress,infoVisibility) ; 
		prevState = firmwarePortInfo.getState() ;
	}
	private void updateFirmwareUI(final boolean showFirmwareUI, final String status, final int progressVisibility, final int progress, final int infoVisibility) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if(showFirmwareUI) {
					showFirmwareUpdatePopup();
					firmwareUpdateText.setText(status);
					firmwareProgress.setVisibility(progressVisibility);
					firmwareProgress.setProgress(progress);
					firmwareInfoButton.setVisibility(infoVisibility);
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lbl_firmware_update_available:
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
			Intent intent = new Intent(getActivity(), IndoorDetailsActivity.class);
			startActivity(intent);
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
		dialogFragment.dismiss();
	}

	@Override
	public void onNegativeButtonClicked() {
		showSupportFragment();
	}

	private void showSupportFragment() {
		dialogFragment.dismiss();
		((MainActivity) getActivity()).showFragment(new SupportFragment());
	}

	@Override
	public void onErrorOccurred(PURIFIER_EVENT purifierEvent) {
		
	}
}
