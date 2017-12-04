/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.ui.utils.FontLoader;
import com.philips.cdp.registration.ui.utils.RegConstants;

public class XRegError extends RelativeLayout {

    private Context mContext;

    private TextView mTvError;

    private String mSigninErrMsg;

    private TextView mTvCloseIcon;

    public XRegError(Context context) {
        super(context);
        mContext = context;
        initUi(R.layout.reg_error_mapping);
    }

    public XRegError(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initUi(R.layout.reg_error_mapping);
    }


    private void initUi(int resourceId) {
        LayoutInflater li = LayoutInflater.from(mContext);
        li.inflate(resourceId, this, true);
        mTvError = (XTextView) findViewById(R.id.tv_reg_error_message);
        mTvCloseIcon = (TextView) findViewById(R.id.iv_reg_close);
        FontLoader.getInstance().setTypeface(mTvCloseIcon, RegConstants.PUIICON_TTF);
    }

    public void setError(String errorMsg) {
        if (null == errorMsg) {
            return;
        }
        mSigninErrMsg = errorMsg;
        mTvError.setText(errorMsg);
        setVisibility(VISIBLE);
    }

    public String getErrorMsg() {
        return mSigninErrMsg;
    }

    public void hideError() {
        setVisibility(GONE);
    }
}