package com.philips.cdp.di.iap.response.payment;

import java.io.Serializable;

public class CardType implements Serializable{
    private static final long serialVersionUID = 8160886424090692482L;
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
