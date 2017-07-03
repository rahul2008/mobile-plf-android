package com.philips.amwelluapp.providerslist;

import android.content.Context;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.philips.amwelluapp.base.PTHBaseView;
import com.philips.amwelluapp.utility.PTHManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSProvidersListPresenterTest {

    PTHManager pthManager;

    @Mock
    private PTHBaseView mUiBaseView;

    @Mock
    private PTHProviderListViewInterface pthProviderListViewInterface;

    @Mock
    private PTHProvidersListCallback pthProvidersListCallback;

    PTHProviderListPresenter pthProviderListPresenter;

    @Mock
    PTHManager pthManagerMock;

    @Mock
    Consumer consumer;

    @Mock
    Practice practice;

    @Mock
    AWSDK awsdkMock;

    @Mock
    Context context;

    @Mock
    SDKError sdkError;

    @Mock
    PracticeProvidersManager practiseprovidermanagerMock;

    @Captor
    private ArgumentCaptor requestProviderListCaptor;

    @Mock
    List<ProviderInfo> providerInfo;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        pthManager = PTHManager.getInstance();
        pthManager.setAwsdk(awsdkMock);
        pthProviderListPresenter = new PTHProviderListPresenter(mUiBaseView,pthProviderListViewInterface);

    }

    @Test
    public void testFetchProviderListMethod() throws AWSDKInstantiationException {
        pthProviderListPresenter = new PTHProviderListPresenter(mUiBaseView,pthProviderListViewInterface){
            @Override
            PTHManager getPthManager() {
                return pthManagerMock;
            }
        };
        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        pthProviderListPresenter.fetchProviderList(consumer,practice);
        verify(pthManagerMock).getProviderList(any(Context.class),any(Consumer.class),any(Practice.class), (PTHProvidersListCallback) requestProviderListCaptor.capture());
        PTHProvidersListCallback value = (PTHProvidersListCallback) requestProviderListCaptor.getValue();
        value.onProvidersListReceived(providerInfo,any(SDKError.class));

    }
}
