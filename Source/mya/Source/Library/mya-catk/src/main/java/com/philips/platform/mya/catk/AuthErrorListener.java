/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk;

import com.android.volley.VolleyError;

public interface AuthErrorListener {

    void onAuthError(NetworkAbstractModel model, VolleyError error);

}
