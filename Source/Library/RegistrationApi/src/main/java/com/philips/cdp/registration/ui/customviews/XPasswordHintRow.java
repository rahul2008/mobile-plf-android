
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

public class XPasswordHintRow extends RelativeLayout {

    private Context mContext;

    private TextView mTvIconText;

    private TextView mTvIconTextDesc;


    public XPasswordHintRow(Context context) {
        super(context);
        this.mContext = context;
        initUi(R.layout.x_password_hint_row);
    }

    public XPasswordHintRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initUi(R.layout.x_password_hint_row);
    }

    private final void initUi(int resourceId) {
        LayoutInflater li = LayoutInflater.from(mContext);
        li.inflate(resourceId, this, true);
        mTvIconText = (TextView) findViewById(R.id.tv_icon_text);
        FontLoader.getInstance().setTypeface(mTvIconText, RegConstants.PUIICON_TTF);
        mTvIconTextDesc = (TextView) findViewById(R.id.tv_icon_text_desc);
    }


    public void setTextDesc(int stringId) {
        mTvIconTextDesc.setText(mContext.getString(stringId));
    }

    public void showCorrectIcon() {
        mTvIconText.setTextColor(mContext.getResources().getColor(R.color.reg_password_hint_correct_ic_color));
        mTvIconText.setText(R.string.ic_reg_check);
    }

    public void showInCorrectIcon() {
        mTvIconText.setTextColor(mContext.getResources().getColor(R.color.reg_password_hint_incorrect_ic_color));
        mTvIconText.setText(R.string.ic_reg_close);
    }


}
