package com.philips.cdp.prodreg;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.prodreg.register.ProdRegHelper;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    private ToggleButton toggleButton;
    private EditText mRegChannel, mSerialNumber, mPurchaseDate, mCtn;
    private String TAG = getClass().toString();
    private Calendar mCalendar;
    private boolean eMailConfiguration = false;
    private ProdRegHelper prodRegHelper;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity_main);
        mRegChannel = (EditText) findViewById(R.id.edt_reg_channel);
        mSerialNumber = (EditText) findViewById(R.id.edt_serial_number);
        mPurchaseDate = (EditText) findViewById(R.id.edt_purchase_date);
        mCtn = (EditText) findViewById(R.id.edt_ctn);
        toggleButton = (ToggleButton) findViewById(R.id.toggbutton);
        toggleButton.setChecked(eMailConfiguration);
        prodRegHelper = new ProdRegHelper();
        Intent intent = getIntent();
        if (intent != null) {
            mCtn.setText(intent.getStringExtra("ctn") != null ? intent.getStringExtra("ctn") : "");
            mSerialNumber.setText(intent.getStringExtra("serial") != null ? intent.getStringExtra("serial") : "");
            mPurchaseDate.setText(intent.getStringExtra("date") != null ? intent.getStringExtra("date") : "");
        }
    }

    private void registerProduct() {
        Product product = new Product(mCtn.getText().toString(), Sector.B2C, Catalog.CONSUMER);
        product.setSerialNumber(mSerialNumber.getText().toString());
        product.setPurchaseDate(mPurchaseDate.getText().toString());
        product.sendEmail(eMailConfiguration);
        final ProdRegListener listener = new ProdRegListener() {
            @Override
            public void onProdRegSuccess(RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                Toast.makeText(ProductActivity.this, getResources().getString(R.string.product_registered_successfully), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProdRegFailed(RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                Log.d(TAG, "Negative Response Data : " + registeredProduct.getProdRegError().getDescription() + " with error code : " + registeredProduct.getProdRegError().getCode());
                Toast.makeText(ProductActivity.this, registeredProduct.getProdRegError().getDescription(), Toast.LENGTH_SHORT).show();
            }
        };
        prodRegHelper.addProductRegistrationListener(listener);
        prodRegHelper.getSignedInUserWithProducts().registerProduct(product);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.submitproduct:
                final User mUser = new User(this);
                if (!(mUser.isUserSignIn() && mUser.getEmailVerificationStatus())) {
                    Log.d(TAG, "On Click : User Registration");
                    RegistrationLaunchHelper.launchRegistrationActivityWithAccountSettings(this);
                    Util.navigateFromUserRegistration();
                }
                if (mCtn.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(ProductActivity.this, getResources().getString(R.string.enter_ctn_number), Toast.LENGTH_SHORT).show();
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
            default:
                break;
        }
    }

    public void onClickPurchaseDate(View view) {
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, mYear, mMonthInt, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
}

