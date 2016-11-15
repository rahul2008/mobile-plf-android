package com.philips.platform.appinfra.rest.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.philips.platform.appinfra.rest.ServiceIDUrlFormatting;
import com.philips.platform.appinfra.rest.TokenProviderInterface;

import java.util.Map;

/**
 * Created by 310243577 on 11/3/2016.
 */

public class StringRequest extends com.android.volley.toolbox.StringRequest {
    private Map<String, String> mHeader;
    private TokenProviderInterface mProvider;

    public StringRequest(int method, String url, Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public StringRequest(int method, String url, TokenProviderInterface tokenProviderInterface,
                         Map<String, String> header, Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.mProvider = tokenProviderInterface;

        this.mHeader = header;
    }


    public StringRequest(int method, String serviceID, ServiceIDUrlFormatting.SERVICEPREFERENCE pref,
                         String urlExtension, Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {
        super(method, ServiceIDUrlFormatting.formatUrl(serviceID, pref, urlExtension), listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (mProvider != null && mHeader != null) {
            TokenProviderInterface.Token token = mProvider.getToken();
            String scheme = "";
            if (token.getTokenType() == TokenProviderInterface.TokenType.OAUTH2)
                scheme = "Bearer";
            else
                throw new IllegalArgumentException("unsupported token type");
            mHeader.put("Authorization", scheme + " " + token.getTokenValue());
            return mHeader;

        }
        return null;
    }
}
