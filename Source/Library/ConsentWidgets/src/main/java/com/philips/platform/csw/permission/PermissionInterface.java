/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission;

import java.util.List;

import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentDefinition;

import android.support.annotation.NonNull;

public interface PermissionInterface {

    void showProgressDialog();

    void hideProgressDialog();

    void onConsentGetFailed(int error);

    void onConsentRetrieved(@NonNull final List<ConsentView> consent);

    void onCreateConsentFailed(ConsentDefinition definition, int errorCode);

    void onCreateConsentSuccess(ConsentDefinition definition, Consent consent, int code);
}
