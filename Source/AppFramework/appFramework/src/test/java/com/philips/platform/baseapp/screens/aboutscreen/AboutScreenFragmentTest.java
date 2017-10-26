/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.aboutscreen;

import android.widget.TextView;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.baseapp.screens.termsandconditions.TermsAndPrivacyStateData;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class AboutScreenFragmentTest {

    private ActivityController<TestActivity> activityController;
    private HamburgerActivity hamburgerActivity = null;
    private AboutScreenFragment aboutScreenFragment;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private static AboutScreenContract.Action aboutScreenActionListener;

    @Before
    public void setUp() {
        aboutScreenFragment = new AboutScreenFragmentMock();
        activityController = Robolectric.buildActivity(TestActivity.class);
        hamburgerActivity = activityController.create().start().get();
        hamburgerActivity.getSupportFragmentManager().beginTransaction().add(aboutScreenFragment, null).commit();
    }

    @Test
    public void testVersion() {
        TextView version = (TextView) aboutScreenFragment.getView().findViewById(R.id.uid_about_screen_version);
        assertEquals(version.getText(), aboutScreenFragment.getResources().getString(R.string.RA_About_App_Version) + BuildConfig.VERSION_NAME);
    }

    @Test
    public void testDescription() {
        TextView content = (TextView) aboutScreenFragment.getView().findViewById(R.id.uid_about_screen_disclosure);
        assertEquals(content.getText(), aboutScreenFragment.getResources().getString(R.string.RA_DLS_about_description));
    }

    @Test
    public void testTermsClick() {
        aboutScreenFragment.getView().findViewById(R.id.uid_about_screen_terms).performClick();
        verify(aboutScreenActionListener).loadTermsAndPrivacy(TermsAndPrivacyStateData.TermsAndPrivacyEnum.TERMS_CLICKED);
    }

    @Test
    public void testPrivacyClick() {
        aboutScreenFragment.getView().findViewById(R.id.uid_about_screen_privacy).performClick();
        verify(aboutScreenActionListener).loadTermsAndPrivacy(TermsAndPrivacyStateData.TermsAndPrivacyEnum.PRIVACY_CLICKED);
    }

    @Test
    public void testTitle() {
        assertEquals(hamburgerActivity.getString(R.string.RA_AboutScreen_Title), aboutScreenFragment.getActionbarTitle());
    }

    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
        hamburgerActivity = null;
        aboutScreenActionListener = null;
        aboutScreenFragment = null;
        activityController = null;
    }

    public static class AboutScreenFragmentMock extends AboutScreenFragment {

        @Override
        protected AboutScreenContract.Action getPresenter() {
            return aboutScreenActionListener;
        }
    }
}
