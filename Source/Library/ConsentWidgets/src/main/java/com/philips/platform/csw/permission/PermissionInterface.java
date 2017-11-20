package com.philips.platform.csw.permission;

import android.support.annotation.NonNull;

import java.util.List;

public interface PermissionInterface {

    void showProgressDialog();

    void hideProgressDialog();

    void onConsentRetrieved(@NonNull final List<ConsentView> consent);
}
