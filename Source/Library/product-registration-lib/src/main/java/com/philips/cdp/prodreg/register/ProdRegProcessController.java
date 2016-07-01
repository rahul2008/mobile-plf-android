package com.philips.cdp.prodreg.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.prodreg.ProdRegConstants;
import com.philips.cdp.prodreg.RegistrationState;
import com.philips.cdp.prodreg.fragments.ProdRegConnectionFragment;
import com.philips.cdp.prodreg.fragments.ProdRegRegistrationFragment;
import com.philips.cdp.prodreg.listener.MetadataListener;
import com.philips.cdp.prodreg.listener.RegisteredProductsListener;
import com.philips.cdp.prodreg.listener.SummaryListener;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;
import com.philips.cdp.prodreg.model.summary.ProductSummaryResponse;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegProcessController {

    public interface ProcessControllerCallBacks {
        void dismissLoadingDialog();
        void exitProductRegistration();

        void showLoadingDialog(String message);
        void showFragment(Fragment fragment);
        void showAlertOnError(int responseCode);
    }
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
                if (user.isUserSignIn()) {
                    //Signed in case
                    if (!isApiCallingProgress) {
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
            ProdRegHelper prodRegHelper = new ProdRegHelper();
            isApiCallingProgress = true;
            prodRegHelper.getSignedInUserWithProducts().getRegisteredProducts(getRegisteredProductsListener());
        }
    }

    @NonNull
    private RegisteredProductsListener getRegisteredProductsListener() {
        return new RegisteredProductsListener() {
            @Override
            public void getRegisteredProductsSuccess(final List<RegisteredProduct> registeredProducts, final long timeStamp) {
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
                if (productMetadataResponse != null) {
                    dependencyBundle = new Bundle();
                    dependencyBundle.putSerializable(ProdRegConstants.PROD_REG_PRODUCT_METADATA, productMetadataResponse.getData());
                    doSummaryRequest();
                }
            }

            @Override
            public void onErrorResponse(final String errorMessage, final int responseCode) {
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
                    dependencyBundle.putSerializable(ProdRegConstants.PROD_REG_PRODUCT_SUMMARY, productSummaryResponse.getData());
                    final ProdRegRegistrationFragment prodRegRegistrationFragment = new ProdRegRegistrationFragment();
                    prodRegRegistrationFragment.setArguments(dependencyBundle);
                    processControllerCallBacks.dismissLoadingDialog();
                    processControllerCallBacks.showFragment(prodRegRegistrationFragment);
                }
            }

            @Override
            public void onErrorResponse(final String errorMessage, final int responseCode) {
                final ProdRegRegistrationFragment prodRegRegistrationFragment = new ProdRegRegistrationFragment();
                prodRegRegistrationFragment.setArguments(dependencyBundle);
                processControllerCallBacks.dismissLoadingDialog();
                processControllerCallBacks.showFragment(prodRegRegistrationFragment);
            }
        };
    }

    protected boolean isCtnRegistered(final List<RegisteredProduct> registeredProducts, final Product product) {
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
}
