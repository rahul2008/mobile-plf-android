package com.philips.cdp.di.iap.integration;

import android.app.Application;
import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.core.NetworkEssentialsFactory;
import com.philips.cdp.di.iap.session.IAPListener;
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

/**
 * Created by indrajitkumar on 26/09/16.
 */
@RunWith(RobolectricTestRunner.class)
public class IAPHandlerTest {
    @Mock
    AppInfra mAppInfra;
    @Mock
    Context mContext;
    private MockIAPDependencies mIAPDependencies;
    private MockIAPSetting mIAPSetting;
    private MockIAPHandler mockIAPHandler;
    private IAPHandler iapHandler;
    private IAPListener iapListener;

    @Before
    public void setUp() throws Exception {
        mIAPDependencies = new MockIAPDependencies(mAppInfra);
        mIAPSetting = new MockIAPSetting(mContext);
        mockIAPHandler = new MockIAPHandler(mIAPDependencies, mIAPSetting);
        iapHandler = new IAPHandler(mIAPDependencies, mIAPSetting);
        iapListener = new IAPListener() {
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
    public void initIAPRequestCodeForHybrisData() throws Exception {
        assertEquals(NetworkEssentialsFactory.LOAD_HYBRIS_DATA, iapHandler.getNetworkEssentialReqeustCode(false));
        assertEquals(NetworkEssentialsFactory.LOAD_LOCAL_DATA, iapHandler.getNetworkEssentialReqeustCode(true));
    }

    @Test
    public void useAppLocalHandlerNotNull() throws Exception {
        mIAPSetting = new MockIAPSetting(mContext);
        mIAPSetting.setUseLocalData(true);
        assertNotNull(iapHandler.getExposedAPIImplementor(mIAPSetting));
        assertEquals(true, mIAPSetting.isUseLocalData());
    }

    @Test
    public void useHybrisLocalHandlerNotNull() throws Exception {
        mIAPSetting.setUseLocalData(false);
        assertNotNull(iapHandler.getExposedAPIImplementor(mIAPSetting));
        assertEquals(false, mIAPSetting.isUseLocalData());
    }

    @Test(expected = RuntimeException.class)
    public void launchIAPAsActivityForBuyDirect() throws Exception {
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW, null);
        mockIAPHandler.launchIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
    }

    @Test
    public void launchIAPAsActivityForProductDetail() throws Exception {
        IAPFlowInput input = new IAPFlowInput("HX9043/64");
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW, input);
        mockIAPHandler.launchIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
    }

    @Test
    public void launchIAPAsActivityForCategorized() throws Exception {
        ArrayList<String> ctns = new ArrayList<>();
        IAPFlowInput input = new IAPFlowInput(ctns);
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, input);
        mockIAPHandler.launchIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
    }

    @Test
    public void launchIAPAsFragment() throws Exception {
        FragmentActivity activity = Robolectric.setupActivity(FragmentActivity.class);
        mockIAPHandler.launchIAP(new FragmentLauncher(activity, R.id.cart_container, new ActionBarListener() {
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
        mockIAPHandler.getFragmentFromScreenID(1, new IAPFlowInput("HX8331/11"));
        mockIAPHandler.getFragmentFromScreenID(2, new IAPFlowInput("HX8331/11"));
        mockIAPHandler.getFragmentFromScreenID(3, new IAPFlowInput("HX8331/11"));
        mockIAPHandler.getFragmentFromScreenID(4, new IAPFlowInput("HX8331/11"));
    }

    @Test(expected = NullPointerException.class)
    public void verifyOnSuccess() throws Exception {
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        IAPListener iapListener = iapLaunchInput.getIapListener();
        FragmentActivity activity = Robolectric.setupActivity(FragmentActivity.class);
        mockIAPHandler.launchIAP(new FragmentLauncher(activity, R.id.cart_container, new ActionBarListener() {
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
        mockIAPHandler.initIAP(new ActivityLauncher
                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 1), iapLaunchInput);
        iapListener.onFailure(IAPConstant.IAP_ERROR_UNKNOWN);

    }

    @Test(expected = NullPointerException.class)
    public void onSuccessOfInitForActivityLauncher() throws Exception {
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        mockIAPHandler.onSuccessOfInitilization(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_BEHIND, 1),
                iapLaunchInput, iapListener);
        iapListener.onSuccess();

    }

    @Test(expected = NullPointerException.class)
    public void onSuccessOfInitForFragmentLauncher() throws Exception {
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIapListener(iapListener);
        mockIAPHandler.onSuccessOfInitilization(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_BEHIND, 1),
                iapLaunchInput, iapListener);

    }

    @Test
    public void onFailureOfInitilization() throws Exception {
        mockIAPHandler.onFailureOfInitilization(new Message(), iapListener);
    }

    @Test
    public void initControllerFactory() throws Exception {
        mockIAPHandler.initControllerFactory(mIAPSetting);
    }

    @Test(expected = NullPointerException.class)
    public void setLocale() throws Exception {
        IAPSettings mIAPSetting = new IAPSettings(new Application());
        mockIAPHandler.setLangAndCountry(mIAPSetting);
    }

    @Test
    public void getIAPErrorCodeForUnknonError() throws Exception {
        mockIAPHandler.getIAPErrorCode(new Message());
    }

    @Test
    public void getIAPErrorCode() throws Exception {
        Message msg = new Message();
        msg.obj = new IAPNetworkError(null, 0, null);
        mockIAPHandler.getIAPErrorCode(msg);
    }
}