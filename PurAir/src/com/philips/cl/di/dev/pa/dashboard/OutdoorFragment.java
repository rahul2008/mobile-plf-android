package com.philips.cl.di.dev.pa.dashboard;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class OutdoorFragment extends BaseFragment {
	
	private static final String CITY = "city";
	private FontTextView cityName,updated,temp,aqi,aqiTitle,aqiSummary;
	private ImageView aqiPointerCircle;
	
	public static OutdoorFragment newInstance(OutdoorDto outdoorDto) {
		ALog.i(ALog.DASHBOARD, "OutdoorFragment$newInstance");
		OutdoorFragment fragment = new OutdoorFragment();
		if(outdoorDto != null) {
			Bundle args = new Bundle();
			args.putSerializable(CITY, outdoorDto);
			fragment.setArguments(args);
		}
		return fragment;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ALog.i(ALog.DASHBOARD, "OutdoorFragment onActivityCreated");
		initViews(getView());
	}
	
	private void initViews(View view) {
		OutdoorManager.getInstance().getOutdoorDashboardData(0);
		cityName = (FontTextView) view.findViewById(R.id.hf_outdoor_city);
		updated = (FontTextView) view.findViewById(R.id.hf_outdoor_time_update);
		temp = (FontTextView) view.findViewById(R.id.hf_outdoor_temprature);
		aqi = (FontTextView) view.findViewById(R.id.hf_outdoor_aqi_reading);
		aqiTitle = (FontTextView) view.findViewById(R.id.hf_outdoor_aqi_title);
		aqiSummary = (FontTextView) view.findViewById(R.id.hf_outdoor_aqi_summary);
		aqiPointerCircle = (ImageView) view.findViewById(R.id.hf_outdoor_circle_pointer);
		Bundle bundle = getArguments();

		if(bundle != null) {
			ALog.i(ALog.DASHBOARD, "OutdoorFragment$onCreateView");
			OutdoorDto city= (OutdoorDto) getArguments().getSerializable(
					CITY);
			updateUI(city);
		} 
	}
	
	private void updateUI(OutdoorDto city) {
		ALog.i(ALog.DASHBOARD, "UpdateUI");		
		cityName.setText(city.getCityName());		
		updated.setText(city.getUpdatedTime());		
		temp.setText(city.getTemperature());		
		aqi.setText(city.getAqi());
		aqiTitle.setText(city.getAqiTitle());
		aqiSummary.setText(city.getAqiSummary());
		aqiPointerCircle.setImageResource(city.getAqiPointerImageResId());
		aqiPointerCircle.invalidate();
		setRotationAnimation(aqiPointerCircle, city.getAqiPointerRotaion());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ALog.i(ALog.DASHBOARD, "OutdoorFragment$onCreateView");
		
		View view = inflater.inflate(R.layout.hf_outdoor_dashboard, null);
		return view;
	}
	
	private void setRotationAnimation(ImageView aqiPointer, float rotation) {
		Drawable drawable = aqiPointer.getDrawable();
		ALog.i(ALog.DASHBOARD, "OutdoorFragment$getRotationAnimation rotation " + rotation + " aqiPointer.getWidth()/2 " + (aqiPointer.getWidth()/2) + " drawable " + drawable.getMinimumHeight());
		
		Animation aqiCircleRotateAnim = new RotateAnimation(0.0f, rotation, drawable.getMinimumWidth()/2, drawable.getMinimumHeight()/2);
		
	    aqiCircleRotateAnim.setDuration(2000);  
	    aqiCircleRotateAnim.setRepeatCount(0);     
	    aqiCircleRotateAnim.setFillAfter(true);
	 
	    aqiPointer.setAnimation(aqiCircleRotateAnim);
	}
}
