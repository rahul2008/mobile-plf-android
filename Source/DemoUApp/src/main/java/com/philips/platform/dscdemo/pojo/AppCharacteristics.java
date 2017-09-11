package com.philips.platform.dscdemo.pojo;

import java.io.Serializable;
import java.util.List;

public class AppCharacteristics implements Serializable {
    private String type;
    private String value;

    private List<AppCharacteristics> characteristics;

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setCharacteristics(List<AppCharacteristics> characteristics) {
        this.characteristics = characteristics;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public List<AppCharacteristics> getCharacteristics() {
        return characteristics;
    }
}