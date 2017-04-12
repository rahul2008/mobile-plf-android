/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.uid;

public class Property {
    String item;

    String value;

    String type;

    @Override
    public String toString() {
        return "Property [item = " + item + ", value = " + value + ", type = " + type + "]";
    }
}
