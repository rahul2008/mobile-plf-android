/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import com.philips.cdp.cloudcontroller.api.CloudController;
import com.philips.cdp.cloudcontroller.api.pairing.PairingController;
import com.philips.cdp.cloudcontroller.api.pairing.PairingEntity;
import com.philips.cdp.cloudcontroller.api.pairing.PairingRelation;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.icpinterface.PairingService;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static com.philips.cdp.dicommclient.port.common.PairingHandler.PAIRING_DATA_ACCESS_RELATIONSHIP;
import static com.philips.cdp.dicommclient.port.common.PairingHandler.RELATION_STATUS_COMPLETED;
import static com.philips.cdp.dicommclient.port.common.PairingHandler.RELATION_STATUS_FAILED;
import static java.lang.Long.parseLong;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PairingHandlerTest {

    private static final String USER_PROVIDER_TYPE = "user";
    private static final String CPP_PROVIDER_TYPE = "cpp";
    private final String APP_CPP_ID = "app-eui64";
    private final String APP_TYPE = "app-android-dev";
    private final String DEVICE_TYPE = "device-type";
    private final String DEVICE_CPP_ID = "device-cpp-id";
    private final String USER_ID = "user-id";
    private final String ACCESS_TOKEN = "access-token";

    @Mock
    private NetworkNode networkNodeMock;

    @Mock
    private Appliance applianceMock;

    @Mock
    private CloudController cloudControllerMock;

    @Mock
    private PairingPort pairingPortMock;

    @Mock
    private PairingListener<Appliance> pairingListenerMock;

    @Captor
    private ArgumentCaptor<DICommPortListener> pairingListenerCaptor;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Captor
    private ArgumentCaptor<PairingRelation> pairingRelationshipCaptor;

    @Mock
    private PairingController pairingControllerMock;

    private PairingHandler pairingHandler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        PairingHandler.clear();

        when(applianceMock.getNetworkNode()).thenReturn(networkNodeMock);
        when(applianceMock.getPairingPort()).thenReturn(pairingPortMock);
        when(applianceMock.getDeviceType()).thenReturn(DEVICE_TYPE);

        when(cloudControllerMock.getAppType()).thenReturn(APP_TYPE);
        when(cloudControllerMock.getAppCppId()).thenReturn(APP_CPP_ID);
        when(cloudControllerMock.isSignOn()).thenReturn(true);
        when(cloudControllerMock.getPairingController()).thenReturn(pairingControllerMock);

        when(networkNodeMock.getCppId()).thenReturn(DEVICE_CPP_ID);

        pairingHandler = new PairingHandlerForTest(applianceMock, pairingListenerMock);

        DICommLog.disableLogging();
    }

    @Test
    public void testGenerateRandomSecretKeyNotNull() {
        PairingHandler manager = new PairingHandler<>(null, (PairingListener<Appliance>) null, cloudControllerMock);
        String randomKey = manager.generateRandomSecretKey();
        assertNotNull(randomKey);
    }

    @Test
    public void testGenerateRandomSecretKeyNotEqual() {
        PairingHandler manager = new PairingHandler<>(null, (PairingListener<Appliance>) null, cloudControllerMock);
        String randomKey = manager.generateRandomSecretKey();
        String randomKey1 = manager.generateRandomSecretKey();
        assertFalse(randomKey.equals(randomKey1));
    }

    @Test
    public void testGenerateRandomSecretKeyLenght() {
        PairingHandler manager = new PairingHandler<>(null, (PairingListener<Appliance>) null, cloudControllerMock);
        String randomKey = manager.generateRandomSecretKey();
        assertThat(randomKey.length()).isEqualTo(16);
    }

    @Test
    public void testGenerateRandomSecretKeyIsHex() {
        PairingHandler manager = new PairingHandler<>(null, (PairingListener<Appliance>) null, cloudControllerMock);
        String randomKey = manager.generateRandomSecretKey();
        parseLong(randomKey.substring(0, 8), 16);
        parseLong(randomKey.substring(9, 16), 16);
    }

    @Test
    public void testGetDiffInDays() {
        long rl = PairingHandler.getDiffInDays(0L);
        assertTrue(rl != 0);
    }

    @Test
    public void whenPairingIsStartedThenItIsTriggeredOnPairingPortWithProperAppTypeAndCPPId() throws Exception {
        pairingHandler.startPairing();
        verify(pairingPortMock).addPortListener(pairingListenerCaptor.capture());

        verify(pairingPortMock).pair(eq(APP_TYPE), eq(APP_CPP_ID), stringCaptor.capture());
    }

    @Test
    public void whenPairingPortReportsSuccessThenRelationshipIsAddedWithTheSecretKey() throws Exception {
        whenPairingIsStartedThenItIsTriggeredOnPairingPortWithProperAppTypeAndCPPId();

        pairingListenerCaptor.getValue().onPortUpdate(pairingPortMock);

        ArgumentCaptor<String> secretCaptor = ArgumentCaptor.forClass(String.class);
        verify(pairingControllerMock).addRelationship(pairingRelationshipCaptor.capture(), eq(pairingHandler.mPairingCallback), secretCaptor.capture());
        assertEquals(stringCaptor.getValue(), secretCaptor.getValue());
    }

    @Test
    public void whenPairingPortReportsSuccessThenRelationshipIsAddedWithTrusteeIsCurrentAppliance() throws Exception {
        whenPairingPortReportsSuccessThenRelationshipIsAddedWithTheSecretKey();

        PairingEntity usedTrustee = pairingRelationshipCaptor.getValue().getTrusteeEntity();
        assertEquals(DEVICE_TYPE, usedTrustee.type);
        assertEquals(DEVICE_CPP_ID, usedTrustee.id);
        assertEquals(CPP_PROVIDER_TYPE, usedTrustee.provider);
    }

    @Test
    public void whenPairingPortReportsSuccessThenPairingDiCommRelationshipsIsSent() throws Exception {
        whenPairingPortReportsSuccessThenRelationshipIsAddedWithTheSecretKey();

        PairingRelation relation = pairingRelationshipCaptor.getValue();
        assertEquals(PairingHandler.PAIRING_DI_COMM_RELATIONSHIP, relation.getType());
        assertThat(relation.getPermissions().contains(PairingController.PERMISSION_CHANGE)).isTrue();
        assertThat(relation.getPermissions().contains(PairingController.PERMISSION_RESPONSE)).isTrue();
    }

    @Test
    public void whenPairingPortReportsErrorThenRetryIsIssuedSilently() throws Exception {
        whenPairingIsStartedThenItIsTriggeredOnPairingPortWithProperAppTypeAndCPPId();
        reset(pairingPortMock);

        pairingListenerCaptor.getValue().onPortError(pairingPortMock, Error.NOT_UNDERSTOOD, "");

        verify(pairingPortMock).pair(eq(APP_TYPE), eq(APP_CPP_ID), stringCaptor.capture());
        verify(pairingListenerMock, never()).onPairingFailed(applianceMock);
    }

    @Test
    public void whenPairingPortErrorLimitIsReachedThenErrorIsReported() throws Exception {
        whenPairingIsStartedThenItIsTriggeredOnPairingPortWithProperAppTypeAndCPPId();
        reset(pairingPortMock);

        pairingListenerCaptor.getValue().onPortError(pairingPortMock, Error.NOT_UNDERSTOOD, "");
        pairingListenerCaptor.getValue().onPortError(pairingPortMock, Error.NOT_UNDERSTOOD, "");
        pairingListenerCaptor.getValue().onPortError(pairingPortMock, Error.NOT_UNDERSTOOD, "");
        pairingListenerCaptor.getValue().onPortError(pairingPortMock, Error.NOT_UNDERSTOOD, "");

        verify(pairingListenerMock).onPairingFailed(applianceMock);
    }

    @Test
    public void whenPairingIsCompletedThenAddNotifyRelationshipsIsSent() throws Exception {
        whenPairingPortReportsSuccessThenRelationshipIsAddedWithTheSecretKey();

        pairingHandler.mPairingCallback.onRelationshipAdd(RELATION_STATUS_COMPLETED);

        verify(pairingControllerMock).addRelationship(pairingRelationshipCaptor.capture(), eq(pairingHandler.mPairingCallback));

        PairingRelation relation = pairingRelationshipCaptor.getValue();
        assertEquals(PairingHandler.PAIRING_NOTIFY_RELATIONSHIP, relation.getType());
        assertTrue(relation.getPermissions().contains(PairingController.PERMISSION_PUSH));
    }

    @Test
    public void whenPairingHasFailedThenRetryIsIssuedSilently() throws Exception {
        whenPairingPortReportsSuccessThenRelationshipIsAddedWithTheSecretKey();
        reset(pairingPortMock);

        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);

        verify(pairingPortMock).pair(eq(APP_TYPE), eq(APP_CPP_ID), stringCaptor.capture());
        verify(pairingListenerMock, never()).onPairingFailed(applianceMock);
    }

    @Test
    public void whenPairingAddRelationshipErrorLimitIsReachedThenErrorIsReported() throws Exception {
        whenPairingPortReportsSuccessThenRelationshipIsAddedWithTheSecretKey();
        reset(pairingPortMock);

        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);
        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);
        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);
        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);

        verify(pairingListenerMock).onPairingFailed(applianceMock);
    }

    @Test
    public void whenPairingHasStatusIsNotCompletedThenRetryIsIssuedSilently() throws Exception {
        whenPairingPortReportsSuccessThenRelationshipIsAddedWithTheSecretKey();
        reset(pairingPortMock);

        pairingHandler.mPairingCallback.onRelationshipAdd(RELATION_STATUS_FAILED);

        verify(pairingPortMock).pair(eq(APP_TYPE), eq(APP_CPP_ID), stringCaptor.capture());
        verify(pairingListenerMock, never()).onPairingFailed(applianceMock);
    }

    @Test
    public void whenPairingAddRelationshipErrorLimitIsReachedThenErrorIsReported2() throws Exception {
        whenPairingPortReportsSuccessThenRelationshipIsAddedWithTheSecretKey();
        reset(pairingPortMock);

        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);
        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);
        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);
        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);

        verify(pairingListenerMock).onPairingFailed(applianceMock);
    }

    @Test
    public void whenNotifyIsCompletedThenPairingSuccessIsReported() throws Exception {
        whenPairingIsCompletedThenAddNotifyRelationshipsIsSent();

        pairingHandler.mPairingCallback.onRelationshipAdd(RELATION_STATUS_COMPLETED);

        verify(pairingListenerMock).onPairingSuccess(applianceMock);
    }

    @Test
    public void whenNotifyIsFailedThenRetryIsIssuedSilently() throws Exception {
        whenPairingIsCompletedThenAddNotifyRelationshipsIsSent();
        reset(pairingPortMock);

        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.FORBIDDEN_ERROR);

        verify(pairingPortMock).pair(eq(APP_TYPE), eq(APP_CPP_ID), stringCaptor.capture());
        verify(pairingListenerMock, never()).onPairingFailed(applianceMock);
    }

    @Test
    public void whenNotifyStatusIsNotCompleteThenRetryIsIssuedSilently() throws Exception {
        whenPairingIsCompletedThenAddNotifyRelationshipsIsSent();
        reset(pairingPortMock);

        pairingHandler.mPairingCallback.onRelationshipAdd(RELATION_STATUS_FAILED);

        verify(pairingPortMock).pair(eq(APP_TYPE), eq(APP_CPP_ID), stringCaptor.capture());
        verify(pairingListenerMock, never()).onPairingFailed(applianceMock);
    }

    @Test
    public void whenNotifyAddRelationshipErrorLimitIsReachedThenErrorIsReported2() throws Exception {
        whenPairingIsCompletedThenAddNotifyRelationshipsIsSent();

        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);
        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);
        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);
        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);

        verify(pairingListenerMock).onPairingFailed(applianceMock);
    }

    // User pairing
    @Test
    public void whenUserPairingIsStartedThenItIsTriggeredOnPairingPortWithProperUserId() throws Exception {

        pairingHandler.startUserPairing(USER_ID, ACCESS_TOKEN);

        verify(pairingPortMock).addPortListener(pairingListenerCaptor.capture());
        verify(pairingPortMock).pair(eq(USER_PROVIDER_TYPE), eq(USER_ID), anyString());
    }

    @Test
    public void whenPairingPortReportSuccessThenUserRelationshipIsAdded() throws Exception {
        whenUserPairingIsStartedThenItIsTriggeredOnPairingPortWithProperUserId();

        pairingListenerCaptor.getValue().onPortUpdate(pairingPortMock);

        ArgumentCaptor<String> secretCaptor = ArgumentCaptor.forClass(String.class);
        verify(pairingControllerMock).addRelationship(pairingRelationshipCaptor.capture(), eq(pairingHandler.mPairingCallback), secretCaptor.capture());

        PairingRelation relation = pairingRelationshipCaptor.getValue();

        PairingEntity trustor = relation.getTrustorEntity();
        PairingEntity trustee = relation.getTrusteeEntity();
        assertEquals(USER_ID, trustor.id);
        assertEquals(ACCESS_TOKEN, trustor.credentials);
        assertEquals(DEVICE_CPP_ID, trustee.id);
        assertNull(trustee.credentials);

        assertThat(relation.getPermissions().contains(PairingController.PERMISSION_CHANGE)).isTrue();
        assertThat(relation.getPermissions().contains(PairingController.PERMISSION_RESPONSE)).isTrue();
    }

    @Test
    public void whenDuringUserPairingPairingPortReportsErrorThenRetryIsIssuedSilently() throws Exception {
        whenUserPairingIsStartedThenItIsTriggeredOnPairingPortWithProperUserId();
        reset(pairingPortMock);

        pairingListenerCaptor.getValue().onPortError(pairingPortMock, Error.NOT_UNDERSTOOD, "");

        verify(pairingPortMock).pair(eq(USER_PROVIDER_TYPE), eq(USER_ID), anyString());
        verify(pairingListenerMock, never()).onPairingFailed(applianceMock);
    }

    @Test
    public void whenUserRelationshipWasAddedSuccessfullyThenSuccessIsReported() throws Exception {
        whenPairingPortReportSuccessThenUserRelationshipIsAdded();

        pairingHandler.mPairingCallback.onRelationshipAdd(RELATION_STATUS_COMPLETED);

        verify(pairingListenerMock).onPairingSuccess(applianceMock);
    }

    @Test
    public void whenUserRelationshipHasFailedThenRetryIsIssuedSilently() throws Exception {
        whenPairingPortReportSuccessThenUserRelationshipIsAdded();
        reset(pairingPortMock);

        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);

        verify(pairingPortMock).pair(eq(USER_PROVIDER_TYPE), eq(USER_ID), anyString());
        verify(pairingListenerMock, never()).onPairingFailed(applianceMock);
    }

    @Test
    public void whenUserPairingAddRelationshipErrorLimitIsReachedThenErrorIsReported() throws Exception {
        whenPairingPortReportSuccessThenUserRelationshipIsAdded();
        reset(pairingPortMock);

        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);
        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);
        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);
        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);

        verify(pairingListenerMock).onPairingFailed(applianceMock);
    }

    @Test
    public void whenAddUserRelationshipStatusIsNotCompletedThenRetryIsIssuedSilently() throws Exception {
        whenPairingPortReportSuccessThenUserRelationshipIsAdded();
        reset(pairingPortMock);

        pairingHandler.mPairingCallback.onRelationshipAdd(RELATION_STATUS_FAILED);

        verify(pairingPortMock).pair(eq(USER_PROVIDER_TYPE), eq(USER_ID), anyString());
        verify(pairingListenerMock, never()).onPairingFailed(applianceMock);
    }

    @Test
    public void whenAddUserRelationshipErrorLimitIsReachedThenErrorIsReported2() throws Exception {
        whenPairingPortReportSuccessThenUserRelationshipIsAdded();
        reset(pairingPortMock);

        PairingService pairingService = mock(PairingService.class);
        when(pairingService.getAddRelationStatus()).thenReturn(RELATION_STATUS_FAILED);

        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);
        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);
        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);
        pairingHandler.mPairingCallback.onPairingError(Commands.PAIRING_ADD_RELATIONSHIP, Errors.CONNECT_TIMEDOUT);

        verify(pairingListenerMock).onPairingFailed(applianceMock);
    }

    @Test
    public void whenDataAccessRelationshipRemovalRequestReturnsFailureDueToNetworkNotBeingAvailableThenFailureIsNotifiedOnPairingCallback() {
        pairingHandler.initializeRelationshipRemoval();

        pairingHandler.currentRelationshipType = PAIRING_DATA_ACCESS_RELATIONSHIP;
        pairingHandler.mPairingCallback.onRelationshipRemove(Errors.NETWORK_NOT_AVAILABLE);

        verify(pairingListenerMock).onPairingFailed(applianceMock);
    }

    @Test
    public void whenDataAccessRelationshipRemovalRequestReturnsSuccessThenSuccessIsNotifiedOnPairingCallback() {
        pairingHandler.initializeRelationshipRemoval();

        pairingHandler.currentRelationshipType = PAIRING_DATA_ACCESS_RELATIONSHIP;
        pairingHandler.mPairingCallback.onRelationshipRemove(Errors.SUCCESS);

        verify(pairingListenerMock).onPairingSuccess(applianceMock);
    }

    class PairingHandlerForTest extends PairingHandler<Appliance> {

        PairingHandlerForTest(Appliance appliance, PairingListener<Appliance> pairingListener) {
            super(appliance, pairingListener, cloudControllerMock);
        }

    }
}
