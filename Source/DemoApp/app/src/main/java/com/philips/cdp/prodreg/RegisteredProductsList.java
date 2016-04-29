package com.philips.cdp.prodreg;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.philips.cdp.prodreg.listener.RegisteredProductsListener;
import com.philips.cdp.prodreg.register.ProdRegHelper;
import com.philips.cdp.prodreg.register.RegisteredProduct;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegisteredProductsList extends AppCompatActivity {
    private ProductAdapter productAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        ProdRegHelper prodRegHelper = new ProdRegHelper();
        prodRegHelper.init(this);
        prodRegHelper.getSignedInUserWithProducts().getRegisteredProducts(new RegisteredProductsListener() {
            @Override
            public void getRegisteredProducts(final List<RegisteredProduct> registeredProducts, final long timeStamp) {
                productAdapter = new ProductAdapter(RegisteredProductsList.this, registeredProducts);
                mRecyclerView.setAdapter(productAdapter);
            }
        });

    }
}
