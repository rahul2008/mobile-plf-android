package com.philips.cdp.digitalcare.rateandreview.test;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.digitalcare.rateandreview.RateThisAppFragment;
import com.philips.cdp.sampledigitalcareapp.LaunchDigitalCare;


/**
 * This is the testing Class to test the ProductRating features testing.
 *
 * @author naveen@philips.com
 * @since 26/august/2015
 */
public class RateThisAppFragmentTest extends
        ActivityInstrumentationTestCase2<LaunchDigitalCare> {

    private RateThisAppFragment mRateThisAppScreen = null;
    private Activity mLauncherActivity = null;

    public RateThisAppFragmentTest() {
        super(LaunchDigitalCare.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mLauncherActivity = getActivity();

    }

    private Fragment startFragment(Fragment fragment) {
        FragmentTransaction mFragmentTransaction = mLauncherActivity.getFragmentManager().beginTransaction();
      //   mFragmentTransaction.add(android.R.id.conta)
        // mFragmentTransaction.add(
        return null;
    }

    public void testTheLauncherContext() {
        assertNotNull(mLauncherActivity);
    }

   /* @SmallTest
    public void testIsRateThisAppScreenIsMocked() {
        boolean validate = false;
        String received = mRateThisAppScreen.getClass().getSimpleName();
        if (received.equalsIgnoreCase("RateThisAppFragment_Proxy"))
            validate = true;
        assertTrue(validate);
    }*/

}
