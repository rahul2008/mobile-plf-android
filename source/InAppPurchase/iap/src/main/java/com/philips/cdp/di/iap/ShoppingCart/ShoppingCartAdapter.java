package com.philips.cdp.di.iap.ShoppingCart;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.session.NetworkImageLoader;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.di.iap.view.CountDropDown;
import com.philips.cdp.uikit.customviews.UIKitListPopupWindow;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.utils.RowItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ShoppingCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ShoppingCartPresenter.LoadListener{

    private Context mContext;
    private Resources mResources;
    private ArrayList<ShoppingCartData> mData = new ArrayList<ShoppingCartData>();
    private LayoutInflater mInflater;
    private ShoppingCartPresenter mPresenter;
    private Drawable countArrow;
    private UIKitListPopupWindow mPopupWindow;
    private FragmentManager mFragmentManager;
    private ShoppingCartData shoppingCartDataForProductDetailPage;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private static final int DELETE = 0;
    private static final int INFO = 1;
    private Drawable mOptionsDrawable;

    public ShoppingCartAdapter(Context context, ArrayList<ShoppingCartData> shoppingCartData, android.support.v4.app.FragmentManager fragmentManager) {
        mContext = context;
        mResources = context.getResources();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mData = shoppingCartData;
        mPresenter = new ShoppingCartPresenter(context, this,fragmentManager);
        mFragmentManager = fragmentManager;
        setCountArrow(context);
        initOptionsDrawable();
    }

    void initOptionsDrawable() {
        mOptionsDrawable = VectorDrawable.create(mContext, R.drawable.iap_options_icon_5x17);
    }

    private void setCountArrow(final Context context) {
        countArrow = VectorDrawable.create(context, R.drawable.iap_product_count_drop_down);
        int width = (int) mResources.getDimension(R.dimen.iap_count_drop_down_icon_width);
        int height = (int) mResources.getDimension(R.dimen.iap_count_drop_down_icon_height);
        countArrow.setBounds(0, 0, width, height);
    }

    @Override
    public int getItemViewType(final int position) {
        if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionFooter(final int position) {
        return position == mData.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.iap_shopping_cart_footer, parent, false);
            return new FooterShoppingCartViewHolder(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.iap_shopping_cart_data, parent, false);
            return new ShoppingCartProductHolder(v);
        }
        return null;
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
                        if(Utility.isInternetConnected(mContext)) {
                            if (!Utility.isProgressDialogShowing()) {
                                Utility.showProgressDialog(mContext, "Updating Cart Details");
                                mPresenter.updateProductQuantity(mData.get(position), newCount);
                            }
                        }else{
                            NetworkUtility.getInstance().showErrorDialog(mFragmentManager,mContext.getString(R.string.iap_ok),mContext.getString(R.string.iap_network_error),mContext.getString(R.string.iap_check_connection));
                        }
                    }
                });
                mPopupWindow = countPopUp.getPopUpWindow();
                countPopUp.show();
            }
        });
    }

    private void bindDeleteOrInfoPopUP(final View view, final int selectedItem) {
        List<RowItem> rowItems = new ArrayList<RowItem>();

        String delete = mResources.getString(R.string.iap_delete);
        String info = mResources.getString(R.string.iap_info);
        final String[] descriptions = new String[]{delete, info};

        rowItems.add(new RowItem(VectorDrawable.create(mContext, R.drawable.iap_trash_bin), descriptions[0]));
        rowItems.add(new RowItem(ContextCompat.getDrawable(mContext, R.drawable.iap_info), descriptions[1]));
        mPopupWindow =  new UIKitListPopupWindow(mContext, view, UIKitListPopupWindow.UIKIT_Type.UIKIT_BOTTOMLEFT, rowItems);

        mPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                switch (position) {
                    case DELETE:
                        if (Utility.isInternetConnected(mContext)) {
                            if (!Utility.isProgressDialogShowing()) {
                                Utility.showProgressDialog(mContext,mContext.getString(R.string.iap_deleting_item));
                                mPresenter.deleteProduct(mData.get(selectedItem));
                                mPopupWindow.dismiss();
                            }
                        }else{
                            NetworkUtility.getInstance().showErrorDialog(mFragmentManager, mContext.getString(R.string.iap_ok), mContext.getString(R.string.iap_network_error), mContext.getString(R.string.iap_check_connection));
                        }
                        break;
                    case INFO:
                        setTheProductDataForDisplayingInProductDetailPage(selectedItem);
                        break;
                    default:
                }
            }
        });
        mPopupWindow.show();
    }

    private void setTheProductDataForDisplayingInProductDetailPage(int position) {
        shoppingCartDataForProductDetailPage = mData.get(position);
        EventHelper.getInstance().notifyEventOccurred(IAPConstant.PRODUCT_DETAIL_FRAGMENT);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(mData.size()==0)
            return;

        if (holder instanceof ShoppingCartProductHolder) {
            //Product Layout
            final ShoppingCartData cartData = mData.get(position);
            ShoppingCartProductHolder shoppingCartProductHolder = (ShoppingCartProductHolder) holder;
            String imageURL = cartData.getImageURL();
            shoppingCartProductHolder.mTvProductTitle.setText(cartData.getProductTitle());
            shoppingCartProductHolder.mIvOptions.setImageDrawable(mOptionsDrawable);
            shoppingCartProductHolder.mTvPrice.setText(cartData.getTotalPriceFormatedPrice());
            shoppingCartProductHolder.mTvQuantity.setText(cartData.getQuantity() + "");

            getNetworkImage(shoppingCartProductHolder, imageURL);

            shoppingCartProductHolder.mDotsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    bindDeleteOrInfoPopUP(view,position);
                }
            });
            //Add arrow mark
            shoppingCartProductHolder.mTvQuantity.setCompoundDrawables(null,null,countArrow,null);
            bindCountView(shoppingCartProductHolder.mQuantityLayout, position);
        } else {
            //Footer Layout
            FooterShoppingCartViewHolder shoppingCartFooter = (FooterShoppingCartViewHolder) holder;
                ShoppingCartData data = null;
                if (mData.get(0) != null) {
                    data = mData.get(0);

                    shoppingCartFooter.mTotalItems.setText(mContext.getString(R.string.iap_total) + " (" + data.getTotalItems() + " " + mContext.getString(R.string.iap_items) + ")");
                    shoppingCartFooter.mTotalCost.setText(data.getTotalPriceWithTaxFormatedPrice());
                    if(null != data.getDeliveryCost()) {
                        shoppingCartFooter.mDeliveryPrice.setText(data.getDeliveryCost().getFormattedValue());
                    }else{
                        shoppingCartFooter.mDeliveryPrice.setText("$0.0");
                    }
                }
        }
    }

    private void getNetworkImage(final ShoppingCartProductHolder shoppingCartProductHolder, final String imageURL) {
        ImageLoader mImageLoader;
        // Instantiate the RequestQueue.
        mImageLoader = NetworkImageLoader.getInstance(mContext)
                .getImageLoader();

        mImageLoader.get(imageURL, ImageLoader.getImageListener(shoppingCartProductHolder.mNetworkImage,
                R.drawable.toothbrush, android.R.drawable
                        .ic_dialog_alert));
        shoppingCartProductHolder.mNetworkImage.setImageUrl(imageURL, mImageLoader);
    }

    public void onStop() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    public ShoppingCartData getTheProductDataForDisplayingInProductDetailPage(){
        return shoppingCartDataForProductDetailPage;
    }

    @Override
    public int getItemCount() {
        if (mData.size() == 0) {
            return 0;
        } else {
            return mData.size() + 1;
        }
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public void onLoadFinished(final ArrayList<ShoppingCartData> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public class ShoppingCartProductHolder extends RecyclerView.ViewHolder {
        NetworkImageView mNetworkImage;
        FrameLayout mDotsLayout;
        TextView mTvPrice;
        TextView mTvProductTitle;
        RelativeLayout mQuantityLayout;
        TextView mTvStock;
        TextView mTvQuantity;
        ImageView mIvOptions;

        public ShoppingCartProductHolder(final View itemView) {
            super(itemView);
            mNetworkImage = (NetworkImageView) itemView.findViewById(R.id.image);
            mDotsLayout = (FrameLayout) itemView.findViewById(R.id.frame);
            mTvPrice = (TextView) itemView.findViewById(R.id.price);
            mTvProductTitle = (TextView)itemView.findViewById(R.id.text1Name);
            mQuantityLayout = (RelativeLayout) itemView.findViewById(R.id.quantity_count_layout);
            mTvStock = (TextView) itemView.findViewById(R.id.out_of_stock);
            mTvQuantity = (TextView) itemView.findViewById(R.id.text2value);
            mIvOptions = (ImageView) itemView.findViewById(R.id.dots);
        }
    }

    public class FooterShoppingCartViewHolder extends RecyclerView.ViewHolder {
        TextView mDeliveryPrice;
        TextView mVatValue;
        TextView mTotalItems;
        TextView mTotalCost;

        public FooterShoppingCartViewHolder(View itemView) {
            super(itemView);
            mDeliveryPrice = (TextView) itemView.findViewById(R.id.iap_tv_delivery_price);
            mVatValue = (TextView) itemView.findViewById(R.id.iap_tv_vat_value);
            mTotalItems = (TextView) itemView.findViewById(R.id.iap_tv_totalItems);
            mTotalCost = (TextView) itemView.findViewById(R.id.iap_tv_totalcost);
        }
    }

}
