package com.philips.cdp.digitalcare.fragments.rateandreview.test;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.digitalcare.activity.DigitalCareActivity;
import com.philips.cdp.digitalcare.fragments.rateandreview.RateThisAppFragment;


/**
 * This is the testing Class to test the ProductRating features testing.
 *
 * @author naveen@philips.com
 * @since 26/august/2015
 */
public class RateThisAppFragmentTest extends
        ActivityInstrumentationTestCase2<DigitalCareActivity> {

    private RateThisAppFragment mRateThisAppScreen = null;
    private Activity mLauncherActivity = null;

    public RateThisAppFragmentTest() {
        super(DigitalCareActivity.class);
    }

   /* @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mLauncherActivity = getActivity();

    }*/

    private Fragment startFragment(Fragment fragment) {
        FragmentTransaction mFragmentTransaction = mLauncherActivity.getFragmentManager().beginTransaction();
        //   mFragmentTransaction.add(android.R.id.conta)
        // mFragmentTransaction.add(
        return null;
    }

    public void testTheLauncherContext() {
        assertNull(mLauncherActivity);
    }

   /* @SmallTest
    public void testIsRateThisAppScreenIsMocked() {
        boolean validate = false;
        String received = mRateThisAppScreen.getClass().getSimpleName();
        if (received.equalsIgnoreCase("RateThisAppFragment_Proxy"))
            validate = true;
        assertTrue(validate);
    }*/


    /**
     * Adding testCases Scenario's,
     */

   /* @SmallTest
    public void testProductImageAvailablity() {

    }

    @SmallTest
    public void testProductNameAvailability() {

    }

    @SmallTest
    public void textProductCtnAvailability() {

    }

    @SmallTest
    public void testProductRatigForOneRating() {

    }

    @SmallTest
    public void testProductRatigForTwoRating() {

    }

    @SmallTest
    public void testProductRatigForThreeRating() {

    }

    @SmallTest
    public void testProductRatigForFourRating() {

    }

    @SmallTest
    public void testProductRatigForFiveRating() {

    }

    @SmallTest
    public void testProductRatingReviewHeaderText() {

    }

    @SmallTest
    public void testProductRatingSummaryTest() {

    }

    @SmallTest
    public void testProductRatingSummaryTestWithMinimum50Characters() {

    }

    @SmallTest
    public void testProductRatingSummaryTestWithUrlInText() {

    }

    @SmallTest
    public void testProductRatingNickName() {

    }

    @SmallTest
    public void testEmailWithText() {

    }

    @SmallTest
    public void textEmailWithWildCharactersOne() {
        //@ Starting at first
    }

    @SmallTest
    public void textEmailWithWildCharactersTwo() {
        //@ adding at end of the Email
    }

    @SmallTest
    public void textEmailWithWildCharactersTwo() {
        //Two @@ characters shouldn't be in email
    }

    @SmallTest
    public void textCheckBoxBooleanValue() {

    }*/

}
