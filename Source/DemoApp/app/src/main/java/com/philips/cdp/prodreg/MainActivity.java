package com.philips.cdp.prodreg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnUserRegistration;
    private Button mBtnProductRegistration;
    private Button mBtnDiCom;
    private Button mBtnRegisterList;
    private String TAG = getClass().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnUserRegistration = (Button) findViewById(R.id.btn_user_registration);
        mBtnUserRegistration.setOnClickListener(this);

        mBtnProductRegistration = (Button) findViewById(R.id.btn_product_registration);
        mBtnProductRegistration.setOnClickListener(this);

        mBtnDiCom = (Button) findViewById(R.id.btn_dicom);
        mBtnDiCom.setOnClickListener(this);

        mBtnRegisterList= (Button) findViewById(R.id.btn_register_list);
        mBtnRegisterList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_user_registration:
                Log.d(TAG, "On Click : User Registration");
                RegistrationLaunchHelper.launchRegistrationActivityWithAccountSettings(this);
                Util.navigateFromUserRegistration();
                break;
            case R.id.btn_product_registration:
                intent = new Intent(this, ProductActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_dicom:
                intent = new Intent(this, DiComActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_register_list:
                intent = new Intent(this, RegisteredProductsList.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
