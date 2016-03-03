package com.philips.cdp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.philips.cdp.backend.ProdRegHelper;
import com.philips.cdp.core.ProdRegConstants;
import com.philips.cdp.demo.R;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.model.ProductResponse;
import com.philips.cdp.productrequest.RegisteredRequest;
import com.philips.cdp.productrequest.RegistrationRequest;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

import java.util.Calendar;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText regChannel, serialNumber, purchaseDate, ctn;
    private String mLocale = "en_GB";
    private String mCatalogCode = "CONSUMER";
    private String TAG = getClass().toString();
    private Calendar calendar;
    private int monthInt, mdateInt, year, month, day;
    private String mMonth, mDate;
    private String[] mDisplayDate;

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            Log.d(TAG, "Response Data : " + arg1 + "-" + arg2 + "-" + arg3);
            monthInt = (arg2 + 1);
            mdateInt = arg3;
            if (monthInt < 10) {
                mMonth = "0" + monthInt;
            } else {
                mMonth = Integer.toString(monthInt);
            }
            if (mdateInt < 10) {
                mDate = "0" + mdateInt;
            } else {
                mDate = Integer.toString(mdateInt);
            }
            purchaseDate.setText(arg1 + "-" + mMonth + "-" + mDate);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity_main);
        regChannel = (EditText) findViewById(R.id.edt_reg_channel);
        serialNumber = (EditText) findViewById(R.id.edt_serial_number);
        purchaseDate = (EditText) findViewById(R.id.edt_purchase_date);
        ctn = (EditText) findViewById(R.id.edt_ctn);
        initEditText();
    }

    private void initEditText() {
        Log.d(TAG, "MICRO SITE_ID : " + RegistrationConfiguration.getInstance().getPilConfiguration().getMicrositeId());
        regChannel.setText(ProdRegConstants.MICRO_SITE_ID + RegistrationConfiguration.getInstance().getPilConfiguration().getMicrositeId());
        serialNumber.setText("");
        purchaseDate.setText("");
        ctn.setText("");
    }

    private void registerProduct(final String accessToken) {
        PrxLogger.enablePrxLogger(true);

        RegistrationRequest registrationRequest = new RegistrationRequest(ctn.getText().toString(), accessToken, serialNumber.getText().toString());
        registrationRequest.setSector(Sector.B2C);
        registrationRequest.setCatalog(Catalog.CONSUMER);
        registrationRequest.setmLocale(mLocale);
        registrationRequest.setmCatalogCode(mCatalogCode);
        registrationRequest.setRegistrationChannel(regChannel.getText().toString());
        registrationRequest.setPurchaseDate(purchaseDate.getText().toString());

        ProdRegHelper prodRegHelper = new ProdRegHelper(this);
        prodRegHelper.registerProduct(this, registrationRequest, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                Toast.makeText(ProductActivity.this, "Product registered successfully", Toast.LENGTH_SHORT).show();
                ProductResponse productResponse = (ProductResponse) responseData;
                if (productResponse.getData() != null)
                    Log.d(TAG, " Response Data : " + productResponse.getData());
            }

            @Override
            public void onResponseError(String error, int code) {
                Log.d(TAG, "Negative Response Data : " + error + " with error code : " + code);
                Toast.makeText(ProductActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registeredProduct(final String accessToken) {
        RegisteredRequest registeredRequest = new RegisteredRequest(accessToken);
        registeredRequest.setSector(Sector.B2C);
        registeredRequest.setCatalog(Catalog.CONSUMER);
        registeredRequest.setmLocale("en_GB");
        ProdRegHelper prodRegHelper = new ProdRegHelper(this);
        prodRegHelper.getRegisteredProduct(this, registeredRequest, new ResponseListener() {
            @Override
            public void onResponseSuccess(final ResponseData responseData) {
                Log.d(TAG, responseData.toString());
                Toast.makeText(ProductActivity.this, responseData.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseError(final String errorMessage, final int responseCode) {
                Log.d(TAG, errorMessage);
            }
        });
    }

    @Override
    public void onClick(View v) {
        final User mUser = new User(this);
        if (mUser.isUserSignIn(ProductActivity.this) && mUser.getEmailVerificationStatus(ProductActivity.this)) {
            if (ctn.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(ProductActivity.this, getResources().getString(R.string.enter_ctn_number), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProductActivity.this, "user signed in", Toast.LENGTH_SHORT).show();
                registeredProduct(mUser.getAccessToken());
            }
        } else {
            Toast.makeText(ProductActivity.this, "user not signed in", Toast.LENGTH_SHORT).show();
            ProductLog.producrlog(ProductLog.ONCLICK, "On Click : User Registration");
            RegistrationHelper.getInstance().getUserRegistrationListener().registerEventNotification(new UserRegistrationListener() {
                @Override
                public void onUserRegistrationComplete(final Activity activity) {
                    activity.finish();
                }

                @Override
                public void onPrivacyPolicyClick(final Activity activity) {

                }

                @Override
                public void onTermsAndConditionClick(final Activity activity) {

                }

                @Override
                public void onUserLogoutSuccess() {

                }

                @Override
                public void onUserLogoutFailure() {

                }

                @Override
                public void onUserLogoutSuccessWithInvalidAccessToken() {

                }
            });
            RegistrationLaunchHelper.launchRegistrationActivityWithAccountSettings(this);
        }
    }

    public void onClickPurchaseDate(View view) {
        if (!purchaseDate.getText().toString().equalsIgnoreCase("")) {
            mDisplayDate = purchaseDate.getText().toString().split("-");
            year = Integer.parseInt(mDisplayDate[0]);
            month = Integer.parseInt(mDisplayDate[1]) - 1;
            day = Integer.parseInt(mDisplayDate[2]);
        } else {
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
}

