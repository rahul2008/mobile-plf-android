/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import android.app.Application;
import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.iapHandler.HybrisHandler;
import com.philips.cdp.di.iap.iapHandler.LocalHandler;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class IAPHandlerTest {
    @Mock
    AppInfra mAppInfra;
    @Mock
    Context mContext;

    @Mock
    ServiceDiscoveryInterface mServiceDiscoveryInterface;

    private IAPSettings mIAPSettings;
    private MockIAPDependencies mIAPDependencies;
    private MockIAPHandler mMockIAPHandler;
    private IAPListener mIapListener;

    @Before
    public void setUp() throws Exception {
        TestUtils.getStubbedHybrisDelegate();

        mIAPDependencies = new MockIAPDependencies(mAppInfra);
        mIAPSettings = new IAPSettings(mContext);
        mMockIAPHandler = new MockIAPHandler(mIAPDependencies, mIAPSettings);

        //IAP Listener
        mIapListener = new IAPListener() {
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
        };

    }

    @Test
    public void testIAPListener() throws Exception {
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIapListener(mIapListener);
        iapLaunchInput.getIapListener();
    }

    @Test
    public void testIAPListenerWithNull() throws Exception {
        try {
            IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
            iapLaunchInput.setIapListener(null);
            iapLaunchInput.getIapListener();
        } catch (RuntimeException exception) {
            String message = "Set IAPListener in your vertical app ";
            assertEquals(message, exception.getMessage());
        }
    }

    @Test
    public void testInitControllerFactory() throws Exception {
        mMockIAPHandler.initControllerFactory();
    }

    @Test(expected = NullPointerException.class)
    public void testInitHybrisDelegate() {
        mIAPSettings.setUseLocalData(true);
        mMockIAPHandler.initHybrisDelegate();
    }

    //Init IAP
    @Test(expected = NullPointerException.class)
    public void testInitIAPErrorResponse() throws Exception {
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        IAPListener iapListener = iapLaunchInput.getIapListener();
        mMockIAPHandler.initIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
        iapListener.onFailure(IAPConstant.IAP_ERROR_UNKNOWN);
    }

    @Test
    public void onSuccessOfInitAsFragment() throws Exception {
        TestUtils.getStubbedHybrisDelegate();
        FragmentActivity activity = Robolectric.setupActivity(FragmentActivity.class);
        IAPFlowInput input = new IAPFlowInput("HX9043/64");
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(9, input);
        mMockIAPHandler.
                onSuccessOfInitialization(new FragmentLauncher(activity, R.id.cart_container, new ActionBarListener() {
                    @Override
                    public void updateActionBar(int i, boolean b) {

                    }

                    @Override
                    public void updateActionBar(String s, boolean b) {

                    }
                }), iapLaunchInput, mIapListener);

        mIapListener.onSuccess();
    }

    @Test
    public void testOnSuccessOfInitAsActivity() throws Exception {
        ArrayList<String> ctns = new ArrayList<>();
        ctns.add("HX9043/64");
        IAPFlowInput input = new IAPFlowInput(ctns);
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIapListener(mIapListener);
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, input);
        IAPSettings iapSettings = new IAPSettings(new Application());
        MockIAPHandler mockIAPHandler = new MockIAPHandler(mIAPDependencies, iapSettings);
        mockIAPHandler.onSuccessOfInitialization(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_BEHIND, 1),
                iapLaunchInput, mIapListener);
        mIapListener.onSuccess();
    }

    @Test
    public void testOnFailureOfInit() throws Exception {
        mMockIAPHandler.onFailureOfInitialization(new Message(), mIapListener);
    }

    @Test
    public void testOnFailureOfInitWithNullListener() throws Exception {
        mMockIAPHandler.onFailureOfInitialization(new Message(), null);
    }

    //Launch IAP
    @Test(expected = RuntimeException.class)
    public void testLaunchIAPSuccessResponse() throws Exception {
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        IAPListener iapListener = iapLaunchInput.getIapListener();
        FragmentActivity activity = Robolectric.setupActivity(FragmentActivity.class);
        mMockIAPHandler.launchIAP(new FragmentLauncher(activity, R.id.cart_container, new ActionBarListener() {
            @Override
            public void updateActionBar(int i, boolean b) {

            }

            @Override
            public void updateActionBar(String s, boolean b) {

            }
        }), new IAPLaunchInput());
        iapListener.onSuccess();

    }

    //Launch As Activity
    @Test
    public void launchIAPAsActivityForCategorized() throws Exception {
        ArrayList<String> ctns = new ArrayList<>();
        ctns.add("HX9043/64");
        IAPFlowInput input = new IAPFlowInput(ctns);
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, input);
        IAPSettings iapSettings = new IAPSettings(new Application());
        MockIAPHandler mockIAPHandler = new MockIAPHandler(mIAPDependencies, iapSettings);
        mockIAPHandler.launchIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
    }

    @Test(expected = RuntimeException.class)
    public void launchIAPAsActivityForCategorizedWithNoProducts() throws Exception {
        ArrayList<String> ctns = new ArrayList<>();
        IAPFlowInput input = new IAPFlowInput(ctns);
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, input);
        mMockIAPHandler.launchIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
    }

    @Test(expected = RuntimeException.class)
    public void launchIAPAsActivityForCategorizedWithNull() throws Exception {
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, null);
        mMockIAPHandler.launchIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
    }

    @Test(expected = RuntimeException.class)
    public void launchIAPAsActivityForBuyDirect() throws Exception {
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW, new IAPFlowInput(""));
        IAPSettings iapSettings = new IAPSettings(new Application());
        MockIAPHandler mockIAPHandler = new MockIAPHandler(mIAPDependencies, iapSettings);
        mockIAPHandler.launchIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
    }

    @Test
    public void launchIAPAsActivityForProductDetail() throws Exception {
        IAPFlowInput input = new IAPFlowInput("HX9043/64");
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW, input);
        IAPSettings iapSettings = new IAPSettings(new Application());
        MockIAPHandler mockIAPHandler = new MockIAPHandler(mIAPDependencies, iapSettings);
        mockIAPHandler.launchIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
    }

    //Launch As Fragment
    @Test(expected = RuntimeException.class)
    public void testLaunchIAPAsFragmentWithNoInput() throws Exception {
        FragmentActivity activity = Robolectric.setupActivity(FragmentActivity.class);
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, null);
        mMockIAPHandler.
                launchIAP(new FragmentLauncher(activity, R.id.cart_container, new ActionBarListener() {
                    @Override
                    public void updateActionBar(int i, boolean b) {

                    }

                    @Override
                    public void updateActionBar(String s, boolean b) {

                    }
                }), iapLaunchInput);
    }

    @Test
    public void testLaunchIAPAsFragment() throws Exception {
        TestUtils.getStubbedHybrisDelegate();
        FragmentActivity activity = Robolectric.setupActivity(FragmentActivity.class);
        IAPFlowInput input = new IAPFlowInput("HX9043/64");
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(9, input);
        mMockIAPHandler.
                launchIAP(new FragmentLauncher(activity, R.id.cart_container, new ActionBarListener() {
                    @Override
                    public void updateActionBar(int i, boolean b) {

                    }

                    @Override
                    public void updateActionBar(String s, boolean b) {

                    }
                }), iapLaunchInput);
    }

    @Test
    public void testGetFragment() throws Exception {
        mMockIAPHandler.getFragment(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, new IAPFlowInput("HX8331/11"));
        mMockIAPHandler.getFragment(IAPLaunchInput.IAPFlows.IAP_SHOPPING_CART_VIEW, new IAPFlowInput("HX8331/11"));
        mMockIAPHandler.getFragment(IAPLaunchInput.IAPFlows.IAP_PURCHASE_HISTORY_VIEW, new IAPFlowInput("HX8331/11"));
        mMockIAPHandler.getFragment(IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW, new IAPFlowInput("HX8331/11"));
        mMockIAPHandler.getFragment(IAPLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW, new IAPFlowInput("HX8331/11"));
    }

    //add fragment test case

    @Test
    public void testGetHybrisExposedAPIImplementor() {
        mIAPSettings.setUseLocalData(false);
        assertTrue(mMockIAPHandler.getExposedAPIImplementor() instanceof HybrisHandler);
        assertEquals(false, mIAPSettings.isUseLocalData());
    }

    @Test
    public void testGetLocalExposedAPIImplementor() {
        mIAPSettings.setUseLocalData(true);
        mIAPSettings.setHostPort("acc.occ.philips.com");
        assertEquals(mIAPSettings.getHostPort(), "acc.occ.philips.com");
        mIAPSettings.setProposition("Tuscany2016");
        assertEquals(mIAPSettings.getProposition(), "Tuscany2016");
        assertTrue(mMockIAPHandler.getExposedAPIImplementor() instanceof LocalHandler);
        assertEquals(true, mIAPSettings.isUseLocalData());
    }

    @Test
    public void testIsStoreInitialized() {
        TestUtils.getStubbedHybrisDelegate();
        mMockIAPHandler.isStoreInitialized(mContext);
    }

    @Test
    public void testGetIAPErrorCodeForUnknownError() throws Exception {
        mMockIAPHandler.getIAPErrorCode(new Message());
    }

    @Test
    public void testGetIAPErrorCodeForIAPNetworkError() throws Exception {
        Message msg = new Message();
        msg.obj = new IAPNetworkError(null, 0, null);
        mMockIAPHandler.getIAPErrorCode(msg);
    }

}