package com.philips.platform.ths.intake;

import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.health.Condition;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
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

import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSConditionsFragmentTest {

    public THSConditionsFragmentTestMock thsConditionsFragment;

    @Mock
    AWSDK awsdkMock;

    @Mock
    THSConsumerWrapper pthConsumer;

    @Mock
    THSVisitContext pthVisitContext;

    @Mock
    Consumer consumerMock;

    @Mock
    VisitContext visitManagerMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    THSMedicalConditionsPresenter presenterMock;

    @Mock
    FragmentLauncher fragmentLauncherMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    List<THSCondition> pthConditionses;

    @Mock
    THSCondition pthConditionsMock;

    @Mock
    Condition conditionMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        thsConditionsFragment = new THSConditionsFragmentTestMock();
        thsConditionsFragment.setActionBarListener(actionBarListenerMock);

        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);

        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(pthConsumer);
        THSManager.getInstance().setVisitContext(pthVisitContext);

        when(pthConsumer.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
    }

/*    @Test
    public void onActivityCreatedWhenThsConsitionsIsFetchedFromServerWithZeroConditions() throws Exception {
        thsConditionsFragment.setTHSConditions(pthConditionses);
        SupportFragmentTestUtil.startFragment(thsConditionsFragment);
        assertNotNull(thsConditionsFragment.mTHSConditions);
    }*/

/*    @Test
    public void onActivityCreatedWhenThsConsitionsIsFetchedFromServerWithOneConditions() throws Exception {
        when(pthConditionses.size()).thenReturn(1);
        when(pthConditionses.get(0)).thenReturn(pthConditionsMock);
        when(pthConditionsMock.getCondition()).thenReturn(conditionMock);
        when(conditionMock.isCurrent()).thenReturn(true);
        thsConditionsFragment.setTHSConditions(pthConditionses);
        SupportFragmentTestUtil.startFragment(thsConditionsFragment);
        assertNotNull(thsConditionsFragment.mTHSConditions);
    }*/

    @Test
    public void onClickContinueBtn() throws Exception {
        SupportFragmentTestUtil.startFragment(thsConditionsFragment);
        thsConditionsFragment.thsMedicalConditionsPresenter = presenterMock;
        thsConditionsFragment.setFragmentLauncher(fragmentLauncherMock);
        final View viewById = thsConditionsFragment.getView().findViewById(R.id.continue_btn);
        viewById.performClick();
        verify(presenterMock).onEvent(R.id.continue_btn);
    }

    @Test
    public void onClickSkipBtn() throws Exception {
        SupportFragmentTestUtil.startFragment(thsConditionsFragment);
        thsConditionsFragment.thsMedicalConditionsPresenter = presenterMock;
        thsConditionsFragment.setFragmentLauncher(fragmentLauncherMock);
        final View viewById = thsConditionsFragment.getView().findViewById(R.id.conditions_skip);
        viewById.performClick();
        verify(presenterMock).onEvent(R.id.conditions_skip);
    }


    @Test
    public void handleBackEvent() throws Exception {
        Assert.assertEquals(thsConditionsFragment.handleBackEvent(),false);
    }

   /* @Test
    public void setConditionsClickCheckBox() throws Exception {
        SupportFragmentTestUtil.startFragment(thsConditionsFragment);

        when(pthConditionses.size()).thenReturn(1);
        when(pthConditionses.get(0)).thenReturn(pthConditionsMock);
        when(pthConditionsMock.getCondition()).thenReturn(conditionMock);
        when(conditionMock.isCurrent()).thenReturn(true);

        thsConditionsFragment.setConditions(pthConditionses);
        final View childAt = thsConditionsFragment.mLinerLayout.getChildAt(0);
        childAt.performClick();

        verify(conditionMock).setCurrent(true);
    }*/

}