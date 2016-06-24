package com.philips.cdp.prodreg.fragments;

import android.app.Activity;
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

import java.lang.ref.WeakReference;
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
    private int count = 0;
    private ProdRegLoadingFragment prodRegLoadingFragment;
    private WeakReference<Activity> mActivityWeakRef;

    @Override
    public String getActionbarTitle() {
        return getActivity().getString(R.string.PPR_NavBar_Title);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_process, container, false);
        if (savedInstanceState == null) {
            showProgressAlertDialog(getActivity().getString(R.string.PPR_Looking_For_Products_Lbltxt));
        }
        return view;
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

    @Override
    public void onStart() {
        super.onStart();
        mActivityWeakRef = new WeakReference<Activity>(getActivity());
        final Bundle arguments = getArguments();
        if (mActivityWeakRef != null && arguments != null) {
            ArrayList<Product> regProdList = (ArrayList<Product>) arguments.getSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT);
            currentProduct = regProdList.get(0);
            User user = new User(getActivity());
            if (!user.isUserSignIn()) {
                count = count + 1;
                if (count < 2) {
                    RegistrationLaunchHelper.launchRegistrationActivityWithAccountSettings(getActivity());
                } else {
                    clearFragmentStack();
                }
            } else {
                getRegisteredProducts();
            }
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
                if (mActivityWeakRef != null && productSummaryResponse != null) {
                    dependencyBundle.putSerializable(ProdRegConstants.PROD_REG_PRODUCT_SUMMARY, productSummaryResponse.getData());
                    final ProdRegRegistrationFragment prodRegRegistrationFragment = new ProdRegRegistrationFragment();
                    prodRegRegistrationFragment.setArguments(dependencyBundle);
                    if (prodRegLoadingFragment != null) prodRegLoadingFragment.dismiss();
                    showFragment(prodRegRegistrationFragment);
                }
            }

            @Override
            public void onErrorResponse(final String errorMessage, final int responseCode) {
                if (mActivityWeakRef != null) {
                    final ProdRegRegistrationFragment prodRegRegistrationFragment = new ProdRegRegistrationFragment();
                    prodRegRegistrationFragment.setArguments(dependencyBundle);
                    if (prodRegLoadingFragment != null) prodRegLoadingFragment.dismiss();
                    showFragment(prodRegRegistrationFragment);
                }
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
                if (mActivityWeakRef != null) {
                    if (!isCtnRegistered(registeredProducts, currentProduct) && getActivity() != null && !getActivity().isFinishing()) {
                        currentProduct.getProductMetadata(getActivity(), getMetadataListener());
                    } else {
                        if (prodRegLoadingFragment != null) prodRegLoadingFragment.dismiss();
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
                if (mActivityWeakRef != null && productMetadataResponse != null) {
                    dependencyBundle = new Bundle();
                    dependencyBundle.putSerializable(ProdRegConstants.PROD_REG_PRODUCT_METADATA, productMetadataResponse.getData());
                    doSummaryRequest();
                }
            }

            @Override
            public void onErrorResponse(final String errorMessage, final int responseCode) {
                if (mActivityWeakRef != null && prodRegLoadingFragment != null)
                    prodRegLoadingFragment.dismiss();
                if (mActivityWeakRef != null) {
                    showAlertOnError(responseCode);
                }
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
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        if (prodRegLoadingFragment != null && prodRegLoadingFragment.isVisible()) {
            prodRegLoadingFragment.dismissAllowingStateLoss();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivityWeakRef = null;
    }
}
