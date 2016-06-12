package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.philips.cdp.prodreg.RegistrationState;
import com.philips.cdp.prodreg.error.ErrorHandler;
import com.philips.cdp.prodreg.listener.MetadataListener;
import com.philips.cdp.prodreg.listener.RegisteredProductsListener;
import com.philips.cdp.prodreg.listener.SummaryListener;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;
import com.philips.cdp.prodreg.model.summary.ProductSummaryResponse;
import com.philips.cdp.prodreg.register.ProdRegHelper;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.util.ProdRegConstants;
import com.philips.cdp.product_registration_lib.R;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegProcessFragment extends ProdRegBaseFragment {

    public static final String TAG = ProdRegProcessFragment.class.getName();
    private ProgressBar progressBar;
    private Product currentProduct;
    private Bundle dependencyBundle;

    @Override
    public String getActionbarTitle() {
        return getString(R.string.app_name);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_process, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.prodreg_progressBar);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            final Bundle bundle = getArguments();
            if (bundle != null) {
                currentProduct = (Product) bundle.getSerializable(ProdRegConstants.PROD_REG_PRODUCT);
            }
            getRegisteredProducts();
        }
    }

    private void makeSummaryRequest() {
        if (currentProduct != null) {
            dependencyBundle.putSerializable(ProdRegConstants.PROD_REG_PRODUCT, currentProduct);
            currentProduct.getProductSummary(getActivity(), currentProduct, getSummaryListener());
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
                    showFragment(prodRegRegistrationFragment);
                }
            }

            @Override
            public void onErrorResponse(final String errorMessage, final int responseCode) {
                showAlert("Summary Failed", new ErrorHandler().getError(responseCode).getDescription());
            }
        };
    }

    private void getRegisteredProducts() {
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            if (currentProduct != null) {
                ProdRegHelper prodRegHelper = ProdRegHelper.getInstance();
                prodRegHelper.getSignedInUserWithProducts().getRegisteredProducts(getRegisteredProductsListener());
            }
        }
    }

    @NonNull
    private RegisteredProductsListener getRegisteredProductsListener() {
        return new RegisteredProductsListener() {
            @Override
            public void getRegisteredProductsSuccess(final List<RegisteredProduct> registeredProducts, final long timeStamp) {
                if (registeredProducts.size() > 0) {
                    if (!isCtnRegistered(registeredProducts, currentProduct) && getActivity() != null && !getActivity().isFinishing()) {
                        currentProduct.getProductMetadata(getActivity(), getMetadataListener());
                    } else {
                        showFragment(new ProdRegConnectionFragment());
                    }
                }
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

    @NonNull
    private MetadataListener getMetadataListener() {
        return new MetadataListener() {
            @Override
            public void onMetadataResponse(final ProductMetadataResponse productMetadataResponse) {
                if (productMetadataResponse != null) {
                    dependencyBundle = new Bundle();
                    dependencyBundle.putSerializable(ProdRegConstants.PROD_REG_PRODUCT_METADATA, productMetadataResponse.getData());
                    makeSummaryRequest();
                }
            }

            @Override
            public void onErrorResponse(final String errorMessage, final int responseCode) {
                showAlert("Metadata Failed", new ErrorHandler().getError(responseCode).getDescription());
            }
        };
    }
}
