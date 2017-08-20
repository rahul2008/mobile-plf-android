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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.visit.VisitReport;
import com.americanwell.sdk.entity.visit.VisitReportDetail;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.utility.CircularImageView;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.ImageButton;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class THSVisitHistoryDetailFragment extends THSBaseFragment{
    VisitReportDetail mVisitReportDetail;
    Label mLabelVisit;
    CircularImageView mProviderImage;
    Label mLabelProviderName;
    Label mLabelPracticeName;
    Label mLabelAppointmentDate;
    Label mLabelCreditCardCharge;
    private Button continueButton;
    RelativeLayout mRelativeLayoutProviderLayout;
    RelativeLayout mRelativeLayoutDownloadReport;
    THSVisitHistoryDetailPresenter mThsVisitHistoryPresenter;
    private VisitReport mVisitReport;
    private Label pharmacyName, pharmacyZip, pharmacyState, pharmacyAddressLineOne, pharmacyAddressLIneTwo,
            consumerName, consumerCity, consumerShippingAddress, consumerState, consumerShippingZip;
    ImageButton mImageButtonPharmacyEdit;
    ImageButton mImageButtonShippingAddressEdit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_visit_summary, container, false);
        final Bundle arguments = getArguments();
        mVisitReportDetail = arguments.getParcelable(THSConstants.THS_VISIT_REPORT_DETAIL);
        mVisitReport = arguments.getParcelable(THSConstants.THS_VISIT_REPORT);
        mThsVisitHistoryPresenter = new THSVisitHistoryDetailPresenter(this);
        mLabelVisit = (Label) view.findViewById(R.id.ths_waiting_room_title_label);
        mProviderImage = (CircularImageView) view.findViewById(R.id.details_providerImage);
        mLabelProviderName = (Label) view.findViewById(R.id.details_providerNameLabel);
        mLabelPracticeName = (Label) view.findViewById(R.id.details_practiceNameLabel);

        mLabelAppointmentDate = (Label) view.findViewById(R.id.ths_appointment_date);
        mLabelAppointmentDate.setVisibility(View.VISIBLE);

        mLabelCreditCardCharge = (Label) view.findViewById(R.id.ths_wrap_up_payment_cost);
        mImageButtonPharmacyEdit = (ImageButton) view.findViewById(R.id.ps_edit_pharmacy);
        mImageButtonPharmacyEdit.setVisibility(View.GONE);

        mImageButtonShippingAddressEdit = (ImageButton) view.findViewById(R.id.ps_edit_consumer_shipping_address);
        mImageButtonShippingAddressEdit.setVisibility(View.GONE);

        Label prescriptionLabel = (Label) view.findViewById(R.id.ps_prescription_sent_label);
        prescriptionLabel.setText("Your prescription was sent to");
        consumerCity = (Label) view.findViewById(R.id.ps_consumer_city);
        consumerName = (Label) view.findViewById(R.id.ps_consumer_name);
        consumerShippingAddress = (Label) view.findViewById(R.id.ps_consumer_shipping_address);
        consumerShippingZip = (Label) view.findViewById(R.id.ps_consumer_shipping_zip);
        consumerState = (Label) view.findViewById(R.id.ps_consumer_state);
        pharmacyAddressLineOne = (Label) view.findViewById(R.id.ps_pharmacy_address_line_one);
        pharmacyAddressLIneTwo = (Label) view.findViewById(R.id.ps_pharmacy_address_line_two);
        pharmacyName = (Label) view.findViewById(R.id.ps_pharmacy_name);
        pharmacyState = (Label) view.findViewById(R.id.ps_pharmacy_state);
        pharmacyZip = (Label) view.findViewById(R.id.ps_pharmacy_zip_code);
        continueButton =(Button)view.findViewById(R.id.ths_visit_summary_continue_button);
        continueButton.setVisibility(View.GONE);
        mRelativeLayoutProviderLayout = (RelativeLayout) view.findViewById(R.id.ths_waiting_room_provider_detail_relativelayout);
        mRelativeLayoutProviderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mVisitReportDetail==null || mVisitReportDetail.getAssignedProviderInfo()==null){
                    showToast("No Assigned Provider");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putParcelable(THSConstants.THS_PROVIDER_INFO,mVisitReportDetail.getAssignedProviderInfo());
                bundle.putParcelable(THSConstants.THS_PRACTICE_INFO,mVisitReportDetail.getAssignedProviderInfo().getPracticeInfo());
                addFragment(new THSProviderDetailsFragment(),THSProviderDetailsFragment.TAG,bundle);
            }
        });
        mRelativeLayoutDownloadReport = (RelativeLayout) view.findViewById(R.id.ths_wrap_up_visit_report_relativelayout);
        mRelativeLayoutDownloadReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mThsVisitHistoryPresenter.onEvent(R.id.ths_pdf_container);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBarListener actionBarListener = getActionBarListener();
        if(null != actionBarListener){
            actionBarListener.updateActionBar(getString(R.string.ths_visit),true);
        }
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

        if(mVisitReportDetail.getVisitCost().isFree()){
            mLabelCreditCardCharge.setText("$ 0");
        }else{
            double cost= mVisitReportDetail.getVisitCost().getExpectedConsumerCopayCost();

            mLabelCreditCardCharge.setText(Double.toString(cost));
        }

        Address address = mVisitReportDetail.getShippingAddress();
        String consumerFullName = mVisitReportDetail.getConsumerInfo().getFullName();
        updateShippingAddressView(address, consumerFullName);
        Pharmacy pharmacy = mVisitReportDetail.getPharmacy();
        updatePharmacyDetailsView(pharmacy);

        mLabelCreditCardCharge.setText(mVisitReportDetail.getPaymentAmount() + "");
    }

    public VisitReport getVisitReport() {
        return mVisitReport;
    }

    public void updateShippingAddressView(Address address, String consumerName1) {
        consumerName.setText(consumerName1);
        consumerCity.setText(address.getCity());
        consumerState.setText(address.getState().getCode());
        consumerShippingAddress.setText(address.getAddress1());
        consumerShippingZip.setText(address.getZipCode());
    }

    public void updatePharmacyDetailsView(Pharmacy pharmacy) {
        pharmacyAddressLineOne.setText(pharmacy.getAddress().getAddress1());
        pharmacyAddressLIneTwo.setText(pharmacy.getAddress().getAddress2());
        pharmacyName.setText(pharmacy.getName());
        pharmacyState.setText(pharmacy.getAddress().getState().getCode());
        pharmacyZip.setText(pharmacy.getAddress().getZipCode());
    }
}
