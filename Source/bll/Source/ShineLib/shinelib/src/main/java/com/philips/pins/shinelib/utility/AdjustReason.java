/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import java.nio.ByteBuffer;

public class AdjustReason {
    public final boolean manualTimeUpdate;
    public final boolean externalReferenceTimeUpdate;
    public final boolean changeOfTimeZone;
    public final boolean changeOfDST;

    public AdjustReason(ByteBuffer byteBuffer) {
        byte value = byteBuffer.get();
        manualTimeUpdate            = ((value & 0x01) != 0) ? true : false;
        externalReferenceTimeUpdate = ((value & 0x02) != 0) ? true : false;
        changeOfTimeZone            = ((value & 0x04) != 0) ? true : false;
        changeOfDST                 = ((value & 0x08) != 0) ? true : false;
    }
}
