package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.prodreg.listener.SummaryListener;
import com.philips.cdp.prodreg.model.summary.ProductSummaryResponse;
import com.philips.cdp.prodreg.register.ProdRegHelper;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;
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
    private Product currentProduct;

    @Override
    public String getActionbarTitle() {
        return getActivity().getString(R.string.prodreg_actionbar_title);
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            currentProduct = (Product) bundle.getSerializable("product");
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ProdRegHelper prodRegHelper = new ProdRegHelper();
                final ProdRegListener listener = new ProdRegListener() {
                    @Override
                    public void onProdRegSuccess(RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                        Toast.makeText(getActivity(), "Product Registered Successfully", Toast.LENGTH_SHORT).show();
                        showAlert("Success", "Registration success", 0);
                    }

                    @Override
                    public void onProdRegFailed(RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                        Log.d(getClass() + "", "Negative Response Data : " + registeredProduct.getProdRegError().getDescription() + " with error code : " + registeredProduct.getProdRegError().getCode());
                        showAlert("Failed", registeredProduct.getProdRegError().toString(), 0);
//                        Toast.makeText(getActivity(), registeredProduct.getProdRegError().getDescription(), Toast.LENGTH_SHORT).show();
                    }
                };
                prodRegHelper.addProductRegistrationListener(listener);
                prodRegHelper.getSignedInUserWithProducts().registerProduct(currentProduct);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (currentProduct != null) {
            currentProduct.getProductSummary(getActivity(), currentProduct, new SummaryListener() {
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
                    progressBar.setVisibility(View.GONE);
                    showAlert("Summary", errorMessage, responseCode);
                }
            });
        } else progressBar.setVisibility(View.GONE);
    }
}
