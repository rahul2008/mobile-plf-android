package com.philips.cl.di.dev.pa.dashboard;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.ICPDownloadListener;
import com.philips.cl.di.dev.pa.dashboard.PurifierCurrentCityData.PurifierCurrentCityPercentListener;
import com.philips.cl.di.dev.pa.datamodel.IndoorTrendDto;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.fragment.DownloadAlerDialogFragement;
import com.philips.cl.di.dev.pa.fragment.IndoorAQIExplainedDialogFragment;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Coordinates;
import com.philips.cl.di.dev.pa.util.DashboardUtil.Detail;
import com.philips.cl.di.dev.pa.util.GraphConst;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.util.Utils;
import com.philips.cl.di.dev.pa.view.AirView;
import com.philips.cl.di.dev.pa.view.FontButton;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.dev.pa.view.GraphView;
import com.philips.icpinterface.data.Errors;

public class IndoorDetailFragment extends BaseFragment implements OnClickListener,
		ICPDownloadListener, PurifierCurrentCityPercentListener {
	
	private final static int DOWNLOAD_FAILED = 1;
	private final static int DOWNLOAD_NA = 2;
	private final static int DOWNLOAD_COMPLETE = 3;
	private final static int OUTDOOR_TASK_COMPLETE = 4;
	private AirPurifier currentPurifier;
	private LinearLayout graphLayout;
	private TextView lastDayBtn, lastWeekBtn, lastFourWeekBtn;
	private FontButton airQualityExplainFB;
	private FontTextView msgFirst, msgSecond;
	private ViewGroup indoorBarChart, outdoorBarChart;
	private ProgressBar rdcpDownloadProgressBar;
	private List<float[]> lastDayRDCPValues;
	private List<float[]> last7daysRDCPValues;
	private List<float[]> last4weeksRDCPValues;
	private List<Float> hrlyAqiValues;
	private List<Float> dailyAqiValues ;
	private List<Integer> goodAirInfos;
	private List<Integer> currentCityGoodAirInfos;
	private int dayIndex = 0;
	private float lastDayRDCPVal[];
	private float last7daysRDCPVal[];
	private float last4weeksRDCPVal[];
	private String outdoorTitle = PurAirApplication.getAppContext().getString(R.string.good); 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.indoor_detail_fragment, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPage(TrackPageConstants.INDOOR_DETAILS);
		Coordinates.getInstance(getMainActivity());//Initialize all trend density independent pixel co-ordinate
		currentPurifier = AirPurifierManager.getInstance().getCurrentPurifier();
		init();
		lastDayRDCPVal = AppConstants.LAST_DAY_AQIHISTORIC_ARR;
		last7daysRDCPVal = AppConstants.LAST_WEEK_AQIHISTORIC_ARR;
		last4weeksRDCPVal = AppConstants.LAST_MONTH_AQIHISTORIC_ARR;
		
		//Purifier current city good air quality historic data download
		PurifierCurrentCityData.getInstance().setListener(this);
		if (currentPurifier != null) {
			PurifierCurrentCityData.getInstance().startCurrentCityAreaIdTask(
					currentPurifier.getLatitude(), currentPurifier.getLongitude());
		}

		downloadAQIHistoricData();
	}
	
	private void downloadAQIHistoricData() {
		if (currentPurifier == null) return;
		rdcpDownloadProgressBar.setVisibility(View.VISIBLE);
		CPPController.getInstance(getMainActivity()).setDownloadDataListener(this) ;
		CPPController.getInstance(getMainActivity()).downloadDataFromCPP(Utils.getCPPQuery(currentPurifier), 2048); //2048KB
	}

	/**
	 * Initialize UI widget
	 * */
	private void init() {
		initList();
		
		View view = getView();
		graphLayout = (LinearLayout) view.findViewById(R.id.trendsOutdoorlayoutGraph);

		lastDayBtn = (TextView) view.findViewById(R.id.detailsOutdoorLastDayLabel);
		lastWeekBtn = (TextView) view.findViewById(R.id.detailsOutdoorLastWeekLabel);
		lastFourWeekBtn = (TextView) view.findViewById(R.id.detailsOutdoorLastFourWeekLabel);


		msgFirst = (FontTextView) view.findViewById(R.id.idFirstMsg);
		msgSecond = (FontTextView) view.findViewById(R.id.idSecondMsg);

		rdcpDownloadProgressBar = (ProgressBar) view.findViewById(R.id.rdcpDownloadProgressBar);
		indoorBarChart = (RelativeLayout) view.findViewById(R.id.indoorDashboardBarPerc);
		outdoorBarChart = (RelativeLayout) view.findViewById(R.id.outdoorDashboardBarPerc);
		airQualityExplainFB = (FontButton) view.findViewById(R.id.indoor_detail_air_quality_explain_tv);

		initClickListener();
	}
	
	private void initList() {
		lastDayRDCPValues = new ArrayList<float[]>();
		last7daysRDCPValues = new ArrayList<float[]>();
		last4weeksRDCPValues = new ArrayList<float[]>();
		goodAirInfos = new ArrayList<Integer>();
		currentCityGoodAirInfos = new ArrayList<Integer>();
	}
	
	private void initClickListener() {
		lastDayBtn.setOnClickListener(this);
		lastWeekBtn.setOnClickListener(this);
		lastFourWeekBtn.setOnClickListener(this);
		airQualityExplainFB.setOnClickListener(this);
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
		case R.id.indoor_detail_air_quality_explain_tv:
			airQualityExplain();
			break;
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
		
		if (getMainActivity() != null && rdcpValues != null && rdcpValues.size() > 0) {
			graphLayout.addView(new GraphView(getMainActivity(), rdcpValues, 0, Detail.INDOOR));
		}	
	}
	
	private void addBarChartView(ViewGroup viewGroup, List<Integer> goodAirList, int index) {
		if (getMainActivity() != null && goodAirList != null && goodAirList.size() > 2) {
			FontTextView percentTxt = new FontTextView(getMainActivity());
			RelativeLayout.LayoutParams percentTxtParams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			percentTxtParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			percentTxt.setGravity(Gravity.CENTER);
			percentTxt.setTextColor(Color.WHITE);
			percentTxt.setTextSize(21);
			if (viewGroup.getChildCount() > 0) {
				viewGroup.removeAllViews();
			}
			viewGroup.addView(new AirView(getMainActivity(), goodAirList.get(index), 60, 80));
			percentTxt.setText(goodAirList.get(index) + "%");
			viewGroup.addView(percentTxt, percentTxtParams);
		}
	}

	private void setViewOnClick(int index) {
		switch (index) {
		case 0:
			lastDayBtn.setTextColor(GraphConst.COLOR_PHILIPS_BLUE);
			lastWeekBtn.setTextColor(Color.LTGRAY);
			lastFourWeekBtn.setTextColor(Color.LTGRAY);
			msgFirst.setText(getString(R.string.aqi_message_last_day));
			msgSecond.setText(getString(R.string.detail_aiq_message_last_day));
			break;
		case 1:
			lastDayBtn.setTextColor(Color.LTGRAY);
			lastWeekBtn.setTextColor(GraphConst.COLOR_PHILIPS_BLUE);
			lastFourWeekBtn.setTextColor(Color.LTGRAY);
			msgFirst.setText(getString(R.string.aqi_message_last7day));
			msgSecond.setText(getString(R.string.detail_aiq_message_last7day));		
			break;
		case 2:
			lastDayBtn.setTextColor(Color.LTGRAY);
			lastWeekBtn.setTextColor(Color.LTGRAY);
			lastFourWeekBtn.setTextColor(GraphConst.COLOR_PHILIPS_BLUE);
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

	private void airQualityExplain() {
		if (getMainActivity() == null) return;
		try {
			FragmentTransaction fragTransaction = 
					getMainActivity().getSupportFragmentManager().beginTransaction();

			Fragment prevFrag = getMainActivity()
					.getSupportFragmentManager().findFragmentByTag("indoor_aqi_analysis");
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
		if (getMainActivity() == null) return;
		try {
			FragmentTransaction fragTransaction = getMainActivity().getSupportFragmentManager().beginTransaction();
			
			Fragment prevFrag = getMainActivity().getSupportFragmentManager()
					.findFragmentByTag("alert_aqi_historic_download_failed");
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
			if (getMainActivity() == null) return;
			
			switch (msg.what) {
			case DOWNLOAD_NA:
				showAlertDialogHistoryDoawnload(getString(R.string.aqi_history), 
						getString(R.string.aqi_history_not_avialable));
				rdcpDownloadProgressBar.setVisibility(View.GONE);
				break;
			case DOWNLOAD_FAILED:
				showAlertDialogHistoryDoawnload(getString(R.string.aqi_history), 
						getString(R.string.aqi_history_download_failed));
				rdcpDownloadProgressBar.setVisibility(View.GONE);
				break;
			case DOWNLOAD_COMPLETE:
				addAqiReading();
				rdcpDownloadProgressBar.setVisibility(View.GONE);
				break;
			case OUTDOOR_TASK_COMPLETE:
				updateOutdoorBarChart();
				break;
			default:
				break;
			}
		};
	};
	
	private void updateOutdoorBarChart() {
		if (currentPurifier != null) {
			addCurrentCityGoodAQIIntoList(PurifierCurrentCityData.getInstance()
					.getPurifierCurrentCityGoodAQ(currentPurifier.getNetworkNode().getCppId()));
			showOutdoorBarChart();
		}
	}

	/**
	 * rDcp values download
	 */
	@Override
	public void onDataDownload(int status, String downloadedData) {
		ALog.i(ALog.INDOOR_DETAILS, "onDataDownload status: " + status);
		String eui64 = currentPurifier.getNetworkNode().getCppId();
		if( status == Errors.SUCCESS ) {
			if (downloadedData != null && !downloadedData.isEmpty()) {
				Utils.getIndoorAqiValues(downloadedData, currentPurifier.getNetworkNode().getCppId()) ;
				
				IndoorTrendDto inDto = SessionDto.getInstance().getIndoorTrendDto(eui64);
				
				if (inDto != null) {
					hrlyAqiValues = inDto.getHourlyList() ;
					dailyAqiValues = inDto.getDailyList() ;
					addGoodAQIIntoList(inDto);
					addCurrentCityGoodAQIIntoList(PurifierCurrentCityData.getInstance()
							.getPurifierCurrentCityGoodAQ(currentPurifier.getNetworkNode().getCppId()));
				}
				handlerDownload.sendEmptyMessage(DOWNLOAD_COMPLETE);

			} else {
				handlerDownload.sendEmptyMessage(DOWNLOAD_NA);
			}
		} else {
			handlerDownload.sendEmptyMessage(DOWNLOAD_FAILED);
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
	
	private void showOutdoorBarChart() {
		if (goodAirInfos != null && !goodAirInfos.isEmpty()) {
			addBarChartView(outdoorBarChart, currentCityGoodAirInfos, dayIndex);
		}
	}
	
	private MainActivity getMainActivity() {
		return (MainActivity) getActivity();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		PurifierCurrentCityData.getInstance().removeListener();
		CPPController.getInstance(getMainActivity()).removeDownloadDataListener();
		handlerDownload.removeMessages(DOWNLOAD_COMPLETE);
		handlerDownload.removeMessages(DOWNLOAD_NA);
		handlerDownload.removeMessages(DOWNLOAD_FAILED);
		handlerDownload.removeMessages(OUTDOOR_TASK_COMPLETE);
	}

	//Current city percentage calculation task
	@Override
	public void onTaskComplete() {
		handlerDownload.sendEmptyMessage(OUTDOOR_TASK_COMPLETE);
	}

}
