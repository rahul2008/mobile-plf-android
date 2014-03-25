package com.philips.cl.di.dev.pa.test;

import java.util.List;
import com.philips.cl.di.dev.pa.controller.DeviceInfoController;
import com.philips.cl.di.dev.pa.dto.DeviceInfoDto;
import com.philips.cl.di.dev.pa.pureairui.MainActivity;
import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private MainActivity activity;
	
	DeviceInfoController deviceInfoController;
	List<DeviceInfoDto> deviceInfoList;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(false);

		activity = getActivity();
	}

	public void testActivityTitle() {
		assertEquals("PHILIPS Smart Air Purifier", activity.getTitle());
	}
	
}
