package com.philips.cdp.prodreg;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.backend.ProdRegHelper;
import com.philips.cdp.prodreg.backend.Product;
import com.philips.cdp.prodreg.handler.ProdRegListener;
import com.philips.cdp.prodreg.model.RegisteredProduct;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    private ToggleButton toggleButton;
    private Button submitButton;
    private EditText mRegChannel, mSerialNumber, mPurchaseDate, mCtn;
    private String TAG = getClass().toString();
    private Calendar mCalendar;
    private int mMonthInt, mDateInt, mYear, mDay;
    private String mMonth, mDate;
    private String[] mEditDisplayDate;
    private String mGetDeviceDate;
    private Date mDisplayDate, mDeviceDate;
    private String MICRO_SITE_ID = "MS";
    private boolean eMailConfiguration = true;

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            Log.d(TAG, "Response Data : " + arg1 + "-" + arg2 + "-" + arg3);
            mMonthInt = (arg2 + 1);
            mDateInt = arg3;
            if (mMonthInt < 10) {
                mMonth = getResources().getString(R.string.zero) + mMonthInt;
            } else {
                mMonth = Integer.toString(mMonthInt);
            }
            if (mDateInt < 10) {
                mDate = getResources().getString(R.string.zero) + mDateInt;
            } else {
                mDate = Integer.toString(mDateInt);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_formate));
            mCalendar = Calendar.getInstance();
            mGetDeviceDate = dateFormat.format(mCalendar.getTime());
            mDisplayDate = new Date();
            mDeviceDate = new Date();
            try {
                mDisplayDate = dateFormat.parse(arg1 + "-" + mMonth + "-" + mDate);
                mDeviceDate = dateFormat.parse(mGetDeviceDate);
                if (mDisplayDate.after(mDeviceDate)) {
                    Log.d(TAG, " Response Data : " + "Error in Date");
                } else {
                    mPurchaseDate.setText(arg1 + "-" + mMonth + "-" + mDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity_main);
        mRegChannel = (EditText) findViewById(R.id.edt_reg_channel);
        mSerialNumber = (EditText) findViewById(R.id.edt_serial_number);
        mPurchaseDate = (EditText) findViewById(R.id.edt_purchase_date);
        mCtn = (EditText) findViewById(R.id.edt_ctn);
        toggleButton = (ToggleButton) findViewById(R.id.toggbutton);
        submitButton = (Button) findViewById(R.id.submitproduct);
        toggleButton.setChecked(eMailConfiguration);
    }

    private void registerProduct() {
        ProdRegHelper prodRegHelper = new ProdRegHelper();
        prodRegHelper.setLocale(Locale.getDefault().getLanguage(), Locale.getDefault().getCountry());
        Product product = new Product(mCtn.getText().toString(), mSerialNumber.getText().toString(), mPurchaseDate.getText().toString(),
                Sector.B2C, Catalog.CONSUMER);
        product.setShouldSendEmailAfterRegistration(String.valueOf(eMailConfiguration));
        final ProdRegListener listener = new ProdRegListener() {
            @Override
            public void onProdRegSuccess() {
                submitButton.setEnabled(true);
                Toast.makeText(ProductActivity.this, getResources().getString(R.string.product_registered_successfully), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProdRegFailed(RegisteredProduct registeredProduct) {
                submitButton.setEnabled(true);
                Log.d(TAG, "Negative Response Data : " + registeredProduct.getProdRegError().getDescription() + " with error code : " + registeredProduct.getProdRegError().getCode());
                Toast.makeText(ProductActivity.this, registeredProduct.getProdRegError().getDescription(), Toast.LENGTH_SHORT).show();
            }
        };
        prodRegHelper.registerProduct(this, product, listener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.submitproduct:
                final User mUser = new User(this);
                if (!(mUser.isUserSignIn(ProductActivity.this) && mUser.getEmailVerificationStatus(ProductActivity.this))) {
                    Toast.makeText(ProductActivity.this, getResources().getString(R.string.user_not_signed), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "On Click : User Registration");
                    RegistrationLaunchHelper.launchRegistrationActivityWithAccountSettings(this);
                    Util.navigateFromUserRegistration();
                }
                if (mCtn.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(ProductActivity.this, getResources().getString(R.string.enter_ctn_number), Toast.LENGTH_SHORT).show();
                } else {
                    submitButton.setEnabled(false);
                    mRegChannel.setText(MICRO_SITE_ID + RegistrationConfiguration.getInstance().getPilConfiguration().getMicrositeId());
                    registerProduct();
                }
                break;
            case R.id.toggbutton:
                eMailConfiguration = toggleButton.isChecked();
                break;
            default:
                break;
        }
    }

    public void onClickPurchaseDate(View view) {
        if (!mPurchaseDate.getText().toString().equalsIgnoreCase("")) {
            mEditDisplayDate = mPurchaseDate.getText().toString().split("-");
            mYear = Integer.parseInt(mEditDisplayDate[0]);
            mMonthInt = Integer.parseInt(mEditDisplayDate[1]) - 1;
            mDay = Integer.parseInt(mEditDisplayDate[2]);
        } else {
            mCalendar = Calendar.getInstance();
            mYear = mCalendar.get(Calendar.YEAR);
            mMonthInt = mCalendar.get(Calendar.MONTH);
            mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, mYear, mMonthInt, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
}

