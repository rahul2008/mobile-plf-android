package com.philips.cl.di.dev.pa.dashboard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.outdoorlocations.AddOutdoorLocationHelper;
import com.philips.cl.di.dev.pa.outdoorlocations.DummyData;
import com.philips.cl.di.dev.pa.outdoorlocations.OutdoorDataProvider;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.util.LocationUtils;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class OutdoorFragment extends BaseFragment implements OnClickListener {

	private FontTextView cityNameTV, updated, temp, aqi, aqiTitle, aqiSummary1, aqiSummary2, cityId, tvOutdoorDataProvider;
	private ImageView aqiPointerCircle;
	private ImageView weatherIcon ;
	private RelativeLayout rootLayout;
	private ImageView aqiCircleMeter, myLocArrowImg ;
	private float prevRotation;

	private FontTextView lastUpdated;
	private FontTextView pmValue;
	private LinearLayout pmLayout;
	private FontTextView weatherText;
	
	private OutdoorAQI outdoorAQI ;
	private OutdoorWeather weather;
	
	private int outdoorDataProvider;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState); 
		MetricsTracker.trackPage("dashboard:outdoor");
		ALog.i(ALog.DASHBOARD, "OutdoorFragment onActivityCreated");
		initViews(getView());
	}

	private void initViews(View view) {
		rootLayout = (RelativeLayout) view.findViewById(R.id.hf_outdoor_root_lyt);
		myLocArrowImg = (ImageView) view.findViewById(R.id.hf_my_loc_image);
		cityNameTV = (FontTextView) view.findViewById(R.id.hf_outdoor_city);
		tvOutdoorDataProvider = (FontTextView) view.findViewById(R.id.hf_outdoor_location);
		cityNameTV.setSelected(true);
		//If Chinese language selected set font-type-face normal
		if( LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")
				|| LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
			cityNameTV.setTypeface(Typeface.DEFAULT);
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
		weatherText=(FontTextView)view.findViewById(R.id.hf_outdoor_weather_text);
		lastUpdated = (FontTextView) view.findViewById(R.id.hf_outdoor_time_update_lb);
		pmValue=(FontTextView)view.findViewById(R.id.hf_outdoor_pm_value);
		pmLayout=(LinearLayout)view.findViewById(R.id.pm_layout);
		Bundle bundle = getArguments();

		if(bundle != null) {
			int position = bundle.getInt("position");
			List<String> userCitiesList = OutdoorManager.getInstance().getUsersCitiesList();
			ALog.i(ALog.DASHBOARD, "OutdoorFragment$initViews bundle " + position + " list size " + userCitiesList.size());
			if(userCitiesList.size() > 0) {
				String areaID = userCitiesList.get(position);
				OutdoorCity city = OutdoorManager.getInstance().getCityData(areaID);
				if(city != null && city.getOutdoorCityInfo() != null) {
					String cityName = getCityWithRespectToLocale(city.getOutdoorCityInfo());
					updateUI(city, cityName, areaID);
				}
				if (areaID.isEmpty() && city == null) {
					updateUI(city, getString(R.string.locating), areaID);
				}
			}
		} 
	}
	
	private String getCityWithRespectToLocale(OutdoorCityInfo cityInfo) {
		String cityName = cityInfo.getCityName();
		if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")) {
			cityName = cityInfo.getCityNameCN();
		} else if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
			cityName = cityInfo.getCityNameTW();
		}
		return cityName;
	}

	@SuppressLint("SimpleDateFormat")
	private void updateUI(OutdoorCity city, String outdoorCityName, String areaID) {
		ALog.i(ALog.DASHBOARD, "UpdateUI");		
		cityNameTV.setText(AddOutdoorLocationHelper.getFirstWordCapitalInSentence(outdoorCityName));
		myLocArrowImg.setVisibility(View.VISIBLE);
		if (city == null) return;
		
		outdoorDataProvider = city.getOutdoorCityInfo().getDataProvider() ;
		if (!LocationUtils.getCurrentLocationAreaId().equals(areaID)) {
			myLocArrowImg.setVisibility(View.GONE);
		}
		
		if( outdoorDataProvider == OutdoorDataProvider.US_EMBASSY.ordinal()) {
			tvOutdoorDataProvider.setVisibility(View.VISIBLE) ;
			tvOutdoorDataProvider.setText(getString(R.string.us_embassy)) ;
		}
		
		cityId.setText(areaID);
		outdoorAQI = city.getOutdoorAQI();
		weather = city.getOutdoorWeather();
		
		addDummyDataForDemoMode(areaID);
		if(outdoorAQI != null) {
			//Set outdoor background
			rootLayout.setBackgroundResource(OutdoorImage.valueOf(areaID, outdoorAQI.getAQI()));
			aqi.setText("" + outdoorAQI.getAQI());
			aqiTitle.setText(outdoorAQI.getAqiTitle());
			String outdoorAQISummary [] = outdoorAQI.getAqiSummary() ;
			if( outdoorAQISummary != null && outdoorAQISummary.length > 1 ) {
				aqiSummary2.setText(outdoorAQI.getAqiSummary()[0]);
			}
			aqiPointerCircle.setImageResource(outdoorAQI.getAqiPointerImageResId());
			Utils.rotateImageView(aqiPointerCircle, prevRotation, outdoorAQI.getAqiPointerRotaion());
			prevRotation = outdoorAQI.getAqiPointerRotaion();
			aqiCircleMeter.setVisibility(View.VISIBLE) ;
			pmLayout.setVisibility(View.VISIBLE);
			pmValue.setText(""+outdoorAQI.getPM25());
			lastUpdated.setVisibility(View.VISIBLE);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm"); //201411121905
			SimpleDateFormat requiredDateFormat = new SimpleDateFormat("HH:mm");
			String date;
			try {
				Date dt=dateFormat.parse(outdoorAQI.getTimeStamp());
				date = requiredDateFormat.format(dt);
				updated.setText(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		if(weather != null) {
			weatherIcon.setImageResource(weather.getWeatherIcon());
			weatherText.setVisibility(View.GONE);
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
			OutdoorManager.getInstance().startAllCitiesTask();
			HomeFragment homeFragment = (HomeFragment) getParentFragment();
			if (homeFragment != null) {
				homeFragment.toggleOutdoorDetailFragment(cityNameTV.getText().toString(), outdoorAQI, outdoorDataProvider);
			}
			break;
		default:
			break;	
		}
	}

	//Dummy data for demo mode, if actual data not available
	@SuppressLint("SimpleDateFormat")
	private void addDummyDataForDemoMode(String areaID) {
		if (PurAirApplication.isDemoModeEnable() && outdoorAQI == null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			String date = dateFormat.format(Utils.getCurrentChineseDate());
			int aqi = DummyData.getInstance().getAqi(areaID);
			int pm2Point5 = DummyData.getInstance().getPmTwoPointFive(aqi, areaID);
			int pm10 = 35;
			int so2 = 3;
			int no2 = 11;
			String time = "201411121905"; //This time is not used, added this to avoid null value in OutdoorAQI object.
			outdoorAQI = new OutdoorAQI(pm2Point5, aqi, pm10, so2, no2, areaID, time);
			int temprature = 6;
			int humidity = 16;
			int weatherIconId = 0;
			weather = new OutdoorWeather(temprature, humidity, weatherIconId, areaID, date);
		}
	}
}
