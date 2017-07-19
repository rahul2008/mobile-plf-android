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
import com.philips.platform.uid.view.widget.Label;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class THSAvailableProviderListBasedOnDateFragment extends THSBaseFragment implements View.OnClickListener, OnDateSetChangedInterface{
    public static final String TAG = THSAvailableProviderListBasedOnDateFragment.class.getSimpleName();

    Date mDate;
    THSAvailableProviderListBasedOnDatePresenter mTHSAvailableProviderListBasedOnDatePresenter;

    private Practice mPractice;
    private RecyclerView recyclerView;
    THSAvailableProviderList mTHSAvailableProviderList;
    View mChangeAppointDateView;
    Label mLabelNumberOfAvailableDoctors;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_available_doctors_based_on_time, container, false);
        Bundle bundle = getArguments();
        mDate = (Date)bundle.getSerializable(THSConstants.THS_DATE);
        mPractice = bundle.getParcelable(THSConstants.THS_PRACTICE_INFO);
        mTHSAvailableProviderListBasedOnDatePresenter = new THSAvailableProviderListBasedOnDatePresenter(this,this);
        recyclerView = (RecyclerView) view.findViewById(R.id.providerListRecyclerView);
        mChangeAppointDateView = view.findViewById(R.id.calendar_view);
        mChangeAppointDateView.setOnClickListener(this);
        mLabelNumberOfAvailableDoctors = (Label)view.findViewById(R.id.number_of_available_doctors);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.ths_available_provider), true);
        }
        refreshView();
        return view;
    }

    @Override
    public int getContainerID() {
        return ((ViewGroup)getView().getParent()).getId();
    }

    public Practice getPractice() {
        return mPractice;
    }

    public void updateProviderAdapterList(final THSAvailableProviderList availableProviderses) {
        mTHSAvailableProviderList = availableProviderses;

        List<THSAvailableProvider> listOfProviderInfos = new ArrayList();
        for (AvailableProvider availableProvider:availableProviderses.getAvailableProvidersList()) {
            THSAvailableProvider thsProviderInfo = new THSAvailableProvider();
            thsProviderInfo.setAvailableProvider(availableProvider);
            listOfProviderInfos.add(thsProviderInfo);
        }
        THSProvidersListAdapter adapter= new THSProvidersListAdapter(listOfProviderInfos);

        mLabelNumberOfAvailableDoctors.setText(listOfProviderInfos.size() + " available location specialists");

        adapter.setOnProviderItemClickListener(new OnProviderListItemClickListener() {
            @Override
            public void onItemClick(THSProviderEntity item) {
                mTHSAvailableProviderListBasedOnDatePresenter.launchAvailableProviderDetailFragment(item,mDate,mPractice);
            }
        });
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.calendar_view){
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
}

