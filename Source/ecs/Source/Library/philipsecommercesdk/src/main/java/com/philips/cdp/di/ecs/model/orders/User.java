package com.philips.cdp.di.ecs.model.orders;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String uid;

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }
}
