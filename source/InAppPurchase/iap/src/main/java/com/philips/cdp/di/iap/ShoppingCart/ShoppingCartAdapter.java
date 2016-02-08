/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.ShoppingCart;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.uikit.customviews.UIKitListPopupWindow;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.utils.RowItem;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartAdapter extends BaseAdapter implements ShoppingCartPresenter.LoadListener{
    private static final int DELETE = 0;
    private static final int INFO = 1;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SHIPPING_DETAILS = 1;

    private Context mContext;
    private Resources mResources;
    // private LayoutInflater inflater = null;
    private ArrayList<ShoppingCartData> mData = new ArrayList<ShoppingCartData>();
    private LayoutInflater mInflater;
    private ShoppingCartPresenter mPresenter;
    //ShoppingCartData summary;

    public ShoppingCartAdapter(Context context, ArrayList<ShoppingCartData> shoppingCartData) {
        mContext = context;
        mResources = context.getResources();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mData = shoppingCartData;
        mPresenter = new ShoppingCartPresenter(context, this);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(final int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    //TODO:
    @Override
    public int getItemViewType(int position) {
        ShoppingCartData summary = mData.get(position);
        if(summary.getProductTitle().contains("*")){
            return TYPE_SHIPPING_DETAILS;
        }else{
            return TYPE_ITEM;
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        //TODO: Fix the holder optimization
        ViewHolder holder = null;
        final ShoppingCartData cartData = mData.get(position);
        int rowType = getItemViewType(position);
        holder = new ViewHolder();
        switch (rowType) {
            case TYPE_ITEM:
                String imageURL = cartData.getImageURL();

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
                FrameLayout frameLayout = (FrameLayout) convertView.findViewById(R.id.frame);

                holder.imageUrl = imageURL;

                holder.from.setText(mResources.getString(R.string.iap_product_item_quantity));
                holder.nameOption.setText(cartData.getProductTitle());
                holder.price.setText(cartData.getCurrency() + " " + cartData.getTotalPrice());
                holder.valueOption.setText(cartData.getQuantity()+"");

                //TODO: Fix it
                ImageLoader mImageLoader;
                // Instantiate the RequestQueue.
                mImageLoader = NetworkImageLoader.getInstance(mContext)
                        .getImageLoader();

                mImageLoader.get(imageURL, ImageLoader.getImageListener(holder.image,
                        R.drawable.toothbrush, android.R.drawable
                                .ic_dialog_alert));
                holder.image.setImageUrl(imageURL, mImageLoader);
                Utility.dismissProgressDialog();
                holder.dots.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        bindDeleteOrInfoPopUP(view);
                    }
                });
                break;

            case TYPE_SHIPPING_DETAILS:
                convertView = mInflater.inflate(R.layout.shopping_cart_price, null);
                holder.name = (TextView) convertView.findViewById(R.id.ifo);
                holder.number = (TextView) convertView.findViewById(R.id.numberwithouticon);
                holder.on_off = (TextView) convertView.findViewById(R.id.medium);
                holder.arrow = (ImageView) convertView.findViewById(R.id.arrowwithouticons);
                holder.description = (TextView) convertView.findViewById(R.id.text_description_without_icons);
                holder.totoalcost = (TextView) convertView.findViewById(R.id.totalcost);

                //TODO: Fix with getTag while setting
                if(position == mData.size()-1){
                //Last Row
                    ShoppingCartData data=null;
                    if(mData.get(0)!=null) {
                        data = mData.get(0);

                        holder.name.setVisibility(View.VISIBLE);
                        holder.name.setText("VAT");

                        //TODO: Fix count form server request
                        holder.number.setVisibility(View.VISIBLE);
                        holder.number.setText("0");

                        holder.description.setVisibility(View.VISIBLE);
                        holder.description.setText("Total (" + data.getTotalItems() + ")");

                        holder.totoalcost.setVisibility(View.VISIBLE);
                        holder.totoalcost.setText(data.getCurrency() + " " + data.getTotalPrice());
                    }
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
        return convertView;
    }

    private void bindDeleteOrInfoPopUP(final View view) {
        List<RowItem> rowItems = new ArrayList<RowItem>();

        String delete = mResources.getString(R.string.iap_delete);
        String info = mResources.getString(R.string.iap_info);
        final String[] descriptions = new String[]{delete,info};

        rowItems.add(new RowItem(VectorDrawable.create(mContext, R.drawable.uikit_gear_19_19), descriptions[0]));
        rowItems.add(new RowItem(VectorDrawable.create(mContext, R.drawable.uikit_share), descriptions[1]));
        final UIKitListPopupWindow popUP =  new UIKitListPopupWindow(mContext, view, UIKitListPopupWindow.UIKIT_Type.UIKIT_BOTTOMLEFT, rowItems);

        popUP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                switch (position) {
                    case DELETE:
                        mPresenter.deleteProduct(mData.get(position));
                        popUP.dismiss();
                        break;
                    case INFO:
                        Toast.makeText(mContext.getApplicationContext(), "Details Screen Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
            }
        });
        popUP.show();
    }

    @Override
    public void onLoadFinished(ArrayList<ShoppingCartData> data) {
        mData = data;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
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
}
