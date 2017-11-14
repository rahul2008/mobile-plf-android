//package com.philips.cdp.di.iap.screens;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.View;
//
//import com.philips.cdp.di.iap.BuildConfig;
//import com.philips.cdp.di.iap.CustomRobolectricRunner;
//import com.philips.cdp.di.iap.R;
//import com.philips.cdp.di.iap.TestUtils;
//import com.philips.cdp.di.iap.utils.IAPConstant;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.robolectric.RuntimeEnvironment;
//import org.robolectric.annotation.Config;
//import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;
//
//import static org.mockito.MockitoAnnotations.initMocks;
//
//@RunWith(CustomRobolectricRunner.class)
//@Config(constants = BuildConfig.class, sdk = 21)
//public class TrackOrderFragmentTest {
//    private Context mContext;
//    private TrackOrderFragment trackOrderFragment;
//    @Mock
//    private View mockView;
//
//    @Before
//    public void setUp() {
//        initMocks(this);
//
//        mContext = RuntimeEnvironment.application;
//        TestUtils.getStubbedStore();
//        TestUtils.getStubbedHybrisDelegate();
//    }
//
//    @Test
//    public void shouldDisplayAddressSelectionFragment() {
//        trackOrderFragment = TrackOrderFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
//        SupportFragmentTestUtil.startFragment(trackOrderFragment);
//    }
//
//    @Test
//    public void shouldBundle_With_PURCHASE_ID() {
//        final Bundle bundle = new Bundle();
//        bundle.putString(IAPConstant.PURCHASE_ID, "fdf");
//        trackOrderFragment = TrackOrderFragment.createInstance(bundle, InAppBaseFragment.AnimationType.NONE);
//        SupportFragmentTestUtil.startFragment(trackOrderFragment);
//    }
//
//    @Test
//    public void shouldBundle_With_TRACKING_ID() {
//        final Bundle bundle = new Bundle();
//        bundle.putString(IAPConstant.TRACKING_ID, "fdf");
//        trackOrderFragment = TrackOrderFragment.createInstance(bundle, InAppBaseFragment.AnimationType.NONE);
//        SupportFragmentTestUtil.startFragment(trackOrderFragment);
//    }
//
//    @Test
//    public void shouldBundle_With_DELIVERY_NAME() {
//        final Bundle bundle = new Bundle();
//        bundle.putString(IAPConstant.DELIVERY_NAME, "fdf");
//        trackOrderFragment = TrackOrderFragment.createInstance(bundle, InAppBaseFragment.AnimationType.NONE);
//        SupportFragmentTestUtil.startFragment(trackOrderFragment);
//    }
//
//    @Test
//    public void shouldBundle_With_DELIVERY_ADDRESS() {
//        final Bundle bundle = new Bundle();
//        bundle.putString(IAPConstant.DELIVERY_ADDRESS, "fdf");
//        trackOrderFragment = TrackOrderFragment.createInstance(bundle, InAppBaseFragment.AnimationType.NONE);
//        SupportFragmentTestUtil.startFragment(trackOrderFragment);
//    }
//
//    @Test
//    public void shouldBundle_With_ORDER_TRACK_URL() {
//        final Bundle bundle = new Bundle();
//        bundle.putString(IAPConstant.ORDER_TRACK_URL, "fdf");
//        trackOrderFragment = TrackOrderFragment.createInstance(bundle, InAppBaseFragment.AnimationType.NONE);
//        SupportFragmentTestUtil.startFragment(trackOrderFragment);
//    }
//
//    @Test
//    public void shouldOnClick() {
//        Mockito.when(mockView.getId()).thenReturn(R.id.btn_track);
//        final Bundle bundle = new Bundle();
//        bundle.putString(IAPConstant.ORDER_TRACK_URL, "fdf");
//        trackOrderFragment = TrackOrderFragment.createInstance(bundle, InAppBaseFragment.AnimationType.NONE);
//        //trackOrderFragment.onCreateView(null, null, null);
//        trackOrderFragment.onClick(mockView);
//        SupportFragmentTestUtil.startFragment(trackOrderFragment);
//    }
//
//
//}