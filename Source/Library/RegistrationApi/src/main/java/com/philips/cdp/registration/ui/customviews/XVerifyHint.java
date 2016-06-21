
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
import android.widget.LinearLayout;

import com.philips.cdp.registration.R;

public class XVerifyHint extends LinearLayout {

    private Context mContext;

    private XPasswordHintRow mCharLength;

    private XPasswordHintRow mUpperCase;



    public XVerifyHint(Context context) {
        super(context);
        this.mContext = context;
        initUi(R.layout.x_mobilea_verify_hint);
    }

    public XVerifyHint(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initUi(R.layout.x_mobilea_verify_hint);
    }

    private final void initUi(int resourceId) {
        LayoutInflater li = LayoutInflater.from(mContext);
        li.inflate(resourceId, this, true);

        mCharLength = (XPasswordHintRow) findViewById(R.id.x_reg_length);
        mCharLength.setTextDesc(R.string.Verfiy_SMS_Hint_lbltxt);

        mUpperCase = (XPasswordHintRow) findViewById(R.id.x_reg_uppper_case);
        mUpperCase.setTextDesc(R.string.Verfiy_countdown_Hint_lbltxt);

    }

}
