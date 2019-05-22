package com.philips.platform.pim.rest;

import java.util.Map;

public interface PIMRequestInterface {

    String getUrl();

    Map<String, String> getHeader();

    String getBody();

    int getMethodType();

}
