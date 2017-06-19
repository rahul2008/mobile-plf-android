package com.philips.amwelluapp.providerslist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.amwelluapp.R;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.ArrayList;
import java.util.List;

public class ProvidersListFragment extends Fragment {

    private FragmentLauncher fragmentLauncher;
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pth_providers_list_fragment,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.providerListRecyclerView);
        List<Provider> providerList = getProvidersList();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new PTHProvidersListAdapter(providerList));
        return view;
    }

    @NonNull
    private List<Provider> getProvidersList() {
        Provider provider = new Provider();
        provider.setProviderName("Dr. Eylin White");
        provider.setProviderAvailable(true);
        provider.setProviderPractise("Pedriatrician");
        provider.setProviderImageURL("something");
        provider.setProviderRating((float) 4.5);

        Provider provider1 = new Provider();
        provider1.setProviderName("Dr. Eylin White");
        provider1.setProviderAvailable(true);
        provider1.setProviderPractise("Pedriatrician");
        provider1.setProviderImageURL("something");
        provider1.setProviderRating((float) 3.5);

        Provider provider2 = new Provider();
        provider2.setProviderName("Dr. Eylin White");
        provider2.setProviderAvailable(true);
        provider2.setProviderPractise("Pedriatrician");
        provider2.setProviderImageURL("something");
        provider2.setProviderRating((float) 2.5);


        List<Provider> providerList = new ArrayList<>();

        providerList.add(provider);
        providerList.add(provider1);
        providerList.add(provider2);
        return providerList;
    }

    public void setFragmentLauncher(FragmentLauncher fragmentLauncher) {
        this.fragmentLauncher = fragmentLauncher;
    }
}
