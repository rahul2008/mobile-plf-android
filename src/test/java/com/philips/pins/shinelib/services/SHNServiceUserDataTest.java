package com.philips.pins.shinelib.services;

import android.util.Log;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.framework.SHNFactory;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Set;
import java.util.Timer;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Timer.class, Log.class})
public class SHNServiceUserDataTest extends TestCase {

    private SHNFactory mockedShnFactory;
    private SHNService mockedShnService;
    private SHNCharacteristic mockedShnCharacteristic;
    private SHNCharacteristic mockedShnUserControlPointCharacteristic;

    private SHNServiceUserData shnServiceUserData;
    private SHNIntegerResultListener shnIntegerResultListener;
    private SHNResultListener shnResultListener;

    private SHNCharacteristic.SHNCharacteristicChangedListener shnCharacteristicChangedListener;

    private static final byte OP_CODE_REGISTER_NEW_USER = (byte) 0x01;
    private static final byte OP_CODE_CONSENT = (byte) 0x02;
    private static final byte OP_CODE_DELETE_USER_DATA = (byte) 0x03;
    private static final byte OP_CODE_RESPONSE = (byte) 0x20;

    private static final byte RESPONSE_CODE_SUCCESS = (byte) 0x01;
    private static final byte RESPONSE_CODE_OP_CODE_NOT_SUPPORTED = (byte) 0x02;
    private static final byte RESPONSE_CODE_INVALID_PARAMETER = (byte) 0x03;
    private static final byte RESPONSE_CODE_OPERATION_FAILED = (byte) 0x04;
    private static final byte RESPONSE_CODE_USER_NOT_AUTHORIZED = (byte) 0x05;

    @Before
    public void setUp() {
        mockedShnFactory = Mockito.mock(SHNFactory.class);
        mockedShnService = Mockito.mock(SHNService.class);

        when(mockedShnFactory.createNewSHNService(any(UUID.class), any(Set.class), any(Set.class))).thenReturn(mockedShnService);

        mockedShnCharacteristic = Mockito.mock(SHNCharacteristic.class);
        mockedShnUserControlPointCharacteristic = Mockito.mock(SHNCharacteristic.class);

        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.DATABASE_CHANGE_INCREMENT_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.USER_CONTROL_POINT_CHARACTERISTIC_UUID)).thenReturn(mockedShnUserControlPointCharacteristic);
        when(mockedShnUserControlPointCharacteristic.getUuid()).thenReturn(SHNServiceUserData.USER_CONTROL_POINT_CHARACTERISTIC_UUID);

        mockStatic(Log.class);
        PowerMockito.when(Log.w(anyString(), anyString())).thenReturn(0);

        shnServiceUserData = new SHNServiceUserData(mockedShnFactory);
        shnIntegerResultListener = mock(SHNIntegerResultListener.class);
        shnResultListener = mock(SHNResultListener.class);
    }

    private void servicesDidBecomeAvailable() {
        shnServiceUserData.onServiceStateChanged(mockedShnService, SHNService.State.Available);
        when(mockedShnService.getState()).thenReturn(SHNService.State.Available);
        ArgumentCaptor<SHNCharacteristic.SHNCharacteristicChangedListener> characteristicChangedListenerArgumentCaptor = ArgumentCaptor.forClass(SHNCharacteristic.SHNCharacteristicChangedListener.class);
        verify(mockedShnUserControlPointCharacteristic).setShnCharacteristicChangedListener(characteristicChangedListenerArgumentCaptor.capture());
        shnCharacteristicChangedListener = characteristicChangedListenerArgumentCaptor.getValue();
    }

    @Test
    public void initializeTest() {
        assertNotNull(shnServiceUserData);

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        ArgumentCaptor<Set> requiredSetArgumentCaptor = ArgumentCaptor.forClass(Set.class);
        ArgumentCaptor<Set> optionalSetArgumentCaptor = ArgumentCaptor.forClass(Set.class);

        verify(mockedShnFactory).createNewSHNService(uuidArgumentCaptor.capture(), requiredSetArgumentCaptor.capture(), optionalSetArgumentCaptor.capture());
        assertEquals(SHNServiceUserData.SERVICE_UUID, uuidArgumentCaptor.getValue());

        assertNotNull(requiredSetArgumentCaptor.getValue());
        assertTrue(requiredSetArgumentCaptor.getValue().contains(SHNServiceUserData.DATABASE_CHANGE_INCREMENT_CHARACTERISTIC_UUID));
        assertTrue(requiredSetArgumentCaptor.getValue().contains(SHNServiceUserData.USER_CONTROL_POINT_CHARACTERISTIC_UUID));
        assertTrue(requiredSetArgumentCaptor.getValue().contains(SHNServiceUserData.USER_INDEX_CHARACTERISTIC_UUID));
        assertEquals(3, requiredSetArgumentCaptor.getValue().size());

        assertNotNull(optionalSetArgumentCaptor.getValue());
        assertEquals(27, optionalSetArgumentCaptor.getValue().size()); // Review Make all UD characteristics available

        verify(mockedShnService).registerSHNServiceListener(shnServiceUserData);
    }

    @Test
    public void whenServiceIsEnabledThenNotificationsForUserControlPointAreEnabled() {
        shnServiceUserData.onServiceStateChanged(mockedShnService, SHNService.State.Available);

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnUserControlPointCharacteristic).setIndication(booleanArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        assertTrue(booleanArgumentCaptor.getValue());
    }

    @Test
    public void whenRegisterNewUserIsCalledWithIllegalConsentCodeThenResponseIsError() {
        setUpRegisterNewUserStage1(10000);

        verify(shnIntegerResultListener).onActionCompleted(SHNServiceUserData.UNSUCCESSFUL_OPERATION_VALUE, SHNResult.SHNInvalidParameterError);
    }

    @Test
    public void whenRegisterNewUserIsCalledWithLegalConsentCodeThenCorrectPackageIsWrittenToControlPoint() {
        setUpRegisterNewUserStage1(999);

        ArgumentCaptor<byte[]> byteArrayArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnUserControlPointCharacteristic).write(byteArrayArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        assertNotNull(byteArrayArgumentCaptor.getValue());
        assertEquals(3, byteArrayArgumentCaptor.getValue().length);

        byte[] request = {OP_CODE_REGISTER_NEW_USER, (byte) 0xE7, 0x03}; // 0x03E7 == 999
        for (int i = 0; i < request.length; i++) {
            assertEquals("Mismatch at byte " + i, request[i], byteArrayArgumentCaptor.getValue()[i]);
        }
    }

    @Test
    public void whenRegisterNewUserIsCalledWithLegalConsentCodeAndResultIsOkThenResponseIsNotSent() {
        setUpRegisterNewUserStage2(999, SHNResult.SHNOk);

        verify(shnIntegerResultListener, never()).onActionCompleted(anyInt(), any(SHNResult.class));
    }

    @Test
    public void whenRegisterNewUserIsCalledWithLegalConsentCodeAndResultIsNotOkThenResponseIsSent() {
        setUpRegisterNewUserStage2(999, SHNResult.SHNInvalidParameterError);

        verify(shnIntegerResultListener).onActionCompleted(SHNServiceUserData.UNSUCCESSFUL_OPERATION_VALUE, SHNResult.SHNInvalidParameterError);
    }

    @Test
    public void whenNewUserIsRegisteredWithResultOkThenUserIdIsReturned() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS, 0x14};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(0x14, SHNResult.SHNOk);
    }

    @Test
    public void whenNewUserIsRegisteredWithResultOpCodeNotSupportedThenUserIdIsNotReturned() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_OP_CODE_NOT_SUPPORTED};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(SHNServiceUserData.UNSUCCESSFUL_OPERATION_VALUE, SHNResult.SHNUnsupportedOperation);
    }

    @Test
    public void whenNewUserIsRegisteredWithResultOperationFailedThenUserIdIsNotReturned() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_OPERATION_FAILED};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(SHNServiceUserData.UNSUCCESSFUL_OPERATION_VALUE, SHNResult.SHNOperationFailed);
    }

    @Test
    public void whenNewUserIsRegisteredWithResultInvalidParameterThenUserIdIsNotReturned() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_INVALID_PARAMETER};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(SHNServiceUserData.UNSUCCESSFUL_OPERATION_VALUE, SHNResult.SHNInvalidParameterError);
    }

    @Test
    public void whenNewUserIsRegisteredAndResponseIsWithWrongOpResponseCodeThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE + 1, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS, 0x14};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(SHNServiceUserData.UNSUCCESSFUL_OPERATION_VALUE, SHNResult.SHNInvalidResponseError);
    }

    @Test
    public void whenNewUserIsRegisteredAndResponseIsWithWrongOpOperationCodeThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER + 1, RESPONSE_CODE_SUCCESS, 0x14};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(SHNServiceUserData.UNSUCCESSFUL_OPERATION_VALUE, SHNResult.SHNInvalidResponseError);
    }

    @Test
    public void whenNewUserIsRegisteredAndResponseIsInvalidThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, 0x14};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(SHNServiceUserData.UNSUCCESSFUL_OPERATION_VALUE, SHNResult.SHNInvalidResponseError);
    }

    @Test
    public void whenNewUserIsRegisteredAndResponseIsIncomplete1ThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(SHNServiceUserData.UNSUCCESSFUL_OPERATION_VALUE, SHNResult.SHNResponseIncompleteError);
    }

    @Test
    public void whenNewUserIsRegisteredAndResponseIsIncomplete2ThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(SHNServiceUserData.UNSUCCESSFUL_OPERATION_VALUE, SHNResult.SHNResponseIncompleteError);
    }

    @Test
    public void whenNewUserIsRegisteredAndResponseIsIncomplete3ThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(SHNServiceUserData.UNSUCCESSFUL_OPERATION_VALUE, SHNResult.SHNResponseIncompleteError);
    }

    @Test
    public void whenNewUserIsRegisteredWithResponseNotOkAndNotificationIsReceivedThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS, 0x14};
        setUpRegisterNewUserStage3(999, SHNResult.SHNTimeoutError, response);

        verify(shnIntegerResultListener).onActionCompleted(SHNServiceUserData.UNSUCCESSFUL_OPERATION_VALUE, SHNResult.SHNTimeoutError);
    }

    private void setUpRegisterNewUserStage1(int consentCode) {
        shnServiceUserData.registerNewUser(consentCode, shnIntegerResultListener);
    }

    private void setUpRegisterNewUserStage2(int consentCode, SHNResult result) {
        setUpRegisterNewUserStage1(consentCode);

        ArgumentCaptor<byte[]> byteArrayArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnUserControlPointCharacteristic).write(byteArrayArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(result, null);
    }

    private void setUpRegisterNewUserStage3(int consentCode, SHNResult result, byte[] response) {
        servicesDidBecomeAvailable();
        setUpRegisterNewUserStage2(consentCode, result);

        shnCharacteristicChangedListener.onCharacteristicChanged(mockedShnUserControlPointCharacteristic, response);
    }

//    private void assertUserControlPointWriteOnceWithCodes(int numberTotal, int numberExecuted, byte code) {
//        verify(mockedShnService, times(numberTotal)).getSHNCharacteristic(SHNServiceUserData.USER_CONTROL_POINT_CHARACTERISTIC_UUID);
//        ArgumentCaptor<byte[]> byteArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
//        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
//        verify(mockedShnCharacteristic, times(numberExecuted)).write(byteArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());
//
//        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArgumentCaptor.getValue());
//        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
//        assertEquals(code, byteBuffer.get());
//    }

    private void assertUserControlPointWriteOnceWithCodes(byte operationCode, int consentCode) {
        verify(mockedShnService).getSHNCharacteristic(SHNServiceUserData.USER_CONTROL_POINT_CHARACTERISTIC_UUID);
        ArgumentCaptor<byte[]> byteArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnUserControlPointCharacteristic).write(byteArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArgumentCaptor.getValue());
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        assertEquals(operationCode, byteBuffer.get());
        assertEquals(consentCode, byteBuffer.getShort());
    }

    @Test
    public void whenNewUserIsRegisteredThanCharacteristicIsWritten() {
        setUpRegisterNewUserStage1(999);

        verify(mockedShnService).getSHNCharacteristic(SHNServiceUserData.USER_CONTROL_POINT_CHARACTERISTIC_UUID);
        verify(mockedShnUserControlPointCharacteristic).write(any(byte[].class), any(SHNCommandResultReporter.class));
    }

    @Test
    public void whenNewUserIsRegisteredThanCharacteristicIsWrittenWithProperOpCode() {
        int consentCode = 999;
        setUpRegisterNewUserStage1(consentCode);

        assertUserControlPointWriteOnceWithCodes(OP_CODE_REGISTER_NEW_USER, consentCode);
    }

    @Test
    public void whenNewUserIsRegisteredWithResultOkThanListenerIsNotNotified() {
        setUpRegisterNewUserStage2(999, SHNResult.SHNOk);

        verify(shnIntegerResultListener, never()).onActionCompleted(anyInt(), any(SHNResult.class));
    }

    @Test
    public void whenNewUserIsRegisteredWithResultNotOkThanListenerNotNotified() {
        setUpRegisterNewUserStage2(999, SHNResult.SHNTimeoutError);

        verify(shnIntegerResultListener).onActionCompleted(SHNServiceUserData.UNSUCCESSFUL_OPERATION_VALUE, SHNResult.SHNTimeoutError);
    }

    @Test
    public void whenNewUserIsRegisteredAndNotificationIsReceivedThanListenerIsNotified() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS, (byte) 20};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(20, SHNResult.SHNOk);
    }

    @Test
    public void whenNewUserIsRegisteredWithResultErrorThanListenerIsNotified() {
        setUpRegisterNewUserStage2(999, SHNResult.SHNTimeoutError);

        verify(shnIntegerResultListener).onActionCompleted(SHNServiceUserData.UNSUCCESSFUL_OPERATION_VALUE, SHNResult.SHNTimeoutError);
    }

    @Test
    public void whenNewUserIsRegisteredTwiceThenFirstIsExecuting() {
        int consentCode = 999;
        setUpRegisterNewUserStage1(consentCode);
        setUpRegisterNewUserStage1(888);

        assertUserControlPointWriteOnceWithCodes(OP_CODE_REGISTER_NEW_USER, consentCode);
    }

    @Test
    public void whenNewUserIsRegisteredTwiceInARowAndFirstIsNotifiedThenSecondCommandStartsExecuting() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS, 0x24};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.USER_CONTROL_POINT_CHARACTERISTIC_UUID)).thenReturn(mockedShnUserControlPointCharacteristic);
        setUpRegisterNewUserStage1(888);

        verify(mockedShnService, times(2 + 1)).getSHNCharacteristic(SHNServiceUserData.USER_CONTROL_POINT_CHARACTERISTIC_UUID); // one more for the notifications enabling
        verify(mockedShnUserControlPointCharacteristic, times(2)).write(any(byte[].class), any(SHNCommandResultReporter.class));
    }

    private void setUpConsentExistingUserStage1(int userId, int consentCode) {
        shnServiceUserData.consentExistingUser(userId, consentCode, shnResultListener);
    }

    private void setUpConsentExistingUserStage2(int userId, int consentCode, SHNResult result) {
        setUpConsentExistingUserStage1(userId, consentCode);

        ArgumentCaptor<byte[]> byteArrayArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnUserControlPointCharacteristic).write(byteArrayArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(result, null);
    }

    private void setUpConsentExistingUserStage3(int userId, int consentCode, SHNResult result, byte[] response) {
        servicesDidBecomeAvailable();
        setUpConsentExistingUserStage2(userId, consentCode, result);

        shnCharacteristicChangedListener.onCharacteristicChanged(mockedShnUserControlPointCharacteristic, response);
    }

    private void assertUserControlPointWriteOnceWithCode(byte operationCode) {
        verify(mockedShnService).getSHNCharacteristic(SHNServiceUserData.USER_CONTROL_POINT_CHARACTERISTIC_UUID);
        ArgumentCaptor<byte[]> byteArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnUserControlPointCharacteristic).write(byteArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArgumentCaptor.getValue());
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        assertEquals(operationCode, byteBuffer.get());
    }

    @Test
    public void whenConsentExistingUserIsCalledThanCharacteristicIsWritten() {
        int userId = 10;
        setUpConsentExistingUserStage1(userId, 999);

        verify(mockedShnService).getSHNCharacteristic(SHNServiceUserData.USER_CONTROL_POINT_CHARACTERISTIC_UUID);
        verify(mockedShnUserControlPointCharacteristic).write(any(byte[].class), any(SHNCommandResultReporter.class));
    }

    @Test
    public void whenConsentExistingUserIsCalledThanCommandIsCreatedAndStarted() {
        int userId = 10;
        setUpConsentExistingUserStage1(userId, 999);

        assertUserControlPointWriteOnceWithCode(OP_CODE_CONSENT);
    }

    @Test
    public void whenConsentExistingUserWithLegalDataIsCalledThenCorrectPackageIsWrittenToControlPoint() {
        int userId = 0x10;
        setUpConsentExistingUserStage1(userId, 999);

        ArgumentCaptor<byte[]> byteArrayArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnUserControlPointCharacteristic).write(byteArrayArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        assertNotNull(byteArrayArgumentCaptor.getValue());
        assertEquals(4, byteArrayArgumentCaptor.getValue().length);

        byte[] request = {OP_CODE_CONSENT, (byte) userId, (byte) 0xE7, 0x03};
        for (int i = 0; i < request.length; i++) {
            assertEquals("Mismatch at byte " + i, request[i], byteArrayArgumentCaptor.getValue()[i]);
        }
    }

    @Test
    public void whenConsentExistingUserIsCalledWithLegalConsentCodeAndResultIsOkThenListenerIsNotNotified() {
        setUpConsentExistingUserStage2(10, 999, SHNResult.SHNOk);

        verify(shnResultListener, never()).onActionCompleted(any(SHNResult.class));
    }

    @Test
    public void whenConsentExistingUserIsCalledWithLegalConsentCodeAndResultIsNotOkThenListenerIsNotified() {
        setUpConsentExistingUserStage2(10, 999, SHNResult.SHNUnsupportedOperation);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNUnsupportedOperation);
    }

    @Test
    public void whenConsentExistingUserIsCalledWithIllegalConsentCodeThenResponseIsError() {
        setUpConsentExistingUserStage1(256, 999);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNInvalidParameterError);
    }

    @Test
    public void whenConsentExistingUserIsCalledWithResultOkThenListenerIsNotified() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_CONSENT, RESPONSE_CODE_SUCCESS};
        setUpConsentExistingUserStage3(10, 999, SHNResult.SHNOk, response);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenConsentExistingUserIsCalledWithResultErrorThanListenerIsNotified() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS};
        setUpConsentExistingUserStage3(10, 999, SHNResult.SHNOk, response);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNInvalidResponseError);
    }

    @Test
    public void whenConsentExistingUserIsCalledWithResultOperationFailedThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_CONSENT, RESPONSE_CODE_OPERATION_FAILED};
        setUpConsentExistingUserStage3(10, 999, SHNResult.SHNOk, response);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNOperationFailed);
    }

    @Test
    public void whenConsentExistingUserIsCalledWithResultSHNUserNotAuthorizedThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_CONSENT, RESPONSE_CODE_USER_NOT_AUTHORIZED};
        setUpConsentExistingUserStage3(10, 999, SHNResult.SHNOk, response);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNUserNotAuthorized);
    }

    @Test
    public void whenConsentExistingUserIsCalledAndResponseIsWithWrongOpResponseCodeThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE + 1, OP_CODE_CONSENT, RESPONSE_CODE_SUCCESS};
        setUpConsentExistingUserStage3(10, 999, SHNResult.SHNOk, response);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNInvalidResponseError);
    }

    @Test
    public void whenNewConsentExistingUserIsCalledWithResponseNotOkAndNotificationIsReceivedThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_CONSENT, RESPONSE_CODE_SUCCESS};
        setUpConsentExistingUserStage3(10, 999, SHNResult.SHNTimeoutError, response);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNTimeoutError);
    }

    @Test
    public void whenNewConsentExistingUserIsCalledThenCommandIsFinishedAfterNotification() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_CONSENT, RESPONSE_CODE_SUCCESS, 0x14};
        setUpConsentExistingUserStage3(10, 999, SHNResult.SHNOk, response);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenNewConsentExistingUserIsCalledWithResultNotOkThanCommandIsFinishedAfterResponse() {
        setUpConsentExistingUserStage2(10, 999, SHNResult.SHNTimeoutError);

        assertUserControlPointWriteOnceWithCode(OP_CODE_CONSENT);
    }

    @Test
    public void whenTwoTaskAreSentThenFirstIsExecuting() {
        setUpRegisterNewUserStage1(999);
        setUpConsentExistingUserStage1(10, 999);

        assertUserControlPointWriteOnceWithCode(OP_CODE_REGISTER_NEW_USER);
    }

    @Test
    public void whenTwoTaskAreSentAndFirstOneIsFinishedThenSecondIsExecuting() {
        setUpRegisterNewUserStage2(999, SHNResult.SHNOk);
        setUpConsentExistingUserStage1(10, 888);

        servicesDidBecomeAvailable();
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS, 0x11};
        shnCharacteristicChangedListener.onCharacteristicChanged(mockedShnUserControlPointCharacteristic, response);

        verify(mockedShnService, times(2 + 1)).getSHNCharacteristic(SHNServiceUserData.USER_CONTROL_POINT_CHARACTERISTIC_UUID); // one more for the notifications
        verify(mockedShnUserControlPointCharacteristic, times(2)).write(any(byte[].class), any(SHNCommandResultReporter.class));
    }

    @Test
    public void whenTwoTaskAreFinishedThanQueueIsEmpty() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS, 0x11};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        reset(mockedShnUserControlPointCharacteristic);
        byte[] response2 = {OP_CODE_RESPONSE, OP_CODE_CONSENT, RESPONSE_CODE_SUCCESS};
        setUpConsentExistingUserStage2(10, 888, SHNResult.SHNOk);
        shnCharacteristicChangedListener.onCharacteristicChanged(mockedShnUserControlPointCharacteristic, response2);

        verify(mockedShnService, times(2 + 1)).getSHNCharacteristic(SHNServiceUserData.USER_CONTROL_POINT_CHARACTERISTIC_UUID); // one more for the notifications
        verify(mockedShnUserControlPointCharacteristic).write(any(byte[].class), any(SHNCommandResultReporter.class));
    }

    private void setUpDeleteUserStage1() {
        shnServiceUserData.deleteUser(shnResultListener);
    }

    private void setUpDeleteUserStage2(SHNResult result) {
        setUpDeleteUserStage1();

        ArgumentCaptor<byte[]> byteArrayArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnUserControlPointCharacteristic).write(byteArrayArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(result, null);
    }

    private void setUpDeleteUserStage3(SHNResult result, byte[] response) {
        servicesDidBecomeAvailable();
        setUpDeleteUserStage2(result);

        shnCharacteristicChangedListener.onCharacteristicChanged(mockedShnUserControlPointCharacteristic, response);
    }

    @Test
    public void whenDeleteUserIsCalledThanCommandIsCreatedAndStarted() {
        setUpDeleteUserStage1();

        assertUserControlPointWriteOnceWithCode(OP_CODE_DELETE_USER_DATA);
    }

    @Test
    public void whenDeleteUserWithLegalDataThenCorrectPackageIsWrittenToControlPoint() {
        setUpDeleteUserStage1();

        ArgumentCaptor<byte[]> byteArrayArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnUserControlPointCharacteristic).write(byteArrayArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        assertNotNull(byteArrayArgumentCaptor.getValue());
        assertEquals(1, byteArrayArgumentCaptor.getValue().length);

        assertEquals(OP_CODE_DELETE_USER_DATA, byteArrayArgumentCaptor.getValue()[0]);
    }

    @Test
    public void whenDeleteUserIsCalledWithLegalConsentCodeAndResultIsOkThenResponseIsNotSent() {
        setUpDeleteUserStage2(SHNResult.SHNOk);

        verify(shnResultListener, never()).onActionCompleted(any(SHNResult.class));
    }

    @Test
    public void whenDeleteUserIsCalledWithLegalConsentCodeAndResultIsNotOkThenResponseIsSent() {
        setUpDeleteUserStage2(SHNResult.SHNUnsupportedOperation);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNUnsupportedOperation);
    }

    @Test
    public void whenDeleteUserIsCalledThanCommandIsNotFinishedAfterResponse() {
        setUpDeleteUserStage2(SHNResult.SHNOk);

        assertUserControlPointWriteOnceWithCode(OP_CODE_DELETE_USER_DATA);
    }

    @Test
    public void whenDeleteUserIsCalledWithResultOkThenResultIsReported() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_DELETE_USER_DATA, RESPONSE_CODE_SUCCESS};
        setUpDeleteUserStage3(SHNResult.SHNOk, response);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenDeleteUserIsCalledWithResultOkAndNotificationHasWrongOpCodeThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_CONSENT, RESPONSE_CODE_SUCCESS};
        setUpDeleteUserStage3(SHNResult.SHNOk, response);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNInvalidResponseError);
    }

    @Test
    public void whenDeleteUserIsCalledWithResultOperationFailedThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_DELETE_USER_DATA, RESPONSE_CODE_OPERATION_FAILED};
        setUpDeleteUserStage3(SHNResult.SHNOk, response);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNOperationFailed);
    }

    @Test
    public void whenDeleteUserIsCalledWithEmptyResultThenErrorIsReported() {
        byte[] response = {};
        setUpDeleteUserStage3(SHNResult.SHNOk, response);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNResponseIncompleteError);
    }

    @Test
    public void whenDeleteUserIsCalledAndResponseIsWithWrongOpResponseCodeThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE + 1, OP_CODE_DELETE_USER_DATA, RESPONSE_CODE_SUCCESS};
        setUpDeleteUserStage3(SHNResult.SHNOk, response);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNInvalidResponseError);
    }

    @Test
    public void whenNewDeleteUserIsCalledWithResponseNotOkAndNotificationIsReceivedThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_DELETE_USER_DATA, RESPONSE_CODE_SUCCESS};
        setUpDeleteUserStage3(SHNResult.SHNTimeoutError, response);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNTimeoutError);
    }

    @Test
    public void whenThreeTaskAreSentThenFirstIsExecuting() {
        int consentCode1 = 999;
        setUpRegisterNewUserStage1(consentCode1);
        setUpConsentExistingUserStage1(10, 999);
        setUpDeleteUserStage1();

        assertUserControlPointWriteOnceWithCodes(OP_CODE_REGISTER_NEW_USER, consentCode1);
    }

    @Test
    public void whenThreeTaskAreSentAndFirstOneIsFinishedThenSecondIsExecuting() {
        setUpRegisterNewUserStage2(999, SHNResult.SHNOk);
        setUpConsentExistingUserStage1(10, 888);
        setUpDeleteUserStage1();

        servicesDidBecomeAvailable();
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS, 0x11};
        shnCharacteristicChangedListener.onCharacteristicChanged(mockedShnUserControlPointCharacteristic, new byte[4]);

        verify(mockedShnService, times(2 + 1)).getSHNCharacteristic(SHNServiceUserData.USER_CONTROL_POINT_CHARACTERISTIC_UUID);
        verify(mockedShnUserControlPointCharacteristic, times(2)).write(any(byte[].class), any(SHNCommandResultReporter.class));
    }

    @Test
    public void whenTwoTasksAreFinishedThanQueueIsEmpty() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS, 0x11};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        reset(mockedShnUserControlPointCharacteristic);
        byte[] response2 = {OP_CODE_RESPONSE, OP_CODE_DELETE_USER_DATA, RESPONSE_CODE_SUCCESS};
        setUpDeleteUserStage2(SHNResult.SHNOk);
        shnCharacteristicChangedListener.onCharacteristicChanged(mockedShnUserControlPointCharacteristic, response2);

        verify(mockedShnService, times(2 + 1)).getSHNCharacteristic(SHNServiceUserData.USER_CONTROL_POINT_CHARACTERISTIC_UUID); // one more for the notifications
        verify(mockedShnUserControlPointCharacteristic).write(any(byte[].class), any(SHNCommandResultReporter.class));
    }
}