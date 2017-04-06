/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer.appliance.property;

import android.text.TextUtils;

public class Property
{
    private String name;
    private String key;
    private PropertyType type;
    private String valueText;

    public Property(String name, String key, PropertyType type) {
        this.name = name;
        this.key = key;
        this.type = type;
    }

    public Property() {
        this("", "", PropertyType.STRING);
    }

    public void setType(PropertyType type) {
        this.type = type;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PropertyType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getValueText() {
        if(TextUtils.isEmpty(valueText)) {
            return "";
        }
        return valueText;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
    }
}
