package com.philips.cdp.di.ecs.integration;

import java.util.Map;


/**
 * The interface Iap services.
 */
public interface ECSServiceProvider {


   /* *//**
     * Hybris oath authentication, Janrain basic token is used to obtain Hybris oath token and save it within IAPSDKService and return true if success.
     *
     * @param oauthData      the oauth data (Janrain token details)
     * @param ECSSDKCallback the iapsdk callback success block containing boolean
     *//*
    public void  hybrisOathAuthentication(Map<String, String> oauthData, ECSSDKCallback ECSSDKCallback);
*/

    /**
     * Gets iap config data including catalogId, rootCategory, net , siteId, faqUrl, helpDeskEmail, helpDeskPhone and helpUrl
     *
     * @param propositionId  the proposition id
     * @param locale         the locale
     * @param ECSSDKCallback the iapsdk callback success block containing IAPConfiguration object
     */
    public void getIAPConfig(String propositionId, String locale, ECSSDKCallback ECSSDKCallback);


    /**
     * Gets product list along with product summary detail.
     *
     * @param ECSSDKCallback the iapsdk callback success block containing Products object (a list of ProductsEntity and other fields)
     *//*
    public void getProductList(ECSSDKCallback ECSSDKCallback);


    *//**
     * Gets product detail containing assets and disclaimer details
     *
     * @param ECSSDKCallback the iapsdk callback success block containing AssetModel and DisclaimerModel
     *//*
    public void getProductDetail(ECSSDKCallback ECSSDKCallback);


    *//**
     * Invalidate IAPSDKService and reset data. To use further more IAPSDKService initialize method should be called.
     *
     * @param ECSSDKCallback the iapsdk callback success block containing boolean
     *//*
    public void InvalidateIAPSDK(ECSSDKCallback ECSSDKCallback);*/
}
