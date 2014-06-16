package com.philips.cl.di.dev.pa.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.ICPDownloadListener;
import com.philips.cl.di.dev.pa.dashboard.HomeOutdoorData;
import com.philips.cl.di.dev.pa.dashboard.IndoorDashboardUtils;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.fragment.IndoorAQIExplainedDialogFragment;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager.PURIFIER_EVENT;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Coordinates;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.GraphConst;
import com.philips.cl.di.dev.pa.util.PercentDetailsClickListener;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.dev.pa.view.GraphView;
import com.philips.cl.di.dev.pa.view.PercentBarLayout;
import com.philips.icpinterface.data.Errors;

public class IndoorDetailsActivity extends BaseActivity implements OnClickListener,
		PercentDetailsClickListener, ICPDownloadListener, AirPurifierEventListener {

	private PurAirDevice currentPurifier;
	
	private LinearLayout graphLayout;
	private TextView lastDayBtn, lastWeekBtn, lastFourWeekBtn;
	private TextView heading;
	private ImageView circleImg;
	private FontTextView msgFirst, msgSecond, indoorDbIndexName;
	private ImageView indexBottBg;
	private HorizontalScrollView horizontalScrollView;
	private ProgressBar rdcpDownloadProgressBar;
	private PercentBarLayout percentBarLayout;
	private FontTextView barTopNum, barTopName, selectedIndexBottom;
	private FontTextView mode, filter, aqiStatus, aqiSummary;
	private List<int[]> powerOnReadingsValues;
	private List<float[]> lastDayRDCPValues;
	private List<float[]> last7daysRDCPValues;
	private List<float[]> last4weeksRDCPValues;

	private List<Float> hrlyAqiValues;
	private List<Float> dailyAqiValues ;
	private List<Integer> goodAirInfos;
	private List<Integer> powerOnStatusList;
	private Coordinates coordinates;

	private String outdoorTitle = "";

	private int goodAirCount = 0;
	private int totalAirCount = 0;
	private Handler handler = new Handler();

	private int powerOnReadings[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0 };

	public float lastDayRDCPVal[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,
			-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, };
	
	public float last7daysRDCPVal[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F};

	public float last4weeksRDCPVal[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,
			-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		currentPurifier = PurifierManager.getInstance().getCurrentPurifier();
		if (currentPurifier == null) {
			ALog.d(ALog.INDOOR_DETAILS, "Not starting indoor activity - Current purifier cannot be null");
			finish();
		}
		
		setContentView(R.layout.activity_trends_indoor);
		HomeOutdoorData.getInstance().startOutdoorAQITask();
		coordinates = Coordinates.getInstance(this);
		initializeUI();

		try {
			initActionBar();
		} catch (ClassCastException e) {
			ALog.e(ALog.INDOOR_DETAILS, "Actionbar: " + e.getMessage());
		}
		rdcpDownloadProgressBar.setVisibility(View.VISIBLE);
		if (SessionDto.getInstance().getIndoorTrendDto() == null &&
				CPPController.getInstance(this) != null) {
				CPPController.getInstance(this).setDownloadDataListener(this) ;
				CPPController.getInstance(this).downloadDataFromCPP(Utils.getCPPQuery(currentPurifier), 2048) ;
		} 
		else if( SessionDto.getInstance().getIndoorTrendDto()  != null ) {
			hrlyAqiValues = SessionDto.getInstance().getIndoorTrendDto().getHourlyList() ;
			dailyAqiValues = SessionDto.getInstance().getIndoorTrendDto().getDailyList() ;
			powerOnStatusList = SessionDto.getInstance().getIndoorTrendDto().getPowerDetailsList() ;
			addAqiReading();
		}
		if (currentPurifier == null) return;
		getDataFromDashboard(currentPurifier.getAirPortInfo());
	}

	@Override
	protected void onResume() {
		super.onResume();
		PurifierManager.getInstance().addAirPurifierEventListener(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		PurifierManager.getInstance().removeAirPurifierEventListener(this);
	}
	
	private Runnable downloadDataRunnble = new Runnable() {

		@Override
		public void run() {
			handler.removeCallbacks(downloadDataRunnble);
			rdcpDownloadProgressBar.setVisibility(View.GONE);
			callGraphViewOnClickEvent(0, lastDayRDCPValues);
		}
	};


	
	/**
	 * Initialize UI widget
	 * */
	private void initializeUI() {
		powerOnReadingsValues = new ArrayList<int[]>();
		lastDayRDCPValues = new ArrayList<float[]>();
		last7daysRDCPValues = new ArrayList<float[]>();
		last4weeksRDCPValues = new ArrayList<float[]>();
		goodAirInfos = new ArrayList<Integer>();

		graphLayout = (LinearLayout) findViewById(R.id.trendsOutdoorlayoutGraph);

		lastDayBtn = (TextView) findViewById(R.id.detailsOutdoorLastDayLabel);
		lastWeekBtn = (TextView) findViewById(R.id.detailsOutdoorLastWeekLabel);
		lastFourWeekBtn = (TextView) findViewById(R.id.detailsOutdoorLastFourWeekLabel);

//		modeIcon = (ImageView) findViewById(R.id.inModeIcon); 
//		filterIcon = (ImageView) findViewById(R.id.inFilterIcon); 
		circleImg = (ImageView) findViewById(R.id.inDetailsDbCircle); 
		indexBottBg= (ImageView) findViewById(R.id.indoorDbIndexBottBg); 

		msgFirst = (FontTextView) findViewById(R.id.idFirstMsg);
		msgSecond = (FontTextView) findViewById(R.id.idSecondMsg);
//		modeLabel = (FontTextView) findViewById(R.id.inModeTxt);
		mode = (FontTextView) findViewById(R.id.inModeType);
//		filterLabel = (FontTextView) findViewById(R.id.inFilterTxt);
		filter = (FontTextView) findViewById(R.id.inFilterType);
		aqiStatus = (FontTextView) findViewById(R.id.inDetailsDbStatus);
		aqiSummary = (FontTextView) findViewById(R.id.inDetailsDbSummary);
		indoorDbIndexName = (FontTextView) findViewById(R.id.indoorDbIndexName);

		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.indoorDbHorizontalScroll);
		barTopNum = (FontTextView) findViewById(R.id.indoorDbBarTopNum);
		barTopName = (FontTextView) findViewById(R.id.indoorDbBarTopName);
		selectedIndexBottom = (FontTextView) findViewById(R.id.indoorDbIndexBott);
		selectedIndexBottom.setText(String.valueOf(1));
		
		rdcpDownloadProgressBar = (ProgressBar) findViewById(R.id.rdcpDownloadProgressBar);
		
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
		((ImageView)view.findViewById(R.id.back_to_home_img)).setVisibility(View.GONE);
		((ImageView)view.findViewById(R.id.add_location_img)).setVisibility(View.GONE);
		actionBar.setCustomView(view);
	}
	
	
	/*Sets Action bar title */
	public void setActionBarTitle(String name) {    	
		heading = (TextView) findViewById(R.id.action_bar_title);
		heading.setTypeface(Fonts.getGillsansLight(this));
		heading.setTextSize(24);
		if (name == null || name.trim().length() == 0) {
			heading.setText("");
			indoorDbIndexName.setText("");
			barTopName.setText("");
		} else {
			heading.setText(name);
			indoorDbIndexName.setText(name);
			barTopName.setText(name);
		}
	}

	/**
	 * Parsing reading
	 * */
	public void addAqiReading() {

		if (goodAirInfos.size() > 0) {
			goodAirInfos.clear();
		}

		/**Last day*/
		if (lastDayRDCPValues.size() > 0) {
			lastDayRDCPValues.clear();
		} 

		if (powerOnReadingsValues.size() > 0) {
			powerOnReadingsValues.clear();
		} 

		goodAirCount = 0;
		totalAirCount = 0;
		if (hrlyAqiValues != null && hrlyAqiValues.size() == 24) {

			for (int i = 0; i < lastDayRDCPVal.length; i++) {
				lastDayRDCPVal[i] = hrlyAqiValues.get(i);
				if (powerOnStatusList != null && powerOnStatusList.size() == 24) {
					powerOnReadings[i] = powerOnStatusList.get(i);
				} 
				if (hrlyAqiValues.get(i) != -1) {
					if (hrlyAqiValues.get(i) <= 2) {
						goodAirCount ++;
					}
					totalAirCount ++;
				}
			}
		}
		goodAirInfos.add(Utils.getPercentage(goodAirCount, totalAirCount));
		lastDayRDCPValues.add(lastDayRDCPVal);
		powerOnReadingsValues.add(powerOnReadings);

		/**Last 7 days and last 4 weeks*/
		if (last7daysRDCPValues.size() > 0) {
			last7daysRDCPValues.clear();
		} 

		if (last4weeksRDCPValues.size() > 0) {
			last4weeksRDCPValues.clear();
		} 

		int tempIndex = 0;
		goodAirCount = 0;
		totalAirCount = 0;
		int tempGood = 0;
		int tempCount = 0;
		if (dailyAqiValues != null && dailyAqiValues.size() > 0
				&& dailyAqiValues.size() == 28) {

			for (int i = 0; i < last4weeksRDCPVal.length; i++) {
				if (i > 20) {
					last7daysRDCPVal[tempIndex] = dailyAqiValues.get(i);
					tempIndex++;
					if (dailyAqiValues.get(i) != -1) {
						if (dailyAqiValues.get(i) <= 2) {
							tempGood ++;
						}
						tempCount ++;
					}
				}
				last4weeksRDCPVal[i] = dailyAqiValues.get(i);
				if (dailyAqiValues.get(i) != -1) {
					if (dailyAqiValues.get(i) <= 2) {
						goodAirCount ++;
					}
					totalAirCount ++;
				}
			}

		}
		goodAirInfos.add(Utils.getPercentage(tempGood, tempCount));
		goodAirInfos.add(Utils.getPercentage(goodAirCount, totalAirCount));
		last7daysRDCPValues.add(last7daysRDCPVal);
		last4weeksRDCPValues.add(last4weeksRDCPVal);

		handler.removeCallbacks(downloadDataRunnble);
		handler.post(downloadDataRunnble);
	}

	
	/**
	 * onClick
	 * */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.detailsOutdoorLastDayLabel: {
			callGraphViewOnClickEvent(0, lastDayRDCPValues);
			
			lastDayBtn.setTextColor(GraphConst.COLOR_DODLE_BLUE);
			lastWeekBtn.setTextColor(Color.LTGRAY);
			lastFourWeekBtn.setTextColor(Color.LTGRAY);
			msgFirst.setText(getString(R.string.detail_aiq_message_last_day));
			msgSecond.setText(getString(R.string.detail_aiq_message_last_day));
			break;
		}
		case R.id.detailsOutdoorLastWeekLabel: {
			callGraphViewOnClickEvent(1, last7daysRDCPValues);

			lastDayBtn.setTextColor(Color.LTGRAY);
			lastWeekBtn.setTextColor(GraphConst.COLOR_DODLE_BLUE);
			lastFourWeekBtn.setTextColor(Color.LTGRAY);
			msgFirst.setText(getString(R.string.detail_aiq_message_last7day));
			msgSecond.setText(getString(R.string.detail_aiq_message_last7day));
			break;
		}
		case R.id.detailsOutdoorLastFourWeekLabel: {
			callGraphViewOnClickEvent(2, last4weeksRDCPValues);
			
			lastDayBtn.setTextColor(Color.LTGRAY);
			lastWeekBtn.setTextColor(Color.LTGRAY);
			lastFourWeekBtn.setTextColor(GraphConst.COLOR_DODLE_BLUE);
			msgFirst.setText(getString(R.string.detail_aiq_message_last4week));
			msgSecond.setText(getString(R.string.detail_aiq_message_last4week));
			break;
		}
		default:
			break;
		}

	}
	
	private void callGraphViewOnClickEvent(int index, List<float[]> rdcpValues) {
		removeChildViewFromBar();
		if (goodAirInfos != null && goodAirInfos.size() > 0) {
			percentBarLayout = new PercentBarLayout(IndoorDetailsActivity.this,
					null, goodAirInfos, this, index, 0);
			percentBarLayout.setClickable(true);
			horizontalScrollView.addView(percentBarLayout);
		}

		if (rdcpValues != null && rdcpValues.size() > 0) {
			graphLayout.addView(new GraphView(this, rdcpValues, null, coordinates, 0, indexBottBg));
		}	
	}

	private void removeChildViewFromBar() {

		barTopNum.setText("1");
		//barTopName.setText("Living room");
		selectedIndexBottom.setText("1");

		if (horizontalScrollView.getChildCount() > 0) {
			horizontalScrollView.removeAllViews();
		}

		if (graphLayout.getChildCount() > 0) {
			graphLayout.removeAllViews();
		}
	}

	@Override
	public void clickedPosition(int position, int index) {
		selectedIndexBottom.setText(""+(position+1));
		barTopNum.setText(""+(position+1));

		if (horizontalScrollView.getChildCount() > 0) {
			horizontalScrollView.removeAllViews();
		}
		if (goodAirInfos != null && goodAirInfos.size() > 0) {
			percentBarLayout = new PercentBarLayout(IndoorDetailsActivity.this, 
					null, goodAirInfos, this, index, position);
			percentBarLayout.setClickable(true);
			horizontalScrollView.addView(percentBarLayout);
		}

		if (graphLayout.getChildCount() > 0) {
			graphLayout.removeViewAt(0);
		}
	}

	/**
	 * 
	 */
	private void getDataFromDashboard(AirPortInfo airPortInfo) {
		String purifierName = "";
		if (currentPurifier != null) {
			purifierName = currentPurifier.getName();
		}
		setActionBarTitle(purifierName);
		/**
		 * Updating all the details in the screen, which is passed from Dashboard
		 */
		if (airPortInfo != null) {

			if (ConnectionState.DISCONNECTED == currentPurifier.getConnectionState()) {
				mode.setText(getString(R.string.off));
				aqiStatus.setText(getString(R.string.no_connection));
				aqiSummary.setText(AppConstants.EMPTY_STRING) ;
			} 
			else {
				if(!airPortInfo.getPowerMode().equals(AppConstants.POWER_ON)) {
					mode.setText(getString(R.string.off));
				}
				else {
					mode.setText(getString(IndoorDashboardUtils.getFanSpeedText(airPortInfo.getFanSpeed())));
				}
				filter.setText(IndoorDashboardUtils.getFilterStatus(airPortInfo));

				int indoorAQI = airPortInfo.getIndoorAQI();
				ALog.i(ALog.INDOOR_DETAILS, "indoorAQI: " + indoorAQI);
				circleImg.setImageDrawable(Utils.getIndoorAQICircleBackground(this, indoorAQI));

				String [] aqiStatusAndCommentArray = Utils.getAQIStatusAndSummary(indoorAQI) ;
				if( aqiStatusAndCommentArray == null || aqiStatusAndCommentArray.length < 2 ) {
					return ;
				}
				aqiStatus.setText(aqiStatusAndCommentArray[0]);
				aqiSummary.setText(aqiStatusAndCommentArray[1]) ;
			}
		}
	}

	public void aqiAnalysisClick(View v) {
		FragmentManager fragMan = getSupportFragmentManager();
		fragMan.beginTransaction().add(IndoorAQIExplainedDialogFragment.newInstance(aqiStatus.getText().toString(), outdoorTitle), "outdoor").commit();
	}

	/**
	 * rDcp values download
	 */
	@Override
	public void onDataDownload(int status, String downloadedData) {
		ALog.i(ALog.INDOOR_DETAILS, "onDataDownload status: " + status);
		if( status == Errors.SUCCESS) {
			Utils.getIndoorAqiValues(downloadedData) ;
			
			if( SessionDto.getInstance().getIndoorTrendDto() != null ) {
				hrlyAqiValues = SessionDto.getInstance().getIndoorTrendDto().getHourlyList() ;
				dailyAqiValues = SessionDto.getInstance().getIndoorTrendDto().getDailyList() ;
				powerOnStatusList = SessionDto.getInstance().getIndoorTrendDto().getPowerDetailsList() ;
			}
			addAqiReading();
		} else {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					rdcpDownloadProgressBar.setVisibility(View.GONE);
					Toast.makeText(getApplicationContext(), 
							getString(R.string.download_failed), Toast.LENGTH_LONG).show();
				}
			});
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		handler.removeCallbacks(downloadDataRunnble);
		finish();
	}

	@Override
	public void onAirPurifierChanged() {
		// NOP
	}
	
	@Override
	public void onAirPurifierEventReceived() {
		PurAirDevice purifier = PurifierManager.getInstance().getCurrentPurifier();
		if (purifier == null) return;
		
		final AirPortInfo airPortInfo = purifier.getAirPortInfo();
		//TODO : Update fields using this information.
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				getDataFromDashboard(airPortInfo);
			}
		});
	}

	@Override
	public void onFirmwareEventReceived() {
		//NOP
	}

	@Override
	public void onErrorOccurred(PURIFIER_EVENT purifierEvent) {
		// TODO Auto-generated method stub
		
	}
}

