/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.philips.cdp.prodreg.constants.ProdRegError;
import com.philips.cdp.prodreg.error.ErrorHandler;
import com.philips.cdp.prodreg.imagehandler.ImageRequestHandler;
import com.philips.cdp.prodreg.listener.DialogOkButtonListener;
import com.philips.cdp.prodreg.localcache.ProdRegCache;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.model.summary.Data;
import com.philips.cdp.prodreg.register.ProdRegRegistrationController;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.tagging.AnalyticsConstants;
import com.philips.cdp.prodreg.tagging.ProdRegTagging;
import com.philips.cdp.prodreg.util.ProdRegUtil;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.uikit.customviews.InlineForms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProdRegRegistrationFragment extends ProdRegBaseFragment implements ProdRegRegistrationController.RegisterControllerCallBacks {

    public static final String TAG = ProdRegRegistrationFragment.class.getName();
    private ImageLoader imageLoader;
    private TextView productFriendlyNameTextView, productTitleTextView, productCtnTextView;
    private Button registerButton;
    private ImageView productImageView;
    private EditText serial_number_editText, date_EditText;
    private InlineForms serialLayout, purchaseDateLayout;
    private ProdRegRegistrationController prodRegRegistrationController;
    private boolean textWatcherCalled = false;
    private boolean loadingFlag = false;
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            final int mMonthInt = (arg2 + 1);
            final ProdRegUtil prodRegUtil = new ProdRegUtil();
            String mMonth = prodRegUtil.getValidatedString(mMonthInt);
            String mDate = prodRegUtil.getValidatedString(arg3);
            SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));
            final Calendar mCalendar = Calendar.getInstance();
            final String mGetDeviceDate = dateFormat.format(mCalendar.getTime());
            try {
                final String text = arg1 + "-" + mMonth + "-" + mDate;
                final Date mDisplayDate = dateFormat.parse(text);
                final Date mDeviceDate = dateFormat.parse(mGetDeviceDate);
                if (!mDisplayDate.after(mDeviceDate)) {
                    date_EditText.setText(text);
                    prodRegRegistrationController.isValidDate(text);
                }
            } catch (ParseException e) {
                ProdRegLogger.e(TAG, e.getMessage());
            }
        }
    };

    @Override
    public String getActionbarTitle() {
        return getActivity().getString(R.string.PPR_NavBar_Title);
    }

    @Override
    public List<RegisteredProduct> getRegisteredProducts() {
        return prodRegRegistrationController.getRegisteredProducts();
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        prodRegRegistrationController = new ProdRegRegistrationController(this, getActivity());
        dismissLoadingDialog();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_single_product, container, false);
        productFriendlyNameTextView = (TextView) view.findViewById(R.id.friendly_name);
        productTitleTextView = (TextView) view.findViewById(R.id.product_title);
        productCtnTextView = (TextView) view.findViewById(R.id.product_ctn);
        serial_number_editText = (EditText) view.findViewById(R.id.serial_edit_text);
        date_EditText = (EditText) view.findViewById(R.id.date_edit_text);
        serialLayout = (InlineForms) view.findViewById(R.id.InlineForms_serial_number);
        purchaseDateLayout = (InlineForms) view.findViewById(R.id.InlineForms_date);
        imageLoader = ImageRequestHandler.getInstance(getActivity().getApplicationContext()).getImageLoader();
        registerButton = (Button) view.findViewById(R.id.btn_register);
        productImageView = (ImageView) view.findViewById(R.id.product_image);
        registerButton.setOnClickListener(onClickRegister());
        date_EditText.setKeyListener(null);
        date_EditText.setOnClickListener(onClickPurchaseDate());
        return view;
    }

    @Override
    public void onStart() {
        resetErrorDialogIfExists();
        super.onStart();
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textWatcherCalled = false;
        Bundle bundle = getArguments();
        prodRegRegistrationController.init(bundle);
        prodRegRegistrationController.handleState();
    }

    @NonNull
    private TextWatcher getWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (textWatcherCalled)
                    prodRegRegistrationController.isValidSerialNumber(serial_number_editText.getText().toString());
                textWatcherCalled = true;
            }
        };
    }

    private void handleSerialNumberEditTextOnError() {
        serialLayout.setValidator(new InlineForms.Validator() {
            @Override
            public void validate(final View editText, final boolean hasFocus) {
                if (!hasFocus)
                    prodRegRegistrationController.isValidSerialNumber(serial_number_editText.getText().toString());
            }
        });
    }

    private void showErrorMessageSerialNumber(final EditText editTextView, final String format) {
        if (TextUtils.isEmpty(format)) {
            serialLayout.setErrorMessage(getActivity().getString(R.string.PPR_Please_Enter_SerialNum_Txtfldtxt));
        } else
            serialLayout.setErrorMessage(new ErrorHandler().getError(getActivity(), ProdRegError.INVALID_SERIALNUMBER.getCode()).getDescription() + format);
        serialLayout.showError(editTextView);
    }

    private void handleDateEditTextOnError() {
        purchaseDateLayout.setValidator(new InlineForms.Validator() {
            @Override
            public void validate(final View editText, final boolean hasFocus) {
                if (!hasFocus)
                    prodRegRegistrationController.isValidDate(date_EditText.getText().toString());
            }
        });
    }

    private void showErrorMessageDate(final EditText editTextView) {
        final FragmentActivity activity = getActivity();
        purchaseDateLayout.setErrorMessage(new ErrorHandler().getError(activity, ProdRegError.INVALID_DATE.getCode()).getDescription());
        ProdRegTagging.getInstance().trackActionWithCommonGoals("ProdRegRegistrationScreen", "specialEvents", "purchaseDateRequired");
        final ProdRegCache prodRegCache = new ProdRegCache(activity);
        new ProdRegUtil().storeProdRegTaggingMeasuresCount(prodRegCache, AnalyticsConstants.Product_REGISTRATION_DATE_COUNT, 1);
        ProdRegTagging.getInstance().trackActionWithCommonGoals("ProdRegRegistrationScreen", "noOfProductRegistrationStarts", String.valueOf(prodRegCache.getIntData(AnalyticsConstants.Product_REGISTRATION_DATE_COUNT)));
        purchaseDateLayout.showError(editTextView);
    }

    private View.OnClickListener onClickPurchaseDate() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                int mYear;
                int mMonthInt;
                int mDay;
                if (!date_EditText.getText().toString().equalsIgnoreCase("")) {
                    final String[] mEditDisplayDate = date_EditText.getText().toString().split("-");
                    mYear = Integer.parseInt(mEditDisplayDate[0]);
                    mMonthInt = Integer.parseInt(mEditDisplayDate[1]) - 1;
                    mDay = Integer.parseInt(mEditDisplayDate[2]);
                } else {
                    final Calendar mCalendar = Calendar.getInstance();
                    mYear = mCalendar.get(Calendar.YEAR);
                    mMonthInt = mCalendar.get(Calendar.MONTH);
                    mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), myDateListener, mYear, mMonthInt, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMinDate(new ProdRegUtil().getMinDate());
                datePickerDialog.show();
            }
        };
    }

    @NonNull
    private View.OnClickListener onClickRegister() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                prodRegRegistrationController.registerProduct(date_EditText.getText().toString(), serial_number_editText.getText().toString());
            }
        };
    }

    @Override
    public boolean onBackPressed() {
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            return clearFragmentStack(true);
        }
        return true;
    }

    @Override
    public DialogOkButtonListener getDialogOkButtonListener() {
        return new DialogOkButtonListener() {
            @Override
            public void onOkButtonPressed() {
                dismissAlertOnError();
            }
        };
    }

    @Override
    public void exitProductRegistration() {
        clearFragmentStack(true);
    }

    @Override
    public void showAlertOnError(int responseCode) {
        super.showAlertOnError(responseCode);
    }

    @Override
    public void showFragment(Fragment fragment) {
        super.showFragment(fragment);
    }

    @Override
    public void isValidDate(boolean validDate) {
        if (validDate) {
            purchaseDateLayout.removeError(date_EditText);
        } else
            showErrorMessageDate(date_EditText);
    }

    @Override
    public void isValidSerialNumber(boolean validSerialNumber, String format) {
        if (validSerialNumber) {
            serialLayout.removeError(serial_number_editText);
        } else
            showErrorMessageSerialNumber(serial_number_editText, format);
    }

    @Override
    public void setSummaryView(final Data summaryData) {
        if (summaryData != null) {
            final String productTitle = summaryData.getProductTitle();
            if (!TextUtils.isEmpty(productTitle)) {
                productTitleTextView.setVisibility(View.VISIBLE);
                productTitleTextView.setText(productTitle);
            } else {
                productTitleTextView.setVisibility(View.GONE);
            }
            imageLoader.get(summaryData.getImageURL(), ImageLoader.getImageListener(productImageView, R.drawable.product_placeholder, R.drawable.product_placeholder));
            serial_number_editText.addTextChangedListener(getWatcher());
        }
    }

    @Override
    public void setProductView(final RegisteredProduct registeredProduct) {
        date_EditText.setText(registeredProduct.getPurchaseDate());
        serial_number_editText.setText(registeredProduct.getSerialNumber());
        final String productCtn = registeredProduct.getCtn();
        if (!TextUtils.isEmpty(registeredProduct.getCtn())) {
            productCtnTextView.setVisibility(View.VISIBLE);
            productCtnTextView.setText(productCtn);
        } else {
            productCtnTextView.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(registeredProduct.getFriendlyName())) {
            productFriendlyNameTextView.setVisibility(View.GONE);
        } else {
            productFriendlyNameTextView.setVisibility(View.VISIBLE);
            productFriendlyNameTextView.setText(registeredProduct.getFriendlyName());
        }
        handleDateEditTextOnError();
        handleSerialNumberEditTextOnError();
    }

    @Override
    public void requireFields(final boolean requireDate, final boolean requireSerialNumber) {
        if (requireDate)
            purchaseDateLayout.setVisibility(View.VISIBLE);
        if (requireSerialNumber)
            serialLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoadingDialog() {
        loadingFlag = false;
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            Fragment prev = activity.getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null && prev instanceof DialogFragment) {
                ((DialogFragment) prev).dismissAllowingStateLoss();
            }
        }
    }

    @Override
    public void showLoadingDialog() {
        if (!loadingFlag) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
                ft.commitAllowingStateLoss();
            }
            DialogFragment newFragment = ProdRegLoadingFragment.newInstance(getString(R.string.PPR_Registering_Products_Lbltxt));
            newFragment.show(getActivity().getSupportFragmentManager(), "dialog");
            loadingFlag = true;
            getActivity().getSupportFragmentManager().executePendingTransactions();
        }
    }
}
