/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.philips.cdp.prodreg.constants.AnalyticsConstants;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.constants.ProdRegError;
import com.philips.cdp.prodreg.error.ErrorHandler;
import com.philips.cdp.prodreg.imagehandler.ImageRequestHandler;
import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.cdp.prodreg.listener.DialogOkButtonListener;
import com.philips.cdp.prodreg.localcache.ProdRegCache;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.model.summary.Data;
import com.philips.cdp.prodreg.register.ProdRegRegistrationController;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.tagging.ProdRegTagging;
import com.philips.cdp.prodreg.util.ProdRegUtil;
import com.philips.cdp.product_registration_lib.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ProdRegRegistrationFragment extends ProdRegBaseFragment implements ProdRegRegistrationController.RegisterControllerCallBacks {

    public static final String TAG = ProdRegRegistrationFragment.class.getName();
    private ImageLoader imageLoader;
    private LinearLayout dateParentLayout, dateErrorLayout, serialNumberErrorLayout, findSerialNumberLayout, serialNumberParentLayout, successLayout;
    private TextView productFriendlyNameTextView, productTitleTextView, productCtnTextView, dateErrorTextView, serialNumberErrorTextView,prSuccessConfigurableTextView,productRegSucess;
    private ImageView productImageView;
    private EditText serial_number_editText;
    private EditText date_EditText;
    private ProdRegRegistrationController prodRegRegistrationController;
    private boolean textWatcherCalled = false;
    private Button registerButton;
    private FragmentActivity mActivity;
    private TextView tickIcon;
    private Dialog dialog;
    private DatePickerDialog datePickerDialog;


    @SuppressWarnings("SimpleDateFormat")
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            final int mMonthInt = (arg2 + 1);
            final ProdRegUtil prodRegUtil = new ProdRegUtil();
            String mMonth = prodRegUtil.getValidatedString(mMonthInt);
            String mDate = prodRegUtil.getValidatedString(arg3);
            SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            System.out.println("UTctime " + PRUiHelper.getInstance().getAppInfraInstance().getTime().getUTCTime());
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
    public int getActionbarTitleResId() {
        return R.string.PPR_NavBar_Title;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.PPR_NavBar_Title);
    }

    @Override
    public boolean getBackButtonState() {
        return true;
    }

    @Override
    public List<RegisteredProduct> getRegisteredProducts() {
        return prodRegRegistrationController.getRegisteredProducts();
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        setRetainInstance(true);
        prodRegRegistrationController = new ProdRegRegistrationController(this, mActivity);
        dismissLoadingDialog();
    }

    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_single_product, container, false);
        productFriendlyNameTextView = (TextView) view.findViewById(R.id.friendly_name);
        dateParentLayout = (LinearLayout) view.findViewById(R.id.date_edit_text_layout);
        serialNumberParentLayout = (LinearLayout) view.findViewById(R.id.serial_edit_text_layout);
        serialNumberErrorLayout = (LinearLayout) view.findViewById(R.id.serial_number_error_layout);
        dateErrorLayout = (LinearLayout) view.findViewById(R.id.date_error_layout);
        findSerialNumberLayout = (LinearLayout) view.findViewById(R.id.find_serial_number_layout);
        successLayout = (LinearLayout) view.findViewById(R.id.successLayout);
        productTitleTextView = (TextView) view.findViewById(R.id.product_title);
        productCtnTextView = (TextView) view.findViewById(R.id.product_ctn);
        productRegSucess = (TextView) view.findViewById(R.id.product_registered) ;
        dateErrorTextView = (TextView) view.findViewById(R.id.dateErrorTextView);
        serialNumberErrorTextView = (TextView) view.findViewById(R.id.serialNumberErrorTextView);
        serial_number_editText = (EditText) view.findViewById(R.id.serial_edit_text);
        date_EditText = (EditText) view.findViewById(R.id.date_edit_text);
        imageLoader = ImageRequestHandler.getInstance(mActivity.getApplicationContext()).getImageLoader();
        prSuccessConfigurableTextView = (TextView) view.findViewById(R.id.pr_success_configurable_textView);
        registerButton = (Button) view.findViewById(R.id.btn_register);
        final Button continueButton = (Button) view.findViewById(R.id.continueButton);
        productImageView = (ImageView) view.findViewById(R.id.product_image);
        tickIcon = (TextView) view.findViewById(R.id.tick_icon);
        registerButton.setOnClickListener(onClickRegister());
        date_EditText.setKeyListener(null);
        date_EditText.setOnTouchListener(onClickPurchaseDate());
        continueButton.setOnClickListener(onClickContinue());
        ProdRegTagging.getInstance().trackPage("RegistrationScreen", "trackPage", "RegistrationScreen");
        findSerialNumberLayout.setOnClickListener(onClickFindSerialNumber());
        return view;
    }

    @NonNull
    private View.OnClickListener onClickContinue() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                clearFragmentStack();
                handleCallBack(false);
                unRegisterProdRegListener();
            }
        };
    }

    private View.OnClickListener onClickFindSerialNumber() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                prodRegRegistrationController.onClickFindSerialNumber();
            }
        };
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
        if (savedInstanceState == null || !savedInstanceState.getBoolean(ProdRegConstants.PRODUCT_REGISTERED)) {
            prodRegRegistrationController.init(bundle);
            prodRegRegistrationController.handleState();
        } else {
            showSuccessLayout();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(ProdRegConstants.PROGRESS_STATE, prodRegRegistrationController.isApiCallingProgress());
        outState.putBoolean(ProdRegConstants.PRODUCT_REGISTERED, prodRegRegistrationController.isProductRegistered());
        super.onSaveInstanceState(outState);
    }

    @NonNull
    private TextWatcher getWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                ValidateSerialNumber();
            }

            @Override
            public void afterTextChanged(final Editable s) {
                ValidateSerialNumber();
            }
        };
    }

    private void ValidateSerialNumber() {
        if (textWatcherCalled)
            prodRegRegistrationController.isValidSerialNumber(serial_number_editText.getText().toString());
        textWatcherCalled = true;
    }

    private void handleSerialNumberEditTextOnError() {

        serial_number_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                prodRegRegistrationController.isValidSerialNumber(serial_number_editText.getText().toString());
            }
        });
    }

    private void showErrorMessageSerialNumber() {

        findSerialNumberLayout.setVisibility(View.VISIBLE);
        if (serial_number_editText.length() != 0) {
            serialNumberErrorLayout.setVisibility(View.VISIBLE);
            serialNumberErrorTextView.setText(getString(R.string.PPR_Invalid_SerialNum_ErrMsg));
        } else {
            serialNumberErrorLayout.setVisibility(View.GONE);
        }
    }

    private void handleDateEditTextOnError() {

        date_EditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (!hasFocus)
                    prodRegRegistrationController.isValidDate(date_EditText.getText().toString());
            }
        });
    }

    private void showErrorMessageDate() {

        dateErrorLayout.setVisibility(View.VISIBLE);
        dateErrorTextView.setText(new ErrorHandler().getError(mActivity, ProdRegError.INVALID_DATE.getCode()).getDescription());
        final ProdRegCache prodRegCache = new ProdRegCache();
        new ProdRegUtil().storeProdRegTaggingMeasuresCount(prodRegCache, AnalyticsConstants.Product_REGISTRATION_DATE_COUNT, 1);
        ProdRegTagging.getInstance().trackAction("PurchaseDateRequiredEvent", "specialEvents", "purchaseDateRequired");
    }

    private View.OnTouchListener onClickPurchaseDate() {
        return new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
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
                    datePickerDialog = new DatePickerDialog(mActivity, myDateListener, mYear, mMonthInt, mDay);
                    final ProdRegUtil prodRegUtil = new ProdRegUtil();
                    datePickerDialog.getDatePicker().setMinDate(prodRegUtil.getMinDate());
                    datePickerDialog.getDatePicker().setMaxDate(prodRegUtil.getMaxDate());
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        };
    }

    @NonNull
    private View.OnClickListener onClickRegister() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                registerButton.setEnabled(false);
                prodRegRegistrationController.registerProduct(date_EditText.getText().toString(), serial_number_editText.getText().toString());
            }
        };
    }

    @Override
    public boolean handleBackEvent() {
        if (mActivity != null && !mActivity.isFinishing()) {
            final boolean fragmentStack = clearFragmentStack();
            handleCallBack(true);
            unRegisterProdRegListener();
            return fragmentStack;
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

        clearFragmentStack();
        unRegisterProdRegListener();
    }

    @Override
    public void showAlertOnError(int responseCode){
    super.showAlertOnError(responseCode);
    }

    @Override
    public void buttonEnable() {
        registerButton.setEnabled(true);
    }

    @Override
    public void showFragment(Fragment fragment) {
        super.showFragment(fragment);
    }

    @Override
    public void isValidDate(boolean validDate) {
        if (validDate) {
            dateErrorLayout.setVisibility(View.GONE);
        } else
            showErrorMessageDate();
    }

    @Override
    public void isValidSerialNumber(boolean validSerialNumber) {
        if (validSerialNumber) {
            findSerialNumberLayout.setVisibility(View.GONE);
            serialNumberErrorLayout.setVisibility(View.GONE);
        } else
            showErrorMessageSerialNumber();
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

            imageLoader.get(summaryData.getImageURL(), ImageLoader.getImageListener(productImageView, R.drawable.prodreg_placeholder, R.drawable.prodreg_placeholder));
            serial_number_editText.addTextChangedListener(getWatcher());
        }
    }

    @Override
    public void setProductView(final RegisteredProduct registeredProduct) {
        date_EditText.setText(registeredProduct.getPurchaseDate());
        serial_number_editText.setText(registeredProduct.getSerialNumber());
        if (!registeredProduct.getEmail()) {
            prSuccessConfigurableTextView.setVisibility(View.GONE);
        }
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
            dateParentLayout.setVisibility(View.VISIBLE);
        if (requireSerialNumber)
            serialNumberParentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void logEvents(final String tag, final String data) {
        ProdRegLogger.v(tag, data);
    }

    @Override
    public void tagEvents(final String event, final String key, final String value) {
        ProdRegTagging.getInstance().trackAction(event, key, value);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void showSuccessLayout() {
        serialNumberParentLayout.setVisibility(View.GONE);
        dateParentLayout.setVisibility(View.GONE);
        registerButton.setVisibility(View.GONE);
        productCtnTextView.setVisibility(View.GONE);
        successLayout.setVisibility(View.VISIBLE);
        tickIcon.setVisibility(View.VISIBLE);
        productRegSucess.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAlreadyRegisteredDialog(RegisteredProduct registeredProduct) {
        dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.prodreg_already_registered_dialog);
        dialog.show();
        dialog.setCancelable(false);
        TextView serialNumberTitle = (TextView) dialog.findViewById(R.id.serial_number_title_message);
        TextView serialNumberRegisteredOn = (TextView) dialog.findViewById(R.id.serial_number_registered_message);
        TextView serialNumberWarranty = (TextView) dialog.findViewById(R.id.serial_number_warranty_message);
        serialNumberTitle.setText(getString(R.string.PPR_registered_serial).concat(" ").concat(registeredProduct.getSerialNumber()).concat(" ").concat(getString(R.string.PPR_before)));
        Button changeSerialNumber = (Button) dialog.findViewById(R.id.button_continue);
        if (!TextUtils.isEmpty(registeredProduct.getPurchaseDate())) {
            serialNumberRegisteredOn.setVisibility(View.VISIBLE);
            serialNumberRegisteredOn.setText(getString(R.string.PPR_registered_on).concat(" ").concat(registeredProduct.getPurchaseDate()));
        }
        if (!TextUtils.isEmpty(registeredProduct.getEndWarrantyDate())) {
            serialNumberWarranty.setVisibility(View.VISIBLE);
            serialNumberWarranty.setText(getString(R.string.PPR_warranty_until).concat(" ").concat(registeredProduct.getEndWarrantyDate()));
        }
        changeSerialNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog.dismiss();
                clearFragmentStack();
                handleCallBack(true);
                unRegisterProdRegListener();
            }
        });
    }

    @Override
    public void dismissLoadingDialog() {
         ProdRegLoadingAlertDialog.dismissProdRegLoadingDialog();
    }

    @Override
    public void showLoadingDialog() {
        ProdRegLoadingAlertDialog.showProdRegLoadingDialog(getString(R.string.PPR_Registering_Products_Lbltxt),getActivity());
    }

    @Override
    public void onStop() {
        dismissDialogs();
        super.onStop();

    }
    
    private void dismissDialogs() {
        dismissAlertOnError();
        dismissLoadingDialog();

        if (dialog != null) {
            dialog.dismiss();
        }
        if (datePickerDialog != null) {
            datePickerDialog.dismiss();
        }
    }
}
