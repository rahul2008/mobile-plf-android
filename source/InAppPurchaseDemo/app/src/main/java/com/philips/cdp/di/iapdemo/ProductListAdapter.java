package com.philips.cdp.di.iapdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.activity.ShoppingCartActivity;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;

public class ProductListAdapter extends BaseAdapter {
    ArrayList<ShoppingCartData> mProductList = new ArrayList<>();
    LayoutInflater inflater;
    Context mContext;

    public ProductListAdapter(Context context, ArrayList<ShoppingCartData> productList) {
        this.mProductList = productList;
        this.mContext = context;
        inflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public ShoppingCartData getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.product_list_item, parent, false);
            mViewHolder = new ProductViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ProductViewHolder) convertView.getTag();
        }

        final ShoppingCartData currentProduct = getItem(position);

        mViewHolder.name.setText(currentProduct.getCtnNumber());

        mViewHolder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isInternetConnected(mContext)) {
                    ((DemoAppActivity) mContext).addToCart(false, currentProduct.getCtnNumber());
                } else {
                    Utility.showNetworkError(((Activity) mContext), false);
                }

            }
        });

        mViewHolder.buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isInternetConnected(mContext)) {
                    if (((DemoAppActivity) mContext).getCount() == 0) {
                        ((DemoAppActivity) mContext).addToCart(true, currentProduct.getCtnNumber());
                    } else {
                        Intent myIntent = new Intent(mContext, ShoppingCartActivity.class);
                        mContext.startActivity(myIntent);
                    }
                } else {
                    Utility.showNetworkError(((Activity) mContext), false);
                }
            }
        });

        return convertView;
    }

    private class ProductViewHolder {
        TextView name;
        Button buyNow;
        Button addToCart;

        public ProductViewHolder(View product) {
            name = (TextView) product.findViewById(R.id.name);
            buyNow = (Button) product.findViewById(R.id.buy_now);
            addToCart = (Button) product.findViewById(R.id.add_to_Cart);
        }
    }
}
