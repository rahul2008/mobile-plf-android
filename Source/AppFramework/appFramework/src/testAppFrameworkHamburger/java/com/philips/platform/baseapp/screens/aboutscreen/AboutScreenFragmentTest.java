/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.aboutscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 25)
public class AboutScreenFragmentTest {
    private HamburgerActivity hamburgerActivity = null;
    private AboutScreenFragmentMock aboutScreenFragment;

    @Before
    public void setUp(){
        aboutScreenFragment = new AboutScreenFragmentMock();
        SupportFragmentTestUtil.startFragment(aboutScreenFragment);
    }

    @Test
    public void testAboutScreenFragment(){

        assertNotNull(aboutScreenFragment);
    }

    @Test
    public void testVersion(){
        TextView  version =(TextView)aboutScreenFragment.view.findViewById(R.id.about_version);
        assertEquals(version.getText(),aboutScreenFragment.getResources().getString(R.string.RA_About_App_Version) +BuildConfig.VERSION_NAME);
    }

    @Test
    public void testDescription(){
        TextView  content =(TextView)aboutScreenFragment.view.findViewById(R.id.about_content);
        assertEquals(content.getText(),aboutScreenFragment.getResources().getString(R.string.RA_About_Description));
    }

    public static class AboutScreenFragmentMock extends AboutScreenFragment {
       View view;
//       @Override
//       protected void startAppTagging() {
//
//       }

       @Override
       public void onResume() {
            super.onResume();
       }

       @Override
       public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
           view = super.onCreateView(inflater, container, savedInstanceState);
           return view;
       }

       @Override
       protected void updateActionBar() {

       }
   }
}
