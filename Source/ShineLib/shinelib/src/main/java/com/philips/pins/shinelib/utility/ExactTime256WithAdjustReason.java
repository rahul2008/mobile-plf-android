/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import java.nio.ByteBuffer;

public class ExactTime256WithAdjustReason {
    public final ExactTime256 exactTime256;
    public final AdjustReason adjustReason;

    public ExactTime256WithAdjustReason(ByteBuffer byteBuffer) {
        exactTime256 = new ExactTime256(byteBuffer);
        adjustReason = new AdjustReason(byteBuffer);
    }
}
