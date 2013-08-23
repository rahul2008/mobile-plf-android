package com.philips.cl.di.dev.pa.screens.fragments;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.screens.customviews.BarOneDayView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class OutdoorDetailsFragment extends Fragment {

	public static final String TAG = OutdoorDetailsFragment.class.getSimpleName();
	View vMain;
	ImageView ivLeftMenu, ivCenterLabel, ivRightDeviceIcon;
	BarOneDayView ivGraphView;
	int[] arrayAQIValues = { };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		vMain = inflater.inflate(R.layout.activity_outdoor_details, container,
				false);
		initialiseNavigationBar();
		initilizeViews();
		return vMain;
	}

	private void initilizeViews() {
		ivGraphView = (BarOneDayView) vMain.findViewById(R.id.ivGraph);
		ivGraphView.drawGraph(arrayAQIValues);

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
