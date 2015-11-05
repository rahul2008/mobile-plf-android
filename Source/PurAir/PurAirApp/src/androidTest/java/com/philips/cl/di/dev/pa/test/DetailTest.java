package com.philips.cl.di.dev.pa.test;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.dashboard.HomeFragment;
import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;

public class DetailTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private MainActivity activity;
	private FragmentManager fragManager;

	public DetailTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(false);
		activity = getActivity();
		fragManager = activity.getSupportFragmentManager();
	}
	
	public void testToggleIndoorDetailFragment() {
		final Fragment fragment = fragManager.findFragmentById(R.id.llContainer);
		
		if (fragment instanceof HomeFragment) {
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					((HomeFragment) fragment).toggleIndoorDetailFragment();
					ViewGroup indoorDetailContainer = (LinearLayout) 
							fragment.getView().findViewById(R.id.home_fragment_indoor_detail_container);
					assertEquals(View.VISIBLE, indoorDetailContainer.getVisibility());
				}
			});
		}
	}
	
	public void testToggleOutdoorDetailFragment() {
		final Fragment fragment = fragManager.findFragmentById(R.id.llContainer);
		
		if (fragment instanceof HomeFragment) {
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					OutdoorAQI outdoorAQI = new OutdoorAQI(123, 150, "100", "3", "11", "101010100", "201411121905");
					((HomeFragment) fragment).toggleOutdoorDetailFragment("Bangalore", outdoorAQI, 0);
					ViewGroup outdoorDetailContainer = (RelativeLayout)
							fragment.getView().findViewById(R.id.home_fragment_outdoor_detail_container);
					assertEquals(View.VISIBLE, outdoorDetailContainer.getVisibility());
				}
			});
		}
	}
}
