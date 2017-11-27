/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import android.os.Bundle;
import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.consumer.ConsumerInfo;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.visit.VisitReport;
import com.americanwell.sdk.entity.visit.VisitReportDetail;
import com.americanwell.sdk.entity.visit.VisitSchedule;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSVisitHistoryDetailFragmentTest {

    THSVisitHistoryDetailFragment mThsVisitHistoryDetailFragment;

    Bundle arguments;

    @Mock
    VisitReport visitReportMock;

    @Mock
    User userMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    Authentication authenticationMock;

    @Mock
    AppConfigurationInterface appConfigurationInterfaceMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    State stateMock;

    @Mock
    AppInfraInterface appInfraInterfaceMock;

    @Mock
    Consumer consumerMock;

    @Mock
    THSSDKError thssdkErrorMock;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    ProviderInfo providerInfoMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    Pharmacy pharmacyMock;

    @Mock
    ConsumerInfo consumerInfoMock;

    @Mock
    VisitSchedule visitScheduleMock;

    @Mock
    VisitReportDetail visitReportDetailMock;

    @Mock
    Address addressMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        THSManager.getInstance().setAwsdk(awsdkMock);

        arguments = new Bundle();
        arguments.putParcelable(THSConstants.THS_VISIT_REPORT, visitReportMock);

        mThsVisitHistoryDetailFragment = new THSVisitHistoryDetailFragmentTestMock();
        mThsVisitHistoryDetailFragment.setArguments(arguments);

        THSManager.getInstance().TEST_FLAG = true;
        THSManager.getInstance().setUser(userMock);

        when(userMock.getHsdpUUID()).thenReturn("123");
        when(userMock.getHsdpAccessToken()).thenReturn("123");


        THSManager.getInstance().setAwsdk(awsdkMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        when(appInfraInterfaceMock.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterfaceMock.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);

        THSManager.getInstance().setAppInfra(appInfraInterfaceMock);

        mThsVisitHistoryDetailFragment.setActionBarListener(actionBarListenerMock);
        SupportFragmentTestUtil.startFragment(mThsVisitHistoryDetailFragment);
    }

    @Test(expected = NullPointerException.class)
    public void updateViewWithProviderInfo() throws Exception {
        when(visitReportDetailMock.getConsumerInfo()).thenReturn(consumerInfoMock);
        when(consumerInfoMock.getFullName()).thenReturn("Spootyi Hallur");
        when(visitReportDetailMock.getSchedule()).thenReturn(visitScheduleMock);
        when(visitScheduleMock.getActualStartTime()).thenReturn(23L);
        when(visitReportDetailMock.getAssignedProviderInfo()).thenReturn(providerInfoMock);
        mThsVisitHistoryDetailFragment.updateView(visitReportDetailMock);
        final CharSequence text = mThsVisitHistoryDetailFragment.mLabelPracticeName.getText();
        assertNull(text);
    }

    @Test
    public void updateView() throws Exception {
        when(visitReportDetailMock.getConsumerInfo()).thenReturn(consumerInfoMock);
        when(consumerInfoMock.getFullName()).thenReturn("Spootyi Hallur");
        when(visitReportDetailMock.getSchedule()).thenReturn(visitScheduleMock);
        when(visitScheduleMock.getActualStartTime()).thenReturn(23L);
        mThsVisitHistoryDetailFragment.updateView(visitReportDetailMock);
        final CharSequence text = mThsVisitHistoryDetailFragment.mLabelPracticeName.getText();
        assert text!=null;
        assert text!="";
    }

    @Test
    public void updateViewVisitReportNull() throws Exception {
        when(visitReportDetailMock.getConsumerInfo()).thenReturn(consumerInfoMock);
        when(consumerInfoMock.getFullName()).thenReturn("Spootyi Hallur");
        when(visitReportDetailMock.getSchedule()).thenReturn(visitScheduleMock);
        when(visitScheduleMock.getActualStartTime()).thenReturn(23L);
        mThsVisitHistoryDetailFragment.updateView(null);
        final CharSequence text = mThsVisitHistoryDetailFragment.mLabelPracticeName.getText();
        assertNotNull(text);
    }

    @Test
    public void getVisitReport() throws Exception {
        final VisitReport visitReport = mThsVisitHistoryDetailFragment.getVisitReport();
        assertNotNull(visitReport);
        assertThat(visitReport).isInstanceOf(VisitReport.class);
    }

    @Test
    public void updateShippingAddressView() throws Exception {

        when(addressMock.getCity()).thenReturn("cambrige");
        when(addressMock.getState()).thenReturn(stateMock);
        when(addressMock.getAddress1()).thenReturn("address1");
        when(addressMock.getZipCode()).thenReturn("35006");

        mThsVisitHistoryDetailFragment.updateShippingAddressView(addressMock,"Spoorti");
        final CharSequence text = mThsVisitHistoryDetailFragment.consumerShippingZip.getText();
        assert text!=null;
    }

    @Test
    public void updateShippingAddressView_Address_null() throws Exception {

        when(addressMock.getCity()).thenReturn("cambrige");
        when(addressMock.getState()).thenReturn(stateMock);
        when(addressMock.getAddress1()).thenReturn("address1");
        when(addressMock.getZipCode()).thenReturn("35006");

        mThsVisitHistoryDetailFragment.updateShippingAddressView(null,"Spoorti");
        final CharSequence text = mThsVisitHistoryDetailFragment.consumerShippingZip.getText();
        assert text!=null;
    }

    @Test
    public void updatePharmacyDetailsView() throws Exception {
        when(pharmacyMock.getAddress()).thenReturn(addressMock);
        when(addressMock.getAddress1()).thenReturn("address 1");
        when(addressMock.getAddress2()).thenReturn("address 2");
        when(pharmacyMock.getName()).thenReturn("pharmacy");
        when(addressMock.getState()).thenReturn(stateMock);
        when(stateMock.getCode()).thenReturn("123");
        when(addressMock.getZipCode()).thenReturn("350035");

        mThsVisitHistoryDetailFragment.updatePharmacyDetailsView(pharmacyMock);
        final CharSequence text = mThsVisitHistoryDetailFragment.pharmacyZip.getText();
        assertNotNull(text);
    }

    @Test
    public void updatePharmacyDetailsViewPharmacyNull() throws Exception {
        mThsVisitHistoryDetailFragment.updatePharmacyDetailsView(null);
        final CharSequence text = mThsVisitHistoryDetailFragment.pharmacyZip.getText();
        assertNotNull(text);
    }

    @Test
    public void showHippsNotice() throws Exception {
        mThsVisitHistoryDetailFragment.showHippsNotice("http://ssss");
    }

    @Test
    public void clickProviderLayoutLayout(){
        final View viewById = mThsVisitHistoryDetailFragment.getView().findViewById(R.id.ths_waiting_room_provider_detail_relativelayout);
        viewById.performClick();

    }

}