/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.controller;

import android.content.Context;

import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.MockNetworkController;

import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class AddressControllerTest extends TestCase {

    private MockNetworkController mNetworkController;
    @Mock
    private HybrisDelegate mHybrisDelegate;
    @Mock
    private AddressController mAddressController;
    @Mock
    private Context mContext;

    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mNetworkController = new MockNetworkController(mContext);
        mHybrisDelegate = TestUtils.getStubbedHybrisDelegate();
        mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(null);
    }


}