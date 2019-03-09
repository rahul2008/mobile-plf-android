package com.philips.platform.pim.rest;

import java.util.Map;

public interface PimRestClientInterface {

    String getUrl();

    Map<String, String> getHeader();

    byte[] getBody();

    int getMethodType();

}
