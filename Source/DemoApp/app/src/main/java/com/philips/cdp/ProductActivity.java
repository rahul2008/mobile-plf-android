package com.philips.cdp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.philips.cdp.backend.RegistrationRequestManager;
import com.philips.cdp.demo.R;
import com.philips.cdp.model.ProductMetaData;
import com.philips.cdp.model.ProductResponse;
import com.philips.cdp.model.RegisteredDataResponse;
import com.philips.cdp.productbuilder.ProductMetaDataBuilder;
import com.philips.cdp.productbuilder.RegisteredBuilder;
import com.philips.cdp.productbuilder.RegistrationBuilder;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText regChannel, serialNumber, purchaseDate, ctn;
    private String mCtn = "HD8969/09";
    private String mSectorCode = "B2C";
    private String mLocale = "ru_RU";
    private String mCatalogCode = "CONSUMER";
    private String TAG = getClass().toString();
    
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
        regChannel.setText("MS81376");
        serialNumber.setText("ab123456789012");
        purchaseDate.setText("2016-02-15");
        ctn.setText("HD8969/09");
    }

    private void registerProduct(final String accessToken) {
        PrxLogger.enablePrxLogger(true);

        RegistrationBuilder registrationBuilder = new RegistrationBuilder(mCtn, accessToken, serialNumber.getText().toString());
        registrationBuilder.setmSectorCode(mSectorCode);
        registrationBuilder.setmLocale(mLocale);
        registrationBuilder.setmCatalogCode(mCatalogCode);
        registrationBuilder.setRegistrationChannel(regChannel.getText().toString());
        registrationBuilder.setPurchaseDate(purchaseDate.getText().toString());

        RegistrationRequestManager mRequestManager = new RegistrationRequestManager(this);
        mRequestManager.executeRequest(registrationBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                Toast.makeText(ProductActivity.this, "Product Registered successfully", Toast.LENGTH_SHORT).show();
                ProductResponse productResponse = (ProductResponse) responseData;
                Log.d(TAG, "Positive Response Data : " + productResponse.isSuccess());
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
        PrxLogger.enablePrxLogger(true);

        RegisteredBuilder registeredDataBuilder = new RegisteredBuilder(accessToken);

        RegistrationRequestManager mRequestManager = new RegistrationRequestManager(this);
        mRequestManager.executeRequest(registeredDataBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                RegisteredDataResponse registeredDataResponse = (RegisteredDataResponse) responseData;
                Log.d(TAG, " Response Data : " + registeredDataResponse.isSuccess());
                Toast.makeText(ProductActivity.this, "productMetaData Response Data : " + registeredDataResponse.isSuccess(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponseError(String error, int code) {
                Log.d(TAG, "Negative Response Data : " + error + " with error code : " + code);
                Toast.makeText(ProductActivity.this, "error in response", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void productMetaData(final String accessToken) {
        PrxLogger.enablePrxLogger(true);

        ProductMetaDataBuilder productMetaDataBuilder = new ProductMetaDataBuilder(mCtn, accessToken);
        productMetaDataBuilder.setmSectorCode(mSectorCode);
        productMetaDataBuilder.setmLocale(mLocale);
        productMetaDataBuilder.setmCatalogCode(mCatalogCode);

        RegistrationRequestManager mRequestManager = new RegistrationRequestManager(this);
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
            RegistrationLaunchHelper.launchRegistrationActivityWithAccountSettings(this);
        }
    }


}

