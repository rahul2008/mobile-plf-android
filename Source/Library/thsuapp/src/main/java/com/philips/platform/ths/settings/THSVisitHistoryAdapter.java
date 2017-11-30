/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.entity.visit.VisitReport;
import com.philips.platform.ths.R;
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class THSVisitHistoryAdapter extends RecyclerView.Adapter<THSVisitHistoryAdapter.CustomViewHolder>{
    List<VisitReport> mVisitReports;
    THSVisitHistoryFragment mThsVisitHistoryFragment;

    public THSVisitHistoryAdapter(List<VisitReport> visitReports, THSVisitHistoryFragment thsVisitHistoryFragment) {
        mVisitReports = visitReports;
        mThsVisitHistoryFragment = thsVisitHistoryFragment;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ths_visit_history_layout, parent, false);

        return new THSVisitHistoryAdapter.CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        final VisitReport visitReport = mVisitReports.get(position);

        final Long scheduledStartTime = visitReport.getSchedule().getActualStartTime();
        final String date = new SimpleDateFormat(THSConstants.DATE_TIME_FORMATTER, Locale.getDefault()).format(scheduledStartTime).toString();
        holder.mLabelAppointmrntDate.setText(date);

        holder.mLabelProviderName.setText(visitReport.getProviderName());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(THSConstants.THS_VISIT_REPORT,mVisitReports.get(holder.getAdapterPosition()));
                mThsVisitHistoryFragment.addFragment(new THSVisitHistoryDetailFragment(),THSProviderDetailsFragment.TAG,bundle, false);
            }
        };
        holder.mLabelProviderName.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return mVisitReports.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        Label mLabelAppointmrntDate;
        Label mLabelProviderName;

        public CustomViewHolder(View view) {
            super(view);
            this.mLabelAppointmrntDate = (Label) view.findViewById(R.id.ths_appointment_date);
            this.mLabelProviderName = (Label) view.findViewById(R.id.providerNameLabel);
            this.mLabelProviderName = (Label) view.findViewById(R.id.provider_details_layout_container);
        }
    }
}
