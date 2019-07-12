/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.screens;

import android.os.Bundle;

import com.ecs.demouapp.ui.session.NetworkConstants;
import com.ecs.demouapp.ui.utils.ECSConstant;


public class WebPrivacy extends WebFragment {
    public static final String TAG = WebPrivacy.class.getName();

    @Override
    protected boolean isJavaScriptEnable() {
        return true;
    }

    @Override
    protected String getWebUrl() {

        return "https://www.usa.philips.com/a-w/privacy-notice.html";
    }

    public static WebPrivacy createInstance(Bundle args, AnimationType animType) {
        WebPrivacy fragment = new WebPrivacy();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitleAndBackButtonVisibility("Privacy policy",true);
    }
}
