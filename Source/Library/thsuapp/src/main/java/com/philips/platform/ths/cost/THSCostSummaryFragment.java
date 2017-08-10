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
import android.widget.RelativeLayout;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

import static com.philips.platform.ths.utility.THSConstants.IS_INSURANCE_AVAILABLE_KEY;

public class THSCostSummaryFragment extends THSBaseFragment implements View.OnClickListener {
    public static final String TAG = THSCostSummaryFragment.class.getSimpleName();
    private ActionBarListener actionBarListener;
    private THSCostSummaryPresenter mPresenter;
    private RelativeLayout mProgressbarContainer;
    private Button mCostSummaryContinueButton;
    protected Label costBigLabel;
    protected Label costSmallLabel;
    private RelativeLayout mRelativeLayoutCostContainer;

     Label mInsuranceName, mInsuranceMemberId, mInsuranceSubscriptionType;
     Label mCardType, mMaskedCardNumber, mCardExpirationDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_cost_detail, container, false);
        mPresenter = new THSCostSummaryPresenter(this);
        Bundle bundle= getArguments();

        mRelativeLayoutCostContainer = (RelativeLayout) view.findViewById(R.id.cost_container);
        costBigLabel = (Label) view.findViewById(R.id.ths_cost_summary_cost_big_label);
        costSmallLabel = (Label) view.findViewById(R.id.ths_cost_summary_cost_small_label);
        mProgressbarContainer=(RelativeLayout) view.findViewById(R.id.ths_cost_summary_relativelayout);
        mCostSummaryContinueButton =(Button) view.findViewById(R.id.ths_cost_summary_continue_button);
        mCostSummaryContinueButton.setOnClickListener(this);

        mInsuranceName=(Label) view.findViewById(R.id.ths_cost_summary_insurance_name);
        mInsuranceMemberId=(Label) view.findViewById(R.id.ths_cost_summary_insurance_member_id);
        mInsuranceSubscriptionType=(Label) view.findViewById(R.id.ths_cost_summary_insurance_subscriber_type);

        mCardType=(Label) view.findViewById(R.id.ths_cost_summary_card_type);
        mMaskedCardNumber=(Label) view.findViewById(R.id.ths_cost_summary_masked_card_number);
        mCardExpirationDate=(Label) view.findViewById(R.id.ths_cost_summary_card_expiration);



        boolean isInsuranceAvialable = bundle.getBoolean(IS_INSURANCE_AVAILABLE_KEY);
        if(isInsuranceAvialable){
            mPresenter.fetchExistingSubscription();
        }else{
            mInsuranceName.setText("No Insurance");
        }
        mPresenter.createVisit();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createCustomProgressBar(mProgressbarContainer, BIG);
        actionBarListener = getActionBarListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar("Cost", true);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ths_cost_summary_continue_button){
            mPresenter.onEvent(R.id.ths_cost_summary_continue_button);
        }

    }
}
