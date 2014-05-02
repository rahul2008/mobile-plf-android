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
	
	public static OutdoorFragment newInstance(OutdoorDto outdoorDto) {
		ALog.i(ALog.DASHBOARD, "OutdoorFragment$newInstance");
		OutdoorFragment fragment = new OutdoorFragment();
		if(outdoorDto != null) {
			Bundle args = new Bundle();
			args.putString("updated", outdoorDto.getUpdatedTime());
			args.putString("cityName", outdoorDto.getCityName());
			args.putString("temp", outdoorDto.getTemperature());
			args.putString("aqi", outdoorDto.getAqi());
			args.putString("icon", outdoorDto.getWeatherIcon());
			args.putString("aqiTitle", outdoorDto.getAqiTitle());
			args.putString("aqiSummary", outdoorDto.getAqiSummary());
			args.putInt("pointerImageResId", outdoorDto.getAqiPointerImageResId());
			args.putFloat("pointerRotation", outdoorDto.getAqiPointerRotaion());
			fragment.setArguments(args);
		}
		return fragment;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ALog.i(ALog.DASHBOARD, "OutdoorFragment onActivityCreated");
		updateUi(getView());
	}
	
	private void updateUi(View view) {
		FontTextView cityName = (FontTextView) view.findViewById(R.id.hf_outdoor_city);
		FontTextView updated = (FontTextView) view.findViewById(R.id.hf_outdoor_time_update);
		FontTextView temp = (FontTextView) view.findViewById(R.id.hf_outdoor_temprature);
		FontTextView aqi = (FontTextView) view.findViewById(R.id.hf_outdoor_aqi_reading);
		FontTextView aqiTitle = (FontTextView) view.findViewById(R.id.hf_outdoor_aqi_title);
		FontTextView aqiSummary = (FontTextView) view.findViewById(R.id.hf_outdoor_aqi_summary);
		ImageView aqiPointerCircle = (ImageView) view.findViewById(R.id.hf_outdoor_circle_pointer);
		
		OutdoorDto outdoorDto = OutdoorManager.getInstance().getOutdoorDashboardData(0);
		Bundle bundle = getArguments();
		
		if(bundle != null) {
			ALog.i(ALog.DASHBOARD, "OutdoorFragment$updateUi getArgs " + getArguments().getString("cityName"));
			cityName.setText(bundle.getString("cityName"));
			updated.setText(bundle.getString("updated"));
			temp.setText(bundle.getString("temp"));
			aqi.setText(bundle.getString("aqi"));
			aqiTitle.setText(bundle.getString("aqiTitle"));
			aqiSummary.setText(bundle.getString("aqiSummary"));
			aqiPointerCircle.setImageResource(bundle.getInt("pointerImageResId"));
			setRotationAnimation(aqiPointerCircle, bundle.getFloat("pointerRotation"));
		} else if (outdoorDto != null) {
			ALog.i(ALog.DASHBOARD, "OutdoorFragment$updateUi update from OutdoorDto");
			cityName.setText(outdoorDto.getCityName());
			updated.setText(outdoorDto.getUpdatedTime());
			temp.setText(outdoorDto.getTemperature());
			aqi.setText(outdoorDto.getAqi());
			aqiTitle.setText(outdoorDto.getAqiTitle());
			aqiSummary.setText(outdoorDto.getAqiSummary());
			aqiPointerCircle.setImageResource(outdoorDto.getAqiPointerImageResId());
			aqiPointerCircle.invalidate();
			setRotationAnimation(aqiPointerCircle, outdoorDto.getAqiPointerRotaion());
		}
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
