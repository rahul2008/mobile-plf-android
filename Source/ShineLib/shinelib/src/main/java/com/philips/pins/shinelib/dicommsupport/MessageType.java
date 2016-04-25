package com.philips.pins.shinelib.dicommsupport;

 enum  MessageType {
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

    private byte diCommMessageTypeCode;

    MessageType(int diCommMessageTypeCode) {
        this.diCommMessageTypeCode = (byte) diCommMessageTypeCode;
    }

    public byte getDiCommMessageTypeCode() {
        return diCommMessageTypeCode;
    }

    public static MessageType fromDiCommMessageTypeCode(byte diCommMessageTypeCode) {
        for (MessageType messageType : MessageType.values()) {
            if (messageType.getDiCommMessageTypeCode() == diCommMessageTypeCode) {
                return messageType;
            }
        }

        return null;
    }
}
