/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.rest.request;


import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Response;
import com.philips.platform.appinfra.rest.ServiceIDUrlFormatting;

public class ImageRequest extends com.android.volley.toolbox.ImageRequest {

    public ImageRequest(String url, Response.Listener<Bitmap> listener, int maxWidth,
                          int maxHeight, ImageView.ScaleType scaleType, Bitmap.Config decodeConfig, Response.ErrorListener errorListener) throws HttpForbiddenException {
        super(url, listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);
        if (!url.contains("https")) {
            throw new HttpForbiddenException();
        }
    }
    public ImageRequest(String serviceID, ServiceIDUrlFormatting.SERVICEPREFERENCE pref, String urlExtension, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight,
                        ImageView.ScaleType scaleType, Bitmap.Config decodeConfig, Response.ErrorListener errorListener) throws HttpForbiddenException {
        super(ServiceIDUrlFormatting.formatUrl(serviceID, pref, urlExtension), listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);
    }
}
