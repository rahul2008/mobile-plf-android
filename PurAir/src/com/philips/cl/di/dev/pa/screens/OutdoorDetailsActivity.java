package com.philips.cl.di.dev.pa.screens;

import java.util.Calendar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.detail.utils.Coordinates;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.screens.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.screens.customviews.GraphView;
import com.philips.cl.di.dev.pa.screens.customviews.WeatherReportLayout;
import com.philips.cl.di.dev.pa.util.Fonts;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
	private Calendar calender;
	private int powerOnReadings[] = new int[24];
	private float lastDayReadings[] = new float[24];
	private float last7dayReadings[] = new float[7];
	private float last4weekReadings[] = new float[28];
	private boolean isDataLoaded;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_details_outdoor);

		/**For right menu start*/
		//setContentView(R.layout.activity_main_aj);
		
		coordinates = new Coordinates(this);

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		initializeUI();
		
		initActionBar();
		setActionBarTitle();

		setUpMapIfNeeded();

		try {
			sessionDto = SessionDto.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		getDataFromDashboard();
	}


	@Override
	protected void onResume() {
		super.onResume();
		if (sessionDto != null) {
			isDataLoaded = getXCoordinates();
		}
		if (graphLayout.getChildCount() > 0) {
			graphLayout.removeAllViews();
		}
		if (isDataLoaded) {
			graphLayout.addView(new GraphView(this, lastDayReadings, powerOnReadings, true, coordinates));
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * Reading data from server
	 * */
	private boolean getXCoordinates() {
			boolean isOutdoorDataPresent = false ; 
			if (sessionDto.getOutdoorEventDto() != null){
				
				int pm25s[] = sessionDto.getOutdoorEventDto().getPm25();
				if (pm25s != null && pm25s.length > 0) {
					pm1.setText(getString(R.string.pm25) + "  " + pm25s[0]);
				}
				
				int pm10s[] = sessionDto.getOutdoorEventDto().getPm10();
				if (pm10s != null && pm10s.length > 0) {
					pm2.setText(getString(R.string.pm10) + "  " + pm10s[0]);
				}
				
				int so2s[] = sessionDto.getOutdoorEventDto().getSo2();
				if (so2s != null && so2s.length > 0) {
					pm3.setText(getString(R.string.so2) + "  " + so2s[0]);
				}
				
				int no2s[] = sessionDto.getOutdoorEventDto().getNo2();
				if (no2s != null && no2s.length > 0) {
					pm4.setText(getString(R.string.no2) + "  " + no2s[0]);
				}
				
				
				int odx1[] = sessionDto.getOutdoorEventDto().getIdx();
				int odx[] = null;
				if (odx1 != null && odx1.length > 0) {
					odx = new int[odx1.length];
					for (int i = 0; i < odx1.length; i++) {
						odx[i] = odx1[odx1.length - 1 - i];
					}
				}
				
				/**last day days*/
				/**
				 * Adding last 24 data into lastDayReadings array, from index 696 t0 719 
				 */
				
				if (odx != null) {
					isOutdoorDataPresent = true ;
					for (int i = 0; i < lastDayReadings.length; i++) {
						lastDayReadings[i] = odx[696 + i];
					}
				}
				
				/**last 7 days*/
				/**
				 * Adding last 7 days data into last7dayReadings array, from index last to till 7 days
				 */
				int hr = calender.get(Calendar.HOUR_OF_DAY);
				if (hr == 0) {
					hr = 24;
				}
				int last7dayHrs = (6*24) + hr;
				
				if (odx != null) {
					float sum = 0;
					float avg = 0;
					int j = 0;
					for (int i = 0; i < last7dayHrs; i++) {
						float x = odx[720 - last7dayHrs + i];
						sum = sum + x;
						if (i == 23 || i == 47 || i == 71 || i == 95 || i == 119 || i == 143) {
							avg = sum / (float)24;
							last7dayReadings[j] = avg;
							j++;
							sum = 0;
							avg = 0;
						} else if (i == last7dayHrs - 1) {
							System.out.println("sumlast==="+sum );
							avg = sum / (float)hr;
							last7dayReadings[j] = avg;
							sum = 0;
							avg = 0;
						}
					}
				}
	
				/**last 4 weeks*/
				/**
				 * TODO - Explain the logic in 3lines
				 */
				int last4WeekHrs = (3*7*24) + (6*24) + hr;
				int last4WeekIndexStart = 720 - last4WeekHrs;
				//System.out.println("lastWeekHrs=="+last4WeekHrs);
	
				if (odx != null) {
					int count = 1;
					float sum = 0;
					float avg = 0;
					int j = 0;
					for (int i = 0; i < last4WeekHrs; i++) {
	
						float x = odx[last4WeekIndexStart + i];
						sum = sum + x;
						if (count == 24 && j < 21) {
							avg = sum / (float)24;
							last4weekReadings[j] = avg;
							j++;
							sum = 0;
							avg = 0;
							count = 0;
						} else if (j >= 21){
							for (int m = 0; m <last7dayReadings.length; m++) {
								last4weekReadings[j] = last7dayReadings[m];
								j++;
							}
							break;
						}
						count ++;
					}
				}
			}
		return isOutdoorDataPresent ;
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
				circleImg.setImageDrawable(setAQICircleBackground(aqiInt));
				
				setAdviceIconTex(aqiInt);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
			}
		}
	}

	/**
	 * This method will set the AQI circle background depending on the AQI range
	 * @param aqi
	 * @return
	 */
	private Drawable setAQICircleBackground(int aqi) {
		if(aqi >= 0 && aqi <= 50) {
			return getResources().getDrawable(R.drawable.aqi_blue_circle_2x);
		} else if(aqi > 50 && aqi <= 100) {
			return getResources().getDrawable(R.drawable.aqi_pink_circle_2x);
		} else if(aqi > 100 && aqi <= 150) {
			return getResources().getDrawable(R.drawable.aqi_bluedark_circle_2x);
		} else if(aqi > 150 && aqi <= 200) {
			return getResources().getDrawable(R.drawable.aqi_purple_circle_2x);
		} else if(aqi > 200 && aqi <= 300) {
			return getResources().getDrawable(R.drawable.aqi_fusia_circle_2x);
		} else if(aqi > 300 /*&& aqi <= 500*/) {
			return getResources().getDrawable(R.drawable.aqi_red_circle_2x);
		}
		return null;
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

		} else if(aqiInt > 300 /*&& aqi <= 500*/) {
			maskImg.setImageResource(R.drawable.red_close_mask_2x); 
			openWindowImg.setImageResource(R.drawable.red_close_win_2x);  
			avoidImg.setImageResource(R.drawable.red_close_run_2x); 

			maskTxt.setText(getString(R.string.mask_od_msg1));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg1)); 
			avoidTxt.setText(getString(R.string.advice_od_msg1));

		}
		/*if (aqiInt <= 200) {
			maskImg.setImageResource(R.drawable.mask_not_needed_2x); 
			openWindowImg.setImageResource(R.drawable.open_windows_2x);  
			avoidImg.setImageResource(R.drawable.outdoor_act_2x); 

			maskTxt.setText(getString(R.string.mask_od_msg2));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg2)); 
			avoidTxt.setText(getString(R.string.advice_od_msg2));

		} else {
			maskImg.setImageResource(R.drawable.protection_icon_2x); 
			openWindowImg.setImageResource(R.drawable.window_icon_2x);  
			avoidImg.setImageResource(R.drawable.run_close_icon_2x); 

			maskTxt.setText(getString(R.string.mask_od_msg1));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg1)); 
			avoidTxt.setText(getString(R.string.advice_od_msg1));
		}
		*/	
	}

	/**
	 * Initialize UI widget
	 * */
	private void initializeUI() {
		calender = Calendar.getInstance();
		graphLayout = (LinearLayout) findViewById(R.id.detailsOutdoorlayoutGraph);
		//indoorPercentLayout = (RelativeLayout) findViewById(R.id.detailsOutdoorIndoorPercentLayout);
		//outdoorPercentLayout = (RelativeLayout) findViewById(R.id.detailsOutdoorOutdoorPercentLayout);
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

		/**Add today weather*/
		wetherScrollView.addView(new WeatherReportLayout(this, null, 8));

		/**Add weather forecast*/
		WeatherReportLayout weatherReportLayout = new WeatherReportLayout(this, null, 5);
		weatherReportLayout.setOrientation(LinearLayout.VERTICAL);
		wetherForcastLayout.addView(weatherReportLayout);

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
		mActionBar.setHomeButtonEnabled(true);
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
			if (isDataLoaded) {
				graphLayout.addView(new GraphView(this, lastDayReadings, powerOnReadings, true, coordinates));
			}
			lastDayBtn.setTextColor(Color.rgb(100, 149, 237));
			lastWeekBtn.setTextColor(Color.GRAY);
			lastFourWeekBtn.setTextColor(Color.GRAY);
			msgSecond.setText(getString(R.string.msg_bot_1));
			break;
		}
		case R.id.detailsOutdoorLastWeekLabel: {
			removeChildViewFromBar();
			if (isDataLoaded) {
				graphLayout.addView(new GraphView(this, last7dayReadings, null, true, coordinates));
			}
			lastDayBtn.setTextColor(Color.GRAY);
			lastWeekBtn.setTextColor(Color.rgb(100, 149, 237));
			lastFourWeekBtn.setTextColor(Color.GRAY);
			msgSecond.setText(getString(R.string.msg_bot_2));
			break;
		}
		case R.id.detailsOutdoorLastFourWeekLabel: {
			removeChildViewFromBar();
			if (isDataLoaded) {
				graphLayout.addView(new GraphView(this, last4weekReadings, null, true, coordinates));
			}
			lastDayBtn.setTextColor(Color.GRAY);
			lastWeekBtn.setTextColor(Color.GRAY);
			lastFourWeekBtn.setTextColor(Color.rgb(100, 149, 237));
			msgSecond.setText(getString(R.string.msg_bot_3));
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

	private void removeChildViewFromBar() {
		if (graphLayout.getChildCount() > 0) {
			graphLayout.removeAllViews();
		}
		if (sessionDto != null) {
			isDataLoaded = getXCoordinates();
		}
	}

	public void aqiAnalysisClick(View v) {
		Intent intent = new Intent(this, OutdoorAQIAnalysisActivity.class);
		startActivity(intent);
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
					.getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {

		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(new LatLng(31.230638, 121.473584)) 
		.zoom(13)                   // Sets the zoom
		.bearing(0)                // Sets the orientation of the camera to east
		.tilt(30)                   // Sets the tilt of the camera to 30 degrees
		.build();                   // Creates a CameraPosition from the builder
		
		mMap.getUiSettings().setZoomControlsEnabled(false);
		mMap.getUiSettings().setScrollGesturesEnabled(false);
		mMap.getUiSettings().setZoomGesturesEnabled(false);
		mMap.getUiSettings().setAllGesturesEnabled(false);
		mMap.getUiSettings().setCompassEnabled(false);
		mMap.getUiSettings().setRotateGesturesEnabled(false);
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

		mMap.addMarker(new MarkerOptions().position(new LatLng(31.230638, 121.473584)));

		//later we will use
		/*mMap.addMarker(new MarkerOptions()
        .position(new LatLng(31.230638, 121.473584))
        .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.transparent_bg, "Shanghai")))
        );*/

	}

	/**
	 * This will use for writing number on text
	 * */
	private Bitmap writeTextOnDrawable(int drawableId, String text) {

		Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
				.copy(Bitmap.Config.ARGB_8888, true);

		Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.BLACK);
		paint.setTypeface(tf);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(coordinates.getIdTxtSize());

		Rect textRect = new Rect();
		paint.getTextBounds(text, 0, text.length(), textRect);

		Canvas canvas = new Canvas(bm);

		//Calculate the positions
		int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

		int yPos = (int) (canvas.getHeight()) - 5 ;  

		canvas.drawText(text, xPos, yPos, paint);

		return  bm;
	}
	
	/**
	 * Writing text on image for heading
	 * */
	
	private Bitmap writeTextOnDrawableHead(int drawableId, String text) {

		Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
				.copy(Bitmap.Config.ARGB_8888, true);

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
	public void onBackPressed() {
		
			finish();
		
	}


}
