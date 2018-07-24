/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

public enum StreamIdentifier {
    UNKNOWN(-1),
    STREAM_0(0),
    STREAM_1(1),
    STREAM_2(2);

    private int value;
    StreamIdentifier(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static StreamIdentifier fromValue(int value) {
        for (StreamIdentifier identifier : values()) {
            if (identifier.getValue() == value) {
                return identifier;
            }
        }
        return UNKNOWN;
    }
}
