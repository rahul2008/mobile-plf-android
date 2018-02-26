/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.payment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.Label;

import static com.philips.platform.ths.utility.THSConstants.CVV_HELP_TEXT;
import static com.philips.platform.ths.utility.THSConstants.THS_PAYMENT_METHOD;


@SuppressWarnings("serial")
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
        mCardHolderNameEditText = view.findViewById(R.id.ths_payment_detail_card_holder_name_edittext);
        mCardNumberEditText = view.findViewById(R.id.ths_payment_detail_card_number_edittext);
        mCardExpiryMonthEditText = view.findViewById(R.id.ths_payment_detail_card_expiration_month_edittext);
        mCardExpiryYearEditText = view.findViewById(R.id.ths_payment_detail_card_expiration_year_edittext);
        mCVCcodeEditText = view.findViewById(R.id.ths_payment_detail_card_cvc_edittext);
        mPaymentDetailContinueButton = view.findViewById(R.id.ths_payment_detail_continue_button);
        mPaymentDetailContinueButton.setOnClickListener(this);
        mProgressbarContainer = view.findViewById(R.id.ths_payment_detail_container);
        cvvDetail = view.findViewById(R.id.ths_payment_detail_card_cvc_help);
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
        THSTagUtils.doTrackPageWithInfo(THS_PAYMENT_METHOD,null,null);
        if (null != actionBarListener) {
            actionBarListener.updateActionBar(getString(R.string.ths_credit_card_details_fragment_name), true);
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
