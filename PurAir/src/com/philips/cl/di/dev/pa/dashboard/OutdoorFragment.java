package com.philips.cl.di.dev.pa.dashboard;

import java.util.Locale;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.OutdoorDetailsActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.util.LocationUtils;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class OutdoorFragment extends BaseFragment implements OnClickListener, OnPageChangeListener {
	
	private FontTextView cityName, updated, temp, aqi, aqiTitle, aqiSummary1, aqiSummary2, cityId;
	private ImageView aqiPointerCircle;
	private ImageView weatherIcon ;
	private RelativeLayout rootLayout;
	private ImageView aqiCircleMeter, myLocArrowImg ;
	private float prevRotation;
	
	private FontTextView lastUpdated;
	private FontTextView pmValue;
	private LinearLayout pmLayout;
	
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
		myLocArrowImg = (ImageView) view.findViewById(R.id.hf_my_loc_image);
		cityName = (FontTextView) view.findViewById(R.id.hf_outdoor_city);
		cityName.setSelected(true);
		//If Chinese language selected set font-type-face normal
		if( LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")
				|| LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
			cityName.setTypeface(Typeface.DEFAULT);
		}
		cityId  = (FontTextView) view.findViewById(R.id.hf_outdoor_city_id);
		updated = (FontTextView) view.findViewById(R.id.hf_outdoor_time_update);
		temp = (FontTextView) view.findViewById(R.id.hf_outdoor_temprature);
		aqi = (FontTextView) view.findViewById(R.id.hf_outdoor_aqi_reading);
		aqiTitle = (FontTextView) view.findViewById(R.id.hf_outdoor_aqi_title);
		//Remove as track ticket #1165
		aqiSummary1 = (FontTextView) view.findViewById(R.id.hf_outdoor_aqi_summary1);
		aqiSummary1.setVisibility(View.GONE);
		aqiSummary2 = (FontTextView) view.findViewById(R.id.hf_outdoor_aqi_summary2);
		aqiPointerCircle = (ImageView) view.findViewById(R.id.hf_outdoor_circle_pointer);
		aqiPointerCircle.setOnClickListener(this);
		aqiCircleMeter = (ImageView) view.findViewById(R.id.hf_outdoor_circle_meter);
		weatherIcon = (ImageView) view.findViewById(R.id.hf_outdoor_weather_image) ;
		lastUpdated = (FontTextView) view.findViewById(R.id.hf_outdoor_time_update_lb);
		pmValue=(FontTextView)view.findViewById(R.id.hf_outdoor_pm_value);
		pmLayout=(LinearLayout)view.findViewById(R.id.pm_layout);
		Bundle bundle = getArguments();

		if(bundle != null) {
			int position = bundle.getInt("position");
			ALog.i(ALog.DASHBOARD, "OutdoorFragment$initViews bundle " + position + " list size " + OutdoorManager.getInstance().getUsersCitiesList().size());
			if(OutdoorManager.getInstance().getUsersCitiesList().size() > 0) {
				String areaID = OutdoorManager.getInstance().getUsersCitiesList().get(position);
				OutdoorCity city = OutdoorManager.getInstance().getCityData(areaID);
				if(city != null && city.getOutdoorCityInfo() != null) {
					ALog.i(ALog.DASHBOARD, "OutdoorFragment$initViews city data " + city.getOutdoorCityInfo().getCityName() + " areaID " + areaID);
					ALog.i(ALog.DASHBOARD, "LanguageUtils.getLanguageForLocale(Locale.getDefault()); " + LanguageUtils.getLanguageForLocale(Locale.getDefault()));
					if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")) {
						updateUI(city, city.getOutdoorCityInfo().getCityNameCN(), areaID);
					} else if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
						updateUI(city, city.getOutdoorCityInfo().getCityNameTW(), areaID);
					}else {
						updateUI(city, city.getOutdoorCityInfo().getCityName(), areaID);
					}
				}
			}
		} 
		
	}
	
	private OutdoorAQI outdoorAQI ;
	private void updateUI(OutdoorCity city, String outdoorCityName, String areaID) {
		ALog.i(ALog.DASHBOARD, "UpdateUI");		
		
		if (LocationUtils.getCurrentLocationAreaId().equals(areaID) 
				&& LocationUtils.getCurrentLocationEnabled()) {
			myLocArrowImg.setVisibility(View.VISIBLE);
		} else {
			myLocArrowImg.setVisibility(View.GONE);
		}
		
		cityName.setText(outdoorCityName);
		cityId.setText(areaID);
		outdoorAQI = null;
		if(city.getOutdoorAQI() != null) {
			outdoorAQI = city.getOutdoorAQI();
			ALog.i(ALog.DASHBOARD, "OutdoorFragment$updateUI AQI " + city.getOutdoorAQI().getAQI());
			//Set outdoor background
			rootLayout.setBackgroundResource(OutdoorImage.valueOf(areaID, outdoorAQI.getAQI()));
			aqi.setText("" + outdoorAQI.getAQI());
			aqiTitle.setText(outdoorAQI.getAqiTitle());
			String outdoorAQISummary [] = outdoorAQI.getAqiSummary() ;
			if( outdoorAQISummary != null && outdoorAQISummary.length > 1 ) {
//				aqiSummary1.setText(outdoorAQI.getAqiSummary()[0]);
				aqiSummary2.setText(outdoorAQI.getAqiSummary()[1]);
			}
			aqiPointerCircle.setImageResource(outdoorAQI.getAqiPointerImageResId());
			Utils.rotateImageView(aqiPointerCircle, prevRotation, outdoorAQI.getAqiPointerRotaion());
			prevRotation = outdoorAQI.getAqiPointerRotaion();
			aqiCircleMeter.setVisibility(View.VISIBLE) ;
			pmLayout.setVisibility(View.VISIBLE);
			pmValue.setText(""+outdoorAQI.getPM25());
			lastUpdated.setVisibility(View.VISIBLE);
		}
		if(city.getOutdoorWeather() != null) {
			OutdoorWeather weather = city.getOutdoorWeather();
			updated.setText(weather.getUpdatedTime());
			weatherIcon.setImageResource(weather.getWeatherIcon());
			temp.setText("" + weather.getTemperature()+AppConstants.UNICODE_DEGREE);
			lastUpdated.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ALog.i(ALog.DASHBOARD, "OutdoorFragment$onCreateView");
		
		View view = inflater.inflate(R.layout.hf_outdoor_dashboard, null);
		return view;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hf_outdoor_circle_pointer:
			if (cityId.getText() == null || outdoorAQI == null) return;
//			if((OutdoorManager.getInstance().getAllCitiesList() != null) 
//					&& (OutdoorManager.getInstance().getAllCitiesList().size() < 100)){
				OutdoorManager.getInstance().startAllCitiesTask();
//			}
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
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		
	}

	@Override
	public void onPageSelected(int position) {
	}
}
