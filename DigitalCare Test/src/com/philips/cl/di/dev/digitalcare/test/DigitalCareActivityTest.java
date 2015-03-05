package com.philips.cl.di.dev.digitalcare.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.digitalcare.DigitalCareActivity;
import com.philips.cl.di.dev.digitalcare.R;
import com.philips.cl.di.dev.digitalcare.customview.DigitalCareFontButton;
import com.philips.cl.di.dev.digitalcare.fragment.ContactUsFragment;
import com.philips.cl.di.dev.digitalcare.fragment.TwitterScreenFragment;
import com.philips.cl.di.dev.digitalcare.util.FragmentObserver;
import com.philips.cl.di.dev.digitalcare.util.Utils;

public class DigitalCareActivityTest extends
		ActivityInstrumentationTestCase2<DigitalCareActivity> {

	public DigitalCareActivity mActivity = null;
	public RelativeLayout mContainer = null;
	public View decorView = null;

	public ImageView mHomeIcon = null;
	public ImageView mBackbutton = null;

	public DigitalCareFontButton mViewDetails = null, mContactUs = null;

	public DigitalCareActivityTest() {
		super(DigitalCareActivity.class);

	}

	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mContainer = (RelativeLayout) mActivity
				.findViewById(R.id.mainContainer);
		decorView = mActivity.getWindow().getDecorView();

		mHomeIcon = (ImageView) mActivity.findViewById(R.id.home_icon);
		mBackbutton = (ImageView) mActivity.findViewById(R.id.back_to_home_img);
	}

	@MediumTest
	public void testfragmentView() {
		assertNotNull("Fragment Container Not available", mContainer);
	}

	@MediumTest
	public void testActivityValidation() {
		assertNotNull("DigitalCare Activity Not Found", mActivity);
	}

	@MediumTest
	public void testContainerParams() {
		final ViewGroup.LayoutParams layoutParams = mContainer
				.getLayoutParams();
		assertNotNull("Params not found for Fragment Container", layoutParams);
		assertEquals(layoutParams.width, WindowManager.LayoutParams.FILL_PARENT);
		assertEquals("Fragment Height is wrong", layoutParams.height,
				WindowManager.LayoutParams.FILL_PARENT);
	}

	@MediumTest
	public void testHomeIconAvailability() {
		assertNotNull("Home Icon not loaded ", mHomeIcon);
	}

	@MediumTest
	public void testBackIconAvailability() {
		assertNotNull("Back Icon not available ", mBackbutton);
	}

	@MediumTest
	public void testDigitalCareConnectionLogic() {
		boolean isConnected = Utils.isConnected(mActivity);

		assertEquals("\n \n No Connection \n \n", true, isConnected);
	}

	@SmallTest
	public void testDeviceTypeMobileLogic() {
		boolean isMobile = Utils.isTablet(mActivity.getApplicationContext());

		// assertEquals("This may be Tablet so failed " , false , isMobile);
		assertEquals("\n \nThis may be Tablet so failed \n \n", false, isMobile);
	}

	@SmallTest
	public void testDeviceTypeTableteLogic() {
		boolean isMobile = Utils.isTablet(mActivity.getApplicationContext());

		// assertEquals("This may be Tablet so failed " , false , isMobile);
		assertEquals(" \n \n This may be Mobile so failed \n \n", true,
				isMobile);
	}

	@SmallTest
	public void testSimAvailabilityLogic() {
		boolean isAvailable = Utils.isSimAvailable(mActivity);
		assertEquals(" \n \n Please try by inserting SIM \n \n", true,
				isAvailable);

	}

	/**
	 * Twitter Testing
	 */
	@MediumTest
	public void testTwitterConsumerKey() {
		String expected = "vM5RNi5CCodJnL7e5BOA";
		String received = mActivity.getString(R.string.twitter_consumer_key);
		assertEquals("ConsumerKEy is invalid", expected, received);
	}

	@MediumTest
	public void testTwitterConsumerSecreatKey() {
		String expected = "FQNS0eBbM8J5DDjDglLCwOvAUUesYhN5uFxdxjKLgk";
		String received = mActivity.getString(R.string.twitter_consumer_secret);
		assertEquals("ConsumerSecreat is invalid", expected, received);
	}

	@MediumTest
	public void testTwitterCallbackKey() {
		String expected = "http://javatechig.android.app";
		String received = mActivity.getString(R.string.twitter_callback);
		assertEquals("ConsumerCallback is invalid", expected, received);
	}

	@MediumTest
	public void testTwitterOauthVerifier() {
		String expected = "oauth_verifier";
		String received = mActivity.getString(R.string.twitter_oauth_verifier);
		assertEquals("oAuth is invalid", expected, received);
	}

	@MediumTest
	public void testTwitterFunctionality() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Class cls = Class.forName("com.philips.cl.di.dev.digitalcare.DigitalCareActivity");
		Object obj = cls.newInstance();
		
		
		Class[] paramString = new Class[1];	
		paramString[0] = Fragment.class;
		
		
		Method method = cls.getDeclaredMethod("showFragment", paramString);
		method.setAccessible(true);
		
		ContactUsFragment mObject = new ContactUsFragment();
		 
		
		 Object object = method.invoke(obj, mObject);
		 
		 assertNotNull("Contact Us Screent Not Found " , object);
		
	}

	@MediumTest
	public void testCDLSLink() throws NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		String received = null, expected = "http://www.philips.com/prx/cdls/B2C/en_GB/CARE/PERSONAL_CARE_GR.querytype.(fallback)";
		ContactUsFragment mFragment = new ContactUsFragment();
		Field mVariable = ContactUsFragment.class.getDeclaredField("mURL");
		mVariable.setAccessible(true);
		received = (String) mVariable.get(mFragment);

		assertEquals(expected, received);
	}
	
	
	@SmallTest
	public void testFragmentTitle()
	{
		FragmentObserver observer = new FragmentObserver();
		assertNotNull(observer.getActionbarTitle());
	}

}
