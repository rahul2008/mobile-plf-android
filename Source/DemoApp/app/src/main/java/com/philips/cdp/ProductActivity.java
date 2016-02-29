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
import com.philips.cdp.model.ProductMetaData;
import com.philips.cdp.model.ProductResponse;
import com.philips.cdp.productbuilder.ProductMetaDataBuilder;
import com.philips.cdp.productbuilder.RegisteredBuilder;
import com.philips.cdp.productbuilder.RegistrationBuilder;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.RequestManager;
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
    private String mCtn = "HD8969/09";
    private String mSectorCode = "B2C";
    private String mLocale = "en_GB";
    private String mCatalogCode = "CONSUMER";
    private String TAG = getClass().toString();

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            Log.d(TAG, "Response Data : " + arg1 + "-" + arg2 + "-" + arg3);
            purchaseDate.setText(arg1 + "-" + (arg2 + 1) + "-" + arg3);
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
        serialNumber.setText("ab123456789012");
        purchaseDate.setText("2016-02-15");
        ctn.setText("HD8969/09");
    }

    private void registerProduct(final String accessToken) {
        PrxLogger.enablePrxLogger(true);

        RegistrationBuilder registrationBuilder = new RegistrationBuilder(ctn.getText().toString(), accessToken, serialNumber.getText().toString());
        registrationBuilder.setSector(Sector.B2C);
        registrationBuilder.setCatalog(Catalog.CONSUMER);
        registrationBuilder.setmLocale(mLocale);
        registrationBuilder.setmCatalogCode(mCatalogCode);
        registrationBuilder.setRegistrationChannel(regChannel.getText().toString());
        registrationBuilder.setPurchaseDate(purchaseDate.getText().toString());

        ProdRegHelper prodRegHelper = new ProdRegHelper(this);
        prodRegHelper.registerProduct(this, registrationBuilder, new ResponseListener() {
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
        RegisteredBuilder registeredBuilder = new RegisteredBuilder(accessToken);
        registeredBuilder.setSector(Sector.B2C);
        registeredBuilder.setCatalog(Catalog.CONSUMER);
        registeredBuilder.setmLocale("en_GB");
        ProdRegHelper prodRegHelper = new ProdRegHelper(this);
        prodRegHelper.getRegisteredProduct(this, registeredBuilder, new ResponseListener() {
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

    private void productMetaData(final String accessToken) {
        PrxLogger.enablePrxLogger(true);

        ProductMetaDataBuilder productMetaDataBuilder = new ProductMetaDataBuilder(mCtn, accessToken);
        productMetaDataBuilder.setmSectorCode(mSectorCode);
        productMetaDataBuilder.setmLocale(mLocale);
        productMetaDataBuilder.setmCatalogCode(mCatalogCode);

        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(this);
        mRequestManager.executeRequest(productMetaDataBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                ProductMetaData productMetaData = (ProductMetaData) responseData;
                Log.d(TAG, "productMetaData Response Data : " + productMetaData.isSuccess());
                Toast.makeText(ProductActivity.this, "productMetaData Response Data : " + productMetaData.isSuccess(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponseError(String error, int code) {
                Log.d(TAG, "Negative Response Data : " + error + " with error code : " + code);
                Toast.makeText(ProductActivity.this, "error in response", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        final User mUser = new User(this);
        if (mUser.isUserSignIn(ProductActivity.this) && mUser.getEmailVerificationStatus(ProductActivity.this)) {
            Toast.makeText(ProductActivity.this, "user signed in", Toast.LENGTH_SHORT).show();
            registerProduct(mUser.getAccessToken());
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
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, myDateListener, year, month, day).show();
    }
}

