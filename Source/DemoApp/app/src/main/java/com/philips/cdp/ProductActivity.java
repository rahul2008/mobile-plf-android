package com.philips.cdp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.philips.cdp.backend.ProdRegHelper;
import com.philips.cdp.backend.ProdRegRequestInfo;
import com.philips.cdp.com.philips.cdp.Util;
import com.philips.cdp.core.ProdRegConstants;
import com.philips.cdp.demo.R;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.model.ProductResponse;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <b> Steps to register a product </b>
 * <br></br>
 * <pre>
 *       RegistrationRequest registrationRequest = new RegistrationRequest("HC5410/83", "ww6bsdquca864kbt", "1344");
 *       registrationRequest.setSector(Sector.B2C);
 *       registrationRequest.setCatalog(Catalog.CONSUMER);
 *       registrationRequest.setmLocale("en_GB");
 *       registrationRequest.setmCatalogCode(mCatalogCode);
 *
 *       ProdRegHelper prodRegHelper = new ProdRegHelper(this);
 *       prodRegHelper.setLocale("en", "GB");
 *       prodRegHelper.registerProduct(this, registrationRequest, new ResponseListener() {
 *           @Override
 *            public void onResponseSuccess(ResponseData responseData) {
 *            Toast.makeText(ProductActivity.this, "Product registered successfully", Toast.LENGTH_SHORT).show();
 *          }
 *
 *           @Override
 *             public void onResponseError(String error, int code) {
 *               Toast.makeText(ProductActivity.this, error, Toast.LENGTH_SHORT).show();
 *           }
 *         });
 *
 *  </pre>
 */
public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText regChannel, serialNumber, purchaseDate, ctn;
    private String TAG = getClass().toString();
    private Calendar calendar;
    private int monthInt, mdateInt, year, month, day;
    private String mMonth, mDate;
    private String[] mEditDisplayDate;
    private String mGetDeviceDate;
    private Date mDisplayDate, mDeviceDate;

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
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            calendar = Calendar.getInstance();
            mGetDeviceDate = dateFormat.format(calendar.getTime());
            mDisplayDate = new Date();
            mDeviceDate = new Date();
            try {
                mDisplayDate = dateFormat.parse(arg1 + "-" + mMonth + "-" + mDate);
                mDeviceDate = dateFormat.parse(mGetDeviceDate);
                if (mDisplayDate.after(mDeviceDate)) {
                    Log.d(TAG, " Response Data : " + "Error in Date");
                } else {
                    purchaseDate.setText(arg1 + "-" + mMonth + "-" + mDate);
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
        regChannel = (EditText) findViewById(R.id.edt_reg_channel);
        serialNumber = (EditText) findViewById(R.id.edt_serial_number);
        purchaseDate = (EditText) findViewById(R.id.edt_purchase_date);
        ctn = (EditText) findViewById(R.id.edt_ctn);
        initEditText();
    }

    private void initEditText() {
        regChannel.setText("");
        serialNumber.setText("");
        purchaseDate.setText("");
        ctn.setText("");
    }

    private void registerProduct() {
        PrxLogger.enablePrxLogger(true);

        ProdRegRequestInfo prodRegRequestInfo = new ProdRegRequestInfo(ctn.getText().toString(), serialNumber.getText().toString(), Sector.B2C, Catalog.CONSUMER);
        ProdRegHelper prodRegHelper = new ProdRegHelper();
        prodRegHelper.setLocale("en", "GB");
        prodRegRequestInfo.setPurchaseDate(purchaseDate.getText().toString());
        final ResponseListener listener = new ResponseListener() {
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
        };
        prodRegHelper.registerProduct(this, prodRegRequestInfo, listener);
    }

    private void registeredProduct() {
        ProdRegHelper prodRegHelper = new ProdRegHelper();
        final ResponseListener listener = new ResponseListener() {
            @Override
            public void onResponseSuccess(final ResponseData responseData) {
                Log.d(TAG, responseData.toString());
                Toast.makeText(ProductActivity.this, responseData.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseError(final String errorMessage, final int responseCode) {
                Log.d(TAG, errorMessage);
            }
        };
        ProdRegRequestInfo prodRegRequestInfo = new ProdRegRequestInfo(null, null, Sector.B2C, Catalog.CONSUMER);
        prodRegHelper.setLocale("en", "GB");
        prodRegHelper.getRegisteredProduct(this, prodRegRequestInfo, listener);
    }

    @Override
    public void onClick(View v) {
        final User mUser = new User(this);
        if (mUser.isUserSignIn(ProductActivity.this) && mUser.getEmailVerificationStatus(ProductActivity.this)) {
            if (ctn.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(ProductActivity.this, getResources().getString(R.string.enter_ctn_number), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProductActivity.this, "user signed in", Toast.LENGTH_SHORT).show();
                regChannel.setText(ProdRegConstants.MICRO_SITE_ID + RegistrationConfiguration.getInstance().getPilConfiguration().getMicrositeId());
                registerProduct();
            }
        } else {
            Toast.makeText(ProductActivity.this, "user not signed in", Toast.LENGTH_SHORT).show();
            ProductLog.producrlog(ProductLog.ONCLICK, "On Click : User Registration");
            RegistrationLaunchHelper.launchRegistrationActivityWithAccountSettings(this);
            Util.navigateFromUserRegistration();
        }
    }

    public void onClickPurchaseDate(View view) {
        if (!purchaseDate.getText().toString().equalsIgnoreCase("")) {
            mEditDisplayDate = purchaseDate.getText().toString().split("-");
            year = Integer.parseInt(mEditDisplayDate[0]);
            month = Integer.parseInt(mEditDisplayDate[1]) - 1;
            day = Integer.parseInt(mEditDisplayDate[2]);
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

