package com.philips.cl.di.dev.pa.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.ForecastCityDto;
import com.philips.cl.di.dev.pa.dashboard.ForecastWeatherDto;
import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;
import com.philips.cl.di.dev.pa.dashboard.OutdoorController;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;
import com.philips.cl.di.dev.pa.fragment.OutdoorAQIExplainedDialogFragment;
import com.philips.cl.di.dev.pa.purifier.TaskGetHttp;
import com.philips.cl.di.dev.pa.purifier.TaskGetWeatherData.WeatherDataListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Coordinates;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.GraphConst;
import com.philips.cl.di.dev.pa.util.OutdoorDetailsListener;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.dev.pa.view.GraphView;
import com.philips.cl.di.dev.pa.view.WeatherReportLayout;

public class OutdoorDetailsActivity extends BaseActivity 
	implements OnClickListener, ServerResponseListener, WeatherDataListener, OutdoorDetailsListener {

	private GoogleMap mMap;
	private LinearLayout graphLayout, wetherForcastLayout;
	private HorizontalScrollView wetherScrollView;
	private FontTextView lastDayBtn, lastWeekBtn, lastFourWeekBtn;
	private FontTextView aqiValue, location, summaryTitle, summary, pm1, pm2, pm3, pm4;
	private TextView heading;
	private ImageView circleImg;
	private ImageView avoidImg, openWindowImg, maskImg;
	private ImageView mapEnlargeImg;
	private FontTextView avoidTxt, openWindowTxt, maskTxt;
	private FontTextView msgSecond;
	private ProgressBar aqiProgressBar;
	private ProgressBar weatherProgressBar;
	private Coordinates coordinates;
	private static String currentCityTime;
	
	private ViewGroup mapLayout;
	private ImageView mapImg;
	private View mapBackground;
	private double latitude;
	private double longitude;
	private String areaId;
	
	private List<OutdoorAQI> outdoorAQIs;
	
	public float lastDayAQIReadings[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,
			-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F };

	public float last7dayAQIReadings[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F};

	public float last4weekAQIReadings[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,
			-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details_outdoor);
		coordinates = Coordinates.getInstance(this);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		initializeUI();
		try {
			initActionBar();
		} catch (ClassCastException e) {
			ALog.e(ALog.OUTDOOR_DETAILS, "Actionbar: " + e.getMessage());
		}
		setActionBarTitle();
		getDataFromDashboard();
	}
	
	/**
	 * Reading data from server
	 * */
	@SuppressLint({ "UseSparseArrays", "SimpleDateFormat" })
	private void addAQIHistoricData() {
		ALog.i(ALog.OUTDOOR_DETAILS, "Calculate Aqi value....");
		
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		
		Date currentDate = new Date(cal.getTimeInMillis());
		String currentDateStr = formatDate.format(currentDate);
		String currentDateWithHrStr = currentDateStr + " " + Utils.get2DigitHr(cal.get(Calendar.HOUR_OF_DAY));
		
		HashMap<Integer, Float> hourlyAqiValueMap = new HashMap<Integer, Float>();
		HashMap<Integer, Float> dailyAqiValueMap = new HashMap<Integer, Float>();
		HashMap<Integer, Integer> dailyAqiValueCounterMap = new HashMap<Integer, Integer>();
		
		for (int index = 0; index < outdoorAQIs.size(); index++) {
			if (outdoorAQIs.get(index) == null || outdoorAQIs.get(index).getTimeStamp() == null) return;
			String upadtedDateStr = getHistoricDataUpdateDate(outdoorAQIs.get(index).getTimeStamp());
			float aqi = outdoorAQIs.get(index).getAQI();
			int numberOfHr = Utils.getDifferenceBetweenHrFromCurrentHr(currentDateWithHrStr,
					upadtedDateStr + " " + outdoorAQIs.get(index).getTimeStamp().substring(8, 10));
			if (numberOfHr >= 0 && numberOfHr < 24) {
				hourlyAqiValueMap.put(numberOfHr, aqi);
			}
			int numberOfDays = Utils.getDifferenceBetweenDaysFromCurrentDay(currentDateStr, upadtedDateStr);
			if (numberOfDays >= 0) {
				if (dailyAqiValueMap.containsKey(numberOfDays) 
						&& dailyAqiValueCounterMap.containsKey(numberOfDays)) {
					float tempAqi = dailyAqiValueMap.get(numberOfDays);
					tempAqi = tempAqi + aqi;
					dailyAqiValueMap.put(numberOfDays, tempAqi);
					int counterMap = dailyAqiValueCounterMap.get(numberOfDays);
					counterMap++;
					dailyAqiValueCounterMap.put(numberOfDays, counterMap);

				} else {
					dailyAqiValueCounterMap.put(numberOfDays, 1);
					dailyAqiValueMap.put(numberOfDays, aqi);
				}
			}
		}
		updateLastDayArray(hourlyAqiValueMap);
		updateWeeklyArray(dailyAqiValueMap, dailyAqiValueCounterMap);
		setViewlastDayAQIReadings();
	}
	
	private void updateLastDayArray(HashMap<Integer, Float> hourlyAqiValueMap) {
		if (!hourlyAqiValueMap.isEmpty()) {
			for (int i = 0; i < 24; i++) {
				if (hourlyAqiValueMap.containsKey(i)) {
					lastDayAQIReadings[23 - i] = hourlyAqiValueMap.get(i);;
				} else {
					lastDayAQIReadings[23 - i] = -1;
				}
			}
		}
	}
	
	private void updateWeeklyArray(HashMap<Integer, Float> dailyAqiValueMap, 
			HashMap<Integer, Integer> dailyAqiValueCounterMap) {
		if (dailyAqiValueMap.isEmpty()) return;
		for (int mapKey = 0; mapKey < dailyAqiValueMap.size(); mapKey++) {
			float avgAqi = 0.0F;
			if (mapKey < 28) {
				if (dailyAqiValueMap.get(mapKey) != null) {
					avgAqi = dailyAqiValueMap.get(mapKey) / dailyAqiValueCounterMap.get(mapKey);
				} else if (mapKey == 0) {
					avgAqi = -1;
				}
				if (mapKey < 7) {
					last7dayAQIReadings[6 - mapKey] = avgAqi;
				}
				
				if (mapKey < 28) {
					last4weekAQIReadings[27 - mapKey] = avgAqi;
				}
			} else {
				break;
			}
		}
	}
	
	private String getHistoricDataUpdateDate(String timeStamp) {
		if (timeStamp == null || timeStamp.isEmpty()) return null;
		StringBuilder builder = new StringBuilder();
		builder.append(timeStamp.substring(0, 4)).append("-");
		builder.append(timeStamp.substring(4, 6)).append("-");;
		builder.append(timeStamp.substring(6, 8));
		return builder.toString();
	}
	
	/** Getting data from Main screen*/
	private void getDataFromDashboard() {
		/**
		 * Updating all the details in the screen, which is passed from Dashboard
		 */
		OutdoorController.getInstance().setOutdoorDetailsListener(this) ;
		areaId = getIntent().getStringExtra(AppConstants.EXTRA_AREA_ID);
		startCityAQIHistoryTask(areaId);
		OutdoorController.getInstance().startCityOneDayForecastTask(areaId) ;
		OutdoorController.getInstance().startCityFourDayForecastTask(areaId) ;

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
		mapEnlargeImg = (ImageView) findViewById(R.id.oDmapInlarge); 
		mapEnlargeImg.setVisibility(View.INVISIBLE);

		msgSecond = (FontTextView) findViewById(R.id.detailsOutdoorSecondMsg);
		avoidTxt = (FontTextView) findViewById(R.id.avoidOutdoorTxt); 
		openWindowTxt = (FontTextView) findViewById(R.id.openWindowTxt); 
		maskTxt = (FontTextView) findViewById(R.id.maskTxt);

		aqiProgressBar = (ProgressBar) findViewById(R.id.outdoorAqiDownloadProgressBar);
		weatherProgressBar = (ProgressBar) findViewById(R.id.weatherProgressBar);
		
		mapLayout = (RelativeLayout) findViewById(R.id.img_map_layt);
		mapImg = (ImageView) findViewById(R.id.img_map);
		mapBackground = (View) findViewById(R.id.view_map_bg);

		if(!Utils.isGooglePlayServiceAvailable()) {
			mapLayout.setVisibility(View.GONE);
		}
		/**
		 * Set click listener
		 * */
		lastDayBtn.setOnClickListener(this);
		lastWeekBtn.setOnClickListener(this);
		lastFourWeekBtn.setOnClickListener(this);
		mapEnlargeImg.setOnClickListener(this);
	}
	
	/**
	 * InitActionBar
	 */
	private void initActionBar() throws ClassCastException {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		Drawable d=getResources().getDrawable(R.drawable.ews_nav_bar_2x);  
		actionBar.setBackgroundDrawable(d);
		View view = getLayoutInflater().inflate(R.layout.home_action_bar, null);
		((ImageView)view.findViewById(R.id.right_menu_img)).setVisibility(View.GONE);
		((ImageView)view.findViewById(R.id.left_menu_img)).setVisibility(View.GONE);
		ImageView backToHome = ((ImageView)view.findViewById(R.id.back_to_home_img));
		backToHome.setVisibility(View.VISIBLE);
		backToHome.setOnClickListener(this);
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
				setViewlastDayAQIReadings();
				break;
			}
			case R.id.detailsOutdoorLastWeekLabel: {
				setViewlast7DayAQIReadings();
				break;
			}
			case R.id.detailsOutdoorLastFourWeekLabel: {
				setViewlast4WeeksAQIReadings();
				break;
			}
			case R.id.oDmapInlarge: {
				Intent mapIntent = new Intent(OutdoorDetailsActivity.this, MapOdActivity.class);
				mapIntent.putExtra("centerLatF", latitude);
				mapIntent.putExtra("centerLngF", longitude);
				mapIntent.putExtra("centerCity", "Shanghai");
				mapIntent.putExtra("otherInfo", new String[]{});
				startActivity(mapIntent);
				break;
			}
			case R.id.back_to_home_img: {
				finish();
				break;
			}
			default:
				break;
		}
	}
	
	private void setViewlastDayAQIReadings() {
		removeChildViewFromBar();
		if (lastDayAQIReadings != null && lastDayAQIReadings.length > 0) {
			graphLayout.addView(new GraphView(this, lastDayAQIReadings, coordinates));
		}
		lastDayBtn.setTextColor(GraphConst.COLOR_DODLE_BLUE);
		lastWeekBtn.setTextColor(Color.LTGRAY);
		lastFourWeekBtn.setTextColor(Color.LTGRAY);
		msgSecond.setText(getString(R.string.detail_aiq_message_last_day));
	}
	
	private void setViewlast7DayAQIReadings() {
		removeChildViewFromBar();
		if (last7dayAQIReadings != null && last7dayAQIReadings.length > 0) {
			graphLayout.addView(new GraphView(this, last7dayAQIReadings, coordinates));
		}
		lastDayBtn.setTextColor(Color.LTGRAY);
		lastWeekBtn.setTextColor(GraphConst.COLOR_DODLE_BLUE);
		lastFourWeekBtn.setTextColor(Color.LTGRAY);
		msgSecond.setText(getString(R.string.detail_aiq_message_last7day));
	}

	private void setViewlast4WeeksAQIReadings() {
		removeChildViewFromBar();
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
		wetherScrollView.addView(new WeatherReportLayout(this, null,
				currentCityTime, SessionDto.getInstance().getWeatherDetails()));

		/**Add weather forecast*/
		WeatherReportLayout weatherReportLayout = new WeatherReportLayout(this, null,
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
		try {
			FragmentManager fragMan = this.getSupportFragmentManager();
			fragMan.beginTransaction().add(OutdoorAQIExplainedDialogFragment.newInstance(), "outdoorexplained").commit();
		} catch (IllegalStateException e) {
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
	
	@SuppressLint("HandlerLeak")
	private void startOutdoorAQITask(String cityName) {
		if (cityName == null) {
			return;
		}
	
		TaskGetHttp shanghaiAQI = new TaskGetHttp(String.format(
				AppConstants.OUTDOOR_AQI_URL,cityName.trim()), OutdoorDetailsActivity.this, this);
		shanghaiAQI.start();

	}
	
	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if ( msg.what == 1 ) {
				aqiProgressBar.setVisibility(View.GONE);
				if (outdoorAQIs != null && !outdoorAQIs.isEmpty()) {
					addAQIHistoricData();
				}
			} else if ( msg.what == 2 ) {
				weatherProgressBar.setVisibility(View.GONE);
				updateWeatherFields() ;
			}
		};
	};
	
	public void startCityAQIHistoryTask(String areaID) {
		if (PurAirApplication.isDemoModeEnable()) return;
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		
		TaskGetHttp aqiHistoricTask = new TaskGetHttp(OutdoorController.getInstance().buildURL(
				OutdoorController.BASE_URL_AQI, areaID, "air_his", 
				Utils.getDate((cal.getTimeInMillis() - (1000 * 60 * 60 * 24 * 30l))) + "," 
				+ Utils.getDate(cal.getTimeInMillis()), OutdoorController.APP_ID), 
				areaID, PurAirApplication.getAppContext(), this);
		aqiHistoricTask.start();
	}

	@Override
	public void receiveServerResponse(int responseCode, String responseData, String areaID) {
		ALog.i(ALog.OUTDOOR_DETAILS, "Outdoor Aqi downloaded response code: " + responseCode);
		if (responseCode == 200) {
			outdoorAQIs = DataParser.parseHistoricalAQIData(responseData, areaID);
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

	@Override
	public void onHourlyWeatherForecastReceived(final List<Weatherdto> weatherList) {
		if( weatherList != null ) {
			ALog.i(ALog.OUTDOOR_DETAILS, "Outdoor Weather received: "+weatherList.size()) ;
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					wetherScrollView.addView(new WeatherReportLayout(OutdoorDetailsActivity.this, null,
							currentCityTime,weatherList));
					
				}
			});
			
		}
	}

	@Override
	public void onWeatherForecastReceived(final List<ForecastWeatherDto> weatherList) {
		// TODO Auto-generated method stub
		if( weatherList != null ) {
			ALog.i(ALog.OUTDOOR_DETAILS, "Outdoor Weather received: "+weatherList.size()) ;
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					wetherScrollView.addView(new WeatherReportLayout(OutdoorDetailsActivity.this, null, weatherList));
					
				}
			});
			
		}
	}

	@Override
	public void onAQIHisttReceived(List<OutdoorAQI> outdoorAQIHistory) {
		// TODO Auto-generated method stub
		
	}
}
