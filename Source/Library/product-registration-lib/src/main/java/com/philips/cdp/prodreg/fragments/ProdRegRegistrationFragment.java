package com.philips.cdp.prodreg.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.philips.cdp.prodreg.util.ProdRegUtil;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.uikit.customviews.InlineForms;

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
    private EditText serial_number_editText, date_EditText;
    private InlineForms serialLayout, purchaseDateLayout;

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
                    date_EditText.setText(text);
                    if (!ProdRegUtil.isValidDate(text)) {
                        showErrorMessageDate(date_EditText);
                    } else {
                        purchaseDateLayout.removeError(date_EditText);
                    }
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
        serial_number_editText = (EditText) view.findViewById(R.id.serial_edit_text);
        date_EditText = (EditText) view.findViewById(R.id.date_edit_text);
        serialLayout = (InlineForms) view.findViewById(R.id.InlineForms_serial_number);
        purchaseDateLayout = (InlineForms) view.findViewById(R.id.InlineForms_date);
        imageLoader = ImageRequestHandler.getInstance(getActivity()).getImageLoader();
        register = (Button) view.findViewById(R.id.btn_register);
        productImageView = (ImageView) view.findViewById(R.id.product_image);
        register.setOnClickListener(onClickRegister());
        date_EditText.setKeyListener(null);
        date_EditText.setOnClickListener(onClickPurchaseDate());
        serial_number_editText.addTextChangedListener(getWatcher());
        return view;
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
                if (isValidSerialNumber(productMetadataResponseData, serial_number_editText.getText().toString())) {
                    serialLayout.removeError(serial_number_editText);
                } else {
                    showErrorMessageSerialNumber(serial_number_editText);
                }
            }
        };
    }

    @NonNull
    private ProdRegListener getProdRegListener(final ProgressDialog progress) {
        return new ProdRegListener() {
            @Override
            public void onProdRegSuccess(RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                if (getActivity() != null && !getActivity().isFinishing() && progress != null) {
                    progress.dismiss();
                    showFragment(new ProdRegSuccessFragment());
                }
            }

            @Override
            public void onProdRegFailed(RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                Log.d(getClass() + "", "Negative Response Data : " + registeredProduct.getProdRegError().getDescription() + " with error code : " + registeredProduct.getProdRegError().getCode());
                if (getActivity() != null && !getActivity().isFinishing() && progress != null) {
                    progress.dismiss();
                    showAlert("Failed", registeredProduct.getProdRegError().toString());
                }
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
            date_EditText.setText(currentProduct.getPurchaseDate());
            serial_number_editText.setText(currentProduct.getSerialNumber());
            handleDateEditTextOnError();
            handleSerialNumberEditTextOnError();
            validateFields();
        }
    }

    private boolean validateFields() {
        final boolean isValidSerialNumber = isValidSerialNumber(productMetadataResponseData, serial_number_editText.getText().toString());
        final boolean isValidDate = isValidDate(productMetadataResponseData, date_EditText.getText().toString());
        if (!isValidDate && !isValidSerialNumber) {
            showErrorMessageDate(date_EditText);
            showErrorMessageSerialNumber(serial_number_editText);
        } else if (!isValidDate) {
            showErrorMessageDate(date_EditText);
        } else if (!isValidSerialNumber) {
            showErrorMessageSerialNumber(serial_number_editText);
        } else {
            return true;
        }
        return false;
    }

    private boolean isValidDate(final ProductMetadataResponseData productMetadataResponseData, final String purchaseDate) {
        boolean isValidDate = true;
        if (productMetadataResponseData != null && productMetadataResponseData.getRequiresDateOfPurchase().equalsIgnoreCase("true")) {
            isValidDate = ProdRegUtil.isValidDate(purchaseDate);
            purchaseDateLayout.setVisibility(View.VISIBLE);
        }
        return isValidDate;
    }

    private void handleSerialNumberEditTextOnError() {
        serialLayout.setValidator(new InlineForms.Validator() {
            @Override
            public void validate(final View editText, final boolean hasFocus) {
                EditText editTextView = (EditText) editText;
                showErrorMessageSerialNumber(editTextView);
            }
        });
    }

    private void showErrorMessageSerialNumber(final EditText editTextView) {
        serialLayout.setErrorMessage("Invalid_serial_number");
        serialLayout.showError(editTextView);
    }

    private void handleDateEditTextOnError() {
        purchaseDateLayout.setValidator(new InlineForms.Validator() {
            @Override
            public void validate(final View editText, final boolean hasFocus) {
                EditText editTextView = (EditText) editText;
                final String date = editTextView.getText().toString();
                if (!ProdRegUtil.isValidDate(date)) {
                    showErrorMessageDate(editTextView);
                } else {
                    purchaseDateLayout.removeError(date_EditText);
                }
            }
        });
    }

    private void showErrorMessageDate(final EditText editTextView) {
        purchaseDateLayout.setErrorMessage("Invalid_date");
        purchaseDateLayout.showError(editTextView);
    }

    protected boolean isValidSerialNumber(final ProductMetadataResponseData data, final String serialNumber) {
        if (data != null && data.getRequiresSerialNumber().equalsIgnoreCase("true")) {
            serialLayout.setVisibility(View.VISIBLE);
            if ((ProdRegUtil.isValidSerialNumber(data.getSerialNumberFormat(), serialNumber)))
                return false;
        }
        return true;
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
                datePickerDialog.show();
            }
        };
    }

    @NonNull
    private View.OnClickListener onClickRegister() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (validateFields()) {
                    ProgressDialog progress = ProgressDialog.show(getActivity(), "",
                            "Registering your product", true);
                    progress.setCancelable(false);
                    currentProduct.setPurchaseDate(date_EditText.getText().toString());
                    currentProduct.setSerialNumber(serial_number_editText.getText().toString());
                    ProdRegHelper prodRegHelper = new ProdRegHelper();
                    prodRegHelper.addProductRegistrationListener(getProdRegListener(progress));
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
