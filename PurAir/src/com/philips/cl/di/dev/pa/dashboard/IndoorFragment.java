package com.philips.cl.di.dev.pa.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.IndoorDetailsActivity;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.firmware.FirmwarePortInfo;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.purifier.AirPurifierController;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class IndoorFragment extends BaseFragment implements AirPurifierEventListener {

	private FontTextView fanModeTxt;
	private FontTextView filterStatusTxt; 
	private FontTextView aqiStatusTxt;
	private FontTextView aqiSummaryTxt; 
	private FontTextView purifierNameTxt;
	//	private ImageView aqiPointer;
	private int indoorAQIValue;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.hf_indoor_dashboard, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//Start download purifier city AQI
		HomeOutdoorData.getInstance().startOutdoorAQITask();
	}

	@Override
	public void onResume() {
		super.onResume();
		AirPurifierController.getInstance().addAirPurifierEventListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		AirPurifierController.getInstance().removeAirPurifierEventListener(this);
	}

	private void updateDashboard(AirPortInfo airPurifierEvent) {
		int indoorAqi = airPurifierEvent.getIndoorAQI();
		indoorAQIValue = indoorAqi;
		fanModeTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_fan_mode);
		setFanSpeedText(fanModeTxt, airPurifierEvent.getFanSpeed());

		filterStatusTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_filter);
		setFilterStatus(filterStatusTxt, airPurifierEvent);

		aqiStatusTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_aqi_reading);
		aqiSummaryTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_aqi_summary);
		
		setAqiText(aqiStatusTxt, indoorAqi);
		purifierNameTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_purifier_name);
		if (PurifierManager.getInstance().getCurrentPurifier() != null) {
			purifierNameTxt.setText(PurifierManager.getInstance().getCurrentPurifier().getName());
		}

		ImageView aqiPointer = (ImageView) getView().findViewById(R.id.hf_indoor_circle_pointer);

		aqiPointer.setOnClickListener(pointerImageClickListener);
		setAqiPointerBackground(aqiPointer, indoorAqi);
		aqiPointer.invalidate();
		setAqiPointerRotation(aqiPointer, indoorAqi);
	}

	//TODO : Move all following methods to DashboardUtils.
	//TODO : Change strings
	private void setFanSpeedText(FontTextView fanMode, String fanSpeed) {
		if(fanSpeed.equals(AppConstants.FAN_SPEED_SILENT)) {
			fanMode.setText(R.string.silent);
		} else if (fanSpeed.equals(AppConstants.FAN_SPEED_AUTO)) {
			fanMode.setText(R.string.auto);
		} else if (fanSpeed.equals(AppConstants.FAN_SPEED_TURBO)) {
			fanMode.setText(R.string.turbo);
		} else if (fanSpeed.equals(AppConstants.FAN_SPEED_ONE)) {
			fanMode.setText(R.string.one);
		} else if (fanSpeed.equals(AppConstants.FAN_SPEED_TWO)) {
			fanMode.setText(R.string.one);
		} else if (fanSpeed.equals(AppConstants.FAN_SPEED_THREE)) {
			fanMode.setText(R.string.one);
		}
	}

	private void setFilterStatus(FontTextView filterStatus, AirPortInfo airPurifierEvent) {
		filterStatus.setText(Utils.getFilterStatusForDashboard(airPurifierEvent));
	}


	private void setAqiText(FontTextView aqiStatus, int indoorAQI) {
		ALog.i(ALog.DASHBOARD, "setAqiText indoorAqi " + indoorAQI);
		String [] aqiStatusAndCommentArray = Utils.getAQIStatusAndSummary(indoorAQI) ;
		if( aqiStatusAndCommentArray == null || aqiStatusAndCommentArray.length < 2 ) {
			return ;
		}
		aqiStatus.setText(aqiStatusAndCommentArray[0]);
		aqiSummaryTxt.setText(aqiStatusAndCommentArray[1]) ;

	}


	private void setAqiPointerBackground(ImageView aqiPointer, int indoorAQI) {
		ALog.i(ALog.DASHBOARD, "setAqiPointerBackground indoorAqi " + indoorAQI);
		if(indoorAQI >= 0 && indoorAQI <= 14) {
			ALog.i(ALog.DASHBOARD, "blue_circle_with_arrow_2x indoorAqi " + indoorAQI);
			aqiPointer.setImageResource(R.drawable.blue_circle_with_arrow_2x);
		} else if (indoorAQI > 14 && indoorAQI <= 23) {
			ALog.i(ALog.DASHBOARD, "light_pink_circle_arrow1_2x indoorAqi " + indoorAQI);
			aqiPointer.setImageResource(R.drawable.light_pink_circle_arrow1_2x);
		} else if (indoorAQI > 23 && indoorAQI <= 35) {
			ALog.i(ALog.DASHBOARD, "red_circle_arrow_2x indoorAqi " + indoorAQI);
			aqiPointer.setImageResource(R.drawable.red_circle_arrow_2x);
		} else if (indoorAQI > 35) {
			ALog.i(ALog.DASHBOARD, "light_red_circle_arrow_2x indoorAqi " + indoorAQI);
			aqiPointer.setImageResource(R.drawable.light_red_circle_arrow_2x );
		}
	}

	private void setAqiPointerRotation(ImageView aqiPointer, int indoorAQI) {
		float rotation = 0.0f;
		if(indoorAQI >= 0 && indoorAQI <= 14) {
			rotation = indoorAQI * 1.9f;
		} else if (indoorAQI > 14 && indoorAQI <= 23) {
			indoorAQI -= 14;
			rotation = 27.0f + (indoorAQI * 3.25f);
		} else if (indoorAQI > 23 && indoorAQI <= 35) {
			indoorAQI -= 23;
			rotation = 56.0f + (indoorAQI * 2.33f);
		} else if (indoorAQI > 35) {
			indoorAQI -= 35;
			rotation = 86.0f + (indoorAQI * 1.0f);
			if(rotation > 302) {
				rotation = 302;
			}
		}
		ALog.i(ALog.DASHBOARD, "blue_circle_with_arrow_2x indoorAqi " + indoorAQI + " rotation " + rotation + " aqiPointer.getWidth()/2 " + aqiPointer.getWidth()/2);
		Animation aqiCircleRotateAnim = new RotateAnimation(0.0f, rotation, aqiPointer.getWidth()/2, aqiPointer.getWidth()/2);

		aqiCircleRotateAnim.setDuration(2000);  
		aqiCircleRotateAnim.setRepeatCount(0);     
		aqiCircleRotateAnim.setFillAfter(true);

		aqiPointer.setAnimation(aqiCircleRotateAnim);
	}

	@Override
	public void airPurifierEventReceived(final AirPortInfo airPurifierEvent) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				updateDashboard(airPurifierEvent);
			}
		});
	}

	@Override
	public void firmwareEventReceived(FirmwarePortInfo firmwarePortInfo) {
		//NOP
	}

	private OnClickListener pointerImageClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			MainActivity.isClickEvent = true ;
			Intent intent = new Intent(getActivity(), IndoorDetailsActivity.class);
			String indoorDashboardInfos[] = new String[5];
			indoorDashboardInfos[0] = fanModeTxt.getText().toString();
			indoorDashboardInfos[1] = filterStatusTxt.getText().toString();
			indoorDashboardInfos[2] = String.valueOf(indoorAQIValue);
			//			indoorDashboardInfos[3] = aqiStatusTxt.getText().toString();
			//			indoorDashboardInfos[4] = aqiSummaryTxt.getText().toString();
			intent.putExtra("indoor", indoorDashboardInfos);
			startActivity(intent);

		}
	};

}
