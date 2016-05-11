/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.ShoppingCart;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.di.iap.Fragments.ShoppingCartFragment;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.CartAddProductRequest;
import com.philips.cdp.di.iap.model.CartCreateRequest;
import com.philips.cdp.di.iap.model.CartCurrentInfoRequest;
import com.philips.cdp.di.iap.model.CartDeleteProductRequest;
import com.philips.cdp.di.iap.model.CartUpdateProductQuantityRequest;
import com.philips.cdp.di.iap.model.GetRetailersInfoRequest;
import com.philips.cdp.di.iap.response.carts.Carts;
import com.philips.cdp.di.iap.response.carts.EntriesEntity;
import com.philips.cdp.di.iap.response.error.Error;
import com.philips.cdp.di.iap.response.error.ServerError;
import com.philips.cdp.di.iap.response.retailers.StoreEntity;
import com.philips.cdp.di.iap.response.retailers.WebResults;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.tagging.Tagging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCartPresenter {
    Context mContext;
    ArrayList<ShoppingCartData> mProductData;
    ArrayList<StoreEntity> mStoreEntities;
    private LoadListener mLoadListener;
    private LoadListenerForRetailer mLoadListenerForRetailer;
    private HybrisDelegate mHybrisDelegate;
    private Store mStore;
    private FragmentManager mFragmentManager;

    public interface LoadListener {
        void onLoadFinished(ArrayList<ShoppingCartData> data);
    }

    public ShoppingCartPresenter() {
    }

    public ShoppingCartPresenter(Context context, android.support.v4.app.FragmentManager fragmentManager) {
        mContext = context;
        mFragmentManager = fragmentManager;
    }

    public interface LoadListenerForRetailer {
        void onLoadFinished(ArrayList<StoreEntity> data);
    }

    public ShoppingCartPresenter(Context context, LoadListener listener, android.support.v4.app.FragmentManager fragmentManager) {
        mContext = context;
        mProductData = new ArrayList<>();
        mLoadListener = listener;
        mFragmentManager = fragmentManager;
    }

    public ShoppingCartPresenter(Context context, LoadListenerForRetailer listener, android.support.v4.app.FragmentManager fragmentManager) {
        mContext = context;
        mStoreEntities = new ArrayList<>();
        mLoadListenerForRetailer = listener;
        mFragmentManager = fragmentManager;
    }


    public ShoppingCartPresenter(android.support.v4.app.FragmentManager pFragmentManager) {
        mFragmentManager = pFragmentManager;
    }

    public void setHybrisDelegate(HybrisDelegate delegate) {
        mHybrisDelegate = delegate;
    }

    public HybrisDelegate getHybrisDelegate() {
        if (mHybrisDelegate == null) {
            mHybrisDelegate = HybrisDelegate.getInstance(mContext);
        }
        return mHybrisDelegate;
    }

    private Store getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }

    public void refreshList(ArrayList<ShoppingCartData> data) {
        if (mLoadListener != null) {
            mLoadListener.onLoadFinished(data);
        }
    }

    public void refreshListForRetailer(ArrayList<StoreEntity> data) {
        if (mLoadListenerForRetailer != null) {
            mLoadListenerForRetailer.onLoadFinished(data);
        }
    }

    private void sendHybrisRequest(int code, AbstractModel model, RequestListener listener) {
        getHybrisDelegate().sendRequest(code, model, model);
    }

    public void getCurrentCartDetails() {
        CartCurrentInfoRequest model = new CartCurrentInfoRequest(getStore(), null,
                new AbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(Message msg) {

                        if (msg.obj instanceof Carts) {
                            Carts cartData = (Carts) msg.obj;
                            if (cartData.getCarts().get(0).getEntries() == null) {
                                msg = Message.obtain(msg);
                            } else {
                                PRXProductDataBuilder builder = new PRXProductDataBuilder(mContext, cartData,
                                        this);
                                builder.build();
                                return;
                            }
                        }

                        if (msg.obj instanceof ArrayList) {
                            mProductData = (ArrayList<ShoppingCartData>) msg.obj;
                            if (mProductData == null || mProductData.size() == 0) {
                                EventHelper.getInstance().notifyEventOccurred(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED);
                                Utility.dismissProgressDialog();
                                return;
                            }
                            refreshList(mProductData);
                            CartModelContainer.getInstance().setShoppingCartData(mProductData);
                        } else {
                            EventHelper.getInstance().notifyEventOccurred(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED);
                            dismissProgressDialog();
                        }
                        dismissProgressDialog();
                    }

                    @Override
                    public void onModelDataError(final Message msg) {
                        if (isNoCartError(msg)) {
                            EventHelper.getInstance().notifyEventOccurred(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED);
                            Utility.dismissProgressDialog();
                        } else {
                            handleModelDataError(msg);
                        }
                    }
                });
        model.setContext(mContext);
        sendHybrisRequest(0, model, model);
    }

    private void handleModelDataError(final Message msg) {
        IAPLog.e(IAPConstant.SHOPPING_CART_PRESENTER, "Error:" + msg.obj);
        IAPLog.d(IAPConstant.SHOPPING_CART_PRESENTER, msg.obj.toString());
        NetworkUtility.getInstance().showErrorMessage(msg, mFragmentManager, mContext);
        dismissProgressDialog();
    }

    public void getRetailersInformation(String ctn) {

        Map<String, String> query = new HashMap<>();
        query.put(ModelConstants.PRODUCT_CODE, String.valueOf(ctn));

        GetRetailersInfoRequest model = new GetRetailersInfoRequest(getStore(), query,
                new AbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(Message msg) {
                        WebResults webResults = null;
                        if (msg.obj instanceof WebResults) {
                            webResults = (WebResults) msg.obj;
                        }

                        if (webResults.getWrbresults().getOnlineStoresForProduct() == null || webResults.getWrbresults().getOnlineStoresForProduct().getStores().getStore() == null || webResults.getWrbresults().getOnlineStoresForProduct().getStores().getStore().size() == 0) {
                            NetworkUtility.getInstance().showErrorDialog(mFragmentManager,mContext.getString(R.string.iap_ok),"No Retailers for this product","No Retailers for this product");
                            Utility.dismissProgressDialog();
                            return;
                        }
                        mStoreEntities = (ArrayList<StoreEntity>) webResults.getWrbresults().getOnlineStoresForProduct().getStores().getStore();
                        refreshListForRetailer(mStoreEntities);

                        dismissProgressDialog();
                    }

                    @Override
                    public void onModelDataError(final Message msg) {
                        handleModelDataError(msg);
                    }
                });
        model.setContext(mContext);
        sendHybrisRequest(0, model, model);
    }

    private void dismissProgressDialog() {
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    public void deleteProduct(final ShoppingCartData summary) {
        Map<String, String> query = new HashMap<>();
        query.put(ModelConstants.ENTRY_CODE, String.valueOf(summary.getEntryNumber()));

        CartDeleteProductRequest model = new CartDeleteProductRequest(getStore(), query,
                new AbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(final Message msg) {
                        //Track product delete action
                        Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA,
                                IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.PRODUCT_REMOVED);
                        getCurrentCartDetails();
                    }

                    @Override
                    public void onModelDataError(final Message msg) {
                        getCurrentCartDetails();
                    }
                });
        sendHybrisRequest(0, model, model);
    }

    public void updateProductQuantity(final ShoppingCartData data, final int count, final int quantityStatus) {
        HashMap<String, String> query = new HashMap<String, String>();
        query.put(ModelConstants.PRODUCT_CODE, data.getCtnNumber());
        query.put(ModelConstants.PRODUCT_QUANTITY, String.valueOf(count));
        query.put(ModelConstants.PRODUCT_ENTRYCODE, String.valueOf(data.getEntryNumber()));

        CartUpdateProductQuantityRequest model = new CartUpdateProductQuantityRequest(getStore(),
                query, new AbstractModel.DataLoadListener() {
            @Override
            public void onModelDataLoadFinished(final Message msg) {
                if (quantityStatus == 1) {
                    //Track Add to cart action
                    Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA, IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.ADD_TO_CART);
                } else if (quantityStatus == 0) {
                    //Track product delete action
                    Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA,
                            IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.PRODUCT_REMOVED);
                }
                getCurrentCartDetails();
            }

            @Override
            public void onModelDataError(final Message msg) {
                IAPLog.d(IAPConstant.SHOPPING_CART_PRESENTER, msg.obj.toString());
                NetworkUtility.getInstance().showErrorMessage(msg, mFragmentManager, mContext);
                Utility.dismissProgressDialog();
            }
        });
        sendHybrisRequest(0, model, model);
    }

    private void createCart(final Context context, final IAPCartListener iapHandlerListener, final String ctnNumber, final boolean isBuy) {
        HybrisDelegate delegate = HybrisDelegate.getInstance(context);
        CartCreateRequest model = new CartCreateRequest(delegate.getStore(), null, null);
        delegate.sendRequest(RequestCode.CREATE_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                if (isBuy) {
                    addProductToCart(context, ctnNumber, iapHandlerListener, true);
                } else {
                    if (iapHandlerListener != null) {
                        iapHandlerListener.onSuccess(0);
                    }
                }
            }

            @Override
            public void onError(final Message msg) {
                if (iapHandlerListener != null) {
                    iapHandlerListener.onFailure(msg);
                }
            }
        });
    }

    public void addProductToCart(final Context context, String productCTN, final IAPCartListener
            iapHandlerListener,
                                 final boolean isFromBuyNow) {
        if (productCTN == null) return;
        HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.PRODUCT_CODE, productCTN);
        HybrisDelegate delegate = HybrisDelegate.getInstance(context);
        CartAddProductRequest model = new CartAddProductRequest(delegate.getStore(), params, null);
        delegate.sendRequest(RequestCode.ADD_TO_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                if (isFromBuyNow) {
                    launchShoppingCart();
                    if (iapHandlerListener != null) {
                        iapHandlerListener.onSuccess(0);
                    }
                } else if (iapHandlerListener != null) {
                    iapHandlerListener.onSuccess(0);
                }
            }

            @Override
            public void onError(final Message msg) {
                if (iapHandlerListener != null) {
                    iapHandlerListener.onFailure(msg);
                }
            }
        });
    }

    public void getProductCartCount(final Context context, final IAPCartListener
            iapHandlerListener) {
        HybrisDelegate delegate = HybrisDelegate.getInstance(context);
        CartCurrentInfoRequest model = new CartCurrentInfoRequest(delegate.getStore(), null, null);
        model.setContext(context);

        delegate.sendRequest(RequestCode.GET_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
                    createCart(context, iapHandlerListener, null, false);
                } else {
                    Carts getCartData = (Carts) msg.obj;
                    if (null != getCartData) {
                        int quantity = 0;
                        int totalItems = getCartData.getCarts().get(0).getTotalItems();
                        List<EntriesEntity> entries = getCartData.getCarts().get(0).getEntries();
                        if (totalItems != 0 && null != entries) {
                            for (int i = 0; i < entries.size(); i++) {
                                quantity = quantity + entries.get(i).getQuantity();
                            }
                        }
                        if (iapHandlerListener != null) {
                            iapHandlerListener.onSuccess(quantity);
                        }
                    }
                }
            }

            @Override
            public void onError(final Message msg) {
                handleNoCartErrorOrNotifyError(msg, context, iapHandlerListener, null, false);
            }
        });
    }

    private void launchShoppingCart() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fl_mainFragmentContainer, new ShoppingCartFragment());
        transaction.addToBackStack(ShoppingCartFragment.TAG);
        transaction.commitAllowingStateLoss();
    }

    public void buyProduct(final Context context, final String ctnNumber, final IAPCartListener
            iapHandlerListener) {
        if (ctnNumber == null) return;
        HybrisDelegate delegate = HybrisDelegate.getInstance(context);
        CartCurrentInfoRequest model = new CartCurrentInfoRequest(delegate.getStore(), null, null);
        model.setContext(context);

        delegate.sendRequest(RequestCode.GET_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
                    createCart(context, iapHandlerListener, ctnNumber, true);
                } else if (msg.obj instanceof Carts) {
                    Carts getCartData = (Carts) msg.obj;
                    if (null != getCartData) {
                        int totalItems = getCartData.getCarts().get(0).getTotalItems();
                        List<EntriesEntity> entries = getCartData.getCarts().get(0).getEntries();
                        if (totalItems != 0 && null != entries) {
                            boolean isProductAvailable = false;
                            for (int i = 0; i < entries.size(); i++) {
                                if (entries.get(i).getProduct().getCode().equalsIgnoreCase(ctnNumber)) {
                                    isProductAvailable = true;
                                    launchShoppingCart();
                                    break;
                                }
                            }
                            if (!isProductAvailable)
                                addProductToCart(context, ctnNumber, iapHandlerListener, true);
                            if (iapHandlerListener != null) {
                                iapHandlerListener.onSuccess(0);
                            }
                        } else {
                            addProductToCart(context, ctnNumber, iapHandlerListener, true);
                        }
                    }
                }
            }

            @Override
            public void onError(final Message msg) {
                handleNoCartErrorOrNotifyError(msg, context, iapHandlerListener, ctnNumber, true);
            }
        });
    }

    private void handleNoCartErrorOrNotifyError(final Message msg, final Context context, final IAPCartListener iapHandlerListener, final String ctnNumber, final boolean isBuy) {
        if (isNoCartError(msg)) {
            createCart(context, iapHandlerListener, ctnNumber, isBuy);
        } else if (iapHandlerListener != null) {
            iapHandlerListener.onFailure(msg);
        }
    }

    private boolean isNoCartError(final Message msg) {
        if (msg.obj instanceof IAPNetworkError) {
            ServerError error = ((IAPNetworkError) msg.obj).getServerError();
            if (error != null && error.getErrors() != null &&
                    error.getErrors().get(0) != null) {
                Error err = error.getErrors().get(0);
                //// TODO: 04-05-2016 add with proper string type or type check
                if ("No cart created yet.".equals(err.getMessage())) {
                    return true;
                }
            }
        }
        return false;
    }
}
