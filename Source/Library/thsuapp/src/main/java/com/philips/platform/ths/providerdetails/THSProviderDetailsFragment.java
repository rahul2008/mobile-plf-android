/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.practice.PracticeInfo;
import com.americanwell.sdk.entity.provider.EstimatedVisitCost;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSAvailableProvider;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.ProgressBarWithLabel;


import static com.philips.platform.ths.utility.THSConstants.THS_PROVIDER_DETAIL_ALERT;
import com.philips.platform.ths.utility.THSManager;

import static com.philips.platform.ths.utility.THSConstants.THS_PROVIDER_DETAIL_PAGE;
import static com.philips.platform.ths.utility.THSConstants.THS_SYMPTOMS_PAGE;

/**
 * This class is used to display the provider details selected by the user.
 */
public class THSProviderDetailsFragment extends THSBaseFragment implements View.OnClickListener, THSProviderDetailsViewInterface, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = THSProviderDetailsFragment.class.getSimpleName();
    private Consumer consumer;
    protected THSProviderInfo mThsProviderInfo;
    protected THSAvailableProvider mThsAvailableProvider;
    protected THSProviderDetailsPresenter providerDetailsPresenter;
    private Practice mPractice;
    protected PracticeInfo mPracticeInfo;
    protected THSProviderDetailsDisplayHelper mThsProviderDetailsDisplayHelper;

    private Provider mProvider;
    private ProviderInfo mProviderInfo;

    protected Label dodProviderFoundMessage;
    protected Label mProgressBarLabel;
    protected RelativeLayout mProgressBarWithLabelContainer;
    AlertDialogFragment alertDialogFragment;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        providerDetailsPresenter = new THSProviderDetailsPresenter(this, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_provider_details_fragment, container, false);
        mProgressBarWithLabelContainer = (RelativeLayout) view.findViewById(R.id.ths_match_making_ProgressBarWithLabel_container);
        dodProviderFoundMessage = (Label) view.findViewById(R.id.dodProviderFound);
        mProgressBarLabel = (Label) view.findViewById(R.id.ths_match_making_ProgressBar_message_label);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.ths_provider_details), true);
        }
        mThsProviderDetailsDisplayHelper = new THSProviderDetailsDisplayHelper(getContext(), this, this, this, this, view);


        if (THSManager.getInstance().isMatchMakingVisit()) { // if provider is not yet selected
            providerDetailsPresenter.doMatchMaking();
        } else { // if provider is already selected
            dodProviderFoundMessage.setVisibility(View.GONE);
            final Bundle arguments = getArguments();
            if (arguments != null) {
                mPracticeInfo = arguments.getParcelable(THSConstants.THS_PRACTICE_INFO);
                mProvider = arguments.getParcelable(THSConstants.THS_PROVIDER);
                mProviderInfo = arguments.getParcelable(THSConstants.THS_PROVIDER_INFO);
                if (arguments.getParcelable(THSConstants.THS_PRACTICE_INFO) != null) {
                    mPracticeInfo = arguments.getParcelable(THSConstants.THS_PRACTICE_INFO);
                }
                if (mProviderInfo != null) {
                    THSProviderInfo thsProviderInfo = new THSProviderInfo();
                    thsProviderInfo.setTHSProviderInfo(mProviderInfo);
                    setTHSProviderEntity(thsProviderInfo);
                }else if(mProvider!=null){
                    THSProviderInfo thsProviderInfo = new THSProviderInfo();
                    thsProviderInfo.setTHSProviderInfo(mProvider);
                    setTHSProviderEntity(thsProviderInfo);
                }else {
                    showError("Provider not supplied");
                }
            }
            onRefresh();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        THSManager.getInstance().getThsTagging().trackPageWithInfo(THS_PROVIDER_DETAIL_PAGE,null,null);

    }

    /**
     * This method is used to set the provider details in the provider details screen.
     *
     * @param provider
     */
    @Override
    public void updateView(Provider provider) {
        setProvider(provider);
        mThsProviderDetailsDisplayHelper.updateView(provider, null);
    }

    public void setTHSProviderEntity(THSProviderEntity thsProviderEntity) {
        if (thsProviderEntity instanceof THSProviderInfo) {
            this.mThsProviderInfo = (THSProviderInfo) thsProviderEntity;
        } else if (thsProviderEntity instanceof THSAvailableProvider) {
            this.mThsAvailableProvider = (THSAvailableProvider) thsProviderEntity;
        }
    }

    public void setConsumerAndPractice(Consumer consumer, Practice practice) {
        this.consumer = consumer;
        this.mPractice = practice;
    }

    public THSProviderEntity getProviderEntitiy() {
        if (mThsProviderInfo != null) {
            return mThsProviderInfo;
        } else if (mThsAvailableProvider != null) {
            return mThsAvailableProvider;
        } else {
            return null;
        }
    }

    @Override
    public THSProviderInfo getTHSProviderInfo() {
        if (mThsProviderInfo != null) {
            return mThsProviderInfo;
        } else if (mThsAvailableProvider != null) {
            THSProviderInfo thsProviderInfo = new THSProviderInfo();
            thsProviderInfo.setTHSProviderInfo(mThsAvailableProvider.getProviderInfo());
            return thsProviderInfo;
        } else {
            return null;
        }
    }

    @Override
    public Practice getPractice() {
        return mPractice;
    }

    @Override
    public PracticeInfo getPracticeInfo() {
        return mPracticeInfo;
    }

    @Override
    public Consumer getConsumerInfo() {
        return consumer;
    }

    @Override
    public void dismissRefreshLayout() {
        mThsProviderDetailsDisplayHelper.dismissRefreshLayout();
    }

    @Override
    public String getFragmentTag() {
        return THSProviderDetailsFragment.TAG;
    }


    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }

    @Override
    public void onRefresh() {

        if (mProvider != null) {
            providerDetailsPresenter.onProviderDetailsReceived(mProvider, null);
            return;
        }

        if (mThsProviderDetailsDisplayHelper == null)
            return;
        mThsProviderDetailsDisplayHelper.setRefreshing();
        providerDetailsPresenter.fetchProviderDetails();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.detailsButtonOne) {
            providerDetailsPresenter.onEvent(R.id.detailsButtonOne);
        } else if (i == R.id.detailsButtonTwo) {
            providerDetailsPresenter.onEvent(R.id.detailsButtonTwo);
        } else if (i == R.id.uid_dialog_positive_button) {
            providerDetailsPresenter.onEvent(R.id.uid_dialog_positive_button);
        }
    }

    @Override
    public Provider getProvider() {
        return mProvider;
    }

    @Override
    public void updateEstimatedCost(EstimatedVisitCost estimatedVisitCost) {
        mThsProviderDetailsDisplayHelper.updateEstimateCost(estimatedVisitCost);
    }

    @Override
    public void onCalenderItemClick(int position) {

    }

    @Override
    public String getReminderTime() {
        return null;
    }

    public void setProvider(Provider mProvider) {
        this.mProvider = mProvider;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alertDialogFragment = (AlertDialogFragment) getFragmentManager().findFragmentByTag(THS_PROVIDER_DETAIL_ALERT);
        if (alertDialogFragment != null) {
            alertDialogFragment.setPositiveButtonListener(this);

        }
    }

    protected void showProgressbar() {
        createCustomProgressBar(mProgressBarWithLabelContainer, BIG);
    }

    @Override
    public boolean handleBackEvent() {
        if (THSManager.getInstance().isMatchMakingVisit()) {
            providerDetailsPresenter.cancelMatchMaking();
            getActivity().getSupportFragmentManager().popBackStack();
            return true;
        } else {
            return super.handleBackEvent();
        }
    }
}



