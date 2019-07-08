/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.cart;

import android.os.Message;

public interface IAPCartListener {
    void onSuccess(int count);

    void onFailure(final Message msg);
}
