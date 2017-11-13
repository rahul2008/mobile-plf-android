package com.philips.platform.ths.insurance;

import android.view.View;
import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKLocalDate;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.insurance.HealthPlan;
import com.americanwell.sdk.entity.insurance.Relationship;
import com.americanwell.sdk.entity.insurance.Subscription;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSInsuranceDetailFragmentTest {
    THSInsuranceDetailFragmentMock thsInsuranceDetailFragment;


    @Mock
    AWSDK awsdkMock;

    @Mock
    Consumer consumerMoxk;

    @Mock
    THSConsumerWrapper thsConsumerMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    THSInsuranceDetailPresenter THSInsuranceDetailPresenterMock;

    @Mock
    THSSubscription thsSubscriptionMock;

    @Mock
    Subscription SubscriptionMock;

    @Mock
    HealthPlan healthPlanMock;

    @Mock
    Relationship relationshipMock;


    @Mock
    SDKLocalDate sdkLocalDate;


    @Mock
    FragmentLauncher fragmentLauncherMock;

    @Mock

    THSRelationship THSRelationshipMock;


    @Mock
    LoggingInterface loggingInterface;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;

        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(thsConsumerMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);


        when(thsConsumerMock.getConsumer()).thenReturn(consumerMoxk);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        thsInsuranceDetailFragment = new THSInsuranceDetailFragmentMock();
        thsInsuranceDetailFragment.setActionBarListener(actionBarListenerMock);


    }

    @Test
    public void onCreateView() throws Exception {

    }

    @Test
    public void onActivityCreated() throws Exception {

    }

    @Test
    public void showProgressbar() throws Exception {

    }

    @Test
    public void onResume() throws Exception {

    }

    @Test
    public void onClick() throws Exception {

    }

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;


    @Test
    public void onDOBClick() throws Exception {
        SupportFragmentTestUtil.startFragment(thsInsuranceDetailFragment);
        thsInsuranceDetailFragment.mPresenter = THSInsuranceDetailPresenterMock;
        thsInsuranceDetailFragment.setFragmentLauncher(fragmentLauncherMock);

        final View viewById = thsInsuranceDetailFragment.getView().findViewById(R.id.ths_insurance_detail_provider_relation_dob_edittext);
        assertTrue(viewById.performClick());
    }

    @Test
    public void onInsuranceListClick() throws Exception {
        SupportFragmentTestUtil.startFragment(thsInsuranceDetailFragment);
        thsInsuranceDetailFragment.mPresenter = THSInsuranceDetailPresenterMock;
        thsInsuranceDetailFragment.setFragmentLauncher(fragmentLauncherMock);

        final View viewById = thsInsuranceDetailFragment.getView().findViewById(R.id.ths_insurance_detail_provider_select_insurance_edit_text);
        assertTrue(viewById.performClick());
    }

    @Test
    public void onRelationshipListClick() throws Exception {
        SupportFragmentTestUtil.startFragment(thsInsuranceDetailFragment);
        thsInsuranceDetailFragment.mPresenter = THSInsuranceDetailPresenterMock;
        thsInsuranceDetailFragment.setFragmentLauncher(fragmentLauncherMock);

        final View viewById = thsInsuranceDetailFragment.getView().findViewById(R.id.ths_insurance_detail_provider_select_relation_edit_text);
        assertTrue(viewById.performClick());
    }

    @Test
    public void onSkipClick() throws Exception {
        SupportFragmentTestUtil.startFragment(thsInsuranceDetailFragment);
        thsInsuranceDetailFragment.mPresenter = THSInsuranceDetailPresenterMock;
        thsInsuranceDetailFragment.setFragmentLauncher(fragmentLauncherMock);

        final View viewById = thsInsuranceDetailFragment.getView().findViewById(R.id.ths_insurance_detail_skip_button);
        assertTrue(viewById.performClick());
        verify(THSInsuranceDetailPresenterMock, atLeastOnce()).onEvent(R.id.ths_insurance_detail_skip_button);
    }

    @Test
    public void onContinueClick() throws Exception {
        SupportFragmentTestUtil.startFragment(thsInsuranceDetailFragment);
        thsInsuranceDetailFragment.mPresenter = THSInsuranceDetailPresenterMock;
        thsInsuranceDetailFragment.setFragmentLauncher(fragmentLauncherMock);

        thsInsuranceDetailFragment.mTHSRelationshipList.getRelationShipList().add(relationshipMock);
        final View viewById = thsInsuranceDetailFragment.getView().findViewById(R.id.ths_insurance_detail_continue_button);
        assertTrue(viewById.performClick());

        thsInsuranceDetailFragment.mInsuranceRelationship = relationshipMock;
        thsInsuranceDetailFragment.mNotPrimarySubscriberCheckBox.setChecked(true);
        final View viewById1 = thsInsuranceDetailFragment.getView().findViewById(R.id.ths_insurance_detail_continue_button);
        assertTrue(viewById1.performClick());
    }


    @Test
    public void updateInsuranceUI() throws Exception {
        SupportFragmentTestUtil.startFragment(thsInsuranceDetailFragment);
        thsInsuranceDetailFragment.mPresenter = THSInsuranceDetailPresenterMock;
        thsInsuranceDetailFragment.setFragmentLauncher(fragmentLauncherMock);

        when(thsSubscriptionMock.getSubscription()).thenReturn(SubscriptionMock);
        when(SubscriptionMock.getHealthPlan()).thenReturn(healthPlanMock);
        when(healthPlanMock.getName()).thenReturn("Health Plan Name");
        when(healthPlanMock.isUsesSuffix()).thenReturn(true);
        when(SubscriptionMock.getSubscriberId()).thenReturn("subscriber ID");
        when(SubscriptionMock.getSubscriberSuffix()).thenReturn("suffix");
        when(SubscriptionMock.getRelationship()).thenReturn(relationshipMock);
        when(relationshipMock.isPrimarySubscriber()).thenReturn(false);
        when(SubscriptionMock.getPrimarySubscriberFirstName()).thenReturn("first name");
        when(SubscriptionMock.getPrimarySubscriberLastName()).thenReturn("last name");
        when(SubscriptionMock.getPrimarySubscriberDateOfBirth()).thenReturn(SDKLocalDate.valueOf("1992-05-12"));

        thsInsuranceDetailFragment.updateInsuranceUI(thsSubscriptionMock);
    }

    @Test
    public void showDatePicker() throws Exception {

    }

}