package com.philips.platform.ths.providerdetails;

import android.content.Context;
import android.os.Bundle;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.intake.THSSymptomsFragment;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSProviderDetailsPresenterTest {

    THSManager THSManager;

    THSProviderDetailsPresenter providerDetailsPresenter;

    @Mock
    AWSDK awsdkMock;

    @Mock
    THSProviderDetailsViewInterface thsProviderDetailsViewInterface;

    @Mock
    THSManager THSManagerMock;

    @Mock
    Context contextMock;

    @Mock
    PracticeProvidersManager practiseprovidermanagerMock;

    @Captor
    private ArgumentCaptor requestProviderDetialsCaptor;

    @Mock
    THSProviderInfo thsProviderInfo;

    @Mock
    ProviderInfo providerInfo;

    @Mock
    Consumer consumerMock;

    @Mock
    Provider providerMock;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    Throwable throwableMock;

    @Mock
    THSBaseFragment thsBaseFragmentMock;

    @Mock
    THSConsumerWrapper thsConsumerWrapper;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;


    @Mock
    THSConsumer thsConsumer;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        THSManager = THSManager.getInstance();
        THSManager.setAwsdk(awsdkMock);

        com.philips.platform.ths.utility.THSManager.getInstance().setThsConsumer(thsConsumer);
        com.philips.platform.ths.utility.THSManager.getInstance().setThsParentConsumer(thsConsumer);
        when(thsConsumer.getConsumer()).thenReturn(consumerMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);


        THSManager.getInstance().setPTHConsumer(thsConsumerWrapper);
        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        providerDetailsPresenter = new THSProviderDetailsPresenter(thsProviderDetailsViewInterface, thsBaseFragmentMock);
        when(thsBaseFragmentMock.isFragmentAttached()).thenReturn(true);
    }

    @Test
    public void fetchProviderDetails() {
        when(thsProviderDetailsViewInterface.getTHSProviderInfo()).thenReturn(thsProviderInfo);
        when(thsProviderDetailsViewInterface.getContext()).thenReturn(contextMock);
        when(thsProviderDetailsViewInterface.getConsumerInfo()).thenReturn(consumerMock);
        when(thsProviderInfo.getProviderInfo()).thenReturn(providerInfo);
        providerDetailsPresenter.fetchProviderDetails();
        verify(practiseprovidermanagerMock).getProvider(any(ProviderInfo.class),any(Consumer.class),any(SDKCallback.class));
    }

    @Test
    public void fetchProviderDetailsThrowsException() {
        when(thsProviderDetailsViewInterface.getTHSProviderInfo()).thenReturn(thsProviderInfo);
        when(thsProviderDetailsViewInterface.getContext()).thenReturn(contextMock);
        when(thsProviderDetailsViewInterface.getConsumerInfo()).thenReturn(consumerMock);
        when(thsProviderInfo.getProviderInfo()).thenReturn(providerInfo);
        doThrow(AWSDKInstantiationException.class).when(practiseprovidermanagerMock).getProvider(any(ProviderInfo.class),
                any(Consumer.class),any(SDKCallback.class));
        providerDetailsPresenter.fetchProviderDetails();
        verify(practiseprovidermanagerMock).getProvider(any(ProviderInfo.class),any(Consumer.class),any(SDKCallback.class));
    }

    @Test
    public void fetchProviderDetailsWhenTHSProviderInfoIsNull() {
        providerDetailsPresenter.fetchProviderDetails();
        verify(thsProviderDetailsViewInterface).dismissRefreshLayout();
    }

    @Test
    public void onProviderDetailsReceived(){
        when(thsBaseFragmentMock.isFragmentAttached()).thenReturn(true);
        sdkErrorMock =null;
        providerDetailsPresenter.onProviderDetailsReceived(providerMock,sdkErrorMock);
        verify(thsProviderDetailsViewInterface).updateView(providerMock);
    }

    @Test
    public void onProviderDetailsFetchError(){
        providerDetailsPresenter.onProviderDetailsFetchError(throwableMock);
    }

    @Test
    public void onEventdetailsButtonOne(){
        providerDetailsPresenter.onEvent(R.id.detailsButtonOne);
        verify(thsBaseFragmentMock).addFragment(any(THSSymptomsFragment.class),anyString(),any(Bundle.class), anyBoolean());
    }

    @Test
    public void onEventdetailsButtonTwo(){
        providerDetailsPresenter.onEvent(R.id.detailsButtonTwo);
//        verify(thsBaseFragmentMock).addFragment(any(THSAvailableProviderDetailFragment.class),anyString(),any(Bundle.class));
    }

    //TODO: add the validation one's code is added - Action on Spoorti
    @Test
    public void onEventdetailsButtonContinue(){
        providerDetailsPresenter.onEvent(R.id.detailsButtonContinue);

    }
}
