/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.model;

import android.os.Bundle;

public interface ModelQuery {
    String getUrl(int requestCode);

    Object parseResponse(Object response);

    int getMethod(int requestCode);

    Bundle requestBody();
}
