/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.visit.VisitReport;
import com.americanwell.sdk.entity.visit.VisitReportDetail;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.utility.CircularImageView;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.welcome.THSWelcomeBackFragment;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class THSVisitHistoryAdapter extends RecyclerView.Adapter<THSVisitHistoryAdapter.CustomViewHolder>{
    List<VisitReport> mVisitReports;
    THSVisitHistoryFragment mThsVisitHistoryFragment;
    VisitReportDetail mVisitReportDetail;

    public THSVisitHistoryAdapter(List<VisitReport> visitReports, THSVisitHistoryFragment thsVisitHistoryFragment) {
        mVisitReports = visitReports;
        mThsVisitHistoryFragment = thsVisitHistoryFragment;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ths_scheduled_visits_list_item, parent, false);

        return new THSVisitHistoryAdapter.CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        final VisitReport visitReport = mVisitReports.get(position);

        final Long scheduledStartTime = visitReport.getSchedule().getActualStartTime();
        final String date = new SimpleDateFormat(THSConstants.DATE_TIME_FORMATTER, Locale.getDefault()).format(scheduledStartTime).toString();
        holder.mLabelAppointmrntDate.setText(date);

        holder.mLinearLayoutButtonContainer.setVisibility(View.GONE);

        holder.mLabelPracticeName.setText(visitReport.getConsumerName());
        holder.mLabelProviderName.setText(visitReport.getProviderName());


        try {
            THSManager.getInstance().getVisitReportDetail(mThsVisitHistoryFragment.getContext(), visitReport, new THSVisitReportDetailCallback() {
                @Override
                public void onResponse(VisitReportDetail visitReportDetail, SDKError sdkError) {
                    mVisitReportDetail = visitReportDetail;
                    if (visitReportDetail.getAssignedProviderInfo().hasImage()) {
                        try {
                            final Drawable drawable = ContextCompat.getDrawable(mThsVisitHistoryFragment.getContext(), R.drawable.doctor_placeholder);
                            THSManager.getInstance().getAwsdk(holder.mImageViewCircularImageView.getContext()).
                                    getPracticeProvidersManager().
                                    newImageLoader(visitReportDetail.getAssignedProviderInfo(),
                                            holder.mImageViewCircularImageView, ProviderImageSize.LARGE).placeholder(drawable).build().load();
                        } catch (AWSDKInstantiationException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(THSConstants.THS_VISIT_REPORT_DETAIL,mVisitReportDetail);
                mThsVisitHistoryFragment.addFragment(new THSVisitHistoryDetailFragment(),THSProviderDetailsFragment.TAG,bundle);
            }
        };
        holder.mProviderLayout.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return mVisitReports.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        Label mLabelAppointmrntDate;
        CircularImageView mImageViewCircularImageView;
        Label mLabelProviderName;
        Label mLabelPracticeName;
        RelativeLayout mProviderLayout;
        LinearLayout mLinearLayoutButtonContainer;

        public CustomViewHolder(View view) {
            super(view);
            this.mLabelAppointmrntDate = (Label) view.findViewById(R.id.ths_appointment_date);
            this.mImageViewCircularImageView = (CircularImageView) view.findViewById(R.id.ths_providerImage);
            this.mLabelProviderName = (Label) view.findViewById(R.id.providerNameLabel);
            this.mLabelPracticeName = (Label) view.findViewById(R.id.practiceNameLabel);
            this.mProviderLayout = (RelativeLayout) view.findViewById(R.id.provider_details_layout_container);
            mLinearLayoutButtonContainer = (LinearLayout) view.findViewById(R.id.ths_button_container);
        }
    }
}
