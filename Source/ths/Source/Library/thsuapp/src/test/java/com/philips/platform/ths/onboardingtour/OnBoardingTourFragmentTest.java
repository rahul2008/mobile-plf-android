/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.onboardingtour;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class OnBoardingTourFragmentTest {

    OnBoardingTourFragment mOnBoardingTourFragment;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    ViewPager viewPagerMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    Consumer consumerMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    PagerAdapter pagerAdapterMock;

    @Mock
    OnBoardingTourPresenter mOnBoardingTourPresenterMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Captor
    private ArgumentCaptor<ViewPager.OnPageChangeListener> requestCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        THSManager.getInstance().setAwsdk(awsdkMock);

        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        THSManager.getInstance().setThsParentConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);


        mOnBoardingTourFragment = new OnBoardingTourFragment();
        mOnBoardingTourFragment.setActionBarListener(actionBarListenerMock);
        SupportFragmentTestUtil.startFragment(mOnBoardingTourFragment);
    }

    @Test
    public void startAppTagging() throws Exception {
        assertNotNull(mOnBoardingTourFragment);
    }

    @Test
    public void check_onPageScrolled(){
        when(viewPagerMock.getAdapter()).thenReturn(pagerAdapterMock);
        mOnBoardingTourFragment.pager = viewPagerMock;
        mOnBoardingTourFragment.addPageChangeListener();
        verify(viewPagerMock).addOnPageChangeListener(requestCaptor.capture());
        final ViewPager.OnPageChangeListener value = requestCaptor.getValue();
        value.onPageScrolled(1,0.2F,2);
    }

    @Test
    public void check_onPageSelected_0(){
        when(viewPagerMock.getAdapter()).thenReturn(pagerAdapterMock);
        mOnBoardingTourFragment.pager = viewPagerMock;
        mOnBoardingTourFragment.addPageChangeListener();
        verify(viewPagerMock).addOnPageChangeListener(requestCaptor.capture());
        final ViewPager.OnPageChangeListener value = requestCaptor.getValue();
        value.onPageSelected(0);
    }

    @Test
    public void check_onPageSelected_3(){
        when(viewPagerMock.getAdapter()).thenReturn(pagerAdapterMock);
        when(pagerAdapterMock.getCount()).thenReturn(2);
        mOnBoardingTourFragment.pager = viewPagerMock;
        mOnBoardingTourFragment.addPageChangeListener();
        verify(viewPagerMock).addOnPageChangeListener(requestCaptor.capture());
        final ViewPager.OnPageChangeListener value = requestCaptor.getValue();
        value.onPageSelected(3);
    }

    @Test
    public void check_onPageSelected_2(){
        when(viewPagerMock.getAdapter()).thenReturn(pagerAdapterMock);
        when(pagerAdapterMock.getCount()).thenReturn(4);
        mOnBoardingTourFragment.pager = viewPagerMock;
        mOnBoardingTourFragment.addPageChangeListener();
        verify(viewPagerMock).addOnPageChangeListener(requestCaptor.capture());
        final ViewPager.OnPageChangeListener value = requestCaptor.getValue();
        value.onPageSelected(3);
    }

    @Test
    public void check_onPageSelected_1(){
        when(viewPagerMock.getAdapter()).thenReturn(pagerAdapterMock);
        when(pagerAdapterMock.getCount()).thenReturn(2);
        mOnBoardingTourFragment.pager = viewPagerMock;
        mOnBoardingTourFragment.addPageChangeListener();
        verify(viewPagerMock).addOnPageChangeListener(requestCaptor.capture());
        final ViewPager.OnPageChangeListener value = requestCaptor.getValue();
        value.onPageSelected(1);
    }

    @Test
    public void check_onPageScrollStateChanged(){
        when(viewPagerMock.getAdapter()).thenReturn(pagerAdapterMock);
        when(pagerAdapterMock.getCount()).thenReturn(2);
        mOnBoardingTourFragment.pager = viewPagerMock;
        mOnBoardingTourFragment.addPageChangeListener();
        verify(viewPagerMock).addOnPageChangeListener(requestCaptor.capture());
        final ViewPager.OnPageChangeListener value = requestCaptor.getValue();
        value.onPageScrollStateChanged(1);
    }

    @Test
    public void onClick_welcome_rightarrow() throws Exception {
        mOnBoardingTourFragment.pager = viewPagerMock;
        final View viewById = mOnBoardingTourFragment.getView().findViewById(R.id.welcome_rightarrow);
        viewById.performClick();
        verify(viewPagerMock).arrowScroll(View.FOCUS_RIGHT);
    }

    @Test
    public void onClick_FOCUS_LEFT() throws Exception {
        mOnBoardingTourFragment.pager = viewPagerMock;
        final View viewById = mOnBoardingTourFragment.getView().findViewById(R.id.welcome_leftarrow);
        viewById.performClick();
        verify(viewPagerMock).arrowScroll(View.FOCUS_LEFT);
    }

    @Test
    public void onClick_welcome_start_registration_button() throws Exception {
        mOnBoardingTourFragment.pager = viewPagerMock;
        mOnBoardingTourFragment.presenter = mOnBoardingTourPresenterMock;
        final View viewById = mOnBoardingTourFragment.getView().findViewById(R.id.welcome_start_registration_button);
        viewById.performClick();
        verify(mOnBoardingTourPresenterMock).onEvent(anyInt());
    }

    @Test
    public void onClick_welcome_welcome_skip_button() throws Exception {
        mOnBoardingTourFragment.pager = viewPagerMock;
        mOnBoardingTourFragment.presenter = mOnBoardingTourPresenterMock;
        final View viewById = mOnBoardingTourFragment.getView().findViewById(R.id.welcome_skip_button);
        viewById.performClick();
        verify(mOnBoardingTourPresenterMock).onEvent(anyInt());
    }

}