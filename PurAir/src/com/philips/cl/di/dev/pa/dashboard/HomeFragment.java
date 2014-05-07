package com.philips.cl.di.dev.pa.dashboard;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;

public class HomeFragment extends BaseFragment implements UpdateUIonDataChange{

	private ViewPager indoorViewPager;
	private ViewPager outdoorViewPager;
	
	private OutdoorPagerAdapter outdoorPagerAdapter;
	private IndoorPagerAdapter indoorPagerAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		
		ALog.i(ALog.DASHBOARD, "Density " + getResources().getDisplayMetrics().densityDpi);
		
		switch (getResources().getDisplayMetrics().densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			ALog.i(ALog.DASHBOARD, "Density LOW");
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			ALog.i(ALog.DASHBOARD, "Density MEDIUM");
			break;
		case DisplayMetrics.DENSITY_HIGH:
			ALog.i(ALog.DASHBOARD, "Density HIGH");
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			ALog.i(ALog.DASHBOARD, "Density XXHIGH");
		default:
			ALog.i(ALog.DASHBOARD, "DEFAULT");
			break;
		}
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initDashboardViewPager();
		OutdoorManager.getInstance().setUIChangeListener(this);
		
	}
	
	private void initDashboardViewPager() {
		ALog.i(ALog.DASHBOARD, "HomeFragment$initDashboardViewPager");
		indoorViewPager = (ViewPager) getView().findViewById(R.id.hf_indoor_dashboard_viewpager);
		indoorPagerAdapter = new IndoorPagerAdapter(getChildFragmentManager());
		indoorViewPager.setAdapter(indoorPagerAdapter);
	
		outdoorViewPager = (ViewPager) getView().findViewById(R.id.hf_outdoor_dashboard_viewpager);
		outdoorPagerAdapter = new OutdoorPagerAdapter(getChildFragmentManager());
		outdoorViewPager.setAdapter(outdoorPagerAdapter);
	}
	
	@Override
	public void notifyUIOnDataChange() {
		ALog.i(ALog.DASHBOARD, "notifyUIOnDataChange " + getActivity());	
		if (getActivity() == null) {
			return;
		}
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				outdoorViewPager.setAdapter(null);
				outdoorViewPager.setAdapter(outdoorPagerAdapter);
			}
		});
	}

}
