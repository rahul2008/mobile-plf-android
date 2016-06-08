package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponseData;
import com.philips.cdp.prodreg.model.summary.Data;
import com.philips.cdp.prodreg.register.ProdRegHelper;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;
import com.philips.cdp.prodreg.util.ImageRequestHandler;
import com.philips.cdp.prodreg.util.ProdRegConstants;
import com.philips.cdp.product_registration_lib.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegRegistrationFragment extends ProdRegBaseFragment {

    private ImageLoader imageLoader;
    private TextView productTitleTextView;
    private Button register;
    private ImageView productImageView;
    private ProductMetadataResponseData productMetadataResponseData;
    private Product currentProduct;

    @Override
    public String getActionbarTitle() {
        return getActivity().getString(R.string.prodreg_actionbar_title);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_single_product_summary, container, false);
        productTitleTextView = (TextView) view.findViewById(R.id.product_title);
        imageLoader = ImageRequestHandler.getInstance(getActivity()).getImageLoader();
        register = (Button) view.findViewById(R.id.btn_register);
        productImageView = (ImageView) view.findViewById(R.id.product_image);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ProdRegHelper prodRegHelper = new ProdRegHelper();
                final ProdRegListener listener = new ProdRegListener() {
                    @Override
                    public void onProdRegSuccess(RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                        Toast.makeText(getActivity(), "Product Registered Successfully", Toast.LENGTH_SHORT).show();
                        showAlert("Success", "Registration success");
                    }

                    @Override
                    public void onProdRegFailed(RegisteredProduct registeredProduct, UserWithProducts userWithProducts) {
                        Log.d(getClass() + "", "Negative Response Data : " + registeredProduct.getProdRegError().getDescription() + " with error code : " + registeredProduct.getProdRegError().getCode());
                        showAlert("Failed", registeredProduct.getProdRegError().toString());
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

        Bundle bundle = getArguments();
        Data summaryData = null;
        if (bundle != null) {
            currentProduct = (Product) bundle.getSerializable(ProdRegConstants.PROD_REG_PRODUCT);
            productMetadataResponseData = (ProductMetadataResponseData) bundle.getSerializable(ProdRegConstants.PROD_REG_PRODUCT_METADATA);
            summaryData = (Data) bundle.getSerializable(ProdRegConstants.PROD_REG_PRODUCT_SUMMARY);
        }

        if (summaryData != null) {
            final String productTitle = summaryData.getProductTitle();
            productTitleTextView.setText(productTitle != null ? productTitle : "");
            imageLoader.get(summaryData.getImageURL(), ImageLoader.getImageListener(productImageView, R.drawable.ic_launcher, R.drawable.ic_launcher));
        }
    }
}
