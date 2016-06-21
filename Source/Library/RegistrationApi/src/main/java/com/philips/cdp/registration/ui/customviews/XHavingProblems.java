
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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.cdp.registration.R;

public class XHavingProblems extends LinearLayout {

    private Context mContext;

    public XHavingProblems(Context context) {
        super(context);
        this.mContext = context;
        initUi();
    }

    public XHavingProblems(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initUi();
    }


    private final void initUi() {

        String baseString = mContext.getString(R.string.reg_VerifyEmail_ResendErrorMsg_lbltxt);
        String[] splitString = baseString.split("\n");

        LayoutInflater li = LayoutInflater.from(mContext);
        TextView tvFirstString = (TextView) li.inflate(R.layout.reg_plain_text, null, false);
        tvFirstString.setText(splitString[0]);
        addView(tvFirstString);
        for (int i = 1; i < splitString.length; i++) {
            View view = li.inflate(R.layout.reg_bullet_text_row, null, false);
            TextView tvBulletTextIcon = (TextView) view.findViewById(R.id.tv_bullet_ic_text);
            tvBulletTextIcon.setText("- ");
            TextView tvBulletText = (TextView) view.findViewById(R.id.tv_bullet_text);
            tvBulletText.setText(splitString[i].replaceFirst("- ", ""));
            addView(view);
        }
    }


}
