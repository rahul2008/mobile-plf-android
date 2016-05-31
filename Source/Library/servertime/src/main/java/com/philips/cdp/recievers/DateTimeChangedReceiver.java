/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.philips.cdp.servertime.ServerTime;

/**
 * Created by 310202337 on 1/27/2016.
 */
public class DateTimeChangedReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(final Context context, final Intent intent) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerTime.init(context);
                ServerTime.getInstance().refreshOffset();
            }
        }).start();


    }
}
