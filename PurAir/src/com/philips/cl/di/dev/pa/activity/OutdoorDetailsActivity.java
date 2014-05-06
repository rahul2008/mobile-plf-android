package com.philips.cl.di.dev.pa.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.OutdoorDto;
import com.philips.cl.di.dev.pa.datamodel.OutdoorAQIEventDto;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.fragment.OutdoorAQIExplainedDialogFragment;
import com.philips.cl.di.dev.pa.purifier.TaskGetHttp;
import com.philips.cl.di.dev.pa.purifier.TaskGetWeatherData;
import com.philips.cl.di.dev.pa.purifier.TaskGetWeatherData.WeatherDataListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Coordinates;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.GraphConst;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.dev.pa.view.GraphView;
import com.philips.cl.di.dev.pa.view.WeatherReportLayout;

public class OutdoorDetailsActivity extends ActionBarActivity 
	implements OnClickListener, ServerResponseListener, WeatherDataListener {

	private GoogleMap mMap;
	private LinearLayout graphLayout, wetherForcastLayout;
	private HorizontalScrollView wetherScrollView;
	private FontTextView lastDayBtn, lastWeekBtn, lastFourWeekBtn;
	private FontTextView aqiValue, location, summaryTitle, summary, pm1, pm2, pm3, pm4;
	private TextView heading;
	private ImageView circleImg;
	private ImageView avoidImg, openWindowImg, maskImg;
	private ImageView mapClickImg;
	private FontTextView avoidTxt, openWindowTxt, maskTxt;
	private FontTextView msgSecond;
	private ProgressBar aqiProgressBar;
	private ProgressBar weatherProgressBar;
	private Coordinates coordinates;
	private float lastDayAQIReadings[] = new float[24];
	private float last7dayAQIReadings[] = new float[7];
	private float last4weekAQIReadings[] = new float[28];
	private OutdoorAQIEventDto aqiEventDto;
	private static String currentCityTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details_outdoor);
		coordinates = Coordinates.getInstance(this);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		initializeUI();
		initActionBar();
		setActionBarTitle();
		getDataFromDashboard();
	}

	/**
	 * Reading data from server
	 * */
	private void getXCoordinates() {
		ALog.i(ALog.OUTDOOR_DETAILS, "Calculate Aqi value....");
		aqiEventDto = SessionDto.getInstance().getOutdoorEventDto();
		if (aqiEventDto == null) {
			return;
		}

		setPM25();	
		setPM10();	
		setSO2();	
		setNO2();	

		int idx[] = aqiEventDto.getIdx();
			
		if (idx.length == 0) {
			return;
		}
		String currentCityTimeHr =  aqiEventDto.getT().substring(11, 13);
		int hr = Utils.getLastDayHours(currentCityTimeHr);

		calculatelastDayAQIReadings(idx);	
		calculatelast7DayAQIReadings(idx, hr);	
		calculatelast4WeeksAQIReadings(idx, hr);
		
		if (graphLayout.getChildCount() > 0) {
			graphLayout.removeAllViews();
		}
		if (lastDayAQIReadings != null && lastDayAQIReadings.length > 0) {
			graphLayout.addView(
					new GraphView(OutdoorDetailsActivity.this, lastDayAQIReadings, coordinates));
		}
			
	}
	
	private void calculatelastDayAQIReadings(int idx[]) {
		/** last day days */
		/**
		 * Calculate last 24 hours values and add in to last day AQI value
		 * array
		 */
		int lastDayHr = 24;
		if (idx.length < lastDayHr) {
			return;
		}
		for (int i = 0; i < lastDayAQIReadings.length; i++) {
			if (i == 0 && idx[i] == 0) {
				idx[i] = idx[i + 1];
			}
			lastDayAQIReadings[i] = idx[lastDayHr - 1 - i];
		}
	}
	
	private void calculatelast7DayAQIReadings(int idx[], int hr) {
		/**
		 * Calculate 7 days hours 6 days hours plus current hours Calculate
		 * average of every days hours value
		 */
		int last7dayHrs = 6 * 24 + hr;
		
		if (idx.length < last7dayHrs) {
			return;
		}

		float sum = 0;
		float avg = 0;
		int j = 0;
		for (int i = 0; i < last7dayHrs; i++) {
			float x = idx[last7dayHrs - 1 - i];
			sum = sum + x;
			if (is24Hours(i)) {
				avg = sum / (float) 24;
				last7dayAQIReadings[j] = avg;
				j++;
				sum = 0;
				avg = 0;
			} else if (i == last7dayHrs - 1) {
				avg = sum / (float) hr;
				last7dayAQIReadings[j] = avg;
				sum = 0;
				avg = 0;
			}
		}
	}
	
	private boolean is24Hours(int index) {
		boolean is24Hr = false;
		index = index + 1;
		if (index % 24 == 0) {
			is24Hr = true;
		}
		return is24Hr;
	}
	
	private void calculatelast4WeeksAQIReadings(int idx[], int hr) {
		/** last 4 weeks */
		/**
		 * Calculate last 4 weeks hours Calculate average of every days
		 * hours value
		 */
		int last4WeekHrs = 3 * 7 * 24 + 6 * 24 + hr;
		
		if (idx.length < last4WeekHrs) {
			return;
		}

		int count = 1;
		float sum = 0;
		float avg = 0;
		int j = 0;
		for (int i = 0; i < last4WeekHrs; i++) {

			float x = idx[last4WeekHrs - 1 - i];
			sum = sum + x;
			if (count == 24 && j < 21) {
				avg = sum / (float) 24;
				last4weekAQIReadings[j] = avg;
				j++;
				sum = 0;
				avg = 0;
				count = 0;
			} else if (j >= 21) {
				for (int m = 0; m < last7dayAQIReadings.length; m++) {
					last4weekAQIReadings[j] = last7dayAQIReadings[m];
					j++;
				}
				break;
			}
			count++;
		}
	}
	
	private void setPM25() {
		int pm25s[] = aqiEventDto.getPm25();
		if (pm25s != null && pm25s.length > 0) {
			String pm25 = String.valueOf(pm25s[0]);
			if (pm25s[0] == 0 && pm25s.length > 1) {
				pm25 = String.valueOf(pm25s[1]);
			}
			pm1.setText(getString(R.string.pm25) + "  " + pm25);
		}
	}
	
	private void setPM10() {
		int pm10s[] = aqiEventDto.getPm10();
		if (pm10s != null && pm10s.length > 0) {
			String pm10 = String.valueOf(pm10s[0]);
			if (pm10s[0] == 0 && pm10s.length > 1) {
				pm10 = String.valueOf(pm10s[1]);
			}
			pm2.setText(getString(R.string.pm10) + "  " + pm10);
		}
	}
	
	private void setSO2() {
		int so2s[] = aqiEventDto.getSo2();
		if (so2s != null && so2s.length > 0) {
			String so2 = String.valueOf(so2s[0]);
			if (so2s[0] == 0 && so2s.length > 1) {
				so2 = String.valueOf(so2s[1]);
			}
			pm3.setText(getString(R.string.so2) + "  " + so2);
		}
	}
	
	private void setNO2() {
		int no2s[] = aqiEventDto.getNo2();
		if (no2s != null && no2s.length > 0) {
			String no2 = String.valueOf(no2s[0]);
			if (no2s[0] == 0 && no2s.length > 1) {
				no2 = String.valueOf(no2s[1]);
			}
			pm4.setText(getString(R.string.no2) + "  " + no2);
		}
	}

	/** Getting data from Main screen*/
	private void getDataFromDashboard() {
		/**
		 * Updating all the details in the screen, which is passed from Dashboard
		 */
		Bundle bundle = getIntent().getExtras();

		if(bundle != null) {
			ALog.i(ALog.OUTDOOR_DETAILS, "Data come from dashboard");
			OutdoorDto city= (OutdoorDto) bundle.getSerializable(AppConstants.KEY_CITY);
			heading.setText(city.getCityName());
			location.setText("");
			summaryTitle.setText(city.getAqiTitle());
			summary.setText(city.getAqiSummary()[0]);
			aqiValue.setText(city.getAqi());
			if (city.getAqi() != null) {
				try {
					int aqiInt = Integer.parseInt(city.getAqi().trim());
					circleImg.setImageDrawable(Utils.getOutdoorAQICircleBackground(this, aqiInt));
					
					setAdviceIconTex(aqiInt);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			startOutdoorAQITask(city.getCityName());
			currentCityTime = city.getUpdatedTime();
			startWeatherDataTask(city.getGeo());
			setUpMapIfNeeded(city.getGeo());
		} 
	
	}
	
	/**Set advice icon and text*/ 
	private void setAdviceIconTex(int aqiInt) {
		
		if(aqiInt >= 0 && aqiInt <= 50) {
			maskImg.setImageResource(R.drawable.blue_mask_2x); 
			openWindowImg.setImageResource(R.drawable.blue_win_2x);  
			avoidImg.setImageResource(R.drawable.blue_run_2x); 

			maskTxt.setText(getString(R.string.mask_od_msg2));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg2)); 
			avoidTxt.setText(getString(R.string.advice_od_msg2));

		} else if(aqiInt > 50 && aqiInt <= 100) {
			maskImg.setImageResource(R.drawable.dark_blue_mask_2x); 
			openWindowImg.setImageResource(R.drawable.dark_blue_win_2x);  
			avoidImg.setImageResource(R.drawable.dark_blue_run_2x); 

			maskTxt.setText(getString(R.string.mask_od_msg2));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg2)); 
			avoidTxt.setText(getString(R.string.advice_od_msg2));

		} else if(aqiInt > 100 && aqiInt <= 150) {
			maskImg.setImageResource(R.drawable.purple_close_mask_2x); 
			openWindowImg.setImageResource(R.drawable.purple_win_2x);  
			avoidImg.setImageResource(R.drawable.purple_close_run_2x); 

			maskTxt.setText(getString(R.string.mask_od_msg2));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg2)); 
			avoidTxt.setText(getString(R.string.advice_od_msg1));

		} else if(aqiInt > 150 && aqiInt <= 200) {
			maskImg.setImageResource(R.drawable.dark_purple_mask_2x); 
			openWindowImg.setImageResource(R.drawable.dark_purple_close_win_2x);  
			avoidImg.setImageResource(R.drawable.dark_purple_run_2x); 

			maskTxt.setText(getString(R.string.mask_od_msg2));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg1)); 
			avoidTxt.setText(getString(R.string.advice_od_msg1));

		} else if(aqiInt > 200 && aqiInt <= 300) {
			maskImg.setImageResource(R.drawable.light_red_close_mask_2x); 
			openWindowImg.setImageResource(R.drawable.light_red_close_win_2x);  
			avoidImg.setImageResource(R.drawable.light_red_close_run_2x); 

			maskTxt.setText(getString(R.string.mask_od_msg1));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg1)); 
			avoidTxt.setText(getString(R.string.advice_od_msg1));

		} else if (aqiInt > 300) {
			maskImg.setImageResource(R.drawable.red_close_mask_2x); 
			openWindowImg.setImageResource(R.drawable.red_close_win_2x);  
			avoidImg.setImageResource(R.drawable.red_close_run_2x); 

			maskTxt.setText(getString(R.string.mask_od_msg1));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg1)); 
			avoidTxt.setText(getString(R.string.advice_od_msg1));

		}
	}

	/**
	 * Initialize UI widget
	 * */
	private void initializeUI() {
		graphLayout = (LinearLayout) findViewById(R.id.detailsOutdoorlayoutGraph);
		wetherScrollView = (HorizontalScrollView) findViewById(R.id.odTodayWetherReportHSV);
		wetherForcastLayout = (LinearLayout) findViewById(R.id.odWetherForcastLL);

		lastDayBtn = (FontTextView) findViewById(R.id.detailsOutdoorLastDayLabel);
		lastWeekBtn = (FontTextView) findViewById(R.id.detailsOutdoorLastWeekLabel);
		lastFourWeekBtn = (FontTextView) findViewById(R.id.detailsOutdoorLastFourWeekLabel);
		aqiValue = (FontTextView) findViewById(R.id.od_detail_aqi_reading);
		location = (FontTextView) findViewById(R.id.od_detail_location);
		summaryTitle = (FontTextView) findViewById(R.id.odStatusTitle);
		summary = (FontTextView) findViewById(R.id.odStatusDescr);
		pm1 = (FontTextView) findViewById(R.id.odPMValue1);
		pm2 = (FontTextView) findViewById(R.id.odPMValue2);
		pm3 = (FontTextView) findViewById(R.id.odPMValue3);
		pm4 = (FontTextView) findViewById(R.id.odPMValue4);

		circleImg = (ImageView) findViewById(R.id.od_detail_circle_pointer);
		avoidImg = (ImageView) findViewById(R.id.avoidOutdoorImg);  
		openWindowImg = (ImageView) findViewById(R.id.openWindowImg);  
		maskImg = (ImageView) findViewById(R.id.maskImg); 
		mapClickImg = (ImageView) findViewById(R.id.oDmapInlarge); 


		msgSecond = (FontTextView) findViewById(R.id.detailsOutdoorSecondMsg);
		avoidTxt = (FontTextView) findViewById(R.id.avoidOutdoorTxt); 
		openWindowTxt = (FontTextView) findViewById(R.id.openWindowTxt); 
		maskTxt = (FontTextView) findViewById(R.id.maskTxt);

		aqiProgressBar = (ProgressBar) findViewById(R.id.outdoorAqiDownloadProgressBar);
		weatherProgressBar = (ProgressBar) findViewById(R.id.weatherProgressBar);

		/**
		 * Set click listener
		 * */
		lastDayBtn.setOnClickListener(this);
		lastWeekBtn.setOnClickListener(this);
		lastFourWeekBtn.setOnClickListener(this);
		mapClickImg.setOnClickListener(this);
	}
	
	/**
	 * InitActionBar
	 */
	private void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		View view = getLayoutInflater().inflate(R.layout.action_bar, null);
		((ImageView)view.findViewById(R.id.right_menu_img)).setVisibility(View.GONE);
		((ImageView)view.findViewById(R.id.left_menu_img)).setVisibility(View.GONE);
		((ImageView)view.findViewById(R.id.back_to_home_img)).setVisibility(View.GONE);
		((ImageView)view.findViewById(R.id.add_location_img)).setVisibility(View.GONE);
		actionBar.setCustomView(view);
		
	}
	
	/*Sets Action bar title */
	public void setActionBarTitle() {    	
		heading = (TextView) findViewById(R.id.action_bar_title);
		heading.setTypeface(Fonts.getGillsansLight(this));
		heading.setTextSize(24);
		heading.setText(getString(R.string.title_outdoor_db));
	}

	/**
	 * onClick
	 * */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.detailsOutdoorLastDayLabel: {
				removeChildViewFromBar();
				setViewlastDayAQIReadings();
				break;
			}
			case R.id.detailsOutdoorLastWeekLabel: {
				removeChildViewFromBar();
				setViewlast7DayAQIReadings();
				break;
			}
			case R.id.detailsOutdoorLastFourWeekLabel: {
				removeChildViewFromBar();
				setViewlast4WeeksAQIReadings();
				break;
			}
			case R.id.oDmapInlarge: {
				Intent mapIntent = new Intent(OutdoorDetailsActivity.this, MapOdActivity.class);
				mapIntent.putExtra("centerLatF", 31.230638F);
				mapIntent.putExtra("centerLngF", 121.473584F);
				mapIntent.putExtra("centerCity", "Shanghai");
				mapIntent.putExtra("otherInfo", new String[]{});
				startActivity(mapIntent);
				break;
			}
		}
	}
	
	private void setViewlastDayAQIReadings() {
		if (lastDayAQIReadings != null && lastDayAQIReadings.length > 0) {
			graphLayout.addView(
					new GraphView(this, lastDayAQIReadings, coordinates));
		}
		lastDayBtn.setTextColor(GraphConst.COLOR_DODLE_BLUE);
		lastWeekBtn.setTextColor(Color.LTGRAY);
		lastFourWeekBtn.setTextColor(Color.LTGRAY);
		msgSecond.setText(getString(R.string.detail_aiq_message_last_day));
	}
	
	private void setViewlast7DayAQIReadings() {
		if (last7dayAQIReadings != null && last7dayAQIReadings.length > 0) {
			graphLayout.addView(new GraphView(this, last7dayAQIReadings, coordinates));
		}
		lastDayBtn.setTextColor(Color.LTGRAY);
		lastWeekBtn.setTextColor(GraphConst.COLOR_DODLE_BLUE);
		lastFourWeekBtn.setTextColor(Color.LTGRAY);
		msgSecond.setText(getString(R.string.detail_aiq_message_last7day));
	}

	private void setViewlast4WeeksAQIReadings() {
		if (last4weekAQIReadings != null && last4weekAQIReadings.length > 0) {
			graphLayout.addView(new GraphView(this, last4weekAQIReadings, coordinates));
		}
		lastDayBtn.setTextColor(Color.LTGRAY);
		lastWeekBtn.setTextColor(Color.LTGRAY);
		lastFourWeekBtn.setTextColor(GraphConst.COLOR_DODLE_BLUE);
		msgSecond.setText(getString(R.string.detail_aiq_message_last4week));
	}
	
	private void updateWeatherFields() {
		/**Add today weather*/
		wetherScrollView.addView(new WeatherReportLayout(this, null, 8, 
				currentCityTime, SessionDto.getInstance().getWeatherDetails()));

		/**Add weather forecast*/
		WeatherReportLayout weatherReportLayout = new WeatherReportLayout(this, null, 5, 
				currentCityTime, SessionDto.getInstance().getWeatherDetails());
		weatherReportLayout.setOrientation(LinearLayout.VERTICAL);
		wetherForcastLayout.addView(weatherReportLayout);
	}

	/**
	 * 
	 */
	private void removeChildViewFromBar() {
		if (graphLayout.getChildCount() > 0) {
			graphLayout.removeAllViews();
		}
	}

	/**
	 * 
	 * @param v
	 */
	public void aqiAnalysisClick(View v) {
		FragmentManager fragMan = this.getSupportFragmentManager();
		fragMan.beginTransaction().add(OutdoorAQIExplainedDialogFragment.newInstance(), "outdoorexplained").commit();
	}

	/**
	 * 
	 */
	private void setUpMapIfNeeded(String geo) {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
					.getMap();
			if (mMap != null) {
				setUpMap(geo);
			}
		}
	}

	/**
	 * 
	 */
	private void setUpMap(String geo) {
		if (geo == null) {
			return;
		}
		String geoArr[] =geo.split(",");
		if (geoArr == null || geoArr.length < 2) {
			return;
		}
		try {
			double lat = Double.parseDouble(geoArr[0].trim());
			double lng = Double.parseDouble(geoArr[1].trim());
			CameraPosition cameraPosition = new CameraPosition.Builder()
			.target(new LatLng(lat, lng))
			.zoom(13)              
			.bearing(0)                
			.tilt(30)                   
			.build();                   
			
			mMap.getUiSettings().setZoomControlsEnabled(false);
			mMap.getUiSettings().setScrollGesturesEnabled(false);
			mMap.getUiSettings().setZoomGesturesEnabled(false);
			mMap.getUiSettings().setAllGesturesEnabled(false);
			mMap.getUiSettings().setCompassEnabled(false);
			mMap.getUiSettings().setRotateGesturesEnabled(false);
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

			mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
		} catch (NumberFormatException e) {
			ALog.e(ALog.OUTDOOR_DETAILS, e.getMessage());
		}

	}
	
	public static String getCurrentCityTime() {
		return currentCityTime;
	}

	@Override
	public void onBackPressed() {
		finish();
	}
	
	private void startOutdoorAQITask(String cityName) {
		if (cityName == null) {
			return;
		}
	
		TaskGetHttp shanghaiAQI = new TaskGetHttp(String.format(
				AppConstants.OUTDOOR_AQI_URL,cityName.trim()), OutdoorDetailsActivity.this, this);
		shanghaiAQI.start();

	}
	
	private void startWeatherDataTask(String geoCoordinate) {
		ALog.i(ALog.OUTDOOR_DETAILS, "Latitute and Longitude: " + geoCoordinate);
		if (geoCoordinate == null) {
			return;
		}
		TaskGetWeatherData statusUpdateTask = new TaskGetWeatherData(
				String.format(AppConstants.WEATHER_SERVICE_URL,"31.2000,121.5000"), this);
		statusUpdateTask.start();

	}
	
	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if ( msg.what == 1 ) {
				aqiProgressBar.setVisibility(View.GONE);
				getXCoordinates();
			} else if ( msg.what == 2 ) {
				weatherProgressBar.setVisibility(View.GONE);
				updateWeatherFields() ;
			}
		};
	};
	

	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
		ALog.i(ALog.OUTDOOR_DETAILS, "Outdoor Aqi downloaded response code: " + responseCode);
		if (responseCode == 200) {
			SessionDto.getInstance().setOutdoorEventDto(DataParser.parseOutdoorAQIData(responseData)) ;
			handler.sendEmptyMessage(1);
		}
		
	}

	@SuppressLint("HandlerLeak")
	@Override
	public void weatherDataUpdated(String weatherData) {
		ALog.i(ALog.OUTDOOR_DETAILS, "Outdoor Weather downloaded");
		SessionDto.getInstance().setWeatherDetails(DataParser.parseWeatherData(weatherData)) ;
		handler.sendEmptyMessage(2) ;
	}
}
