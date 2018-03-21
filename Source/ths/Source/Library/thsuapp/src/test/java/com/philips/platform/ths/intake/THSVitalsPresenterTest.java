package com.philips.platform.ths.intake;

import android.support.v4.app.FragmentActivity;
import android.text.Editable;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.entity.visit.Vitals;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.americanwell.sdk.manager.SDKValidatedCallback;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.R;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSVitalsPresenterTest {

    THSVitalsPresenter thsVitalsPresenter;

    @Mock
    EditText editTextMock;

    @Mock
    Editable editableMock;

    @Mock
    THSVitalsFragment pTHBaseViewMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    THSConsumerWrapper pthConsumerMock;

    @Mock
    Consumer consumerMock;

    @Mock
    FragmentActivity activityMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    THSVisitContext pthVisitContextMock;

    @Mock
    VisitContext visitContextMock;

    @Mock
    THSVitals thsVitals;

    @Mock
    THSSDKError pthsdkError;

    @Mock
    Throwable throwableMock;

    @Mock
    Map<String, String> mapMock;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    THSVItalsUIInterface thsvItalsUIInterface;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    THSProviderInfo pthProviderInfoMock;

    @Mock
    ProviderInfo providerInfo;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        thsVitalsPresenter = new THSVitalsPresenter(thsvItalsUIInterface, pTHBaseViewMock);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(pthConsumerMock);
        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);
        when(pthConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(activityMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);

        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        THSManager.getInstance().setThsParentConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);

        when(pthProviderInfoMock.getProviderInfo()).thenReturn(providerInfo);

        THSManager.getInstance().setVisitContext(pthVisitContextMock);
        when(pthVisitContextMock.getVisitContext()).thenReturn(visitContextMock);
        when(pTHBaseViewMock.isFragmentAttached()).thenReturn(true);
    }

    @Test
    public void onEventSkipBtn() throws Exception {
        thsVitalsPresenter.onEvent(R.id.vitals_skip);
        verify(thsvItalsUIInterface).launchMedicationFragment();
    }

    @Test
    public void onEventContinueBtnWhenInputIsValidUpdateConditionsThrowsException() throws Exception {
        doThrow(AWSDKInstantiationException.class).when(consumerManagerMock).updateVitals(any(Consumer.class), any(Vitals.class), any(VisitContext.class), any(SDKValidatedCallback.class));
        when(thsvItalsUIInterface.getTHSVitals()).thenReturn(thsVitals);
        thsVitalsPresenter.onEvent(R.id.vitals_continue_btn);
        verify(consumerManagerMock).updateVitals(any(Consumer.class), (Vitals)isNull(), (VisitContext)isNull(), any(SDKValidatedCallback.class));
    }

    @Test
    public void getVitals() throws Exception {
        thsVitalsPresenter.getVitals();
        verify(consumerManagerMock).getVitals(any(Consumer.class), any(VisitContext.class), any(SDKCallback.class));
    }

    @Test
    public void onResponse() throws Exception {
        thsVitalsPresenter.onResponse(thsVitals, pthsdkError);
        verify(thsvItalsUIInterface).updateUI(thsVitals);
    }

    @Test
    public void onFailure() throws Exception {
        when(pTHBaseViewMock.isFragmentAttached()).thenReturn(false);
        thsVitalsPresenter.onFailure(throwableMock);
    }

    @Test
    public void onUpdateVitalsValidationFailure() throws Exception {
        thsVitalsPresenter.onUpdateVitalsValidationFailure(mapMock);
        verify(pTHBaseViewMock).showError(anyString());
    }

    @Test
    public void onUpdateVitalsResponse() throws Exception {
        sdkErrorMock = null;
        when(pTHBaseViewMock.isFragmentAttached()).thenReturn(true);
        thsVitalsPresenter.onUpdateVitalsResponse(sdkErrorMock);
        verify(thsvItalsUIInterface).launchMedicationFragment();
    }

    @Test
    public void onUpdateVitalsResponseSDKErrorNull() throws Exception {
        thsVitalsPresenter.onUpdateVitalsResponse(null);
//        verify(pTHBaseViewMock).showError(anyString());
    }

    @Test
    public void onUpdateVitalsFailure() throws Exception {
        when(pTHBaseViewMock.isFragmentAttached()).thenReturn(false);
        thsVitalsPresenter.onUpdateVitalsFailure(throwableMock);
//        verifyNoMoreInteractions(pTHBaseViewMock);
    }

    @Test
    public void stringToInteger() throws Exception {
        int number = thsVitalsPresenter.stringToInteger("20");
        assertNotNull(number);
        assert number == 20;
    }


    @Test
    public void stringToDouble() throws Exception {
        Double aDouble = thsVitalsPresenter.stringToDouble("30.5");
        assertNotNull(aDouble);
        assert aDouble == 30.5;
    }


    @Test
    public void isTextValid() throws Exception {
        when(editTextMock.getText()).thenReturn(editableMock);
        when(editableMock.toString()).thenReturn("spoorti");
        boolean isValid = thsVitalsPresenter.isTextValid(editTextMock);
        assertNotNull(isValid);
        assertTrue(isValid);
    }

    @Test
    public void isTextValidTextEmpty() throws Exception {
        when(editTextMock.getText()).thenReturn(editableMock);
        when(editableMock.toString()).thenReturn("");
        boolean isValid = thsVitalsPresenter.isTextValid(editTextMock);
        assertNotNull(isValid);
        assertFalse(isValid);
    }

    @Test
    public void getTextFromEditText() throws Exception {
        when(editTextMock.getText()).thenReturn(editableMock);
        when(editableMock.toString()).thenReturn("spoorti");
        String text = thsVitalsPresenter.getTextFromEditText(editTextMock);
        assertNotNull(text);
        assert text.equalsIgnoreCase("Spoorti");
    }

    @Test
    public void checkIfValueEntered() {
        when(editTextMock.getText()).thenReturn(editableMock);
        when(editableMock.length()).thenReturn(0);
        boolean isvalue = thsVitalsPresenter.checkIfValueEntered(editTextMock);
        assertFalse(!isvalue);
    }

    @Test
    public void checkIfValueEnteredIsValidTrue() {
        when(editTextMock.getText()).thenReturn(editableMock);
        when(editableMock.length()).thenReturn(1);
        boolean isvalue = thsVitalsPresenter.checkIfValueEntered(editTextMock);
        assertTrue(isvalue);
    }
}