package com.philips.cdp.di.iap.activity;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MainActivityTest {

    private MainActivity mainActivity;

    @Before
    public void setup() {
        mainActivity = new MainActivity();
        //   mainActivity = Robolectric.setupActivity(MainActivity.class);
//        mainActivity = Robolectric.buildActivity(MainActivity.class).create().get();
    }

    @Test
    public void testCheckActivityNotNull() {
        Assert.assertNotNull(mainActivity);
    }
//    @Test
//    public void testShouldSetHeaderTitle() {
//        String str = new MainActivity().getResources().getString(R.string.iap_shopping_cart);
//        org.junit.Assert.assertThat(str, CoreMatchers.equalTo("Shopping Cart"));
//    }
}