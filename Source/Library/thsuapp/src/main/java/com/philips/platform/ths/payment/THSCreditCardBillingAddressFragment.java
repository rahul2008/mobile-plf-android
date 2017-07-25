package com.philips.platform.ths.payment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.pharmacy.THSSpinnerAdapter;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.Label;

import java.util.List;

/**
 * Created by philips on 7/23/17.
 */

public class THSCreditCardBillingAddressFragment extends THSBaseFragment implements View.OnClickListener {

    public static final String TAG = THSCreditCardBillingAddressFragment.class.getSimpleName();
    private ActionBarListener actionBarListener;
    THSCreditCardBillingAddressPresenter mTHSCreditCardBillingAddressPresenter;
    Bundle mBundle;

    Label mBillingAddresslabel;
    private RelativeLayout mProgressbarContainer;

    AppCompatSpinner stateSpinner;
    private THSSpinnerAdapter stateSpinnerAdapter;
    List<State> stateList = null;

    EditText mAddressOneEditText;
    EditText mAddressTwoEditText;
    EditText mCityEditText;
    EditText mZipcodeEditText;
    Button mContinueButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_shipping_address_fragment, container, false);
        mBundle = getArguments();
        mBillingAddresslabel = (Label) view.findViewById(R.id.shipping_address_text_label);
        mBillingAddresslabel.setText("Billing Address"); //todo put in string.xml
        mAddressOneEditText = (EditText) view.findViewById(R.id.sa_shipping_address_line_one);
        mAddressTwoEditText = (EditText) view.findViewById(R.id.sa_shipping_address_line_two);
        mCityEditText = (EditText) view.findViewById(R.id.sa_town);
        mZipcodeEditText = (EditText) view.findViewById(R.id.sa_postal_code);
        mContinueButton = (Button) view.findViewById(R.id.update_shipping_address);
        mContinueButton.setOnClickListener(this);
        mTHSCreditCardBillingAddressPresenter = new THSCreditCardBillingAddressPresenter(this);
        stateSpinner = (AppCompatSpinner) view.findViewById(R.id.sa_state_spinner);

        try {
            stateList = THSManager.getInstance().getAwsdk(getActivity().getApplicationContext()).getConsumerManager().getValidPaymentMethodStates();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

        stateSpinnerAdapter = new THSSpinnerAdapter(getActivity(), R.layout.ths_pharmacy_spinner_layout, stateList);
        stateSpinner.setAdapter(stateSpinnerAdapter);
        stateSpinner.setSelection(0);
        mProgressbarContainer = (RelativeLayout) view.findViewById(R.id.shipping_address_container);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
        //createCustomProgressBar(mProgressbarContainer, MEDIUM);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar("Billing address", true);
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
        if (v.getId() == R.id.update_shipping_address) {
            mTHSCreditCardBillingAddressPresenter.onEvent(R.id.update_shipping_address);
        }

    }
}
