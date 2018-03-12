package com.philips.platform.appinfra.consentmanager;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.FetchConsentTypesStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionState;
import com.philips.platform.pif.chi.datamodel.ConsentState;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.philips.platform.pif.chi.ConsentError.CONSENT_ERROR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

public class ConsentManagerTest {
    private static ConsentManager mConsentManager;
    private ConsentHandlerInterface mReceivedConsentHandlerInterface;

    private SampleHandler1 mSampleHandler1;
    private SampleHandler2 mSampleHandler2;
    private SampleHandler3 mSampleHandler3;

    private ConsentState mActiveConsentState;
    private ConsentState mRejectedConsentState;
    private ConsentError mConsentError;

    private ConsentDefinitionState mReceivedConsentDefinitionState;
    private List<ConsentDefinitionState> mReceivedConsentDefinitionStateList;

    @Mock
    private AppInfra appInfra;

    @Mock
    private PostConsentCallback mPostConsentCallback;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mConsentManager = new ConsentManager(appInfra);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        mConsentManager = null;
    }

    @Test(expected = RuntimeException.class)
    public void register_shouldThrowExceptionOnSameType() {
        givenRegisteredConsent();
        whenRegisteringDuplicateConsentType();
    }

    @Test(expected = RuntimeException.class)
    public void deregister_ShouldRemoveConsentTypeLinked() {
        givenRegisteredConsent();
        whenDeRegisterConsentTypeIsInvoked();
        verifyExceptionThrownOnGetHandlerForTheRemovedConsentType();
    }

    @Test
    public void getHandler_ShouldReturnRegisteredHandler() {
        givenRegisteredConsent();
        whenGetHandlerIsInvokedForTheRegisteredConsentType();
        verifyRegisteredHandlerIsReturned();
    }

    @Test
    public void fetchConsentState_ShouldReturnSuccessWithCorrectConsentStatus() {
        givenRegisteredConsent();
        whenFetchConsentStateIsInvokedForSuccessCase();
        verifyCorrectStatusIsReturned();
    }

    @Test
    public void fetchConsentState_ShouldReturnFailureWhenHandlerReturnsFailure() {
        givenRegisteredConsent();
        whenFetchConsentStateIsInvokedForFailureCase();
        verifyErrorIsReceived();
    }

    @Test
    public void fetchConsentStates_ShouldReturnSuccessWithCorrectStatus() {
        givenRegisteredConsent();
        whenFetchConsentStatesIsInvokedForSuccessCase();
        verifyCorrectListIsReturned();
    }

    @Test
    public void fetchConsentStates_ShouldReturnFailure() {
        givenRegisteredConsent();
        whenFetchConsentStatesIsInvokedForFailureCase();
        verifyErrorIsReceived();
    }

    @Test
    public void storeConsentState_ShouldReturnSuccess() {
        givenRegisteredConsent();
        whenStoreConsentIsInvokedForSuccessCase();
        verify(mPostConsentCallback).onPostConsentSuccess();
    }

    @Test
    public void storeConsentState_ShouldReturnFailure() {
        givenRegisteredConsent();
        whenStoreConsentIsInvokedForFailureCase();
        verify(mPostConsentCallback).onPostConsentFailed(mConsentError);
    }

    private void whenStoreConsentIsInvokedForFailureCase() {
        ConsentDefinition consentDefinition = new ConsentDefinition("text", "help", Arrays.asList("testConsent1", "testConsent3"), 1);
        mConsentManager.storeConsentState(consentDefinition, true, mPostConsentCallback);
    }

    private void whenStoreConsentIsInvokedForSuccessCase() {
        ConsentDefinition consentDefinition = new ConsentDefinition("text", "help", Arrays.asList("testConsent1", "testConsent5"), 1);
        mConsentManager.storeConsentState(consentDefinition, true, mPostConsentCallback);
    }

    private void verifyCorrectListIsReturned() {
        assertEquals(2, mReceivedConsentDefinitionStateList.size());
    }

    private void whenFetchConsentStateIsInvokedForSuccessCase() {
        ConsentDefinition consentDefinition = new ConsentDefinition("text", "help", Arrays.asList("testConsent1", "testConsent5"), 1);
        mConsentManager.fetchConsentState(consentDefinition, new FetchConsentCallbackListener());
    }

    private void verifyCorrectStatusIsReturned() {
        assertEquals(ConsentStatus.rejected, mReceivedConsentDefinitionState.getConsentStatus());
    }

    private void whenFetchConsentStateIsInvokedForFailureCase() {
        ConsentDefinition consentDefinition = new ConsentDefinition("text", "help", Arrays.asList("testConsent1", "testConsent3"), 1);
        mConsentManager.fetchConsentState(consentDefinition, new FetchConsentCallbackListener());
    }

    private void verifyErrorIsReceived() {
        assertNotNull(mConsentError);
    }

    private void whenFetchConsentStatesIsInvokedForSuccessCase() {
        List<ConsentDefinition> consentDefinitionList = new ArrayList<>();
        consentDefinitionList.add(new ConsentDefinition("text", "help", Arrays.asList("testConsent1", "testConsent5"), 1));
        consentDefinitionList.add(new ConsentDefinition("text", "help", Arrays.asList("testConsent2", "testConsent6"), 1));
        mConsentManager.fetchConsentStates(consentDefinitionList, new FetchConsentsCallbackListener());
    }

    private void whenFetchConsentStatesIsInvokedForFailureCase() {
        List<ConsentDefinition> consentDefinitionList = new ArrayList<>();
        consentDefinitionList.add(new ConsentDefinition("text", "help", Arrays.asList("testConsent1", "testConsent3"), 1));
        consentDefinitionList.add(new ConsentDefinition("text", "help", Arrays.asList("testConsent2", "testConsent6"), 1));
        mConsentManager.fetchConsentStates(consentDefinitionList, new FetchConsentsCallbackListener());
    }

    private void verifyRegisteredHandlerIsReturned() {
        assertEquals(mReceivedConsentHandlerInterface, mSampleHandler1);
    }

    private void whenGetHandlerIsInvokedForTheRegisteredConsentType() {
        mReceivedConsentHandlerInterface = mConsentManager.getHandler("testConsent1");
    }

    private void verifyExceptionThrownOnGetHandlerForTheRemovedConsentType() {
        mConsentManager.getHandler("testConsent1");
    }

    private void givenRegisteredConsent() {
        mSampleHandler1 = new SampleHandler1();
        mSampleHandler2 = new SampleHandler2();
        mSampleHandler3 = new SampleHandler3();
        mConsentManager.register(Arrays.asList("testConsent1", "testConsent2"), mSampleHandler1);
        mConsentManager.register(Arrays.asList("testConsent3", "testConsent4"), mSampleHandler2);
        mConsentManager.register(Arrays.asList("testConsent5", "testConsent6"), mSampleHandler3);
    }

    private void whenRegisteringDuplicateConsentType() {
        mConsentManager.register(Collections.singletonList("testConsent1"), mSampleHandler1);
    }

    private void whenDeRegisterConsentTypeIsInvoked() {
        mConsentManager.deregister(Collections.singletonList("testConsent1"));
    }

    private void givenConsentError() {
        mConsentError = new ConsentError("Sample Error", CONSENT_ERROR);
    }

    private void givenActiveConsentState() {
        mActiveConsentState = new ConsentState(ConsentStatus.active, 1);
    }

    private void givenRejectedConsentState() {
        mRejectedConsentState = new ConsentState(ConsentStatus.rejected, 1);
    }

    private class SampleHandler1 implements ConsentHandlerInterface {
        @Override
        public void fetchConsentTypeState(String consentType, FetchConsentTypeStateCallback callback) {
            givenActiveConsentState();
            callback.onGetConsentsSuccess(mActiveConsentState);
        }

        @Override
        public void fetchConsentTypeStates(List<String> consentTypes, FetchConsentTypesStateCallback callback) {
//            callback.onGetConsentsSuccess();
        }

        @Override
        public void storeConsentTypeState(String consentType, boolean status, int version, PostConsentTypeCallback callback) {
            callback.onPostConsentSuccess();
        }
    }

    private class SampleHandler2 implements ConsentHandlerInterface {
        @Override
        public void fetchConsentTypeState(String consentType, FetchConsentTypeStateCallback callback) {
            givenConsentError();
            callback.onGetConsentsFailed(mConsentError);
        }

        @Override
        public void fetchConsentTypeStates(List<String> consentTypes, FetchConsentTypesStateCallback callback) {
            givenConsentError();
            callback.onGetConsentsFailed(mConsentError);
        }

        @Override
        public void storeConsentTypeState(String consentType, boolean status, int version, PostConsentTypeCallback callback) {
            givenConsentError();
            callback.onPostConsentFailed(mConsentError);
        }
    }

    private class SampleHandler3 implements ConsentHandlerInterface {

        @Override
        public void fetchConsentTypeState(String consentType, FetchConsentTypeStateCallback callback) {
            givenRejectedConsentState();
            callback.onGetConsentsSuccess(mRejectedConsentState);
        }

        @Override
        public void fetchConsentTypeStates(List<String> consentTypes, FetchConsentTypesStateCallback callback) {

        }

        @Override
        public void storeConsentTypeState(String consentType, boolean status, int version, PostConsentTypeCallback callback) {
            callback.onPostConsentSuccess();
        }
    }

    private class FetchConsentCallbackListener implements FetchConsentCallback {

        @Override
        public void onGetConsentsSuccess(ConsentDefinitionState consentDefinitionState) {
            mReceivedConsentDefinitionState = consentDefinitionState;
        }

        @Override
        public void onGetConsentsFailed(ConsentError error) {
            mConsentError = error;
        }
    }

    private class FetchConsentsCallbackListener implements FetchConsentsCallback {

        @Override
        public void onGetConsentsSuccess(List<ConsentDefinitionState> consentDefinitionStateList) {
            mReceivedConsentDefinitionStateList = consentDefinitionStateList;
        }

        @Override
        public void onGetConsentsFailed(ConsentError error) {
            mConsentError = error;
        }
    }
}