package com.philips.pins.shinelib.utility;

import java.nio.ByteBuffer;

/**
 * Created by 310188215 on 07/07/15.
 */
public class ExactTime256WithAdjustReason {
    public final ExactTime256 exactTime256;
    public final AdjustReason adjustReason;

    public ExactTime256WithAdjustReason(ByteBuffer byteBuffer) {
        exactTime256 = new ExactTime256(byteBuffer);
        adjustReason = new AdjustReason(byteBuffer);
    }
}
