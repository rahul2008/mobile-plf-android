package com.philips.cdp.di.iap.integration;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

@RunWith(RobolectricTestRunner.class)
public class IAPInterfaceTest {

    @Mock
    Context mContext;

    @Mock
    private IAPDependencies mIAPDependencies;

    @Mock
    private IAPLaunchInput mIapLaunchInput;

    private IAPSettings mIAPSettings;
    private IAPInterface mIapInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mIAPSettings = new IAPSettings(mContext);
        mIapInterface = new IAPInterface();
        mIapLaunchInput.setIapListener(new IAPListener() {
            @Override
            public void onGetCartCount(int count) {

            }

            @Override
            public void onUpdateCartCount() {

            }

            @Override
            public void updateCartIconVisibility(boolean shouldShow) {

            }

            @Override
            public void onGetCompleteProductList(ArrayList<String> productList) {

            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int errorCode) {

            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void testInit() {
        mIapInterface.init(mIAPDependencies, mIAPSettings);
    }
}