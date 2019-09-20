package com.philips.cdp.di.ecs.model.retailers;

import java.io.Serializable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TextEntity implements Serializable {
    private String Key;
    private String Value;

    public String getKey() {
        return Key;
    }

    public String getValue() {
        return Value;
    }
}
