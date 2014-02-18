package com.philips.cl.di.dev.pa.screens;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.controller.CPPController;
import com.philips.cl.di.dev.pa.controller.SensorDataController;
import com.philips.cl.di.dev.pa.detail.utils.Coordinates;
import com.philips.cl.di.dev.pa.detail.utils.GraphConst;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.dto.IndoorHistoryDto;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.interfaces.ICPDownloadListener;
import com.philips.cl.di.dev.pa.interfaces.PercentDetailsClickListener;
import com.philips.cl.di.dev.pa.interfaces.SensorEventListener;
import com.philips.cl.di.dev.pa.screens.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.screens.customviews.GraphView;
import com.philips.cl.di.dev.pa.screens.customviews.PercentBarLayout;
import com.philips.cl.di.dev.pa.utils.DataParser;
import com.philips.cl.di.dev.pa.utils.Fonts;
import com.philips.cl.di.dev.pa.utils.Utils;
import com.philips.icpinterface.data.Errors;

public class IndoorDetailsActivity extends ActionBarActivity implements OnClickListener,
PercentDetailsClickListener, SensorEventListener, ICPDownloadListener {

	private ActionBar mActionBar;
	private final String TAG = "IndoorDetailsActivity";
	private LinearLayout graphLayout;
	private TextView lastDayBtn, lastWeekBtn, lastFourWeekBtn;
	private TextView heading;
	private ImageView circleImg, modeIcon, filterIcon;
	private CustomTextView msgFirst, msgSecond;
	private ImageView indexBottBg;
	private HorizontalScrollView horizontalScrollView;
	private PercentBarLayout percentBarLayout;
	private CustomTextView barTopNum, barTopName, selectedIndexBottom;
	private CustomTextView modeLabel, filterLabel, mode, filter, aqiStatus, aqiSummary;
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
	private String startDate;
	private String startDateHr;
	private String endDate;
	private String endDateHr;
	private Handler handler = new Handler();

	private int powerOnReadings[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0 };

	private float lastDayRDCPVal[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,
			-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, };

	private float last7daysRDCPVal[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F};

	private float last4weeksRDCPVal[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,
			-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_trends_indoor);
		coordinates = Coordinates.getInstance(this);
		initializeUI();

		SensorDataController.getInstance(this).addListener(this) ;

		//parseReading();

		initActionBar();
		setActionBarTitle();

		getDataFromDashboard();


		if (SessionDto.getInstance().getIndoorTrendDto() == null &&
				CPPController.getInstance(this) != null) {
			if(CPPController.getInstance(this).isSignOn()) {
				CPPController.getInstance(this).setDownloadDataListener(this) ;
				//"Clientid=1c5a6bfffe6341fe;datatype=airquality.1;startDate=2014-01-12T05:46:05.1508314Z;endDate=2014-02-13T06:46:05.1508314Z"
				CPPController.getInstance(this).downloadDataFromCPP(Utils.getCPPQuery(this), 2048) ;
			}
			else {
				Toast.makeText(this, "Please signon", Toast.LENGTH_LONG).show() ;
			}
		} 
		else if( SessionDto.getInstance().getIndoorTrendDto()  != null ) {
			hrlyAqiValues = SessionDto.getInstance().getIndoorTrendDto().getHourlyList() ;
			dailyAqiValues = SessionDto.getInstance().getIndoorTrendDto().getDailyList() ;
			powerOnStatusList = SessionDto.getInstance().getIndoorTrendDto().getPowerDetailsList() ;
			parseReading();
		}
	}


	private Runnable downloadDataRunnble = new Runnable() {

		@Override
		public void run() {
			handler.removeCallbacks(downloadDataRunnble);
			if (goodAirInfos != null && goodAirInfos.size() > 0) {
				percentBarLayout = new PercentBarLayout(IndoorDetailsActivity.this,
						null, goodAirInfos, IndoorDetailsActivity.this, 0, 0);
				percentBarLayout.setClickable(true);
				horizontalScrollView.addView(percentBarLayout);
			}
			if (lastDayRDCPValues != null && lastDayRDCPValues.size() > 0) {
				graphLayout.addView(new GraphView(IndoorDetailsActivity.this, 
						lastDayRDCPValues.get(0), lastDayRDCPValues, powerOnReadingsValues.get(0), 
						coordinates, 0, indexBottBg));
			}
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

		modeIcon = (ImageView) findViewById(R.id.inModeIcon); 
		filterIcon = (ImageView) findViewById(R.id.inFilterIcon); 
		circleImg = (ImageView) findViewById(R.id.inDetailsDbCircle); 
		indexBottBg= (ImageView) findViewById(R.id.indoorDbIndexBottBg); 

		msgFirst = (CustomTextView) findViewById(R.id.idFirstMsg);
		msgSecond = (CustomTextView) findViewById(R.id.idSecondMsg);
		modeLabel = (CustomTextView) findViewById(R.id.inModeTxt);
		mode = (CustomTextView) findViewById(R.id.inModeType);
		filterLabel = (CustomTextView) findViewById(R.id.inFilterTxt);
		filter = (CustomTextView) findViewById(R.id.inFilterType);
		aqiStatus = (CustomTextView) findViewById(R.id.inDetailsDbStatus);
		aqiSummary = (CustomTextView) findViewById(R.id.inDetailsDbSummary);


		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.indoorDbHorizontalScroll);
		barTopNum = (CustomTextView) findViewById(R.id.indoorDbBarTopNum);
		barTopName = (CustomTextView) findViewById(R.id.indoorDbBarTopName);
		selectedIndexBottom = (CustomTextView) findViewById(R.id.indoorDbIndexBott);

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
		heading.setText(getString(R.string.title_indoor_db));

	}

	/**
	 * Parsing reading
	 * */
	private void parseReading() {
		//For test
		//String js = "{\"Series\":[{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T06:25:03\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"60116\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T06:35:02\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"60140\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T06:45:02\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"60154\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T06:55:02\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"60169\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T07:05:02\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"60183\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T07:18:36\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"60203\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T07:28:36\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"60217\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T09:36:52\",\"DataKeyValuePairs\":{\"aqi\":\"10\",\"tfav\":\"60802\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T09:46:52\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"60854\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T09:56:52\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"60917\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T10:06:52\",\"DataKeyValuePairs\":{\"aqi\":\"10\",\"tfav\":\"60980\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T11:48:27\",\"DataKeyValuePairs\":{\"aqi\":\"24\",\"tfav\":\"61187\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T11:58:27\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"61230\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T12:08:27\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"61293\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T12:18:27\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"61356\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T12:28:27\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"61419\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T12:38:27\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"61482\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T13:31:24\",\"DataKeyValuePairs\":{\"aqi\":\"18\",\"tfav\":\"61561\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T13:41:24\",\"DataKeyValuePairs\":{\"aqi\":\"10\",\"tfav\":\"61579\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T13:57:46\",\"DataKeyValuePairs\":{\"aqi\":\"36\",\"tfav\":\"61598\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T14:12:46\",\"DataKeyValuePairs\":{\"aqi\":\"41\",\"tfav\":\"61691\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T14:55:18\",\"DataKeyValuePairs\":{\"aqi\":\"24\",\"tfav\":\"61709\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T15:10:24\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"61795\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T15:44:05\",\"DataKeyValuePairs\":{\"aqi\":\"56\",\"tfav\":\"61962\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T16:44:01\",\"DataKeyValuePairs\":{\"aqi\":\"45\",\"tfav\":\"62023\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T16:55:28\",\"DataKeyValuePairs\":{\"aqi\":\"50\",\"tfav\":\"62096\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-11T17:05:28\",\"DataKeyValuePairs\":{\"aqi\":\"30\",\"tfav\":\"62158\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T04:20:29\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"66412\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T04:30:29\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"66453\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T04:40:29\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"66472\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T04:55:16\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"66499\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T05:24:21\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"66541\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T05:42:34\",\"DataKeyValuePairs\":{\"aqi\":\"36\",\"tfav\":\"66591\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T06:16:17\",\"DataKeyValuePairs\":{\"aqi\":\"24\",\"tfav\":\"66668\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T12:48:15\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"67007\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T13:39:05\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"67196\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T13:49:05\",\"DataKeyValuePairs\":{\"aqi\":\"10\",\"tfav\":\"67217\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T13:59:01\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"67246\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T14:08:58\",\"DataKeyValuePairs\":{\"aqi\":\"18\",\"tfav\":\"67288\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T14:25:21\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"67364\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T14:35:21\",\"DataKeyValuePairs\":{\"aqi\":\"36\",\"tfav\":\"67427\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T14:49:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"67510\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T14:59:26\",\"DataKeyValuePairs\":{\"aqi\":\"18\",\"tfav\":\"67536\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T15:09:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"67563\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T15:19:26\",\"DataKeyValuePairs\":{\"aqi\":\"36\",\"tfav\":\"67606\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T15:29:26\",\"DataKeyValuePairs\":{\"aqi\":\"24\",\"tfav\":\"67669\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T15:39:26\",\"DataKeyValuePairs\":{\"aqi\":\"30\",\"tfav\":\"67732\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T15:49:26\",\"DataKeyValuePairs\":{\"aqi\":\"18\",\"tfav\":\"67758\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T15:59:26\",\"DataKeyValuePairs\":{\"aqi\":\"24\",\"tfav\":\"67772\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T16:09:26\",\"DataKeyValuePairs\":{\"aqi\":\"24\",\"tfav\":\"67786\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T16:19:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"67801\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T16:29:26\",\"DataKeyValuePairs\":{\"aqi\":\"30\",\"tfav\":\"67815\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T16:39:26\",\"DataKeyValuePairs\":{\"aqi\":\"24\",\"tfav\":\"67829\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T16:49:26\",\"DataKeyValuePairs\":{\"aqi\":\"36\",\"tfav\":\"67844\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T16:59:26\",\"DataKeyValuePairs\":{\"aqi\":\"45\",\"tfav\":\"67858\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T17:09:26\",\"DataKeyValuePairs\":{\"aqi\":\"24\",\"tfav\":\"67872\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T17:19:26\",\"DataKeyValuePairs\":{\"aqi\":\"50\",\"tfav\":\"67887\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T17:29:26\",\"DataKeyValuePairs\":{\"aqi\":\"24\",\"tfav\":\"67901\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T17:39:26\",\"DataKeyValuePairs\":{\"aqi\":\"24\",\"tfav\":\"67915\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T17:49:26\",\"DataKeyValuePairs\":{\"aqi\":\"10\",\"tfav\":\"67930\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T17:59:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"67944\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T18:09:26\",\"DataKeyValuePairs\":{\"aqi\":\"36\",\"tfav\":\"67959\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T18:19:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"67973\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T18:29:26\",\"DataKeyValuePairs\":{\"aqi\":\"50\",\"tfav\":\"67987\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T18:39:26\",\"DataKeyValuePairs\":{\"aqi\":\"45\",\"tfav\":\"68002\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T18:49:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"68016\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T18:59:26\",\"DataKeyValuePairs\":{\"aqi\":\"18\",\"tfav\":\"68030\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T19:09:26\",\"DataKeyValuePairs\":{\"aqi\":\"24\",\"tfav\":\"68045\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T19:19:26\",\"DataKeyValuePairs\":{\"aqi\":\"30\",\"tfav\":\"68059\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T19:29:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"68073\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T19:39:26\",\"DataKeyValuePairs\":{\"aqi\":\"24\",\"tfav\":\"68088\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T19:49:26\",\"DataKeyValuePairs\":{\"aqi\":\"18\",\"tfav\":\"68102\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T19:59:26\",\"DataKeyValuePairs\":{\"aqi\":\"36\",\"tfav\":\"68117\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T20:09:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"68131\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T20:19:26\",\"DataKeyValuePairs\":{\"aqi\":\"18\",\"tfav\":\"68145\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T20:29:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"68160\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T20:39:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68174\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T20:49:26\",\"DataKeyValuePairs\":{\"aqi\":\"36\",\"tfav\":\"68188\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T20:59:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"68203\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T21:09:26\",\"DataKeyValuePairs\":{\"aqi\":\"30\",\"tfav\":\"68217\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T21:19:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68231\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T21:29:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68246\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T21:39:26\",\"DataKeyValuePairs\":{\"aqi\":\"24\",\"tfav\":\"68260\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T21:49:26\",\"DataKeyValuePairs\":{\"aqi\":\"10\",\"tfav\":\"68274\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T21:59:26\",\"DataKeyValuePairs\":{\"aqi\":\"24\",\"tfav\":\"68289\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T22:09:26\",\"DataKeyValuePairs\":{\"aqi\":\"24\",\"tfav\":\"68303\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T22:19:26\",\"DataKeyValuePairs\":{\"aqi\":\"18\",\"tfav\":\"68318\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T22:29:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"68332\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T22:39:26\",\"DataKeyValuePairs\":{\"aqi\":\"10\",\"tfav\":\"68346\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T22:49:26\",\"DataKeyValuePairs\":{\"aqi\":\"10\",\"tfav\":\"68361\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T22:59:26\",\"DataKeyValuePairs\":{\"aqi\":\"10\",\"tfav\":\"68375\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T23:09:26\",\"DataKeyValuePairs\":{\"aqi\":\"10\",\"tfav\":\"68389\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T23:19:26\",\"DataKeyValuePairs\":{\"aqi\":\"18\",\"tfav\":\"68404\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T23:29:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"68418\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T23:39:26\",\"DataKeyValuePairs\":{\"aqi\":\"18\",\"tfav\":\"68432\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T23:49:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"68447\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-12T23:59:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"68461\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T00:09:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"68476\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T00:19:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"68490\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T00:29:26\",\"DataKeyValuePairs\":{\"aqi\":\"18\",\"tfav\":\"68504\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T00:39:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"68519\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T00:49:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"68533\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T00:59:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68547\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T01:09:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68562\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T01:19:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68576\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T01:29:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68590\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T01:39:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68605\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T01:49:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68619\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T01:59:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68633\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T02:09:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68648\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T02:19:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68662\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T02:29:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68677\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T02:39:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68691\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T02:49:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68705\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T02:59:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68720\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T03:09:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68734\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T03:19:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68748\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T03:29:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68763\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T03:39:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"68777\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T03:49:26\",\"DataKeyValuePairs\":{\"aqi\":\"2\",\"tfav\":\"68791\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T03:59:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68806\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T04:09:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68823\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T04:19:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68886\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T04:29:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"68949\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T04:39:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"69012\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-13T04:49:26\",\"DataKeyValuePairs\":{\"aqi\":\"0\",\"tfav\":\"69075\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-14T06:22:20\",\"DataKeyValuePairs\":{\"aqi\":\"10\",\"tfav\":\"69159\"}},{\"DataType\":\"airquality.1\",\"Timestamp\":\"2014-02-14T10:38:34\",\"DataKeyValuePairs\":{\"aqi\":\"18\",\"tfav\":\"69239\"}}]}";
		//onDataDownload(Errors.SUCCESS, js);

		if (goodAirInfos != null) {
			goodAirInfos.clear();
		}

		/**Last day*/
		if (lastDayRDCPValues != null) {
			lastDayRDCPValues.clear();
		} 

		if (powerOnReadingsValues != null) {
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
					if (hrlyAqiValues.get(i) < 2) {
						goodAirCount ++;
					}
					totalAirCount ++;
				}
			}
		}
		percentage(goodAirCount, totalAirCount);
		lastDayRDCPValues.add(lastDayRDCPVal);
		powerOnReadingsValues.add(powerOnReadings);

		/**Last 7 days and last 4 weeks*/
		if (last7daysRDCPValues != null) {
			last7daysRDCPValues.clear();
		} 

		if (last4weeksRDCPValues != null) {
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
						if (dailyAqiValues.get(i) < 2) {
							tempGood ++;
						}
						tempCount ++;
					}
				}
				last4weeksRDCPVal[i] = dailyAqiValues.get(i);
				if (dailyAqiValues.get(i) != -1) {
					if (dailyAqiValues.get(i) < 2) {
						goodAirCount ++;
					}
					totalAirCount ++;
				}
			}

		}
		percentage(tempGood, tempCount);
		percentage(goodAirCount, totalAirCount);
		last7daysRDCPValues.add(last7daysRDCPVal);
		last4weeksRDCPValues.add(last4weeksRDCPVal);
		GraphConst.calculateOutdoorAQIValues();

		handler.removeCallbacks(downloadDataRunnble);
		handler.post(downloadDataRunnble);
	}

	/**
	 * 
	 * @param goodAir
	 * @param totalAir
	 */
	private void percentage(int goodAir, int totalAir) {
		int percent = 0;
		if (totalAir > 0) {
			percent = (goodAir * 100) / totalAir;
		}
		goodAirInfos.add(percent);
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
			msgFirst.setText(getString(R.string.msg_top_1));
			msgSecond.setText(getString(R.string.msg_bot_1));
			break;
		}
		case R.id.detailsOutdoorLastWeekLabel: {
			callGraphViewOnClickEvent(1, last7daysRDCPValues);

			lastDayBtn.setTextColor(Color.LTGRAY);
			lastWeekBtn.setTextColor(GraphConst.COLOR_DODLE_BLUE);
			lastFourWeekBtn.setTextColor(Color.LTGRAY);
			msgFirst.setText(getString(R.string.msg_top_2));
			msgSecond.setText(getString(R.string.msg_bot_2));
			break;
		}
		case R.id.detailsOutdoorLastFourWeekLabel: {
			callGraphViewOnClickEvent(2, last4weeksRDCPValues);
			
			lastDayBtn.setTextColor(Color.LTGRAY);
			lastWeekBtn.setTextColor(Color.LTGRAY);
			lastFourWeekBtn.setTextColor(GraphConst.COLOR_DODLE_BLUE);
			msgFirst.setText(getString(R.string.msg_top_3));
			msgSecond.setText(getString(R.string.msg_bot_3));
			break;
		}
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
			graphLayout.addView(new GraphView(this, 
					rdcpValues.get(0), rdcpValues, null, coordinates, 0, indexBottBg));
		}	
	}

	private void removeChildViewFromBar() {

		barTopNum.setText("1");
		barTopName.setText("Living room");
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
	private void getDataFromDashboard() {
		String datas[] = getIntent().getStringArrayExtra("indoor");
		/**
		 * Updating all the details in the screen, which is passed from Dashboard
		 */
		Log.i(TAG, "Data from Dashboard= " + datas);
		if (datas != null && datas.length > 0) {

			if (datas[0] != null) {
				mode.setText(datas[0]);
			}

			if (datas[1] != null) {
				filter.setText(datas[1]);
			}
			if (datas[2] != null) {
				try {
					int pSence = Integer.parseInt(datas[2].trim());
					circleImg.setImageDrawable(Utils.setAQICircleBackground(this, pSence));

					Utils.setIndoorAQIStatusAndComment(this, pSence, aqiStatus, aqiSummary);
				} catch (NumberFormatException e) {
					//e.printStackTrace();
				}
			}

			if (datas[5] != null) {
				outdoorTitle = datas[5];
			}
		}
	}

	

	@Override
	protected void onStop() {
		super.onStop();
		SensorDataController.getInstance(this).removeListener(this) ;
	}

	public void aqiAnalysisClick(View v) {
		Intent intent = new Intent(this, IndoorAQIAnalysisActivity.class);
		intent.putExtra("indoortitle", aqiStatus.getText());
		intent.putExtra("outdoortitle", outdoorTitle);
		startActivity(intent);
	}

	@Override
	public void sensorDataReceived(AirPurifierEventDto airPurifierEventDto) {
		// TODO Auto-generated method stub
		Log.i("Indoor", "Sensor Event Received") ;
	}

	/**
	 * rDcp values download
	 */
	@Override
	public void onDataDownload(int status, String downloadedData) {
		
		if( status == Errors.SUCCESS) {
			Utils.parseIndoorDetails(downloadedData) ;
			if( SessionDto.getInstance().getIndoorTrendDto() != null ) {
				hrlyAqiValues = SessionDto.getInstance().getIndoorTrendDto().getHourlyList() ;
				dailyAqiValues = SessionDto.getInstance().getIndoorTrendDto().getDailyList() ;
				powerOnStatusList = SessionDto.getInstance().getIndoorTrendDto().getPowerDetailsList() ;
				
			}
			parseReading();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		handler.removeCallbacks(downloadDataRunnble);
		finish();
	}
}

