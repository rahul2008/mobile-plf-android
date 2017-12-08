
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
import com.philips.cdp.registration.ui.utils.FieldsValidator;

public class XPasswordHint extends LinearLayout {

    private Context mContext;

    private XPasswordHintRow mCharLength;

    private XPasswordHintRow mUpperCase;

    private XPasswordHintRow mSpecialChar;

    private XPasswordHintRow mNumbers;


    public XPasswordHint(Context context) {
        super(context);
        this.mContext = context;
        initUi(R.layout.reg_password_hint);
    }

    public XPasswordHint(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initUi(R.layout.reg_password_hint);
    }

    private final void initUi(int resourceId) {
        LayoutInflater li = LayoutInflater.from(mContext);
        li.inflate(resourceId, this, true);

        mCharLength = (XPasswordHintRow) findViewById(R.id.x_reg_length);
        mCharLength.setTextDesc(R.string.reg_Create_Account_PasswordHint_Length_lbltxt);

        mUpperCase = (XPasswordHintRow) findViewById(R.id.x_reg_uppper_case);
        mUpperCase.setTextDesc(R.string.reg_Create_Account_PasswordHint_Aplhabets_lbltxt);

        mSpecialChar = (XPasswordHintRow) findViewById(R.id.x_reg_special_length);
        mSpecialChar.setTextDesc(R.string.reg_Create_Account_PasswordHint_SpecialCharacters_lbltxt);

        mNumbers = (XPasswordHintRow) findViewById(R.id.x_reg_numbers);
        mNumbers.setTextDesc(R.string.reg_Create_Account_PasswordHint_Numbers_lbltxt);

    }

    public void updateValidationStatus(String passwordString) {

        if (FieldsValidator.isAlphabetPresent(passwordString)) {
            mUpperCase.showCorrectIcon();
        }else{
            mUpperCase.showInCorrectIcon();
        }

        if (FieldsValidator.isNumberPresent(passwordString)) {
            mNumbers.showCorrectIcon();
        }else{
            mNumbers.showInCorrectIcon();
        }

        if (FieldsValidator.isSymbolsPresent(passwordString)) {
            mSpecialChar.showCorrectIcon();
        }else{
            mSpecialChar.showInCorrectIcon();
        }

        if (FieldsValidator.isPasswordLengthMeets(passwordString)) {
            mCharLength.showCorrectIcon();
        }else{
            mCharLength.showInCorrectIcon();
        }
    }
}
