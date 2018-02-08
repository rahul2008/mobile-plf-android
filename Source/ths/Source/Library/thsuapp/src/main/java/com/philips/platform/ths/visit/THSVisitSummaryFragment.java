package com.philips.platform.ths.visit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.visit.Visit;
import com.americanwell.sdk.entity.visit.VisitSummary;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.CircularImageView;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.ths.welcome.THSWelcomeBackFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.utils.UIDNavigationIconToggler;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SPECIAL_EVENT;
import static com.philips.platform.ths.utility.THSConstants.THS_VIDEO_CALL_ENDS;
import static com.philips.platform.ths.utility.THSConstants.THS_VISIT_ARGUMENT_KEY;
import static com.philips.platform.ths.utility.THSConstants.THS_VISIT_SUMMARY;

/**
 * Created by philips on 8/4/17.
 */

public class THSVisitSummaryFragment extends THSBaseFragment implements View.OnClickListener {
    public static final String TAG = THSVisitSummaryFragment.class.getSimpleName();

    private ActionBarListener actionBarListener;
    THSVisitSummaryPresenter mTHSVisitSummaryPresenter;
    THSRatingDialogFragment thsRatingDialogFragment;
    private Button continueButton;
    CircularImageView mImageProviderImage;

    Label pharmacyName, pharmacyZip, pharmacyState, pharmacyAddressLineOne, pharmacyAddressLIneTwo,
            mConsumerName, consumerCity, consumerShippingAddress, consumerState, consumerShippingZip;

    Label providerName;
    Label providerPractice;
    Label visitCost;
    Visit mVisit;
    private UIDNavigationIconToggler navIconToggler;


    protected Label medicationShippingLabel, prescriptionLabel;
    protected RelativeLayout medicationShippingRelativeLayout,ps_pharmacy_list_layout_item;
    static final long serialVersionUID = 1127L;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_visit_summary, container, false);
        navIconToggler = new UIDNavigationIconToggler(getActivity());


        // hiding unused components
        view.findViewById(R.id.isAvailableLayout).setVisibility(View.GONE);
        view.findViewById(R.id.ps_edit_consumer_shipping_address).setVisibility(View.GONE);
        view.findViewById(R.id.ps_edit_pharmacy).setVisibility(View.GONE);

        medicationShippingLabel = view.findViewById(R.id.ps_shipped_to_label);
        medicationShippingRelativeLayout = view.findViewById(R.id.ps_shipping_layout_item);

        Bundle bundle = getArguments();
        mVisit = bundle.getParcelable(THS_VISIT_ARGUMENT_KEY);

        providerName = view.findViewById(R.id.details_providerNameLabel);
        providerPractice = view.findViewById(R.id.details_practiceNameLabel);


        visitCost = view.findViewById(R.id.ths_wrap_up_payment_cost);

        prescriptionLabel = view.findViewById(R.id.ps_prescription_sent_label);
        prescriptionLabel.setText(getString(R.string.ths_prescription_sent_to_label));

        ps_pharmacy_list_layout_item = view.findViewById(R.id.ps_pharmacy_list_layout_item);


        consumerCity = view.findViewById(R.id.ps_consumer_city);
        mConsumerName = view.findViewById(R.id.ps_consumer_name);
        consumerShippingAddress = view.findViewById(R.id.ps_consumer_shipping_address);
        consumerShippingZip = view.findViewById(R.id.ps_consumer_shipping_zip);
        consumerState = view.findViewById(R.id.ps_consumer_state);
        pharmacyAddressLineOne = view.findViewById(R.id.ps_pharmacy_address_line_one);
        pharmacyAddressLIneTwo = view.findViewById(R.id.ps_pharmacy_address_line_two);
        pharmacyName = view.findViewById(R.id.ps_pharmacy_name);
        pharmacyState = view.findViewById(R.id.ps_pharmacy_state);
        pharmacyZip = view.findViewById(R.id.ps_pharmacy_zip_code);
        continueButton = view.findViewById(R.id.ths_visit_summary_continue_button);
        continueButton.setOnClickListener(this);

        mTHSVisitSummaryPresenter = new THSVisitSummaryPresenter(this);
        mTHSVisitSummaryPresenter.fetchVisitSummary();

        thsRatingDialogFragment = new THSRatingDialogFragment();
        thsRatingDialogFragment.setThsVisitSummaryPresenter(mTHSVisitSummaryPresenter);
        thsRatingDialogFragment.show(getFragmentManager(), "TAG");
        mImageProviderImage = view.findViewById(R.id.details_providerImage);


        //
        Fragment mFragment = getFragmentManager().findFragmentByTag(THSWelcomeBackFragment.TAG);
        if (mFragment instanceof THSWelcomeBackFragment) {
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, THS_VIDEO_CALL_ENDS);
        } else {
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "completeInstantAppointment");

        }
        THSTagUtils.doTrackPageWithInfo(THS_VISIT_SUMMARY, null, null);
        //
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();

    }


    @Override
    public void onResume() {
        super.onResume();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar("Thank you", true);
        }
        navIconToggler.hideNavigationIcon();
    }


    @Override
    public int getContainerID() {
        return ((ViewGroup) getView().getParent()).getId();
    }


    void updateShippingAddressView(Address address, String consumerName) {
        mConsumerName.setText(consumerName);
        if (null != address) {
            consumerCity.setText(address.getCity());
            consumerState.setText(address.getState().getCode());
            consumerShippingAddress.setText(address.getAddress1());
            consumerShippingZip.setText(address.getZipCode());
        } else {
            medicationShippingLabel.setVisibility(View.GONE);
            medicationShippingRelativeLayout.setVisibility(View.GONE);
        }
    }

    void updatePharmacyDetailsView(Pharmacy pharmacy) {
        if (null != pharmacy) {
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

    void updateView(THSVisitSummary tHSVisitSummary) {
        VisitSummary visitSummary = tHSVisitSummary.getVisitSummary();
        providerName.setText(visitSummary.getAssignedProviderInfo().getFullName());
        mTHSVisitSummaryPresenter.mProviderInfo = visitSummary.getAssignedProviderInfo();
        providerPractice.setText(visitSummary.getAssignedProviderInfo().getSpecialty().getName());
        if (tHSVisitSummary.getVisitSummary().getVisitCost().isFree()) {
            visitCost.setText("$ 0");
        } else {
            //mTHSVisitSummaryFragment.visitCost.setText(tHSVisitSummary.getVisitSummary().getVisitCost().getDeferredBillingWrapUpText());
            double cost = tHSVisitSummary.getVisitSummary().getVisitCost().getExpectedConsumerCopayCost();

            visitCost.setText("$" + Double.toString(cost));
        }
        if (visitSummary.getAssignedProviderInfo().hasImage()) {
            try {
                THSManager.getInstance().getAwsdk(getFragmentActivity()).getPracticeProvidersManager().
                        newImageLoader(visitSummary.getAssignedProviderInfo(), mImageProviderImage,
                                ProviderImageSize.SMALL).placeholder(mImageProviderImage.getResources().
                        getDrawable(R.drawable.doctor_placeholder, getFragmentActivity().getTheme())).build().load();
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        }


        Address address = visitSummary.getShippingAddress();
        String consumerFullName = visitSummary.getConsumerInfo().getFullName();
        updateShippingAddressView(address, consumerFullName);
        Pharmacy pharmacy = visitSummary.getPharmacy();
        updatePharmacyDetailsView(pharmacy);
        mTHSVisitSummaryPresenter.getVisitHistory();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        mTHSVisitSummaryPresenter.onEvent(v.getId());
    }

    @Override
    public boolean handleBackEvent() {
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        navIconToggler.restoreNavigationIcon();
    }
}
