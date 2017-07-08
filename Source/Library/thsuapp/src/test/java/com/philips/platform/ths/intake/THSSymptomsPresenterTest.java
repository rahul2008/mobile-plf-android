package com.philips.platform.ths.intake;

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
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.PTHManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class THSSymptomsPresenterTest {

    com.philips.platform.ths.intake.THSSymptomsPresenter THSSymptomsPresenter;

    @Mock
    THSSymptomsFragment pTHBaseViewMock;

    @Mock
    THSProviderInfo THSProviderInfo;

    @Mock
    THSConsumer THSConsumer;

    @Mock
    AWSDK awsdk;

    @Mock
    FragmentActivity activityMock;

    @Mock
    THSSDKError THSSDKError;

    @Mock
    com.philips.platform.ths.intake.THSVisitContext THSVisitContext;

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
        THSSymptomsPresenter = new THSSymptomsPresenter(pTHBaseViewMock, THSProviderInfo);
        PTHManager.getInstance().setAwsdk(awsdk);
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(activityMock);
    }

    @Test
    public void onResponse() throws Exception {
        List list = new ArrayList();
        list.add(legalText);
        when(THSVisitContext.getLegalTexts()).thenReturn(list);
        THSSymptomsPresenter.onResponse(THSVisitContext, THSSDKError);
        verify(pTHBaseViewMock).addTopicsToView(THSVisitContext);
        verify(legalText).setAccepted(true);
    }

    @Test
    public void onFailure() throws Exception {
        THSSymptomsPresenter.onFailure(throwable);
    }

    @Test
    public void getVisitContext() throws Exception {
        when(THSVisitContext.getVisitContext()).thenReturn(visitContext);
        when(THSProviderInfo.getProviderInfo()).thenReturn(providerInfo);
        when(THSConsumer.getConsumer()).thenReturn(consumerMock);
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(fragmentActivity);
        when(awsdk.getVisitManager()).thenReturn(visitManagerMock);
        THSSymptomsPresenter.getVisitContext();
        verify(visitManagerMock).getVisitContext(any(Consumer.class),any(ProviderInfo.class),any(SDKCallback.class));
    }

    @Test
    public void getVisitContextThrowsMalformedURLException() throws Exception {
        when(THSVisitContext.getVisitContext()).thenReturn(visitContext);
        when(THSProviderInfo.getProviderInfo()).thenReturn(providerInfo);
        when(THSConsumer.getConsumer()).thenReturn(consumerMock);
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(fragmentActivity);
        when(awsdk.getVisitManager()).thenReturn(visitManagerMock);
        doThrow(MalformedURLException.class).when(awsdk).getVisitManager();
        THSSymptomsPresenter.getVisitContext();
        verifyNoMoreInteractions(visitManagerMock);
    }

    @Test
    public void getVisitContextThrowsURISyntaxException() throws Exception {
        when(THSVisitContext.getVisitContext()).thenReturn(visitContext);
        when(THSProviderInfo.getProviderInfo()).thenReturn(providerInfo);
        when(THSConsumer.getConsumer()).thenReturn(consumerMock);
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(fragmentActivity);
        when(awsdk.getVisitManager()).thenReturn(visitManagerMock);
        doThrow(URISyntaxException.class).when(awsdk).getVisitManager();
        THSSymptomsPresenter.getVisitContext();
        verifyNoMoreInteractions(visitManagerMock);
    }

    @Test
    public void getVisitContextThrowsAWSDKInstantiationException() throws Exception {
        when(THSVisitContext.getVisitContext()).thenReturn(visitContext);
        when(THSProviderInfo.getProviderInfo()).thenReturn(providerInfo);
        when(THSConsumer.getConsumer()).thenReturn(consumerMock);
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(fragmentActivity);
        when(awsdk.getVisitManager()).thenReturn(visitManagerMock);
        doThrow(AWSDKInstantiationException.class).when(awsdk).getVisitManager();
        THSSymptomsPresenter.getVisitContext();
        verifyNoMoreInteractions(visitManagerMock);
    }

    @Test
    public void getVisitContextThrowsAWSDKInitializationException() throws Exception {
        when(THSVisitContext.getVisitContext()).thenReturn(visitContext);
        when(THSProviderInfo.getProviderInfo()).thenReturn(providerInfo);
        when(THSConsumer.getConsumer()).thenReturn(consumerMock);
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(fragmentActivity);
        when(awsdk.getVisitManager()).thenReturn(visitManagerMock);
        doThrow(AWSDKInitializationException.class).when(awsdk).getVisitManager();
        THSSymptomsPresenter.getVisitContext();
        verifyNoMoreInteractions(visitManagerMock);
    }

}