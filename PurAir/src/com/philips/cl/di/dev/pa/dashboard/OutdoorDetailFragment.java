package com.philips.cl.di.dev.pa.dashboard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.AirAnalysisExplainActivity;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.activity.MarkerActivity;
import com.philips.cl.di.dev.pa.adapter.NeighbourhoodCityBaseAdapter;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.fragment.DownloadAlerDialogFragement;
import com.philips.cl.di.dev.pa.fragment.MarkerMapFragment;
import com.philips.cl.di.dev.pa.outdoorlocations.DummyData;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Coordinates;
import com.philips.cl.di.dev.pa.util.DashboardUtil;
import com.philips.cl.di.dev.pa.util.GraphConst;
import com.philips.cl.di.dev.pa.util.ListViewHelper;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.OutdoorDetailsListener;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.FontButton;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.dev.pa.view.GraphView;
import com.philips.cl.di.dev.pa.view.ImageRound;
import com.philips.cl.di.dev.pa.view.WeatherForecastLayout;

public class OutdoorDetailFragment extends BaseFragment implements OnClickListener, OutdoorDetailsListener {
	private LinearLayout graphLayout, wetherForcastLayout, adviceLayout;
	private WeatherForecastLayout weatherReportLayout;
	private HorizontalScrollView wetherScrollView;
	private FontTextView lastDayBtn, lastWeekBtn, lastFourWeekBtn;
	private ImageView gaodeMapZoom ;
	private ImageView avoidImg, openWindowImg, maskImg;
	private ImageView dummyMapImage;
	private FontTextView avoidTxt, openWindowTxt, maskTxt;
	private ProgressBar aqiProgressBar;
	private ProgressBar weatherProgressBar;
	private static int currentCityHourOfDay;
	private static int currentCityDayOfWeek;
	private FontTextView aqitv;
	private FontTextView pmtv;
	private LinearLayout neighbourhoodcityll;
	private ListView nearbycitylv;
	private NeighbourhoodCityBaseAdapter adapter;
	private ToggleButton multiCityTrendTB;
	private AqiHistoricState aqiHistoricSate = AqiHistoricState.LAST_DAY;
	private ViewGroup city1stLL, city2ndLL, city3rdLL, city4thLL;
	private FontTextView city1stTV, city2ndTV, city3rdTV, city4thTV;
	private ProgressBar city1stPB, city2ndPB, city3rdPB, city4thPB;
	private ImageRound city1stIMGR, city2ndIMGR, city3rdIMGR, city4thIMGR;

	private enum AqiHistoricState {
		LAST_DAY, LAST_WEEK, LAST_MONTH
	}

	private ViewGroup mapGaodeLayout;
	private String selectedCityAreaId;
	private Calendar calenderGMTChinese;

	private HashMap<Integer, float[]> lastDayRDCPValuesMap;
	private HashMap<Integer, float[]> last7daysRDCPValuesMap;
	private HashMap<Integer, float[]> last4weeksRDCPValuesMap;

	private float lastDayAQIHistoricArr[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,
			-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F};
	private float last7dayAQIHistoricArr[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F};
	private float last4weekAQIHistoricArr[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,
			-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F};

	private OutdoorDetailHelper detailHelper;
	public enum NearbyInfoType {AQI,PM_25};

	private List<OutdoorAQI> nearbyLocationAQIs;
	private List<String> topThreeCityAreaIdList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.outdoor_detail_fragment, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		OutdoorManager.getInstance().setOutdoorDetailsListener(this);
		calenderGMTChinese = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		Coordinates.getInstance(getMainActivity());

		OutdoorManager.getInstance().resetNeighborhoodCitiesData();
		OutdoorManager.getInstance().saveNearbyCityData();
		initializeUI();
		setClickEvent(true);
		showGraphView();
		
		getDataFromDashboard();

		requestAQIAndWeatherData();
		addFragment(new MarkerMapFragment(), R.id.outdoor_detail_map_container, "map_frag");
		if (PurAirApplication.isDemoModeEnable() 
				&& OutdoorController.getInstance().isPhilipsSetupWifiSelected()) {
			multiCityTrendTB.setClickable(false);
			multiCityTrendTB.setEnabled(false);
		} else {
			multiCityTrendTB.setClickable(true);
			multiCityTrendTB.setEnabled(true);
		}
	}

	private MainActivity getMainActivity() {
		return (MainActivity) getActivity();
	}

	private void showNearbyCityInfo(List<OutdoorAQI> nearbyLocationAQIs) {
		if (getActivity() == null) return;
		NeighbourhoodCityBaseAdapter adapter = new NeighbourhoodCityBaseAdapter(getActivity(), nearbyLocationAQIs, NearbyInfoType.AQI, selectedCityAreaId);
		nearbycitylv.setAdapter(adapter);
		neighbourhoodcityll.setVisibility(View.VISIBLE);
		ListViewHelper.setListViewSize(nearbycitylv);
	}
	/**
	 * Reading data from server
	 * */
	@SuppressLint({ "UseSparseArrays", "SimpleDateFormat" })
	private void calculateCMAAQIHistoricData(List<OutdoorAQI> outdoorAQIHistory, String areaId) {
		ALog.i(ALog.OUTDOOR_DETAILS, "Calculate Aqi value....");
		setClickEvent(true);

		if (outdoorAQIHistory != null && !outdoorAQIHistory.isEmpty()) {
			detailHelper = new OutdoorDetailHelper();
			detailHelper.calculateCMAAQIHistoricData(outdoorAQIHistory);
			updateAQIHistoricData(areaId);
		}

		handler.sendEmptyMessage(1);
	}

	private void updateAQIHistoricData(String areaId) {
		int color = DashboardUtil.getColorWithRespectToIndex(areaId, topThreeCityAreaIdList);
		lastDayAQIHistoricArr = detailHelper.getUpdateLastDayAQIHistoricArr();
		lastDayRDCPValuesMap.put(color, lastDayAQIHistoricArr);
		last7dayAQIHistoricArr = detailHelper.getUpdateLast7DayAQIHistoricArr();
		last7daysRDCPValuesMap.put(color, last7dayAQIHistoricArr);
		last4weekAQIHistoricArr = detailHelper.getUpdateLast4weekAQIHistoricArr();
		last4weeksRDCPValuesMap.put(color, last4weekAQIHistoricArr);
		currentCityHourOfDay = detailHelper.getCurrentCityHourOfDay();
		currentCityDayOfWeek = detailHelper.getCurrentCityDayOfWeek();
		setCityTrendProgressBarInvisible(areaId);
	}

	@SuppressLint("UseSparseArrays")
	private void calculateUSEmbassyAQIHistoricData(List<OutdoorAQI> outdoorAQIHistory, String cityName) {
		setClickEvent(true);

		if (outdoorAQIHistory != null && !outdoorAQIHistory.isEmpty()) {
			detailHelper = new OutdoorDetailHelper();
			detailHelper.calculateUSEmbassyAQIHistoricData(outdoorAQIHistory);
			updateAQIHistoricData(cityName);
		}

		handler.sendEmptyMessage(1);
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
		disableMapZoomImage();
		/**
		 * Updating all the details in the screen, which is passed from Dashboard
		 */
		Bundle bundle = getArguments();
		if (bundle == null) return;
		OutdoorAQI aqiValue = (OutdoorAQI) bundle.getSerializable(AppConstants.OUTDOOR_AQI) ;

		if( aqiValue != null) {
			setOutdoorCityCode(aqiValue.getAreaID());
			selectedCityAreaId = aqiValue.getAreaID() ;
			setAdviceIconTex(aqiValue.getAQI());

			if (OutdoorController.getInstance().isPhilipsSetupWifiSelected()){
				weatherProgressBar.setVisibility(View.GONE);
				aqiProgressBar.setVisibility(View.GONE);
				showAlertDialog("", getString(R.string.outdoor_download_failed));
				return;
			}

		}
	}

	private void requestAQIAndWeatherData() {
		if(selectedCityAreaId == null || selectedCityAreaId.isEmpty()) return;
		OutdoorManager.getInstance().startHistoricalAQITask(selectedCityAreaId);
		OutdoorManager.getInstance().startOneDayWeatherForecastTask(selectedCityAreaId);
		OutdoorManager.getInstance().startCityFourDayForecastTask(selectedCityAreaId);
		OutdoorManager.getInstance().startNearbyLocalitiesTask(selectedCityAreaId);
		city1stLL.setVisibility(View.VISIBLE);
		city1stIMGR.color(Color.GRAY);
		city1stTV.setText(OutdoorManager.getInstance().getLocaleCityNameFromAreaId(selectedCityAreaId));
	}

	private boolean multiCityAQIHistoricTaskStart = false;
	private void requestAQIHistoricDataForMultipleCity() {
		multiCityAQIHistoricTaskStart = true;
		setMultipleCityVisibility(View.VISIBLE);
		
		topThreeCityAreaId();
		int index = 0;
		for (String key : topThreeCityAreaIdList) {
			OutdoorManager.getInstance().startHistoricalAQITask(key);
			setCityNameAndColor(index, key) ;
			index++;
		}
	}

	private void disableMapZoomImage() {
		if (PurAirApplication.isDemoModeEnable()  
				&& OutdoorController.getInstance().isPhilipsSetupWifiSelected()) {
			dummyMapImage.setVisibility(View.VISIBLE);
			gaodeMapZoom.setVisibility(View.INVISIBLE);
		}
	}

	/**Set advice icon and text*/ 
	private void setAdviceIconTex(int aqiInt) {
		if(aqiInt >= 0 && aqiInt <= 50) {
			adviceLayout.setBackgroundColor(GraphConst.COLOR_STATE_BLUE);
			maskImg.setImageResource(R.drawable.advice_mask_not_needed); 
			openWindowImg.setImageResource(R.drawable.advice_window_open);  
			avoidImg.setImageResource(R.drawable.advice_go_outdoor); 

			maskTxt.setText(getString(R.string.mask_od_msg1));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg1)); 
			avoidTxt.setText(getString(R.string.advice_od_msg1));

		} else if(aqiInt > 50 && aqiInt <= 100) {
			adviceLayout.setBackgroundColor(GraphConst.COLOR_ROYAL_BLUE);
			maskImg.setImageResource(R.drawable.advice_mask_not_needed); 
			openWindowImg.setImageResource(R.drawable.advice_window_open);  
			avoidImg.setImageResource(R.drawable.advice_go_outdoor); 

			maskTxt.setText(getString(R.string.mask_od_msg1));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg1)); 
			avoidTxt.setText(getString(R.string.advice_od_msg2));

		} else if(aqiInt > 100 && aqiInt <= 150) {
			adviceLayout.setBackgroundColor(GraphConst.COLOR_INDIGO);
			maskImg.setImageResource(R.drawable.advice_mask_not_needed); 
			openWindowImg.setImageResource(R.drawable.advice_window_open);  
			avoidImg.setImageResource(R.drawable.advice_avoid_outdoor); 

			maskTxt.setText(getString(R.string.mask_od_msg1));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg1)); 
			avoidTxt.setText(getString(R.string.advice_od_msg3));

		} else if(aqiInt > 150 && aqiInt <= 200) {
			adviceLayout.setBackgroundColor(GraphConst.COLOR_PURPLE);
			maskImg.setImageResource(R.drawable.advice_mask_not_needed); 
			openWindowImg.setImageResource(R.drawable.advice_window_closed);  
			avoidImg.setImageResource(R.drawable.advice_avoid_outdoor); 

			maskTxt.setText(getString(R.string.mask_od_msg1));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg2)); 
			avoidTxt.setText(getString(R.string.advice_od_msg3));

		} else if(aqiInt > 200 && aqiInt <= 300) {
			adviceLayout.setBackgroundColor(GraphConst.COLOR_DEEP_PINK);
			maskImg.setImageResource(R.drawable.advice_mask_needed); 
			openWindowImg.setImageResource(R.drawable.advice_window_closed);  
			avoidImg.setImageResource(R.drawable.advice_avoid_outdoor); 

			maskTxt.setText(getString(R.string.mask_od_msg2));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg2)); 
			avoidTxt.setText(getString(R.string.advice_od_msg3));

		} else if (aqiInt > 300) {
			adviceLayout.setBackgroundColor(GraphConst.COLOR_RED);
			maskImg.setImageResource(R.drawable.advice_mask_needed); 
			openWindowImg.setImageResource(R.drawable.advice_window_closed);  
			avoidImg.setImageResource(R.drawable.advice_avoid_outdoor); 

			maskTxt.setText(getString(R.string.mask_od_msg2));
			openWindowTxt.setText(getString(R.string.openwindow_od_msg2)); 
			avoidTxt.setText(getString(R.string.advice_od_msg3));

		}
	}

	/**
	 * Initialize UI widget
	 * */
	@SuppressLint("UseSparseArrays")
	private void initializeUI() {
		topThreeCityAreaIdList = new ArrayList<String>();
		lastDayRDCPValuesMap = new HashMap<Integer, float[]>();
		last7daysRDCPValuesMap = new HashMap<Integer, float[]>();
		last4weeksRDCPValuesMap = new HashMap<Integer, float[]>();
		View view = getView();
		graphLayout = (LinearLayout) view.findViewById(R.id.detailsOutdoorlayoutGraph);
		wetherScrollView = (HorizontalScrollView) view.findViewById(R.id.odTodayWetherReportHSV);
		wetherForcastLayout = (LinearLayout) view.findViewById(R.id.odWetherForcastLL);
		adviceLayout = (LinearLayout) view.findViewById(R.id.outdoor_advice_ll);

		lastDayBtn = (FontTextView) view.findViewById(R.id.detailsOutdoorLastDayLabel);
		lastWeekBtn = (FontTextView) view.findViewById(R.id.detailsOutdoorLastWeekLabel);
		lastFourWeekBtn = (FontTextView) view.findViewById(R.id.detailsOutdoorLastFourWeekLabel);

		avoidImg = (ImageView) view.findViewById(R.id.avoidOutdoorImg);  
		openWindowImg = (ImageView) view.findViewById(R.id.openWindowImg);  
		maskImg = (ImageView) view.findViewById(R.id.maskImg); 
		dummyMapImage = (ImageView) view.findViewById(R.id.dummy_map_img);

		avoidTxt = (FontTextView) view.findViewById(R.id.avoidOutdoorTxt); 
		openWindowTxt = (FontTextView) view.findViewById(R.id.openWindowTxt); 
		maskTxt = (FontTextView) view.findViewById(R.id.maskTxt);

		aqiProgressBar = (ProgressBar) view.findViewById(R.id.outdoorAqiDownloadProgressBar);
		weatherProgressBar = (ProgressBar) view.findViewById(R.id.weatherProgressBar);

		mapGaodeLayout = (RelativeLayout) view.findViewById(R.id.outdoor_detail_map_rl);
		setupGaodeMap();
		aqitv = (FontTextView) view.findViewById(R.id.aqitv);
		pmtv = (FontTextView) view.findViewById(R.id.pmtv);
		neighbourhoodcityll = (LinearLayout) view.findViewById(R.id.neighbourhoodDetailsLayout);
		nearbycitylv = (ListView) view.findViewById(R.id.outdoor_detail_neighborhood_listview);

		/**
		 * Set click listener
		 * */
		lastDayBtn.setOnClickListener(this);
		lastWeekBtn.setOnClickListener(this);
		lastFourWeekBtn.setOnClickListener(this);
		aqitv.setOnClickListener(this);
		pmtv.setOnClickListener(this);

		FontButton airQualityAnalysis = (FontButton) view.findViewById(R.id.outdoor_detail_air_quality_analysis_fb);
		airQualityAnalysis.setOnClickListener(this);

		initiMultipleCityView(view);
	}

	private void initiMultipleCityView(View view) {
		city1stLL = (LinearLayout) view.findViewById(R.id.city_1_ll);
		city2ndLL = (LinearLayout) view.findViewById(R.id.city_2_ll); 
		city2ndLL.setVisibility(View.GONE);
		city3rdLL = (LinearLayout) view.findViewById(R.id.city_3_ll); 
		city3rdLL.setVisibility(View.GONE);
		city4thLL = (LinearLayout) view.findViewById(R.id.city_4_ll);
		city4thLL.setVisibility(View.GONE);
		city1stTV = (FontTextView) view.findViewById(R.id.city_1_tv);
		city2ndTV = (FontTextView) view.findViewById(R.id.city_2_tv);
		city3rdTV = (FontTextView) view.findViewById(R.id.city_3_tv);
		city4thTV = (FontTextView) view.findViewById(R.id.city_4_tv);
		city1stPB = (ProgressBar) view.findViewById(R.id.city_1_progressBar);
		city2ndPB = (ProgressBar) view.findViewById(R.id.city_2_progressBar);
		city3rdPB = (ProgressBar) view.findViewById(R.id.city_3_progressBar); 
		city4thPB = (ProgressBar) view.findViewById(R.id.city_4_progressBar);
		city1stIMGR = (ImageRound) view.findViewById(R.id.city_1_iv);
		city1stIMGR.color(GraphConst.PHILIPS_RED_COLOR);
		city2ndIMGR = (ImageRound) view.findViewById(R.id.city_2_iv);
		city2ndIMGR.color(GraphConst.PHILIPS_DARK_GREEN_COLOR);
		city3rdIMGR = (ImageRound) view.findViewById(R.id.city_3_iv); 
		city3rdIMGR.color(GraphConst.PHILIPS_BRIFHT_BLUE_COLOR);
		city4thIMGR = (ImageRound) view.findViewById(R.id.city_4_iv);
		city4thIMGR.color(GraphConst.BLACK_COLOR);
		
		multiCityTrendTB = (ToggleButton) view.findViewById(R.id.multi_city_trends_toggle);
		multiCityTrendTB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked && !multiCityAQIHistoricTaskStart) {
					requestAQIHistoricDataForMultipleCity();
				}

				setTrentView(aqiHistoricSate);
				
				if (isChecked) {
					setMultipleCityVisibility(View.VISIBLE);
					city1stTV.setTextColor(GraphConst.PHILIPS_RED_COLOR);
					city1stIMGR.color(GraphConst.PHILIPS_RED_COLOR);
				} else {
					setMultipleCityVisibility(View.GONE);
					city1stTV.setTextColor(Color.GRAY);
					city1stIMGR.color(Color.GRAY);
				}

			}
		});

	}

	private void setupGaodeMap() {
		mapGaodeLayout.setVisibility(View.VISIBLE);

		gaodeMapZoom = (ImageView) getView().findViewById(R.id.gaodeMapZoomImg);
		gaodeMapZoom.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		MetricsTracker.trackPage(TrackPageConstants.OUTDOOR_DETAILS);
	}

	@Override
	public void onDestroy() {
		OutdoorManager.getInstance().removeOutdoorDetailsListener();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.gaodeMapZoomImg:
			Intent gaodeMapIntent = new Intent(getMainActivity(), MarkerActivity.class);
			startActivity(gaodeMapIntent);
			break;
		case R.id.detailsOutdoorLastDayLabel:
			MetricsTracker.trackPage(TrackPageConstants.OUTDOOR_DETAILS + "LastDay");
			aqiHistoricSate = AqiHistoricState.LAST_DAY;
			setTrentView(aqiHistoricSate);
			break;
		case R.id.detailsOutdoorLastWeekLabel: 
			MetricsTracker.trackPage(TrackPageConstants.OUTDOOR_DETAILS + "LastWeek");
			aqiHistoricSate = AqiHistoricState.LAST_WEEK;
			setTrentView(aqiHistoricSate);
			break;
		case R.id.detailsOutdoorLastFourWeekLabel: 
			MetricsTracker.trackPage(TrackPageConstants.OUTDOOR_DETAILS + "LastFourWeeks");
			aqiHistoricSate = AqiHistoricState.LAST_MONTH;
			setTrentView(aqiHistoricSate);
			break;
		case R.id.aqitv: 
			aqitv.setBackgroundResource(R.drawable.highlight_text_view_bg);
			pmtv.setBackgroundResource(R.drawable.normal_text_view_bg);
			setNeighbourhoodAdapter(NearbyInfoType.AQI) ;
			break;
		case R.id.pmtv: 
			pmtv.setBackgroundResource(R.drawable.highlight_text_view_bg);
			aqitv.setBackgroundResource(R.drawable.normal_text_view_bg);
			setNeighbourhoodAdapter(NearbyInfoType.PM_25) ;
			break;
		case R.id.outdoor_detail_air_quality_analysis_fb:
			Intent intent = new Intent(getActivity(), AirAnalysisExplainActivity.class);
			intent.putExtra(AirAnalysisExplainActivity.TYPE_EXTRA, AirAnalysisExplainActivity.OUTDOOR_EXTRA);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void setNeighbourhoodAdapter(NearbyInfoType infoType) {
		if (nearbyLocationAQIs == null || nearbyLocationAQIs.isEmpty() || getActivity() == null) {
			return;
		}
		adapter = new NeighbourhoodCityBaseAdapter(getActivity(),nearbyLocationAQIs, infoType, selectedCityAreaId);
		nearbycitylv.setAdapter(adapter);
	}

	private synchronized void setTrentView(AqiHistoricState aqiHistoricState) {
		removeChildViewFromBar();
		switch (aqiHistoricState) {
		case LAST_DAY:
			setViewlastDayAQIReadings();
			break;
		case LAST_WEEK:
			setViewlast7DayAQIReadings();
			break;
		case LAST_MONTH:
			setViewlast4WeeksAQIReadings();
			break;
		default:
			//NOP
			break;
		}
	}

	private synchronized void setViewlastDayAQIReadings() {
		if (lastDayRDCPValuesMap != null && lastDayRDCPValuesMap.size() > 0 && getMainActivity() != null) {
			graphLayout.addView(new GraphView(getMainActivity(), lastDayRDCPValuesMap, multiCityTrendTB.isChecked()));
		}
		lastDayBtn.setBackgroundResource(R.drawable.highlight_text_view_bg);
		lastWeekBtn.setBackgroundResource(R.drawable.normal_text_view_bg);
		lastFourWeekBtn.setBackgroundResource(R.drawable.normal_text_view_bg);
	}

	private synchronized void setViewlast7DayAQIReadings() {
		if (last7daysRDCPValuesMap != null && last7daysRDCPValuesMap.size() > 0 && getMainActivity() != null) {
			graphLayout.addView(new GraphView(getMainActivity(), last7daysRDCPValuesMap, multiCityTrendTB.isChecked()));
		}
		lastDayBtn.setBackgroundResource(R.drawable.normal_text_view_bg);
		lastWeekBtn.setBackgroundResource(R.drawable.highlight_text_view_bg);
		lastFourWeekBtn.setBackgroundResource(R.drawable.normal_text_view_bg);
	}

	private synchronized void setViewlast4WeeksAQIReadings() {
		if (last4weeksRDCPValuesMap != null && last4weeksRDCPValuesMap.size() > 0 && getMainActivity() != null) {
			graphLayout.addView(new GraphView(getMainActivity(), last4weeksRDCPValuesMap, multiCityTrendTB.isChecked()));
		}
		lastDayBtn.setBackgroundResource(R.drawable.normal_text_view_bg);
		lastWeekBtn.setBackgroundResource(R.drawable.normal_text_view_bg);
		lastFourWeekBtn.setBackgroundResource(R.drawable.highlight_text_view_bg);
	}

	/**
	 * 
	 */
	private void removeChildViewFromBar() {
		if (graphLayout.getChildCount() > 0) {
			graphLayout.removeAllViews();
		}
	}

	private void setClickEvent(boolean click) {
		lastDayBtn.setClickable(click);
		lastWeekBtn.setClickable(click);
		lastFourWeekBtn.setClickable(click);
	}

	public static int getCurrentCityHourOfDay() {
		return currentCityHourOfDay;
	}

	public static int getCurrentCityDayOfWeek() {
		return currentCityDayOfWeek;
	}

	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (getMainActivity() == null) return;
			if ( msg.what == 1 ) {
				aqiProgressBar.setVisibility(View.GONE);
				setTrentView(aqiHistoricSate);
			} else if ( msg.what == 2 ) {
				aqiProgressBar.setVisibility(View.GONE);
				weatherProgressBar.setVisibility(View.GONE);
				showAlertDialog("", getString(R.string.outdoor_download_failed));
			}
		};
	};

	private void showAlertDialog(String title, String message) {
		if (getMainActivity() == null) return;
		if (PurAirApplication.isDemoModeEnable() 
				&& OutdoorController.getInstance().isPhilipsSetupWifiSelected()) {
			addDummyDataForDemoMode();
		} else {
			try {
				FragmentTransaction fragTransaction = getMainActivity().getSupportFragmentManager().beginTransaction();

				Fragment prevFrag = getMainActivity().getSupportFragmentManager().findFragmentByTag("outdoor_download_failed");
				if (prevFrag != null) {
					fragTransaction.remove(prevFrag);
				}

				fragTransaction.add(DownloadAlerDialogFragement.
						newInstance(title, message), "outdoor_download_failed").commitAllowingStateLoss();
			} catch (IllegalStateException e) {
				ALog.e(ALog.ERROR, "Error: " + e.getMessage());
			}
		}
	}

	private void addDummyDataForDemoMode() {
		currentCityHourOfDay = calenderGMTChinese.get(Calendar.HOUR_OF_DAY);
		currentCityDayOfWeek = calenderGMTChinese.get(Calendar.DAY_OF_WEEK);
		lastDayAQIHistoricArr = DummyData.getInstance().getLastDayAqis(selectedCityAreaId);
		lastDayRDCPValuesMap.put(GraphConst.PHILIPS_RED_COLOR, lastDayAQIHistoricArr);
		last7dayAQIHistoricArr = DummyData.getInstance().getLastWeekAqis(selectedCityAreaId);
		last7daysRDCPValuesMap.put(GraphConst.PHILIPS_RED_COLOR, last7dayAQIHistoricArr);
		last4weekAQIHistoricArr = DummyData.getInstance().getLastMonthAqis(selectedCityAreaId);
		last4weeksRDCPValuesMap.put(GraphConst.PHILIPS_RED_COLOR, last4weekAQIHistoricArr);

		setTrentView(aqiHistoricSate);
		setClickEvent(true);

		if (wetherScrollView.getChildCount() > 0) {
			wetherScrollView.removeAllViews();
		}
		wetherScrollView.addView(new WeatherForecastLayout(
				getActivity(), null, DummyData.getInstance().getTodayWeatherForecast()));

		if (weatherReportLayout != null && weatherReportLayout.getChildCount() > 0) {
			weatherReportLayout.removeAllViews();
		}

		weatherReportLayout = new WeatherForecastLayout(
				getActivity(), null, 0, DummyData.getInstance().getFourDayWeatherForecast());
		weatherReportLayout.setOrientation(LinearLayout.VERTICAL);
		wetherForcastLayout.addView(weatherReportLayout);
	}

	@Override
	public void onOneDayWeatherForecastReceived(final List<Weatherdto> weatherList) {
		if( weatherList != null && getMainActivity() != null) {
			ALog.i(ALog.OUTDOOR_DETAILS, "Outdoor Weather received: "+weatherList.size()) ;
			getMainActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (getActivity() == null) return;
					if (wetherScrollView.getChildCount() > 0) {
						wetherScrollView.removeAllViews();
					}
					wetherScrollView.addView(
							new WeatherForecastLayout(getActivity(), null, weatherList));
				}
			});
		}
	}

	private void setCityNameAndColor(int position, String areaId) {
		String cityName = OutdoorManager.getInstance().getLocaleCityNameFromAreaId(areaId);
		switch (position) {
		case 0:
			city2ndTV.setText(cityName);
			city2ndPB.setVisibility(View.VISIBLE);
			break;
		case 1:
			city3rdTV.setText(cityName);
			city3rdPB.setVisibility(View.VISIBLE);
			break;
		case 2:
			city4thTV.setText(cityName);
			city4thPB.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}
	
	private void setCityTrendProgressBarInvisible(final String areaId) {
		if (getActivity() == null) return;
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (areaId.equals(selectedCityAreaId)) {
					city1stPB.setVisibility(View.GONE);
					return;
				}
				if (topThreeCityAreaIdList == null) return;
				switch (topThreeCityAreaIdList.indexOf(areaId)) {
				case 0:
					city2ndPB.setVisibility(View.GONE);
					break;
				case 1:
					city3rdPB.setVisibility(View.GONE);
					break;
				case 2:
					city4thPB.setVisibility(View.GONE);
					break;

				default:
					break;
				}
			}
		});
		
	}
	
	private void setMultipleCityVisibility(int visibility) {
		city2ndLL.setVisibility(visibility);
		city3rdLL.setVisibility(visibility);
		city4thLL.setVisibility(visibility);
	}
	
	private void topThreeCityAreaId() {
		topThreeCityAreaIdList.clear();
		List<String> userSelectedCityAreaIdList = OutdoorManager.getInstance().getUsersCitiesList();
		for (String areaId : userSelectedCityAreaIdList) {
			if (topThreeCityAreaIdList.size() < 3 && !areaId.equals(selectedCityAreaId) && !areaId.isEmpty()) {
				topThreeCityAreaIdList.add(areaId);
			}
		}
	}


	@Override
	public void onFourDayWeatherForecastReceived(final List<ForecastWeatherDto> weatherList) {
		ALog.i(ALog.OUTDOOR_DETAILS, "Outdoor Weather received: "+weatherList.size()) ;
		if (getMainActivity() == null) return;
		getMainActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (getActivity() == null) return;
				weatherProgressBar.setVisibility(View.GONE);
				if( weatherList != null ) {

					if (weatherReportLayout != null && weatherReportLayout.getChildCount() > 0) {
						weatherReportLayout.removeAllViews();
					}

					weatherReportLayout = 
							new WeatherForecastLayout(getActivity(), null, 0, weatherList);
					weatherReportLayout.setOrientation(LinearLayout.VERTICAL);
					wetherForcastLayout.addView(weatherReportLayout);
				}
			}
		});
	}

	//TODO : Show error message when no data is shown. handler.sendEmptyMessage(2);
	@Override
	public void onAQIHistoricalDataReceived(List<OutdoorAQI> outdoorAQIHistory, String areaId) {
		ALog.i(ALog.OUTDOOR_DETAILS, "Outdoor onAQIHistoricalDataReceived: "+areaId) ;
		if (getMainActivity() == null) return;
		synchronized (this) {
			if (OutdoorManager.getInstance().getUSEmbassyCities().contains(areaId)) {
				calculateUSEmbassyAQIHistoricData(outdoorAQIHistory, areaId);
			} else {
				calculateCMAAQIHistoricData(outdoorAQIHistory, areaId);
			}
		}
		
	}

	@Override
	public void onNearbyLocationsDataReceived(final List<OutdoorAQI> nearbyLocationAQIs) {
		if (nearbyLocationAQIs == null || nearbyLocationAQIs.isEmpty() || getMainActivity() == null) {
			return;
		}
		this.nearbyLocationAQIs = nearbyLocationAQIs;
		OutdoorManager.getInstance().setNeighborhoodCitiesData(nearbyLocationAQIs);
		getMainActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				showNearbyCityInfo(nearbyLocationAQIs);
			}
		});
	}

	private void addFragment(Fragment fragment, int containerId, String tag) {
		MainActivity activity = (MainActivity)getActivity();
		if (activity == null) return;
		try{
			FragmentTransaction fragmentTransaction = 
					activity.getSupportFragmentManager().beginTransaction();
			Fragment prevFragmentMap = activity.getSupportFragmentManager()
					.findFragmentByTag(tag);
			if (prevFragmentMap != null) {
				fragmentTransaction.remove(prevFragmentMap);
			}
			fragmentTransaction.add(containerId, fragment, tag);
			fragmentTransaction.commit();
		} catch (IllegalStateException e) {
			ALog.e(ALog.ERROR, "IllegalStateException: " + e.getMessage());
		}
	}


	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (getActivity() == null) return;
		try {
			FragmentTransaction fragmentTransaction = 
					getActivity().getSupportFragmentManager().beginTransaction();
			Fragment prevFragmentMap = getActivity().getSupportFragmentManager()
					.findFragmentById(R.id.outdoor_detail_map_container);
			if (prevFragmentMap != null) {
				fragmentTransaction.remove(prevFragmentMap);
			}
			fragmentTransaction.commitAllowingStateLoss();
		} catch (IllegalStateException e) {
			ALog.e(ALog.ERROR, "IllegalStateException");
		}
	}
	
	//Display empty graph view
	private void showGraphView() {
		lastDayRDCPValuesMap.put(GraphConst.PHILIPS_RED_COLOR, lastDayAQIHistoricArr);
		last7daysRDCPValuesMap.put(GraphConst.PHILIPS_RED_COLOR, last7dayAQIHistoricArr);
		last4weeksRDCPValuesMap.put(GraphConst.PHILIPS_RED_COLOR, last4weekAQIHistoricArr);
		setViewlastDayAQIReadings();
	}

}
