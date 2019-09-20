package com.philips.cdp.di.ecs.request;

import com.philips.cdp.di.ecs.util.ECSConfiguration;
import com.philips.platform.appinfra.rest.TokenProviderInterface;

import java.util.HashMap;
import java.util.Map;

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
                return ECSConfiguration.INSTANCE.getAccessToken();
            }
        };
    }

    @Override
    public TokenProviderInterface getTokenProviderInterface() {
        return this;
    }

    @Override
    public Map<String, String> getHeader() {
        HashMap<String, String> authMap =new HashMap<String, String>();
        authMap.put("Authorization","Bearer " + ECSConfiguration.INSTANCE.getAccessToken());
        return authMap;
    }

}