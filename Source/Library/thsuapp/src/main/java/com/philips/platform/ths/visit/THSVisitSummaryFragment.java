package com.philips.platform.ths.visit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.ImageButton;
import com.philips.platform.uid.view.widget.Label;

/**
 * Created by philips on 8/4/17.
 */

public class THSVisitSummaryFragment extends THSBaseFragment implements View.OnClickListener {
    public static final String TAG = THSVisitSummaryFragment.class.getSimpleName();

    private ActionBarListener actionBarListener;
    THSVisitSummaryPresenter mTHSVisitSummaryPresenter;
    THSRatingDialogFragment thsRatingDialogFragment;
    private Button continueButton;
     Label pharmacyName, pharmacyZip, pharmacyState, pharmacyAddressLineOne, pharmacyAddressLIneTwo,
            consumerName, consumerCity, consumerShippingAddress, consumerState, consumerShippingZip;

    Integer mProviderRating;
    Integer mVisitRating;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_visit_summary, container, false);


        // hiding unused components
        view.findViewById(R.id.isAvailableLayout).setVisibility(View.GONE);
        view.findViewById(R.id.ps_edit_consumer_shipping_address).setVisibility(View.GONE);
        view.findViewById(R.id.ps_edit_pharmacy).setVisibility(View.GONE);

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
       // mTHSVisitSummaryPresenter.fetchVisitSummary();
       // thsRatingDialogFragment = new THSRatingDialogFragment();
        thsRatingDialogFragment = new THSRatingDialogFragment();
        thsRatingDialogFragment.setThsVisitSummaryPresenter(mTHSVisitSummaryPresenter);
        thsRatingDialogFragment.show(getFragmentManager(),"TAG");


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
        if(v.getId()==R.id.ths_visit_summary_continue_button){
            mTHSVisitSummaryPresenter.onEvent(R.id.ths_visit_summary_continue_button);
        }

    }
}
