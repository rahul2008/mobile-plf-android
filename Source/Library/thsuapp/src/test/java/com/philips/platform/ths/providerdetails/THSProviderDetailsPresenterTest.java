package com.philips.platform.ths.providerdetails;

import android.content.Context;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by philips on 7/3/17.
 */

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
    PracticeProvidersManager practiseprovidermanagerMock;

    @Captor
    private ArgumentCaptor requestProviderDetialsCaptor;

    @Mock
    Provider provider;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        THSManager = THSManager.getInstance();
        THSManager.setAwsdk(awsdkMock);
        providerDetailsPresenter = new THSProviderDetailsPresenter(THSPRoviderDetailsViewInterface);
    }

    @Test
    public void testFetchProviderDetailsMethod() throws AWSDKInstantiationException {
        providerDetailsPresenter = new THSProviderDetailsPresenter(THSPRoviderDetailsViewInterface){
            @Override
            protected THSManager getPTHManager() {
                return THSManagerMock;
            }
        };
        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        providerDetailsPresenter.fetchProviderDetails();
        verify(THSManagerMock).getProviderDetails(any(Context.class),any(Consumer.class),any(ProviderInfo.class), (THSProviderDetailsCallback) requestProviderDetialsCaptor.capture());
        THSProviderDetailsCallback value = (THSProviderDetailsCallback) requestProviderDetialsCaptor.getValue();
        value.onProviderDetailsReceived(provider,any(SDKError.class));

    }
}
