package com.philips.platform.appinfra.rest;

/**
 * Created by 310238114 on 9/13/2016.
 */
public interface TokenProviderInterface {

    enum TokenType {OAUTH2}; // other types are HTTP Basic and HTTP Digest, but there are currently not supported
    public interface Token {
        TokenType getTokenType();
        String getTokenValue();
    }
    Token getToken();


}
