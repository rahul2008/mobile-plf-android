package com.philips.pins.shinelib.dicommsupport;

import android.support.annotation.NonNull;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;

class DiCommMessage {

    public enum MessageType {

        InitializeRequest(1),
        PutPropsRequest(3),
        GetPropsRequest(4),
        SubscribeRequest(5),
        UnsubscribeRequest(6),
        ChangeIndicationRequest(8),
        GetProdsRequest(10),
        GetPortsRequest(11),
        AddPropsRequest(12),
        DelPropsRequest(13),
        RawRequest(15),
        // ExecMethodRequest( 3 Clashes with PutProps?

        // Responses
        GenericResponse(7),
        InitializeResponse(2),

        // Events
        ChangeIndicationEvent(9);

        private byte bite;

        MessageType(int bite) {
            this.bite = (byte) bite;
        }

        public byte getByte() {
            return bite;
        }

        public static MessageType fromByte(byte type) {
            for (MessageType messageType : MessageType.values()) {
                if (messageType.getByte() == type) {
                    return messageType;
                }
            }

            return null;
        }
    }

    public static final byte FIRST_BYTE = (byte) 0xFE;
    public static final byte SECOND_BYTE = (byte) 0xFF;

    private final MessageType type;
    private final byte[] data;

    public DiCommMessage(@NonNull byte[] data) throws InvalidParameterException {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);

            if (byteBuffer.get() != FIRST_BYTE || byteBuffer.get() != SECOND_BYTE) {
                throw new InvalidParameterException();
            }

            this.type = MessageType.fromByte(byteBuffer.get());
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

    public DiCommMessage(@NonNull MessageType type, @NonNull byte[] data) {
        this.type = type;
        this.data = data;
    }

    public byte[] toData() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(5 + data.length);
        byteBuffer.put(FIRST_BYTE);
        byteBuffer.put(SECOND_BYTE);
        byteBuffer.put(type.getByte());
        byteBuffer.putShort((short) data.length);
        byteBuffer.put(data);

        return byteBuffer.array();
    }

    public MessageType getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }
}
