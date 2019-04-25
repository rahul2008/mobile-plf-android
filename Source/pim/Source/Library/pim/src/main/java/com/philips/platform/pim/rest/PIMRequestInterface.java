package com.philips.platform.pim.rest;

import java.util.Map;

public interface PIMRequestInterface {
    int METHOD_TYPE_GET = 0;

    String getUrl();

    Map<String, String> getHeader();

    byte[] getBody();

    int getMethodType();

}
