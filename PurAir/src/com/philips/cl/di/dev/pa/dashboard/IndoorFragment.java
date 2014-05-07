package com.philips.cl.di.dev.pa.dashboard;

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

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.IndoorDetailsActivity;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.firmware.FirmwarePortInfo;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.purifier.AirPurifierController;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class IndoorFragment extends BaseFragment implements AirPurifierEventListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.hf_indoor_dashboard, null);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		AirPurifierController.getInstance().addAirPurifierEventListener(this);
		if(PurifierManager.getInstance().getCurrentPurifier() != null) {
			updateDashboard(PurifierManager.getInstance().getCurrentPurifier().getAirPortInfo());
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		AirPurifierController.getInstance().removeAirPurifierEventListener(this);
	}

	private void updateDashboard(AirPortInfo airPortInfo) {
		if(airPortInfo == null) {
			return;
		}
		
		int indoorAqi = airPortInfo.getIndoorAQI();
		
		FontTextView fanModeTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_fan_mode);
		fanModeTxt.setText(getString(DashboardUtils.getFanSpeedText(airPortInfo.getFanSpeed())));

		FontTextView filterStatusTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_filter);
		filterStatusTxt.setText(DashboardUtils.getFilterStatus(airPortInfo));

		FontTextView aqiStatusTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_aqi_reading);
		aqiStatusTxt.setText(getString(DashboardUtils.getAqiTitle(indoorAqi)));
		
		FontTextView aqiSummaryTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_aqi_summary);
		aqiSummaryTxt.setText(getString(DashboardUtils.getAqiSummary(indoorAqi)));
		
		FontTextView purifierNameTxt = (FontTextView) getView().findViewById(R.id.hf_indoor_purifier_name);
		if (PurifierManager.getInstance().getCurrentPurifier() != null) {
			purifierNameTxt.setText(PurifierManager.getInstance().getCurrentPurifier().getName());
		}

		ImageView aqiPointer = (ImageView) getView().findViewById(R.id.hf_indoor_circle_pointer);

		aqiPointer.setOnClickListener(pointerImageClickListener);
		aqiPointer.setImageResource(DashboardUtils.getAqiPointerBackgroundId(indoorAqi));
		aqiPointer.invalidate();
		setRotationAnimation(aqiPointer, DashboardUtils.getAqiPointerRotation(indoorAqi));
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
			((MainActivity)getActivity()).isClickEvent = true ;
			
			Intent intent = new Intent(getActivity(), IndoorDetailsActivity.class);
			startActivity(intent);

		}
	};

}
