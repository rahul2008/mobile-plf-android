/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.listener;

/**
 * Created by philips on 10/24/17.
 */

public interface CreateConsentListener {

    void onSuccess(int code);
    int onFailure(int code);
}
