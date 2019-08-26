/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.cart;

import android.content.Context;
import android.os.Message;
import android.util.Log;


import com.ecs.demouapp.ui.container.CartModelContainer;
import com.ecs.demouapp.ui.controller.AddressController;
import com.ecs.demouapp.ui.response.error.Error;
import com.ecs.demouapp.ui.response.error.ServerError;
import com.ecs.demouapp.ui.session.IAPNetworkError;
import com.ecs.demouapp.ui.utils.ECSUtility;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.DeliveryModes;
import com.philips.cdp.di.ecs.model.address.GetDeliveryModes;
import com.philips.cdp.di.ecs.model.address.GetUser;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.cart.EntriesEntity;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.error.ECSErrors;

import java.util.List;


public class ShoppingCartPresenter extends AbstractShoppingCartPresenter
        implements AddressController.AddressListener {

    private AddressController mAddressController;



    public ShoppingCartPresenter() {
    }

    public ShoppingCartPresenter(Context context, ShoppingCartListener<?> listener) {
        super(context, listener);
        mAddressController = new AddressController(context, ShoppingCartPresenter.this);
    }


    @Override
    public void getCurrentCartDetails() {

        ECSUtility.getInstance().getEcsServices().getShoppingCart(new ECSCallback<ECSShoppingCart, Exception>() {
            @Override
            public void onResponse(ECSShoppingCart result) {
                mLoadListener.onLoadFinished(result);
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                Message message = new Message();
                message.obj = "Could not fetch data";
                mLoadListener.onLoadError(message);
            }
        });
    }

    @Override
    public void deleteProduct(final ShoppingCartData summary) {
    }

    @Override
    public void deleteProduct(EntriesEntity entriesEntity) {
        ECSUtility.getInstance().getEcsServices().updateQuantity(0, entriesEntity, new ECSCallback<ECSShoppingCart, Exception>() {
            @Override
            public void onResponse(ECSShoppingCart result) {
                mLoadListener.onLoadFinished(result);
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                Message message = new Message();
                message.obj = "Could not fetch data";
                mLoadListener.onLoadError(message);
            }
        });

    }

    @Override
    public void updateProductQuantity(EntriesEntity entriesEntity, int count) {

        ECSUtility.getInstance().getEcsServices().updateQuantity(count, entriesEntity, new ECSCallback<ECSShoppingCart, Exception>() {
            @Override
            public void onResponse(ECSShoppingCart result) {
                mLoadListener.onLoadFinished(result);
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                Message message = new Message();
                message.obj = "Could not fetch data";
                mLoadListener.onLoadError(message);
            }
        });
    }

    @Override
    public void updateProductQuantity(final ShoppingCartData data, final int count, final int quantityStatus) {

    }



    @Override
    public void addProductToCart(final Context context, String productCTN, final ECSCartListener
            iapHandlerListener, final boolean isFromBuyNow) {

    }

    @Override
    public void getProductCartCount(final Context context, final ECSCartListener
            iapCartListener) {

        ECSUtility.getInstance().getEcsServices().getShoppingCart(new ECSCallback<ECSShoppingCart, Exception>() {
            @Override
            public void onResponse(ECSShoppingCart carts) {

                 // You can check if cart is not there and call createCart here
                if (carts != null) {
                    iapCartListener.onSuccess(carts);
                } else {
                    Message message = new Message();
                    message.obj = "Error Fetching Cart";
                    iapCartListener.onFailure(message);
                }


            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                Message message = new Message();
                message.obj = "Error Fetching Cart";
                iapCartListener.onFailure(message);
            }
        });

    }

    @Override
    public void addProductToCart(Product product, final ECSCartListener
            iapHandlerListener) {

        ECSUtility.getInstance().getEcsServices().addProductToShoppingCart(product, new ECSCallback<ECSShoppingCart, Exception>() {
            @Override
            public void onResponse(ECSShoppingCart result) {
                iapHandlerListener.onSuccess(result);
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                Message message = new Message();
                message.obj = error.getMessage();
                if(null!=error.getMessage()){
                    message.obj = error.getMessage();
                }

                iapHandlerListener.onFailure(message);
            }
        });

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

    public boolean isValidPromotion(String code) {
        return code.toLowerCase().contains("free") && code.toLowerCase().contains("ship");
    }

    @Override
    public void onGetRegions(Message msg) {
        //NOP
    }

    @Override
    public void onGetUser(Message msg) {
        if (msg.obj instanceof IAPNetworkError) {
            return;
        } else if (msg.obj instanceof GetUser) {
            final GetUser user = (GetUser) msg.obj;
            if (user.getDefaultAddress() != null) {
                 //TODO
                //mAddressController.setDeliveryAddress(user.getDefaultAddress());
            } else {
                return;
            }
        }
    }

    @Override
    public void onCreateAddress(Message msg) {
        //NOP
    }

    @Override
    public void onGetAddress(Message msg) {
        //NOP
        Log.d("pabitra", "I am heere");
    }

    @Override
    public void onSetDeliveryAddress(Message msg) {
    }

    @Override
    public void onGetDeliveryModes(Message msg) {
        if ((msg.obj instanceof IAPNetworkError)) {
            return;
        }else if(msg.obj instanceof Exception){
            Exception exception = (Exception) msg.obj;
            ECSErrors.showECSToast(mContext,exception.getMessage());
        } if ((msg.obj instanceof GetDeliveryModes)) {
            final GetDeliveryModes deliveryModes = (GetDeliveryModes) msg.obj;
            final List<DeliveryModes> deliveryModeList = deliveryModes.getDeliveryModes();
            CartModelContainer.getInstance().setDeliveryModes(deliveryModeList);
            if (deliveryModeList.size() > 0) {
                mAddressController.setDeliveryMode(deliveryModeList.get(0).getCode());
            }
        }
    }

    @Override
    public void onSetDeliveryMode(Message msg) {
        return;
    }


}
