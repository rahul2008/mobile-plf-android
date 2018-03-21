/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.consumer.RemindOptions;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.EstimatedVisitCost;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.providerdetails.THSProviderDetailsDisplayHelper;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.providerslist.THSProviderListPresenter;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_15_MINS_REMINDER;
import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static com.philips.platform.ths.utility.THSConstants.THS_DATE;
import static com.philips.platform.ths.utility.THSConstants.THS_EIGHT_HOURS_REMINDER;
import static com.philips.platform.ths.utility.THSConstants.THS_FOUR_HOURS_REMINDER;
import static com.philips.platform.ths.utility.THSConstants.THS_NO_REMINDER_STRING;
import static com.philips.platform.ths.utility.THSConstants.THS_ONE_DAY_REMINDER;
import static com.philips.platform.ths.utility.THSConstants.THS_ONE_HOUR_REMINDER;
import static com.philips.platform.ths.utility.THSConstants.THS_ONE_WEEK_REMINDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSAvailableProviderDetailFragmentTest {

    THSAvailableProviderDetailFragment mThsAvailableProviderDetailFragment;

    @Mock
    AWSDK awsdkMock;

    @Mock
    Date dateMock;

    @Mock
    ActionBarListener actionBarListener;

    @Mock
    FragmentLauncher framgmentLauncherMock;

    @Mock
    com.philips.platform.ths.providerslist.THSProviderListViewInterface THSProviderListViewInterface;

    @InjectMocks
    THSProviderListPresenter providerListPresenter;

    @Mock
    Consumer consumerMock;
    @Mock
    ProviderInfo providerInfo;

    @Mock
    Practice practiceMock;

    @Mock
    PracticeProvidersManager practiseprovidermanagerMock;

    @Mock
    com.philips.platform.ths.base.THSBaseView THSBaseView;

    @Mock
    Provider providerMock;

    @Mock
    EstimatedVisitCost estimatedVisitCostMock;

    @Mock
    THSProviderInfo thsProviderInfoMock;

    @Mock
    THSAvailableProvider thsAvailableProviderInfoMock;

    @Mock
    List listMock;

    @Mock
    THSAvailableProviderDetailPresenter presenterMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    Practice practiceInfoMock;

    @Mock
    Context contextMock;

    @Mock
    THSAvailableProvider thsProviderEntityMock;

    @Mock
    User userMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    THSProviderDetailsDisplayHelper thsProviderDetailsDisplayHelperMock;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);

        THSManager.getInstance().TEST_FLAG = true;
        when(userMock.getHsdpUUID()).thenReturn("abc");
        when(userMock.getGivenName()).thenReturn("Spoorti");
        when(userMock.getEmail()).thenReturn("sssss");
        when(userMock.getHsdpAccessToken()).thenReturn("2233");
        when(userMock.getHsdpAccessToken()).thenReturn("eeee");
        THSManager.getInstance().setUser(userMock);

        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);
        List list = new ArrayList();
        list.add(thsConsumerMock);
        when(thsConsumerMock.getDependents()).thenReturn(list);
        when(thsConsumerMock.getDependents()).thenReturn(list);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);


        when(THSManager.getInstance().getAwsdk(contextMock).getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        mThsAvailableProviderDetailFragment = new THSAvailableProviderDetailFragment();
        mThsAvailableProviderDetailFragment.setConsumerAndPractice(consumerMock, practiceMock);
        mThsAvailableProviderDetailFragment.setActionBarListener(actionBarListener);


        Bundle bundle = new Bundle();
        bundle.putParcelable(THSConstants.THS_PROVIDER_ENTITY,thsProviderEntityMock);
        bundle.putSerializable(THS_DATE,dateMock);
        bundle.putParcelable(THSConstants.THS_PRACTICE_INFO,practiceInfoMock);
        mThsAvailableProviderDetailFragment.setArguments(bundle);
        mThsAvailableProviderDetailFragment.thsAvailableDetailProviderPresenter = presenterMock;

        SupportFragmentTestUtil.startFragment(mThsAvailableProviderDetailFragment);
    }

    @Test
    public void getFragmentTag() throws Exception {
        assert ((THSAvailableProviderDetailFragment)mThsAvailableProviderDetailFragment).getFragmentTag().
                equalsIgnoreCase(THSAvailableProviderDetailFragment.TAG);
    }

    @Test
    public void getPractice() throws Exception {
        assert mThsAvailableProviderDetailFragment.getPractice()!=null;
    }

    @Test
    public void onCalenderItemClick() throws Exception {
        mThsAvailableProviderDetailFragment.thsAvailableDetailProviderPresenter = presenterMock;
        mThsAvailableProviderDetailFragment.onCalenderItemClick(0);
        verify(presenterMock).updateContinueButtonState(true);
    }

    @Test
    public void onClick_calendar_container() throws Exception {
        mThsAvailableProviderDetailFragment.thsAvailableDetailProviderPresenter = presenterMock;
        final View viewById = mThsAvailableProviderDetailFragment.getView().findViewById(R.id.calendar_container);
        viewById.performClick();
        verify(presenterMock).onEvent(R.id.calendar_container);
    }

    @Test
    public void onClick_detailsButtonContinue() throws Exception {
        mThsAvailableProviderDetailFragment.thsAvailableDetailProviderPresenter = presenterMock;
        final View viewById = mThsAvailableProviderDetailFragment.getView().findViewById(R.id.detailsButtonContinue);
        viewById.performClick();
        verify(presenterMock).scheduleAppointment(anyInt());
    }

    @Test
    public void onClick_set_reminder_layout() throws Exception {
        mThsAvailableProviderDetailFragment.thsAvailableDetailProviderPresenter = presenterMock;
        final View viewById = mThsAvailableProviderDetailFragment.getView().findViewById(R.id.set_reminder_layout);
        viewById.performClick();
        verify(presenterMock).onEvent(R.id.set_reminder_layout);
    }

    @Test
    public void getProviderEntity() throws Exception {
        final THSProviderInfo providerEntity = mThsAvailableProviderDetailFragment.getProviderEntity();
        assertThat(providerEntity).isInstanceOf(THSProviderInfo.class);
    }

    @Test
    public void getProviderEntity_THSProviderInfo() throws Exception {
        mThsAvailableProviderDetailFragment.thsProviderEntity = thsProviderEntityMock;
        final THSProviderInfo providerEntity = mThsAvailableProviderDetailFragment.getProviderEntity();
        assertThat(providerEntity).isInstanceOf(THSProviderInfo.class);
    }

    @Test
    public void setDate() throws Exception {
        mThsAvailableProviderDetailFragment.setDate(dateMock);
        final Date date = mThsAvailableProviderDetailFragment.getDate();
        assertThat(date).isInstanceOf(Date.class);
        assertNotNull(date);
    }

    @Test
    public void onPostData_NO_REMINDER() throws Exception {
        mThsAvailableProviderDetailFragment.onPostData(THS_NO_REMINDER_STRING);
        mThsAvailableProviderDetailFragment.remindOptions = RemindOptions.NO_REMINDER;
    }

    @Test
    public void onPostData_THS_15_MINS_REMINDER() throws Exception {
        mThsAvailableProviderDetailFragment.onPostData(THS_15_MINS_REMINDER);
        mThsAvailableProviderDetailFragment.remindOptions = RemindOptions.FIFTEEN_MIN;
    }

    @Test
    public void onPostData_THS_ONE_HOUR_REMINDER() throws Exception {
        mThsAvailableProviderDetailFragment.onPostData(THS_ONE_HOUR_REMINDER);
        mThsAvailableProviderDetailFragment.remindOptions = RemindOptions.ONE_HOUR;
    }

    @Test
    public void onPostData_THS_FOUR_HOURS_REMINDER() throws Exception {
        mThsAvailableProviderDetailFragment.onPostData(THS_FOUR_HOURS_REMINDER);
        mThsAvailableProviderDetailFragment.remindOptions = RemindOptions.FOUR_HOURS;
    }

    @Test
    public void onPostData_THS_EIGHT_HOURS_REMINDER() throws Exception {
        mThsAvailableProviderDetailFragment.onPostData(THS_EIGHT_HOURS_REMINDER);
        mThsAvailableProviderDetailFragment.remindOptions = RemindOptions.EIGHT_HOURS;
    }

    @Test
    public void onPostData_THS_EIGHT_THS_ONE_DAY_REMINDER() throws Exception {
        mThsAvailableProviderDetailFragment.onPostData(THS_ONE_DAY_REMINDER);
        mThsAvailableProviderDetailFragment.remindOptions = RemindOptions.ONE_DAY;
    }

    @Test
    public void onPostData_THS_EIGHT_THS_ONE_WEEK_REMINDER() throws Exception {
        mThsAvailableProviderDetailFragment.onPostData(THS_ONE_WEEK_REMINDER);
        mThsAvailableProviderDetailFragment.remindOptions = RemindOptions.ONE_WEEK;
    }

    @Test
    public void updateEstimatedCost() throws Exception {
        mThsAvailableProviderDetailFragment.thsProviderDetailsDisplayHelper = thsProviderDetailsDisplayHelperMock;
        mThsAvailableProviderDetailFragment.updateEstimatedCost(estimatedVisitCostMock);
        verify(thsProviderDetailsDisplayHelperMock).updateEstimateCost(estimatedVisitCostMock);
    }

    @Test
    public void getReminderOptions_ONE_WEEK() throws Exception {
        mThsAvailableProviderDetailFragment.remindOptions = RemindOptions.ONE_WEEK;
        final String reminderOptions = mThsAvailableProviderDetailFragment.getReminderOptions();
        assert  reminderOptions.equalsIgnoreCase(RemindOptions.ONE_WEEK);
    }

    @Test
    public void getReminderOptions_ONE_DEFAULT() throws Exception {
        mThsAvailableProviderDetailFragment.remindOptions = RemindOptions.NO_REMINDER;
        final String reminderOptions = mThsAvailableProviderDetailFragment.getReminderOptions();
        assert  reminderOptions.equalsIgnoreCase(RemindOptions.NO_REMINDER);
    }

    @Test
    public void getReminderTime() throws Exception {
        mThsAvailableProviderDetailFragment.thsProviderDetailsDisplayHelper = thsProviderDetailsDisplayHelperMock;
        mThsAvailableProviderDetailFragment.getReminderTime();
        verify(thsProviderDetailsDisplayHelperMock).getReminderValue();
    }

}