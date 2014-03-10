package com.philips.cl.di.dev.pa.screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.customviews.GraphView;
import com.philips.cl.di.dev.pa.customviews.WeatherReportLayout;
import com.philips.cl.di.dev.pa.detail.utils.Coordinates;
import com.philips.cl.di.dev.pa.detail.utils.GraphConst;
import com.philips.cl.di.dev.pa.dto.OutdoorAQIEventDto;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.utils.Fonts;
import com.philips.cl.di.dev.pa.utils.Utils;

public class OutdoorDetailsActivity extends ActionBarActivity implements OnClickListener {

	private ActionBar mActionBar;
	private GoogleMap mMap;
	private LinearLayout graphLayout, wetherForcastLayout;
	private HorizontalScrollView wetherScrollView;
	private CustomTextView lastDayBtn, lastWeekBtn, lastFourWeekBtn;
	private CustomTextView aqiValue, location, summaryTitle, summary, pm1, pm2, pm3, pm4;
	private TextView heading;
	private ImageView circleImg;
	private ImageView avoidImg, openWindowImg, maskImg;
	private ImageView mapClickImg;
	private CustomTextView avoidTxt, openWindowTxt, maskTxt;
	private CustomTextView msgSecond;
	private Coordinates coordinates;
	private SessionDto sessionDto;
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

		setUpMapIfNeeded();
		
		getDataFromDashboard();
		
		if (aqiEventDto != null) {
			getXCoordinates();
		}
		if (graphLayout.getChildCount() > 0) {
			graphLayout.removeAllViews();
		}
		if (lastDayAQIReadings != null && lastDayAQIReadings.length > 0) {
			graphLayout.addView(
					new GraphView(this, lastDayAQIReadings, coordinates));
		}
		
		/**Add today weather*/
		wetherScrollView.addView(new WeatherReportLayout(this, null, 8, currentCityTime));

		/**Add weather forecast*/
		WeatherReportLayout weatherReportLayout = new WeatherReportLayout(this, null, 5, currentCityTime);
		weatherReportLayout.setOrientation(LinearLayout.VERTICAL);
		wetherForcastLayout.addView(weatherReportLayout);
	}


	@Override
	protected void onResume() {
		super.onResume();
		
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * Reading data from server
	 * */
	private void getXCoordinates() {
		if (aqiEventDto != null) {

			int pm25s[] = aqiEventDto.getPm25();
			if (pm25s != null && pm25s.length > 0) {
				String pm25 = String.valueOf(pm25s[0]);
				if (pm25s[0] == 0 && pm25s.length > 1) {
					pm25 = String.valueOf(pm25s[1]);
				}
				pm1.setText(getString(R.string.pm25) + "  " + pm25);
			}

			int pm10s[] = aqiEventDto.getPm10();
			if (pm10s != null && pm10s.length > 0) {
				String pm10 = String.valueOf(pm10s[0]);
				if (pm10s[0] == 0 && pm10s.length > 1) {
					pm10 = String.valueOf(pm10s[1]);
				}
				pm2.setText(getString(R.string.pm10) + "  " + pm10);
			}

			int so2s[] = aqiEventDto.getSo2();
			if (so2s != null && so2s.length > 0) {
				String so2 = String.valueOf(so2s[0]);
				if (so2s[0] == 0 && so2s.length > 1) {
					so2 = String.valueOf(so2s[1]);
				}
				pm3.setText(getString(R.string.so2) + "  " + so2);
			}

			int no2s[] = aqiEventDto.getNo2();
			if (no2s != null && no2s.length > 0) {
				String no2 = String.valueOf(no2s[0]);
				if (no2s[0] == 0 && no2s.length > 1) {
					no2 = String.valueOf(no2s[1]);
				}
				pm4.setText(getString(R.string.no2) + "  " + no2);
			}

			int idx[] = aqiEventDto.getIdx();

			/** last day days */
			/**
			 * Calculate last 24 hours values and add in to last day AQI value
			 * array
			 */
			int lastDayHr = 24;
			if (idx != null) {
				
				for (int i = 0; i < lastDayAQIReadings.length; i++) {
					if (i == 0 && idx[i] == 0) {
						idx[i] = idx[i+1];
						//lastDayHr = 25;
					}
					lastDayAQIReadings[i] = idx[lastDayHr - 1 - i];
				}
			}

			/** last 7 days */
			/**
			 * Calculate last 7 day values and add in to last 7 day AQI value
			 * array Calculate current day hours
			 */
			currentCityTime = aqiEventDto.getT();
			String currentCityTimeHr =  aqiEventDto.getT().substring(11, 13);
			int hr = 0;
			if (currentCityTimeHr != null) {
				try {
					hr = Integer.parseInt(currentCityTimeHr);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			if (hr == 0) {
				hr = 24;
			}

			/**
			 * Calculate 7 days hours 6 days hours plus current hours Calculate
			 * average of every days hours value
			 */
			int last7dayHrs = (6 * 24) + hr;

			if (idx != null) {
				float sum = 0;
				float avg = 0;
				int j = 0;
				for (int i = 0; i < last7dayHrs; i++) {
					float x = idx[last7dayHrs - 1 - i];
					sum = sum + x;
					if (i == 23 || i == 47 || i == 71 || i == 95 || i == 119
							|| i == 143) {
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

			/** last 4 weeks */
			/**
			 * Calculate last 4 weeks hours Calculate average of every days
			 * hours value
			 */
			int last4WeekHrs = (3 * 7 * 24) + (6 * 24) + hr;

			if (idx != null) {
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
		}
	}

	/** Getting data from Main screen*/
	private void getDataFromDashboard() {
		String []datas = getIntent().getStringArrayExtra("outdoor");
		/**
		 * Updating all the details in the screen, which is passed from Dashboard
		 */
		if (datas != null && datas.length > 0) {
			//locationCity.setText(bundleDatas[1]);
			heading.setText(datas[1]);
			location.setText(datas[2]);
			summaryTitle.setText(datas[5]);
			summary.setText(datas[6]);

			aqiValue.setText(datas[7]);
			
			try {
				int aqiInt = Integer.parseInt(datas[7].trim());
				circleImg.setImageDrawable(Utils.getOutdoorAQICircleBackground(this, aqiInt));
				
				setAdviceIconTex(aqiInt);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		aqiEventDto = (OutdoorAQIEventDto) getIntent().getSerializableExtra("outdoorAqi");
	
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

		lastDayBtn = (CustomTextView) findViewById(R.id.detailsOutdoorLastDayLabel);
		lastWeekBtn = (CustomTextView) findViewById(R.id.detailsOutdoorLastWeekLabel);
		lastFourWeekBtn = (CustomTextView) findViewById(R.id.detailsOutdoorLastFourWeekLabel);
		aqiValue = (CustomTextView) findViewById(R.id.od_detail_aqi_reading);
		location = (CustomTextView) findViewById(R.id.od_detail_location);
		summaryTitle = (CustomTextView) findViewById(R.id.odStatusTitle);
		summary = (CustomTextView) findViewById(R.id.odStatusDescr);
		pm1 = (CustomTextView) findViewById(R.id.odPMValue1);
		pm2 = (CustomTextView) findViewById(R.id.odPMValue2);
		pm3 = (CustomTextView) findViewById(R.id.odPMValue3);
		pm4 = (CustomTextView) findViewById(R.id.odPMValue4);

		circleImg = (ImageView) findViewById(R.id.od_detail_circle_pointer);
		avoidImg = (ImageView) findViewById(R.id.avoidOutdoorImg);  
		openWindowImg = (ImageView) findViewById(R.id.openWindowImg);  
		maskImg = (ImageView) findViewById(R.id.maskImg); 
		mapClickImg = (ImageView) findViewById(R.id.oDmapInlarge); 


		msgSecond = (CustomTextView) findViewById(R.id.detailsOutdoorSecondMsg);
		avoidTxt = (CustomTextView) findViewById(R.id.avoidOutdoorTxt); 
		openWindowTxt = (CustomTextView) findViewById(R.id.openWindowTxt); 
		maskTxt = (CustomTextView) findViewById(R.id.maskTxt);

		

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
		mActionBar = getSupportActionBar();
		mActionBar.setIcon(null);
		mActionBar.setHomeButtonEnabled(false);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		mActionBar.setCustomView(R.layout.action_bar);
		
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
				if (lastDayAQIReadings != null && lastDayAQIReadings.length > 0) {
					graphLayout.addView(
							new GraphView(this, lastDayAQIReadings, coordinates));
				}
				lastDayBtn.setTextColor(GraphConst.COLOR_DODLE_BLUE);
				lastWeekBtn.setTextColor(Color.LTGRAY);
				lastFourWeekBtn.setTextColor(Color.LTGRAY);
				msgSecond.setText(getString(R.string.detail_aiq_message_last_day));
				break;
			}
			case R.id.detailsOutdoorLastWeekLabel: {
				removeChildViewFromBar();
				if (last7dayAQIReadings != null && last7dayAQIReadings.length > 0) {
					graphLayout.addView(new GraphView(this, last7dayAQIReadings, coordinates));
				}
				lastDayBtn.setTextColor(Color.LTGRAY);
				lastWeekBtn.setTextColor(GraphConst.COLOR_DODLE_BLUE);
				lastFourWeekBtn.setTextColor(Color.LTGRAY);
				msgSecond.setText(getString(R.string.detail_aiq_message_last7day));
				break;
			}
			case R.id.detailsOutdoorLastFourWeekLabel: {
				removeChildViewFromBar();
				if (last4weekAQIReadings != null && last4weekAQIReadings.length > 0) {
					graphLayout.addView(new GraphView(this, last4weekAQIReadings, coordinates));
				}
				lastDayBtn.setTextColor(Color.LTGRAY);
				lastWeekBtn.setTextColor(Color.LTGRAY);
				lastFourWeekBtn.setTextColor(GraphConst.COLOR_DODLE_BLUE);
				msgSecond.setText(getString(R.string.detail_aiq_message_last4week));
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

	/**
	 * 
	 */
	private void removeChildViewFromBar() {
		if (graphLayout.getChildCount() > 0) {
			graphLayout.removeAllViews();
		}
		if (sessionDto != null) {
			getXCoordinates();
		}
	}

	/**
	 * 
	 * @param v
	 */
	public void aqiAnalysisClick(View v) {
		Intent intent = new Intent(this, OutdoorAQIAnalysisActivity.class);
		startActivity(intent);
	}

	/**
	 * 
	 */
	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
					.getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	/**
	 * 
	 */
	private void setUpMap() {
		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(new LatLng(31.230638, 121.473584)) 
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

		mMap.addMarker(new MarkerOptions().position(new LatLng(31.230638, 121.473584)));

	}
	
	public static String getCurrentCityTime() {
		return currentCityTime;
	}

	
	@Override
	public void onBackPressed() {
		finish();
	}
}
