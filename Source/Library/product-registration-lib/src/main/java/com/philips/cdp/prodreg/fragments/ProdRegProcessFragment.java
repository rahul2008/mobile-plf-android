package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.prodreg.ProdRegConstants;
import com.philips.cdp.prodreg.RegistrationState;
import com.philips.cdp.prodreg.alert.ProdRegLoadingFragment;
import com.philips.cdp.prodreg.listener.DialogOkButtonListener;
import com.philips.cdp.prodreg.listener.MetadataListener;
import com.philips.cdp.prodreg.listener.RegisteredProductsListener;
import com.philips.cdp.prodreg.listener.SummaryListener;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;
import com.philips.cdp.prodreg.model.summary.ProductSummaryResponse;
import com.philips.cdp.prodreg.register.ProdRegHelper;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegProcessFragment extends ProdRegBaseFragment {

    public static final String TAG = ProdRegProcessFragment.class.getName();
    private Product currentProduct;
    private Bundle dependencyBundle;
    private ProdRegLoadingFragment prodRegLoadingFragment;
    private boolean launchedRegistration = false;
    private boolean isSavedInstanceState;

    @Override
    public String getActionbarTitle() {
        return getActivity().getString(R.string.PPR_NavBar_Title);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            showProgressAlertDialog(getActivity().getString(R.string.PPR_Looking_For_Products_Lbltxt));
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        final Bundle arguments = getArguments();
        if (arguments != null) {
            ArrayList<Product> regProdList = (ArrayList<Product>) arguments.getSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT);
            currentProduct = regProdList.get(0);
            User user = new User(getActivity());
            if (user.isUserSignIn()) {
                //Signed in case
                if (!isSavedInstanceState) {
                    getRegisteredProducts();
                }
            } else {
                //Not signed in
                if (launchedRegistration) {
                    //Registration page has already launched
                    if (prodRegLoadingFragment != null) prodRegLoadingFragment.dismiss();
                    clearFragmentStack();
                } else {
                    //Registration is not yet launched.
                    launchedRegistration = true;
                    RegistrationLaunchHelper.launchRegistrationActivityWithAccountSettings(getActivity());
                }
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean(ProdRegConstants.PROGRESS_STATE)) {
            isSavedInstanceState = true;
            showProgressAlertDialog(getActivity().getString(R.string.PPR_Looking_For_Products_Lbltxt));
        }
    }

    private void showProgressAlertDialog(final String description) {
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            prodRegLoadingFragment = new ProdRegLoadingFragment();
            prodRegLoadingFragment.setCancelable(false);
            prodRegLoadingFragment.show(activity.getSupportFragmentManager(), "dialog");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    prodRegLoadingFragment.setDescription(description);
                }
            }, 200);
        }
    }

    private void doSummaryRequest() {
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing() && currentProduct != null) {
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
                    if (prodRegLoadingFragment != null) prodRegLoadingFragment.dismiss();
                    showFragment(prodRegRegistrationFragment);
                }
            }

            @Override
            public void onErrorResponse(final String errorMessage, final int responseCode) {
                final ProdRegRegistrationFragment prodRegRegistrationFragment = new ProdRegRegistrationFragment();
                prodRegRegistrationFragment.setArguments(dependencyBundle);
                if (prodRegLoadingFragment != null) prodRegLoadingFragment.dismiss();
                showFragment(prodRegRegistrationFragment);
            }
        };
    }

    private void getRegisteredProducts() {
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            if (currentProduct != null) {
                ProdRegHelper prodRegHelper = new ProdRegHelper();
                prodRegHelper.getSignedInUserWithProducts().getRegisteredProducts(getRegisteredProductsListener());
            }
        }
    }

    @NonNull
    private RegisteredProductsListener getRegisteredProductsListener() {
        return new RegisteredProductsListener() {
            @Override
            public void getRegisteredProductsSuccess(final List<RegisteredProduct> registeredProducts, final long timeStamp) {
                if (!isCtnRegistered(registeredProducts, currentProduct) && getActivity() != null && !getActivity().isFinishing()) {
                    currentProduct.getProductMetadata(getActivity(), getMetadataListener());
                } else {
                    if (prodRegLoadingFragment != null) prodRegLoadingFragment.dismiss();
                    showFragment(new ProdRegConnectionFragment());
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
                    doSummaryRequest();
                }
            }

            @Override
            public void onErrorResponse(final String errorMessage, final int responseCode) {
                if (prodRegLoadingFragment != null)
                    prodRegLoadingFragment.dismiss();
                showAlertOnError(responseCode);
            }
        };
    }

    @Override
    public DialogOkButtonListener getDialogOkButtonListener() {
        return new DialogOkButtonListener() {
            @Override
            public void onOkButtonPressed() {
                final FragmentActivity activity = getActivity();
                if (activity != null && !activity.isFinishing()) {
                    clearFragmentStack();
                }
            }
        };
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        if (prodRegLoadingFragment != null && prodRegLoadingFragment.isVisible()) {
            outState.putBoolean(ProdRegConstants.PROGRESS_STATE, true);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
