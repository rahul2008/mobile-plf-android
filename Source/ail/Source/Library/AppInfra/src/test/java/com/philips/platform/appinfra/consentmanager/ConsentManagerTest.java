package com.philips.platform.appinfra.consentmanager;

import android.os.Looper;

import com.google.common.collect.ImmutableList;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;
import com.philips.platform.pif.chi.datamodel.ConsentVersionStates;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Looper.class, ConsentManager.class})
public class ConsentManagerTest {
    private ConsentManager consentManager;
    private ConsentError error = new ConsentError("", 1);
    private CountDownLatch lock = new CountDownLatch(1);
    private ConsentHandlerInterfaceSpy handler1 = new ConsentHandlerInterfaceSpy();
    private ConsentHandlerInterfaceSpy handler2 = new ConsentHandlerInterfaceSpy();
    private ConsentHandlerInterfaceSpy handler3 = new ConsentHandlerInterfaceSpy();

    private ConsentDefinitionStatus returnedConsentStatus;
    private ConsentHandlerInterface returnedHandler;
    private ConsentError returnedError;
    private boolean isPostConsentCallbackInvoked;
    private List<ConsentDefinitionStatus> returnedConsentsStatus;

    @Mock
    private AppInfra appInfra;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        consentManager = new ConsentManager(appInfra);
        AndroidMockUtil.mockMainThreadHandler();
    }

    @After
    public void tearDown() {
        returnedError = null;
        returnedConsentsStatus = null;
    }

    @Test
    public void getHandler_ShouldReturnRegisteredHandler() {
        givenHandler(handler1, "testConsent");
        givenHandler(handler2, "testConsent2");
        whenGettingConsentHandlerForType("testConsent");
        thenHandlerIsReturned(handler1);
    }

    @Test(expected = RuntimeException.class)
    public void register_shouldThrowExceptionOnSameType() {
        givenHandler(handler1, "testConsent");
        givenHandler(handler1, "testConsent");
    }

    @Test(expected = RuntimeException.class)
    public void deregister_ShouldRemoveConsentTypeLinked() {
        givenHandler(handler1, "testConsent");
        whenDeRegisterConsentTypeIsInvoked("testConsent");
        whenGettingConsentHandlerForType("testConsent");
    }

    @Test
    public void storeConsentState_SingleConsentTypeInConsentDefinition() {
        givenHandler(handler1, "testConsent");
        givenRegisteredConsentDefinitions(1, "testConsent");
        whenStoringConsents(consentDefinition(1, "testConsent"), true);
        thenConsentIsStored(handler1, 1, "testConsent", true);
        thenPostConsentSuccessCallbackIsInvoked(true);
    }

    @Test
    public void storeConsentState_MultipleConsentTypeInConsentDefinition() {
        givenHandler(handler1, "testConsent1");
        givenHandler(handler2, "testConsent2");
        givenRegisteredConsentDefinitions(1, "testConsent1", "testConsent2");
        whenStoringConsents(consentDefinition(1, "testConsent1", "testConsent2"), true);
        thenConsentIsStored(handler1, 1, "testConsent1", true);
        thenConsentIsStored(handler2, 1, "testConsent2", true);
        thenPostConsentSuccessCallbackIsInvoked(true);
    }

    @Test
    public void storeConsentState_ShouldReturnError_WhenStoringConsentFails() {
        givenHandler(handler1, "testConsent");
        givenHandlerReturnsError(handler1, error);
        givenRegisteredConsentDefinitions(1, "testConsent");
        whenStoringConsents(consentDefinition(1, "testConsent"), true);
        thenErrorIsReturned(error);
        thenPostConsentSuccessCallbackIsInvoked(false);
    }

    @Test
    public void fetchConsentDefinitionState_ShouldReturnSuccessWithCorrectConsentStatus() {
        givenHandler(handler1, "testConsent");
        givenHandlerReturns(handler1, ConsentStates.active, 1);
        whenFetchingConsentDefinitionState(consentDefinition(1, "testConsent"));
        thenHandlerIsInvoked(handler1, "testConsent");
        thenReturnedStatusIs(ConsentStates.active);
    }

    @Test
    public void fetchConsentDefinitionState_CallsAllCorrectHandlers() {
        givenHandler(handler1, "testConsent1");
        givenHandler(handler2, "testConsent2");
        givenHandler(handler3, "testConsent3");
        givenHandlerReturns(handler1, ConsentStates.active, 1);
        givenHandlerReturns(handler2, ConsentStates.active, 1);
        givenHandlerReturns(handler3, ConsentStates.active, 1);
        whenFetchingConsentDefinitionState(consentDefinition(1, "testConsent1", "testConsent2", "testConsent3"));
        thenHandlerIsInvoked(handler1, "testConsent1");
        thenHandlerIsInvoked(handler2, "testConsent2");
        thenHandlerIsInvoked(handler3, "testConsent3");
        thenReturnedStatusIs(ConsentStates.active);
    }

    @Test
    public void fetchConsentDefinitionState_ShouldReturnActive_WhenAllHandlersReturnActive() {
        givenHandler(handler1, "testConsent1");
        givenHandler(handler2, "testConsent2");
        givenHandlerReturns(handler1, ConsentStates.active, 1);
        givenHandlerReturns(handler2, ConsentStates.active, 1);
        whenFetchingConsentDefinitionState(consentDefinition(1, "testConsent1", "testConsent2"));
        thenReturnedStatusIs(ConsentStates.active);
    }

    @Test
    public void fetchConsentDefinitionState_ShouldReturnInactive_WhenOneOfTheConsentTypesIsInactive() {
        givenHandler(handler1, "testConsent1");
        givenHandler(handler2, "testConsent2");
        givenHandler(handler3, "testConsent3");
        givenHandlerReturns(handler1, ConsentStates.active, 1);
        givenHandlerReturns(handler2, ConsentStates.inactive, 1);
        givenHandlerReturns(handler3, ConsentStates.active, 1);
        whenFetchingConsentDefinitionState(consentDefinition(1, "testConsent1", "testConsent2", "testConsent3"));
        thenReturnedStatusIs(ConsentStates.inactive);
    }

    @Test
    public void fetchConsentDefinitionState_ShouldReturnRejected_WhenOneOfTheConsentTypesIsRejected() {
        givenHandler(handler1, "testConsent1");
        givenHandler(handler2, "testConsent2");
        givenHandler(handler3, "testConsent3");
        givenHandlerReturns(handler2, ConsentStates.inactive, 1);
        givenHandlerReturns(handler3, ConsentStates.rejected, 1);
        givenHandlerReturns(handler1, ConsentStates.active, 1);
        whenFetchingConsentDefinitionState(consentDefinition(1, "testConsent1", "testConsent2", "testConsent3"));
        thenReturnedStatusIs(ConsentStates.rejected);
    }

    @Test
    public void fetchConsentDefinitionState_ShouldReturnRejected_WhenOneOfTheConsentTypesIsRejected_OrderingOfReturnedStatusesHasSameResult() {
        givenHandler(handler1, "testConsent1");
        givenHandler(handler2, "testConsent2");
        givenHandler(handler3, "testConsent3");
        givenHandlerReturns(handler1, ConsentStates.rejected, 1);
        givenHandlerReturns(handler2, ConsentStates.inactive, 1);
        givenHandlerReturns(handler3, ConsentStates.active, 1);
        whenFetchingConsentDefinitionState(consentDefinition(1, "testConsent1", "testConsent2", "testConsent3"));
        thenReturnedStatusIs(ConsentStates.rejected);
    }

    @Test
    public void fetchConsentDefinitionState_ShouldReturn_AppVersionHigher_WhenOneOfBackendConsentsIsLower() {
        givenHandler(handler1, "testConsent1");
        givenHandler(handler2, "testConsent2");
        givenHandlerReturns(handler1, ConsentStates.active, 1);
        givenHandlerReturns(handler2, ConsentStates.active, 0);
        whenFetchingConsentDefinitionState(consentDefinition(1, "testConsent1", "testConsent2"));
        thenReturnedVersionStatusIs(ConsentVersionStates.AppVersionIsHigher);
    }

    @Test
    public void fetchConsentDefinitionState_ShouldReturn_AppVersionHigher_WhenOneOfBackendConsentsIsLower_OrderingOfReturnedStatusesHasSameResult() {
        givenHandler(handler1, "testConsent1");
        givenHandler(handler2, "testConsent2");
        givenHandlerReturns(handler1, ConsentStates.active, 0);
        givenHandlerReturns(handler2, ConsentStates.active, 1);
        whenFetchingConsentDefinitionState(consentDefinition(1, "testConsent1", "testConsent2"));
        thenReturnedVersionStatusIs(ConsentVersionStates.AppVersionIsHigher);
    }

    @Test
    public void fetchConsentDefinitionState_ShouldReturnInactive_WhenOneOfTheHandlersReturnsALowerVersion() {
        givenHandler(handler1, "testConsent1");
        givenHandler(handler2, "testConsent2");
        givenHandlerReturns(handler1, ConsentStates.active, 1);
        givenHandlerReturns(handler2, ConsentStates.active, 0);
        whenFetchingConsentDefinitionState(consentDefinition(1, "testConsent1", "testConsent2"));
        thenReturnedVersionStatusIs(ConsentVersionStates.AppVersionIsHigher);
        thenReturnedStatusIs(ConsentStates.inactive);
    }

    @Test
    public void fetchConsentDefinitionState_ShouldReturnAppVersionLower_WhenOneOfBackendConsentsIsHigher() {
        givenHandler(handler1, "testConsent1");
        givenHandler(handler2, "testConsent2");
        givenHandlerReturns(handler1, ConsentStates.active, 0);
        givenHandlerReturns(handler2, ConsentStates.active, 1);
        whenFetchingConsentDefinitionState(consentDefinition(0, "testConsent1", "testConsent2"));
        thenReturnedVersionStatusIs(ConsentVersionStates.AppVersionIsLower);
    }

    @Test
    public void fetchConsentDefinitionState_ShouldReturnFailureWhenOneOfTheHandlersFails() {
        givenHandler(handler1, "testConsent1");
        givenHandler(handler2, "testConsent2");
        givenHandlerReturns(handler1, ConsentStates.rejected, 1);
        givenHandlerReturnsError(handler2, error);
        whenFetchingConsentDefinitionState(consentDefinition(1, "testConsent1", "testConsent2"));
        thenErrorIsReturned(error);
    }

    @Test
    public void fetchConsentTypeState_ShouldUseTheCorrectHandler() {
        givenHandler(handler1, "testConsent1");
        givenHandler(handler2, "testConsent2");
        givenHandlerReturns(handler1, ConsentStates.active, 1);
        givenHandlerReturns(handler2, ConsentStates.inactive, 1);
        givenRegisteredConsentDefinitions(1, "testConsent1");
        whenFetchingConsentTypeState("testConsent1");
        thenHandlerIsInvoked(handler1, "testConsent1");
        thenReturnedStatusIs(ConsentStates.active);
    }

    @Test
    public void fetchConsentTypeState_ShouldReturnBackendConsentStatus_WhenBackendIsSameVersionAsApp() {
        givenHandler(handler1, "testConsent");
        givenHandlerReturns(handler1, ConsentStates.active, 1);
        givenRegisteredConsentDefinitions(1, "testConsent");
        whenFetchingConsentTypeState("testConsent");
        thenReturnedStatusIs(ConsentStates.active);
    }

    @Test
    public void fetchConsentTypeState_ShouldReturnBackendVersion_WhenBackendVersionIsHigher() {
        givenHandler(handler1, "testConsent");
        givenHandlerReturns(handler1, ConsentStates.active, 2);
        givenRegisteredConsentDefinitions(1, "testConsent");
        whenFetchingConsentTypeState("testConsent");
        thenReturnedStatusIs(ConsentStates.active);
    }

    @Test
    public void fetchConsentTypeState_ShouldReturnInactive_WhenBackendVersionIsLower() {
        givenHandler(handler1, "testConsent");
        givenHandlerReturns(handler1, ConsentStates.active, 0);
        givenRegisteredConsentDefinitions(1, "testConsent");
        whenFetchingConsentTypeState("testConsent");
        thenReturnedStatusIs(ConsentStates.inactive);
    }

    @Test
    public void fetchConsentTypeState_ShouldReturnAppVersionHigher_WhenBackendVersionIsLower() {
        givenHandler(handler1, "testConsent");
        givenHandlerReturns(handler1, ConsentStates.active, 0);
        givenRegisteredConsentDefinitions(1, "testConsent");
        whenFetchingConsentTypeState("testConsent");
        thenReturnedVersionStatusIs(ConsentVersionStates.AppVersionIsHigher);
    }

    @Test
    public void fetchConsentTypeState_ShouldReturnAppVersionLower_WhenBackendVersionIsHigher() {
        givenHandler(handler1, "testConsent");
        givenHandlerReturns(handler1, ConsentStates.active, 2);
        givenRegisteredConsentDefinitions(1, "testConsent");
        whenFetchingConsentTypeState("testConsent");
        thenReturnedVersionStatusIs(ConsentVersionStates.AppVersionIsLower);
    }

    @Test
    public void fetchConsentTypeState_ShouldReturnInSync_WhenBackendVersionIsSame() {
        givenHandler(handler1, "testConsent");
        givenHandlerReturns(handler1, ConsentStates.active, 1);
        givenRegisteredConsentDefinitions(1, "testConsent");
        whenFetchingConsentTypeState("testConsent");
        thenReturnedVersionStatusIs(ConsentVersionStates.InSync);
    }

    @Test
    public void fetchConsentTypeState_ShouldReturnFailureWhenOneOfTheHandlersFails() {
        givenHandler(handler1, "testConsent");
        givenHandlerReturnsError(handler1, error);
        givenRegisteredConsentDefinitions(1, "testConsent");
        whenFetchingConsentTypeState("testConsent");
        thenErrorIsReturned(error);
    }

    @Test
    public void fetchConsentStates_ShouldReturnErrorIfTimesOut() {
        givenFetchTimeOutIs(3);
        givenHandler(handler1, "testConsent");
        givenFetchConsentDoesNotReturnForHandler(handler1);
        whenFetchConsentStates(ImmutableList.of(consentDefinition(1,"testConsent")));
        thenTimeoutErrorIsReturned();
    }

    private void givenFetchConsentDoesNotReturnForHandler(ConsentHandlerInterfaceSpy handler) {
        handler.fetchDoNothing = true;
    }

    private void givenFetchTimeOutIs(int timeout) {
        consentManager.timeout = timeout;
    }

    private void givenHandler(ConsentHandlerInterfaceSpy handler, String... consentTypes) {
        consentManager.registerHandler(Arrays.asList(consentTypes), handler);
    }

    private void givenHandlerReturns(ConsentHandlerInterfaceSpy handler, ConsentStates status, int version) {
        handler.returns = new ConsentStatus(status, version);
    }

    private void givenHandlerReturnsError(ConsentHandlerInterfaceSpy handler, ConsentError error) {
        handler.returnsError = error;
    }

    private void givenRegisteredConsentDefinitions(int version, String... consentTypes) {
        consentManager.registerConsentDefinitions(Collections.singletonList(consentDefinition(version, consentTypes)));
    }

    private void whenFetchingConsentTypeState(String consentType) {
        consentManager.fetchConsentTypeState(consentType, new FetchConsentCallbackListener());
        waitForThreadsToComplete();
    }

    private void whenStoringConsents(ConsentDefinition consentDefinition, boolean status) {
        consentManager.storeConsentState(consentDefinition, status, new PostConsentCallbackListener());
        waitForThreadsToComplete();
    }

    private void whenDeRegisterConsentTypeIsInvoked(String consentType) {
        consentManager.deregisterHandler(Collections.singletonList(consentType));
    }

    private void whenFetchingConsentDefinitionState(ConsentDefinition consentDefinition) {
        consentManager.fetchConsentState(consentDefinition, new FetchConsentCallbackListener());
        waitForThreadsToComplete();
    }

    private void whenFetchConsentStates(final List<ConsentDefinition> consentDefinitions) throws RuntimeException {
        consentManager.fetchConsentStates(consentDefinitions, new FetchConsentsCallBackListener());
        waitForThreadsToComplete();
    }

    private void thenReturnedStatusIs(ConsentStates expectedStatus) {
        assertEquals(expectedStatus, returnedConsentStatus.getConsentState());
    }

    private void thenReturnedVersionStatusIs(ConsentVersionStates expectedStatus) {
        assertEquals(expectedStatus, returnedConsentStatus.getConsentVersionState());
    }

    private void thenConsentIsStored(ConsentHandlerInterfaceSpy expectedHandler, int expectedVersion, String expectedConsentType, boolean expectedStatus) {
        assertEquals(expectedConsentType, expectedHandler.storeConsentTypeState_consentType);
        assertEquals(expectedVersion, expectedHandler.storeConsentTypeState_version);
        assertEquals(expectedStatus, expectedHandler.storeConsentTypeState_status);
    }

    private void thenErrorIsReturned(ConsentError error) {
        assertSame(error, returnedError);
    }

    private void thenHandlerIsInvoked(ConsentHandlerInterfaceSpy handler, String expectedConsentType) {
        assertEquals(expectedConsentType, handler.fetchConsentTypeState_consentType);
    }

    private void thenHandlerIsReturned(ConsentHandlerInterfaceSpy handler) {
        assertSame(handler, returnedHandler);
    }

    private void thenPostConsentSuccessCallbackIsInvoked(boolean isInvoked) {
        assertEquals(isInvoked, isPostConsentCallbackInvoked);
    }

    private void whenGettingConsentHandlerForType(String consentType) {
        returnedHandler = consentManager.getHandler(consentType);
    }

    private void thenTimeoutErrorIsReturned() {
        assertNotNull(returnedError);
        assertEquals(returnedError.getErrorCode(), ConsentError.CONSENT_ERROR_CONNECTION_TIME_OUT);
    }

    private void waitForThreadsToComplete() {
        try {
            lock.await();
        } catch (InterruptedException e) {
            fail("Error waiting for threads to finish");
        }
    }

    private ConsentDefinition consentDefinition(int version, String... strings) {
        return new ConsentDefinition(0, 0, Arrays.asList(strings), version);
    }

    private class ConsentHandlerInterfaceSpy implements ConsentHandlerInterface {
        String fetchConsentTypeState_consentType;
        String storeConsentTypeState_consentType;
        int storeConsentTypeState_version;
        boolean storeConsentTypeState_status;
        ConsentStatus returns;
        ConsentError returnsError;
        boolean fetchDoNothing;

        @Override
        public void fetchConsentTypeState(String consentType, FetchConsentTypeStateCallback callback) {
            if (fetchDoNothing) {
                return;
            }

            fetchConsentTypeState_consentType = consentType;
            if (returnsError != null) {
                callback.onGetConsentsFailed(returnsError);
            } else {
                callback.onGetConsentsSuccess(returns);
            }
        }

        @Override
        public void storeConsentTypeState(String consentType, boolean status, int version, PostConsentTypeCallback callback) {
            if (returnsError != null) {
                callback.onPostConsentFailed(returnsError);
            } else {
                storeConsentTypeState_consentType = consentType;
                storeConsentTypeState_version = version;
                storeConsentTypeState_status = status;
                callback.onPostConsentSuccess();
            }
        }
    }

    private class FetchConsentCallbackListener implements FetchConsentCallback {

        @Override
        public void onGetConsentSuccess(ConsentDefinitionStatus consentDefinitionStatus) {
            returnedConsentStatus = consentDefinitionStatus;
            lock.countDown();
        }

        @Override
        public void onGetConsentFailed(ConsentError error) {
            returnedError = error;
            lock.countDown();
        }
    }

    private class PostConsentCallbackListener implements PostConsentCallback {

        @Override
        public void onPostConsentFailed(ConsentError error) {
            returnedError = error;
            lock.countDown();
        }

        @Override
        public void onPostConsentSuccess() {
            isPostConsentCallbackInvoked = true;
            lock.countDown();
        }
    }

    private class FetchConsentsCallBackListener implements FetchConsentsCallback {

        @Override
        public void onGetConsentsSuccess(List<ConsentDefinitionStatus> consentDefinitionStatusList) {
            returnedConsentsStatus = consentDefinitionStatusList;
            lock.countDown();
        }

        @Override
        public void onGetConsentsFailed(ConsentError error) {
            returnedError = error;
            lock.countDown();
        }
    }
}