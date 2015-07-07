package com.philips.pins.shinelib.services;

import android.util.Log;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.SHNStringResultListener;
import com.philips.pins.shinelib.framework.SHNFactory;
import com.philips.pins.shinelib.utility.ScalarConverters;

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
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.Timer;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
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

        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.DATABASE_CHANGE_INDEX_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.USER_INDEX_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.FIRST_NAME_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.LAST_NAME_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);
        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.AGE_CHARACTERISTIC_UUID)).thenReturn(mockedShnCharacteristic);

        when(mockedShnService.getSHNCharacteristic(SHNServiceUserData.USER_CONTROL_POINT_CHARACTERISTIC_UUID)).thenReturn(mockedShnUserControlPointCharacteristic);

        mockStatic(Log.class);
        PowerMockito.when(Log.w(anyString(), anyString())).thenReturn(0);

        shnServiceUserData = new SHNServiceUserData(mockedShnFactory);
        shnIntegerResultListener = mock(SHNIntegerResultListener.class);
        shnResultListener = mock(SHNResultListener.class);
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
        assertTrue(requiredSetArgumentCaptor.getValue().contains(SHNServiceUserData.DATABASE_CHANGE_INDEX_CHARACTERISTIC_UUID));
        assertTrue(requiredSetArgumentCaptor.getValue().contains(SHNServiceUserData.USER_CONTROL_POINT_CHARACTERISTIC_UUID));
        assertTrue(requiredSetArgumentCaptor.getValue().contains(SHNServiceUserData.USER_INDEX_CHARACTERISTIC_UUID));
        assertEquals(3, requiredSetArgumentCaptor.getValue().size());

        assertNotNull(optionalSetArgumentCaptor.getValue());
        assertNotSame(0, optionalSetArgumentCaptor.getValue().size()); // Review Make all UD characteristics available

        verify(mockedShnService).registerSHNServiceListener(shnServiceUserData);
    }

    @Test
    public void whenServiceIsEnabledThenNotificationsForUserControlPointAreEnabled() {
        shnServiceUserData.onServiceStateChanged(mockedShnService, SHNService.State.Available);

        ArgumentCaptor<Boolean> booleanArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnUserControlPointCharacteristic).setNotification(booleanArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        assertTrue(booleanArgumentCaptor.getValue());
    }

    @Test
    public void getFirstNameWithResultOkTest() {
        SHNStringResultListener mockedShnStringResultListener = mock(SHNStringResultListener.class); // Review Test does not verify that the correct characteristic is read.
        shnServiceUserData.getFirstName(mockedShnStringResultListener);                              // Review Possibly the test should indicate to test a String characteristic.

        assertGetStringCharacteristic(mockedShnStringResultListener, "Jack");
    }

    @Test
    public void getLastNameWithResultOkTest() {
        SHNStringResultListener mockedShnStringResultListener = mock(SHNStringResultListener.class);
        shnServiceUserData.getLastName(mockedShnStringResultListener);

        assertGetStringCharacteristic(mockedShnStringResultListener, "Jones");
    }

    private void assertGetStringCharacteristic(SHNStringResultListener mockedShnStringResultListener, String name) {
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());
        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();

        shnCommandResultReporter.reportResult(SHNResult.SHNOk, name.getBytes()); // Review String characteristics are UTF_8 encoded. Not a problem in these test cases.

        verify(mockedShnStringResultListener).onActionCompleted(name, SHNResult.SHNOk);
    }

    @Test
    public void setFirstNameWithResultOkTest() { // Review See getFirstName...
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        String name = "Jack";
        shnServiceUserData.setFirstName(name, mockedShnResultListener);

        assertSetStringCharacteristic(name, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void setLastNameWithResultOkTest() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        String name = "Jones";
        shnServiceUserData.setLastName(name, mockedShnResultListener);

        assertSetStringCharacteristic(name, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void setAgeWithResultOkTest() { // Review Where is the test for getAge...? and again it is not verifying the characteristic as the name implies.
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        int age = 89;
        shnServiceUserData.setAge(age, mockedShnResultListener);

        assertSetUInt8Characteristic(age, SHNResult.SHNOk);
        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenIncrementDatabaseIndexIsCalledThenCharacteristicIsRed() { // Review the characteristic is not checked.
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        shnServiceUserData.incrementDatabaseIndex(mockedShnResultListener);

        verify(mockedShnCharacteristic).read(any(SHNCommandResultReporter.class));
    }

    @Test
    public void whenIncrementDatabaseIndexIsCalledAndResultIsOkayThenCurrentIndexIsReported() { // Review where is the current index reported????
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        shnServiceUserData.incrementDatabaseIndex(mockedShnResultListener);

        byte[] data = {0x01, 0x00, 0x00, 0x00};
        verifyReadOnCharacteristicWithResult(data, SHNResult.SHNOk);
        verifyWriteOnCharacteristicWithResult(2, SHNResult.SHNOk);

        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenIncrementDatabaseIndexIsCalledAndResultOfReadIsNotOkayThenCurrentIndexIsReported() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        shnServiceUserData.incrementDatabaseIndex(mockedShnResultListener);

        byte[] data = {0x01, 0x00, 0x00, 0x00};
        verifyReadOnCharacteristicWithResult(data, SHNResult.SHNServiceUnavailableError);

        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNServiceUnavailableError);
    }

    @Test
    public void whenIncrementDatabaseIndexIsCalledAndResultOrWriteIsNotOkayThenCurrentIndexIsReported() {
        SHNResultListener mockedShnResultListener = mock(SHNResultListener.class);

        shnServiceUserData.incrementDatabaseIndex(mockedShnResultListener);

        byte[] data = {0x01, 0x00, 0x00, 0x00};
        verifyReadOnCharacteristicWithResult(data, SHNResult.SHNOk);
        verifyWriteOnCharacteristicWithResult(2, SHNResult.SHNServiceUnavailableError);

        verify(mockedShnResultListener).onActionCompleted(SHNResult.SHNServiceUnavailableError);
    }

    private void verifyReadOnCharacteristicWithResult(byte[] data, SHNResult result) {
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(result, data);
    }

    private void verifyWriteOnCharacteristicWithResult(int value, SHNResult result) {
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        ArgumentCaptor<byte[]> byteArrayArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(mockedShnCharacteristic).write(byteArrayArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArrayArgumentCaptor.getValue());
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        assertEquals(value, byteBuffer.getInt());

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(result, null);
    }

    private void assertSetStringCharacteristic(String name, SHNResult result) {
        ArgumentCaptor<byte[]> byteArrayArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnCharacteristic).write(byteArrayArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());
        String resultString = new String(byteArrayArgumentCaptor.getValue(), StandardCharsets.UTF_8);
        assertEquals(name, resultString);

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(result, null);
    }

    private void assertSetUInt8Characteristic(int request, SHNResult result) {
        ArgumentCaptor<byte[]> byteArrayArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnCharacteristic).write(byteArrayArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArrayArgumentCaptor.getValue());
        int response = ScalarConverters.ubyteToInt(byteBuffer.get());

        assertEquals(request, response);

        shnCommandResultReporterArgumentCaptor.getValue().reportResult(result, null);
    }

    @Test
    public void getUserIndexTest() { // Review it is not verified that the proper characteristic is used.
        SHNIntegerResultListener mockedShnIntegerResultListener = mock(SHNIntegerResultListener.class);
        shnServiceUserData.getUserIndex(mockedShnIntegerResultListener);

        byte[] index = {0x04}; // Review what is this magic number?

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());
        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();

        shnCommandResultReporter.reportResult(SHNResult.SHNOk, index);

        verify(mockedShnIntegerResultListener).onActionCompleted(4, SHNResult.SHNOk);
    }

    @Test
    public void whenUserIdIsMaxThenItIsParsedProperly() {
        SHNIntegerResultListener mockedShnIntegerResultListener = mock(SHNIntegerResultListener.class);
        shnServiceUserData.getUserIndex(mockedShnIntegerResultListener);

        byte[] index = {(byte) 0xFF};

        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnCharacteristic).read(shnCommandResultReporterArgumentCaptor.capture());
        SHNCommandResultReporter shnCommandResultReporter = shnCommandResultReporterArgumentCaptor.getValue();

        shnCommandResultReporter.reportResult(SHNResult.SHNOk, index);

        verify(mockedShnIntegerResultListener).onActionCompleted(255, SHNResult.SHNOk);
    }

    @Test
    public void whenRegisterNewUserIsCalledWithIllegalConsentCodeThenResponseIsError() {
        setUpRegisterNewUserStage1(10000);

        verify(shnIntegerResultListener).onActionCompleted(-1, SHNResult.SHNInvalidParameterError);
    }

    @Test
    public void whenRegisterNewUserIsCalledWithLegalConsentCodeThenCorrectPackageIsWrittenToControlPoint() {
        setUpRegisterNewUserStage1(999);

        ArgumentCaptor<byte[]> byteArrayArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<SHNCommandResultReporter> shnCommandResultReporterArgumentCaptor = ArgumentCaptor.forClass(SHNCommandResultReporter.class);
        verify(mockedShnUserControlPointCharacteristic).write(byteArrayArgumentCaptor.capture(), shnCommandResultReporterArgumentCaptor.capture());

        assertNotNull(byteArrayArgumentCaptor.getValue());
        assertEquals(3, byteArrayArgumentCaptor.getValue().length);

        byte[] request = {OP_CODE_REGISTER_NEW_USER, (byte) 0xE7, 0x03}; // Review 0x03E7 == 999
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

        verify(shnIntegerResultListener).onActionCompleted(-1, SHNResult.SHNInvalidParameterError); // Review -1 magic number
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

        verify(shnIntegerResultListener).onActionCompleted(-1, SHNResult.SHNUnsupportedOperation);
    }

    @Test
    public void whenNewUserIsRegisteredWithResultOperationFailedThenUserIdIsNotReturned() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_OPERATION_FAILED};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(-1, SHNResult.SHNOperationFailed);
    }

    @Test
    public void whenNewUserIsRegisteredWithResultInvalidParameterThenUserIdIsNotReturned() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_INVALID_PARAMETER};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(-1, SHNResult.SHNInvalidParameterError);
    }

    @Test
    public void whenNewUserIsRegisteredAndResponseIsWithWrongOpResponseCodeThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE + 1, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS, 0x14};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(-1, SHNResult.SHNInvalidResponseError);
    }

    @Test
    public void whenNewUserIsRegisteredAndResponseIsWithWrongOpOperationCodeThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER + 1, RESPONSE_CODE_SUCCESS, 0x14};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(-1, SHNResult.SHNInvalidResponseError);
    }

    @Test
    public void whenNewUserIsRegisteredAndResponseIsInvalidThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, 0x14};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(-1, SHNResult.SHNInvalidResponseError);
    }

    @Test
    public void whenNewUserIsRegisteredAndResponseIsIncomplete1ThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(-1, SHNResult.SHNResponseIncompleteError);
    }

    @Test
    public void whenNewUserIsRegisteredAndResponseIsIncomplete2ThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(-1, SHNResult.SHNResponseIncompleteError);
    }

    @Test
    public void whenNewUserIsRegisteredAndResponseIsIncomplete3ThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        verify(shnIntegerResultListener).onActionCompleted(-1, SHNResult.SHNResponseIncompleteError);
    }

    @Test
    public void whenNewUserIsRegisteredWithResponseNotOkAndNotificationIsReceivedThenErrorIsReported() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS, 0x14};
        setUpRegisterNewUserStage3(999, SHNResult.SHNTimeoutError, response);

        verify(shnIntegerResultListener).onActionCompleted(-1, SHNResult.SHNTimeoutError);
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
        setUpRegisterNewUserStage2(consentCode, result);

        //had to make it protected so I can use this call in the tests. Is there a best way to do it?
        // Review: Yes you can capture it during the onServiceChangedCall of the service. It is the parameter of the setShnCharacteristicChangedListener call on the controlPoint characteristic.
        shnServiceUserData.shnCharacteristicChangedListener.onCharacteristicChanged(mockedShnUserControlPointCharacteristic, response);
    }

    @Test
    public void whenNewUserIsRegisteredThanCommandIsCreatedAndStarted() {
        setUpRegisterNewUserStage1(999);

        SHNServiceUserData.SHNUserDataCommand.Command[] commandsType = {SHNServiceUserData.SHNUserDataCommand.Command.REGISTER};
        assertQueueIsNotEmptyWithSize(1, commandsType); // Review for sequential readers helper function could be listed first...
        assertTrue(shnServiceUserData.executing); // review testing an internal. The external observable behaviour is that one command at a time is processed...
    }

    @Test
    public void whenNewUserIsRegisteredThanCommandIsNotFinishedAfterResponse() {
        setUpRegisterNewUserStage2(999, SHNResult.SHNOk);

        SHNServiceUserData.SHNUserDataCommand.Command[] commandsType = {SHNServiceUserData.SHNUserDataCommand.Command.REGISTER};
        assertQueueIsNotEmptyWithSize(1, commandsType);
        assertTrue(shnServiceUserData.executing);
    }

    private void assertQueueIsNotEmptyWithSize(int size, SHNServiceUserData.SHNUserDataCommand.Command[] commandsType) { // Review size is redundant. It equals the length of the commandsType array.
        assertNotNull(shnServiceUserData.commandQueue); // Review the queue should exist after creating the service
        assertEquals(size, shnServiceUserData.commandQueue.size());
        for (int i = 0; i < commandsType.length; i++) {
            assertEquals("Mismatch at command " + i, commandsType[i], shnServiceUserData.commandQueue.get(i).getType());
        }
    }

    @Test
    public void whenNewUserIsRegisteredThanCommandIsFinishedAfterNotification() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS, 0x14};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        assertQueueIsEmpty();
    }

    @Test
    public void whenNewUserIsRegisteredWithResultNotOkThanCommandIsFinishedAfterResponse() {
        setUpRegisterNewUserStage2(999, SHNResult.SHNTimeoutError);

        assertQueueIsEmpty();
    }

    private void assertQueueIsEmpty() {
        assertFalse(shnServiceUserData.executing);
        assertNotNull(shnServiceUserData.commandQueue);
        assertEquals(0, shnServiceUserData.commandQueue.size());
    }

    @Test
    public void whenNewUserIsRegisteredTwiceThenTwoTasksAreCreated() {
        setUpRegisterNewUserStage1(999);
        setUpRegisterNewUserStage1(888);

        SHNServiceUserData.SHNUserDataCommand.Command[] commandsType = {SHNServiceUserData.SHNUserDataCommand.Command.REGISTER, SHNServiceUserData.SHNUserDataCommand.Command.REGISTER};
        assertQueueIsNotEmptyWithSize(2, commandsType);
    }

    @Test
    public void whenNewUserIsRegisteredTwiceThenFirstIsExecuting() {
        setUpRegisterNewUserStage1(999);
        setUpRegisterNewUserStage1(888);

        assertTrue(shnServiceUserData.executing);
        SHNServiceUserData.SHNUserDataCommand command = shnServiceUserData.commandQueue.getFirst();
        assertEquals(SHNServiceUserData.SHNUserDataCommand.Command.REGISTER, command.getType());
        assertEquals(999, command.getConsentCode());
    }

    @Test
    public void whenNewUserIsRegisteredTwiceAndFirstIsNotifiedThenSecondCommandStartsExecuting() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS, 0x24};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response); // Review at this point, there is no command in the queue!
        setUpRegisterNewUserStage1(888);

        assertTrue(shnServiceUserData.executing);
        SHNServiceUserData.SHNUserDataCommand command = shnServiceUserData.commandQueue.getFirst();
        assertEquals(SHNServiceUserData.SHNUserDataCommand.Command.REGISTER, command.getType());
        assertEquals(888, command.getConsentCode());
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
        setUpConsentExistingUserStage2(userId, consentCode, result);

        //had to make it protected so I can use this call in the tests. Is there a best way to do it?
        shnServiceUserData.shnCharacteristicChangedListener.onCharacteristicChanged(mockedShnUserControlPointCharacteristic, response);
    }

    @Test
    public void whenConsentExistingUserIsCalledThanCommandIsCreatedAndStarted() {
        int userId = 10;
        setUpConsentExistingUserStage1(userId, 999);

        SHNServiceUserData.SHNUserDataCommand.Command[] commandsType = {SHNServiceUserData.SHNUserDataCommand.Command.CONSENT};
        assertQueueIsNotEmptyWithSize(1, commandsType);
        assertTrue(shnServiceUserData.executing);
    }

    @Test
    public void whenConsentExistingUserWithLegalDataThenCorrectPackageIsWrittenToControlPoint() {
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
    public void whenConsentExistingUserIsCalledWithLegalConsentCodeAndResultIsOkThenResponseIsNotSent() {
        setUpConsentExistingUserStage2(10, 999, SHNResult.SHNOk);

        verify(shnResultListener, never()).onActionCompleted(any(SHNResult.class));
    }

    @Test
    public void whenConsentExistingUserIsCalledWithLegalConsentCodeAndResultIsNotOkThenResponseIsSent() {
        setUpConsentExistingUserStage2(10, 999, SHNResult.SHNUnsupportedOperation);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNUnsupportedOperation);
    }

    @Test
    public void whenConsentExistingUserIsCalledWithIllegalConsentCodeThenResponseIsError() {
        setUpConsentExistingUserStage1(256, 999);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNInvalidParameterError);
    }

    @Test
    public void whenConsentExistingUserIsCalledThanCommandIsNotFinishedAfterResponse() {
        int userId = 10;
        setUpConsentExistingUserStage2(userId, 999, SHNResult.SHNOk);

        SHNServiceUserData.SHNUserDataCommand.Command[] commandsType = {SHNServiceUserData.SHNUserDataCommand.Command.CONSENT};
        assertQueueIsNotEmptyWithSize(1, commandsType);
    }

    @Test
    public void whenConsentExistingUserIsCalledWithResultOkThenResultIsReported() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_CONSENT, RESPONSE_CODE_SUCCESS};
        setUpConsentExistingUserStage3(10, 999, SHNResult.SHNOk, response);

        verify(shnResultListener).onActionCompleted(SHNResult.SHNOk);
    }

    @Test
    public void whenConsentExistingUserIsCalledWithResultOkAndNotificationHasWrongOpCodeThenErrorIsReported() {
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

        assertQueueIsEmpty();
    }

    @Test
    public void whenNewConsentExistingUserIsCalledWithResultNotOkThanCommandIsFinishedAfterResponse() {
        setUpConsentExistingUserStage2(10, 999, SHNResult.SHNTimeoutError);

        assertQueueIsEmpty();
    }

    @Test
    public void whenTwoTaskAreSentThenTwoCommandsAreCreated() {
        setUpRegisterNewUserStage1(999);
        setUpConsentExistingUserStage1(10, 999);

        SHNServiceUserData.SHNUserDataCommand.Command[] commandsType = {SHNServiceUserData.SHNUserDataCommand.Command.REGISTER, SHNServiceUserData.SHNUserDataCommand.Command.CONSENT};

        assertQueueIsNotEmptyWithSize(2, commandsType);
    }

    @Test
    public void whenTwoTaskAreSentThenFirstIsExecuting() {
        setUpRegisterNewUserStage1(999);
        setUpConsentExistingUserStage1(10, 999);

        assertTrue(shnServiceUserData.executing);
        SHNServiceUserData.SHNUserDataCommand command = shnServiceUserData.commandQueue.getFirst();
        assertEquals(SHNServiceUserData.SHNUserDataCommand.Command.REGISTER, command.getType());
        assertEquals(999, command.getConsentCode());
    }

    @Test
    public void whenTwoTaskAreSentAndFirstOneIsFinishedThenSecondIsExecuting() {
        setUpRegisterNewUserStage2(999, SHNResult.SHNOk);
        setUpConsentExistingUserStage1(10, 888);

        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS, 0x11};
        shnServiceUserData.shnCharacteristicChangedListener.onCharacteristicChanged(mockedShnUserControlPointCharacteristic, response);

        assertTrue(shnServiceUserData.executing);
        SHNServiceUserData.SHNUserDataCommand command = shnServiceUserData.commandQueue.getFirst();
        assertEquals(SHNServiceUserData.SHNUserDataCommand.Command.CONSENT, command.getType());
        assertEquals(888, command.getConsentCode());
    }

    @Test
    public void whenTwoTaskAreFinishedThanQueueIsEmpty() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS, 0x11};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        reset(mockedShnUserControlPointCharacteristic);
        byte[] response2 = {OP_CODE_RESPONSE, OP_CODE_CONSENT, RESPONSE_CODE_SUCCESS};
        setUpConsentExistingUserStage3(10, 888, SHNResult.SHNOk, response2);

        assertQueueIsEmpty();
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
        setUpDeleteUserStage2(result);

        //had to make it protected so I can use this call in the tests. Is there a best way to do it?
        shnServiceUserData.shnCharacteristicChangedListener.onCharacteristicChanged(mockedShnUserControlPointCharacteristic, response);
    }

    @Test
    public void whenDeleteUserIsCalledThanCommandIsCreatedAndStarted() {
        setUpDeleteUserStage1();

        SHNServiceUserData.SHNUserDataCommand.Command[] commandsType = {SHNServiceUserData.SHNUserDataCommand.Command.DELETE};
        assertQueueIsNotEmptyWithSize(1, commandsType);
        assertTrue(shnServiceUserData.executing);
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

        SHNServiceUserData.SHNUserDataCommand.Command[] commandsType = {SHNServiceUserData.SHNUserDataCommand.Command.DELETE};
        assertQueueIsNotEmptyWithSize(1, commandsType);
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
    public void whenNewDeleteUserIsCalledThenCommandIsFinishedAfterNotification() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_DELETE_USER_DATA, RESPONSE_CODE_SUCCESS, 0x14};
        setUpDeleteUserStage3(SHNResult.SHNOk, response);

        assertQueueIsEmpty();
    }

    @Test
    public void whenNewDeleteUserIsCalledWithResultNotOkThanCommandIsFinishedAfterResponse() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_DELETE_USER_DATA, RESPONSE_CODE_SUCCESS};
        setUpDeleteUserStage3(SHNResult.SHNTimeoutError, response);

        assertQueueIsEmpty();
    }

    @Test
    public void whenThreeTaskAreSentThenTwoCommandsAreCreated() { // Review Only two?? I expect three and so do you :-)
        setUpRegisterNewUserStage1(999);
        setUpConsentExistingUserStage1(10, 999);
        setUpDeleteUserStage1();

        SHNServiceUserData.SHNUserDataCommand.Command[] commandsType = {SHNServiceUserData.SHNUserDataCommand.Command.REGISTER, SHNServiceUserData.SHNUserDataCommand.Command.CONSENT,
                SHNServiceUserData.SHNUserDataCommand.Command.DELETE};

        assertQueueIsNotEmptyWithSize(3, commandsType);
    }

    @Test
    public void whenThreeTaskAreSentThenFirstIsExecuting() {
        setUpRegisterNewUserStage1(999);
        setUpConsentExistingUserStage1(10, 999);
        setUpDeleteUserStage1();

        assertTrue(shnServiceUserData.executing);
        SHNServiceUserData.SHNUserDataCommand command = shnServiceUserData.commandQueue.getFirst();
        assertEquals(SHNServiceUserData.SHNUserDataCommand.Command.REGISTER, command.getType());
        assertEquals(999, command.getConsentCode());
    }

    @Test
    public void whenThreeTaskAreSentAndFirstOneIsFinishedThenSecondIsExecuting() {
        setUpRegisterNewUserStage2(999, SHNResult.SHNOk);
        setUpConsentExistingUserStage1(10, 888);
        setUpDeleteUserStage1();

        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS, 0x11};
        shnServiceUserData.shnCharacteristicChangedListener.onCharacteristicChanged(mockedShnUserControlPointCharacteristic, response);

        assertTrue(shnServiceUserData.executing);
        SHNServiceUserData.SHNUserDataCommand command = shnServiceUserData.commandQueue.getFirst();
        assertEquals(SHNServiceUserData.SHNUserDataCommand.Command.CONSENT, command.getType());
        assertEquals(888, command.getConsentCode());

        SHNServiceUserData.SHNUserDataCommand.Command[] commandsType = {SHNServiceUserData.SHNUserDataCommand.Command.CONSENT, SHNServiceUserData.SHNUserDataCommand.Command.DELETE};

        assertQueueIsNotEmptyWithSize(2, commandsType);
    }

    @Test
    public void whenTwoTasksAreFinishedThanQueueIsEmpty() {
        byte[] response = {OP_CODE_RESPONSE, OP_CODE_REGISTER_NEW_USER, RESPONSE_CODE_SUCCESS, 0x11};
        setUpRegisterNewUserStage3(999, SHNResult.SHNOk, response);

        reset(mockedShnUserControlPointCharacteristic);
        byte[] response2 = {OP_CODE_RESPONSE, OP_CODE_CONSENT, RESPONSE_CODE_SUCCESS};
        setUpDeleteUserStage3(SHNResult.SHNOk, response2);

        assertQueueIsEmpty();
    }
}