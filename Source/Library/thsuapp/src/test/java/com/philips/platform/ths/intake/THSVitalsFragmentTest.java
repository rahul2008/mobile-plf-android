package com.philips.platform.ths.intake;

import android.text.Editable;
import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.entity.visit.Vitals;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.InputValidationLayout;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.booleanThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSVitalsFragmentTest {

    THSVitalsFragmentTestMock thsVitalsFragment;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    THSConsumerWrapper pthConsumerMock;

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

    @Mock
    THSVitalsFragmentTestMock thsVitalsFragmentMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    InputValidationLayout inputValidationLayoutMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    EditText mSystolicMock;

    @Mock
    Editable editableMock;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);

        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        THSManager.getInstance().setThsParentConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);


        thsVitalsFragment = new THSVitalsFragmentTestMock();
        thsVitalsFragment.setActionBarListener(actionBarListenerMock);


        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(pthConsumerMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);


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
        thsVitalsFragment.mFarenheitInputLayoutContainer = inputValidationLayoutMock;
        thsVitalsFragment.mSystolicInputValidationLayout = inputValidationLayoutMock;
        thsVitalsFragment.mDiastolicInputValidationLayout = inputValidationLayoutMock;
        thsVitalsFragment.mWeightInputLayoutContainer = inputValidationLayoutMock;
        thsVitalsFragment.mSystolic = mSystolicMock;
        when(mSystolicMock.getText()).thenReturn(editableMock);
        when(mSystolicMock.getText().toString()).thenReturn("68");
        when(presenterMock.checkIfValueEntered(mSystolicMock)).thenReturn(true);
        when(inputValidationLayoutMock.isShowingError()).thenReturn(false);
        final View viewById = thsVitalsFragment.getView().findViewById(R.id.vitals_continue_btn);
        viewById.performClick();
        verify(presenterMock,atLeastOnce()).onEvent(R.id.vitals_continue_btn);
    }

    @Test
    public void onClickSkipBtn() throws Exception {
        SupportFragmentTestUtil.startFragment(thsVitalsFragment);
        thsVitalsFragment.mThsVitalsPresenter = presenterMock;
        thsVitalsFragment.setFragmentLauncher(fragmentLauncherMock);
        thsVitalsFragment.mFarenheitInputLayoutContainer = inputValidationLayoutMock;
        thsVitalsFragment.mSystolicInputValidationLayout = inputValidationLayoutMock;
        thsVitalsFragment.mDiastolicInputValidationLayout = inputValidationLayoutMock;
        final View viewById = thsVitalsFragment.getView().findViewById(R.id.vitals_skip);
        viewById.performClick();
        verify(presenterMock,atLeastOnce()).onEvent(R.id.vitals_skip);
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
        thsVitalsFragment.updateVitalsData();
    }

    @Test
    public void updateUI() throws Exception {
        SupportFragmentTestUtil.startFragment(thsVitalsFragment);
        thsVitalsFragment.mThsVitalsPresenter = presenterMock;
        thsVitalsFragment.updateUI(thsVitalsMock);
        verify(thsVitalsMock,atLeastOnce()).getDiastolic();
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
        thsVitalsFragment.validateBloodPressure();
        verify(presenterMock).checkIfValueEntered(thsVitalsFragment.mSystolic);
    }

    @Test
    public void validateIfDiastolicValueIsWrong() throws Exception {
        SupportFragmentTestUtil.startFragment(thsVitalsFragment);
        thsVitalsFragment.mThsVitalsPresenter = presenterMock;
        when(presenterMock.isTextValid(thsVitalsFragment.mSystolic)).thenReturn(true);
        thsVitalsFragmentMock.validateBloodPressure();
        verify(thsVitalsFragmentMock).validateBloodPressure();
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

        thsVitalsFragment.validateBloodPressure();
        verify(presenterMock).checkIfValueEntered(thsVitalsFragment.mSystolic);
    }
}