package com.philips.cl.di.base.test;
import com.philips.cl.di.base.NavigationActivity;

public class NavigationActivityTest extends
		android.test.ActivityInstrumentationTestCase2<NavigationActivity> {
	private NavigationActivity mActivity;

	public NavigationActivityTest() {
		super("com.philips.cl.di.base", NavigationActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
	}

}