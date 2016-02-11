package com.philips.cdp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.demo.R;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.registration.UserWithProduct;
import com.philips.cdp.registration.dao.ProductRegistrationInfo;
import com.philips.cdp.registration.handlers.ProductRegistrationHandler;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

public class MainActivity extends ProductRegistrationActivity implements View.OnClickListener {
    private Button mBtnUserRegistration;
    private Button mBtnProductRegistration;

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

   /* private void test(final String response) {
        ProductLog.producrlog(ProductLog.ONCLICK, "On Click : User Registration");
//                RegistrationLaunchHelper.launchRegistrationActivityWithAccountSettings(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                makeRequest(response);
            }
        }).start();
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_user_registration:
                ProductLog.producrlog(ProductLog.ONCLICK, "On Click : User Registration");
                RegistrationLaunchHelper.launchRegistrationActivityWithAccountSettings(this);
                break;
            case R.id.btn_product_registration:
                ProductLog.producrlog(ProductLog.ONCLICK, "On Click : Production Registration");
                launchProductActivity(this);
              /*  UserWithProduct userWithProduct = new UserWithProduct(this);
                userWithProduct.getRefreshedAccessToken(new ProductRegistrationHandler() {
                    @Override
                    public void onRegisterSuccess(final String response) {
                        ProductLog.producrlog(ProductLog.ONCLICK, "Access token---"+response);
                        test(response);
                    }

                    @Override
                    public void onRegisterFailedWithFailure(final int error) {

                    }
                });*/

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

            }
        }, "en_GB", this);

    }
}
