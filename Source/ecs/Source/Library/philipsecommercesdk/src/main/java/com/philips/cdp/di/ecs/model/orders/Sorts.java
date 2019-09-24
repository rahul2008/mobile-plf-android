package com.philips.cdp.di.ecs.model.orders;

import java.io.Serializable;

public class Sorts implements Serializable {
    private String code;
    private boolean selected;

    public String getCode() {
        return code;
    }

    public boolean isSelected() {
        return selected;
    }

}
