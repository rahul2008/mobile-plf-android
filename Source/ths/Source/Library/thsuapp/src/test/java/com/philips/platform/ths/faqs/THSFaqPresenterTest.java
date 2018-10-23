/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.faqs;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URL;
import java.util.HashMap;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSFaqPresenterTest {

    THSFaqPresenter mTHSFaqPresenter;

    @Mock
    THSFaqFragment mThsFaqFragmentMock;

    @Mock
    AppConfigurationInterface appConfigurationInterface;

    @Mock
    AppInfraInterface appInfraInterfaceMock;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Captor
    private ArgumentCaptor<ServiceDiscoveryInterface.OnGetServiceUrlListener> getServiceUrlListenerArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(appInfraInterfaceMock.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        when(appInfraInterfaceMock.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterfaceMock.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        THSManager.getInstance().setAppInfra(appInfraInterfaceMock);
        when(appInfraInterfaceMock.getConfigInterface()).thenReturn(appConfigurationInterface);
        mTHSFaqPresenter = new THSFaqPresenter(mThsFaqFragmentMock);
    }

    @Test
    public void getFaqonError() throws Exception {
        mTHSFaqPresenter.getFaq();
        verify(serviceDiscoveryMock).getServiceUrlWithCountryPreference(anyString(), getServiceUrlListenerArgumentCaptor.capture());
        final ServiceDiscoveryInterface.OnGetServiceUrlListener value = getServiceUrlListenerArgumentCaptor.getValue();
        value.onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT, anyString());
        verify(mThsFaqFragmentMock).showError(anyString());
    }

    @Test
    public void getFaqonSuccess() throws Exception {
        mTHSFaqPresenter.getFaq();
        verify(serviceDiscoveryMock).getServiceUrlWithCountryPreference(anyString(), getServiceUrlListenerArgumentCaptor.capture());
        final ServiceDiscoveryInterface.OnGetServiceUrlListener value = getServiceUrlListenerArgumentCaptor.getValue();
        URL url_g = new URL("http://www.google.com/");
        value.onSuccess(url_g);
    }

    @Test
    public void onEvent() throws Exception {
        mTHSFaqPresenter.onEvent(-1);
    }

    @Test
    public void parseJson() throws Exception {
        mTHSFaqPresenter.parseJson("[{\n" +
                "\t\t\"section\": \"Doctor Visit\",\n" +
                "\t\t\"faq\": [{\n" +
                "\t\t\t\t\"question\": \"Will I receive an e-mail upon scheduling the appointment?\",\n" +
                "\t\t\t\t\"answer\": \"Yes, you will receive an e-mail once the appointment is scheduled. The e-mail will have the details of the appointment like Doctor Name, Date and time of the appointment and also the link to launch the app directly\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"question\": \"Will I get reminder message on the scheduled appointment?\",\n" +
                "\t\t\t\t\"answer\": \"  While booking an appointment, there is a choice to select the reminder notice like 15 mins before the appointment or 1 day before the appointment, etc.\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"question\": \"Assume the I am accessing the appointment confirmation e-mail on my phone and what happens if I click on the link in the e-mail?\",\n" +
                "\t\t\t\t\"answer\": \"Upon clicking the link in the appointment confirmation e-mail, \\\\n If the phone has the Philips app, then it will be launched else you will be directed to google pay store or Apple store\"\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"section\": \"Payment and Insurance\",\n" +
                "\t\t\"faq\": [{\n" +
                "\t\t\t\t\"question\": \"Can I use my credit card to pay the consultation fees?\",\n" +
                "\t\t\t\t\"answer\": \"Yes, you can pay the consultation fees using the credit card. We use secure payment gateway and we complaint to PCI standards\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"question\": \"Can I pay through the insurance?\",\n" +
                "\t\t\t\t\"Answer\": \"Yes, you can pay through your insurance provided if it is listed in the app.\"\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t},\n" +
                " \t{\n" +
                "\t\t\"section\": \"Privacy and Security\",\n" +
                "\t\t\"faq\": [{\n" +
                "\t\t\t\t\"question\": \"Is my medical data secured? Is the Telehealth component HIPAA compliant?\",\n" +
                "\t\t\t\t\"answer\": \"Yes, your data is completely secured and we are HIPAA compliant.\"\n" +
                "\t\t\t}\n" +
                "\t\t\t\n" +
                "\t\t]\n" +
                "\t}\n" +
                "]");
        verify(mThsFaqFragmentMock).updateFaqs(any(HashMap.class));
    }

}