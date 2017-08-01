package com.philips.platform.baseapp.screens.termsandconditions;

import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.robolectric.Shadows.shadowOf;

/**
 * Created by philips on 27/07/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE,constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 25)
public class TermsAndConditionsStateTest extends TestCase{
    private TermsAndConditionsState termsAndConditionsState;
    private FragmentLauncher fragmentLauncher;
    private HamburgerActivity launchActivity;

    @Before
    public void setUp() throws Exception{
        super.setUp();
        termsAndConditionsState = new TermsAndConditionsState();
        launchActivity = Robolectric.setupActivity(TestActivity.class);
        termsAndConditionsState.init(launchActivity);
        termsAndConditionsState.updateDataModel();
        fragmentLauncher = new FragmentLauncher(launchActivity, R.id.frame_container, launchActivity);
    }

    @Test
    public void navigateTest(){
        TermsAndPrivacyStateData termsAndPrivacyStateData=new TermsAndPrivacyStateData();
        termsAndPrivacyStateData.setTermsAndPrivacyEnum(TermsAndPrivacyStateData.TermsAndPrivacyEnum.TERMS_CLICKED);
        termsAndConditionsState.setUiStateData(termsAndPrivacyStateData);
        termsAndConditionsState.navigate(fragmentLauncher);
        assertNotNull(shadowOf(launchActivity).getNextStartedActivity());
    }
}