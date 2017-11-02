/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.prodreg.constants.AnalyticsConstants;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.constants.ProdRegError;
import com.philips.cdp.prodreg.constants.RegistrationState;
import com.philips.cdp.prodreg.fragments.ProdRegFindSerialFragment;
import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.prodreg.localcache.ProdRegCache;
import com.philips.cdp.prodreg.model.metadata.MetadataSerNumbSampleContent;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponseData;
import com.philips.cdp.prodreg.model.summary.Data;
import com.philips.cdp.prodreg.util.ProdRegUtil;
import com.philips.cdp.registration.User;

import java.util.ArrayList;
import java.util.List;

public class ProdRegRegistrationController {

    public static final String TAG = ProdRegRegistrationController.class.getSimpleName();

    public interface RegisterControllerCallBacks extends ProdRegProcessController.ProcessControllerCallBacks {
        void isValidDate(boolean validDate);

        void isValidSerialNumber(boolean validSerialNumber);

        void setSummaryView(Data summaryData);

        void setProductView(RegisteredProduct registeredProduct);

        void requireFields(boolean requireDate, boolean requireSerialNumber);

        void logEvents(String tag, String data);

        void tagEvents(String event, String key, String value);

        void showSuccessLayout();

        void showAlreadyRegisteredDialog(RegisteredProduct registeredProduct);
    }
    private boolean isApiCallingProgress = false;
    private boolean isProductRegistered = false;
    private RegisterControllerCallBacks registerControllerCallBacks;
    private ProductMetadataResponseData productMetadataResponseData;
    private RegisteredProduct registeredProduct;
    private FragmentActivity fragmentActivity;
    private User user;
    private ArrayList<RegisteredProduct> registeredProducts;
    private ProdRegUtil prodRegUtil = new ProdRegUtil();
    private Bundle dependencyBundle;

    public ProdRegRegistrationController(final RegisterControllerCallBacks registerControllerCallBacks, final FragmentActivity fragmentActivity) {
        this.registerControllerCallBacks = registerControllerCallBacks;
        this.fragmentActivity = fragmentActivity;
        this.user = new User(fragmentActivity);
    }

    public ProdRegRegistrationController(final RegisterControllerCallBacks registerControllerCallBacks,final FragmentActivity fragmentActivity,User user) {
        this.registerControllerCallBacks = registerControllerCallBacks;
        this.user = user;
        this.fragmentActivity = fragmentActivity;
    }

    public boolean isApiCallingProgress() {
        return isApiCallingProgress;
    }

    public void handleState() {
        if (getRegisteredProduct().isProductAlreadyRegistered(getLocalRegisteredProducts())) {
            registeredProduct = getRegisteredProduct().getRegisteredProductIfExists(getLocalRegisteredProducts());
            registerControllerCallBacks.showAlreadyRegisteredDialog(getRegisteredProduct());
            updateRegisteredProductsList(registeredProduct);
        }
    }

    @NonNull
    protected LocalRegisteredProducts getLocalRegisteredProducts() {
        return new LocalRegisteredProducts(user);
    }

    @SuppressWarnings("noinspection unchecked")
    public void init(final Bundle bundle) {
        if (bundle != null) {
            this.dependencyBundle = bundle;
            registeredProducts = (ArrayList<RegisteredProduct>) bundle.getSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT);
            registeredProduct = (RegisteredProduct) bundle.getSerializable(ProdRegConstants.PROD_REG_PRODUCT);
            productMetadataResponseData = (ProductMetadataResponseData) bundle.getSerializable(ProdRegConstants.PROD_REG_PRODUCT_METADATA);
            final Data summaryData = (Data) bundle.getSerializable(ProdRegConstants.PROD_REG_PRODUCT_SUMMARY);
            updateSummaryView(summaryData);
            updateProductView();
        } else {
            registerControllerCallBacks.exitProductRegistration();
        }
    }

    private void updateSummaryView(final Data summaryData) {
        registerControllerCallBacks.setSummaryView(summaryData);
    }

    private void updateProductView() {
        if (registeredProduct != null) {
            registerControllerCallBacks.setProductView(getRegisteredProduct());
            handleRequiredFieldState(registeredProduct);
        }
    }

    private void handleRequiredFieldState(RegisteredProduct registeredProduct) {
        if (productMetadataResponseData != null) {
            final boolean requiredSerialNumber = productMetadataResponseData.getRequiresSerialNumber().equalsIgnoreCase("true");
            final boolean isValidSerialNumber = prodRegUtil.isValidSerialNumber(requiredSerialNumber, productMetadataResponseData.getSerialNumberFormat(), registeredProduct.getSerialNumber());
            registerControllerCallBacks.isValidSerialNumber(isValidSerialNumber);
            final boolean requireSerialNumber = requiredSerialNumber & !isValidSerialNumber;
            registerControllerCallBacks.requireFields(productMetadataResponseData.getRequiresDateOfPurchase().equalsIgnoreCase("true"), requireSerialNumber);
        }
    }

    public boolean isValidSerialNumber(final String serialNumber) {
        boolean isValidSerialNumber = true;
        if (productMetadataResponseData != null) {
            final boolean requiredSerialNumber = productMetadataResponseData.getRequiresSerialNumber().equalsIgnoreCase("true");
            isValidSerialNumber = prodRegUtil.isValidSerialNumber(requiredSerialNumber, productMetadataResponseData.getSerialNumberFormat(), serialNumber);
            final MetadataSerNumbSampleContent serialNumberSampleContent = productMetadataResponseData.getSerialNumberSampleContent();
            if (serialNumberSampleContent != null)
                registerControllerCallBacks.isValidSerialNumber(isValidSerialNumber);
            else
                registerControllerCallBacks.isValidSerialNumber(isValidSerialNumber);
        }
        return isValidSerialNumber;
    }

    public boolean isValidDate(final String text) {
        final boolean validDate = prodRegUtil.isValidDate(text);
        registerControllerCallBacks.isValidDate(validDate);
        return validDate;
    }

    public void registerProduct(final String purchaseDate, final String serialNumber) {
        final boolean validDate = validatePurchaseDate(purchaseDate);
        final boolean validSerialNumber = validateSerialNumber(serialNumber);
        if (!isApiCallingProgress && validDate && validSerialNumber) {
            registerControllerCallBacks.tagEvents("RegistrationEvent", "specialEvents", "extendWarrantyOption");
           // registerControllerCallBacks.showLoadingDialog();
            registerControllerCallBacks.logEvents(TAG, "Registering product with product details as CTN::" + getRegisteredProduct().getCtn());
            if (getRegisteredProduct().getRegistrationState() != RegistrationState.REGISTERED)
                getRegisteredProduct().setPurchaseDate(purchaseDate);
            getRegisteredProduct().setSerialNumber(serialNumber);
            ProdRegHelper prodRegHelper = getProdRegHelper();
            prodRegHelper.addProductRegistrationListener(getProdRegListener());
            final ProdRegCache prodRegCache = getProdRegCache();
            prodRegUtil.storeProdRegTaggingMeasuresCount(prodRegCache, AnalyticsConstants.Product_REGISTRATION_START_COUNT, 1);
            registerControllerCallBacks.tagEvents("RegistrationEvent", "noOfProductRegistrationStarts", String.valueOf(prodRegCache.getIntData(AnalyticsConstants.Product_REGISTRATION_START_COUNT)));
            prodRegHelper.getSignedInUserWithProducts(fragmentActivity).registerProduct(getRegisteredProduct());
        }
    }

    @NonNull
    protected ProdRegHelper getProdRegHelper() {
        return new ProdRegHelper();
    }

    @NonNull
    protected ProdRegCache getProdRegCache() {
        return new ProdRegCache();
    }

    private boolean validateSerialNumber(final String serialNumber) {
        boolean validSerialNumber = true;
        if (productMetadataResponseData.getRequiresSerialNumber().equalsIgnoreCase("true")) {
            validSerialNumber = isValidSerialNumber(serialNumber);
        }
        return validSerialNumber;
    }

    private boolean validatePurchaseDate(final String purchaseDate) {
        boolean validPurchaseDate = true;
        if (productMetadataResponseData.getRequiresDateOfPurchase().equalsIgnoreCase("true")) {
            validPurchaseDate = isValidDate(purchaseDate);
        }
        return validPurchaseDate;
    }

    @NonNull
    protected ProdRegListener getProdRegListener() {
        return new ProdRegListener() {
            @Override
            public void onProdRegSuccess(RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                registerControllerCallBacks.logEvents(TAG, "Product registered successfully");
                isProductRegistered = true;
                if (fragmentActivity != null && !fragmentActivity.isFinishing()) {
                    ProdRegRegistrationController.this.registeredProduct = registeredProduct;
                    registerControllerCallBacks.buttonEnable();
                  //  registerControllerCallBacks.dismissLoadingDialog();
                    final ProdRegCache prodRegCache = getProdRegCache();
                    prodRegUtil.storeProdRegTaggingMeasuresCount(prodRegCache, AnalyticsConstants.Product_REGISTRATION_COMPLETED_COUNT, 1);
                    registerControllerCallBacks.tagEvents("RegistrationSuccessEvent", "noOfProductRegistrationCompleted", String.valueOf(prodRegCache.getIntData(AnalyticsConstants.Product_REGISTRATION_COMPLETED_COUNT)));
                    updateRegisteredProductsList(registeredProduct);
                    registerControllerCallBacks.tagEvents("ProdRegSuccessEvent", "productModel", registeredProduct.getCtn());
                    registerControllerCallBacks.showSuccessLayout();
                }
            }

            @Override
            public void onProdRegFailed(RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                registerControllerCallBacks.logEvents(TAG, "Product registration failed");
                if (fragmentActivity != null && !fragmentActivity.isFinishing()) {
                    ProdRegRegistrationController.this.registeredProduct = registeredProduct;
                    registerControllerCallBacks.buttonEnable();
                    //registerControllerCallBacks.dismissLoadingDialog();
                    if (registeredProduct.getProdRegError() != ProdRegError.PRODUCT_ALREADY_REGISTERED) {
                        registerControllerCallBacks.showAlertOnError(registeredProduct.getProdRegError().getCode());
                    } else {
                        registerControllerCallBacks.showAlreadyRegisteredDialog(registeredProduct);
                        updateRegisteredProductsList(registeredProduct);
                    }
                }
            }
        };
    }

    protected User getUser() {
        return user;
    }

    public RegisteredProduct getRegisteredProduct() {
        return registeredProduct;
    }

    public List<RegisteredProduct> getRegisteredProducts() {
        updateRegisteredProductsList(registeredProduct);
        return registeredProducts;
    }

    private void updateRegisteredProductsList(final RegisteredProduct registeredProduct) {
        if (registeredProducts != null) {
            registeredProducts.remove(registeredProduct);
            registeredProducts.add(registeredProduct);
        }
    }

    public void onClickFindSerialNumber() {
        registerControllerCallBacks.showFragment(getFindSerialNumberFragment());
    }

    @NonNull
    private ProdRegFindSerialFragment getFindSerialNumberFragment() {
        final ProdRegFindSerialFragment prodRegFindSerialFragment = new ProdRegFindSerialFragment();
        prodRegFindSerialFragment.setArguments(dependencyBundle);
        return prodRegFindSerialFragment;
    }

    public boolean isProductRegistered() {
        return isProductRegistered;
    }
}
