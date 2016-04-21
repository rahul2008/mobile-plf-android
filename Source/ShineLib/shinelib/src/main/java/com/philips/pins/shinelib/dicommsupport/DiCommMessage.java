package com.philips.pins.shinelib.dicommsupport;

import android.support.annotation.NonNull;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;

class DiCommMessage {

    private static final byte FIRST_START_BYTE = (byte) 0xFE;
    private static final byte SECOND_START_BYTE = (byte) 0xFF;
    private static final int HEADER_SIZE = 5;

    private final MessageType messageTypeIdentifier;
    private final byte[] data;

    public DiCommMessage(@NonNull byte[] data) throws InvalidParameterException {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);

            if (byteBuffer.get() != FIRST_START_BYTE || byteBuffer.get() != SECOND_START_BYTE) {
                throw new InvalidParameterException();
            }

            this.messageTypeIdentifier = MessageType.fromByte(byteBuffer.get());
            int length = byteBuffer.getShort();

            this.data = new byte[byteBuffer.remaining()];
            byteBuffer.get(this.data);

            if (this.data.length != length) {
                throw new InvalidParameterException();
            }
        } catch (BufferUnderflowException e) {
            throw new InvalidParameterException(e.getMessage());
        }
    }

    public DiCommMessage(@NonNull MessageType messageTypeIdentifier, @NonNull byte[] data) {
        this.messageTypeIdentifier = messageTypeIdentifier;
        this.data = data;
    }

    public byte[] toData() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(HEADER_SIZE + data.length);
        byteBuffer.put(FIRST_START_BYTE);
        byteBuffer.put(SECOND_START_BYTE);
        byteBuffer.put(messageTypeIdentifier.getByte());
        byteBuffer.putShort((short) data.length);
        byteBuffer.put(data);

        return byteBuffer.array();
    }

    public MessageType getMessageTypeIdentifier() {
        return messageTypeIdentifier;
    }

    public byte[] getData() {
        return data;
    }
}
