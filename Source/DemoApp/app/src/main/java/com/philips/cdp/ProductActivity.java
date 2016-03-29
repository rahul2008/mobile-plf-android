package com.philips.cdp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.philips.cdp.handler.ProdRegConstants;
import com.philips.cdp.backend.ProdRegHelper;
import com.philips.cdp.backend.ProdRegRequestInfo;
import com.philips.cdp.demo.R;
import com.philips.cdp.handler.ErrorType;
import com.philips.cdp.handler.ProdRegListener;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.model.ProdRegResponse;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mRegChannel, mSerialNumber, mPurchaseDate, mCtn;
    private String TAG = getClass().toString();
    private Calendar mCalendar;
    private int mMonthInt, mDateInt, mYear, mDay;
    private String mMonth, mDate;
    private String[] mEditDisplayDate;
    private String mGetDeviceDate;
    private Date mDisplayDate, mDeviceDate;

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
    }

    private void registerProduct() {
        ProdRegRequestInfo prodRegRequestInfo = new ProdRegRequestInfo(mCtn.getText().toString(), mSerialNumber.getText().toString(), Sector.B2C, Catalog.CONSUMER);
        ProdRegHelper prodRegHelper = new ProdRegHelper();
        prodRegHelper.setLocale(Locale.getDefault().getLanguage(), Locale.getDefault().getCountry());
        prodRegRequestInfo.setPurchaseDate(mPurchaseDate.getText().toString());
        final ProdRegListener listener = new ProdRegListener() {
            @Override
            public void onProdRegSuccess(ResponseData responseData) {
                Toast.makeText(ProductActivity.this, getResources().getString(R.string.product_registered_successfully), Toast.LENGTH_SHORT).show();
                ProdRegResponse prodRegResponse = (ProdRegResponse) responseData;
                if (prodRegResponse.getData() != null)
                    Log.d(TAG, " Response Data : " + prodRegResponse.getData());
            }

            @Override
            public void onProdRegFailed(ErrorType errorType) {
                Log.d(TAG, "Negative Response Data : " + errorType.getDescription() + " with error code : " + errorType.getCode());
                Toast.makeText(ProductActivity.this, errorType.getDescription(), Toast.LENGTH_SHORT).show();
            }
        };
        prodRegHelper.registerProduct(this, prodRegRequestInfo, listener);
    }

    @Override
    public void onClick(View v) {
        final User mUser = new User(this);
        if (mUser.isUserSignIn(ProductActivity.this) && mUser.getEmailVerificationStatus(ProductActivity.this)) {
            if (mCtn.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(ProductActivity.this, getResources().getString(R.string.enter_ctn_number), Toast.LENGTH_SHORT).show();
            } else {
                mRegChannel.setText(ProdRegConstants.MICRO_SITE_ID + RegistrationConfiguration.getInstance().getPilConfiguration().getMicrositeId());
                registerProduct();
            }
        } else {
            Toast.makeText(ProductActivity.this, getResources().getString(R.string.user_not_signed), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "On Click : User Registration");
            RegistrationLaunchHelper.launchRegistrationActivityWithAccountSettings(this);
            Util.navigateFromUserRegistration();
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

