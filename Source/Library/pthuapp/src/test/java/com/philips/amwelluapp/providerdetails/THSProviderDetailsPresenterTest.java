package com.philips.amwelluapp.providerdetails;

import android.content.Context;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.philips.amwelluapp.utility.PTHManager;

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

    PTHManager pthManager;

    PTHProviderDetailsPresenter providerDetailsPresenter;

    @Mock
    AWSDK awsdkMock;

    @Mock
    PTHPRoviderDetailsViewInterface pthpRoviderDetailsViewInterface;

    @Mock
    PTHManager pthManagerMock;

    @Mock
    PracticeProvidersManager practiseprovidermanagerMock;

    @Captor
    private ArgumentCaptor requestProviderDetialsCaptor;

    @Mock
    Provider provider;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        pthManager = PTHManager.getInstance();
        pthManager.setAwsdk(awsdkMock);
        providerDetailsPresenter = new PTHProviderDetailsPresenter(pthpRoviderDetailsViewInterface);
    }

    @Test
    public void testFetchProviderDetailsMethod() throws AWSDKInstantiationException {
        providerDetailsPresenter = new PTHProviderDetailsPresenter(pthpRoviderDetailsViewInterface){
            @Override
            protected PTHManager getPTHManager() {
                return pthManagerMock;
            }
        };
        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        providerDetailsPresenter.fetchProviderDetails();
        verify(pthManagerMock).getProviderDetails(any(Context.class),any(Consumer.class),any(ProviderInfo.class), (PTHProviderDetailsCallback) requestProviderDetialsCaptor.capture());
        PTHProviderDetailsCallback value = (PTHProviderDetailsCallback) requestProviderDetialsCaptor.getValue();
        value.onProviderDetailsReceived(provider,any(SDKError.class));

    }
}
