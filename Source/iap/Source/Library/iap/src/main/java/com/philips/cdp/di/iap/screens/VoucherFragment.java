package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.VoucherController;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.Label;


public class VoucherFragment extends InAppBaseFragment implements View.OnClickListener,VoucherController.VoucherListener {


    VoucherController mVoucherController;
    Button mApplyVoucherButton;
    Context mContext;
    EditText voucherEditText;
    RelativeLayout voucherLayout;
 //   public AppliedVoucherAdapter mAppliedVoucherAdapter;

    public static final String TAG = VoucherFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = getArguments();
       // CartModelContainer.getInstance().setVoucherCode("ASQE20"); // reset singleton
        String appliedCoupon = CartModelContainer.getInstance().getVoucherCode();
        View rootView = inflater.inflate(R.layout.iap_voucher_detail, container, false);
        voucherLayout = (RelativeLayout) rootView.findViewById(R.id.voucher_detail_layout);
        mApplyVoucherButton = (Button) rootView.findViewById(R.id.voucher_btn);
        voucherEditText = (EditText) rootView.findViewById(R.id.coupon_editText);
        voucherEditText.addTextChangedListener(new IAPTextWatcher(voucherEditText));
        Label voucherCodeLabel = (Label) rootView.findViewById(R.id.enter_voucher_label);
        mApplyVoucherButton.setEnabled(false);
        mApplyVoucherButton.setOnClickListener(this);
        mVoucherController = new VoucherController(mContext, VoucherFragment.this);
       // mAppliedVoucherAdapter=new AppliedVoucherAdapter(mContext,);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitleAndBackButtonVisibility(R.string.iap_apply_voucher, true);
    }

    @Override
    public void onStop() {
        super.onStop();
        /*if (mAppliedVoucherAdapter != null)
            mAppliedVoucherAdapter.onStop();*/
        hideProgressBar();
        NetworkUtility.getInstance().dismissErrorDialog();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.voucher_btn) {

            if (null != voucherEditText.getText().toString()) {
                createCustomProgressBar(voucherLayout, BIG);
                mVoucherController.applyCoupon(voucherEditText.getText().toString().trim());
            }
        }
    }

    @Override
    public void onApplyVoucherResponse(Message msg) {
        hideProgressBar();
        if(null==msg.obj) {
            Toast.makeText(getActivity(), "Vouchers Applied Successfully",
                    Toast.LENGTH_SHORT).show();
            // mVoucherFragment.getActivity().getSupportFragmentManager().popBackStackImmediate();
            NetworkUtility.getInstance().showVoucherSuccessMessage(msg, getFragmentManager(),mContext);
        }
        if ((msg.obj instanceof IAPNetworkError)) {
            NetworkUtility.getInstance().showVoucherErrorMessage(msg, getFragmentManager(),mContext);
        }
    }


    private class IAPTextWatcher implements TextWatcher {
        private android.widget.EditText mEditText;

        IAPTextWatcher(android.widget.EditText editText) {
            mEditText = editText;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //  Do nothing
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (voucherEditText .getText().toString().trim().length() !=0) {
                    mApplyVoucherButton.setEnabled(true);
                }
                else {
                    mApplyVoucherButton.setEnabled(false);
                }
        }

        public synchronized void afterTextChanged(Editable text) {
            if (voucherEditText .getText().toString().trim().length() ==0) {
                mApplyVoucherButton.setEnabled(false);
            }
        }
    }

}
