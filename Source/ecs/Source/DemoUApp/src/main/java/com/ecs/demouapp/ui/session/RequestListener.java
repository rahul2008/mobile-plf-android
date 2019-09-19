/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.session;

import android.os.Message;

public interface RequestListener {
    void onSuccess(Message msg);

    void onError(Message msg);
}
