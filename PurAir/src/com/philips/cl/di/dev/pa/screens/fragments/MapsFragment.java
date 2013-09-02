package com.philips.cl.di.dev.pa.screens.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.screens.customviews.CustomTextView;
import com.philips.cl.di.dev.pa.utils.Utils;

public class MapsFragment extends Fragment {

	private View rootView;
	private RelativeLayout rlMapBg, rlOverlayAQI;
	private CustomTextView tvMapAQI;
	private int iAqiValue;
	private String sCityName;
	private ImageView ivLeftMenu, ivCenterLabel, ivRightDeviceIcon;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_map, container, false);
		Bundle b = getArguments();
		sCityName = b.getString(AppConstants.CITYNAME);
		iAqiValue = b.getInt(AppConstants.OUTDOOR_AQI);
		initialiseUI();
		initialiseNavigationBar();
		populateUI(sCityName, iAqiValue);
		return rootView;
	}

	private void initialiseUI() {
		rlMapBg = (RelativeLayout) rootView.findViewById(R.id.rlMapBg);
		rlOverlayAQI = (RelativeLayout) rootView
				.findViewById(R.id.rlAQIOverlay);
		tvMapAQI = (CustomTextView) rootView.findViewById(R.id.tvMapAQI);
	}

	private void populateUI(String sCityName, int iAQI) {
		String sDrawableBG = Utils.getMapBg(sCityName);
		String sDrawableOverlay = Utils.getMapOverlay(iAQI);
		rlMapBg.setBackgroundResource(Utils.getResourceID(sDrawableBG,
				getActivity()));
		rlOverlayAQI.setBackgroundResource(Utils.getResourceID(
				sDrawableOverlay, getActivity()));
		tvMapAQI.setText(String.valueOf(iAqiValue));
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
		ivCenterLabel.setBackgroundResource(R.drawable.map_label_black);
	}

}
