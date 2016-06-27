package com.philips.cdp.prodreg.register;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.RegistrationState;
import com.philips.cdp.prodreg.error.ProdRegError;

import java.util.List;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class RegisteredProduct extends Product {

    private RegistrationState registrationState;
    private String endWarrantyDate;
    private String uuid = "";
    private ProdRegError prodRegError;
    private String contractNumber;
    private String lastSolicitDate;

    /**
     * API to Registered product
     *
     * @param ctn     ctn as String
     * @param sector  Sector as Enum's
     * @param catalog Catalog as Enum's
     */
    public RegisteredProduct(final String ctn, final Sector sector, final Catalog catalog) {
        super(ctn, sector, catalog);
    }

    /**
     * API return registration state
     * @return it return registration
     */
    public RegistrationState getRegistrationState() {
        return registrationState;
    }

    /**
     * API need to set registration state
     * @param registrationState registrationState
     */
    public void setRegistrationState(final RegistrationState registrationState) {
        this.registrationState = registrationState;
    }

    /**
     * API return Warranty Date
     * @return endWarrantyDate as String
     */
    public String getEndWarrantyDate() {
        if (endWarrantyDate == null)
            return lastSolicitDate;
        else
            return endWarrantyDate;
    }

    /**
     * API set warranty date
     * @param endWarrantyDate endWarrantyDate as String
     */

    public void setEndWarrantyDate(final String endWarrantyDate) {
        this.endWarrantyDate = endWarrantyDate;
    }

    /**
     * APi return UUID of user
     * @return return uuid as string
     */
    public String getUserUUid() {
        return uuid;
    }

    /**
     * API set uuid of User
     * @param userUUid userUUid as string
     */

    public void setUserUUid(final String userUUid) {
        this.uuid = userUUid;
    }

    /**
     * Api return error
     * @return prodRegError
     */
    public ProdRegError getProdRegError() {
        return prodRegError;
    }

    /**
     * API need to set error type
     * @param prodRegError prodRegError
     */
    public void setProdRegError(final ProdRegError prodRegError) {
        this.prodRegError = prodRegError;
    }

    /**
     * API check cached UUid , CTN and getUUID ,getctn
     * @param object this object return registered product details
     * @return return true or false
     */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof RegisteredProduct) {
            RegisteredProduct registeredProduct = (RegisteredProduct) object;
                final String parentUuid = registeredProduct.getUserUUid();
                final String currentUuid = getUserUUid();
                boolean parentState = parentUuid.length() != 0;
                boolean currentState = currentUuid.length() != 0;
                boolean shouldConsiderUUID = isShouldConsiderUUID(parentUuid, currentUuid, parentState, currentState);
            final String previousCtn = registeredProduct.getCtn();
            final String currentCtn = getCtn();
            final String previousSerialNumber = registeredProduct.getSerialNumber();
            final String currentSerialNumber = getSerialNumber();
            if (!shouldConsiderUUID && previousCtn.equals(currentCtn) && previousSerialNumber.equals(currentSerialNumber)) {
                    return true;
            } else if (previousCtn.equals(currentCtn) && previousSerialNumber.equals(currentSerialNumber) && parentUuid.equals(currentUuid))
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

    public RegisteredProduct getRegisteredProductIfExists(final LocalRegisteredProducts localRegisteredProducts) {
        final List<RegisteredProduct> registeredProducts = localRegisteredProducts.getRegisteredProducts();
        final int index = registeredProducts.indexOf(this);
        if (index != -1) {
            RegisteredProduct product = registeredProducts.get(index);
            if (product.getProdRegError() != null)
                updateFields(product);
            localRegisteredProducts.updateRegisteredProducts(product);
            return product;
        }
        return null;
    }

    private void updateFields(final RegisteredProduct product) {
        product.setUserUUid(getUserUUid());
        product.sendEmail(getEmail());
    }

    @Override
    public int hashCode() {
        final int value = 5 * 10 + ((getCtn() == null) ? 0 : getCtn().hashCode());
        return value;
    }

    /**
     * API return contract Number
     * @return return contract number as string
     */
    public String getContractNumber() {
        return contractNumber;
    }

    /**
     * API set the contract number
     * @param contractNumber contractNumber as String
     */
    public void setContractNumber(final String contractNumber) {
        this.contractNumber = contractNumber;
    }
}
