package com.philips.platform.dscdemo.pojo;

import java.io.Serializable;
import java.util.List;

public class AppUserCharacteristics implements Serializable {

    private List<AppCharacteristics> characteristics;

    public void setCharacteristics(List<AppCharacteristics> characteristics) {
        this.characteristics = characteristics;
    }

    public List<AppCharacteristics> getCharacteristics() {
        return characteristics;
    }

}