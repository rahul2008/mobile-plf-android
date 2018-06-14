/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.workarounds;

public enum OS {
    LOLLIPOP(21, 22),
    MARSHMALLOW(23),
    NOUGAT(24, 25),
    OREO(26);

    private int[] versions;

    public int[] geVersions() {
        return versions;
    }

    OS(int... versions) {
        this.versions = versions;
    }
}
