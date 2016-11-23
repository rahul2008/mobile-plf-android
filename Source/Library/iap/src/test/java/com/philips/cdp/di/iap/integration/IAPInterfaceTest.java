package com.philips.cdp.di.iap.integration;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
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
        mIapInterface = new MockIAPInterface();
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

    @Test
    public void testInit() {
        mIapInterface.init(mIAPDependencies, mIAPSettings);
    }

    @Test
    public void testLaunch() {
        TestUtils.getStubbedHybrisDelegate();
        FragmentActivity activity = Robolectric.setupActivity(FragmentActivity.class);
        IAPFlowInput input = new IAPFlowInput("HX9043/64");
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW, input);
        mIAPSettings.setUseLocalData(true);
        mIapInterface.init(mIAPDependencies, mIAPSettings);
        mIapInterface.launch(new FragmentLauncher(activity, R.id.cart_container, new ActionBarListener() {
            @Override
            public void updateActionBar(int i, boolean b) {

            }

            @Override
            public void updateActionBar(String s, boolean b) {

            }
        }), iapLaunchInput);
    }

    @Test
    public void testGetProductCount(){
        mIapInterface.getProductCartCount(new IAPListener() {
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

    @Test
    public void getCompleteProductList(){
        mIapInterface.getCompleteProductList(null);
    }

}