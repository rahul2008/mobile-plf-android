/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.ShoppingCart;

import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.core.AbstractShoppingCartPresenter;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.CartAddProductRequest;
import com.philips.cdp.di.iap.model.CartCreateRequest;
import com.philips.cdp.di.iap.model.CartCurrentInfoRequest;
import com.philips.cdp.di.iap.model.CartDeleteProductRequest;
import com.philips.cdp.di.iap.model.CartUpdateProductQuantityRequest;
import com.philips.cdp.di.iap.prx.PRXDataBuilder;
import com.philips.cdp.di.iap.response.carts.Carts;
import com.philips.cdp.di.iap.response.carts.CartsEntity;
import com.philips.cdp.di.iap.response.carts.EntriesEntity;
import com.philips.cdp.di.iap.response.error.Error;
import com.philips.cdp.di.iap.response.error.ServerError;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.prxclient.datamodels.summary.Data;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.tagging.Tagging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCartPresenter extends AbstractShoppingCartPresenter implements AbstractModel.DataLoadListener {

    public interface ShoppingCartLauncher {
        void launchShoppingCart();
    }

    Carts mCartData = null;

    public ShoppingCartPresenter() {
    }

    public ShoppingCartPresenter(Context context, LoadListener listener, FragmentManager fragmentManager) {
        super(context, listener, fragmentManager);
    }

    public ShoppingCartPresenter(android.support.v4.app.FragmentManager pFragmentManager) {
        mFragmentManager = pFragmentManager;
    }

    public void setHybrisDelegate(HybrisDelegate delegate) {
        mHybrisDelegate = delegate;
    }

    @Override
    public void getCurrentCartDetails() {
        CartCurrentInfoRequest model = new CartCurrentInfoRequest(getStore(), null, this);
        model.setContext(mContext);
        sendHybrisRequest(0, model, model);
    }

    private void notifyListChanged(final HashMap<String, SummaryModel> prxModel) {
        ArrayList<ShoppingCartData> products = mergeResponsesFromHybrisAndPRX();
        refreshList(products);
        CartModelContainer.getInstance().setShoppingCartData(products);
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    private ArrayList<ShoppingCartData> mergeResponsesFromHybrisAndPRX() {
        CartsEntity cartsEntity = mCartData.getCarts().get(0);
        List<EntriesEntity> entries = cartsEntity.getEntries();
        HashMap<String, SummaryModel> list = CartModelContainer.getInstance().getPRXDataObjects();
        ArrayList<ShoppingCartData> products = new ArrayList<>();
        String ctn;
        for (EntriesEntity entry : entries) {
            ctn = entry.getProduct().getCode();
            ShoppingCartData cartItem = new ShoppingCartData(entry, mCartData.getCarts().get(0).getDeliveryCost());
            cartItem.setVatInclusive(cartsEntity.isNet());
            Data data;
            if (list.containsKey(ctn)) {
                data = list.get(ctn).getData();
            } else {
                continue;
            }
            cartItem.setImageUrl(data.getImageURL());
            cartItem.setProductTitle(data.getProductTitle());
            cartItem.setCtnNumber(ctn);
            cartItem.setCartNumber(cartsEntity.getCode());
            cartItem.setQuantity(entry.getQuantity());
            cartItem.setFormatedPrice(entry.getBasePrice().getFormattedValue());
            cartItem.setValuePrice(String.valueOf(entry.getBasePrice().getValue()));
            cartItem.setTotalPriceWithTaxFormatedPrice(cartsEntity.getTotalPriceWithTax().getFormattedValue());
            cartItem.setTotalPriceFormatedPrice(entry.getTotalPrice().getFormattedValue());
            cartItem.setTotalItems(cartsEntity.getTotalItems());
            cartItem.setMarketingTextHeader(data.getMarketingTextHeader());
            cartItem.setDeliveryAddressEntity(cartsEntity.getDeliveryAddress());
            cartItem.setVatValue(cartsEntity.getTotalTax().getFormattedValue());
            cartItem.setDeliveryItemsQuantity(cartsEntity.getDeliveryItemsQuantity());
            //required for Tagging
            cartItem.setCategory(cartsEntity.getEntries().get(0).getProduct().getCategories().get(0).getCode());
            products.add(cartItem);
        }
        return products;
    }

    @Override
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

    @Override
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
                mLoadListener.onLoadListenerError((IAPNetworkError) msg.obj);
                Utility.dismissProgressDialog();
            }
        });
        sendHybrisRequest(0, model, model);
    }

    private void createCart(final Context context, final IAPCartListener iapHandlerListener,
                            final String ctnNumber, final ShoppingCartLauncher mShoppingCartLauncher,
                            final boolean isBuy) {
        HybrisDelegate delegate = HybrisDelegate.getInstance(context);
        CartCreateRequest model = new CartCreateRequest(delegate.getStore(), null, null);
        delegate.sendRequest(RequestCode.CREATE_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                if (isBuy) {
                    addProductToCart(context, ctnNumber, iapHandlerListener, mShoppingCartLauncher, true);
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

    @Override
    public void addProductToCart(final Context context, String productCTN, final IAPCartListener
            iapHandlerListener, final ShoppingCartLauncher mShoppingCartLauncher,
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
                    mShoppingCartLauncher.launchShoppingCart();
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

    @Override
    public void getProductCartCount(final Context context, final IAPCartListener
            iapHandlerListener, final ShoppingCartLauncher mShoppingCartLauncher) {
        HybrisDelegate delegate = HybrisDelegate.getInstance(context);
        CartCurrentInfoRequest model = new CartCurrentInfoRequest(delegate.getStore(), null, null);
        model.setContext(context);

        delegate.sendRequest(RequestCode.GET_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
                    createCart(context, iapHandlerListener, null, mShoppingCartLauncher, false);
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
                handleNoCartErrorOrNotifyError(msg, context, iapHandlerListener, null, mShoppingCartLauncher,
                        false);
            }
        });
    }

    @Override
    public void buyProduct(final Context context, final String ctnNumber, final IAPCartListener
            iapHandlerListener, final ShoppingCartLauncher mShoppingCartLauncher) {
        if (ctnNumber == null) return;
        HybrisDelegate delegate = HybrisDelegate.getInstance(context);
        CartCurrentInfoRequest model = new CartCurrentInfoRequest(delegate.getStore(), null, null);
        model.setContext(context);

        delegate.sendRequest(RequestCode.GET_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
                    createCart(context, iapHandlerListener, ctnNumber, mShoppingCartLauncher, true);
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
                                    mShoppingCartLauncher.launchShoppingCart();
                                    break;
                                }
                            }
                            if (!isProductAvailable)
                                addProductToCart(context, ctnNumber, iapHandlerListener, mShoppingCartLauncher,
                                        true);
                            if (iapHandlerListener != null) {
                                iapHandlerListener.onSuccess(0);
                            }
                        } else {
                            addProductToCart(context, ctnNumber, iapHandlerListener, mShoppingCartLauncher, true);
                        }
                    }
                }
            }

            @Override
            public void onError(final Message msg) {
                handleNoCartErrorOrNotifyError(msg, context, iapHandlerListener, ctnNumber, mShoppingCartLauncher,
                        true);
            }
        });
    }

    private void handleNoCartErrorOrNotifyError(final Message msg, final Context context,
                                                final IAPCartListener iapHandlerListener,
                                                final String ctnNumber,
                                                final ShoppingCartLauncher mShoppingCartLauncher,
                                                final boolean isBuy) {
        if (isNoCartError(msg)) {
            createCart(context, iapHandlerListener, ctnNumber, mShoppingCartLauncher, isBuy);
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

    @Override
    public void onModelDataLoadFinished(final Message msg) {
        if (processResponseFromHybrisForGetCart(msg)) return;
        if (processResponseFromPRX(msg)) return;
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

    private boolean processResponseFromPRX(final Message msg) {
        if (msg.obj instanceof HashMap) {
            HashMap<String, SummaryModel> prxModel = (HashMap<String, SummaryModel>) msg.obj;
            if (prxModel == null || prxModel.size() == 0) {
                EventHelper.getInstance().notifyEventOccurred(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED);
                Utility.dismissProgressDialog();
                return true;
            }
            notifyListChanged(prxModel);
        } else {
            EventHelper.getInstance().notifyEventOccurred(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED);
            dismissProgressDialog();
        }
        return false;
    }

    private boolean processResponseFromHybrisForGetCart(final Message msg) {
        if (msg.obj instanceof Carts) {
            mCartData = (Carts) msg.obj;
            if (mCartData != null && mCartData.getCarts().get(0).getEntries() != null) {
                makePrxCall(mCartData);
                return true;
            }
        }
        return false;
    }

    private void makePrxCall(final Carts mCarts) {
        ArrayList<String> ctnsToBeRequestedForPRX = new ArrayList<>();
        List<EntriesEntity> entries = mCarts.getCarts().get(0).getEntries();
        ArrayList<String> productsToBeShown = new ArrayList<>();
        String ctn;

        CartModelContainer cartModelContainer = CartModelContainer.getInstance();
        for (EntriesEntity entry : entries) {
            ctn = entry.getProduct().getCode();
            productsToBeShown.add(ctn);
            if (!cartModelContainer.isPRXDataPresent(ctn)) {
                ctnsToBeRequestedForPRX.add(entry.getProduct().getCode());
            }
        }
        if (ctnsToBeRequestedForPRX.size() > 0) {
            PRXDataBuilder builder = new PRXDataBuilder(mContext, ctnsToBeRequestedForPRX, this);
            builder.preparePRXDataRequest();
        } else {
            HashMap<String, SummaryModel> prxModel = new HashMap<>();
            for (String ctnPresent : productsToBeShown) {
                prxModel.put(ctnPresent, cartModelContainer.getProductData(ctnPresent));
            }
            notifyListChanged(prxModel);
        }
    }

}
