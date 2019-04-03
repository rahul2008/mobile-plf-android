package com.philips.cdp.di.iap.integration;

import org.json.JSONObject;

/**
 * Created by philips on 4/1/19.
 */

public interface IAPMockInterface {


     boolean isMockEnabled();

     JSONObject GetMockJson(String fileName);

     JSONObject GetProductCatalogResponse();

     JSONObject CartAddProductResponse();

     JSONObject CartCreateResponse();

     JSONObject CartDeleteProductResponse();

     JSONObject CartUpdateProductQuantityResponse();

     JSONObject ContactCallResponse();

     JSONObject CreateAddressResponse();

     JSONObject DeleteAddressResponse();

     JSONObject DeleteCartResponse();

     JSONObject DeleteVoucherResponse();

     JSONObject GetAddressResponse();

     JSONObject GetAppliedVoucherResponse();

     JSONObject GetApplyVoucherResponse();

     JSONObject GetCartsResponse();

     JSONObject GetCurrentCartResponse();

     JSONObject GetDeliveryModesResponse();

     JSONObject GetPaymentDetailResponse();

     JSONObject GetRegionsResponse();

     JSONObject GetUserResponse();

     JSONObject OAuthResponse();

     JSONObject PaymentResponse();

     JSONObject ProductDetailResponse();

     JSONObject RefreshOAuthResponse();

     JSONObject SetDeliveryAddressModeResponse();

     JSONObject SetDeliveryAddressResponse();

     JSONObject SetPaymentDetailsResponse();

     JSONObject UpdateAddressResponse();

     JSONObject GetRetailersInfoResponse();
}
