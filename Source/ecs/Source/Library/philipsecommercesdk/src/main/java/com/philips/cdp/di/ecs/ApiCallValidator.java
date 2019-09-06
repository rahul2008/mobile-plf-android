package com.philips.cdp.di.ecs;

import android.support.annotation.Nullable;

import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.error.ECSErrorEnum;
import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.util.ECSConfig;

import java.util.List;


public class ApiCallValidator {

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
            return new ECSError(100,"Site ID is not present");
        }

        if(isCategoryNull()){
            return new ECSError(101 ,"Category is not present");
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
            return new ECSError(100,"Invalid ctn list");
        }
        if(isLocaleNull()){
            return new ECSError(100,"Locale is not present");
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
            return new ECSError(9000,"Token can not be null or empty");
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getProductListAPIValidateError(int pageSize) {

        if(pageSize<=0){
            return new ECSError(9000,"Page size can not be zero or negative");
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getProductForAPIValidateError(String ctn) {

        if(isINValidString(ctn)){
            return new ECSError(9000,"CTN can not be null or empty");
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getProductDetailAPIValidateError(Product product) {
        if(isINValidString(product.getCode())){
            return new ECSError(9000,"Invalid product ,Product code can not be null or empty");
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getAddProductToShoppingCartError(Product product) {
        return getProductDetailAPIValidateError(product);
    }

    public ECSError getUpdateQuantityError(int quantity) {

        if(quantity<0){
            return new ECSError(9000,"Quantity can not be less than zero");
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getSetVoucherError(String voucherCode) {
        if(isINValidString(voucherCode)){
            return new ECSError(9000,"VoucherCode can not be null or empty");
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getVoucherError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getRemoveVoucherError(String voucherCode) {

        if(isINValidString(voucherCode)){
            return new ECSError(9000,"VoucherCode can not be null or empty");
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getDeliveryModesError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getSetDeliveryModeError(String deliveryModeID) {

        if(isINValidString(deliveryModeID)){
            return new ECSError(9000 ,"Delivery Mode ID can not be null or empty");
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
            return new ECSError(9000,"Invalid Address , Address ID  can not be null or empty");
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getUpdateAddressError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getSetDefaultAddressError(Addresses address) {
        if(isINValidString(address.getId())){
            return new ECSError(9000,"Invalid Address , Address ID  can not be null or empty");
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getDeleteAddressError(Addresses address) {
        if(isINValidString(address.getId())){
            return new ECSError(9000,"Invalid Address , Address ID  can not be null or empty");
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getPaymentsError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getSetPaymentMethodError(String paymentDetailsId) {

        if(isINValidString(paymentDetailsId)){
            return new ECSError(9000 ,"Invalid paymeent method , Payment ID  can not be null or empty");
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getRetailersError(String productID) {

        if(isINValidString(productID)){
            return new ECSError(9000,"Invalid product ,Product code can not be null or empty");
        }
        if(isLocaleNull()){
            return new ECSError(9000,"Locale is not present");
        }
        return null;
    }

    public ECSError getSubmitOrderError(String cvv) {

        if(isINValidString(cvv)){
            return new ECSError(9000 ,"Invalid cvv, it can not be null or empty");
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getMakePaymentError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getOrderHistoryError(int pageNumber) {
        if(pageNumber<0){
            return new ECSError(9000,"PageNumber can not be less than the zero");
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getOrderDetailError(String orderId) {

        if(isINValidString(orderId)){
            return new ECSError(9000 ,"Invalid order id, it can not be null or empty");
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getUserProfileError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }
}
