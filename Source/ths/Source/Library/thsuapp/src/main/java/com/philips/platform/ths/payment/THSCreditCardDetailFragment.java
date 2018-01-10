/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.payment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.Label;

import static com.philips.platform.ths.utility.THSConstants.CVV_HELP_TEXT;
import static com.philips.platform.ths.utility.THSConstants.THS_PAYMENT_METHOD;



public class THSCreditCardDetailFragment extends THSBaseFragment implements View.OnClickListener {

    public static final String TAG = THSCreditCardDetailFragment.class.getSimpleName();
    private ActionBarListener actionBarListener;
    THSCreditCardDetailPresenter mTHSCreditCardDetailPresenter;
    private RelativeLayout mProgressbarContainer;
    EditText mCardHolderNameEditText;
    EditText mCardNumberEditText;
    EditText mCardExpiryMonthEditText;
    EditText mCardExpiryYearEditText;
    EditText mCVCcodeEditText;
    Label cvvDetail;
    private Button mPaymentDetailContinueButton;
    AlertDialogFragment alertDialogFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_payment_detail, container, false);
        mTHSCreditCardDetailPresenter = new THSCreditCardDetailPresenter(this);
        mCardHolderNameEditText = (EditText) view.findViewById(R.id.ths_payment_detail_card_holder_name_edittext);
        mCardNumberEditText = (EditText) view.findViewById(R.id.ths_payment_detail_card_number_edittext);
        mCardExpiryMonthEditText = (EditText) view.findViewById(R.id.ths_payment_detail_card_expiration_month_edittext);
        mCardExpiryYearEditText = (EditText) view.findViewById(R.id.ths_payment_detail_card_expiration_year_edittext);
        mCVCcodeEditText = (EditText) view.findViewById(R.id.ths_payment_detail_card_cvc_edittext);
        mPaymentDetailContinueButton = (Button) view.findViewById(R.id.ths_payment_detail_continue_button);
        mPaymentDetailContinueButton.setOnClickListener(this);
        mProgressbarContainer = (RelativeLayout) view.findViewById(R.id.ths_payment_detail_container);
        cvvDetail = (Label) view.findViewById(R.id.ths_payment_detail_card_cvc_help);
        mCVCcodeEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        cvvDetail.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
        createCustomProgressBar(mProgressbarContainer, BIG);
        mTHSCreditCardDetailPresenter.getPaymentMethod();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //This needs to be taken care by proposition to avoid losing listener on rotation
        alertDialogFragment = (AlertDialogFragment) getFragmentManager().findFragmentByTag(CVV_HELP_TEXT);
        if (alertDialogFragment != null) {
            alertDialogFragment.setPositiveButtonListener(this);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        THSManager.getInstance().getThsTagging().trackPageWithInfo(THS_PAYMENT_METHOD,null,null);
        if (null != actionBarListener) {
            actionBarListener.updateActionBar("Payment method", true);
        }

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        mTHSCreditCardDetailPresenter.onEvent(v.getId());
    }
}
