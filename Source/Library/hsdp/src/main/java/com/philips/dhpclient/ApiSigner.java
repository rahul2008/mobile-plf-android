package com.philips.dhpclient;

import java.util.Map;

public interface ApiSigner {

    String createHeader(String httpMethod, String queryParams, Map<String, String> headers, String url, String body);

    class Get {
        public static ApiSigner signer(String signingKey, String signingSecret) {

            if (signingKey == null || signingSecret == null)
                throw new IllegalArgumentException("Missing authentication signing keys");

            if(signingSecret.length() == 128) {
                return new HSDPApiSigner(signingKey, signingSecret);
            }
            return new DhpApiSigner(signingKey, signingSecret);
        }
    }
}
