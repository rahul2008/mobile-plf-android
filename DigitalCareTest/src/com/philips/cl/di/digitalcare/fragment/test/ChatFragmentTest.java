package com.philips.cl.di.digitalcare.fragment.test;

import android.app.Activity;
import android.test.AndroidTestCase;

public class ChatFragmentTest extends AndroidTestCase {

	public Activity mActivity = null;
	
	public ChatFragmentTest(Activity activity) {
		super();
		this.mActivity = activity;
	}

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	
	public void testChatTestCase()
	{
		assertNotNull("Chat Fragment Activity is Null " , mActivity);
	}

}
