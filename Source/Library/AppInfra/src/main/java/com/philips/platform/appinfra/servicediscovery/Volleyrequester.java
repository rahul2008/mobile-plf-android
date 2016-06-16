/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery;

/**
 * Created by 310238655 on 6/3/2016.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Volleyrequester {
    private static Volleyrequester mVolleyQueue = null;
    private RequestQueue mRequestQueue = null;

    public Volleyrequester() {
    }

    public static Volleyrequester getInstance() {
        if(mVolleyQueue == null) {
            mVolleyQueue = new Volleyrequester();
        }

        return mVolleyQueue;
    }

    public RequestQueue getRequestQueue(Context context) {
        if(this.mRequestQueue == null) {
            this.mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        return this.mRequestQueue;
    }
}
