/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.visit.Appointment;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.providerslist.OnProviderListItemClickListener;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Label;

import java.util.List;

public class THSScheduledVisitsFragment extends THSBaseFragment {
    public static final String TAG = THSScheduledVisitsFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private THSScheduledVisitsPresenter mThsSchedulesVisitsPresenter;
    private THSScheduledVisitsAdapter mThsScheduledVisitsAdapter;
    private Label mNumberOfAppointmentsLabel;
    RelativeLayout mRelativeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_scheduled_visits_list, container, false);
        mThsSchedulesVisitsPresenter = new THSScheduledVisitsPresenter(this);
        mNumberOfAppointmentsLabel = (Label) view.findViewById(R.id.ths_number_of_visits);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.ths_visit_dates_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.providerListItemLayout);
        mNumberOfAppointmentsLabel.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBarListener actionBarListener = getActionBarListener();
        if(null != actionBarListener){
            actionBarListener.updateActionBar(getString(R.string.ths_appointments),true);
        }
        getAppointments();
    }

    private void getAppointments() {
        try {
            createCustomProgressBar(mRelativeLayout,BIG);
            mThsSchedulesVisitsPresenter.getAppointmentsSince(null);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    public void updateList(List<Appointment> appointments) {
        if(getContext() == null)
            return;
        mNumberOfAppointmentsLabel.setVisibility(View.VISIBLE);
        String text = getString(R.string.ths_number_of_visits_scheduled,appointments.size());
        mNumberOfAppointmentsLabel.setText(text);
        mThsScheduledVisitsAdapter = new THSScheduledVisitsAdapter(appointments, this);
        mRecyclerView.setAdapter(mThsScheduledVisitsAdapter);
    }

    public void refreshList() {
        createCustomProgressBar(mRelativeLayout,BIG);
        getAppointments();
    }
}
