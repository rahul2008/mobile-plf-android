package com.philips.platform.appframework.homescreen;

import android.app.Application;
import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@RunWith(RobolectricTestRunner.class)
public class HomeScreenPresenterTest {

    HomeScreenFragmentPresenter mHomeScreenFragmentPresenter;
    Application application;
    Context context;

    @Before
    public void setUP() {
        application = RuntimeEnvironment.application;
        context = application.getApplicationContext();
        MockitoAnnotations.initMocks(this);
        mHomeScreenFragmentPresenter = new HomeScreenFragmentPresenter();
    }

    @Test
    public void testOnCLick() throws Exception {
        mHomeScreenFragmentPresenter.onClick(2, context);
    }

    @Test
    public void testOnLoad() throws Exception {
        mHomeScreenFragmentPresenter.onLoad(context);
    }
}

