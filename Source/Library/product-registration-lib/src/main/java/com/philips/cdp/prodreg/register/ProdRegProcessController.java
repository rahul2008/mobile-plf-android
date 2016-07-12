/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.prodreg.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.constants.RegistrationState;
import com.philips.cdp.prodreg.fragments.ProdRegConnectionFragment;
import com.philips.cdp.prodreg.fragments.ProdRegRegistrationFragment;
import com.philips.cdp.prodreg.listener.MetadataListener;
import com.philips.cdp.prodreg.listener.RegisteredProductsListener;
import com.philips.cdp.prodreg.listener.SummaryListener;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;
import com.philips.cdp.prodreg.model.summary.ProductSummaryResponse;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

import java.util.ArrayList;
import java.util.List;

public class ProdRegProcessController {

    public interface ProcessControllerCallBacks {
        void dismissLoadingDialog();
        void exitProductRegistration();

        void showLoadingDialog();
        void showFragment(Fragment fragment);
        void showAlertOnError(int responseCode);
    }

    private static final String TAG = ProdRegProcessController.class.getSimpleName();
    private Product currentProduct;
    private ProcessControllerCallBacks processControllerCallBacks;
    private Bundle dependencyBundle;
    private boolean launchedRegistration = false;
    private boolean isApiCallingProgress = false;
    private FragmentActivity fragmentActivity;
    private User user;

    public ProdRegProcessController(final ProcessControllerCallBacks processControllerCallBacks, final FragmentActivity fragmentActivity) {
        this.processControllerCallBacks = processControllerCallBacks;
        this.fragmentActivity = fragmentActivity;
        this.user = new User(fragmentActivity);
    }

    public void process(final Bundle arguments) {
        if (arguments != null) {
            ArrayList<Product> regProdList = (ArrayList<Product>) arguments.getSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT);
            if (regProdList != null) {
                currentProduct = regProdList.get(0);
                if (getUser().isUserSignIn()) {
                    //Signed in case
                    if (!isApiCallingProgress) {
                        ProdRegLogger.v(TAG, "Checking product in registered product list");
                        getRegisteredProducts();
                    }
                } else {
                    //Not signed in

                    if (launchedRegistration) {
                        //Registration page has already launched
                        processControllerCallBacks.dismissLoadingDialog();
                        processControllerCallBacks.exitProductRegistration();
                    } else {
                        //Registration is not yet launched.
                        launchedRegistration = true;
                        ProdRegLogger.v(TAG, "User not signed in. Startng sign in flow");
                        RegistrationLaunchHelper.launchRegistrationActivityWithAccountSettings(fragmentActivity);
                    }
                }
            } else {
                processControllerCallBacks.exitProductRegistration();
            }
        }
    }

    private void getRegisteredProducts() {
        if (fragmentActivity != null && !fragmentActivity.isFinishing() && currentProduct != null) {
            ProdRegHelper prodRegHelper = getProdRegHelper();
            isApiCallingProgress = true;
            ProdRegLogger.v(TAG, "Getting registered product list");
            prodRegHelper.getSignedInUserWithProducts().getRegisteredProducts(getRegisteredProductsListener());
        }
    }

    @NonNull
    protected ProdRegHelper getProdRegHelper() {
        return new ProdRegHelper();
    }

    @NonNull
    protected RegisteredProductsListener getRegisteredProductsListener() {
        return new RegisteredProductsListener() {
            @Override
            public void onGetRegisteredProductListSuccess(final List<RegisteredProduct> registeredProducts, final long timeStamp) {
                ProdRegLogger.v(TAG, "Get Product Registered list::success");
                if (!isCtnRegistered(registeredProducts, currentProduct) && fragmentActivity != null && !fragmentActivity.isFinishing()) {
                    currentProduct.getProductMetadata(fragmentActivity, getMetadataListener());
                } else {
                    processControllerCallBacks.dismissLoadingDialog();
                    processControllerCallBacks.showFragment(new ProdRegConnectionFragment());
                }
            }
        };
    }

    @NonNull
    private MetadataListener getMetadataListener() {
        return new MetadataListener() {
            @Override
            public void onMetadataResponse(final ProductMetadataResponse productMetadataResponse) {
                ProdRegLogger.v(TAG, "Get Product Metadata::success");
                if (productMetadataResponse != null) {
                    dependencyBundle = new Bundle();
                    dependencyBundle.putSerializable(ProdRegConstants.PROD_REG_PRODUCT_METADATA, productMetadataResponse.getData());
                    doSummaryRequest();
                }
            }

            @Override
            public void onErrorResponse(final String errorMessage, final int responseCode) {
                ProdRegLogger.v(TAG, "Get Product Metadata::error");
                processControllerCallBacks.dismissLoadingDialog();
                processControllerCallBacks.showAlertOnError(responseCode);
            }
        };
    }

    private void doSummaryRequest() {
        if (fragmentActivity != null && !fragmentActivity.isFinishing() && currentProduct != null) {
            dependencyBundle.putSerializable(ProdRegConstants.PROD_REG_PRODUCT, currentProduct);
            currentProduct.getProductSummary(fragmentActivity, currentProduct, getSummaryListener());
        }
    }

    @NonNull
    private SummaryListener getSummaryListener() {
        return new SummaryListener() {
            @Override
            public void onSummaryResponse(final ProductSummaryResponse productSummaryResponse) {
                if (productSummaryResponse != null) {
                    ProdRegLogger.v(TAG, "Get Product Summary::success");
                    dependencyBundle.putSerializable(ProdRegConstants.PROD_REG_PRODUCT_SUMMARY, productSummaryResponse.getData());
                    final ProdRegRegistrationFragment prodRegRegistrationFragment = getProdRegRegistrationFragment();
                    prodRegRegistrationFragment.setArguments(dependencyBundle);
                    processControllerCallBacks.dismissLoadingDialog();
                    processControllerCallBacks.showFragment(prodRegRegistrationFragment);
                }
            }

            @Override
            public void onErrorResponse(final String errorMessage, final int responseCode) {
                ProdRegLogger.v(TAG, "Get Product Summary::error");
                final ProdRegRegistrationFragment prodRegRegistrationFragment = getProdRegRegistrationFragment();
                prodRegRegistrationFragment.setArguments(dependencyBundle);
                processControllerCallBacks.dismissLoadingDialog();
                processControllerCallBacks.showFragment(prodRegRegistrationFragment);
            }
        };
    }

    @NonNull
    protected ProdRegRegistrationFragment getProdRegRegistrationFragment() {
        return new ProdRegRegistrationFragment();
    }

    private boolean isCtnRegistered(final List<RegisteredProduct> registeredProducts, final Product product) {
        for (RegisteredProduct result : registeredProducts) {
            if (product.getCtn().equalsIgnoreCase(result.getCtn()) && product.getSerialNumber().equals(result.getSerialNumber()) && result.getRegistrationState() == RegistrationState.REGISTERED) {
                return true;
            }
        }
        return false;
    }

    public boolean isLaunchedRegistration() {
        return launchedRegistration;
    }

    public void setLaunchedRegistration(final boolean launchedRegistration) {
        this.launchedRegistration = launchedRegistration;
    }

    public boolean isApiCallingProgress() {
        return isApiCallingProgress;
    }

    protected User getUser() {
        return user;
    }
}
