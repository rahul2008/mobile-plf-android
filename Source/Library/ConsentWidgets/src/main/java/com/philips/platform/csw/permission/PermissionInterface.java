package com.philips.platform.csw.permission;

import android.support.annotation.NonNull;

import com.philips.platform.catk.model.Consent;

import java.util.List;

public interface PermissionInterface {

    void showProgressDialog();

    void hideProgressDialog();

    void onConsentsRetrieved(@NonNull final List<Consent> consentList);

}
