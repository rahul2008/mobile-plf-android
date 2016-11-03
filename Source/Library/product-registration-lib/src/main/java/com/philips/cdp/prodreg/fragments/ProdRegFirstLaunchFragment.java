/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.prodreg.activity.ProdRegBaseActivity;
import com.philips.cdp.prodreg.constants.AnalyticsConstants;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.constants.ProdRegError;
import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.cdp.prodreg.localcache.ProdRegCache;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.tagging.ProdRegTagging;
import com.philips.cdp.prodreg.util.ProdRegUtil;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.registration.User;

import java.util.List;

public class ProdRegFirstLaunchFragment extends ProdRegBaseFragment {
    public static final String TAG = ProdRegFirstLaunchFragment.class.getName();
    private List<RegisteredProduct> registeredProducts;
    private int resId = 0;
    private Bundle dependencies;
    private TextView titleTextView, subTitle1, subTitle2;
    private Button registerLater;

    @Override
    public int getActionbarTitleResId() {
        return R.string.PPR_NavBar_Title;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.PPR_NavBar_Title);
    }

    @Override
    public boolean getBackButtonState() {
        return false;
    }

    @Override
    public List<RegisteredProduct> getRegisteredProducts() {
        return registeredProducts;
    }

    private void setImageBackground() {
        if (getView() != null && resId != 0) {
            getView().setBackgroundResource(resId);
            registerLater.setBackgroundResource(R.drawable.uikit_white_transparent_selector);
            setWhiteTextColors();
        }
    }

    private void setWhiteTextColors() {
        int whiteColor = Color.WHITE;
        registerLater.setTextColor(whiteColor);
        titleTextView.setTextColor(whiteColor);
        subTitle1.setTextColor(whiteColor);
        subTitle2.setTextColor(whiteColor);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_first_launch, container, false);
        final Button registerButton = (Button) view.findViewById(R.id.yes_register_button);
        registerLater = (Button) view.findViewById(R.id.no_thanks_button);
        titleTextView = (TextView) view.findViewById(R.id.conf_action_textview);
        subTitle1 = (TextView) view.findViewById(R.id.conf_subtext_textview);
        subTitle2 = (TextView) view.findViewById(R.id.conf_subtext_two_textview);

        registerButton.setOnClickListener(onClickRegister());
        registerLater.setOnClickListener(onClickNoThanks());
        ProdRegTagging.getInstance().trackPage("ProductRegistrationOfferScreen", "trackPage", "ProductRegistrationOfferScreen");
        return view;
    }

    @SuppressWarnings("noinspection unchecked")
    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dependencies = getArguments();
        if (dependencies != null) {
            registeredProducts = (List<RegisteredProduct>) dependencies.getSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT);
            resId = dependencies.getInt(ProdRegConstants.PROD_REG_FIRST_IMAGE_ID);
            setImageBackground();
        }
    }

    @NonNull
    private View.OnClickListener onClickNoThanks() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                clearFragmentStack();
                handleCallBack(true);
                unRegisterProdRegListener();
            }
        };
    }

    @NonNull
    private View.OnClickListener onClickRegister() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final FragmentActivity activity = getActivity();
                final User user = new User(activity);
                if (user.isUserSignIn()) {
                    final ProdRegProcessFragment processFragment = new ProdRegProcessFragment();
                    processFragment.setArguments(dependencies);
                    ProdRegTagging.getInstance().trackAction("ProductRegistrationEvent", "specialEvents", "productregistrationOptin");
                    final ProdRegCache prodRegCache = new ProdRegCache();
                    new ProdRegUtil().storeProdRegTaggingMeasuresCount(prodRegCache, AnalyticsConstants.PRODUCT_REGISTRATION_EXTENDED_WARRANTY_COUNT, 1);
                    ProdRegTagging.getInstance().trackAction("ProductRegistrationEvent", "noOfExtendedWarrantyOptIns", String.valueOf(prodRegCache.getIntData(AnalyticsConstants.PRODUCT_REGISTRATION_EXTENDED_WARRANTY_COUNT)));
                    showFragment(processFragment);
                } else {
                    clearFragmentStack();
                    PRUiHelper.getInstance().getProdRegUiListener().onProdRegFailed(ProdRegError.USER_NOT_SIGNED_IN);
                    unRegisterProdRegListener();
                    if (activity != null && !activity.isFinishing() && activity instanceof ProdRegBaseActivity) {
                        activity.finish();
                    }
                }
            }
        };
    }
}
