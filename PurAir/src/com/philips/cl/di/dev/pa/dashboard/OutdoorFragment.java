package com.philips.cl.di.dev.pa.dashboard;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.AirTutorialActivity;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter.DrawerEvent;
import com.philips.cl.di.dev.pa.dashboard.DrawerAdapter.DrawerEventListener;
import com.philips.cl.di.dev.pa.fragment.AlertDialogFragment;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkStateListener;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class OutdoorFragment extends BaseFragment implements OnClickListener, AlertDialogBtnInterface, DrawerEventListener, NetworkStateListener {
	
	private FontTextView cityName, updated,temp,aqi,aqiTitle,aqiSummary1,aqiSummary2;
	private ImageView aqiPointerCircle;
	private ImageView weatherIcon ;
	private LinearLayout takeATourPopup;
	private ImageView aqiCircleMeter ;
	
	private String[] cityNames = {"Beijing", "Shanghai", "Cheng Du"};
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ALog.i(ALog.DASHBOARD, "OutdoorFragment onActivityCreated");
		initViews(getView());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		NetworkReceiver.getInstance().addNetworkStateListener(this);
		DrawerAdapter.getInstance().addDrawerListener(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		NetworkReceiver.getInstance().removeNetworkStateListener(this);
		DrawerAdapter.getInstance().removeDrawerListener(this);
	}
	
	private void initViews(View view) {
		cityName = (FontTextView) view.findViewById(R.id.hf_outdoor_city);
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
		Bundle bundle = getArguments();

		if(bundle != null) {
			int position = bundle.getInt("position");
			ALog.i(ALog.DASHBOARD, "OutdoorFragment$initViews bundle " + position);
			
			String areaID = OutdoorManager.getInstance().getCitiesList().get(position);
			OutdoorCity city = OutdoorManager.getInstance().getCityData(areaID);
			if(city != null) {
				ALog.i(ALog.DASHBOARD, "OutdoorFragment$initViews city data " + city + " areaID " + areaID);
				updateUI(city, cityNames[position]);
			}
		} 
		
		if(((MainActivity)getActivity()).getVisits()<=3 && !((MainActivity)getActivity()).isTutorialPromptShown){
			takeATourPopup = (LinearLayout) view.findViewById(R.id.take_tour_prompt_drawer);			
			showTakeATourPopup();
			
			FontTextView takeATourText = (FontTextView) view.findViewById(R.id.lbl_take_tour);
			takeATourText.setOnClickListener(this);
			
			ImageButton takeATourCloseButton = (ImageButton) view.findViewById(R.id.btn_close_tour_layout);
			takeATourCloseButton.setOnClickListener(this);
		}
		
	}
	
	private void updateUI(OutdoorCity city, String outdoorCityName) {
		ALog.i(ALog.DASHBOARD, "UpdateUI");		
		
		cityName.setText(outdoorCityName);
		
		if(city.getOutdoorAQI() != null) {
			OutdoorAQI outdoorAQI = city.getOutdoorAQI();
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
		
		Animation aqiCircleRotateAnim = new RotateAnimation(0.0f, rotation, drawable.getMinimumWidth()/2, drawable.getMinimumHeight()/2);
		
	    aqiCircleRotateAnim.setDuration(2000);  
	    aqiCircleRotateAnim.setRepeatCount(0);     
	    aqiCircleRotateAnim.setFillAfter(true);
	 
	    aqiPointer.setAnimation(aqiCircleRotateAnim);
	}
	
	private void showTakeATourPopup() {
		takeATourPopup.setVisibility(View.VISIBLE);
		Animation bottomUp = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_up);
		takeATourPopup .startAnimation(bottomUp);
	}
	
	private void hideTakeATourPopup() {
		if (takeATourPopup != null) {
			takeATourPopup.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hf_outdoor_circle_pointer:
//			Bundle bundle = getArguments();
//			if (bundle != null) {
//				Intent intent = new Intent(getActivity(), OutdoorDetailsActivity.class);
//				intent.putExtras(bundle);
//				startActivity(intent);
//			}
			break;
			
		case R.id.lbl_take_tour:
			((MainActivity)getActivity()).isTutorialPromptShown = true;
			Intent intentOd = new Intent(getActivity(), AirTutorialActivity.class);
			startActivity(intentOd);
			hideTakeATourPopup();
			break;
			
		case R.id.btn_close_tour_layout:
			((MainActivity)getActivity()).isTutorialPromptShown = true;
			hideTakeATourPopup();
			showTutorialDialog();
			break;
		default:
			break;	
		}
	}
	
	private void showTutorialDialog() {
		AlertDialogFragment dialog = AlertDialogFragment.newInstance(R.string.alert_take_tour, R.string.alert_taketour_text, R.string.alert_take_tour, R.string.close);
		dialog.setOnClickListener(this);
		dialog.show(getActivity().getSupportFragmentManager(), "");
	}

	@Override
	public void onPositiveButtonClicked() {
		Intent intentOd = new Intent(getActivity(), AirTutorialActivity.class);
		startActivity(intentOd);
	}

	@Override
	public void onNegativeButtonClicked() {
		// NOP
	}

	@Override
	public void onDrawerEvent(DrawerEvent event, View drawerView) {
		switch (event) {
		case DRAWER_CLOSED:
			break;
		case DRAWER_OPENED:
			hideTakeATourPopup();
			break;
		default:
			break;
		}
	}

	@Override
	public void onConnected() {
		ALog.i(ALog.DASHBOARD, "OutdoorFragment$onConnected");
		OutdoorManager.getInstance().startCitiesTask();
	}

	@Override
	public void onDisconnected() {
		
	}
}
