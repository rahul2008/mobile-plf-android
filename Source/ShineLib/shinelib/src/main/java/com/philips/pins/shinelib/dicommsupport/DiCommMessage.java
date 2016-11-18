/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.dicommsupport;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.dicommsupport.exceptions.IncompleteMessageException;
import com.philips.pins.shinelib.dicommsupport.exceptions.InvalidMessageTerminationException;
import com.philips.pins.shinelib.dicommsupport.exceptions.StartBytesNotFoundException;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class DiCommMessage {

    private static final int HEADER_SIZE = 5;
    private static final byte FIRST_START_BYTE = (byte) 0xFE;
    private static final byte SECOND_START_BYTE = (byte) 0xFF;
    private static final byte END_BYTE = (byte) 0x00;

    private final MessageType messageType;
    private final byte[] mPayload;

    public DiCommMessage(@NonNull ByteBuffer byteBuffer) throws StartBytesNotFoundException, IncompleteMessageException, InvalidMessageTerminationException {
        try {
            if (byteBuffer.get() != FIRST_START_BYTE || byteBuffer.get() != SECOND_START_BYTE) {
                throw new StartBytesNotFoundException();
            }

            this.messageType = MessageType.fromDiCommMessageTypeCode(byteBuffer.get());
            int payloadLength = byteBuffer.getShort() & 0xffff; // convert to unsigned int, no negatives allowed.

            mPayload = new byte[payloadLength];
            byteBuffer.get(mPayload);

            if (payloadLength > 0 && mPayload[payloadLength - 1] != END_BYTE) {
                throw new InvalidMessageTerminationException();
            }
        } catch (BufferUnderflowException e) {
            throw new IncompleteMessageException(e);
        }
    }

    public DiCommMessage(@NonNull MessageType messageType, @NonNull byte[] payload) {
        this.messageType = messageType;
        this.mPayload = payload;
    }

    public byte[] toData() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(HEADER_SIZE + mPayload.length);
        byteBuffer.put(FIRST_START_BYTE);
        byteBuffer.put(SECOND_START_BYTE);
        byteBuffer.put(messageType.getDiCommMessageTypeCode());
        byteBuffer.putShort((short) mPayload.length);
        byteBuffer.put(mPayload);

        return byteBuffer.array();
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public byte[] getPayload() {
        return mPayload;
    }

    public int getTotalDataSize() {
        return HEADER_SIZE + mPayload.length;
    }
}
