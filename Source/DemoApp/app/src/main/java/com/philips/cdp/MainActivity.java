package com.philips.cdp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.philips.cdp.backend.RegistrationRequestManager;
import com.philips.cdp.demo.R;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.model.ProductResponse;
import com.philips.cdp.productbuilder.RegistrationBuilder;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.UserWithProduct;
import com.philips.cdp.registration.dao.ProductRegistrationInfo;
import com.philips.cdp.registration.handlers.ProductRegistrationHandler;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

public class MainActivity extends ProductRegistrationActivity implements View.OnClickListener {
    private Button mBtnUserRegistration;
    private Button mBtnProductRegistration;

    private String mCtn = "HD8967/01";
    private String mSectorCode = "B2C";
    private String mLocale = "en_GB";
    private String mCatalogCode = "CARE";
    private String mRequestTag = null;
    private String TAG = getClass().toString();

    private static void launchProductActivity(Context context) {
        Intent registrationIntent = new Intent(context, ProductActivity.class);
        context.startActivity(registrationIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnUserRegistration = (Button) findViewById(R.id.btn_user_registration);
        mBtnUserRegistration.setOnClickListener(this);

        mBtnProductRegistration = (Button) findViewById(R.id.btn_product_registration);
        mBtnProductRegistration.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void registerProduct(final String accessToken) {
        PrxLogger.enablePrxLogger(true);

        RegistrationBuilder registrationBuilder = new RegistrationBuilder(mCtn, accessToken, "AB1234567891012");
        registrationBuilder.setmSectorCode(mSectorCode);
        registrationBuilder.setmLocale(mLocale);
        registrationBuilder.setmCatalogCode(mCatalogCode);
        registrationBuilder.setRegistrationChannel("MS81376");
        registrationBuilder.setPurchaseDate("2016-12-02");

        RegistrationRequestManager mRequestManager = new RegistrationRequestManager(this);
        mRequestManager.executeRequest(registrationBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                ProductResponse productResponse = (ProductResponse) responseData;
                Log.d(TAG, "Positive Response Data : " + productResponse.isSuccess());
            }

            @Override
            public void onResponseError(String error, int code) {
                Log.d(TAG, "Negative Response Data : " + error + " with error code : " + code);
                Toast.makeText(MainActivity.this, "error in response", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_user_registration:
                ProductLog.producrlog(ProductLog.ONCLICK, "On Click : User Registration");
                RegistrationLaunchHelper.launchRegistrationActivityWithAccountSettings(this);
                break;
            case R.id.btn_product_registration:

                UserWithProduct userWithProduct = new UserWithProduct(this);
                userWithProduct.getRefreshedAccessToken(new ProductRegistrationHandler() {
                    @Override
                    public void onRegisterSuccess(final String response) {

                        User mUser = new User(MainActivity.this);
                        if (mUser.isUserSignIn(MainActivity.this) && mUser.getEmailVerificationStatus(MainActivity.this)) {
                            Toast.makeText(MainActivity.this, "user signed in", Toast.LENGTH_SHORT).show();
                            registerProduct(response);
                        } else {
                            Toast.makeText(MainActivity.this, "user not signed in", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onRegisterFailedWithFailure(final int error) {
                        Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            default:
                break;
        }
    }

    private void makeRequest(final String response) {
        /*HttpClient httpClient = new HttpClient();
        Pair p1 = new Pair("productSerialNumber","rt234556778777");
        Pair p2 = new Pair("purchaseDate","2015-12-02");
        Pair p3 = new Pair("registrationChannel","MS81376");
        List<Pair<String,String>> al = new ArrayList<Pair<String,String>>();
        al.add(p1);
        al.add(p2);
        al.add(p3);
        httpClient.callPost("https://www.philips.co.uk/prx/registration/B2C/en_GB/CARE/products/HD8978/01.register.type.product", al, response);*/

        ProductRegistrationInfo productRegistrationInfo = new ProductRegistrationInfo();
        productRegistrationInfo.setCatalog(Catalog.CONSUMER);
        productRegistrationInfo.setSector(Sector.B2C);
        productRegistrationInfo.setProductSerialNumber("rt234556778777");
//        productRegistrationInfo.setProductModelNumber(mModelNo);
        productRegistrationInfo.setPurchaseDate("2015-12-02");
        productRegistrationInfo.setRegistrationChannel("MS81376");
        UserWithProduct userWithProduct = new UserWithProduct(this);
        userWithProduct.register(productRegistrationInfo, new ProductRegistrationHandler() {
            @Override
            public void onRegisterSuccess(final String response) {
                ProductLog.producrlog(ProductLog.ONCLICK, "Access token response ---" + response);
            }

            @Override
            public void onRegisterFailedWithFailure(final int error) {
                Toast.makeText(MainActivity.this, "Failed to get access token", Toast.LENGTH_SHORT).show();
            }
        }, "en_GB", this);

    }
}
