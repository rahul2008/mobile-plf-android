package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.localcache.ProdRegCache;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.tagging.AnalyticsConstants;
import com.philips.cdp.prodreg.tagging.ProdRegTagging;
import com.philips.cdp.prodreg.util.ProdRegUtil;
import com.philips.cdp.product_registration_lib.R;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegFirstLaunchFragment extends ProdRegBaseFragment {
    public static final String TAG = ProdRegFirstLaunchFragment.class.getName();
    private List<RegisteredProduct> registeredProducts;
    @Override
    public String getActionbarTitle() {
        return getActivity().getString(R.string.PPR_NavBar_Title);
    }

    @Override
    public List<RegisteredProduct> getRegisteredProducts() {
        return registeredProducts;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_first_launch, container, false);
        final Button registerButton = (Button) view.findViewById(R.id.yes_register_button);
        final Button registerLater = (Button) view.findViewById(R.id.no_thanks_button);
        registerButton.setOnClickListener(onClickRegister());
        registerLater.setOnClickListener(onClickNoThanks());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            registeredProducts = bundle.getParcelableArrayList(ProdRegConstants.MUL_PROD_REG_CONSTANT);
        }
    }

    @NonNull
    private View.OnClickListener onClickNoThanks() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                clearFragmentStack(true);
            }
        };
    }

    @NonNull
    private View.OnClickListener onClickRegister() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ProdRegProcessFragment processFragment = new ProdRegProcessFragment();
                processFragment.setArguments(getArguments());
                ProdRegTagging.getInstance().trackActionWithCommonGoals("ProdRegFirstLaunchScreen", "specialEvents", "productregistrationOptin");
                final ProdRegCache prodRegCache = new ProdRegCache(getActivity());
                ProdRegUtil.storeProdRegTaggingMeasuresCount(prodRegCache, AnalyticsConstants.Product_REGISTRATION_EXTENDED_WARRANTY_COUNT, 1);
                ProdRegTagging.getInstance().trackActionWithCommonGoals("ProdRegFirstLaunchScreen", "noOfExtendedWarrantyOptIns", String.valueOf(prodRegCache.getIntData(AnalyticsConstants.Product_REGISTRATION_EXTENDED_WARRANTY_COUNT)));
                showFragment(processFragment);
            }
        };
    }
}
