/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.rest;


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
    public interface Token {
        /**
         * Gets token type.
         *
         * @return the token type
         */
        TokenType getTokenType();

        /**
         * Gets token value.
         *
         * @return the token value
         */
        String getTokenValue();
    }

    /**
     * Gets token.
     *
     * @return the token
     */
    Token getToken();


}
