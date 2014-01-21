package com.philips.cl.di.dev.pa.screens;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.detail.utils.Coordinates;
import com.philips.cl.di.dev.pa.interfaces.PercentDetailsClickListener;
import com.philips.cl.di.dev.pa.screens.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.screens.customviews.GraphView;
import com.philips.cl.di.dev.pa.screens.customviews.PercentBarLayout;

public class IndoorDetailsActivity extends Activity implements OnClickListener, PercentDetailsClickListener {
	
	private LinearLayout graphLayout;
	private TextView lastDayBtn, lastWeekBtn, lastFourWeekBtn;
	private TextView updatedTitle, updatedValue, locationCity, location, weatherValue;
	private TextView aqiReading, aqiStatusTitle, aqiStatus;
	private ImageView circlePointer, circleMeter, cityImg, weatherImg;
	private ImageView backImg, rightImg, centerLabelImg;
	private CustomTextView msgFirst, msgSecond;
	private ImageView outdoorCirclePointer, indexBottBg;
	private HorizontalScrollView horizontalScrollView;
	private PercentBarLayout percentBarLayout;
	private CustomTextView barTopNum, barTopName, selectedIndexBottom;
	private CustomTextView titleDashboard;
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
	int powerOnFlgs11[] = { 0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			1, 1, 0, 0, 0, 1 };
	float yCoordinates11[] = { 2.5F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F,
			2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F,
			2.5F, 5.5F, 3.5F, 1.5F };
	int powerOnFlgs12[] = { 0, 0, 0, 1,1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			1, 1, 0, 0, 0, 1 };
	float yCoordinates12[] = { 3.5F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F,
			2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F,
			2.5F, 5.5F, 4.5F, 3.5F };
	int powerOnFlgs13[] = { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			1, 1, 0, 0, 0, 1 };
	float yCoordinates13[] = { 0.5F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F,
			2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F,
			2.5F, 5.5F, 1.5F, 4.5F };
	int powerOnFlgs14[] = { 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			1, 1, 0, 0, 0, 1 };
	float yCoordinates14[] = { 4.5F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F,
			2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F,
			2.5F, 5.5F, 5.5F, 5.5F };
	int powerOnFlgs15[] = { 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			1, 1, 0, 0, 0, 0 };
	float yCoordinates15[] = { 5.5F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F,
			2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F,
			2.5F, 5.5F, 6.5F, 0.5F };
	
	
	float yCoordinates20[] = { 0F, 1.5F, 0.5F, 5.5F, 2.5F, 2.5F, 2.5F };
	float yCoordinates21[] = { 0F, 2.5F, 3.5F, 1.5F, 2.5F, 2.5F, 1.5F };
	float yCoordinates22[] = { 5.5F, 2.5F, 2.5F, 3.5F, 1.5F, 2.5F, 3.5F };
	float yCoordinates23[] = { 2.5F, 1.5F, 2.5F, 5.5F, 2.5F, 1.5F, 0.5F };
	float yCoordinates24[] = { 3.5F, 1.5F, 3.5F, 5.5F, 2.5F, 2.5F, 4.5F };
	float yCoordinates25[] = { 0F, 1.5F, 2.5F, 5.5F, 2.5F, 1.5F, 4.5F };
	
	float yCoordinates30[] = { 0F, 1.5F, 2.5F, 2.5F, 2.5F, 2.5F, 9.5F, 0F,
			1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F,
			2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 5.5F };
	float yCoordinates31[] = { 0F, 6.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F, 0F,
			1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F,
			2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 3.5F, 2.5F };
	float yCoordinates32[] = { 0F, 3.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F, 0F,
			1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F,
			2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 4.5F, 1.5F };
	float yCoordinates33[] = { 0F, 2.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F, 0F,
			1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F,
			2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 5.5F, 3.5F };
	float yCoordinates34[] = { 0F, 4.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F, 0F,
			1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F,
			2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 1.5F, 4.5F };
	float yCoordinates35[] = { 0F, 0.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F, 0F,
			1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F,
			2.5F, 9.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 3.5F, 0.5F };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trends_indoor);
		initializeUI();
		
		coordinates = new Coordinates(this);
		
		parseReading();
		
		/*float yCoordinates[] = { 0F, 11.5F, 0F, 5.5F, 2.5F, 2.5F, 0F, 1.5F,
				2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F,
				1.5F, 2.5F, 5.5F, 2.5F, 2.5F };
		valueList.add(yCoordinates);
		float yCoordinates1[] = { 2.5F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F,
				2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F,
				1.5F, 2.5F, 5.5F, 3.5F, 1.5F };
		valueList.add(yCoordinates1);
		float yCoordinates2[] = { 3.5F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F,
				2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F,
				1.5F, 2.5F, 5.5F, 4.5F, 3.5F };
		valueList.add(yCoordinates2);
		float yCoordinates3[] = { 0.5F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F,
				2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F,
				1.5F, 2.5F, 5.5F, 1.5F, 4.5F };
		valueList.add(yCoordinates3);
		float yCoordinates4[] = { 4.5F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F,
				2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F,
				1.5F, 2.5F, 5.5F, 5.5F, 5.5F };
		valueList.add(yCoordinates4);
		float yCoordinates5[] = { 5.5F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F,
				2.5F, 5.5F, 2.5F, 2.5F, 0F, 1.5F, 2.5F, 5.5F, 2.5F, 2.5F, 0F,
				1.5F, 2.5F, 5.5F, 6.5F, 0.5F };
		valueList.add(yCoordinates5);*/
		
		
		graphLayout.addView(new GraphView(this, 
				lastDayReadingsList.get(0), lastDayReadingsList,powerOnReadingsList.get(0), coordinates, 0, indexBottBg));
		
		/*horizontalScrollView = (HorizontalScrollView) findViewById(R.id.indoorDbHorizontalScroll);
		barTopNum = (CustomTextView) findViewById(R.id.indoorDbBarTopNum);
		barTopName = (CustomTextView) findViewById(R.id.indoorDbBarTopName);
		
		percentBarLayout = new PercentBarLayout(this, null, 10, barTopNum, barTopName);
		percentBarLayout.setClickable(true);
        horizontalScrollView.addView(percentBarLayout);*/
		
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
		//indoorPercentLayout = (RelativeLayout) findViewById(R.id.trendsOutdoorIndoorPercentLayout);
		//outdoorPercentLayout = (RelativeLayout) findViewById(R.id.trendsOutdoorOutdoorPercentLayout);
		//outdoorCirclePointer = (ImageView) findViewById(R.id.outdoor_circle_pointer);
		
		updatedTitle = (TextView) findViewById(R.id.tv_updated_title);
		//updatedValue = (TextView) findViewById(R.id.tv_updated_value);
		locationCity = (TextView) findViewById(R.id.tv_location_city);
		location = (TextView) findViewById(R.id.tv_location);
		weatherValue = (TextView) findViewById(R.id.tv_outdoor_weather_value);
		aqiReading = (TextView) findViewById(R.id.outdoor_aqi_reading);
		aqiStatusTitle = (TextView) findViewById(R.id.tv_outdoor_aqi_status_title);
		aqiStatus = (TextView) findViewById(R.id.tv_outdoor_aqi_status_message);
		
		circlePointer = (ImageView) findViewById(R.id.outdoor_circle_pointer);
		circleMeter = (ImageView) findViewById(R.id.outdoor_circle_meter);
		cityImg = (ImageView) findViewById(R.id.detailsOutdoorDbImg);
		weatherImg = (ImageView) findViewById(R.id.iv_outdoor_weather_image);
		backImg = (ImageView) findViewById(R.id.ivLeftMenu); 
		centerLabelImg = (ImageView) findViewById(R.id.ivCenterLabel); 
		rightImg = (ImageView) findViewById(R.id.ivRightDeviceIcon); 
		indexBottBg= (ImageView) findViewById(R.id.indoorDbIndexBottBg); 
		
		msgFirst = (CustomTextView) findViewById(R.id.idFirstMsg);
		msgSecond = (CustomTextView) findViewById(R.id.idSecondMsg);
		titleDashboard = (CustomTextView) findViewById(R.id.detailsOutdoorDbLabel);
		titleDashboard.setText(getString(R.string.title_indoor_db));
		/**
		 * set image top bar
		 * */
		backImg.setBackgroundResource(R.drawable.trends_txt_bg);
		backImg.setImageResource(R.drawable.top_leftindicator);
		//centerLabelImg.setImageResource(resId)
		rightImg.setBackgroundResource(R.drawable.trends_txt_bg);
		rightImg.setImageResource(R.drawable.right_bar_icon_blue_2x);
		//outdoorCirclePointer.setRotation(45);
		
		
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.indoorDbHorizontalScroll);
		barTopNum = (CustomTextView) findViewById(R.id.indoorDbBarTopNum);
		barTopName = (CustomTextView) findViewById(R.id.indoorDbBarTopName);
		selectedIndexBottom = (CustomTextView) findViewById(R.id.indoorDbIndexBott);
		
		percentBarLayout = new PercentBarLayout(IndoorDetailsActivity.this, null, 7, this, 0, 0);
		percentBarLayout.setClickable(true);
        horizontalScrollView.addView(percentBarLayout);
		
		//outdoorCirclePointer.setRotation(45);
		
		//indoorPercentLayout.addView(new AirView(this, 50));
		//outdoorPercentLayout.addView(new AirView(this, 100));
		
		/**
		 * Set click listener
		 * */
		lastDayBtn.setOnClickListener(this);
		lastWeekBtn.setOnClickListener(this);
		lastFourWeekBtn.setOnClickListener(this);
		backImg.setOnClickListener(this);
		rightImg.setOnClickListener(this);
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
		
		for (int i = 0; i < lastDayReadings.length; i++) {
			lastDayReadings[i] = yCoordinates11[i];
			powerOnReadings[i] = powerOnFlgs11[i];
		}
		lastDayReadingsList.add(yCoordinates11);
		powerOnReadingsList.add(powerOnFlgs11);
		
		
		for (int i = 0; i < lastDayReadings.length; i++) {
			lastDayReadings[i] = yCoordinates12[i];
			powerOnReadings[i] = powerOnFlgs12[i];
		}
		lastDayReadingsList.add(yCoordinates12);
		powerOnReadingsList.add(powerOnFlgs12);
		
		for (int i = 0; i < lastDayReadings.length; i++) {
			lastDayReadings[i] = yCoordinates13[i];
			powerOnReadings[i] = powerOnFlgs13[i];
		}
		lastDayReadingsList.add(yCoordinates13);
		powerOnReadingsList.add(powerOnFlgs13);
		
		for (int i = 0; i < lastDayReadings.length; i++) {
			lastDayReadings[i] = yCoordinates14[i];
			powerOnReadings[i] = powerOnFlgs14[i];
		}
		lastDayReadingsList.add(yCoordinates14);
		powerOnReadingsList.add(powerOnFlgs14);
		
		for (int i = 0; i < lastDayReadings.length; i++) {
			lastDayReadings[i] = yCoordinates15[i];
			powerOnReadings[i] = powerOnFlgs15[i];
		}
		lastDayReadingsList.add(yCoordinates15);
		powerOnReadingsList.add(powerOnFlgs15);
		
		
		/**Last 7 days*/
		if (last7dayReadingsList != null) {
			last7dayReadingsList.clear();
		} 
		for (int i = 0; i < last7dayReadings.length; i++) {
			last7dayReadings[i] = yCoordinates20[i];
		}
		last7dayReadingsList.add(yCoordinates20);
		
		for (int i = 0; i < last7dayReadings.length; i++) {
			last7dayReadings[i] = yCoordinates21[i];
		}
		last7dayReadingsList.add(yCoordinates21);
		
		for (int i = 0; i < last7dayReadings.length; i++) {
			last7dayReadings[i] = yCoordinates22[i];
		}
		last7dayReadingsList.add(yCoordinates22);
		
		for (int i = 0; i < last7dayReadings.length; i++) {
			last7dayReadings[i] = yCoordinates23[i];
		}
		last7dayReadingsList.add(yCoordinates23);
		
		for (int i = 0; i < last7dayReadings.length; i++) {
			last7dayReadings[i] = yCoordinates24[i];
		}
		last7dayReadingsList.add(yCoordinates24);
		
		for (int i = 0; i < last7dayReadings.length; i++) {
			last7dayReadings[i] = yCoordinates25[i];
		}
		last7dayReadingsList.add(yCoordinates25);
		
		/**Last 4 weeks*/
		if (last4weekReadingsList != null) {
			last4weekReadingsList.clear();
		} 
		
		for (int i = 0; i < last4weekReadings.length; i++) {
			last4weekReadings[i] = yCoordinates30[i];
		}
		last4weekReadingsList.add(yCoordinates30);

		for (int i = 0; i < last4weekReadings.length; i++) {
			last4weekReadings[i] = yCoordinates31[i];
		}
		last4weekReadingsList.add(yCoordinates31);

		for (int i = 0; i < last4weekReadings.length; i++) {
			last4weekReadings[i] = yCoordinates32[i];
		}
		last4weekReadingsList.add(yCoordinates32);

		for (int i = 0; i < last4weekReadings.length; i++) {
			last4weekReadings[i] = yCoordinates33[i];
		}
		last4weekReadingsList.add(yCoordinates33);

		for (int i = 0; i < last4weekReadings.length; i++) {
			last4weekReadings[i] = yCoordinates34[i];
		}
		last4weekReadingsList.add(yCoordinates34);

		for (int i = 0; i < last4weekReadings.length; i++) {
			last4weekReadings[i] = yCoordinates35[i];
		}
		last4weekReadingsList.add(yCoordinates35);

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
						null, 7, this, 0, 0);
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
						null, 7, this, 1, 0);
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
						null, 7, this, 2, 0);
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
				null, 7, this, index, position);
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
		
		
		//Toast.makeText(getApplicationContext(), "Item Click " + position, Toast.LENGTH_SHORT).show();
	}
	
	public void aqiAnalysisClick(View v) {
		Intent intent = new Intent(this, OutdoorAQIAnalysisActivity.class);
		startActivity(intent);
	}

}

