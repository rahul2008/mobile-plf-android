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
import com.philips.cdp.prodreg.fragments.ProdRegRegistrationFragment;
import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.cdp.prodreg.listener.MetadataListener;
import com.philips.cdp.prodreg.listener.SummaryListener;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;
import com.philips.cdp.prodreg.model.summary.ProductSummaryResponse;

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
    private ArrayList<RegisteredProduct> registeredProducts;
    public ProdRegProcessController(final ProcessControllerCallBacks processControllerCallBacks, final FragmentActivity fragmentActivity) {
        this.processControllerCallBacks = processControllerCallBacks;
        this.fragmentActivity = fragmentActivity;
    }

    @SuppressWarnings("noinspection unchecked")
    public void process(final Bundle arguments) {
        if (arguments != null) {
            registeredProducts = (ArrayList<RegisteredProduct>) arguments.getSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT);
            dependencyBundle = arguments;
            if (registeredProducts != null && registeredProducts.size() > 0) {
                currentProduct = registeredProducts.get(0);
                if (!isApiCallingProgress) {
                    if (!TextUtils.isEmpty(currentProduct.getCtn())) {
                        currentProduct.getProductMetadata(fragmentActivity, getMetadataListener());
                    } else {
                        currentProduct.setProdRegError(ProdRegError.MISSING_CTN);
                        processControllerCallBacks.dismissLoadingDialog();
                        processControllerCallBacks.showAlertOnError(ProdRegError.MISSING_CTN.getCode());
                    }
                }
            } else {
                processControllerCallBacks.exitProductRegistration();
                PRUiHelper.getInstance().getProdRegUiListener().onProdRegFailed(ProdRegError.PRODUCTS_NOT_FOUND);
            }
        }
    }

    private void doSummaryRequest() {
        if (fragmentActivity != null && !fragmentActivity.isFinishing() && currentProduct != null) {
            dependencyBundle.putSerializable(ProdRegConstants.PROD_REG_PRODUCT, currentProduct);
            currentProduct.getProductSummary(fragmentActivity, currentProduct, getSummaryListener());
        }
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

    public boolean isLaunchedRegistration() {
        return launchedRegistration;
    }

    public void setLaunchedRegistration(final boolean launchedRegistration) {
        this.launchedRegistration = launchedRegistration;
    }

    public boolean isApiCallingProgress() {
        return isApiCallingProgress;
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
