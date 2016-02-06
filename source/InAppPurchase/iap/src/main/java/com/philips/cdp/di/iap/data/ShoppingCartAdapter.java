/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.data;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.model.CartModel;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.di.iap.view.CountDropDown;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingCartAdapter extends BaseAdapter {
    public Activity activity;
    Bundle saveBundle = new Bundle();
   // private LayoutInflater inflater = null;
    private ArrayList<ProductData> mData = new ArrayList<ProductData>();
    private LayoutInflater mInflater;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SHIPPING_DETAILS = 1;
    ProductData summary;

    private final Drawable countArrow;

    enum Type {
        TOPLEFT, TOPRIGHT, LEFT, RIGHT, BOTTOMLEFT, BOTTOMRIGHT
    }

    public ShoppingCartAdapter(Activity activity) {
        this.activity = activity;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        countArrow = VectorDrawable.create(activity, R.drawable.iap_product_count_drop_down);
        addShippingItem();
    }

    public void addItem(final ProductData item) {
        mData.add(mData.size() - 3, item);
         Log.i("SPOORTI", "mData = " + mData.toString());
        notifyDataSetChanged();
    }


    public void addShippingItem() {
        ProductData summary = new ProductData();
        summary.setProductTitle("**shippingItem**");
        mData.add(summary);

        mData.add(summary);
        mData.add(summary);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(final int position) {
        return position;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        ProductData summary = mData.get(position);
        if(summary.getProductTitle().contains("*")){
            return TYPE_SHIPPING_DETAILS;
        }else{
            return TYPE_ITEM;
        }
        //int i =  sectionHeader.contains(position) ? TYPE_SHIPPING_DETAILS : TYPE_ITEM;
        //return i;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder holder = null;
        int rowType = getItemViewType(position);
        holder = new ViewHolder();
        switch (rowType) {
            case TYPE_ITEM:
                summary = mData.get(position);
                String imageURL = summary.getImageURL();

                try {
                    convertView = mInflater.inflate(R.layout.listview_shopping_cart, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }



                holder.image = (NetworkImageView) convertView.findViewById(R.id.image);
                holder.nameOption = (TextView) convertView.findViewById(R.id.text1Name);
                holder.valueOption = (TextView) convertView.findViewById(R.id.text2value);
                holder.from = (TextView) convertView.findViewById(R.id.from);
                holder.price = (TextView)convertView.findViewById(R.id.price);
                holder.dots = (ImageView) convertView.findViewById(R.id.dots);
//                FrameLayout frameLayout = (FrameLayout) convertView.findViewById(R.id.frame);

                holder.imageUrl = imageURL;

                holder.from.setText("Quantity: ");
                holder.nameOption.setText(summary.getProductTitle());
                //Add arrow mark
                holder.nameOption.setCompoundDrawables(null,null,countArrow,null);
                holder.price.setText(summary.getCurrency() + " " + summary.getPrice());
                holder.valueOption.setText(summary.getQuantity()+"");

                ImageLoader mImageLoader;
                // Instantiate the RequestQueue.
                mImageLoader = NetworkImageLoader.getInstance(activity)
                        .getImageLoader();

                mImageLoader.get(imageURL, ImageLoader.getImageListener(holder.image,
                        R.drawable.toothbrush, android.R.drawable
                                .ic_dialog_alert));
                holder.image.setImageUrl(imageURL, mImageLoader);
                Utility.dismissProgressDialog();

                //Bind count drop down with view
                bindCountView(holder.valueOption, position);
                break;

            case TYPE_SHIPPING_DETAILS:
                convertView = mInflater.inflate(R.layout.shopping_cart_price, null);
                holder.name = (TextView) convertView.findViewById(R.id.ifo);
                holder.number = (TextView) convertView.findViewById(R.id.numberwithouticon);
                holder.on_off = (TextView) convertView.findViewById(R.id.medium);
                holder.arrow = (ImageView) convertView.findViewById(R.id.arrowwithouticons);
                holder.description = (TextView) convertView.findViewById(R.id.text_description_without_icons);
                holder.totoalcost = (TextView) convertView.findViewById(R.id.totalcost);

                if(position == mData.size()-1){
                //Last Row
                    holder.name.setVisibility(View.VISIBLE);
                    holder.name.setText("VAT");

                    holder.number.setVisibility(View.VISIBLE);
                    holder.number.setText("0");

                    holder.description.setVisibility(View.VISIBLE);
                    holder.description.setText("Total (" + summary.getTotalItems() + ")");

                    holder.totoalcost.setVisibility(View.VISIBLE);
                    holder.totoalcost.setText(summary.getCurrency() + " " + summary.getPrice());

                }
                if(position == mData.size()-2){
                //2nd Last Row
                    holder.name.setVisibility(View.VISIBLE);
                    holder.name.setText("Delivery via UPS Parcel");

                    holder.number.setVisibility(View.VISIBLE);
                    holder.number.setText("0");

                    holder.description.setVisibility(View.VISIBLE);
                    holder.description.setText("Delivery is free when you spend USD 100 or more");
                }
                if(position == mData.size() - 3){
                    //3rd Last Row
                    holder.name.setVisibility(View.VISIBLE);
                    holder.name.setText("Claim voucher");

                    holder.arrow.setVisibility(View.VISIBLE);
                }
                break;
        }
        convertView.setTag(holder);

        holder = (ViewHolder) convertView.getTag();


        return convertView;
    }

    public Bundle getSavedBundle() {
        return saveBundle;
    }

    public void setSavedBundle(Bundle bundle) {
        saveBundle = bundle;
    }

    private void setSwitchState(CompoundButton toggleSwitch, String code) {
        if (saveBundle.containsKey(code)) {
            toggleSwitch.setChecked(saveBundle.getBoolean(code));
        }
    }

    public static class ViewHolder {
        TextView name;// = (TextView) vi.findViewById(R.id.ifo);
        TextView number;// = (TextView) vi.findViewById(R.id.numberwithouticon);
        TextView on_off;// = (TextView) vi.findViewById(R.id.medium);
        ImageView arrow;// = (FontIconTextView) vi.findViewById(R.id.arrowwithouticons);
        TextView description;// = (TextView) vi.findViewById(R.id.text_description_without_icons);
        TextView totoalcost;
        NetworkImageView image;
        ImageView dots;
        //ImageView image;// = (ImageView) vi.findViewById(R.id.image);
        TextView nameOption;// = (TextView) vi.findViewById(R.id.text1Name);
        TextView valueOption;// = (TextView) vi.findViewById(R.id.text2value);
        TextView from;// = (TextView) vi.findViewById(R.id.from);
        TextView price;
        String imageUrl;
        Bitmap bitmap;
    }

    private class DownloadAsyncTask extends AsyncTask<ViewHolder, Void, ViewHolder> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // Utility.showProgressDialog(activity, "getting Cart Info");
        }

        @Override
        protected ViewHolder doInBackground(ViewHolder... params) {
            //load image directly
            ViewHolder viewHolder = params[0];

            try {
                URL imageURL = new URL(viewHolder.imageUrl);
                viewHolder.bitmap = BitmapFactory.decodeStream(imageURL.openStream());
            } catch (IOException e) {
                // TODO: handle exception
                Log.e("error", "Downloading Image Failed");
                viewHolder.bitmap = null;
            }

            return viewHolder;
        }

        @Override
        protected void onPostExecute(ViewHolder result) {
            // TODO Auto-generated method stub
            if (result.bitmap == null) {
                result.image.setImageResource(R.drawable.toothbrush);
            } else {
                result.image.setImageBitmap(result.bitmap);
            }
            Utility.dismissProgressDialog();
        }
    }

    private void bindCountView(final View view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ProductData data = mData.get(position);

                CountDropDown countPopUp= new CountDropDown(v, data.getTotalItems(), data
                        .getQuantity(), new CountDropDown.CountUpdateListener() {
                            @Override
                            public void countUpdate(final int oldCount, final int newCount) {
                                updateProductCount(position,newCount);
                            }
                        });
                countPopUp.show();
            }
        });
    }

    //Should be moved to container view
    void updateProductCount(int position, int count) {
        ProductData product = mData.get(position);
        HashMap<String,String> params = new HashMap<String, String>();
        params.put(CartModel.PRODUCT_CODE, product.getCtnNumber());
        params.put(CartModel.PRODUCT_QUANTITY,String.valueOf(count));
        HybrisDelegate.getInstance(activity.getApplicationContext())
                .sendRequest(RequestCode.UPDATE_PRODUCT_COUNT,null,params);
    }
}
