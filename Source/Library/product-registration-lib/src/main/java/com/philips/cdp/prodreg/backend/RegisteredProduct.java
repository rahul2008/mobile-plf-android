package com.philips.cdp.prodreg.backend;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.RegistrationState;
import com.philips.cdp.prodreg.error.ProdRegError;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegisteredProduct extends Product {

    private RegistrationState registrationState;
    private String endWarrantyDate;
    private String uuid = "";
    private ProdRegError prodRegError;
    private String contractNumber;

    public RegisteredProduct(final String ctn, final String serialNumber, final String purchaseDate, final Sector sector, final Catalog catalog) {
        super(ctn, serialNumber, purchaseDate, sector, catalog);
    }

    public RegistrationState getRegistrationState() {
        return registrationState;
    }

    public void setRegistrationState(final RegistrationState registrationState) {
        this.registrationState = registrationState;
    }

    public String getEndWarrantyDate() {
        return endWarrantyDate;
    }

    public void setEndWarrantyDate(final String endWarrantyDate) {
        this.endWarrantyDate = endWarrantyDate;
    }

    public String getUserUUid() {
        return uuid;
    }

    public void setUserUUid(final String userUUid) {
        this.uuid = userUUid;
    }

    public ProdRegError getProdRegError() {
        return prodRegError;
    }

    public void setProdRegError(final ProdRegError prodRegError) {
        this.prodRegError = prodRegError;
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof RegisteredProduct) {
            RegisteredProduct registeredProduct = (RegisteredProduct) object;
            final String parentUuid = registeredProduct.getUserUUid();
            final String currentUuid = getUserUUid();
            boolean parentState = parentUuid.length() != 0;
            boolean currentState = currentUuid.length() != 0;
            boolean shouldConsiderUUID = isShouldConsiderUUID(parentUuid, currentUuid, parentState, currentState);
            if (!shouldConsiderUUID && registeredProduct.getCtn().equals(this.getCtn()) && registeredProduct.getSerialNumber().equals(this.getSerialNumber())) {
                return true;
            } else if (registeredProduct.getCtn().equals(this.getCtn()) && registeredProduct.getSerialNumber().equals(this.getSerialNumber()) && parentUuid.equals(currentUuid))
                return true;
        }
        return false;
    }

    protected boolean isShouldConsiderUUID(final String parentUuid, final String currentUuid, final boolean parentState, final boolean currentState) {
        boolean shouldConsiderUUID = false;
        if (!parentState || !currentState && (!parentUuid.equals(currentUuid))) {
            shouldConsiderUUID = false;
        } else if (!parentUuid.equals(currentUuid)) {
            shouldConsiderUUID = true;
        }
        return shouldConsiderUUID;
    }

    protected boolean IsUserRegisteredLocally(final LocalRegisteredProducts localRegisteredProducts) {
        final List<RegisteredProduct> registeredProducts = localRegisteredProducts.getRegisteredProducts();
        final int index = registeredProducts.indexOf(this);
        if (index != -1) {
            RegisteredProduct product = registeredProducts.get(index);
            final RegistrationState registrationState = product.getRegistrationState();
            return registrationState == RegistrationState.REGISTERED;
        }
        return false;
    }


    @Override
    public int hashCode() {
        final int value = 5 * 10 + ((getCtn() == null) ? 0 : getCtn().hashCode());
        return value;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(final String contractNumber) {
        this.contractNumber = contractNumber;
    }
}
