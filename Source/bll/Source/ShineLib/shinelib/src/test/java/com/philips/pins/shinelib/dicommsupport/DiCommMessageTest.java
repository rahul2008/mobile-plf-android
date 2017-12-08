package com.philips.pins.shinelib.dicommsupport;

import com.philips.pins.shinelib.dicommsupport.exceptions.IncompleteMessageException;
import com.philips.pins.shinelib.dicommsupport.exceptions.StartBytesNotFoundException;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class DiCommMessageTest {

    private static final int HEADER_LENGTH = 5;

    private final byte firstDataByte = (byte) 0xCA;
    private final byte secondDataByte = (byte) 0xFE;
    private final byte thirdByte = (byte) 0xFE;
    private final byte endByte = (byte) 0x00;

    @Test
    public void canBeConstructedFromData() {
        byte[] data = {(byte) 0xFE, (byte) 0xFF, (byte) 0, (byte) 0, (byte) 0};
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);

        new DiCommMessage(byteBuffer);
    }

    @Test(expected = StartBytesNotFoundException.class)
    public void throwsErrorWhenDataHeaderIsInvalid() {
        byte[] data = new byte[3];
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);

        new DiCommMessage(byteBuffer);
    }

    @Test(expected = IncompleteMessageException.class)
    public void throwsErrorWhenDataHeaderSecondIsInvalid() {
        byte[] data = {(byte) 0xFE};
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);

        new DiCommMessage(byteBuffer);
    }

    @Test(expected = IncompleteMessageException.class)
    public void throwsErrorWhenDataHeaderDoesNotContainType() {
        byte[] data = {(byte) 0xFE, (byte) 0xFF};
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);

        new DiCommMessage(byteBuffer);
    }

    @Test(expected = IncompleteMessageException.class)
    public void throwsErrorWhenDataHeaderDoesNotContainLength() {
        byte[] data = {(byte) 0xFE, (byte) 0xFF, (byte) 3};

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);

        new DiCommMessage(byteBuffer);
    }

    @Test
    public void whenPutPropRequestTypeReceivedWhenMessageTypeIsPutPropRequest() {
        byte[] data = {(byte) 0xFE, (byte) 0xFF, (byte) 3, (byte) 0, (byte) 0};

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        DiCommMessage message = new DiCommMessage(byteBuffer);

        assertEquals(MessageType.PutPropsRequest, message.getMessageType());
    }

    @Test
    public void whenGetPropRequestTypeReceivedWhenMessageTypeIsGetPropRequest() {
        byte[] data = {(byte) 0xFE, (byte) 0xFF, (byte) 4, (byte) 0, (byte) 0};

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        DiCommMessage message = new DiCommMessage(byteBuffer);

        assertEquals(MessageType.GetPropsRequest, message.getMessageType());
    }

    @Test
    public void whenMessageIsReceivedThenPayloadIsExtracted() {
        byte[] data = {(byte) 0xFE, (byte) 0xFF, (byte) 4, (byte) 0, (byte) 2, firstDataByte, endByte};

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        DiCommMessage message = new DiCommMessage(byteBuffer);
        assertEquals(firstDataByte, message.getPayload()[0]);
        assertEquals(endByte, message.getPayload()[1]);
    }

    @Test(expected = IncompleteMessageException.class)
    public void throwsErrorWhenMessageIsShorterThanExpected() {
        byte[] data = {(byte) 0xFE, (byte) 0xFF, (byte) 4, (byte) 0, (byte) 2, firstDataByte};

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        new DiCommMessage(byteBuffer);
    }

    @Test
    public void canExtractMessageWhenLongerThanExpected() {
        byte[] data = {(byte) 0xFE, (byte) 0xFF, (byte) 4, (byte) 0, (byte) 2, firstDataByte, endByte, thirdByte};

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        new DiCommMessage(byteBuffer);
    }

    @Test
    public void canBeConstructedWithTypeAndData() {
        byte[] data = {firstDataByte, secondDataByte};

        DiCommMessage message = new DiCommMessage(MessageType.PutPropsRequest, data);

        assertEquals(MessageType.PutPropsRequest, message.getMessageType());
        assertEquals(data[0], message.getPayload()[0]);
        assertEquals(data[1], message.getPayload()[1]);
    }

    @Test
    public void whenConvertedToBytesThenHasProperHeader() {
        byte[] data = {(byte) 0xCA, (byte) 0xFE};

        DiCommMessage message = new DiCommMessage(MessageType.PutPropsRequest, data);

        byte[] bytes = message.toData();
        assertEquals((byte) 0xFE, bytes[0]);
        assertEquals((byte) 0xFF, bytes[1]);
    }

    @Test
    public void whenPutPropsRequestConvertedToBytesThenHeaderHasRightType() {
        byte[] data = {(byte) 0xCA, (byte) 0xFE};

        MessageType putPropsRequest = MessageType.PutPropsRequest;
        DiCommMessage message = new DiCommMessage(putPropsRequest, data);

        byte[] bytes = message.toData();
        assertEquals(putPropsRequest.getDiCommMessageTypeCode(), bytes[2]);
    }

    @Test
    public void whenGetPropsRequestConvertedToBytesThenHeaderHasRightType() {
        byte[] data = {(byte) 0xCA, (byte) 0xFE};

        MessageType getPropsRequest = MessageType.GetPropsRequest;
        DiCommMessage message = new DiCommMessage(getPropsRequest, data);

        byte[] bytes = message.toData();
        assertEquals(getPropsRequest.getDiCommMessageTypeCode(), bytes[2]);
    }

    @Test
    public void whenConvertedToBytesThenHasDataLength() {
        byte[] data = {(byte) 0xCA, (byte) 0xFE};

        DiCommMessage message = new DiCommMessage(MessageType.GetPropsRequest, data);

        byte[] bytes = message.toData();
        assertEquals((byte) 0, bytes[3]);
        assertEquals((byte) 2, bytes[4]);
    }

    @Test
    public void whenConvertedToBytesThenHasData() {
        byte[] data = {(byte) 0xCA, (byte) 0xFE};

        DiCommMessage message = new DiCommMessage(MessageType.GetPropsRequest, data);

        byte[] bytes = message.toData();
        assertEquals(data[0], bytes[5]);
        assertEquals(data[1], bytes[6]);
    }

    @Test
    public void whenConvertedToBytesThenTotalLengthEqualsHeaderLengthPlusDataLength() {
        byte[] data = {(byte) 0xCA, (byte) 0xFE};

        DiCommMessage message = new DiCommMessage(MessageType.GetPropsRequest, data);

        byte[] bytes = message.toData();
        assertEquals(HEADER_LENGTH + data.length, bytes.length);
    }

    @Test
    public void whenConvertedToBytesWithNoDataThenTotalLengthEqualsHeaderLength() {
        byte[] data = {};

        DiCommMessage message = new DiCommMessage(MessageType.GetPropsRequest, data);

        byte[] bytes = message.toData();
        assertEquals(HEADER_LENGTH, bytes.length);
    }

    @Test
    public void whenGetTotalDataSizeIsCalledThenTotalMessageSizeIsReturned() {
        int length = 3;
        byte[] data = {(byte) 0xFE, (byte) 0xFF, (byte) 4, (byte) 0, (byte) length, firstDataByte, secondDataByte, endByte};

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        DiCommMessage message = new DiCommMessage(byteBuffer);

        assertEquals(HEADER_LENGTH + length, message.getTotalDataSize());
    }

    @Test
    public void whenGetTotalDataSizeIsCalledThenTotalMessageSizeIsReturned2() {
        byte[] data = {firstDataByte, secondDataByte, thirdByte};

        DiCommMessage diCommMessage = new DiCommMessage(MessageType.PutPropsRequest, data);

        assertEquals(HEADER_LENGTH + data.length, diCommMessage.getTotalDataSize());
    }
}
