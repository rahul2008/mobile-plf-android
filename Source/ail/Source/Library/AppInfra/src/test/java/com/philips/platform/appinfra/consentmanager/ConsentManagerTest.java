package com.philips.platform.appinfra.consentmanager;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;
import com.philips.platform.pif.chi.datamodel.ConsentStates;

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
    private SampleHandler3 mSampleHandler3;

    private ConsentStatus mActiveConsentStatus;
    private ConsentStatus mRejectedConsentStatus;
    private ConsentStatus mInactiveConsentStatus;
    private ConsentError mConsentError;

    private ConsentDefinitionStatus mReceivedConsentDefinitionStatus;
    private List<ConsentDefinitionStatus> mReceivedConsentDefinitionStatusList;

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

    @Test
    public void fetchConsentTypeState_ShouldReturnStatus() {
        givenRegisteredConsent();
        givenRegisteredConsentDefinitions();
        whenFetchConsentTypeStateIsInvokedForSuccess();
        verifyActiveStatusIsReturned();
    }

    private void whenFetchConsentTypeStateIsInvokedForSuccess() {
        mConsentManager.fetchConsentTypeState("testConsent1", new FetchConsentCallbackListener());
    }

    private void givenRegisteredConsentDefinitions() {
        mConsentManager.registerConsentDefinitions(Collections.singletonList(new ConsentDefinition(0,0, Arrays.asList("testConsent1", "testConsent3"), 1)));
    }

    private void whenStoreConsentIsInvokedForFailureCase() {
        ConsentDefinition consentDefinition = new ConsentDefinition(0,0, Arrays.asList("testConsent1", "testConsent3"), 1);
        mConsentManager.storeConsentState(consentDefinition, true, mPostConsentCallback);
    }

    private void whenStoreConsentIsInvokedForSuccessCase() {
        ConsentDefinition consentDefinition = new ConsentDefinition(0,0, Arrays.asList("testConsent1", "testConsent5"), 1);
        mConsentManager.storeConsentState(consentDefinition, true, mPostConsentCallback);
    }

    private void verifyCorrectListIsReturned() {
        assertEquals(2, mReceivedConsentDefinitionStatusList.size());
    }

    private void whenFetchConsentStateIsInvokedForSuccessCase() {
        ConsentDefinition consentDefinition = new ConsentDefinition(0,0, Arrays.asList("testConsent1", "testConsent5"), 1);
        mConsentManager.fetchConsentState(consentDefinition, new FetchConsentCallbackListener());
    }

    private void verifyCorrectStatusIsReturned() {
        assertEquals(ConsentStates.rejected, mReceivedConsentDefinitionStatus.getConsentState());
    }

    private void verifyActiveStatusIsReturned() {
        assertEquals(ConsentStates.active, mReceivedConsentDefinitionStatus.getConsentState());
    }

    private void whenFetchConsentStateIsInvokedForFailureCase() {
        ConsentDefinition consentDefinition = new ConsentDefinition(0,0, Arrays.asList("testConsent1", "testConsent3"), 1);
        mConsentManager.fetchConsentState(consentDefinition, new FetchConsentCallbackListener());
    }

    private void verifyErrorIsReceived() {
        assertNotNull(mConsentError);
    }

    private void whenFetchConsentStatesIsInvokedForSuccessCase() {
        List<ConsentDefinition> consentDefinitionList = new ArrayList<>();
        consentDefinitionList.add(new ConsentDefinition(0,0, Arrays.asList("testConsent1", "testConsent5"), 1));
        consentDefinitionList.add(new ConsentDefinition(0,0, Arrays.asList("testConsent2", "testConsent6"), 1));
        mConsentManager.fetchConsentStates(consentDefinitionList, new FetchConsentsCallbackListener());
    }

    private void whenFetchConsentStatesIsInvokedForFailureCase() {
        List<ConsentDefinition> consentDefinitionList = new ArrayList<>();
        consentDefinitionList.add(new ConsentDefinition(0,0, Arrays.asList("testConsent1", "testConsent3"), 1));
        consentDefinitionList.add(new ConsentDefinition(0,0, Arrays.asList("testConsent2", "testConsent6"), 1));
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
        SampleHandler2 mSampleHandler2 = new SampleHandler2();
        mSampleHandler3 = new SampleHandler3();
        SampleHandler4 mSampleHandler4 = new SampleHandler4();
        mConsentManager.registerHandler(Arrays.asList("testConsent1", "testConsent2"), mSampleHandler1);
        mConsentManager.registerHandler(Arrays.asList("testConsent3", "testConsent4"), mSampleHandler2);
        mConsentManager.registerHandler(Collections.singletonList("testConsent5"), mSampleHandler3);
        mConsentManager.registerHandler(Collections.singletonList("testConsent6"), mSampleHandler4);
    }

    private void whenRegisteringDuplicateConsentType() {
        mConsentManager.registerHandler(Collections.singletonList("testConsent1"), mSampleHandler3);
    }

    private void whenDeRegisterConsentTypeIsInvoked() {
        mConsentManager.deregisterHandler(Collections.singletonList("testConsent1"));
    }

    private void givenConsentError() {
        mConsentError = new ConsentError("Sample Error", CONSENT_ERROR);
    }

    private void givenActiveConsentState() {
        mActiveConsentStatus = new ConsentStatus(ConsentStates.active, 1);
    }

    private void givenRejectedConsentState() {
        mRejectedConsentStatus = new ConsentStatus(ConsentStates.rejected, 2);
    }

    private void givenInactiveConsentState() {
        mInactiveConsentStatus = new ConsentStatus(ConsentStates.inactive, 0);
    }

    private class SampleHandler1 implements ConsentHandlerInterface {
        @Override
        public void fetchConsentTypeState(String consentType, FetchConsentTypeStateCallback callback) {
            givenActiveConsentState();
            callback.onGetConsentsSuccess(mActiveConsentStatus);
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
        public void storeConsentTypeState(String consentType, boolean status, int version, PostConsentTypeCallback callback) {
            givenConsentError();
            callback.onPostConsentFailed(mConsentError);
        }
    }

    private class SampleHandler3 implements ConsentHandlerInterface {

        @Override
        public void fetchConsentTypeState(String consentType, FetchConsentTypeStateCallback callback) {
            givenRejectedConsentState();
            callback.onGetConsentsSuccess(mRejectedConsentStatus);
        }

        @Override
        public void storeConsentTypeState(String consentType, boolean status, int version, PostConsentTypeCallback callback) {
            callback.onPostConsentSuccess();
        }
    }

    private class SampleHandler4 implements ConsentHandlerInterface {
        @Override
        public void fetchConsentTypeState(String consentType, FetchConsentTypeStateCallback callback) {
            givenInactiveConsentState();
            callback.onGetConsentsSuccess(mInactiveConsentStatus);
        }

        @Override
        public void storeConsentTypeState(String consentType, boolean status, int version, PostConsentTypeCallback callback) {
            callback.onPostConsentSuccess();
        }
    }


    private class FetchConsentCallbackListener implements FetchConsentCallback {

        @Override
        public void onGetConsentSuccess(ConsentDefinitionStatus consentDefinitionStatus) {
            mReceivedConsentDefinitionStatus = consentDefinitionStatus;
        }

        @Override
        public void onGetConsentFailed(ConsentError error) {
            mConsentError = error;
        }
    }

    private class FetchConsentsCallbackListener implements FetchConsentsCallback {

        @Override
        public void onGetConsentsSuccess(List<ConsentDefinitionStatus> consentDefinitionStatusList) {
            mReceivedConsentDefinitionStatusList = consentDefinitionStatusList;
        }

        @Override
        public void onGetConsentsFailed(ConsentError error) {
            mConsentError = error;
        }
    }
}