/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerslist;

import android.os.Bundle;
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
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.intake.THSSearchFragment;
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

import java.util.List;

public class THSProvidersListFragment extends THSBaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, THSProviderListViewInterface {
    public static final String TAG = THSProvidersListFragment.class.getSimpleName();
    public static final String DIALOG_TAG = THSProvidersListFragment.class.getSimpleName() + "Dialog";
    private RecyclerView recyclerView;
    private THSProviderListPresenter THSProviderListPresenter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Practice practice;
    private Consumer mConsumer;
    private THSProvidersListAdapter THSProvidersListAdapter;
    private ActionBarListener actionBarListener;
    protected Button btn_get_started;
    protected Button btn_schedule_appointment;
    private RelativeLayout mRelativeLayoutContainer;
    private Label seeFirstDoctorLabel;
    private AlertDialogFragment alertDialogFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle=getArguments();
        setHasOptionsMenu(true);
        practice =bundle.getParcelable(THSConstants.PRACTICE_FRAGMENT);
        mConsumer = bundle.getParcelable(THSConstants.THS_CONSUMER);
        View view = inflater.inflate(R.layout.ths_providers_list_fragment, container, false);
        THSProviderListPresenter = new THSProviderListPresenter(this, this);
        recyclerView = (RecyclerView) view.findViewById(R.id.providerListRecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        seeFirstDoctorLabel = (Label) view.findViewById(R.id.seeFirstDoctorLabel);
        btn_get_started = (Button) view.findViewById(R.id.getStartedButton);
        btn_get_started.setOnClickListener(this);
        btn_schedule_appointment = (Button) view.findViewById(R.id.getScheduleAppointmentButton);
        btn_schedule_appointment.setOnClickListener(this);
        mRelativeLayoutContainer = (RelativeLayout) view.findViewById(R.id.provider_list_container);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ths_provider_search_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() ==  R.id.ths_provider_search ) {
            THSSearchFragment thsSearchFragment = new THSSearchFragment();
            thsSearchFragment.setFragmentLauncher(getFragmentLauncher());
            thsSearchFragment.setPractice(practice);
            thsSearchFragment.setActionBarListener(getActionBarListener());
            Bundle bundle = new Bundle();
            bundle.putInt(THSConstants.SEARCH_CONSTANT_STRING,THSConstants.PROVIDER_SEARCH_CONSTANT);
            bundle.putParcelable(THSConstants.THS_CONSUMER,mConsumer);
            addFragment(thsSearchFragment,THSSearchFragment.TAG,bundle, true);
            }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        THSManager.getInstance().setMatchMakingVisit(false);
        if (null != actionBarListener) {
            actionBarListener.updateActionBar(getActivity().getResources().getString(R.string.provider_list_title), true);
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
        THSProviderListPresenter.fetchProviderList(mConsumer, practice);
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
                pthProviderDetailsFragment.setConsumerAndPractice(mConsumer, practice);
                pthProviderDetailsFragment.setFragmentLauncher(getFragmentLauncher());
                Bundle bundle = new Bundle();
                bundle.putParcelable(THSConstants.THS_CONSUMER,mConsumer);
                addFragment(pthProviderDetailsFragment,THSProviderDetailsFragment.TAG,bundle, true);
            }
        });
        recyclerView.setAdapter(THSProvidersListAdapter);

    }

    @Override
    public void updateMainView(boolean isOnline) {
        mRelativeLayoutContainer.setVisibility(RelativeLayout.VISIBLE);
        if(isOnline){
            btn_get_started.setVisibility(View.VISIBLE);
            btn_schedule_appointment.setVisibility(View.GONE);
            if (getContext() != null) {
                btn_get_started.setText(getContext().getString(R.string.get_started));
                seeFirstDoctorLabel.setText(getString(R.string.ths_provider_list_header_text_one));
            }
        }else {
           btn_schedule_appointment.setVisibility(View.VISIBLE);
           btn_get_started.setVisibility(View.GONE);
            if (getContext() != null) {
                btn_schedule_appointment.setText(getContext().getString(R.string.schedule_appointment));
                seeFirstDoctorLabel.setText(getString(R.string.ths_provider_list_header_text_two));
            }
        }
    }

    @Override
    public void showNoProviderErrorDialog() {
        alertDialogFragment = new AlertDialogFragment.Builder(UIDHelper.getPopupThemedContext(getContext())).setDialogType(DialogConstants.TYPE_ALERT).setTitle(R.string.ths_provider_fetch_error)
                .setMessage(R.string.ths_provider_fetch_error_text).
                        setPositiveButton(R.string.ths_insurance_not_verified_confirm_primary_button_text, this).setCancelable(false).create();
        alertDialogFragment.show(getActivity().getSupportFragmentManager(),DIALOG_TAG);

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.getStartedButton) {
            createCustomProgressBar(mRelativeLayoutContainer, BIG);
            THSProviderListPresenter.onEvent(R.id.getStartedButton);
        }else if(i==R.id.getScheduleAppointmentButton){
            THSProviderListPresenter.onEvent(R.id.getScheduleAppointmentButton);
        }else if(i == R.id.ths_provider_search){
            THSSearchFragment thsSearchFragment = new THSSearchFragment();
            thsSearchFragment.setFragmentLauncher(getFragmentLauncher());
            thsSearchFragment.setPractice(practice);
            thsSearchFragment.setActionBarListener(getActionBarListener());
            Bundle bundle = new Bundle();
            bundle.putInt(THSConstants.SEARCH_CONSTANT_STRING,THSConstants.PROVIDER_SEARCH_CONSTANT);
            bundle.putParcelable(THSConstants.THS_CONSUMER,mConsumer);
            addFragment(thsSearchFragment,THSSearchFragment.TAG,bundle, true);
        }
        else if(i == R.id.uid_dialog_positive_button){
            alertDialogFragment.dismiss();
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    public Practice getPractice() {
        return practice;
    }

    public void setPractice(Practice practice) {
        this.practice = practice;
    }

    public Consumer getConsumer() {
        return mConsumer;
    }
}
