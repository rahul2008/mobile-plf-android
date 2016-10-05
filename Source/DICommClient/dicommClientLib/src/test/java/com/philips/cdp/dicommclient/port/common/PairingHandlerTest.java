/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.cpp.ICPEventListener;
import com.philips.cdp.dicommclient.cpp.pairing.IPairingController;
import com.philips.cdp.dicommclient.cpp.pairing.PairingController;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.icpinterface.PairingService;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;
import com.philips.icpinterface.data.PairingEntitiyReference;
import com.philips.icpinterface.data.PairingInfo;
import com.philips.icpinterface.data.PairingRelationship;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, DICommClientWrapper.class, DiscoveryManager.class})
public class PairingHandlerTest {

    @Mock
    private NetworkNode networkNodeMock;

    @Mock
    private DICommAppliance diCommApplianceMock;

    @Mock
    private CppController cppControllerMock;

    @Mock
    private DiscoveryManager discoveryManagerMock;

    @Mock
    private PairingPort pairingPortMock;

    @Mock
    private PairingListener<DICommAppliance> pairingListenerMock;

    @Mock
    private PairingService pairingServiceMock;

    @Captor
    private ArgumentCaptor<DICommPortListener> pairingListenerCaptor;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Captor
    private ArgumentCaptor<PairingInfo> pairingInfoCaptor;

    @Captor
    private ArgumentCaptor<PairingEntitiyReference> pairingEntitiyReferenceCaptor;

    @Captor
    private ArgumentCaptor<PairingRelationship> pairingRelationshipCaptor;

    private final String APP_CPP_ID = "UUID";
    private final String APP_TYPE = "DEV";
    private final String DEVICE_TYPE = "Air";
    private final String NETWORK_CPP_ID = "NETWORK_CPP_ID";
    private final String USER_ID = "test-user";
    private final String ACCESS_TOKEN = "abc";
    private final String DEVICE_RELATIONSHIP_TYPE = "cph_device_association";
    private PairingHandler pairingHandler;
    private IPairingController pairingController;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(Log.class);

        DICommLog.disableLogging();
        PairingHandler.clear();

        pairingController = new PairingControllerForTest(cppControllerMock);

        when(diCommApplianceMock.getNetworkNode()).thenReturn(networkNodeMock);
        when(diCommApplianceMock.getPairingPort()).thenReturn(pairingPortMock);
        when(diCommApplianceMock.getDeviceType()).thenReturn(DEVICE_TYPE);

        when(cppControllerMock.getAppType()).thenReturn(APP_TYPE);
        when(cppControllerMock.getAppCppId()).thenReturn(APP_CPP_ID);
        when(cppControllerMock.isSignOn()).thenReturn(true);
        when(cppControllerMock.getPairingController()).thenReturn(pairingController);

        when(networkNodeMock.getCppId()).thenReturn(NETWORK_CPP_ID);

        mockStatic(DICommClientWrapper.class);
        when(DICommClientWrapper.getCloudController()).thenReturn(cppControllerMock);

        when(discoveryManagerMock.getApplianceByCppId(anyString())).thenReturn(diCommApplianceMock);
        mockStatic(DiscoveryManager.class);
        when(DiscoveryManager.getInstance()).thenReturn(discoveryManagerMock);

        pairingHandler = new PairingHandlerForTest(diCommApplianceMock, pairingListenerMock);
    }

    @Test
    public void testGenerateRandomSecretKeyNotNull() {
        PairingHandler manager = new PairingHandler<>(null, (PairingListener<DICommAppliance>) null);
        String randomKey = manager.generateRandomSecretKey();
        assertNotNull(randomKey);
    }

    @Test
    public void testGenerateRandomSecretKeyNotEqual() {
        PairingHandler manager = new PairingHandler<>(null, (PairingListener<DICommAppliance>) null);
        String randomKey = manager.generateRandomSecretKey();
        String randomKey1 = manager.generateRandomSecretKey();
        assertFalse(randomKey.equals(randomKey1));
    }

    @Test
    public void testGetDiffInDays() {
        long rl = PairingHandler.getDiffInDays(0L);
        assertTrue(rl != 0);
    }

    @Test
    public void whenParingIsStartedThenItIsTriggeredOnPairingPortWithProperAppTypeAndCPPId() throws Exception {
        pairingHandler.startPairing();
        verify(pairingPortMock).addPortListener(pairingListenerCaptor.capture());

        verify(pairingPortMock).triggerPairing(eq(APP_TYPE), eq(APP_CPP_ID), stringCaptor.capture());
    }

    @Test
    public void whenPairingPortReportsSuccessThenRelationshipIsAddedWithTheSecretKey() throws Exception {
        whenParingIsStartedThenItIsTriggeredOnPairingPortWithProperAppTypeAndCPPId();

        pairingListenerCaptor.getValue().onPortUpdate(pairingPortMock);

        verify(pairingServiceMock).addRelationshipRequest((PairingEntitiyReference) isNull(), any(PairingEntitiyReference.class), (PairingEntitiyReference) isNull(), any(PairingRelationship.class), pairingInfoCaptor.capture());
        assertEquals(stringCaptor.getValue(), pairingInfoCaptor.getValue().pairingInfoSecretKey);
    }

    @Test
    public void whenPairingPortReportsSuccessThenPairingServiceCommandIsPAIRING_ADD_RELATIONSHIP() throws Exception {
        whenParingIsStartedThenItIsTriggeredOnPairingPortWithProperAppTypeAndCPPId();

        pairingListenerCaptor.getValue().onPortUpdate(pairingPortMock);

        verify(pairingServiceMock).setPairingServiceCommand(Commands.PAIRING_ADD_RELATIONSHIP);
    }

    @Test
    public void whenPairingPortReportsSuccessThenRelationshipIsAddedWithTrusteeIsCurrentAppliance() throws Exception {
        whenParingIsStartedThenItIsTriggeredOnPairingPortWithProperAppTypeAndCPPId();

        pairingListenerCaptor.getValue().onPortUpdate(pairingPortMock);

        verify(pairingServiceMock).addRelationshipRequest((PairingEntitiyReference) isNull(), pairingEntitiyReferenceCaptor.capture(), (PairingEntitiyReference) isNull(), any(PairingRelationship.class), any(PairingInfo.class));
        assertEquals(DEVICE_TYPE, pairingEntitiyReferenceCaptor.getValue().entityRefType);
        assertEquals(NETWORK_CPP_ID, pairingEntitiyReferenceCaptor.getValue().entityRefId);
        assertEquals("cpp", pairingEntitiyReferenceCaptor.getValue().entityRefProvider);
    }

    @Test
    public void whenPairingPortReportsSuccessThenPairingDiCommRelationshipsIsSend() throws Exception {
        whenParingIsStartedThenItIsTriggeredOnPairingPortWithProperAppTypeAndCPPId();

        pairingListenerCaptor.getValue().onPortUpdate(pairingPortMock);

        verify(pairingServiceMock).addRelationshipRequest((PairingEntitiyReference) isNull(), pairingEntitiyReferenceCaptor.capture(), (PairingEntitiyReference) isNull(), pairingRelationshipCaptor.capture(), any(PairingInfo.class));
        assertEquals(PairingHandler.PAIRING_DI_COMM_RELATIONSHIP, pairingRelationshipCaptor.getValue().pairingRelationshipRelationType);
    }

    @Test
    public void whenPairingPortReportsSuccessThenPairingServiceCommandIsExecuted() throws Exception {
        whenParingIsStartedThenItIsTriggeredOnPairingPortWithProperAppTypeAndCPPId();

        pairingListenerCaptor.getValue().onPortUpdate(pairingPortMock);

        verify(pairingServiceMock).executeCommand();
    }

    @Test
    public void whenPairingPortReportsErrorThenRetryIsIssuedSilently() throws Exception {
        whenParingIsStartedThenItIsTriggeredOnPairingPortWithProperAppTypeAndCPPId();
        reset(pairingPortMock);

        pairingListenerCaptor.getValue().onPortError(pairingPortMock, Error.BADREQUEST, "");

        verify(pairingPortMock).triggerPairing(eq(APP_TYPE), eq(APP_CPP_ID), stringCaptor.capture());
        verify(pairingListenerMock, never()).onPairingFailed(diCommApplianceMock);
    }

    @Test
    public void whenPairingPortErrorLimitIsReachedThenErrorIsReported() throws Exception {
        whenParingIsStartedThenItIsTriggeredOnPairingPortWithProperAppTypeAndCPPId();
        reset(pairingPortMock);

        pairingListenerCaptor.getValue().onPortError(pairingPortMock, Error.BADREQUEST, "");
        pairingListenerCaptor.getValue().onPortError(pairingPortMock, Error.BADREQUEST, "");
        pairingListenerCaptor.getValue().onPortError(pairingPortMock, Error.BADREQUEST, "");
        pairingListenerCaptor.getValue().onPortError(pairingPortMock, Error.BADREQUEST, "");

        verify(pairingListenerMock).onPairingFailed(diCommApplianceMock);
    }

    @Test
    public void whenPairingIsCompletedThenAddNotifyRelationshipsIsSend() throws Exception {
        whenPairingPortReportsSuccessThenPairingServiceCommandIsExecuted();

        PairingService pairingService = mock(PairingService.class);
        when(pairingService.getAddRelationStatus()).thenReturn("completed");
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.SUCCESS, pairingService);

        verify(pairingServiceMock).addRelationshipRequest((PairingEntitiyReference) isNull(), pairingEntitiyReferenceCaptor.capture(), (PairingEntitiyReference) isNull(), pairingRelationshipCaptor.capture(), (PairingInfo) isNull());
        assertEquals(PairingHandler.PAIRING_NOTIFY_RELATIONSHIP, pairingRelationshipCaptor.getValue().pairingRelationshipRelationType);
    }

    @Test
    public void whenPairingHasFailedThenRetryIsIssuedSilently() throws Exception {
        whenPairingPortReportsSuccessThenPairingServiceCommandIsExecuted();
        reset(pairingPortMock);

        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, null);

        verify(pairingPortMock).triggerPairing(eq(APP_TYPE), eq(APP_CPP_ID), stringCaptor.capture());
        verify(pairingListenerMock, never()).onPairingFailed(diCommApplianceMock);
    }

    @Test
    public void whenPairingAddRelationshipErrorLimitIsReachedThenErrorIsReported() throws Exception {
        whenPairingPortReportsSuccessThenPairingServiceCommandIsExecuted();
        reset(pairingPortMock);

        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, null);
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, null);
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, null);
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, null);

        verify(pairingListenerMock).onPairingFailed(diCommApplianceMock);
    }

    @Test
    public void whenPairingHasStatusIsNotCompletedThenRetryIsIssuedSilently() throws Exception {
        whenPairingPortReportsSuccessThenPairingServiceCommandIsExecuted();
        reset(pairingPortMock);

        PairingService pairingService = mock(PairingService.class);
        when(pairingService.getAddRelationStatus()).thenReturn("failed");
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.SUCCESS, pairingService);

        verify(pairingPortMock).triggerPairing(eq(APP_TYPE), eq(APP_CPP_ID), stringCaptor.capture());
        verify(pairingListenerMock, never()).onPairingFailed(diCommApplianceMock);
    }

    @Test
    public void whenPairingAddRelationshipErrorLimitIsReachedThenErrorIsReported2() throws Exception {
        whenPairingPortReportsSuccessThenPairingServiceCommandIsExecuted();
        reset(pairingPortMock);

        PairingService pairingService = mock(PairingService.class);
        when(pairingService.getAddRelationStatus()).thenReturn("failed");
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, pairingService);
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, pairingService);
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, pairingService);
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, pairingService);

        verify(pairingListenerMock).onPairingFailed(diCommApplianceMock);
    }

    @Test
    public void whenNotifyIsCompletedThenPairingSuccessIsReported() throws Exception {
        whenPairingIsCompletedThenAddNotifyRelationshipsIsSend();

        PairingService pairingService = mock(PairingService.class);
        when(pairingService.getAddRelationStatus()).thenReturn("completed");
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.SUCCESS, pairingService);

        verify(pairingListenerMock).onPairingSuccess(diCommApplianceMock);
    }

    @Test
    public void whenNotifyIsFailedThenRetryIsIssuedSilently() throws Exception {
        whenPairingIsCompletedThenAddNotifyRelationshipsIsSend();
        reset(pairingPortMock);

        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.FORBIDDEN_ERROR, null);

        verify(pairingPortMock).triggerPairing(eq(APP_TYPE), eq(APP_CPP_ID), stringCaptor.capture());
        verify(pairingListenerMock, never()).onPairingFailed(diCommApplianceMock);
    }

    @Test
    public void whenNotifyStatusIsNotCompleteThenRetryIsIssuedSilently() throws Exception {
        whenPairingIsCompletedThenAddNotifyRelationshipsIsSend();
        reset(pairingPortMock);

        PairingService pairingService = mock(PairingService.class);
        when(pairingService.getAddRelationStatus()).thenReturn("failed");
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.SUCCESS, pairingService);

        verify(pairingPortMock).triggerPairing(eq(APP_TYPE), eq(APP_CPP_ID), stringCaptor.capture());
        verify(pairingListenerMock, never()).onPairingFailed(diCommApplianceMock);
    }

    @Test
    public void whenNotifyAddRelationshipErrorLimitIsReachedThenErrorIsReported2() throws Exception {
        whenPairingIsCompletedThenAddNotifyRelationshipsIsSend();

        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, null);
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, null);
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, null);
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, null);

        verify(pairingListenerMock).onPairingFailed(diCommApplianceMock);
    }

    // User pairing
    @Test
    public void whenUserParingIsStartedThenItIsTriggeredOnPairingPortWithProperUserId() throws Exception {
        pairingHandler.startUserPairing(USER_ID, ACCESS_TOKEN, DEVICE_RELATIONSHIP_TYPE);
        verify(pairingPortMock).addPortListener(pairingListenerCaptor.capture());

        verify(pairingPortMock).triggerPairing(eq(APP_TYPE), eq(USER_ID), anyString());
    }

    @Test
    public void whenPairingPortReportSuccessThenUserRelationshipIsAdded() throws Exception {
        whenUserParingIsStartedThenItIsTriggeredOnPairingPortWithProperUserId();

        pairingListenerCaptor.getValue().onPortUpdate(pairingPortMock);

        verify(pairingServiceMock).addRelationshipRequest(pairingEntitiyReferenceCaptor.capture(), pairingEntitiyReferenceCaptor.capture(), (PairingEntitiyReference) isNull(), any(PairingRelationship.class), any(PairingInfo.class));
        PairingEntitiyReference trustor = pairingEntitiyReferenceCaptor.getAllValues().get(0);
        PairingEntitiyReference trustee = pairingEntitiyReferenceCaptor.getAllValues().get(1);

        assertEquals(USER_ID, trustor.entityRefId);
        assertEquals(ACCESS_TOKEN, trustor.entityRefCredentials);
        assertEquals(NETWORK_CPP_ID, trustee.entityRefId);
        assertNull(trustee.entityRefCredentials);
    }

    @Test
    public void whenDuringUserPairingPairingPortReportsErrorThenRetryIsIssuedSilently() throws Exception {
        whenUserParingIsStartedThenItIsTriggeredOnPairingPortWithProperUserId();
        reset(pairingPortMock);

        pairingListenerCaptor.getValue().onPortError(pairingPortMock, Error.BADREQUEST, "");

        verify(pairingPortMock).triggerPairing(eq(APP_TYPE), eq(USER_ID), anyString());
        verify(pairingListenerMock, never()).onPairingFailed(diCommApplianceMock);
    }

    @Test
    public void whenUserRelationshipWasAddedSuccessfullyThenSuccessIsReported() throws Exception {
        whenPairingPortReportSuccessThenUserRelationshipIsAdded();

        PairingService pairingService = mock(PairingService.class);
        when(pairingService.getAddRelationStatus()).thenReturn("completed");
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.SUCCESS, pairingService);

        verify(pairingListenerMock).onPairingSuccess(diCommApplianceMock);
    }

    @Test
    public void whenUserRelationshipHasFailedThenRetryIsIssuedSilently() throws Exception {
        whenPairingPortReportSuccessThenUserRelationshipIsAdded();
        reset(pairingPortMock);

        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, null);

        verify(pairingPortMock).triggerPairing(eq(APP_TYPE), eq(USER_ID), anyString());
        verify(pairingListenerMock, never()).onPairingFailed(diCommApplianceMock);
    }

    @Test
    public void whenUserPairingAddRelationshipErrorLimitIsReachedThenErrorIsReported() throws Exception {
        whenPairingPortReportSuccessThenUserRelationshipIsAdded();
        reset(pairingPortMock);

        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, null);
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, null);
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, null);
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, null);

        verify(pairingListenerMock).onPairingFailed(diCommApplianceMock);
    }

    @Test
    public void whenAddUserRelationshipStatusIsNotCompletedThenRetryIsIssuedSilently() throws Exception {
        whenPairingPortReportSuccessThenUserRelationshipIsAdded();
        reset(pairingPortMock);

        PairingService pairingService = mock(PairingService.class);
        when(pairingService.getAddRelationStatus()).thenReturn("failed");
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.SUCCESS, pairingService);

        verify(pairingPortMock).triggerPairing(eq(APP_TYPE), eq(USER_ID), anyString());
        verify(pairingListenerMock, never()).onPairingFailed(diCommApplianceMock);
    }

    @Test
    public void whenAddUserRelationshipErrorLimitIsReachedThenErrorIsReported2() throws Exception {
        whenPairingPortReportSuccessThenUserRelationshipIsAdded();
        reset(pairingPortMock);

        PairingService pairingService = mock(PairingService.class);
        when(pairingService.getAddRelationStatus()).thenReturn("failed");
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, pairingService);
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, pairingService);
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, pairingService);
        pairingHandler.mIcpEventListener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT, pairingService);

        verify(pairingListenerMock).onPairingFailed(diCommApplianceMock);
    }

    class PairingHandlerForTest extends PairingHandler<DICommAppliance> {

        public PairingHandlerForTest(DICommAppliance appliance, PairingListener<DICommAppliance> pairingListener) {
            super(appliance, pairingListener);
        }
    }

    class PairingControllerForTest extends PairingController {

        public PairingControllerForTest(@NonNull CppController cloudController) {
            super(cloudController);
        }

        @Override
        public PairingService createPairingService(ICPEventListener icpEventListener) {
            return pairingServiceMock;
        }
    }
}
