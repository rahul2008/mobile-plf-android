/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerslist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.Provider;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.intake.THSSearchFragment;
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSCustomButtonWithDrawableIcon;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Label;

import java.util.Iterator;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_NO_PROVIDER_FOR_PRACTICE;
import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_RESPONSE_OK;
import static com.philips.platform.ths.utility.THSConstants.THS_PROVIDERLIST_ABFLOW1;
import static com.philips.platform.ths.utility.THSConstants.THS_PROVIDER_LIST;
import static com.philips.platform.ths.utility.THSConstants.THS_PROVIDER_LIST_FLOW2;

public class THSProvidersListFragment extends THSBaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, THSProviderListViewInterface, OnProviderListItemClickListener {
    public static final String TAG = THSProvidersListFragment.class.getSimpleName();
    public static final String DIALOG_TAG = THSProvidersListFragment.class.getSimpleName() + "Dialog";
    protected RecyclerView recyclerView;
    protected THSProviderListPresenter THSProviderListPresenter;
    private SwipeRefreshLayout swipeRefreshLayout;
    protected Practice practice;
    private Consumer consumer;
    protected THSProvidersListAdapter THSProvidersListAdapter;
    private ActionBarListener actionBarListener;
    protected THSCustomButtonWithDrawableIcon btn_get_started;
    protected THSCustomButtonWithDrawableIcon btn_schedule_appointment;
    private RelativeLayout mRelativeLayoutContainer;
    private Label seeFirstDoctorLabel;
    private AlertDialogFragment alertDialogFragment;
    static final long serialVersionUID = 147L;
    private Provider provider;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        setHasOptionsMenu(true);
        getParcelableObjects(bundle);
        View view = inflater.inflate(R.layout.ths_providers_list_fragment, container, false);
        THSProviderListPresenter = new THSProviderListPresenter(this, this);
        recyclerView = (RecyclerView) view.findViewById(R.id.providerListRecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        seeFirstDoctorLabel = (Label) view.findViewById(R.id.seeFirstDoctorLabel);
        btn_get_started = (THSCustomButtonWithDrawableIcon) view.findViewById(R.id.getStartedButton);
        btn_get_started.setOnClickListener(this);
        btn_schedule_appointment = (THSCustomButtonWithDrawableIcon) view.findViewById(R.id.getScheduleAppointmentButton);
        btn_schedule_appointment.setOnClickListener(this);
        mRelativeLayoutContainer = (RelativeLayout) view.findViewById(R.id.provider_list_container);
        return view;
    }

    public void getParcelableObjects(Bundle bundle) {
        practice = bundle.getParcelable(THSConstants.PRACTICE_FRAGMENT);
        consumer = THSManager.getInstance().getConsumer(getContext());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ths_provider_search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ths_provider_search) {
            THSSearchFragment thsSearchFragment = new THSSearchFragment();
            thsSearchFragment.setFragmentLauncher(getFragmentLauncher());
            thsSearchFragment.setPractice(practice);
            thsSearchFragment.setActionBarListener(getActionBarListener());
            Bundle bundle = new Bundle();
            bundle.putInt(THSConstants.SEARCH_CONSTANT_STRING, THSConstants.PROVIDER_SEARCH_CONSTANT);
            addFragment(thsSearchFragment, THSSearchFragment.TAG, bundle, true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isProviderListABFlow1()) {
            THSTagUtils.doTrackPageWithInfo(THS_PROVIDER_LIST, null, null);
        } else {
            THSTagUtils.doTrackPageWithInfo(THS_PROVIDER_LIST_FLOW2, null, null);
        }
        THSManager.getInstance().setMatchMakingVisit(false);
        if (null != actionBarListener) {
            actionBarListener.updateActionBar(getActivity().getResources().getString(R.string.ths_providerList_title), true);
        }
    }

    private boolean isProviderListABFlow1() {
        return THSManager.getInstance().getProviderListABFlow().equalsIgnoreCase(THS_PROVIDERLIST_ABFLOW1);
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
        THSProviderListPresenter.fetchProviderList(consumer, practice);
    }

    @Override
    public void updateProviderAdapterList(final List<THSProviderInfo> thsProviderInfos) {
        swipeRefreshLayout.setRefreshing(false);
        List<THSProviderInfo> listAfterFilter = checkForUrgentCare(thsProviderInfos);
        if (listAfterFilter.size() > 0) {
            THSProvidersListAdapter = new THSProvidersListAdapter(listAfterFilter, practice);
            THSProvidersListAdapter.setOnProviderItemClickListener(this);
            recyclerView.setAdapter(THSProvidersListAdapter);
        } else {
            showNoProviderErrorDialog();
        }

    }

    protected List<THSProviderInfo> checkForUrgentCare(List<THSProviderInfo> thsProviderInfos) {
        if (!practice.isShowScheduling()) {
            Iterator<THSProviderInfo> thsIterator = getProviderInfoIterator(thsProviderInfos);
            while (thsIterator.hasNext()) {
                if (thsIterator.next().getProviderInfo().getVisibility().toString().equals(THSConstants.PROVIDER_OFFLINE)) {
                    thsIterator.remove();
                }
            }
            return thsProviderInfos;
        } else {
            return thsProviderInfos;
        }
    }

    @NonNull
    protected Iterator<THSProviderInfo> getProviderInfoIterator(List<THSProviderInfo> thsProviderInfos) {
        return thsProviderInfos.iterator();
    }

    public void launchProviderDetailsFragment(THSProviderEntity item) {
        THSProviderDetailsFragment thsProviderDetailsFragment = new THSProviderDetailsFragment();
        thsProviderDetailsFragment.setActionBarListener(getActionBarListener());
        thsProviderDetailsFragment.setTHSProviderEntity(item);
        thsProviderDetailsFragment.setConsumerAndPractice(consumer, practice);
        thsProviderDetailsFragment.setFragmentLauncher(getFragmentLauncher());
        addFragment(thsProviderDetailsFragment, THSProviderDetailsFragment.TAG, null, true);
    }

    @Override
    public void updateMainView(boolean isOnline) {
        mRelativeLayoutContainer.setVisibility(RelativeLayout.VISIBLE);

        if (isOnline) {
            btn_get_started.setVisibility(View.VISIBLE);
            btn_schedule_appointment.setVisibility(View.GONE);
            if (getContext() != null) {
                seeFirstDoctorLabel.setText(getString(R.string.ths_providerList_Available_Message));
            }
        } else {
            btn_schedule_appointment.setVisibility(View.VISIBLE);
            btn_get_started.setVisibility(View.GONE);
            if (getContext() != null) {
                seeFirstDoctorLabel.setText(getString(R.string.ths_provider_list_header_text_two));
            }
        }
    }

    @Override
    public void showNoProviderErrorDialog() {
        alertDialogFragment = new AlertDialogFragment.Builder(UIDHelper.getPopupThemedContext(getContext())).setDialogType(DialogConstants.TYPE_ALERT).setTitle(R.string.ths_provider_fetch_error)
                .setMessage(R.string.ths_no_provider).
                        setPositiveButton(R.string.ths_Ok_title, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialogFragment.dismiss();
                                THSTagUtils.tagInAppNotification(THS_ANALYTICS_NO_PROVIDER_FOR_PRACTICE, THS_ANALYTICS_RESPONSE_OK);
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        }).setCancelable(false).create();
        alertDialogFragment.show(getActivity().getSupportFragmentManager(), DIALOG_TAG);

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.getStartedButton) {
            createCustomProgressBar(mRelativeLayoutContainer, BIG);
            THSProviderListPresenter.onEvent(R.id.getStartedButton);
        } else if (i == R.id.getScheduleAppointmentButton) {
            THSProviderListPresenter.onEvent(R.id.getScheduleAppointmentButton);
        } else if (i == R.id.ths_provider_search) {
            THSSearchFragment thsSearchFragment = new THSSearchFragment();
            thsSearchFragment.setFragmentLauncher(getFragmentLauncher());
            thsSearchFragment.setPractice(practice);
            thsSearchFragment.setActionBarListener(getActionBarListener());
            Bundle bundle = new Bundle();
            bundle.putInt(THSConstants.SEARCH_CONSTANT_STRING, THSConstants.PROVIDER_SEARCH_CONSTANT);
            addFragment(thsSearchFragment, THSSearchFragment.TAG, bundle, true);
        }
    }

    public Practice getPractice() {
        return practice;
    }

    public void setPractice(Practice practice) {
        this.practice = practice;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Provider getProvider() {
        return provider;
    }

    @Override
    public void onItemClick(THSProviderEntity providerEntity, int viewId) {
        if (!isProviderListABFlow1() && (viewId == R.id.provider_select || viewId == R.id.provider_schedule ||
                viewId == R.id.doctor_schedule_action_bar || viewId == R.id.doctor_select_action_bar)) {
            THSProviderListPresenter.onEvent(providerEntity, viewId, practice);
        } else {
            launchProviderDetailsFragment(providerEntity);
        }
    }

}
