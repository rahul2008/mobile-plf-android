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
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.activity.OutdoorDetailsActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class OutdoorFragment extends BaseFragment {
	
	private FontTextView cityName,updated,temp,aqi,aqiTitle,aqiSummary1,aqiSummary2;
	private ImageView aqiPointerCircle;
	private ImageView weatherIcon ;
	
	public static OutdoorFragment newInstance(OutdoorDto outdoorDto) {
		ALog.i(ALog.DASHBOARD, "OutdoorFragment$newInstance");
		OutdoorFragment fragment = new OutdoorFragment();
		if(outdoorDto != null) {
			Bundle args = new Bundle();
			args.putSerializable(AppConstants.KEY_CITY, outdoorDto);
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
		aqiSummary1 = (FontTextView) view.findViewById(R.id.hf_outdoor_aqi_summary1);
		aqiSummary2 = (FontTextView) view.findViewById(R.id.hf_outdoor_aqi_summary2);
		aqiPointerCircle = (ImageView) view.findViewById(R.id.hf_outdoor_circle_pointer);
		weatherIcon = (ImageView) view.findViewById(R.id.hf_outdoor_weather_image) ;
		Bundle bundle = getArguments();

		if(bundle != null) {
			ALog.i(ALog.DASHBOARD, "OutdoorFragment$onCreateView");
			OutdoorDto city= (OutdoorDto) getArguments().getSerializable(AppConstants.KEY_CITY);
			updateUI(city);
		} 
	}
	
	private void updateUI(OutdoorDto outdoorDto) {
		ALog.i(ALog.DASHBOARD, "UpdateUI");		
		cityName.setText(outdoorDto.getCityName());
		if( outdoorDto.getUpdatedTime() != null && !outdoorDto.getUpdatedTime().isEmpty()) {
			String [] splitDateandTime = outdoorDto.getUpdatedTime().split(" ") ;
			if( splitDateandTime.length > 1 ) {
				// Updating only time - Ignoring the date
				updated.setText(splitDateandTime[1]);
			}			
		}
				
		temp.setText(outdoorDto.getTemperature()+AppConstants.UNICODE_DEGREE);		
		aqi.setText(outdoorDto.getAqi());
		aqiTitle.setText(outdoorDto.getAqiTitle());
		String outdoorAQISummary [] = outdoorDto.getAqiSummary() ;
		if( outdoorAQISummary != null && outdoorAQISummary.length > 1 ) {
			aqiSummary1.setText(outdoorDto.getAqiSummary()[0]);
			aqiSummary2.setText(outdoorDto.getAqiSummary()[1]);
		}
		aqiPointerCircle.setImageResource(outdoorDto.getAqiPointerImageResId());
		aqiPointerCircle.setOnClickListener(pointerImageClickListener);
		aqiPointerCircle.invalidate();
		setOutdoorTemperatureImage(outdoorDto.getWeatherIcon(), "Yes") ;
		setRotationAnimation(aqiPointerCircle, outdoorDto.getAqiPointerRotaion());
	}
	
	//TODO : Move to dto
	private void setOutdoorTemperatureImage(String weatherDesc, String isDayTime) {
		if(getActivity() != null) {
		Drawable weatherImage = null;
		ALog.i(ALog.OUTDOOR_DETAILS, "setOutdoorTemperatureImage " + weatherDesc);
		if(weatherDesc == null || weatherDesc.equals("") || getActivity() == null) {
			return;
		}

		if(weatherDesc.compareToIgnoreCase(AppConstants.SUNNY) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.sunny_white);
		} else if (weatherDesc.compareToIgnoreCase(AppConstants.MIST) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.mist_white);
		} else if (weatherDesc.compareToIgnoreCase(AppConstants.CLOUDY) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.cloudy_white);
		} else if (weatherDesc.compareToIgnoreCase(AppConstants.PARTLY_CLOUDY) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.partly_cloudy_white);
		} else if (weatherDesc.compareToIgnoreCase(AppConstants.PARTLY_CLOUDY) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.partly_cloudy_night_white);
		} else if (weatherDesc.compareToIgnoreCase(AppConstants.CLEAR_SKIES) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.clear_sky_night_white);
		} else if (weatherDesc.compareToIgnoreCase(AppConstants.SNOW) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.snow_white);
		} else if (weatherDesc.compareToIgnoreCase(AppConstants.LIGHT_RAIN_SHOWER) == 0 
				|| weatherDesc.compareToIgnoreCase(AppConstants.LIGHT_DRIZZLE) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.light_rain_shower_white);
		} else if (weatherDesc.compareToIgnoreCase(AppConstants.PATCHY_LIGHT_RAIN_IN_AREA_WITH_THUNDER) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.light_rain_with_thunder_white);
		} else if (weatherDesc.compareToIgnoreCase(AppConstants.MODERATE_OR_HEAVY_RAIN_SHOWER) == 0 
				|| weatherDesc.compareToIgnoreCase(AppConstants.TORRENTIAL_RAIN_SHOWER) == 0 
				|| weatherDesc.compareToIgnoreCase(AppConstants.HEAVY_RAIN) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.heavy_rain_white);
		} else if (weatherDesc.compareToIgnoreCase(AppConstants.HEAVY_RAIN_AT_TIMES) == 0) {
			//TODO : Replace with proper icon. Icon not found, replacing with heavy raind
			weatherImage = getResources().getDrawable(R.drawable.heavy_rain_white);
		} else if (weatherDesc.compareToIgnoreCase(AppConstants.MODERATE_OR_HEAVY_RAIN_IN_AREA_WITH_THUNDER) == 0) {
			weatherImage = getResources().getDrawable(R.drawable.moderate_rain_with_thunder_white);
		} else if (weatherDesc.compareToIgnoreCase(AppConstants.CLEAR) == 0) {
			if(isDayTime.compareToIgnoreCase("Yes") == 0)
				weatherImage = getResources().getDrawable(R.drawable.sunny_white);
			else
				weatherImage = getResources().getDrawable(R.drawable.clear_sky_night_white);
		} else {
			weatherImage = getResources().getDrawable(R.drawable.light_rain_shower_white);
		}	

		weatherIcon.setImageDrawable(weatherImage);
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
	
	private OnClickListener pointerImageClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			((MainActivity)getActivity()).isClickEvent = true ;
			Bundle bundle = getArguments();
			if (bundle != null) {
				Intent intent = new Intent(getActivity(), OutdoorDetailsActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}
	};
}
