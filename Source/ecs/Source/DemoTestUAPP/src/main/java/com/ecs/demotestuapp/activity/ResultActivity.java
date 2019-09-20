
package com.ecs.demotestuapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ecs.demotestuapp.R;
import com.ecs.demotestuapp.model.PropertyItem;
import com.ecs.demotestuapp.util.ECSDataHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.ECSOAuthProvider;
import com.philips.cdp.di.ecs.model.address.ECSAddress;
import com.philips.cdp.di.ecs.model.address.ECSDeliveryMode;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.config.ECSConfig;
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData;
import com.philips.cdp.di.ecs.model.products.ECSProduct;
import com.philips.cdp.di.ecs.model.products.ECSProducts;
import com.philips.cdp.di.ecs.model.user.ECSUserProfile;
import com.philips.cdp.di.ecs.model.voucher.ECSVoucher;

import java.util.List;


public class ResultActivity extends AppCompatActivity {

    TextView tvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_screen);

        tvResult = findViewById(R.id.tv_result);

        String result = getIntent().getStringExtra("result");
        tvResult.setText("Result\n\n"+result);
    }

  }