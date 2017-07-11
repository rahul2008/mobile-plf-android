package com.philips.platform.ths.appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.providerslist.OnProviderListItemClickListener;
import com.philips.platform.ths.providerslist.THSProvidersListAdapter;
import com.philips.platform.ths.utility.THSConstants;

import java.util.Date;
import java.util.List;

public class THSAvailableProviderListBasedOnDateFragment extends THSBaseFragment{
    public static final String TAG = THSAvailableProviderListBasedOnDateFragment.class.getSimpleName();
    Date mDate;
    THSAvailableProviderListBasedOnDatePresenter mTHSAvailableProviderListBasedOnDatePresenter;

    private Practice mPractice;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_available_doctors_based_on_time, container, false);
        Bundle bundle = getArguments();
        mDate = (Date)bundle.getSerializable(THSConstants.THS_DATE);
        mPractice = bundle.getParcelable(THSConstants.THS_PRACTICE_INFO);
        mTHSAvailableProviderListBasedOnDatePresenter = new THSAvailableProviderListBasedOnDatePresenter(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.providerListRecyclerView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.ths_available_provider), true);
        }
        try {
            mTHSAvailableProviderListBasedOnDatePresenter.getAvailableProvidersBasedOnDate();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getContainerID() {
        return ((ViewGroup)getView().getParent()).getId();
    }

    public Practice getPractice() {
        return mPractice;
    }

    public void updateProviderAdapterList(final List<ProviderInfo> providerInfos) {
        THSAvailableProviderListBasedOnDateAdapter adapter= new THSAvailableProviderListBasedOnDateAdapter(providerInfos, mTHSAvailableProviderListBasedOnDatePresenter);
        adapter.setOnProviderItemClickListener(new OnProviderListItemClickListener() {
            @Override
            public void onItemClick(ProviderInfo item) {

            }
        });
        recyclerView.setAdapter(adapter);

    }
}

