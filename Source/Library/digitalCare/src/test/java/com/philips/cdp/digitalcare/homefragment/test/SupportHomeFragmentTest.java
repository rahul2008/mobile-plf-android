package com.philips.cdp.digitalcare.homefragment.test;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.philips.cdp.digitalcare.BuildConfig;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.homefragment.SupportHomeFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by philips on 6/29/17.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SupportHomeFragmentTest {

    private SupportHomeFragmentMock supportScreenFragment;

    @Before
    public void setUp(){
        supportScreenFragment = new SupportHomeFragmentMock();
    }

    @Test
    public void testSupportScreenFragment(){
        assertNotNull(supportScreenFragment);
    }

    @Test
    public void testSupportScreenFragmentNull(){
        supportScreenFragment = null;
        assertNull(supportScreenFragment);
    }

/*    @Test
    public void testCreateButtonLayout(){

        TypedArray titles = supportScreenFragment.getResources().obtainTypedArray
                (R.array.social_service_provider_menu_title);
        final TypedArray resources = supportScreenFragment.getResources().obtainTypedArray
                (R.array.social_service_provider_menu_resources);
        for (int i = 0; i < titles.length(); i++) {
            supportScreenFragment.createButtonLayout(titles.getResourceId(i, 0), resources.getResourceId(i, 0));
        }

        assertNotNull();
    }*/


    public static class SupportHomeFragmentMock extends SupportHomeFragment {
        View view;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

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
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public void onConfigurationChanged(Configuration config) {
            super.onConfigurationChanged(config);
        }
    }

}
