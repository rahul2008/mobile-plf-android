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
import android.widget.ScrollView;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.payment.THSPaymentMethod;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.Label;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.philips.platform.ths.utility.THSConstants.THS_COST_SUMMARY;
import static com.philips.platform.ths.utility.THSConstants.THS_COST_SUMMARY_COUPON_CODE_ERROR;
import static com.philips.platform.ths.utility.THSConstants.THS_COST_SUMMARY_CREATE_VISIT_ERROR;


@SuppressWarnings("serial")
public class THSCostSummaryFragment extends THSBaseFragment implements View.OnClickListener {
    public static final String TAG = THSCostSummaryFragment.class.getSimpleName();
    private ActionBarListener actionBarListener;
    protected THSCostSummaryPresenter mPresenter;
    private RelativeLayout mProgressbarContainer;

    ScrollView costSummaryScrollView;
    RelativeLayout costSummaryVisibleRelativeLayout;
    RelativeLayout costSummaryCalculateContainer;
    //ProgressBarWithLabel mProgressBarWithLabel;
    Button costSummaryCancelButton;
    Button mCostSummaryContinueButton;
    Button mAddPaymentMethodButton;
    protected Label costBigLabel;
    protected Label costSmallLabel;


    private FrameLayout mInsuranceDetailFrameLayout;
    RelativeLayout mInsuranceDetailRelativeLayout;
    RelativeLayout mNoInsuranceDetailRelativeLayout;

    protected FrameLayout mPaymentMethodDetailFrameLayout;
    RelativeLayout mPaymentMethodDetailRelativeLayout;
    RelativeLayout mNoPaymentMethodDetailRelativeLayout;

    RelativeLayout mCostSummaryContinueButtonRelativeLayout;
    RelativeLayout mAddPaymentMethodButtonRelativeLayout;

    Label mInsuranceName, mInsuranceMemberId, mInsuranceSubscriptionType;
    Label mCardType, mMaskedCardNumber, mCardExpirationDate;
    Label mInitialVisitCostLabel;
    Label mActualCostHeader;

    Label mPaymentNotRequired;

    public THSVisit getThsVisit() {
        return thsVisit;
    }

    THSVisit thsVisit;
    protected THSPaymentMethod mTHSPaymentMethod;

    AlertDialogFragment alertDialogFragmentCreateVisit;
    AlertDialogFragment alertDialogFragmentCouponCode;

    EditText mCouponCodeEdittext;
    Button mCouponCodeButton;
    protected  AtomicBoolean isPromoCodeAlreadyApplied = new AtomicBoolean(false);;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_cost_summary, container, false);
        mPresenter = new THSCostSummaryPresenter(this);

        costSummaryScrollView = view.findViewById(R.id.cost_scrollview_container);
        costSummaryCalculateContainer = view.findViewById(R.id.ths_cost_summary_calculate_container);
        costSummaryVisibleRelativeLayout = view.findViewById(R.id.ths_cost_summary_details_visible);

        costBigLabel = view.findViewById(R.id.ths_cost_summary_cost_big_label);
        costSmallLabel = view.findViewById(R.id.ths_cost_summary_cost_small_label);

        costSummaryCancelButton = view.findViewById(R.id.ths_cost_summary_cancel_button);
        costSummaryCancelButton.setOnClickListener(this);

        mCostSummaryContinueButton = view.findViewById(R.id.ths_cost_summary_continue_button);
        mCostSummaryContinueButton.setOnClickListener(this);

        mAddPaymentMethodButton = view.findViewById(R.id.ths_cost_summary_add_payment_method_button);
        mAddPaymentMethodButton.setOnClickListener(this);

        mInsuranceName = view.findViewById(R.id.ths_cost_summary_insurance_name);
        mInsuranceMemberId = view.findViewById(R.id.ths_cost_summary_insurance_member_id);
        mInsuranceSubscriptionType = view.findViewById(R.id.ths_cost_summary_insurance_subscriber_type);

        mCardType = view.findViewById(R.id.ths_cost_summary_card_type);
        mMaskedCardNumber = view.findViewById(R.id.ths_cost_summary_masked_card_number);
        mCardExpirationDate = view.findViewById(R.id.ths_cost_summary_card_expiration);

        mInsuranceDetailFrameLayout = view.findViewById(R.id.ths_cost_summary_insurance_detail_framelayout);
        mInsuranceDetailFrameLayout.setOnClickListener(this);
        mInsuranceDetailRelativeLayout = view.findViewById(R.id.ths_cost_summary_insurance_detail_relativelayout);
        mNoInsuranceDetailRelativeLayout = view.findViewById(R.id.ths_cost_summary_no_insurance_detail_relativelayout);


        mPaymentMethodDetailFrameLayout = view.findViewById(R.id.ths_cost_summary_payment_detail_framelayout);
        mPaymentMethodDetailFrameLayout.setOnClickListener(this);
        mPaymentMethodDetailRelativeLayout = view.findViewById(R.id.ths_cost_summary_payment_detail_relativelayout);
        mNoPaymentMethodDetailRelativeLayout = view.findViewById(R.id.ths_cost_summary_no_payment_detail_relativelayout);

        mCostSummaryContinueButtonRelativeLayout = view.findViewById(R.id.ths_cost_summary_continue_button_relativelayout);
        mAddPaymentMethodButtonRelativeLayout = view.findViewById(R.id.ths_cost_summary_add_payment_method_button_relativelayout);

        mCouponCodeEdittext = view.findViewById(R.id.ths_cost_summary_promotion_code_edittext);
        mCouponCodeButton = view.findViewById(R.id.ths_cost_summary_promotion_code_apply_button);
        mCouponCodeButton.setOnClickListener(this);
        mInitialVisitCostLabel = view.findViewById(R.id.ths_cost_summary_initial_visit_cost_label);
        mActualCostHeader = view.findViewById(R.id.ths_cost_summary_title_label);
        mPaymentNotRequired = view.findViewById(R.id.ths_cost_summary_no_payment_detail_required_label);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
        showCalculatingCostScreen();
        mPresenter.createVisit();
        mPresenter.getPaymentMethod();

        mPresenter.fetchExistingSubscription();

    }

    @Override
    public void onResume() {
        super.onResume();
        THSTagUtils.doTrackPageWithInfo(THS_COST_SUMMARY,null,null);
        if (null != actionBarListener) {
            actionBarListener.updateActionBar(getString(R.string.ths_cost_summary_fragment_name), true);
        }
        alertDialogFragmentCreateVisit = (AlertDialogFragment) getFragmentManager().findFragmentByTag(THS_COST_SUMMARY_CREATE_VISIT_ERROR);
        alertDialogFragmentCouponCode = (AlertDialogFragment) getFragmentManager().findFragmentByTag(THS_COST_SUMMARY_COUPON_CODE_ERROR);

    }



    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        mPresenter.onEvent(v.getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thsVisit = null;
    }

    void hideCalculatingCostScreen() {
        hideProgressBar();
        costSummaryScrollView.setScrollContainer(true);
        costSummaryCalculateContainer.setVisibility(View.GONE);
        costSummaryVisibleRelativeLayout.setVisibility(View.VISIBLE);

    }

    void showCalculatingCostScreen() {
        createCustomProgressBar(costSummaryCalculateContainer,BIG);
        costSummaryScrollView.setScrollContainer(false);
        costSummaryVisibleRelativeLayout.setVisibility(View.GONE);
        costSummaryCalculateContainer.setVisibility(View.VISIBLE);
    }

    void enablePaymentOption(boolean enable){
        mPaymentMethodDetailFrameLayout.setClickable(enable);
    }
}
