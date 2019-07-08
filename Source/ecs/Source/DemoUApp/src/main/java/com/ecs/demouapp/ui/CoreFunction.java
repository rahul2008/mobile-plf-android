package com.ecs.demouapp.ui;

import android.content.Context;

import com.ecs.demouapp.R;

public class CoreFunction {

    public String getContent(Context context) {
        return context.getString(R.string.iap_app_name);
    }
}
