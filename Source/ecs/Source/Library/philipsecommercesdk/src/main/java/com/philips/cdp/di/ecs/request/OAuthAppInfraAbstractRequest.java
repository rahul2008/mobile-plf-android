package com.philips.cdp.di.ecs.request;

import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.platform.appinfra.rest.TokenProviderInterface;

public abstract class OAuthAppInfraAbstractRequest extends AppInfraAbstractRequest {

    @Override
    public Token getToken() {
        return new Token() {
            @Override
            public TokenType getTokenType() {
                return TokenType.OAUTH2;
            }

            @Override
            public String getTokenValue() {
                return ECSConfig.INSTANCE.getAccessToken();
            }
        };
    }

    @Override
    public TokenProviderInterface getTokenProviderInterface() {
        return this::getToken;
    }
}
