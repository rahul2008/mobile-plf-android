package com.philips.cdp.di.iap.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.di.iap.utils.IAPConstant;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import java.util.ArrayList;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.buildActivity;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class IAPActivityTest {
    @Mock
    FragmentManager fragmentManagerMock;
    @Mock
    FragmentTransaction fragmentTransactionMock;
    @Captor
    ArgumentCaptor<Fragment> fragmentArgumentCaptor;

    private IAPActivity activity;
    private ShadowActivity shadowActivity;
    Intent intent;
    private MockNetworkController mNetworkController;
    private HybrisDelegate mHybrisDelegate;

    @Before
    public void setUp() {
        initMocks(this);

        when(fragmentManagerMock.beginTransaction()).thenReturn(fragmentTransactionMock);

        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
        intent = new Intent();
        intent.putExtra(IAPConstant.IAP_KEY_ACTIVITY_THEME, 0);
    }

    @Test
    public void shouldNotApplied_DefaultTheme() throws Exception {
        int themeIndex = intent.getIntExtra(IAPConstant.IAP_KEY_ACTIVITY_THEME, 0);
        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).create().resume().get();
        Assert.assertEquals(themeIndex, 0);
    }

    @Test
    public void shouldLandInProductCatalogScreen() throws Exception {
        intent.putExtra(IAPConstant.IAP_LANDING_SCREEN, 0);
        int landingIndex = intent.getIntExtra(IAPConstant.IAP_LANDING_SCREEN, 0);
        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).create().resume().get();
        Assert.assertEquals(landingIndex, 0);
    }

//    @Test
//    public void shouldLandInShoppingCartFragment() throws Exception {
//        intent.putExtra(IAPConstant.IAP_LANDING_SCREEN, 1);
//        int landingIndex = intent.getIntExtra(IAPConstant.IAP_LANDING_SCREEN, 1);
//        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).create().resume().get();
//        Assert.assertNotSame(landingIndex, 1);
//    }

    @Test
    public void shouldLandInPurchaseHistoryFragment() throws Exception {
        intent.putExtra(IAPConstant.IAP_LANDING_SCREEN, 2);
        int landingIndex = intent.getIntExtra(IAPConstant.IAP_LANDING_SCREEN, 2);
        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).create().resume().get();
        Assert.assertEquals(landingIndex, 2);
    }

    @Test
    public void shouldLandInProductDetailFragment() throws Exception {
        //when
        intent.putExtra(IAPConstant.IAP_LANDING_SCREEN, 3);
        intent.putExtra(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL, "HX8033/21");
        int landingIndex = intent.getIntExtra(IAPConstant.IAP_LANDING_SCREEN, 3);

        //then
        startActivity();

        //verify
        Assert.assertEquals(landingIndex, 3);
        verifyAddedFragment();
    }

    private void startActivity() {
        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).get();
        IAPActivity spyActivity = Mockito.spy(activity);

        Mockito.doReturn(fragmentManagerMock).when(spyActivity).getSupportFragmentManager();
        spyActivity.onCreate(null);
        spyActivity.onResume();

        verify(fragmentTransactionMock).replace(anyInt(), fragmentArgumentCaptor.capture(), anyString());
    }

    private void verifyAddedFragment() {
        verify(fragmentTransactionMock).replace(
                eq(R.id.fl_mainFragmentContainer),
                isA(fragmentArgumentCaptor.getValue().getClass()),
                anyString());
        verify(fragmentTransactionMock).commitAllowingStateLoss();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldLandInBuyDirectFragment() throws Exception {
        intent.putExtra(IAPConstant.IAP_LANDING_SCREEN, 4);
        intent.putExtra(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL, "HX8033/21");
        int landingIndex = intent.getIntExtra(IAPConstant.IAP_LANDING_SCREEN, 4);
        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).create().resume().get();
        Assert.assertEquals(landingIndex, 4);
    }

    @Test
    public void shouldCalled_OnDestryView() {
        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).create().resume().destroy().get();
        Assert.assertNotNull(activity);
    }

    @Test
    public void shouldCalled_OnPause() {
        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).create().resume().pause().get();
        Assert.assertNotNull(activity);
    }

    @Test
    public void shouldCalled_updateActionBarWithtrue() {
        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).create().resume().get();
        activity.updateActionBar("InAppPurchase", true);
    }

    @Test
    public void shouldCalled_updateActionBarWithFalse() {
        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).create().resume().get();
        activity.updateActionBar("InAppPurchase", false);
    }

    @Test
    public void shouldCalled_updateActionBarWithResourceID() {
        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).create().resume().get();
        activity.updateActionBar(String.valueOf(324), true);
    }

    @Test
    public void shouldCalled_onGetCartCount0() {
        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).create().resume().get();
        activity.onGetCartCount(0);
    }

    @Test
    public void shouldCalled_onGetCartCount1() {
        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).create().resume().get();
        activity.onGetCartCount(1);
    }

    @Test
    public void shouldCalled_onUpdateCartCount() {
        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).create().resume().get();
        activity.onUpdateCartCount();
    }

    @Test
    public void shouldCalled_onFinish() {
        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).create().resume().get();
        activity.finish();
    }

    @Test
    public void shouldCalled_onBackPressed() {
        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).create().resume().get();
        activity.onBackPressed();
    }

    @Test
    public void shouldCalled_onGetCompleteProductList() {
        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).create().resume().get();
        activity.onGetCompleteProductList(new ArrayList<String>());
    }

    @Test
    public void shouldCalled_onSuccess() {
        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).create().resume().get();
        activity.onSuccess();
    }

    @Test
    public void shouldCalled_onFailure() {
        activity = buildActivity(IAPActivity.class, intent).withIntent(intent).create().resume().get();
        activity.onFailure(0);
    }
}