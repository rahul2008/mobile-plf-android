package com.philips.cdp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.demo.R;
import com.philips.cdp.registration.HttpClient;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

import java.util.ArrayList;
import java.util.List;

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_user_registration:
                ProductLog.producrlog(ProductLog.ONCLICK, "On Click : User Registration");
                RegistrationLaunchHelper.launchRegistrationActivityWithAccountSettings(this);
                break;
            case R.id.btn_product_registration:
                Intent intent = new Intent(this, ProductActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void registerProductExisting(final String response) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new HttpClient();
                Pair p1 = new Pair("productSerialNumber", "ab123456789012");
                Pair p2 = new Pair("purchaseDate", "2016-02-15");
                Pair p3 = new Pair("registrationChannel", "MS81376");
                List<Pair<String, String>> al = new ArrayList<Pair<String, String>>();
                al.add(p1);
                al.add(p2);
                al.add(p3);
//                httpClient.callPost("https://acc.philips.co.uk/prx/registration/B2C/de_DE/CONSUMER/products/HD8978/01.register.type.product?",al,response);
                String response2 = httpClient.callPost("https://acc.philips.co.uk/prx/registration/B2C/ru_RU/CONSUMER/products/HD8969/09.register.type.product?", al, response);
            }
        }).start();
    }
}
