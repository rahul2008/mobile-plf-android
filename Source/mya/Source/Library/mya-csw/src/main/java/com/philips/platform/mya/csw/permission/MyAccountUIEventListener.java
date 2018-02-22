/*
 * Copyright (c) 2018 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.mya.csw.permission;

import java.io.Serializable;

public interface MyAccountUIEventListener extends Serializable {
    void onPrivacyNoticeClicked();
}
