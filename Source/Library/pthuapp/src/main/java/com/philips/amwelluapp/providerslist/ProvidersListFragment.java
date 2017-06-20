package com.philips.amwelluapp.providerslist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.philips.amwelluapp.R;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.List;

public class ProvidersListFragment extends Fragment {

    private FragmentLauncher fragmentLauncher;
    private RecyclerView recyclerView;
    private List<ProviderInfo> providerInfoList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pth_providers_list_fragment,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.providerListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new PTHProvidersListAdapter(providerInfoList));
        return view;
    }
    public void setProvidersList(List<ProviderInfo> providersList){
        providerInfoList = providersList;
    }

}
