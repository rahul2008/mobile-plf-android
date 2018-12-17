/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.rest.request;


import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.philips.platform.appinfra.rest.RestManager;
import com.philips.platform.appinfra.rest.ServiceIDUrlFormatting;
import com.philips.platform.appinfra.rest.TokenProviderInterface;

import java.util.Map;

/**
 * A wrapper class of canned request for getting an image at a given URL and calling
 * back with a decoded Bitmap.
 */
public class ImageRequest extends com.android.volley.toolbox.ImageRequest {
    private Map<String, String> mHeader;
    private TokenProviderInterface mProvider;
    private Map<String,String> mParams;

    public ImageRequest(String url, Response.Listener<Bitmap> listener, int maxWidth,
                        int maxHeight, ImageView.ScaleType scaleType, Bitmap.Config decodeConfig,
                        Response.ErrorListener errorListener,  Map<String, String> header,Map<String, String> params,
                        TokenProviderInterface tokenProviderInterface)  {
        super(url, listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);
        this.mProvider = tokenProviderInterface;
        this.mHeader = header;
        this.mParams = params;
        VolleyLog.DEBUG = false;
//        Log.v(AppInfraLogEventID.AI_REST, "Image Request");
    }

    public ImageRequest(String serviceID, ServiceIDUrlFormatting.SERVICEPREFERENCE pref, String urlExtension, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight,
                        ImageView.ScaleType scaleType, Bitmap.Config decodeConfig, Response.ErrorListener errorListener)  {
        super(ServiceIDUrlFormatting.formatUrl(serviceID, pref, urlExtension), listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);
        VolleyLog.DEBUG = false;
//        Log.v(AppInfraLogEventID.AI_REST, "Image Request");
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if(mHeader != null) {
            if (mProvider != null) {
                final Map<String, String> tokenHeader = RestManager.setTokenProvider(mProvider);
                mHeader.putAll(tokenHeader);
            }
//            Log.v(AppInfraLogEventID.AI_REST, "Image Request get Headers"+mHeader);
            return mHeader;
        }
        return super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams()
            throws AuthFailureError {
        if (mParams != null) {
            return mParams;
        }

        return super.getParams();
    }
}
