/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.workarounds;

enum OS {
    Lollipop(21, 22),
    Marshmallow(23),
    Nougat(24, 25),
    Oreo(26);

    private int[] versions;

    public int[] geVersions() {
        return versions;
    }

    OS(int... versions) {
        this.versions = versions;
    }
}
