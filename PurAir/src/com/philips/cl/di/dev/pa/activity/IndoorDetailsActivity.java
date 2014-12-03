package com.philips.cl.di.dev.pa.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.ICPDownloadListener;
import com.philips.cl.di.dev.pa.dashboard.IndoorDashboardUtils;
import com.philips.cl.di.dev.pa.dashboard.PurifierCurrentCityData;
import com.philips.cl.di.dev.pa.dashboard.PurifierCurrentCityData.PurifierCurrentCityPercentListener;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.datamodel.IndoorTrendDto;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.fragment.DownloadAlerDialogFragement;
import com.philips.cl.di.dev.pa.fragment.IndoorAQIExplainedDialogFragment;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager.PurifierEvent;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Coordinates;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.GraphConst;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.AirView;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.dev.pa.view.GraphView;
import com.philips.icpinterface.data.Errors;

public class IndoorDetailsActivity extends BaseActivity implements OnClickListener,
			ICPDownloadListener, AirPurifierEventListener, PurifierCurrentCityPercentListener {

	private PurAirDevice currentPurifier;

	private LinearLayout graphLayout;
	private TextView lastDayBtn, lastWeekBtn, lastFourWeekBtn;
	private TextView heading;
	private ImageView circleImg;
	private ImageView backgroundImage;
	private FontTextView msgFirst, msgSecond;
	private ViewGroup indoorBarChart, outdoorBarChart;
	private ProgressBar rdcpDownloadProgressBar;
	private FontTextView mode, filter, aqiStatusTxt, aqiSummary;
	private List<int[]> powerOnReadingsValues;
	private List<float[]> lastDayRDCPValues;
	private List<float[]> last7daysRDCPValues;
	private List<float[]> last4weeksRDCPValues;
	private List<Float> hrlyAqiValues;
	private List<Float> dailyAqiValues ;
	private List<Integer> goodAirInfos;
	private List<Integer> currentCityGoodAirInfos;
	private Coordinates coordinates;
	private int dayIndex = 0;

	private String outdoorTitle = PurAirApplication.getAppContext().getString(R.string.good);

	private int powerOnReadings[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0 };

	public float lastDayRDCPVal[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,
			-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F};

	public float last7daysRDCPVal[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F};

	public float last4weeksRDCPVal[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,
			-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		currentPurifier = PurifierManager.getInstance().getCurrentPurifier();
		if (currentPurifier == null) {
			ALog.d(ALog.INDOOR_DETAILS, "Not starting indoor activity - Current purifier cannot be null");
			finish();
			return;
		}

		setContentView(R.layout.activity_trends_indoor);
		coordinates = Coordinates.getInstance(this);
		init();
		
		//Purifier current city good air quality historic data download
		PurifierCurrentCityData.getInstance().setListener(this);
		if (currentPurifier != null) {
			PurifierCurrentCityData.getInstance().startCurrentCityAreaIdTask(
					currentPurifier.getLatitude(), currentPurifier.getLongitude());
		}

		try {
			initActionBar();
		} catch (ClassCastException e) {
			ALog.e(ALog.INDOOR_DETAILS, "Actionbar: " + "Error: " + e.getMessage());
		}

		getRDCPValue();

		if (currentPurifier == null) return;
		getDataFromDashboard(currentPurifier.getAirPortInfo());
	}

	private void getRDCPValue() {
		if (currentPurifier == null) return;
		rdcpDownloadProgressBar.setVisibility(View.VISIBLE);
		CPPController.getInstance(this).setDownloadDataListener(this) ;
		CPPController.getInstance(this).downloadDataFromCPP(Utils.getCPPQuery(currentPurifier), 2048) ;
	}

	@Override
	protected void onResume() {
		super.onResume();
		DiscoveryManager.getInstance().start(null);
		PurifierManager.getInstance().addAirPurifierEventListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		DiscoveryManager.getInstance().stop();
		PurifierManager.getInstance().removeAirPurifierEventListener(this);
	}

	/**
	 * Initialize UI widget
	 * */
	private void init() {
		initList();

		graphLayout = (LinearLayout) findViewById(R.id.trendsOutdoorlayoutGraph);

		lastDayBtn = (TextView) findViewById(R.id.detailsOutdoorLastDayLabel);
		lastWeekBtn = (TextView) findViewById(R.id.detailsOutdoorLastWeekLabel);
		lastFourWeekBtn = (TextView) findViewById(R.id.detailsOutdoorLastFourWeekLabel);

		backgroundImage = (ImageView) findViewById(R.id.detailsOutdoorDbImg); 
		circleImg = (ImageView) findViewById(R.id.inDetailsDbCircle); 

		msgFirst = (FontTextView) findViewById(R.id.idFirstMsg);
		msgSecond = (FontTextView) findViewById(R.id.idSecondMsg);
		mode = (FontTextView) findViewById(R.id.inModeType);
		filter = (FontTextView) findViewById(R.id.inFilterType);
		aqiStatusTxt = (FontTextView) findViewById(R.id.inDetailsDbStatus);
		aqiSummary = (FontTextView) findViewById(R.id.inDetailsDbSummary);

		rdcpDownloadProgressBar = (ProgressBar) findViewById(R.id.rdcpDownloadProgressBar);
		indoorBarChart = (RelativeLayout) findViewById(R.id.indoorDashboardBarPerc);
		outdoorBarChart = (RelativeLayout) findViewById(R.id.outdoorDashboardBarPerc);

		initClickListener();
	}
	
	private void initList() {
		powerOnReadingsValues = new ArrayList<int[]>();
		lastDayRDCPValues = new ArrayList<float[]>();
		last7daysRDCPValues = new ArrayList<float[]>();
		last4weeksRDCPValues = new ArrayList<float[]>();
		goodAirInfos = new ArrayList<Integer>();
		currentCityGoodAirInfos = new ArrayList<Integer>();
	}
	
	private void initClickListener() {
		/**
		 * Set click listener
		 * */
		lastDayBtn.setOnClickListener(this);
		lastWeekBtn.setOnClickListener(this);
		lastFourWeekBtn.setOnClickListener(this);
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
	public void setActionBarTitle(String name) {    	
		heading = (TextView) findViewById(R.id.action_bar_title);
		//If Chinese language selected set font-type-face normal
		if( LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")
				|| LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
			heading.setTypeface(Typeface.DEFAULT);
		} else {
			heading.setTypeface(Fonts.getGillsansLight(this));
		}
		if (name == null || name.trim().length() == 0) {
			heading.setText("");
		} else {
			heading.setText(name);
		}
	}

	/**
	 * Parsing reading
	 * */
	public void addAqiReading() {

		clearLists();
		addLastDayAQIIntoList();
		addLastMonthAQIIntoList();
		callGraphViewOnClickEvent(0, lastDayRDCPValues);
	}

	private void addLastDayAQIIntoList() {
		/**Last day*/
		if (hrlyAqiValues != null && hrlyAqiValues.size() == 24) {

			for (int i = 0; i < lastDayRDCPVal.length; i++) {
				lastDayRDCPVal[i] = hrlyAqiValues.get(i);
			}
		}

		lastDayRDCPValues.add(lastDayRDCPVal);
		powerOnReadingsValues.add(powerOnReadings);
	}

	private void addLastMonthAQIIntoList() {
		/**Last 7 days and last 4 weeks*/
		int tempIndex = 0;

		if (dailyAqiValues != null && dailyAqiValues.size() > 0
				&& dailyAqiValues.size() == 28) {

			for (int i = 0; i < last4weeksRDCPVal.length; i++) {
				if (i > 20) {
					last7daysRDCPVal[tempIndex] = dailyAqiValues.get(i);
					tempIndex++;
				}
				last4weeksRDCPVal[i] = dailyAqiValues.get(i);
			}
		}
		last7daysRDCPValues.add(last7daysRDCPVal);
		last4weeksRDCPValues.add(last4weeksRDCPVal);
	}

	/**
	 * Clear list if old data available.
	 */
	private void clearLists() {
		if (lastDayRDCPValues.size() > 0) lastDayRDCPValues.clear();

		if (powerOnReadingsValues.size() > 0) powerOnReadingsValues.clear();

		if (last7daysRDCPValues.size() > 0) last7daysRDCPValues.clear();

		if (last4weeksRDCPValues.size() > 0) last4weeksRDCPValues.clear();
	}

	/**
	 * onClick
	 * */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.detailsOutdoorLastDayLabel: {
			callGraphViewOnClickEvent(0, lastDayRDCPValues);
			break;
		}
		case R.id.detailsOutdoorLastWeekLabel: {
			callGraphViewOnClickEvent(1, last7daysRDCPValues);
			break;
		}
		case R.id.detailsOutdoorLastFourWeekLabel: {
			callGraphViewOnClickEvent(2, last4weeksRDCPValues);
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

	private void callGraphViewOnClickEvent(int index, List<float[]> rdcpValues) {
		dayIndex = index;
		setViewOnClick(index);
		removeChildViewFromBar();
		addBarChartView(indoorBarChart, goodAirInfos, index);
		showOutdoorBarChart();
		
		if (rdcpValues != null && rdcpValues.size() > 0) {
			graphLayout.addView(new GraphView(this, rdcpValues, null, coordinates, 0));
		}	
	}
	
	private void addBarChartView(ViewGroup viewGroup, List<Integer> goodAirList, int index) {
		if (goodAirList != null && goodAirList.size() > 2) {
			FontTextView percentTxt = new FontTextView(this);
			RelativeLayout.LayoutParams percentTxtParams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			percentTxtParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			percentTxt.setGravity(Gravity.CENTER);
			percentTxt.setTextColor(Color.WHITE);
			percentTxt.setTextSize(21);
			if (viewGroup.getChildCount() > 0) {
				viewGroup.removeAllViews();
			}
			viewGroup.addView(new AirView(this, goodAirList.get(index), 60, 80));
			percentTxt.setText(goodAirList.get(index) + "%");
			viewGroup.addView(percentTxt, percentTxtParams);
		}
	}

	private void setViewOnClick(int index) {
		switch (index) {
		case 0:
			lastDayBtn.setTextColor(GraphConst.COLOR_DODLE_BLUE);
			lastWeekBtn.setTextColor(Color.LTGRAY);
			lastFourWeekBtn.setTextColor(Color.LTGRAY);
			msgFirst.setText(getString(R.string.aqi_message_last_day));
			msgSecond.setText(getString(R.string.detail_aiq_message_last_day));
			break;
		case 1:
			lastDayBtn.setTextColor(Color.LTGRAY);
			lastWeekBtn.setTextColor(GraphConst.COLOR_DODLE_BLUE);
			lastFourWeekBtn.setTextColor(Color.LTGRAY);
			msgFirst.setText(getString(R.string.aqi_message_last7day));
			msgSecond.setText(getString(R.string.detail_aiq_message_last7day));		
			break;
		case 2:
			lastDayBtn.setTextColor(Color.LTGRAY);
			lastWeekBtn.setTextColor(Color.LTGRAY);
			lastFourWeekBtn.setTextColor(GraphConst.COLOR_DODLE_BLUE);
			msgFirst.setText(getString(R.string.aqi_message_last4week));
			msgSecond.setText(getString(R.string.detail_aiq_message_last4week));
			break;
		default:
			break;
		}
	}

	private void removeChildViewFromBar() {
		if (graphLayout.getChildCount() > 0) {
			graphLayout.removeAllViews();
		}
	}

	private void updatePurifierUIFields() {
		PurAirDevice purifier = PurifierManager.getInstance().getCurrentPurifier();
		if (purifier == null) return;

		final AirPortInfo airPortInfo = purifier.getAirPortInfo();
		
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				getDataFromDashboard(airPortInfo);
			}
		});
	}

	private void getDataFromDashboard(AirPortInfo airPortInfo) {
		if (currentPurifier == null) return;
		setActionBarTitle(currentPurifier.getName());
		if (airPortInfo == null) return;
		/**
		 * Updating all the details in the screen, which is passed from Dashboard
		 */

		if (ConnectionState.DISCONNECTED == currentPurifier.getConnectionState()) {
			disconnect(getString(R.string.not_connected));
			//set image background
			backgroundImage.setImageResource(R.drawable.home_indoor_bg_2x);
			aqiSummary.setText(AppConstants.EMPTY_STRING) ;
		} 
		else {
			if(!airPortInfo.getPowerMode().equals(AppConstants.POWER_ON)) {
				mode.setText(getString(R.string.off));
			}
			else {
				mode.setText(getString((airPortInfo.getFanSpeed().getFanSpeedTextResID())));
			}
			filter.setText(IndoorDashboardUtils.getFilterStatus(airPortInfo));

			int indoorAQI = airPortInfo.getIndoorAQI();
			
			circleImg.setImageDrawable(Utils.getIndoorAQICircleBackground(this, indoorAQI));

			String [] aqiStatusAndCommentArray = Utils.getAQIStatusAndSummary(indoorAQI) ;
			if( aqiStatusAndCommentArray == null || aqiStatusAndCommentArray.length < 2 ) return ;
			aqiStatusTxt.setText(aqiStatusAndCommentArray[0]);
			aqiStatusTxt.setTextSize(22.0f);
			aqiSummary.setText(aqiStatusAndCommentArray[1]) ;
			backgroundImage.setImageResource(Utils.getBackgroundResource(indoorAQI));
			showAlertErrorAirPort(airPortInfo);
		}
	}
	
	private void showAlertErrorAirPort(AirPortInfo airPortInfo) {
		String powerMode = airPortInfo.getPowerMode();
		if (AppConstants.POWER_STATUS_C.equalsIgnoreCase(powerMode)) {
			disconnect(getString(R.string.cover_open));
		} else if (AppConstants.POWER_STATUS_E.equalsIgnoreCase(powerMode)) {
			disconnect(getString(R.string.error_air_port));
		}
	}
	
	private void disconnect(String message) {
		mode.setText(getString(R.string.off));
		filter.setText(AppConstants.EMPTY_STRING);
		circleImg.setImageResource(R.drawable.grey_circle_2x);
		aqiStatusTxt.setTextSize(18.0f);
		aqiStatusTxt.setText(message);
	}

	public void aqiAnalysisClick(View v) {
		try {
			FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
			
			Fragment prevFrag = getSupportFragmentManager().findFragmentByTag("indoor_aqi_analysis");
			if (prevFrag != null) {
				fragTransaction.remove(prevFrag);
			}
			
			fragTransaction.add(IndoorAQIExplainedDialogFragment.
					newInstance(outdoorTitle, outdoorTitle), "indoor_aqi_analysis").commit();
		} catch (IllegalStateException e) {
			ALog.e(ALog.INDOOR_DETAILS, "Error: " + e.getMessage());
		}
	}
	
	/**
	 * Show alert dialog AQI historic data download failed
	 */
	private void showAlertDialogHistoryDoawnload(String title, String message) {
		try {
			FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
			
			Fragment prevFrag = getSupportFragmentManager().findFragmentByTag("alert_aqi_historic_download_failed");
			if (prevFrag != null) {
				fragTransaction.remove(prevFrag);
			}
			
			fragTransaction.add(DownloadAlerDialogFragement.
					newInstance(title, message), "alert_aqi_historic_download_failed").commitAllowingStateLoss();
		} catch (IllegalStateException e) {
			ALog.e(ALog.INDOOR_DETAILS, "Error: " + e.getMessage());
		}
	}

	@SuppressLint("HandlerLeak")
	private final Handler handlerDownload = new Handler() {
		public void handleMessage(Message msg) {
			rdcpDownloadProgressBar.setVisibility(View.GONE);
			if ( msg.what == 1 ) {
				showAlertDialogHistoryDoawnload(getString(R.string.aqi_history), 
						getString(R.string.aqi_history_not_avialable));
			} else if ( msg.what == 2 ) {
				showAlertDialogHistoryDoawnload(getString(R.string.aqi_history), 
						getString(R.string.aqi_history_download_failed));
			} else if ( msg.what == 3 ) {
				addAqiReading();
			}
		};
	};

	/**
	 * rDcp values download
	 */
	@Override
	public void onDataDownload(int status, String downloadedData) {
		ALog.i(ALog.INDOOR_DETAILS, "onDataDownload status: " + status);
		String eui64 = currentPurifier.getEui64();
		if( status == Errors.SUCCESS ) {
			if (downloadedData != null && !downloadedData.isEmpty()) {
				Utils.getIndoorAqiValues(downloadedData, currentPurifier.getEui64()) ;
				
				IndoorTrendDto inDto = SessionDto.getInstance().getIndoorTrendDto(eui64);
				
				if (inDto != null) {
					hrlyAqiValues = inDto.getHourlyList() ;
					dailyAqiValues = inDto.getDailyList() ;
					addGoodAQIIntoList(inDto);
					addCurrentCityGoodAQIIntoList(PurifierCurrentCityData.getInstance().getPurifierCurrentCityGoodAQ(currentPurifier.getEui64()));
				}
				handlerDownload.sendEmptyMessage(3);

			} else {
				handlerDownload.sendEmptyMessage(1);
			}
		} else {
			handlerDownload.sendEmptyMessage(2);

		}
	}

	private void addGoodAQIIntoList(IndoorTrendDto trendDto) {
		if (trendDto.getGoodAirQualityList() == null) return;
		if (!goodAirInfos.isEmpty())  goodAirInfos.clear();
		goodAirInfos.addAll(trendDto.getGoodAirQualityList());
	}
	
	private void addCurrentCityGoodAQIIntoList(List<Integer> goodAQ) {
		if (goodAQ == null || goodAQ.isEmpty()) return;
		if (!currentCityGoodAirInfos.isEmpty())  currentCityGoodAirInfos.clear();
		currentCityGoodAirInfos.addAll(goodAQ);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	public void onAirPurifierChanged() {
		updatePurifierUIFields();
	}

	@Override
	public void onAirPurifierEventReceived() {
		updatePurifierUIFields();
	}

	@Override
	public void onFirmwareEventReceived() {
		//NOP
	}

	@Override
	public void onErrorOccurred(PurifierEvent purifierEvent) {
		// NOP
	}
	
	private void showOutdoorBarChart() {
		if (goodAirInfos != null && !goodAirInfos.isEmpty()) {
			addBarChartView(outdoorBarChart, currentCityGoodAirInfos, dayIndex);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Coordinates.reset();
		PurifierCurrentCityData.getInstance().removeListener();
	}

	//Current city percentage calculation task
	@Override
	public void onTaskComplete() {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (currentPurifier == null) return;
				addCurrentCityGoodAQIIntoList(PurifierCurrentCityData.getInstance()
						.getPurifierCurrentCityGoodAQ(currentPurifier.getEui64()));
				showOutdoorBarChart();
				ALog.i(ALog.INDOOR_DETAILS, "Updated purifier current city good air: " + currentCityGoodAirInfos);
			}
		});
	}
}

