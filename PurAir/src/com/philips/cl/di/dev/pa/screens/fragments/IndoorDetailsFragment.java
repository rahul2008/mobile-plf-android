package com.philips.cl.di.dev.pa.screens.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.screens.adapters.DatabaseAdapter;
import com.philips.cl.di.dev.pa.screens.customviews.BarOneDayView;
import com.philips.cl.di.dev.pa.screens.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.utils.Utils;

public class IndoorDetailsFragment extends Fragment {

	public static final String TAG = IndoorDetailsFragment.class
			.getSimpleName();

	View vMain;
	private int iAQI;
	private TextView tvIndoorAQI;
	ImageView ivLeftMenu, ivCenterLabel, ivRightDeviceIcon;
	BarOneDayView ivGraphView;
	private DatabaseAdapter adapter ;
	Bundle bundle;
	ArrayList<Integer> alAQIValues;
	private CustomTextView tvCityName, tvDay, tvTime, tvHighValue,
			tvLowValue, tvRankValue, tvPercent, tvAVGPMValue, tvOneTimeValue;

	private String sCityName, sAQIValue, sLastUpdatedTime, sLastUpdatedDay;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		vMain = inflater.inflate(R.layout.activity_indoor_details, container,
				false);
		bundle = this.getArguments();
		adapter = new DatabaseAdapter(getActivity());
		adapter.open();
		iAQI = bundle.getInt(AppConstants.INDOOR_AQI, 0);
		sCityName = bundle.getString(AppConstants.CITYNAME, "--");
		sLastUpdatedDay = bundle.getString(AppConstants.LAST_UPDATED_DAY, "--");
		sLastUpdatedTime = bundle.getString(AppConstants.LAST_UPDATED_TIME,
				"--");
		
		// format has to be like --08/19/2013 
		alAQIValues  = adapter.getAQIValuesForDay(sLastUpdatedDay/*"08/19/2013"*/, 0); // 0 for Home
		adapter.close();
		initialiseNavigationBar();
		initilizeViews();
		populateViews();
		return vMain;
	}

	private void initilizeViews() {
		ivGraphView = (BarOneDayView) vMain.findViewById(R.id.ivGraph);
		
		tvIndoorAQI = (TextView) vMain.findViewById(R.id.tvAqi);

		tvDay = (CustomTextView) vMain.findViewById(R.id.tvDay);
		tvTime = (CustomTextView) vMain.findViewById(R.id.tvTime);
		tvHighValue= (CustomTextView) vMain.findViewById(R.id.tvHighValue);
		tvLowValue= (CustomTextView) vMain.findViewById(R.id.tvLowValue);
		tvRankValue= (CustomTextView) vMain.findViewById(R.id.tvRankValue);
		tvPercent = (CustomTextView) vMain.findViewById(R.id.tvPercent);
		tvAVGPMValue = (CustomTextView) vMain.findViewById(R.id.tvAVGPMValue);

	}

	private void populateViews() {

		tvDay.setText(sLastUpdatedDay);
		tvTime.setText(sLastUpdatedTime);
		tvIndoorAQI.setText(String.valueOf(iAQI));
		tvIndoorAQI.setTextColor(Utils.getAQIColor(iAQI));
		
		if(alAQIValues!=null && alAQIValues.size()>0){
		tvHighValue.setText(String.valueOf(Utils.getMaximumAQI(alAQIValues)));
		tvLowValue.setText(String.valueOf(Utils.getMinimumAQI(alAQIValues)));
		tvPercent.setText(String.valueOf(Utils.getGoodAirPercentage(alAQIValues))+"%");
		tvAVGPMValue.setText(String.valueOf(Utils.getAverageAQI(alAQIValues)));
		ivGraphView.drawGraph(Utils.getArrayForAQIGraph(alAQIValues));
		}
		else
		{
			tvHighValue.setText("--");
			tvLowValue.setText("--");
			tvPercent.setText("--");
			tvAVGPMValue.setText("--");
			//ivGraphView.drawGraph();
		}

	}

	private void initialiseNavigationBar() {
		ivLeftMenu = (ImageView) getActivity().findViewById(R.id.ivLeftMenu);
		ivRightDeviceIcon = (ImageView) getActivity().findViewById(
				R.id.ivRightDeviceIcon);
		ivCenterLabel = (ImageView) getActivity().findViewById(
				R.id.ivCenterLabel);

		ivLeftMenu.setBackgroundResource(R.drawable.left_menu_black);
		ivRightDeviceIcon
				.setBackgroundResource(R.drawable.right_device_icon_black);
		ivCenterLabel.setBackgroundResource(R.drawable.home_label_black);
	}

}
