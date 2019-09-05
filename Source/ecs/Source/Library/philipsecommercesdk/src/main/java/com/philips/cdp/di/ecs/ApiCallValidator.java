package com.philips.cdp.di.ecs;

import android.support.annotation.Nullable;

import com.philips.cdp.di.ecs.constants.ModelConstants;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.OAuthInput;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.cart.EntriesEntity;
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
            return new ECSError(new Exception("Locale is not present"),100);
        }

        if(isBaseURLNull()){
            return new ECSError(new Exception("Base URL is not present"),101);
        }
        return null;
    }

    @Nullable
    private ECSError checkSiteIDAndCategory() {
        if(isSiteIDNull()){
            return new ECSError(new Exception("Site ID is not present"),100);
        }

        if(isCategoryNull()){
            return new ECSError(new Exception("Category is not present"),101);
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
            return new ECSError(new Exception("Invalid ctn list"),100);
        }
        if(isLocaleNull()){
            return new ECSError(new Exception("Locale is not present"),100);
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
            return new ECSError(new Exception("Token can not be null or empty"),9000);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getProductListAPIValidateError(int pageSize) {

        if(pageSize<=0){
            return new ECSError(new Exception("Page size can not be zero or negative"),9000);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getProductForAPIValidateError(String ctn) {

        if(isINValidString(ctn)){
            return new ECSError(new Exception("CTN can not be null or empty"),9000);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getProductDetailAPIValidateError(Product product) {
        if(isINValidString(product.getCode())){
            return new ECSError(new Exception("Invalid product ,Product code can not be null or empty"),9000);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getAddProductToShoppingCartError(Product product) {
        return getProductDetailAPIValidateError(product);
    }

    public ECSError getUpdateQuantityError(int quantity) {

        if(quantity<0){
            return new ECSError(new Exception("Quantity can not be less than zero"),9000);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getSetVoucherError(String voucherCode) {
        if(isINValidString(voucherCode)){
            return new ECSError(new Exception("VoucherCode can not be null or empty"),9000);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getVoucherError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getRemoveVoucherError(String voucherCode) {

        if(isINValidString(voucherCode)){
            return new ECSError(new Exception("VoucherCode can not be null or empty"),9000);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getDeliveryModesError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getSetDeliveryModeError(String deliveryModeID) {

        if(isINValidString(deliveryModeID)){
            return new ECSError(new Exception("Delivery Mode ID can not be null or empty"),9000);
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
            return new ECSError(new Exception("Invalid Address , Address ID  can not be null or empty"),9000);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getUpdateAddressError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getSetDefaultAddressError(Addresses address) {
        if(isINValidString(address.getId())){
            return new ECSError(new Exception("Invalid Address , Address ID  can not be null or empty"),9000);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getDeleteAddressError(Addresses address) {
        if(isINValidString(address.getId())){
            return new ECSError(new Exception("Invalid Address , Address ID  can not be null or empty"),9000);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getPaymentsError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getSetPaymentMethodError(String paymentDetailsId) {

        if(isINValidString(paymentDetailsId)){
            return new ECSError(new Exception("Invalid paymeent method , Payment ID  can not be null or empty"),9000);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getRetailersError(String productID) {

        if(isINValidString(productID)){
            return new ECSError(new Exception("Invalid product ,Product code can not be null or empty"),9000);
        }
        if(isLocaleNull()){
            return new ECSError(new Exception("Locale is not present"),100);
        }
        return null;
    }

    public ECSError getSubmitOrderError(String cvv) {

        if(isINValidString(cvv)){
            return new ECSError(new Exception("Invalid cvv, it can not be null or empty"),9000);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getMakePaymentError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getOrderHistoryError(int pageNumber) {
        if(pageNumber<0){
            return new ECSError(new Exception("PageNumber can not be less than the zero"),9000);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getOrderDetailError(String orderId) {

        if(isINValidString(orderId)){
            return new ECSError(new Exception("Invalid order id, it can not be null or empty"),9000);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSError getUserProfileError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }
}
