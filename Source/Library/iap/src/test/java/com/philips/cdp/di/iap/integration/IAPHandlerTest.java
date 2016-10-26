/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.platform.appinfra.AppInfra;
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
import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class IAPHandlerTest {
    @Mock
    AppInfra mAppInfra;
    @Mock
    Context mContext;
    private MockIAPSetting mIAPSetting;
    private MockIAPHandler mMockIAPHandler;
    private IAPListener mIapListener;

    @Before
    public void setUp() throws Exception {
        MockIAPDependencies mIAPDependencies = new MockIAPDependencies(mAppInfra);
        mIAPSetting = new MockIAPSetting(mContext);
        mMockIAPHandler = new MockIAPHandler(mIAPDependencies, mIAPSetting);
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
    public void useAppLocalHandlerNotNull() throws Exception {
        mIAPSetting = new MockIAPSetting(mContext);
        mIAPSetting.setUseLocalData(true);
        assertNotNull(mMockIAPHandler.getExposedAPIImplementor());
        assertEquals(true, mIAPSetting.isUseLocalData());
    }

    @Test
    public void useHybrisLocalHandlerNotNull() throws Exception {
        mIAPSetting.setUseLocalData(false);
        assertNotNull(mMockIAPHandler.getExposedAPIImplementor());
        assertEquals(false, mIAPSetting.isUseLocalData());
    }

    @Test(expected = RuntimeException.class)
    public void launchIAPAsActivityForBuyDirect() throws Exception {
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW, null);
        mMockIAPHandler.launchIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
    }

    @Test
    public void launchIAPAsActivityForProductDetail() throws Exception {
        IAPFlowInput input = new IAPFlowInput("HX9043/64");
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW, input);
        mMockIAPHandler.launchIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
    }

    @Test
    public void launchIAPAsActivityForCategorized() throws Exception {
        ArrayList<String> ctns = new ArrayList<>();
        IAPFlowInput input = new IAPFlowInput(ctns);
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, input);
        mMockIAPHandler.launchIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
    }

    @Test
    public void launchIAPAsFragment() throws Exception {
        FragmentActivity activity = Robolectric.setupActivity(FragmentActivity.class);
        mMockIAPHandler.launchIAP(new FragmentLauncher(activity, R.id.cart_container, new ActionBarListener() {
            @Override
            public void updateActionBar(int i, boolean b) {

            }

            @Override
            public void updateActionBar(String s, boolean b) {

            }
        }), new IAPLaunchInput());
    }

    @Test
    public void getFragmentFromScreenID() throws Exception {
        mMockIAPHandler.getFragmentFromScreenID(1, new IAPFlowInput("HX8331/11"));
        mMockIAPHandler.getFragmentFromScreenID(2, new IAPFlowInput("HX8331/11"));
        mMockIAPHandler.getFragmentFromScreenID(3, new IAPFlowInput("HX8331/11"));
        mMockIAPHandler.getFragmentFromScreenID(4, new IAPFlowInput("HX8331/11"));
    }

    @Test(expected = NullPointerException.class)
    public void verifyOnSuccess() throws Exception {
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

    @Test(expected = NullPointerException.class)
    public void verifyOnError() throws Exception {
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        IAPListener iapListener = iapLaunchInput.getIapListener();
        mMockIAPHandler.initIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
        iapListener.onFailure(IAPConstant.IAP_ERROR_UNKNOWN);

    }

    @Test
    public void testWithNullIAPListener() throws Exception {
        try {
            IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
            iapLaunchInput.setIapListener(null);
            iapLaunchInput.getIapListener();
        } catch (RuntimeException exception) {
            String message = "Set IAPListener in your vertical app ";
            assertEquals(message, exception.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void onSuccessOfInitForActivityLauncher() throws Exception {
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        mMockIAPHandler.onSuccessOfInitialization(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_BEHIND, 1),
                iapLaunchInput, mIapListener);
        mIapListener.onSuccess();

    }

    @Test(expected = NullPointerException.class)
    public void onSuccessOfInitForFragmentLauncher() throws Exception {
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIapListener(mIapListener);
        mMockIAPHandler.onSuccessOfInitialization(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_BEHIND, 1),
                iapLaunchInput, mIapListener);

    }

    @Test
    public void onFailureOfInitialization() throws Exception {
        mMockIAPHandler.onFailureOfInitialization(new Message(), mIapListener);
    }

    @Test
    public void initControllerFactory() throws Exception {
        mMockIAPHandler.initControllerFactory();
    }

    @Test(expected = NullPointerException.class)
    public void setLocale() throws Exception {
        mMockIAPHandler.setLangAndCountry();
    }

    @Test
    public void getIAPErrorCodeForUnknonError() throws Exception {
        mMockIAPHandler.getIAPErrorCode(new Message());
    }

    @Test
    public void getIAPErrorCode() throws Exception {
        Message msg = new Message();
        msg.obj = new IAPNetworkError(null, 0, null);
        mMockIAPHandler.getIAPErrorCode(msg);
    }
}