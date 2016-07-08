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

public class XRegError extends RelativeLayout {

    private Context mContext;

    private TextView mTvError;

    private String mSigninErrMsg;

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

        /** inflate amount layout */
        LayoutInflater li = LayoutInflater.from(mContext);
        li.inflate(resourceId, this, true);
        mTvError = (XTextView) findViewById(R.id.tv_reg_error_message);
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

/*
    public String getSigninErrorMsg() {
        System.out.println("*************** GET mErrMsg : "+mErrMsg);
        return mErrMsg;
    }


    public void setSavedErrMsg(String errorMsg){
        System.out.println("*************** SET mErrMsg : "+mErrMsg);
        mErrMsg = errorMsg;
    }

    public String getErrorMsg() {
        System.out.println("*************** GET mErrMsg : "+mErrMsg);
       return mErrMsg;
    }
*/

    public void hideError() {
        setVisibility(GONE);
    }

}