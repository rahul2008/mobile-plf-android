package com.ecs.demotestuapp.util;

import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.model.address.ECSAddress;
import com.philips.cdp.di.ecs.model.address.ECSDeliveryMode;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.config.ECSConfig;
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData;
import com.philips.cdp.di.ecs.model.user.ECSUserProfile;
import com.philips.cdp.di.ecs.model.voucher.ECSVoucher;

import java.util.List;

public enum  ECSDataHolder {

    INSTANCE;

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

    ECSConfig ecsConfig;
    ECSOAuthData ecsoAuthData;
    String propositionID;
    ECSUserProfile ecsUserProfile;
    List<ECSDeliveryMode> ecsDeliveryModes;

    public List<ECSAddress> getEcsAddressList() {
        return ecsAddressList;
    }

    public void setEcsAddressList(List<ECSAddress> ecsAddressList) {
        this.ecsAddressList = ecsAddressList;
    }

    List<ECSAddress> ecsAddressList;

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

    ECSShoppingCart ecsShoppingCart;

    public String getJanrainID() {
        return janrainID;
    }

    String janrainID;

    public void setECSService(ECSServices ecsServices) {
        this.ecsServices = ecsServices;
    }

    public void setJanrainID(String janrainID) {
        this.janrainID = janrainID;
    }
}
