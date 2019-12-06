package com.philips.cdp.di.iap.integration;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(RobolectricTestRunner.class)
public class IAPInterfaceTest {

    private Context mContext;
    private IAPInterface mIapInterface;
    private IAPSettings mIAPSettings;

    @Mock
    private IAPDependencies mIAPDependencies;
    @Mock
    private IAPLaunchInput mIapLaunchInput;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mContext = getInstrumentation().getContext();
        mIAPSettings = new IAPSettings(mContext);
        mIapInterface = new MockIAPInterface();
        mIapLaunchInput.setIapListener(Mockito.mock(IAPListener.class));
    }

    @Test
    public void testInit() {
        mIapInterface.init(mIAPDependencies, mIAPSettings);
        Assert.assertNotNull(mIAPDependencies);
        Assert.assertNotNull(mIAPSettings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLaunch() {
        TestUtils.getStubbedHybrisDelegate();
        FragmentActivity activity = Robolectric.setupActivity(FragmentActivity.class);
        ArrayList<String> blackListedRetailer = new ArrayList<>();
        IAPFlowInput input = new IAPFlowInput("HX9043/64");
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW, input, null,blackListedRetailer);
        mIAPSettings.setUseLocalData(true);
        mIapInterface.init(mIAPDependencies, mIAPSettings);
        mIapInterface.launch(new FragmentLauncher(activity, R.id.fl_mainFragmentContainer, Mockito.mock(ActionBarListener.class)), iapLaunchInput);
    }

    @Test
    public void testGetProductCount() {
        IAPListener listener = Mockito.mock(IAPListener.class);
        mIapInterface.getProductCartCount(listener);
        Assert.assertNotNull(listener);

    }

    @Test
    public void getCompleteProductList() {
        IAPListener listener = Mockito.mock(IAPListener.class);
        mIapInterface.getCompleteProductList(listener);
        Assert.assertNotNull(listener);
    }


    @Test(expected = NullPointerException.class)
    public void isCartVisible() {
        IAPListener listener = Mockito.mock(IAPListener.class);
        //  mIapInterface.init(mIAPDependencies, mIAPSettings);
        mIapInterface.isCartVisible(listener);
        Assert.assertNotNull(listener);
    }

}