/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.cost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.Label;


import static com.philips.platform.ths.utility.THSConstants.THS_COST_SUMMARY_ALERT;


public class THSCostSummaryFragment extends THSBaseFragment implements View.OnClickListener {
    public static final String TAG = THSCostSummaryFragment.class.getSimpleName();
    private ActionBarListener actionBarListener;
    private THSCostSummaryPresenter mPresenter;
    private RelativeLayout mProgressbarContainer;
    Button mCostSummaryContinueButton;
    Button mAddPaymentMethodButton;
    protected Label costBigLabel;
    protected Label costSmallLabel;


    private FrameLayout mInsuranceDetailFrameLayout;
    RelativeLayout mInsuranceDetailRelativeLayout;
    RelativeLayout mNoInsuranceDetailRelativeLayout;

    private FrameLayout mPaymentMethodDetailFrameLayout;
    RelativeLayout mPaymentMethodDetailRelativeLayout;
    RelativeLayout mNoPaymentMethodDetailRelativeLayout;

    RelativeLayout mCostSummaryContinueButtonRelativeLayout;
    RelativeLayout mAddPaymentMethodButtonRelativeLayout;

    Label mInsuranceName, mInsuranceMemberId, mInsuranceSubscriptionType;
    Label mCardType, mMaskedCardNumber, mCardExpirationDate;
    Label mInitialVisitCostLabel;
    Label mActualCostHeader;

    THSVisit thsVisit;

    AlertDialogFragment alertDialogFragment;

    EditText mCouponCodeEdittext;
    Button mCouponCodeButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_cost_summary, container, false);
        mPresenter = new THSCostSummaryPresenter(this);



        costBigLabel = (Label) view.findViewById(R.id.ths_cost_summary_cost_big_label);
        costSmallLabel = (Label) view.findViewById(R.id.ths_cost_summary_cost_small_label);
        mProgressbarContainer = (RelativeLayout) view.findViewById(R.id.ths_cost_summary_relativelayout);
        mCostSummaryContinueButton = (Button) view.findViewById(R.id.ths_cost_summary_continue_button);
        mCostSummaryContinueButton.setOnClickListener(this);

        mAddPaymentMethodButton = (Button) view.findViewById(R.id.ths_cost_summary_add_payment_method_button);
        mAddPaymentMethodButton.setOnClickListener(this);

        mInsuranceName = (Label) view.findViewById(R.id.ths_cost_summary_insurance_name);
        mInsuranceMemberId = (Label) view.findViewById(R.id.ths_cost_summary_insurance_member_id);
        mInsuranceSubscriptionType = (Label) view.findViewById(R.id.ths_cost_summary_insurance_subscriber_type);

        mCardType = (Label) view.findViewById(R.id.ths_cost_summary_card_type);
        mMaskedCardNumber = (Label) view.findViewById(R.id.ths_cost_summary_masked_card_number);
        mCardExpirationDate = (Label) view.findViewById(R.id.ths_cost_summary_card_expiration);

        mInsuranceDetailFrameLayout = (FrameLayout) view.findViewById(R.id.ths_cost_summary_insurance_detail_framelayout);
        mInsuranceDetailFrameLayout.setOnClickListener(this);
        mInsuranceDetailRelativeLayout = (RelativeLayout) view.findViewById(R.id.ths_cost_summary_insurance_detail_relativelayout);
        mNoInsuranceDetailRelativeLayout = (RelativeLayout) view.findViewById(R.id.ths_cost_summary_no_insurance_detail_relativelayout);


        mPaymentMethodDetailFrameLayout = (FrameLayout) view.findViewById(R.id.ths_cost_summary_payment_detail_framelayout);
        mPaymentMethodDetailFrameLayout.setOnClickListener(this);
        mPaymentMethodDetailRelativeLayout = (RelativeLayout) view.findViewById(R.id.ths_cost_summary_payment_detail_relativelayout);
        mNoPaymentMethodDetailRelativeLayout = (RelativeLayout) view.findViewById(R.id.ths_cost_summary_no_payment_detail_relativelayout);

        mCostSummaryContinueButtonRelativeLayout = (RelativeLayout) view.findViewById(R.id.ths_cost_summary_continue_button_relativelayout);
        mAddPaymentMethodButtonRelativeLayout = (RelativeLayout) view.findViewById(R.id.ths_cost_summary_add_payment_method_button_relativelayout);

        mCouponCodeEdittext = (EditText) view.findViewById(R.id.ths_cost_summary_promotion_code_edittext);
        mCouponCodeButton = (Button) view.findViewById(R.id.ths_cost_summary_promotion_code_apply_button);
        mCouponCodeButton.setOnClickListener(this);
        mInitialVisitCostLabel = (Label)view.findViewById(R.id.ths_cost_summary_initial_visit_cost_label);
        mActualCostHeader= (Label) view.findViewById(R.id.ths_cost_summary_title_label);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createCustomProgressBar(mProgressbarContainer, BIG);
        actionBarListener = getActionBarListener();
        alertDialogFragment = (AlertDialogFragment) getFragmentManager().findFragmentByTag(THS_COST_SUMMARY_ALERT);
        if (alertDialogFragment != null) {
            alertDialogFragment.setPositiveButtonListener(this);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar("Cost", true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.fetchExistingSubscription();
        mPresenter.getPaymentMethod();
        //mPresenter.createVisit();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ths_cost_summary_continue_button) {
            mPresenter.onEvent(R.id.ths_cost_summary_continue_button);
        } else if (v.getId() == R.id.ths_cost_summary_insurance_detail_framelayout) {
            mPresenter.onEvent(R.id.ths_cost_summary_insurance_detail_framelayout);
        } else if (v.getId() == R.id.ths_cost_summary_payment_detail_framelayout || v.getId() == R.id.ths_cost_summary_add_payment_method_button) {
            mPresenter.onEvent(R.id.ths_cost_summary_payment_detail_framelayout);
        }else if (v.getId() == R.id.uid_dialog_positive_button) {
            mPresenter.onEvent(R.id.uid_dialog_positive_button);
        } else if (v.getId() == R.id.ths_cost_summary_promotion_code_apply_button) {
            mPresenter.onEvent(R.id.ths_cost_summary_promotion_code_apply_button );
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thsVisit=null;
    }
}
