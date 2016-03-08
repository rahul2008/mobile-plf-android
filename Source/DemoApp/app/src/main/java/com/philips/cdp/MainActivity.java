package com.philips.cdp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.com.philips.cdp.Util;
import com.philips.cdp.demo.R;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnUserRegistration;
    private Button mBtnProductRegistration;

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
                Util.navigateFromUserRegistration();
                break;
            case R.id.btn_product_registration:
                Intent intent = new Intent(this, ProductActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
