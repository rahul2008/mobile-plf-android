package com.philips.cdp.di.ecs;

import android.support.annotation.Nullable;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.error.ECSErrorEnum;
import com.philips.cdp.di.ecs.error.ECSErrorWrapper;
import com.philips.cdp.di.ecs.integration.ECSOAuthProvider;
import com.philips.cdp.di.ecs.model.address.ECSAddress;
import com.philips.cdp.di.ecs.model.orders.ECSOrderDetail;
import com.philips.cdp.di.ecs.model.products.ECSProduct;
import com.philips.cdp.di.ecs.util.ECSConfiguration;

import java.util.List;


public class ApiInputValidator {

    ECSManager mECSManager;

    public ApiInputValidator(ECSManager mECSManager) {
        this.mECSManager = mECSManager;
    }

    public ApiInputValidator() {

    }

    ECSErrorWrapper getConfigAPIValidateError(){
        return checkLocaleAndBaseURL();
    }

    @Nullable
    private ECSErrorWrapper checkLocaleAndBaseURL() {
        if(isLocaleNull()){
            return getECSErrorWrapper(ECSErrorEnum.ECSLocaleNotFound);
        }
        if(isBaseURLNull()){
            return getECSErrorWrapper(ECSErrorEnum.ECSBaseURLNotFound);
        }
        return null;
    }

    private ECSErrorWrapper getECSErrorWrapper(ECSErrorEnum ecsErrorEnum) {
        return new ECSErrorWrapper(new Exception(ecsErrorEnum.getLocalizedErrorString()),new ECSError(ecsErrorEnum.getErrorCode(),ecsErrorEnum.toString()));
    }

    @Nullable
    private ECSErrorWrapper checkSiteIDAndCategory() {
        if(isSiteIDNull() || isCategoryNull()){
            return getECSErrorWrapper(ECSErrorEnum.ECSSiteIdNotFound);
        }
        return null;
    }

    private boolean isLocaleNull(){
       return ECSConfiguration.INSTANCE.getLocale() == null;
    }

    private boolean isBaseURLNull(){
        return ECSConfiguration.INSTANCE.getBaseURL() == null;
    }

    private boolean isCategoryNull(){
        return ECSConfiguration.INSTANCE.getRootCategory() == null;
    }

    private boolean isSiteIDNull(){
        return ECSConfiguration.INSTANCE.getSiteId() == null;
    }

    private boolean isINValidString(String inputString){
        return inputString==null || inputString.isEmpty();
    }

    private boolean isINValidList(List list){
        return list==null || list.isEmpty();
    }

    public ECSErrorWrapper getProductSummaryAPIValidateError(List<String> ctns) {

        if(isINValidList(ctns)){
            return getECSErrorWrapper(ECSErrorEnum.ECSCtnNotProvided);
        }
        if(isLocaleNull()){
            return getECSErrorWrapper(ECSErrorEnum.ECSLocaleNotFound);
        }
        return null;
    }

    public ECSErrorWrapper getECSShoppingCartAPIValidateError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    private ECSErrorWrapper checkLocaleBaseURLSiteIDAndCategory() {
        ECSErrorWrapper ecsErrorWrapper =  checkLocaleAndBaseURL();

        if(ecsErrorWrapper!=null){
            return ecsErrorWrapper;
        }
        return checkSiteIDAndCategory();
    }

    public ECSErrorWrapper getCreateShoppingCartAPIValidateError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getHybrisOathAuthenticationAPIValidateError(ECSOAuthProvider oAuthInput) {
        if(isINValidString(oAuthInput.getOAuthID())){
            return getECSErrorWrapper(ECSErrorEnum.ECSOAuthDetailError);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getProductListAPIValidateError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getProductForAPIValidateError(String ctn) {

        if(isINValidString(ctn)){
            return getECSErrorWrapper(ECSErrorEnum.ECSCtnNotProvided);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getProductDetailAPIValidateError(ECSProduct product) {
        if(isINValidString(product.getCode())){
            return getECSErrorWrapper(ECSErrorEnum.ECSCtnNotProvided);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getAddProductToShoppingCartError(ECSProduct product) {
        return getProductDetailAPIValidateError(product);
    }

    public ECSErrorWrapper getUpdateQuantityError(int quantity) {

        if(quantity<0){
            return getECSErrorWrapper(ECSErrorEnum.ECSCommerceCartModificationError);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getSetVoucherError(String voucherCode) {
        if(isINValidString(voucherCode)){
            return getECSErrorWrapper(ECSErrorEnum.ECSUnsupportedVoucherError);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getVoucherError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getRemoveVoucherError(String voucherCode) {
        return getSetVoucherError(voucherCode);
    }

    public ECSErrorWrapper getDeliveryModesError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getSetDeliveryModeError(String deliveryModeID) {

        if(isINValidString(deliveryModeID)){
            return getECSErrorWrapper(ECSErrorEnum.ECSUnsupportedDeliveryModeError);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getRegionsError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getListSavedAddressError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getCreateNewAddressError(ECSAddress address) {
        return getAddressValidator(address);
    }

    public ECSErrorWrapper getSetDeliveryAddressError(ECSAddress address) {
        return getAddressValidator(address);
    }

    public ECSErrorWrapper getUpdateAddressError(ECSAddress address) {
        return getAddressValidator(address);
    }

    public ECSErrorWrapper getSetDefaultAddressError(ECSAddress address) {
        return getAddressValidator(address);
    }

    private ECSErrorWrapper getAddressValidator(ECSAddress address){
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getDeleteAddressError(ECSAddress address) {
        return getAddressValidator(address);
    }

    public ECSErrorWrapper getPaymentsError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getSetPaymentMethodError(String paymentDetailsId) {

        if(isINValidString(paymentDetailsId)){
            return getECSErrorWrapper(ECSErrorEnum.ECSInvalidPaymentInfoError);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getRetailersError(String productID) {

        if(isINValidString(productID)){
            return getECSErrorWrapper(ECSErrorEnum.ECSCtnNotProvided);
        }
        if(isLocaleNull()){
            return getECSErrorWrapper(ECSErrorEnum.ECSLocaleNotFound);
        }
        return null;
    }

    public ECSErrorWrapper getSubmitOrderError(String cvv) {

        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getMakePaymentError(ECSOrderDetail orderDetail, ECSAddress billingAddress) {
        if(isINValidString(orderDetail.getCode())){
            return getECSErrorWrapper(ECSErrorEnum.ECSorderIdNil);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getOrderHistoryError(int pageNumber) {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getOrderDetailError(String orderId) {

        if(isINValidString(orderId)){
            return getECSErrorWrapper(ECSErrorEnum.ECSorderIdNil);
        }
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public ECSErrorWrapper getUserProfileError() {
        return checkLocaleBaseURLSiteIDAndCategory();
    }

    public  static ECSErrorWrapper getErrorLocalizedErrorMessage(ECSErrorEnum ecsErrorEnum){
        ECSError ecsError = new ECSError(ecsErrorEnum.getErrorCode(),ecsErrorEnum.toString());
        return  new  ECSErrorWrapper(new Exception(ecsErrorEnum.getLocalizedErrorString()), ecsError);
    }
}
