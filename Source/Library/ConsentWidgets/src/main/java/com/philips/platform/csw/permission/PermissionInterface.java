package com.philips.platform.csw.permission;

import android.support.annotation.NonNull;

public interface PermissionInterface {

    void showProgressDialog();

    void hideProgressDialog();

    void onConsentRetrieved(@NonNull final ConsentView consent);
}
