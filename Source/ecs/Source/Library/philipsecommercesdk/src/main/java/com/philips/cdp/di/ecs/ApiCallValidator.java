package com.philips.cdp.di.ecs;

import android.support.annotation.Nullable;

import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.error.ECSErrorEnum;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.config.HybrisConfigResponse;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.util.ECSConfig;

import java.util.List;


public class ApiCallValidator {

    ECSManager mECSManager;

    public ApiCallValidator(ECSManager mECSManager) {
        this.mECSManager = mECSManager;
    }

    public ApiCallValidator() {

    }

    ECSError getConfigAPIValidateError(){
        ECSError ecsError = checkLocaleAndBaseURL();
        if (ecsError != null) return ecsError;
        return null;
    }

    @Nullable
    private ECSError checkLocaleAndBaseURL() {
        if(isLocaleNull()){
            return new ECSError(ECSErrorEnum.ECSLocaleNotFound.getErrorCode(), ECSErrorEnum.ECSLocaleNotFound.getLocalizedErrorString());
        }

        if(isBaseURLNull()){
            return new ECSError(ECSErrorEnum.ECSBaseURLNotFound.getErrorCode() , ECSErrorEnum.ECSBaseURLNotFound.getLocalizedErrorString());
        }
        return null;
    }

    @Nullable
    private ECSError checkSiteIDAndCategory() {
        if(isSiteIDNull()){
            return new ECSError(ECSErrorEnum.ECSSiteIdNotFound.getErrorCode(),ECSErrorEnum.ECSSiteIdNotFound.getLocalizedErrorString());
        }

        if(isCategoryNull()){
            return new ECSError(ECSErrorEnum.ECSSiteIdNotFound.getErrorCode() ,ECSErrorEnum.ECSSiteIdNotFound.getLocalizedErrorString());
        }
        return null;
    }

    private boolean isAppInfraNull(){
        return ECSConfig.INSTANCE.getAppInfra() == null;
    }

    private boolean isLocaleNull(){
       return ECSConfig.INSTANCE.getLocale() == null;
    }

    private boolean isBaseURLNull(){
        return ECSConfig.INSTANCE.getBaseURL() == null;
    }

    private boolean isCategoryNull(){
        return ECSConfig.INSTANCE.getRootCategory() == null;
    }

    private boolean isSiteIDNull(){
        return ECSConfig.INSTANCE.getSiteId() == null;
    }

    private boolean isINValidString(String inputString){
        return inputString==null && inputString.isEmpty();
    }

    private boolean isINValidList(List list){
        return list==null && list.isEmpty();
    }

    public ECSError getProductSummaryAPIValidateError(List<String> ctns) {

        if(isINValidList(ctns)){
            return new ECSError(ECSErrorEnum.ECSCtnNotProvided.getErrorCode(),ECSErrorEnum.ECSCtnNotProvided.getLocalizedErrorString());
        }
        if(isLocaleNull()){
            return new ECSError(ECSErrorEnum.ECSLocaleNotFound.getErrorCode(), ECSErrorEnum.ECSLocaleNotFound.getLocalizedErrorString());
        }
        return null;
    }

    public ECSError getECSShoppingCartAPIValidateError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    private ECSError checkLocaleBaseURLSiteIDAndCategory() {
        ECSError ecsError =  checkLocaleAndBaseURL();
        if(ecsError!=null){
            return ecsError;
        }

        ECSError ecsError1 = checkSiteIDAndCategory();
        if(ecsError1!=null){
            return ecsError1;
        }
        return null;
    }

    public ECSError getCreateShoppingCartAPIValidateError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getHybrisOathAuthenticationAPIValidateError(OAuthInput oAuthInput) {
        if(isINValidString(oAuthInput.getOAuthID())){
            return new ECSError(ECSErrorEnum.ECSOAuthDetailError.getErrorCode(),ECSErrorEnum.ECSOAuthDetailError.getLocalizedErrorString());
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getProductListAPIValidateError(int pageSize) {

        if(pageSize<=0){
            return new ECSError(ECSErrorEnum.ECSInvalidPageSizeError.getErrorCode(),ECSErrorEnum.ECSInvalidPageSizeError.getLocalizedErrorString());
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getProductForAPIValidateError(String ctn) {

        if(isINValidString(ctn)){
            return new ECSError(ECSErrorEnum.ECSCtnNotProvided.getErrorCode(),ECSErrorEnum.ECSCtnNotProvided.getLocalizedErrorString());
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getProductDetailAPIValidateError(Product product) {
        if(isINValidString(product.getCode())){
            return new ECSError(ECSErrorEnum.ECSInvalidProductError.getErrorCode(),ECSErrorEnum.ECSInvalidProductError.getLocalizedErrorString());
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getAddProductToShoppingCartError(Product product) {
        return getProductDetailAPIValidateError(product);
    }

    public ECSError getUpdateQuantityError(int quantity) {

        if(quantity<0){
            return new ECSError(ECSErrorEnum.ECSCommerceCartModificationError.getErrorCode(),ECSErrorEnum.ECSCommerceCartModificationError.getLocalizedErrorString());
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getSetVoucherError(String voucherCode) {
        if(isINValidString(voucherCode)){
            return new ECSError(ECSErrorEnum.ECSUnsupportedVoucherError.getErrorCode(),ECSErrorEnum.ECSUnsupportedVoucherError.getLocalizedErrorString());
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getVoucherError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getRemoveVoucherError(String voucherCode) {
        return getSetVoucherError(voucherCode);
    }

    public ECSError getDeliveryModesError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getSetDeliveryModeError(String deliveryModeID) {

        if(isINValidString(deliveryModeID)){
            return new ECSError(ECSErrorEnum.ECSUnsupportedDeliveryModeError.getErrorCode() ,ECSErrorEnum.ECSUnsupportedDeliveryModeError.getLocalizedErrorString());
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getRegionsError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getListSavedAddressError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getCreateNewAddressError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getSetDeliveryAddressError(Addresses address) {
        if(isINValidString(address.getId())){
            return new ECSError(ECSErrorEnum.ECSInvalidAddressError.getErrorCode(),ECSErrorEnum.ECSInvalidAddressError.getLocalizedErrorString());
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getUpdateAddressError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getSetDefaultAddressError(Addresses address) {
       return getSetDeliveryAddressError(address);
    }

    public ECSError getDeleteAddressError(Addresses address) {
       return getSetDeliveryAddressError(address);
    }

    public ECSError getPaymentsError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getSetPaymentMethodError(String paymentDetailsId) {

        if(isINValidString(paymentDetailsId)){
            return new ECSError(ECSErrorEnum.ECSInvalidPaymentInfoError.getErrorCode() ,ECSErrorEnum.ECSInvalidPaymentInfoError.getLocalizedErrorString());
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getRetailersError(String productID) {

        if(isINValidString(productID)){
            return new ECSError(ECSErrorEnum.ECSInvalidProductError.getErrorCode(),ECSErrorEnum.ECSInvalidProductError.getLocalizedErrorString());
        }
        if(isLocaleNull()){
            return new ECSError(ECSErrorEnum.ECSLocaleNotFound.getErrorCode(), ECSErrorEnum.ECSLocaleNotFound.getLocalizedErrorString());
        }
        return null;
    }

    public ECSError getSubmitOrderError(String cvv) {

        if(isINValidString(cvv)){
            return new ECSError(ECSErrorEnum.ECSInvalidCvvError.getErrorCode() ,ECSErrorEnum.ECSInvalidCvvError.getLocalizedErrorString());
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getMakePaymentError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getOrderHistoryError(int pageNumber) {
        if(pageNumber<0){
            return new ECSError(ECSErrorEnum.ECSInvalidPageSizeError.getErrorCode(),ECSErrorEnum.ECSInvalidPageSizeError.getLocalizedErrorString());
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getOrderDetailError(String orderId) {

        if(isINValidString(orderId)){
            return new ECSError(ECSErrorEnum.ECSorderIdNil.getErrorCode() ,ECSErrorEnum.ECSorderIdNil.getLocalizedErrorString());
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getUserProfileError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public void getECSConfig(ECSCallback<HybrisConfigResponse, Exception> ecsCallback) {
        ECSError configAPIValidateError = getConfigAPIValidateError();
        if(configAPIValidateError == null){
            mECSManager.getHybrisConfigResponse(ecsCallback);
        }else{
            ecsCallback.onFailure(new Exception("dchd"),configAPIValidateError);
        }
    }
}
