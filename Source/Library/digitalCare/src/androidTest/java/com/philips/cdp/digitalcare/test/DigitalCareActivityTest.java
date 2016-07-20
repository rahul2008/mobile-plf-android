package com.philips.cdp.digitalcare.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.activity.DigitalCareActivity;
import com.philips.cdp.digitalcare.activity.DigitalCareBaseActivity;
import com.philips.cdp.digitalcare.customview.DigitalCareFontButton;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

/**
 * @author naveen@philips.com
 * @description This contains all the testcases of LaunchDigitalCare.
 * @Since Mar 5, 2015
 */
public class DigitalCareActivityTest extends
        ActivityInstrumentationTestCase2<DigitalCareActivity> {

    public Activity mActivity = null;
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
    protected void testonCreate() {
        DigitalCareBaseActivity baseActivity = new DigitalCareBaseActivity() {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

            }

            @Override
            protected void initActionBar() throws ClassCastException {
                super.initActionBar();
            }

            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                super.onConfigurationChanged(newConfig);
            }

            @Override
            protected void onResume() {
                super.onResume();
            }

            @Override
            protected void onPause() {
                super.onPause();
            }

            @Override
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                return super.onKeyDown(keyCode, event);
            }

            @Override
            protected void onDestroy() {
                super.onDestroy();
            }

            @Override
            protected void enableActionBarHome() {
                super.enableActionBarHome();
            }

            @Override
            protected void showFragment(Fragment fragment) {
                super.showFragment(fragment);
            }
        };
    }

    public void testConsumerCareActivity() {
        DigitalCareActivity digitalCareActivity = new DigitalCareActivity();
        digitalCareActivity.onCreate(getBundleWithValue());
        digitalCareActivity.onSaveInstanceState(getBundleWithNullValue());
        digitalCareActivity.animateThisScreen();
    }

    public void testConsumerCareActivityWithNullBundle() {
        DigitalCareActivity digitalCareActivity = new DigitalCareActivity();
        digitalCareActivity.onCreate(getBundleWithNullValue());
        digitalCareActivity.onSaveInstanceState(getBundleWithValue());
        digitalCareActivity.animateThisScreen();
    }


    private Bundle getBundleWithValue() {
        Bundle bundle = new Bundle();
        bundle.putString("digitalCare", "CoCo");
        bundle.putString("productSelection", "CoCo");
        return bundle;
    }


    private Bundle getBundleWithNullValue() {
        Bundle bundle = new Bundle();
        return bundle;
    }


	/*@MediumTest
    public void testfragmentView() {
		assertNotNull("Fragment Container Not available", mContainer);
	}
*/
/*	@MediumTest
    public void testActivityValidation() {
		assertNull("DigitalCare Activity Not Found", mActivity);
	}*/

	/*@MediumTest
    public void testContainerParams() {
		final ViewGroup.LayoutParams layoutParams = mContainer
				.getLayoutParams();
		assertNotNull("Params not found for Fragment Container", layoutParams);
		assertEquals(layoutParams.width, WindowManager.LayoutParams.MATCH_PARENT);
		assertEquals("Fragment Height is wrong", layoutParams.height,
				WindowManager.LayoutParams.MATCH_PARENT);
	}*/

	/*@MediumTest
    public void testHomeIconAvailability() {
		assertNotNull("Home Icon not loaded ", mHomeIcon);
	}*/

	/*@MediumTest
    public void testBackIconAvailability() {
		assertNotNull("Back Icon not available ", mBackbutton);
	}*/

	/*
	 * @MediumTest public void testDigitalCareConnectionLogic() { boolean
	 * isConnected = Utils.isConnected(mActivity);
	 * 
	 * assertEquals("\n \n No Connection \n \n", true, isConnected); }
	 */

	/*
	 * @SmallTest public void testDeviceTypeMobileLogic() { boolean actual =
	 * Utils.isTablet(mActivity.getApplicationContext()), expected = false;
	 * 
	 * DeviceType mFeature = new DeviceType(mActivity); expected =
	 * mFeature.isMobileDevice();
	 * 
	 * assertEquals("\n \nThis may be Tablet so failed \n \n", expected,
	 * actual); }
	 */

	/*
	 * @SmallTest public void testDeviceTypeTableteLogic() { boolean actual =
	 * Utils.isTablet(mActivity.getApplicationContext()), expected = false;
	 * 
	 * DeviceType mFeature = new DeviceType(mActivity); expected =
	 * mFeature.isTabletDevice();
	 * 
	 * // assertEquals("This may be Tablet so failed " , false , isMobile);
	 * assertEquals(" \n \n This may be Mobile so failed \n \n", expected,
	 * actual); }
	 */

	/*@SmallTest
	public void testSimAvailabilityLogic() {
		CallFeature mCall = new CallFeature();
		boolean isAvailable = Utils.isSimAvailable(mActivity), expected = mCall
				.isSimAvailable(mActivity);
		assertEquals(" \n \n Please try by inserting SIM \n \n", expected,
				isAvailable);

	}*/
//
//	/**
//	 * Twitter Testing
//	 */
//	@MediumTest
//	public void testTwitterConsumerKey() {
//		String expected = "qgktZw1ffdoreBjbiYfvnIPJe";
//		String received = mActivity.getString(R.string.twitter_consumer_key);
//		assertEquals("ConsumerKEy is invalid", expected, received);
//	}
//
//	@MediumTest
//	public void testTwitterConsumerSecreatKey() {
//		String expected = "UUItcyGgL9v2j2vBBh9p5rHIuemsOlHdkMiuIMJ7VphlG38JK3";
//		String received = mActivity.getString(R.string.twitter_consumer_secret);
//		assertEquals("ConsumerSecreat is invalid", expected, received);
//	}

	/*@MediumTest
	public void testCDLSPrefixLink() throws NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		String received = null, expected = "http://www.philips.com/prx/cdls/B2C/";
		ContactUsFragment mFragment = new ContactUsFragment();
		Field mVariable = ContactUsFragment.class.getDeclaredField("CDLS_BASE_URL_PREFIX");
		mVariable.setAccessible(true);
		received = (String) mVariable.get(mFragment);

		assertEquals(expected, received);
	}
	
	@MediumTest
	public void testCDLSPostfixLink() throws NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		String received = null, expected = ".querytype.(fallback)";
		ContactUsFragment mFragment = new ContactUsFragment();
		Field mVariable = ContactUsFragment.class.getDeclaredField("CDLS_BASE_URL_POSTFIX");
		mVariable.setAccessible(true);
		received = (String) mVariable.get(mFragment);

		assertEquals(expected, received);
	}*/

//	@SmallTest
//	public void testFaceBookID() {
//		String expected = "1537018913230025";
//		String actual = mActivity.getString(R.string.facebook_app_id);
//		assertEquals(expected, actual);
//	}

}
