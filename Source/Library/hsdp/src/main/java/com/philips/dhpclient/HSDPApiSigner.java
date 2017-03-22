package com.philips.dhpclient;

import com.philips.platform.appinfra.apisigning.HSDPPHSApiSigning;

import java.util.Map;


public class HSDPApiSigner extends HSDPPHSApiSigning implements ApiSigner {


    public HSDPApiSigner(String sharedKey, String hexSecretKey) {
        super(sharedKey, hexSecretKey);
    }

    @Override
    public String createHeader(String httpMethod, String queryParams, Map<String, String> headers, String url, String body) {
        String header = createSignature(httpMethod, queryParams, headers, url, body);
        return header;
    }
}
