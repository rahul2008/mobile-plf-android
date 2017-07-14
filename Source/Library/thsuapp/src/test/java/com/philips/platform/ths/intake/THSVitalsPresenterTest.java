package com.philips.platform.ths.intake;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.entity.visit.Vitals;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKValidatedCallback;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
    THSConsumer pthConsumerMock;

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
    Map<String, ValidationReason> mapMock;

    @Mock
    SDKError sdkErrorMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        thsVitalsPresenter = new THSVitalsPresenter(pTHBaseViewMock);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(pthConsumerMock);

        when(pthConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(activityMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);

        THSManager.getInstance().setVisitContext(pthVisitContextMock);
        when(pthVisitContextMock.getVisitContext()).thenReturn(visitContextMock);
    }

    @Test
    public void onEventContinueBtnWhenInputIsInvalid() throws Exception {
        thsVitalsPresenter.onEvent(R.id.vitals_continue_btn);
        verifyNoMoreInteractions(awsdkMock);
    }

    @Test
    public void onEventSkipBtn() throws Exception {
        thsVitalsPresenter.onEvent(R.id.vitals_skip);
        verify(pTHBaseViewMock).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class));
    }
    @Test
    public void onEventInvalidBtn() throws Exception {
        thsVitalsPresenter.onEvent(R.id.view_having_problem);
        verifyNoMoreInteractions(pTHBaseViewMock);
    }

    @Test
    public void onEventContinueBtnWhenInputIsValidUpdateConditionsThrowsException() throws Exception {
        doThrow(AWSDKInstantiationException.class).when(consumerManagerMock).updateVitals(any(Consumer.class),any(Vitals.class),any(VisitContext.class),any(SDKValidatedCallback.class));;
        when(pTHBaseViewMock.getTHSVitals()).thenReturn(thsVitals);
        when(pTHBaseViewMock.validate()).thenReturn(true);
        thsVitalsPresenter.onEvent(R.id.vitals_continue_btn);
        verify(consumerManagerMock).updateVitals(any(Consumer.class),any(Vitals.class),any(VisitContext.class),any(SDKValidatedCallback.class));
        verify(pTHBaseViewMock).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class));
    }

    @Test
    public void onEventContinueBtnWhenInputIsValid() throws Exception {
        when(pTHBaseViewMock.getTHSVitals()).thenReturn(thsVitals);
        when(pTHBaseViewMock.validate()).thenReturn(true);
        thsVitalsPresenter.onEvent(R.id.vitals_continue_btn);
        verify(consumerManagerMock).updateVitals(any(Consumer.class),any(Vitals.class),any(VisitContext.class),any(SDKValidatedCallback.class));
    }

    @Test
    public void getVitals() throws Exception {

    }

    @Test
    public void onResponse() throws Exception {
        thsVitalsPresenter.onResponse(thsVitals,pthsdkError);
        verify(pTHBaseViewMock).updateUI(thsVitals);
    }

    @Test
    public void onFailure() throws Exception {
        thsVitalsPresenter.onFailure(throwableMock);
    }

    @Test
    public void onUpdateVitalsValidationFailure() throws Exception {
        thsVitalsPresenter.onUpdateVitalsValidationFailure(mapMock);
        verify(pTHBaseViewMock).showToast(anyString());
    }

    @Test
    public void onUpdateVitalsResponse() throws Exception {
        thsVitalsPresenter.onUpdateVitalsResponse(sdkErrorMock);
        verify(pTHBaseViewMock).showToast(anyString());
    }

    @Test
    public void onUpdateVitalsResponseSDKErrorNull() throws Exception {
        thsVitalsPresenter.onUpdateVitalsResponse(null);
        verify(pTHBaseViewMock).showToast(anyString());
    }

    @Test
    public void onUpdateVitalsFailure() throws Exception {
        thsVitalsPresenter.onUpdateVitalsFailure(throwableMock);
        verify(pTHBaseViewMock).showToast(anyString());
    }

    @Test
    public void stringToInteger() throws Exception {
        int number = thsVitalsPresenter.stringToInteger("20");
        assertNotNull(number);
        assert number == 20;
    }

    @Test
    public void integerToString() throws Exception {
        String value = thsVitalsPresenter.integerToString(20);
        assertNotNull(value);
        assert value.equalsIgnoreCase("20");
    }

    @Test
    public void stringToDouble() throws Exception {
        Double aDouble = thsVitalsPresenter.stringToDouble("30.5");
        assertNotNull(aDouble);
        assert  aDouble == 30.5;
    }

    @Test
    public void doubleToString() throws Exception {
        String value = thsVitalsPresenter.doubleToString(30.5);
        assertNotNull(value);
        assert value.equalsIgnoreCase("30.5");
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

}