package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.listener.SummaryListener;
import com.philips.cdp.prodreg.model.summary.ProductSummaryResponse;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.util.ImageRequester;
import com.philips.cdp.product_registration_lib.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegisterSingleProductFragment extends ProdRegBaseFragment {

    ImageLoader imageLoader;
    private TextView productTitle;
    private Button register;
    private ImageView productImageView;
    private RelativeLayout summaryLayout;
    private ProgressBar progressBar;

    @Override
    public String getActionbarTitle() {
        return null;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_single_product_summary, container, false);
        productTitle = (TextView) view.findViewById(R.id.product_title);
        imageLoader = ImageRequester.getInstance(getActivity()).getImageLoader();
        register = (Button) view.findViewById(R.id.btn_register);
        productImageView = (ImageView) view.findViewById(R.id.product_image);
        summaryLayout = (RelativeLayout) view.findViewById(R.id.summary_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Product product = new Product("RQ1250/17", Sector.B2C, Catalog.CONSUMER);
        product.getProductSummary(getActivity(), product, new SummaryListener() {
            @Override
            public void onSummaryResponse(final ProductSummaryResponse productSummaryResponse) {
                summaryLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                final String productTitle = productSummaryResponse.getData().getProductTitle();
                RegisterSingleProductFragment.this.productTitle.setText(productTitle != null ? productTitle : "");
                imageLoader.get(productSummaryResponse.getData().getImageURL(), ImageLoader.getImageListener(productImageView, R.drawable.ic_launcher, R.drawable.ic_launcher));
            }

            @Override
            public void onErrorResponse(final String errorMessage, final int responseCode) {

            }
        });
    }
}
