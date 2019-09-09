package com.philips.cdp.di.ecs;

import android.support.annotation.VisibleForTesting;

import com.philips.cdp.di.ecs.error.ECSErrorWrapper;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.address.GetDeliveryModes;
import com.philips.cdp.di.ecs.model.address.GetShippingAddressData;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.cart.EntriesEntity;
import com.philips.cdp.di.ecs.model.config.HybrisConfigResponse;
import com.philips.cdp.di.ecs.model.oauth.OAuthResponse;
import com.philips.cdp.di.ecs.model.order.Orders;
import com.philips.cdp.di.ecs.model.order.OrdersData;
import com.philips.cdp.di.ecs.model.orders.OrderDetail;
import com.philips.cdp.di.ecs.model.payment.MakePaymentData;
import com.philips.cdp.di.ecs.model.payment.PaymentMethods;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.region.RegionsList;
import com.philips.cdp.di.ecs.model.retailers.WebResults;
import com.philips.cdp.di.ecs.model.user.UserProfile;
import com.philips.cdp.di.ecs.model.voucher.GetAppliedValue;

import java.util.List;

public class ECSCallValidator {

    ECSManager ecsManager;

    @VisibleForTesting
    ECSManager getECSManager() {
        return ecsManager;
    }

    ECSCallValidator() {
        ecsManager = new ECSManager();
    }

    public void getECSConfig(ECSCallback<HybrisConfigResponse, Exception> ecsCallback) {
        ECSErrorWrapper configAPIValidateError = new ApiInputValidator().getConfigAPIValidateError();
        if (configAPIValidateError == null) {
            new ECSManager().getHybrisConfigResponse(ecsCallback);
        } else {
            ecsCallback.onFailure(configAPIValidateError.getException(), configAPIValidateError.getEcsError());
        }
    }

    public void getProductSummary(List<String> ctns, ECSCallback<List<Product>, Exception> ecsCallback) {

        ECSErrorWrapper configAPIValidateError = new ApiInputValidator().getProductSummaryAPIValidateError(ctns);
        if (configAPIValidateError == null) {
            ecsManager.getSummary(ctns,ecsCallback);
        } else {
            ecsCallback.onFailure(configAPIValidateError.getException(), configAPIValidateError.getEcsError());
        }
    }

    public void getECSShoppingCart(ECSCallback<ECSShoppingCart, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getECSShoppingCartAPIValidateError();
        if (ecsErrorWrapper == null) {
            ecsManager.getECSShoppingCart(ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void createECSShoppingCart(ECSCallback<ECSShoppingCart, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getCreateShoppingCartAPIValidateError();
        if (ecsErrorWrapper == null) {
            ecsManager.createECSShoppingCart(ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void getOAuth(OAuthInput oAuthInput, ECSCallback<OAuthResponse, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getHybrisOathAuthenticationAPIValidateError(oAuthInput);
        if (ecsErrorWrapper == null) {
            ecsManager.getOAuth(oAuthInput,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void getProductList(int currentPage, int pageSize, ECSCallback<Products, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getProductListAPIValidateError();
        if (ecsErrorWrapper == null) {
            ecsManager.getProductList(currentPage,pageSize,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void getProductFor(String ctn, ECSCallback<Product, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getProductForAPIValidateError(ctn);
        if (ecsErrorWrapper == null) {
            ecsManager.getProductFor(ctn,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void getProductDetail(Product product, ECSCallback<Product, Exception> ecsCallback) {
        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getProductDetailAPIValidateError(product);
        if (ecsErrorWrapper == null) {
            ecsManager.getProductDetail(product,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void addProductToShoppingCart(Product product, ECSCallback<ECSShoppingCart, Exception> ecsCallback) {
        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getAddProductToShoppingCartError(product);
        if (ecsErrorWrapper == null) {
            ecsManager.addProductToShoppingCart(product,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void updateQuantity(int quantity, EntriesEntity entriesEntity, ECSCallback<ECSShoppingCart, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getUpdateQuantityError(quantity);
        if (ecsErrorWrapper == null) {
            ecsManager.updateQuantity(quantity,entriesEntity,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void setVoucher(String voucherCode, ECSCallback<GetAppliedValue, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getSetVoucherError(voucherCode);
        if (ecsErrorWrapper == null) {
            ecsManager.setVoucher(voucherCode,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }

    }

    public void getVoucher(ECSCallback<GetAppliedValue, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getVoucherError();
        if (ecsErrorWrapper == null) {
            ecsManager.getVoucher(ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void removeVoucher(String voucherCode, ECSCallback<GetAppliedValue, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getRemoveVoucherError(voucherCode);
        if (ecsErrorWrapper == null) {
            ecsManager.removeVoucher(voucherCode,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void getDeliveryModes(ECSCallback<GetDeliveryModes, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getDeliveryModesError();
        if (ecsErrorWrapper == null) {
            ecsManager.getDeliveryModes(ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void setDeliveryMode(String deliveryModeID, ECSCallback<Boolean, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getSetDeliveryModeError(deliveryModeID);
        if (ecsErrorWrapper == null) {
            ecsManager.setDeliveryMode(deliveryModeID,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }

    }

    public void getRegions(ECSCallback<RegionsList, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getRegionsError();
        if (ecsErrorWrapper == null) {
            ecsManager.getRegions(ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void getListSavedAddress(ECSCallback<GetShippingAddressData, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getListSavedAddressError();
        if (ecsErrorWrapper == null) {
            ecsManager.getListSavedAddress(ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void createNewAddress(Addresses ecsAddress, ECSCallback<GetShippingAddressData, Exception> ecsCallback) {

        ECSErrorWrapper hybrisOathAuthenticationAPIValidateError = new ApiInputValidator().getCreateNewAddressError(ecsAddress);

        if (hybrisOathAuthenticationAPIValidateError == null) {
            ecsManager.createNewAddress(ecsAddress,ecsCallback);
        } else {
            ecsCallback.onFailure(hybrisOathAuthenticationAPIValidateError.getException(), hybrisOathAuthenticationAPIValidateError.getEcsError());
        }
    }

    public void createNewAddress(Addresses address, ECSCallback<Addresses, Exception> ecsCallback, boolean b) {

        ECSErrorWrapper hybrisOathAuthenticationAPIValidateError = new ApiInputValidator().getCreateNewAddressError(address);

        if (hybrisOathAuthenticationAPIValidateError == null) {
            ecsManager.createNewAddress(address,ecsCallback,b);
        } else {
            ecsCallback.onFailure(hybrisOathAuthenticationAPIValidateError.getException(), hybrisOathAuthenticationAPIValidateError.getEcsError());
        }
    }

    public void setDeliveryAddress(Addresses address, ECSCallback<Boolean, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getSetDeliveryAddressError(address);

        if (ecsErrorWrapper == null) {
            ecsManager.setDeliveryAddress(address,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void updateAddress(Addresses address, ECSCallback<Boolean, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getUpdateAddressError(address);
        if (ecsErrorWrapper == null) {
            ecsManager.setDeliveryAddress(address,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void deleteAddress(Addresses address, ECSCallback<GetShippingAddressData, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getDeleteAddressError(address);
        if (ecsErrorWrapper == null) {
            ecsManager.deleteAddress(address,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void getPayments(ECSCallback<PaymentMethods, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getPaymentsError();
        if (ecsErrorWrapper == null) {
            ecsManager.getPayments(ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void setPaymentMethod(String paymentDetailsId, ECSCallback<Boolean, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getSetPaymentMethodError(paymentDetailsId);
        if (ecsErrorWrapper == null) {
            ecsManager.setPaymentMethod(paymentDetailsId,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void getRetailers(String productID, ECSCallback<WebResults, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getRetailersError(productID);
        if (ecsErrorWrapper == null) {
            ecsManager.getRetailers(productID,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void submitOrder(String cvv, ECSCallback<OrderDetail, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getSubmitOrderError(cvv);
        if (ecsErrorWrapper == null) {
            ecsManager.submitOrder(cvv,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void makePayment(OrderDetail orderDetail, Addresses billingAddress, ECSCallback<MakePaymentData, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getMakePaymentError(orderDetail,billingAddress);
        if (ecsErrorWrapper == null) {
            ecsManager.makePayment(orderDetail,billingAddress,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void getOrderHistory(int pageNumber, ECSCallback<OrdersData, Exception> ecsCallback) {

        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getOrderHistoryError(pageNumber);
        if (ecsErrorWrapper == null) {
            ecsManager.getOrderHistory(pageNumber,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void getOrderDetail(String orderId, ECSCallback<OrderDetail, Exception> ecsCallback) {
        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getOrderDetailError(orderId);

        if (ecsErrorWrapper == null) {
            ecsManager.getOrderDetail(orderId,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void getOrderDetail(Orders orders, ECSCallback<Orders, Exception> ecsCallback) {
        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getOrderDetailError(orders.getCode());

        if (ecsErrorWrapper == null) {
            ecsManager.getOrderDetail(orders,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void getUserProfile(ECSCallback<UserProfile, Exception> ecsCallback) {
        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getUserProfileError();

        if (ecsErrorWrapper == null) {
            ecsManager.getUserProfile(ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void refreshAuth(OAuthInput oAuthInput, ECSCallback<OAuthResponse, Exception> ecsCallback) {
        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getHybrisOathAuthenticationAPIValidateError(oAuthInput);

        if (ecsErrorWrapper == null) {
            ecsManager.refreshAuth(oAuthInput,ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }

    public void getHybrisConfig(ECSCallback<Boolean, Exception> ecsCallback) {
        ECSErrorWrapper ecsErrorWrapper = new ApiInputValidator().getConfigAPIValidateError();

        if (ecsErrorWrapper == null) {
            ecsManager.getHybrisConfig(ecsCallback);
        } else {
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }
}
