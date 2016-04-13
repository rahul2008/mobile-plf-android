package com.philips.cdp.prodreg;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.philips.cdp.prodreg.backend.LocalRegisteredProducts;
import com.philips.cdp.prodreg.localcache.LocalSharedPreference;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegisteredProductsList extends AppCompatActivity {
    private ProductAdapter productAdapter;
    private RecyclerView mRecyclerView;
    private LocalSharedPreference localSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
              mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        LocalRegisteredProducts localRegisteredProducts = new LocalRegisteredProducts(RegisteredProductsList.this);
        productAdapter = new ProductAdapter(RegisteredProductsList.this, localRegisteredProducts.getRegisteredProducts());
       mRecyclerView.setAdapter(productAdapter);
    }

}
