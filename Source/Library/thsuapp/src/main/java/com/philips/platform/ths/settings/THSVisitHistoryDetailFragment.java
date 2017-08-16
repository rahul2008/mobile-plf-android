/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.visit.VisitReportDetail;
import com.americanwell.sdk.entity.visit.VisitRx;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.utility.CircularImageView;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;

public class THSVisitHistoryDetailFragment extends THSBaseFragment{
    VisitReportDetail mVisitReportDetail;
    Label mLabelVisit;
    CircularImageView mProviderImage;
    Label mLabelProviderName;
    Label mLabelPracticeName;
    Label mLabelAppointmentDate;
    Label mLabelCreditCardCharge;
    Label mPrescriptionDetail;
    Label mLabelShippingDetail;
    RelativeLayout mRelativeLayoutProviderLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_visit_report_detail, container, false);
        final Bundle arguments = getArguments();
        mVisitReportDetail = arguments.getParcelable(THSConstants.THS_VISIT_REPORT_DETAIL);
        mLabelVisit = (Label) view.findViewById(R.id.ths_waiting_room_title_label);
        mProviderImage = (CircularImageView) view.findViewById(R.id.ths_providerImage);
        mLabelProviderName = (Label) view.findViewById(R.id.providerNameLabel);
        mLabelPracticeName = (Label) view.findViewById(R.id.practiceNameLabel);
        mLabelAppointmentDate = (Label) view.findViewById(R.id.ths_appointment_date);
        mLabelCreditCardCharge = (Label) view.findViewById(R.id.ths_credit_card_charge);
        mPrescriptionDetail = (Label) view.findViewById(R.id.prescription_detail);
        mLabelShippingDetail = (Label) view.findViewById(R.id.medication_detail);
        mRelativeLayoutProviderLayout = (RelativeLayout) view.findViewById(R.id.providerDetailLayout);
        mRelativeLayoutProviderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(THSConstants.THS_PROVIDER,mVisitReportDetail.getAssignedProviderInfo());
                addFragment(new THSProviderDetailsFragment(),THSProviderDetailsFragment.TAG,bundle);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateView();
    }

    private void updateView() {
        mLabelVisit.setText(getString(R.string.ths_visit_summary));
        if(mVisitReportDetail == null){
            return;
        }
        final ProviderInfo assignedProviderInfo = mVisitReportDetail.getAssignedProviderInfo();
        if (assignedProviderInfo != null) {
            try {
                final Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.doctor_placeholder);
                THSManager.getInstance().getAwsdk(mProviderImage.getContext()).
                        getPracticeProvidersManager().
                        newImageLoader(assignedProviderInfo,
                                mProviderImage, ProviderImageSize.LARGE).placeholder(drawable).build().load();
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
            mLabelProviderName.setText(assignedProviderInfo.getFullName());

        }
        mLabelPracticeName.setText(mVisitReportDetail.getPracticeName());

        final Long actualStartTime = mVisitReportDetail.getSchedule().getActualStartTime();
        if (actualStartTime != null) {
            final String date = new SimpleDateFormat(THSConstants.DATE_TIME_FORMATTER, Locale.getDefault()).format(actualStartTime).toString();
            mLabelAppointmentDate.setText(date);
        }

        final String address1 = mVisitReportDetail.getPharmacy().getAddress().getAddress1();

        if (address1 != null) {
            mPrescriptionDetail.setText(address1.toString());
        }

        final String address2 = mVisitReportDetail.getShippingAddress().getAddress1();
        if (address2 != null) {
            mLabelShippingDetail.setText(address2.toString());
        }

        mLabelCreditCardCharge.setText(mVisitReportDetail.getPaymentAmount() + "");
    }

}
