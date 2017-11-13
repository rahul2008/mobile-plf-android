package com.philips.platform.ths.visit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.visit.Visit;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.utility.CircularImageView;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.ths.welcome.THSWelcomeBackFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.utils.UIDNavigationIconToggler;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.ImageButton;
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
            consumerName, consumerCity, consumerShippingAddress, consumerState, consumerShippingZip;

    Label providerName;
    Label providerPractice;
    Label visitCost;
    Visit mVisit;
    private UIDNavigationIconToggler navIconToggler;

    Integer mProviderRating;
    Integer mVisitRating;

    protected Label medicationShippingLabel;
    protected RelativeLayout medicationShippingRelativeLayout;
   



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_visit_summary, container, false);
        navIconToggler = new UIDNavigationIconToggler(getActivity());
        

        // hiding unused components
        view.findViewById(R.id.isAvailableLayout).setVisibility(View.GONE);
        view.findViewById(R.id.ps_edit_consumer_shipping_address).setVisibility(View.GONE);
        view.findViewById(R.id.ps_edit_pharmacy).setVisibility(View.GONE);

        medicationShippingLabel =  (Label) view.findViewById(R.id.ps_shipped_to_label);
        medicationShippingRelativeLayout =(RelativeLayout) view.findViewById(R.id.ps_shipping_layout_item);

        Bundle bundle = getArguments();
        mVisit=bundle.getParcelable(THS_VISIT_ARGUMENT_KEY);

        providerName = (Label) view.findViewById(R.id.details_providerNameLabel);
        providerPractice = (Label) view.findViewById(R.id.details_practiceNameLabel);


        visitCost= (Label) view.findViewById(R.id.ths_wrap_up_payment_cost);

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
        continueButton.setOnClickListener(this);

        mTHSVisitSummaryPresenter = new THSVisitSummaryPresenter(this);
        mTHSVisitSummaryPresenter.fetchVisitSummary();

        thsRatingDialogFragment = new THSRatingDialogFragment();
        thsRatingDialogFragment.setThsVisitSummaryPresenter(mTHSVisitSummaryPresenter);
        thsRatingDialogFragment.show(getFragmentManager(),"TAG");
        mImageProviderImage = (CircularImageView) view.findViewById(R.id.details_providerImage);


        //
        Fragment mFragment = getFragmentManager().findFragmentByTag(THSWelcomeBackFragment.TAG);
        if (mFragment instanceof THSWelcomeBackFragment){
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA,THS_SPECIAL_EVENT,THS_VIDEO_CALL_ENDS);
        }else{
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA,THS_SPECIAL_EVENT,"completeInstantAppointment");

        }
        THSManager.getInstance().getThsTagging().trackPageWithInfo(THS_VISIT_SUMMARY,null,null);
        //
        return  view;
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
