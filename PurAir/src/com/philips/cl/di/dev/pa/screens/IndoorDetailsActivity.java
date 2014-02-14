package com.philips.cl.di.dev.pa.screens;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.controller.CPPController;
import com.philips.cl.di.dev.pa.controller.SensorDataController;
import com.philips.cl.di.dev.pa.detail.utils.Coordinates;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.dto.IndoorHistoryDto;
import com.philips.cl.di.dev.pa.interfaces.ICPDownloadListener;
import com.philips.cl.di.dev.pa.interfaces.PercentDetailsClickListener;
import com.philips.cl.di.dev.pa.interfaces.SensorEventListener;
import com.philips.cl.di.dev.pa.screens.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.screens.customviews.GraphView;
import com.philips.cl.di.dev.pa.screens.customviews.PercentBarLayout;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.utils.DataParser;
import com.philips.cl.di.dev.pa.utils.Utils;
import com.philips.icpinterface.data.Errors;

public class IndoorDetailsActivity extends ActionBarActivity implements OnClickListener, PercentDetailsClickListener, SensorEventListener, ICPDownloadListener {

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
	private List<int[]> powerOnReadingsList;
	private List<float[]> lastDayReadingsList;
	private List<float[]> last7dayReadingsList;
	private List<float[]> last4weekReadingsList;
	private Coordinates coordinates;

	private int powerOnReadings[] = new int[24];
	private float lastDayReadings[] = new float[24];
	private float last7dayReadings[] = new float[7];
	private float last4weekReadings[] = new float[28];

	int powerOnFlgs10[] = { 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			1, 1, 0, 0, 0, 1 };

	float yCoordinates10[] = { 0F, 11.5F, 0F, 5.5F, 2.5F, 2.5F, 0F, 1.5F, 2.5F,
			5.5F, 2.5F, 2.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F, 2.5F,
			5.5F, 2.5F, 2.5F };

	float yCoordinates20[] = { 2F, 3F, 4F, 3F, 2F, 8F, 1F };

	float yCoordinates30[] = { 0F, 1.5F, 2.5F, 2.5F, 2.5F, 2.5F, 9.5F, 0F,
			1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F,
			2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 5.5F };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_trends_indoor);
		coordinates = new Coordinates(this);
		initializeUI();

		SensorDataController.getInstance(this).addListener(this) ;

		parseReading();

		initActionBar();
		setActionBarTitle();

		graphLayout.addView(new GraphView(this, 
				lastDayReadingsList.get(0), lastDayReadingsList,powerOnReadingsList.get(0), coordinates, 0, indexBottBg));

		getDataFromDashboard();

		CPPController.getInstance(this).setDownloadDataListener(this) ;
		CPPController.getInstance(this).downloadDataFromCPP("Clientid=1c5a6bfffe6341fe;datatype=airquality.1;startDate=2014-01-12T05:46:05.1508314Z;endDate=2014-02-13T05:46:05.1508314Z",2048) ;
	}

	/**
	 * Initialize UI widget
	 * */
	private void initializeUI() {
		powerOnReadingsList = new ArrayList<int[]>();
		lastDayReadingsList = new ArrayList<float[]>();
		last7dayReadingsList = new ArrayList<float[]>();
		last4weekReadingsList = new ArrayList<float[]>();

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

		percentBarLayout = new PercentBarLayout(IndoorDetailsActivity.this, null, 2, this, 0, 0);
		percentBarLayout.setClickable(true);
		horizontalScrollView.addView(percentBarLayout);


		/**
		 * Set click listener
		 * */
		lastDayBtn.setOnClickListener(this);
		lastWeekBtn.setOnClickListener(this);
		lastFourWeekBtn.setOnClickListener(this);
		//rightImg.setOnClickListener(this);
	}

	/**
	 * InitActionBar
	 */
	private void initActionBar() {
		mActionBar = getSupportActionBar();
		mActionBar.setIcon(null);
		mActionBar.setHomeButtonEnabled(true);
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
		/**Last day*/
		if (lastDayReadingsList != null) {
			lastDayReadingsList.clear();
		} 

		if (powerOnReadingsList != null) {
			powerOnReadingsList.clear();
		} 

		for (int i = 0; i < lastDayReadings.length; i++) {
			lastDayReadings[i] = yCoordinates10[i];
			powerOnReadings[i] = powerOnFlgs10[i];
		}
		lastDayReadingsList.add(yCoordinates10);
		powerOnReadingsList.add(powerOnFlgs10);

		/**Last 7 days*/
		if (last7dayReadingsList != null) {
			last7dayReadingsList.clear();
		} 
		for (int i = 0; i < last7dayReadings.length; i++) {
			last7dayReadings[i] = yCoordinates20[i];
		}
		last7dayReadingsList.add(yCoordinates20);

		/**Last 4 weeks*/
		if (last4weekReadingsList != null) {
			last4weekReadingsList.clear();
		} 

		for (int i = 0; i < last4weekReadings.length; i++) {
			last4weekReadings[i] = yCoordinates30[i];
		}
		last4weekReadingsList.add(yCoordinates30);

	}

	/**
	 * onClick
	 * */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.detailsOutdoorLastDayLabel: {
			removeChildViewFromBar();

			percentBarLayout = new PercentBarLayout(IndoorDetailsActivity.this,
					null, 2, this, 0, 0);
			percentBarLayout.setClickable(true);
			horizontalScrollView.addView(percentBarLayout);

			graphLayout.addView(new GraphView(this, 
					lastDayReadingsList.get(0), lastDayReadingsList, powerOnReadingsList.get(0), coordinates, 0, indexBottBg));
			lastDayBtn.setTextColor(Color.rgb(100, 149, 237));
			lastWeekBtn.setTextColor(Color.GRAY);
			lastFourWeekBtn.setTextColor(Color.GRAY);
			msgFirst.setText(getString(R.string.msg_top_1));
			msgSecond.setText(getString(R.string.msg_bot_1));
			break;
		}
		case R.id.detailsOutdoorLastWeekLabel: {
			removeChildViewFromBar();

			percentBarLayout = new PercentBarLayout(IndoorDetailsActivity.this, 
					null, 2, this, 1, 0);
			percentBarLayout.setClickable(true);
			horizontalScrollView.addView(percentBarLayout);

			/*valueList.clear();
				float yCoordinates[] = { 0F, 1.5F, 0.5F, 5.5F, 2.5F, 2.5F, 2.5F };
				valueList.add(yCoordinates);
				float yCoordinates1[] = { 0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 1.5F };
				valueList.add(yCoordinates1);
				float yCoordinates2[] = { 0F, 1.5F, 2.5F, 5.5F, 2.5F, 4.5F, 1.5F };
				valueList.add(yCoordinates2);
				float yCoordinates3[] = { 0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 3.5F };
				valueList.add(yCoordinates3);
				float yCoordinates4[] = { 0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 4.5F };
				valueList.add(yCoordinates4);
				float yCoordinates5[] = { 0F, 1.5F, 2.5F, 5.5F, 2.5F, 1.5F, 4.5F };
				valueList.add(yCoordinates5);*/
			graphLayout.addView(new GraphView(this, 
					last7dayReadingsList.get(0), last7dayReadingsList, null, coordinates, 0, indexBottBg));
			lastDayBtn.setTextColor(Color.GRAY);
			lastWeekBtn.setTextColor(Color.rgb(100, 149, 237));
			lastFourWeekBtn.setTextColor(Color.GRAY);
			msgFirst.setText(getString(R.string.msg_top_2));
			msgSecond.setText(getString(R.string.msg_bot_2));
			break;
		}
		case R.id.detailsOutdoorLastFourWeekLabel: {
			removeChildViewFromBar();

			percentBarLayout = new PercentBarLayout(IndoorDetailsActivity.this,
					null, 2, this, 2, 0);
			percentBarLayout.setClickable(true);
			horizontalScrollView.addView(percentBarLayout);


			/*valueList.clear();
				float yCoordinates[] = { 0F, 1.5F, 2.5F, 2.5F, 2.5F, 2.5F, 9.5F,
						0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F,
						5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F,
						5.5F };
				valueList.add(yCoordinates);
				float yCoordinates1[] = { 0F, 6.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F,
						0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F,
						5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 3.5F,
						2.5F };
				valueList.add(yCoordinates1);
				float yCoordinates2[] = { 0F, 3.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F,
						0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F,
						5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 4.5F,
						1.5F };
				valueList.add(yCoordinates2);
				float yCoordinates3[] = { 0F, 2.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F,
						0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F,
						5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 5.5F,
						3.5F };
				valueList.add(yCoordinates3);
				float yCoordinates4[] = { 0F, 4.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F,
						0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F,
						5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 1.5F,
						4.5F };
				valueList.add(yCoordinates4);
				float yCoordinates5[] = { 0F, 0.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F,
						0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F,
						5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 3.5F,
						0.5F };
				valueList.add(yCoordinates5);*/
			graphLayout.addView(new GraphView(this, last4weekReadingsList
					.get(0), last4weekReadingsList, null, coordinates, 0, indexBottBg));
			lastDayBtn.setTextColor(Color.GRAY);
			lastWeekBtn.setTextColor(Color.GRAY);
			lastFourWeekBtn.setTextColor(Color.rgb(100, 149, 237));
			msgFirst.setText(getString(R.string.msg_top_3));
			msgSecond.setText(getString(R.string.msg_bot_3));
			break;
		}
		case R.id.ivLeftMenu: {
			//Toast.makeText(getApplicationContext(), "Left Menu", Toast.LENGTH_SHORT).show();
			finish();
		}
		case R.id.ivRightDeviceIcon: {
			//Toast.makeText(getApplicationContext(), "Right Menu", Toast.LENGTH_SHORT).show();
		}
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

		percentBarLayout = new PercentBarLayout(IndoorDetailsActivity.this, 
				null, 2, this, index, position);
		percentBarLayout.setClickable(true);
		horizontalScrollView.addView(percentBarLayout);

		if (graphLayout.getChildCount() > 0) {
			graphLayout.removeViewAt(0);
		}
		/*valueList.clear();
		float yCoordinates[] = { 0F, 7.5F, 0.5F, 5.5F, 2.5F, 2.5F, 2.5F };
		valueList.add(yCoordinates);
		float yCoordinates1[] = { 0F, 2.5F, 3.5F, 1.5F, 2.5F, 2.5F, 1.5F };
		valueList.add(yCoordinates1);
		float yCoordinates2[] = { 0F, 1.5F, 4.5F, 1.5F, 2.5F, 4.5F, 2.5F };
		valueList.add(yCoordinates2);
		float yCoordinates3[] = { 5.5F, 2.5F, 2.5F, 3.5F, 1.5F, 2.5F, 3.5F };
		valueList.add(yCoordinates3);
		float yCoordinates4[] = { 3.5F, 1.5F, 3.5F, 5.5F, 2.5F, 2.5F, 4.5F };
		valueList.add(yCoordinates4);
		float yCoordinates5[] = { 2.5F, 1.5F, 2.5F, 5.5F, 2.5F, 1.5F, 0.5F };
		valueList.add(yCoordinates5);*/
		if (index == 0) {
			graphLayout.addView(new GraphView(this, lastDayReadingsList
					.get(position), lastDayReadingsList, powerOnReadingsList
					.get(position), coordinates, position, indexBottBg));

		} else if (index == 1) {
			graphLayout.addView(new GraphView(this, last7dayReadingsList
					.get(position), last7dayReadingsList, null, coordinates,
					position, indexBottBg));

		} else if (index == 2) {
			graphLayout.addView(new GraphView(this, last4weekReadingsList
					.get(position), last4weekReadingsList, null, coordinates,
					position, indexBottBg));

		}

	}

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
					circleImg.setImageDrawable(setAQICircleBackground(pSence));

					setIndoorAQIStatusAndComment(pSence);
				} catch (NumberFormatException e) {
					//e.printStackTrace();
				}
			}
			/*if (datas[3] != null) {
				String tempStatus[] = datas[3].trim().split(" ");
				if (tempStatus != null && tempStatus.length > 1) {
					aqiStatus.setText(tempStatus[0]+"\n"+tempStatus[1]);
				} else {
					aqiStatus.setText(datas[3]);
				}
			}*/

			/*if (datas[4] != null) {
				aqiSummary.setText(datas[4]);
			}*/
		}
	}

	/**
	 * 
	 * @param pSense
	 * @return
	 */
	private Drawable setAQICircleBackground(int pSense) {
		Log.i(TAG, "aqi=  " + pSense);
		if(pSense >= 0 && pSense < 2) {
			return getResources().getDrawable(R.drawable.aqi_blue_circle_2x);
		} else if(pSense >= 2 && pSense < 3) {
			return getResources().getDrawable(R.drawable.aqi_purple_circle_2x);
		} else if(pSense >= 3 && pSense < 4) {
			return getResources().getDrawable(R.drawable.aqi_fusia_circle_2x);
		} else {
			return getResources().getDrawable(R.drawable.aqi_red_circle_2x);
		}
	}

	/**
	 * 
	 * @param pSense
	 * @return
	 */
	private void setIndoorAQIStatusAndComment(int pSense) {
		if(pSense >= 0 && pSense <= 1) {
			aqiStatus.setText(getString(R.string.good)) ;
			aqiSummary.setText(getString(R.string.very_healthy_msg_indoor)) ;
		} else if(pSense > 2 && pSense <= 3) {
			aqiStatus.setText(getString(R.string.moderate)) ;
			aqiSummary.setText(getString(R.string.healthy_msg_indoor)) ;
		} else if(pSense > 3 && pSense <= 4) {
			aqiStatus.setText(getString(R.string.unhealthy)) ;
			aqiSummary.setText(getString(R.string.slightly_polluted_msg_indoor)) ;
		} else if(pSense < 4) {
			String tempStatus[] = getString(R.string.very_unhealthy).trim().split(" ");
			if (tempStatus != null && tempStatus.length > 1) {
				aqiStatus.setText(tempStatus[0]+"\n"+tempStatus[1]);
			} else { 
				aqiStatus.setText(getString(R.string.very_unhealthy)) ;
			}
			aqiSummary.setText(getString(R.string.moderately_polluted_msg_indoor)) ;
		} 
	}

	private Bitmap writeTextOnDrawableHead(int drawableId, String text) {

		Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
				.copy(Bitmap.Config.ARGB_8888, true);

		//Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);.

		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.rgb(65, 105, 225));
		paint.setTypeface(Fonts.getGillsansLight(this));
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(coordinates.getIdRectMarginLeft());

		Rect textRect = new Rect();
		paint.getTextBounds(text, 0, text.length(), textRect);

		Canvas canvas = new Canvas(bm);

		//Calculate the positions
		int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

		int yPos = (int) (canvas.getHeight()) / 2 + 10 ;  

		canvas.drawText(text, xPos, yPos, paint);

		return  bm;
	}

	@Override
	protected void onStop() {
		super.onStop();
		SensorDataController.getInstance(this).removeListener(this) ;
	}

	public void aqiAnalysisClick(View v) {
		Intent intent = new Intent(this, IndoorAQIAnalysisActivity.class);
		startActivity(intent);
	}

	@Override
	public void sensorDataReceived(AirPurifierEventDto airPurifierEventDto) {
		// TODO Auto-generated method stub
		Log.i("Indoor", "Sensor Event Received") ;
	}


	@Override
	public void onDataDownload(int status, String downloadedData) {
		List<Float> aqi = new ArrayList<Float>() ;
		int counter = 0 ;
		float aqiSum = 0.0f;

		String currentAQIDate = "" ;
		if ( status == Errors.SUCCESS && downloadedData != null ) {
			List<IndoorHistoryDto> indoorAQIHistory = new DataParser(downloadedData).parseHistoryData() ;
			if( indoorAQIHistory != null ) {
				for ( int index = 0 ; index < indoorAQIHistory.size() ; index ++ ) {
					String date = indoorAQIHistory.get(index).getTimeStamp() ;
					
					if ( index == 0 ) {
						int numberOfDays = Utils.getDifferenceBetweenDaysFromCurrentDay(date.substring(0,10)) ;
						if ( numberOfDays < 28 ) {
							for( int i = 0; i < (28 - numberOfDays) ; i ++ ) {
								aqi.add(-1.0F) ;
							}
						}
					}
					
					if ( currentAQIDate.equals("")) {
						aqiSum = indoorAQIHistory.get(index).getAqi() ;
						counter = 1 ;
					}
					else if ( !currentAQIDate.equals("")) {
						if(currentAQIDate.substring(0,10).equals(date.substring(0,10))) {
							aqiSum =  aqiSum + indoorAQIHistory.get(index).getAqi() ;							
							counter ++ ;
							
							if ( index == indoorAQIHistory.size() -1 ) {
								aqiSum = aqiSum / counter ;
								aqi.add(aqiSum) ;
							}
						}
						else {
							aqiSum = aqiSum / counter ;
							aqi.add(aqiSum) ;
							
							aqiSum = indoorAQIHistory.get(index).getAqi() ;
							counter = 1;
						}
					}
					currentAQIDate = date ;				
				}
			}
		}
	}
}

