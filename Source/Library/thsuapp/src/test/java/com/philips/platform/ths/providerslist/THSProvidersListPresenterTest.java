package com.philips.platform.ths.providerslist;

import android.content.Context;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.utility.PTHManager;

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
    private THSBaseView mUiBaseView;

    @Mock
    private com.philips.platform.ths.providerslist.THSProviderListViewInterface THSProviderListViewInterface;

    @Mock
    private com.philips.platform.ths.providerslist.THSProvidersListCallback THSProvidersListCallback;

    com.philips.platform.ths.providerslist.THSProviderListPresenter THSProviderListPresenter;

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
        THSProviderListPresenter = new THSProviderListPresenter(mUiBaseView, THSProviderListViewInterface);

    }

    @Test
    public void testFetchProviderListMethod() throws AWSDKInstantiationException {
        THSProviderListPresenter = new THSProviderListPresenter(mUiBaseView, THSProviderListViewInterface){
            @Override
            PTHManager getPthManager() {
                return pthManagerMock;
            }
        };
        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiseprovidermanagerMock);
        THSProviderListPresenter.fetchProviderList(consumer,practice);
        verify(pthManagerMock).getProviderList(any(Context.class),any(Consumer.class),any(Practice.class), (THSProvidersListCallback) requestProviderListCaptor.capture());
        THSProvidersListCallback value = (THSProvidersListCallback) requestProviderListCaptor.getValue();
        value.onProvidersListReceived(providerInfo,any(SDKError.class));

    }
}
