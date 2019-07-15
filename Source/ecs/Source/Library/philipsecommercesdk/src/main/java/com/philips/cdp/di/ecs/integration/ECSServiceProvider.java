package com.philips.cdp.di.ecs.integration;


import com.philips.cdp.di.ecs.model.asset.Assets;
import com.philips.cdp.di.ecs.model.disclaimer.Disclaimers;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;

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


    void InvalidateECS(ECSCallback<Boolean,Exception> eCSCallback);


    void getProductDetail(Product product, ECSCallback<Product,Exception> ecsCallback);
}
