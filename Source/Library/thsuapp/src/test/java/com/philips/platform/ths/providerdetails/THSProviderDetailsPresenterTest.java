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
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSAvailableProviderFragment;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.intake.THSSymptomsFragment;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
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
    com.philips.platform.ths.providerdetails.THSPRoviderDetailsViewInterface THSPRoviderDetailsViewInterface;

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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        THSManager = THSManager.getInstance();
        THSManager.setAwsdk(awsdkMock);
        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        providerDetailsPresenter = new THSProviderDetailsPresenter(THSPRoviderDetailsViewInterface, thsBaseFragmentMock);
    }

    @Test
    public void fetchProviderDetails() {
        when(THSPRoviderDetailsViewInterface.getTHSProviderInfo()).thenReturn(thsProviderInfo);
        when(THSPRoviderDetailsViewInterface.getContext()).thenReturn(contextMock);
        when(THSPRoviderDetailsViewInterface.getConsumerInfo()).thenReturn(consumerMock);
        when(thsProviderInfo.getProviderInfo()).thenReturn(providerInfo);
        providerDetailsPresenter.fetchProviderDetails();
        verify(practiseprovidermanagerMock).getProvider(any(ProviderInfo.class),any(Consumer.class),any(SDKCallback.class));
    }

    @Test
    public void fetchProviderDetailsThrowsException() {
        when(THSPRoviderDetailsViewInterface.getTHSProviderInfo()).thenReturn(thsProviderInfo);
        when(THSPRoviderDetailsViewInterface.getContext()).thenReturn(contextMock);
        when(THSPRoviderDetailsViewInterface.getConsumerInfo()).thenReturn(consumerMock);
        when(thsProviderInfo.getProviderInfo()).thenReturn(providerInfo);
        doThrow(AWSDKInstantiationException.class).when(practiseprovidermanagerMock).getProvider(any(ProviderInfo.class),
                any(Consumer.class),any(SDKCallback.class));
        providerDetailsPresenter.fetchProviderDetails();
        verify(practiseprovidermanagerMock).getProvider(any(ProviderInfo.class),any(Consumer.class),any(SDKCallback.class));
    }

    @Test
    public void fetchProviderDetailsWhenTHSProviderInfoIsNull() {
        providerDetailsPresenter.fetchProviderDetails();
        verify(THSPRoviderDetailsViewInterface).dismissRefreshLayout();
    }

    @Test
    public void onProviderDetailsReceived(){
        providerDetailsPresenter.onProviderDetailsReceived(providerMock,sdkErrorMock);
        verify(THSPRoviderDetailsViewInterface).updateView(providerMock);
    }

    @Test
    public void onProviderDetailsFetchError(){
        providerDetailsPresenter.onProviderDetailsFetchError(throwableMock);
    }

    @Test
    public void onEventdetailsButtonOne(){
        providerDetailsPresenter.onEvent(R.id.detailsButtonOne);
        verify(thsBaseFragmentMock).addFragment(any(THSSymptomsFragment.class),anyString(),any(Bundle.class));
    }

    @Test
    public void onEventdetailsButtonTwo(){
        providerDetailsPresenter.onEvent(R.id.detailsButtonTwo);
        verify(thsBaseFragmentMock).addFragment(any(THSAvailableProviderFragment.class),anyString(),any(Bundle.class));
    }

    //TODO: add the validation one's code is added - Action on Spoorti
    @Test
    public void onEventdetailsButtonContinue(){
        providerDetailsPresenter.onEvent(R.id.detailsButtonContinue);

    }
}
