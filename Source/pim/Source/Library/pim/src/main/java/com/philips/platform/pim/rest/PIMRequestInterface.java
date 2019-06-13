package com.philips.platform.pim.rest;

import java.util.Map;

public interface PIMRequestInterface {

    String getUrl();

    Map<String, String> getHeader();

    Map<String, String> getParams();

    String getBody();

    int getMethodType();

}
