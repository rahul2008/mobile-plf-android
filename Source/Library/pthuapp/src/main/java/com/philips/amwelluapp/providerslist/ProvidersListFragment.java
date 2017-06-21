package com.philips.amwelluapp.providerslist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.philips.amwelluapp.R;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.List;

public class ProvidersListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private FragmentLauncher fragmentLauncher;
    private RecyclerView recyclerView;
    private List<ProviderInfo> providerInfoList;
    private PTHProviderListPresenter pthProviderListPresenter;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pth_providers_list_fragment,container,false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.providerListRecyclerView);
        pthProviderListPresenter = new PTHProviderListPresenter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new PTHProvidersListAdapter(providerInfoList,pthProviderListPresenter));
        return view;
    }
    public void setProvidersList(List<ProviderInfo> providersList){
        providerInfoList = providersList;
    }

    @Override
    public void onRefresh() {

    }
}
