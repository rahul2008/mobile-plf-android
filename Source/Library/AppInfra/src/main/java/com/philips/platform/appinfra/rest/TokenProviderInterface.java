/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.rest;

/**
 * The interface for Token Type and Token .
 */
public interface TokenProviderInterface {

    /**
     * The enum Token type.
     */
    enum TokenType {
        /**
         * Oauth 2 token type.
         */
        OAUTH2}; // other types are HTTP Basic and HTTP Digest, but there are currently not supported

    /**
     * The interface Token.
     */
    interface Token {
        /**
         * Gets token type.
         *
         * @return the token type
         * @since 1.0.0
         */
        TokenType getTokenType();

        /**
         * Gets token value.
         *
         * @return the token value
         * @since 1.0.0
         */
        String getTokenValue();
    }

    /**
     * Gets token.
     *
     * @return the token
     * @since 1.0.0
     */
    Token getToken();


}
