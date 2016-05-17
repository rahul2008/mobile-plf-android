package com.philips.cdp.prodreg;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.philips.cdp.prodreg.listener.RegisteredProductsListener;
import com.philips.cdp.prodreg.register.ProdRegHelper;
import com.philips.cdp.prodreg.register.RegisteredProduct;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegisteredProductsList extends AppCompatActivity {
    public interface OnItemClickListener {
        void onItemClick(RegisteredProduct item);
    }
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final TextView mVersion = (TextView) findViewById(R.id.txt_version);
        final ProdRegHelper prodRegHelper = new ProdRegHelper();
        mVersion.setText("versionName :" + prodRegHelper.getLibVersion());
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ProdRegHelper prodRegHelper = new ProdRegHelper();
        prodRegHelper.init(this);
        prodRegHelper.getSignedInUserWithProducts().getRegisteredProducts(new RegisteredProductsListener() {
            @Override
            public void getRegisteredProductsSuccess(final List<RegisteredProduct> registeredProducts, final long timeStamp) {
                final ProductAdapter productAdapter = new ProductAdapter(RegisteredProductsList.this, registeredProducts, new OnItemClickListener() {
                    @Override
                    public void onItemClick(final RegisteredProduct registeredProduct) {
                        Intent intent = new Intent(RegisteredProductsList.this, ProductActivity.class);
                        intent.putExtra("ctn", registeredProduct.getCtn());
                        intent.putExtra("date", registeredProduct.getPurchaseDate());
                        intent.putExtra("serial", registeredProduct.getSerialNumber());
                        startActivity(intent);
                    }
                });
                mRecyclerView.setAdapter(productAdapter);
            }
        });
    }
}
