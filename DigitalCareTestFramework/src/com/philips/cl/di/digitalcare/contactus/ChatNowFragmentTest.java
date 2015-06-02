package com.philips.cl.di.digitalcare.contactus;

import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

/**
 * 
 * @author naveen@philips.com
 * 
 */
public class ChatNowFragmentTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	private ChatNowFragment fixture = null;

	public ChatNowFragmentTest() {
		super(LaunchDigitalCare.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		fixture = new ChatNowFragment();
	}

	@Test
	public void testChatNowFragmentObject() throws Exception {
		assertNotNull(fixture);
	}

	/*@Test
	public void testGetActionbarTitle() throws Exception {
		String result = fixture.getActionbarTitle();
		assertNotNull(result);
	}

	@Test
	public void testOnCreateView() throws Exception {
		LayoutInflater inflater = LayoutInflater
				.from(new AccountAuthenticatorActivity());
		ViewGroup container = new AbsoluteLayout(new ContextWrapper(new MutableContextWrapper(new ContextThemeWrapper())));
		Bundle savedInstanceState = new Bundle();
		View result = fixture.onCreateView(inflater, container,
				savedInstanceState);
		assertNotNull(result);
	}*/
}
