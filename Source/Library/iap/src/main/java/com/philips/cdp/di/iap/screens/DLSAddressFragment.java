package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.controller.PaymentController;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.platform.uid.view.widget.CheckBox;

import java.util.HashMap;
import java.util.Locale;

public class DLSAddressFragment extends InAppBaseFragment implements View.OnClickListener, AddressController.AddressListener {

    public static final String TAG = DLSAddressFragment.class.getSimpleName();
    private Context mContext;
    protected Fragment shippingFragment;
    protected Fragment billingFragment;
    protected CheckBox checkBox;
    protected Button mBtnContinue;
    protected Button mBtnCancel;
    private AddressController mAddressController;
    private PaymentController mPaymentController;
    private HashMap<String, String> mShippingAddressHashMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.iap_address, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View rootView) {

        TextView tv_checkOutSteps = (TextView) rootView.findViewById(R.id.tv_checkOutSteps);
        tv_checkOutSteps.setText(String.format(mContext.getString(R.string.iap_checkout_steps), "2"));

        shippingFragment = getFragmentByID(R.id.fragment_shipping_address);
        billingFragment = getFragmentByID(R.id.fragment_billing_address);
        mBtnContinue = (Button) rootView.findViewById(R.id.btn_continue);
        mBtnCancel = (Button) rootView.findViewById(R.id.btn_cancel);

        mBtnContinue.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

        mAddressController = new AddressController(mContext, this);
        // mPaymentController = new PaymentController(mContext, this);

        checkBox = (CheckBox) rootView.findViewById(R.id.use_this_address_checkbox);
        setFragmentVisibility(billingFragment, false);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    setFragmentVisibility(billingFragment, false);
                } else {
                    setFragmentVisibility(billingFragment, true);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    public static DLSAddressFragment createInstance(Bundle args, AnimationType animType) {
        DLSAddressFragment fragment = new DLSAddressFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }


    void setFragmentVisibility(Fragment fragment, boolean isVisible) {
        FragmentManager fm = getFragmentManager();
        if (isVisible) {
            fm.beginTransaction()
                    .show(fragment)
                    .commit();
        } else {
            fm.beginTransaction()
                    .hide(fragment)
                    .commit();
        }

    }

    Fragment getFragmentByID(int id) {
        FragmentManager f = getChildFragmentManager();
        return f.findFragmentById(id);
    }


    @Override
    public void onClick(View v) {
        Utility.hideKeypad(mContext);
        if (!isNetworkConnected()) return;
        if (v == mBtnContinue) {
            //Edit and save address
            if (mBtnContinue.getText().toString().equalsIgnoreCase(getString(R.string.iap_save))) {
                if (!isProgressDialogShowing()) {
                    showProgressDialog(mContext, getString(R.string.iap_please_wait));
                    final AddressFields shippingAddress = (AddressFields) getArguments().getSerializable(IAPConstant.IAP_SHIPING_ADDRESS);
                    HashMap<String, String> addressHashMap = addressPayload(shippingAddress);

                    mAddressController.updateAddress(addressHashMap);
                }
            } else {//Add new address
//                if (!isProgressDialogShowing()) {
//                    showProgressDialog(mContext, getString(R.string.iap_please_wait));
//                    if (mlLState.getVisibility() == View.GONE)
//                        mShippingAddressFields.setRegionIsoCode(null);
//                    if (CartModelContainer.getInstance().getAddressId() != null) {
//                        HashMap<String, String> updateAddressPayload = addressPayload();
//                        if (mlLState.getVisibility() == View.VISIBLE && CartModelContainer.getInstance().getRegionIsoCode() != null)
//                            updateAddressPayload.put(ModelConstants.REGION_ISOCODE, CartModelContainer.getInstance().getRegionIsoCode());
//                        updateAddressPayload.put(ModelConstants.ADDRESS_ID, CartModelContainer.getInstance().getAddressId());
//                        mAddressController.updateAddress(updateAddressPayload);
//                    } else {
//                        mAddressController.createAddress(mShippingAddressFields);
//                    }
//                }
            }
        } else if (v == mBtnCancel) {
            Fragment fragment = getFragmentManager().findFragmentByTag(BuyDirectFragment.TAG);
            if (fragment != null) {
                moveToVerticalAppByClearingStack();
            } else {
                getFragmentManager().popBackStackImmediate();
            }
        }
    }

    private HashMap<String, String> addressPayload(AddressFields pAddressFields) {
        mShippingAddressHashMap.put(ModelConstants.FIRST_NAME, pAddressFields.getFirstName());
        mShippingAddressHashMap.put(ModelConstants.LAST_NAME, pAddressFields.getLastName());
        mShippingAddressHashMap.put(ModelConstants.LINE_1, pAddressFields.getLine1());
        mShippingAddressHashMap.put(ModelConstants.LINE_2, pAddressFields.getLine2());
        mShippingAddressHashMap.put(ModelConstants.TITLE_CODE, pAddressFields.getTitleCode().toLowerCase(Locale.getDefault()));
        mShippingAddressHashMap.put(ModelConstants.COUNTRY_ISOCODE, pAddressFields.getCountryIsocode());
        mShippingAddressHashMap.put(ModelConstants.POSTAL_CODE, pAddressFields.getPostalCode().replaceAll(" ", ""));
        mShippingAddressHashMap.put(ModelConstants.TOWN, pAddressFields.getTown());
//        if (mAddressFieldsHashmap != null)
//            mShippingAddressHashMap.put(ModelConstants.ADDRESS_ID, mAddressFieldsHashmap.get(ModelConstants.ADDRESS_ID));
        final String addressId = CartModelContainer.getInstance().getAddressId();
        if (addressId != null) {
            mShippingAddressHashMap.put(ModelConstants.ADDRESS_ID, addressId);
        }
        mShippingAddressHashMap.put(ModelConstants.PHONE_1, pAddressFields.getPhone1().replaceAll(" ", ""));
        mShippingAddressHashMap.put(ModelConstants.PHONE_2, pAddressFields.getPhone1().replaceAll(" ", ""));
        mShippingAddressHashMap.put(ModelConstants.EMAIL_ADDRESS, pAddressFields.getEmail());
        if (!CartModelContainer.getInstance().isAddessStateVisible()) {
            mShippingAddressHashMap.put(ModelConstants.REGION_ISOCODE, null);
        }

        return mShippingAddressHashMap;
    }


    @Override
    public void onGetRegions(Message msg) {

    }

    @Override
    public void onGetUser(Message msg) {

    }

    @Override
    public void onCreateAddress(Message msg) {

    }

    @Override
    public void onGetAddress(Message msg) {

    }

    @Override
    public void onSetDeliveryAddress(Message msg) {

    }

    @Override
    public void onGetDeliveryModes(Message msg) {

    }

    @Override
    public void onSetDeliveryMode(Message msg) {

    }
}
