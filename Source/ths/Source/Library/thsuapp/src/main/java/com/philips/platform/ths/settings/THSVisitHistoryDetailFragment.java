/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.ths.visit.THSDownloadReportPrivacyNoticeFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.ImageButton;
import com.philips.platform.uid.view.widget.Label;

import java.text.SimpleDateFormat;
import java.util.Locale;


import static com.philips.platform.ths.utility.THSConstants.THS_HIPPA;
import static com.philips.platform.ths.utility.THSConstants.THS_VISIT_HISTORY;

public class THSVisitHistoryDetailFragment extends THSBaseFragment {
    Label mLabelVisit;
    CircularImageView mProviderImage;
    Label mLabelProviderName;
    Label mLabelPracticeName;
    Label mLabelAppointmentDate;
    Label mLabelCreditCardCharge;
    private Button continueButton;
    RelativeLayout mRelativeLayoutProviderLayout;
    RelativeLayout mRelativeLayoutDownloadReport;
    RelativeLayout mRelativeLayoutSummaryReport;
    THSVisitHistoryDetailPresenter mThsVisitHistoryPresenter;
    private VisitReport mVisitReport;
    private Label pharmacyName;
    protected Label pharmacyZip;
    private Label pharmacyState;
    private Label pharmacyAddressLineOne;
    private Label pharmacyAddressLIneTwo;
    private Label consumerName;
    private Label consumerCity;
    private Label consumerShippingAddress;
    private Label consumerState;
    protected Label consumerShippingZip;
    Label mImageButtonPharmacyEdit;
    Label mImageButtonShippingAddressEdit;

    protected Label medicationShippingLabel,prescriptionLabel;
    protected RelativeLayout medicationShippingRelativeLayout;
    private RelativeLayout mLayoutContainer;
    private RelativeLayout mAvailableTimeSlots,ps_pharmacy_list_layout_item;
    static final long serialVersionUID = 143L;
    public static final String TAG = THSVisitHistoryDetailFragment.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_visit_summary, container, false);
        final Bundle arguments = getArguments();
        mVisitReport = arguments.getParcelable(THSConstants.THS_VISIT_REPORT);

        mLayoutContainer = (RelativeLayout) view.findViewById(R.id.scroll_view_container);

        mThsVisitHistoryPresenter = new THSVisitHistoryDetailPresenter(this);
        mLabelVisit = (Label) view.findViewById(R.id.ths_waiting_room_title_label);
        mProviderImage = (CircularImageView) view.findViewById(R.id.details_providerImage);
        mLabelProviderName = (Label) view.findViewById(R.id.details_providerNameLabel);
        mLabelPracticeName = (Label) view.findViewById(R.id.details_practiceNameLabel);
        mAvailableTimeSlots = (RelativeLayout) view.findViewById(R.id.isAvailableLayout);
        mAvailableTimeSlots.setVisibility(View.GONE);

        mLabelAppointmentDate = (Label) view.findViewById(R.id.ths_appointment_date);
        mLabelAppointmentDate.setVisibility(View.VISIBLE);

        mLabelCreditCardCharge = (Label) view.findViewById(R.id.ths_wrap_up_payment_cost);
        mImageButtonPharmacyEdit = view.findViewById(R.id.ps_edit_pharmacy);
        mImageButtonPharmacyEdit.setVisibility(View.GONE);

        mImageButtonShippingAddressEdit = view.findViewById(R.id.ps_edit_consumer_shipping_address);
        mImageButtonShippingAddressEdit.setVisibility(View.GONE);

        prescriptionLabel = (Label) view.findViewById(R.id.ps_prescription_sent_label);
        prescriptionLabel.setText("Your prescription was sent to");
        ps_pharmacy_list_layout_item = view.findViewById(R.id.ps_pharmacy_list_layout_item);
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
        continueButton = (Button) view.findViewById(R.id.ths_visit_summary_continue_button);
        continueButton.setVisibility(View.GONE);
        mRelativeLayoutProviderLayout = (RelativeLayout) view.findViewById(R.id.ths_waiting_room_provider_detail_relativelayout);

        mRelativeLayoutSummaryReport = (RelativeLayout) view.findViewById(R.id.ths_wrap_up_visit_summary_report_relativelayout);
        mRelativeLayoutSummaryReport.setVisibility(View.GONE);
        mRelativeLayoutDownloadReport = (RelativeLayout) view.findViewById(R.id.ths_wrap_up_visit_report_relativelayout);
        mRelativeLayoutDownloadReport.setVisibility(View.VISIBLE);
        mRelativeLayoutDownloadReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                THSDownloadReportPrivacyNoticeFragment tHSDownloadReportPrivacyNoticeFragment = new THSDownloadReportPrivacyNoticeFragment();
                tHSDownloadReportPrivacyNoticeFragment.setPresenter(mThsVisitHistoryPresenter);
                tHSDownloadReportPrivacyNoticeFragment.show(getFragmentManager(), "TAG");
            }
        });

        medicationShippingLabel = (Label) view.findViewById(R.id.ps_shipped_to_label);
        medicationShippingRelativeLayout = (RelativeLayout) view.findViewById(R.id.ps_shipping_layout_item);

        ActionBarListener actionBarListener = getActionBarListener();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar(getString(R.string.ths_visit), true);
        }
        createCustomProgressBar(mLayoutContainer, BIG);
        mThsVisitHistoryPresenter.getVisitReportDetail(mVisitReport);
        return view;
    }

    protected void updateView(final VisitReportDetail visitReportDetail) {
        mLayoutContainer.setVisibility(View.VISIBLE);
        mLabelVisit.setText(getString(R.string.ths_visit_summary));
        if (visitReportDetail == null) {
            return;
        }
        final ProviderInfo assignedProviderInfo = visitReportDetail.getAssignedProviderInfo();
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
            mLabelPracticeName.setText(assignedProviderInfo.getSpecialty().getName());
        }


        final Long actualStartTime = visitReportDetail.getSchedule().getActualStartTime();
        if (actualStartTime != null) {
            final String date = new SimpleDateFormat(THSConstants.DATE_TIME_FORMATTER, Locale.getDefault()).format(actualStartTime).toString();
            mLabelAppointmentDate.setText(date);
        }

        Address address = visitReportDetail.getShippingAddress();
        String consumerFullName = visitReportDetail.getConsumerInfo().getFullName();
        updateShippingAddressView(address, consumerFullName);
        Pharmacy pharmacy = visitReportDetail.getPharmacy();
        updatePharmacyDetailsView(pharmacy);

        mLabelCreditCardCharge.setText("$" + visitReportDetail.getPaymentAmount());

        mRelativeLayoutProviderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visitReportDetail == null || visitReportDetail.getAssignedProviderInfo() == null) {
                    showError("No Assigned Provider");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putParcelable(THSConstants.THS_PROVIDER_INFO, visitReportDetail.getAssignedProviderInfo());
                bundle.putParcelable(THSConstants.THS_PRACTICE_INFO, visitReportDetail.getAssignedProviderInfo().getPracticeInfo());
                addFragment(new THSProviderDetailsFragment(), THSProviderDetailsFragment.TAG, bundle, false);
            }
        });
    }

    public VisitReport getVisitReport() {
        return mVisitReport;
    }

    public void updateShippingAddressView(Address address, String consumerName1) {
        consumerName.setText(consumerName1);
        if (address != null) {
            consumerCity.setText(address.getCity());
            consumerState.setText(address.getState().getCode());
            consumerShippingAddress.setText(address.getAddress1());
            consumerShippingZip.setText(address.getZipCode());
        } else {
            medicationShippingLabel.setVisibility(View.GONE);
            medicationShippingRelativeLayout.setVisibility(View.GONE);
        }
    }

    public void updatePharmacyDetailsView(Pharmacy pharmacy) {
        if (pharmacy != null) {
            pharmacyAddressLineOne.setText(pharmacy.getAddress().getAddress1());
            pharmacyAddressLIneTwo.setText(pharmacy.getAddress().getAddress2());
            pharmacyName.setText(pharmacy.getName());
            pharmacyState.setText(pharmacy.getAddress().getState().getCode());
            pharmacyZip.setText(pharmacy.getAddress().getZipCode());
        }else {
            prescriptionLabel.setVisibility(View.GONE);
            ps_pharmacy_list_layout_item.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        THSTagUtils.doTrackPageWithInfo(THS_VISIT_HISTORY, null, null);
    }

    public void showHippsNotice(String url) {
        //THSTagUtils.doTrackPageWithInfo(THS_HIPPA,null,null);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
        THSTagUtils.doTrackPageWithInfo(THS_HIPPA,null,null);
    }
}
