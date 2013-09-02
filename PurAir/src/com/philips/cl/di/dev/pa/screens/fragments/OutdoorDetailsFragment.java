package com.philips.cl.di.dev.pa.screens.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.screens.adapters.DatabaseAdapter;
import com.philips.cl.di.dev.pa.screens.customviews.BarOneDayView;
import com.philips.cl.di.dev.pa.screens.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.utils.Utils;

public class OutdoorDetailsFragment extends Fragment {

	public static final String TAG = OutdoorDetailsFragment.class
			.getSimpleName();
	View vMain;
	ImageView ivLeftMenu, ivCenterLabel, ivRightDeviceIcon;
	BarOneDayView ivGraphView;
	ArrayList<Integer> alAQIValues;
	private Bundle bundle;
	private int iAQI;

	private CustomTextView tvCityName, tvDay, tvTime, tvAQI, tvHighValue,
			tvLowValue, tvRankValue, tvPercent, tvAVGPMValue, tvOneTimeValue;

	private DatabaseAdapter adapter;

	private String sCityName, sAQIValue, sLastUpdatedTime, sLastUpdatedDay;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		vMain = inflater.inflate(R.layout.activity_outdoor_details, container,
				false);

		bundle = this.getArguments();
		iAQI = bundle.getInt(AppConstants.OUTDOOR_AQI, 0);
		sCityName = bundle.getString(AppConstants.CITYNAME, "--");
		sLastUpdatedDay = bundle.getString(AppConstants.LAST_UPDATED_DAY, "--");
		sLastUpdatedTime = bundle.getString(AppConstants.LAST_UPDATED_TIME,
				"--");
		initialiseNavigationBar();
		initilizeViews();
		adapter = new DatabaseAdapter(getActivity());
		adapter.open();
		alAQIValues = adapter.getAQIValuesForDay(sLastUpdatedDay, 2);// 2 for
		adapter.close();																// shanghai
		
		populateViews();
		return vMain;
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	private void initilizeViews() {
		ivGraphView = (BarOneDayView) vMain.findViewById(R.id.ivGraph);

		tvCityName = (CustomTextView) vMain.findViewById(R.id.tvCityName);
		tvDay = (CustomTextView) vMain.findViewById(R.id.tvDay);
		tvTime = (CustomTextView) vMain.findViewById(R.id.tvTime);
		tvAQI = (CustomTextView) vMain.findViewById(R.id.tvAqi);
		tvHighValue = (CustomTextView) vMain.findViewById(R.id.tvHighValue);

		tvLowValue = (CustomTextView) vMain.findViewById(R.id.tvLowValue);
		tvRankValue = (CustomTextView) vMain.findViewById(R.id.tvRankValue);
		tvPercent = (CustomTextView) vMain.findViewById(R.id.tvPercent);
		tvAVGPMValue = (CustomTextView) vMain.findViewById(R.id.tvAVGPMValue);

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
		ivCenterLabel.setBackgroundResource(R.drawable.outdoor_label_black);
	}

	private void populateViews() {
		tvCityName.setText(sCityName);
		tvDay.setText(sLastUpdatedDay);
		tvTime.setText(sLastUpdatedTime);
		tvAQI.setText(String.valueOf(iAQI));
		tvAQI.setTextColor(Utils.getAQIColor(iAQI));
		if (alAQIValues != null && alAQIValues.size() > 0) {
			tvHighValue.setText(String.valueOf(Utils.getMaximumAQI(alAQIValues)));
			tvLowValue.setText(String.valueOf(Utils.getMinimumAQI(alAQIValues)));
			tvPercent.setText(String.valueOf(Utils.getGoodAirPercentage(alAQIValues))+"%");
			tvAVGPMValue.setText(String.valueOf(Utils.getAverageAQI(alAQIValues)));
			ivGraphView.drawGraph(Utils.getArrayForAQIGraph(alAQIValues));
		} else {
			tvHighValue.setText("--");
			tvLowValue.setText("--");
			tvPercent.setText("--");
			tvAVGPMValue.setText("--");
		}

	}
}
