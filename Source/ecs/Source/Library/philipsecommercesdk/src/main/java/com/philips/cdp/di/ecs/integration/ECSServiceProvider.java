package com.philips.cdp.di.ecs.integration;


import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.address.GetDeliveryModes;
import com.philips.cdp.di.ecs.model.address.GetShippingAddressData;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.cart.EntriesEntity;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.region.RegionsList;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.model.voucher.GetAppliedValue;

import java.util.List;

/**
 * The interface Iap services.
 */
public interface ECSServiceProvider {


   /* *//**
     * Hybris oath authentication, Janrain basic token is used to obtain Hybris oath token and save it within IAPSDKService and return true if success.
     *
     * @param oauthData      the oauth data (Janrain token details)
     * @param ECSCallback the iapsdk callback success block containing boolean
     *//*
    public void  hybrisOathAuthentication(Map<String, String> oauthData, ECSCallback ECSCallback);
*/

    /**
     * Gets iap config data including catalogId, rootCategory, net , siteId, faqUrl, helpDeskEmail, helpDeskPhone and helpUrl
     *
     * @param eCSCallback the iapsdk callback success block containing IAPConfiguration object
     */
     //public void getECSConfig(ECSCallback<HybrisConfigResponse, Exception> eCSCallback);


    /**
     * Gets product list along with product summary detail.
     *
     * @param ECSCallback the iapsdk callback success block containing Products object (a list of ProductsEntity and other fields)
     *//*
    public void getProductList(ECSCallback ECSCallback);


    *//**
     * Gets product detail containing assets and disclaimer details
     *
     * @param eCSCallback the iapsdk callback success block containing AssetModel and DisclaimerModel
     */
    void getProductList(int currentPage, int pageSize, ECSCallback<Products,Exception> eCSCallback);

    void getProductFor(String ctn,ECSCallback<Product,Exception> eCSCallback );


    void InvalidateECS(ECSCallback<Boolean, Exception> eCSCallback);


    void getProductDetail(Product product, ECSCallback<Product,Exception> ecsCallback);

    void configureECS(ECSCallback<Boolean,Exception> ecsCallback);

    void getECSConfig(ECSCallback<HybrisConfigResponse, Exception> ecsCallback);

    void getProductSummary(List<String> ctns, ECSCallback<List<Product>,Exception> ecsCallback);

    void getShoppingCart(ECSCallback<ECSShoppingCart,Exception> ecsCallback);

    void createShoppingCart(ECSCallback<ECSShoppingCart,Exception> ecsCallback);

    void addProductToShoppingCart(Product product, ECSCallback<ECSShoppingCart, Exception> ecsCallback);

    void updateQuantity(int quantity, EntriesEntity entriesEntity, ECSCallback<ECSShoppingCart, Exception> ecsCallback) ;

    //voucher
    void setVoucher(String voucherCode, ECSCallback<GetAppliedValue,Exception> ecsCallback);

    void getVoucher(ECSCallback<GetAppliedValue,Exception> ecsCallback);

    void removeVoucher(String voucherCode, ECSCallback<GetAppliedValue,Exception> ecsCallback);

    void getDeliveryModes(ECSCallback<GetDeliveryModes,Exception> ecsCallback);

    void setDeliveryMode(String deliveryModeID,ECSCallback<GetDeliveryModes,Exception> ecsCallback);

    void getRegions(ECSCallback<RegionsList, Exception> ecsCallback);

    void getListSavedAddress(ECSCallback<GetShippingAddressData, Exception> ecsCallback);

    void setDeliveryAddress(Addresses address,ECSCallback<Boolean, Exception> ecsCallback);
}
