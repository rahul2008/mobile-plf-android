package com.philips.cl.di.dev.pa.dashboard;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.activity.MarkerActivity;
import com.philips.cl.di.dev.pa.adapter.NeighbourhoodCityBaseAdapter;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.fragment.DownloadAlerDialogFragement;
import com.philips.cl.di.dev.pa.fragment.MarkerMapFragment;
import com.philips.cl.di.dev.pa.fragment.OutdoorAQIExplainedDialogFragment;
import com.philips.cl.di.dev.pa.outdoorlocations.DummyData;
import com.philips.cl.di.dev.pa.outdoorlocations.OutdoorDataProvider;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Coordinates;
import com.philips.cl.di.dev.pa.util.GraphConst;
import com.philips.cl.di.dev.pa.util.ListViewHelper;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.OutdoorDetailsListener;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.FontButton;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.dev.pa.view.GraphView;
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
	
	
	private ViewGroup mapGaodeLayout;
	private String areaId;
	private List<OutdoorAQI> outdoorAQIs;
	private Calendar calenderGMTChinese;
	
	private float lastDayAQIHistoricArr[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,
			-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F};
	private float last7dayAQIHistoricArr[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F};
	private float last4weekAQIHistoricArr[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,
			-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F};
	
	private int outdoorDataProvider = OutdoorDataProvider.CMA.ordinal();
	private OutdoorDetailHelper detailHelper;
	public enum NearbyInfoType {AQI,PM_25};
	
	private List<OutdoorAQI> nearbyLocationAQIs;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.outdoor_detail_fragment, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		calenderGMTChinese = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		Coordinates.getInstance(getMainActivity());
		
		OutdoorManager.getInstance().resetNeighborhoodCitiesData();
		OutdoorManager.getInstance().saveNearbyCityData();
		
		initializeUI();
		setClickEvent(false);
		detailHelper = new OutdoorDetailHelper(lastDayAQIHistoricArr, last7dayAQIHistoricArr, last4weekAQIHistoricArr);
		getDataFromDashboard();

		requestAQIAndWeatherData();
		addFragment(new MarkerMapFragment(), R.id.outdoor_detail_map_container, "map_frag");
	}

	private MainActivity getMainActivity() {
		return (MainActivity) getActivity();
	}
	
	private void showNearbyCityInfo(List<OutdoorAQI> nearbyLocationAQIs) {
		if (getActivity() == null) return;
		NeighbourhoodCityBaseAdapter adapter = new NeighbourhoodCityBaseAdapter(getActivity(), nearbyLocationAQIs, NearbyInfoType.AQI, areaId);
		nearbycitylv.setAdapter(adapter);
		neighbourhoodcityll.setVisibility(View.VISIBLE);
		ListViewHelper.setListViewSize(nearbycitylv);
	}
	/**
	 * Reading data from server
	 * */
	@SuppressLint({ "UseSparseArrays", "SimpleDateFormat" })
	private void calculateCMAAQIHistoricData() {
		ALog.i(ALog.OUTDOOR_DETAILS, "Calculate Aqi value....");
		setClickEvent(true);
		
		if (outdoorAQIs != null && !outdoorAQIs.isEmpty() && getMainActivity() != null) {
			detailHelper.calculateCMAAQIHistoricData(outdoorAQIs);
			updateAQIHistoricData();
		}
		
		handler.sendEmptyMessage(1);
	}
	
	private void updateAQIHistoricData() {
		lastDayAQIHistoricArr = detailHelper.getUpdateLastDayAQIHistoricArr();
		last7dayAQIHistoricArr = detailHelper.getUpdateLast7DayAQIHistoricArr();
		last4weekAQIHistoricArr = detailHelper.getUpdateLast4weekAQIHistoricArr();
		currentCityHourOfDay = detailHelper.getCurrentCityHourOfDay();
		currentCityDayOfWeek = detailHelper.getCurrentCityDayOfWeek();
	}
	
	@SuppressLint("UseSparseArrays")
	private void calculateUSEmbassyAQIHistoricData() {
		setClickEvent(true);
		
		if (outdoorAQIs != null && !outdoorAQIs.isEmpty()) {
			detailHelper.calculateUSEmbassyAQIHistoricData(outdoorAQIs);
			updateAQIHistoricData();
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

		outdoorDataProvider = bundle.getInt(AppConstants.OUTDOOR_DATAPROVIDER, 0);

		if( aqiValue != null) {
			setOutdoorCityCode(aqiValue.getAreaID());
			areaId = aqiValue.getAreaID() ;
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
		if(areaId == null || areaId.isEmpty()) return;
		OutdoorManager.getInstance().startHistoricalAQITask(areaId);
		OutdoorManager.getInstance().startOneDayWeatherForecastTask(areaId);
		OutdoorManager.getInstance().startCityFourDayForecastTask(areaId);
		OutdoorManager.getInstance().startNearbyLocalitiesTask(areaId);
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
	private void initializeUI() {
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
		OutdoorManager.getInstance().setOutdoorDetailsListener(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		OutdoorManager.getInstance().removeOutdoorDetailsListener(this);
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
				setViewlastDayAQIReadings();
				break;
			case R.id.detailsOutdoorLastWeekLabel: 
				MetricsTracker.trackPage(TrackPageConstants.OUTDOOR_DETAILS + "LastWeek");
				setViewlast7DayAQIReadings();
				break;
			case R.id.detailsOutdoorLastFourWeekLabel: 
				MetricsTracker.trackPage(TrackPageConstants.OUTDOOR_DETAILS + "LastFourWeeks");
				setViewlast4WeeksAQIReadings();
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
				airQualityAnalysis();
				break;
			default:
				break;
		}
	}
	
	private void setNeighbourhoodAdapter(NearbyInfoType infoType) {
		if (nearbyLocationAQIs == null || nearbyLocationAQIs.isEmpty() || getActivity() == null) {
			return;
		}
		adapter = new NeighbourhoodCityBaseAdapter(getActivity(),nearbyLocationAQIs, infoType, areaId);
		nearbycitylv.setAdapter(adapter);
	}
	
	private void setViewlastDayAQIReadings() {
		removeChildViewFromBar();
		if (lastDayAQIHistoricArr != null && lastDayAQIHistoricArr.length > 0 && getMainActivity() != null) {
			graphLayout.addView(new GraphView(getMainActivity(), lastDayAQIHistoricArr));
		}
		lastDayBtn.setBackgroundResource(R.drawable.highlight_text_view_bg);
		lastWeekBtn.setBackgroundResource(R.drawable.normal_text_view_bg);
		lastFourWeekBtn.setBackgroundResource(R.drawable.normal_text_view_bg);
	}
	
	private void setViewlast7DayAQIReadings() {
		removeChildViewFromBar();
		if (last7dayAQIHistoricArr != null && last7dayAQIHistoricArr.length > 0 && getMainActivity() != null) {
			graphLayout.addView(new GraphView(getMainActivity(), last7dayAQIHistoricArr));
		}
		lastDayBtn.setBackgroundResource(R.drawable.normal_text_view_bg);
		lastWeekBtn.setBackgroundResource(R.drawable.highlight_text_view_bg);
		lastFourWeekBtn.setBackgroundResource(R.drawable.normal_text_view_bg);
	}

	private void setViewlast4WeeksAQIReadings() {
		removeChildViewFromBar();
		if (last4weekAQIHistoricArr != null && last4weekAQIHistoricArr.length > 0 && getMainActivity() != null) {
			graphLayout.addView(new GraphView(getMainActivity(), last4weekAQIHistoricArr));
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

	/**
	 * 
	 * @param v
	 */
	public void airQualityAnalysis() {
		if (getMainActivity() == null) return;
		try {
			FragmentManager fragMan = getMainActivity().getSupportFragmentManager();
			fragMan.beginTransaction().add(
					OutdoorAQIExplainedDialogFragment.newInstance(), "outdoorexplained").commit();
		} catch (IllegalStateException e) {
			ALog.e(ALog.OUTDOOR_DETAILS, "Error: " + e.getMessage());
		}
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
				setViewlastDayAQIReadings();
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
		lastDayAQIHistoricArr= DummyData.getInstance().getLastDayAqis(areaId);
		last7dayAQIHistoricArr= DummyData.getInstance().getLastWeekAqis(areaId);
		last4weekAQIHistoricArr = DummyData.getInstance().getLastMonthAqis(areaId);

		setViewlastDayAQIReadings();
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
	public void onAQIHistoricalDataReceived(List<OutdoorAQI> outdoorAQIHistory) {
		if (getMainActivity() == null) return;
		outdoorAQIs = outdoorAQIHistory;
		if (outdoorDataProvider == OutdoorDataProvider.US_EMBASSY.ordinal()) {
			calculateUSEmbassyAQIHistoricData();
		} else {
			calculateCMAAQIHistoricData();
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

}
