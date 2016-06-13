package com.philips.cdp.prodreg.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponseData;
import com.philips.cdp.prodreg.model.summary.Data;
import com.philips.cdp.prodreg.register.ProdRegHelper;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;
import com.philips.cdp.prodreg.util.ImageRequestHandler;
import com.philips.cdp.prodreg.util.ProdRegConstants;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.uikit.customviews.PuiEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegRegistrationFragment extends ProdRegBaseFragment {

    public static final String TAG = ProdRegRegistrationFragment.class.getName();
    private ImageLoader imageLoader;
    private TextView productTitleTextView;
    private Button register;
    private ImageView productImageView;
    private ProductMetadataResponseData productMetadataResponseData;
    private Product currentProduct;
    private PuiEditText serial_number_editText, date_EditText;
    private View.OnFocusChangeListener mFocusChangeListenerDate;
    private View.OnFocusChangeListener mFocusChangeListenerSerial;
    private RelativeLayout serialLayout, purchaseDateLayout;
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            final int mMonthInt = (arg2 + 1);
            String mMonth = getValidatedString(mMonthInt);
            String mDate = getValidatedString(arg3);
            SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_formate));
            final Calendar mCalendar = Calendar.getInstance();
            final String mGetDeviceDate = dateFormat.format(mCalendar.getTime());
            try {
                final String text = arg1 + "-" + mMonth + "-" + mDate;
                final Date mDisplayDate = dateFormat.parse(text);
                final Date mDeviceDate = dateFormat.parse(mGetDeviceDate);
                if (mDisplayDate.after(mDeviceDate)) {
                    Log.d(TAG, " Response Data : " + "Error in Date");
                } else {
                    date_EditText.getEditText().setText(text);
                    date_EditText.setValidator(new PuiEditText.Validator() {
                        @Override
                        public boolean validate(final String inputToBeValidated) {
                            return isValidDate(text);
                        }
                    });
                    mFocusChangeListenerDate.onFocusChange(date_EditText, false);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public String getActionbarTitle() {
        return getActivity().getString(R.string.prodreg_actionbar_title);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_single_product, container, false);
        productTitleTextView = (TextView) view.findViewById(R.id.product_title);
        serial_number_editText = (PuiEditText) view.findViewById(R.id.serial_edit_text);
        date_EditText = (PuiEditText) view.findViewById(R.id.date_edit_text);
        serialLayout = (RelativeLayout) view.findViewById(R.id.serial_edit_text_layout);
        purchaseDateLayout = (RelativeLayout) view.findViewById(R.id.date_edit_text_layout);
        imageLoader = ImageRequestHandler.getInstance(getActivity()).getImageLoader();
        register = (Button) view.findViewById(R.id.btn_register);
        productImageView = (ImageView) view.findViewById(R.id.product_image);
        register.setOnClickListener(onClickRegister());
        date_EditText.getEditText().setKeyListener(null);
//        date_EditText.setWidth((int) getActivity().getResources().getDimension(R.dimen.prodreg_edittext_width));
//        serial_number_editText.setWidth((int) getActivity().getResources().getDimension(R.dimen.prodreg_edittext_width));
        date_EditText.getEditText().setOnClickListener(onClickPurchaseDate());
        mFocusChangeListenerDate = date_EditText.getEditText().getOnFocusChangeListener();
        mFocusChangeListenerSerial = serial_number_editText.getEditText().getOnFocusChangeListener();
        return view;
    }

    @NonNull
    private ProdRegListener getProdRegListener(final ProgressDialog progress) {
        return new ProdRegListener() {
            @Override
            public void onProdRegSuccess(RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                progress.dismiss();
                showFragment(new ProdRegSuccessFragment());
            }

            @Override
            public void onProdRegFailed(RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                Log.d(getClass() + "", "Negative Response Data : " + registeredProduct.getProdRegError().getDescription() + " with error code : " + registeredProduct.getProdRegError().getCode());
                progress.dismiss();
                showAlert("Failed", registeredProduct.getProdRegError().toString());
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        Data summaryData = null;
        if (bundle != null) {
            currentProduct = (Product) bundle.getSerializable(ProdRegConstants.PROD_REG_PRODUCT);
            productMetadataResponseData = (ProductMetadataResponseData) bundle.getSerializable(ProdRegConstants.PROD_REG_PRODUCT_METADATA);
            summaryData = (Data) bundle.getSerializable(ProdRegConstants.PROD_REG_PRODUCT_SUMMARY);
        }
        updateUi(summaryData);
    }

    private void updateUi(final Data summaryData) {
        if (summaryData != null) {
            final String productTitle = summaryData.getProductTitle();
            productTitleTextView.setText(productTitle != null ? productTitle : "");
            imageLoader.get(summaryData.getImageURL(), ImageLoader.getImageListener(productImageView, R.drawable.ic_launcher, R.drawable.ic_launcher));

        }
        if (currentProduct != null) {
            date_EditText.getEditText().setText(currentProduct.getPurchaseDate());
            serial_number_editText.getEditText().setText(currentProduct.getSerialNumber());
            validateFields();
        }
    }

    private void validateFields() {
        final boolean isValidSerialNumber = isValidSerialNumber(productMetadataResponseData, currentProduct.getSerialNumber());
        boolean isValidDate = true;
        if (productMetadataResponseData != null && productMetadataResponseData.getRequiresDateOfPurchase().equalsIgnoreCase("true")) {
            purchaseDateLayout.setVisibility(View.VISIBLE);
            isValidDate = isValidDate(currentProduct.getPurchaseDate());
        }
        if (!isValidDate && !isValidSerialNumber) {
            handleDateEditTextOnError();
            handleSerialNumberEditTextOnError();
        } else if (!isValidDate) {
            handleDateEditTextOnError();
        } else if (!isValidSerialNumber) {
            handleSerialNumberEditTextOnError();
        } else {
            register.setEnabled(true);
        }
    }

    private void handleSerialNumberEditTextOnError() {
        serial_number_editText.setValidator(new PuiEditText.Validator() {
            @Override
            public boolean validate(final String inputToBeValidated) {
                return (!processSerialNumber(productMetadataResponseData, inputToBeValidated));
            }
        });
        serialLayout.setVisibility(View.VISIBLE);
        mFocusChangeListenerSerial.onFocusChange(serial_number_editText, false);
    }

    private void handleDateEditTextOnError() {
        date_EditText.setValidator(new PuiEditText.Validator() {
            @Override
            public boolean validate(final String inputToBeValidated) {
                return isValidDate(inputToBeValidated);
            }
        });
        mFocusChangeListenerDate.onFocusChange(date_EditText, false);
    }

    protected boolean isValidSerialNumber(final ProductMetadataResponseData data, final String serialNumber) {
        if (data != null && data.getRequiresSerialNumber().equalsIgnoreCase("true")) {
            serialLayout.setVisibility(View.VISIBLE);
            if ((processSerialNumber(data, serialNumber)))
                return false;
        }
        return true;
    }

    private boolean processSerialNumber(final ProductMetadataResponseData data, final String serialNumber) {
        return serialNumber == null || serialNumber.length() < 1 || !serialNumber.matches(data.getSerialNumberFormat());
    }

    protected boolean isValidDate(final String date) {
        if (date != null) {
            String[] dates = date.split("-");
            return dates.length > 1 && Integer.parseInt(dates[0]) > 1999 && !isFutureDate(date);
        } else return false;
    }

    protected boolean isFutureDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        final String mGetDeviceDate = dateFormat.format(calendar.getTime());
        try {
            final Date mDisplayDate = dateFormat.parse(date);
            final Date mDeviceDate = dateFormat.parse(mGetDeviceDate);
            return mDisplayDate.after(mDeviceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getValidatedString(final int value) {
        final String valueString;
        if (value < 10) {
            valueString = getResources().getString(R.string.zero) + value;
        } else {
            valueString = Integer.toString(value);
        }
        return valueString;
    }

    private View.OnClickListener onClickPurchaseDate() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                int mYear;
                int mMonthInt;
                int mDay;
                if (!date_EditText.getEditText().getText().toString().equalsIgnoreCase("")) {
                    final String[] mEditDisplayDate = date_EditText.getEditText().getText().toString().split("-");
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
                datePickerDialog.show();
            }
        };
    }

    @NonNull
    private View.OnClickListener onClickRegister() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                currentProduct.setSerialNumber(serial_number_editText.getEditText().getText().toString());
                currentProduct.setPurchaseDate(date_EditText.getEditText().getText().toString());

                final boolean isValidSerialNumber = isValidSerialNumber(productMetadataResponseData, currentProduct.getSerialNumber());
                boolean isValidDate = true;
                if (productMetadataResponseData != null && productMetadataResponseData.getRequiresDateOfPurchase().equalsIgnoreCase("true")) {
                    isValidDate = isValidDate(currentProduct.getPurchaseDate());
                }

                if (!isValidDate && !isValidSerialNumber) {
                    handleDateEditTextOnError();
                    handleSerialNumberEditTextOnError();
                } else if (!isValidDate) {
                    handleDateEditTextOnError();
                } else if (!isValidSerialNumber) {
                    handleSerialNumberEditTextOnError();
                } else {
                    ProgressDialog progress;
                    progress = ProgressDialog.show(getActivity(), "",
                            "Registering your product", true);
                    progress.setCancelable(false);
                    currentProduct.setPurchaseDate(date_EditText.getEditText().getText().toString());
                    currentProduct.setSerialNumber(serial_number_editText.getEditText().getText().toString());
                    ProdRegHelper prodRegHelper = ProdRegHelper.getInstance();
                    final ProdRegListener listener = getProdRegListener(progress);
                    prodRegHelper.addProductRegistrationListener(listener);
                    prodRegHelper.getSignedInUserWithProducts().registerProduct(getMappedRegisteredProduct());
                }
            }
        };
    }

    private RegisteredProduct getMappedRegisteredProduct() {
        RegisteredProduct registeredProduct = new RegisteredProduct(currentProduct.getCtn(), currentProduct.getSector(), currentProduct.getCatalog());
        registeredProduct.setSerialNumber(currentProduct.getSerialNumber());
        registeredProduct.setPurchaseDate(currentProduct.getPurchaseDate());
        registeredProduct.sendEmail(currentProduct.getEmail());
        return registeredProduct;
    }

    @Override
    public boolean onBackPressed() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            clearFragmentStack();
            return false;
        }
        return true;
    }
}
