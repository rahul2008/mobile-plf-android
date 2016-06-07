package com.philips.cdp.prodreg.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.R;
import com.philips.cdp.prodreg.Util;
import com.philips.cdp.prodreg.launcher.FragmentLauncher;
import com.philips.cdp.prodreg.listener.ActionbarUpdateListener;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.util.ProdRegConfigManager;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ManualRegistrationFragment extends Fragment implements View.OnClickListener {

    private ToggleButton toggleButton;
    private EditText mRegChannel, mSerialNumber, mPurchaseDate, mCtn;
    private String TAG = getClass().toString();
    private Calendar mCalendar;
    private Button submitProduct;
    private boolean eMailConfiguration = false;
    private FragmentActivity fragmentActivity;
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            Log.d(TAG, "Response Data : " + arg1 + "-" + arg2 + "-" + arg3);
            final int mMonthInt = (arg2 + 1);
            String mMonth;
            if (mMonthInt < 10) {
                mMonth = getResources().getString(R.string.zero) + mMonthInt;
            } else {
                mMonth = Integer.toString(mMonthInt);
            }
            String mDate;
            if (arg3 < 10) {
                mDate = getResources().getString(R.string.zero) + arg3;
            } else {
                mDate = Integer.toString(arg3);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_formate));
            mCalendar = Calendar.getInstance();
            final String mGetDeviceDate = dateFormat.format(mCalendar.getTime());
            Date mDisplayDate;
            Date mDeviceDate;
            try {
                final String text = arg1 + "-" + mMonth + "-" + mDate;
                mDisplayDate = dateFormat.parse(text);
                mDeviceDate = dateFormat.parse(mGetDeviceDate);
                if (mDisplayDate.after(mDeviceDate)) {
                    Log.d(TAG, " Response Data : " + "Error in Date");
                } else {
                    mPurchaseDate.setText(text);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_activity_main, container, false);
        init(view);
        return view;
    }

    private void init(final View view) {
        fragmentActivity = getActivity();
        mRegChannel = (EditText) view.findViewById(R.id.edt_reg_channel);
        mSerialNumber = (EditText) view.findViewById(R.id.edt_serial_number);
        mPurchaseDate = (EditText) view.findViewById(R.id.edt_purchase_date);
        mCtn = (EditText) view.findViewById(R.id.edt_ctn);
        submitProduct = (Button) view.findViewById(R.id.submitproduct);
        submitProduct.setOnClickListener(this);
        mPurchaseDate.setOnClickListener(this);
        toggleButton = (ToggleButton) view.findViewById(R.id.toggbutton);
        toggleButton.setChecked(eMailConfiguration);
        toggleButton.setOnClickListener(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCtn.setText(bundle.getString("ctn") != null ? bundle.getString("ctn") : "");
            mSerialNumber.setText(bundle.getString("serial") != null ? bundle.getString("serial") : "");
            mPurchaseDate.setText(bundle.getString("date") != null ? bundle.getString("date") : "");
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.submitproduct:
                final User mUser = new User(fragmentActivity);
                if (!(mUser.isUserSignIn() && mUser.getEmailVerificationStatus())) {
                    Log.d(TAG, "On Click : User Registration");
                    RegistrationLaunchHelper.launchRegistrationActivityWithAccountSettings(fragmentActivity);
                    Util.navigateFromUserRegistration();
                }
                if (mCtn.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(fragmentActivity, getResources().getString(R.string.enter_ctn_number), Toast.LENGTH_SHORT).show();
                } else {
                    String MICRO_SITE_ID = "MS";
                    final String text = MICRO_SITE_ID + RegistrationConfiguration.getInstance().getPilConfiguration().getMicrositeId();
                    mRegChannel.setText(text);
                    registerProduct();
                }
                break;
            case R.id.toggbutton:
                eMailConfiguration = toggleButton.isChecked();
                break;
            case R.id.edt_purchase_date:
                onClickPurchaseDate();
            default:
                break;
        }
    }

    public void onClickPurchaseDate() {
        int mYear;
        int mMonthInt;
        int mDay;
        if (!mPurchaseDate.getText().toString().equalsIgnoreCase("")) {
            final String[] mEditDisplayDate = mPurchaseDate.getText().toString().split("-");
            mYear = Integer.parseInt(mEditDisplayDate[0]);
            mMonthInt = Integer.parseInt(mEditDisplayDate[1]) - 1;
            mDay = Integer.parseInt(mEditDisplayDate[2]);
        } else {
            mCalendar = Calendar.getInstance();
            mYear = mCalendar.get(Calendar.YEAR);
            mMonthInt = mCalendar.get(Calendar.MONTH);
            mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(fragmentActivity, myDateListener, mYear, mMonthInt, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void registerProduct() {
        Product product = new Product(mCtn.getText().toString(), Sector.B2C, Catalog.CONSUMER);
        product.setSerialNumber(mSerialNumber.getText().toString());
        product.setPurchaseDate(mPurchaseDate.getText().toString());
        product.sendEmail(eMailConfiguration);
        invokeProdRegFragment(product);
    }

    private void invokeProdRegFragment(Product product) {
        FragmentLauncher fragLauncher = new FragmentLauncher(
                fragmentActivity, R.id.parent_layout, new ActionbarUpdateListener() {
            @Override
            public void updateActionbar(final String var1) {
            }
        });
        fragLauncher.setAnimation(0, 0);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        fragLauncher.setArguments(bundle);
        ProdRegConfigManager.getInstance().invokeProductRegistration(fragLauncher);
    }
}
