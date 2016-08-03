/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.constants.ProdRegError;
import com.philips.cdp.prodreg.constants.RegistrationState;
import com.philips.cdp.prodreg.fragments.ProdRegConnectionFragment;
import com.philips.cdp.prodreg.fragments.ProdRegRegistrationFragment;
import com.philips.cdp.prodreg.listener.MetadataListener;
import com.philips.cdp.prodreg.listener.RegisteredProductsListener;
import com.philips.cdp.prodreg.listener.SummaryListener;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;
import com.philips.cdp.prodreg.model.summary.ProductSummaryResponse;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.settings.RegistrationHelper;
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

    private RegisteredProduct currentProduct;
    private ProcessControllerCallBacks processControllerCallBacks;
    private Bundle dependencyBundle;
    private boolean launchedRegistration = false;
    private boolean isApiCallingProgress = false;
    private FragmentActivity fragmentActivity;
    private User user;
    private ArrayList<RegisteredProduct> registeredProducts;

    public ProdRegProcessController(final ProcessControllerCallBacks processControllerCallBacks, final FragmentActivity fragmentActivity) {
        this.processControllerCallBacks = processControllerCallBacks;
        this.fragmentActivity = fragmentActivity;
        this.user = new User(fragmentActivity);
        dependencyBundle = new Bundle();
    }

    public void process(final Bundle arguments) {
        if (arguments != null) {
            registeredProducts = arguments.getParcelableArrayList(ProdRegConstants.MUL_PROD_REG_CONSTANT);
            dependencyBundle.putParcelableArrayList(ProdRegConstants.MUL_PROD_REG_CONSTANT, registeredProducts);
            if (registeredProducts != null) {
                currentProduct = registeredProducts.get(0);
                if (getUser().isUserSignIn()) {
                    //Signed in case
                    if (!isApiCallingProgress) {
                        getRemoteRegisteredProducts();
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
                        RegistrationHelper.getInstance().getAppTaggingInterface().setPreviousPage("demoapp:home");
                        RegistrationLaunchHelper.launchRegistrationActivityWithAccountSettings(fragmentActivity);
                    }
                }
            } else {
                processControllerCallBacks.exitProductRegistration();
            }
        }
    }

    private void getRemoteRegisteredProducts() {
        if (fragmentActivity != null && !fragmentActivity.isFinishing() && currentProduct != null) {
            ProdRegHelper prodRegHelper = getProdRegHelper();
            isApiCallingProgress = true;
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
            public void getRegisteredProducts(final List<RegisteredProduct> registeredProducts, final long timeStamp) {
                if (!isCtnRegistered(registeredProducts, currentProduct) && fragmentActivity != null && !fragmentActivity.isFinishing()) {
                    if (!TextUtils.isEmpty(currentProduct.getCtn()))
                        currentProduct.getProductMetadata(fragmentActivity, getMetadataListener());
                    else {
                        currentProduct.setProdRegError(ProdRegError.MISSING_CTN);
                        processControllerCallBacks.dismissLoadingDialog();
                        processControllerCallBacks.showAlertOnError(ProdRegError.MISSING_CTN.getCode());
                    }
                } else {
                    processControllerCallBacks.dismissLoadingDialog();
                    final ProdRegConnectionFragment connectionFragment = getConnectionFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(ProdRegConstants.MUL_PROD_REG_CONSTANT, ProdRegProcessController.this.registeredProducts);
                    connectionFragment.setArguments(bundle);
                    processControllerCallBacks.showFragment(connectionFragment);
                }
            }
        };
    }

    @NonNull
    protected ProdRegConnectionFragment getConnectionFragment() {
        return new ProdRegConnectionFragment();
    }

    @NonNull
    protected MetadataListener getMetadataListener() {
        return new MetadataListener() {
            @Override
            public void onMetadataResponse(final ProductMetadataResponse productMetadataResponse) {
                if (productMetadataResponse != null) {
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
            dependencyBundle.putParcelable(ProdRegConstants.PROD_REG_PRODUCT, currentProduct);
            currentProduct.getProductSummary(fragmentActivity, currentProduct, getSummaryListener());
        }
    }

    @NonNull
    protected SummaryListener getSummaryListener() {
        return new SummaryListener() {
            @Override
            public void onSummaryResponse(final ProductSummaryResponse productSummaryResponse) {
                if (productSummaryResponse != null) {
                    dependencyBundle.putSerializable(ProdRegConstants.PROD_REG_PRODUCT_SUMMARY, productSummaryResponse.getData());
                    final ProdRegRegistrationFragment prodRegRegistrationFragment = getProdRegRegistrationFragment();
                    prodRegRegistrationFragment.setArguments(dependencyBundle);
                    processControllerCallBacks.dismissLoadingDialog();
                    processControllerCallBacks.showFragment(prodRegRegistrationFragment);
                }
            }

            @Override
            public void onErrorResponse(final String errorMessage, final int responseCode) {
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

    private boolean isCtnRegistered(final List<RegisteredProduct> registeredProducts, final RegisteredProduct product) {
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

    public List<RegisteredProduct> getRegisteredProductsList() {
        updateRegisteredProductsList(currentProduct);
        return registeredProducts;
    }

    private void updateRegisteredProductsList(final RegisteredProduct registeredProduct) {
        if (registeredProducts != null) {
            registeredProducts.remove(registeredProduct);
            registeredProducts.add(registeredProduct);
        }
    }
}
