
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
import com.philips.cdp.prodreg.fragments.ProdRegConnectionFragment;
import com.philips.cdp.prodreg.fragments.ProdRegSuccessFragment;
import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.prodreg.localcache.ProdRegCache;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponseData;
import com.philips.cdp.prodreg.model.summary.Data;
import com.philips.cdp.prodreg.tagging.AnalyticsConstants;
import com.philips.cdp.prodreg.tagging.ProdRegTagging;
import com.philips.cdp.prodreg.util.ProdRegUtil;
import com.philips.cdp.registration.User;

import java.util.ArrayList;
import java.util.List;

public class ProdRegRegistrationController {

    public interface RegisterControllerCallBacks extends ProdRegProcessController.ProcessControllerCallBacks {
        void isValidDate(boolean validDate);

        void isValidSerialNumber(boolean validSerialNumber, String format);

        void setSummaryView(Data summaryData);

        void setProductView(RegisteredProduct registeredProduct);

        void requireFields(boolean requireDate, boolean requireSerialNumber);
    }

    private static final String TAG = ProdRegRegistrationController.class.getSimpleName();

    private RegisterControllerCallBacks registerControllerCallBacks;
    private ProductMetadataResponseData productMetadataResponseData;
    private RegisteredProduct registeredProduct;
    private FragmentActivity fragmentActivity;
    private User user;
    private ArrayList<RegisteredProduct> registeredProducts;

    public ProdRegRegistrationController(final RegisterControllerCallBacks registerControllerCallBacks, final FragmentActivity fragmentActivity) {
        this.registerControllerCallBacks = registerControllerCallBacks;
        this.fragmentActivity = fragmentActivity;
        this.user = new User(fragmentActivity);
    }

    public void handleState() {
        if (getRegisteredProduct().isProductAlreadyRegistered(getLocalRegisteredProducts())) {
            final ProdRegSuccessFragment prodRegSuccessFragment = getSuccessFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT, registeredProducts);
            prodRegSuccessFragment.setArguments(bundle);
            registerControllerCallBacks.showFragment(prodRegSuccessFragment);
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
            handleRequiredFieldState(registeredProduct);
            registerControllerCallBacks.setProductView(getRegisteredProduct());
        }
    }

    private void handleRequiredFieldState(RegisteredProduct registeredProduct) {
        if (productMetadataResponseData != null) {
            final boolean requiredSerialNumber = productMetadataResponseData.getRequiresSerialNumber().equalsIgnoreCase("true");
            final boolean isValidSerialNumber = ProdRegUtil.isValidSerialNumber(requiredSerialNumber, productMetadataResponseData.getSerialNumberFormat(), registeredProduct.getSerialNumber());
            final boolean requireSerialNumber = productMetadataResponseData.getRequiresSerialNumber().equalsIgnoreCase("true") & !isValidSerialNumber;
            registerControllerCallBacks.requireFields(productMetadataResponseData.getRequiresDateOfPurchase().equalsIgnoreCase("true"), requireSerialNumber);
        }
    }

    public boolean isValidSerialNumber(final String serialNumber) {
        final String serialNumberFormat = productMetadataResponseData.getSerialNumberFormat();
        final boolean requiredSerialNumber = productMetadataResponseData != null && productMetadataResponseData.getRequiresSerialNumber().equalsIgnoreCase("true");
        final boolean isValidSerialNumber = ProdRegUtil.isValidSerialNumber(requiredSerialNumber, productMetadataResponseData.getSerialNumberFormat(), serialNumber);
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
            ProdRegTagging.getInstance().trackActionWithCommonGoals("ProdRegRegistrationScreen", "specialEvents", "extendWarrantyOption");
            registerControllerCallBacks.showLoadingDialog();
            ProdRegLogger.v(TAG, "Registering product with product details as CTN::" + getRegisteredProduct().getCtn());
            getRegisteredProduct().setPurchaseDate(purchaseDate);
            getRegisteredProduct().setSerialNumber(serialNumber);
            ProdRegHelper prodRegHelper = getProdRegHelper();
            prodRegHelper.addProductRegistrationListener(getProdRegListener());
            final ProdRegCache prodRegCache = getProdRegCache();
            ProdRegUtil.storeProdRegTaggingMeasuresCount(prodRegCache, AnalyticsConstants.Product_REGISTRATION_START_COUNT, 1);
            ProdRegTagging.getInstance().trackActionWithCommonGoals("ProdRegRegistrationScreen", "noOfProductRegistrationStarts", String.valueOf(prodRegCache.getIntData(AnalyticsConstants.Product_REGISTRATION_START_COUNT)));
            prodRegHelper.getSignedInUserWithProducts().registerProduct(getRegisteredProduct());
        }
    }

    @NonNull
    protected ProdRegHelper getProdRegHelper() {
        return new ProdRegHelper();
    }

    @NonNull
    protected ProdRegCache getProdRegCache() {
        return new ProdRegCache(fragmentActivity);
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
                ProdRegLogger.v(TAG, "Product registered successfully");
                if (fragmentActivity != null && !fragmentActivity.isFinishing()) {
                    ProdRegRegistrationController.this.registeredProduct = registeredProduct;
                    registerControllerCallBacks.dismissLoadingDialog();
                    final ProdRegCache prodRegCache = getProdRegCache();
                    ProdRegUtil.storeProdRegTaggingMeasuresCount(prodRegCache, AnalyticsConstants.Product_REGISTRATION_COMPLETED_COUNT, 1);
                    ProdRegTagging.getInstance().trackActionWithCommonGoals("ProdRegRegistrationScreen", "noOfProductRegistrationCompleted", String.valueOf(prodRegCache.getIntData(AnalyticsConstants.Product_REGISTRATION_COMPLETED_COUNT)));
                    final ProdRegSuccessFragment fragment = getSuccessFragment();
                    updateRegisteredProductsList(registeredProduct);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ProdRegConstants.PROD_REG_PRODUCT, registeredProduct);
                    bundle.putSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT, registeredProducts);
                    fragment.setArguments(bundle);
                    registerControllerCallBacks.showFragment(fragment);
                }
            }

            @Override
            public void onProdRegFailed(RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                ProdRegLogger.v(TAG, "Product registration failed");
                if (fragmentActivity != null && !fragmentActivity.isFinishing()) {
                    ProdRegRegistrationController.this.registeredProduct = registeredProduct;
                    registerControllerCallBacks.dismissLoadingDialog();
                    if (registeredProduct.getProdRegError() != ProdRegError.PRODUCT_ALREADY_REGISTERED) {
                        registerControllerCallBacks.showAlertOnError(registeredProduct.getProdRegError().getCode());
                    } else {
                        final ProdRegConnectionFragment connectionFragment = getConnectionFragment();
                        updateRegisteredProductsList(registeredProduct);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT, registeredProducts);
                        connectionFragment.setArguments(bundle);
                        registerControllerCallBacks.showFragment(connectionFragment);
                    }
                }
            }
        };
    }

    @NonNull
    protected ProdRegConnectionFragment getConnectionFragment() {
        return new ProdRegConnectionFragment();
    }

    protected User getUser() {
        return user;
    }

    protected RegisteredProduct getRegisteredProduct() {
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
}
