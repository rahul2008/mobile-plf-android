package com.philips.platform.ths.intake;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.SDKCallback;
import com.americanwell.sdk.manager.VisitManager;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class THSSymptomsPresenterTest {

    THSSymptomsPresenter pthSymptomsPresenter;

    @Mock
    THSSymptomsFragment pTHBaseViewMock;

    @Mock
    THSProviderInfo pthProviderInfo;

    @Mock
    THSConsumer pthConsumer;

    @Mock
    AWSDK awsdk;

    @Mock
    FragmentActivity activityMock;

    @Mock
    THSSDKError pthsdkError;

    @Mock
    THSVisitContext pthVisitContext;

    @Mock
    ProviderInfo providerInfo;

    @Mock
    VisitContext visitContext;

    @Mock
    LegalText legalText;

    @Mock
    Throwable throwable;

    @Mock
    FragmentActivity fragmentActivity;

    @Mock
    VisitManager visitManagerMock;

    @Mock
    Consumer consumerMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        pthSymptomsPresenter = new THSSymptomsPresenter(pTHBaseViewMock,pthProviderInfo);
        THSManager.getInstance().setAwsdk(awsdk);
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(activityMock);
    }

    @Test
    public void onResponse() throws Exception {
        List list = new ArrayList();
        list.add(legalText);
        when(pthVisitContext.getLegalTexts()).thenReturn(list);
        pthSymptomsPresenter.onResponse(pthVisitContext, pthsdkError);
        verify(pTHBaseViewMock).addTopicsToView(pthVisitContext);
//        verify(legalText).setAccepted(true);
    }

    @Test
    public void onFailure() throws Exception {
        pthSymptomsPresenter.onFailure(throwable);
    }

    @Test
    public void getVisitContext() throws Exception {
        when(pthVisitContext.getVisitContext()).thenReturn(visitContext);
        when(pthProviderInfo.getProviderInfo()).thenReturn(providerInfo);
        when(pthConsumer.getConsumer()).thenReturn(consumerMock);
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(fragmentActivity);
        when(awsdk.getVisitManager()).thenReturn(visitManagerMock);
        THSManager.getInstance().setPTHConsumer(pthConsumer);
        THSManager.getInstance().setVisitContext(pthVisitContext);
        pthSymptomsPresenter.getVisitContext();
        verify(visitManagerMock).getVisitContext(any(Consumer.class),any(ProviderInfo.class),any(SDKCallback.class));
    }

    @Test
    public void getVisitContextThrowsMalformedURLException() throws Exception {
        when(pthVisitContext.getVisitContext()).thenReturn(visitContext);
        when(pthProviderInfo.getProviderInfo()).thenReturn(providerInfo);
        when(pthConsumer.getConsumer()).thenReturn(consumerMock);
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(fragmentActivity);
        when(awsdk.getVisitManager()).thenReturn(visitManagerMock);
        doThrow(MalformedURLException.class).when(awsdk).getVisitManager();
        pthSymptomsPresenter.getVisitContext();
        verifyNoMoreInteractions(visitManagerMock);
    }

    @Test
    public void getVisitContextThrowsURISyntaxException() throws Exception {
        when(pthVisitContext.getVisitContext()).thenReturn(visitContext);
        when(pthProviderInfo.getProviderInfo()).thenReturn(providerInfo);
        when(pthConsumer.getConsumer()).thenReturn(consumerMock);
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(fragmentActivity);
        when(awsdk.getVisitManager()).thenReturn(visitManagerMock);
        doThrow(URISyntaxException.class).when(awsdk).getVisitManager();
        pthSymptomsPresenter.getVisitContext();
        verifyNoMoreInteractions(visitManagerMock);
    }

    @Test
    public void getVisitContextThrowsAWSDKInstantiationException() throws Exception {
        when(pthVisitContext.getVisitContext()).thenReturn(visitContext);
        when(pthProviderInfo.getProviderInfo()).thenReturn(providerInfo);
        when(pthConsumer.getConsumer()).thenReturn(consumerMock);
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(fragmentActivity);
        when(awsdk.getVisitManager()).thenReturn(visitManagerMock);
        doThrow(AWSDKInstantiationException.class).when(awsdk).getVisitManager();
        pthSymptomsPresenter.getVisitContext();
        verifyNoMoreInteractions(visitManagerMock);
    }

    @Test
    public void getVisitContextThrowsAWSDKInitializationException() throws Exception {
        when(pthVisitContext.getVisitContext()).thenReturn(visitContext);
        when(pthProviderInfo.getProviderInfo()).thenReturn(providerInfo);
        when(pthConsumer.getConsumer()).thenReturn(consumerMock);
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(fragmentActivity);
        when(awsdk.getVisitManager()).thenReturn(visitManagerMock);
        doThrow(AWSDKInitializationException.class).when(awsdk).getVisitManager();
        pthSymptomsPresenter.getVisitContext();
        verifyNoMoreInteractions(visitManagerMock);
    }

    @Test
    public void onEventTest(){
        pthSymptomsPresenter.onEvent(R.id.continue_btn);
        verify(pTHBaseViewMock).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class));
    }

}