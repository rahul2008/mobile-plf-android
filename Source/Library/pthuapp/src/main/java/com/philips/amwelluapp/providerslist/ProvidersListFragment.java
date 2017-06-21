package com.philips.amwelluapp.providerslist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uid.view.widget.ProgressBar;

import java.util.List;

public class ProvidersListFragment extends PTHBaseFragment implements SwipeRefreshLayout.OnRefreshListener,UIProviderListViewInterface{

    private FragmentLauncher fragmentLauncher;
    private RecyclerView recyclerView;
    private List<ProviderInfo> providerInfoList;
    private PTHProviderListPresenter pthProviderListPresenter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Practice practice;
    private Consumer consumer;
    private ProgressBar progressBar;
    private PTHProvidersListAdapter pthProvidersListAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pth_providers_list_fragment,container,false);
        pthProviderListPresenter = new PTHProviderListPresenter(getActivity(),this);
        recyclerView = (RecyclerView) view.findViewById(R.id.providerListRecyclerView);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pthProviderListPresenter.fetchProviderList(consumer,practice);
    }

    public void setProvidersList(List<ProviderInfo> providersList){
        providerInfoList = providersList;
    }

    public void setPracticeAndConsumer(Practice practice, Consumer consumer){
        this.practice = practice;
        this.consumer = consumer;

    }
    @Override
    public void onRefresh() {

    }

    @Override
    public void updateProviderAdapterList(List<ProviderInfo> providerInfos) {
        pthProvidersListAdapter = new PTHProvidersListAdapter(providerInfos,pthProviderListPresenter);
        recyclerView.setAdapter(pthProvidersListAdapter);

    }
}
