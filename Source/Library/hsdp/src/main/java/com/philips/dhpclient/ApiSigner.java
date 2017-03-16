package com.philips.dhpclient;

import java.util.Map;

public interface ApiSigner {

    String createHeader(String httpMethod, String queryParams, Map<String, String> headers, String url, String body);
}
