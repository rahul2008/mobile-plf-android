package com.philips.platform.appinfra.rest.request;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

/**
 * Created by 310243577 on 10/27/2016.
 */

public class AIImageRequest extends ImageRequest {
    public AIImageRequest(String url, Response.Listener<Bitmap> listener, int maxWidth,
                          int maxHeight, ImageView.ScaleType scaleType, Bitmap.Config decodeConfig, Response.ErrorListener errorListener) throws HttpForbiddenException {
        super(url, listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);
        if (!url.contains("https")) {
            throw new HttpForbiddenException();
        }
    }

    public AIImageRequest(String url, Response.Listener<Bitmap> listener, int maxWidth,
                          int maxHeight, Bitmap.Config decodeConfig, Response.ErrorListener errorListener) throws HttpForbiddenException {
        super(url, listener, maxWidth, maxHeight, decodeConfig, errorListener);
        if (!url.contains("https")) {
            throw new HttpForbiddenException();
        }
    }
}
