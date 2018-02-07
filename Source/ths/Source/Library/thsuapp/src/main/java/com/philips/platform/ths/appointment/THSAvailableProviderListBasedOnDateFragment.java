/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.AvailableProvider;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.providerslist.OnProviderListItemClickListener;
import com.philips.platform.ths.providerslist.THSProvidersListAdapter;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.philips.platform.ths.utility.THSConstants.THS_SCHEDULE_APPOINTMENT_PICK_PROVIDER;

public class THSAvailableProviderListBasedOnDateFragment extends THSBaseFragment implements View.OnClickListener, OnDateSetChangedInterface {
    public static final String TAG = THSAvailableProviderListBasedOnDateFragment.class.getSimpleName();

    protected Date mDate;
    protected THSAvailableProviderListBasedOnDatePresenter mTHSAvailableProviderListBasedOnDatePresenter;

    private Practice mPractice;
    private RecyclerView recyclerView;
    private Label mLabelNumberOfAvailableDoctors;
    private Label mLabelDate;
    protected THSAvailableProviderList mThsAvailableProviderList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_available_doctors_based_on_time, container, false);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.ths_pick_a_provider), true);
        }
        Label label = (Label) view.findViewById(R.id.schedule_appointment);
        label.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        Bundle bundle = getArguments();
        if (mDate == null) {
            mDate = (Date) bundle.getSerializable(THSConstants.THS_DATE);
        }
        mPractice = bundle.getParcelable(THSConstants.THS_PRACTICE_INFO);
        mThsAvailableProviderList = bundle.getParcelable(THSConstants.THS_AVAILABLE_PROVIDER_LIST);
        mTHSAvailableProviderListBasedOnDatePresenter = new THSAvailableProviderListBasedOnDatePresenter(this, this);
        if (null != view) {
            recyclerView = (RecyclerView) view.findViewById(R.id.providerListRecyclerView);
            recyclerView.setNestedScrollingEnabled(false);
            mLabelNumberOfAvailableDoctors = (Label) view.findViewById(R.id.number_of_available_doctors);
            mLabelDate = (Label) view.findViewById(R.id.calendar_view);
            mLabelDate.setOnClickListener(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            refreshList();
        }
    }

    protected void refreshList() {
        if (mThsAvailableProviderList != null) {
            updateProviderAdapterList(mThsAvailableProviderList);
        } else {
            refreshView();
        }
    }

    public Practice getPractice() {
        return mPractice;
    }

    public void updateProviderAdapterList(final THSAvailableProviderList availableProviderses) {
        if (getContext() != null) {
            mLabelDate.setText(new SimpleDateFormat(THSConstants.DATE_FORMATTER, Locale.getDefault()).format(mDate));

            List<THSAvailableProvider> listOfProviderInfos = new ArrayList<>();
            for (AvailableProvider availableProvider : availableProviderses.getAvailableProvidersList()) {
                THSAvailableProvider thsProviderInfo = new THSAvailableProvider();
                thsProviderInfo.setAvailableProvider(availableProvider);
                listOfProviderInfos.add(thsProviderInfo);
            }
            THSProvidersListAdapter adapter = new THSProvidersListAdapter(listOfProviderInfos);

            mLabelNumberOfAvailableDoctors.setText(listOfProviderInfos.size() + " " + getString(R.string.ths_available_location_specialists_string));

            adapter.setOnProviderItemClickListener(new OnProviderListItemClickListener() {
                @Override
                public void onItemClick(THSProviderEntity item) {
                    mTHSAvailableProviderListBasedOnDatePresenter.launchAvailableProviderDetailFragment(item, mDate, mPractice);
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.calendar_view) {
            mTHSAvailableProviderListBasedOnDatePresenter.onEvent(R.id.calendar_view);
        }
    }

    @Override
    public void refreshView() {
        try {
            mTHSAvailableProviderListBasedOnDatePresenter.getAvailableProvidersBasedOnDate();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    @Override
    public void onResume() {
        super.onResume();
        THSTagUtils.doTrackPageWithInfo(THS_SCHEDULE_APPOINTMENT_PICK_PROVIDER,null,null);

    }
}

