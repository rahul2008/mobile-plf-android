/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import android.os.Bundle;
import android.view.View;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.provider.ProviderType;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.utils.UIDNavigationIconToggler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSConfirmAppointmentFragmentTest {

    THSConfirmAppointmentFragment mThsConfirmAppointmentFragemnt;

    @Mock
    THSProviderInfo thsProviderInfoMock;

    @Mock
    ProviderInfo providerInfoMock;

    @Mock
    Date dateMock;


    @Mock
    ProviderType providerTypeMock;

    @Mock
    User userMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    Consumer consumerMock;

    @Mock
    THSConsumerWrapper thsConsumerWrapperMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

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

        THSManager.getInstance().setPTHConsumer(thsConsumerWrapperMock);
        when(thsConsumerWrapperMock.getConsumer()).thenReturn(consumerMock);

        when(consumerMock.getEmail()).thenReturn("sds");

        mThsConfirmAppointmentFragemnt = new THSConfirmAppointmentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(THSConstants.THS_PROVIDER_INFO,thsProviderInfoMock);
        bundle.putSerializable(THSConstants.THS_DATE,dateMock);
        bundle.putString(THSConstants.THS_SET_REMINDER_EXTRA_KEY,THSConstants.THS_NO_REMINDER_STRING);
        mThsConfirmAppointmentFragemnt.setArguments(bundle);

        when(thsProviderInfoMock.getProviderInfo()).thenReturn(providerInfoMock);
                when(providerInfoMock.getFullName()).thenReturn("spoo");
        when(providerInfoMock.getSpecialty()).thenReturn(providerTypeMock);
        when(providerTypeMock.getName()).thenReturn("spoo");
        when(providerInfoMock.hasImage()).thenReturn(false);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);
        mThsConfirmAppointmentFragemnt.setActionBarListener(actionBarListenerMock);

        SupportFragmentTestUtil.startFragment(mThsConfirmAppointmentFragemnt);
    }

    @Test
    public void onStop(){
        mThsConfirmAppointmentFragemnt.onStop();
    }

    @Test
    public void getTHSProviderInfo() throws Exception {
        final THSProviderInfo thsProviderInfo = mThsConfirmAppointmentFragemnt.getTHSProviderInfo();
        assertNotNull(thsProviderInfo);
        assertThat(thsProviderInfo).isInstanceOf(THSProviderInfo.class);
    }

    @Test
    public void getAppointmentDate() throws Exception {
        final Date appointmentDate = mThsConfirmAppointmentFragemnt.getAppointmentDate();
        assertNotNull(appointmentDate);
        assertThat(appointmentDate).isInstanceOf(Date.class);
    }

    @Test
    public void onClick() throws Exception {
        final View viewById = mThsConfirmAppointmentFragemnt.getView().findViewById(R.id.ok_got_it);
        viewById.performClick();
    }

}