package com.philips.cl.di.dev.pa.dashboard;

import java.util.Locale;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.OutdoorDetailsActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class OutdoorFragment extends BaseFragment implements OnClickListener, OnPageChangeListener {
	
	private FontTextView cityName, updated,temp,aqi,aqiTitle,aqiSummary1,aqiSummary2, cityId;
	private ImageView aqiPointerCircle;
	private ImageView weatherIcon ;
	private RelativeLayout rootLayout;
	private ImageView aqiCircleMeter ;
	
	private FontTextView lastUpdated;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ALog.i(ALog.DASHBOARD, "OutdoorFragment onActivityCreated");
		initViews(getView());
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	private void initViews(View view) {
		rootLayout = (RelativeLayout) view.findViewById(R.id.hf_outdoor_root_lyt);
		cityName = (FontTextView) view.findViewById(R.id.hf_outdoor_city);
		cityId  = (FontTextView) view.findViewById(R.id.hf_outdoor_city_id);
		updated = (FontTextView) view.findViewById(R.id.hf_outdoor_time_update);
		temp = (FontTextView) view.findViewById(R.id.hf_outdoor_temprature);
		aqi = (FontTextView) view.findViewById(R.id.hf_outdoor_aqi_reading);
		aqiTitle = (FontTextView) view.findViewById(R.id.hf_outdoor_aqi_title);
		aqiSummary1 = (FontTextView) view.findViewById(R.id.hf_outdoor_aqi_summary1);
		aqiSummary2 = (FontTextView) view.findViewById(R.id.hf_outdoor_aqi_summary2);
		aqiPointerCircle = (ImageView) view.findViewById(R.id.hf_outdoor_circle_pointer);
		aqiPointerCircle.setOnClickListener(this);
		aqiCircleMeter = (ImageView) view.findViewById(R.id.hf_outdoor_circle_meter);
		weatherIcon = (ImageView) view.findViewById(R.id.hf_outdoor_weather_image) ;
		lastUpdated = (FontTextView) view.findViewById(R.id.hf_outdoor_time_update_lb);
		Bundle bundle = getArguments();

		if(bundle != null) {
			int position = bundle.getInt("position");
			ALog.i(ALog.DASHBOARD, "OutdoorFragment$initViews bundle " + position + " list size " + OutdoorManager.getInstance().getCitiesList().size());
			if(OutdoorManager.getInstance().getCitiesList().size() > 0) {
				String areaID = OutdoorManager.getInstance().getCitiesList().get(position);
				OutdoorCity city = OutdoorManager.getInstance().getCityData(areaID);
				if(city != null) {
					ALog.i(ALog.DASHBOARD, "OutdoorFragment$initViews city data " + city + " areaID " + areaID);
					ALog.i(ALog.DASHBOARD, "LanguageUtils.getLanguageForLocale(Locale.getDefault()); " + LanguageUtils.getLanguageForLocale(Locale.getDefault()));
					if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")) {
						updateUI(city, city.getOutdoorCityInfo().getCityNameCN(), areaID);
					} else {
						updateUI(city, city.getOutdoorCityInfo().getCityName(), areaID);
					}
				}
			}
		} 
		
	}
	
	private OutdoorAQI outdoorAQI ;
	private void updateUI(OutdoorCity city, String outdoorCityName, String areaID) {
		ALog.i(ALog.DASHBOARD, "UpdateUI");		
		
		cityName.setText(outdoorCityName);
		cityId.setText(areaID);
		lastUpdated.setVisibility(View.VISIBLE);
		if(city.getOutdoorAQI() != null) {
			outdoorAQI = city.getOutdoorAQI();
			ALog.i(ALog.DASHBOARD, "OutdoorFragment$updateUI AQI " + city.getOutdoorAQI().getAQI());
			//Set outdoor background
			rootLayout.setBackgroundResource(OutdoorImage.valueOf(areaID, outdoorAQI.getAQI()));
			aqi.setText("" + outdoorAQI.getAQI());
			aqiTitle.setText(outdoorAQI.getAqiTitle());
			String outdoorAQISummary [] = outdoorAQI.getAqiSummary() ;
			if( outdoorAQISummary != null && outdoorAQISummary.length > 1 ) {
				aqiSummary1.setText(outdoorAQI.getAqiSummary()[0]);
				aqiSummary2.setText(outdoorAQI.getAqiSummary()[1]);
			}
			aqiPointerCircle.setImageResource(outdoorAQI.getAqiPointerImageResId());
			setRotationAnimation(aqiPointerCircle, outdoorAQI.getAqiPointerRotaion());
			aqiCircleMeter.setVisibility(View.VISIBLE) ;
		}
		if(city.getOutdoorWeather() != null) {
			OutdoorWeather weather = city.getOutdoorWeather();
			updated.setText(weather.getUpdatedTime());
			weatherIcon.setImageResource(weather.getWeatherIcon());
			temp.setText("" + weather.getTemperature()+AppConstants.UNICODE_DEGREE);
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
		
		Animation aqiCircleRotateAnim = new RotateAnimation(0.0f, rotation, drawable.getMinimumWidth() / (float) 2, drawable.getMinimumHeight() / (float) 2);
		
	    aqiCircleRotateAnim.setDuration(2000);  
	    aqiCircleRotateAnim.setRepeatCount(0);     
	    aqiCircleRotateAnim.setFillAfter(true);
	 
	    aqiPointer.setAnimation(aqiCircleRotateAnim);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hf_outdoor_circle_pointer:
			if (cityId.getText() == null) return;
			Intent intent = new Intent(getActivity(), OutdoorDetailsActivity.class);
			intent.putExtra(AppConstants.OUTDOOR_CITY_NAME, cityName.getText().toString());
			intent.putExtra(AppConstants.OUTDOOR_AQI, outdoorAQI) ;
			startActivity(intent);
			break;
		default:
			break;	
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int state) {
		ALog.i(ALog.TEMP, "OutdoorFragment$onPageScrollStateChanged " + state);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		
	}

	@Override
	public void onPageSelected(int position) {
		ALog.i(ALog.TEMP, "OutdoorFragment$onPageSelected " + position);
	}
}
