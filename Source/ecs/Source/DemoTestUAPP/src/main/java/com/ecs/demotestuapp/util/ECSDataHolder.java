package com.ecs.demotestuapp.util;

import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.model.address.ECSAddress;
import com.philips.cdp.di.ecs.model.address.ECSDeliveryMode;
import com.philips.cdp.di.ecs.model.address.ECSUserProfile;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.config.ECSConfig;
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData;
import com.philips.cdp.di.ecs.model.orders.ECSOrderDetail;
import com.philips.cdp.di.ecs.model.orders.ECSOrderHistory;
import com.philips.cdp.di.ecs.model.payment.ECSPayment;
import com.philips.cdp.di.ecs.model.products.ECSProducts;
import com.philips.cdp.di.ecs.model.region.ECSRegion;
import com.philips.cdp.di.ecs.model.voucher.ECSVoucher;

import java.util.ArrayList;
import java.util.List;

public enum  ECSDataHolder {

    INSTANCE;

    ECSConfig ecsConfig;
    ECSOAuthData ecsoAuthData;
    String propositionID;
    ECSUserProfile ecsUserProfile;
    List<ECSDeliveryMode> ecsDeliveryModes = new ArrayList<>();
    ECSDeliveryMode ecsDeliveryMode;
    ECSProducts ecsProducts;
    List<ECSAddress> ecsAddressList = new ArrayList<>();
    ECSShoppingCart ecsShoppingCart;
    ECSOrderHistory ecsOrderHistory;

    public ECSOrderDetail getEcsOrderDetail() {
        return ecsOrderDetail;
    }

    public void setEcsOrderDetail(ECSOrderDetail ecsOrderDetail) {
        this.ecsOrderDetail = ecsOrderDetail;
    }

    ECSOrderDetail ecsOrderDetail;


    public List<ECSPayment> getEcsPayments() {
        return ecsPayments;
    }

    public void setEcsPayments(List<ECSPayment> ecsPayments) {
        this.ecsPayments = ecsPayments;
    }

    List<ECSPayment> ecsPayments = new ArrayList<>();

    public ECSOrderHistory getEcsOrderHistory() {
        return ecsOrderHistory;
    }

    public void setEcsOrderHistory(ECSOrderHistory ecsOrderHistory) {
        this.ecsOrderHistory = ecsOrderHistory;
    }



    public List<ECSRegion> getEcsRegions() {
        return ecsRegions;
    }

    public void setEcsRegions(List<ECSRegion> ecsRegions) {
        this.ecsRegions = ecsRegions;
    }

    List<ECSRegion> ecsRegions = new ArrayList<>();



    String janrainID;

    public ECSServices getEcsServices() {
        return ecsServices;
    }

    private ECSServices ecsServices;

    public ECSConfig getEcsConfig() {
        return ecsConfig;
    }

    public void setEcsConfig(ECSConfig ecsConfig) {
        this.ecsConfig = ecsConfig;
    }

    public ECSOAuthData getEcsoAuthData() {
        return ecsoAuthData;
    }

    public void setEcsoAuthData(ECSOAuthData ecsoAuthData) {
        this.ecsoAuthData = ecsoAuthData;
    }

    public String getPropositionID() {
        return propositionID;
    }

    public void setPropositionID(String propositionID) {
        this.propositionID = propositionID;
    }

    public ECSUserProfile getEcsUserProfile() {
        return ecsUserProfile;
    }

    public void setEcsUserProfile(ECSUserProfile ecsUserProfile) {
        this.ecsUserProfile = ecsUserProfile;
    }



    public ECSDeliveryMode getEcsDeliveryMode() {
        return ecsDeliveryMode;
    }

    public void setEcsDeliveryMode(ECSDeliveryMode ecsDeliveryMode) {
        this.ecsDeliveryMode = ecsDeliveryMode;
    }

    public ECSProducts getEcsProducts() {
        return ecsProducts;
    }

    public void setEcsProducts(ECSProducts ecsProducts) {
        this.ecsProducts = ecsProducts;
    }




    public List<ECSAddress> getEcsAddressList() {
        return ecsAddressList;
    }

    public void setEcsAddressList(List<ECSAddress> ecsAddressList) {
        this.ecsAddressList = ecsAddressList;
    }



    public List<ECSDeliveryMode> getEcsDeliveryModes() {
        return ecsDeliveryModes;
    }

    public void setEcsDeliveryModes(List<ECSDeliveryMode> ecsDeliveryModes) {
        this.ecsDeliveryModes = ecsDeliveryModes;
    }



    public List<ECSVoucher> getVouchers() {
        return vouchers;
    }

    public void setVouchers(List<ECSVoucher> vouchers) {
        this.vouchers = vouchers;
    }

    List<ECSVoucher> vouchers;

    public ECSShoppingCart getEcsShoppingCart() {
        return ecsShoppingCart;
    }

    public void setEcsShoppingCart(ECSShoppingCart ecsShoppingCart) {
        this.ecsShoppingCart = ecsShoppingCart;
    }



    public String getJanrainID() {
        return janrainID;
    }



    public void setECSService(ECSServices ecsServices) {
        this.ecsServices = ecsServices;
    }

    public void setJanrainID(String janrainID) {
        this.janrainID = janrainID;
    }

    public void resetData(){

        ecsConfig = null;
        ecsoAuthData = null;
        propositionID = null;
        ecsUserProfile = null;
        ecsDeliveryModes = null;
        ecsDeliveryMode = null;
        ecsProducts = null;
        ecsAddressList = null;
        ecsShoppingCart = null;
        janrainID = null;
    }
}
