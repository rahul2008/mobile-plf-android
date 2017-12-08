/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.activity.IAPActivity;
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

import java.util.ArrayList;

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
//        Intent intent = new Intent();
//        intent.putExtra(IAPConstant.CATEGORISED_PRODUCT_CTNS, "HX8332/11");
//        intent.putStringArrayListExtra(IAPConstant.IAP_IGNORE_RETAILER_LIST, new ArrayList<String>());
//
//        Robolectric.buildActivity(IAPActivity.class).withIntent(intent).start().get();


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
            public void onSuccess(boolean bool) {

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

    @Test(expected = NullPointerException.class)
    public void onSuccessOfInitAsFragment() throws Exception {
        TestUtils.getStubbedHybrisDelegate();
        FragmentActivity activity = Robolectric.setupActivity(FragmentActivity.class);
        final ArrayList<String> blackListedretailder = new ArrayList<>();
        IAPFlowInput input = new IAPFlowInput("HX9043/64");
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(9, input, blackListedretailder);
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

    @Test(expected = NullPointerException.class)
    public void testOnSuccessOfInitAsActivity() throws Exception {
        ArrayList<String> ctns = new ArrayList<>();
        ArrayList<String> blackListedRetailer = new ArrayList<>();
        ctns.add("HX9043/64");
        IAPFlowInput input = new IAPFlowInput(ctns);
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIapListener(mIapListener);
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, input, blackListedRetailer);
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
    @Test(expected = NullPointerException.class)
    public void launchIAPAsActivityForCategorized() throws Exception {
        ArrayList<String> ctns = new ArrayList<>();
        ctns.add("HX9043/64");
        ArrayList<String> blackListedRetailer = new ArrayList<>();
        IAPFlowInput input = new IAPFlowInput(ctns);
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, input, blackListedRetailer);
        IAPSettings iapSettings = new IAPSettings(new Application());
        MockIAPHandler mockIAPHandler = new MockIAPHandler(mIAPDependencies, iapSettings);
        mockIAPHandler.launchIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
    }

    @Test(expected = RuntimeException.class)
    public void launchIAPAsActivityForCategorizedWithNoProducts() throws Exception {
        ArrayList<String> ctns = new ArrayList<>();
        ArrayList<String> blackListedRetailer = new ArrayList<>();
        IAPFlowInput input = new IAPFlowInput(ctns);
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, input, blackListedRetailer);
        mMockIAPHandler.launchIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
    }

    @Test(expected = RuntimeException.class)
    public void launchIAPAsActivityForCategorizedWithNull() throws Exception {
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, null, null);
        mMockIAPHandler.launchIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
    }

    @Test(expected = RuntimeException.class)
    public void launchIAPAsActivityForBuyDirect() throws Exception {
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        ArrayList<String> blackListedRetailer = new ArrayList<>();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW, new IAPFlowInput(""), blackListedRetailer);
        IAPSettings iapSettings = new IAPSettings(new Application());
        MockIAPHandler mockIAPHandler = new MockIAPHandler(mIAPDependencies, iapSettings);
        mockIAPHandler.launchIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
    }

    @Test(expected = IllegalStateException.class)
    public void launchIAPAsActivityForProductDetail() throws Exception {
        ArrayList blackListedRetailer = new ArrayList<>();
        IAPFlowInput input = new IAPFlowInput("HX9043/64");
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW, input, blackListedRetailer);
        IAPSettings iapSettings = new IAPSettings(new Application());
        MockIAPHandler mockIAPHandler = new MockIAPHandler(mIAPDependencies, iapSettings);
        Intent intent = new Intent();
        intent.putExtra(IAPConstant.CATEGORISED_PRODUCT_CTNS, "HX8332/11");
        intent.putStringArrayListExtra(IAPConstant.IAP_IGNORE_RETAILER_LIST, new ArrayList<String>());

        Robolectric.buildActivity(IAPActivity.class).withIntent(intent).start().get();
        //Robolectric.setupActivity(TestActivity.class);

        mockIAPHandler.launchIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
    }

    //Launch As Fragment
    @Test(expected = RuntimeException.class)
    public void testLaunchIAPAsFragmentWithNoInput() throws Exception {
        FragmentActivity activity = Robolectric.setupActivity(FragmentActivity.class);
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, null, null);
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

    @Test(expected = NullPointerException.class)
    public void testLaunchIAPAsFragment() throws Exception {
        TestUtils.getStubbedHybrisDelegate();
        FragmentActivity activity = Robolectric.setupActivity(FragmentActivity.class);
        ArrayList<String> blackListedRetailer = new ArrayList<>();
        IAPFlowInput input = new IAPFlowInput("HX9043/64");
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(9, input, blackListedRetailer);
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
    public void testGetProductCatalogFragment() throws Exception {
        final IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        IAPFlowInput pIapFlowInput = new IAPFlowInput("HX8332/11");
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, pIapFlowInput);
        mMockIAPHandler.getFragment(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, iapLaunchInput);
    }

    @Test
    public void testGetShoppingCartFragment() throws Exception {
        final IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        IAPFlowInput pIapFlowInput = new IAPFlowInput("HX8332/11");
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_SHOPPING_CART_VIEW, pIapFlowInput);
        mMockIAPHandler.getFragment(IAPLaunchInput.IAPFlows.IAP_SHOPPING_CART_VIEW, iapLaunchInput);
    }

    @Test
    public void testGetPurchaseHistoryFragment() throws Exception {
        final IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        IAPFlowInput pIapFlowInput = new IAPFlowInput("HX8332/11");
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PURCHASE_HISTORY_VIEW, pIapFlowInput);
        mMockIAPHandler.getFragment(IAPLaunchInput.IAPFlows.IAP_PURCHASE_HISTORY_VIEW, iapLaunchInput);
    }

    @Test
    public void testGetProductDetailFragment() throws Exception {
        final IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        IAPFlowInput pIapFlowInput = new IAPFlowInput("HX8332/11");
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW, pIapFlowInput);
        mMockIAPHandler.getFragment(IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW, iapLaunchInput);
    }


    @Test
    public void testGetProductDetailFragmentWithIgnoreRetailerList() throws Exception {
        final IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        IAPFlowInput pIapFlowInput = new IAPFlowInput("HX8332/11");
        ArrayList<String> mIgnoreRetailerList = new ArrayList<>();
        mIgnoreRetailerList.add("dfsd");
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW, pIapFlowInput, mIgnoreRetailerList);
        mMockIAPHandler.getFragment(IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW, iapLaunchInput);
    }


    @Test
    public void testGetBuyDirectFragment() throws Exception {
        final IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        IAPFlowInput pIapFlowInput = new IAPFlowInput("HX8332/11");
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW, pIapFlowInput);
        mMockIAPHandler.getFragment(IAPLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW, iapLaunchInput);
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

    @Test(expected = NullPointerException.class)
    public void testInitPreRequisite() throws Exception {
        mMockIAPHandler.initPreRequisite();
    }

    @Test(expected = NullPointerException.class)
    public void testInitIAPRequisite() throws Exception {
        mMockIAPHandler.initIAPRequisite();
    }

}