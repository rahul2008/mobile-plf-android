/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.ShoppingCart;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import com.philips.cdp.di.iap.activity.ShoppingCartActivity;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.di.iap.view.CountDropDown;
import com.philips.cdp.uikit.customviews.UIKitListPopupWindow;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.utils.RowItem;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartAdapter extends BaseAdapter implements ShoppingCartPresenter.LoadListener {
    private static final int DELETE = 0;
    private static final int INFO = 1;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SHIPPING_DETAILS = 1;

    private Context mContext;
    private Resources mResources;
    private ArrayList<ShoppingCartData> mData = new ArrayList<ShoppingCartData>();
    private LayoutInflater mInflater;
    private ShoppingCartPresenter mPresenter;
    private final Drawable countArrow;
    private UIKitListPopupWindow mPopupWindow;
    //ShoppingCartData summary;
    private boolean mIsOutOfStock;

    public ShoppingCartAdapter(Context context, ArrayList<ShoppingCartData> shoppingCartData) {
        mContext = context;
        mResources = context.getResources();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mData = shoppingCartData;
        mPresenter = new ShoppingCartPresenter(context, this);
        countArrow = VectorDrawable.create(context, R.drawable.iap_product_count_drop_down);
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

    @Override
    public int getItemViewType(int position) {
        ShoppingCartData summary = mData.get(position);
        if (summary.getCtnNumber() == null) {
            return TYPE_SHIPPING_DETAILS;
        } else {
            return TYPE_ITEM;
        }
    }

    //TODO
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        final ShoppingCartData cartData = mData.get(position);
        int rowType = getItemViewType(position);
        holder = new ViewHolder();
        switch (rowType) {
            case TYPE_ITEM:
                String imageURL = cartData.getImageURL();
                convertView = mInflater.inflate(R.layout.listview_shopping_cart, null);
                holder.image = (NetworkImageView) convertView.findViewById(R.id.image);
                holder.nameOption = (TextView) convertView.findViewById(R.id.text1Name);
                holder.valueOption = (TextView) convertView.findViewById(R.id.text2value);
                holder.from = (TextView) convertView.findViewById(R.id.from);
                holder.price = (TextView) convertView.findViewById(R.id.price);
                holder.dots = (ImageView) convertView.findViewById(R.id.dots);
                holder.quantitydropdown = (ImageView) convertView.findViewById(R.id.quantity_drop_down);

                holder.outOfStock = (TextView) convertView.findViewById(R.id.out_of_stock);
                if(mIsOutOfStock)
                    holder.outOfStock.setVisibility(View.VISIBLE);
                else
                    holder.outOfStock.setVisibility(View.GONE);

                FrameLayout frameLayout = (FrameLayout) convertView.findViewById(R.id.frame);
                holder.imageUrl = imageURL;

                holder.from.setText(mResources.getString(R.string.iap_product_item_quantity));
                holder.nameOption.setText(cartData.getProductTitle());
                holder.price.setText(cartData.getCurrency() + " " + cartData.getTotalPrice());
                holder.valueOption.setText(cartData.getQuantity() + "");

                //TODO: Fix it
                ImageLoader mImageLoader;
                // Instantiate the RequestQueue.
                mImageLoader = NetworkImageLoader.getInstance(mContext)
                        .getImageLoader();

                mImageLoader.get(imageURL, ImageLoader.getImageListener(holder.image,
                        R.drawable.toothbrush, android.R.drawable
                                .ic_dialog_alert));
                holder.image.setImageUrl(imageURL, mImageLoader);
                holder.dots.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        bindDeleteOrInfoPopUP(view);
                    }
                });

                //Add arrow mark
                //holder.quantitydropdown.setCompoundDrawables(null, null, countArrow, null);
                holder.quantitydropdown.setImageDrawable(countArrow);
                bindCountView(holder.quantitydropdown, position);
                break;

            case TYPE_SHIPPING_DETAILS:


                convertView = mInflater.inflate(R.layout.shopping_cart_price, null);

                holder.name = (TextView) convertView.findViewById(R.id.ifo);
                holder.number = (TextView) convertView.findViewById(R.id.numberwithouticon);
                holder.on_off = (TextView) convertView.findViewById(R.id.medium);
                holder.arrow = (ImageView) convertView.findViewById(R.id.arrowwithouticons);
                holder.description = (TextView) convertView.findViewById(R.id.text_description_without_icons);
                holder.totoalcost = (TextView) convertView.findViewById(R.id.totalcost);

                if (position == mData.size() - 1) {
                    //Last Row
                    ShoppingCartData data = null;
                    if (mData.get(0) != null) {
                        data = mData.get(0);

                        holder.name.setVisibility(View.VISIBLE);
                        holder.name.setText("VAT");

                        //TODO: Fix count form server request
                        holder.number.setVisibility(View.VISIBLE);
                        holder.number.setText("0");

                        holder.description.setVisibility(View.VISIBLE);
                        int totalItems = mData.size()-3;
                        holder.description.setText("Total (" + totalItems + ")");

                        holder.totoalcost.setVisibility(View.VISIBLE);
                        holder.totoalcost.setText(data.getCurrency() + " " + data.getTotalPrice());
                    }
                }
                if (position == mData.size() - 2) {
                    //2nd Last Row
                    holder.name.setVisibility(View.VISIBLE);
                    holder.name.setText("Delivery via UPS Parcel");

                    holder.number.setVisibility(View.VISIBLE);
                    holder.number.setText("TBD");

                    holder.description.setVisibility(View.VISIBLE);
                    holder.description.setText("Delivery is free when you spend USD 100 or more");
                }
                if (position == mData.size() - 3) {
                    //3rd Last Row
                    holder.name.setVisibility(View.VISIBLE);
                    holder.name.setText("Claim voucher");

                    holder.arrow.setVisibility(View.VISIBLE);
                }
                break;
        }

        return convertView;
    }

    private void bindDeleteOrInfoPopUP(final View view) {
        List<RowItem> rowItems = new ArrayList<RowItem>();

        String delete = mResources.getString(R.string.iap_delete);
        String info = mResources.getString(R.string.iap_info);
        final String[] descriptions = new String[]{delete, info};

        rowItems.add(new RowItem(VectorDrawable.create(mContext, R.drawable.uikit_gear_19_19), descriptions[0]));
        rowItems.add(new RowItem(VectorDrawable.create(mContext, R.drawable.uikit_share), descriptions[1]));
        mPopupWindow =  new UIKitListPopupWindow(mContext, view, UIKitListPopupWindow.UIKIT_Type.UIKIT_BOTTOMLEFT, rowItems);

        mPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                switch (position) {
                    case DELETE:
                        mPresenter.deleteProduct(mData.get(position));
                        mPopupWindow.dismiss();
                        break;
                    case INFO:
                        Toast.makeText(mContext.getApplicationContext(), "Details Screen Not Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
            }
        });
        mPopupWindow.show();
    }

    private void bindCountView(final View view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ShoppingCartData data = mData.get(position);

                CountDropDown countPopUp = new CountDropDown(v, data.getStockLevel(), data
                        .getQuantity(), new CountDropDown.CountUpdateListener() {
                    @Override
                    public void countUpdate(final int oldCount, final int newCount) {
                        Utility.showProgressDialog(mContext,"Updating Cart Details");
                        mPresenter.updateProductQuantity(mData,position, newCount);
                    }
                });
                mPopupWindow = countPopUp.getPopUpWindow();
                countPopUp.show();
            }
        });
    }

    @Override
    public void onLoadFinished(ArrayList<ShoppingCartData> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void onStop() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }
    @Override
    public void updateStock(boolean isOutOfStock) {
        mIsOutOfStock = isOutOfStock;
        notifyDataSetChanged();

        ((ShoppingCartActivity)mContext).setCheckoutBtnState(!isOutOfStock);
    }

    private static class ViewHolder {
        TextView name;
        TextView number;
        TextView on_off;
        ImageView arrow;
        TextView description;
        TextView totoalcost;
        NetworkImageView image;
        ImageView dots;
        TextView nameOption;
        TextView valueOption;
        TextView from;
        TextView price;
        String imageUrl;
        ImageView quantitydropdown;
        TextView outOfStock;
    }
}
