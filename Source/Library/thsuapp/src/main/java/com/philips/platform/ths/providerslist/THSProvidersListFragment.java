package com.philips.platform.ths.providerslist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.pharmacy.THSPharmacyAndShippingFragment;
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.ProgressBar;

import java.util.List;

public class THSProvidersListFragment extends THSBaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, THSProviderListViewInterface {

    private FragmentLauncher fragmentLauncher;
    private RecyclerView recyclerView;
    private List<ProviderInfo> providerInfoList;
    private THSProviderListPresenter THSProviderListPresenter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Practice practice;
    private Consumer consumer;
    private ProgressBar progressBar;
    private THSProvidersListAdapter THSProvidersListAdapter;
    private ActionBarListener actionBarListener;
    Button btn_get_started;
    private RelativeLayout mRelativeLayoutContainer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ths_providers_list_fragment, container, false);
        THSProviderListPresenter = new THSProviderListPresenter(this, this);
        recyclerView = (RecyclerView) view.findViewById(R.id.providerListRecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        btn_get_started = (Button) view.findViewById(R.id.getStartedButton);
        btn_get_started.setOnClickListener(this);
        mRelativeLayoutContainer = (RelativeLayout) view.findViewById(R.id.provider_list_container);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar("Providers screen", true);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        actionBarListener = getActionBarListener();
        onRefresh();
    }

    //TODO: Review Comment - Spoorti - Not sure if setter can be removed in case parameters are passed by bundle
    public void setPracticeAndConsumer(Practice practice, Consumer consumer) {
        this.practice = practice;
        this.consumer = consumer;

    }

    @Override
    public void onRefresh() {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
        THSProviderListPresenter.fetchProviderList(consumer, practice);
    }

    @Override
    public void updateProviderAdapterList(final List<ProviderInfo> providerInfos) {
        swipeRefreshLayout.setRefreshing(false);
        THSProvidersListAdapter = new THSProvidersListAdapter(providerInfos, THSProviderListPresenter);
        THSProvidersListAdapter.setOnProviderItemClickListener(new OnProviderListItemClickListener() {
            @Override
            public void onItemClick(ProviderInfo item) {

                THSProviderDetailsFragment pthProviderDetailsFragment = new THSProviderDetailsFragment();
                pthProviderDetailsFragment.setActionBarListener(getActionBarListener());
                pthProviderDetailsFragment.setProviderAndConsumerAndPractice(item, consumer, practice);
                getActivity().getSupportFragmentManager().beginTransaction().replace(getContainerID(), pthProviderDetailsFragment, "Provider Details").addToBackStack(null).commit();
            }
        });
        recyclerView.setAdapter(THSProvidersListAdapter);

    }

    @Override
    public int getContainerID() {
        return ((ViewGroup) getView().getParent()).getId();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        THSPharmacyAndShippingFragment thsPharmacyFragment = new THSPharmacyAndShippingFragment();
        THSConsumer pthConsumer = new THSConsumer();
        pthConsumer.setConsumer(consumer);
        thsPharmacyFragment.setConsumer(pthConsumer);
        thsPharmacyFragment.setActionBarListener(getActionBarListener());
        if (i == R.id.getStartedButton) {
            getFragmentActivity().getSupportFragmentManager().
                    beginTransaction().replace(getContainerID(),
                    thsPharmacyFragment,"Pharmacy List").addToBackStack(null).commit();
        }
    }

    public Practice getPractice() {
        return practice;
    }

    public void setPractice(Practice practice) {
        this.practice = practice;
    }
}
