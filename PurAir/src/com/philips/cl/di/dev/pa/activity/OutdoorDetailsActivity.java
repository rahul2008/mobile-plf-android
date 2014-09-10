package com.philips.cl.di.dev.pa.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.ForecastWeatherDto;
import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;
import com.philips.cl.di.dev.pa.dashboard.OutdoorController;
import com.philips.cl.di.dev.pa.dashboard.OutdoorImage;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;
import com.philips.cl.di.dev.pa.fragment.DownloadAlerDialogFragement;
import com.philips.cl.di.dev.pa.fragment.OutdoorAQIExplainedDialogFragment;
import com.philips.cl.di.dev.pa.purifier.TaskGetHttp;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Coordinates;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.GraphConst;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.util.OutdoorDetailsListener;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.dev.pa.view.GraphView;
import com.philips.cl.di.dev.pa.view.WeatherReportLayout;

public class OutdoorDetailsActivity extends BaseActivity 
	implements OnClickListener, ServerResponseListener, OutdoorDetailsListener {

	private LinearLayout graphLayout, wetherForcastLayout;
	private WeatherReportLayout weatherReportLayout;
	private HorizontalScrollView wetherScrollView;
	private FontTextView lastDayBtn, lastWeekBtn, lastFourWeekBtn;
	private FontTextView aqiValue, summaryTitle, summary;
	private TextView heading;
	private ImageView circleImg, backgroundImage;
	private ImageView avoidImg, openWindowImg, maskImg;
	private FontTextView avoidTxt, openWindowTxt, maskTxt;
	private FontTextView msgSecond;
	private ProgressBar aqiProgressBar;
	private ProgressBar weatherProgressBar;
	private Coordinates coordinates;
	private static String currentCityTime;
	
	private ViewGroup mapGaodeLayout;
	private String areaId;
	private FontTextView pmValue;
	private LinearLayout pmLayout;
	private List<OutdoorAQI> outdoorAQIs;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat formatDate;
	private Calendar calenderGMTChinese;
	
	private float lastDayAQIReadings[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,
			-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F };

	private float last7dayAQIReadings[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F};

	private float last4weekAQIReadings[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,
			-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F };
	
	private HashMap<Integer, Float> dailyAqiValueMap;
	private HashMap<Integer, Integer> dailyAqiValueCounterMap;

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details_outdoor);
		formatDate = new SimpleDateFormat("yyyy-MM-dd");
		calenderGMTChinese = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
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
	
	private Date getCurrentDate() {
		TimeZone timeZoneChina = TimeZone.getTimeZone("GMT+8");
		TimeZone timeZoneCurrent = Calendar.getInstance().getTimeZone();
		
		//Time difference between time zone and GMT
		int offsetChina = timeZoneChina.getOffset(Calendar.getInstance().getTimeInMillis());
		int offsetCurrent = timeZoneCurrent.getOffset(Calendar.getInstance().getTimeInMillis());
		int offset = offsetChina - offsetCurrent;
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		
		Date currentDate = new Date(cal.getTimeInMillis() + offset);
		return currentDate;
	}
	
	/**
	 * Reading data from server
	 * */
	@SuppressLint({ "UseSparseArrays", "SimpleDateFormat" })
	private void addAQIHistoricData() {
		ALog.i(ALog.OUTDOOR_DETAILS, "Calculate Aqi value....");
		
		HashMap<String, Float> allHourlyAqiValueMap = new HashMap<String, Float>();
		HashMap<String, Integer> allHourlyAqiValueCounterMap = new HashMap<String, Integer>();
		
		dailyAqiValueMap = new HashMap<Integer, Float>();
		dailyAqiValueCounterMap = new HashMap<Integer, Integer>();
		
		for (int index = 0; index < outdoorAQIs.size(); index++) {
			if (outdoorAQIs.get(index) == null || outdoorAQIs.get(index).getTimeStamp() == null) return;
			String updatedDateStr = getHistoricDataUpdateDate(outdoorAQIs.get(index).getTimeStamp());
			float aqi = outdoorAQIs.get(index).getAQI();
//			System.out.println("Outdoor historic aqi : " + aqi + " time: " + outdoorAQIs.get(index).getTimeStamp());
			String dateWithHour = updatedDateStr + " " + outdoorAQIs.get(index).getTimeStamp().substring(8, 10);
			
			if (allHourlyAqiValueMap.containsKey(dateWithHour) 
					&& allHourlyAqiValueCounterMap.containsKey(dateWithHour)) {
				
				float faqi = allHourlyAqiValueMap.get(dateWithHour);
				faqi = faqi + aqi;
				allHourlyAqiValueMap.put(dateWithHour, faqi);
				int counterMap = allHourlyAqiValueCounterMap.get(dateWithHour);
				counterMap++;
				allHourlyAqiValueCounterMap.put(dateWithHour, counterMap);
			} else {
				allHourlyAqiValueMap.put(dateWithHour, aqi);
				allHourlyAqiValueCounterMap.put(dateWithHour, 1);
			}
		}
		
		averageHourlyAQI(allHourlyAqiValueMap, allHourlyAqiValueCounterMap);
		updateWeeklyArray(dailyAqiValueMap, dailyAqiValueCounterMap);
		
		//Clear object
		allHourlyAqiValueMap.clear();
		allHourlyAqiValueMap = null;
		allHourlyAqiValueCounterMap.clear();
		allHourlyAqiValueCounterMap = null;
		dailyAqiValueMap.clear();
		dailyAqiValueMap = null;
		dailyAqiValueCounterMap.clear();
		dailyAqiValueCounterMap = null;
		
		handler.sendEmptyMessage(1);
	}
	
	private void averageHourlyAQI(HashMap<String, Float> allHourlyAqiValueMap,
			HashMap<String, Integer> allHourlyAqiValueCounterMap) {
		if (allHourlyAqiValueMap.isEmpty()) return;
		
		Set<String> mapKeys = allHourlyAqiValueMap.keySet();
		for (String mapKey : mapKeys) {
			if (allHourlyAqiValueMap.containsKey(mapKey) 
					&& allHourlyAqiValueCounterMap.containsKey(mapKey)) {
				float aqi = allHourlyAqiValueMap.get(mapKey) / allHourlyAqiValueCounterMap.get(mapKey);
				allHourlyAqiValueMap.put(mapKey, aqi);
				updateLastDayArray(aqi, mapKey);
				averageDailyAQI(aqi, mapKey);
			}
		}
	}
	
	private void updateLastDayArray(float aqi, String key) {
		String currentDateStr = formatDate.format(getCurrentDate());
		String currentDateWithHrStr = currentDateStr + " " + 
				Utils.get2DigitHr(calenderGMTChinese.get(Calendar.HOUR_OF_DAY));
		
		int numberOfHr = Utils.getDifferenceBetweenHrFromCurrentHr(currentDateWithHrStr, key);
		
		if (numberOfHr >= 0 && numberOfHr < 24) {
			lastDayAQIReadings[23 - numberOfHr] = aqi;
		}
	}
	
	private void averageDailyAQI(float aqi, String key) {
		
		String currentDateStr = formatDate.format(getCurrentDate());
		String updatedDateStr = "";
		String [] keyArr = key.split(" ");
		if (keyArr != null && keyArr.length > 0) {
			updatedDateStr = keyArr[0];
		}
		
		int numberOfDays = Utils.getDifferenceBetweenDaysFromCurrentDay(currentDateStr, updatedDateStr);
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

	private void updateWeeklyArray(HashMap<Integer, Float> dailyAqiValueMap, 
			HashMap<Integer, Integer> dailyAqiValueCounterMap) {
		if (dailyAqiValueMap.isEmpty()) return;
		for (int mapKey = 0; mapKey < dailyAqiValueMap.size(); mapKey++) {
			float avgAqi = -1F;
			if (mapKey < 28) {
				if (dailyAqiValueMap.containsKey(mapKey) 
						&& dailyAqiValueCounterMap.containsKey(mapKey)) {
					avgAqi = dailyAqiValueMap.get(mapKey) / dailyAqiValueCounterMap.get(mapKey);
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
	
	private static String mSelectedCityCode = null;

	public static String getSelectedCityCode(){
		return mSelectedCityCode;
	}
	
	private void setOutdoorCityCode(String areaCode){
		mSelectedCityCode = areaCode;
	}
	
	/** Getting data from Main screen*/
	private void getDataFromDashboard() {
		/**
		 * Updating all the details in the screen, which is passed from Dashboard
		 */
		OutdoorController.getInstance().setOutdoorDetailsListener(this) ;
		
		OutdoorAQI aqiValue = (OutdoorAQI) getIntent().getSerializableExtra(AppConstants.OUTDOOR_AQI) ;
		
		if( aqiValue != null) {
			setOutdoorCityCode(aqiValue.getAreaID());
			areaId = aqiValue.getAreaID() ;
			setAdviceIconTex(aqiValue.getAQI());
			//set image background corresponding with city and areaId
			backgroundImage.setImageResource(OutdoorImage.valueOf(areaId, aqiValue.getAQI()));
			
			this.aqiValue.setText(""+aqiValue.getAQI()) ;
			this.circleImg.setImageResource(getImageResId(aqiValue.getAQI())) ;
			String [] summary = aqiValue.getAqiSummary() ;
			if( summary != null && summary.length == 2 ) {
				this.summaryTitle.setText(summary[0]) ;
				this.summary.setText(summary[1]);
			}
			
			pmLayout.setVisibility(View.VISIBLE);
			pmValue.setText(""+aqiValue.getPM25());
			
			if (OutdoorController.getInstance().isPhilipsSetupWifiSelected()){
				weatherProgressBar.setVisibility(View.GONE);
				aqiProgressBar.setVisibility(View.GONE);
				showAlertDialog("", getString(R.string.outdoor_download_failed));
				return;
			}
			startCityAQIHistoryTask(areaId);
			OutdoorController.getInstance().startCityOneDayForecastTask(areaId) ;
			OutdoorController.getInstance().startCityFourDayForecastTask(areaId) ;
		}
	}
	
	private int getImageResId(int p2) {
		if(p2 >= 0 && p2 <= 50) {
			return R.drawable.aqi_blue_circle_2x;
		} else if(p2 > 50 && p2 <= 100) {
			return R.drawable.aqi_pink_circle_2x;
		} else if(p2 > 100 && p2 <= 150) {
			return R.drawable.aqi_light_pink_circle_2x;
		} else if(p2 > 150 && p2 <= 200) {
			return R.drawable.aqi_purple_circle_2x;
		} else if(p2 > 200 && p2 <= 300) {
			return R.drawable.aqi_fusia_circle_2x;
		} else if(p2 > 300 && p2 <= 500) {
			return R.drawable.aqi_red_circle_2x;
		}

		return R.drawable.blue_circle_with_arrow_small;
	}
	
	/**Set advice icon and text*/ 
	private void setAdviceIconTex(int aqiInt) {
		if(aqiInt >= 0 && aqiInt <= 50) {
			maskImg.setImageResource(R.drawable.blue_mask_2x); 
			openWindowImg.setImageResource(R.drawable.blue_win_2x);  
			avoidImg.setImageResource(R.drawable.blue_run_2x); 

			maskTxt.setText(getString(R.string.mask_od_msg1));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg1)); 
			avoidTxt.setText(getString(R.string.advice_od_msg1));

		} else if(aqiInt > 50 && aqiInt <= 100) {
			maskImg.setImageResource(R.drawable.dark_blue_mask_2x); 
			openWindowImg.setImageResource(R.drawable.dark_blue_win_2x);  
			avoidImg.setImageResource(R.drawable.dark_blue_run_2x); 

			maskTxt.setText(getString(R.string.mask_od_msg1));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg1)); 
			avoidTxt.setText(getString(R.string.advice_od_msg2));

		} else if(aqiInt > 100 && aqiInt <= 150) {
			maskImg.setImageResource(R.drawable.purple_close_mask_2x); 
			openWindowImg.setImageResource(R.drawable.purple_win_2x);  
			avoidImg.setImageResource(R.drawable.purple_close_run_2x); 

			maskTxt.setText(getString(R.string.mask_od_msg1));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg1)); 
			avoidTxt.setText(getString(R.string.advice_od_msg3));

		} else if(aqiInt > 150 && aqiInt <= 200) {
			maskImg.setImageResource(R.drawable.dark_purple_mask_2x); 
			openWindowImg.setImageResource(R.drawable.dark_purple_close_win_2x);  
			avoidImg.setImageResource(R.drawable.dark_purple_run_2x); 

			maskTxt.setText(getString(R.string.mask_od_msg1));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg2)); 
			avoidTxt.setText(getString(R.string.advice_od_msg3));

		} else if(aqiInt > 200 && aqiInt <= 300) {
			maskImg.setImageResource(R.drawable.light_red_close_mask_2x); 
			openWindowImg.setImageResource(R.drawable.light_red_close_win_2x);  
			avoidImg.setImageResource(R.drawable.light_red_close_run_2x); 

			maskTxt.setText(getString(R.string.mask_od_msg2));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg2)); 
			avoidTxt.setText(getString(R.string.advice_od_msg3));

		} else if (aqiInt > 300) {
			maskImg.setImageResource(R.drawable.red_close_mask_2x); 
			openWindowImg.setImageResource(R.drawable.red_close_win_2x);  
			avoidImg.setImageResource(R.drawable.red_close_run_2x); 

			maskTxt.setText(getString(R.string.mask_od_msg2));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg2)); 
			avoidTxt.setText(getString(R.string.advice_od_msg3));

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
		summaryTitle = (FontTextView) findViewById(R.id.odStatusTitle);
		summary = (FontTextView) findViewById(R.id.odStatusDescr);
		
		pmValue=(FontTextView)findViewById(R.id.hf_outdoor_pm_value);
		pmLayout=(LinearLayout)findViewById(R.id.pm_layout);

		backgroundImage = (ImageView) findViewById(R.id.detailsOutdoorDbImg);
		circleImg = (ImageView) findViewById(R.id.od_detail_circle_pointer);
		avoidImg = (ImageView) findViewById(R.id.avoidOutdoorImg);  
		openWindowImg = (ImageView) findViewById(R.id.openWindowImg);  
		maskImg = (ImageView) findViewById(R.id.maskImg); 

		msgSecond = (FontTextView) findViewById(R.id.detailsOutdoorSecondMsg);
		avoidTxt = (FontTextView) findViewById(R.id.avoidOutdoorTxt); 
		openWindowTxt = (FontTextView) findViewById(R.id.openWindowTxt); 
		maskTxt = (FontTextView) findViewById(R.id.maskTxt);

		aqiProgressBar = (ProgressBar) findViewById(R.id.outdoorAqiDownloadProgressBar);
		weatherProgressBar = (ProgressBar) findViewById(R.id.weatherProgressBar);
		
		mapGaodeLayout = (RelativeLayout) findViewById(R.id.gaode_map_layt);
		setupGaodeMap();
		
		/**
		 * Set click listener
		 * */
		lastDayBtn.setOnClickListener(this);
		lastWeekBtn.setOnClickListener(this);
		lastFourWeekBtn.setOnClickListener(this);
	}
	
	private void setupGaodeMap() {
		mapGaodeLayout.setVisibility(View.VISIBLE);
		
		ImageView gaodeMapZoom = (ImageView) findViewById(R.id.gaodeMapZoomImg);
		gaodeMapZoom.setOnClickListener(this);
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
		//If Chinese language selected set font-type-face normal
		if( LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")
				|| LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
			heading.setTypeface(Typeface.DEFAULT);
		} else {
			heading.setTypeface(Fonts.getGillsansLight(this));
		}
		heading.setTextSize(24);
		heading.setText(getIntent().getStringExtra(AppConstants.OUTDOOR_CITY_NAME));
	}

	/**
	 * onClick
	 * */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.gaodeMapZoomImg:
				Intent gaodeMapIntent = new Intent(OutdoorDetailsActivity.this, MarkerActivity.class);
				startActivity(gaodeMapIntent);
				break;
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
			fragMan.beginTransaction().add(
					OutdoorAQIExplainedDialogFragment.newInstance(), "outdoorexplained").commit();
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
				setViewlastDayAQIReadings();
			} else if ( msg.what == 2 ) {
				aqiProgressBar.setVisibility(View.GONE);
				weatherProgressBar.setVisibility(View.GONE);
				showAlertDialog("", getString(R.string.outdoor_download_failed));
			}
		};
	};
	
	private void showAlertDialog(String title, String message) {
		try {
			FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
			
			Fragment prevFrag = getSupportFragmentManager().findFragmentByTag("outdoor_download_failed");
			if (prevFrag != null) {
				fragTransaction.remove(prevFrag);
			}
			
			fragTransaction.add(DownloadAlerDialogFragement.
					newInstance(title, message), "outdoor_download_failed").commitAllowingStateLoss();
		} catch (IllegalStateException e) {
			ALog.e(ALog.ERROR, e.getMessage());
		}
	}
	
	public void startCityAQIHistoryTask(String areaID) {
		if (OutdoorController.getInstance().isPhilipsSetupWifiSelected()) return;
		
		TimeZone timeZoneChina = TimeZone.getTimeZone("GMT+8");
		TimeZone timeZoneCurrent = Calendar.getInstance().getTimeZone();
		
		//Time difference between time zone and GMT
		int offsetChina = timeZoneChina.getOffset(Calendar.getInstance().getTimeInMillis());
		int offsetCurrent = timeZoneCurrent.getOffset(Calendar.getInstance().getTimeInMillis());
		int offset = offsetChina - offsetCurrent;
		
		Calendar cal = Calendar.getInstance(timeZoneChina);
		// cal.getTimeInMillis() selected time zone, time in milli seconds, it give same time all time zone
		// So we implemented time zone concept to get selected time zone, time in milli seconds
		
		TaskGetHttp aqiHistoricTask = new TaskGetHttp(OutdoorController.getInstance().buildURL(
				OutdoorController.BASE_URL_AQI, areaID, "air_his", 
				Utils.getDate((cal.getTimeInMillis() + offset - (1000 * 60 * 60 * 24 * 30l))) + "," 
				+ Utils.getDate(cal.getTimeInMillis() + offset), OutdoorController.APP_ID), 
				areaID, PurAirApplication.getAppContext(), this);
		aqiHistoricTask.start();
	}

	@Override
	public void receiveServerResponse(int responseCode, String responseData, String areaID) {
		ALog.i(ALog.OUTDOOR_DETAILS, "Outdoor Aqi downloaded response code: " + responseCode);
		if (responseCode == 200) {
			outdoorAQIs = DataParser.parseHistoricalAQIData(responseData, areaID);
			if (outdoorAQIs != null && !outdoorAQIs.isEmpty()) {
				addAQIHistoricData();
			}
		} else {
			handler.sendEmptyMessage(2);
		}
	}

	@Override
	public void onHourlyWeatherForecastReceived(final List<Weatherdto> weatherList) {
		if( weatherList != null ) {
			ALog.i(ALog.OUTDOOR_DETAILS, "Outdoor Weather received: "+weatherList.size()) ;
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					if (wetherScrollView.getChildCount() > 0) {
						wetherScrollView.removeAllViews();
					}
					wetherScrollView.addView(
							new WeatherReportLayout(OutdoorDetailsActivity.this, null, weatherList));
				}
			});
		}
	}

	@Override
	public void onWeatherForecastReceived(final List<ForecastWeatherDto> weatherList) {

		ALog.i(ALog.OUTDOOR_DETAILS, "Outdoor Weather received: "+weatherList.size()) ;
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				weatherProgressBar.setVisibility(View.GONE);
				if( weatherList != null ) {
					
					if (weatherReportLayout != null && weatherReportLayout.getChildCount() > 0) {
						weatherReportLayout.removeAllViews();
					}
					
					weatherReportLayout = 
							new WeatherReportLayout(OutdoorDetailsActivity.this, null, 0, weatherList);
					weatherReportLayout.setOrientation(LinearLayout.VERTICAL);
					wetherForcastLayout.addView(weatherReportLayout);
				}
			}
		});
	}

	@Override
	public void onAQIHisttReceived(List<OutdoorAQI> outdoorAQIHistory) {
		// TODO Auto-generated method stub
	}
	
}
