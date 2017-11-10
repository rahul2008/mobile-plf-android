/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.listener;

import android.os.Message;

/**
 * Created by Maqsood on 10/18/17.
 */

public interface RequestListener {
    void onResponseSuccess(Message msg);
    void onResponseError(Message msg);
}
