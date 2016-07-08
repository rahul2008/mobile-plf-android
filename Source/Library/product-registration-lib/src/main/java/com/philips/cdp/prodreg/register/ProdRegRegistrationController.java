
/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.prodreg.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.constants.ProdRegError;
import com.philips.cdp.prodreg.fragments.ProdRegSuccessFragment;
import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.prodreg.localcache.ProdRegCache;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponseData;
import com.philips.cdp.prodreg.model.summary.Data;
import com.philips.cdp.prodreg.tagging.ProdRegTagging;
import com.philips.cdp.prodreg.util.ProdRegUtil;
import com.philips.cdp.registration.User;

public class ProdRegRegistrationController {

    public interface RegisterControllerCallBacks extends ProdRegProcessController.ProcessControllerCallBacks {
        void isValidDate(boolean validDate);

        void isValidSerialNumber(boolean validSerialNumber, String format);

        void setSummaryView(Data summaryData);

        void setProductView(RegisteredProduct registeredProduct);

        void requireFields(boolean requireDate, boolean requireSerialNumber);
    }

    private RegisterControllerCallBacks registerControllerCallBacks;
    private ProductMetadataResponseData productMetadataResponseData;
    private RegisteredProduct registeredProduct;
    private FragmentActivity fragmentActivity;
    private User user;

    public ProdRegRegistrationController(final RegisterControllerCallBacks registerControllerCallBacks, final FragmentActivity fragmentActivity) {
        this.registerControllerCallBacks = registerControllerCallBacks;
        this.fragmentActivity = fragmentActivity;
        this.user = new User(fragmentActivity);
    }

    public void handleState() {
        if (getRegisteredProduct().isProductAlreadyRegistered(getLocalRegisteredProducts())) {
            registerControllerCallBacks.showFragment(getSuccessFragment());
        }
    }

    @NonNull
    protected ProdRegSuccessFragment getSuccessFragment() {
        return new ProdRegSuccessFragment();
    }

    @NonNull
    protected LocalRegisteredProducts getLocalRegisteredProducts() {
        return new LocalRegisteredProducts(fragmentActivity, user);
    }

    public void init(final Bundle bundle) {
        if (bundle != null) {
            final Product currentProduct = (Product) bundle.getSerializable(ProdRegConstants.PROD_REG_PRODUCT);
            mapToRegisteredProduct(currentProduct);
            productMetadataResponseData = (ProductMetadataResponseData) bundle.getSerializable(ProdRegConstants.PROD_REG_PRODUCT_METADATA);
            final Data summaryData = (Data) bundle.getSerializable(ProdRegConstants.PROD_REG_PRODUCT_SUMMARY);
            updateSummaryView(summaryData);
            updateProductView();
        } else {
            registerControllerCallBacks.exitProductRegistration();
        }
    }

    private RegisteredProduct mapToRegisteredProduct(final Product currentProduct) {
        if (currentProduct != null) {
            registeredProduct = new RegisteredProduct(currentProduct.getCtn(), currentProduct.getSector(), currentProduct.getCatalog());
            registeredProduct.setSerialNumber(currentProduct.getSerialNumber());
            registeredProduct.setPurchaseDate(currentProduct.getPurchaseDate());
            registeredProduct.sendEmail(currentProduct.getEmail());
        }
        return registeredProduct;
    }

    private void updateSummaryView(final Data summaryData) {
        if (summaryData != null) {
            registerControllerCallBacks.setSummaryView(summaryData);
        }
    }

    private void updateProductView() {
        if (registeredProduct != null) {
            handleRequiredFieldState(getRegisteredProduct());
            registerControllerCallBacks.setProductView(getRegisteredProduct());
        }
    }

    private void handleRequiredFieldState(final RegisteredProduct registeredProduct) {
        if (productMetadataResponseData != null) {
            final boolean requireSerialNumber = productMetadataResponseData.getRequiresSerialNumber().equalsIgnoreCase("true") && ProdRegUtil.isInValidSerialNumber(productMetadataResponseData.getSerialNumberFormat(), registeredProduct.getSerialNumber());
            registerControllerCallBacks.requireFields(productMetadataResponseData.getRequiresDateOfPurchase().equalsIgnoreCase("true"), requireSerialNumber);
        }
    }

    public boolean isValidSerialNumber(final String serialNumber) {
        final String serialNumberFormat = productMetadataResponseData.getSerialNumberFormat();
        final boolean isValidSerialNumber = !ProdRegUtil.isInValidSerialNumber(serialNumberFormat, serialNumber);
        registerControllerCallBacks.isValidSerialNumber(isValidSerialNumber, serialNumberFormat);
        return isValidSerialNumber;
    }

    public boolean isValidDate(final String text) {
        final boolean validDate = ProdRegUtil.isValidDate(text);
        registerControllerCallBacks.isValidDate(validDate);
        return validDate;
    }

    public void registerProduct(final String purchaseDate, final String serialNumber) {
        final boolean validDate = validatePurchaseDate(purchaseDate);
        final boolean validSerialNumber = validateSerialNumber(serialNumber);
        if (validDate && validSerialNumber) {
            ProdRegTagging.getInstance(fragmentActivity).trackActionWithCommonGoals("ProdRegRegistrationScreen", "specialEvents", "extendWarrantyOption");
            registerControllerCallBacks.showLoadingDialog();
            getRegisteredProduct().setPurchaseDate(purchaseDate);
            getRegisteredProduct().setSerialNumber(serialNumber);
            ProdRegHelper prodRegHelper = new ProdRegHelper();
            prodRegHelper.addProductRegistrationListener(getProdRegListener());
            final ProdRegCache prodRegCache = new ProdRegCache(fragmentActivity);
            ProdRegUtil.storeProdRegTaggingMeasuresCount(prodRegCache, ProdRegConstants.Product_REGISTRATION_START_COUNT, 1);
            ProdRegTagging.getInstance(fragmentActivity).trackActionWithCommonGoals("ProdRegRegistrationScreen", "noOfProductRegistrationStarts", String.valueOf(prodRegCache.getIntData(ProdRegConstants.Product_REGISTRATION_START_COUNT)));
            prodRegHelper.getSignedInUserWithProducts().registerProduct(getRegisteredProduct());
        }
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
    private ProdRegListener getProdRegListener() {
        return new ProdRegListener() {
            @Override
            public void onProdRegSuccess(RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                if (fragmentActivity != null && !fragmentActivity.isFinishing()) {
                    registerControllerCallBacks.dismissLoadingDialog();
                    final ProdRegCache prodRegCache = new ProdRegCache(fragmentActivity);
                    ProdRegUtil.storeProdRegTaggingMeasuresCount(prodRegCache, ProdRegConstants.Product_REGISTRATION_COMPLETED_COUNT, 1);
                    ProdRegTagging.getInstance(fragmentActivity).trackActionWithCommonGoals("ProdRegRegistrationScreen", "noOfProductRegistrationCompleted", String.valueOf(prodRegCache.getIntData(ProdRegConstants.Product_REGISTRATION_COMPLETED_COUNT)));
                    final ProdRegSuccessFragment fragment = getSuccessFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ProdRegConstants.PROD_REG_PRODUCT, registeredProduct);
                    fragment.setArguments(bundle);
                    registerControllerCallBacks.showFragment(fragment);
                }
            }

            @Override
            public void onProdRegFailed(RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                if (fragmentActivity != null && !fragmentActivity.isFinishing()) {
                    registerControllerCallBacks.dismissLoadingDialog();
                    if (registeredProduct.getProdRegError() != ProdRegError.PRODUCT_ALREADY_REGISTERED) {
                        registerControllerCallBacks.showAlertOnError(registeredProduct.getProdRegError().getCode());
                    } else {
                        final ProdRegSuccessFragment fragment = getSuccessFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ProdRegConstants.PROD_REG_PRODUCT, registeredProduct);
                        fragment.setArguments(bundle);
                        registerControllerCallBacks.showFragment(fragment);
                    }
                }
            }
        };
    }

    protected User getUser() {
        return user;
    }

    protected RegisteredProduct getRegisteredProduct() {
        return registeredProduct;
    }
}
