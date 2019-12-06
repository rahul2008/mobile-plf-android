/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.philips.cdp.prodreg.constants.AnalyticsConstants;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.imagehandler.ImageRequestHandler;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.tagging.ProdRegTagging;
import com.philips.cdp.prodreg.util.ProdRegUtil;
import com.philips.cdp.product_registration_lib.R;

import java.util.ArrayList;
import java.util.List;

public class ProdRegSuccessFragment extends ProdRegBaseFragment {

    public static final String TAG = ProdRegSuccessFragment.class.getName();
    private ArrayList<RegisteredProduct> regProdList;
    private ImageView imageBackground;
    private String imgURL;
    private TextView prSuccessConfigurableTextView, prg_product_title, prg_product_description, prg_success_thanks_textView;
    private static final long serialVersionUID = -6635233525340545672L;


    @Override
    public int getActionbarTitleResId() {
        return R.string.PRG_NavBar_Title;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.PRG_NavBar_Title);
    }

    @Override
    public boolean getBackButtonState() {
        return true;
    }

    @Override
    public List<RegisteredProduct> getRegisteredProducts() {
        return regProdList;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_register_success, container, false);
        ProdRegTagging.trackPage(AnalyticsConstants.PRG_SUCCESS);
        Button button = view.findViewById(R.id.continueButton);
        imageBackground = view.findViewById(R.id.success_background_image);
        prSuccessConfigurableTextView = view.findViewById(R.id.prg_success_configurable_textView);
        prg_product_title = view.findViewById(R.id.prg_product_title);
        prg_product_description = view.findViewById(R.id.prg_product_description);
        prg_success_thanks_textView = view.findViewById(R.id.prg_success_thanks_textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                clearFragmentStack();
                handleCallBack(false);
                unRegisterProdRegListener();
            }
        });
        return view;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Bundle arguments = getArguments();
        if (arguments != null) {
            RegisteredProduct registeredProduct = (RegisteredProduct) arguments.getSerializable(ProdRegConstants.PROD_REG_PRODUCT);
            regProdList = (ArrayList<RegisteredProduct>) arguments.getSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT);
            imgURL = arguments.getString(ProdRegConstants.PROD_REG_FIRST_IMAGE_ID);
            prg_product_description.setText(registeredProduct.getCtn());
            prg_product_title.setText(arguments.getString(ProdRegConstants.PROD_REG_TITLE));
            setImageBackground();

            if (registeredProduct != null) {
                if (!registeredProduct.getEmail()) {
                    prSuccessConfigurableTextView.setVisibility(View.GONE);
                }
                ProdRegTagging.trackAction(AnalyticsConstants.SEND_DATA, AnalyticsConstants.PRODUCT_MODEL_KEY, registeredProduct.getCtn());

                ProdRegUtil prodRegUtil = new ProdRegUtil();
                String warantyPeriod = prodRegUtil.getDisplayDate(arguments.getString(ProdRegConstants.PROD_REG_WARRANTY));
                if (warantyPeriod.isEmpty() || warantyPeriod.equals("null")) {
                    prg_success_thanks_textView.setVisibility(View.GONE);
                } else {
                    prg_success_thanks_textView.setText(prodRegUtil.generateSpannableText
                            (getString(R.string.PRG_Extended_Warranty_Lbltxt), " "+warantyPeriod));
                    prg_success_thanks_textView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean handleBackEvent() {
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            final boolean fragmentStack = clearFragmentStack();
            handleCallBack(true);
            unRegisterProdRegListener();
            return fragmentStack;
        }
        return true;
    }

    private void setImageBackground() {
        if (imgURL != null && imgURL.length() > 0) {
            imageBackground.setVisibility(View.VISIBLE);
            ImageLoader
                    imageLoader = ImageRequestHandler.getInstance(getActivity().getApplicationContext()).getImageLoader();

            imageLoader.get(imgURL, ImageLoader.getImageListener(imageBackground,
                    R.drawable.product_placeholder, R.drawable.product_placeholder));
        }
    }
}
