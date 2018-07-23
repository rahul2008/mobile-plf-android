package com.philips.pins.shinelib.protocols.moonshinestreaming;

public class SHNProtocolInformation {

    public static final int PROTOCOL_VERSION_OFFSET = 0;
    public static final int TX_WINDOW_SIZE_OFFSET = 1;
    public static final int RX_WINDOW_SIZE_OFFSET = 2;
    public final int protocolVersion;
    public final int txWindowSize;
    public final int rxWindowSize;

    private SHNProtocolInformation(int protocolVersion, int txWindowSize, int rxWindowSize) {
        this.protocolVersion = protocolVersion;
        this.txWindowSize = txWindowSize;
        this.rxWindowSize = rxWindowSize;
    }

    public static SHNProtocolInformation createFromData(byte[] data) {
        SHNProtocolInformation shnProtocolInformation = null;
        if (data != null && data.length == 3) {
            shnProtocolInformation =
                new SHNProtocolInformation(data[PROTOCOL_VERSION_OFFSET], data[TX_WINDOW_SIZE_OFFSET],
                    data[RX_WINDOW_SIZE_OFFSET]);
        }
        return shnProtocolInformation;
    }
}
