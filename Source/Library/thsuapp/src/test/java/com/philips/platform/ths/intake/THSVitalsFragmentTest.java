package com.philips.platform.ths.intake;

import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.entity.visit.Vitals;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSVitalsFragmentTest {

    THSVitalsFragmentTestMock thsVitalsFragment;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    THSConsumer pthConsumerMock;

    @Mock
    Consumer consumerMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    THSVitalsPresenter presenterMock;

    @Mock
    FragmentLauncher fragmentLauncherMock;

    @Mock
    THSVisitContext pthVisitContextMock;

    @Mock
    VisitContext visitContextMock;

    @Mock
    THSVitals thsVitalsMock;

    @Mock
    Vitals vitalsMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);

        thsVitalsFragment = new THSVitalsFragmentTestMock();
        thsVitalsFragment.setActionBarListener(actionBarListenerMock);


        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(pthConsumerMock);

        THSManager.getInstance().setVisitContext(pthVisitContextMock);
        when(pthVisitContextMock.getVisitContext()).thenReturn(visitContextMock);

        when(pthConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
    }

    @Test
    public void onCreateView() throws Exception {

    }

    @Test
    public void onActivityCreated() throws Exception {

    }

    @Test
    public void getContainerID() throws Exception {

    }

    @Test
    public void handleBackEvent() throws Exception {
        Assert.assertEquals(thsVitalsFragment.handleBackEvent(),false);
    }

    @Test
    public void onClickContinueBtn() throws Exception {
        SupportFragmentTestUtil.startFragment(thsVitalsFragment);
        thsVitalsFragment.mThsVitalsPresenter = presenterMock;
        thsVitalsFragment.setFragmentLauncher(fragmentLauncherMock);
        final View viewById = thsVitalsFragment.getView().findViewById(R.id.vitals_continue_btn);
        viewById.performClick();
        verify(presenterMock).onEvent(R.id.vitals_continue_btn);
    }

    @Test
    public void onClickSkipBtn() throws Exception {
        SupportFragmentTestUtil.startFragment(thsVitalsFragment);
        thsVitalsFragment.mThsVitalsPresenter = presenterMock;
        thsVitalsFragment.setFragmentLauncher(fragmentLauncherMock);
        final View viewById = thsVitalsFragment.getView().findViewById(R.id.vitals_skip);
        viewById.performClick();
        verify(presenterMock).onEvent(R.id.vitals_skip);
    }

    @Test
    public void setVitalsValues() throws Exception {
        SupportFragmentTestUtil.startFragment(thsVitalsFragment);
        thsVitalsFragment.mThsVitalsPresenter = presenterMock;
        thsVitalsFragment.setVitalsValues();
        verify(presenterMock).isTextValid(thsVitalsFragment.mSystolic);
        verify(presenterMock).isTextValid(thsVitalsFragment.mDiastolic);
        verify(presenterMock).isTextValid(thsVitalsFragment.mWeight);
        verify(presenterMock).isTextValid(thsVitalsFragment.mTemperature);
    }

    @Test
    public void setVitalsValuesWithValidText() throws Exception {
        SupportFragmentTestUtil.startFragment(thsVitalsFragment);
        thsVitalsFragment.mThsVitalsPresenter = presenterMock;
        thsVitalsFragment.setTHSVitals(thsVitalsMock);

        when(thsVitalsMock.getVitals()).thenReturn(vitalsMock);

        when(presenterMock.isTextValid(thsVitalsFragment.mSystolic)).thenReturn(true);
        when(presenterMock.isTextValid(thsVitalsFragment.mDiastolic)).thenReturn(true);
        when(presenterMock.isTextValid(thsVitalsFragment.mWeight)).thenReturn(true);
        when(presenterMock.isTextValid(thsVitalsFragment.mTemperature)).thenReturn(true);

        thsVitalsFragment.setVitalsValues();
        verify(presenterMock).isTextValid(thsVitalsFragment.mSystolic);
        verify(presenterMock).isTextValid(thsVitalsFragment.mDiastolic);
        verify(presenterMock).isTextValid(thsVitalsFragment.mWeight);
        verify(presenterMock).isTextValid(thsVitalsFragment.mTemperature);
    }

    @Test
    public void updateUI() throws Exception {
        SupportFragmentTestUtil.startFragment(thsVitalsFragment);
        thsVitalsFragment.mThsVitalsPresenter = presenterMock;
        thsVitalsFragment.updateUI(thsVitalsMock);
        assertTrue(thsVitalsFragment.mContinue.isEnabled());
    }

    @Test
    public void getTHSVitals() throws Exception {
        thsVitalsFragment.setTHSVitals(thsVitalsMock);
        THSVitals thsVitals = thsVitalsFragment.getTHSVitals();
        assertNotNull(thsVitals);
        assertThat(thsVitals).isInstanceOf(THSVitals.class);
    }

    @Test
    public void validateIfSystolicValueIsWrong() throws Exception {
        SupportFragmentTestUtil.startFragment(thsVitalsFragment);
        thsVitalsFragment.mThsVitalsPresenter = presenterMock;
        thsVitalsFragment.validate();
        verify(presenterMock).isTextValid(thsVitalsFragment.mSystolic);
    }

    @Test
    public void validateIfDiastolicValueIsWrong() throws Exception {
        SupportFragmentTestUtil.startFragment(thsVitalsFragment);
        thsVitalsFragment.mThsVitalsPresenter = presenterMock;
        when(presenterMock.isTextValid(thsVitalsFragment.mSystolic)).thenReturn(true);
        thsVitalsFragment.validate();
        verify(presenterMock).isTextValid(thsVitalsFragment.mDiastolic);
    }

    @Test
    public void validateIfDiastolicIsHigherThanSystolic() throws Exception {
        SupportFragmentTestUtil.startFragment(thsVitalsFragment);
        thsVitalsFragment.mThsVitalsPresenter = presenterMock;
        when(presenterMock.isTextValid(thsVitalsFragment.mSystolic)).thenReturn(true);
        when(presenterMock.isTextValid(thsVitalsFragment.mDiastolic)).thenReturn(true);
        thsVitalsFragment.validate();
        verify(presenterMock).isTextValid(thsVitalsFragment.mDiastolic);
        verify(presenterMock).isTextValid(thsVitalsFragment.mSystolic);
    }

    @Test
    public void validateValidationPasses() throws Exception {
        SupportFragmentTestUtil.startFragment(thsVitalsFragment);
        thsVitalsFragment.mThsVitalsPresenter = presenterMock;
        when(presenterMock.isTextValid(thsVitalsFragment.mSystolic)).thenReturn(true);
        when(presenterMock.isTextValid(thsVitalsFragment.mDiastolic)).thenReturn(true);

        when(presenterMock.getTextFromEditText(thsVitalsFragment.mSystolic)).thenReturn("20");
        when(presenterMock.getTextFromEditText(thsVitalsFragment.mDiastolic)).thenReturn("30");
        when(presenterMock.stringToInteger("20")).thenReturn(20);
        when(presenterMock.stringToInteger("30")).thenReturn(30);

        thsVitalsFragment.validate();
        verify(presenterMock).isTextValid(thsVitalsFragment.mDiastolic);
        verify(presenterMock).isTextValid(thsVitalsFragment.mSystolic);
    }
}