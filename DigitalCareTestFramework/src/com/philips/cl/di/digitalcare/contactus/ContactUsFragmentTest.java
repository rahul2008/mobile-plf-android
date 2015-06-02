package com.philips.cl.di.digitalcare.contactus;

import org.junit.Test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.sampledigitalcareapp.LaunchDigitalCare;

/**
 * 
 * @author naveen@philips.com
 * 
 */
public class ContactUsFragmentTest extends
		ActivityInstrumentationTestCase2<LaunchDigitalCare> {

	private ContactUsFragment fixture = null;
	private Activity mActivity = null;
	private String mActionBarTitle = null;

	public ContactUsFragmentTest() {
		super(LaunchDigitalCare.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		fixture = new ContactUsFragment();
		mActivity = getActivity();
		mActionBarTitle = mActivity.getString(R.string.contact_us);
	}

	@Test
	public void testContactUsFragmentCreation() throws Exception {
		assertNotNull(fixture);
	}

	/*@Test
	public void testGetActionbarTitle() throws Exception {
		fixture.onAttach(mActivity);
		String result = fixture.getActionbarTitle();
		assertNotNull(result);
	}*/

}
