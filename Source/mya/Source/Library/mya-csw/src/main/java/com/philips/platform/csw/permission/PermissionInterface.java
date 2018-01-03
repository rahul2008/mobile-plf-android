/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission;

import com.philips.platform.mya.chi.ConsentError;

public interface PermissionInterface {

    void showProgressDialog();

    void hideProgressDialog();

    void showErrorDialog(ConsentError error);
}
