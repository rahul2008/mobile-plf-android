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
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.intake.THSSymptomsFragment;
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.ProgressBar;

import java.util.List;

public class THSProvidersListFragment extends THSBaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, THSProviderListViewInterface {
    public static final String TAG = THSProvidersListFragment.class.getSimpleName();
    private FragmentLauncher fragmentLauncher;
    private RecyclerView recyclerView;
    private List<THSProviderInfo> thsProviderInfos;
    private THSProviderListPresenter THSProviderListPresenter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Practice mPractice;
    private Consumer consumer;
    private THSProvidersListAdapter THSProvidersListAdapter;
    private ActionBarListener actionBarListener;
    Button btn_get_started;
    Button btn_schedule_appointment;
    private RelativeLayout mRelativeLayoutContainer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle=getArguments();
        mPractice=bundle.getParcelable(THSConstants.PRACTICE_FRAGMENT);
        consumer= THSManager.getInstance().getPTHConsumer().getConsumer();
        View view = inflater.inflate(R.layout.ths_providers_list_fragment, container, false);
        THSProviderListPresenter = new THSProviderListPresenter(this, this);
        recyclerView = (RecyclerView) view.findViewById(R.id.providerListRecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        btn_get_started = (Button) view.findViewById(R.id.getStartedButton);
        btn_get_started.setOnClickListener(this);
        btn_schedule_appointment = (Button) view.findViewById(R.id.getScheduleAppointmentButton);
        btn_schedule_appointment.setOnClickListener(this);
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



    @Override
    public void onRefresh() {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
        THSProviderListPresenter.fetchProviderList(consumer, mPractice);
    }

    @Override
    public void updateProviderAdapterList(final List<THSProviderInfo> thsProviderInfos) {
        swipeRefreshLayout.setRefreshing(false);
        THSProvidersListAdapter = new THSProvidersListAdapter(thsProviderInfos);
        THSProvidersListAdapter.setOnProviderItemClickListener(new OnProviderListItemClickListener() {
            @Override
            public void onItemClick(THSProviderEntity item) {

                THSProviderDetailsFragment pthProviderDetailsFragment = new THSProviderDetailsFragment();
                pthProviderDetailsFragment.setActionBarListener(getActionBarListener());
                pthProviderDetailsFragment.setTHSProviderEntity(item);
                pthProviderDetailsFragment.setConsumerAndPractice(consumer, mPractice);
                pthProviderDetailsFragment.setFragmentLauncher(getFragmentLauncher());
                getActivity().getSupportFragmentManager().beginTransaction().replace(getContainerID(),
                        pthProviderDetailsFragment, "Provider Details").addToBackStack(null).commit();
            }
        });
        recyclerView.setAdapter(THSProvidersListAdapter);

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.getStartedButton) {
            createCustomProgressBar(mRelativeLayoutContainer, BIG);
            THSProviderListPresenter.onEvent(R.id.getStartedButton);
        }else if(i==R.id.getScheduleAppointmentButton){
            createCustomProgressBar(mRelativeLayoutContainer, BIG);
            THSProviderListPresenter.onEvent(R.id.getScheduleAppointmentButton);
        }
    }

    public Practice getPractice() {
        return mPractice;
    }

    public void setPractice(Practice practice) {
        this.mPractice = practice;
    }
}
