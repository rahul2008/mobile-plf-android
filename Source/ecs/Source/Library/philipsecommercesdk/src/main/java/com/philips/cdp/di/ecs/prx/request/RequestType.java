package com.philips.cdp.di.ecs.prx.request;




public enum RequestType {

    GET(0),
    POST(1);

    private int value;


    RequestType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
