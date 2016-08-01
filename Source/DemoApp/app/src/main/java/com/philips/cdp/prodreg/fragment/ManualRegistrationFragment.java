package com.philips.cdp.prodreg.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.R;
import com.philips.cdp.prodreg.launcher.ActivityLauncher;
import com.philips.cdp.prodreg.launcher.FragmentLauncher;
import com.philips.cdp.prodreg.launcher.ProdRegConfig;
import com.philips.cdp.prodreg.launcher.ProdRegUiHelper;
import com.philips.cdp.prodreg.listener.ActionbarUpdateListener;
import com.philips.cdp.prodreg.listener.ProdRegUiListener;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;
import com.philips.cdp.prodreg.util.ProdRegUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ManualRegistrationFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = ManualRegistrationFragment.class.getName();
    private ToggleButton toggleButton;
    private EditText mFriendlyName, mSerialNumber, mPurchaseDate, mCtn;
    private Calendar mCalendar;
    private Button pr_activity_a, pr_activity_b, pr_fragment_a, pr_fragment_b;
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
            SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));
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
        mSerialNumber = (EditText) view.findViewById(R.id.edt_serial_number);
        mPurchaseDate = (EditText) view.findViewById(R.id.edt_purchase_date);
        mFriendlyName = (EditText) view.findViewById(R.id.friendly_name_edittext);
        mCtn = (EditText) view.findViewById(R.id.edt_ctn);
        pr_activity_a = (Button) view.findViewById(R.id.pr_activity_a);
        pr_activity_b = (Button) view.findViewById(R.id.pr_activity_b);
        pr_fragment_a = (Button) view.findViewById(R.id.pr_fragment_a);
        pr_fragment_b = (Button) view.findViewById(R.id.pr_fragment_b);
        toggleButton = (ToggleButton) view.findViewById(R.id.toggbutton);
        setOnClickListeners();
        toggleButton.setChecked(eMailConfiguration);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCtn.setText(bundle.getString("ctn") != null ? bundle.getString("ctn") : "");
            mSerialNumber.setText(bundle.getString("serial") != null ? bundle.getString("serial") : "");
            mPurchaseDate.setText(bundle.getString("date") != null ? bundle.getString("date") : "");
        }
    }

    private void setOnClickListeners() {
        pr_activity_a.setOnClickListener(this);
        pr_activity_b.setOnClickListener(this);
        pr_fragment_a.setOnClickListener(this);
        pr_fragment_b.setOnClickListener(this);
        mPurchaseDate.setOnClickListener(this);
        toggleButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.pr_activity_a:
                makeRegistrationRequest(true, "app_flow");
                break;
            case R.id.pr_activity_b:
                makeRegistrationRequest(true, "user_flow");
                break;
            case R.id.pr_fragment_a:
                makeRegistrationRequest(false, "app_flow");
                break;
            case R.id.pr_fragment_b:
                makeRegistrationRequest(false, "user_flow");
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

    private void makeRegistrationRequest(final boolean isActivity, final String type) {
        registerProduct(isActivity, type);
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
        datePickerDialog.getDatePicker().setMinDate(ProdRegUtil.getMinDate());
        datePickerDialog.show();
    }

    private void registerProduct(final boolean isActivity, final String type) {
        Product product = new Product(mCtn.getText().toString(), Sector.B2C, Catalog.CONSUMER);
        product.setSerialNumber(mSerialNumber.getText().toString());
        product.setPurchaseDate(mPurchaseDate.getText().toString());
        product.setFriendlyName(mFriendlyName.getText().toString());
        product.sendEmail(eMailConfiguration);
        invokeProdRegFragment(product, isActivity, type);
    }

    private void invokeProdRegFragment(Product product, final boolean isActivity, final String type) {
        ArrayList<Product> products = new ArrayList<>();
        products.add(product);
        ProdRegConfig prodRegConfig;
        if (!isActivity) {
            FragmentLauncher fragLauncher = new FragmentLauncher(
                    fragmentActivity, R.id.parent_layout, new ActionbarUpdateListener() {
                @Override
                public void updateActionbar(final String var1) {
                    if (getActivity() != null && getActivity().getActionBar() != null)
                        getActivity().getActionBar().setTitle(var1);
                }
            });
            fragLauncher.setAnimation(0, 0);
            if (type.equalsIgnoreCase("app_flow")) {
                prodRegConfig = new ProdRegConfig(products, true);
            } else {
                prodRegConfig = new ProdRegConfig(products, false);
            }
            ProdRegUiHelper.getInstance().invokeProductRegistration(fragLauncher, prodRegConfig, getProdRegUiListener());
        } else {
            ActivityLauncher activityLauncher = new ActivityLauncher(getActivity(), ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0);
            if (type.equalsIgnoreCase("app_flow")) {
                prodRegConfig = new ProdRegConfig(products, true);
            } else {
                prodRegConfig = new ProdRegConfig(products, false);
            }
            ProdRegUiHelper.getInstance().invokeProductRegistration(activityLauncher, prodRegConfig, getProdRegUiListener());
        }
    }

    @NonNull
    private ProdRegUiListener getProdRegUiListener() {
        return new ProdRegUiListener() {
            @Override
            public void onProdRegContinue(final List<RegisteredProduct> registeredProduct, final UserWithProducts userWithProduct) {
                ProdRegLogger.v(TAG, registeredProduct.get(0).getRegistrationState() + "");
            }

            @Override
            public void onProdRegBack(final List<RegisteredProduct> registeredProduct, final UserWithProducts userWithProduct) {
                ProdRegLogger.v(TAG, registeredProduct.get(0).getProdRegError() + "");
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(getString(R.string.app_name));
    }

    @Override
    public void onStop() {
        super.onStop();
        hideKeyboard();
    }

    protected void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
